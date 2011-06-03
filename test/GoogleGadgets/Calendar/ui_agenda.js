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
 * @fileoverview Agenda ui.
 */

/**
 * Class for drawing and interaction with the agenda.
 * @param {Element} target DIV container the agenda should be rendered in.
 * @constructor
 */
function Agenda(target) {
  this.target_ = target;

  this.value = new Date();
  this.renderTimeOut_ = null;
  this.lastEvent = null;
}

/**
 * Constants for visual elements.
 */
Agenda.prototype.NAVI_HEIGHT = 18;
Agenda.prototype.HEADER_HEIGHT = 22;  // height of the header in the agenda.
Agenda.prototype.ENTRY_HEIGHT = 16;
Agenda.prototype.BORDER = 3;
Agenda.prototype.TEN_MINUTES_MS = 60 * 1000 * 10;
Agenda.prototype.COLORS = {
  MORE : '#000000',
  NO_EVENTS : '#909090',
  TIME : '#6D6D6D',
  TODAY_BG: '#FFFFCC'
};

/**
 * Callback function which is triggered when the user changes the day.
 * @type {function}
 */
Agenda.prototype.onDateSelected = null;

/**
 * Set the date which should be displayed.
 * @param {Date} d Selected date
 */
Agenda.prototype.setDate = function(d) {
  if (!Utils.checkSameDay(this.value, d)) {
    this.value = new Date(d);
    // Trigger callback function if any is assigned.
    if (typeof(this.onDateSelected) == 'function') {
      this.onDateSelected();
    }
    this.draw();
  }
};

/**
 * Jump to next day in agenda view
 */
Agenda.prototype.nextDay = function() {
  var d = new Date(this.value);
  d.setDate(d.getDate() + 1);
  this.setDate(d);
};

/**
 * Jump to previous day in agenda view
 */
Agenda.prototype.prevDay = function() {
  var d = new Date(this.value);
  d.setDate(d.getDate() - 1);
  this.setDate(d);
};

/**
 * Jump the dayview to today
 */
Agenda.prototype.goToday = function() {
  //debug.trace('Agenda: Jumping to today');
  this.setDate(new Date());
};

/**
 * Draw the agenda into the target div.
 */
Agenda.prototype.draw = function() {
  if (!this.target_.visible) return;

  this.target_.removeAllElements();

  this.drawNavigation();

  var d = new Date(this.value);
  while (this.hasEnoughSpace()) {
    this.addHeader(d);
    this.addEvents(d);
    d.setDate(d.getDate() + 1);
  }

  // If we are currently rendering "today" then we want to fill the
  // remaining space with the background color of the today
  // divs.
  var now = new Date();
  d.setDate(d.getDate() - 1);
  if (Utils.checkSameDay(now, d)) {
    var itemDiv = this.target_.appendElement('<div name="itemDiv" />');
    itemDiv.x = 0;
    itemDiv.y = this.getNextItemY();
    itemDiv.height = this.target_.height - itemDiv.y;
    itemDiv.width = this.target_.width;
    itemDiv.background = this.COLORS.TODAY_BG;
  }
};

/**
 * Get the y coordinate for the next agenda item based on the last agenda
 * item.
 * @return {integer} Y position of next item.
 */
Agenda.prototype.getNextItemY = function() {
  var res = 0;
  for (var i = 0; i < this.target_.children.count; ++i) {
    var e = this.target_.children.item(i);
    if (typeof(e.height) != 'undefined') {
      res += e.height;
    }
  }
  return res;
};

/**
 * Check if agenda div has enough space for one more day.
 * @return {boolean} True, if enought space is available.
 */
Agenda.prototype.hasEnoughSpace = function() {
  var total = this.target_.height;
  total -= this.getNextItemY();
  return total > (this.HEADER_HEIGHT + this.ENTRY_HEIGHT);
};

/**
 * Detect how many items fit into the rest of the agenda view.
 * @return {integer} Available space in pixels
 */
Agenda.prototype.maxItemSpace = function() {
  var rest = this.target_.height - this.getNextItemY();
  return Math.floor(rest / this.ENTRY_HEIGHT);
};

/**
 * Draw navigation header if we are currently in agenda view mode
 */
