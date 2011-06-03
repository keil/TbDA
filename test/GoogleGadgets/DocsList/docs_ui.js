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

function DocsUi(mainDiv, gadget) {
  this.mainDiv = mainDiv;
  this.gadget = gadget;
  this.onSearch = null;
  this.onSearchReset = null;

  this.contentArea = child(this.mainDiv, 'contentArea');
  this.container = child(this.contentArea, 'contentContainer');
  this.content = child(this.container, 'doclistContent');

  if (this.container.scrollbar) {
    if (this.container.scrollbar.lineStep) {
      this.container.scrollbar.lineStep = 20;
    }
  }

  this.documents = [];

  this.sortUi = new SortUi(child(this.mainDiv, 'sortOptionsArea'));
  this.searchUi = new SearchUi(child(this.mainDiv, 'searchStatus'),
      child(this.gadget.window, 'autoFill'), this.gadget);

  this.itemNameWidth = 0;
  this.searchUi.onSearch = this.onSearchUiSearch.bind(this);
  this.searchUi.onReset = this.onSearchUiReset.bind(this);
}

DocsUi.prototype.keyDown = function() {
  if (event.keycode == KEYS.TAB) {
    this.searchUi.focus();
    return;
  }
};

DocsUi.prototype.keyUp = function() {
};

DocsUi.prototype.onSearchUiSearch = function(query) {
  if (this.onSearch) {
    this.onSearch(query);
  }
};

DocsUi.prototype.onSearchUiReset = function() {
  if (this.onSearchReset) {
    this.onSearchReset();
  }
};

DocsUi.prototype.reset = function() {
  this.documents = [];
  this.resetSearch();
  this.draw();
};

DocsUi.prototype.resetSearch = function() {
  this.searchUi.reset();
};

DocsUi.prototype.clear = function() {
  this.content.removeAllElements();
};

DocsUi.prototype.show = function() {
  this.mainDiv.visible = true;
};

DocsUi.prototype.hide = function() {
  this.mainDiv.visible = false;
};

DocsUi.prototype.sort = function() {
  this.documents.sort(DocsUi.sortByDate);
};

DocsUi.sortByName = function(a, b) {
  a = a.title.toLowerCase();
  b = b.title.toLowerCase();
  if (!a || a < b) {
    return -1;
  } else if (!b || a > b) {
    return 1;
  } else {
    return 0;
  }
};

DocsUi.sortByDate = function(a, b) {
  a = a.updated.getTime();
  b = b.updated.getTime();
  if (a > b) {
    return -1;
  } else if (a < b) {
    return 1;
  } else {
    return 0;
  }
};

DocsUi.prototype.redraw = function(documents) {
  this.documents = documents;
  this.draw();
};

DocsUi.prototype.draw = function() {
  var documents = this.documents;
  this.clear();

  for (var i = 0; i < documents.length; ++i) {
    var document = documents[i];

    var item = this.content.appendElement('<div width="100%" height="20" cursor="hand" enabled="true" />');

    var iconDiv = item.appendElement('<div name="icon" x="2" y="2" width="16" height="16" />');
    iconDiv.background = document.getIcon();

    var titleLabel = item.appendElement('<label name="title" x="21" y="2" font="helvetica" size="8" color="#000000" trimming="character-ellipsis" />');
    titleLabel.innerText = document.title;
    titleLabel.tooltip = document.title;

    var starDiv = item.appendElement('<div name="star" y="4" width="12" height="12" background="images/icon-star.gif" visible="false" />');
    starDiv.visible = document.starred;

    var dateLabel = item.appendElement('<label name="date" y="2" font="helvetica" size="8" color="#66b3ff" align="right" />');
    dateLabel.innerText = document.date;
    dateLabel.tooltip = document.date;

    item.onmouseover = function() { event.srcElement.background='#E0ECF7'; };
    item.onmouseout = function() { event.srcElement.background=''; };
    item.onclick = g_gadget.openUrlTokenAuth.bind(g_gadget, document.link);

    this.content.appendElement('<div height="1" background="#dddddd" />');
  }

  this.resizeContent();
};

