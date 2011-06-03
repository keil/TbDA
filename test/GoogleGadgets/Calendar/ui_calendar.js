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
 * @fileoverview Mini calendar ui.
 */

/**
 * Class for drawing and interaction with the mini calendar.
 * @param {Element} target DIV container the calendar should be rendered to
 * @constructor
 */
function MiniCalendar(target) {
  this.target_ = target;
  this.future_ = null;

  this.value = new Date(0);
  this.renderDate_ = new Date(0);

  // Visual objects:
  this.monthBox_ = null;
  this.yearBox_ = null;
}

/**
 * Constants for visual elements.
 */
MiniCalendar.prototype.MONTH_HEADER_HEIGHT = 27;
MiniCalendar.prototype.WEEKDAY_HEADER_HEIGHT = 19;
MiniCalendar.prototype.WEEK_LINE_HEIGHT = 16;
MiniCalendar.prototype.DESIGN_BORDER = 2;
MiniCalendar.prototype.COLORS = {
  DAY_GRAYED: '#C0C0C0',
  DAY_DEFAULT: '#000000',
  DAY_BUSY100: '#095CD4',
  DAY_BUSY75: '#1E75CC',
  DAY_BUSY50: '#337FCC',
  DAY_BUSY25: '#4789CC',
  DAY_SELECTED: '#FFFFFF',
  DAY_BG_DEFAULT: '',
  DAY_BG_HOVER: '#ECECEC',
  DAY_BG_SELECTED: 'images/day_selected.png',
  DAY_BG_TODAY: '#FFFFCC',
  LINK: '#0000FF'
};


/**
 * Set the date which should be displayed.
 * @param {Date} d Selected date
 */
MiniCalendar.prototype.setDate = function(d) {
  this.selectDay(new Date(d));
};

/**
 * Return the currently displayed year.
 * @return {int} Year
 */
MiniCalendar.prototype.getCurrentYear = function() {
  return this.renderDate_.getFullYear();
};

/**
 * Return the currently displayed month.
 * @return {int} Year
 */
MiniCalendar.prototype.getCurrentMonth = function() {
  return this.renderDate_.getMonth();
};

/**
 * Callback function which is triggered when the user selects
 * a day in the calendar.
 * @type {function}
 */
MiniCalendar.prototype.onDateSelected = null;

/**
 * Callback function which is triggered when the height of the
 * calendar has changed
 * @type {function}
 */
MiniCalendar.prototype.onCalendarResized = null;

/**
 * Jump the current calendar view to today
 */
MiniCalendar.prototype.goToday = function() {
  //debug.trace('Calendar: Jumping to today');
  this.selectDay(new Date());
};

/**
 * Draw the mini calendar into the target div. This function calls the 
 * different drawing routines which make up the calendar.
 */
MiniCalendar.prototype.draw = function() {
  if (!this.target_.visible) return;

  this.closeFutureChooser();
  this.target_.removeAllElements();

  //debug.trace('Drawing calendar now');
  this.drawMonthHeader();
  this.drawWeekHeader();
  this.drawWeeks();

  this.target_.focus();
};

/**
 * Draw the month header of the mini calendar.
 */
