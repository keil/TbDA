options.DefaultValue("lat") = 48.23;
options.DefaultValue("lon") = 14.18;
options.DefaultValue("sunrise") = "5:15 AM";
options.DefaultValue("sunset") = "9:00 PM";
options.DefaultValue("zone") = 2; // day saving time, in winter 1
options.DefaultValue("city") = "Linz, Austria";
options.DefaultValue("city_id") = "AUXX0016";
options.DefaultValue("zoom") = 1;  
options.DefaultValue("transparency") = 255;  
options.DefaultValue("shift_speed") = 255;
options.DefaultValue("show_info") = false;;

var _minimized = false;
var _extended = false;
var _showhideanim = 0;
var _showhideanim_value = 0;
var _collapseextendanim = 0;
var _collapseextendanim_value = 0;
var _showinfolabel = false;
var _latlon = 0;
var _zoom = 0;
var _opacity = 0;
var _shift_speed = 0;

function view_onopen() {
  _latlon = options("lat")*1000000 + options("lon"); //check variable if map has to be moved in onoptionchanged
  _zoom = options("zoom");
  _opacity = options("transparency");
  _shift_speed = options("shift_speed");
  _showinfolabel = options("show_info");
  ShowInfoLabel(_showinfolabel);
  
  // Fetch City Data
  FetchCityData(options("city_id")); // async call -> call back: AfterFetchCityData()
  
  // set transparency
  clock.opacity = options("transparency");
  
  // Fill info labels
  FillInfoLabels();
  
  //start timer
  OnMinuteTimer();
  OnSecondTimer();
  //Set world Position
  MoveMap();
}

function view_onoptionchanged() {
  if (_latlon != options("lat")*1000000 + options("lon") || _zoom != options("zoom")) {
    _latlon = options("lat")*1000000 + options("lon");
    _zoom = options("zoom");
    MoveMap();
  }
  else if (_showinfolabel != options("show_info")) { 
    _showinfolabel = options("show_info");
    ShowInfoLabel(_showinfolabel);
  }
  else if (_opacity != options("transparency")) { 
    clock.opacity = options("transparency");
  }
  else if (_shift_speed != options("shift_speed")) { 
    // nothing
  }
  else {
    FillInfoLabels();
  }
}

function view_onminimize() {
  _minimized = true;
  OnMinuteTimer();
  OnSecondTimer();
}

function view_onrestore() {
  _minimized = false;
  view.caption = GADGET_NAME;
}

function FillInfoLabels() {
  var end = options("city").indexOf(",");
  info_label.innerText = options("city").substr(0,end);

  var start = options("city").indexOf(",");
  info_state.innerText = options("city").substr(start+2);
  
  info_lon.innerText = options("lon").toLocaleString() + "°";
  info_lat.innerText = options("lat").toLocaleString() + "°";
  info_sunr.innerText = new Date(Date.parse("Tue, 27 May 1980 " + options("sunrise"))).toLocaleTimeString();
  info_suns.innerText = new Date(Date.parse("Tue, 27 May 1980 " + options("sunset"))).toLocaleTimeString();
  info_zone.innerText = "UTC " + (options("zone") < 0 ? "":"+") + options("zone") + ":00";
}

function ShowInfoLabel(show) { 
  if (_showhideanim != 0) {
    view.cancelAnimation(_showhideanim);
  }
  if (show) {
    view.height += info_div.height + info_bottom.height;
    clock.height += info_div.height + info_bottom.height;
    _showhideanim = view.beginAnimation("ShowHideAnimation()", _showhideanim_value, 255, 255-_showhideanim_value);
  }
  else {
    _showhideanim = view.beginAnimation("ShowHideAnimation()", _showhideanim_value, 0, _showhideanim_value);
  }
}

function ShowHideAnimation() {
  _showhideanim_value = Math.round(event.value);
  info_div.opacity = _showhideanim_value;
  info_bottom.opacity = _showhideanim_value;
  if (!options("show_info") && _showhideanim_value == 0) {
    view.height = "140";
    clock.height = "140";
  }
}

function OnMinuteTimer() {
  var now = new Date();
  UpdateMinuteHand(now);
  UpdateHourHand(now);
  
  var timeout = (61 - now.getSeconds()) * 1000;
  view.setTimeout("OnMinuteTimer()", timeout);
}

function OnSecondTimer() {
  var now = new Date();
  UpdateSecondHand(now);
  DayNightShift();

  if (_minimized) {
    view.caption = now.toLocaleTimeString();
  }
    
  var timeout = 1000;
  view.setTimeout("OnSecondTimer()", timeout);
}

function UpdateHourHand(now) {
  var hours = now.getHours();
  if (hours >= 12) {
    hours -= 12;
  }
    
  var minutes = (now.getMinutes() + (60 * hours)) / 2;
  clock_hour_hand.rotation = minutes;
}

