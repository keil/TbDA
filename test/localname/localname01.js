function f(p) {
    dumpValue(p.z);
    p.z = null;
}

var a = new Object();
var b = new Object();
var c = new Object();

a.z = 42;
b.z = 24;
c.z = 12;


f(a);
f(b);
f(c);
dumpValue(a.z);
dumpValue(b.z);
dumpValue(c.z);
