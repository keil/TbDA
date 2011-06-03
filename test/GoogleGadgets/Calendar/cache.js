// Copyright (C) 2008 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Caching and storage of calendar data.
 */

/**
 * Class for caching and retrieving of calendar data.
 * @constructor
 */
function Cache() {
  this.events_ = {};
  this.calendars_ = [];
}

/**
 * Constant. One day in mili-seconds for event length check.
 */
Cache.prototype.ONE_DAY = 24 * 60 * 60 * 1000;

/**
 * Generate cache identifier based on calendar and start/end dates.
 * @param {Calendar} cal Calendar data
 * @param {Date} start Starting date
 * @return {string} Cache location identifier.
 */
Cache.prototype.createIdentifier = function(cal, start) {
  return cal.id + '/' + Utils.getDateIso8601(start);
};

/**
 * Add a new calendar to the internal calendar store.
 * @param {Calendar} cal Calendar object.
 */
Cache.prototype.addCalendar = function(cal) {
  // Adding new calendar to list
  this.calendars_.push(cal);
};

/**
 * Read the number of available calendars.
 * @return {int} Number of calendars
 */
Cache.prototype.getCalendarCount = function() {
  return this.calendars_.length;
};

/**
 * Get calendar by index.
 * @param {integer} idx Index of calendar.
 * @return {Calendar} calendar object
 */
Cache.prototype.getCalendar = function(idx) {
  if (idx < this.getCalendarCount() && idx >= 0) {
    return this.calendars_[idx];
  } else {
    return null;
  }
};

/**
 * Get calendar by string id.
 * @param {string} calId Calendar ID
 * @return {Calendar} calendar object
 */
Cache.prototype.getCalendarByID = function(calId) {
  for (var i = 0; i < this.calendars_.length; ++i) {
    var cal = this.calendars_[i];
    if (cal.id == calId) {
      return cal;
    }
  }
  return null;
};

/**
 * Clear all calendar entries from the local cache object.
 */
Cache.prototype.clearCalendarCache = function() {
  this.calendars_ = [];
};

/**
 * Clear all events from the local cache object.
 */
Cache.prototype.clearEventCache = function() {
  this.events_ = {};
};

/**
 * Get all events for all visible calendars on the specified date.
 * @param {Date} d Day to get events for.
 * @param {boolean} sorted Sort the events.
 * @return {Array} List of all events.
 */
Cache.prototype.getEventsForDay = function(d, sorted) {
  var out = [];
  for (var i = 0; i < this.calendars_.length; ++i) {
    if (!this.calendars_[i].isSelected()) {
      continue;
    }
    var loc = this.createIdentifier(this.calendars_[i], d);
    var cachedItems = this.events_[loc];
    if (cachedItems) {
      out = out.concat(cachedItems);
    } else if (typeof(cachedItems) == 'undefined') {
      var start = new Date(d.getFullYear(), d.getMonth(), 1);
      var end = new Date(d.getFullYear(), d.getMonth() + 1, 1);

      this.setEventsForCalendarRange(this.calendars_[i], start, end, []);
      g_events.getEventsFromServer(this.calendars_[i], start, end);
    }
  }

  if (sorted && out.length > 1) {
    out.sort(this.sortByDateFunc);
  }

  return out;
};

/**
 * Sort function to sort events by date. All day events always come first.
 * @param {CalendarEvent} a Event a
 * @param {CalendarEvent} b Event b
 * @return {integer} sorting order
 */
Cache.prototype.sortByDateFunc = function(a, b) {
  if (b.isAllDay != a.isAllDay) {
    return b.isAllDay ? 1 : -1;
  }

  if (Utils.sameStartTime(a.startTime, b.startTime)) {
    return b.title.toLowerCase() > a.title.toLowerCase() ? -1 : 1;
  }

  // Only sort timed events by start time
  if (!b.isAllDay) {
    var startDiff = a.startTime.getTime() - b.startTime.getTime();
    if (startDiff) {
      return startDiff;
    }
  }

  // Fall back on alphabetical
  return b.title.toLowerCase() > a.title.toLowerCase() ? -1 : 1;
};

