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

Auth.LOGIN_ERRORS = {
  'BadAuthentication': strings.ERROR_BAD_AUTH,
  'NotVerified': strings.ERROR_NOT_VERIFIED,
  'TermsNotAgreed': strings.ERROR_TERMS,
  'CaptchaRequired': strings.ERROR_CAPTCHA,
  'Unknown': strings.ERROR_UNKNOWN,
  'AccountDeleted': strings.ERROR_ACCOUNT_DELETED,
  'AccountDisabled': strings.ERROR_ACCOUNT_DISABLED,
  'ServiceDisabled': strings.ERROR_SERVICE_DISABLED,
  'ServiceUnavailable': strings.ERROR_SERVICE_UNAVAILABLE };

function Auth() {
  options.putDefaultValue(Auth.OPTIONS_KEY_TOKEN, '');
  options.putDefaultValue(Auth.OPTIONS_KEY_USERNAME, '');

  this.token = this.getStoredToken();
  this.username = this.getStoredUsername();
  this.appsDomain = '';
}

Auth.OPTIONS_KEY_TOKEN = 'token';
Auth.OPTIONS_KEY_USERNAME = 'username';

Auth.prototype.getStoredToken = function() {
  return options.getValue(Auth.OPTIONS_KEY_TOKEN);
};

Auth.prototype.setStoredToken = function(token) {
  options.putValue(Auth.OPTIONS_KEY_TOKEN, token);
  options.encryptValue(Auth.OPTIONS_KEY_TOKEN);
};

Auth.prototype.getStoredUsername = function() {
  return options.getValue(Auth.OPTIONS_KEY_USERNAME);
};

Auth.prototype.setStoredUsername = function(username) {
  options.putValue(Auth.OPTIONS_KEY_USERNAME, username);
  options.encryptValue(Auth.OPTIONS_KEY_USERNAME);
};

Auth.DEFAULT_DOMAIN = 'gmail.com';
Auth.URL = 'https://www.google.com/accounts/ClientLogin';
Auth.SERVICE = 'writely';
Auth.TYPE = 'HOSTED_OR_GOOGLE';

Auth.prototype.login = function(user, pass, isRemember, onSuccess, onFailure) {
  var passValue = pass;
  var userValue = user;

  if (userValue.indexOf('@') == -1) {
    userValue = userValue + '@' + Auth.DEFAULT_DOMAIN;
  } else {
    this.appsDomain = userValue.substr(userValue.indexOf('@') + 1);
  }

  var defaultDomainIndex = userValue.indexOf('@' + Auth.DEFAULT_DOMAIN);
  this.username = defaultDomainIndex == -1 ?
      userValue :
      userValue.substr(0, defaultDomainIndex);

  var params = { 'Email': userValue,
                 'Passwd': passValue,
                 'service': Auth.SERVICE,
                 'source': REPORTED_CLIENT_NAME,
                 'accountType': Auth.TYPE };
  var data = buildQueryString(params);

  g_httpRequest.connect(Auth.URL, data,
      this.onLoginSuccess.bind(this, user, isRemember, onSuccess),
      this.onLoginError.bind(this, onFailure));
};

Auth.prototype.clear = function() {
  this.username = '';
  this.token = '';
  this.appsDomain = '';
  this.setStoredToken('');
  this.setStoredUsername('');
};

Auth.prototype.parseResponse = function(response) {
  var responseLines = response.split('\n');
  var responseData = {};
  for (var i = 0; i < responseLines.length; ++i) {
    var split = responseLines[i].indexOf('=');
    var key = responseLines[i].substr(0, split);
    var value = responseLines[i].substr(split + 1);
    responseData[key] = value;
  }

  return responseData;
};

Auth.prototype.onLoginSuccess = function(response,
    user, isRemember, onSuccess) {
  var responseData = this.parseResponse(response);

  this.token = responseData['Auth'];

  if (isRemember) {
    this.setStoredToken(this.token);
    this.setStoredUsername(user);
  }

  onSuccess();
};

Auth.prototype.onLoginError = function(status, response, onFailure) {
  this.clear();

  var error = 'Unknown';

  if (status == 403) {
    var responseData = this.parseResponse(response);
    error = responseData['Error'] || error;
  } else if (status === 0) {
    onFailure(error, strings.ERROR_SERVER_OR_NETWORK);
    return;
  }

  onFailure(error, Auth.LOGIN_ERRORS[error]);
};

Auth.prototype.hasCredentials = function() {
  return this.token && this.username;
};
