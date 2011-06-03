function f1(x,y) {
	return x+y;
}
assert(f1(1,2) == 3);

function f2(x,y) {
	return x+y;
}
assert(f2(1,2,3) == 3);

function f3(x,y) {
	return x+y;
}
assert(isNaN(f3(1)));

function f4(x,y) {
	return arguments[0]+arguments["1"]+arguments[2];
}
assert(f4(1,"foo",3) == "1foo3");

function f5(x,y) {
	this.q = x+y;
}
assert((new f5(1,2)).q == 3);

function f6(x,y) {
	return Object;
}
assert((new f6(1,2)) == Object);

function f7(x) {
	x = x + 1;
	return g();
	function g() {return x+2}
}
assert(f7(1)==4);
dumpValue(f7(1));


