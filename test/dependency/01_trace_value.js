//////////////////////////////////////////////////
// 1.0) Value

var value1 = trace( 7 );
var value2 = trace( "string" );
var value3 = trace( true );
var value4 = trace( 13 );

//////////////////////////////////////////////////
// 1.1) Test

var test1 = value1;
var test2 = value2;
var test3 = value3;

var test4 = value1 + test1;
var test5 = value1 + value4;

//////////////////////////////////////////////////
// 1.2) Ausgabe

//	d(test1) = {t0}
//	d(test2) = {t1}
//	d(test3) = {t2}
//	d(test4) = {t0}
//	d(test5) = {t3, t0}

//	d(value1) = {t0}
//	d(value2) = {t1}
//	d(value3) = {t2}
//	d(value4) = {t3}