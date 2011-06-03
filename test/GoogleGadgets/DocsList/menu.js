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

function DocumentMenu(mainDiv) {
  this.onSelected = null;

  this.mainDiv = mainDiv;
  this.list = child(this.mainDiv, 'newDocumentOptions');

  this.formItem = child(this.list, 'newDocumentForm');
  this.presentationItem = child(this.list, 'newDocumentPresentation');
  this.spreadsheetItem = child(this.list, 'newDocumentSpreadsheet');
  this.documentItem = child(this.list, 'newDocumentDocument');

  this.formItem.onclick = this.newDocument.bind(this,
      Document.FORM);
  this.presentationItem.onclick = this.newDocument.bind(this,
      Document.PRESENTATION);
  this.spreadsheetItem.onclick = this.newDocument.bind(this,
      Document.SPREADSHEET);
  this.documentItem.onclick = this.newDocument.bind(this,
      Document.DOCUMENT);
}

DocumentMenu.prototype.isOpen = function() {
  return this.mainDiv.visible;
};

DocumentMenu.prototype.toggle = function() {
  if (this.isOpen()) {
    this.close();
  } else {
    this.open();
  }
};

DocumentMenu.prototype.open = function() {
  this.mainDiv.visible = true;
};

DocumentMenu.prototype.close = function() {
  this.mainDiv.visible = false;
};

DocumentMenu.prototype.newDocument = function(type) {
  this.close();

  if (this.onSelected) {
    this.onSelected(type);
  }
};


function ShowMenu(mainDiv) {
  this.onSelected = null;

  this.mainDiv = mainDiv;
  this.list = child(this.mainDiv, 'items');

  this.allItem = child(this.list, 'showAll');
  this.ownedItem = child(this.list, 'showOwned');
  this.starredItem = child(this.list, 'showStarred');

  this.allItem.onclick = this.handleClick.bind(this,
      Main.FILTER_ALL);
  this.ownedItem.onclick = this.handleClick.bind(this,
      Main.FILTER_OWNED);
  this.starredItem.onclick = this.handleClick.bind(this,
      Main.FILTER_STARRED);
}

ShowMenu.prototype.isOpen = function() {
  return this.mainDiv.visible;
};

ShowMenu.prototype.toggle = function(filter) {
  if (this.isOpen()) {
    this.close();
  } else {
    this.open(filter);
  }
};

ShowMenu.prototype.open = function(filter) {
  child(this.allItem, 'check').visible = false;
  child(this.ownedItem, 'check').visible = false;
  child(this.starredItem, 'check').visible = false;

  if (filter == Main.FILTER_OWNED) {
    child(this.ownedItem, 'check').visible = true;
  } else if (filter == Main.FILTER_STARRED) {
    child(this.starredItem, 'check').visible = true;
  } else {
    child(this.allItem, 'check').visible = true;
  }

  this.mainDiv.visible = true;
};

ShowMenu.prototype.close = function() {
  this.mainDiv.visible = false;
};

ShowMenu.prototype.handleClick = function(type) {
  this.close();

  if (this.onSelected) {
    this.onSelected(type);
  }
};
