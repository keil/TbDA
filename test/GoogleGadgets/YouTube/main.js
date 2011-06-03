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

debug.info('------ new yt instance -------');

/**
 * Queue of videos waiting to be displayed
 * @type Array<YouTubeVideo>
 */
var gl_videosQueue = [];
/**
 * Map of video data for videos currently shown in the gadget.
 * @type Object<YouTubeVideo>
 */
var gl_videoData = {};
/**
 * Number of video search requests that we are expecting from YouTube
 * @type Number
 */
var gl_pendingResponses = undefined;
/**
 * Number of thumbnail requests that we are expecting from YouTube
 * @type Number
 */
var gl_pendingThumbnails = undefined;
/**
 * Flag if we recieve a double click. This is used to supress the single click
 * function being called when we get a double click.
 * @type Boolean
 */
var gl_receivedDblclick = false;
/**
 * Used to determine if the details view is visible or not. This helps when a
 * user clicks an item for the second time, we close the details pane instead
 * of reopening it.
 * @type Number
 */
var gl_currentItemDetailsView = -1;
/**
 * The url of the currently selected video (used for details view).
 * @type String
 */
var gl_selectedVideoUrl = undefined;
/**
 * @type Object<KeywordsProfile>
 */
var gl_keywordProfile = new KeywordsProfile();
/**
 * Animation timer ID used to fade listbox in and out. Set when an animation is
 * in progress.
 * @type Number
 */
var gl_contentAnimationTimer = undefined;
/**
 * Set to true if the user is currently interacting with the gadget in any way
 * @type Boolean
 */
var gl_isUserInteracting = false;
/**
 * Timeout ID used to try refreshing the videos again if it currently needs to
 * be avoided.
 * @type Number
 */
var gl_restartVideoRefreshTimeout = undefined;
/**
 * Animation Timer ID used to fade the status label in and out.
 * @type Number
 */
var gl_statusAnimationTimer = undefined;
/**
 * Timer ID used to delete old displayed videos to prevent the list from being
 * too long
 * @type Number
 */
var gl_removeVideosTimer = undefined;

/**
 * Current number of video refreshes made (resets once in half an hour)
 * @type Number
 */
var gl_videoRequestCount = 0;

var gl_poppedOut = false;

/**
 * Offload initializing the gadget so the gadget display is refreshed to the
 * right size first.
 */
function _onOpen() {
  updateStatus(strings.UPDATING_VIDEOS);
  view.setTimeout(init, 200);
}

function _onPopin() {
  gl_poppedOut = false;
}

function _onPopout() {
  gl_poppedOut = true;
  view.setTimeout(_onSize, 200);
}

function setScrollImages() {
  var listbox = undefined;
  if (content.visible === true && content.scrollbar !== null) {
    listbox = content;
  }

  if (searchresults.visible === true && searchresults.scrollbar !== null) {
    listbox = searchresults;
  }

  if (listbox) {
    listbox.scrollbar.thumbImage = "images/main/scrollbar.png";
    listbox.scrollbar.thumbDownImage = "images/main/scrollbar.png";
    listbox.scrollbar.thumbOverImage = "images/main/scrollbar.png";
    listbox.scrollbar.background = "images/main/scrollbar_track.png";
  }
}

function _logo_onclick() {
  framework.openUrl('http://www.youtube.com');
}

var g_versionChecker;
var VERSION_INFO_URL = 'http://desktop.google.com/plugins/versions/youtube.txt';

var g_contentRefreshTimer;

/**
 * Initial setup, creates a dummy xml details view to set the default size of
 * the actual details view that will contain the videos. Loads the map of
 * videos that have been previously watched and begin gathering the first
 * set of videos.
 */
