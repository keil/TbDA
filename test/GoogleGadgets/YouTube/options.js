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
 * @fileoverview Helper functions to simplify fetching saved user preferences.
 */

/**
 * Static object
 * @constructor
 */
function Options() {}

/**
 * Key used to store currently showing feed/stream.
 * @type String
 */
Options.FEED_KEY = 'feed';

options.putDefaultValue(Options.FEED_KEY, strings.FEATURED);

/**
 * API callback to create menu items
 * @param {Menu} menu http://desktop.google.com/dev/gadget_apiref.html#menu
 */
Options.onAddMenuItems = function(menu) {
  menu.AddItem(strings.MENU_REFRESH, 0, _onRefreshButton);

  menu.AddItem("-", 0x800, 0); // Separator

  var feed = options.getValue(Options.FEED_KEY);

  // Youtube standard video feeds
  menu.AddItem(strings.FEATURED,
               feed == strings.FEATURED ? gddMenuItemFlagChecked : 0,
               Options.switchFeed);
  menu.AddItem(strings.MOST_VIEWED,
               feed == strings.MOST_VIEWED ? gddMenuItemFlagChecked : 0,
               Options.switchFeed);
  menu.AddItem(strings.MOST_DISCUSSED,
               feed == strings.MOST_DISCUSSED ? gddMenuItemFlagChecked : 0,
               Options.switchFeed);
  menu.AddItem(strings.TOP_FAVORITED,
               feed == strings.TOP_FAVORITED ? gddMenuItemFlagChecked : 0,
               Options.switchFeed);
};

/**
 * @return {String} String describing the current feed chosen (same as the menu
 *     item string).
 */
Options.currentFeed = function() {
  var value = options.getValue(Options.FEED_KEY);
  assert(value !== null && value.length > 0);
  return value;
};

Options.switchFeed = function(value) {
  options.putValue(Options.FEED_KEY, value);

  if (isSearchMode()) {
    exitSearchMode();
  }

  switchFeed(value);
};
