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
var undef = trace ( undefined );

//////////////////////////////////////////////////

/* 15.1.1.2 */
var test_infinity = infinity;
/* 15.1.1.1 */
var test_nan = nan;
/* 15.1.1.3 */
var test_undef = undef;


/* 15.1.2.1 */
// var test_eval = eval( s0 );
/* 15.1.2.2 */
var test_parseint = parseInt( s0 );
var test_parseint2 = parseInt( s0, n1 );
/* 15.1.2.3 */
var test_parsefloat = parseFloat( s0 );
/* 15.1.2.4 */
var test_isnan = isNaN( n0 );
/* 15.1.2.5 */
var test_isfinite = isFinite( n0 );


/* 15.1.3.1 */
var test_decodeuri			= decodeURI( s0 );
/* 15.1.3.2 */
var test_decodeuricomponent	= decodeURIComponent( s0 );
/* 15.1.3.3 */
var test_encodeuri			= encodeURI( s0 );
/* 15.1.3.4 */
var test_encodeuricomponent	= encodeURIComponent( s0 );
