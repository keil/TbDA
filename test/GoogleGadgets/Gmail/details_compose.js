function ComposeUi(parentDiv, to, subject, isForward) {
  this.parentDiv = parentDiv;
  this.isForward = isForward;
  this.isButtonsEnabled = true;

  if (to == MILU_BLANK_LINE_MARKER) {
    to = '';
  }

  this.mainDiv = Ele.create(this.parentDiv, 'div', 'composeDiv');
  Ele.event(this.mainDiv, 'onkeyup', bind(this, this.onKeyUp));

  this.headerDiv = Ele.create(this.mainDiv, 'div', 'composeHeaderDiv');

  // IE has this bug where 100% width textareas fill up the entire window.
  // Wrapping it with a fieldset is one workaround.
  var fixFieldset = Ele.create(this.mainDiv, 'fieldset');
  this.body = Ele.create(fixFieldset, 'textarea', 'composeBody');
  this.footerDiv = Ele.create(this.mainDiv, 'div', 'composeFooterDiv');

  this.fieldsTable = Ele.create(this.headerDiv, 'table', 'composeFieldsTable');
  this.fieldsBody = Ele.create(this.fieldsTable, 'tbody');

  this.contactsUi = new ContactsUi(this.mainDiv);
  this.contactsUi.hide();

  var newField = this.addField(g_strings('TO'));
  this.toFieldRow = newField[0];
  this.toField = newField[1];
  this.setTo(to);
  Ele.event(this.toField, 'onkeydown',
      bind(this.contactsUi, this.contactsUi.handleKeyDown));
  // IE
  Ele.event(this.toField, 'onpropertychange',
      bind(this.contactsUi, this.contactsUi.populate));
  // Gecko
  Ele.event(this.toField, 'oninput',
      bind(this.contactsUi, this.contactsUi.populate));

  newField = this.addField(g_strings('CC'));
  this.ccFieldRow = newField[0];
  this.ccField = newField[1];
  Ele.event(this.ccField, 'onkeydown',
      bind(this.contactsUi, this.contactsUi.handleKeyDown));
  // IE
  Ele.event(this.ccField, 'onpropertychange',
      bind(this.contactsUi, this.contactsUi.populate));
  // Gecko
  Ele.event(this.ccField, 'oninput',
      bind(this.contactsUi, this.contactsUi.populate));
  Ele.hide(this.ccFieldRow);

  newField = this.addField(g_strings('BCC'));
  this.bccFieldRow = newField[0];
  this.bccField = newField[1];
  Ele.event(this.bccField, 'onkeydown',
      bind(this.contactsUi, this.contactsUi.handleKeyDown));
  // IE
  Ele.event(this.bccField, 'onpropertychange',
      bind(this.contactsUi, this.contactsUi.populate));
  // Gecko
  Ele.event(this.bccField, 'oninput',
      bind(this.contactsUi, this.contactsUi.populate));
  Ele.hide(this.bccFieldRow);

  this.addFieldControls();

  newField = this.addField(g_strings('SUBJECT'));
  this.subjectFieldRow = newField[0];
  this.subjectField = newField[1];
  this.subjectField.value = subject;

  // Wrapper div needed for smooth hover transitions.
  // Without it, the background image occassionally disappears.
  var buttonWrapper = Ele.create(this.footerDiv, 'div', 'composeButtonWrapper');
  this.sendButton = Ele.create(buttonWrapper, 'a', 'composeButton');
  this.sendButton.href = '#';
  Ele.text(this.sendButton, g_strings('SEND'));
  Ele.event(this.sendButton, 'onclick', bind(this, this.attemptSend));
  Ele.event(this.sendButton, 'onfocus',
      bind(this, this.onButtonFocus, this.sendButton));
  Ele.event(this.sendButton, 'onblur',
      bind(this, this.onButtonBlur, this.sendButton));
  Ele.event(this.sendButton, 'onkeyup',
      bind(this, this.onButtonKeyUp, this.sendButton));

  buttonWrapper = Ele.create(this.footerDiv, 'div', 'composeButtonWrapper');
  this.discardButton = Ele.create(buttonWrapper, 'a', 'composeButton');
  this.discardButton.href = '#';
  Ele.text(this.discardButton, g_strings('DISCARD'));
  Ele.event(this.discardButton, 'onclick',
      bind(this, this.discard));
  Ele.event(this.discardButton, 'onfocus',
      bind(this, this.onButtonFocus, this.discardButton));
  Ele.event(this.discardButton, 'onblur',
      bind(this, this.onButtonBlur, this.discardButton));
  Ele.event(this.discardButton, 'onkeyup',
      bind(this, this.onButtonKeyUp, this.discardButton));

  this.setFocus();
}

