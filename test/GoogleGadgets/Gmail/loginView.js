// Copyright 2007 Google Inc.
// All Rights Reserved.

// @fileoverview Functions for handling the login view of the gadget

function LoginView(gadget) {
  this.gadget = gadget;
}



/**
 * This function is called onkeypress of the username editbox. accepts tab and
 * enter key.
 */
LoginView.prototype.onUsernameKeyPress = function() {
  if (event.keycode == KEYS.ENTER) {
    if (pass.value === '') {
      pass.focus();
    } else {
      this.gadget.login();
    }
    event.returnValue = false;
  }
};



/**
 * This function is called onkeydown of the password editbox. accepts enter key.
 */
LoginView.prototype.onPasswordKeyPress = function() {
  if (event.keycode == KEYS.ENTER) {
    if (user.value.length === 0) {
      user.focus(); // Put focus on user field.
    } else {
      this.gadget.login();
    }
    event.returnValue = false;
  }
};



/**
 * This function is called onkeydown of the remember checkbox.
 */
LoginView.prototype.onRememberKeyPress = function() {
  if (event.keycode == KEYS.ENTER ||
      event.keycode == KEYS.SPACE) {
    remember.value = !remember.value;
    this.onRememberFocus(true);
  }
};



LoginView.prototype.onLoginKeyPress = function() {
  if (event.keycode == KEYS.ENTER ||
      event.keycode == KEYS.SPACE) {
    // Put focus on user field if its empty.
    if (user.value.length === 0) {
      user.focus();
    } else {
      this.gadget.login();
    }
  }
};



/**
 * This function is called onfocusin/out of the search editbox.
 */
LoginView.prototype.onUsernameFocus = function(got) {
  this.gadget.keyboardShortcutsEnabled = !got;
};



/**
 * This function is called onfocusin/out of the search editbox.
 */
LoginView.prototype.onPasswordFocus = function(got) {
  this.gadget.keyboardShortcutsEnabled = !got;
};



/*
 * This function is called when the remember checkbox gets or looses focus
 */
LoginView.prototype.onRememberFocus = function(got) {
  this.gadget.keyboardShortcutsEnabled = !got;
  rememberFocus.visible = got;
};



/*
 * This function is called when the action combo gets or looses focus
 */
LoginView.prototype.onLoginFocus = function(got) {
  this.gadget.keyboardShortcutsEnabled = !got;
  login.image = got ? 'images/action_hover.png' : 'images/action_default.png';
};
