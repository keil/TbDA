function MailView(gadget) {
  this.threads = [];
  this.labels = [];

  this.contacts = [];
  this.lastContactsUpdate = null;

  this.viewOffset = 0;
  this.folder = null;
  this.labelIndex = null;

  this.lastTimestamp = null;
  this.totalResponseThreads = null;
  this.fetchedResponseThreads = null;
  this.unreadResponseThreads = null;

  this.actionToken = null;
  this.mailsInView = 0;

  // Refresh failure count.
  this.failedCount = 0;
  this.uiMailView = new UiMailView(this);
  this.gadget = gadget;

  lblAccount.innerText = user.value.indexOf('@') > -1 ?
      user.value.substr(0, user.value.indexOf('@')) : user.value;
  lblAccount.tooltip = lblAccount.innerText;
  user.color = UIDATA.DISABLED_TEXTBOX_COLOR;
  user.enabled = false;
  pass.color = UIDATA.DISABLED_TEXTBOX_COLOR;
  pass.enabled = false;

  this.user = user.value;
  user.value = '';
  this.pass = pass.value;
  pass.value = '';

  if (Utils.isGmailAccount(this.user)) {
    g_httpRequest.host = CONNECTION_DATA.GMAIL_HOST;
    g_httpRequest.url = CONNECTION_DATA.HTTPS_PREFIX +
        CONNECTION_DATA.GMAIL_HOST + CONNECTION_DATA.GMAIL_URL;
  } else {
    var host = CONNECTION_DATA.APPS_DOMAIN_PREFIX + Utils.getDomain(this.user);
    g_httpRequest.host = host;
    g_httpRequest.url = CONNECTION_DATA.HTTPS_PREFIX + host +
        CONNECTION_DATA.APPS_URL;
  }

  this.gadget.onResize(true);
  this.uiMailView.updateNumberOfMailInView();
  this.uiMailView.oldMailsInView = this.mailsInView;
  this.login();
}

MailView.prototype.isGmailHost = function() {
  return g_httpRequest.host == CONNECTION_DATA.GMAIL_HOST;
};

// Refresh interval.
MailView.REFRESH_INTERVAL_MS = 5 * 60 * 1000;

// Upper limit to refresh interval when in backoff mode.
MailView.MAX_REFRESH_INTERVAL_MS = 60 * 60 * 1000;

// Interval for checking if machine is back online.
MailView.NETWORK_CHECK_INTERVAL_MS = 60 * 1000;

MailView.prototype.setupMailView = function() {
  this.gadget.keyboardShortcutsEnabled = true;
  loginDiv.visible = false;
  mainDiv.visible = true;
  this.gadget.onResize(false);
};

MailView.prototype.destroyMailView = function() {
  view.clearInterval(this.refreshToken);
  this.restoreTitleBar();
  this.gadget.keyboardShortcutsEnabled = false;
  options.putValue(OPTION_NAMES.USER, '');
  options.putValue(OPTION_NAMES.PASS, '');
  remember.value = false;
  user.value = pass.value = '';
  user.enabled = pass.enabled = true;
  user.color = pass.color = UIDATA.ENABLED_TEXTBOX_COLOR;
  rememberFocus.visible = false;
  loginDiv.visible = true;
  mainDiv.visible = false;
  mailList.removeAllElements();
  HTTPRequest.queue = [];
  g_httpRequest.handler = g_httpRequest.failedHandler = null;
  actionsMenuBorder.visible = switchComboBorder.visible = false;
  user.focus();
  this.gadget.loginView.onPasswordFocus(false);
  lblAccount.innerText = '';
  lblAccount.tooltip = '';
  this.gadget.onResize(false);
  this.gadget.mailView = null;
};

/**
 * Schedules next refresh and implements backoff.
 */
