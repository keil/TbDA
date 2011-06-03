// Copyright 2007 Google Inc.
// All Rights Reserved.

// @fileoverview Functions for handling the ui of the mail view

function UiMailView(mailView) {
  // Used to compute number of new mails to be displayed
  this.oldMailsInView = 0;
  this.selectLast = false; // when true the last mail not the first is focused

  this.switchComboScrollTimer = null;

  this.mailView = mailView;
}



/**
 * Invoked when the inbox data arrives. Parses the threadlist and
 * renders the listbox.
 */
UiMailView.prototype.showResponse = function() {
  this.updateFolderLabel();
  folderLabel.visible = true;
  for (var i = 0; i < this.mailView.fetchedResponseThreads; ++i) {
    // Create the XML for the next list item to be appended
    // the item
    var curThread = this.mailView.threads[i];
    curThread.ui.mainItem = mailList.appendElement(
        '<item width = "100%" background = "' +
        sanitizeXml(mailList.background) +
        '" cursor="hand" />');
    // sender label
    curThread.ui.senderLbl = curThread.ui.mainItem.appendElement(
        '<label width = "' + sanitizeXml(mailList.width - UIDATA.DATE_WIDTH) +
        '" font = "' + UIDATA.FONT + '" size = "' + UIDATA.FONT_SIZE_SENDER +
        '" bold = "' + (curThread.read ? 'false' : 'true') +
        '" trimming="character-ellipsis" />');
    // date label
    curThread.ui.dateLbl = curThread.ui.mainItem.appendElement(
        '<label align="right" color = "' + UIDATA.FONT_COLOR_MAIL +
        '" x="' + sanitizeXml(mailList.width - UIDATA.DATE_WIDTH) +
        '" font="' + UIDATA.FONT + '" size = "' + UIDATA.FONT_SIZE_DATE +
        '" width = "' + (UIDATA.DATE_WIDTH) + '">' +
        curThread.date + '</label>');
    // subject label
    curThread.ui.subjectLbl = curThread.ui.mainItem.appendElement(
        '<label y="' + UIDATA.MAIL_FIRST_LINE_HEIGHT +
        '" width="' + sanitizeXml(mailList.width -
        (curThread.hasAttachment ? UIDATA.PAPERCLIP_OFFSET :
        UIDATA.STAR_OFFSET)) + '" size="' + UIDATA.FONT_SIZE_SUBJECT +
        '" color="' + UIDATA.FONT_COLOR_SUBJECT +
        '" font="' + UIDATA.FONT + '" trimming="character-ellipsis" />');
    // attachment image
    curThread.ui.attachImg = curThread.ui.mainItem.appendElement(
        '<img src = "' + (curThread.hasAttachment ?
        IMAGE_PATHS.PAPERCLIP : '') + '" x="' +
        sanitizeXml(mailList.width - UIDATA.PAPERCLIP_OFFSET) + '" y = "' +
        (UIDATA.MAIL_FIRST_LINE_HEIGHT) + '"/>');
    // star image
    curThread.ui.starImg = curThread.ui.mainItem.appendElement(
        '<img src = "' + (curThread.starred ?
        IMAGE_PATHS.STAR_ON : IMAGE_PATHS.STAR_OFF) +
        '" x = "' + sanitizeXml(mailList.width - UIDATA.STAR_OFFSET) +
        '" y = "' + (UIDATA.MAIL_FIRST_LINE_HEIGHT) +
        '" enabled="true" cursor="hand"/>');
    curThread.ui.mainItem.appendElement('<div y="' +
        sanitizeXml(mailList.itemHeight - 1) +
        '" width="100%" height="1" background="#DDDDDD" />');
    curThread.ui.mainItem.onclick = (function(curThread) {
      return function() {
        curThread.open();
      };
    })(curThread);
    curThread.ui.mainItem.onmouseover = (function(i, curThread) {
      return function() {
        if (mailList.enabled) {
          mailList.selectedIndex = i;
        }
      };
    })(i, curThread);
    curThread.ui.mainItem.onrclick = (function(curThread) {
      return function() {
        var eventX = event.type == 'onkeyup' ? view.width * 0.3 : event.x;
        var eventY = event.type == 'onkeyup' ?
            mailList.itemHeight * 0.4 : event.y;
        curThread.mailView.uiMailView.popupActionsMenu(
            curThread, eventX, eventY);
      };
    })(curThread);
    curThread.ui.starImg.onclick = (function(curThread) {
      return function() {
        curThread.toggleStar();
      };
    })(curThread);
    curThread.ui.senderLbl.innerText = curThread.sender;
    curThread.ui.senderLbl.tooltip = curThread.sender;
    curThread.ui.subjectLbl.innerText = curThread.subject;
    curThread.ui.subjectLbl.tooltip = curThread.subject;
  }
  mailList.visible = true;
  this.mailView.gadget.onResize(false);
};