function init() {
  _onDock();

  getFreshVideos(true);
  updateFeedDescription();
  exitSearchMode();

  g_contentRefreshTimer = view.setInterval(getFreshVideos,
      CONTENT_REFRESH_INTERVAL_MS);

  plugin.onAddCustomMenuItems = Options.onAddMenuItems;

  var feed = Options.currentFeed();
  setDescriptionText(feed);
  view.setInterval(function() { gl_videoRequestCount = 0; }, 30 * 60 * 1000);

  g_versionChecker = new VersionChecker(strings.VERSION_STRING,
      VERSION_INFO_URL, onMandatoryUpgrade);
}

function onMandatoryUpgrade(upgradeInfo) {
  debug.trace('Received mandatory upgrade notice.');

  plugin.onAddCustomMenuItems = null;

  killTimers();

  feed_select.visible = false;
  content.visible = false;
  searchresults.visible = false;
  messageDiv.visible = false;
  searchbox.visible = false;
  drop_arrow_container.y = -200;
  description.y = -200;

  upgradeReason.innerText = upgradeInfo.reason;
  upgradeInfoUrl.href = upgradeInfo.infoUrl;
  upgradeDownloadUrl.href = upgradeInfo.downloadUrl;

  upgradeDiv.visible = true;
  updateStatus(strings.PLEASE_UPGRADE);
}

function displayMessage(message, isShowLink) {
  messageDiv.visible = true;
  messageLabel.innerText = message;
  messageRefreshLink.visible = isShowLink;
}

function killTimers() {
  view.clearInterval(g_contentRefreshTimer);
  view.clearTimeout(gl_networkCheckTimeout);
  view.clearTimeout(gl_restartVideoRefreshTimeout);
}

function setDescriptionText(text) {
  description.innerText = text;
  descriptionSizer.value = text;

  // It's okay if this is not i18n. It's a hack for this particular
  // english string.
  if (text == 'Most discussed videos') {
    drop_arrow.x = 110;
  } else {
    drop_arrow.x = descriptionSizer.idealBoundingRect.width;
  }
}

/**
 * Called when the gadget is closing, save all of the viewed videos so they
 * don't get displayed again.
 */
function _onClose() {
}

/**
 * Resize function which handles the sizing to keep the resizable gadget
 * looking good.
 */
function _onSize() {
  // Goes through all visible videos and adjusts the width of the contents.
  var width = view.width;
  var height = view.height;

  // Adjust the position of the rest of the elements
  // search box
  searchbox.width = width - SEARCHBOX_RIGHT_PADDING - SEARCHBOX_LEFT_PADDING;
  searchbox.y = height - SEARCHBOX_BOTTOM_PADDING - searchbox.height;
  searchfield_middle.width = searchbox.width - searchfield_right.width;
  searchfield_right.x = searchbox.width - searchfield_right.width;
  search_close.x = searchbox.width - SEARCHCLOSE_RIGHT_PADDING;
  searchfield.width = searchbox.width - search_close.width - searchfield.x;

  // list box - horizontal
  content.width = width - CONTENT_WIDTH_PADDING;
  searchresults.width = content.width;
  top_middle.width = width - LEFT_WIDTH;
  top_right.x = width - RIGHT_WIDTH;
  middle_middle.width = width - LEFT_WIDTH;
  middle_right.x = width - RIGHT_WIDTH;
  bottom_middle.width = width - LEFT_WIDTH;
  bottom_right.x = width - RIGHT_WIDTH;
  status.width = width - status.x - RIGHT_WIDTH;
  description.width = width - status.x - RIGHT_WIDTH;
  drop_arrow_container.width = width - drop_arrow_container.x - RIGHT_WIDTH;
  feed_select.width = width - feed_select.x - RIGHT_WIDTH;

  // list box - vertical
  content.height = height - CONTENT_HEIGHT_PADDING;
  content.height = content.height - searchbox.height;
  searchresults.height = content.height;

  upgradeDiv.width = width - CONTENT_WIDTH_PADDING;
  upgradeDiv.height = content.height + searchbox.height;

  messageDiv.width = content.width;
  messageDiv.height = content.height;

  middle_left.height = height - TOP_HEIGHT;
  middle_middle.height = height - TOP_HEIGHT;
  middle_right.height = height - TOP_HEIGHT;
  bottom_left.y = height - BOTTOM_HEIGHT;
  bottom_middle.y = height - BOTTOM_HEIGHT;
  bottom_right.y = height - BOTTOM_HEIGHT;

  if (content.scrollbar && content.scrollbar.visible) {
    content.itemwidth = content.width - content.scrollbar.offsetWidth;
  } else {
    content.itemwidth = content.width;
  }

  if (searchresults.scrollbar && searchresults.scrollbar.visible) {
    searchresults.itemwidth = searchresults.width -
        searchresults.scrollbar.offsetWidth;
  } else {
    searchresults.itemwidth = searchresults.width;
  }

  var item_width_offset;

  for (var i = 0; i < content.children.count; ++i) {
    var item = content.children.item(i);
    item_width_offset = content.itemwidth - item.children('title').x;
    item.children('title').width = item_width_offset;
    item.children('desc').width = item_width_offset;
    item.children('view_length').width = item_width_offset;
  }

  for (i = 0; i < searchresults.children.count; ++i) {
    item = searchresults.children.item(i);
    item_width_offset = searchresults.itemwidth - item.children('title').x;
    item.children('title').width = item_width_offset;
    item.children('desc').width = item_width_offset;
    item.children('view_length').width = item_width_offset;
  }

  setScrollImages();
}