MailView.prototype.scheduleRefresh = function() {
  var interval = MailView.REFRESH_INTERVAL_MS * (this.failedCount + 1);

  // A dash of randomness.
  var fuzz = interval / 5;
  fuzz *= Math.random();
  fuzz = Math.floor(fuzz);

  interval += fuzz;

  if (interval > MailView.MAX_REFRESH_INTERVAL_MS) {
    interval = MailView.MAX_REFRESH_INTERVAL_MS;
  }

  if (this.refreshToken) {
    view.clearTimeout(this.refreshToken);
  }

  this.refreshToken = view.setTimeout(bind(this, this.refresh), interval);
};

/**
 * This function is invoked onchange of the action combobox
 */
MailView.prototype.onSwitchComboChange = function() {
  if (switchCombo.selectedIndex == COMBO_POSITION.LABELS_SEPARATOR) {
    event.returnValue = false;
    return;
  }
  switchComboBorder.visible = false;
  if (switchCombo.selectedIndex == COMBO_POSITION.INBOX) {
    this.requestInbox(0);
  } else if (switchCombo.selectedIndex > COMBO_POSITION.LABELS_SEPARATOR) {
    this.requestLabel(switchCombo.selectedIndex -
        COMBO_POSITION.LABELS_SEPARATOR - 1, 0);
  } else {
    this.displayMails(0, COMBO_POSITION[switchCombo.selectedIndex]);
  }
};

/**
 * Invoked when the user browses into the older sections of the view
 * Changes this.viewOffset as necessary and
 * invokes the appropriate handler
 */
MailView.prototype.browseOlderMails = function() {
  var newOffset = this.viewOffset + this.mailsInView;
  if (newOffset < this.totalResponseThreads &&
      newOffset != this.viewOffset) {
    this.viewOffset = newOffset;
    this.refreshView();
  }
};

/**
 * Invoked when the user browses into the newer sections of the view
 * Changes this.viewOffset as necessary and
 * invokes the appropriate handler
 */
MailView.prototype.browseNewerMails = function() {
  var newOffset = this.viewOffset - this.mailsInView;
  if (newOffset < 0) {
    newOffset = 0;
  }
  if (this.viewOffset == newOffset) {
    this.uiMailView.selectLast = false;
    return;
  }
  this.viewOffset = newOffset;
  this.refreshView();
};

/**
 * This function is invoked when the user choses an option from the
 * actionsMenu
 */
MailView.prototype.actionsMenuOptionChosen = function() {
  this.gadget.removeActionsMenu();
  if (actionsMenu.selectedIndex === 0) {
    this.threads[mailList.selectedIndex].open();
  } else {
    var action = actionsMenu.selectedIndex;
    if (action == ACTION.STAR && mnuStar.innerText == MENU_REMOVE_STAR) {
      action = ACTION.UNSTAR;
    } else if (action > ACTION.STAR) {
      action = ACTION.MARK_AS_READ;
      if (mnuMarkRead.innerText == MENU_MARK_UNREAD) {
        action = ACTION.MARK_AS_UNREAD;
      }
    }
    this.threads[mailList.selectedIndex].actionExecute(action);
  }
};

/**
 * This function if invoked whenever the user requests a search operation
 * Search operations can be preset filters such as spam or trash.
 * @param {Number} offset The offset of the new view
 */
MailView.prototype.search = function(offset, searchString) {
  if (searchString === null) {
    // Is a search box query.
    searchString = searchText.value;
  }
  if (this.folder != FILTERS.SEARCH) {
    searchText.value = '';
  }
  this.viewOffset = offset;
  var data = UrlBuilder.buildSearchUrl(searchString, offset,
      this.mailsInView);
  g_httpRequest.connectToGmailServer(data,
      bind(this, this.parseThreadlist),
      bind(this, this.failedParseThreadlist),
      false, this);
};

/**
 * Connects to the Gmail server to download the required threadlist.
 * Number of messages in the threadlist is
 * this.mailsInView
 * @param {Number} offset The inbox offset of the new view
 */
