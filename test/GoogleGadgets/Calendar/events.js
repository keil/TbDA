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
 * @fileoverview Event storage for calendar gadget.
 */

/**
 * Class for storing and updating calendar events.
 * @constructor
 */
function Events() {
  // If we just started, show the last 8 hours worth of notifications
  var lastEightHours = new Date().getTime() - (8 * 60 * 60 * 1000);
  this.lastReminders = parseInt(options.getValue(OPTIONS.LAST_REMINDER));
  if (isNaN(this.lastReminders) || this.lastReminders < lastEightHours) {
    this.lastReminders = lastEightHours;
  }
  this.lastDate = new Date();
  this.lastRedraw = new Date();

  this.dataRetries = 0;
  this.nextRetry = 0;
  this.resetLastRetry();

  this.queueLength = 0;
}

// Constants for Events.

// Milliseconds interval for the timer.
Events.prototype.TIMER_INTERVAL = 5000;

// After how many minutes to check for calendar updates.
// Official calendar page uses 10 minutes.
Events.prototype.UPDATE_AGE_MIN = 15;

// Reminder age check.
Events.prototype.REMINDER_AGE = 58000;

// Maximum length in characters of the tooltip titles.
Events.prototype.TOOLTIP_LENGTH = 35;

// Retry request after this timeout in miliseconds.
Events.prototype.REQUEST_RETRY_MS = 5000;

/**
 * Callback function when events have been received from the server.
 */
Events.prototype.onEventsReceived = null;

/**
 * Callback function when calendars have been received from the server.
 */
Events.prototype.onCalendarsReceived = null;

Events.prototype.startTimer = function() {
  this.stopTimer();
  this.updateTimer = setInterval(Utils.bind(this.onTimer, this),
      this.TIMER_INTERVAL);
};

Events.prototype.stopTimer = function() {
  clearInterval(this.updateTimer);
};

/**
 * Get list of calendars for the user.
 * @param {string} opt_url Request URL. Needed for redirects.
 */
Events.prototype.getUserCalendars = function(opt_url) {
  debug.trace('Get user calendars.');

  if (!Utils.isOnline()) {
    g_calendarGadget.showErrorMsg(strings.OFFLINE);
    return;
  }

  if (g_auth.getAuthToken() == null) {
    debug.error('No AuthToken stored in auth object');
    g_auth.login();
    return;
  }

  if (!opt_url) {
    opt_url = CALENDAR_FEED_URL;
  }

  Utils.showLoading();

  var req = Utils.createXhr();
  req.open('GET', opt_url, true);
  req.onReadyStateChange = Utils.bind(this.onGetUserCalendars, this, req);
  req.setRequestHeader('Authorization',
      'GoogleLogin auth=' + g_auth.getAuthToken());
  // MS XHR does not send auth headers when it automaticaly follows a 302.
  // By setting this header, we instead get a 412 and can redirect manually.
  // See:
  // http://groups.google.com/group/google-calendar-help-dataapi/tree/browse_frm/month/2006-07/599f6774d43e5362
  req.setRequestHeader('X-If-No-Redirect', '1');
  req.setRequestHeader('If-Modified-Since', 'Sat, 1 Jan 2000 00:00:00 GMT');
  req.send();
};

/**
 * Callback function when receiving the list of user calendars
 * @param {XMLHttpRequest} req calling XMLHttpRequest object
 */
Events.prototype.onGetUserCalendars = function(req) {
  if (!req || req.readyState != 4) {  // completed OK?
    return;
  }

  if (req.status == 412) {
    // we've been redirected
    debug.trace('Redirecting user to new location');
    var location = req.getResponseHeader('X-Redirect-Location');
    this.getUserCalendars(location);
  } else if (req.status == 401) {
    g_auth.clearAuthToken();
    g_auth.login();
  } else if (req.status == 403) {
    g_calendarGadget.showErrorMsg(strings.ERROR_ACCOUNT_DISABLED_OR_DELETED);
    g_auth.clearAuthToken();
    g_calendarGadget.showLogin();
  } else if (req.status == 200) {
    options.putValue(OPTIONS.CALENDARDATA, req.responseText);
    var data = jsonParse(req.responseText);

    if (!data.feed || data.feed.length == 0) {
      // no <feed> element
      g_calendarGadget.showErrorMsg(ERROR_EMPTY_FEED);
      return;
    }

    if (typeof(this.onCalendarsReceived) == 'function') {
      this.onCalendarsReceived(data);
    }
  }
};

