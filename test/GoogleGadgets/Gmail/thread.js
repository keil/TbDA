// Copyright 2007 Google Inc.
// All Rights Reserved.

// @fileoverview Functions for handling thread manipulations.

function Thread(mailView) {
  this.id = null;
  this.read = null;
  this.starred = null;
  this.hasAttachment = null;
  this.sender = null;
  this.subject = null;
  this.date = null;
  this.isSpam = null;
  this.isTrash = null;
  this.ui = {mainItem: null, attachImg: null, starImg: null, senderLbl: null,
      dateLbl: null, subjectLbl: null};
  this.mailView = mailView;
  this.messages = [];

  this.colorMap = [];
  for (var i = 0; i < SENDER_COLOR_PALETTE.length; ++i) {
    this.colorMap.push({color: SENDER_COLOR_PALETTE[i], from: []});
  }
}

/**
 * This function gets the color of the heading for a message in the
 * conversation . It checks the color map to see if the sender has already
 * occured in the threadlist. If yes, it returns the same color. Else,
 * it makes a new entry into the color map and returns the new color
 * @param{Message} message is the message for which to fetch the color
 */
Thread.prototype.getMessageColor = function(message) {
  for (var i = 0; i < this.colorMap.length; ++i) {
    for (var j = 0; j < this.colorMap[i].from.length; ++j) {
      if (this.colorMap[i].from[j] == message.from) {
        return this.colorMap[i].color;
      }
    }
  }

  var initIndex = Math.abs(message.hashValue() % this.colorMap.length);
  var curIndex = initIndex;

  do {
    if (this.colorMap[curIndex].from.length === 0) {
      this.colorMap[curIndex].from.push(message.from);
      return this.colorMap[curIndex].color;
    }
    curIndex = (curIndex + 1) % this.colorMap.length;
  } while(curIndex != initIndex);

  this.colorMap[initIndex].from.push(message.from);
  return this.colorMap[initIndex].color;
};

Thread.prototype.getActionToken = function() {
  return this.mailView.actionToken;
};

/**
 * This function executes actions on messages
 * @param {Number} action is the number of the action chosen
 * @param {Number} i is the index of the message in the conversation
 */
Thread.prototype.actionExecute = function(action) {
  var data = UrlBuilder.buildActionUrl(action,
      this.id, this.getActionToken(), this.isSpam, this.isTrash);
  g_httpRequest.connectToGmailServer(data,
      bind(this, this.actionExecutionComplete_, action), null, false);
};

/**
 * This function handles the UI changes once any action
 * is performed successfully
 */
Thread.prototype.actionExecutionComplete_ = function(action, responseData) {
  if (action < ACTION.STAR) {
    this.mailView.refreshView();
  }
  if (action == ACTION.ARCHIVE) {
    this.ensureDetailsClosed();
    g_errorMessage.displayMessage(CONVERSATION_ARCHIVED);
  } else if (action == ACTION.TRASH) {
    this.ensureDetailsClosed();
    g_errorMessage.displayMessage(CONVERSATION_TRASHED);
  } else if (action == ACTION.REPORT_SPAM) {
    g_errorMessage.displayMessage(CONVERSATION_REPORT_SPAM);
  } else if (action == ACTION.STAR) {
    this.ui.starImg.src = IMAGE_PATHS.STAR_ON;
    this.starred = true;
  } else if (action == ACTION.UNSTAR) {
    this.ui.starImg.src = IMAGE_PATHS.STAR_OFF;
    this.starred = false;
    if (this.mailView.folder == FILTERS.STARRED) {
      this.mailView.refreshView();
    }
  } else if (action == ACTION.MARK_AS_READ &&
      !this.read) {
    this.ui.senderLbl.bold = false;
    this.read = true;
    --this.mailView.unreadResponseThreads;
    this.mailView.uiMailView.updateFolderLabel();
    this.mailView.uiMailView.updateLabelUnreadCount(true);
  } else if (action == ACTION.MARK_AS_UNREAD &&
      this.read) {
    this.ui.senderLbl.bold = true;
    this.read = false;
    ++this.mailView.unreadResponseThreads;
    this.mailView.uiMailView.updateFolderLabel();
    this.mailView.uiMailView.updateLabelUnreadCount(false);
  }
};

/**
 * This function toggles the star on a mail
 * @param {Number} i is the index of the mail in the threadlist
 */
Thread.prototype.toggleStar = function() {
  if (!mailList.enabled) {
    return;
  }

  this.actionExecute(this.starred ? ACTION.UNSTAR : ACTION.STAR);
};