MailView.prototype.requestInbox = function(offset) {
  this.folder = FILTERS.INBOX;
  searchText.value = '';
  var data = UrlBuilder.buildInboxUrl(offset, this.mailsInView);
  g_httpRequest.connectToGmailServer(data,
      bind(this, this.parseThreadlist),
      bind(this, this.failedParseThreadlist),
      false);
};

/**
 * Logs into to the Gmail server and receives a threadlist.
 * Number of messages in the threadlist is
 * this.mailsInView
 */
MailView.prototype.login = function() {
  this.folder = FILTERS.INBOX;
  var data = UrlBuilder.buildLoginUrl(this.mailsInView,
      this.user, this.pass);

  // Necessary auth headers.
  var headers = {};
  headers[CONNECTION_DATA.MOBILE_AUTH_HEADER] =
      CONNECTION_DATA.MOBILE_AUTH_HEADER_VALUE;

  g_httpRequest.connectToGmailServer(data,
      bind(this, this.handleLogin),
      bind(this, this.failedParseThreadlist),
      false, null, false, headers);
};

/**
 * This function opens up the details view for the labels
 * @param {Number} labelIndex is the index of the label we wish to get the
 * threadlist for.
 * @param {Number} offset this is the current view offset
 */
MailView.prototype.requestLabel = function(labelIndex, offset) {
   this.folder = FILTERS.LABEL;
   this.labelIndex = labelIndex;
   this.viewOffset = offset;
   var data = UrlBuilder.buildLabelUrl(this.labels[labelIndex].name,
       offset, this.mailsInView);
   g_httpRequest.connectToGmailServer(data,
       bind(this, this.parseThreadlist),
       bind(this, this.failedParseThreadlist),
       false);
};

MailView.prototype.displayMails = function(offset, filter) {
  this.search(offset, filter);
  this.folder = filter;
};

/**
 * Invoked periodically to refresh the view or other data.
 */
MailView.prototype.refresh = function() {
  this.refreshView();

  // Keep contacts fresh.
  var now = new Date();

  if (!this.lastContactsUpdate ||
          now.getTime() - this.lastContactsUpdate.getTime() >
          MailView.CONTACTS_REFRESH_INTERVAL_MS) {
    this.retrieveContacts();
  }
};

/**
 * Invoked whenever the View is to be refreshed. Can be user/script invoked.
 */
MailView.prototype.refreshView = function() {
  // Though this check is present in HTTPRequest, handling it here to ensure
  // timely refresh when network is back online.
  //
  // Alternative would be to report offline in HTTPRequest's error reporting.
  // We could then determine machine is offline in this class' error handler.
  if (!Utils.isOnline()) {
    view.setTimeout(bind(this, this.refreshView),
        MailView.NETWORK_CHECK_INTERVAL_MS);
    return;
  }

  // Send a request containing previous timestamp to the server
  if (this.folder == FILTERS.INBOX) {
    this.requestInbox(this.viewOffset);
  } else if (this.folder == FILTERS.SEARCH) {
    this.search(this.viewOffset, searchText.value);
  } else if (this.folder == FILTERS.LABEL) {
    this.requestLabel(this.labelIndex, this.viewOffset);
  } else {
    this.displayMails(this.viewOffset, this.folder);
  }
};

/**
 * This function is called when a request has generated an error
 */
MailView.prototype.failedParseThreadlist = function(status){
  ++this.failedCount;
  this.scheduleRefresh();

  if (this.totalResponseThreads === null) {
    this.destroyMailView();
    g_errorMessage.displayMessage(THREAD_LIST_DATA_INFO.LOGIN_ERRORS[status] ?
        THREAD_LIST_DATA_INFO.LOGIN_ERRORS[status] : SERVER_OR_NETWORK_ERROR);
  }
};

/**
 * Returns contacts list.
 */
MailView.prototype.getContacts = function() {
  return this.contacts;
};

// Refresh contacts if they are older than 15 minutes.
MailView.CONTACTS_REFRESH_INTERVAL_MS = 15 * 60 * 1000;

/**
 * Retrieves contacts.
 */
