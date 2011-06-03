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

/*
SortUi.DATE_OPTION = 'date';
SortUi.NAME_OPTION = 'name';
SortUi.SORT_TYPE_OPTION = 'sort_type';
*/

function SortUi(mainDiv) {
  /*
  options.putDefaultValue(SortUi.SORT_TYPE_OPTION, SortUi.DATE_OPTION);
  this.active = options.getValue(SortUi.SORT_TYPE_OPTION);
  this.onChange = null;
  */

  this.mainDiv = mainDiv;
  this.nameColumn = child(this.mainDiv, 'sortOptionsName');
  this.nameArrow = child(this.nameColumn, 'sortOptionsNameArrow');
  this.nameDateDivider = child(this.mainDiv, 'sortOptionsNameDateDivider');
  this.dateNameDivider = child(this.mainDiv, 'sortOptionsDateNameDivider');
  this.dateColumn = child(this.mainDiv, 'sortOptionsDate');
  this.dateArrow = child(this.dateColumn, 'sortOptionsDateArrow');

  /*
  this.nameColumn.onclick = this.name.bind(this);
  this.dateColumn.onclick = this.date.bind(this);

  this.draw();
  */
}

/*
SortUi.prototype.isDate = function() {
  return this.active == SortUi.DATE_OPTION;
};

SortUi.prototype.isName = function() {
  return this.active == SortUi.NAME_OPTION;
};

SortUi.prototype.saveState = function() {
  options.putValue(SortUi.SORT_TYPE_OPTION, this.active);
};

SortUi.prototype.name = function() {
  if (this.active == SortUi.NAME_OPTION) {
    this.date();
    return;
  }
  this.active = SortUi.NAME_OPTION;
  this.saveState();
  this.draw();

  if (this.onChange) {
    this.onChange();
  }
};

SortUi.prototype.date = function() {
  if (this.active == SortUi.DATE_OPTION) {
    this.name();
    return;
  }
  this.active = SortUi.DATE_OPTION;
  this.saveState();
  this.draw();

  if (this.onChange) {
    this.onChange();
  }
};

SortUi.ACTIVE_BG = 'images/active-bg.gif';
SortUi.INACTIVE_BG = 'images/inactive-bg.gif';

SortUi.prototype.draw = function() {
  if (this.active == SortUi.DATE_OPTION) {
    this.dateColumn.background = SortUi.ACTIVE_BG ;
    this.nameColumn.background = SortUi.INACTIVE_BG;

    this.nameDateDivider.visible = true;
    this.dateNameDivider.visible = false;
    this.nameArrow.visible = false;
    this.dateArrow.visible = true;
  } else {
    this.dateColumn.background = SortUi.INACTIVE_BG;
    this.nameColumn.background = SortUi.ACTIVE_BG;

    this.nameDateDivider.visible = false;
    this.dateNameDivider.visible = true;
    this.nameArrow.visible = true;
    this.dateArrow.visible = false;
  }
};
*/

SortUi.prototype.resize = function(fullWidth, nameWidth) {
  this.mainDiv.width = fullWidth;
  this.mainDiv.x = 2;

  this.nameColumn.width = nameWidth;
  this.nameDateDivider.x = this.nameColumn.width;
  this.dateNameDivider.x = this.nameColumn.width;
  this.dateColumn.x = this.nameDateDivider.width + this.nameDateDivider.x;
  this.dateColumn.width = this.mainDiv.width - this.dateColumn.x;

  this.nameArrow.x = this.nameColumn.width - (this.nameArrow.width + 5);
  this.dateArrow.x = this.dateColumn.width - (this.dateArrow.width + 5);
};
