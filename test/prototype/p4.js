// extends p1.js

Pirate.allHandsOnDeck = function(n) {
  var voices = [];
  n.times(function(i) {
    voices.push(new Pirate('Sea dog').say(i + 1));
  });
  return voices;
}

dumpValue(Pirate.allHandsOnDeck(3));