/**
 * Add event to the specified calendar. This will initialize the event list
 * for this calendar if it doesn't have events for this day yet.
 * @param {Calendar} cal Calendar to add event to
 * @param {CalendarEvent} event Event data
 */
Cache.prototype.addEventToCalendar = function(cal, event) {
  event.calendarId = cal.id;
  if (!event.startTime || !event.endTime) {
    return;
  }

  if (!Utils.checkSameDay(event.startTime, event.endTime) && event.isAllDay) {
    var start = new Date(event.startTime);
    var end = new Date(event.endTime);
    for (var filldate = new Date(start);
         !Utils.checkSameDay(filldate, end);
         filldate.setDate(filldate.getDate() + 1)) {
      var loc = this.createIdentifier(cal, filldate);
      if (!this.events_[loc]) {
        this.events_[loc] = [];
      }
      this.events_[loc].push(event);
    }
  } else if (!Utils.checkSameDay(event.startTime, event.endTime) &&
             !event.isAllDay) {
    var timeDiff = event.endTime - event.startTime;
    if (timeDiff > this.ONE_DAY) {
      var start = new Date(event.startTime);
      var end = new Date(event.endTime);
      end.setDate(end.getDate() + 1);
      event.isAllDay = true;
      for (var filldate = new Date(start);
           !Utils.checkSameDay(filldate, end);
           filldate.setDate(filldate.getDate() + 1)) {
        var loc = this.createIdentifier(cal, filldate);
        if (!this.events_[loc]) {
          this.events_[loc] = [];
        }
        this.events_[loc].push(event);
      }
    } else {
      var loc = this.createIdentifier(cal, event.startTime);
      if (!this.events_[loc]) {
        this.events_[loc] = [];
      }
      this.events_[loc].push(event);
    }
  } else {
    var loc = this.createIdentifier(cal, event.startTime);
    if (!this.events_[loc]) {
      this.events_[loc] = [];
    }
    this.events_[loc].push(event);
  }
};

/**
 * Add update to the specified calendar. Will replace the existing event
 * if any is available or add the event as a new one.
 * for this calendar if it doesn't have events for this day yet.
 * @param {Calendar} cal Calendar to add event to
 * @param {CalendarEvent} event Event data
 */
Cache.prototype.addUpdateToCalendar = function(cal, event) {
  event.calendarId = cal.id;

  // Find and remove event if it has been deleted.
  if (event.status == event.STATUS_CANCELED) {
    var start = new Date(event.startTime);
    var end = new Date(event.endTime);
    end.setDate(end.getDate() + 1);
    for (var filldate = new Date(start);
         !Utils.checkSameDay(filldate, end);
         filldate.setDate(filldate.getDate() + 1)) {
      var loc = this.createIdentifier(cal, filldate);
      var events = this.events_[loc];
      if (!events) continue;
      for (var i = events.length - 1; i >= 0; --i) {
        if (events[i].id == event.id) {
          events.splice(i, 1);
        }
      }
    }
    return;
  }

  // Find and remove previous instances of the event if
  // it was moved to a new location.
  if (event.updated != event.published) {
    for (var loc in this.events_) {
      var events = this.events_[loc];
      for (var i = events.length - 1; i >= 0; --i) {
        if (events[i].id == event.id) {
          events.splice(i, 1);
        }
      }
    }
  }

  // Add event if added or moved
  this.addEventToCalendar(cal, event);
};

/**
 * Add events to the cache for a specified time period and calendar.
 * @param {Calendar} cal Calendar these events belong to.
 * @param {Date} start Start date of these events.
 * @param {Array} events Array of events for this time period and calendar.
 */
Cache.prototype.setEventsForCalendar = function(cal, start, events) {
  var loc = this.createIdentifier(cal, start);
  this.events_[loc] = events;
};

/**
 * Set events for a specific range for a calendar
 * @param {Calendar} cal Calendar
 * @param {Date} start Start date
 * @param {Date} end End date
 * @param {Array} events List of events
 */
Cache.prototype.setEventsForCalendarRange = function(cal, start, end, events) {
  for (var filldate = new Date(start);
       !Utils.checkSameDay(filldate, end);
       filldate.setDate(filldate.getDate() + 1)) {
    this.setEventsForCalendar(cal, filldate, events);
  }
};
