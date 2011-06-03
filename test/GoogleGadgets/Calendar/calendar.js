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
 * @fileoverview this object holds all information about a single calendar
 * of the user.
 */

/**
 * Object for calendar data.
 * @constructor
 */
function Calendar() {
  this.title = strings.NEW_CALENDAR_TITLE;
  this.id = '';
  this.url = '';
  this.email = '';
  this.color = '';
  this.accessLevel = '';
  this.selected = false;
  this.timezone = '';
  this.hidden = false;
  this.updated = false;
  this.lastUpdate = new Date(0);
  this.lastUpdateLocal = new Date(0);
  this.overrideName = '';
  this.calendarMinutes = -1;
}

/**
 * Check visibility of a calendar.
 * @return {boolean} True, if calendar is visible
 */
Calendar.prototype.isVisible = function() {
  return !this.hidden;
};

/**
 * Check if calendar is selected.
 * @return {boolean} True, if calendar is visible
 */
Calendar.prototype.isSelected = function() {
  if (this.hidden) return false;

  var visibleOpt = options.getValue(OPTIONS.SHOW + this.id);
  if (typeof(visibleOpt) == 'boolean') {
    return visibleOpt;
  } else {
    return this.selected;
  }
};

/**
 * Return title of the calendar. Use overrideName if set.
 * @return {string} Calendar title
 */
Calendar.prototype.getTitle = function() {
  if (this.overrideName) {
    return this.overrideName;
  } else {
    return this.title;
  }
};

/**
 * Set date and time of last update for this calendar. This is
 * local time not server time.
 */
Calendar.prototype.setUpdateDate = function() {
  this.lastUpdateLocal = new Date();
};

/**
 * Get date and time of last update for this calendar. This is
 * local time not server time.
 */
Calendar.prototype.getUpdateDate = function() {
  return this.lastUpdateLocal;
};

/**
 * Parse XMLNode into calendar data structure.
 * @param {Object} elem DOM Element
 */
Calendar.prototype.parse = function(elem) {
  this.id = this.get$t(elem.id);
  this.id = this.id.substring(this.id.lastIndexOf("/") + 1);
  this.title = this.get$t(elem.title);
  this.email = this.get$t(elem.author[0].email);
  for (var i = 0; i < elem.link.length; ++i) {
    if (elem.link[i].rel == 'alternate') {
      var url = elem.link[i].href;
      url = Utils.forceHttpsUrl(url);
      this.url = url;
    }
  }
  this.color = this.getVal(elem.gCal$color);
  this.accessLevel = this.getVal(elem.gCal$accesslevel);
  if (this.accessLevel != 'owner') {
    options.putDefaultValue(OPTIONS.SHOW + this.id, false);
  }
  this.selected = this.getVal(elem.gCal$selected) == 'true';
  this.timezone = this.getVal(elem.gCal$timezone);
  this.hidden = this.getVal(elem.gCal$hidden) == 'true';
  this.updated = Utils.rfc3339StringToDate(this.get$t(elem.updated));
  this.overrideName = this.getVal(elem.gCal$overridename);
};

/**
 * Retrieve value of calendar attribute if available in JSON
 */
Calendar.prototype.getVal = function(obj) {
  return obj ? obj.value : '';
};

Calendar.prototype.get$t = function(obj) {
  return obj ? obj.$t : '';
};