function UpdateMinuteHand(now) {
  var seconds = (now.getSeconds() + (60 * now.getMinutes())) / 10;
  clock_minute_hand.rotation = seconds ;
}

function UpdateSecondHand(now) {
  var seconds = (60 * now.getSeconds()) / 10;
  clock_second_hand.rotation = seconds ;
}

function parseTime(time) {
  if (time.length == 0) {
    return new Date();
  }
  
  var colonPos = time.search(":");
  var spacePos = time.search(" ");
  
  var hours = time.substr(0, colonPos);
  var minutes = time.substr(colonPos+1, 2);
  var AMPM = time.substr(spacePos+1);
  
  if (hours == "12") {
    hours = 0;
  }
  
  if (AMPM == "PM") {
    hours = parseInt(hours) + 12;
  }
  var d = new Date();
  d.setHours(hours);
  d.setMinutes(minutes);
  d.setSeconds(0);
  
  return d;
}

function MoveMap() {
  var latitude = options("lat");
  var longitude = options("lon");
  var width;
  var height;
  var offset;
  switch (options("zoom")) {
    case 1:
      width = 1280;
      height = 512;
      offset = 128;
      break;
    case 2:
      width = 1920;
      height = 768;
      offset = 192;
      break;
    case 3:
      width = 2560;
      height = 1024;
      offset = 256;
      break;
    default:
      width = 1920;
      height = 768;
      offset = 192;
      break;
  }
  var w = width - 2 * offset;
  var x = (longitude * (w/2) / 180) + (w/2) + offset;
  var y = (height/2) - ((height/2) * latitude / 90);
  day_world.visible = false;
  day_world.width = width;
  day_world.height = height;
  
  day_world.x = -x+64;
  day_world.y = -y+64;
  day_world.visible = true;

  night_world.visible = false;
  night_world.width = width;
  night_world.height = height;

  night_world.x = -x+64;
  night_world.y = -y+64;
  night_world.visible = true;
}

function DayNightShift() {
  // opacity based on sunrise/sunset 
  var sunrise = parseTime(options("sunrise"));
  var sunset = parseTime(options("sunset"));
  var now = new Date();
  
  var srt = sunrise.getTime();
  var sst = sunset.getTime();
  var nt = now.getTime();
  
  if (nt <= srt) {  
    // Before Sunset
    diff = srt - nt;
    diff = diff / 10000;
    if (diff > options("shift_speed")) {
      diff = 255;
    }
    else {
      diff = diff / options("shift_speed") * 255;
    }
    night_world.opacity = (diff);
    clock_frame_night.opacity = (diff);
    clock_overlay_night.opacity = (diff);
  }
  else if (nt > srt && nt <= sst) {
    // Day Between Sunrise and Sunset
    diff = sst - nt;
    diff = diff / 10000;
    if (diff > options("shift_speed")) {
      diff = 255;
    }
    else {
      diff = diff / options("shift_speed") * 255;
    }
    night_world.opacity = (255 - diff);
    clock_frame_night.opacity = (255 - diff);
    clock_overlay_night.opacity = (255 - diff);
  }
  else {
    // After Sunset
    night_world.opacity = 255;
    clock_frame_night.opacity = 255;
    clock_overlay_night.opacity = 255;
  }
}

function AfterSearchCity(error) {
  //CallBack for SearchCity()
}

function AfterFetchCityData(error) {
  search_progress.value=0;
  search_progress.visible=false;
  //CallBack for FetchCityData
  if (error != 0) {
    alert(ERROR_CITY_DATA);
  }
}

function collapse_extend_button_clicked() {
  _extended = !_extended;
  if (_collapseextendanim != 0) {
    view.cancelAnimation(_collapseextendanim);
  }
  if (_extended) {
    collapse_extend_button.image = "collapse_normal.png";
    collapse_extend_button.overImage = "collapse_over.png";
    collapse_extend_button.downImage = "collapse_down.png";
    _collapseextendanim = view.beginAnimation("CollapseExtendAnimation()", _collapseextendanim_value, info_top.height, 2*(info_top.height - _collapseextendanim_value));
  }
  else {
    collapse_extend_button.image = "extend_normal.png";
    collapse_extend_button.overImage = "extend_over.png";
    collapse_extend_button.downImage = "extend_down.png";
    _collapseextendanim = view.beginAnimation("CollapseExtendAnimation()", _collapseextendanim_value, 20, 2*_collapseextendanim_value);
  }
}

function CollapseExtendAnimation() {
  _collapseextendanim_value = Math.round(event.value);
  info_div.height = _collapseextendanim_value;
  info_bottom.y = info_div.y + info_div.height;
  view.height = 140 + info_div.height + info_bottom.height;
  clock.height = 140 + info_div.height + info_bottom.height;
}