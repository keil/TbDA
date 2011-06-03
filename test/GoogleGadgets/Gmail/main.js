var g_gmailGadget = new GmailGadget();
var g_resources = new Resources();
var g_detailsView = new DetailsViewController();

function GmailGadget() {
  switchComboUpLbl.innerText = '\u25B2';
  switchComboDownLbl.innerText = '\u25BC';
  this.loginView = new LoginView(this);
  this.mailView = null;

  this.lastKeyPressed = null;
  this.keyboardShortcutsEnabled = true;

  this.resizeToken = null;  // Timeout token used in onResize

  debug.trace('Curent gadget version is: ' + VERSION_STRING);
  options.putValue(OPTION_NAMES.VERSION_NUM, VERSION_STRING);
}

/**
 * Sizes details view at startup.
 * Dimensions are set in size_details_view.xml.
 */
GmailGadget.prototype.sizeDetailsView = function() {
  var details = new DetailsView();
  details.SetContent('', undefined, 'size_details_view.xml', false, 0);
  try {
    plugin.showDetailsView(details, '', 0, nullFunction);
    plugin.closeDetailsView();
  } catch (ex) {
    debug.warning('Could not size details view');
    debug.warning(ex.message);
  }
};

/**
 * Called when the gadget loads, handles resizing and autologin if it has to
 */
GmailGadget.prototype.onOpen = function() {
/*
  Use the manifest to restrict platforms instead.
  var isWindows = false;

  try {
    isWindows = Utils.isWindows();
  } catch (ex) {
    debug.warning(ex.message);
  }

  if (!isWindows) {
    loginDiv.visible = false;
    notCompatibleDiv.visible = true;
    g_errorMessage.removeMessage();
    this.onResize(false);
    return;
  }
*/
  this.sizeDetailsView();
  plugin.OnAddCustomMenuItems = bind(this, this.onAddCustomMenuItems);
  this.onResize(false);
  user.value = options.getValue(OPTION_NAMES.USER);
  pass.value = options.getValue(OPTION_NAMES.PASS);
  if (user.value !== '' && pass.value !== ''){
    remember.value = true;
    this.login();
  }
};

GmailGadget.TERMS_URL = 'http://mail.google.com/mail/help/terms.html';

GmailGadget.prototype.openTermsPage = function() {
  framework.openUrl(GmailGadget.TERMS_URL);
};

GmailGadget.prototype.isMailView = function() {
  return mainDiv.visible;
};

GmailGadget.prototype.isUpgradeView = function() {
  return upgradeDiv.visible;
};

GmailGadget.prototype.isLoginView = function() {
  return loginDiv.visible;
};

GmailGadget.prototype.isNotCompatibleView = function() {
  return notCompatibleDiv.visible;
};

GmailGadget.prototype.onAddCustomMenuItems = function(menu) {
  if (this.isMailView()) {
    menu.AddItem(COMPOSE,
        this.isComposeOpen() ?
        gddMenuItemFlagGrayed : 0,
        bind(this, this.compose));
    menu.AddItem(searchTextBorder.visible ? HIDE_SEARCH : SHOW_SEARCH, 0,
        bind(this.mailView.uiMailView,
             this.mailView.uiMailView.toggleSearchVisibility));
    menu.AddItem(strings.REFRESH, 0,
        bind(this.mailView, this.mailView.refreshView));
    menu.AddItem(LOGOUT, 0,
        bind(this, this.logout));
  }

  menu.AddItem(strings.TERMS, 0, bind(this, this.openTermsPage));
};

/**
 * Stores username and password if remember is checked and calls requestInbox
 */
GmailGadget.prototype.login = function() {
  if (remember.value) {
    options.putValue(OPTION_NAMES.USER, user.value);
    options.encryptValue(OPTION_NAMES.USER);
    options.putValue(OPTION_NAMES.PASS, pass.value);
    options.encryptValue(OPTION_NAMES.PASS);
  } else {
    options.putValue(OPTION_NAMES.USER, '');
    options.putValue(OPTION_NAMES.PASS, '');
  }
  this.mailView = new MailView(this);
};

