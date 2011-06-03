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
 * @fileoverview Functions for the main view of the gadget.
 */

var g_calendarGadget = null;

var g_auth = null;
var g_cache = new Cache();
var g_errorMessage = null;
var g_events = null;
var g_uiAgenda = null;
var g_uiCal = null;
var g_uiDayView = null;

CalendarGadget.VERSION_CHECK_URL =
    'http://desktop.google.com/plugins/versions/calendar.txt';

/**
 * Class for the main gadget. Handles gadget events and syncs the views.
 * @constructor
 */
function CalendarGadget() {
  // Attach events to view
  view.onsizing = Utils.bind(this.scheduleResize, this);
  view.onsize = Utils.bind(this.resizeDesign, this);
  view.ondock = Utils.bind(this.resize, this);
  view.onundock = Utils.bind(this.resize, this);

  this.shiftDown_ = false;
  this.ctrlDown_ = false;

  this.detailsView = null;
  this.redrawTimer = null;
  this.versionChecker = new VersionChecker(strings.GADGET_VERSION,
      CalendarGadget.VERSION_CHECK_URL,
      Utils.bind(this.onMandatoryUpgrade, this));
  this.ownCalendarsUrls = {};
}

/**
 * Constants for the minimum size of the gadget.
 */
CalendarGadget.prototype.GADGET_MIN_WIDTH = 135;
CalendarGadget.prototype.GADGET_MIN_HEIGHT = 155;

/**
 * Initialize and run the gadget. This method handles setup of the gadget and
 * correct initialization of all needed objects.
 */
CalendarGadget.prototype.run = function() {
  plugin.onAddCustomMenuItems = Utils.bind(this.addMenuItems, this);
  view.onoptionchanged = Utils.bind(this.onOptionChanged, this);

  g_errorMessage = new ErrorMessage();

  g_events = new Events();
  g_events.onEventsReceived = Utils.bind(this.onEventsReceived, this);

  g_auth = new Auth();
  g_auth.onLoginFailure = Utils.bind(this.onLoginFailure, this);
  g_auth.onLoginSuccess = Utils.bind(this.onLoginSuccess, this);

  g_auth.login();

  // Keyboard interaction for login screen
  user.onkeyPress = Utils.bind(this.loginKeyPress, this, user.name);
  user.onkeydown = Utils.bind(this.onKeyDown, this, user.name);
  user.onkeyup = Utils.bind(this.onKeyUp, this, user.name);
  pass.onkeypress = Utils.bind(this.loginKeyPress, this, pass.name);
  pass.onkeydown = Utils.bind(this.onKeyDown, this, pass.name);
  pass.onkeyup = Utils.bind(this.onKeyUp, this, pass.name);
  remember.onkeypress = Utils.bind(this.loginKeyPress, this, remember.name);
  remember.onkeydown = Utils.bind(this.onKeyDown, this, remember.name);
  remember.onkeyup = Utils.bind(this.onKeyUp, this, remember.name);
  remember.onfocusin = Utils.bind(this.onRememberFocus, this, true);
  remember.onfocusout = Utils.bind(this.onRememberFocus, this, false);
  captcha.onkeypress = Utils.bind(this.loginKeyPress, this, captcha.name);
  captcha.onkeydown = Utils.bind(this.onKeyDown, this, captcha.name);
  captcha.onkeyup = Utils.bind(this.onKeyUp, this, captcha.name);
  login.onclick = Utils.bind(this.doLogin, this);
  login.onkeypress = Utils.bind(this.loginKeyPress, this, login.name);
  login.onkeydown = Utils.bind(this.onKeyDown, this, login.name);
  login.onkeyup = Utils.bind(this.onKeyUp, this, login.name);
  login.onfocusin = Utils.bind(this.onLoginFocus, this, true);
  login.onfocusout = Utils.bind(this.onLoginFocus, this, false);

  // Footer links/buttons
  linkToday.onclick = Utils.bind(this.goToday, this);
  linkAddEvent.onclick = Utils.bind(this.quickAddEvent, this);
  linkOptions.onclick = Utils.bind(this.showOptions, this);

  g_uiAgenda = new Agenda(agendaDiv);
  g_uiAgenda.onDateSelected = Utils.bind(this.onDateSelected, this);

  g_uiDayView = new DayView(dayviewDiv);
  g_uiDayView.onDateSelected = Utils.bind(this.onDateSelected, this);

  g_uiCal = new MiniCalendar(miniCalendarDiv);
  g_uiCal.onDateSelected = Utils.bind(this.onDateSelected, this);
  g_uiCal.onCalendarResized = Utils.bind(this.onCalendarResized, this);

  this.goToday();

  newAccountLink.href = 'https://www.google.com/accounts/NewAccount' +
      '?service=cl&passive=true&nui=1' +
      '&continue=' +
      encodeURIComponent('https://www.google.com/calendar/render?tab=wc') +
      '&followup=' +
      encodeURIComponent('https://www.google.com/calendar/render?tab=wc');
  this.resize();
};