ComposeUi.prototype.discard = function() {
  if (this.isButtonsEnabled && this.onDiscard) {
    this.onDiscard(this);
  }
};

ComposeUi.prototype.onDiscard = null;
ComposeUi.prototype.onSend = null;
ComposeUi.prototype.onSendFinish = null;

ComposeUi.prototype.attemptSend = function() {
  if (!this.isButtonsEnabled) {
    return;
  }

  // Send mail iff any of to, cc, bcc field has valid email address.
  var to = this.extractEmailsOnly(this.toField.value);
  var cc = this.extractEmailsOnly(this.ccField.value);
  var bcc = this.extractEmailsOnly(this.bccField.value);
  if (to.length > 0 || cc.length > 0 || bcc.length > 0) {
    if (this.body.value === '' &&
        !window.confirm(g_strings('MESSAGE_NO_BODY'))) {
      return;
    }
    if (this.subjectField.value === '' &&
        !window.confirm(g_strings('MESSAGE_NO_SUBJECT'))) {
      return;
    }
    // See Thread.replyOrForward for description of parameters.
    if (this.onSend) {
      this.onSend(bind(this, this.onSendReady),
          bind(this, this.onSendFail),
          this.isForward,
          to, this.subjectField.value, this.body.value, cc, bcc);
      this.disableButtons();
    }
  } else {
    g_alertUi.show(g_strings('INVALID_EMAIL_ADDRESS'));
  }
};

ComposeUi.prototype.enableButtons = function() {
  this.isButtonsEnabled = true;
};

ComposeUi.prototype.disableButtons = function() {
  this.isButtonsEnabled = false;
};

ComposeUi.prototype.onSendReady = function() {
  this.enableButtons();
  g_alertUi.show(g_strings('MESSAGE_SENT'));
  if (this.onSendFinish) {
    this.onSendFinish();
  }
};

ComposeUi.prototype.onSendFail = function() {
  this.enableButtons();
  g_alertUi.show(g_strings('SERVER_OR_NETWORK_ERROR'));
};

ComposeUi.prototype.onButtonFocus = function(button) {
  button.className = 'composeButtonFocus';
};

ComposeUi.prototype.onButtonBlur = function(button) {
  button.className = 'composeButton';
};

ComposeUi.prototype.onButtonKeyUp = function(button, event) {
  var keyCode = event.keyCode;

  if (keyCode == KEY_SPACE) {
    try {
      button.click();
    } catch(e) {
      // TODO: prevent exception that occurs when window is closing.
    }
  }
};

ComposeUi.prototype.onKeyUp = function(event) {
  cancelEvent(event);
};

ComposeUi.prototype.isEmpty = function() {
  return this.toField.value === '' &&
      this.ccField.value === '' &&
      this.bccField.value === '' &&
      this.subjectField.value === '' &&
      this.body.value === '';
};

ComposeUi.prototype.setFocus = function() {
  if (this.toField.value === '') {
    this.toField.focus();
  } else if (this.subjectField.value === '') {
    this.subjectField.focus();
  } else {
    this.body.focus();
  }
};

ComposeUi.prototype.setTo = function(to) {
  this.toField.value = to;
};

ComposeUi.prototype.update = function(to, subject, isForward) {
  this.isForward = isForward;
  this.setTo(to);
  this.subjectField.value = subject;
  this.setFocus();
};

ComposeUi.prototype.destroy = function() {
  this.parentDiv.removeChild(this.mainDiv);
};

/**
 * Adds a label/field pair to the fields table.
 * @returns an array containing the
 * 1. created table row element
 * 2. the input element
 */