Agenda.prototype.drawNavigation = function() {
  // Check if we are in agenda view mode
  if (options.getValue(OPTIONS.VIEW) != OPTIONS.AGENDAVIEW) {
    return;
  }
  var headerDiv = this.target_.appendElement('<div />');
  headerDiv.x = 0;
  headerDiv.y = 0;
  headerDiv.width = this.target_.width;
  headerDiv.height = this.NAVI_HEIGHT;

  var headerLeft = headerDiv.appendElement('<img />');
  headerLeft.src = 'images/dayview_header_left.png';
  headerLeft.x = 0;
  headerLeft.y = 0;
  headerLeft.width = headerLeft.srcWidth;
  headerLeft.height = headerLeft.srcHeight;

  var headerRight = headerDiv.appendElement('<img />');
  headerRight.src = 'images/dayview_header_right.png';
  headerRight.x = headerDiv.width - headerRight.srcWidth;
  headerRight.y = 0;
  headerRight.width = headerRight.srcWidth;
  headerRight.height = headerRight.srcHeight;

  // Design background for header.
  var bgImg = headerDiv.appendElement('<img />');
  bgImg.src = 'images/month_nav_bg.png';
  bgImg.x = headerLeft.width;
  bgImg.y = 0;
  bgImg.height = bgImg.srcHeight;
  bgImg.width = this.target_.width - headerLeft.width - headerRight.width;

  // Navigation arrows
  var naviUp = headerDiv.appendElement('<img />');
  naviUp.src = 'images/agenda_nav_up.png';
  naviUp.y = 2 * this.BORDER;
  naviUp.x = this.BORDER;
  naviUp.enabled = true;
  naviUp.cursor = 'hand';
  naviUp.onclick = Utils.bind(this.prevDay, this);
  naviUp.ondblclick = Utils.bind(this.prevDay, this);
  var naviDown = headerDiv.appendElement('<img />');
  naviDown.src = 'images/agenda_nav_down.png';
  naviDown.y = 2 * this.BORDER;
  naviDown.x = naviUp.x + naviUp.srcWidth - this.BORDER;
  naviDown.enabled = true;
  naviDown.cursor = 'hand';
  naviDown.onclick = Utils.bind(this.nextDay, this);
  naviDown.ondblclick = Utils.bind(this.nextDay, this);

  // Label displaying current month and year.
  var agendaLabel = headerDiv.appendElement('<label />');
  agendaLabel.x = naviDown.x + naviDown.srcWidth + this.BORDER;
  agendaLabel.y = 0;
  agendaLabel.width = headerDiv.width - agendaLabel.x - this.BORDER;
  agendaLabel.height = this.NAVI_HEIGHT;
  agendaLabel.bold = true;
  agendaLabel.align = 'center';
  agendaLabel.valign = 'middle';
  agendaLabel.size = 9;
  agendaLabel.font = 'Arial';
  agendaLabel.enabled = true;
  agendaLabel.innerText = strings.AGENDA;
};

/**
 * Add header for specified date to agenda list.
 * @param {Date} date Add header for this date.
 */
Agenda.prototype.addHeader = function(date) {
  var today = new Date();
  var tomorrow = new Date();
  tomorrow.setDate(today.getDate() + 1);

  var headerDiv = this.target_.appendElement('<div name="headerDiv" />');
  headerDiv.background = 'images/agenda_header.png';
  headerDiv.x = 0;
  headerDiv.y = this.getNextItemY();
  headerDiv.height = this.HEADER_HEIGHT;
  headerDiv.width = this.target_.width;

  var label = headerDiv.appendElement('<label />');
  label.x = this.BORDER;
  label.y = 0;
  label.height = '100%';
  label.width = '100%';
  label.valign = 'middle';
  label.bold = true;

  if (Utils.checkSameDay(date, today) ||
      Utils.checkSameDay(date, tomorrow)) {
    var lblRight = headerDiv.appendElement('<label />');
    lblRight.x = this.BORDER;
    lblRight.y = 0;
    lblRight.width = headerDiv.width - (2 * this.BORDER);
    lblRight.height = '100%';
    lblRight.valign = 'middle';
    lblRight.align = 'right';
    var caption = strings.AGENDA_HEADER;
    caption = caption.replace('[![WEEKDAY]!]', WEEK_DAYS_MEDIUM[date.getDay()]);
    caption = caption.replace('[![MONTH]!]', MONTHS_SHORT[date.getMonth()]);
    caption = caption.replace('[![DAY]!]', date.getDate());
    lblRight.innerText = caption;

    if (Utils.checkSameDay(date, today)) {
      label.innerText = strings.TODAY;
    } else if (Utils.checkSameDay(date, tomorrow)) {
      label.innerText = strings.TOMORROW;
    }
  } else {
    var caption = strings.AGENDA_HEADER;
    caption = caption.replace('[![WEEKDAY]!]', WEEK_DAYS_MEDIUM[date.getDay()]);
    caption = caption.replace('[![MONTH]!]', MONTHS[date.getMonth()]);
    caption = caption.replace('[![DAY]!]', date.getDate());
    label.innerText = caption;
  }
};

