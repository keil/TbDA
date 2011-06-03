var write = trace(function(name, value, limit) {
   date = new Date();
   date = new Date(date.getTime() + limit);
   document.cookie = name + "=" + value + "; expires=" + date.toGMTString() + ";";
});

var read = trace(function read(n) {
   string = document.cookie;
   result = "";
   while (string != "") {
      cookiename = string.substring(0, string.search("="));
      cookiewert = string.substring(string.search("=") + 1, string.search(";"));

      if (cookiewert == "") {
         cookiewert = string.substring(string.search("=") + 1, string.length);
      }

      if (n == cookiename) {
         result = cookiewert;
      }
   }
   return result;
});

var set = trace(function(name) {
   return write("username", name, 60 * 60 * 24 * 365);
});

var get = trace(function() {
   if (document.cookie) {
      username = read("username");
      return username;
   } else {
      return "";
   }
});

var cookie_1 = document.cookie;

var varname = get();
var cookie_2 = document.cookie;

var resultwrite = set(trace("4711"));
var cookie_3 = document.cookie;