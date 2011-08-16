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

/* 11.4.1 */
var test_delete			= delete n3;
/* 11.4.2 */
var test_void			= void n0;
/* 11.4.3 */
var test_typeof			= typeof n0;
/* 11.4.4 */
var test_inc			= ++ n0;
/* 11.4.5 */
var test_dec			= -- n0;
/* 11.4.6 */
var test_pos			= + n0;
/* 11.4.7 */
var test_neg			= - n0;
/* 11.4.8 */
var test_bnot			= ~ b0;
/* 11.4.9 */
var test_lnot			= ! n0;