MiniCalendar.prototype.drawMonthHeader = function() {
  var monthHeader = this.target_.appendElement('<div name="monthHeader"/>');
  monthHeader.x = 0;
  monthHeader.y = 0;
  monthHeader.width = this.target_.width;
  monthHeader.height = this.MONTH_HEADER_HEIGHT;

  var navPrev = monthHeader.appendElement('<img />');
  navPrev.src = 'images/month_nav_prev.png';
  navPrev.x = 0;
  navPrev.y = 0;
  navPrev.width = navPrev.srcWidth;
  navPrev.height = navPrev.srcHeight;
  navPrev.enabled = true;
  navPrev.cursor = 'hand';
  navPrev.onclick = Utils.bind(this.drawPreviousMonth, this, 1);
  navPrev.ondblclick = Utils.bind(this.drawPreviousMonth, this, 2);
  navPrev.onmouseover = function() {
    event.srcElement.src = 'images/month_nav_prev_hover.png';
  };
  navPrev.onmouseout = function() {
    event.srcElement.src = 'images/month_nav_prev.png';
  };

  var navNext = monthHeader.appendElement('<img />');
  navNext.src = 'images/month_nav_next.png';
  navNext.x = monthHeader.width - navNext.srcWidth;
  navNext.y = 0;
  navNext.width = navNext.srcWidth;
  navNext.height = navNext.srcHeight;
  navNext.enabled = true;
  navNext.cursor = 'hand';
  navNext.onclick = Utils.bind(this.drawNextMonth, this, 1);
  navNext.ondblclick = Utils.bind(this.drawNextMonth, this, 2);
  navNext.onmouseover = function() {
    event.srcElement.src = 'images/month_nav_next_hover.png';
  };
  navNext.onmouseout = function() {
    event.srcElement.src = 'images/month_nav_next.png';
  };

  // Design background for header.
  var bgImg = monthHeader.appendElement('<div />');
  bgImg.background = 'images/month_nav_bg.png';
  bgImg.x = navPrev.width;
  bgImg.y = 0;
  bgImg.height = this.MONTH_HEADER_HEIGHT;
  bgImg.width = monthHeader.width - navPrev.width - navNext.width;

  // Label displaying current month and year.
  var monthLabel = monthHeader.appendElement('<label />');
  monthLabel.x = navPrev.width;
  monthLabel.y = 0;
  monthLabel.width = monthHeader.width - navPrev.width - navNext.width;
  monthLabel.height = this.MONTH_HEADER_HEIGHT;
  monthLabel.bold = true;
  monthLabel.align = 'center';
  monthLabel.valign = 'middle';
  monthLabel.size = 10;
  monthLabel.font = 'Arial';
  monthLabel.enabled = true;
  monthLabel.trimming = 'character-ellipsis';
  var labelTxt = strings.MONTH_YEAR_HEADER;
  labelTxt = labelTxt.replace('[![MONTH]!]', MONTHS[this.getCurrentMonth()]);
  labelTxt = labelTxt.replace('[![YEAR]!]', this.getCurrentYear());
  monthLabel.innerText = labelTxt;
  // No future date picker on Mac
  if (!Utils.isMac()) {
    monthLabel.cursor = 'hand';
    monthLabel.onclick = Utils.bind(this.chooseFuture, this);
  }
};

/**
 * Draw the week header of the mini calendar.
 */
MiniCalendar.prototype.drawWeekHeader = function() {
  // Draw week days header.
  var weekHeader = this.target_.appendElement('<div name="weekHeader"/>');
  weekHeader.x = 0;
  weekHeader.y = this.MONTH_HEADER_HEIGHT;
  weekHeader.width = this.target_.width;
  weekHeader.height = this.WEEKDAY_HEADER_HEIGHT;

  var leftImg = weekHeader.appendElement('<img />');
  leftImg.src = 'images/weekday_left.png';
  leftImg.x = 0;
  leftImg.y = 0;
  leftImg.width = leftImg.srcWidth;
  leftImg.height = leftImg.srcHeight;

  var rightImg = weekHeader.appendElement('<img />');
  rightImg.src = 'images/weekday_right.png';
  rightImg.x = this.target_.width - rightImg.srcWidth;
  rightImg.y = 0;
  rightImg.width = rightImg.srcWidth;
  rightImg.height = rightImg.srcHeight;

  // Design background for week header.
  var bgImg = weekHeader.appendElement('<img />');
  bgImg.src = 'images/weekday_bg.png';
  bgImg.x = leftImg.width;
  bgImg.y = 0;
  bgImg.height = bgImg.srcHeight;
  bgImg.width = this.target_.width - leftImg.width - rightImg.width;

  // Get start day of the week. Shift the week days to match the 
  // selected start day.
  var weekStart = g_calendarGadget.getWeekStart();
  var weekDays_ = [];
  for (var i = 0; i < WEEK_DAYS_SHORT.length; ++i) {
    weekDays_[i] = WEEK_DAYS_SHORT[i];
  }

  if (weekStart == 1) {
    weekDays_.push(weekDays_.shift());
  } else if (weekStart == -1) {
    weekDays_.unshift(weekDays_.pop());
  }

  var weekDayWidth = (this.target_.width - 2 * this.DESIGN_BORDER) / 7;
  for (var i = 0; i < weekDays_.length; ++i) {
    var dayHead = weekHeader.appendElement('<label />');
    dayHead.x = this.DESIGN_BORDER + (i * weekDayWidth);
    dayHead.y = 0;
    dayHead.width = weekDayWidth;
    dayHead.height = weekHeader.height;
    dayHead.align = 'center';
    dayHead.valign = 'middle';
    dayHead.color = '#FFFFFF';
    dayHead.bold = true;
    dayHead.size = 8;
    dayHead.font = 'Arial';
    dayHead.innerText = weekDays_[i];
  }
};

