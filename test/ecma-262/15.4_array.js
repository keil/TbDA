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

var a0 = trace ( new Array( n3 ) );

var a1 = trace ( new Array( n3 ) );

var a2 = trace ( new Array( n3 ) );

//////////////////////////////////////////////////

/* 15.4.1.1 */
var test_array1 = Array( n0, s0 );
/* 15.4.2.1 */
var test_array2 = new Array( n0, n1, n2, n3 );
/* 15.4.2.2 */
var test_array3 = new Array( n1 );


/* 15.4.3.1 */
var test_prototype = Array.prototype;
/* 15.4.3.2 */
// var test_isarray = Array.isArray( a0 );


/* 15.4.4.1 */
var test_constructor	= a0.constructor();
/* 15.4.4.2 */
var test_tostring		= a0.toString();
/* 15.4.4.3 */
var test_tolocalestring	= a0.toLocaleString();
/* 15.4.4.4 */
var test_concat			= a0.concat( s0 );
/* 15.4.4.5 */
var test_join			= a0.join( s1 );
/* 15.4.4.6 */
var test_pop			= a0.pop();
/* 15.4.4.7 */
var test_push			= a0.push( s1 );
/* 15.4.4.8 */
var test_reverse		= a1.reverse();
/* 15.4.4.9 */
var test_shift			= a1.shift();
/* 15.4.4.10 */
var test_sclice			= a1.slice( n0, n2 );
/* 15.4.4.11 */
var test_sort			= a1.sort( s0 );
/* 15.4.4.12 */
var test_splice			= a1.splice( s0, n0, n2, n3 );
/* 15.4.4.13 */
var test_unshift		= a1.unshift( n1, n0 );
/* 15.4.4.14 */
//var test_indexof		= a1.indexOf( n2 );
/* 15.4.4.15 */
//var test_lastindexof	= a1.lastIndexOf( n2 );
/* 15.4.4.16 */
//var test_every			= a1.every( s1 );
/* 15.4.4.17 */
//var test_some			= a1.some( s1 );
/* 15.4.4.18 */
//var test_foreach		= a1.forEach( s1 );
/* 15.4.4.19 */
//var test_map			= a1.map( s1 );
/* 15.4.4.20 */
//var test_filter			= a1.filter( s1 );
/* 15.4.4.21 */
//var test_reduce			= a1.reduce( s1 );
/* 15.4.4.22 */
// var test_reduceright	= a2.reduceRight( s1 );


/* 15.4.5.2 */
var test_length2		= a0.length;