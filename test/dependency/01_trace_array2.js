//
//(new Array()).put(7);

////////////////////////////////////////////////////
//// 3.0) Array
//
//var array	= trace ( new Array() );
//var i		= trace ( 0 );
//var j		= trace ( 0 );
//var n		= trace ( 7 );
//
//array[i] = n;
//
//var object = trace ( new Object() );
//object.i = n;
//
////////////////////////////////////////////////////
//// 3.1) Test
//
//var test_a = array;
//var test_b = array[0];
//var test_c = array[j];
//var test_d = array[i];
//
//
//var test_x =  object;
//var test_y =  object.i;
//var test_z =  object[i];
//
//function getArray() {
//	return trace( trace ( new Array() ) );
//}
//
//var a1 = getArray();
//var a2 = getArray();

var array	=	trace ( new Array() );
var value	=	trace ( true );
var i		=	trace ( 0 );
var j		=	trace ( 0 );

array[i] = trace (value);
array[i+1] = trace (value);


var v0	= array;
var v1	= array[0];
var v2	= array[j];

var v3 = array.length;

