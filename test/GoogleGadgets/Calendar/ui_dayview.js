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
 * Class for drawing and interaction with the dayview.
 * @param {Element} target DIV container the dayview should be rendered to
 * @constructor
 */
function DayView(target) {
  this.target_ = target;
  this.controlDiv = null;
  this.contentBg = null;

  this.fadeTimeout = null;

  this.value = new Date();
}

/**
 * Constants for visual elements.
 */
DayView.prototype.MONTH_HEADER_HEIGHT = 27;
DayView.prototype.FOOTER_HEIGHT = 28;
DayView.prototype.CONTROLS_HEIGHT = 26;
DayView.prototype.CONTROLS_OFFSET = 5;
DayView.prototype.MIN_HEIGHT = 126;
DayView.prototype.COLORS = {
  BACKGROUND: '#D6E9F8',
  BACKGROUND_OTHER: '#C2D8E6',
  TEXT: '#2A2C2A',
  MULTIPLY: '#757575'
};


/**
 * Callback function which is triggered when the user changes the day.
 * @type {function}
 */
DayView.prototype.onDateSelected = null;

/**
 * Set the date which should be displayed.
 * @param {Date} d Selected date
 */
DayView.prototype.setDate = function(d) {
  this.selectDay(new Date(d));
};

/**
 * Return the currently displayed year.
 * @return {int} Year
 */
DayView.prototype.getCurrentYear = function() {
  return this.value.getFullYear();
};

/**
 * Return the currently displayed month.
 * @return {int} Year
 */
DayView.prototype.getCurrentMonth = function() {
  return this.value.getMonth();
};

/**
 * Jump the dayview to today
 */
DayView.prototype.goToday = function() {
  //debug.trace('DayView: Jumping to today');
  this.selectDay(new Date());
};

/**
 * Called when a day is selected by the user.
 * @param {Date} day selected date
 */
DayView.prototype.selectDay = function(day) {
  // do nothing if we already show this day
  if (Utils.checkSameDay(this.value, day)) {
    return;
  }

  // onDateSelected needs to fire when value changes
  if (!Utils.checkSameDay(this.value, day)) {
    this.value = new Date(day);
    // Trigger callback function if any is assigned.
    if (typeof(this.onDateSelected) == 'function') {
      this.onDateSelected();
    }
  }

  // Redraw calendar so correct day is selected.
  this.draw();
};

/**
 * Draw the dayview into the target div. 
 */
DayView.prototype.draw = function() {
  if (!this.target_.visible) return;

  this.target_.removeAllElements();
  this.abortFade();

  this.drawMonthHeader();
  this.drawFooter();
  this.drawContent();
  this.drawControls();

  this.scheduleFade();
};

/**
 * Draw the month header day view
 */
DayView.prototype.drawMonthHeader = function() {
  var now = new Date();

  var monthHeader = this.target_.appendElement('<div name="monthHeader" />');
  monthHeader.x = 0;
  monthHeader.y = 0;
  monthHeader.width = this.target_.width;
  monthHeader.height = this.MONTH_HEADER_HEIGHT;

  var headerLeft = monthHeader.appendElement('<img />');
  headerLeft.src = 'images/dayview_header_left.png';
  headerLeft.x = 0;
  headerLeft.y = 0;
  headerLeft.width = headerLeft.srcWidth;
  headerLeft.height = headerLeft.srcHeight;
  if (!Utils.checkSameDay(now, this.value)) {
    headerLeft.colorMultiply = this.COLORS.MULTIPLY;
  }

  var headerRight = monthHeader.appendElement('<img />');
  headerRight.src = 'images/dayview_header_right.png';
  headerRight.x = monthHeader.width - headerRight.srcWidth;
  headerRight.y = 0;
  headerRight.width = headerRight.srcWidth;
  headerRight.height = headerRight.srcHeight;
  if (!Utils.checkSameDay(now, this.value)) {
    headerRight.colorMultiply = this.COLORS.MULTIPLY;
  }

  // Design background for header.
  var bgImg = monthHeader.appendElement('<img />');
  bgImg.src = 'images/month_nav_bg.png';
  bgImg.x = headerLeft.width;
  bgImg.y = 0;
  bgImg.height = bgImg.srcHeight;
  bgImg.width = this.target_.width - headerLeft.width - headerRight.width;
  if (!Utils.checkSameDay(now, this.value)) {
    bgImg.colorMultiply = this.COLORS.MULTIPLY;
  }

  // Label displaying current month and year.
  var monthLabel = monthHeader.appendElement('<label />');
  monthLabel.x = 0;
  monthLabel.y = 0;
  monthLabel.width = monthHeader.width;
  monthLabel.height = this.MONTH_HEADER_HEIGHT;
  monthLabel.bold = true;
  monthLabel.align = 'center';
  monthLabel.valign = 'middle';
  monthLabel.size = 12;
  monthLabel.font = 'arial';
  monthLabel.enabled = true;
  monthLabel.tooltip = g_events.getToolTip(this.value);
  var labelTxt = strings.MONTH_YEAR_HEADER;
  labelTxt = labelTxt.replace('[![MONTH]!]', MONTHS[this.getCurrentMonth()]);
  labelTxt = labelTxt.replace('[![YEAR]!]', this.getCurrentYear());
  monthLabel.innerText = labelTxt;
};

