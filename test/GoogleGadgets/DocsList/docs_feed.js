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

DocsFeed.FEED_URL = 'https://docs.google.com/feeds/documents/private/full';
DocsFeed.LABELS_SCHEME = '/-/{http:%2F%2Fschemas.google.com%2Fg%2F2005%2Flabels}';

DocsFeed.MAX_RESULTS_PARAM = 'max-results';
DocsFeed.MAX_RESULTS = 30;
DocsFeed.SEARCH_PARAM ='q';
DocsFeed.START_INDEX_PARAM = 'start-index';

function DocsFeed(callback, failback, opt_query, opt_label) {
  this.callback = callback;
  this.failback = failback;
  this.query = opt_query;
  this.label = opt_label;
  this.isDead = false;
}

DocsFeed.prototype.destroy = function() {
  this.isDead = true;
};

DocsFeed.prototype.buildFeedUrl = function(startIndex) {
  var params = {};
  params[DocsFeed.MAX_RESULTS_PARAM] = DocsFeed.MAX_RESULTS;
  params[DocsFeed.START_INDEX_PARAM] = startIndex;

  var path = DocsFeed.FEED_URL;

  if (this.query) {
    params[DocsFeed.SEARCH_PARAM] = this.query;
  } else if (this.label) {
    path += DocsFeed.LABELS_SCHEME + encodeURIComponent(this.label);
  }

  return path + '?' + buildQueryString(params);
};

DocsFeed.prototype.retrieve = function() {
  this.retrieveChunk(1);
};

DocsFeed.STOP_RETRIEVE_THRESHOLD = 60;

DocsFeed.prototype.retrieveChunk = function(startIndex) {
  if (startIndex >= DocsFeed.STOP_RETRIEVE_THRESHOLD) {
    // Don't continue if past the threshold.
    return;
  }

  var url = this.buildFeedUrl(startIndex);

  g_authHttpRequest.connect(url, null, this.onRetrieve.bind(this),
      this.onError.bind(this));
};

DocsFeed.prototype.onRetrieve = function(response) {
  if (this.isDead) {
    return;
  }

  var feed = this.parseFeed(response);

  if (!feed) {
    return;
  }

  this.callback(feed);

  var nextIndex = feed.startIndex + feed.itemsPerPage;

  if (nextIndex - 1 < feed.totalResults) {
    // There are more to retrieve.
    this.retrieveChunk(nextIndex);
  }
};

DocsFeed.prototype.onError = function(status, response) {
  // Only consider bad auth an error.
  if (status == 401) {
    this.failback();
  }
};

DocsFeed.prototype.parseFeed = function(response) {
  var doc = createDomDocument();
  doc.loadXML(response);

  var feed = {};
  feed.totalResults = Number(
      doc.getElementsByTagName('openSearch:totalResults')[0].text);
  feed.startIndex = Number(
      doc.getElementsByTagName('openSearch:startIndex')[0].text);
  feed.itemsPerPage = Number(
      doc.getElementsByTagName('openSearch:itemsPerPage')[0].text);

  if (isNaN(feed.totalResults) || isNaN(feed.startIndex) ||
      isNaN(feed.itemsPerPage)) {
    debug.error('Invalid feed paging data.');
    return;
  }

  var documents = [];

  var entryElements = doc.getElementsByTagName('entry');

  for (var i = 0; i < entryElements.length; ++i) {
    var entry = entryElements[i];
    var document = new Document();

    document.title = entry.getElementsByTagName('title')[0].text;
    var date = parseRFC3339(entry.getElementsByTagName('updated')[0].text);
    document.updated = date;
    document.starred = false;

    var now = new Date();
    var yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);

    if (date.formatDate('n j Y') == now.formatDate('n j Y')) {
      document.date = date.formatDate('g:ia');
    } else if (date.formatDate('n j Y') == yesterday.formatDate('n j Y')) {
      document.date = strings.DATE_YESTERDAY;
    } else {
      document.date = date.formatDate('M j');
    }

    var links = entry.getElementsByTagName('link');

    for (var j = 0; j < links.length; ++j) {
      var link = links[j];
      if (link.getAttribute('rel') == 'alternate' || !document.link) {
        document.link = link.getAttribute('href');
      }
    }

    var categories = entry.getElementsByTagName('category');

    for (j = 0; j < categories.length; ++j) {
      var category = categories[j];
      if (category.getAttribute('scheme') ==
          'http://schemas.google.com/g/2005#kind') {
        document.type = category.getAttribute('label');
      }
      if (category.getAttribute('term') ==
          'http://schemas.google.com/g/2005/labels#starred') {
        document.starred = true;
      }
    }

    documents.push(document);
  }

  feed.documents = documents;

  return feed;
};
