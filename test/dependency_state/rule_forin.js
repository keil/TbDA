var array = trace ( new Array());

var m = trace ( 0 );
var n = trace ( 3 );
var o = trace ( 5 );
var p = trace ( 7 );

array[0] = m;
array[1] = n;
array[2] = o;
array[3] = p;

var result = new Array();

// FOR-IN Schleife
for ( i in array ) {
	result.push(i);
	x = i;
	z = 7;
}

var a = result;
var b = result[0];
var c = 7;
var d = x;