/**
 * Draw the weeks of the calendar.
 */
MiniCalendar.prototype.drawWeeks = function() {
  var today = new Date();

  var currentDay = new Date();
  currentDay.setDate(1);
  currentDay.setMonth(this.getCurrentMonth());
  currentDay.setFullYear(this.getCurrentYear());

  // Shift start date so we start displaying with the first possible date
  // which can also be from the previous month.
  var weekStart = g_calendarGadget.getWeekStart();
  currentDay.setDate(currentDay.getDate() - currentDay.getDay() -
                   (-1 * weekStart));
  if (currentDay.getDate() != 1 && currentDay.getDate() < 5) {
    currentDay.setDate(currentDay.getDate() - 7);
  }

  var dayCounter = 0;
  var weekLine = 0;
  var weekDayWidth = (this.target_.width - 2 * this.DESIGN_BORDER) / 7;
  var maxDays = 35;
  var today = new Date();

  // Detect first week we are showing
  var weekCheck = new Date(currentDay);
  weekCheck.setDate(weekCheck.getDate() + 3);
  var firstWeek = Utils.getWeekNumber(weekCheck);

  for (var i = 0; i < maxDays; ++i) {
    var dayBox = this.target_.appendElement('<div />');
    dayBox.x = this.DESIGN_BORDER + (dayCounter * weekDayWidth);
    dayBox.y = this.MONTH_HEADER_HEIGHT + this.WEEKDAY_HEADER_HEIGHT +
      (this.WEEK_LINE_HEIGHT * weekLine);
    dayBox.width = weekDayWidth;
    dayBox.height = this.WEEK_LINE_HEIGHT;

    var dayLabel = dayBox.appendElement('<label name="dayLabel"/>');
    dayLabel.x = 0;
    dayLabel.y = 0;
    dayLabel.width = '100%';
    dayLabel.height = '100%';
    dayLabel.align = 'center';
    dayLabel.valign = 'middle';
    dayLabel.size = 9;
    dayLabel.font = 'Arial';
    dayLabel.innerText = currentDay.getDate();
    if (currentDay.getMonth() != this.getCurrentMonth()) {
      dayLabel.color = this.COLORS.DAY_GRAYED;
    } else if (g_events.isBusy(currentDay)) {
      var events = g_events.getEventsForDay(currentDay);
      var timespan = 0;
      for (var e = 0; e < events.length; ++e) {
        var event = events[e];
        if (event.isAllDay) continue;
        timespan += event.endTime - event.startTime;
      }
      // Get timespan in hours
      timespan /= 1000 * 60 * 60;
      if (timespan >= 6) {
        dayLabel.color = this.COLORS.DAY_BUSY100;
      } else if (timespan >= 4) {
        dayLabel.color = this.COLORS.DAY_BUSY75;
      } else if (timespan >= 2) {
        dayLabel.color = this.COLORS.DAY_BUSY50;
      } else {
        dayLabel.color = this.COLORS.DAY_BUSY25;
      }
      dayLabel.bold = true;
      // Add tooltip with short list of events for this day
      dayLabel.tooltip = g_events.getToolTip(currentDay);
    } else {
      dayLabel.color = this.COLORS.DAY_DEFAULT;
      dayLabel.bold = true;
    }

    // Interaction with the calendar
    dayBox.cursor = 'hand';
    dayBox.enabled = true;

    // Set background and color for selected date
    if (Utils.checkSameDay(this.value, currentDay)) {
      dayBox.background = this.COLORS.DAY_BG_SELECTED;
      dayLabel.color = this.COLORS.DAY_SELECTED;
    } else if (Utils.checkSameDay(today, currentDay)) {
      dayBox.background = this.COLORS.DAY_BG_TODAY;
    }

    // Clone currentDay object since currentDay will change and we don't 
    // want references here
    var current = new Date(currentDay);
    dayBox.onmouseover = Utils.bind(this.onDayMouseOver, this, current);
    dayBox.onmouseout = Utils.bind(this.onDayMouseOut, this, current);
    dayBox.onclick = Utils.bind(this.onDayClick, this, current);

    // Count the days
    ++dayCounter;
    currentDay.setDate(currentDay.getDate() + 1);
    if ((dayCounter > 0) && (dayCounter % 7 === 0)) {
      dayCounter = 0;
      ++weekLine;
    }

    // Extend maxDays if we need a sixth line for this month
    if ((i == maxDays - 1) &&
        currentDay.getMonth() == this.getCurrentMonth()) {
      maxDays += 7;
    }
  }

  // Detect last week we are showing and set tooltip for week header
  var lastWeek = Utils.getWeekNumber(currentDay);
  var caption = strings.WEEK_HEADER;
  caption = caption.replace('[![FIRST]!]', firstWeek);
  caption = caption.replace('[![LAST]!]', lastWeek);
  var weekHeader = this.target_.children.item('weekHeader');
  for (var i = 0; i < weekHeader.children.count - 1; ++i) {
    weekHeader.children.item(i).tooltip = caption;
    weekHeader.children.item(i).cursor = 'help';
  }

  var prevHeight = this.target_.height;
  // Modify target div to match actual size of the mini calendar
  this.target_.height = this.MONTH_HEADER_HEIGHT +
      this.WEEKDAY_HEADER_HEIGHT +
      this.WEEK_LINE_HEIGHT * weekLine;
  if (this.target_.height != prevHeight) {
    if (typeof(this.onCalendarResized) == 'function') {
      this.onCalendarResized();
    }
  }
};