function _onSizing() {
  if (event.width < MIN_GADGET_WIDTH) {
    event.width = MIN_GADGET_WIDTH;
  }

  if (event.height < MIN_GADGET_HEIGHT) {
    event.height = MIN_GADGET_HEIGHT;
  }
}

/**
 * Called when the refresh button is pressed. Displays an updating message and
 * starts a video refresh.
 */
function _onRefreshButton() {
  updateStatus(strings.UPDATING_VIDEOS);
  getFreshVideos(true);

  if (gl_selectedVideoUrl > 0) {
    // Clear state if a details view is currently open
    onDetailsViewClose();
  }
}

/**
 * Mark the start of user interacting with the gadget
 */
function _onMouseOver() {
  gl_isUserInteracting = true;
}

/**
 * Mark the end of user interacting with the gadget
 */
function _onMouseOut() {
  if (gl_currentItemDetailsView < 0) {
    gl_isUserInteracting = false;
  }
}

function _search_onKeydown() {
  if (event.keycode == 13 && // enter key
      searchfield.value !== null && searchfield.value.length > 0) {
    enterSearchMode();
    gl_keywordProfile.setSearchKeywords(searchfield.value);
    searchresults.removeAllElements();
    getFreshVideos(true);
  } else if (event.keycode == 27) {  // ESC key
     _search_reset();
  }
}

function _search_onfocusout() {
  if (!Util.trimWhitespace(searchfield.value)) {
    _search_reset();
  }
}

function _search_reset() {
  searchfield.value = strings.SEARCH;
  searchfield.color = 'gray';
  search_close.visible = false;
  frame.focus();

  exitSearchMode();
}

/**
 * Compiler workaround -- sets the focus to the searchfield
 */
function _search_activate() {
  searchfield.focus();
  if (searchfield.value != strings.SEARCH) {
    return;
  }
  searchfield.value = '';
  searchfield.color = 'black';
  search_close.visible = true;
  hideSelect();
}

function enterSearchMode() {
  if (searchresults.children.count > 0) {
    searchresults.visible = true;
    content.visible = false;
    messageDiv.visible = false;
  }
  setDescriptionText(strings.SEARCH_TITLE);
}

function exitSearchMode() {
  // We don't clear all the search content, because we might need it again
  // later (e.g. if the searchfield is enabled again).
  gl_keywordProfile.setSearchKeywords();
  updateFeedDescription();
  searchresults.visible = false;
  content.visible = true;
  messageDiv.visible = false;

  searchfield.killfocus();
}

function updateFeedDescription() {
  var feed = Options.currentFeed();
  setDescriptionText(feed);
}

