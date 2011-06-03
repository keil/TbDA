// plain function invocation
function f(x, y) {
	return x+y;
}
var a1 = f(17, 4);
dumpValue(a1);

// function invocation via call without receiver
function g(x, y) {
	return x-y;
}
var a2 = g.call(null, 101, 1);
dumpValue(a2);

// function invocation via call with receiver
function h() {
	return this.x;
}
var a3 = h.call({ x: "hoohoo" });
dumpValue(a3);

// calling a primitve
// var d = "xxx".concat("yyy"); // works ok
var a4 = "".concat.call("xxx", "yyy");
dumpValue(a4);

// recursive calling
var a5 = "zzz".substring.call.call("".concat, "xxx", "yyy");
dumpValue(a5);

var a6 = Function.call.call("".concat, "xxx", "yyy");
dumpValue(a6);

var a7 = f.call.call("".concat, "xxx", "yyy");
dumpValue(a7);

var a8 = f.call.call.call("".concat, "xxx", "yyy");
dumpValue(a8);
