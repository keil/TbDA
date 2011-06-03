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
 * Simple HTTP Request v1.00
 * Changelog
 *  1.00 - Initial version
 */

// A general function to simplify downloading data from a website and also
// handle errors gracefully.
function SimpleHTTPRequest() {
  var getStream_ = false;
  var request_ = null;
  var stop_ = false;
  var callbackObject_ = null;
  var receivedResultCallback_ = null;
  var tag_ = null;
  var timeoutTimer_ = null;

  var TIMEOUT_DURATION_MS = 20 * 1000;

  // Do a request to get a webpage. If getText is true, a text version of the
  // output will be returned. If false, the response stream will be returned
  // (useful for getting image data).
  function request(url, callbackObject, receivedResultCallback,
                   opt_getStream, opt_tag, opt_headers) {
    if (opt_tag != null)
      tag_ = opt_tag;

    assert(receivedResultCallback != null);
    if (receivedResultCallback == null)
      return;
    receivedResultCallback_ = receivedResultCallback;

    callbackObject_ = callbackObject || null;

    assert(request_ == null);
    if (request_ != null) {
      callback(null, tag_);
      return;
    }

    request_ = new XMLHttpRequest();

    // Catch any URL errors
    try {
      request_.open("GET", url, true);
    } catch (e) {
      request_ = null;
      callback(null, tag_);
      return;
    }

    request_.onreadystatechange = onData;
    stop_ = false;

    var getStream = false;
    if ((opt_getStream !== undefined) && (opt_getStream === true))
      getStream = true;
    getStream_ = getStream;

    if (opt_headers) {
      for (var key in opt_headers) {
        request_.setRequestHeader(key, opt_headers[key]);
      }
    }

    timeoutTimer_ = view.setTimeout(onTimeout, TIMEOUT_DURATION_MS);

    // Send the request
    try {
      request_.send();
    } catch(e) {
      request_ = null;
      // Timeout handler will call callback.
      return;
    }
  }

  // Stop the current transfer. No data callbacks will be made for this request.
  function stop() {
    if (request_ == null)
      return;

    view.clearTimeout(timeoutTimer_);
    stop_ = true;
    request_.abort();
    request_ = null;
    callbackObject_ = null;
  }

  function onTimeout() {
    debug.warning('Request timed out.');
    callback(null, tag_);

    if (request_ != null) {
      request_.abort();
      request_ = null;
    }

    callbackObject_ = null;
  }

  // Called when data is received from the website
  function onData() {
    // If the download has been stopped, do not continue
    if (stop_)
      return;

    // If the data is not ready (still downloading), do not continue
    assert(request_ != null);
    if (request_.readyState != 4)
      return;

    view.clearTimeout(timeoutTimer_);

    // If there was an http error, do not continue
    if (request_.status != 200) {
      debug.error('XML Request failed: ' + request_.status);
      callback(null, tag_);
      request_ = null;
      return;
    }

    // Return the requested data
    if (getStream_) {
      callback(request_.responseStream, tag_);
    } else {
      callback(request_.responseText, tag_);
    }

    request_ = null;
    callbackObject_ = null;
  }

  // Apply the callback with a response
  function callback(response, tag) {
    if (callbackObject_)
      receivedResultCallback_.call(callbackObject_, response, tag);
    else
      receivedResultCallback_(response, tag);
  }

  this.request = request;
  this.stop = stop;
}
