// Copyright 2007 Google Inc.
// All Rights Reserved.

/**
 * The UrlBuilder class has all the member functions used to generate
 * the urls and return them as and when required
 */
function UrlBuilder() {}
  // All the function names beyond this point in this class are
  // self explanatory



UrlBuilder.urlFromKeyValue = function(key, value) {
  return '&' + key + '=' + encodeURIComponent(value);
};



UrlBuilder.addSpamTrashParam = function(isSpam, isTrash) {
  debug.trace('isSpam = ' + isSpam);
  debug.trace('isTrash = ' + isTrash);
  var result = isSpam ? UrlBuilder.urlFromKeyValue(URL.SPAM_PARAM,
      URL.ENABLED) : '';
  return result + (isTrash ? UrlBuilder.urlFromKeyValue(URL.TRASH_PARAM,
      URL.ENABLED) : '');
};



UrlBuilder.buildLoginUrl = function(numOfMails, user, pass) {
  return CONNECTION_DATA.COMMON_POST_PARAMETERS +
      UrlBuilder.urlFromKeyValue(URL.MODE, URL.LOGIN_MODE) +
      UrlBuilder.urlFromKeyValue(URL.NUM_MAILS_PARAM, numOfMails) +
      UrlBuilder.urlFromKeyValue(URL.USER_PARAM, user) +
      UrlBuilder.urlFromKeyValue(URL.PASS_PARAM, pass);
};



UrlBuilder.buildInboxUrl = function(offset, numOfMails) {
  return CONNECTION_DATA.COMMON_POST_PARAMETERS +
      UrlBuilder.urlFromKeyValue(URL.NUM_MAILS_PARAM, numOfMails) +
      UrlBuilder.urlFromKeyValue(URL.OFFSET_PARAM, offset);
};



UrlBuilder.buildSearchUrl = function(search, offset, numOfMails) {
  return CONNECTION_DATA.COMMON_POST_PARAMETERS +
      UrlBuilder.urlFromKeyValue(URL.SEARCH_LABEL_PARAM, URL.SEARCH_MODE) +
      UrlBuilder.urlFromKeyValue(URL.SEARCH_MODE, search) +
      UrlBuilder.urlFromKeyValue(URL.NUM_MAILS_PARAM, numOfMails) +
      UrlBuilder.urlFromKeyValue(URL.OFFSET_PARAM, offset);
};



UrlBuilder.buildLabelUrl = function(label, offset, numOfMails) {
  return CONNECTION_DATA.COMMON_POST_PARAMETERS +
      UrlBuilder.urlFromKeyValue(URL.SEARCH_LABEL_PARAM, URL.LABEL_MODE) +
      UrlBuilder.urlFromKeyValue(URL.LABEL_MODE, label) +
      UrlBuilder.urlFromKeyValue(URL.NUM_MAILS_PARAM, numOfMails) +
      UrlBuilder.urlFromKeyValue(URL.OFFSET_PARAM, offset);
};



UrlBuilder.buildContactsUrl = function() {
  return CONNECTION_DATA.COMMON_POST_PARAMETERS +
      UrlBuilder.urlFromKeyValue(URL.VIEW_PARAM, URL.CONTACTS_LIST) +
      UrlBuilder.urlFromKeyValue(URL.CONTACTS_LIST_ORDERING,
          URL.CL_ALL_SORTED_FREQUENCY);
};



UrlBuilder.buildComposeUrl = function(actionToken, inboxOffset,
    numberOfMailsInView, to, cc, bcc, subject, body) {
  return CONNECTION_DATA.COMMON_POST_PARAMETERS +
      UrlBuilder.urlFromKeyValue(URL.ACTION_TOKEN_PARAM, actionToken) +
      UrlBuilder.urlFromKeyValue(URL.OFFSET_PARAM, inboxOffset) +
      UrlBuilder.urlFromKeyValue(URL.NUM_MAILS_PARAM, numberOfMailsInView) +
      UrlBuilder.urlFromKeyValue(URL.TO_PARAM, to) +
      UrlBuilder.urlFromKeyValue(URL.CC_PARAM, cc) +
      UrlBuilder.urlFromKeyValue(URL.BCC_PARAM, bcc) +
      UrlBuilder.urlFromKeyValue(URL.SUBJECT_PARAM, subject) +
      UrlBuilder.urlFromKeyValue(URL.BODY_PARAM, body) +
      UrlBuilder.urlFromKeyValue(URL.BU_PARAM, URL.SEND_OPERATION);
};

UrlBuilder.buildReplyOrForwardUrl = function(composeUrl, isForward,
    messageId, threadTitle, threadId, isSpam, isTrash) {
  return composeUrl +
      UrlBuilder.urlFromKeyValue(URL.HAOT_PARAM, isForward ?
          URL.FORWARD_THREAD : URL.QUOTE_THREAD) +
      UrlBuilder.urlFromKeyValue(URL.MESSAGE_URL_PARAM, messageId) +
      UrlBuilder.urlFromKeyValue(URL.CONV_TITLE_PARAM,
          threadTitle) +
      UrlBuilder.urlFromKeyValue(URL.THREAD_ID_PARAM, threadId) +
      UrlBuilder.addSpamTrashParam(isSpam, isTrash);
};

