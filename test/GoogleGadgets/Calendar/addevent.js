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
 * @fileoverview Add event dialog.
 */

var g_addEvent = null;
var g_errorMessage = null;

/**
 * onOpen event handler for add input details view.
 */
function view_onopen() {
  g_errorMessage = new ErrorMessage();
  g_addEvent = new AddEventDialog();
  g_addEvent.fillCalendarList();
  g_addEvent.presetDates();

  setTimeout(function () { eventTitle.focus(); }, 100);
}

/**
 * Class for interaction with the add event dialog.
 * @constructor
 */
function AddEventDialog() {
  this.calendars = detailsViewData.getValue(OPTIONS.CALENDAR);
  this.currentDate = detailsViewData.getValue(OPTIONS.DATE);
  this.currentDate = new Date(this.currentDate);

  eventTitle.onkeypress = Utils.bind(this.onKeyPress, this);
  imgClose.onclick = Utils.bind(this.close, this);

  btnSave.onclick = Utils.bind(this.onSubmit, this);
  eventAllDay.onchange = Utils.bind(this.onAllDayChange, this);

  // Scrollbar code is not available in versions below 5.8
  if (!eventCalendar.scrollbar) return;

  var scrollbar = eventCalendar.scrollbar;
  scrollbar.background = "images/scroll-bar.gif";
  scrollbar.thumbImage = "images/scroll-track.gif";
  scrollbar.thumbDownImage = "images/scroll-track-clicked.gif";
  scrollbar.thumbOverImage = "images/scroll-track.gif";
  scrollbar.rightImage = "images/scroll-down.gif";
  scrollbar.rightDownImage = "images/scroll-down-clicked.gif";
  scrollbar.rightOverImage = "images/scroll-down-over.gif";
  scrollbar.leftImage = "images/scroll-up.gif";
  scrollbar.leftDownImage = "images/scroll-up-clicked.gif";
  scrollbar.leftOverImage = "images/scroll-up-over.gif";
}

/**
 * Constant for colors used in the dialog
 */
AddEventDialog.prototype.COLORS = {
  ERROR_BG: '#D00000',
  DEFAULT_BG: '#3D4AD9'
};

/**
 * Add all calendars to the dropdown list
 */
AddEventDialog.prototype.fillCalendarList = function() {
  for (var i = 0; i < this.calendars.length; ++i) {
    var itemCode = '<item name="' + this.calendars[i].id + '"/>';
    var item = eventCalendar.appendElement(itemCode);
    var lbl = item.appendElement('<label />');
    lbl.x = lbl.y = 0;
    lbl.height = lbl.width = '100%';
    lbl.innerText = this.calendars[i].title;
    lbl.color = '#FFFFFF';
  }
  eventCalendar.selectedIndex = 0;
};

/**
 * Fill date and time edit fields based on the selected date
 * and the current time.
 */
AddEventDialog.prototype.presetDates = function() {
  var now = new Date();
  var d = this.currentDate;
  d.setHours(now.getHours());
  d.setMinutes(now.getMinutes());

  var dateString = strings.ADD_EVENT_DATE_FORMAT;
  dateString = dateString.replace('[![MONTH]!]', d.getMonth() + 1);
  dateString = dateString.replace('[![DAY]!]', d.getDate());
  dateString = dateString.replace('[![YEAR]!]', d.getFullYear());
  eventStartDate.value = dateString;

  if (d.getMinutes() > 30) {
    d.setMinutes(0);
    d.setHours(d.getHours() + 1);
  } else {
    d.setMinutes(30);
  }
  eventStartTime.value = Utils.formatTime(d, true);
  d.setMinutes(d.getMinutes() + 60);
  eventEndTime.value = Utils.formatTime(d, true);

  dateString = strings.ADD_EVENT_DATE_FORMAT;
  dateString = dateString.replace('[![MONTH]!]', d.getMonth() + 1);
  dateString = dateString.replace('[![DAY]!]', d.getDate());
  dateString = dateString.replace('[![YEAR]!]', d.getFullYear());
  eventEndDate.value = dateString;
};