Thread.prototype.archive = function() {
  this.actionExecute(ACTION.ARCHIVE);
};

/**
 * Invoked when the user clicks on a particular mail he/she wants to read.
 * Opens the details view and gives it the thread number.
 */
Thread.prototype.open = function() {
  if (!mailList.enabled) {
    event.returnValue = false;
    return;
  }

  // If already open, close the details view.
  if (g_detailsView.isOpen(this.getDetailsViewId())) {
    g_detailsView.close();
    return;
  }

  if (g_detailsView.detailsView &&
      g_detailsView.detailsView.external.loadThread) {
    g_detailsView.detailsView.external.loadThread(this);
    g_detailsView.openedId = this.getDetailsViewId();
  } else {
    // Create a new details view for displating the mail
    // Display the details view and then pass control to details.js as this
    // results in better user perception of the speed and responsiveness
    var detailsView = new DetailsView();
    detailsView.html_content = true;
    detailsView.setContent('', undefined, g_resources.getDetailsHtml(),
        false, 0);
    detailsView.external = {};
    detailsView.external.thread = this;
    detailsView.external.getMessage = Utils.getMessage;
    detailsView.external.getContacts =
        bind(this.mailView, this.mailView.getContacts);

    g_detailsView.open(this.getDetailsViewId(), detailsView, '',
        gddDetailsViewFlagToolbarOpen | gddDetailsViewFlagDisableAutoClose);
  }

  // Handle the changes in UI to show that the mail is now read.
  if (!this.read) {
    if (this.mailView.unreadResponseThreads > 0) {
      --this.mailView.unreadResponseThreads;
      this.mailView.uiMailView.updateFolderLabel();
    }
    this.ui.senderLbl.bold = false;
    this.read = true;
    this.mailView.uiMailView.updateLabelUnreadCount(true);
  }
};

Thread.prototype.close = function() {
  this.ensureDetailsClosed();
};

Thread.DETAILS_VIEW_ID_PREFIX = 'THREAD_';

Thread.prototype.getDetailsViewId = function() {
  return Thread.DETAILS_VIEW_ID_PREFIX + this.id;
};

/**
 * Closes the details view if the thread is open.
 */
Thread.prototype.ensureDetailsClosed = function() {
  g_detailsView.ensureClose(this.getDetailsViewId());
};

/**
 * Builds URL to view conversation in browser Gmail.
 */
Thread.prototype.buildBrowserUrl = function() {
  // Gmail host is always available in the global HTTPRequest.
  var host = CONNECTION_DATA.HTTPS_PREFIX + g_httpRequest.host;

  var params = '/?' +
      'fs=1' +  // Top frame of frameset.
      UrlBuilder.urlFromKeyValue('tf', 1) +  // Don't show left hand nav menu.
      UrlBuilder.urlFromKeyValue('client', REPORTED_CLIENT_NAME) +
      UrlBuilder.urlFromKeyValue('view', 'cv') +  // Conversation view.
      UrlBuilder.urlFromKeyValue('search', 'all') +  // All messages in thread.
      UrlBuilder.urlFromKeyValue('th', this.id);

  return host + params;
};

Thread.prototype.openInBrowser = function() {
  this.mailView.attemptAuthorizedOpenUrl(this.buildBrowserUrl());
};

/**
 * Retrieves body for a message.
 * @param {Message} message The message object.
 * @param {Function} callback To be called when body is retrieved.
 * @param {Function} onFail To be called upon error.
 */
Thread.prototype.expandMessage = function(message, callback, onFail) {
  var data = UrlBuilder.buildExpandMessageUrl(this.id,
      message.offset, this.isSpam, this.isTrash);

  g_httpRequest.connectToGmailServer(data,
      bind(this, this.parseExpandMessage, callback),
      onFail, false, this, true);
};


/**
 * Parses response and updates message body.
 * @param {Function} callback To be called when message is parsed.
 * @param {ResponseData} responseData Response data.
 * @param {Thread} refThis Thread the message belongs to.
 */
Thread.prototype.parseExpandMessage =
    function(callback, responseData, refThis) {
  if (responseData.getNextLine() != THREAD_LIST_DATA_INFO.LEGAL_CONVERSATION) {
    g_errorMessage.displayMessage(ERROR_LOADING_CONVERSATION);
    return;
  }

  // Skip threadId.
  responseData.skipLines(1);
  // Skip conversation title.
  responseData.skipLines(1);

  var totalMessages = parseInt(responseData.getNextLine(), BASE_TEN);

  var parsedMessages = {};

  for (var i = 0; i < totalMessages; ++i) {
    var parsedMessage = refThis.parseMessage(responseData);
    parsedMessages[parsedMessage.id] = parsedMessage;
  }

  for (i = 0; i < refThis.messages.length; ++i) {
    var message = refThis.messages[i];

    if (!message.isComplete) {
      var completeMessage = parsedMessages[message.id];

      if (completeMessage && completeMessage.isComplete) {
        message.updateBody(completeMessage.body);
      }
    }
  }

  refThis.callExternal(callback);
};