function switchFeed(feed) {
  _search_reset();
  updateFeedDescription();
  gl_keywordProfile.resetFeed();
  content.removeAllElements();
  getFreshVideos(true);
}

function colorItem(item) {
  var title = item.children('title');
  title.color = '#FFFFFF';
  var desc = item.children('desc');
  desc.color = '#999999';
  var view_length = item.children('view_length');
  view_length.color = '#999999';
}

function resizeItem(item) {
  var image = item.children('image');
  image.x = 1;
  image.y = 1;
  image.width = 45;
  image.height = 35;

  var title = item.children('title');
  title.x = 50;
  title.y = 1;
  title.height = 17;
  title.size = 8;
  title.vAlign = 'bottom';
  title.wordWrap = false;
  title.trimming = 'character-ellipsis';

  var desc = item.children('desc');
  desc.visible = false;

  var view_length = item.children('view_length');
  view_length.x = 50;
  view_length.y = 19;
  view_length.size = 7;
  view_length.height = 12;
  view_length.visible = true;
  view_length.vAlign = 'top';
}

function _onDock() {
  view.setTimeout(_onSize, 200);
}

function _onUndock() {
  _onPopout();
}

/**
 * Display a message at the top of the gadget
 * @param {String} message The string to display
 */
function updateStatus(message) {
  if (status.innerText != message) {
    status.innerText = message;
  }

  if (gl_statusAnimationTimer) {
    gl_statusAnimationTimer = view.cancelAnimation(gl_statusAnimationTimer);
  }

  gl_statusAnimationTimer = view.beginAnimation(animateStatusFade,
      status.opacity,
      gddElementMaxOpacity,
      FADE_TIMER_MS);

  // Don't show the description and stats at the same time
  description.visible = false;
  drop_arrow_container.visible = false;
}

/**
 * Removes the message at the top of the gadget
 */
function clearStatus() {
  if (gl_statusAnimationTimer) {
    gl_statusAnimationTimer = view.cancelAnimation(gl_statusAnimationTimer);
  }

  gl_statusAnimationTimer = view.beginAnimation(animateStatusFade,
      status.opacity,
      gddElementMinOpacity,
      FADE_TIMER_MS);
  view.setTimeout(function() {
      status.innerText = '';
      description.visible = true;
      drop_arrow_container.visible = true;
  }, FADE_TIMER_MS);
}

gl_networkCheckTimeout = null;

/**
 * Prepares the gadget to receive new content (might clear the contents if
 * we're in 'shuffle' mode). Initiates 2 queries to youtube - one for the
 * popular videos of the day, one for a personalized list for the user.
 * Cancels the refresh if a video is being watched, but will start a retry
 * timeout.
 * @param {Boolean} opt_force Forces the list to be refreshed
 */
function getFreshVideos(opt_force) {
  if (!framework.system.network.online) {
    if (opt_force) {
      if (gl_networkCheckTimeout) {
        view.clearTimeout(gl_networkCheckTimeout);
      }
      gl_networkCheckTimeout = view.setTimeout(function() {
        getFreshVideos(true);
      }, NETWORK_CHECK_INTERVAL_MS);
    }

    return;
  } else {
    if (gl_networkCheckTimeout) {
      view.clearTimeout(gl_networkCheckTimeout);
    }
  }

  if (!opt_force && (gl_isUserInteracting || system.user.idle)) {
    // Start a timer to restart the refresh
    if (gl_restartVideoRefreshTimeout) {
      view.clearTimeout(gl_restartVideoRefreshTimeout);
    }

    gl_restartVideoRefreshTimeout = view.setTimeout(getFreshVideos,
        RESTART_VIDEO_TIMEOUT_MS);

    return;
  }

  if (!opt_force) { // Don't retry too often
    gl_videoRequestCount += 1;
    if (gl_videoRequestCount > 5) {
      return;
    }
  }

  updateStatus(strings.UPDATING_VIDEOS);

  gl_pendingResponses = 0;
  gl_pendingThumbnails = 0;

  if (isSearchMode()) {
    // Search view
    var query = gl_keywordProfile.buildSearchQuery();
    YouTubeVideo.videoRequest(query, updateVideos);
  } else {
    // Regular view
    var feedQuery = undefined;
    var currentFeed = Options.currentFeed();
    feedQuery = gl_keywordProfile.buildFeedQuery(currentFeed, null, true);
    if (feedQuery) {
      debug.trace(feedQuery);
      YouTubeVideo.videoRequest(feedQuery, updateVideos);
    }
  }
}