MailView.prototype.retrieveContacts = function() {
  var data = UrlBuilder.buildContactsUrl();
  g_httpRequest.connectToGmailServer(data,
      bind(this, this.updateContacts),
      null,
      false, this,
      true);  // Don't display loading.
};

/**
 * This function handles the response recieved from the server for a request to
 * enumerate the contacts.
 */
MailView.prototype.updateContacts = function(responseData) {
  try {
    var currentLine = responseData.getNextLine();
    var totalContacts = parseInt(currentLine, 10);
    var contacts = [];

    for (var i = 0; i < totalContacts; ++i) {
      currentLine = responseData.getNextLine();
      contacts.push(
          {displayName: currentLine, email: responseData.getNextLine()});
    }
    this.contacts = contacts;
    this.lastContactsUpdate = new Date();
  } catch(e) {
    debug.error('MailView.updateContacts failed: ' + e.message);
  }
};

MailView.prototype.notifyMandatoryUpgrade = function(reason, upgradeUrl) {
  loginDiv.visible = false;
  upgradeDiv.visible = true;

  // Not displaying reason. General messaging should be enough.
  upgradeDiv.children.item('upgradeUrl').href = upgradeUrl;
};

// Wait duration for token auth.
MailView.TOKEN_AUTH_TIMEOUT = 5000;

/**
 * Called when login succeeds.
 */
MailView.prototype.handleLogin = function(responseData) {
  this.parseThreadlist(responseData);
  this.retrieveContacts();
};

/**
 * Called when request succeeds. Parses the threadlist response.
 */
