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
 * @fileoverview An object which represents a single event.
 */

/**
 * Simple object for attendees.
 * @param {string} name Name of the attendee.
 * @param {string} email Mail address of the attendee.
 * @constructor
 */
function Attendee(name, email) {
  this.name = name;
  this.email = email;
}

/**
 * Generic toString method for attendee class
 * @return {string} Attendee in string
 */
Attendee.prototype.toString = function() {
  return this.name + '<' + this.email + '>';
};

/**
 * Object for event data.
 * @constructor
 */
function CalendarEvent() {
  this.id = '';
  this.title = '';
  this.location = null;
  this.reminder = -1;
  this.startTime = null;
  this.endTime = null;
  this.isAllDay = false;
  this.attendees = {'yes' : [], 'no' : [], 'maybe' : [], 'waiting' : []};
  this.rsvp = false;
  this.recur = false;
  this.updated = null;
  this.published = null;
  this.creator = null;
  this.organizer = null;
  this.status = '';
  this.desc = '';
  this.alternateUrl = null;
  this.selfUrl = null;
  this.editUrl = null;
  this.originalId = '';
  this.calendarId = '';
}

// RSVP Constants
CalendarEvent.prototype.RSVP_YES = 'yes';
CalendarEvent.prototype.RSVP_NO = 'no';
CalendarEvent.prototype.RSVP_MAYBE = 'maybe';

// Constants
CalendarEvent.prototype.STATUS_CONFIRMED = 0;
CalendarEvent.prototype.STATUS_CANCELED = 1;
CalendarEvent.prototype.STATUS_TENTATIVE = 2;

/**
 * Create clone of this event.
 * @return {CalendarEvent} Cloned event.
 */
CalendarEvent.prototype.clone = function() {
  var event = new CalendarEvent();
  for (var i in this) {
    switch (typeof(this[i])) {
      case 'object':
          if (this[i] instanceof Date) {
            event[i] = new Date(this[i]);
          } else {
            event[i] = this[i];
          }
          break;
      case 'function':
          // Functions are already in the prototype.
          break;
      default:
          event[i] = this[i];
          break;
    }
  }
  return event;
};

/**
 * Parse a XML node into a valid calendar event object
 * @param {XMLNode} elem XML Node from event feed
 */
CalendarEvent.prototype.parse = function(elem) {
  for (var node = elem.firstChild; node != null; node = node.nextSibling) {
    var nodeName = node.nodeName;
    switch (nodeName) {
      case 'gCal:uid':
          this.id = node.getAttribute('value');
          break;
      case 'title':
          if (node.firstChild) {
            this.title = node.firstChild.nodeValue;
          } else {
            this.title = MSG_NO_TITLE;
          }
          break;
      case 'gd:when':
          var startString = node.getAttribute('startTime');
          this.startTime = Utils.rfc3339StringToDate(startString);
          this.endTime =
              Utils.rfc3339StringToDate(node.getAttribute('endTime'));
          this.isAllDay = startString.match(Utils.DATE_REGEX);
          for (var i = 0; i < node.childNodes.length; ++i) {
            if (node.childNodes[i].nodeName == 'gd:reminder') {
              // We are only interested in alert reminders and will only check
              // for short term (minutes) reminders.
              if (node.childNodes[i].getAttribute('method') == 'alert') {
                var rem = parseInt(node.childNodes[i].getAttribute('minutes'));
                if (isNaN(rem)) {
                  this.reminder = -1;
                } else {
                  this.reminder = rem;
                }
              }
           }
          }
          break;
      case 'gd:where':
          this.location = node.getAttribute('valueString');
          break;
      case 'gd:who':
          if (node.firstChild) {
            var attendee = new Attendee(node.getAttribute('valueString'),
                node.getAttribute('email'));
            switch (node.firstChild.getAttribute('value')) {
              case 'http://schemas.google.com/g/2005#event.accepted':
                  this.attendees.yes.push(attendee);
                  break;
              case 'http://schemas.google.com/g/2005#event.declined':
                  this.attendees.no.push(attendee);
                  break;
              case 'http://schemas.google.com/g/2005#event.invited':
                  this.attendees.waiting.push(attendee);
                  break;
              case 'http://schemas.google.com/g/2005#event.tentative':
                  this.attendees.maybe.push(attendee);
                  break;
            }
          }
          if (this.attendees.yes.length +
              this.attendees.no.length +
              this.attendees.maybe.length +
              this.attendees.waiting.length > 0) {
            this.rsvp = true;
          }
          if (node.getAttribute('rel') ==
              'http://schemas.google.com/g/2005#event.organizer') {
            this.organizer = node.getAttribute('valueString');
          }
          break;
      case 'gd:recurrence':
          this.recur = true;
          break;
      case 'gd:eventStatus':
          switch (node.getAttribute('value')) {
            case 'http://schemas.google.com/g/2005#event.canceled':
                this.status = this.STATUS_CANCELED;
                break;
            case 'http://schemas.google.com/g/2005#event.confirmed':
                this.status = this.STATUS_CONFIRMED;
                break;
            case 'http://schemas.google.com/g/2005#event.tentative':
                this.status = this.STATUS_TENTATIVE;
                break;
          }
          break;
      case 'content':
          if (node.firstChild) {
            this.desc = node.firstChild.nodeValue;
          }
          break;
      case 'gd:originalEvent':
          this.originalId = node.getAttribute('id');
          break;
      case 'author':
          if (node.firstChild && node.firstChild.nodeName == 'name') {
            this.creator = node.firstChild.firstChild.nodeValue;
          }
          break;
      case 'link':
          var url = node.getAttribute('href');
          url = Utils.forceHttpsUrl(url);
          switch (node.getAttribute('rel')) {
            case 'alternate':
                this.alternateUrl = url;
                break;
            case 'self':
                this.selfUrl = url;
                break;
            case 'edit':
                this.editUrl = url;
                break;
          }
          break;
      case 'updated':
          this.updated = Utils.rfc3339StringToDate(node.firstChild.nodeValue);
          break;
      case 'published':
          this.published =
              Utils.rfc3339StringToDate(node.firstChild.nodeValue);
          break;
    }
  }
};
