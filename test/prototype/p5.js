// extends p1.js

var john = new Pirate('Long John');
//john.sleep();
// -> ERROR: sleep is not a method

// every person should be able to sleep, not just pirates!
Person.addMethods({
  sleep: function() {
    return this.say('ZzZ');
  }
});

dumpValue(john.sleep()); // -> "Long John: ZzZ, yarr!"
