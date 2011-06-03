//////////////////////////////////////////////////
// 2.0) Value

function onDo() {
	return 13;
}
var function1 = trace ( onDo() );


var function2 = trace ( function() { return 17; } );

var a = trace ( 7 );
var b = trace ( 13 );
var function3 = trace ( function(m, n) { var x = m; var y = n; return x + y; } );


var function2 = trace ( function onDo2() {
	this.doSth = trace ( function()
	{
		return trace ( 17 );
	}  )
} );


//////////////////////////////////////////////////
// 2.1) Test

var test1 = function1;

var test2 = function2;
var test2_1 = function2();
var test2_2 = test2();

var test3 = function3;
var test3_1 = function3(a, b);
var test3_2 = test3(a, b);


var test4 = new function2();
var test4_1 = test4.doSth;
var test4_2 = test4.doSth();



// TESTE funktion in objekt



//////////////////////////////////////////////////
// 2.2) Ausgabe

//	d(function1) = {t0}
//	d(function2) = {t1}
//	d(onDo) = {}
//	d(test1) = {t0}
//	d(test2) = {t1}
//	d(test3) = {}