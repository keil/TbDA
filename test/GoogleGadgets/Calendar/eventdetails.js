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
 * @fileoverview Detailsview for events.
 */

var g_eventDetails = null;
var g_errorMessage = null;

/**
 * onOpen event handler for quick add input details view.
 */
function view_onopen() {
  g_errorMessage = new ErrorMessage();
  g_eventDetails = new EventDetails();
  g_eventDetails.applyColors();
  g_eventDetails.fillDetailsView();
}

/**
 * Class for showing the details of an event.
 * @constructor
 */
function EventDetails() {
  this.event = detailsViewData.getValue(OPTIONS.EVENT);
  this.calendar = detailsViewData.getValue(OPTIONS.CALENDAR);
  this.authToken = detailsViewData.getValue(OPTIONS.AUTH);
  this.userZip = detailsViewData.getValue(OPTIONS.USERZIP);

  imgClose.onclick = Utils.bind(this.close, this);

  // Apply custom scrollbars
  // Scrollbar code is not available in versions below 5.8
  if (!eventDescription.scrollbar) return;
  var scrollbar;
  scrollbar = eventDescription.scrollbar;
  scrollbar.background = "images/scroll-bar.gif";
  scrollbar.thumbImage = "images/scroll-track.gif";
  scrollbar.thumbDownImage = "images/scroll-track-clicked.gif";
  scrollbar.thumbOverImage = "images/scroll-track.gif";
  scrollbar.rightImage = "images/scroll-down.gif";
  scrollbar.rightDownImage = "images/scroll-down-clicked.gif";
  scrollbar.rightOverImage = "images/scroll-down-over.gif";
  scrollbar.leftImage = "images/scroll-up.gif";
  scrollbar.leftDownImage = "images/scroll-up-clicked.gif";
  scrollbar.leftOverImage = "images/scroll-up-over.gif";

  scrollbar = guestList.scrollbar;
  scrollbar.background = "images/scroll-bar.gif";
  scrollbar.thumbImage = "images/scroll-track.gif";
  scrollbar.thumbDownImage = "images/scroll-track-clicked.gif";
  scrollbar.thumbOverImage = "images/scroll-track.gif";
  scrollbar.rightImage = "images/scroll-down.gif";
  scrollbar.rightDownImage = "images/scroll-down-clicked.gif";
  scrollbar.rightOverImage = "images/scroll-down-over.gif";
  scrollbar.leftImage = "images/scroll-up.gif";
  scrollbar.leftDownImage = "images/scroll-up-clicked.gif";
  scrollbar.leftOverImage = "images/scroll-up-over.gif";
}

/**
 * Colors used in the details view 
 */
EventDetails.prototype.COLORS = {
  DISABLED: '#909090'
};

/**
 * Width of the static map image.
 */
EventDetails.prototype.MAP_WIDTH = 125;

/**
 * Height of the static map image.
 */
EventDetails.prototype.MAP_HEIGHT = 75;

/**
 * Zoom level vor the static map image.
 */
EventDetails.prototype.MAP_ZOOM = 10;

/**
 * Color map for the colorMultiply values. First color is the color
 * returned by calendar, the second one is the color which needs
 * to be applied to the background image to create a visually matching
 * color. The values need to be changed if the background image is
 * changed!
 */
EventDetails.prototype.MULTIPLY_MAP = {
  '#A32929': '#900000',
  '#B1365F': '#911640',
  '#7A367A': '#6A366A',
  '#5229A3': '#522993',
  '#29527A': '#294260',
  '#2952A3': '#293293',
  '#1B887A': '#1B685A',
  '#28754E': '#285530',
  '#0D7813': '#0D5813',
  '#528800': '#326000',
  '#88880E': '#68680E',
  '#AB8B00': '#8B6900',
  '#BE6D00': '#9A5D00',
  '#B1440E': '#81340E',
  '#865A5A': '#704A4A',
  '#705770': '#5A425A',
  '#4E5D6C': '#42505C',
  '#5A6986': '#404D6A',
  '#4A716C': '#4A5F58',
  '#6E6E41': '#626240',
  '#8D6F47': '#7C603B'
};