/**
 * Schedule the resizing and clear old timeouts. This is used to minimize 
 * the amount of redraws in the gadget when the gadget is resized. 
 */
CalendarGadget.prototype.scheduleResize = function() {
  if (event && event.width && event.height) {
    if (event.width < this.GADGET_MIN_WIDTH) {
      event.width = this.GADGET_MIN_WIDTH;
    }
    if (event.height < this.GADGET_MIN_HEIGHT) {
      event.height = this.GADGET_MIN_HEIGHT;
    }
  }

  // onSizing gets fired when gadget is dragged. But since we won't change
  // we do not need to redraw!
  if (view.height == event.height && view.width == event.width) {
    return;
  }

  this.resizeDesign();
  this.resizeBlueDialog();

  clearTimeout(this.redrawTimer);
  this.redrawTimer = setTimeout(Utils.bind(this.resize, this), 10);
};

/**
 * Draw the gadget to fit the new size.
 * IMPORTANT: Both files ui_main.js and ui_main_resize.js must be
 * added to the gadget since all the resize functions are in the
 * ui_main_resize.js to improve readibility.
 */
CalendarGadget.prototype.resize = function() {
  this.resizeDesign();

  // Differentiate between dayview and calendar view.
  switch (options.getValue(OPTIONS.VIEW)) {
    case OPTIONS.DAYVIEW:
        this.resizeDayview();
        break;
    case OPTIONS.CALENDARVIEW:
        this.resizeCalendar();
        break;
    case OPTIONS.AGENDAVIEW:
        this.resizeAgenda();
        break;
  }

  this.resizeBlueDialog();
  this.resizeLoginForm();
  this.resizeUpgradeDialog();
};

/**
 * Callback function when login failed.
 * @param {Auth} auth Instance of authentication class
 */
CalendarGadget.prototype.onLoginFailure = function(auth) {
  login.color = '#000000';
  login.enabled = true;
  Utils.hideLoading();
  debug.error('Login failed: ' + auth.authResponse.Error);

  switch (auth.authResponse.Error) {
    case auth.OFFLINE:
        this.showErrorMsg(strings.OFFLINE);
        break;
    case auth.NO_CREDENTIALS:
        break;
    case auth.MANDATORY_UPGRADE:
        this.showUpgrade();
        return;
        break;
    case auth.BAD_AUTHENTICATION:
        this.showErrorMsg(strings.ERROR_BAD_AUTHENTICATION);
        break;
    case auth.NOT_VERIFIED:
        this.showErrorMsg(strings.ERROR_NOT_VERIFIED);
        framework.openUrl(CALENDAR_URL);
        break;
    case auth.TERMS_NOT_AGREED:
        this.showErrorMsg(strings.ERROR_WEBLOGIN_REQUIRED);
        framework.openUrl(CALENDAR_URL);
        break;
    case auth.CAPTCHA_REQUIRED:
        var captchaUrl = auth.CAPTCHA_PAGE +
                         auth.authResponse.CaptchaUrl;
        this.showLogin(captchaUrl, true);
        return;
        break;
    case auth.UNKNOWN:
        this.showErrorMsg(strings.ERROR_UNKNOWN);
        break;
    case auth.ACCOUNT_DISABLED:
    case auth.SERVICE_DISABLED:
        this.showErrorMsg(strings.ERROR_ACCOUNT_DISABLED_OR_DELETED);
        break;
    case auth.SERVICE_UNAVAILABLE:
        this.showErrorMsg(strings.ERROR_SERVICE_UNAVAILABLE);
        break;
    default:
        break;
  }
  this.showLogin(null, false);
};

