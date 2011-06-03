function A() {}
A.prototype.count = 0;
function B() {}
B.prototype = new A;

var x = new B;
var y = new B;
assert(x.count == 0);
assert(y.count == 0);
x.count++;
assert(x.count == 1);
assert(y.count == 0);