/**
 * Apply the calendar and colorMultiply colors to the dialog where applicable.
 */
EventDetails.prototype.applyColors = function() {
  mapsDiv.background = this.calendar.color;
  eventRSVPDiv.background = this.calendar.color;
  eventRSVP.background = this.calendar.color;
  eventRSVP.itemOverColor = this.calendar.color;
  imgBackground.colorMultiply = this.getMultiplyColor();
};

/**
 * Get the colorMultiply color from the multiply map.
 * @return {string} Hex color code of colorMultiply color.
 */
EventDetails.prototype.getMultiplyColor = function() {
  return this.MULTIPLY_MAP[this.calendar.color.toUpperCase()];
};

/**
 * Populate the detailsview with the data from the event object.
 */
EventDetails.prototype.fillDetailsView = function() {
  eventTitle.innerText = this.event.title;
  eventTitle.href = '';
  eventTitle.onclick = Utils.bind(this.openEvent, this);
  eventTitle.tooltip = this.event.title;

  if (this.event.creator) {
    eventCreator.innerText = strings.CREATED_BY.replace('[![NAME]!]',
        this.event.creator);
  } else {
    eventCreator.innerText = '';
  }

  if (this.event.desc) {
    // Append line break to make sure the last line will not be cut off
    // when scrolling.
    eventDescription.value = this.event.desc + '\n';
  } else {
    eventDescription.value = strings.NO_DESCRIPTION;
    eventDescription.color = this.COLORS.DISABLED;
  }

  eventWhen.innerText = Utils.formatEventTimes(this.event, true);

  locationDirections.visible = false;
  if (this.event.location) {
    try {
      // debug.trace('Location for this event is: ' + this.event.location);
      eventLocation.innerText = this.event.location;
      eventLocation.tooltip = this.event.location;
      // Hide map for now. Will be displayed when we request the image.
      mapsDiv.visible = false;
      var geo = new GeoCode();
      geo.onGeoDataAvailable = Utils.bind(this.onGeoDataAvailable, this, geo);
      geo.getGeoData(this.event.location);

      // Show directions link if we have a zip code for this user.
      if (this.userZip != '') {
        locationDirections.visible = true;
        var query = this.userZip + ' to ' + this.event.location;
        query = encodeURIComponent(query);
        locationDirections.href = 'http://maps.google.com/maps?q=' + query;
      }
    } catch (e) {
      debug.error('Fatal error while requesting maps information');
    }

  } else {
    eventWhen.width = 350;
    eventWhen.innerText = Utils.formatEventTimes(this.event);
    eventLocation.visible = false;
    mapsDiv.visible = false;
  }

  if (this.event.rsvp) {
    var yes = eventRSVP.appendElement('<item />');
    yes.cursor = 'hand';
    yesImg = yes.appendElement('<img />');
    yesImg.x = yesImg.y = 1;
    yesImg.src = 'images/rsvp_yes.png';
    yesLbl = yes.appendElement('<label />');
    yesLbl.x = yesImg.srcWidth + 2;
    yesLbl.width = yesLbl.height = '100%';
    yesLbl.innerText = strings.GOING_YES;

    var no = eventRSVP.appendElement('<item />');
    no.cursor = 'hand';
    noImg = no.appendElement('<img />');
    noImg.x = noImg.y = 1;
    noImg.src = 'images/rsvp_no.png';
    noLbl = no.appendElement('<label />');
    noLbl.x = noImg.srcWidth + 2;
    noLbl.width = noLbl.height = '100%';
    noLbl.innerText = strings.GOING_NO;

    var maybe = eventRSVP.appendElement('<item />');
    maybe.cursor = 'hand';
    maybeImg = maybe.appendElement('<img />');
    maybeImg.x = maybeImg.y = 1;
    maybeImg.src = 'images/rsvp_maybe.png';
    maybeLbl = maybe.appendElement('<label />');
    maybeLbl.x = maybeImg.srcWidth + 2;
    maybeLbl.width = maybeLbl.height = '100%';
    maybeLbl.innerText = strings.GOING_MAYBE;

    yesLbl.color = noLbl.color = maybeLbl.color = '#FFFFFF';

    var guestCount = 0;
    for (var att in this.event.attendees) {
      var attList = this.event.attendees[att];
      attList.sort();
      guestCount += attList.length;
      for (var i = 0; i < attList.length; ++i) {
        if (attList[i].email == this.calendar.email) {
          switch (att) {
            case this.event.RSVP_YES:
                eventRSVP.selectedItem = yes;
                yesLbl.bold = true;
                break;
            case this.event.RSVP_NO:
                eventRSVP.selectedItem = no;
                noLbl.bold = true;
                break;
            default:
                eventRSVP.selectedItem = maybe;
                maybeLbl.bold = true;
                break;
          }
          break;
        }
      }
    }

    // If there are no predefined answers then the user cannot choose a response
    // and we hide the eventRSVP dropdown.
    if (eventRSVP.selectedItem == null) {
      eventRSVPDiv.visible = false;
      labelGoing.visible = false;
      eventRSVP.visible = false;
      labelDescription.y = 115;
      eventDescription.y = 140;
      eventDescription.height = 120;
    }

    // Fill the guest list in order yes - maybe - no - waiting and display total
    // guest number behind header.
    labelGuests.innerText = strings.GUESTS.replace('[![COUNT]!]', guestCount);
    var drawOrder = ['yes', 'maybe', 'no', 'waiting'];
    for (var i = 0; i < drawOrder.length; ++i) {
      var drawOpt = drawOrder[i];
      var attendees = this.event.attendees[drawOpt];
      if (attendees.length > 0) {
        var item = guestList.appendElement('<item />');
        var label = item.appendElement('<label />');
        label.x = label.y = 0;
        label.height = label.width = '100%';
        label.bold = true;
        label.font = 'Arial';
        label.size = 11;
        var type = 'GOING_' + drawOpt.toUpperCase();
        var caption = strings.GUEST_HEADER;
        caption = caption.replace('[![ANSWER]!]', strings[type]);
        caption = caption.replace('[![COUNT]!]', attendees.length);
        label.innerText = caption;

        for (var j = 0; j < attendees.length; ++j) {
          var item = guestList.appendElement('<item />');
          var label = item.appendElement('<label />');
          label.x = label.y = 0;
          label.height = label.width = '100%';
          label.innerText = attendees[j].name;
        }
      }
    }

    eventRSVP.onchange = Utils.bind(this.onGoingResponse, this);
  } else {
    eventRSVPDiv.visible = false;
    labelGoing.visible = false;
    eventRSVP.visible = false;
    labelGuests.visible = false;
    guestList.visible = false;
    eventGuestsBg.visible = false;
    labelDescription.y = 115;
    eventDescription.y = 140;
    eventDescription.height = 280;
  }

  if (!this.event.location) {
    eventRSVPDiv.y -= 25;
    labelGoing.y -= 25;
    eventRSVP.y -= 25;
    eventCreator.y -= 25;
    labelDescription.y -= 25;
    eventDescription.y -= 25;
  }

  eventDescriptionBg.x = eventDescription.x - 1;
  eventDescriptionBg.y = eventDescription.y - 1;
  eventDescriptionBg.width = eventDescription.width + 2;
  eventDescriptionBg.height = eventDescription.height + 2;

  if (guestList.visible) {
    eventGuestsBg.x = guestList.x - 1;
    eventGuestsBg.y = guestList.y - 1;
    eventGuestsBg.width = guestList.width + 2;
    eventGuestsBg.height = guestList.height + 2;
  }

  linkEdit.href = '';
  linkEdit.onclick = Utils.bind(this.openEvent, this);
};

