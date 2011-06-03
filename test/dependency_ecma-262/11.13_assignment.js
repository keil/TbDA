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

var test1	= n1;
var test2	= n1;
var test3	= n1;
var test4	= n1;
var test5	= n1;
var test6	= n1;
var test7	= n1;
var test8	= n1;
var test9	= n1;

var test10	= b1;
var test11	= b1;
var test12	= b1;

/* 11.13.1 */
test1	= n2;

/* 11.13.2 */
test2	*= n2;
test3	/= n2;
test4	%= n2;

test5	+= n2;
test6	-= n2;

test7	<<= n2;
test8	>>= n2;
test9	>>>= n2;

test10	&= b0;
test11	^= b0;
test12	|= b0;