ComposeUi.prototype.addField = function(label) {
  var tr = Ele.create(this.fieldsBody, 'tr');
  var labelCell = Ele.create(tr, 'th');
  Ele.text(labelCell, label);
  var inputCell = Ele.create(tr, 'td');
  var input = Ele.create(inputCell, 'input', 'composeField');

  return [tr, input];
};

ComposeUi.prototype.addFieldControls = function() {
  var tr = Ele.create(this.fieldsBody, 'tr');
  var labelCell = Ele.create(tr, 'th');
  var inputCell = Ele.create(tr, 'td');
  this.addCcLink = Ele.create(inputCell, 'label', 'composeFieldControl');
  Ele.event(this.addCcLink, 'onclick', bind(this, this.showCc));
  Ele.text(this.addCcLink, g_strings('ADD_CC'));
  this.addBccLink = Ele.create(inputCell, 'label', 'composeFieldControl');
  Ele.event(this.addBccLink, 'onclick', bind(this, this.showBcc));
  Ele.text(this.addBccLink, g_strings('ADD_BCC'));

  return tr;
};

ComposeUi.prototype.showCc = function() {
  Ele.show(this.ccFieldRow);
  Ele.hide(this.addCcLink);
  this.resize();
};

ComposeUi.prototype.showBcc = function() {
  Ele.show(this.bccFieldRow);
  Ele.hide(this.addBccLink);
  this.resize();
};

/**
 * Check whether an email has valid formatting. We use a simple regular
 * expression that makes sure the local part only contains common ASCII
 * characters, and that the top-level domain consists of 2-6 characters.
 *
 * This method accepts leading and trailing whitespace characters.
 *
 * @return True if valid email.
 */
ComposeUi.prototype.isValidEmailAddress = function(str) {
  var pattern = /^[a-z0-9_\.\-\+=%]+\@(([a-z0-9\-])+\.)+([a-z0-9]{2,6})$/i;
  return pattern.test(trimString(str));
};

ComposeUi.prototype.extractEmailsOnly = function(str) {
  var emails = str.replace(/;/g, ',').split(/,/);
  var emailList = [];
  for (var i = 0; i < emails.length; ++i) {
    var currentEmail = trimString(emails[i]);
    var ltIndex = currentEmail.indexOf('<');
    var gtIndex = currentEmail.indexOf('>');
    if (ltIndex > -1 && gtIndex > -1) {
      if (this.isValidEmailAddress(currentEmail.split('<')[1].split('>')[0])) {
        emailList.push(currentEmail.substr(0, gtIndex + 1));
      }
    } else if (this.isValidEmailAddress(currentEmail)) {
      emailList.push(currentEmail);
    }
  }
  return emailList.join(', ');
};


ContactsUi.MAX_ITEMS = 5;
ContactsUi.ITEM_HEIGHT = 15;

function ContactsUi(parentDiv) {
  this.length = 0;
  this.mainDiv = Ele.create(parentDiv, 'div', 'contacts');
  this.items = [];
  this.selectedIndex = 0;
  this.currentField = null;

  // Create items and reuse them rather than recreating them.
  for (var i = 0; i < ContactsUi.MAX_ITEMS; ++i) {
    var item = Ele.create(this.mainDiv, 'div');
    Ele.event(item, 'onmouseover', bind(this, this.onItemMouseOver, i));
    Ele.event(item, 'onclick', bind(this, this.onItemClick, i));
    this.items.push(item);
  }

  this.onPicked = null;
}

ContactsUi.prototype.handleKeyDown = function(event) {
  if (!this.isVisible()) {
    return;
  }

  var keyCode = event.keyCode;
  var cancel = true;

  if (keyCode == KEY_ESCAPE) {
    this.hide();
  } else if (keyCode == KEY_UP) {
    this.selectPrevious();
  } else if (keyCode == KEY_DOWN) {
    this.selectNext();
  } else if (keyCode == KEY_ENTER || keyCode == KEY_TAB) {
    this.pick();
  } else {
    cancel = false;
  }

  if (cancel) {
    cancelEvent(event);
    if (event.returnValue != undefined) {
      event.returnValue = false;
    }
  }
};

