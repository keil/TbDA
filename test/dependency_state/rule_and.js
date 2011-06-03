function getM() {
	testM = 798;
	return trace (true);
}

function getN() {
	testN = 798;
	return trace (true);
}

function getO() {
	testO = 798;
	return trace (true);
}

var result = getM() && ( getN() || getO() );
var test1 = 7;