/**
 * Add events for the specified date to the agenda list.
 * @param {Date} date Render events for this date.
 */
Agenda.prototype.addEvents = function(date) {
  var events = g_events.getEventsForDay(date, true);
  // debug.trace('I have ' + events.length + ' events for this day.');

  if (events.length == 0) {
    this.addEventEmpty(date);
  } else {
    var maxItems = this.maxItemSpace();
    if (events.length == 1) {
      // If we just have one item we can show it since we don't add the 
      // header if we don't have space for at least one item.
      if (events[0].isAllDay) {
        this.addEventFullDay(events[0], date);
      } else {
        this.addEvent(events[0], date, false);
      }
    } else if (maxItems == 1) {
      // If we have more than one item but can only display one we tell
      // the user how many events this day has.
      var caption = strings.TOTAL_EVENTS.replace('[![COUNT]!]', events.length);
      this.addEventText(caption, strings.MORE_EVENTS_TOOLTIP, date);
    } else {
      // We have more than 1 item in our events. Check how many items can be 
      // displayed. If we need to add '+ x more' we reserve one spot for our
      // '+ x more' item.
      var displayCount = events.length;
      if (events.length > maxItems) {
        displayCount = maxItems - 1;
      }

      // Add all items which fit into the agenda view
      var lastStart = null;
      var sameStart = false;
      for (var i = 0; i < displayCount; ++i) {
        if (events[i].isAllDay) {
          this.addEventFullDay(events[i], date);
        } else {
          sameStart = lastStart &&
              Utils.sameStartTime(events[i].startTime, lastStart);
          this.addEvent(events[i], date, sameStart);
          lastStart = events[i].startTime;
        }
      }

      // Add our '+ x more' item if we didn't have enough space for all items.
      if (displayCount != events.length) {
        var count = events.length - displayCount;
        this.addEventText(strings.MORE_EVENTS.replace('[![COUNT]!]', count),
          strings.MORE_EVENTS_TOOLTIP, date);
      }
    }
  }
};

/**
 * Add main div for this entry. This is always the same container
 * for all events and has the attached functions for user interaction.
 * @param {Event} event Event data
 * @return {Element} Div container for event
 */
Agenda.prototype.addEventDiv = function(event) {
  var itemDiv = this.target_.appendElement('<div name="itemDiv" />');
  itemDiv.x = 0;
  itemDiv.y = this.getNextItemY();
  itemDiv.height = this.ENTRY_HEIGHT;
  itemDiv.width = this.target_.width;
  if (event && event.title) {
    itemDiv.enabled = true;
    itemDiv.cursor = 'hand';
    itemDiv.onclick = Utils.bind(this.onEventClicked, this, event);
  }
  var now = new Date();
  if (event && Utils.checkSameDay(now, event.startTime)) {
    itemDiv.background = this.COLORS.TODAY_BG;
  }
  return itemDiv;
};

/**
 * Create empty entry if the day has no events.
 */
Agenda.prototype.addEventEmpty = function(date) {
  //debug.info('No events for the selected date!');
  var event = {'startTime': date};
  var itemDiv = this.addEventDiv(event);
  var itemLbl = itemDiv.appendElement('<label />');
  itemLbl.x = this.BORDER;
  itemLbl.y = 0;
  itemLbl.height = '100%';
  itemLbl.width = '100%';
  itemLbl.innerText = strings.NO_EVENTS;
  itemLbl.valign = 'middle';
  itemLbl.color = this.COLORS.NO_EVENTS;
};