ContactsUi.prototype.populate = function(event) {
  // IE: Only handle changes to value.
  if (event.propertyName && event.propertyName != 'value') {
    return;
  }

  // IE: event.srcElement; Gecko/Standard: event.target.
  var field = event.srcElement || event.target;
  var fieldValue = field.value;

  // Extract last entry in field.
  var lastCommaIndex = fieldValue.lastIndexOf(',');
  var currentId = fieldValue.substr(lastCommaIndex + 1);
  currentId = trimString(currentId).toUpperCase();
  if (currentId === '') {
    this.hide();
    return;
  }

  var contacts = g_getContacts(currentId, ContactsUi.MAX_ITEMS);
  // Number of valid contacts.
  var numContactsInList = 0;
  // If new list differs from previous.
  var isDifferent = false;

  for (var i = 0; i < contacts.length; ++i) {
    var email = contacts[i].email;
    var displayName = contacts[i].displayName;
    var previousValue = this.getContact(numContactsInList);
    var newValue = displayName != MILU_BLANK_LINE_MARKER ?  displayName : '';
    newValue += '<' + email + '>';
    this.setContact(numContactsInList, newValue);
    isDifferent = isDifferent || previousValue != newValue;
    ++numContactsInList;
  }

  if (isDifferent || this.length != numContactsInList) {
    this.select(0);
  }

  this.setLength(numContactsInList);

  var top = field.offsetParent.offsetTop;
  var height = field.offsetHeight + 1;
  Ele.top(this.mainDiv, top + height);

  this.currentField = field;

  if (numContactsInList) {
    this.show();
  } else {
    this.hide();
  }
};

ContactsUi.prototype.setLength = function(length) {
  this.length = length;

  Ele.height(this.mainDiv, this.length * ContactsUi.ITEM_HEIGHT + 2);
};

ContactsUi.prototype.isVisible = function() {
  return Ele.isVisible(this.mainDiv);
};

ContactsUi.prototype.hide = function() {
  Ele.hide(this.mainDiv);
};

ContactsUi.prototype.show = function() {
  Ele.show(this.mainDiv);
};

ContactsUi.prototype.draw = function() {
  for (var i = 0; i < this.items.length; ++i) {
    if (i == this.selectedIndex) {
      this.items[i].className = 'selected';
    } else {
      this.items[i].className = '';
    }
  }
};

ContactsUi.prototype.getSelectedContact = function() {
  return this.getContact(this.selectedIndex);
};

ContactsUi.prototype.getContact = function(index) {
  if (this.items[index].firstChild) {
    return this.items[index].firstChild.nodeValue;
  }

  return '';
};

ContactsUi.prototype.setContact = function(index, contact) {
  if (index < 0 || index >= this.items.length) {
    return;
  }

  Ele.text(this.items[index], contact);
};

ContactsUi.prototype.select = function(i) {
  this.selectedIndex = i;
  this.draw();
};

ContactsUi.prototype.selectNext = function() {
  var i = this.selectedIndex + 1;
  if (i >= this.length) {
    i = 0;
  }

  this.select(i);
};

ContactsUi.prototype.selectPrevious = function() {
  var i = this.selectedIndex - 1;
  if (i < 0) {
    i = this.length - 1;
  }

  this.select(i);
};

ContactsUi.prototype.pick = function() {
  var fieldValue = this.currentField.value;
  var lastCommaIndex = fieldValue.lastIndexOf(',');
  this.currentField.value = fieldValue.substr(0, lastCommaIndex + 1) +
      this.getSelectedContact() + ', ';

  if (this.onPicked) {
    this.onPicked();
  }

  this.hide();
};

ContactsUi.prototype.onItemClick = function(i) {
  this.select(i);
  this.pick();
};

ContactsUi.prototype.onItemMouseOver = function(i) {
  this.select(i);
};

// TODO: should be called in response to an event rather than being
// called manually.
ComposeUi.prototype.resize = function() {
  var height = this.parentDiv.offsetHeight -
      this.headerDiv.offsetHeight -
      this.footerDiv.offsetHeight - 2;

  if (height > 0) {
    Ele.height(this.body, height);
  }
};