function isSearchMode() {
  return gl_keywordProfile.isSearchMode();
}

/**
 * Prepare videos for display. Checks if they've been watched before, and
 * updates the display with the new videos if it's time to.
 * @param {Array<YouTubeVideo>} videosArray Array of videos returned from
 *     YouTube. Could be null if the request failed.
 */
function updateVideos(videosArray) {
  --gl_pendingResponses;

  if (videosArray) {
    if (videosArray.length) {
      gl_pendingThumbnails += videosArray.length;

      for (var i = 0; i < videosArray.length; ++i) {
        gl_videosQueue.push(videosArray[i]);
        videosArray[i].fetchStatusImage();
      }
    } else {
      displayMessage(strings.NO_VIDEOS_FOUND, false);
    }
  } else {
    displayMessage(strings.NETWORK_ERROR, true);
    clearStatus();
  }
}

/**
 * Checks if we're ready to display all the new content, or if we're still
 * waiting for more.
 */
function shouldDisplay() {
  if (gl_pendingResponses <= 0 && gl_pendingThumbnails <= 0) {
    displayVideos(gl_videosQueue);
  }
}

/**
 * Displays all of the videos in newVideos. In 'shuffle' mode, the entire
 * display is cleared out and replaced with the new items. Otherwise, each item
 * is faded in individually to simulate how it's done in the news panel. The
 * new items are either randomly added, or sorted by popularity depending on
 * user preferences.
 * NOTE: The elements are removed from newVideos as they are displayed
 * @param {Array<YouTubeVideo>} newVideos New yt videos to display
 */
function displayVideos(newVideos) {
  var searchmode = isSearchMode();
  searchresults.visible = searchmode;
  content.visible = !searchmode;
  messageDiv.visible = false;

  var listbox = searchmode ? searchresults : content;
  listbox.removeAllElements();

  gl_videoData = {};

  // Add each item to the head of the listbox
  var length = newVideos.length;
  for (var i = 0; newVideos.length > 0; ++i) {
    var video = newVideos.pop();
    var id = video.id;
    var itemXml = video.getItemXml();
    var newItem = undefined;
    if (listbox.children(0)) {
      newItem = listbox.insertElement(itemXml, listbox.children(0));
    } else {
      newItem = listbox.appendElement(itemXml);
    }

    assert(newItem !== null);
    if (newItem) {
      newItem.children('title').innerText = video.title;
      newItem.children('desc').innerText = video.description;
      newItem.children('view_length').innerText = video.getViewLength();
      var snippet = video.title + '\n\n' +
          video.description;
      newItem.children('title').tooltip = snippet;

      resizeItem(newItem);
      colorItem(newItem);

      var image = newItem.children('image');
      image.src = video.thumbnail.src;
      image.tooltip = snippet;
      gl_videoData[id] = video;
    }
  }

  // Resize the newly added items (it gets added without any sizing)
  view.setTimeout(_onSize, 200);

  clearStatus();
}

/////////////// List Item event handlers

/**
 * Display the details for this selected message.
 * @param {Number} itemId the message id of the item to be displayed
 */
