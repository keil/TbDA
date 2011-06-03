//////////////////////////////////////////////////
// JavaScript Language Specification
//////////////////////////////////////////////////

var b0 = trace ( true );
var b1 = trace ( true );
var b3 = trace ( false );
var b4 = trace ( false );

//////////////////////////////////////////////////

var test1 = !b0; 
var test2 = b0 && b1;
var test3 = b3 || b1;

var test4 = b0 & b1;
var test5 = b0 | b1;

var test6 = b0 == b1; 
var test7 = b0 != b1;