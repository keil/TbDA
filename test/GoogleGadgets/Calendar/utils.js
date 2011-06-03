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

// @fileoverview Utilities.

var Utils = {};

/**
 * Bind function to attach method out of scope.
 * @param {function} method Method to attach.
 * @param {object} self Base object to attach to.
 * @param {*} var_args additional arguments for the function
 * @return {function} attached function
 */
Utils.bind = function(method, self, var_args) {
  var args = Array.prototype.slice.call(arguments, 2);

  return function(var_args) {
    method.apply(self,
        args.concat(Array.prototype.slice.call(arguments)));
  };
};

/**
 * Check to see if two date objects are the same day.
 * @param {Date} date1 Date to check
 * @param {Date} date2 Date to check
 * @return {boolean} True, if dates match on day, month and year.
 */
Utils.checkSameDay = function(date1, date2) {
  return date1.getDate() == date2.getDate() &&
         date1.getMonth() == date2.getMonth() &&
         date1.getFullYear() == date2.getFullYear();
};

/**
 * Check to see if two date objects are the same time.
 * @param {Date} date1 Date to check
 * @param {Date} date2 Date to check
 * @return {boolean} True, if times match on hours and minutes.
 */
Utils.sameStartTime = function(date1, date2) {
  return date1.getHours() == date2.getHours() &&
         date1.getMinutes() == date2.getMinutes();
};

/**
 * Prefix a 0 if number is smaller than 10. e.g. 9 becomes 09.
 * @param {int} num Number to pad
 * @return {string} Padded number
 */
Utils.pad = function(num) {
  return (num < 10 ? '0' : '') + num;
};

/**
 * Get week number for specified date.
 * From http://www.codeproject.com/KB/cs/gregorianwknum.aspx
 * @param {Date} date Date to get week for.
 * @return {int} Week number.
 */
Utils.getWeekNumber = function(date) {
    //Find JulianDay
    month = date.getMonth() + 1; //use 1-12
    day = date.getDate();
    year = date.getFullYear();

    var a = Math.floor((14 - (month)) / 12);
    var y = year + 4800 - a;
    var m = month + (12 * a) - 3;
    var jd = day + Math.floor(((153 * m) + 2) / 5) +
                 (365 * y) + Math.floor(y / 4) - Math.floor(y / 100) +
                 Math.floor(y / 400) - 32045;  // (gregorian calendar)

    //now calc weeknumber according to JD
    var d4 = (jd + 31741 - (jd % 7)) % 146097 % 36524 % 1461;
    var L = Math.floor(d4 / 1460);
    var d1 = ((d4 - L) % 365) + L;
    return Math.floor(d1 / 7) + 1;
};

/**
 * Create ISO8601 date format from date object.
 * @param {Date} date Date to convert to iso8601
 * @param {boolean} opt_time True, if time should be added to ISO8601 date.
 * @return {string} Formatted text
 */
Utils.getDateIso8601 = function(date, opt_time) {
  var rfc = date.getFullYear() + '-' +
            Utils.pad(date.getMonth() + 1) + '-' +
            Utils.pad(date.getDate());
  if (opt_time) {
    var timezone = (date.getTimezoneOffset() / 60);
    rfc = rfc + 'T' +
          Utils.pad(date.getHours()) + ':' +
          Utils.pad(date.getMinutes()) + ':' +
          Utils.pad(date.getSeconds());
    if (timezone == 0) {
      rfc += 'Z';
    } else {
      rfc += (timezone > 0) ? '-' : '+';
      if (timezone < 0) {
        timezone *= -1;
      }
      if (timezone % 1 == 0) {
        rfc += Utils.pad(timezone) + ':00';
      } else {
        var hours = Math.floor(timezone);
        var minutes = (timezone - hours) * 60;
        rfc += Utils.pad(hours) + ':' + Utils.pad(minutes);
     }
    }
  }
  return rfc;
};

// Constants for the rfc339StringToDate function
Utils.DATE_TIME_REGEX = /^(\d\d\d\d)-(\d\d)-(\d\d)T(\d\d):(\d\d):(\d\d)\.\d+(\+|-)(\d\d):(\d\d)$/;
Utils.DATE_TIME_REGEX_Z = /^(\d\d\d\d)-(\d\d)-(\d\d)T(\d\d):(\d\d):(\d\d)\.\d+Z$/;
Utils.DATE_REGEX = /^(\d\d\d\d)-(\d\d)-(\d\d)$/;

/**
 * Convert the incoming date into a javascript date
 * we accept the following 3 forms:
 * 2006-04-28T09:00:00.000-07:00
 * 2006-04-28T09:00:00.000Z
 * 2006-04-19
 * @param {string} rfc3339 date/time string in valid rfc3339 format.
 * @return {Date} Date object with the correct information.
 */