/**
 * Callback function when login is successful.
 * @param {Auth} auth Instance of authentication class
 */
CalendarGadget.prototype.onLoginSuccess = function(auth) {
  Utils.hideLoading();
    // Retrieve only self-owned calendars at first.
  g_events.onCalendarsReceived = Utils.bind(this.onOwnCalendarsReceived, this);
  g_events.getUserCalendars(CALENDAR_OWN_FEED_URL);
  g_events.startTimer();
  this.goToday();
};

/**
 * Callback function for the date selection of the different
 * view objects (calendar, dayview, agenda). Syncs up the 
 * different elements.
 */
CalendarGadget.prototype.onDateSelected = function() {
  switch (options.getValue(OPTIONS.VIEW)) {
    case OPTIONS.CALENDARVIEW:
        g_uiAgenda.setDate(g_uiCal.value);
        g_uiDayView.setDate(g_uiCal.value);
        break;
    case OPTIONS.DAYVIEW:
        g_uiAgenda.setDate(g_uiDayView.value);
        g_uiCal.setDate(g_uiDayView.value);
        break;
    case OPTIONS.AGENDAVIEW:
        g_uiCal.setDate(g_uiAgenda.value);
        g_uiDayView.setDate(g_uiAgenda.value);
        break;
  }
  this.resize();
};

CalendarGadget.prototype.processCalendar = function(cal) {
    g_events.setCalendarMinutes(cal, true);
    debug.info('Calendar parsed: ' + cal.url);
    g_cache.addCalendar(cal);

    var d = new Date();
    var start = new Date(d.getFullYear(), d.getMonth(), 1);
    var end = new Date(d.getFullYear(), d.getMonth() + 1, 1);

    g_events.getEventsFromServer(cal, start, end);
};

/**
 * Callback function after all calendars were received.
 */
CalendarGadget.prototype.onOwnCalendarsReceived = function(data) {
  g_cache.clearCalendarCache();
  var entriesLength = data.feed.entry.length;
  for (var i = 0; i < entriesLength; ++i) {
    var cal = new Calendar();
    cal.parse(data.feed.entry[i]);
    this.processCalendar(cal);

    // Keep track of calendars we've processed, so we don't process them
    // when we retrieve the complete list later.
    this.ownCalendarsUrls[cal.url] = true;
  }

  debug.info(g_cache.getCalendarCount() + ' own calendars');

  dialogDiv.visible = false;
  footerDiv.visible = true;

  Utils.hideLoading();

  // The show/hide options is not available on the mac.
  if (Utils.isMac()) {
    return;
  }

  linkOptions.visible = (g_cache.getCalendarCount() > 0) &&
      !(linkOptions.x < linkAddEvent.x + linkAddEvent.width);
  linkAddEvent.visible = g_cache.getCalendarCount() > 0;
  var tooltip = strings.OPTIONS_TOOLTIP;
  tooltip = tooltip.replace('[![USERNAME]!]',
      options.getValue(OPTIONS.MAIL));
  linkOptions.tooltip = tooltip;

  // Now get the rest.
  g_events.onCalendarsReceived = Utils.bind(this.onCalendarsReceived, this);
  g_events.getUserCalendars();
};

