var count = 0; 
for ( var i = 0; i < 4; i++ ) (function(i){ 
  setTimeout(function(){ 
    assert( i == count++ ); 
  }, i * 200); 
})(i);
