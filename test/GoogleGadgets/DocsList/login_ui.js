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

function LoginUi(mainDiv) {
  this.onLogin = null;

  this.mainDiv = mainDiv;

  this.userField = child(this.mainDiv, 'user');
  this.userField.onkeypress = this.onUserKeyPress.bind(this);

  this.passwordField = child(this.mainDiv, 'pass');
  this.passwordField.onkeypress = this.onPasswordKeyPress.bind(this);

  this.loginButton = child(this.mainDiv, 'login');
  this.loginButton.onkeypress = this.onLoginKeyPress.bind(this);
  this.loginButton.onfocusin = this.onLoginFocus.bind(this, true);
  this.loginButton.onfocusout = this.onLoginFocus.bind(this, false);
  this.loginButton.onclick = this.onLoginPress.bind(this, false);

  this.rememberCheck = child(this.mainDiv, 'remember');
  this.rememberCheck.onchange = this.onRememberFocus.bind(this, true);
  this.rememberCheck.onkeypress = this.onRememberKeyPress.bind(this);
  this.rememberCheck.onfocusin = this.onRememberFocus.bind(this, true);
  this.rememberCheck.onfocusout = this.onRememberFocus.bind(this, false);

  this.rememberCheckFocus = child(this.mainDiv, 'rememberFocus');

  this.reset();
}

LoginUi.prototype.disable = function() {
  this.userField.enabled = false;
  this.passwordField.enabled = false;
  this.loginButton.enabled = false;
  this.rememberCheck.enabled = false;
};

LoginUi.prototype.enable = function() {
  this.userField.enabled = true;
  this.passwordField.enabled = true;
  this.loginButton.enabled = true;
  this.rememberCheck.enabled = true;
};

LoginUi.prototype.resize = function(width, height) {
  this.mainDiv.width = width;
  this.mainDiv.height = height;
  this.userField.width = this.passwordField.width = this.mainDiv.width -
      this.userField.x - this.userField.x;
  this.loginButton.x = this.mainDiv.width - this.loginButton.width;
  this.loginButton.y = this.mainDiv.height - (this.loginButton.height + 10);
};

LoginUi.prototype.hide = function() {
  this.mainDiv.visible = false;
};

LoginUi.prototype.show = function() {
  this.mainDiv.visible = true;
};

LoginUi.prototype.login = function() {
  if (this.onLogin) {
    this.onLogin(this.userField.value, this.passwordField.value,
        this.rememberCheck.value);
  }
};

LoginUi.prototype.reset = function() {
  this.enable();
  this.userField.value = '';
  this.passwordField.value = '';
  this.rememberCheck.value = false;
  this.rememberCheckFocus.visible = false;
};

LoginUi.prototype.onUserKeyPress = function() {
  if (event.keycode == KEYS.ENTER) {
    if (!this.passwordField.value) {
      this.passwordField.focus();
    } else {
      this.login();
    }
    event.returnValue = false;
  }
};

LoginUi.prototype.onPasswordKeyPress = function() {
  if (event.keycode == KEYS.ENTER) {
    if (!this.userField.value) {
      this.userField.focus();
    } else {
      this.login();
    }
    event.returnValue = false;
  }
};

LoginUi.prototype.onRememberKeyPress = function() {
  if (event.keycode == KEYS.ENTER ||
      event.keycode == KEYS.SPACE) {
    this.rememberCheck.value = !this.rememberCheck.value;
    this.onRememberFocus(true);
  }
};

LoginUi.prototype.onLoginKeyPress = function() {
  if (event.keycode == KEYS.ENTER ||
      event.keycode == KEYS.SPACE) {
    this.onLoginPress();
  }
};

LoginUi.prototype.onLoginPress = function() {
  if (!this.userField.value) {
    this.userField.focus();
  } else {
    this.login();
  }
};

LoginUi.prototype.onRememberFocus = function(got) {
  this.rememberCheckFocus.visible = got;
};

LoginUi.prototype.onLoginFocus = function(got) {
  this.loginButton.image = got ?
      'images/action_hover.png' :
      'images/action_default.png';
};
