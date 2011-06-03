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

/**
 * Constructor for CustomScrollbar class.
 */
function CustomScrollbar(mainDiv) {
  this.maxY = 0;
  this.onChange = null;

  this.mainDiv = mainDiv;
  this.bar = child(this.mainDiv, 'scrollbarBar');
  this.track = child(this.mainDiv, 'scrollbarTrack');
  this.up = child(this.mainDiv, 'scrollbarUp');
  this.down = child(this.mainDiv, 'scrollbarDown');

  this.halt = {};

  this.bar.onmousedown = this.startBar.bind(this);
  this.bar.onmousemove = this.dragBar.bind(this);
  this.bar.onmouseup = this.endBar.bind(this);
  this.track.onclick = this.clickTrack.bind(this);

  this.up.onmousedown = this.startUp.bind(this);
  this.down.onmousedown = this.startDown.bind(this);
  this.up.onmouseup = this.endUp.bind(this);
  this.down.onmouseup = this.endDown.bind(this);
}

CustomScrollbar.prototype.isVisible = function() {
  return this.mainDiv.visible;
};

CustomScrollbar.prototype.setMax = function(max) {
  this.maxY = max;
};

CustomScrollbar.prototype.getWidth = function() {
  return this.mainDiv.width;
};

CustomScrollbar.prototype.hide = function() {
  this.mainDiv.visible = false;
};

CustomScrollbar.prototype.show = function() {
  this.mainDiv.visible = true;
};

/**
 * Keyboard controls on keydown
 */
CustomScrollbar.prototype.keydown = function() {
  switch (event.keycode) {
    case KEYS.UP:
      this.startUp();
      break;

    case KEYS.DOWN:
      this.startDown();
      break;

    case KEYS.PAGE_UP:
      this.scrollPageUp();
      break;

    case KEYS.PAGE_DOWN:
      this.scrollPageDown();
      break;

    case KEYS.HOME:
      this.scrollTop();
      break;

    case KEYS.END:
      this.scrollBottom();
      break;
  }
};

CustomScrollbar.prototype.scrollBottom = function() {
  if (this.isVisible()) {
    this.bar.y = this.max();
    this.scroll();
  }
};

CustomScrollbar.prototype.scrollTop = function() {
  if (this.isVisible()) {
    this.bar.y = this.min();
    this.scroll();
  }
};

CustomScrollbar.prototype.scrollPageDown = function() {
  if (this.isVisible()) {
    this.moveBar(this.bar.height);
  }
};

CustomScrollbar.prototype.scrollPageUp = function() {
  if (this.isVisible()) {
    this.moveBar(-this.bar.height);
  }
};

CustomScrollbar.prototype.keyup = function() {
  switch (event.keycode) {
    case KEYS.UP:
      this.endUp();
      break;

    case KEYS.DOWN:
      this.endDown();
      break;
  }
};

CustomScrollbar.prototype.wheel = function() {
  if (this.halt.wheel) {
    return;
  }

  this.halt.wheel = true;

  if (event.wheelDelta > 0) {
    this.startUp();

    var time = 100 * (Math.abs(event.wheelDelta) / 360);
    view.setTimeout(function() {
      this.endUp();
      this.halt.wheel = false;
    }.bind(this), time);
  } else if (event.wheelDelta < 0) {
    this.startDown();

    time = 100 * (Math.abs(event.wheelDelta) / 360);
    view.setTimeout(function() {
      this.endDown();
      this.halt.wheel = false;
    }.bind(this), time);
  }
};

CustomScrollbar.prototype.startUp = function() {
  var time = (this.bar.height && this.track.height) ? 100 / (this.bar.height / this.track.height) : 100;

  this.upTimer = view.beginAnimation(function() {
    this.bar.y = event.value;
    this.scroll();
  }.bind(this), this.bar.y, this.min(), time * this.ratio());
};

CustomScrollbar.prototype.startDown = function() {
  var time = (this.bar.height && this.track.height) ? 100 / (this.bar.height / this.track.height) : 100;

  this.downTimer = view.beginAnimation(function() {
    this.bar.y = event.value;
    this.scroll();
  }.bind(this), this.bar.y, this.max(), time * (1 - this.ratio()));
};

CustomScrollbar.prototype.endUp = function() {
  view.cancelAnimation(this.upTimer);
};

CustomScrollbar.prototype.endDown = function() {
  view.cancelAnimation(this.downTimer);
};

CustomScrollbar.prototype.startBar = function() {
  this.halt.drag = true;
  this.previousDragY = framework.system.cursor.position.y;
};

CustomScrollbar.prototype.endBar = function() {
  this.halt.drag = false;
};

CustomScrollbar.prototype.min = function() {
  return this.up.height;
};

CustomScrollbar.prototype.max = function() {
  return (this.track.height - (this.bar.height - this.up.height + 1));
};

CustomScrollbar.prototype.ratio = function() {
  if (this.max() == this.min()) {
    return 0;
  }
  return (this.bar.y - this.min()) / (this.max() - this.min());
};

CustomScrollbar.prototype.scroll = function() {
  if (this.maxY < 0) {
    this.maxY = 0;
  }

  var newY = this.maxY * this.ratio();

  if (newY > this.maxY) {
    newY = this.maxY;
  }

  this.onChange(newY);
};

CustomScrollbar.prototype.clickTrack = function() {
  var min = this.min();
  var max = this.max();

  if (event.y < min) {
    this.bar.y = min;
  } else if (event.y > max) {
    this.bar.y = max;
  } else {
    this.bar.y = event.y;
  }

  this.scroll();
};

CustomScrollbar.prototype.moveBar = function(moveY) {
  var y = moveY;

  var min = this.min();
  var max = this.max();

  if (y < 0) {
    if (this.bar.y > min) {
      this.bar.y = (this.bar.y + y > min) ? this.bar.y + y : min;
    }
  }
  else if (y > 0) {
    if (this.bar.y < max) {
        this.bar.y = (this.bar.y + y > max) ? max : this.bar.y + y;
    }
  }

  this.scroll();
};

CustomScrollbar.prototype.dragBar = function() {
  if (!this.halt.drag) {
    return;
  }
  this.halt.drag = false;

  var mouseY = framework.system.cursor.position.y;
  this.moveBar(mouseY - this.previousDragY);
  this.previousDragY = mouseY;

  this.halt.drag = true;
};

CustomScrollbar.prototype.resize = function(x, height, sizeRatio) {
  var scrollRatio = this.track.height ? ((this.bar.y - this.up.height) / (this.track.height)) : 0;

  this.mainDiv.x = x;
  this.mainDiv.height = height - 5;

  this.down.y = this.mainDiv.height - this.down.height;
  this.track.height = this.mainDiv.height - (this.down.height + this.up.height);

//  if (this.maxY === 0) {
//    this.bar.height = this.track.height - 1;
//  } else {
    var newHeight = Math.ceil(this.track.height * sizeRatio);
    if (newHeight < 10) {
      newHeight = 10;
    }
    this.bar.height = newHeight >= this.track.height ? this.track.height - 1 : newHeight;
//  }

  var newY = scrollRatio * this.track.height + this.up.height;

  if (newY < this.min()) {
      this.bar.y = this.min();
  } else if (newY > this.max()) {
      this.bar.y = this.max();
  } else {
    this.bar.y = newY;
  }

  this.scroll();
};