/**
 * Generate onMouseOver event handler which will be attached to each day.
 * @param {Date} currentDay Date for the selected day
 */
MiniCalendar.prototype.onDayMouseOver = function(currentDay) {
  if (Utils.checkSameDay(new Date(), currentDay)) {
    //event.srcElement.background = this.COLORS.DAY_BG_TODAY;
  } else if (Utils.checkSameDay(this.value, currentDay)) {
    event.srcElement.background = this.COLORS.DAY_BG_SELECTED;
    event.srcElement.children.item('dayLabel').color =
        this.COLORS.DAY_SELECTED;
  } else {
    event.srcElement.background = this.COLORS.DAY_BG_HOVER;
  }
};

/**
 * Generate onMouseOut event handler which will be attached to each day.
 * @param {Date} currentDay Date for the selected day
 */
MiniCalendar.prototype.onDayMouseOut = function(currentDay) {
  if (Utils.checkSameDay(this.value, currentDay)) {
    event.srcElement.background = this.COLORS.DAY_BG_SELECTED;
  } else if (Utils.checkSameDay(new Date(), currentDay)) {
    event.srcElement.background = this.COLORS.DAY_BG_TODAY;
  } else {
    event.srcElement.background = this.COLORS.DAY_BG_DEFAULT;
  }
};

/**
 * Generate onClick event handler which will be attached to each day.
 * @param {Date} current Date for the selected day
 */
MiniCalendar.prototype.onDayClick = function(current) {
  var currentDay = new Date();
  currentDay.setMonth(current.getMonth());
  currentDay.setDate(current.getDate());
  currentDay.setFullYear(current.getFullYear());
  this.selectDay(currentDay);
};

/**
 * Jump to previous month.
 * @param {integer} steps How many months to move backward.
 */
MiniCalendar.prototype.drawPreviousMonth = function(steps) {
  this.renderDate_.setMonth(this.getCurrentMonth() - steps);
  this.draw();
};

/**
 * Jump to next month.
 * @param {integer} steps How many months to move forward.
 */
MiniCalendar.prototype.drawNextMonth = function(steps) {
  this.renderDate_.setMonth(this.getCurrentMonth() + steps);
  this.draw();
};

/**
 * Called when a day is selected by the user.
 * @param {Date} day selected date
 */
