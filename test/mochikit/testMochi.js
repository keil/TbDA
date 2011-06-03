// Test file using the mochikit. No DOM is used

var arr = ["string", 0, {gt:4}, undefined]
var hg = map(function(x) {return "hest"}, arr)
print (hg)
