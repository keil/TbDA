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

var f0 =  new Function( "test", "return test"  );

//////////////////////////////////////////////////

/* 15.3.1.1 */
var test_function1 = Function( n0, s0 );
/* 15.3.2.1 */
var test_function2 = new Function( n0, s0);


/* 15.3.3.1 */
var test_prototype = Function.prototype;
/* 15.3.3.2 */
var test_length = Function.length;


/* 15.3.4.1 */
//var test_constructor	= f0.constructor( "test", "return test" );
/* 15.3.4.2 */
//var test_tostring		= f0.toString();
/* 15.3.4.3 */
//var test_applay			= f0.applay( s0, o0 );
/* 15.3.4.4 */
//var test_call			= f0.call( s0 );
/* 15.3.4.5 */
//var test_bind			= f0.bind( s0 );


/* 15.3.5.1 */
//var test_length2		= f0.length;
/* 15.3.5.2 */
//var test_prototype2		= f0.prototype;


