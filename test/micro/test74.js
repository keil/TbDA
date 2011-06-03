var gt = "crazy"
for (x in {}) {
    gt = gt +x;
}
dumpValue(x)
var g = {a: 234, b:23, t:gt}
var sum = 0;
for (x in g) {
    dumpValue(x)
    sum = sum + g[x];
}
dumpValue(sum);