MailView.prototype.parseThreadlist = function(responseData) {
  this.failedCount = 0;
  this.scheduleRefresh();

  this.timestamp = null;
  this.totalResponseThreads = 0;
  this.numUnreadResponse = null;

  var currentLine = responseData.getNextLine();
  // Check for a valid response start:
  if (currentLine != THREAD_LIST_DATA_INFO.TRUE) {
    if (currentLine == THREAD_LIST_DATA_INFO.NEED_UPGRADE) {
      this.destroyMailView();
      var reason = responseData.getNextLine();
      var upgradeUrl = responseData.getNextLine();
      this.notifyMandatoryUpgrade(reason, upgradeUrl);
    } else if (this.fetchedResponseThreads === null) {
      // Update display on unsuccessfull login attempt.
      this.destroyMailView();
      // Check if an error was returned:
      if (currentLine == THREAD_LIST_DATA_INFO.LOGIN_ERROR) {
        currentLine = responseData.getNextLine();
        g_errorMessage.displayMessage(
            THREAD_LIST_DATA_INFO.LOGIN_ERRORS[currentLine] ?
            THREAD_LIST_DATA_INFO.LOGIN_ERRORS[currentLine] :
            SERVER_OR_NETWORK_ERROR);
      }
    }
    return;
  }

  if (!mainDiv.visible) {
    this.setupMailView();
  }
  mailList.removeAllElements();

  currentLine = responseData.getNextLine();
  // If currentLine is T, we require an HTTPS header, otherwise
  // it's the timestamp.
  if (currentLine == THREAD_LIST_DATA_INFO.REQUIRE_HTTPS) {
    g_httpRequest.url = CONNECTION_DATA.HTTPS_PREFIX +
        g_httpRequest.url.substr(g_httpRequest.url.indexOf('://') + 3);
    currentLine = responseData.getNextLine();
  } else if (currentLine == THREAD_LIST_DATA_INFO.OPTIONAL_UPGRADE) {
    // Skip all optional upgrade lines.
    responseData.skipLines(3);
    // And get the timestamp.
    currentLine = responseData.getNextLine();
  } else if (currentLine == THREAD_LIST_DATA_INFO.PAGE_BROADCAST) {
    // Skip all broadcast lines.
    responseData.skipLines(1);
    // And get the timestamp.
    currentLine = responseData.getNextLine();
  }
  this.lastTimestamp = currentLine;
  // Read the viewOffset.
  this.viewOffset = parseInt(responseData.getNextLine(), 10);
  // Read the current number of threads fetched.
  this.fetchedResponseThreads = parseInt(responseData.getNextLine(), 10);
  // Read the total number of threads.
  this.totalResponseThreads = parseInt(responseData.getNextLine(), 10);
  // Read the total number of unread threads.
  this.unreadResponseThreads = parseInt(responseData.getNextLine(), 10);
  // Read the action token.
  this.actionToken = responseData.getNextLine();

  this.threads = [];
  for (var i = 0; i <= this.fetchedResponseThreads; ++i) {
    // See if the thread is read. If a number is encountered, it means we
    // reached label enumeration part of the response
    currentLine = responseData.getNextLine();
    if (currentLine != THREAD_LIST_DATA_INFO.TRUE &&
        currentLine != THREAD_LIST_DATA_INFO.FALSE) {
      this.labels = [];
      currentLine = parseInt(currentLine, 10);
      if (!isNaN(currentLine)) {
        for (var j = 0; j < currentLine; ++j) {
          this.labels.push({name: responseData.getNextLine(),
              numUnread: parseInt(responseData.getNextLine(), 10)});
        }
      }
    } else {
      this.threads.push(new Thread(this));
      this.threads[i].isSpam = this.folder == FILTERS.SPAM;
      this.threads[i].isTrash = this.folder == FILTERS.TRASH;
      this.threads[i].read = currentLine == THREAD_LIST_DATA_INFO.TRUE;
      // See if this message is starred:
      this.threads[i].starred =
          responseData.getNextLine() == THREAD_LIST_DATA_INFO.TRUE;
      // See if this thread has attachments:
      this.threads[i].hasAttachment =
          responseData.getNextLine() == THREAD_LIST_DATA_INFO.TRUE;
      // Read the thread sender:
      this.threads[i].sender = responseData.getNextLine();
      // Read the thread subject:
      this.threads[i].subject = responseData.getNextLine();
      if (this.threads[i].subject == BLANK_LINE) {
        this.threads[i].subject = NO_SUBJECT;
      }
      // Read the thread's date:
      currentLine = responseData.getNextLine();
      if (currentLine.charAt(currentLine.length - 1) ==
          THREAD_LIST_DATA_INFO.PM) {
        currentLine = currentLine.substr(0, currentLine.length - 1) + PM;
      } else if (currentLine.charAt(currentLine.length - 1) ==
          THREAD_LIST_DATA_INFO.AM) {
        currentLine = currentLine.substr(0, currentLine.length - 1) + AM;
      }
      this.threads[i].date = currentLine;
      // Read the thread id:
      this.threads[i].id = responseData.getNextLine().substr(4);
    }
  }
  this.uiMailView.populateSwitchCombo();
  g_errorMessage.messageAndErrorDiv.focus();
  this.uiMailView.showResponse();
  mailList.selectedIndex = this.uiMailView.selectLast ?
                           mailList.selectedIndex =
                           mailList.children.count - 1 : 0;
  this.uiMailView.selectLast = false;
  this.updateTitleBar();
};

MailView.prototype.updateTitleBar = function() {
  var caption = strings.GADGET_NAME;
  if (this.unreadResponseThreads !== null) {
    caption += ' (' + this.unreadResponseThreads + ')';
  }

  view.caption = caption;
};

MailView.prototype.restoreTitleBar = function() {
  view.caption = strings.GADGET_NAME;
};

MailView.prototype.getContacts = function(id, max) {
  var result = [];
  for (var i = 0; i < this.contacts.length && result.length < max; ++i) {
    var email = this.contacts[i].email;
    var displayName = this.contacts[i].displayName;

    // Input value matches email or is contained in display name.
    if (email.substr(0, id.length).toUpperCase() == id ||
        displayName.toUpperCase().indexOf(id) != -1) {
      result.push(this.contacts[i]);
    }
  }
  return result;
};