CalendarGadget.prototype.onCalendarsReceived = function(data) {
  this.processCalendarsWithDelay(data, 0);
};

CalendarGadget.CALENDAR_PROCESS_DELAY = 200;

CalendarGadget.prototype.processCalendarsWithDelay = function(data, i) {
  var entriesLength = data.feed.entry.length;

  if (i >= entriesLength) {
    return;
  }

  var cal = new Calendar();
  cal.parse(data.feed.entry[i]);

  if (this.ownCalendarsUrls[cal.url]) {
    debug.info('Calendar already processed');
  } else {
    this.processCalendar(cal);
  }

  view.setTimeout(Utils.bind(this.processCalendarsWithDelay, this, data, ++i),
      CalendarGadget.CALENDAR_PROCESS_DELAY);
};

/**
 * Callback function when new events have been received
 */
CalendarGadget.prototype.onEventsReceived = function() {
  //debug.trace('Events received - redraw');
  if (g_events.queueLength <= 1) {
    g_events.queueLength = 0;
    Utils.hideLoading();
    clearTimeout(this.redrawTimer);
    this.drawUI();
  } else {
    clearTimeout(this.redrawTimer);
    this.redrawTimer = setTimeout(Utils.bind(this.drawUI, this), 250);
  }
};

/**
 * Callback function if the mini calendar changed the size of the
 * target div. Happens, when the month changes from needing 5-lines to
 * 6-lines.
 */
CalendarGadget.prototype.onCalendarResized = function() {
  this.resize();
};

/**
 * Called when the version checker detects a mandatory upgrade.
 */
CalendarGadget.prototype.onMandatoryUpgrade = function(upgradeInfo) {
  debug.warning('Received mandatory upgrade notice.');

  this.logout();

  options.putValue(OPTIONS.UPGRADE_REASON, upgradeInfo.reason);
  options.putValue(OPTIONS.UPGRADE_URL, upgradeInfo.downloadUrl);
  options.putValue(OPTIONS.UPGRADE_INFO, upgradeInfo.infoUrl);
  options.putValue(OPTIONS.UPGRADE, upgradeInfo.mandatoryVersion.toString());

  this.showUpgrade();
};

/**
 * Jump to today
 */
CalendarGadget.prototype.goToday = function() {
  g_uiCal.goToday();
  g_uiDayView.goToday();
  g_uiAgenda.goToday();
};

/**
 * Get the start day of the week.
 * @return {integer} Start day of the week
 */
CalendarGadget.prototype.getWeekStart = function() {
  return options.getValue(OPTIONS.WEEKSTART);
};

/**
 * Add menu items to the context menu of the gadget
 * @param {Object} menu Menu object of the context menu
 */
CalendarGadget.prototype.addMenuItems = function(menu) {
  // Return to today
  var flag = g_auth.getAuthToken() ? 0 : gddMenuItemFlagGrayed;
  menu.addItem(strings.TODAY, flag, Utils.bind(this.goToday, this));

  // Force a refresh if user wants to see his changes immediately instead
  // of waiting for the next scheduled refresh.
  menu.addItem(strings.REFRESH, 0,
      Utils.bind(g_events.updateCheck, g_events, new Date(), true));

  // Show sign out only if currently signed in
  if (g_auth.getAuthToken()) {
    menu.addItem(strings.SIGN_OUT, 0, Utils.bind(this.logout, this));
  }

  // Visit the online page for Google Calendar
  menu.addItem(strings.VISIT_CALENDAR, 0, 
      Utils.bind(this.menuOption, this));
};

/**
 * Called by custom menu items to set certain options based on itemtext
 * @param {string} itemtext Text on menu item
 */