/**
 * This function is invoked when the user clicks on logout
 */
GmailGadget.prototype.logout = function() {
  var data = UrlBuilder.buildLogoutUrl();

  // Necessary auth headers.
  var headers = {};
  headers[CONNECTION_DATA.MOBILE_AUTH_HEADER] =
      CONNECTION_DATA.MOBILE_AUTH_HEADER_VALUE;

  g_httpRequest.connectToGmailServer(data, null, null, false, null, false,
      headers);

  if (!this.isMailView()) {
    return;
  }
  if (searchTextBorder.visible) {
    this.mailView.uiMailView.toggleSearchVisibility();
  }
  g_detailsView.close();
  this.mailView.destroyMailView();
};

GmailGadget.prototype.onOptionChanged = function() {
};

// ID used to distinguise the compose details view.
GmailGadget.COMPOSE_DETAILS_ID = 'COMPOSE';

GmailGadget.prototype.isComposeOpen = function() {
  return g_detailsView.isOpen(GmailGadget.COMPOSE_DETAILS_ID);
};

/**
 * This function is invoked when the user clicks on compose
 * It opens up the corresponding details menu
 */
GmailGadget.prototype.compose = function() {
  if (this.isComposeOpen()) {
    g_detailsView.close();
    return;
  }

  var detailsView = new DetailsView();
  detailsView.html_content = true;
  detailsView.setContent('', undefined, g_resources.getComposeHtml(),
      false, 0);
  detailsView.external = {};
  detailsView.external.gmailGadget = this;
  detailsView.external.getMessage = Utils.getMessage;
  detailsView.external.getContacts =
      bind(this.mailView, this.mailView.getContacts);

  g_detailsView.open(GmailGadget.COMPOSE_DETAILS_ID,
      detailsView,
      strings.COMPOSE,
      gddDetailsViewFlagDisableAutoClose);
};

GmailGadget.prototype.sendMessage = function(callback, onFail,
    isForward, to, subject, body, cc, bcc) {
  var data = UrlBuilder.buildComposeUrl(
      this.mailView.actionToken,
      this.mailView.viewOffset,
      this.mailView.mailsInView,
      to, cc, bcc, subject, body);

  g_httpRequest.connectToGmailServer(data,
      bind(this, this.sentSuccessfully, callback, onFail),
      onFail, false, this);
};

GmailGadget.prototype.discardMessage = function() {
  g_detailsView.close();
  g_errorMessage.displayMessage(MESSAGE_DISCARDED);
};

GmailGadget.prototype.sentSuccessfully = function(callback, onFail,
    responseData) {
  // Check for a valid response start.
  var firstLine = responseData.getNextLine();
  if (firstLine == THREAD_LIST_DATA_INFO.LEGAL_CONVERSATION ||
      firstLine == THREAD_LIST_DATA_INFO.TRUE) {
    if (this.mailView.folder == FILTERS.INBOX) {
      this.mailView.requestInbox(0);
    }
    g_detailsView.close();
    g_errorMessage.displayMessage(MESSAGE_SENT);
  } else {
    onFail();
  }
};

/**
 * Invoked when the user resizes the gadget
 * checkes if user is done resizing by checking at every 500 msec interval
 */
