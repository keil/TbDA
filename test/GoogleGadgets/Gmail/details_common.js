var MILU_BLANK_LINE_MARKER = 'b';

function bind(self, method, var_args) {
  var args = Array.prototype.slice.call(arguments, 2);

  return function(var_args) {
    method.apply(self,
        args.concat(Array.prototype.slice.call(arguments)));
  };
}

function cancelEvent(event) {
  if (event) {
    if (event.stopPropagation) {
      // Gecko/Standard
      event.stopPropagation();
    } else {
      // IE
      event.returnValue = false;
      event.cancelBubble = true;
    }
  }
}

function trimString(str) {
  str = str.replace(/^\s+/g, '');
  return str.replace(/\s+$/g, '');
}

function Ele() {
}

Ele.gel = function(id) {
  return document.getElementById(id);
};

// Shorthand.
function gel(id) {
  return Ele.gel(id);
}

Ele.text = function(element, text) {
  var textNode = document.createTextNode(text);

  if (element.firstChild) {
    return element.replaceChild(textNode, element.firstChild);
  } else {
    return element.appendChild(document.createTextNode(text));
  }
};

Ele.html = function(element, html) {
  element.innerHTML = html;
};

Ele.create = function(element, tag, opt_className) {
  var newElement = element.appendChild(document.createElement(tag));

  if (opt_className) {
    newElement.className = opt_className;
  }

  return newElement;
};

Ele.width = function(element, width) {
  element.style.width = width + 'px';
};

Ele.height = function(element, height) {
  element.style.height = height + 'px';
};

Ele.top = function(element, top) {
  element.style.top = top + 'px';
};

Ele.show = function(element) {
  element.style.display = '';
};

Ele.hide = function(element) {
  element.style.display = 'none';
};

Ele.isVisible = function(element) {
  return element.style.display != 'none';
};

Ele.eventHandlers = [];

Ele.event = function(element, property, handler) {
  var callback = function(event) {
    // Gecko/Standard: event parameter; IE: window.event.
    event = event || window.event;
    handler(event);
  };
  if (element.addEventListener) {
    // Gecko/Standard
    element.addEventListener(property.substring(2), callback, false);
  } else {
    // IE
    element[property] = callback;
  }
  Ele.eventHandlers.push([element, property, callback]);
  // Clear extra closure dependencies.
  element = null;
  property = null;
};

Ele.cleanup = function() {
  for (var i = 0; i < Ele.eventHandlers.length; ++i) {
    var element = Ele.eventHandlers[i][0];
    var property = Ele.eventHandlers[i][1];
    var callback = Ele.eventHandlers[i][2];
    if (element.removeEventListener) {
      // Gecko/Standard
      element.removeEventListener(element, callback, false);
    } else {
      // IE
      element[property] = null;
    }
  }
};

function debug(message) {
  if (gel('debug')) {
    gel('debug').innerHTML = gel('debug').innerHTML + '<br>' + message;
  }
}


AlertUi.MESSAGE_TIMEOUT_MS = 3000;

function AlertUi() {
  this.alertMessage = gel('alertMessage');
  Ele.event(this.alertMessage, 'onclick', bind(this, this.hide));
  this.alertMessageSpan = gel('alertMessageSpan');
  this.hide();
  this.hideTimer = null;
}

AlertUi.prototype.show = function(message) {
  this.hideTimer = window.setTimeout(bind(this, this.hide),
      AlertUi.MESSAGE_TIMEOUT_MS);
  Ele.text(this.alertMessageSpan, message);
  Ele.show(this.alertMessage);
};

AlertUi.prototype.hide = function() {
  Ele.hide(this.alertMessage);
};

var KEY_ESCAPE = 27;
var KEY_SPACE = 32;
var KEY_UP = 38;
var KEY_DOWN = 40;
var KEY_TAB = 9;
var KEY_ENTER = 13;
var KEY_A = 65;
var KEY_F = 70;
var KEY_M = 77;
var KEY_N = 78;
var KEY_P = 80;
var KEY_R = 82;
var KEY_Y = 89;
