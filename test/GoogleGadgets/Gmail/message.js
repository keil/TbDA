// Copyright 2007 Google Inc.
// All Rights Reserved.

// @fileoverview Functions for handling messages.

// This Structure holds all the data related to each message
function Message(thread) {
  this.starred = null;
  this.attachment = null;
  this.attachments = null;
  this.sender = null;
  this.recepients = null;
  this.shortDate = null;
  this.from = null;
  this.replyTo = null;
  this.to = null;
  this.cc = null;
  this.bcc = null;
  this.longDate = null;
  this.subject = null;
  this.id = null;
  this.replyRecepients = null;
  this.replyAllRecepients = null;
  this.body = null;
  this.offset = null;

  this.thread = thread;

  this.isComplete = false;
}

Message.prototype.getFromColor = function() {
  return this.thread.getMessageColor(this);
};

/**
 * This function toggles the star for the messages in the conversation
 * @param {Function} callback Called when operation completes.
 * @param {Function} onFail Called if operation fails.
 */
Message.prototype.toggleMessageStar = function(callback, onFail) {
  var data = UrlBuilder.buildMsgStarUrl(
      this.starred ? ACTION.UNSTAR : ACTION.STAR, this.thread.id,
      this.thread.getActionToken(), this.id,
      this.thread.isSpam, this.thread.isTrash);
  g_httpRequest.connectToGmailServer(data,
      bind(this, this.toggleMsgStarComplete_, callback),
      onFail, false, this, true);
};



/**
 * This function handles the UI manipulations once the star/unstar operation
 * is complete
 * It also handles the main UI star if the star is the first one marked
 * or the last one removed
 */
Message.prototype.toggleMsgStarComplete_ = function(callback,
    responseData, refThis) {
  try {
    refThis.starred = !refThis.starred;
    refThis.thread.updateThreadStars();
    callback();
  } catch (e) {
    debug.error(e.message);
  }
};

Message.prototype.expand = function(callback, onFail) {
  this.thread.expandMessage(this, callback, onFail);
};

// Simple hash value calculation with some of ascii characters of sender
Message.prototype.hashValue = function() {
  var hashNum = 0;
  var str = this.from;
  for (var i = 0; i < str.length; ++i) {
    hashNum += str.charCodeAt(i);
  }
  return hashNum;
};

/**
 * Updates the body.
 */
Message.prototype.updateBody = function(body) {
  this.body = body;
  this.isComplete = true;
};

/**
 * Returns a reply subject stripped of any reply or forward prefixes.
 * @param {boolean} isForward Forward as opposed to reply.
 */
Message.prototype.getReplySubject = function(isForward) {
  var subject = this.subject;

  if (subject.indexOf(REPLY_STRING + ' ') === 0) {
    subject = subject.substr(REPLY_STRING.length + 1);
  } else if (subject.indexOf(FORWARD_STRING + ' ') === 0) {
    subject = subject.substr(FORWARD_STRING.length + 1);
  }

  subject = (isForward ? FORWARD_STRING : REPLY_STRING) + ' ' + subject;

  return subject;
};
