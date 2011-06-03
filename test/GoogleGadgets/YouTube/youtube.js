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
 * @fileoverview This class handles the communication to YouTube. It makes calls
 * to request videos that are returned in GData format.
 */

/**
 * Character used to separate keywords associated to a video
 * @type String
 */
var YOUTUBE_KEYWORD_SEPARATOR = ', ';

/**
 * Class representing a thumbnail which contains a url for the image and the
 * width and height of the image
 * @constructor
 * @param {String} url Url of the thumbnail
 * @param {String} width Width of the thumbnail to display
 * @param {String} height Height of the thumbnail to display
 */
function Thumbnail(url, width, height) {
  this.url = url;
  this.width = width;
  this.height = height;
  this.src = undefined;  // XML response stream
}

/**
 * Callback for thumbnail image requests. Checks if the display should be
 * updated with new content if we're ready.
 * @return {XMLResponseStream} source The image
 */
Thumbnail.prototype.receivedImage = function(source) {
  --gl_pendingThumbnails;
  if (source !== null) {
    this.src = source;
  } else {
    debug.warning('Thumbnail data is null.');
  }

  shouldDisplay();
};

/**
 * Checks if an object has been initialized properly.
 * @return {Bool} True if it's valid
 */
Thumbnail.prototype.isValid = function() {
  return (this.url !== undefined) &&
    (this.width !== undefined) &&
    (this.height !== undefined);
};

/**
 * Class representing a YouTube video. It contains the title, description,
 * length of the video, the number of views, the url, and a thumbnail for
 * the video
 * @constructor
 */
function YouTubeVideo() {
  this.id = ++YouTubeVideo.uniqueId;
  this.title = undefined;
  this.description = undefined;
  this.length = undefined;
  this.viewCount = -1;
  this.thumbnail = undefined;

  // Keywords (or tags) used for the video. Stored as an array of strings.
  this.keywords = undefined;

  // URL of the youtube website for the video
  this.url = undefined;

  // Embedded URL - Source URL for the embedded flash player
  this.embeddedurl = undefined;

  // We only store the average rating
  this.rating = undefined;
  this.numRaters = undefined;
}

/**
 * Static counter of video id's used for each video
 * @type Number
 */
YouTubeVideo.uniqueId = 0;

/**
 * Checks if an object has been initialized properly.
 * @return {Bool} True if it's valid
 */
YouTubeVideo.prototype.isValid = function() {
  return (this.title !== undefined) &&
      (this.description !== undefined) &&
      (this.url !== undefined) &&
      (this.thumbnail !== undefined) &&
      (this.viewCount !== undefined);
      // These don't exist for embedding disabled videos.
      // (this.length !== undefined) &&
      // (this.embeddedurl !== undefined) &&
      // Util.isValidUrl(this.embeddedurl) &&
};

YouTubeVideo.prototype.toString = function() {
  return 'title: ' + this.id + '\n' +
    'description: ' + this.description + '\n' +
    'count: ' + this.viewCount;
};

/**
 * Returns the gadget XML for the video list item
 * @param {Bool} True if the gadget is currently docked to the sidebar
 * @return {String} Item's xml
 */
YouTubeVideo.prototype.getItemXml = function() {
  return '<item name="video_' + this.id + '" ' +
             'onclick="_itemSingleClick(' + this.id + ');" ' +
             'ondblclick="_itemDoubleClick(' + this.id + ');" ' +
             'enabled="true">' +
           '<img name="image" />' +
           '<label name="title" />' +
           '<label name="desc" />' +
           '<label name="view_length" />' +
         '</item>';
};

YouTubeVideo.prototype.getViewLength = function() {
  if (isNaN(this.length)) {
    return '';
  }

  var viewSeconds = this.length % 60;

  var viewLength = Math.floor(this.length / 60) + ':' +
      Math.floor(viewSeconds / 10) + '' + Math.floor(viewSeconds % 10);

  return viewLength;
};

/**
 * Makes a call to YouTube to collect the image for the video
 * (given by imageUrl) asynchronously and displays them as they are received
 * @param {Function} callback Function that will accept the video id, and
 *     downloaded image
 */
YouTubeVideo.prototype.fetchStatusImage = function() {
  var request = new SimpleHTTPRequest();
  request.request(this.thumbnail.url, this.thumbnail,
      this.thumbnail.receivedImage, true);
};

// Keys obtained from http://code.google.com/apis/youtube/dashboard/
// using the open.source.gd account.
YouTubeVideo.DEVELOPER_KEY_HEADERS = {
  'X-GData-Client': 'ytapi-GoogleDesktopGadget',
  'X-GData-Key': 'key=AI39si4b5MPcrrvd4vKOomh3fzBTugXRKj71pfzRQlemZuGKPxknMJ_gQ6tBKuMBxtoRcg1CdZf3JNog8KkobPy1CyY-BUtpZA' };

