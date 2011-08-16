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

/* 11.8.1 */
var test_leftshift		= n0 << n1;
/* 11.8.2 */
var test_rightshift		= n0 >> n1;
/* 11.8.3 */
var test_urightshift	= n0 >>> n1;