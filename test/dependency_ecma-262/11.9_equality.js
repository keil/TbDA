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

//////////////////////////////////////////////////

/* 11.9.1 */
var test_equals				= ( n0 == n1 );
/* 11.9.2 */
var test_notequals			= ( n0 != n1 );
/* 11.9.3 */
var test_strictequals		= ( n0 === n1 );
/* 11.9.4 */
var test_strictenotquals	= ( n0 !== n1 );