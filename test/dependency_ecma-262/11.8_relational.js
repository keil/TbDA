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
var test_lessthan			= n0 < n1;
/* 11.8.2 */
var test_greaterthan		= n0 > n1;
/* 11.8.3 */
var test_lessequalsthan		= n0 <= n1;
/* 11.8.4 */
var test_greaterequalsthan	= n0 >= n1;

/* 11.8.6 */
var test_instanceof			= n0 instanceof Number;
/* 11.8.7 */
var test_in					= n0 in trace ( { n0:n0, n1:n1 } );