CalendarGadget.prototype.menuOption = function(itemtext) {
  switch (itemtext) {
    case strings.MENU_24HOUR:
        options.putValue(OPTIONS.HOUR24, !options.getValue(OPTIONS.HOUR24));
        break;
    case strings.MENU_HIDE:
        options.putValue(OPTIONS.HIDE, !options.getValue(OPTIONS.HIDE));
        break;
    case strings.DAY_SAT:
        options.putValue(OPTIONS.WEEKSTART, START_SATURDAY);
        break;
    case strings.DAY_SUN:
        options.putValue(OPTIONS.WEEKSTART, START_SUNDAY);
        break;
    case strings.DAY_MON:
        options.putValue(OPTIONS.WEEKSTART, START_MONDAY);
        break;
    case strings.VISIT_CALENDAR:
        var user = options.getValue(OPTIONS.MAIL);
        if (user.indexOf("@") == -1 ||
            user.match(/(gmail.com|googlemail.com)$/i)) {
          Utils.redirectWithSuperAuth(CALENDAR_URL);
        } else {
          userDomain = user.substr(user.indexOf("@") + 1);
          var calUrl = CALENDAR_HOSTED.replace('[DOMAIN]', userDomain);
          Utils.redirectWithSuperAuth(calUrl);
        }
        break;
  }

  this.drawUI();
};

/**
 * Redraw all UI elements of gadget
 */
CalendarGadget.prototype.drawUI = function() {
  g_uiCal.draw();
  g_uiDayView.draw();
  g_uiAgenda.draw();
};

/**
 * Switch between views
 * @param {string} itemtext Text on menu item
 */
CalendarGadget.prototype.setView = function(itemtext) {
  switch (itemtext) {
    case strings.CALENDAR_VIEW:
        options.putValue(OPTIONS.VIEW, OPTIONS.CALENDARVIEW);
        break;
    case strings.DAY_VIEW:
        options.putValue(OPTIONS.VIEW, OPTIONS.DAYVIEW);
        break;
    case strings.AGENDA_VIEW:
        options.putValue(OPTIONS.VIEW, OPTIONS.AGENDAVIEW);
        break;
  }
  this.resize();
};

/**
 * Logout user from gadget
 */
CalendarGadget.prototype.logout = function() {
  g_auth.clearAuthToken();
  g_cache.clearCalendarCache();
  g_cache.clearEventCache();
  g_events.stopTimer();
  options.putValue(OPTIONS.CALENDARDATA, '');
  this.drawUI();
  this.showLogin();
};

/**
 * Show upgrade dialog to user.
 */
CalendarGadget.prototype.showUpgrade = function() {
  upgradeReason.innerText = options.getValue(OPTIONS.UPGRADE_REASON);
  upgradeDownloadUrl.href = options.getValue(OPTIONS.UPGRADE_URL);
  upgradeInfoUrl.href = options.getValue(OPTIONS.UPGRADE_INFO);

  footerDiv.visible = false;
  dialogDiv.visible = true;
  upgradeDiv.visible = true;
  loginDiv.visible = false;
};

/**
 * Show login dialog to user.
 * @param {string} opt_captcha URL for the captcha if required
 * @param {boolean} opt_focus True, if input fields should be focused.
 */
CalendarGadget.prototype.showLogin = function(opt_captcha, opt_focus) {
  if (Utils.needsUpgrade()) {
    this.showUpgrade();
    return;
  }

  footerDiv.visible = false;
  dialogDiv.visible = true;
  loginDiv.visible = true;

  if (opt_captcha) {
    userLabel.visible = false;
    userBg.visible = false;
    passLabel.visible = false;
    passBg.visible = false;
    rememberFocus.visible = false;
    remember.visible = false;

    captchaImg.visible = true;
    captchaImg.src = 'images/captcha_loading.jpg';
    Utils.getRemoteImg(opt_captcha, captchaImg);
    captchaBg.visible = true;

    captcha.value = '';
    if (opt_focus) {
      captcha.focus();
    }
  } else {
    userLabel.visible = true;
    userBg.visible = true;
    passLabel.visible = true;
    passBg.visible = true;
    rememberFocus.visible = false;
    remember.visible = true;

    captchaImg.visible = false;
    captchaBg.visible = false;

    user.value = options.getValue(OPTIONS.MAIL);
    if (opt_focus) {
      if (user.value.length === 0) {
        user.focus();
      } else {
        pass.focus();
      }
    }
    pass.value = '';
    remember.value = options.getValue(OPTIONS.REMEMBER);
  }
  login.color = '#000000';
  login.enabled = true;
};

