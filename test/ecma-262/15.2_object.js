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
var o1 = trace ( new Object() );

//////////////////////////////////////////////////

/* 15.2.1.1 */
var test_object1 = Object( n0 );
/* 15.2.2.1 */
var test_object2 = new Object( n0 );


/* 15.2.3.1 */
// var test_prototype = new Object.prototype;


/* 15.2.3.2 */
//var test_propertyof					= Object.getPrototypeOf( o0 );
/* 15.2.3.3 */
//var test_getownpropertydescriptor	= Object.getOwnPropertyDescriptor( o0, s0 );
/* 15.2.3.4 */
//var test_getownpropertynames		= Object.getOwnPropertyNames( o0 );
/* 15.2.3.5 */
//var test_cretae						= Object.create( o0 );
/* 15.2.3.6 */
//var test_defineproperty				= Object.defineProperty( o0, s1, n0 );
/* 15.2.3.7 */
//var test_defineproperties			= Object.defineProperties( o0, s1 );


///* 15.2.3.8 */
//var test_seal				= Object.seal( o0 );
///* 15.2.3.9 */
//var test_freeze				= Object.freeze( o0 );
///* 15.2.3.10 */
//var test_preventextension	= Object.preventExtension( o0 );
///* 15.2.3.11 */
//var test_issealed			= Object.isSealed( o0 );
///* 15.2.3.12 */
//var test_isfrozen			= Object.isFrozen( o0 );
///* 15.2.3.13 */
//var test_isextensible		= Object.isExtensible( o0 );
///* 15.2.3.14 */
//var test_keys				= Object.keys( o0 );

/* 15.2.4.1 */
var test_constructor			= o0.constructor();
/* 15.2.4.2 */
var test_tostring				= o0.toString();
/* 15.2.4.3 */
var test_tolocalestring			= o0.toLocaleString();
/* 15.2.4.4 */
var test_valueof				= o0.valueOf();
/* 15.2.4.5 */
var test_hasownproperty			= o0.hasOwnProperty( s0 );
/* 15.2.4.6 */
var test_isprototypeof			= o0.isPrototypeOf( o1 );
/* 15.2.4.7 */
var test_propertyisenumerable	= o0.propertyIsEnumerable( s0  );

