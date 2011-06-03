//////////////////////////////////////////////////
// ECMA-262
//////////////////////////////////////////////////

var n0 = trace( 3 );
var n1 = trace( 5 );
var n2 = trace( 7 );
var n3 = trace( 11 );

var s0 = trace ( "" );
var s1 = trace ( "" );

var r0 = trace ( new RegExp("", "") );

//////////////////////////////////////////////////

/* 15.10.3.1 */
var test_regex1		= RegExp(s0, s1);
/* 15.10.4.1 */
var test_regex2		= new RegExp(s0, s1);

/* 15.10.5.1 */
var test_prototype = RegExp.prototype;


/* 15.10.6.1 */
var test_constructor	=	r0.constructor("", "");
/* 15.10.6.2 */
var test_exec			=	r0.exec( n0 );
/* 15.10.6.3 */
var test_test			=	r0.test( n0 );
/* 15.10.6.4 */
var test_tostring		=	r0.toString();


/* 15.10.7.1 */
var test_source			=	r0.source;
/* 15.10.7.2 */
var test_global			=	r0.global;
/* 15.10.7.3 */
var test_ignodecases	=	r0.ignoreCase;
/* 15.10.7.4 */
var test_multiline		=	r0.multiline;
/* 15.10.7.5 */
var test_lastindex		=	r0.lastIndex;
