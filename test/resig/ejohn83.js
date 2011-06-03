var Button = { 
  click: function(){ 
    this.clicked = true; 
  } 
}; 
 
var elem = document.createElement("li"); 
elem.innerHTML = "Click me!"; 
elem.onclick = Button.click; 
document.getElementById("results").appendChild(elem); 
 
elem.onclick(); 
assert( elem.clicked );