/**
 * Set the age of the calendar. Used for exponential backoff
 */
Events.prototype.setCalendarMinutes = function(cal, success) {
  if (success) {
    cal.calendarMinutes = this.UPDATE_AGE_MIN + Math.random();
  } else {
    var hourInMinutes = 120;
    if (cal.calendarMinutes > hourInMinutes) {
      cal.calendarMinutes += hourInMinutes;
    } else {
      cal.calendarMinutes *= 2;
    }
  }
};

/**
 * Get the color of the calendar.
 * @param {string} calId Calendar ID
 * @return {string} Hex color code
 */
Events.prototype.getCalendarColor = function(calId) {
  var cal = g_cache.getCalendarByID(calId);
  return cal.color;
};

/**
 * Get the text color of the calendar.
 * @param {string} calId Calendar ID
 * @return {string} Hex color code
 */
Events.prototype.getCalendarTextColor = function(calId) {
  //var cal = g_cache.getCalendarByID(calId);
  //debug.info('Seraching text color for: ' + cal.color);
  // TODO: Automatically detect best text color. Calendar always uses white
  //       but that looks bad on some light colors.
  return '#FFFFFF';
};

/**
 * Get all events for the time period for the provided calendar and store
 * the events for each day into the cache.
 * @param {Calendar} calendar Calendar to retrieve data from
 * @param {Date} startDate Start time of the timeframe
 * @param {Date} endDate End time of the timeframe
 */
Events.prototype.getEventsFromServer = function(calendar, startDate, endDate) {
  if (!Utils.isOnline()) {
    g_calendarGadget.showErrorMsg(strings.OFFLINE);
    return;
  }

  if (!calendar.isSelected()) {
    // We don't need events for a non-selected calendar
    //g_cache.setEventsForCalendarRange(calendar, startDate, endDate, []);
    return;
  }

  Utils.showLoading();

  var url = calendar.url +
      '?start-min=' + encodeURIComponent(Utils.getDateIso8601(startDate)) +
      '&start-max=' + encodeURIComponent(Utils.getDateIso8601(endDate)) +
      // This will give us single events for recurring events and an 
      // automatic purge for events which were moved out of recurring 
      // events.
      '&singleevents=true' +
      '&max-results=500';  // Should give us all events

  url = url.replace('http://', 'https://');

  ++this.queueLength;
  var req = Utils.createXhr();
  req.open('GET', url, true);
  req.setRequestHeader('Authorization',
      'GoogleLogin auth=' + g_auth.getAuthToken());
  req.onReadyStateChange = Utils.bind(this.onReceiveEvents, this, req,
      calendar, startDate, endDate);
  req.send();
};

Events.prototype.resetLastRetry = function() {
  this.dataRetries = 0;
  this.nextRetry = this.REQUEST_RETRY_MS + 1000 * Math.random();
};

Events.prototype.backoffLastRetry = function() {
  this.nextRetry *= 2;
};

/**
 * Callback function for event retrieval.
 * @param {XMLHttpRequest} req The calling XMLHttpRequest object
 * @param {Calendar} calendar The calendar we requested data for
 * @param {Date} startDate The starting date we requested
 * @param {Date} endDate The end date we reqeuested
 */
