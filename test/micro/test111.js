var fib = function f(x) {
	if (x <= 1)
		return 1;
	else
		return f(x-1) + f(x-1);
}

var t = fib(3);
dumpValue(t);
