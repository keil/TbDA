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

/**
 * @fileoverview Utility functions for the you tube gadget.
 */

/**
 * Checks if the word passed in exists in the array
 * @param {String} searchObject The word to search for
 * @param {Function} opt_equalityComparator Function that accepts 2 objects to
 *     compare
 * @return {Bool} true if the word is in the array, false otherwise
 */
Array.prototype.contains = function(searchObject, opt_equalityComparator) {
  return (this.indexOf(searchObject, opt_equalityComparator) >= 0);
};

/**
 * Returns the index of the searchObject in the array
 * @param {String} searchObject The word to search for
 * @param {Function} opt_equalityComparator Function that accepts 2 objects to
 *     compare
 * @return {Number} -1 if not found.
 */
Array.prototype.indexOf = function(searchObject, opt_equalityComparator) {
  for (var i = 0; i < this.length; ++i) {
    if (opt_equalityComparator) {
      if (opt_equalityComparator(this[i], searchObject)) {
        return i;
      }
    } else if (this[i] == searchObject) {
      return i;
    }
  }

  return -1;
};

/**
 * Randomly shuffles an array using the Fisher-Yates algorithm
 */
Array.prototype.shuffle = function() {
  var i = this.length;
  if (i === 0) {
    return;
  }

  while (--i) {
    var j = Math.floor(Math.random() * i);
    assert(this[i] && this[j]);
    var temp1 = this[i];
    this[i] = this[j];
    this[j] = temp1;
  }
};

Array.prototype.map = function(f) {
  var res = new Array(this.length);
  for (var i = 0; i < this.length; ++i) {
    res[i] = f(this[i]);
  }
  return res;
};

Array.prototype.every = function(f) {
  for (var i = 0; i < this.length; ++i) {
    if (!f(this[i])) {
      return false;
    }
  }
  return true;
};

/**
 * Super basic assertion package.
 * @param {Boolean} expression An expression that evaluates to a boolean
 * @param {String{ opt_message Optional message string to display
 */
function assert(expression, opt_message) {
  if (!expression) {
    throw 0;
  }
}

/**
 * Random utility functions used in the gadget
 * @constructor
 */
function Util() {}

/**
 * Returns the data attributed to the node's named child.
 *
 * @param {Node} node The node to extract data from.
 * @param {String} element The name of the node's child.
 * @return {String} Extracted data.
 */
Util.getData = function(node, element) {
  var returnVal = undefined;
  if (!node) {
    return returnVal;
  }

  try {
    returnVal = node.getElementsByTagName(element)[0].text;
  } catch(e) {
    debug.error('ytparse(' + node.nodeName + ':' + element + '): ' + e.message);
  }

  return returnVal;
};

/**
 * Returns the attribute for the node's named child.
 *
 * @param {Node} node The node to extract data from.
 * @param {String} element The name of the node's child.
 * @param {String} attribute The attribute to extract
 * @return {String} Extracted data.
 */
Util.getAttribute = function(node, element, attribute) {
  var returnVal = undefined;
  if (!node) {
    return returnVal;
  }

  try {
    returnVal = node.getElementsByTagName(element)[0].getAttribute(attribute);
  } catch(e) {
    debug.error('ytparse(' + node.nodeName + ':' + element + ':' + attribute +
                '): ' + e.message);
  }

  return returnVal;
};

/**
 * Sanitizes the string to be valid xml. We use this instead of
 * escape/escapeURI/escapeURIComponent because all three fails to handle '&',
 * which is an invalid XML character.
 * @param {String} unsanitized String to be sanitized
 * @return {String} New string that has been sanitized.
 */
Util.sanitize = function(unsanitized) {
  unsanitized = unsanitized.replace(/</g, '&lt;');
  unsanitized = unsanitized.replace(/>/g, '&gt;');
  unsanitized = unsanitized.replace(/&/g, '&amp;');
  return unsanitized;
};

/**
 * Verifies that the url is valid before inserting it as a src in html. Used to
 * prevent against man-in-the-middle attacks in our generated html.
 * @param {String} url Url to verify
 * @return {Boolean} True if the url is valid
 */
Util.isValidUrl = function(url) {
  if (!url || typeof(url) != 'string') {
    return false;
  }

  if (url.indexOf('"') >= 0) {
    return false;
  }

  if (url.indexOf('http://') < 0) {
    return false;
  }

  return true;
};

/**
 * Helper function to return the current time in ms since the epoch
 * @return {Number}
 */
Util.currentTime = function() {
  return (new Date()).getTime();
};

/**
 * Taken from Javascript: The Definitive Guide, in the description of
 * Object.constructor.
 * @param {Object} x Any object
 * @return {Bool} True if the object is an array
 */
Util.isArray = function(x) {
  return (typeof(x) == 'object') && (x.constructor == Array);
};

Util.trimWhitespace = function(str) {
  var trimmed = str.replace(/^\s*|\s*$/g, '');
  trimmed = trimmed.replace(/\s+/g, ' ');
  return trimmed;
};

/**
 * Detect if we are running on an apple.
 * @return {boolean} True, if running on apple.
 */
Util.isMac = function() {
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
Util.isLinux = function() {
  return framework.runtime.osName == 'Linux';
};

/**
 * Detect if we are running on a Windows machine.
 * @return {boolean} True, if running on Windows.
 */
Util.isWindows = function() {
  return framework.runtime.osName.match(/windows/i) !== null;
};

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

