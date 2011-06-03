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
 * @fileoverview All the different resize function of the 
 * CalendarGadget object to improve readability of main
 * file for CalendarGadget object.
 */

CalendarGadget.prototype.resizeDayview = function() {
  // In day view no calendar is visible
  miniCalendarDiv.visible = false;
  dayviewDiv.visible = true;

  // Day view div is in the upper area of the gadget.
  dayviewDiv.x = 0;
  dayviewDiv.y = 0;
  dayviewDiv.width = mainDiv.width;
  dayviewDiv.height = mainDiv.width * 0.8;
  // We need a minimum height for our dayview for it to render correctly.
  if (dayviewDiv.height < g_uiDayView.MIN_HEIGHT) {
    dayviewDiv.height = g_uiDayView.MIN_HEIGHT;
  }
  if (dayviewDiv.height > mainDiv.height) {
    // If our dayview div happens to be larger than the render area we use 
    // all the space we can get to render the dayview in.
    footerDiv.visible = false;
    agendaDiv.visible = false;
    dayviewDiv.height = mainDiv.height;
  } else {
    // We might have enough space to show the agenda. Move agenda to 
    // correct position and then check if we have enough space to display 
    // something useful in it.
    agendaDiv.x = 0;
    agendaDiv.y = dayviewDiv.height;
    agendaDiv.height = mainDiv.height - dayviewDiv.height -
                       footerDiv.height;
    agendaDiv.width = mainDiv.width;
    var minSpace = footerDiv.height + g_uiAgenda.HEADER_HEIGHT +
                    g_uiAgenda.ENTRY_HEIGHT;
    if (dayviewDiv.height + minSpace < mainDiv.height) {
      // Agenda has enough space to show at least one entry so we show and 
      // draw it.
      agendaDiv.visible = true;
      footerDiv.visible = true;
      debug.trace('Resize DayView - draw agenda');
      g_uiAgenda.draw();
    } else {
      // Agenda doesn't have enough space to show anything. The dayview can
      // use all the available space.
      dayviewDiv.height = mainDiv.height;
    }
  }
  g_uiDayView.draw();
};

CalendarGadget.prototype.resizeCalendar = function() {
  // Calendar view. We need the footer, calendar and agenda but no 
  // dayview div.
  footerDiv.visible = true;
  miniCalendarDiv.visible = true;
  agendaDiv.visible = true;
  miniCalendarDiv.y = 0;
  miniCalendarDiv.x = 0;
  miniCalendarDiv.width = mainDiv.width;
  dayviewDiv.visible = false;
  g_uiCal.draw();

  agendaDiv.x = 0;
  agendaDiv.y = miniCalendarDiv.y + miniCalendarDiv.height;
  agendaDiv.height = footerDiv.y - agendaDiv.y;
  agendaDiv.width = mainDiv.width;
  var minSpace = footerDiv.height + g_uiAgenda.HEADER_HEIGHT +
                 g_uiAgenda.ENTRY_HEIGHT;
  if (miniCalendarDiv.height + minSpace < mainDiv.height) {
    // Agenda has enough space to show at least one entry so we show and 
    // draw it.
    agendaDiv.visible = true;
    footerDiv.visible = true;
    g_uiAgenda.draw();
  } else {
    agendaDiv.visible = false;
    footerDiv.visible = false;
  }
};

CalendarGadget.prototype.resizeAgenda = function() {
  miniCalendarDiv.visible = false;
  miniCalendarDiv.height = 0;
  dayviewDiv.visible = false;
  dayviewDiv.height = 0;
  agendaDiv.x = 0;
  agendaDiv.y = 0;
  agendaDiv.height = footerDiv.y - agendaDiv.y;
  agendaDiv.width = mainDiv.width;
  agendaDiv.visible = true;
  g_uiAgenda.draw();
};