GmailGadget.prototype.onResize = function(forceAll) {
  topRightMainBg.x = middleRightMainBg.x = bottomRightMainBg.x =
      view.width - topRightMainBg.width;
  topCenterMainBg.width = middleCenterMainBg.width = bottomCenterMainBg.width =
      topRightMainBg.x - topCenterMainBg.x;
  bottomRightMainBg.y = bottomCenterMainBg.y = bottomLeftMainBg.y =
      view.height - bottomLeftMainBg.height;
  middleLeftMainBg.height = middleCenterMainBg.height =
      middleRightMainBg.height = bottomRightMainBg.y - middleLeftMainBg.y;
  // Adjust the positions of a images to move to the top right corner
  imgLoading.x = view.width - UIDATA.LOADING_LABEL_X_OFFSET;

  if (this.isLoginView() || forceAll) {
    loginDiv.width = view.width - 24;
    loginDiv.height = view.height - 50;
    user.width = pass.width = loginDiv.width - user.x - user.x;
    login.x = loginDiv.width - login.width;
  }
  if (this.isUpgradeView() || forceAll) {
    upgradeDiv.width = view.width - 24;
    upgradeDiv.height = view.height - 50;
  }
  if (this.isNotCompatibleView() || forceAll) {
    notCompatibleDiv.width = view.width - 24;
    notCompatibleDiv.height = view.height - 50;
  }
  if (this.isMailView() || forceAll) {
    mainDiv.width = view.width - 16;
    mainDiv.height = view.height - mainDiv.y;
    footerDiv.y = mainDiv.height - footerDiv.height;
    searchTextBorder.y = footerDiv.y + (searchTextBorder.visible ? -20 : 5);
    mailList.width = mainDiv.width - 4;
    searchTextBorder.width = mainDiv.width - 2;
    searchText.width = searchTextBorder.width - 2;
    searchTextClose.x = searchText.width - searchTextClose.width + 1;

    topRightContentBg.x = middleRightContentBg.x = bottomRightContentBg.x =
        mainDiv.width - topRightContentBg.width;
    topCenterContentBg.width = middleCenterContentBg.width =
        bottomCenterContentBg.width =
        topRightContentBg.x - topCenterContentBg.x;
    bottomRightContentBg.y = bottomCenterContentBg.y = bottomLeftContentBg.y =
        searchTextBorder.y - bottomRightContentBg.height - 3;
    middleLeftContentBg.height = middleCenterContentBg.height =
        middleRightContentBg.height =
        bottomRightContentBg.y - middleLeftContentBg.y;

    mailList.height = searchTextBorder.y - mailList.y - 5;
    browseLabel.width = mainDiv.width - 3 * newer.width;
    older.x = mainDiv.width - 20;
    newer.x = older.x - older.width;

    this.removeActionsMenu();

    logoutLabel.visible = view.width >= 153;
    searchToggleLabel.visible = view.width >= 107;
    composeLabel.visible = view.width >= 68;
  }
  if (this.resizeToken) {
    view.clearTimeout(this.resizeToken);
  }
  if (this.isMailView()) {
    this.resizeToken = setTimeout((function(refThis) {
      return function() {
        if (refThis.mailView !== null) {
          refThis.mailView.uiMailView.resizeView();
        }
      };
    })(this), UIDATA.RESIZE_TIMEOUT);
  }
};

/**
 * This function is called onfocusout of actionsMenu
 */
GmailGadget.prototype.removeActionsMenu = function() {
  actionsMenuBorder.visible = switchComboBorder.visible =
      actionMenuCloser.visible = false;
};

/**
 * This function gets called when a key is pressed and the main view is focused
 */
GmailGadget.prototype.keyPressed = function() {
  if (!this.keyboardShortcutsEnabled || !this.isMailView() ||
      switchComboBorder.visible) {
    return;
  }

  // Report spam
  if (event.keycode == KEYS.EXCLAMATION_MARK) {
    if (mailList.selectedIndex > -1 &&
        this.mailView.folder != FILTERS.SPAM) {
      this.mailView.threads[mailList.selectedIndex].actionExecute(
          ACTION.REPORT_SPAM);
    }
  // Delete
  } else if (event.keycode == KEYS.POUND_SIGN) {
    if (mailList.selectedIndex > -1 &&
        this.mailView.folder != FILTERS.TRASH) {
      this.mailView.threads[mailList.selectedIndex].actionExecute(ACTION.TRASH);
    }
  }
  event.returnValue = false;
};

/**
 * This function gets called when a key is released and the main view is focused
 * we handle most keys here since the arrow keys don't trigger keypressed
 * also, keyUp is case insensitive (actually it's not - but acts like it since
 * a unlike keypressed it gives the code of a pressed key not character)
 */
