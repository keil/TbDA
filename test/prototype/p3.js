var Logger = Class.create({
  initialize: function() { },
  log: [],
  write: function(message) {
    this.log.push(message);
  }
});

var logger = new Logger;
logger.log; // -> []
logger.write('foo');
logger.write('bar');
dumpValue(logger.log); // -> ['foo', 'bar']