Events.prototype.onReceiveEvents = function(req, calendar, startDate, endDate) {
  if (!req || req.readyState != 4) {  // completed OK?
    return;
  }

  --this.queueLength;
  var status = req.status;

  if (status == 200) {
    this.setCalendarMinutes(calendar, true);
    this.resetLastRetry();

    for (var d = new Date(startDate);
         !Utils.checkSameDay(d, endDate);
         d.setDate(d.getDate() + 1)) {
      g_cache.setEventsForCalendar(calendar, d, []);
    }

    var doc = Utils.createXmlDocument(req);
    var feed = doc.getElementsByTagName('feed');
    if (!feed || feed.length == 0) {
      // no <feed> element
      // This feed doesn't have any events. 
      debug.error('No events in feed for ' + calendar.id);
      calendar.setUpdateDate();
      return;
    }

    var elem = doc.getElementsByTagName('entry');
    if (elem != null && elem.length > 0) {
      for (var i = 0; i < elem.length; ++i) {
        var event = new CalendarEvent();
        event.parse(elem[i]);
        if (event.updated > calendar.lastUpdate) {
          calendar.lastUpdate = event.updated;
          calendar.setUpdateDate();
        }
        g_cache.addEventToCalendar(calendar, event);
      }

      // Fire callback when any events were added.
      if (typeof(this.onEventsReceived) == 'function') {
        this.onEventsReceived();
      }
    } else {
      var updated = doc.getElementsByTagName('updated');
      var node = updated[0];
      calendar.lastUpdate =
          Utils.rfc3339StringToDate(node.firstChild.nodeValue);
      calendar.setUpdateDate();
      Utils.hideLoading();
    }
  } else if (this.dataRetries < 5 &&
        (status == 504 || status == 408 || status == 12029)) {
    ++this.dataRetries;
    setTimeout(Utils.bind(this.getEventsFromServer, this, calendar, startDate,
        endDate), this.nextRetry);
    this.backoffLastRetry();
  } else {
    Utils.hideLoading();
  }
};

/**
 * Internal timer function. This will initiate the checks for updates to the 
 * calendars and the check for reminders.
 */
Events.prototype.onTimer = function() {
  var now = new Date();
  var yesterday = new Date();
  yesterday.setDate(yesterday.getDate() - 1);

  if (Utils.checkSameDay(this.lastDate, yesterday) &&
    Utils.checkSameDay(g_uiAgenda.value, yesterday)) {
    this.lastDate = now;
    g_uiAgenda.goToday();
    g_uiCal.goToday();
    g_uiDayView.goToday();
  } else if (!Utils.checkSameDay(this.lastDate, now)) {
    this.lastDate = now;
    g_uiAgenda.draw();
    g_uiCal.draw();
    g_uiDayView.draw();
  }

  this.updateCheck(now);
  this.reminderCheck(now);
  this.autoRedraw(now);
};

/**
 * Redraw agenda every minute to correctly highlight currently active event
 * @param {Date} now Current time and date
 */
Events.prototype.autoRedraw = function(now) {
  var timeDiff = now - this.lastRedraw;
  if (timeDiff > 60 * 1000) {
    g_uiAgenda.draw();
    this.lastRedraw = now;
  }
};

/**
 * Check if any calendar needs updating. Launch update request if calendar 
 * becomes too old.
 * @param {Date} now Current time.
 * @param {boolean} opt_force Force update to calendar list.
 */
Events.prototype.updateCheck = function(now, opt_force) {
  for (var i = 0; i < g_cache.getCalendarCount(); ++i) {
    var cal = g_cache.getCalendar(i);
    if (!cal.isSelected()) continue;

    var timeDiff = now - cal.getUpdateDate();
    timeDiff = timeDiff / 1000 / 60;
    if (timeDiff > cal.calendarMinutes || opt_force) {
      this.setCalendarMinutes(cal, false);
      this.getUpdatesFromServer(cal);

      // Break after sending requests. We just want to check one calendar
      // at a time and balance bandwidth instead of checking all calendars
      // at the same time.
      if (!opt_force) break;
    }
  }
};

/**
 * Check for new reminders and display them
 * @param {Date} now Current date and time
 */
Events.prototype.reminderCheck = function(now) {
  var curTime = now.getTime();

  if (curTime - this.lastReminders < this.REMINDER_AGE) {
    return;
  }

  var start = new Date(now);
  start.setDate(start.getDate() - 1);
  var end = new Date(now);
  end.setDate(end.getDate() + 1);

  // Retrieve all events for days we might need to check reminders for
  var eventlist = [];
  for (var curdate = new Date(start);
       !Utils.checkSameDay(curdate, end);
       curdate.setDate(curdate.getDate() + 1)) {
    eventlist = eventlist.concat(this.getEventsForDay(curdate, false));
  }

  for (var i = 0; i < eventlist.length; ++i) {
    e = eventlist[i];
    // Check if reminder is set
    if (e.reminder == -1) continue;

    var remindTime = e.startTime.getTime();
    var remindBeforeTime = e.reminder * 60 * 1000;
    remindTime -= remindBeforeTime;
    if (remindTime > this.lastReminders && remindTime <= curTime) {
      var item = new ContentItem();
      item.layout = gddContentItemLayoutEmail;
      item.heading = e.title;
      item.source = Utils.formatEventTimes(e);
      item.snippet = e.desc;
      item.onOpenItem = Utils.bind(g_uiAgenda.onEventClicked, g_uiAgenda, e);
      item.open_command = e.url;
      try {
        plugin.addContentItem(item, gddItemDisplayAsNotification);
      } catch (e) {
        debug.error('Cannot show notification in designer\n' + e.message);
      }
    }
  }

  this.lastReminders = curTime;
  options.putValue(OPTIONS.LAST_REMINDER, this.lastReminders);
};