GmailGadget.prototype.keyUp = function() {
  if (event.keycode != KEYS.UP && event.keycode != KEYS.DOWN &&
      event.keycode != KEYS.SPACE && event.keycode != KEYS.ENTER) {
    this.removeActionsMenu();
  }
  if (event.keycode == KEYS.ESCAPE) {
    switchComboBorder.visible = false;
    mailList.focus();
  }
  if (!this.keyboardShortcutsEnabled || !this.isMailView() ||
      switchComboBorder.visible) {
    return;
  }
  g_errorMessage.removeMessage();
  // Search
  if (event.keycode == KEYS.SLASH) {
    if (!searchTextBorder.visible) {
      this.mailView.uiMailView.toggleSearchVisibility();
    }
    searchText.focus();
  // Open menu
  } else if (event.keycode == KEYS.MENU) {
    if (mailList.selectedIndex > -1) {
      this.mailView.uiMailView.popupActionsMenu(
          this.mailView.threads[mailList.selectedIndex], 0.2 * mainDiv.width,
          0.3 * mailList.itemHeight);
    }
  // Move to newer conversation
  } else if (event.keycode == KEYS.DOWN || event.keycode == KEYS.J) {
    if (mailList.enabled) {
      if (actionsMenuBorder.visible) {
        do {
          actionsMenu.selectedIndex = actionsMenu.selectedIndex ==
                                      actionsMenu.children.count - 1 ?
                                      0 : actionsMenu.selectedIndex + 1;
        } while (!actionsMenu.selectedItem.enabled);
      } else if (mailList.selectedIndex == mailList.children.count - 1) {
        if (HTTPRequest.available) {
          this.mailView.browseOlderMails();
        }
      } else {
        mailList.selectedIndex += 1;
      }
    }
  // Move to older conversation
  } else if (event.keycode == KEYS.UP || event.keycode == KEYS.K) {
    if (mailList.enabled) {
      if (actionsMenuBorder.visible) {
        do {
          actionsMenu.selectedIndex = actionsMenu.selectedIndex === 0 ?
                                      actionsMenu.children.count - 1 :
                                      actionsMenu.selectedIndex - 1;
        } while (!actionsMenu.selectedItem.enabled);
      } else if (mailList.selectedIndex < 1) {
        if (HTTPRequest.available) {
          this.mailView.uiMailView.selectLast = true;
          this.mailView.browseNewerMails();
        }
      } else {
        mailList.selectedIndex -= 1;
      }
    }
  // Open
  } else if (event.keycode == KEYS.ENTER || event.keycode == KEYS.SPACE ||
             event.keycode == KEYS.O) {
    if (actionsMenuBorder.visible) {
      this.mailView.actionsMenuOptionChosen();
    } else {
      this.mailView.threads[mailList.selectedIndex].open();
    }
  // Compose
  } else if (event.keycode == KEYS.C) {
    this.compose();
  // G pressed - first part of key combo
  } else if (event.keycode == KEYS.G) {
    setTimeout((function(refThis) {
      return function() {
        refThis.lastKeyPressed = null;
      };
    })(this), UIDATA.DOUBLE_KEY_SHORTCUTS_MAX_DELAY);
  // Star/Unstar
  } else if (event.keycode == KEYS.S && this.lastKeyPressed != KEYS.G) {
    if (mailList.selectedIndex > -1) {
      this.mailView.threads[mailList.selectedIndex].toggleStar();
    }
  // Refresh
  } else if (event.keycode == KEYS.U || event.keycode == KEYS.F5) {
    this.mailView.refreshView();
  // Archive/Unstar
  } else if (event.keycode == KEYS.Y) {
    // Arhive if current folder is Inbox
    if (this.mailView.folder == FILTERS.INBOX) {
      if (mailList.selectedIndex > -1) {
        this.mailView.threads[mailList.selectedIndex].actionExecute(
            ACTION.ARCHIVE);
      }
    // Unstar if current folder is Starred
    } else if (this.mailView.folder == FILTERS.STARRED) {
      if (mailList.selectedIndex > -1) {
        this.mailView.threads[mailList.selectedIndex].toggleStar();
      }
    }
  // Go to
  } else if (this.lastKeyPressed == KEYS.G) {
    // Go to All mail
    if (event.keycode == KEYS.A) {
      if (COMBO_POSITION[switchCombo.selectedIndex] != FILTERS.ALL_MAIL) {
        switchCombo.selectedIndex = COMBO_POSITION[FILTERS.ALL_MAIL];
        this.mailView.onSwitchComboChange();
      }
    // Go to Inbox
    } else if (event.keycode == KEYS.I) {
      if (switchCombo.selectedIndex != COMBO_POSITION['INBOX']) {
        switchCombo.selectedIndex = COMBO_POSITION['INBOX'];
        this.mailView.onSwitchComboChange();
      }
    // Go to Starred
    } else if (event.keycode == KEYS.S) {
      if (COMBO_POSITION[switchCombo.selectedIndex] != FILTERS.STARRED) {
        switchCombo.selectedIndex = COMBO_POSITION[FILTERS.STARRED];
        this.mailView.onSwitchComboChange();
      }
    }
  }
  this.lastKeyPressed = event.keycode;
  event.returnValue = false;
};

