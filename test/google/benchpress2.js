function Benchmark(run) {
  this.run = run;
}

function time(benchmark) {
    benchmark.run();
}

function bubblesort() {
  dumpValue(this);
  var FOO = {};
  //dumpState();
  dumpValue(FOO);
}

function treesort() {
  dumpValue(this);
}

time(new Benchmark(bubblesort));
time(new Benchmark(treesort));
