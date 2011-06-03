var value = trace("1234567890");

var func = trace(function () {
   return value;
}); 

 var write = trace(function() {
   value = "test";

   func = function () {
      return "";
   }

   if(document.cookie) {
      document.cookie = "username=test;";
   }

   document.getElementById = function() {
      return "";
   }

});

var result = write();