/**
 * Called when the selected item in the Going dropdown is changed.
 * This function retrieves the latest version of the event and then changes
 * it and sends it back. This is done synchronous since we do not want the
 * user to close the detailsview and thereby destroying the callback function
 */
EventDetails.prototype.onGoingResponse = function() {
  // Retrieve latest version of event
  var req = Utils.createXhr();
  req.open('GET', this.event.selfUrl, false);
  req.setRequestHeader('Authorization',
      'GoogleLogin auth=' + this.authToken);
  req.send();

  // Only continue if we received a 200 response which means we got an event.
  if (req.status != 200) return;

  // Load the response into a DOMDocument
  var doc = Utils.createXmlDocument(req);

  // Extract entry elements. We should have one element in there.
  var elem = doc.getElementsByTagName('entry');
  if (elem.length == 0) return;
  elem = elem[0];

  // Modify XML of element to match our selected response
  for (var node = elem.firstChild; node != null; node = node.nextSibling) {
    if (node.nodeName == 'gd:who') {
      if (node.firstChild) {
        var email = node.getAttribute('email');
        if (email == this.calendar.email) {
          switch (eventRSVP.selectedIndex) {
            case 0: // Yes selected
                node.firstChild.setAttribute('value',
                    'http://schemas.google.com/g/2005#event.accepted');
                break;
            case 1: // No selected
                node.firstChild.setAttribute('value',
                    'http://schemas.google.com/g/2005#event.declined');
                break;
            case 2: // Maybe selected
                node.firstChild.setAttribute('value',
                    'http://schemas.google.com/g/2005#event.tentative');
                break;
          }
        }
      }
    }
  }

  // Parse XML into CalendarEvent since we need to access the new editUrl.
  var calEvent = new CalendarEvent();
  calEvent.parse(elem);

  // Send the modified XML to the editUrl of the event.
  var req = Utils.createXhr();
  req.open('PUT', calEvent.editUrl, false);
  req.setRequestHeader('Authorization',
      'GoogleLogin auth=' + this.authToken);
  req.setRequestHeader('Content-Type', 'application/atom+xml');
  req.send(elem.xml);

  // Return from function if we cannot successfully set the new event.
  if (req.status != 200) {
    g_errorMessage.displayMessage(strings.ERROR_RSVP_RESPONSE);
    return;
  }

  // Add the event to the options so the update is triggered
  options.putValue(OPTIONS.UPDATE_RSVP_TRIGGER,
      options.getValue(OPTIONS.UPDATE_RSVP_TRIGGER) !== true);
};

