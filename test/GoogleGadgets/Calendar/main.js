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
 * @fileoverview Functions for the main view of the gadget.
 */

function view_onOpen() {
  // Set global variables if not defined by the API. These were introduced in
  // 5.8 and might not be in other platforms yet.
  if (gddDetailsViewFlagNoFrame == undefined) {
    gddDetailsViewFlagNoFrame = 0;
  }
  if (gddDetailsViewFlagDisableAutoClose == undefined) {
    gddDetailsViewFlagDisableAutoClose = 0;
  }

  options.encryptValue(OPTIONS.MAIL);
  options.encryptValue(OPTIONS.PASSWORD);
  options.encryptValue(OPTIONS.AUTH);

  options.putDefaultValue(OPTIONS.MAIL, '');
  options.putDefaultValue(OPTIONS.PASSWORD, '');
  options.putDefaultValue(OPTIONS.REMEMBER, true);
  options.putDefaultValue(OPTIONS.HOUR24, false);
  options.putDefaultValue(OPTIONS.VIEW, OPTIONS.CALENDARVIEW);
  options.putDefaultValue(OPTIONS.WEEKSTART, START_SUNDAY);
  options.putDefaultValue(OPTIONS.AUTH, '');
  options.putDefaultValue(OPTIONS.USE_QUICK_ADD, false);
  options.putDefaultValue(OPTIONS.UPGRADE, strings.GADGET_VERSION);
  options.putValue(OPTIONS.CLOSE_DETAILS, false);

  g_calendarGadget = new CalendarGadget();
  g_calendarGadget.run();
}