function _showDetailsView(itemId) {
  if (gl_receivedDblclick) {
    return;
  }

  if (gl_currentItemDetailsView >= 0) {
    var shouldCloseDetailsView = itemId == gl_currentItemDetailsView;
    onDetailsViewClose();

    if (shouldCloseDetailsView) {
      plugin.CloseDetailsView();
      return;
    }
  }

  var curItem = getVideoById(itemId);

  gl_isUserInteracting = true;
  var xmlDetailsView = new DetailsView();
  xmlDetailsView.detailsViewData.putValue("closeDetailsView", closeDetailsView);
  xmlDetailsView.contentIsView = true;
  xmlDetailsView.setContent("", undefined, "details.xml", false, 0);

  if (curItem !== null && curItem.embeddedurl !== "") {
    debug.trace("embedurl: " + curItem.embeddedurl);
    xmlDetailsView.detailsViewData.putValue("curItem", curItem);
    gl_selectedVideoUrl = curItem.url;
  }

  // Show the details view.
  plugin.showDetailsView(xmlDetailsView,
                         curItem ? curItem.title : strings.VIDEO_ERROR,
                         gddDetailsViewFlagToolbarOpen +
                         gddDetailsViewFlagDisableAutoClose +
                         gddDetailsViewFlagNoFrame,
                         null);

  gl_currentItemDetailsView = itemId;
}

/**
 * Called when the user single clicks on an active element
 * @param {Number} itemId the message id of the item that was clicked on
 */
function _itemSingleClick(itemId) {
  gl_receivedDblclick = false;
  view.setTimeout('_showDetailsView(' + itemId + ');', 200);
}

/**
 * Takes in a message ID and opens the message in an external browser
 * @param {Number} itemId the message id of the item to be displayed
 */
function _itemDoubleClick(itemId) {
  gl_receivedDblclick = true;
  var curItem = getVideoById(itemId);
  framework.openUrl(curItem.url);
  content.clearSelection();
  searchresults.clearSelection();
}

/**
 * Cleans up state when the details view closes
 */
function onDetailsViewClose() {
  gl_currentItemDetailsView = -1;
  content.clearSelection();
  searchresults.clearSelection();
  gl_isUserInteracting = false;
}

/**
 * Gets the YouTubeVideo of a video associated by its unique ID.
 * @param {Number} id ID of the video to get
 * @return {YouTubeVideo} object for the video, if not found null is returned
 */
function getVideoById(id) {
  return gl_videoData[id];
}

/**
 * Closes the details view and cleans up gadget state. Used as a callback from
 * the details view widgets.
 */
function closeDetailsView() {
  plugin.CloseDetailsView();
  onDetailsViewClose();
}

/**
 * Given a video ID, returns the DIV associated with this ID.
 * @param {Number} id YouTubeVideo Id of the video
 * @return {Object} id The div associated with the video ID
 */
function getDivById(id) {
  return content.children.item("video_" + id);
}

/**
 * Sort function to sort a list of Videos by the view count
 * @param {YouTubeVideo} word_a The first video to compare
 * @param {YouTubeVideo} word_b The second video to compare
 * @return {Number} the result of the comparison. > 0 if b is greater than a, < 0
 *    if a is greater than b and 0 if they are equal.
function sortViews(word_a, word_b) {
  return word_b.viewCount - word_a.viewCount;
}
 */

/**
 * Fade the status text at the top of the gadget according to the event value
 */
function animateStatusFade() {
  status.opacity = event.value;
}

/**
 * Fade the content list item
 * @param {Number} id YouTubeVideo Id of the item to be animated
 */
function _animateItem(container, id) {
  var item = container.children('video_' + id);
  if (item) {
    item.opacity = event.value;
  }
}

function _toggleSelect() {
  if (feed_select.visible) {
    hideSelect();
  } else {
    showSelect();
  }
}

function showSelect() {
  feed_select.visible = true;
}

function hideSelect() {
  feed_select.visible = false;
}

function _onSelectOver() {
  event.srcElement.background = '#2a2a2a';
}

function _onSelectOut() {
  event.srcElement.background = '#000000';
}

function _onSelectClick(value) {
  if (Options.currentFeed() != value || isSearchMode()) {
    Options.switchFeed(value);
  }

  hideSelect();
}