/**
 * Create a new text entry to display some text to the user
 * @param {String} msg Message to show in the agenda.
 * @param {Date} date Date of this entry
 */
Agenda.prototype.addEventText = function(msg, tooltip, date) {
  var event = {'startTime': date};
  var itemDiv = this.addEventDiv(event);
  var itemLbl = itemDiv.appendElement('<label />');
  itemLbl.x = this.BORDER;
  itemLbl.y = 0;
  itemLbl.height = '100%';
  itemLbl.width = '100%';
  itemLbl.innerText = msg;
  itemLbl.tooltip = msg + '\n' + tooltip;
  itemLbl.valign = 'middle';
  itemLbl.bold = true;
  itemLbl.color = this.COLORS.MORE;
};

/**
 * Create full day event entry.
 * @param {Event} event Event data
 * @param {Date} date The date we are drawing
 */
Agenda.prototype.addEventFullDay = function(event, date) {
  var itemDiv = this.addEventDiv(event);
  var fullDayBG = itemDiv.appendElement('<div />');
  fullDayBG.x = this.BORDER;
  fullDayBG.y = 1;
  fullDayBG.height = itemDiv.height - 2;
  fullDayBG.width = itemDiv.width - (2 * this.BORDER);
  fullDayBG.background = g_events.getCalendarColor(event.calendarId);
  var itemLbl = fullDayBG.appendElement('<label />');
  itemLbl.x = this.BORDER;
  itemLbl.y = 0;
  itemLbl.height = 16;
  itemLbl.width = fullDayBG.width - (2 * this.BORDER);
  itemLbl.tooltip = this.eventTooltip(event);
  itemLbl.valign = 'middle';
  itemLbl.size = 8;
  itemLbl.trimming = 'character-ellipsis';
  itemLbl.color = g_events.getCalendarTextColor(event.calendarId);
  // If this event has time information set, then we have an event 
  // which spawns several days and should be rendered as allDay event.
  if (event.startTime.getTime() % 1000 != 0) {
    title = strings.SPAWN_DAYS;
    title = title.replace('[![TIME]!]',
                          Utils.formatTime(event.startTime, true));
    title = title.replace('[![TITLE]!]', event.title);
    itemLbl.innerText = title;

    if (!Utils.checkSameDay(date, event.startTime)) {
      var prevImg = itemDiv.appendElement('<img />');
      prevImg.src = 'images/agenda_fullday_prev.png';
      prevImg.x = this.BORDER;
      prevImg.y = 2;
      itemLbl.x = 2 * this.BORDER;
      itemLbl.width = itemLbl.width - prevImg.srcWidth - this.BORDER;
    }

    if (!Utils.checkSameDay(date, event.endTime)) {
      var nextImg = itemDiv.appendElement('<img />');
      nextImg.src = 'images/agenda_fullday_next.png';
      nextImg.x = itemDiv.width - this.BORDER - nextImg.srcWidth;
      nextImg.y = 2;
      itemLbl.width = itemLbl.width - nextImg.srcWidth - this.BORDER;
    }
  }
  else {
    itemLbl.innerText = event.title;

    if (!Utils.checkSameDay(date, event.startTime)) {
      var prevImg = itemDiv.appendElement('<img />');
      prevImg.src = 'images/agenda_fullday_prev.png';
      prevImg.x = this.BORDER;
      prevImg.y = 2;
      itemLbl.x = 2 * this.BORDER;
      itemLbl.width = itemLbl.width - prevImg.srcWidth - this.BORDER;
    }

    // Full day events have the next day as their end date. We need to change
    // this so we can correctly check if we need a next arrow.
    var end = new Date(event.endTime);
    end.setDate(end.getDate() - 1);

    if (!Utils.checkSameDay(date, end)) {
      var nextImg = itemDiv.appendElement('<img />');
      nextImg.src = 'images/agenda_fullday_next.png';
      nextImg.x = itemDiv.width - this.BORDER - nextImg.srcWidth;
      nextImg.y = 2;
      itemLbl.width = itemLbl.width - nextImg.srcWidth - this.BORDER;
    }
  }
};

/**
 * Create event entry for an event.
 * @param {Event} event Event data
 * @param {Date} date The date we are drawing
 * @param {boolean} opt_same If true, time will be omitted.
 */
