
function Polygon() {
  this.area = function() {};
}

function Rectangle() {
    this.temp = Polygon;
    this.temp();
  }

function objectMasquerading () {
  return new Rectangle();
}

function Rectangle2() { }
Rectangle2.prototype = new Polygon();

function sharedClassObject() {
  return new Rectangle2();
}

var rec1 = objectMasquerading();
var rec2 = sharedClassObject();

dumpObject(rec1);
dumpObject(rec2);