/**
 * OnkeyPress handler for input edits.
 */
AddEventDialog.prototype.onKeyPress = function() {
  if (event.keyCode == 13) {
    this.onSubmit();
  }
};

/**
 * Hide time edit fields if allDay checkbox is selected
 */
AddEventDialog.prototype.onAllDayChange = function() {
  starttimeBg.visible = !eventAllDay.value;
  endtimeBg.visible = !eventAllDay.value;
};

/**
 * onSubmit handler when the button for adding the event is clicked.
 */
AddEventDialog.prototype.onSubmit = function() {
  g_errorMessage.removeMessage();

  var datePattern = new RegExp(strings.ADD_EVENT_DATE_FORMAT_REGEX);
  var timePatternAP =
      new RegExp(/^(([1-9]{1}|(1)[0-2]{1})(:[0-5][0-9]){0,1}(am|pm))$/i);
  var timePattern24 = new RegExp(/^(([01][0-9]|2[0-3]):[0-5][0-9])$/);

  // Check if the dates are in the valid date format.
  if (!eventStartDate.value.match(datePattern)) {
    g_errorMessage.displayMessage(strings.ERROR_DATE_INVALID);
    startdateBg.background = this.COLORS.ERROR_BG;
    return;
  } else {
    startdateBg.background = this.COLORS.DEFAULT_BG;
  }
  if (!eventEndDate.value.match(datePattern)) {
    g_errorMessage.displayMessage(strings.ERROR_DATE_INVALID);
    enddateBg.background = this.COLORS.ERROR_BG;
    return;
  } else {
    enddateBg.background = this.COLORS.DEFAULT_BG;
  }

  // Check time format if no all day event.
  if (!eventAllDay.checked) {
    if (!eventStartTime.value.match(timePatternAP) &&
        !eventStartTime.value.match(timePattern24)) {
      g_errorMessage.displayMessage(strings.ERROR_TIME_INVALID);
      starttimeBg.background = this.COLORS.ERROR_BG;
      return;
    } else {
      starttimeBg.background = this.COLORS.DEFAULT_BG;
    }
    if (!eventEndTime.value.match(timePatternAP) &&
        !eventEndTime.value.match(timePattern24)) {
      g_errorMessage.displayMessage(strings.ERROR_TIME_INVALID);
      endtimeBg.background = this.COLORS.ERROR_BG;
      return;
    } else {
      endtimeBg.background = this.COLORS.DEFAULT_BG;
    }
  }

  // get calendar id for selected calendar.
  var calId = '';
  try {
    calId = eventCalendar.children.item(eventCalendar.selectedIndex).name;
  } catch (e) {
    calId = this.calendars[0].id;
  }

  var event = new CalendarEvent();
  event.title = eventTitle.value;
  event.location = eventLocation.value;

  event.startTime =
    Utils.parseDateTime(eventStartDate.value, eventStartTime.value);
  event.endTime =
    Utils.parseDateTime(eventEndDate.value, eventEndTime.value);

  event.isAllDay = eventAllDay.value;
  if (event.isAllDay) {
    event.endTime.setDate(event.endTime.getDate() + 1);
  }

  if (event.startTime > event.endTime) {
    g_errorMessage.displayMessage(strings.ERROR_END_BEFORE_START);
    return;
  }

  event.calendarId = calId;
  event.desc = eventDescription.value;
  options.putValue(OPTIONS.NEW_EVENT, event);
  options.putValue(OPTIONS.CLOSE_DETAILS, true);
};

/**
 * Close handler when the X in the top-right corner is clicked.
 */
AddEventDialog.prototype.close = function() {
  options.putValue(OPTIONS.CLOSE_DETAILS, true);
};