/**
 * Draw the footer of the day view
 */
DayView.prototype.drawFooter = function() {
  var now = new Date();
  var dayViewFooter = this.target_.appendElement('<div />');
  dayViewFooter.x = 0;
  dayViewFooter.y = this.target_.height - this.FOOTER_HEIGHT;
  dayViewFooter.width = this.target_.width;
  dayViewFooter.height = this.FOOTER_HEIGHT;

  var footerLeft = dayViewFooter.appendElement('<img />');
  footerLeft.src = 'images/dayview_bottom_left.png';
  footerLeft.x = 0;
  footerLeft.y = 0;
  footerLeft.width = footerLeft.srcWidth;
  footerLeft.height = footerLeft.srcHeight;
  if (!Utils.checkSameDay(now, this.value)) {
    footerLeft.colorMultiply = this.COLORS.MULTIPLY;
  }

  var footerRight = dayViewFooter.appendElement('<img />');
  footerRight.src = 'images/dayview_bottom_right.png';
  footerRight.x = dayViewFooter.width - footerRight.srcWidth;
  footerRight.y = 0;
  footerRight.width = footerRight.srcWidth;
  footerRight.height = footerRight.srcHeight;
  if (!Utils.checkSameDay(now, this.value)) {
    footerRight.colorMultiply = this.COLORS.MULTIPLY;
  }

  // Design background for header.
  var bgImg = dayViewFooter.appendElement('<img />');
  bgImg.src = 'images/dayview_bottom_bg.png';
  bgImg.x = footerLeft.width;
  bgImg.y = 0;
  bgImg.height = bgImg.srcHeight;
  bgImg.width = this.target_.width - footerLeft.width - footerRight.width;
  if (!Utils.checkSameDay(now, this.value)) {
    bgImg.colorMultiply = this.COLORS.MULTIPLY;
  }
};

/**
 * Draw content of the dayview (number, weekday)
 */
DayView.prototype.drawContent = function() {
  this.contentBg = this.target_.appendElement('<div />');
  this.contentBg.x = 0;
  this.contentBg.y = this.MONTH_HEADER_HEIGHT;
  this.contentBg.width = this.target_.width;
  this.contentBg.height = this.target_.height -
                          this.MONTH_HEADER_HEIGHT -
                          this.FOOTER_HEIGHT;
  var now = new Date();
  if (!Utils.checkSameDay(now, this.value)) {
    this.contentBg.background = this.COLORS.BACKGROUND_OTHER;
  } else {
    this.contentBg.background = this.COLORS.BACKGROUND;
  }
  this.contentBg.enabled = true;
  this.contentBg.onmouseover = Utils.bind(this.onMouseOver, this);
  this.contentBg.onmouseout = Utils.bind(this.onMouseOut, this);
  this.contentBg.tooltip = g_events.getToolTip(this.value);

  var largeNumber = this.contentBg.appendElement('<label name="largeNum" />');
  largeNumber.x = 0;
  largeNumber.width = this.contentBg.width;
  largeNumber.height = Math.min(this.contentBg.height, largeNumber.width) *
    0.98; // height proportional to available height and width
  largeNumber.size = Math.max(this.contentBg.height, this.contentBg.width) *
    0.38; // font size proportional to the size of the element
  largeNumber.y = Math.round(largeNumber.size * 0.1);
  largeNumber.bold = true;
  largeNumber.font = 'Arial';
  largeNumber.align = 'center';
  largeNumber.valign = 'middle';
  largeNumber.innerText = this.value.getDate();
  largeNumber.tooltip = g_events.getToolTip(this.value);

  var weekDay = this.target_.appendElement('<label name="weekDay" />');
  weekDay.x = 0;
  weekDay.y = largeNumber.y + largeNumber.height + this.contentBg.y -
    (largeNumber.height * 0.15);
  weekDay.width = this.target_.width;
  weekDay.height = this.target_.height -
                   weekDay.y;
  weekDay.size = Math.min(weekDay.height, weekDay.width) * 0.3;
  weekDay.bold = true;
  weekDay.font = 'Arial';
  weekDay.align = 'center';
  weekDay.valign = 'middle';
  weekDay.innerText = WEEK_DAYS[this.value.getDay()];
  weekDay.tooltip = g_events.getToolTip(this.value);
};

