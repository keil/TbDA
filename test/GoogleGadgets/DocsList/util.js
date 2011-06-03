/*
Copyright (C) 2008 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

var KEYS = {
  TAB: 9,
  ENTER: 13,
  ESCAPE: 27,
  SPACE: 32,
  UP: 38,
  DOWN: 40,
  PAGE_UP: 33,
  PAGE_DOWN: 34,
  HOME: 36,
  END: 35 };

function createXhr() {
  var xhr;

  try {
    xhr = framework.google.betaXmlHttpRequest2();
  } catch (e) {
    xhr = new XMLHttpRequest();
  }

  return xhr;
}

function createDomDocument() {
  var doc = new DOMDocument();

  try {
    doc.resolveExternals = false;
    doc.validateOnParse = false;
    doc.setProperty('ProhibitDTD', false);
  } catch(e) {
    debug.warning('Could not set MS specific properties.');
  }

  return doc;
}

Function.prototype.bind = function(context) {
  var __method = this;
  var __arguments = [];
  for (var n = 1; n < arguments.length; ++n) {
    __arguments.push(arguments[n]);
  }

  return function() {
    var myargs = [];
    for (var m = 0; m < arguments.length; ++m) {
      myargs.push(arguments[m]);
    }

    return __method.apply(context, myargs.concat(__arguments));
  };
};

function buildQueryString(obj) {
  var parts = [];
  for (var key in obj) {
    var type = typeof obj[key];
    if (type == 'boolean' || type == 'number' || type == 'string') {
      parts.push(key + '=' + encodeURIComponent(obj[key].toString()));
    }
  }

  return parts.join('&');
}

function child(element, childName) {
  return element.children.item(childName);
}

function trim(str) {
  return str.replace(/^\s*|\s*$/g,'');
}

// Converts an RFC3339 date time into local time JS Date.
// Returns null on error.
function parseRFC3339(date) {
  var newDate = new Date();

  // Parse date portion.
  // first 10 chars 'XXXX-XX-XX'
  var datePart = date.substring(0, 10);
  datePart = datePart.split("-");

  if (datePart.length != 3) {
    return null;
  }

  newDate.setUTCFullYear(datePart[0]);
  newDate.setUTCMonth(datePart[1] - 1);
  newDate.setUTCDate(datePart[2]);

  // Check for 'T'.
  var tPart = date.substring(10, 11);

  if (tPart != 'T' && tPart != 't') {
    return null;
  }

  // Parse time portion.
  // 'XX:XX:XX'
  var timePart = date.substring(11, 19);
  timePart = timePart.split(":");

  if (timePart.length != 3) {
    return null;
  }

  newDate.setUTCHours(timePart[0]);
  newDate.setUTCMinutes(timePart[1]);
  newDate.setUTCSeconds(timePart[2]);
  newDate.setUTCMilliseconds(0);

  var index = 19;
  var dateLen = date.length;

  if (date.charAt(index) == '.') {
    // Consume fractional sec.
    do {
      ++index;
    } while (date.charAt(index) >= '0' &&
             date.charAt(index) <= '9' &&
             index < date.length);
  }

  if (index >= date.length) {
    // No zone to parse;
    return newDate;
  }

  if (date.charAt(index) == 'Z') {
    // No offset.
    return newDate;
  }

  var offsetSign = date.charAt(index);

  if (offsetSign != '+' && offsetSign != '-') {
    return null;
  }

  ++index;

  // Parse offset.
  var offsetPart = date.substring(index, index + 5);

  if (offsetPart.length == 4) {
    // Assume colon-less format.
    var tempOffsetPart = [];
    tempOffsetPart[0] = offsetPart.substr(0, 2);
    tempOffsetPart[1] = offsetPart.substr(2, 2);
    offsetPart = tempOffsetPart;
  } else {
    offsetPart = offsetPart.split(":");
  }

  if (offsetPart.length != 2) {
    return null;
  }

  var offsetSeconds = (Number(offsetPart[0]) * 60) + Number(offsetPart[1]);
  var offsetMs = offsetSeconds * 60 * 1000;

  // Adjust for offset.
  if (offsetSign == '+') {
    newDate.setTime(newDate.getTime() - offsetMs);
  } else {
    newDate.setTime(newDate.getTime() + offsetMs);
  }

  return newDate;
}

// Calculate the width of a label
function labelCalcWidth(ele) {
  try {
    var edit = labelCalcHelper;
  } catch(e) {
    var edit = view.appendElement('<edit name="labelCalcHelper" />');
  }

  edit.visible = false;
  edit.y = 2000;
  edit.x = 0;
  edit.width = 1000;
  edit.height = 30;
  edit.value = ele.innerText;
  edit.font = ele.font;
  edit.size = ele.size;
  edit.bold = ele.bold;
  edit.italic = ele.italic;
  edit.underline = ele.underline;
  var idealRect = edit.idealBoundingRect;
  edit.width = idealRect.width;
  edit.height = idealRect.height;
  return idealRect.width;
}

function Utils() {
}

/**
 * Detect if we are running on an apple.
 * @return {boolean} True, if running on apple.
 */
Utils.isMac = function() {
  if (!Utils.isLinux() && !Utils.isWindows()) {
    return framework.system.machine.manufacturer == 'Apple';
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
 * Detect if we are running on a Windows machine.
 * @return {boolean} True, if running on Windows.
 */
Utils.isWindows = function() {
  return framework.runtime.osName.match(/windows/i) !== null;
};

Utils.onlineChecker = null;

Utils.isOnline = function() {
  if (!Utils.onlineChecker) {
    Utils.onlineChecker = new OnlineChecker();
  }

  return Utils.onlineChecker.isOnline();
};
