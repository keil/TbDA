var a = trace ( 'a' );
var b = trace ( 'b' );

//var x = trace ( "a" );

var x = 0;
var y = 0;
var z = 0;

var value = trace ( 'c' );

switch ( value ) {
	case a:
		x++;

	case b:
		y++;

	default:
    	z++;
}

var test1 = 7; 
var tX = x;
var tY = y;
var tZ = z;