/**
 * Handle key events for login screen.
 * @param {string} ele Name of the element
 */
CalendarGadget.prototype.loginKeyPress = function(ele) {
  // Do not handle tabs since this will be done by Google Desktop.
  if (Utils.isMac() && event.keyCode == 9) {
    switch (ele) {
      case 'user': 
          if (this.shiftDown_) {
            remember.focus();
          } else {
            pass.focus();
          }
          break;
      case 'pass': 
          if (this.shiftDown_) {
            user.focus();
          } else {
            remember.focus();
          }
          break;
      case 'remember': 
          if (this.shiftDown_) {
            pass.focus();
          } else {
            user.focus();
          }
          break;
    }
    event.returnValue = false;
  } else if (event.keyCode == 32) {
    switch (ele) {
      case 'remember':
          remember.value = !remember.value;
          break;
      case 'login':
          this.doLogin();
          break;
    }
  } else if (event.keyCode == 13) {
    this.doLogin();
  }
};

/**
 * Handle onfocus* events for remember checkbox on login screen.
 * @param {boolean} focused True, if control was focused
 */
CalendarGadget.prototype.onRememberFocus = function(focused) {
  rememberFocus.visible = focused;
};

CalendarGadget.prototype.onLoginFocus = function(focused) {
  if (focused) {
    login.image = 'images/action_hover.png';
  } else {
    login.image = 'images/action_default.png';
  }
};

/**
 * Key down event to detect shift/control/alt keys
 */
CalendarGadget.prototype.onKeyDown = function() {
  switch (event.keyCode) {
    case 16:
        this.shiftDown_ = true;
        break;
    case 17:
        this.ctrlDown_ = true;
        break;
  }
};

/**
 * Key down event to detect shift/control/alt keys
 */
CalendarGadget.prototype.onKeyUp = function() {
  switch (event.keyCode) {
    case 16:
        this.shiftDown_ = false;
        break;
    case 17:
        this.ctrlDown_ = false;
        break;
  }
};

/**
 * Perform login. Stores user credential in options and calls login
 */
CalendarGadget.prototype.doLogin = function() {
  Utils.showLoading();
  login.color = '#C0C0C0';
  login.enabled = false;
  g_errorMessage.removeMessage();
  if (captchaImg.visible) {
    g_auth.login(g_auth.authResponse.CaptchaToken, captcha.value);
  } else {
    options.putValue(OPTIONS.MAIL, user.value);
    options.putValue(OPTIONS.PASSWORD, pass.value);
    options.putValue(OPTIONS.REMEMBER, remember.value);
    g_auth.login();
  }
};

/**
 * Show error message to user.
 * @param {string} msg Error message
 */
CalendarGadget.prototype.showErrorMsg = function(msg) {
  g_errorMessage.displayMessage(msg);
};

/**
 * show quick add dialog to user
 */