UrlBuilder.buildConversationUrl = function(threadId, searchQuery, isSpam,
    isTrash) {
  // if the gadget is in search mode then all messages
  // which have the search query will be expanded
  var searchAddtion = searchQuery === null ? '' :
      UrlBuilder.urlFromKeyValue(URL.SEARCH_LABEL_PARAM, URL.SEARCH_MODE) +
      UrlBuilder.urlFromKeyValue(URL.SEARCH_MODE, searchQuery);
  return CONNECTION_DATA.COMMON_POST_PARAMETERS +
      UrlBuilder.urlFromKeyValue(URL.VIEW_PARAM, URL.CONVERSATION_VIEW) +
      UrlBuilder.urlFromKeyValue(URL.THREAD_ID_PARAM, threadId) +
      UrlBuilder.urlFromKeyValue(URL.MESSAGE_OUTPUT_HTML_PARAM, 'true') +
      searchAddtion + UrlBuilder.addSpamTrashParam(isSpam, isTrash);
};


UrlBuilder.buildExpandMessageUrl = function(threadId, offset, isSpam,
    isTrash) {
  return CONNECTION_DATA.COMMON_POST_PARAMETERS +
      UrlBuilder.urlFromKeyValue(URL.VIEW_PARAM, URL.CONVERSATION_VIEW) +
      UrlBuilder.urlFromKeyValue(URL.THREAD_ID_PARAM, threadId) +
      UrlBuilder.urlFromKeyValue(URL.MESSAGE_PARAM,
          URL.MESSAGE_EXPAND_OPERATION) +
      UrlBuilder.urlFromKeyValue(URL.MESSAGE_OFFSET_PARAM, offset) +
      UrlBuilder.urlFromKeyValue(URL.MESSAGE_OUTPUT_HTML_PARAM, 'true') +
      UrlBuilder.addSpamTrashParam(isSpam, isTrash);
};


UrlBuilder.buildAttachmentUrl = function(maxWidth, maxHeight, attachmentId,
    threadId) {
  var url = CONNECTION_DATA.COMMON_POST_PARAMETERS +
      UrlBuilder.urlFromKeyValue(URL.ATTACHMENT_VIEW_PARAM,
          URL.ATTACHMENT_VIEW) +
      UrlBuilder.urlFromKeyValue(URL.ATTACHMENT_DISPLAY_PARAM,
          URL.ATTACHMENT_DISPLAY);
  if (maxWidth !== null) {
    url += UrlBuilder.urlFromKeyValue(URL.ATTACHMENT_MAX_WIDTH, maxWidth);
  }
  if (maxHeight !== null) {
    url += UrlBuilder.urlFromKeyValue(URL.ATTACHMENT_MAX_HEIGHT, maxHeight);
  }
  return url +
      UrlBuilder.urlFromKeyValue(URL.ATTACHMENT_ID_PARAM, attachmentId) +
      UrlBuilder.urlFromKeyValue(URL.THREAD_ID_PARAM, threadId) +
      UrlBuilder.urlFromKeyValue(URL.JPEG_SUPPORT_PARAM, URL.ENABLED);
};



UrlBuilder.buildActionUrl = function(action, threadId, actionToken,
    isSpam, isTrash) {
  return CONNECTION_DATA.COMMON_POST_PARAMETERS +
      UrlBuilder.urlFromKeyValue(URL.BU_PARAM, URL.ACTION_PARAM[action]) +
      UrlBuilder.urlFromKeyValue(URL.THREAD_PARAM, threadId) +
      UrlBuilder.urlFromKeyValue(URL.ACTION_TOKEN_PARAM, actionToken) +
      UrlBuilder.addSpamTrashParam(isSpam, isTrash);
};



UrlBuilder.buildMsgStarUrl = function(star, threadId, actionToken, messageId,
                                      isSpam, isTrash) {
  return CONNECTION_DATA.COMMON_POST_PARAMETERS +
      UrlBuilder.urlFromKeyValue(URL.BU_PARAM, URL.ACTION_PARAM[star]) +
      UrlBuilder.urlFromKeyValue(URL.THREAD_ID_PARAM, threadId) +
      UrlBuilder.urlFromKeyValue(URL.ACTION_TOKEN_PARAM, actionToken) +
      UrlBuilder.urlFromKeyValue(URL.MESSAGE_ID_PARAM, messageId) +
      UrlBuilder.addSpamTrashParam(isSpam, isTrash);
};



UrlBuilder.buildLogoutUrl = function() {
  return CONNECTION_DATA.COMMON_POST_PARAMETERS +
      UrlBuilder.urlFromKeyValue(URL.LOGOUT_PARAM, true);
};
