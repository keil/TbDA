var old_city_id;

function view_onopen() {
  zoom_progress_bar.value = options("zoom")*33;
  transparency_progress_bar.value = options("transparency");
  shift_progress_bar.value = options("shift_speed");
  show_info_checkbox.value = options("show_info");
  
  //Set marker in world 
  UpdateMarker();
  //Set labels
  FillLabels();
}

function view_onclose() {
  // how to recognize Cancel?!  
}


function UpdateMarker() {
  var latitude = options("lat");
  var longitude = options("lon");
  
  var w = map.width - 76;
  var h = map.height;
  
  var x = (longitude * (w/2) / 180) + (w/2);
  var y = (h/2) - ((h/2) * latitude / 90);
  
  marker.x = x-10;
  marker.y = y-10;
  
}

function FillLabels() {
  city_info.innerText = options("city");
  lat_info.innerText = options("lat").toLocaleString() + "°";
  lon_info.innerText = options("lon").toLocaleString() + "°";
  sunr_info.innerText = new Date(Date.parse("Tue, 27 May 1980 " + options("sunrise"))).toLocaleTimeString();
  suns_info.innerText = new Date(Date.parse("Tue, 27 May 1980 " + options("sunset"))).toLocaleTimeString();
  zone_info.innerText = "UTC " + (options("zone") < 0 ? "":"+") + options("zone") + ":00";
}

function new_city_edit_onfocusin() {
  new_city_edit.value = "";
}

function new_city_edit_onfocusout() {
  if (new_city_edit.value == "") {
    new_city_edit.value = EDIT_CITY_VALUE;
  }
}

function city_search_onkeypress() {
  if (event.keyCode == 13) { // Enter pressed -> start search
    city_search_button_clicked();
  }
}

function city_search_button_clicked() {  
  search_progress.visible = true;
  //handle error label here
  if (error_label.visible) {
    error_label.visible = false;
  }
  if (new_city_edit.value != "") {
    // search for city
    old_city_id = options("city_id");
    SearchCity(new_city_edit.value);
  }
}

function AfterSearchCity(error) {
  if (error == 0) {
    // search ok -> new city_id in options
    FetchCityData(options("city_id"));
  }
  else {
    // no search result -> error
    search_progress.visible = false;
    search_progress.value=0;
    error_label.innerText = ERROR_UNKNOWN_LOCATION;  
    error_label.visible = true;
  }
}

function AfterFetchCityData(error) {
  search_progress.visible = false;
  search_progress.value=0;
  if (error == 0) {
    // everything ok -> update visuals
    new_city_edit.value = options("city");
    FillLabels();
    UpdateMarker();
  }
  else {
    // error -> reset to old city id
    options("city_id") = old_city_id;
    error_label.innerText = ERROR_CITY_DATA;  
    error_label.visible = true;
  }
}

function zoom_in_button_clicked() {
  if (options("zoom") < 3) {
    options("zoom") = options("zoom") + 1;
    zoom_progress_bar.value = options("zoom")*33;
  }
}

function zoom_out_button_clicked() {
  if (options("zoom") > 1) {
    options("zoom") = options("zoom") - 1;
    zoom_progress_bar.value = options("zoom")*33;
  }
}

function transparency_onchange() {
  options("transparency") = transparency_progress_bar.value;
}

function shift_onchange() {
  options("shift_speed") = shift_progress_bar.value;
}

function show_info_checkbox_onchange() {
  options("show_info") = show_info_checkbox.value;
}