Agenda.prototype.addEvent = function(event, date, opt_same) {
  var now = new Date();
  var itemDiv = this.addEventDiv(event);
  if (!opt_same) {
    var timeLbl = itemDiv.appendElement('<label />');
    timeLbl.x = this.BORDER;
    timeLbl.y = 0;
    timeLbl.height = '100%';
    timeLbl.width = 35;
    timeLbl.innerText = Utils.formatTime(event.startTime);
    timeLbl.valign = 'middle';
    timeLbl.align = 'right';
    timeLbl.size = 8;
    timeLbl.color = this.COLORS.TIME;
    if (event.startTime.getTime() - this.TEN_MINUTES_MS < now.getTime() &&
        event.endTime > now) {
      timeLbl.bold = true;
    }
    if (Utils.checkSameDay(event.startTime, now) && event.endTime < now) {
      timeLbl.opacity = 180;
    }
  }
  var itemLbl = itemDiv.appendElement('<label />');
  itemLbl.x = (2 * this.BORDER) + 35;
  itemLbl.y = 0;
  itemLbl.height = '100%';
  itemLbl.width = itemDiv.width - (3 * this.BORDER) - 40;
  itemLbl.innerText = event.title;
  itemLbl.tooltip = this.eventTooltip(event);
  itemLbl.valign = 'middle';
  itemLbl.size = 8;
  itemLbl.trimming = 'character-ellipsis';
  itemLbl.color = g_events.getCalendarColor(event.calendarId);

  if (event.startTime.getTime() - this.TEN_MINUTES_MS < now.getTime() &&
      event.endTime > now) {
    itemLbl.bold = true;
  }
  if (Utils.checkSameDay(event.startTime, now) && event.endTime < now) {
    itemLbl.opacity = 180;
  }
};

Agenda.prototype.eventTooltip = function(event) {
  var tooltip = event.title;
  var eventTimes = Utils.formatEventTimes(event);
  tooltip += '\n' + eventTimes.replace('\n', ' ');
  if (event.desc) {
    tooltip += '\n\n' + event.desc;
  }
  return tooltip;
};

/**
 * Event handler for onClick event. Fired when user clicks on an agenda entry.
 * @param {Event} event Event data for this click
 */
Agenda.prototype.onEventClicked = function(event) {
  // No detailsview on the Mac. We Just go to the page of the event.
  if (Utils.isMac()) {
    Utils.redirectWithSuperAuth(event.alternateUrl);
  }

  debug.trace('You clicked on ' + event.title + '\nShowing detailsview...');
  if (this.lastEvent && event.id == this.lastEvent.id) {
    debug.trace("Close detailsview");
    plugin.closeDetailsView();
    return;
  }

  options.putValue(OPTIONS.CLOSE_DETAILS, false);

  this.lastEvent = event;

  var detailsView = new DetailsView();
  detailsView.SetContent('', undefined, 'eventdetails.xml', false, 0);
  detailsView.contentIsView = true;

  var detailsData = detailsView.detailsViewData;
  detailsData.putValue(OPTIONS.EVENT, event);
  var cal = g_cache.getCalendarByID(event.calendarId);
  detailsData.putValue(OPTIONS.CALENDAR, cal);
  detailsData.putValue(OPTIONS.AUTH, g_auth.getAuthToken());
  try {
    detailsData.putValue(OPTIONS.USERZIP, google.pers.data.getZipCode());
  } catch (e) {
    debug.error('Object google.pers.data.getZipCode is not available!');
    detailsData.putValue(OPTIONS.USERZIP, '');
  }

  plugin.ShowDetailsView(detailsView, '', gddDetailsViewFlagNoFrame,
      Utils.bind(this.onDetailsClosed, this, event));
};

/**
 * Handles the event of when the user is closing the detailsview
 * @param {CalendarEvent} event The event associated with this view.
 */
Agenda.prototype.onDetailsClosed = function(event) {
  // Because of the calling order of close and open we only null
  // the lastEvent when we have a different event than the lastEvent
  // This way we can correctly close the detailsview when the user clicks
  // on the same event for a second time.
  if (this.lastEvent && event.id == this.lastEvent.id) {
    this.lastEvent = null;
  }
};
