//////////////////////////////////////////////////
// 1) Value

var value1 = trace( 13 );
var value2 = trace( "string" );
var value3 = trace( true );

//////////////////////////////////////////////////
// 2) Array

var array1 = new Array();
array1[0] = trace ( 17 );
array1[ trace ( 3 ) ] = 19;

var array2 = new Array( trace ( 3 ), trace ( 5 ), trace ( 7 ));

var array3 = new Array( trace ( 3 ), trace ( 5 ), trace ( 7 ));

// 3) Funktionen

// Objekte
// var array1 = new Array();
// nachträglich erzeigte objekte


var test1 = trace ( value1 );
var test2 = trace ( test1 );

//function trace(value) {
//	return value;
//}
//
var var11 = 76;
var var14 = 67;

var var13 = var11 + var14;




var x = trace(7);
var y = trace(7);
var z = x + y;



//var object20 = { object20_2 : { a : true, b : 2, c : "string in object" } };
//var object21 = { a : true, b : 2, c : "string in object" };
//
//
//
//


function onDo(ax, bx) {
	var ma = ax;
	var mb = bx;
	return ma + mb;
}

var test = onDo(var11, var14);


//
//var neue_katze = function () {
//	var lebensZahl = 7;
//	var maunz = function () {
//		return (lebensZahl > 0) ? "miau" : "örks";
//	};
// 
//	// gib neues objekt zurück
//	return { 
//		toeten: MARK (function () {
//			lebensZahl -= 1;
//			alert(maunz());
//		})
//	};
//};
//var otto = neue_katze();
//var result = otto.toeten(); // miau





//// RULES
//
///* RULE 1
// * var x; d(x) = {x}
// */
//
//var var10;
//var var11 = 4711;
//var var12 = "test";
//var var13 = true;
//var var14 = 4711;
//
///* RULE 2
// * var x = { a : }; d(x) = {x}
// */
//
//var object20 = { object20_2 : { a : true, b : 2, c : "string in object" } };
//var object21 = { a : true, b : 2, c : "string in object" };
//
//
//var object30 = new Object;
//object30.a = true;
//object30.b = 2;
//object30.c = "string in object";
//
//var array40 = new Array ("rot", "grün", "blau");
//var string41  = new String ("Fröhliche Ostern");
//var date42    = new Date();
//var number43    = new Number(); 
//
//var formular51 = new Object;
//
//formular51.firstName = document.getElementById('firstName');
//formular51.lastName  = document.getElementById("lastName");
//formular51.plz       = document.getElementById("plz");
//formular51.ort       = document.getElementById("ort");
////
//
//var adress = new Object();
//adress = { lastName: "Meier",
//		   plz:      47111,
//		   ort:      "Neukirchen"
//		 };
//
//
//var adress2 = { lastName: "Meier", plz: 47111, ort: "Neukirchen" };
//
//
////function onDo(ax, bx) {
////	var ma = ax;
////	var mb = bx;
////	return ma + mb;
////}
////
////var test = new onDo(var11, var14);
//
//
///* RULE 3
// * func(); d(func()) = {func()}
// * var x = func(); d(x) = {x, func()}
// * 
// */
//
////function func() {
////	var funca = 7;
////}
//
//// var test  = true;
//// var func30 = func();
//// var func31 = new func();
//
//
////var x = 7;
////var y = 7;
////var z = x + y;
////
//////var object1 = { a : true, b : 2, c : "string in object" };
////
////var object2 = { obj : { a : true, b : 2, c : "string in object" } };
////var object2_attribute = object2.obj.c;
////var object2_attribute2 = object2.obj.a;
////
////var object2_attribute3 = object2.obj;
////var object2_attribute4 = object2_attribute3.c;
////
////
//function onDo(ax, bx) {
//	var ma = ax;
//	var mb = bx;
//	return ma + mb;
//}
//
//var test2 = onDo(var14, var11);