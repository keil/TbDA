//////////////////////////////////////////////////
// 3.0) Array

var array1 = new Array();
array1[0] = trace ( 17 );
array1[ trace ( 3 ) ] = 19;

var array2 = new Array( trace ( 3 ), trace ( 5 ), trace ( 7 ));
var array3 = trace ( new Array( array1[0], trace ( 3 ), trace ( 5 ), trace ( 7 )) );

var array4 = trace ( new Array( 3, 5, 7) );

var array5 = Array( trace (19) );
array5[0] = trace ( 3 );
array5[1] = trace ( 5 );
array5[2] = trace ( 7 );

var array6 = Array( );
array6[0] = trace ( 3 );
array6[1] = trace ( 5 );
array6[2] = trace ( 7 );

var array7 = trace ( new Array() );

for(i = 0; i<10; i++) {
	if (i==5) array7[i] = trace(17); 
	else array7[i] = i;
}
array7[10] = trace ( 65 ) ; 

//////////////////////////////////////////////////
// 3.1) Test


var test_a = array7;
var test_b = array7[5];
var test_c = array7[7];
var test_d = array7[10];
var test_f = array7.pop();
var test_g = array7.pop();
var test_h = array7[5];

var test = array1;
var test1 = array1[0];
var test2 = array1[3];

var test3 = array2[0];
var test3x = array2;

var test4 = array3;
var test5 = array3[0];

var test6 = array4;
var test7 = array4[0]


var test7 = array5[1];
var test7s = array5;
var test8 = array6[1];
var test8s = array6;




//////////////////////////////////////////////////
// 3.2) Ausgabe

//	d(array1) = {}
//	d(array2) = {}
//	d(array3) = {t8}

//	d(test1) = {t0}
//	d(test2) = {}
//	d(test3) = {t2}
//	d(test4) = {t8}