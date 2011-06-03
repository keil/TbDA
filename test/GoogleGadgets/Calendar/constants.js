// Copyright (C) 2008 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Constants used in calendar gadget.
 */

var CALENDAR_URL = 'https://calendar.google.com/';
var CALENDAR_FEED_URL = 'https://www.google.com/calendar/feeds/default?alt=json';
var CALENDAR_OWN_FEED_URL = 'http://www.google.com/calendar/feeds/default/owncalendars/full?alt=json';
var CALENDAR_POST_URL =
    'https://www.google.com/calendar/feeds/default/private/full';
var CALENDAR_HOSTED = 'https://www.google.com/calendar/hosted/[DOMAIN]/render';
var GOOGLE_CLIENT = 'google-calendargadget';

// Days of the week. Shortened version.
var WEEK_DAYS_SHORT = [strings.DAY_SUN_S, strings.DAY_MON_S, strings.DAY_TUE_S,
                       strings.DAY_WED_S, strings.DAY_THU_S, strings.DAY_FRI_S,
                       strings.DAY_SAT_S];

// Days of the week. 3-letter version.
var WEEK_DAYS_MEDIUM = [strings.DAY_SUN_M, strings.DAY_MON_M, strings.DAY_TUE_M,
                        strings.DAY_WED_M, strings.DAY_THU_M, strings.DAY_FRI_M,
                        strings.DAY_SAT_M];

// Days of the week.
var WEEK_DAYS = [strings.DAY_SUN, strings.DAY_MON, strings.DAY_TUE,
                 strings.DAY_WED, strings.DAY_THU, strings.DAY_FRI,
                 strings.DAY_SAT];

// Months of the year.
var MONTHS = [strings.MONTH_JAN, strings.MONTH_FEB, strings.MONTH_MAR,
              strings.MONTH_APR, strings.MONTH_MAY, strings.MONTH_JUN,
              strings.MONTH_JUL, strings.MONTH_AUG, strings.MONTH_SEP,
              strings.MONTH_OCT, strings.MONTH_NOV, strings.MONTH_DEC];

var MONTHS_SHORT = [strings.MONTH_JAN_S, strings.MONTH_FEB_S,
                    strings.MONTH_MAR_S, strings.MONTH_APR_S,
                    strings.MONTH_MAY_S, strings.MONTH_JUN_S,
                    strings.MONTH_JUL_S, strings.MONTH_AUG_S,
                    strings.MONTH_SEP_S, strings.MONTH_OCT_S,
                    strings.MONTH_NOV_S, strings.MONTH_DEC_S];

// Options constants
var OPTIONS = {
  MAIL: 'email',
  PASSWORD: 'password',
  REMEMBER: 'remember',
  HOUR24: '24hours',
  VIEW: 'currentView',
  DAYVIEW: 'dayview',
  CALENDARVIEW: 'calendarview',
  AGENDAVIEW: 'agendaview',
  WEEKSTART: 'weekstart',
  AUTH: 'authtoken',
  SID: 'sid',
  LSID: 'lsid',
  CLOSE_DETAILS: 'close-details-view',
  QUICKADD: 'quickaddtext',
  CALENDAR: 'calendarData',
  EVENT: 'eventData',
  NEW_EVENT: 'newEventData',
  UPDATE_EVENT: 'updateEvent',
  SHOW: 'show-',
  HIDE: 'hideviews',
  LAST_REMINDER: 'lastReminder',
  USE_QUICK_ADD: 'useQuickAdd',
  DATE: 'currentDate',
  USERZIP: 'userZipCode',
  UPDATE_RSVP_EVENT: 'rsvpEvent',
  UPDATE_RSVP_CALENDAR: 'rsvpCalendar',
  UPDATE_RSVP_TRIGGER: 'rsvpUpdateTrigger',
  UPGRADE: 'mandatoryUpgrade',
  UPGRADE_REASON: 'upgradeReason',
  UPGRADE_URL: 'upgradeUrl',
  UPGRADE_INFO: 'upgradeInfo',
  CALENDARDATA: 'calendarJson',
  UPDATE_VIEW: 'updateView'
};

// Days the week can start on.
var START_MONDAY = 1;
var START_SUNDAY = 0;
var START_SATURDAY = -1;