Utils.rfc3339StringToDate = function(rfc3339) {
  var parts = Utils.DATE_TIME_REGEX.exec(rfc3339);

  // Try out the Z version.
  if (!parts) {
    parts = Utils.DATE_TIME_REGEX_Z.exec(rfc3339);
  }

  if (parts && parts.length > 0) {
    var d = new Date();
    d.setUTCFullYear(parts[1], parseInt(parts[2], 10) - 1, parts[3]);
    d.setUTCHours(parts[4]);
    d.setUTCMinutes(parts[5]);
    d.setUTCSeconds(parts[6]);

    var tzOffsetFeedMin = 0;
    if (parts.length > 7) {
      tzOffsetFeedMin = parseInt(parts[8], 10) * 60 + parseInt(parts[9], 10);
      if (parts[7] != '-') { // This is supposed to be backwards.
        tzOffsetFeedMin = -tzOffsetFeedMin;
      }
    }
    return new Date(d.getTime() + tzOffsetFeedMin * 60 * 1000);
  }

  parts = Utils.DATE_REGEX.exec(rfc3339);
  if (parts && parts.length > 0) {
    return new Date(parts[1], parseInt(parts[2], 10) - 1, parts[3]);
  }

  return null;
};

/**
 * Parse date and time strings into Date object.
 * @param {string} d Date string
 * @param {string} t Time string
 * @return {Date} Date object.
 */
Utils.parseDateTime = function(d, t) {
  var result = new Date();

  if (t.match(/(am|pm)$/i)) {
    var matches = t.match(/([0-9]+)((:)([0-9]+)){0,1}(am|pm)/i);
    if (!matches) return null;
    var hours = parseInt(matches[1]);
    if (matches[5].toLowerCase() == 'pm' && hours != 12) {
      hours += 12;
    }
    if (matches[5].toLowerCase() == 'am' && hours == 12) {
      hours = 0; 
    }
    result.setHours(hours);

    if (matches[4] != '') {
      result.setMinutes(parseInt(matches[4]));
    } else {
      result.setMinutes(0);
    }
    result.setSeconds(0);

    var msecs = Date.parse(d);
    var dateData = new Date();
    dateData.setTime(msecs);
    result.setDate(dateData.getDate());
    result.setMonth(dateData.getMonth());
    result.setFullYear(dateData.getFullYear());
  } else {
    var msecs = Date.parse(d + ' ' + t);
    result.setTime(msecs);
  }
  return result;
}

/**
 * Format date according to user preference (am/pm or 24-hour style)
 * @param {Date} date Date of event
 * @param {boolean} opt_long True, if the time should use 'am' instead of 'a'
 * @return {string} Formatted time string
 */
Utils.formatTime = function(date, opt_long) {
  var hours = date.getHours();
  var minutes = date.getMinutes();
  if (options.getValue(OPTIONS.HOUR24)) {
    return Utils.pad(hours) + ':' + Utils.pad(minutes);
  } else {
    var am = true;
    if (hours > 12) { // pm range
      am = false;
      hours -= 12;
    } else if (hours == 0) { // midnight
      hours = 12;
    } else if (hours == 12) { // noon
      am = false;
    }
    var suffix = (am ? 'a' : 'p');
    if (opt_long) {
      suffix = (am ? 'am' : 'pm');
    }
    return hours + ((minutes > 0) ? ':' + Utils.pad(minutes) : '') + suffix;
  }
};

/**
 * Create formatted string from event start and end time.
 * @param {CalendarEvent} event Calendar event object
 * @param {boolean} opt_break Force linebreak after dash.
 * @return {string} Formatted time string
 */
Utils.formatEventTimes = function(event, opt_break) {
  var dateStr = '';
  // If this event has time information set, then we have an event 
  // which spawns several days and should be rendered as allDay event.
  if (event.isAllDay && event.startTime.getTime() % 1000 == 0) {
    var startStr = strings.EVENT_ALLDAY;
    startStr = startStr.replace('[![WEEKDAY]!]',
        WEEK_DAYS_MEDIUM[event.startTime.getDay()]);
    startStr = startStr.replace('[![MONTH]!]',
        MONTHS_SHORT[event.startTime.getMonth()]);
    startStr = startStr.replace('[![DAY]!]',
        event.startTime.getDate());

    var endDate = new Date(event.endTime);
    endDate.setDate(endDate.getDate() - 1);
    var endStr = strings.EVENT_ALLDAY;
    endStr = endStr.replace('[![WEEKDAY]!]',
        WEEK_DAYS_MEDIUM[endDate.getDay()]);
    endStr = endStr.replace('[![MONTH]!]',
        MONTHS_SHORT[endDate.getMonth()]);
    endStr = endStr.replace('[![DAY]!]',
        endDate.getDate());

    dateStr = startStr + ' - ' + endStr;
  } else {
    var startStr = strings.EVENT_DATE_TIME;
    startStr = startStr.replace('[![WEEKDAY]!]',
        WEEK_DAYS_MEDIUM[event.startTime.getDay()]);
    startStr = startStr.replace('[![MONTH]!]',
        MONTHS_SHORT[event.startTime.getMonth()]);
    startStr = startStr.replace('[![DAY]!]',
        event.startTime.getDate());
    startStr = startStr.replace('[![TIME]!]',
        Utils.formatTime(event.startTime, true));

    var endStr = strings.EVENT_DATE_TIME;
    endStr = endStr.replace('[![WEEKDAY]!]',
        WEEK_DAYS_MEDIUM[event.endTime.getDay()]);
    endStr = endStr.replace('[![MONTH]!]',
        MONTHS_SHORT[event.endTime.getMonth()]);
    endStr = endStr.replace('[![DAY]!]',
        event.endTime.getDate());
    endStr = endStr.replace('[![TIME]!]',
        Utils.formatTime(event.endTime, true));

    dateStr = startStr + ' - ' + (opt_break ? '\n' : '') + endStr;
  }
  return dateStr;
};

