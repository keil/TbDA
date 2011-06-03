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

var g_options = null;

function options_onOpen() {
  g_options = new OptionsDlg();
  g_options.init();
}

function OptionsDlg() {
}

OptionsDlg.prototype.init = function() {
  view.onok = Utils.bind(this.save, this);

  optionWeekStart.selectedIndex = options.getValue(OPTIONS.WEEKSTART) + 1;
  switch (options.getValue(OPTIONS.VIEW)) {
    case OPTIONS.CALENDARVIEW:
        optionView.selectedIndex = 0;
        break;
    case OPTIONS.DAYVIEW:
        optionView.selectedIndex = 1;
        break;
    case OPTIONS.AGENDAVIEW:
        optionView.selectedIndex = 2;
        break;
  }
  option24Hour.value = options.getValue(OPTIONS.HOUR24);

  if (options.getValue(OPTIONS.CALENDARDATA) == '') {
    calendarError.visible = true;
    return;
  }
  var data = jsonParse(options.getValue(OPTIONS.CALENDARDATA));

  if (!data.feed || data.feed.length == 0) {
    // no <feed> element
    return;
  }

  debug.trace('Display calendars');
  calendarList.removeAllElements();

  var ownCalendars = [];
  var otherCalendars = [];

  for (var i = 0; i < data.feed.entry.length; ++i) {
    var cal = new Calendar();
    cal.parse(data.feed.entry[i]);
    if (!cal.isVisible()) continue;
    if (cal.accessLevel == 'owner') {
      ownCalendars.push(cal);
    } else {
      otherCalendars.push(cal);
    }
  }

  ownCalendars.sort(this.sortCalendars);
  otherCalendars.sort(this.sortCalendars);

  this.addOptionText(strings.MY_CALENDARS);
  for (var i = 0; i < ownCalendars.length; ++i) {
    var cal = ownCalendars[i];
    this.optionsAddCalendar(cal);
  }

  this.addOptionText(strings.OTHER_CALENDARS);
  for (var i = 0; i < otherCalendars.length; ++i) {
    var cal = otherCalendars[i];
    this.optionsAddCalendar(cal);
  }
};

OptionsDlg.prototype.optionsAddCalendar = function(cal) {
  var item = calendarList.appendElement('<item />');
  var bgDiv = item.appendElement('<div />');
  bgDiv.x = bgDiv.y = 0;
  bgDiv.width = bgDiv.height = '100%';
  bgDiv.background = cal.color;
  var checkbox = item.appendElement('<checkbox />');
  checkbox.cursor = 'hand';
  checkbox.x = 2;
  checkbox.y = 0;
  checkbox.height = checkbox.width = '100%';
  checkbox.font = 'arial';
  checkbox.size = 9;
  checkbox.trimming = 'word-ellipsis';
  checkbox.downImage = 'images/checkbox_default.png';
  checkbox.image = 'images/checkbox_default.png';
  checkbox.overImage = 'images/checkbox_default.png';
  checkbox.checkedDownImage = 'images/checkbox_checked.png';
  checkbox.checkedImage = 'images/checkbox_checked.png';
  checkbox.checkedOverImage = 'images/checkbox_checked.png';
  checkbox.value = cal.isSelected();
  checkbox.caption = cal.getTitle();
  checkbox.tooltip = cal.getTitle();
  checkbox.onchange = Utils.bind(this.checkCalendar, this, cal);
  checkbox.color = '#FFFFFF';
};

OptionsDlg.prototype.addOptionText = function(text) {
  var item = calendarList.appendElement('<item />');
  var label = item.appendElement('<label />');
  label.x = label.y = 0;
  label.width = label.height = '100%';
  label.font = 'arial';
  label.innerText = text;
  label.valign = 'middle';
};

OptionsDlg.prototype.checkCalendar = function(cal) {
  debug.trace('Changing visibility of ' + cal.id);
  options.putValue(OPTIONS.SHOW + cal.id, event.srcElement.value);
};

/**
 * Sort function to sort events by date. All day events always come first.
 * @param {Calendar} a Event a
 * @param {Calendar} b Event b
 * @return {integer} sorting order
 */
OptionsDlg.prototype.sortCalendars = function(a, b) {
  return b.title.toLowerCase() > a.title.toLowerCase() ? -1 : 1;
};

OptionsDlg.prototype.save = function() {
  debug.trace('Store options...');

  debug.trace('Selected View: ' + optionView.selectedItem.children(0).innerText);
  switch (optionView.selectedItem.children(0).innerText) {
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

  options.putValue(OPTIONS.WEEKSTART, optionWeekStart.selectedIndex - 1);
  options.putValue(OPTIONS.HOUR24, option24Hour.value);
  options.putValue(OPTIONS.UPDATE_VIEW,
      options.getValue(OPTIONS.UPDATE_VIEW) !== true);
};