/**
 * This function updates the ability to navigate the view to either side
 * based on current view state
 */
UiMailView.prototype.updateNavigationCount = function() {
  var titleString;
  if (this.mailView.fetchedResponseThreads) {
    titleString = (this.mailView.viewOffset + 1) + HYPHEN +
                  (this.mailView.viewOffset +
                  this.mailView.fetchedResponseThreads) + ' ' + OF +
                  ' ' + this.mailView.totalResponseThreads;
  } else {
    titleString = '0' + HYPHEN + '0 ' + OF + ' 0';
  }
  browseLabel.innerText = titleString;
};



/**
 * This function updates the String in the title of the gadget based on the
 * current folder
 */
UiMailView.prototype.updateFolderLabel = function() {
  var folderDisplayName;
  if (this.mailView.folder == FILTERS.INBOX) {
    folderDisplayName = INBOX;
    switchCombo.selectedIndex = 0;
  } else if (this.mailView.folder == FILTERS.SEARCH) {
    folderDisplayName = SEARCH_RESULTS;
    switchCombo.clearSelection();
  } else if (this.mailView.folder == FILTERS.LABEL) {
    folderDisplayName = this.mailView.labels[this.mailView.labelIndex].name;
    switchCombo.selectedIndex = this.mailView.labelIndex +
        COMBO_POSITION['LABELS_SEPARATOR'] + 1;
  } else if (this.mailView.folder == FILTERS.STARRED) {
    folderDisplayName = STARRED;
    switchCombo.selectedIndex = 1;
  } else if (this.mailView.folder == FILTERS.CHATS) {
    folderDisplayName = CHATS;
    switchCombo.selectedIndex = 2;
  } else if (this.mailView.folder == FILTERS.SENT_MAIL) {
    folderDisplayName = SENT_MAIL;
    switchCombo.selectedIndex = 3;
  } else if (this.mailView.folder == FILTERS.ALL_MAIL) {
    folderDisplayName = ALL_MAIL;
    switchCombo.selectedIndex = 4;
  } else if (this.mailView.folder == FILTERS.SPAM) {
    folderDisplayName = SPAM;
    switchCombo.selectedIndex = 5;
  } else if (this.mailView.folder == FILTERS.TRASH) {
    folderDisplayName = TRASH;
    switchCombo.selectedIndex = 6;
  }

  if (!this.mailView.unreadResponseThreads ||
      this.mailView.folder != FILTERS.INBOX) {
    folderLabel.innerText = folderDisplayName + ' \u25BC';
    folderLabel.bold = false;
  } else {
    folderLabel.innerText = folderDisplayName + '(' +
        this.mailView.unreadResponseThreads + ') \u25BC';
    folderLabel.bold = true;
  }
};



/**
 * This function populates the action combobox
 */
UiMailView.prototype.populateSwitchCombo = function() {
  switchCombo.removeAllElements();
  var i = 0;
  var folders = [INBOX, STARRED, CHATS, SENT_MAIL, ALL_MAIL, SPAM, TRASH];
  var curItem = null;
  for (i = 0; i < folders.length; ++i) {
    curItem = switchCombo.appendElement('<item><label y="1" size="8" font="' +
        UIDATA.FONT + '">' + sanitizeXml(folders[i]) + '</label></item>');
    curItem.onclick = this.makeSwitchComboOnClick(curItem);
    if (this.mailView.labels.length > 0 || i < folders.length - 1) {
      curItem.appendElement('<div y="' +
          sanitizeXml(switchCombo.itemHeight - 1) +
          '" width="100%" height="1" background="#DDDDDD" />');
    }
  }
  if (this.mailView.labels.length > 0) {
    switchCombo.appendElement(
        '<item enabled="false"><label y="1" color="#BBBBBB">--------</label>' +
        '<div width="100%" height="1" y="' +
        sanitizeXml(switchCombo.itemHeight - 1) + '" background="#DDDDDD" /></item>');
  }
  for (i = 0; i < this.mailView.labels.length; ++i) {
    curItem = switchCombo.appendElement('<item />');
    if (i < this.mailView.labels.length - 1) {
      curItem.appendElement('<div width="100%" height="1" y="' +
          sanitizeXml(switchCombo.itemHeight - 1) + '" background="#DDDDDD" />');
    }
    curItem.onclick = this.makeSwitchComboOnClick(curItem);
    curItem = curItem.appendElement('<label y="1" size="' +
        UIDATA.ACTION_COMBO_TEXT_SIZE + '" color="' + UIDATA.LABELS_COLOR +
        '" bold="' + (this.mailView.labels[i].numUnread ? 'true' : 'false') +
        '" font="' + UIDATA.FONT + '">' + (this.mailView.labels[i].numUnread ?
        '(' + sanitizeXml(this.mailView.labels[i].numUnread) + ')' : '') + '</label>');
    curItem.innerText = this.mailView.labels[i].name + curItem.innerText;
  }
  switchCombo.selectedIndex = 0;
};

