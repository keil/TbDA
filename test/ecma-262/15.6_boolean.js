//////////////////////////////////////////////////
// ECMA-262
//////////////////////////////////////////////////

var n0 = trace( 3 );
var n1 = trace( 5 );
var n2 = trace( 7 );
var n3 = trace( 11 );

var s0 = trace( "3" );
var s1 = trace( "5" );
var s2 = trace( "7" );
var s3 = trace( "11" );

var b0 = trace( true );
var b1 = trace( false );

var infinity = trace ( Infinity );
var nan = trace ( NaN );
var unbdef = trace ( undefined );

var o0 = trace ( new Object() );

var f0 = trace ( new Function() );

var a0 = trace ( new Array() );


//////////////////////////////////////////////////


/* 15.6.1.1 */
var test_boolean1 = Boolean( b0 );
/* 15.6.2.1 */
var test_boolean2 = new Boolean( b0 );


/* 15.6.3.1 */
var test_prototype = Boolean.prototype;


/* 15.6.4.1 */
var test_constructor	= b0.constructor();
/* 15.6.4.2 */
var test_tostring 		= b0.toString();
/* 15.6.4.3 */
var test_valueof 		= b0.valueOf();