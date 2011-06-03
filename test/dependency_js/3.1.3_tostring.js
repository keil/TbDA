//////////////////////////////////////////////////
// JavaScript Language Specification
//////////////////////////////////////////////////

function1 = trace ( function f() {
	return 42
});

function2 = trace ( function Car(make, model, year) {
	this.make = make
	this.model = model
	this.year = year
} );


var objnull = trace ( null );

var obj = new function2("Ford", "Mustang", 1969);

var posInfinity = trace ( 10*1e308 );

var date1 = trace ( new Date() );

var string1 = trace ( "string1" );
var boolean1 = trace ( true );
var number1 = ( 13 );

var array1 = trace ( new Array(1, 3, 5) );

//////////////////////////////////////////////////

var test1 = string1.toString(); 
var test2 = boolean1.toString();
var test3 = number1.toString();
var test4 = date1.toString();
var test5 = posInfinity.toString();

var test6 = function1.toString();
var test7 = obj.toString();
var test8 = date1.toString();
var test9 = objnull.toString();

