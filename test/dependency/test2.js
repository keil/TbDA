a = "a";
b = "b";
//c = a+b;
d = a + b;

if (Math.random() > 10) {
	var c= Math.random(); 
	for (var i = 0; i < Math.random(); i++ ) {
		c = "a" + c;
	}
	
} else {
	//c = "b";
}



dumpDependency(a);
dumpDependency(b);
dumpDependency(c);
dumpDependency(d);