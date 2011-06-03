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
 * @fileoverview Authenticate user for use of Google Calendar
 */

/**
 * Class for authenticating the user. Login information is provided
 * by the options.
 * @constructor
 */
function Auth() {
  this.authToken_ = options.getValue(OPTIONS.AUTH);
  if (this.authToken_.length == 0) {
    this.authToken_ = null;
  }

  this.retries = 0;
}

/**
 * Response data from login. Used by caller to determine next step if
 * login fails
 */
Auth.prototype.authResponse = null;

/**
 * Constants for the authentication.
 */
Auth.prototype.LOGIN_PAGE = 'https://www.google.com/accounts/ClientLogin';
Auth.prototype.CAPTCHA_PAGE = 'https://www.google.com/accounts/';

/**
 * Error Constants for authentication class
 */
Auth.prototype.OFFLINE = 'Offline';
Auth.prototype.NO_CREDENTIALS = 'NoCredentials';
Auth.prototype.MANDATORY_UPGRADE = 'MandatoryUpgrade';
Auth.prototype.BAD_AUTHENTICATION = 'BadAuthentication';
Auth.prototype.NOT_VERIFIED = 'NotVerified';
Auth.prototype.TERMS_NOT_AGREED = 'TermsNotAgreed';
Auth.prototype.CAPTCHA_REQUIRED = 'CaptchaRequired';
Auth.prototype.UNKNOWN = 'Unknown';
Auth.prototype.ACCOUNT_DELETED = 'AccountDeleted';
Auth.prototype.ACCOUNT_DISABLED = 'AccountDisabled';
Auth.prototype.SERVICE_DISABLED = 'ServiceDisabled';
Auth.prototype.SERVICE_UNAVAILABLE = 'ServiceUnavailable';

/**
 * Callback functions for authentication class.
 */
Auth.prototype.onLoginFailure = null;
Auth.prototype.onLoginSuccess = null;

/**
 * Read authentication token from class.
 * @return {string} AuthToken
 */
Auth.prototype.getAuthToken = function() {
  return this.authToken_;
};

/**
 * Delete current auth token.
 */
Auth.prototype.clearAuthToken = function() {
  this.authToken_ = null;
};

/**
 * Create postdata string for authentication request.
 * @param {string} opt_captchaToken Token of the captcha image.
 * @param {string} opt_captchaText User input text for this captcha.
 * @return {string} post data
 */
Auth.prototype.createPostData = function(opt_captchaToken, opt_captchaText) {
  var postData = {};
  postData.accountType = 'HOSTED_OR_GOOGLE';
  var email = options.getValue(OPTIONS.MAIL);
  if (email.indexOf('@') == -1) {
    email += '@gmail.com';
  }
  postData.Email = email;
  postData.Passwd = options.getValue(OPTIONS.PASSWORD);
  postData.service = 'cl';
  postData.source = GOOGLE_CLIENT;
  if (opt_captchaToken != null) {
    postData.logintoken = opt_captchaToken;
  }
  if (opt_captchaText != null) {
    postData.logincaptcha = opt_captchaText;
  }

  var params = [];
  for (param in postData) {
    params.push(param + '=' + encodeURIComponent(postData[param]));
  }
  return params.join('&');
};

/**
 * Return specific error to calling application if onLoginFailure is set
 * @param {string} errorData Error data for authResponse. Should be one of the
 *     defined error constants in this class
 */
Auth.prototype.returnError = function(errorData) {
  if (typeof(this.onLoginFailure) == 'function') {
    this.authResponse = {'Error': errorData};
    this.onLoginFailure(this);
  }
};

/**
 * Login to user account.
 * @param {string} opt_captchaToken Token of the captcha image.
 * @param {string} opt_captchaText User input text for this captcha.
 */
Auth.prototype.login = function(opt_captchaToken, opt_captchaText) {
  if (!Utils.isOnline()) {
    this.returnError(this.OFFLINE);
    return;
  }

  if (Utils.needsUpgrade()) {
    this.returnError(this.MANDATORY_UPGRADE);
    return;
  }

  if (!this.authToken_ &&
      options.getValue(OPTIONS.MAIL) == '' ||
      options.getValue(OPTIONS.PASSWORD) == '') {
    this.returnError(this.NO_CREDENTIALS);
    return;
  }

  var postString = this.createPostData(opt_captchaToken, opt_captchaText);
  //debug.trace('PostString: ' +
  //   postString.replace(options.getValue(OPTIONS.PASSWORD), '****'));
  //debug.trace('Login URL: ' + this.LOGIN_PAGE);

  var req = Utils.createXhr();
  req.open('POST', this.LOGIN_PAGE, true);
  req.onReadyStateChange = Utils.bind(this.onLoginDone, this, req);
  req.setRequestHeader('Content-type',
      'application/x-www-form-urlencoded');
  req.send(postString);
};

/**
 * Callback function for the XMLHttpRequest object.
 * @param {XMLHttpRequest} req XMLHttpRequest object which calls this function.
 */
Auth.prototype.onLoginDone = function(req) {
  if (!req || req.readyState != 4) {  // completed OK?
    return;
  }

  // Convert response into easy to use data structure.
  var responseLines = req.responseText.split('\n');
  var responseData = {};
  for (var i = 0; i < responseLines.length; ++i) {
    var split = responseLines[i].indexOf('=');
    var key = responseLines[i].substr(0, split);
    var value = responseLines[i].substr(split + 1);
    responseData[key] = value;
  }
  this.authResponse = responseData;

  if (req.status == 200) {
    debug.trace('User successfully verified.');
    // Store authentication data
    this.authToken_ = responseData.Auth;

    if (typeof(this.onLoginSuccess) == 'function') {
      this.onLoginSuccess(this);
    }

    if (!options.getValue(OPTIONS.REMEMBER)) {
      options.putValue(OPTIONS.PASSWORD, '');
    } else {
      options.encryptValue(OPTIONS.AUTH);
      options.putValue(OPTIONS.AUTH, this.authToken_);
    }
  } else if (req.status == 403) {
    if (this.authResponse.Error != this.CAPTCHA_REQUIRED) {
      options.putValue(OPTIONS.PASSWORD, '');
    }

    if (typeof(this.onLoginFailure) == 'function') {
      this.onLoginFailure(this);
    }
  } else if (req.status > 12000) {
    this.returnError(this.OFFLINE);
  } else {
    if (typeof(this.onLoginFailure) == 'function') {
      this.onLoginFailure(this);
    }
  }
};