/**
 * Add controls for the dayview
 */
DayView.prototype.drawControls = function() {
  var largeNumber = this.contentBg.children.item('largeNum');
  this.controlDiv = this.target_.appendElement('<div />');
  this.controlDiv.x = this.CONTROLS_OFFSET;
  this.controlDiv.y = this.MONTH_HEADER_HEIGHT +
                      (largeNumber.height / 2) -
                      (this.CONTROLS_HEIGHT / 2);
  this.controlDiv.width = this.target_.width - (2 * this.CONTROLS_OFFSET);
  this.controlDiv.height = this.CONTROLS_HEIGHT;
  this.controlDiv.opacity = 255;

  var navPrev = this.controlDiv.appendElement('<img />');
  navPrev.src = 'images/dayview_nav_prev.png';
  navPrev.x = 0;
  navPrev.y = 0;
  navPrev.enabled = true;
  navPrev.cursor = 'hand';
  navPrev.onmouseover = Utils.bind(this.abortFade, this);
  navPrev.onmousemove = Utils.bind(this.abortFade, this);
  navPrev.onclick = Utils.bind(this.prevDay, this, 1);
  navPrev.ondblclick = Utils.bind(this.prevDay, this, 2);

  var navNext = this.controlDiv.appendElement('<img />');
  navNext.src = 'images/dayview_nav_next.png';
  navNext.x = this.controlDiv.width - navNext.srcWidth;
  navNext.y = 0;
  navNext.enabled = true;
  navNext.cursor = 'hand';
  navNext.onmouseover = Utils.bind(this.abortFade, this);
  navNext.onmousemove = Utils.bind(this.abortFade, this);
  navNext.onclick = Utils.bind(this.nextDay, this, 1);
  navNext.ondblclick = Utils.bind(this.nextDay, this, 2);
};

/**
 * Event handler for controls. Fades in controls when mouse moves over dayview.
 */
DayView.prototype.onMouseOver = function() {
  beginAnimation(Utils.bind(this.fadeControls, this),
                 this.controlDiv.opacity, 255, 150);
};

/**
 * Event handler for controls. Schedules hiding of controls.
 */
DayView.prototype.onMouseOut = function() {
  this.scheduleFade();
};

/**
 * Schedule the fade out effect
 */
DayView.prototype.scheduleFade = function() {
  this.fadeTimeout = setTimeout(Utils.bind(this.hideControls, this), 1000);
};

/**
 * Start animation to fade out controls.
 */
DayView.prototype.hideControls = function() {
  beginAnimation(Utils.bind(this.fadeControls, this),
                 this.controlDiv.opacity, 0, 300)
};

/**
 * Fading of the events.
 */
DayView.prototype.fadeControls = function() {
  this.controlDiv.opacity = event.value;
};

/**
 * Abort fade-out of the controls.
 */
DayView.prototype.abortFade = function() {
  clearTimeout(this.fadeTimeout);
};

/**
 * Navigation the days (previous).
 * @param {integer} steps Days the current date should be shifted by.
 */
DayView.prototype.prevDay = function(steps) {
  this.value.setDate(this.value.getDate() - steps);
  this.draw();
  if (typeof(this.onDateSelected) == 'function') {
    this.onDateSelected();
  }
  this.abortFade();
};

/**
 * Navigation the days (next).
 * @param {integer} steps Days the current date should be shifted by.
 */
DayView.prototype.nextDay = function(steps) {
  this.value.setDate(this.value.getDate() + steps);
  this.draw();
  if (typeof(this.onDateSelected) == 'function') {
    this.onDateSelected();
  }
  this.abortFade();
};
