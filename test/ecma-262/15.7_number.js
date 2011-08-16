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

/* 15.7.1.1 */
var test_number1 = Number( n0 );
/* 15.7.2.1 */
var test_number2 = new Number( n0 );


/* 15.7.3.1 */
var test_prototype	= Number.prototype;
/* 15.7.3.2 */
var test_maxvalue	= Number.MAX_VALUE;
/* 15.7.3.3 */
var test_minvalue	= Number.MIN_VALUE;
/* 15.7.3.4 */
var test_nan		= Number.NaN;
/* 15.7.3.5 */
var test_ninfinety	= Number.NEGATIVE_INFINITY;
/* 15.7.3.6 */
var test_pinfinety	= Number.POSITIVE_INFINITY;


/* 15.7.4.1 */
var test_constructor	= n0.constructor();
/* 15.7.4.2 */
var test_tostring1		= n0.toString();
var test_tostring2		= n0.toString( n1 );
/* 15.7.4.3 */
var test_tolocalestring	= n0.toLocaleString();
/* 15.7.4.4 */
var test_valueof		= n0.valueOf();
/* 15.7.4.5 */
var test_tofixed		= n0.toFixed( n1 );
/* 15.7.4.6 */
var test_toexponential	= n0.toExponential( n1 );
/* 15.7.4.7 */
var test_toprecision	= n0.toPrecision( n1 );

