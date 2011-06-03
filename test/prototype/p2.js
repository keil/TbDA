var Vulnerable = {
  wound: function(hp) {
    this.health -= hp;
    if (this.health < 0) this.kill();
  },
  kill: function() {
    this.dead = true;
  }
};

// the first argument isn't a class object, so there is no inheritance ...
// simply mix in all the arguments as methods:
var Person = Class.create(Vulnerable, {
  initialize: function() {
    this.health = 100;
    this.dead = false;
  }
});

var bruce = new Person;
bruce.wound(55);
dumpValue(bruce.health); //-> 45