// TODO: This happens to be true in Windows. What about other platforms?
DocsUi.SCROLLBAR_OFFSET = 17;

DocsUi.prototype.resizeContent = function() {
  var y = 0;

  for (var i = 0; i < this.content.children.count; ++i) {
    var div = this.content.children.item(i);
    div.y = y;
    y += div.height;
  }

  this.content.height = y;

  this.content.width = this.container.width - 14;

  if ((this.container.scrollbar && this.container.scrollbar.visible) ||
      (this.content.height > this.container.height && !Main.isDocked)) {
    this.content.width -= DocsUi.SCROLLBAR_OFFSET;
  }

  for (i = 0; i < this.content.children.count; ++i) {
    div = this.content.children.item(i);

    if (div.children.count > 0) {
      // Not a separator.
      var icon = child(div, 'icon');
      var title = child(div, 'title');
      var star = child(div, 'star');
      var date = child(div, 'date');

      var itemNameWidth = this.itemNameWidth;

      if (itemNameWidth > this.content.width) {
        itemNameWidth = this.content.width;
      }

      title.width = itemNameWidth - icon.width - 3;

      if (star.visible) {
        title.width -= star.width;
        var starX = title.x + labelCalcWidth(title);
        if (starX > title.x + title.width) {
          starX = title.x + title.width;
        }
        star.x = starX;
      }

      date.x = this.itemNameWidth + 2;
      date.width = this.content.width - date.x;
    }
  }
};

DocsUi.MIN_ITEM_NAME_WIDTH = 175;
DocsUi.MAX_ITEM_DATE_WIDTH = 85;
DocsUi.MIN_ITEM_DATE_WIDTH = 80;

DocsUi.prototype.resize = function(width, height) {
  this.mainDiv.width = width;
  this.mainDiv.height = height;

  this.contentArea.width = this.mainDiv.width;
  this.contentArea.height = this.mainDiv.height - 83;

  var contentShadowBottom = child(this.contentArea, 'contentShadowBottom');
  var contentShadowBottomLeft = child(this.contentArea, 'contentShadowBottomLeft');
  var contentShadowRight = child(this.contentArea, 'contentShadowRight');
  var contentShadowBottomRight = child(this.contentArea, 'contentShadowBottomRight');

  this.container.width = this.contentArea.width - contentShadowRight.width;
  this.container.height = this.contentArea.height - contentShadowBottom.height;
  contentShadowBottom.width = this.container.width - contentShadowBottomLeft.width;
  contentShadowRight.height = this.contentArea.height - contentShadowBottomRight.height;
  contentShadowBottom.x = contentShadowBottomLeft.width;
  contentShadowRight.x = this.container.width;
  contentShadowBottom.y = this.container.height;
  contentShadowBottomLeft.y = this.container.height;
  contentShadowBottomRight.x = this.container.width;
  contentShadowBottomRight.y = this.container.height;

  var contentWidth = this.mainDiv.width - 6;

  var availableDateWidth = contentWidth - DocsUi.MIN_ITEM_NAME_WIDTH;
  var itemNameWidth = contentWidth - availableDateWidth;

  if (availableDateWidth < DocsUi.MIN_ITEM_DATE_WIDTH) {
    // Date got squeezed out :(
    itemNameWidth = contentWidth;
  } else if (availableDateWidth > DocsUi.MAX_ITEM_DATE_WIDTH) {
    itemNameWidth = contentWidth - DocsUi.MAX_ITEM_DATE_WIDTH;
  }

  this.itemNameWidth = itemNameWidth;
  this.sortUi.resize(contentWidth, this.itemNameWidth);
  this.searchUi.resize(this.mainDiv.width);

  this.resizeContent();
};
