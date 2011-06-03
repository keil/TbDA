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

ErrorMessage.MESSAGE_TIMEOUT = 3000;

function ErrorMessage() {
  this.removeTimer = null;

  this.messageAndErrorDiv = view.appendElement(
      '<div name="messageAndErrorDiv" x="10" height="44" enabled="true" />');
  this.messageAndErrorDiv.onfocusout = this.remove.bind(this);
  this.messageAndErrorDiv.appendElement('<div name="messageAndErrorLeft" ' +
      'x="0" y="0" width="7" height="44" background="images/error_left.png" />');
  this.messageAndErrorCenter = this.messageAndErrorDiv.appendElement(
      '<div name="messageAndErrorCenter" x="7" y="0" height="44" background="images/error_center.png" />');
  this.messageAndErrorRight = this.messageAndErrorDiv.appendElement(
      '<div name="messageAndErrorRight" y="0" width="7" height="44" ' +
      'background="images/error_right.png" />');
  this.messageAndErrorLabel = this.messageAndErrorCenter.appendElement(
      '<label name="messageAndErrorLabel" ' +
      'x="0" y="0" width="100%" height="100%" align="center" valign="middle" ' +
      'font="helvetica" bold="true" size="7" trimming="character-ellipsis" ' +
      'wordWrap="true" />');
  this.messageAndErrorDiv.onclick = this.remove.bind(this);
}

ErrorMessage.prototype.display = function(message) {
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
  this.removeTimer = view.setTimeout(this.remove.bind(this),
      ErrorMessage.MESSAGE_TIMEOUT);

  this.messageAndErrorDiv.focus();
};

ErrorMessage.prototype.remove = function() {
  this.messageAndErrorDiv.visible = false;
};
