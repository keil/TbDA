// Copyright 2007 Google Inc.
// All Rights Reserved.

// @fileoverview Functions for server communications through XMLHttpRequest

function createXhr() {
  var xhr;

  try {
    xhr = framework.google.betaXmlHttpRequest();
  } catch (e) {
    xhr = new XMLHttpRequest();
  }

  return xhr;
}

var g_httpRequest = new HTTPRequest();

function HTTPRequest() {
  this.packet = new createXhr();
  this.handler = null;
  this.failedHandler = null;
  this.isBinary = null;
  this.refThis = null;
  this.url = null;
  this.host = null;
  // Token for timeout timer.
  this.timeoutTimer = null;
}

HTTPRequest.available = true;
HTTPRequest.queue = [];
// Timeout duration. After this, consider the request failed.
HTTPRequest.TIMEOUT_MS = 30 * 1000;

/**
 * Function used to allow a time between httpRequests, so as not to clutter
 * the servers. This function is static since all requests in the gadget are
 * made to the same server and thus must handle all objects of the class type
 */
HTTPRequest.finishedGracePeriod = function() {
  HTTPRequest.available = true;
  if (HTTPRequest.queue.length > 0) {
    var request = HTTPRequest.queue.shift();
    request.requestObject.connectToGmailServer(request.data, request.handler,
        request.failedHandler, request.isBinary, request.refThis,
        request.opt_dontShowLoading);
  }
};

/**
 * Sends out a request using XMLHttpRequest
 * @param {String} data The data to be packed.
 * @param {Function} handler Called when data is ready with parsed response data.
 * @param {Function} failedHandler Called if request fails.
 * @param {boolean} opt_dontShowLoading Don't show red loading message.
 * @param {Object} opt_headers Additional request headers.
 */
HTTPRequest.prototype.connectToGmailServer = function (data, handler,
    failedHandler, isBinary, refThis, opt_dontShowLoading, opt_headers) {
  if (!HTTPRequest.available) {
    // The server fails to handle too many requests at a time so we need to
    // queue them.
    if (HTTPRequest.queue.length > 0 && data.indexOf(URL.OFFSET_PARAM) > -1) {
      // We check to see if the user requested the next/previews page twice
      // this is the only case we need to check as we check it before adding
      // every request so if 3 or more consecutive calls are made only the
      // latest is kept. There is no need for further checking, as other types
      // of request are more naturally stoped from queueing elsewhere.
      if (HTTPRequest.queue[HTTPRequest.queue.length - 1].data.indexOf(
          URL.OFFSET_PARAM) > -1) {
        // if this happens, we only keep the latest request arround
        HTTPRequest.queue.pop();
      }
    }
    HTTPRequest.queue.push({requestObject: this, data: data, handler: handler,
        failedHandler: failedHandler, isBinary: isBinary, refThis: refThis,
        opt_dontShowLoading: opt_dontShowLoading});
    return;
  }
  if (opt_dontShowLoading !== true) {
    try {
      imgLoading.visible = true;
    } catch(e) {
      // rare occurance, if the details view is just closing
      return;
    }
  }

  this.handler = handler;
  this.failedHandler = failedHandler;
  this.isBinary = isBinary;
  this.refThis = refThis;
  this.abort();

  // Check if network is online.
  if (!Utils.isOnline()) {
    this.onFailure();
    return;
  }

  this.packet.onreadystatechange = g_httpRequest.receivedData;
  this.packet.open('POST', this.url, true);
  this.packet.setRequestHeader('cookie', 'none');
  this.packet.setRequestHeader('X-Wap-Proxy-Cookie', 'none');
  this.packet.setRequestHeader('Cache-Control', 'no-cache, no-transform');
  this.packet.setRequestHeader('Content-Type',
                               'application/x-www-form-urlencoded');
  this.packet.setRequestHeader('Host', this.host);

  if (opt_headers) {
    for (var key in opt_headers) {
      this.packet.setRequestHeader(key, opt_headers[key]);
    }
  }

  this.packet.send(data);

  HTTPRequest.available = false;

  // Set timeout timer.
  this.clearTimeout();
  this.timeoutTimer = view.setTimeout(this.makeOnTimeout(),
      HTTPRequest.TIMEOUT_MS);

  debug.trace('Request sent to server : ' + data);
};

HTTPRequest.prototype.clearTimeout = function() {
  if (this.timeoutTimer) {
    view.clearTimeout(this.timeoutTimer);
    this.timeoutTimer = null;
  }
};

HTTPRequest.nullFunction = function() {
};

HTTPRequest.prototype.abort = function() {
  this.packet.onreadystatechange = HTTPRequest.nullFunction;
  this.packet.abort();
};

HTTPRequest.prototype.makeOnTimeout = function() {
  var me = this;

  return function() {
    me.onTimeout();
  };
};

HTTPRequest.prototype.onTimeout = function() {
  debug.error('Request timed out.');
  this.abort();
  setTimeout(HTTPRequest.finishedGracePeriod, TIME_BETWEEN_HTTP_REQUESTS);
  this.onFailure();
};

HTTPRequest.prototype.hideLoading = function() {
  try {
    imgLoading.visible = false;
  } catch(e) {
    // rare occurance, if the details view is closing when receiving data
    debug.warning('Could not hide loading image.');
  }
};

HTTPRequest.prototype.onFailure = function() {
  g_httpRequest.hideLoading();

  if (this.failedHandler !== null) {
    var status = this.packet.readyState == CONNECTION_DATA.LOADED_STATE ?
        this.packet.status : 0;
    this.failedHandler(status, this.refThis);
  } else {
    g_errorMessage.displayMessage(SERVER_OR_NETWORK_ERROR);
  }
};

HTTPRequest.prototype.receivedData = function() {
  if (!g_httpRequest.packet) {
    return;
  }
  if (g_httpRequest.packet.readyState != CONNECTION_DATA.LOADED_STATE) {
    return;
  }
  debug.trace('All data received here...');
  g_httpRequest.clearTimeout();
  setTimeout(HTTPRequest.finishedGracePeriod, TIME_BETWEEN_HTTP_REQUESTS);
  if (g_httpRequest.packet.status != CONNECTION_DATA.HTTP_OK) {
    debug.error('A transfer error has occured !');
    g_httpRequest.onFailure();
    return;
  }
  if (g_httpRequest.handler !== null) {
    if (g_httpRequest.isBinary) {
      g_httpRequest.handler(g_httpRequest.packet.responseStream,
      g_httpRequest.refThis);
    } else {
      var responseData = new ResponseData(g_httpRequest.packet.responseBody);
      g_httpRequest.handler(responseData, g_httpRequest.refThis);
    }
  } else {
    g_httpRequest.hideLoading();
  }
};