/**
 * Get all updated events for the time period for the provided calendar and 
 * update the events for each day in the cache.
 * @param {Calendar} calendar Calendar to retrieve data from
 */
Events.prototype.getUpdatesFromServer = function(calendar) {
  if (!Utils.isOnline()) {
    g_calendarGadget.showErrorMsg(strings.OFFLINE);
    return;
  }

  if (Utils.checkSameDay(new Date(0), calendar.lastUpdate)) {
    return;
  }

  Utils.showLoading();

  // Add one seconds since the lower bound is inclusive.
  calendar.lastUpdate.setSeconds(calendar.lastUpdate.getSeconds() + 1);

  var url = calendar.url +
      '?updated-min=' +
      encodeURIComponent(Utils.getDateIso8601(calendar.lastUpdate, true)) +
      // This will give us single events for recurring events and an
      // automatic purge for events which were moved out of recurring
      // events.
      '&singleevents=true' +
      '&max-results=500';  // Should give us all events

  var req = Utils.createXhr();
  req.open('GET', url, true);
  req.setRequestHeader('Authorization',
      'GoogleLogin auth=' + g_auth.getAuthToken());
  req.onReadyStateChange = Utils.bind(this.onReceiveUpdates, this, req,
      calendar);
  req.send();
  ++this.queueLength;
};

/**
 * Callback function for updates retrieval.
 * @param {XMLHttpRequest} req The calling XMLHttpRequest object
 * @param {Calendar} calendar The calendar we requested data for
 */
Events.prototype.onReceiveUpdates = function(req, calendar) {
  if (!req || req.readyState != 4) {  // completed OK?
    return;
  }

  --this.queueLength;

  if (req.status == 200) {
    this.setCalendarMinutes(calendar, true);

    var doc = Utils.createXmlDocument(req);
    var feed = doc.getElementsByTagName('feed');
    if (!feed || feed.length == 0) {
      // no <feed> element
      // This feed doesn't have any events. 
      debug.error('No feed in response for ' + calendar.id);
      return;
    }

    var elem = doc.getElementsByTagName('entry');
    if (elem != null && elem.length > 0) {
      for (var i = 0; i < elem.length; ++i) {
        var event = new CalendarEvent();
        event.parse(elem[i]);
        if (event.updated > calendar.lastUpdate) {
          calendar.lastUpdate = event.updated;
          calendar.setUpdateDate();
        }
        g_cache.addUpdateToCalendar(calendar, event);
      }

      // Call onEventsReceived when new or changed events were available.
      if (typeof(this.onEventsReceived) == 'function') {
        this.onEventsReceived();
      }
    } else {
      Utils.hideLoading();
      var updated = doc.getElementsByTagName('updated');
      var node = updated[0];
      calendar.lastUpdate =
          Utils.rfc3339StringToDate(node.firstChild.nodeValue);
      calendar.setUpdateDate();
    }
  } else {
    Utils.hideLoading();
  }
};

/**
 * Check if there are any events for the given date.
 * @param {Date} date Date to check
 * @return {boolean} True, if events are present for this day.
 */
Events.prototype.isBusy = function(date) {
  // Just totally random for now. Until there is data in the Event storage
  var events = this.getEventsForDay(date);
  //debug.trace('Day has ' + events.length + ' events!');
  return events.length > 0;
};

/**
 * Get a list of all events for a certain date. 
 * @param {Date} date Day to get events for.
 * @param {boolean} sorted Sort the events.
 * @return {Array} List of all events.
 */
Events.prototype.getEventsForDay = function(date, sorted) {
  return g_cache.getEventsForDay(date, sorted);
};