MiniCalendar.prototype.selectDay = function(day) {
  //debug.trace('Clicked on day: ' + day);

  // do nothing if we already show this day
  if (Utils.checkSameDay(this.renderDate_, day)) {
    return;
  }

  // User chooses date from next/prev month.
  if (day.getMonth() != this.getCurrentMonth()) {
    this.value.setMonth(day.getMonth());
    this.value.setFullYear(day.getFullYear());
  }

  // onDateSelected needs to fire when value changes
  if (!Utils.checkSameDay(day, this.value)) {
    this.value = new Date(day);
    // Trigger callback function if any is assigned.
    if (typeof(this.onDateSelected) == 'function') {
      this.onDateSelected();
    }
  }

  // Assign value of class so that caller can work with it.
  this.renderDate_ = new Date(day);

  // Redraw calendar so correct day is selected.
  this.draw();
};

/**
 * Show dialog to choose a future year to jump to
 */
MiniCalendar.prototype.chooseFuture = function() {
  if (this.future_ != null) {
    this.closeFutureChooser();
    return;
  }
  debug.info('Show dialog to choose future date');

  this.future_ = this.target_.appendElement('<div />');
  this.future_.x = 0;
  this.future_.y = this.MONTH_HEADER_HEIGHT;
  this.future_.width = this.target_.width;
  this.future_.height = this.target_.height - this.MONTH_HEADER_HEIGHT;
  this.future_.background = '#FFFFFF';
  this.future_.enabled = true;

  var itemHeight = this.future_.height / 5;
  var itemWidth = this.future_.width / 3;

  var startYear = this.getCurrentYear() - 7;
  var endYear = this.getCurrentYear() + 7;
  var counter = 0;
  var itemLeft = 0;
  for (var year = startYear; year <= endYear; ++year) {
    var yearDiv = this.future_.appendElement('<div />');
    yearDiv.x = itemLeft;
    yearDiv.y = counter * itemHeight;
    yearDiv.height = itemHeight;
    yearDiv.width = itemWidth;
    yearDiv.enabled = true;
    yearDiv.cursor = 'hand';
    yearDiv.onmouseover = Utils.bind(this.onYearMouseOver, this, year);
    yearDiv.onmouseout = Utils.bind(this.onYearMouseOut, this, year);
    yearDiv.onclick = Utils.bind(this.onYearClick, this, year);

    var yearLabel = yearDiv.appendElement('<label name="yearLabel"/>');
    yearLabel.x = 0;
    yearLabel.y = 0;
    yearLabel.height = '100%';
    yearLabel.width = '100%';
    yearLabel.align = 'center';
    yearLabel.valign = 'middle';
    yearLabel.innerText = year;
    if (year == this.getCurrentYear()) {
      yearDiv.background = this.COLORS.DAY_BG_SELECTED;
      yearLabel.color = this.COLORS.DAY_SELECTED;
      yearLabel.bold = true;
    }

    ++counter;
    if (counter % 5 == 0) {
      counter = 0;
      itemLeft += itemWidth;
    }
  }
};

/**
 * Close the choose future year dialog
 */
MiniCalendar.prototype.closeFutureChooser = function() {
  if (this.future_ == null) return;

  this.target_.removeElement(this.future_);
  this.future_ = null;
};

/**
 * Generate onMouseOver event handler which will be attached to each year.
 * @param {int} year Selected year
 */
MiniCalendar.prototype.onYearMouseOver = function(year) {
  if (year == this.getCurrentYear()) {
    event.srcElement.background = this.COLORS.DAY_BG_SELECTED;
    event.srcElement.children.item('yearLabel').color =
        this.COLORS.DAY_SELECTED;
  } else {
    event.srcElement.background = this.COLORS.DAY_BG_HOVER;
  }
};

/**
 * Generate onMouseOut event handler which will be attached to each year.
 * @param {int} year Selected year
 */
MiniCalendar.prototype.onYearMouseOut = function(year) {
  if (year == this.getCurrentYear()) {
    event.srcElement.background = this.COLORS.DAY_BG_SELECTED;
  } else {
    event.srcElement.background = this.COLORS.DAY_BG_DEFAULT;
  }
};

/**
 * Generate onClick event handler which will be attached to each year.
 * @param {int} year Selected year
 */
MiniCalendar.prototype.onYearClick = function(year) {
  var newDate = new Date(this.value);
  newDate.setFullYear(year);
  this.closeFutureChooser();
  this.selectDay(newDate);
};