UiMailView.prototype.makeSwitchComboOnClick = function(item) {
  return function() {
    switchCombo.selectedItem = item;
    g_gmailGadget.mailView.onSwitchComboChange();
  };
};

UiMailView.prototype.toggleSwitchComboVisibility = function() {
  if (!switchComboBorder.visible) {
    switchComboBorder.width = mainDiv.width - 4;
    switchComboContainer.width = switchComboUp.width = switchComboDown.width =
        switchComboBorder.width - 2 * switchComboContainer.x;

    switchComboBorder.height = 1 + switchCombo.itemHeight *
        Math.min(Math.floor(mailList.height / switchCombo.itemHeight), 9);
    switchComboContainer.height = switchComboBorder.height - 1 -
        switchCombo.itemHeight;
    switchComboDown.y = switchComboContainer.height + switchComboContainer.y;

    switchCombo.height = switchCombo.itemHeight * switchCombo.children.count;
    switchCombo.y = 0;
  }
  switchComboBorder.visible = actionMenuCloser.visible =
      !switchComboBorder.visible;
  switchCombo.clearSelection();
};



/**
 * This function pops up the right click actions menu
 */
UiMailView.prototype.popupActionsMenu = function(curThread, eventX, eventY) {
  if (!mailList.enabled) {
    return;
  }
  mnuTrash.enabled = this.mailView.folder != FILTERS.TRASH;
  mnuSpam.enabled = this.mailView.folder != FILTERS.SPAM;
  lblTrash.opacity = mnuTrash.enabled ? 255 : 128;
  lblSpam.opacity = mnuSpam.enabled ? 255 : 128;
  mnuStar.innerText = curThread.starred ? MENU_REMOVE_STAR : MENU_STAR;
  mnuMarkRead.innerText = curThread.read ? MENU_MARK_UNREAD : MENU_MARK_READ;
  actionsMenu.selectedIndex = 0;
  actionsMenuBorder.visible = actionMenuCloser.visible = true;
  eventX += mailList.x + mainDiv.x;
  eventY += mailList.y + mainDiv.y + curThread.ui.mainItem.offsetY;
  if (eventX + actionsMenuBorder.width < view.width) {
    actionsMenuBorder.x = eventX;
  } else {
    actionsMenuBorder.x = eventX - actionsMenuBorder.width;
    if (actionsMenuBorder.x < 0) {
      actionsMenuBorder.x = 0;
    }
  }
  if (eventY + actionsMenuBorder.height < view.height) {
    actionsMenuBorder.y = eventY;
  } else {
    actionsMenuBorder.y = eventY - actionsMenuBorder.height;
    if (actionsMenuBorder.y < 0) {
      actionsMenuBorder.y = 0;
    }
  }
};



/**
 * This function toggles the visibility of the search box in the view
 * It also handles the related UI changes for the other view elements
 */
UiMailView.prototype.toggleSearchVisibility = function() {
  searchTextBorder.visible = !searchTextBorder.visible;

  if (searchTextBorder.visible) {
    this.mailView.gadget.onResize(false);
    searchText.focus();
  } else {
    if (this.mailView.folder == FILTERS.SEARCH) {
      // Go back to inbox.
      switchCombo.selectedIndex = COMBO_POSITION['INBOX'];
    }
    this.mailView.onSwitchComboChange();
  }
};



/**
 * This function is called onfocusin/out of the search editbox.
 */
UiMailView.prototype.onSearchFocus = function(got) {
  this.mailView.gadget.keyboardShortcutsEnabled = !got;
};



/**
 * This function is called when the search button is clicked
 */
UiMailView.prototype.onSearchClicked = function() {
  this.mailView.folder = FILTERS.SEARCH;
  this.mailView.search(0, null);
};



/**
 * This function is called onkeydown of the search editbox. accepts enter key.
 */
UiMailView.prototype.onSearchKeyDown = function() {
  if (event.keycode == KEYS.ENTER) {
    this.mailView.folder = FILTERS.SEARCH;
    this.mailView.search(0, null);
  } else if (event.keycode == KEYS.ESCAPE) {
    this.toggleSearchVisibility();
  }
};