Thread.prototype.callExternal = function(callback) {
  try {
    callback();
  } catch(e) {
    debug.error(e.message);
  }
};

Thread.prototype.getMessages = function(callback, onFail) {
  var searchQuery =
      this.mailView.folder == FILTERS.SEARCH ? searchText.value : null;
  var data = UrlBuilder.buildConversationUrl(this.id,
      searchQuery,
      this.isSpam, this.isTrash);
  g_httpRequest.connectToGmailServer(data,
      bind(this, this.parseMail, callback),
      onFail, false, this, true);
};

/**
 * Sends a reply or forward message.
 *
 * @param {Function} callback Called upon success.
 * @param {Function} onFail Called upon failure.
 * @param {bool} isForward Is a forward instead of reply.
 * @param {string} messageId Message id.
 * @param {string} to
 * @param {string} subject
 * @param {string} body
 * @param {string} cc
 * @param {string} bcc
 */
Thread.prototype.replyOrForward = function(callback, onFail,
    isForward, messageId, to, subject, body, cc, bcc) {
  var data = UrlBuilder.buildComposeUrl(
      this.getActionToken(),
      this.mailView.viewOffset,
      this.mailView.mailsInView,
      to, cc, bcc, subject, body);

  data = UrlBuilder.buildReplyOrForwardUrl(data, isForward, messageId,
      this.subject, this.id, this.isSpam, this.isTrash);

  g_httpRequest.connectToGmailServer(data,
      bind(this, this.sentSuccessfully, callback, onFail),
      onFail, false, this);
};

/**
 * This function handles the incoming threadlist after a successul send
 *
 * @param {Function} callback Called upon success.
 * @param {Function} onFail Called upon failure.
 */
Thread.prototype.sentSuccessfully = function(callback, onFail,
    responseData, refThis) {
  // Check for a valid response start.
  var firstLine = responseData.getNextLine();
  if (firstLine == THREAD_LIST_DATA_INFO.LEGAL_CONVERSATION ||
      firstLine == THREAD_LIST_DATA_INFO.TRUE) {
    refThis.callExternal(callback);
    if (refThis.mailView.folder == FILTERS.INBOX) {
      refThis.mailView.requestInbox(0);
    }
  } else {
    onFail();
  }
};

/**
 * Parses the threadlist response for the next message.
 */
