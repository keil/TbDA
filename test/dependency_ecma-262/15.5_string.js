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

var r0 = trace ( new RegExp("", "") );

//////////////////////////////////////////////////

/* 15.5.1.1 */
var test_string1 = String( n0 );
/* 15.5.2.1 */
var test_string2 = new String( n0 );

/* 15.5.3.1 */
var test_prototype = String.prototype;


/* 15.5.3.2 */
var test_fromcharcode = String.fromCharCode( n0 );


/* 15.5.4.1 */
var test_constructor			= s0.constructor();
/* 15.5.4.2 */
var test_tostring				= s0.toString();
/* 15.5.4.3 */
var test_valueof				= s0.valueOf();
/* 15.5.4.4 */
var test_charat					= s0.charAt( n0 );
/* 15.5.4.5 */
var test_charcodeat				= s0.charCodeAt( n0 );
/* 15.5.4.6 */
var test_concat					= s0.concat( s1 );
/* 15.5.4.7 */
var test_indexof				= s0.indexOf( s1, n0 );
/* 15.5.4.8 */
var test_lastindexof			= s0.lastIndexOf( s1, n0 );
/* 15.5.4.9 */
var test_localecomapre			= s0.localeCompare( s1 );
/* 15.5.4.10 */
var test_match					= s0.match( r0 );
/* 15.5.4.11 */
var test_replace				= s0.replace( s2, s1 );
/* 15.5.4.12 */
var test_search					= s0.search( r0 );
/* 15.5.4.13 */
var test_slice					= s0.slice( n0, n1 );
/* 15.5.4.14 */
var test_split					= s0.split( s1, n1 );
/* 15.5.4.15 */
var test_substring				= s0.substring( n0, n1 );
/* 15.5.4.16 */
var test_tolowercase			= s0.toLowerCase();
/* 15.5.4.17 */
var test_tolocalelowercase		= s0.toLocaleLowerCase();
/* 15.5.4.18 */
var test_touppercase			= s0.toUpperCase();
/* 15.5.4.19 */
var test_tolocaleuppercase		= s0.toLocaleUpperCase();
/* 15.5.4.20 */
// var test_trim					= s0.trim();


/* 15.5.5.1 */
var test_length2				= s0.length;