/**
 * Load remote image and show in img element.
 * @param {string} url URL of the image.
 * @param {Element} img Image element
 */
Utils.getRemoteImg = function(url, img) {
  var req = Utils.createXhr();
  req.open('GET', url, true);
  req.onReadyStateChange = Utils.bind(Utils.getRemoteImgDone, Utils, req, img);
  req.send();
};

/**
 * Callback function for remote image retrieval. Will apply the responseStream
 * to the src of the img element.
 * @param {XMLHttpRequest} req Calling XHR object
 * @param {Element} img Image element
 */
Utils.getRemoteImgDone = function(req, img) {
  if (req.readyState != 4) return;
  if (req.status == 200) {
    img.src = req.responseStream;
  }
};

/**
 * Create XMLHttpRequest object. Will use new beta version if available.
 * @return {XMLHttpRequest} The newly created XMLHttpRequest object.
 */
Utils.createXhr = function() {
  var xhr;

  try {
    xhr = framework.google.betaXmlHttpRequest2();
  } catch (e) {
    xhr = new XMLHttpRequest();
  }

  return xhr;
};

/**
 * Create DOMDocument object.
 * @return {DOMDocument} The newly created DOM document.
 */
Utils.createDOM = function() {
  var doc = new DOMDocument();
  try {
    doc.resolveExternals = false;
    doc.validateOnParse = false;
    doc.setProperty('ProhibitDTD', false);
  } catch (e) {
    // GGL doesn't support this. 
    // Only Windows specific attributes
  }
  return doc;
};

/**
 * Create DOMDocument object from a completed XHR.
 * @return {DOMDocument} A DOMDocument representation of the XML.
 */
Utils.createXmlDocument = function(request) {
  if (Utils.isWindows()) {
    var doc = Utils.createDOM();
    doc.loadXML(request.responseText);

    return doc;
  } else {
    return request.responseXML;
  }
}

Utils.onlineChecker = null;

/**
 * Check if user is currently connected to the internet
 * @return {boolean} True, if connected.
 */
Utils.isOnline = function() {
  if (!Utils.onlineChecker) {
    Utils.onlineChecker = new OnlineChecker();
  }

  return Utils.onlineChecker.isOnline();
};

/**
 * converts all http:// urls to https:// for security
 */
Utils.forceHttpsUrl = function(url) {
  return url.replace(/^http:\/\//i, 'https://');
};

Utils.redirectWithSuperAuth = function(url) {
  // We lied. There is no super auth.
  framework.openUrl(url);
};

/**
 * Detect if we are running on an apple.
 * @return {boolean} True, if running on apple.
 */
Utils.isMac = function() {
  if (!Utils.isLinux() && !Utils.isWindows()) {
    return framework.system.machine.manufacturer == "Apple";
  } else {
    return false;
  }
};

/**
 * Detect if we are running on a Linux machine.
 * @return {boolean} True, if running on Linux.
 */
Utils.isLinux = function() {
  return framework.runtime.osName == 'Linux';
};

/**
 * Detect if we are running on a Linux machine.
 * @return {boolean} True, if running on Linux.
 */
Utils.isWindows = function() {
  return framework.runtime.osName.match(/windows/i) != null;
};

/**
 * Clean XML Data to generate valid xml text
 * @param {string} str Input string
 * @return {string} Cleaned output
 */
Utils.cleanXml = function(str) {
  str = str.replace(/&/g, '&amp;');
  str = str.replace(/&amp;amp;/g, '&amp;');
  str = str.replace(/</g, '&lt;');
  str = str.replace(/>/g, '&gt;');
  str = str.replace(/\'/g, '&apos;');
  str = str.replace(/"/g, '&quot;');
  return str;
};

/**
 * Show loading indicator
 */
Utils.showLoading = function() {
  loadingIndicator.visible = true;
};

/**
 * Hide loading indicator
 */
Utils.hideLoading = function() {
  loadingIndicator.visible = false;
};

/**
 * Check if an upgrade is needed
 * @return {boolean} True, if upgrade is necessary.
 */
Utils.needsUpgrade = function() {
  debug.trace('Version: ' + options.getValue(OPTIONS.UPGRADE));
  var currentVersion = Version.parse(strings.GADGET_VERSION);
  var upgradeVersion = Version.parse(options.getValue(OPTIONS.UPGRADE));
  return upgradeVersion.isGreater(currentVersion);
};