/**
 * Queries YouTube for an XML response. The videos are then parsed and returns
 * to the callback function with an array of YouTubeVideo objects.
 * @param {String} query URL to query (includes complete parameters for the
 *                       request)
 * @param {Function} callback Post-query callback function that accepts the
 *     array of YouTubeVideo's
 */
YouTubeVideo.videoRequest = function(query, callback) {
  ++gl_pendingResponses;

  var request = new SimpleHTTPRequest();
  request.request(query, null, YouTubeVideo.makeOnVideoRequest(callback),
      null, null, YouTubeVideo.DEVELOPER_KEY_HEADERS);
};

YouTubeVideo.makeOnVideoRequest = function(callback) {
  return function(responseText) {
    YouTubeVideo.onVideoRequest(responseText, callback);
  };
};

YouTubeVideo.onVideoRequest = function(responseText, callback) {
  if (responseText === null) {
    callback(null);
    return;
  }

  var videos = YouTubeVideo.parseVideoResponseXml(responseText);
  debug.info('num responses: ' + videos.length);
  callback(videos);
};

/**
 * Takes in a JSON object from YouTube and parses out the data to create
 * an array of YouTubeVideo objects to return to the callback function.
 * @param {DOMDocument} responseXml responseXML of the XmlHttpRequest
 * @return {Array<YouTubeVideo>} Parsed YouTubeVideo's
 */
YouTubeVideo.parseVideoResponseXml = function(responseXml) {
  var videoArray = [];
  if (!responseXml) {
    return videoArray;
  }

  var doc = new DOMDocument();
  doc.resolveExternals = false;
  doc.validateOnParse = false;
  doc.setProperty('ProhibitDTD', false);

  if (!doc.loadXML(responseXml)) {
    debug.error('ytparse: error loading xml response');
    return videoArray;
  }

  var videoItems = undefined;
  try {
    videoItems = doc.getElementsByTagName('item');
  } catch(e) {
    debug.error('yt: error parsing items');
    return videoArray;
  }

  for (var i = 0; videoItems && i < videoItems.length; ++i) {
    var ytvideo = new YouTubeVideo();
    var item = videoItems[i];
    // parse title
    ytvideo.title = Util.getData(item, 'media:title');
    ytvideo.title = Util.sanitize(ytvideo.title);

    // parse description
    ytvideo.description = Util.getData(item, 'media:description');
    ytvideo.description = Util.sanitize(ytvideo.description);

    // parse keywords
    ytvideo.keywords = Util.getData(item, 'media:keywords');
    ytvideo.keywords = ytvideo.keywords.split(YOUTUBE_KEYWORD_SEPARATOR);

    // parse embeddedUrl, length
    try {
      var content = item.getElementsByTagName('media:content');
      for (var j = 0; content && j < content.length; ++j) {
        if (content[j].getAttribute('type') ==
            'application/x-shockwave-flash') {
          ytvideo.length = content[j].getAttribute('duration');
          ytvideo.embeddedurl = content[j].getAttribute('url');
          break;
        }
      }
    } catch(e) {
      debug.error('ytparse(media:content): ' + e.message);
    }

    // parse url
    ytvideo.url = Util.getAttribute(item, 'media:player', 'url');

    // parse view count
    ytvideo.viewCount = Util.getAttribute(item, 'yt:statistics', 'viewCount');

    // parse rating
    ytvideo.rating = Util.getAttribute(item, 'gd:rating', 'average');
    if (ytvideo.rating === undefined) {
      ytvideo.rating = 0;
    }

    ytvideo.numRaters = Util.getAttribute(item, 'gd:rating', 'numRaters');
    if (ytvideo.numRaters === undefined) {
      ytvideo.numRaters = 0;
    }

    // Get the thumbnail
    var thumbUrl = Util.getAttribute(item, 'media:thumbnail', 'url');
    var thumbHeight = Util.getAttribute(item, 'media:thumbnail', 'height');
    var thumbWidth = Util.getAttribute(item, 'media:thumbnail', 'width');
    ytvideo.thumbnail = new Thumbnail(thumbUrl, thumbWidth, thumbHeight);

    if (ytvideo.isValid() && ytvideo.thumbnail.isValid()) {
      videoArray.push(ytvideo);
    } else {
      debug.trace('ytparse: discarded invalid item - ' + ytvideo.title);
    }
  }

  return videoArray;
};
