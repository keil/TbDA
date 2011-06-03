
function FetchCityData(city_id) {
  var request = new XMLHttpRequest();
  request.onreadystatechange = ReadyStateChanged_CityData;
  request.open("POST", "http://xoap.weather.com/weather/local/"+city_id+"?par=1026134024&key=53318d3ee1447152", true);
  request.send();
  
  function ReadyStateChanged_CityData() {
    var error = 0;
    //progressbar handling
    switch (request.readyState) {
      case 1:
        search_progress.value=62;
        break;
      case 2:
        search_progress.value=75;
        break;
      case 3:
        search_progress.value=87;
        break;
      case 4:
        search_progress.value=100;
        break;
    }

    if (request && request.readyState == 4) {
      if (request.status == 200) {
        try {
		  var start = request.responseText.indexOf("<dnam>");
		  if(start == -1) {
            error = 1;
            return;
          }
          var end = request.responseText.indexOf("</dnam>");
          var city = request.responseText.substr(start+6, end - start - 6);

          var start = request.responseText.indexOf("<lat>");
          if(start == -1) {
            error = 1;
          }
          var end = request.responseText.indexOf("</lat>");
          var latitude = request.responseText.substr(start+5, end - start - 5);
        
          var start = request.responseText.indexOf("<lon>");
          if(start == -1) {
            error = 1;
          }
          var end = request.responseText.indexOf("</lon>");
          var longitude = request.responseText.substr(start+5, end - start - 5);
        
          var start = request.responseText.indexOf("<sunr>");
          if(start == -1) {
            error = 1;
          }
          var end = request.responseText.indexOf("</sunr>");
          var sunrise = request.responseText.substr(start+6, end - start - 6);
        
          var start = request.responseText.indexOf("<suns>");
          if(start == -1) {
            error = 1;
          }
          var end = request.responseText.indexOf("</suns>");
          var sunset = request.responseText.substr(start+6, end - start - 6);

          var start = request.responseText.indexOf("<zone>");
          if(start == -1) {
            error = 1;
          }
          var end = request.responseText.indexOf("</zone>");
          var zone = request.responseText.substr(start+6, end - start - 6);
          
          if (error==0) {
            options("city") = city;
            options("lat") = parseFloat(latitude);
            options("lon") = parseFloat(longitude);
            options("sunrise") = sunrise;
            options("sunset") = sunset;
            options("zone") = zone;
          }
        } catch (e) {
          error = 1;
        }
      }
      else { // request.status != 200
        error = 1;
      }
      AfterFetchCityData(error);
    }
  }  
}

function SearchCity(city) {
  var request = new XMLHttpRequest();
  request.onreadystatechange = ReadyStateChanged_SearchData;
  request.open("POST", "http://xoap.weather.com/search/search?where="+city+"&par=1026134024&key=53318d3ee1447152", true);
  request.send();
  
  function ReadyStateChanged_SearchData() {
    var error = 0;
    //progressbar handling
    switch (request.readyState) {
      case 1:
        search_progress.value=12;
        break;
      case 2:
        search_progress.value=25;
        break;
      case 3:
        search_progress.value=37;
        break;
      case 4:
        search_progress.value=50;
        break;
    }
    
    if (request && request.readyState == 4) {
      if (request.status == 200) {
        try {
          var start = request.responseText.indexOf("<loc id=");
          if (start == -1) { //no city under this name
            error = 1;
          }
          var end = request.responseText.indexOf(" type=");
          var city_id = request.responseText.substr(start+9, end - start - 10);
          
          if (error == 0) {
            options("city_id") = city_id;
          }
        } catch (e) {
          error = 1;
        }
      }
      else { // request.status != 200
        error = 1;
      }    
      AfterSearchCity(error);
    }
  }  
}
