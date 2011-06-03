try {
    var xx = 10 + 10;
    dumpValue(xx)
} 
catch (ee) {
    dumpValue(ee) //shouldn't be printed
}
dumpValue(xx);
