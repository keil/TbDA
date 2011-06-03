// Copyright 2008 Google Inc.
// All Rights Reserved.

// @fileoverview Utilities.


/**
 * Do nothing function.
 */
var nullFunction = function() {};

/**
 * Returns a function that calls the given method on the given instance.
 */
function bind(self, method, var_args) {
  var args = Array.prototype.slice.call(arguments, 2);

  return function(var_args) {
    return method.apply(self,
        args.concat(Array.prototype.slice.call(arguments)));
  };
}

/**
 * Encodes characters that may be used to inject arbritrary XML.
 */
function sanitizeXml(string) {
  string += '';
  string = string.replace(/&/g, "&amp;");
  string = string.replace(/</g, "&lt;");
  string = string.replace(/>/g, "&gt;");
  string = string.replace(/\u0027/g, "&apos;");
  string = string.replace(/\u0022/g, "&quot;");
  return string;
}

var Utils = {};

Utils.getMessage = function(key) {
  return strings[key];
};

Utils.escapeRegexp = function(str) {
  var re = new RegExp("([\(\)\.\*\\\?\+\[\^\$])", "gi");
  return str.replace(re, "\\$1");
};

Utils.isGmailAccount = function(email) {
  var domain = Utils.getDomain(email);

  return !domain || domain == 'gmail.com';
};

Utils.getDomain = function(email) {
  if (email.indexOf('@') == -1) {
    return '';
  }

  return email.substr(email.indexOf('@') + 1);
};


Utils.isWindows = function() {
  return framework.runtime.osName.match(/windows/i) !== null;
};

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
