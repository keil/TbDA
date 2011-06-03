//////////////////////////////////////////////////
// 3.0) Object

var array1 = trace ( new Array() );

var object1 = trace ( new Object );
object1.a = true;
object1.b = 2;
object1.c = "string in object";


var object2 = new Object;
object2.a = trace ( true );
object2.b = trace ( 2 );
object2.c = trace ( "string in object" );


var object3 = new Object;
object3.a = true;
object3.b = 2;
object3.c = "string in object";

trace ( object3.a );
trace ( object3.b );
trace ( object3.c );


var object4 = new Object;
object4.a = true;
object4.b = 2;
object4.c = "string in object";

trace ( object4 );


var object5 = trace ( { a : true, b : 2, c : "string in object" } );
var object6 = { object6_2 : trace ( { a : true, b : 2, c : "string in object" } ) };


var string1 = trace ( new String ("string in object") );
var date1 = trace ( new Date() );
var number1 = trace ( new Number() ); 

function onDo() {
	var ma = 7;
	var mb = 5;
}
var function1 = trace ( new onDo() );

//////////////////////////////////////////////////
// 4.1) Test

var test1 = array1;

var test2 = object1;
var test2_2 = object1.a;

var test3 = object2;
var test3_2 = object2.a;

var test4 = object3;
var test4_2 = object3.a;

var test5 = object4;
var test5_2 = object4.a;

var test6 = object5;
var test6_2 = object5.a;

var test7 = object6;
var test7_2 = object6.object6_2;
var test7_3 = object6.object6_2.a;

var test8 = string1;
var test9 = date1;
var test10 = number1;

var test11 = function1;

//////////////////////////////////////////////////
// 4.2) Ausgabe

//	d(length) = {}
//	d(prototype) = {}
//	d(callee) = {}
//	d(length) = {}
//	d(a) = {}
//	d(b) = {}
//	d(c) = {}
//	d(arguments) = {}
//	d(ma) = {}
//	d(mb) = {}
//	d(a) = {}
//	d(b) = {}
//	d(c) = {}
//	d(constructor) = {}
//	d(a) = {}
//	d(b) = {}
//	d(c) = {}
//	d(object6_2) = {t10}
//	d(a) = {}
//	d(b) = {}
//	d(c) = {}
//	d(length) = {}
//	d(a) = {}
//	d(b) = {}
//	d(c) = {}
//	d(Array) = {}
//	d(Date) = {}
//	d(Number) = {}
//	d(Object) = {}
//	d(String) = {}
//	d(array1) = {t0}
//	d(date1) = {t12}
//	d(function1) = {t14}
//	d(number1) = {t13}
//	d(object1) = {t1}
//	d(object2) = {}
//	d(object3) = {}
//	d(object4) = {t8}
//	d(object5) = {t9}
//	d(object6) = {}
//	d(onDo) = {}
//	d(string1) = {t11}
//	d(test1) = {t0}
//	d(test10) = {t13}
//	d(test11) = {t14}
//	d(test2) = {t1}
//	d(test2_2) = {}
//	d(test3) = {}
//	d(test3_2) = {t2}
//	d(test4) = {}
//	d(test4_2) = {}
//	d(test5) = {t8}
//	d(test5_2) = {}
//	d(test6) = {t9}
//	d(test6_2) = {}
//	d(test7) = {}
//	d(test7_2) = {t10}
//	d(test7_3) = {}
//	d(test8) = {t11}
//	d(test9) = {t12}
//	d(trace) = {}
//	d(a) = {t2}
//	d(b) = {t3}
//	d(c) = {t4}
//	d(length) = {}
//	d(a) = {}
//	d(b) = {}
//	d(c) = {}
//	d(object6_2) = {}