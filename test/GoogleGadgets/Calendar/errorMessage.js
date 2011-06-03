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
 * @fileoverview Functions for adding and handling an error message UI to a view
 */

/**
 * Class to display error message
 * @constructor
 */
function ErrorMessage() {
  this.removeTimer = null;

  this.errorDiv = view.appendElement('<div />');
  this.errorDiv.x = 5;
  this.errorDiv.height = 80;
  this.errorDiv.enabled = true;
  this.errorDiv.visible = false;
  //this.errorDiv.onfocusout = Utils.bind(this.removeMessage, this);

  this.errorLeft = this.errorDiv.appendElement('<div />');
  this.errorLeft.x = 0;
  this.errorLeft.y = 0;
  this.errorLeft.width = 7;
  this.errorLeft.height = 80;
  this.errorLeft.background = 'images/error_left.png';

  this.errorCenter = this.errorDiv.appendElement('<div />');
  this.errorCenter.x = 7;
  this.errorCenter.y = 0;
  this.errorCenter.height = 80;
  this.errorCenter.background = 'images/error_center.png';

  this.errorRight = this.errorDiv.appendElement('<div />');
  this.errorRight.y = 0;
  this.errorRight.width = 7;
  this.errorRight.height = 80;
  this.errorRight.background = 'images/error_right.png';

  this.errorLabel = this.errorCenter.appendElement('<label />');
  this.errorLabel.x = 0;
  this.errorLabel.y = 0;
  this.errorLabel.width = '100%';
  this.errorLabel.height = '100%';
  this.errorLabel.align = 'center';
  this.errorLabel.valign = 'middle';
  this.errorLabel.font = 'arial';
  this.errorLabel.bold = true;
  this.errorLabel.size = 7;
  this.errorLabel.trimming = 'character-ellipsis';
  this.errorLabel.wordwrap = true;

  this.errorDiv.onclick = Utils.bind(this.removeMessage, this);
}

/**
 * ErrorMessage timeout in ms
 */
ErrorMessage.prototype.ERROR_MESSAGE_MAX_DISPLAY_TIME = 5000;

/**
 * Display error message
 * @param {string} message error message
 */
ErrorMessage.prototype.displayMessage = function(message) {
  try {
    this.errorDiv.y =
        (view.height - this.errorDiv.height) / 2;
    this.errorDiv.width =
        view.width - 2 * this.errorDiv.x;
    this.errorRight.x = this.errorDiv.width -
        this.errorRight.width;
    this.errorCenter.width = this.errorRight.x -
        this.errorCenter.x;

    this.errorLabel.innerText = message;
    this.errorDiv.visible = true;

    if (this.removeTimer) {
      view.clearTimeout(this.removeTimer);
    }
    this.removeTimer = view.setTimeout(Utils.bind(this.removeMessage, this),
        this.ERROR_MESSAGE_MAX_DISPLAY_TIME);

    this.errorDiv.focus();
  } catch(e) {
    debug.error('DisplayMessage failed: ' + e.Message);
  }
};

/**
 * Hide the error message
 */
ErrorMessage.prototype.removeMessage = function() {
  try {
    this.errorDiv.visible = false;
  } catch(e) {
    debug.error('Cannot hide error message: ' + e.Message);
  }
};

