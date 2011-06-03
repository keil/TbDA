// Copyright 2007 Google Inc.
// All Rights Reserved.

// @fileoverview Functions for adding and handling an error message UI to a view

var g_errorMessage = new ErrorMessage();



function ErrorMessage() {
  this.removeTimer = null;

  this.messageAndErrorDiv = view.appendElement(
      '<div name="messageAndErrorDiv" x="10" height="44" enabled="true" />');
  this.messageAndErrorDiv.onclick = (function(refThis) {
        return function() {
          refThis.removeMessage();
        };
      })(this);
  this.messageAndErrorDiv.appendElement('<div name="messageAndErrorLeft" ' +
      'x="0" y="0" width="7" height="44" background="' +
      IMAGE_PATHS.ERROR_LEFT + '" />');
  this.messageAndErrorCenter = this.messageAndErrorDiv.appendElement(
      '<div name="messageAndErrorCenter" x="7" y="0" height="44" background="' +
      IMAGE_PATHS.ERROR_CENTER + '" />');
  this.messageAndErrorRight = this.messageAndErrorDiv.appendElement(
      '<div name="messageAndErrorRight" y="0" width="7" height="44" ' +
      'background="' + IMAGE_PATHS.ERROR_RIGHT + '" />');
  this.messageAndErrorLabel = this.messageAndErrorCenter.appendElement(
      '<label name="messageAndErrorLabel" ' +
      'x="0" y="0" width="100%" height="100%" align="center" valign="middle" ' +
      'font="arial" bold="true" size="7" trimming="character-ellipsis" ' +
      'wordWrap="true" />');
}



ErrorMessage.prototype.displayMessage = function(message) {
  try {
    this.messageAndErrorDiv.y =
        (view.height - this.messageAndErrorDiv.height) / 2;
    this.messageAndErrorDiv.width =
        view.width - 2 * this.messageAndErrorDiv.x - 4;
    this.messageAndErrorRight.x = this.messageAndErrorDiv.width -
        this.messageAndErrorRight.width;
    this.messageAndErrorCenter.width = this.messageAndErrorRight.x -
        this.messageAndErrorCenter.x;

    this.messageAndErrorLabel.innerText = message;
    this.messageAndErrorDiv.visible = true;

    if (this.removeTimer) {
      view.clearTimeout(this.removeTimer);
    }
    this.removeTimer = view.setTimeout((function(refThis) {
      return function() {
        refThis.removeMessage();
      };
    })(this), UIDATA.ERROR_MESSAGE_MAX_DISPLAY_TIME);

    this.messageAndErrorDiv.focus();
  } catch(e) {}
};



ErrorMessage.prototype.removeMessage = function() {
  try {
    this.messageAndErrorDiv.visible = false;
  } catch(e) {}
};

