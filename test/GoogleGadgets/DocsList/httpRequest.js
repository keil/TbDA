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

function AuthHTTPRequest(httpRequest, auth) {
  this.httpRequest = httpRequest;
  this.auth = auth;
}

AuthHTTPRequest.prototype.connect = function (url, data, handler, failedHandler,
    headers, isFile) {
  headers = headers || {};
  headers['Authorization'] = 'GoogleLogin auth=' + this.auth.token;

  this.httpRequest.connect(url, data, handler, failedHandler,
      headers, isFile);
};

HTTPRequest.TIME_BETWEEN_REQUESTS = 1000;  // 1 second.
HTTPRequest.TIMEOUT = 30000;  // 30 seconds.

HTTPRequest.USER_AGENT = 'GoogleDesktop-DocsGadget-1.0.0.1';

function HTTPRequest() {
  this.packet = createXhr();

  // Token for timeout timer.
  this.timeoutTimer = null;
  this.loadingIndicator = loading;  // loading is name of a div.
}

HTTPRequest.available = true;
HTTPRequest.queue = [];

/**
 * Function used to allow a time between httpRequests, so as not to clutter
 * the servers. This function is static since all requests in the gadget are
 * made to the same server and thus must handle all objects of the class type
 */
HTTPRequest.finishedGracePeriod = function() {
  HTTPRequest.available = true;
  if (HTTPRequest.queue.length > 0) {
    var request = HTTPRequest.queue.shift();
    request.requestObject.connect(request.url, request.data, request.handler,
        request.failedHandler, request.headers, request.isFile);
  }
};

HTTPRequest.prototype.connect = function (url, data, handler, failedHandler,
    headers, isFile) {
  if (!HTTPRequest.available) {
    HTTPRequest.queue.push({ requestObject: this, url: url, data: data,
        handler: handler, failedHandler: failedHandler,
        headers: headers, isFile: isFile });
    return;
  }

  this.packet.onreadystatechange = HTTPRequest.nullFunction;
  this.packet.abort();

  this.showLoading();

  if (!Utils.isOnline()) {
    this.onFailure(failedHandler);
    return;
  }

  if (isFile) {
    var filename = data;

    var stream = new ActiveXObject('ADODB.Stream');

    stream.Type = 1;  // adTypeBinary
    stream.Open();
    stream.LoadFromFile(filename);

    data = stream.Read(-1);  // adReadAll
    stream.Close();
    stream = null;
  }

  this.packet.onreadystatechange = this.receivedData.bind(this,
      handler, failedHandler);
  debug.trace('Opening URL: ' + url);

  if (data) {
    this.packet.open('POST', url, true);
    this.packet.setRequestHeader('Content-Type',
        'application/x-www-form-urlencoded');
  } else {
    this.packet.open('GET', url, true);
  }

  this.packet.setRequestHeader('User-Agent', HTTPRequest.USER_AGENT);

  for (var key in headers) {
    if (typeof headers[key] == 'string') {
      this.packet.setRequestHeader(key, headers[key]);
    }
  }

  this.packet.send(data);
  HTTPRequest.available = false;

  this.clearTimeout();
  this.timeoutTimer = view.setTimeout(this.onTimeout.bind(this, failedHandler),
      HTTPRequest.TIMEOUT);
};

HTTPRequest.prototype.stop = function() {
  if (!this.packet) {
    return;
  }
  if (this.packet.readyState != 4) {
    this.packet.onreadystatechange = HTTPRequest.nullFunction;
    this.packet.abort();
  }
  view.setTimeout(HTTPRequest.finishedGracePeriod,
      HTTPRequest.TIME_BETWEEN_REQUESTS);
  this.clearTimeout();
  this.hideLoading();
};

HTTPRequest.prototype.clearTimeout = function() {
  if (this.timeoutTimer) {
    view.clearTimeout(this.timeoutTimer);
    this.timeoutTimer = null;
  }
};

HTTPRequest.nullFunction = function() {
};

HTTPRequest.prototype.onTimeout = function(failedHandler) {
  debug.error('Request timed out.');
  this.packet.onreadystatechange = HTTPRequest.nullFunction;
  this.packet.abort();

  view.setTimeout(HTTPRequest.finishedGracePeriod,
      HTTPRequest.TIME_BETWEEN_REQUESTS);
  this.onFailure(failedHandler);
};

HTTPRequest.prototype.showLoading = function() {
  this.loadingIndicator.visible = true;
};

HTTPRequest.prototype.hideLoading = function() {
  this.loadingIndicator.visible = false;
};

HTTPRequest.prototype.onFailure = function(failedHandler) {
  this.hideLoading();

  if (failedHandler) {
    if (this.packet.readyState == 4) {
      failedHandler(this.packet.status, this.packet.responseText);
    } else {
      failedHandler(0, '');
    }
  } else {
    g_errorMessage.display(strings.ERROR_SERVER_OR_NETWORK);
  }
};

HTTPRequest.prototype.receivedData = function(handler, failedHandler) {
  if (!this.packet) {
    return;
  }
  if (this.packet.readyState != 4) {
    return;
  }
  this.hideLoading();
  this.clearTimeout();
  view.setTimeout(HTTPRequest.finishedGracePeriod,
      HTTPRequest.TIME_BETWEEN_REQUESTS);

  if (this.packet.status < 200 || this.packet.status >= 300) {
    debug.error('A transfer error has occured:' + this.packet.status);
    this.onFailure(failedHandler);
    return;
  }

  handler(this.packet.responseText);
};
