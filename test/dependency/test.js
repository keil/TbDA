var x = function() {return 7;}; var y = function() {return 7;}


var b = x==y;

dumpDependency(b,x,y)


//var index = trace (new Array());
//index[0] = trace (0);
//index[1] = trace (1);
//index[2] = trace (2);
//
//var x = 0;
//var i = 0;
//while ( i<3 ) {
//	var j = index[i];
//
//	if(j<7){
//		x = j;
//	}
//	i = i + 1;
//}





// dumpDependency(trace(99));
//
//var a = 5 + (b=trace(6));
//
//dumpDependency(a);
//
//var x = trace (5);
//var y = trace (7);
//
//var z = x + y;
//var zz = Math.abs(4711);
//var zzz = 0;
//
//if(trace(true)) {
//	zzz = 1;
//}

// dumpDependency(x, y, z);

//var index = new Array(0, 1, 2);
////index[0] = trace (0);
////index[1] = trace (1);
////index[2] = trace (2);
//
//var array = new Array();
//var i = 0;
//
//while ( i<1 ) {
//	var j = index[i];
//	
//	//dumpDependency(index);
//	dumpDependency(j);
//	//dumpDependency(j);
//	
//	array[j] = j;
//	i = i+1;
//	//i++;
//	dumpDependency(i);
//}
//
//var a = array[0];
//var b = array[1];
//var c = array[2];

//dumpDependency(a,b,c);

//
//var i = trace ( 7 );
//var j = trace ( Math.abs(i) );

//
//dumpDependency();
//
//var i = 7;
//
//dumpDependency(i);
//
//var j = Math.abs(i);
//
//dumpDependency(j);



//var test = trace( function () {
//	return trace (4711);
//});
//
//var a = trace ( test () );
//var b = trace ( test );
//
//
//function test2 () {
//	return trace (4711);
//}
//
//var c = trace ( test2 () );
//var d = trace ( test2 );





//
//function test () {
//	return trace (4711);
//}
//
//
//
//
//
//
//function test2 () {
//	return trace (4711);
//}





//var index = new Array();
//index[0] = 0;
//index[1] = 1;
//index[2] = 2;
//
//var array = new Array();
//
//var i = 0;
//
//while (i < 3) {
//	var j = index[i];
//	array[j] = trace(632);
//	i = i+1;
//}
//
//var a = array[0];
//var b = array[1];
//var c = array[2];



//var h1 = trace(0);
//var h2 = trace(1);
//var i = trace(1);
//var j = trace(1);
//
////var x = Math.abs(376);
////var y = abs(376);
//
//while(h1 < h2) {
//	h1 = h1 + i;
//}
//
////function abs(c) {
////	return c;
////}


//var h1 = trace(0); // t0
//var h2 = trace(1); // t1
//var i = trace(1);  // t2
//var j = trace(1);  // t3
//
//var array = new Array();
//array[0] = i;
//array[1] = j;
//array[2] = 1;
//array[3] = 1;
//
////while (h1 < 3) {
//	h2 = array[h1];
//	if (h2 < i +1) {
//		array[4] = 0;
//	}
//	array[0] = h2 + 1;
////	h1 = h1 + i;
////}
//
//var a = array[0];
//var b = array[1];
//var c = array[2];
//var d = array[3];
//
//var i
//
//while ()


