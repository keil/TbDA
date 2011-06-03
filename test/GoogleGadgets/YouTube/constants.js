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
 * @fileoverview Constants used in the youtube gadget.
 */

/**
* The height of the DIV that holds a video
* @type Number
*/
var ITEM_HEIGHT = 64;
/**
* Minimum height of the gadget
* @type Number
*/
var MIN_GADGET_HEIGHT = 137;
/**
* Minimum width of the gadget
* @type Number
*/
var MIN_GADGET_WIDTH = 85;
/**
* Amount of time to fade in milliseconds
* @type Number
*/
var FADE_TIMER_MS = 500;
/**
* Amount of time for each new item to fade in milliseconds
* @type Number
*/
var ITEM_FADE_TIME_MS = 100;
/**
* Interval between refreshing new content from youtube
* @type Number
*/
var tempRefreshInterval = 1 * 60 * 60 * 1000;  // 1 hour
var CONTENT_REFRESH_INTERVAL_MS = tempRefreshInterval; // to avoid compiler warning
var NETWORK_CHECK_INTERVAL_MS = 60 * 1000;
/**
* If we should avoid refreshing the videos, this is the timeout to try
* refreshing again.
* @type Number
*/
var RESTART_VIDEO_TIMEOUT_MS = 1 * 60 * 1000;  // One minute
/**
* Offset for the width of the content div
* @type Number
*/
var CONTENT_WIDTH_OFFSET = 5;
/**
* Offset for the text in a docked listitem
* @type Number
*/
var DOCKED_TEXT_X = 68;
/**
* Offset for the text in a undockedlistitem
* @type Number
*/
var UNDOCKED_TEXT_X = 78;
/**
* Horizontal padding between the view and content
* @type Number
*/
var CONTENT_WIDTH_PADDING = 23;
/**
* Vertical padding between the view and content
* @type Number
*/
var CONTENT_HEIGHT_PADDING = 43;
/**
* Refresh Button's padding on the right edge, relative to the view
* @type Number
*/
var REFRESH_RIGHT_PADDING = 21;
/**
* Height of the images on the top of the gadget, used for offset
* @type Number
*/
var TOP_HEIGHT = 152;
/**
* Height of the images on the bottom of the gadget, used for offset
* @type Number
*/
var BOTTOM_HEIGHT = 53;
/**
* Width of the images on the left and right of the gadget, used for offset
* @type Number
*/
var LEFT_WIDTH = 26;
/**
* Width of the images on the right of the gadget, used for offset
* @type Number
*/
var RIGHT_WIDTH = 14;
/**
* Padding around the searchbox
* @type Number
*/
var SEARCHBOX_RIGHT_PADDING = 9;
var SEARCHBOX_LEFT_PADDING = 7;
var SEARCHBOX_BOTTOM_PADDING = 11;
var SEARCHCLOSE_RIGHT_PADDING = 12;