CalendarGadget.prototype.quickAddEvent = function() {
  if (!Utils.isOnline()) {
    g_calendarGadget.showErrorMsg(strings.OFFLINE);
    return;
  }

  debug.trace('Show add event dialog');
  if (this.detailsView !== null) {
    plugin.closeDetailsView();
    this.detailsView = null;
    return;
  }

  options.putValue(OPTIONS.CLOSE_DETAILS, false);

  this.detailsView = new DetailsView();
  if (options.getValue(OPTIONS.USE_QUICK_ADD)) {
    this.detailsView.SetContent('', undefined, 'quickadd.xml', false, 0);
  } else {
    this.detailsView.SetContent('', undefined, 'addevent.xml', false, 0);
  }
  this.detailsView.contentIsView = true;
  var detailsData = this.detailsView.detailsViewData;
  var calendars = [];
  for (var i = 0; i < g_cache.getCalendarCount(); ++i) {
    var cal = g_cache.getCalendar(i);
    if (cal.accessLevel == 'owner' || cal.accessLevel == 'contributor' &&
        cal.isVisible()) {
      calendars.push({'title': cal.getTitle(), 'id': cal.id});
    }
  }
  detailsData.putValue(OPTIONS.CALENDAR, calendars);
  detailsData.putValue(OPTIONS.DATE, g_uiAgenda.value);

  plugin.ShowDetailsView(this.detailsView, '',
      gddDetailsViewFlagDisableAutoClose | gddDetailsViewFlagNoFrame,
      Utils.bind(this.onQuickAddClosed, this));
};

/**
 * Reset detailsview variable if quickadd dialog is closed.
 */
CalendarGadget.prototype.onQuickAddClosed = function() {
  this.detailsView = null;
};

/**
 * callback function if options are changed. Used for interaction with
 * detailsviews since they cannot access the main view of the gadget.
 */
CalendarGadget.prototype.onOptionChanged = function() {
  switch (event.propertyName) {
    case OPTIONS.CLOSE_DETAILS:
        if (options.getValue(OPTIONS.CLOSE_DETAILS)) {
          debug.trace('Close detailsview triggered');
          options.putValue(OPTIONS.CLOSE_DETAILS, false);
          plugin.closeDetailsView();
          this.detailsView = null;
        }
        break;
    case OPTIONS.QUICKADD:
        var calEvent = options.getValue(OPTIONS.QUICKADD);
        if (calEvent !== '') {
          // Store empty string since Google Desktop cannot save objects in 
          // the options.
          options.putValue(OPTIONS.QUICKADD, '');

          g_events.addQuickEvent(calEvent);
        }
        break;
    case OPTIONS.UPDATE_EVENT:
        var calEvent = options.getValue(OPTIONS.UPDATE_EVENT);
        if (typeof(calEvent) == 'object') {
          // Store empty string since Google Desktop cannot save objects in 
          // the options.
          options.putValue(OPTIONS.UPDATE_EVENT, '');

          // For some unknown reason the startTime and endTime are still Date
          // objects but do not have the Date functions.
          calEvent.startTime = new Date(calEvent.startTime);
          calEvent.endTime = new Date(calEvent.endTime);
          for (var att in calEvent.attendees) {
            var attList = calEvent.attendees[att];
            calEvent.attendees[att] = new Array(attList);
          }

          var cal = g_cache.getCalendarByID(calEvent.calendarId);
          g_cache.addUpdateToCalendar(cal, calEvent);
          this.drawUI();
        }
        break;
    case OPTIONS.NEW_EVENT:
        var calEvent = options.getValue(OPTIONS.NEW_EVENT);
        if (typeof(calEvent) == 'object') {
          // Store empty string since Google Desktop cannot save objects in 
          // the options.
          options.putValue(OPTIONS.NEW_EVENT, '');

          // For some unknown reason the startTime and endTime are still Date
          // objects but do not have the Date functions.
          calEvent.startTime = new Date(calEvent.startTime);
          calEvent.endTime = new Date(calEvent.endTime);

          g_events.addNewEvent(calEvent);
        }
        break;
    case OPTIONS.UPDATE_RSVP_TRIGGER:
        g_events.updateCheck(new Date(), true);
        break;
    case OPTIONS.UPDATE_VIEW:
        this.resize();
        break;
  }
};

CalendarGadget.prototype.showOptions = function() {
  plugin.showOptionsDialog();
};
