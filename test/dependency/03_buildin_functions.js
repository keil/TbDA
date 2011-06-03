//////////////////////////////////////////////////
// build-in functions

var s0 = trace ( "string" );
var s1 = trace ( "23" );

var b0 = trace ( true );
var b1 = trace ( false );

var n0 = trace ( 13 );
var n1 = trace ( 17 );


//////////////////////////////////////////////////

// TODO, eval funktioneirt nicht
// var test1 = eval(s0);

var test2 = parseInt(s1);
var test2_1 = parseInt(s1, n0);

var test3 = parseFloat(s1);
var test3_1 = parseFloat(s1);

var test4 = escape (s0);
var test5 = unescape (s0);