Thread.prototype.parseMessage = function(responseData) {
  var refThis = this;
  var curMsg = new Message(refThis);

  // This method woefully lacks error checking.
  // The strategy is for errors to bubble up and get caught.

  // read the exansion status
  curMsg.isComplete = responseData.getNextLine() == THREAD_LIST_DATA_INFO.TRUE;

  // read the message starred status
  curMsg.starred = responseData.getNextLine() == THREAD_LIST_DATA_INFO.TRUE;

  // read if the message has an attachment
  curMsg.attachment =
      responseData.getNextLine() == THREAD_LIST_DATA_INFO.TRUE;

  // read the message sender
  curMsg.sender = responseData.getNextLine();
  if (curMsg.sender == BLANK_LINE) {
    curMsg.sender = UNKNOWN_SENDER;
  }

  // read message recepients
  curMsg.recepients = responseData.getNextLine();
  if (curMsg.recepients == BLANK_LINE) {
    curMsg.recepients = ME;
  }

  // read message short date format
  curMsg.shortDate = responseData.getNextLine();
  if (curMsg.shortDate.charAt(curMsg.shortDate.length - 1) ==
      THREAD_LIST_DATA_INFO.PM) {
    curMsg.shortDate =
        curMsg.shortDate.substr(0, curMsg.shortDate.length - 1) + PM;
  } else if (curMsg.shortDate.charAt(curMsg.shortDate.length - 1) ==
      THREAD_LIST_DATA_INFO.AM) {
    curMsg.shortDate =
        curMsg.shortDate.substr(0, curMsg.shortDate.length - 1) + AM;
  }

  // read message from address
  curMsg.from = responseData.getNextLine();
  if (curMsg.from == BLANK_LINE) {
    curMsg.from = UNKNOWN_SENDER;
  }

  // reply to
  curMsg.replyTo = responseData.getNextLine();

  // read to recepients
  curMsg.to = responseData.getNextLine();

  // read cc recepients
  curMsg.cc = responseData.getNextLine();

  // read bcc recepients
  curMsg.bcc = responseData.getNextLine();

  // read message long date format
  curMsg.longDate = responseData.getNextLine();

  // read message subject
  curMsg.subject = responseData.getNextLine();
  if (curMsg.subject == BLANK_LINE) {
    curMsg.subject = NO_SUBJECT;
  }

  // read message id
  var url = responseData.getNextLine();
  var parts = url.split('#');
  curMsg.offset = parts[0].split('&')[1].split('=')[1];
  curMsg.id = parts[1].split('_')[1];

  // jump over the sender phone line
  responseData.skipLines(1);

  // read message reply recepients
  curMsg.replyRecepients = responseData.getNextLine();

  // read message reply all recepients
  curMsg.replyAllRecepients = responseData.getNextLine();

  // read message body
  var msgBody = [];
  var currentLine = responseData.getNextLine();
  while (currentLine.charAt(0) ==
         THREAD_LIST_DATA_INFO.MESSAGE_BODY_LINE_STARTING_CHARACTER) {
    currentLine = currentLine.substr(
        THREAD_LIST_DATA_INFO.MESSAGE_BODY_LINE_STARTING_CHARACTER.length);
    msgBody.push(currentLine);
    currentLine = responseData.getNextLine();
  }
  var body = msgBody.join(END_LINE_CHAR);
  // No need for target. This is not a browser.
  body = body.replace(/\s+target="_blank"/g, ' ');
  curMsg.body = body;
  curMsg.attachments = [];

  if (currentLine == THREAD_LIST_DATA_INFO.TRUE ||
      currentLine == THREAD_LIST_DATA_INFO.FALSE) {
    // the message is done, but another message of the conversation started
    responseData.decPtr();
  } else {
    // this message has some attachments (currentLine attachments)
    for (var j = 0; j < currentLine; ++j) {
      // each attachment is written on 3 lines : filename, id and mimetype
      // REMOVE_GADGET_BEGIN
      // TODO: implement attachment handling.
      // curMsg.attachments.push(new Attachment(responseData.getNextLine(),
      // responseData.getNextLine(), responseData.getNextLine()));
      // REMOVE_GADGET_END
      responseData.getNextLine();
      responseData.getNextLine();
      responseData.getNextLine();
    }
  }

  return curMsg;
};

/**
 * Parses a thread and all its messages.
 */
Thread.prototype.parseMail = function(callback, responseData, refThis) {
  if (responseData.getNextLine() !=
      THREAD_LIST_DATA_INFO.LEGAL_CONVERSATION) {
    g_errorMessage.displayMessage(ERROR_LOADING_CONVERSATION);
    return;
  }

  if (responseData.getNextLine() != refThis.id) {
    debug.warning('Invalid thread id.');
  }

  // read the conversation title
  refThis.subject = responseData.getNextLine();
  if (refThis.subject == BLANK_LINE) {
    refThis.subject = NO_SUBJECT;
  }

  var totalMessages = parseInt(responseData.getNextLine(), BASE_TEN);
  refThis.messages = [];

  for (var i = 0; i < totalMessages; ++i) {
    try {
      var curMsg = refThis.parseMessage(responseData);
      refThis.messages.push(curMsg);
    } catch (ex) {
      debug.error('Message parse error: ' + ex.message);
    }
  }

  refThis.callExternal(callback);
};

/**
 * Calculates the number of starred messages in this conversation
 */
Thread.prototype.starCount = function() {
  var count = 0;
  for (var i = 0; i < this.messages.length; ++i) {
    if (this.messages[i].starred) {
      ++count;
    }
  }
  return count;
};

Thread.prototype.updateThreadStars = function() {
  var stars = this.starCount();
  debug.trace('Number of stars: ' + stars);
  if (stars === 0 || stars == 1) {
    for (var i = 0; i < this.mailView.threads.length; ++i) {
      if (this.id == this.mailView.threads[i].id) {
        if (stars === 0) {
          this.mailView.threads[i].starred = false;
          this.mailView.threads[i].ui.starImg.src = IMAGE_PATHS.STAR_OFF;
        } else {
          this.mailView.threads[i].starred = true;
          this.mailView.threads[i].ui.starImg.src = IMAGE_PATHS.STAR_ON;
        }
        return;
      }
    }
  }
};