/**
 * Callback function for the geocode object. 
 * @param {GeoCode} geo Geocode class instance
 */
EventDetails.prototype.onGeoDataAvailable = function(geo) {
  if (geo.geoData.Status.code != 200) {
    eventLocation.width = eventDescription.width;
    eventCreator.width = eventDescription.width;
    eventWhen.width = eventDescription.width;
    eventWhen.innerText = Utils.formatEventTimes(this.event);
    locationDirections.visible = false;
    debug.info('Address could not be parsed into a geo location!');
    return;
  }

  var staticUrl = geo.getStaticImgUrl(this.MAP_WIDTH, this.MAP_HEIGHT,
      this.MAP_ZOOM);
  Utils.getRemoteImg(staticUrl, eventMapImg);
  mapsDiv.visible = true;
  eventMapImg.onclick = Utils.bind(this.openMapUrl, this);
};

/**
 * Opens the URL to the event location in Google Maps
 */
EventDetails.prototype.openMapUrl = function() {
  var launchUrl = 'http://maps.google.com/maps?q=' +
      encodeURIComponent(this.event.location);
  framework.openUrl(launchUrl);
};

/**
 * Open the event in the external browser. This will first obtain a special
 * auth token which allows us to login or switch the account if the user
 * is already logged in into another account.
 */
EventDetails.prototype.openEvent = function() {
  if (this.event.alternateUrl) {
    Utils.redirectWithSuperAuth(this.event.alternateUrl);
  }
};

/**
 * Close handler when the X in the top-right corner is clicked.
 */
EventDetails.prototype.close = function() {
  options.putValue(OPTIONS.CLOSE_DETAILS, true);
};