/**
 * Create a tooltip text for the given date.
 * @param {Date} date Day to generate tooltip for.
 * @return {string} Tooltip for this day.
 */
Events.prototype.getToolTip = function(date) {
  var tooltips = [];
  var events = this.getEventsForDay(date, true);
  for (var e = 0; e < events.length; ++e) {
    var title = events[e].title;
    if (title.length > this.TOOLTIP_LENGTH) {
      title = title.substr(0, this.TOOLTIP_LENGTH - 1) + '…';
    }
    var tip = Utils.formatTime(events[e].startTime) + '\t' + title;
    tooltips.push(tip);
  }
  return tooltips.join('\n');
};

/**
 * Create and add a new event to the calendar.
 * @param {CalendarEvent} event Event data for this new event.
 */
Events.prototype.addNewEvent = function(event) {
  var addEntry = '<entry xmlns="http://www.w3.org/2005/Atom" ';
  addEntry += ' xmlns:gd="http://schemas.google.com/g/2005">\n';
  addEntry += '<category scheme="http://schemas.google.com/g/2005#kind" ';
  addEntry += ' term="http://schemas.google.com/g/2005#event"></category>\n';
  addEntry += '<title type="text">' + Utils.cleanXml(event.title) +
      '</title>\n';
  addEntry += '<content type="text">' + Utils.cleanXml(event.desc) +
      '</content>\n';
  addEntry += '<gd:transparency ';
  addEntry += 'value="http://schemas.google.com/g/2005#event.opaque">';
  addEntry += '</gd:transparency>\n';
  addEntry += '<gd:eventStatus ';
  addEntry += ' value="http://schemas.google.com/g/2005#event.confirmed">';
  addEntry += '</gd:eventStatus>\n';
  if (event.location) {
    addEntry += '<gd:where valueString="' + Utils.cleanXml(event.location) +
        '"></gd:where>\n';
  }
  addEntry += '<gd:when ';
  var allDay = event.isAllDay;
  addEntry += 'startTime="' + Utils.getDateIso8601(event.startTime, !allDay) +
      '" ';
  addEntry += 'endTime="' + Utils.getDateIso8601(event.endTime, !allDay) +
      '">';
  addEntry += '</gd:when>\n';
  addEntry += '</entry>';

  var cal = g_cache.getCalendarByID(event.calendarId);
  debug.info('Adding to calendar ' + cal.getTitle());

  var req = Utils.createXhr();
  req.open('POST', cal.url, true);
  req.onReadyStateChange = Utils.bind(this.onAddDone, this, req);
  req.setRequestHeader('Authorization',
      'GoogleLogin auth=' + g_auth.getAuthToken());
  req.setRequestHeader('Content-Type', 'application/atom+xml');

  req.send(addEntry);
};

/**
 * Quick Add new event to primary calendar
 * @param {string} text user input
 */
Events.prototype.addQuickEvent = function(text) {
  var quickAddEntry = '<entry xmlns="http://www.w3.org/2005/Atom" ';
  quickAddEntry += 'xmlns:gCal="http://schemas.google.com/gCal/2005">\n';
  quickAddEntry += '<content type="html">' + Utils.cleanXml(text) +
      '</content>\n';
  quickAddEntry += '<gCal:quickadd value="true"/>\n';
  quickAddEntry += '</entry>';

  var req = Utils.createXhr();
  req.open('POST', CALENDAR_POST_URL, true);
  req.onReadyStateChange = Utils.bind(this.onAddDone, this, req);
  req.setRequestHeader('Authorization',
      'GoogleLogin auth=' + g_auth.getAuthToken());
  req.setRequestHeader('Content-Type', 'application/atom+xml');

  req.send(quickAddEntry);
};

/**
 * Callback for quick add
 * @param {XMLHttpRequest} req Calling XHR object
 */
Events.prototype.onAddDone = function(req) {
  if (!req || req.readyState != 4) {  // completed OK?
    return;
  }

  if (req.status == 201) {
    if (typeof(this.onEventsReceived) == 'function') {
      this.onEventsReceived();
    }
    this.updateCheck(new Date(), true);
  } else {
    g_errorMessage.displayMessage(strings.ERROR_CREATE_EVENT);
  }
};

