var object1 = trace (
		{ o: { p: { a : true, b : 2, c : "string in object" } } }
);

var object2 = trace (
		{ o: trace ({ p: trace ({ a : trace(true), b : trace(2), c : trace("string in object") }) }) }
);

//////////////////////////////////////////////////

var test1 = object1;
var test1_1 = object1.o;
var test1_2 = object1.o.p;
var test1_3 = object1.o.p.a;
var test1_4 = object1.o.p.b;
var test1_5 = object1.o.p.c;

var test2 = object2;
var test2_1 = object2.o;
var test2_2 = object2.o.p;
var test2_3 = object2.o.p.a;
var test2_4 = object2.o.p.b;
var test2_5 = object2.o.p.c;