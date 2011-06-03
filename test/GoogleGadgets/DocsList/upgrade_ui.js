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

function UpgradeUi(mainDiv) {
  this.mainDiv = mainDiv;

  this.reasonLabel = child(this.mainDiv, 'upgradeReason');
  this.downloadLink = child(this.mainDiv, 'upgradeDownloadUrl');
  this.infoLink = child(this.mainDiv, 'upgradeInfoUrl');
}

UpgradeUi.prototype.resize = function(width, height) {
  this.mainDiv.width = width;
  this.mainDiv.height = height;
};

UpgradeUi.prototype.hide = function() {
  this.mainDiv.visible = false;
};

UpgradeUi.prototype.show = function() {
  this.mainDiv.visible = true;
};
