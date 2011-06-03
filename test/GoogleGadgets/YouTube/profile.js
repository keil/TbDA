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

// format=5 returns only embeddable videos
// alt=rss for XML response
// racy=exclude to return only 'proper' videos
var YOUTUBE_BASE_SEARCH = 'http://gdata.youtube.com/feeds/';
var YOUTUBE_SEARCH_FEED = YOUTUBE_BASE_SEARCH + 'videos?orderby=relevance';
var YOUTUBE_STANDARD_FEED = YOUTUBE_BASE_SEARCH + 'standardfeeds/';
var YOUTUBE_COMMON_PARAMETERS = '&alt=rss&racy=exclude';

/**
 * Simple container to store the state of a specific query
 * @constructor
 * @param {String} opt_url Base Url for the query, if applicable. This
 *     shouldn't be an optional parameter, but would require more refactoring
 *     than it's worth.
 */
function Query(opt_url) {
  // These are the parameters used in the previous query.
  this.baseUrl = opt_url; // ok for it to be undefined
}

/**
 * @constructor
 */
function KeywordsProfile() {
  this.searchQuery = undefined;
  this.feedQuery = undefined;
  this.searchKeywords = undefined;

  this.setSearchKeywords();
  this.resetFeed();
}

/**
 * Builds a YouTube gdata query for the keyword
 * @param {Keyword} keyword
 * @return {String}
 */
KeywordsProfile.buildKeywordQuery = function(keyword) {
  var vq = undefined;
  if (typeof(keyword) == 'string') {
    vq = keyword;
  } else {
    vq = keyword.name;
    if (keyword.relatedKeywords.length > 0) {
      vq += '|' + keyword.relatedKeywords.join('|');
    }
  }

  var query = YOUTUBE_SEARCH_FEED + YOUTUBE_COMMON_PARAMETERS +
      '&vq=' + encodeURIComponent(vq);

  return query;
};

KeywordsProfile.prototype.setSearchKeywords = function(opt_terms) {
  if (opt_terms && opt_terms.length > 0) {
    var query = Util.trimWhitespace(opt_terms);
    this.searchKeywords = query.replace(/\s/g, '+');
  } else {
    this.searchKeywords = undefined;
  }

  this.searchQuery = new Query();
};

KeywordsProfile.prototype.isSearchMode = function() {
  return this.searchKeywords !== undefined;
};

KeywordsProfile.prototype.buildSearchQuery = function() {
  return KeywordsProfile.buildKeywordQuery(this.searchKeywords);
};

KeywordsProfile.prototype.resetFeed = function() {
  this.feedQuery = new Query(YOUTUBE_STANDARD_FEED + '[FEED_NAME]?' +
                             YOUTUBE_COMMON_PARAMETERS);
};

/**
 * Builds the query used to get videos from the standard feed.
 */
KeywordsProfile.prototype.buildFeedQuery = function(feed, opt_size,
    opt_isAll) {
  var feedQuery = undefined;
  var getCurrent = false;
  switch (feed) {
    case strings.FEATURED:
      feedQuery = 'recently_featured';
      break;
    case strings.MOST_VIEWED:
      feedQuery = 'most_viewed';
      getCurrent = true;
      break;
    case strings.MOST_DISCUSSED:
      feedQuery = 'most_discussed';
      getCurrent = true;
      break;
    case strings.TOP_FAVORITED:
      feedQuery = 'top_favorites';
      getCurrent = true;
      break;
    default:
      feedQuery = 'recently_featured';
  }

  var baseUrl = this.feedQuery.baseUrl.replace('[FEED_NAME]', feedQuery);

  var query = baseUrl;

  if (getCurrent) {
    query += '&time=today';
  }

  return query;
};