/**
 * This function is used to update the unread count of a label folder when a
 * mail is marked as read, unread, or opened, etc. This count is updated in
 * the title label and the view switch combo box
 * @param {Boolean} decrease: true if count is to be decreased, else false
 */
UiMailView.prototype.updateLabelUnreadCount = function(decrease) {
  // if the current folder is a label and has unread mails, decrement the count
  if (this.mailView.folder == FILTERS.LABEL) {
    if (this.mailView.labels[switchCombo.selectedIndex -
        COMBO_POSITION['LABELS_SEPARATOR'] - 1].numUnread > 0) {
      if (decrease) {
        --this.mailView.labels[switchCombo.selectedIndex -
            COMBO_POSITION['LABELS_SEPARATOR'] - 1].numUnread;
      } else {
        ++this.mailView.labels[switchCombo.selectedIndex -
            COMBO_POSITION['LABELS_SEPARATOR'] - 1].numUnread;
      }
      var labelNum = switchCombo.selectedIndex;
      this.populateSwitchCombo();
      switchCombo.selectedIndex = labelNum;
      folderLabel.innerText =
          this.mailView.labels[this.mailView.labelIndex].name + ' \u25BC';
    }
  }
};



/**
 * Invoked when the user is done resizing.
 * Calculates new inbox view size and invokes requestInbox accordingly
 */
UiMailView.prototype.resizeView = function() {
  if (!mainDiv.visible) {
    return;
  }
  // Calculate number of mails that can now fit into listbox
  this.updateNumberOfMailInView();
  // If number of mails has increased, refresh inbox accordingly
  if (this.mailView.mailsInView > this.oldMailsInView) {
    this.mailView.refreshView();
  // else just reduce the number of mails displayed
  } else {
    var numListElements = mailList.children.count;
    var i;
    for (i = this.mailView.mailsInView; i < numListElements; ++i) {
      mailList.removeElement(mailList.children(
          this.mailView.mailsInView));
    }
    this.mailView.fetchedResponseThreads = mailList.children.count;
    // Adjust each mail entry
    for (i = 0; i < this.mailView.fetchedResponseThreads; ++i) {
      var curMailUi = this.mailView.threads[i].ui;
      curMailUi.attachImg.x = mailList.width - UIDATA.PAPERCLIP_OFFSET;
      curMailUi.starImg.x = mailList.width - UIDATA.STAR_OFFSET;
      curMailUi.subjectLbl.width = mailList.width -
          (curMailUi.attachImg.src === '' ? UIDATA.STAR_OFFSET :
          UIDATA.PAPERCLIP_OFFSET);
      curMailUi.dateLbl.x = curMailUi.senderLbl.width = mailList.width -
          UIDATA.DATE_WIDTH;
    }
  }
  this.updateNavigationCount();
  this.oldMailsInView = this.mailView.mailsInView;
};



/**
 * This function updates the number of mails in the view based on the view
 * height and the constant UI space
 */
UiMailView.prototype.updateNumberOfMailInView = function() {
  // If there is not enough space for any mail, set number of mails to zero.
  if (mailList.height < mailList.itemHeight) {
    this.mailView.mailsInView = 0;
  } else {
    this.mailView.mailsInView =
        Math.floor(mailList.height / mailList.itemHeight);
  }
};



UiMailView.prototype.switchComboScroll = function(up) {
  if (up && switchCombo.y < 0) {
    switchCombo.y += switchCombo.itemHeight;
  } else if (!up &&
      switchCombo.height + switchCombo.y > switchComboContainer.height) {
    switchCombo.y -= switchCombo.itemHeight;
  }
};



UiMailView.prototype.switchComboStartScroll = function(up) {
  if (up) {
    switchComboUp.background = IMAGE_PATHS.SWITCH_SCROLL_UP_HOVER;
  } else {
    switchComboDown.background = IMAGE_PATHS.SWITCH_SCROLL_DOWN_HOVER;
  }
  this.switchComboStopScroll(null);
  this.switchComboScrollTimer = setInterval((function(refThis, refUp) {
      return function() {
        refThis.switchComboScroll(refUp);
      };
    })(this, up), SWITCH_COMBO_SCROLL_DELAY);
  this.switchComboScroll(up);
};



UiMailView.prototype.switchComboStopScroll = function(up) {
  if (up === true) {
    switchComboUp.background = IMAGE_PATHS.SWITCH_SCROLL_UP;
  } else if (up === false) {
    switchComboDown.background = IMAGE_PATHS.SWITCH_SCROLL_DOWN;
  }
  if (this.switchComboScrollTimer !== null) {
    clearInterval(this.switchComboScrollTimer);
    this.switchComboScrollTimer = null;
  }
};