//
// Event handlers for view elements.
//

function _onOpen() {
  g_gmailGadget.onOpen();
}

function _onOptionChanged() {
  g_gmailGadget.onOptionChanged();
}

function _onResize(bool) {
  g_gmailGadget.onResize(bool);
}

function _keyPressed() {
  g_gmailGadget.keyPressed();
}

function _keyUp() {
  g_gmailGadget.keyUp();
}

function _loginView_onUsernameKeyPress() {
  g_gmailGadget.loginView.onUsernameKeyPress();
}

function _loginView_onUsernameFocus(bool) {
  g_gmailGadget.loginView.onUsernameFocus(bool);
}

function _loginView_onPasswordKeyPress() {
  g_gmailGadget.loginView.onPasswordKeyPress();
}

function _loginView_onPasswordFocus(bool) {
  g_gmailGadget.loginView.onPasswordFocus(bool);
}

function _loginView_onRememberKeyPress() {
  g_gmailGadget.loginView.onRememberKeyPress();
}

function _loginView_onRememberFocus(bool) {
  g_gmailGadget.loginView.onRememberFocus(bool);
}

function _loginView_onLoginKeyPress() {
  g_gmailGadget.loginView.onLoginKeyPress();
}

function _loginView_onLoginFocus(bool) {
  g_gmailGadget.loginView.onLoginFocus(bool);
}

function _login() {
  g_gmailGadget.login();
}

function _mailView_uiMailView_toggleSwitchComboVisibility() {
  g_gmailGadget.mailView.uiMailView.toggleSwitchComboVisibility();
}

function _mailView_uiMailView_onSearchKeyDown() {
  g_gmailGadget.mailView.uiMailView.onSearchKeyDown();
}

function _mailView_uiMailView_onSearchFocus(bool) {
  g_gmailGadget.mailView.uiMailView.onSearchFocus(bool);
}

function _mailView_uiMailView_toggleSearchVisibility() {
  g_gmailGadget.mailView.uiMailView.toggleSearchVisibility();
}

function _mailView_browseNewerMails() {
  g_gmailGadget.mailView.browseNewerMails();
}

function _mailView_browseOlderMails() {
  g_gmailGadget.mailView.browseOlderMails();
}

function _compose() {
  g_gmailGadget.compose();
}

function _logout() {
  g_gmailGadget.logout();
}

function _removeActionsMenu() {
  g_gmailGadget.removeActionsMenu();
}

function _mailView_uiMailView_switchComboStartScroll(bool) {
  g_gmailGadget.mailView.uiMailView.switchComboStartScroll(bool);
}

function _mailView_uiMailView_switchComboStopScroll(bool) {
  g_gmailGadget.mailView.uiMailView.switchComboStopScroll(bool);
}

function _mailView_onSwitchComboChange() {
  g_gmailGadget.mailView.onSwitchComboChange();
}

function _mailView_actionsMenuOptionChosen() {
  g_gmailGadget.mailView.actionsMenuOptionChosen();
}