CalendarGadget.prototype.resizeDesign = function() {
  // Position and resize drop shadow images.
  base_topleft.x = 0;
  base_topright.y = 0;
  base_topright.x = view.width - base_topright.srcWidth;
  base_topright.y = 0;
  base_top.x = base_topleft.srcWidth;
  base_top.y = 0;
  base_top.width = view.width - base_topleft.srcWidth - base_topright.srcWidth;
  base_bottomleft.x = 0;
  base_bottomleft.y = view.height - base_bottomleft.srcHeight;
  base_bottomright.x = view.width - base_bottomright.srcWidth;
  base_bottomright.y = view.height - base_bottomright.srcHeight;
  base_bottom.x = base_bottomleft.srcWidth;
  base_bottom.y = view.height - base_bottom.srcheight;
  base_bottom.width = base_top.width;
  base_left.x = 0;
  base_left.y = base_topleft.srcHeight;
  base_left.height = view.height - base_topleft.srcHeight -
                     base_bottomleft.srcHeight;
  base_right.x = view.width - base_right.srcWidth;
  base_right.y = base_topright.srcheight;
  base_right.height = base_left.height;

  // Main gadget content area.
  mainDiv.x = 5;
  mainDiv.y = 5;
  mainDiv.width = view.width - 10;
  mainDiv.height = view.height - 10;

  // Move and resize the white background div.
  background.x = base_left.srcWidth;
  background.y = base_top.srcHeight;
  background.width = view.width - base_left.srcWidth - base_right.srcWidth;
  background.height = view.height - base_top.srcHeight - base_bottom.srcHeight;

  // Move and resize the footer. Move the left and right images to the
  // correct positions and resize background image.
  footerDiv.y = mainDiv.height - footerDiv.height;
  footerDiv.x = 0;
  footerDiv.width = mainDiv.width;
  footerDiv.height = 28;
  footerLeft.x = 0;
  footerLeft.y = 0;
  footerRight.x = footerDiv.width - footerRight.srcWidth;
  footerRight.y = 0;
  footerBg.width = footerDiv.width -
                   footerLeft.srcWidth / 2 -
                   footerRight.srcWidth / 2;
  footerBg.height = footerLeft.srcHeight;
  linkOptions.visible = !Utils.isMac() && g_cache.getCalendarCount() > 0;
  linkAddEvent.visible = !Utils.isMac() && g_cache.getCalendarCount() > 0;

  // The add Events feature is not available on the Mac.
  if (Utils.isMac()) {
    linkToday.width = footerDiv.width - 2 * linkToday.x;
  } else {
    sizeCalc.width = 100;
    sizeCalc.font = linkToday.font;
    sizeCalc.size = 8;
    sizeCalc.bold = linkToday.bold;
    sizeCalc.value = linkToday.innerText;
    linkToday.size = sizeCalc.size;
    linkToday.width = sizeCalc.idealBoundingRect.width;
    linkToday.x = 3;

    sizeCalc.value = linkAddEvent.innerText;
    linkAddEvent.size = sizeCalc.size;
    linkAddEvent.x = linkToday.x + linkToday.width;
    linkAddEvent.width = sizeCalc.idealBoundingRect.width;

    sizeCalc.value = linkOptions.innerText;
    linkOptions.size = sizeCalc.size;
    linkOptions.width = sizeCalc.idealBoundingRect.width;
    linkOptions.x = footerDiv.width - linkOptions.width - linkToday.x;

    linkOptions.visible = linkOptions.visible && 
        !(linkOptions.x < linkAddEvent.x + linkAddEvent.width);
  }

  sizeCalc.value = strings.LOADING;
  loadingIndicator.width = sizeCalc.idealBoundingRect.width + 10;
  loadingIndicator.x = mainDiv.width / 2 - loadingIndicator.width / 2;

  resizeIndicator.x = mainDiv.width - resizeIndicator.srcWidth;
  resizeIndicator.y = mainDiv.height - resizeIndicator.srcHeight;
};

CalendarGadget.prototype.resizeBlueDialog = function() {
  dialogDiv.x = 0;
  dialogDiv.y = 0;
  dialogDiv.width = mainDiv.width;
  dialogDiv.height = mainDiv.height;

  blueTopLeft.x = 0;
  blueTopLeft.y = 0;
  blueTopMiddle.x = blueTopLeft.srcWidth;
  blueTopMiddle.height = blueTopLeft.srcHeight;
  blueTopMiddle.width = dialogDiv.width - blueTopLeft.srcWidth -
      blueTopRight.srcWidth;
  blueTopRight.x = dialogDiv.width - blueTopRight.srcWidth;

  blueBottomLeft.x = 0;
  blueBottomLeft.y = dialogDiv.height - blueBottomLeft.srcHeight;
  blueBottomRight.x = dialogDiv.width - blueBottomRight.srcWidth;
  blueBottomRight.y = dialogDiv.height - blueBottomRight.srcHeight;
  blueBottomMiddle.x = blueBottomLeft.srcWidth;
  blueBottomMiddle.y = blueBottomLeft.y;
  blueBottomMiddle.height = blueBottomLeft.srcHeight;
  blueBottomMiddle.width = blueBottomRight.x - blueBottomLeft.srcWidth;
  dialogBg.x = 0;
  dialogBg.y = blueTopMiddle.height;
  dialogBg.width = dialogDiv.width;
  dialogBg.height = blueBottomLeft.y - blueTopMiddle.height;
};

/**
 * Resize and move the mandatory upgrade dialog
 */
CalendarGadget.prototype.resizeUpgradeDialog = function() {
  upgradeDiv.x = 0;
  upgradeDiv.y = 5;
  upgradeDiv.width = mainDiv.width;
  upgradeDiv.height = mainDiv.height - 10;
};

/**
 * Resize and move the login dialog
 */
CalendarGadget.prototype.resizeLoginForm = function() {
  loginDiv.x = 0;
  loginDiv.y = 5;
  loginDiv.width = mainDiv.width;
  loginDiv.height = mainDiv.height - 10;

  userBg.x = 8;
  userBg.width = loginDiv.width - (userBg.x * 2);
  passBg.x = 8;
  passBg.width = loginDiv.width - (passBg.x * 2);
  captchaBg.x = 8;
  captchaBg.width = loginDiv.width - (captchaBg.x * 2);

  login.x = loginDiv.width - login.width - 8;
  login.size = 8;

  user.width = userBg.width - 2;
  pass.width = passBg.width - 2;
  captcha.width = captchaBg.width - 2;
  captchaImg.x = 8;
  captchaImg.width = loginDiv.width - (captchaImg.x * 2);

  signinTitle.y = 35;
  signinTitle.x = 5;
  signinTitle.width = '100%';
  sizeCalc.font = signinYour.font;
  sizeCalc.size = signinYour.size;
  sizeCalc.value = signinYour.innerText;
  signinYour.y = 53;
  signinYour.x = 5;
  signinYour.width = sizeCalc.idealBoundingRect.width;
  signinLogo.y = 53;
  signinLogo.x = signinYour.x + signinYour.width;
  signinAccount.y = 53;
  signinAccount.x = signinLogo.x + signinLogo.srcWidth + 2;
  signinAccount.width = loginDiv.width - signinAccount.x;

  if (loginDiv.height < (newAccountLink.y + newAccountLink.height)) {
    newAccountLink.visible = false;
    labelNoAccount.visible = false;
  } else {
    newAccountLink.visible = true;
    labelNoAccount.visible = true;
  }
};
