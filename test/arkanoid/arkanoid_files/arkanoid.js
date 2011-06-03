/*

<!-- ------------------------------------------------- -->
<!--  SCHILLMANIA! - DHTML ARKANOID PROJECT V1.2 BETA  -->
<!-- ------------------------------------------------- -->
<!--                                                   -->
<!-- -- [ ABOUT ] ------------------------------------ -->
<!--                                                   -->
<!-- Created in 2002. Pardon the occasional deviance   -->
<!-- from the W3C standards we've all grown to love ;) -->
<!--                                                   -->
<!-- An attempt at recreating the classic arcade game  -->
<!-- entirely in the browser using DHTML (HTML/CSS/JS) -->
<!-- Developed and tested on a 433-MHz Intel Celeron   -->
<!--                                                   -->
<!-- ---[ RECENT CHANGES ]---------------------------- -->
<!--                                                   -->
<!-- VERSION 1.2a - 07/2007                            -->
<!--  * Added basic Apple iPhone support! (v1.0)       -->
<!--                                                   -->
<!-- VERSION 1.2 - 02/2007                             -->
<!--  * Updated timeout to interval-based animation    -->
<!--  * Added XHR ("ajax"), updating JS load technique -->
<!--  * Added URL hash bookmarking for user levels     -->
<!--  * Added support for Safari + Opera, IE 7 tweaks  -->
<!--  * Fixed event handling - drag+drop, mouse move   -->
<!--    under Firefox, Safari, Opera                   -->
<!--  * Fixed broken/cached image preload case         -->
<!--  * Made preload optional (doesn't deny launch)    -->
<!--  * Fixed visual overflow on laser, exit sequence  -->
<!--                                                   -->
<!-- VERSION 1.1 - 2003                                -->
<!--  * Help/About section added                       -->
<!--  * Sound/SFX support for Mozilla (finally)        -->
<!--  * Improved error handling for browsers w/o flash -->
<!--                                                   -->

<!-- VERSION 1.0B                                      -->

<!--  * Mozilla compatible! (tested on win32 so far)   -->
<!--  * Ball speed-up bug fixed (now works properly)   -->
<!--  * jsLoad() bug fix                               -->
<!--  * Laser powerup "no warp" (event handling) fixed -->
<!--  * Icon replacement of text-based top menu/"nav"  -->
<!--  * Filtering of inappropriate/suggestive levels   -->
<!--  ..(Yes, even with a 13x20 grid you get the idea) -->
<!--  * Various other corrections/improvements         -->
<!--                                                   -->
<!-- VERSION 0.995A                                    -->
<!--  * Highscore tracking (top 10) implemented        -->
<!--  * Level browser "preview" image/generator added  -->
<!--  * All original arcade levels (minus bosses) done -->
<!--  * "Iterations/second" counter added to top menu  -->
<!--  * Removal of pop-up window requirement           -->

<!--  * Browser resize handling (game auto-centering)  -->
<!--  * Game pause / "home" link added to console      -->

<!--                                                   -->
<!-- ---[ GENERAL DISCLAIMER STUFF ]------------------ -->
<!--                                                   -->
<!-- Not responsible for headaches, lost time or loss  -->
<!-- of productivity, girlfriends etc. as a result of  -->
<!-- trying to make sense of this code! .. It is bad!  -->
<!-- However, feedback is appreciated as always!       -->
<!--                                                   -->
<!-- ---[ MISCELLANEOUS ] ---------------------------- -->
<!--                                                   -->
<!-- Comments, questions etc: email "s" at this domain -->

*/

// thou shalt not put this game in a frameset and show ads
if (top.location != document.location) {
  top.location.href = document.location.href;
}

var baseHref = 'http://'+(window.location.href.toLowerCase().indexOf('www')+1?'www.':'')+'schillmania.com/arkanoid/';
var titleString = 'DHTML Arkanoid V1.2B | ';
var NOSOUND = 0;
var ie = (navigator.appName == 'Microsoft Internet Explorer')?true:false;
var isSafari = (navigator.userAgent.toLowerCase().indexOf('safari')+1);
var isOpera = (navigator.appName.indexOf('Opera')+1?1:0);
var isFirefox = navigator.userAgent.match(/Firefox/i);
var isIphone = navigator.userAgent.match(/iPhone|iPad/i); // yep, the apple iPhone (+April 2010, iPad too.) It kinda works. Instead of document.mousemove for paddle movement, capture click on a wide element and grab event.clientX. Should be able to capture finger movements, drag instead. Will get to this at some point.
if (isOpera) NOSOUND = 1; // hack
var flash = [];
var launchFlag = 1; // 0 = force preload
var progress = 0;
var capsuleNOSYNC = 1;
var startItems = ['Start Game (Arcade version)','Start Level Browser (Play user-submitted levels)','Start Level Editor (Create your own levels)'];

// [image preloader] Images, Objects, Bytes, Path, Counters, Total
var pI  = ['0','1','10','2','3','4','5','6','7','8','9','ball','ball_m','ball_static','ball_static_0_1','ball_static_1_1','ball_static_2_1','bg_left','bg_right','bg_top','blank','btn_arcade_0','btn_arcade_1','btn_browser_0','btn_browser_1','btn_divider','btn_editor_0','btn_editor_1','btn_help_0','btn_help_1','btn_highscores_0','btn_highscores_1','btn_pause_0','btn_pause_1','btn_play_0','btn_play_1','close','error_hide','error_ok','error_title','ie_0','ie_1','laser','life_icon','mozilla_0','mozilla_1','netscape_0','netscape_1','new_game','preview_bg.png','preview_bg_1.png','paddle','paddle_blank','paddle_explode','paddle_extend','paddle_laser','paddle_restrict','paddle_static','paddle_shadow','paddle_twin','paddle_unexplode','portal_0','portal_1','portal_1_0','portal_1_1','portal_1_3','portal_2','portal_3','preload_0','preload_1','preload_2','preload_3','sfx_0','sfx_1','win32_0','win32_1','brick/blue','brown','cyan','funky','funky_1','funky_respawn','gold','gold_1','green','grey','grey_1','magenta','red','teal','white','yellow','level_browser/btn_divider','error','page_0','page_1','random_0','random_1','title_bar_bg','level_editor/blue','brown','funky','gold','green','grey','grid','magenta','name_field_bg','name_field_border','none','red','save','save_1','shadow_bottom','shadow_right','teal','test_level','test_level_1','toolbox_title','white','wipe','wipe_1','yellow','highscores/1','2','3','4','5','6','7','8','9','10'];
var pIB = [];
var pIBAvg = 148109/pI.length;
for (var i=0; i<pI.length; i++) {
  pIB[i] = pIBAvg;
}

var capsuleBytes = [739,772,751,723,689,673,850,844,702,1635,780,746];
var pIBrickTypes = ['b','c','d','e','i','l','m','n','p','r','s','t'];
for (i=0; i<pIBrickTypes.length; i++) {
  if (capsuleNOSYNC) {
    pI[pI.length] = (i==0?'capsule/':'')+pIBrickTypes[i];
    pIB[pIB.length] = capsuleBytes[i];
  } else {
    for (var j=0; j<13; j++) {
      pI[pI.length] = (i==0&&j==0?'capsule/':'')+pIBrickTypes[i]+j;
      pIB[pIB.length] = 140.94642857142857142857142857143;
    }
  }
}

var pIO = [];
var pID = [];
var pIPath = 'image/';
var pIPathAppend = '';
var pILoaded = 0;
var pIBLoaded = 0;
var pIImages = pI.length;
var pIBytes = sum(pIB);
var flashDone = 0;
var flashLoaded = 0;
var pWidth = 381; // progress bar width

var screenX = 0;
var screenY = 0;
var clicked = 0;

function Movie(mvObj, mvFileName, mvBytes) {
  pIBytes += mvBytes; // add flash size to total
  this.loadMovie = function() {
    if (ie)
      this.movie.movie = this.fileName;
    else {
      // doesn't seem to work - hardcoded into embed.
      // this.movie.src = this.fileName;
    }
  }
  if (!NOSOUND && !isFirefox) {
    this.bytesLoaded = function() { return (this.movieNotNull()==1?parseInt(this.movie.PercentLoaded()*.01*this.bytes):0) }
  }
  else {
    this.bytesLoaded = function() { return this.bytes }
  }
  this.movieNotNull = function() {
    if (ie) {
      if (!this.movie.movie)
        return 0;
      else 
        return (this.movie.movie.indexOf(this.fileName)<=0?1:0);
    } else {
      if (!this.movie.src)
        return 0;
      else
        return (this.movie.src.indexOf(this.fileName)<=0?1:0);
    }
  }
  this.movie = (isSafari?document[mvObj]:document.getElementById(mvObj));
  this.fileName = mvFileName;
  this.bytes = mvBytes;
}

function NullMovie(mvBytes) {
  // Workaround: Null movie object for machines without flash support
  this.loadMovie = function() {
    this.movie.movie = '';
  }
  this.bytesLoaded = function() { return this.bytes }
  this.movieNotNull = function() { return 0 }
  this.fileName = null;
  this.movie = new Object();
  this.bytes = mvBytes;
}

function launchPopup(fixed) {
  if (fixed) 
    window.open(baseHref+'arkanoid.html','schillmaniaDHTMLArkanoid','width=468,height=500,toolbar=no,status=no');
  else
    window.open(baseHref+'arkanoid.html','schillmaniaDHTMLArkanoid');
}

function initError(errMsg,errLink) {
  return '<span style="font-family:Tahoma, Verdana;font-size:48px;color:#e0e0e0" onmouseover="this.style.color=\'#cccccc\'" onmouseout="this.style.color=\'#e0e0e0\'" onclick="'+errLink+'" class="button">DHTML ARKANOID</span><br /><span class="smallText" style="color:#aaaaaa">&nbsp;'+errMsg+'</span><br /><span class="smallText" style="display:block;margin-top:6px;color:#888888">&nbsp;DESIGN AND CODE BY SCOTT SCHILLER, <a href="http://www.schillmania.com">SCHILLMANIA.COM</a></span>';
}

function init() {
  screenX = 468;
  screenY = 500;
  getClientCoords();
  arkanoidMainContainer = document.getElementById('mainContainer');
  document.getElementById('initContainer').style.display = 'none';
  // remove "initContainer"
  // document.body.removeChild(document.getElementById('initContainer'));
  getClientCoords();
  centerContent();
  arkanoidMainContainer.style.display = 'block';
  window.onresize = function() {getClientCoords();centerContent()}

  // flash support test
  try {
    var oMovie = isSafari?document['f1moz']:document.getElementById('f1'+(!ie?'moz':''));
    if (oMovie.PercentLoaded()) {
      // flash support appears to be OK
      flash = [new Movie('f0'+(!ie?'moz':''),'arkanoid_audio.swf',96428),oMovie];
    }
  } catch(e) {
    // Exception: no flash support, or access to Flash activeX denied
    window.status = 'Warning: Unable to initialize Flash support. Sound will not be available.';
    setTimeout("window.status=''",5000);
    NOSOUND = 1; // set global flag
    document.getElementById('o_sfx').style.display='none'; // hide SFX control
    //flash = [new NullMovie(96428),new NullMovie(1)];
    flash[0] = new NullMovie(96428);
    flash[1] = new NullMovie(1);
  }
  if (!NOSOUND) document.getElementById('o_sfx').style.display='block'; // show SFX control
  startPreload();
  arkanoidBody = document.getElementById('arkanoidContainer');
  // detect URL parameters
  if (window.location.hash) {
    var param = window.location.hash.substr(1);
    if (param.indexOf('level')!=-1) {
      var usrLevel = parseInt(param.substr(5));
      if (isNaN(usrLevel)) return false;
      usrLevel = Math.max(1,usrLevel)-1; // basic filtering
      launch('levelBrowser',usrLevel);
    } else {
      if (param == 'browser') launch('levelBrowser');
      if (param == 'editor') launch('levelEditor');
    }
  }
}

function centerContent() {
  if (arkanoidMainContainer) {
    arkanoidMainContainer.style.left = parseInt(bodyX/2 - (screenX/2) + 35);
    arkanoidMainContainer.style.top = parseInt(bodyY/2) - 190;
  } else if (arkanoidBody) {
    arkanoidBody.style.left = (document.body.clientWidth/2) - (468/2);
    arkanoidBody.style.top = (document.body.clientHeight/2) - (502/2);
    arkanoidBodyOffX = parseInt(arkanoidBody.style.left);
    arkanoidBodyOffY = parseInt(arkanoidBody.style.top);
  }
}

function showProgress() {
  var newProgress = parseInt(progress/pWidth*pWidth);
  var o = document.getElementById('pre1');
  if (o) o.style.clip = 'rect(0px '+newProgress+'px 111px 0px)';
  if (progress >= pWidth && launchFlag != 2)
    preloadComplete();
}

function sum(o) {
  var total = 0;
  for (var i=0; i<o.length; i++) {
    total += parseInt(o[i]);
  }
  return total;
}

function startPreload() {
  if (!NOSOUND) {
    try {
      flash[0].loadMovie();
      flashDone = 0;
      checkFlash();
    } catch(e) {
      // oh well
      NOSOUND = true;
      flashDone = 1;
    }
  } else {
    flashDone = 1;
  }
  for (var i=0; i<pI.length; i++) {
    pID[i] = 0; // initialize "done" flag array
  }
  for (i=0; i<16; i++) {
    pIAddImage();
  }
  showProgress();
}

function pIAddImage() {
  if (pIO.length < pI.length) {
    var i = pIO.length;
    pIO[i] = new Image();
    pIO[i].onload = Function("pILoadHandler("+i+","+pIB[i]+");this.onload=null");
    pIO[i].onerror = Function("pILoadHandler("+i+","+pIB[i]+");this.onerror=null");
    if (pI[i].indexOf('/')>=0) {
      pIPathAppend = pI[i].substr(0,pI[i].indexOf('/')+1);
    }
    pIO[i].src = (pI[i].substr(0,2)!='./'?pIPath:'') + (pIPathAppend&&pI[i].indexOf(pIPathAppend)<0?pIPathAppend:'') + pI[i] + (pI[i].indexOf('.')>=0?'':'.gif');
  }
}

function pILoadHandler(i, bytes) {
// buggy complete state seems to fail on occasion under Mozilla - ignored for now.
//  if (((ie && pIO[i].readyState == 'complete') || (!ie && pIO[i].complete)) && !pID[i]) { // (!ie && pIO[i].complete)
    pID[i] = 1;
    pIO[i].onload = null;
    pILoaded++;
    pIBLoaded += bytes;
    progress = parseInt(((pIBLoaded+flashLoaded)/pIBytes)*pWidth);
    showProgress();
    setTimeout(pIAddImage,1); // no timeout means stack overflow in IE
    // pIAddImage();
//  }
}

function checkFlash() {
  if (NOSOUND) {
    flashDone = 1;
    return false;
  }
  if (!flashDone) {
    try {
      flashLoaded = flash[0].bytesLoaded();
      if ((flashLoaded == flash[0].bytes) && flash[0].movieNotNull())
        flashDone = 1;
      else
        setTimeout("checkFlash()",20);
    } catch(e) {
      // oh well
      flashDone = 1;
    }
  }
}

function preloadComplete() {
  launchFlag = 2;
  var o = document.getElementById('pre1');
  if (o) {
    o.src = 'image/preload_3.gif';
    document.getElementById('ohlookitsgottabeajax').src = 'image/blank.gif';
    for (var i=0; i<3; i++) {
      document.getElementById('preBall'+i).className = 'button';
      setTimeout("setStart("+i+","+1+",1)",150*(i+1));
      setTimeout("setStart("+i+","+0+",1)",600+(200*(i+1)));
    }
    document.getElementById('preStart0').style.visibility = 'visible';
  }
  if (isIphone) {
    document.getElementById('iphone-note').innerHTML = 'start &nbsp;|&nbsp; browse &nbsp;|&nbsp; edit';	
  }
}

function setStart(oIndex,n,noLabel) {
  var oBall = document.getElementById('preBall'+oIndex);
  var oStart = document.getElementById('preStart0');
  if (oStart && !noLabel) oStart.innerHTML = n?startItems[oIndex]:'';
  if (oBall) oBall.src = 'image/ball_static_'+(n?oIndex+'_':'')+n+'.gif';
  if (!clicked)
    playPreSound('wind'+(n==1?'Down':'Up'),70);
  else
    clicked = 0;
}

function playPreSound(audioTarget,volume) {
  if (!volume && volume != 0) {
    volume = -1;
  }
  if (flash[1] && !NOSOUND) {
    if (flash[1].ReadyState == 4 || flash[1].PercentLoaded() == 100) {
      flash[1].TGotoLabel("/" + audioTarget, "start")
      flash[1].TPlay("/" + audioTarget)
      if (volume != -1) 
        playPreFrame(audioTarget,'v'+volume); // play at 100% volume (default)
      else
        playPreFrame(audioTarget,'v30');
    }
  }
}

function playPreFrame(audioTarget, label) {
  if (flash[1].ReadyState == 4 || flash[1].PercentLoaded() == 100)
    flash[1].TGotoLabel("/"+audioTarget,label);
}

function setTitle(sTitle) {
  try {
    document.title = titleString+sTitle;
  } catch(e) {
    // oh well
  }
}

function launch(type,userLevel) {
  if (launchFlag>0) {
    playPreSound('launch','60');
    clicked = 1;
    arkanoidBody.style.display = 'block';
    arkanoidMainContainer.style.display = 'none';
    document.body.removeChild(arkanoidMainContainer);
    arkanoidMainContainer = null;
    centerContent();
    // document.body.style.cursor = "url('circle.cur')";
    document.body.className = 'active';
    interfaceInit();
    if (type == 'levelEditor') {
      setTimeout("startLevelEditor()",1500);
      levelBrowser = new LevelBrowser();
      levelBrowser.hide();
    } else if (type == 'levelBrowser') {
      var f1 = function() {levelBrowser = new LevelBrowser();}
      var f2 = function() {levelBrowser.show();}
      if (typeof userLevel != 'undefined') {
        f2 = function() {
          levelBrowser.hide(); // level=levelBrowser.hide();
          loadLevel(userLevel,1);
        }
      }
      f1();
      f2();
    } else {
      menu.setNewStateAllowed(0);
      setTimeout("menu.setNewStateAllowed(1)",5000);
      levelBrowser = new LevelBrowser();
      levelBrowser.hide();
      setTimeout("arkanoidInit()",1500);
    }
  }
}

// Arkanoid game engine code

var bBaseX = 32; // brick base X/Y coords
var bBaseY = 96;
var bOffX = 13; // offset from 32
var bOffY = 16; // offset from 32
var marginX = 18; // screen limitations
var marginY = 24;
var paddleHeight = 32;
var paddleWidth = 72;
var paddleHalfWidth = parseInt(paddleWidth/2);
var bodyX;
var bodyY;
var balls = [];
var bricks = [];
var map;
var capsules = [];
var thePaddle;
var portal0;
var portal1;
var warpEnabled = 0;
var warpActive = 0;
var warpSide = '';
var statusWindow;
var delay = navigator.appVersion.toLowerCase().indexOf('windows nt')>=0?20:2;
var DEBUG = 0;
// var NOSOUND = 0;
var player;
var activeBalls = 0;
var ballVXMax = 15;
var ballRespawnEnabled = 0; // flag for "N" capsule
var options = new OptionSet();
var powerup;
var lasers = [];
var caughtBalls = 0;
var portalActive = 0;
var pointMultiplier = 1;
var levelEditor;
var levelBrowser;
var stopGame = 0;
var gamePaused = 0;
var lN = []; // user-defined levels index
var saveResult = null;
var preventOnetimeHandler = 0;
var gameActive = 0;
var gameControls = null;
var ipsCounter = null;
var ips = 0;
var arkanoidBody = null;
var arkanoidBodyOffX = 0;
var arkanoidBodyOffY = 0;
var arkanoidMainContainer = null;
var highScores = null;
var help = null;
var menu = null;

/* brickTypes structure:
   ---------------------
   associative name
   color/image name (eg. grey.gif)
   # of hits
   point value
   respawn flag
   capsule (power-up) flag
*/

var brickTypeData = [];

brickTypeData['grey'] = ['grey','#C3C3C3',2,50,0,0];
brickTypeData['gold'] = ['gold','#D0A000',-1,500,0,0];
brickTypeData['funky'] = ['funky','#C3C3C3',2,50,1,0];
brickTypeData['red'] = ['red','#B00000',1,90,0,1];
brickTypeData['teal'] = ['teal','#F00000',1,70,0,1];
brickTypeData['blue'] = ['blue','#0090F0',1,100,0,1];
brickTypeData['yellow'] = ['yellow','#F0F000',1,120,0,1];
brickTypeData['green'] = ['green','#00B200',1,10,0,1];
brickTypeData['white'] = ['white','#F0F0F0',1,50,0,1];
brickTypeData['brown'] = ['brown','#F76100',1,10,0,1];
brickTypeData['magenta'] = ['magenta','#D641B5',1,110,0,1];

var brickTypes = ['grey','gold','funky','red','teal','blue','yellow','green','white','brown','magenta'];

var A = '10'; // brick type literal
var X = null;

function getRandomBrickType() {
  return brickTypes[parseInt(Math.random()*brickTypes.length)];
}

/* capsuleType structure:
   ----------------------
   associative name
   selection frequency (n/100)
   point value
*/

capsuleTypeData = [];

capsuleTypeData['disrupt'] = ['disrupt',10,100];
capsuleTypeData['catch'] = ['catch',10,100];
capsuleTypeData['twin'] = ['twin',10,100];
capsuleTypeData['node'] = ['node',10,100];
capsuleTypeData['laser'] = ['laser',10,100];
capsuleTypeData['extend'] = ['extend',10,100];
capsuleTypeData['restrict'] = ['restrict',10,100];
capsuleTypeData['slow'] = ['slow',10,100];
capsuleTypeData['ishadow'] = ['ishadow',10,100];
capsuleTypeData['super'] = ['super',3,100];
capsuleTypeData['mega'] = ['mega',3,100];
capsuleTypeData['player'] = ['player',2,100];
capsuleTypeData['bypass'] = ['bypass',2,100];

var capsuleTypes = ['disrupt','catch','twin','node','laser','extend','restrict','slow','ishadow','super','mega','player','bypass'];

var capsuleSections = [];
capsuleSections[0] = 1;
var capsuleTmp = 0;

for (i=0; i<capsuleTypes.length; i++) { // create random range reference grid
  capsuleTmp += capsuleTypeData[capsuleTypes[i]][1];
  capsuleSections[i+1] = capsuleTmp;
}
delete capsuleTmp;

function getRandomCapsuleType() {
  var rnd = parseInt(Math.random()*99);
  var tmp = 0;
  var cSection = parseInt(Math.floor(rnd));
  while (capsuleSections[tmp] - rnd < 0 && tmp < capsuleSections.length-1) {
    tmp++;
  }
  return capsuleTypes[tmp];
}

var level = 1;
var levelData = [];

levelData['test'] = [
  [X,X,X,X,X,1,1,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,1,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X],
  [X,X,X,X,X,1,X,1,X,X,X,X,X]
];

function getRnd(n) { 
  return parseInt(Math.random()*n)+1;
}

function getPlusMinus() {
  return parseInt(Math.random()*2) == 1?1:-1;
}

function nothing() { 
  return false;
}

Shadow.prototype.die = function() {
  if (this.o) {
    arkanoidBody.removeChild(this.o);
    this.o = null;
  }
}

Shadow.prototype.hide = function() {
  this.o.style.display = 'none';
}

Shadow.prototype.show = function() {
  this.o.style.display = 'block';
}

function Shadow(brickX,brickY) {
  this.o = document.createElement('div');
  this.o.className = 'shadow';
  this.o.style.left = (32*brickX)+brickX+14 - bOffX;
  this.o.style.top = (16*brickY)+brickY+13 - bOffY;
  if (brickX == 13) this.o.style.width = 16; // no shadow on last column
  arkanoidBody.appendChild(this.o);
}

Brick.prototype.reSpawn = function() {
  this.o.src = 'image/brick/'+this.type+'_respawn.gif';
  this.o.style.display = 'block';
  this.hits = brickTypeData[this.type][2];
  this.shadow.show();
}

Brick.prototype.die = function() {
  playSound('brick_die',30,1);
  if (player.score) player.score.addPoints(this.points);
  if (this.respawn != 1) {
    this.remove();
    if (this.capsule) this.capsule.activate();
    map.killBrick(this.col,this.row); // nullify brick reference in map (preventing future hit events)
  } else {
    this.hide();
    this.shadow.hide();
    setTimeout("brickRespawn("+this.col+","+this.row+")",3000);
  }
}

Brick.prototype.hide = function() {
  this.o.style.display = 'none';
}

Brick.prototype.remove = function() {
  arkanoidBody.removeChild(this.o);
  this.o = null;
  this.shadow.die();
  this.killMethods();
}

Brick.prototype.reset = function() {
  if (brickTypeData[this.type][2] != 1) {
    this.o.src = 'image/brick/'+this.type+'.gif';
    this.timer = 0;
  }
}

Brick.prototype.shimmer = function(noTimeout) {
  if (brickTypeData[this.type][2] != 1) {
    this.o.src = 'image/brick/'+this.type+'_1.gif';
    if (!this.timer && !noTimeout) this.timer = setTimeout("var t=map.brickExistsAt("+this.col+","+this.row+");if (t!=-1) bricks[t].reset()",1000);
  }
}

Brick.prototype.canBeHit = function() {
  return (this.o.style.display != 'none'?1:0);
}

Brick.prototype.registerHit = function() {
  this.shimmer();
  this.o.width = 36;
  this.o.height = 16;
  if (this.hits) {
    this.hits--;
    if (!this.hits) this.die();
    else playSound('brick_hit',30,1);
  }
}

Brick.prototype.killMethods = function() {
  this.reSpawn = nothing;
  this.die = nothing;
  this.hide = nothing;
  this.remove = nothing;
  this.reset = nothing;
  this.shimmer = nothing;
  this.canBeHit = nothing;
  this.registerHit = nothing;
}

function Brick(bCol,bRow,bTI) { // bTI: brickTypeIndex
  this.col = bCol;
  this.row = bRow;
  this.type = brickTypeData[bTI][0];
  this.bgColor = brickTypeData[bTI][1];
  this.hits = brickTypeData[bTI][2];
  this.points = brickTypeData[bTI][3];
  this.respawn = (brickTypeData[bTI][4] == 1?1:0);
  this.timer = 0;
  this.shadow = new Shadow(this.col,this.row);
  this.o = document.createElement('img');
  this.o.src = 'image/brick/'+this.type+'.gif';
  this.o.className = 'brick';
  this.o.style.backgroundColor = this.bgColor;
  this.o.style.width = 32;
  this.o.style.height = 16;
  this.o.style.left = parseInt(this.o.style.width) * this.col + this.col - bOffX;
  this.o.style.top = parseInt(this.o.style.height) * this.row + this.row - bOffY;
  this.o.onmouseover = cancelBubble;
  this.o.onmouseout = cancelBubble;
  if ((this.type == 0 || this.type == 2) && level >= 10) {
    this.hits = (level>=20?4:level>=10?3:this.hits);
  }
  arkanoidBody.appendChild(this.o);
  this.capsule = (brickTypeData[bTI][5] == 1 && getRnd(5)==2?new Capsule(parseInt(this.o.style.left),parseInt(this.o.style.top)):0);
  capsules[capsules.length] = this.capsule; // out of scope, but ah well
}

BrickMap.prototype.getBrickIndex = function(x,y) {
  return this.map[x][y];
}

BrickMap.prototype.killBrick = function(x,y) {
  if (bricks[this.map[x][y]].type != 'funky' && bricks[this.map[x][y]].type != 'gold') this.bricksKilled++;
  this.map[x][y] = -1;
  if (this.bricksKilled >= this.killableBricks) {
    killActiveBalls();
    // level is done
    showPortal();
  }
}

BrickMap.prototype.brickExistsAt = function(ballCol,ballRow) {
  return (this.map[ballCol] && this.map[ballCol][ballRow] != null)?this.map[ballCol][ballRow]:-1;
}

BrickMap.prototype.destroy = function() {
  for (var i=0; i<14; i++) {
    for (var j=0; j<22; j++) {
      if (this.brickExistsAt(i,j) != -1) bricks[this.brickExistsAt(i,j)].remove();
      this.map[i][j] = -1;
    }
  }
}

function BrickMap() {
  // 2D array of 13W x 20H
  // (429 pixels by 340 px)
  // ----------------------
  this.bricksKilled = 0;   // counter (level pass indicator)
  this.killableBricks = 0; // # of normal destroyable bricks
  this.map = [];           // map data array
  for (var i=0; i<20; i++) {
    this.map[i] = [];
    for (var j=0; j<13; j++) {
      this.map[i][j] = -1;
    }
  }
  for (i=0; i<bricks.length; i++) {
    this.map[bricks[i].col][bricks[i].row] = i; // store brick's index reference in map (eg. 3 = bricks[3]);
    if (bricks[i].type != 'funky' && bricks[i].type != 'gold') this.killableBricks++;
  }
}

PaddleShadow.prototype.animate = function() {
  var diffX = this.history[0] - thePaddle.x;
  this.setLeft(diffX>50?thePaddle.x+50:diffX<-50?thePaddle.x-50:this.history[0]);
}

PaddleShadow.prototype.activate = function() {
  this.reset();
  this.setLeft(thePaddle.x);
  this.o.style.display = 'block';
  this.active = 1;
}

PaddleShadow.prototype.deactivate = function() {
  this.o.style.display = 'none';
  this.active = 0;
}

PaddleShadow.prototype.reset = function() {
  for (var i=0; i<6; i++) {
    this.history[i] = 0;
  }
}

PaddleShadow.prototype.setLeft = function(x) {
  this.o.style.left = x;
  this.x = x;
}

PaddleShadow.prototype.setTop = function(y) {
  this.o.style.top = y;
  this.y = y;
}

PaddleShadow.prototype.writeHistory = function() {
  this.history[6] = thePaddle.x;
  for (var i=0; i<6; i++) {
    this.history[i] = this.history[i+1];
  }
}

function PaddleShadow(x,y) {
  this.active = 0;
  this.x = x;
  this.y = y;
  this.o = document.createElement('img');
  this.o.style.position = 'absolute';
  this.o.style.display = 'none';
  this.o.style.left = this.x;
  this.o.style.top = this.y;
  this.o.src = 'image/paddle_shadow.gif';
  this.o.width = 72;
  this.o.height = 20;
  this.history = [0,0,0,0,0,0];
  this.reset();
  arkanoidBody.appendChild(this.o);
}

Paddle.prototype.at = function(ballX, ballWidth) { // test if paddle is underneath ball
  var diffX = ballX - this.x;
  if (this.iShadow.active) return ((ballX >= this.x-8 && ballX <= this.x + paddleWidth+8) || (ballX >= this.iShadow.x-8 && ballX <= this.iShadow.x + paddleWidth+8)?1:0)
  else if (powerup.currentEffect != 'twin') return (ballX >= this.x-8 && ballX <= this.x + paddleWidth+8?1:0);
  else return (ballX >= this.x-8 && (diffX < 57 || diffX > 72) && ballX <= this.x + paddleWidth+8?1:0);
}

Paddle.prototype.registerHit = function() {
  playSound('player_hit',30,1);
  if (!this.frame || (this.frame && this.frameDirection < 0)) this.frameDirection = 4; // if hit while neutral or going "down", start going up at 3x rate
}

Paddle.prototype.reset = function() {
  this.setType();
}

Paddle.prototype.setLeft = function(x) {
  this.o.style.left = x+'px';
  this.x = x;
}

Paddle.prototype.setTop = function(y) {
  this.o.style.top = y;
  this.y = y;
}

Paddle.prototype.animate = function() {
  if (!this.type) {
    if (this.frame != 0 || this.frameDirection != 0) {
      this.frame += this.frameDirection;
      if (this.frame >= 10) {
        this.frame = 10;
        this.frameDirection = -1;
      }
      else if (this.frame <= 0 && this.frameDirection < 0) {
        this.frame = 0;
        this.frameDirection = 0;
      }
      this.setFrame(this.frame);
    }
  }
  if (powerup.currentEffect == 'iShadow') {
    this.iShadow.writeHistory();
    this.iShadow.animate();
  }
  if (warpActive && warpEnabled) this.warp();
}

Paddle.prototype.setFrame = function() {
  this.o.src = 'image/' + this.frame + '.gif';
}

Paddle.prototype.warp = function() {
  if (warpEnabled) {
    killActiveBalls();
    if (warpSide == 'l') {
      this.setLeft(parseInt(this.o.style.left) - 2);
      if (this.x < 0-paddleWidth) {
        doWarp();
      }
    } else {
      this.setLeft(this.x + 2);
      if (this.x > screenX) {
        doWarp();
      }
    }
  }
}

Paddle.prototype.createBall = function() {
  // create ball "stuck" to paddle (start)
  balls[balls.length] = new Ball(this.x+42,this.y-12,3,-5);
  caughtBalls = 1;
  var b = balls[balls.length-1];
  b.catchEnabled = 1;
  b.active = 0;
  b.caught = 1;
  b.offX = b.x - this.x;
}

Paddle.prototype.explode = function() {
  killGameHandlers();
  this.o.src = 'image/paddle_explode.gif';
}

Paddle.prototype.reSpawn = function() {
  this.center();
  this.show();
  this.o.src = 'image/paddle_unexplode.gif';
  setTimeout("thePaddle.reSpawnFinish();thePaddle.createBall();setOnetimeHandler()",1000);
}

Paddle.prototype.reSpawnFinish = function() {
  this.o.src = 'image/paddle.gif';
  setMouseMove();
}

Paddle.prototype.center = function() {
  this.setLeft(parseInt(screenX/2)-paddleHalfWidth);
}

Paddle.prototype.vAlign = function(offset) {
  this.setTop(parseInt(screenY-paddleHeight)-(offset?offset:0));
}

Paddle.prototype.show = function() {
  this.o.style.display = 'block';
}

Paddle.prototype.hide = function() {
  this.o.style.display = 'none';
}

Paddle.prototype.setType = function(t) {
  this.oldWidth = paddleWidth;
  this.type = (t?t:null);
  this.o.src = 'image/paddle'+(this.type?'_'+this.type:'')+'.gif';
  if (this.type == 'laser') {
    if (this.height != 23) {
      this.o.style.height = 23;
      this.vAlign(3); // offset vertical by 3px to mask change in height
    }
  } else {
    this.o.style.height = 20;
    this.vAlign(0);
  }
  paddleWidth = this.o.style.width = (this.type?this.types[this.type]:72);
  paddleHalfWidth = parseInt(paddleWidth/2);
  if (paddleWidth != this.oldWidth) this.o.style.left = parseInt(this.o.style.left)-(parseInt(paddleWidth-this.oldWidth)/2);
}

function Paddle() {
  this.type = null;
  this.types = [];
  this.types['extend'] = 96;
  this.types['restrict'] = 48;
  this.types['laser'] = 72;
  this.types['twin'] = 135;
  this.oldWidth = 0;
  this.o = document.createElement('img');
  this.o.id = 'usrPaddle';
  this.o.style.position = 'absolute';
  this.o.style.display = 'none';
  this.o.src = 'image/paddle_blank.gif';
  this.o.style.zIndex = 3;
  this.x = 0;
  this.y = 0;
  this.center();
  this.vAlign();
  this.o.ondragstart = function() {event.cancelBubble=true;return false}
  arkanoidBody.appendChild(this.o);
  this.iShadow = new PaddleShadow(this.x,this.y)
  this.frame = 0;
  this.frameDirection = 0;
  if (isIphone) {
    this.iphoneTarget = document.createElement('div');
    this.iphoneTarget.href = '#';
    this.iphoneTarget.id = 'iphoneTarget';
    this.iphoneTarget.onclick = mmH;
    arkanoidBody.appendChild(this.iphoneTarget);
  }
}

Ball.prototype.init = function() {
  this.o.style.left = this.x;
  this.o.style.top = this.y;
  this.o.style.display = 'block';
  this.active = 1;
  activeBalls++;
}

Ball.prototype.setLeft = function(x) {
  this.o.style.left = x;
  this.x = x;
}

Ball.prototype.setTop = function(y) {
  this.o.style.top = y;
  this.y = y;
}

Ball.prototype.move = function() {
  var newX = this.x + this.vX;
  var newY = this.y + this.vY;
  var inPaddleArea = 0;
  if (newX > marginX && newX < screenX - this.width - marginX) this.setLeft(newX);
  else if (newX < marginX && this.vX < 0) {
    this.setLeft(marginX + 1);
    this.bounce('x');
  }
  else if (newX >= screenX - this.width - marginX && this.vX > 0) {
    this.setLeft(screenX - this.width - marginX - 1);
    this.bounce('x');
  }
  if (newY > marginY && newY < screenY - paddleHeight - this.height + 1) this.setTop(newY);
  else if (newY <= marginY && this.vY < 0) {
    this.setTop(marginY + 1);
    this.bounce('y');
  }
  if (newY >= screenY - paddleHeight - this.height + 1 && this.vY > 0) {
    if (newY < screenY - paddleHeight + 16) { // if within paddle y hit range
      if (thePaddle.at(newX,this.width) && !this.dying) { // .. and paddle is underneath..
        if (this.x < screenY - paddleHeight - this.height + 1) this.setTop(this.y = screenY - paddleHeight - this.height + 1);
        this.bounce('y');
        thePaddle.registerHit();
        var v = this.x + 8 - thePaddle.x - paddleHalfWidth;
        if (v >= -12 && (v <= 12)) this.vX = v/10;
        else this.vX = v/4;
        this.vX = (this.vX<-10?-10:this.vX>10?10:this.vX);
        if (this.catchEnabled) {
          // ball "caught"
          caughtBalls++;
          this.caught = 1;
          this.active = 0;
          this.offX = this.x - thePaddle.x; // ball position relative to paddle
        }
      }
      else this.setTop(newY); // move down, not dead yet
    }
    else this.die();
  }
}

Ball.prototype.bounce = function(axis) {
  if (axis == 'x') {
    this.vX *= -1;
    this.speedUpX();
  } else if (axis == 'y') {
    this.vY *= -1;
    this.speedUpY();
  } else {
    this.vX *= -1;
    this.speedUpX();
    this.vY *= -1;
    this.speedUpY();
  }
}

Ball.prototype.speedUpX = function() {
  if (this.vX < 20) this.vX += (0.01*(this.vX>0?1:-1));
}

Ball.prototype.speedUpY = function() {
  if (this.vY < 15) this.vY += (0.01*(this.vY>0?1:-1));
}

Ball.prototype.getCol = function(x) {
  return Math.floor((x+bOffX)/33);
}

Ball.prototype.getRow = function(y) {
  return Math.floor((y+bOffY)/17);
}

Ball.prototype.collisionCheck = function() {
  var col = this.getCol(this.x);
  var row = this.getRow(this.y);
  var i = map.brickExistsAt(col,row);
  if (i == -1) { // nothing at current location, try next
    col = this.getCol(this.x+this.vX);
    row = this.getRow(this.y+this.vY);
    i = map.brickExistsAt(col,row);
  }
  if (i != -1 && this.type == 'm') { // if ball is in/on brick, and m type
    bricks[i].die();
    this.speedUpX();
    this.speedUpY();
  }
  else if (i != -1 && bricks[i].canBeHit()) { // if ball is in/on a brick area
    bricks[i].registerHit();
    // --------
    // what side did it hit? .. determine x, y, xy bounce
    // This logic needs to be revamped.
    //                                          --------
    var diffX = ((col*33) - bOffX - this.x); // determine offset from brick side
    var diffY = Math.abs((row*17) - bOffY - this.y); // offset from top
    if ((diffX < -33 || diffX > 0) && (this.vX<0?map.brickExistsAt(col+1,row) == -1:map.brickExistsAt(col-1,row) == -1) ) { // bounce on open edges only
      var diffXY = Math.abs(Math.abs(this.vX)-Math.abs(this.vY));
      if (diffXY >= 1) this.bounce('x');
      else { // bounce on both axes
        this.vX += Math.random()*(this.vX>0?1:-1);
        this.bounce();
      }
    }
    else this.bounce('y');
  }
}

Ball.prototype.setType = function(t) {
  this.type = t;
  this.o.src = 'image/ball'+(t?'_'+t:'')+'.gif';
}

Ball.prototype.remove = function() {
  if (this.o) {
    arkanoidBody.removeChild(this.o);
    this.o = null;
    this.move = function() {return false}
  }
  this.active = 0;
  this.caught = 0;
  this.catchEnabled = 0;
  this.dying = 0;
}

Ball.prototype.die = function() {
  if (this.y <= screenY) {
    this.dying = 1;
    this.catchEnabled = 0;
    this.x += this.vX;
    this.y += this.vY;
    this.o.style.left = this.x;
    this.o.style.top = this.y;
  } else if (this.active || this.catchEnabled || this.caught) {
    this.remove();
    activeBalls--;
    if (!activeBalls && !portalActive) player.die();
    else if (activeBalls < 3 && ballRespawnEnabled) powerup.apply();
  }
}

function Ball(x,y,vX,vY,type) {
  this.active = 0;
  this.dying = 0;
  this.width = 12;
  this.height = 13;
  this.o = document.createElement('img');
  this.o.src = 'image/ball.gif';
  this.o.className = 'ball';
  this.o.style.width = this.width;
  this.o.style.height = this.height;
  this.o.onmouseover = cancelBubble;
  this.o.onmouseout = cancelBubble;
  this.offX = 0;
  this.x = parseInt(x);
  this.y = parseInt(y);
  this.vX = vX;
  this.vY = vY;
  this.type = (type?type:'');
  arkanoidBody.appendChild(this.o);
  this.init();
}

Capsule.prototype.setFrame = function() {
  this.frame++;
  if (this.frame>=this.frameSequence.length) this.frame = 0;
  this.o.src = 'image/capsule/'+this.typeBase+this.frameSequence[this.frame]+'.gif';
}

Capsule.prototype.setLeft = function(x) {
  this.o.style.left = x;
  this.x = x;
}

Capsule.prototype.setTop = function(y) {
  this.o.style.top = y;
  this.y = y;
}

Capsule.prototype.animate = function() {
  newY = parseInt(this.o.style.top) + 4;
  this.setTop(newY);
  this.setFrame();
  if (newY >= screenY - paddleHeight - this.height && newY < screenY - paddleHeight) { // within paddle Y area
    if (thePaddle.at(this.x,this.width) && !this.activated && !warpEnabled) {
      this.activated = 1; // "used" flag
      this.applyEffect();
      this.remove();
    }
  }
  else if (newY > screenY) this.remove();
  // if at paddle, disappear and activate
  // if beyond paddle, go into "remission"
}

Capsule.prototype.activate = function() {
  this.active = 1;
  this.width = 32;
  this.height = 16;
  this.o = document.createElement('img');
  this.o.style.position = 'absolute';
  this.o.style.width = this.width;
  this.o.style.height = this.height;
  this.o.style.left = this.x;
  this.o.style.top = this.y;
  this.o.style.zIndex = 3;
  this.setFrame();
  arkanoidBody.appendChild(this.o);
  if (capsuleNOSYNC) {
    this.o.src = 'image/capsule/'+this.typeBase+'.gif';
  }
  this.animate();
}

Capsule.prototype.remove = function() {
  this.active = 0;
  arkanoidBody.removeChild(this.o);
}

Capsule.prototype.applyEffect = function() {
  // eg. set catch, laser, additional balls etc
  // re-referenced to methods below
}

Capsule.prototype.disrupt = function() {
  powerup.setCurrentEffect('disrupt');
  var oBall = getHighestBall(); // get ball object
  if (!oBall) return false; // potential error condition? - effect cannot be applied.
  for (var i=0; i<7; i++) {
    balls[balls.length] = new Ball(oBall.x,oBall.y,oBall.vX+(0.75*(i+1)),oBall.vY,0);
  }
}

Capsule.prototype.cCatch = function() {
  powerup.setCurrentEffect('cCatch');
  for (var i=0; i<balls.length; i++) {
    if (balls[i].active) balls[i].catchEnabled = 1;
  }
  document.onmousedown = powerup.cCatch;
}

Capsule.prototype.twin = function() {
  powerup.setCurrentEffect('twin');
  thePaddle.setType('twin');
}

Capsule.prototype.node = function() {
  powerup.setCurrentEffect('node');
  ballRespawnEnabled = 1;
  powerup.apply();
}

Capsule.prototype.laser = function() {
  powerup.setCurrentEffect('laser');
  thePaddle.setType('laser');
  document.onmousedown = function(e) {
	if (e) {
	  event=e;
	}
	powerup.laser(event.clientX-8);
	if (e && typeof e.stopPropagation != 'undefined') {
	  e.stopPropagation = true;
	} else if (event) {
	  // e.preventDefault();
          event.cancelBubble = true;
	}
	return false;
  }
}

Capsule.prototype.cExtend = function() {
  powerup.setCurrentEffect('extend');
  playSound('player_extend',30,1);
  thePaddle.setType('extend');
}

Capsule.prototype.restrict = function() {
  powerup.setCurrentEffect('restrict');
  playSound('player_restrict',30,1);
  thePaddle.setType('restrict');
  pointMultiplier = 2;
}

Capsule.prototype.slow = function() {
  powerup.setCurrentEffect('slow');
  for (var i=0; i<balls.length; i++) {
    if (balls[i].active) {
      balls[i].vX *= (balls[i].vX>5||balls[i].vX<-5?2/3:1);
      balls[i].vY *= (balls[i].vY>5||balls[i].vY<-5?2/3:1);
    }
  }
}

Capsule.prototype.iShadow = function() {
  powerup.setCurrentEffect('iShadow');
  thePaddle.iShadow.activate();
}

Capsule.prototype.cSuper = function() {
  powerup.setCurrentEffect('sSuper');
}

Capsule.prototype.mega = function() {
  powerup.setCurrentEffect('mega');
  for (var i=0; i<balls.length; i++) {
    if (balls[i].active) balls[i].setType('m');
  }
}

Capsule.prototype.player = function() {
  powerup.setCurrentEffect('player');
  player.lives.plusOne();
  player.lives.update();
}

Capsule.prototype.bypass = function() {
  powerup.setCurrentEffect('bypass');
  showPortal();
}

function Capsule(x,y,type) {
  this.active = 0;
  this.activated = 0;
  this.type = (!type?getRandomCapsuleType():type);
  this.typeBase = this.type.charAt(0);
  this.frame = 0;
  this.frameSequence = [0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,7,7,7,7,7,7,7,7,7,7,8,8,9,9,10,10,11,11,12,12,13,13];
  this.x = x;
  this.y = y;
  this.o = null;
  if (this.typeBase == 'd') this.applyEffect = this.disrupt;
  else if (this.typeBase == 'c') this.applyEffect = this.cCatch;
  else if (this.typeBase == 't') this.applyEffect = this.twin;
  else if (this.typeBase == 'n') this.applyEffect = this.node;
  else if (this.typeBase == 'l') this.applyEffect = this.laser;
  else if (this.typeBase == 'e') this.applyEffect = this.cExtend;
  else if (this.typeBase == 'r') this.applyEffect = this.restrict;
  else if (this.typeBase == 's') this.applyEffect = this.slow;
  else if (this.typeBase == 'i') this.applyEffect = this.iShadow;
  else if (this.typeBase == 's') this.applyEffect = this.cSuper;
  else if (this.typeBase == 'm') this.applyEffect = this.mega;
  else if (this.typeBase == 'p') this.applyEffect = this.player;
  else if (this.typeBase == 'b') this.applyEffect = this.bypass;
  if (capsuleNOSYNC) { // Save extra 120+ images at expense of capsule spin/falling synchronization
    this.setFrame = function() {}
  }
}

// powerup methods - for effects that are applied more than once

Powerup.prototype.apply = function() {} // reference method

Powerup.prototype.cSuper = function() {}

Powerup.prototype.cCatch = function() {
  for (var i=0; i<balls.length; i++) {
    // fire all caught balls
    if (balls[i].caught == 1) {
      balls[i].caught = 0;
      balls[i].active = 1;
      caughtBalls--;
    }
  }
}

Powerup.prototype.node = function() {
  var oBall = getHighestBall();
  var i = 1;
  if (!oBall.dying) {
    while (activeBalls < 3) {
      balls[balls.length] = new Ball(oBall.x,oBall.y+(i*2),oBall.vX+(1.5*(i)),oBall.vY);
      i++;
    }
  }
}

Powerup.prototype.laser = function(x) {
  playSound('player_laser',30,1)
  lasers[lasers.length] = new LaserFire(x-arkanoidBodyOffX);
}

Powerup.prototype.reset = function() {
  // kill powerup-specific flags, etc.
  this.apply = nothing;
  ballRespawnEnabled = 0;
  if (this.currentEffect == 'iShadow') thePaddle.iShadow.deactivate();
  else if (this.currentEffect == 'cCatch') {
    document.onmousedown = '';
    for (var i=0; i<balls.length; i++) {
      if (balls[i].catchEnabled || balls[i].caught) {
        balls[i].caught = 0;
        balls[i].catchEnabled = 0;
        balls[i].active = 1;
      }
    }
  }
  else if (this.currentEffect == 'restrict') {
    pointMultiplier = 1;
    thePaddle.setType();
  }
  else if (this.currentEffect == 'extend' || this.currentEffect == 'twin') thePaddle.setType();
  else if (this.currentEffect == 'laser') {
    thePaddle.setType();
    // document.onclick = '';
    document.onmousedown = null;
  }
  else if (this.currentEffect == 'mega') { // reset balls to normal
    for (var i=0; i<balls.length; i++) {
      if (balls[i].active) balls[i].setType('');
    }
  }
  this.currentEffect = null;
}

Powerup.prototype.setCurrentEffect = function(e) {
  this.reset();
  this.lastEffect = this.currentEffect;
  this.currentEffect = e;
  if (e == 'cCatch' || e == 'node' || e == 'laser' || e == 'cSuper') this.apply = eval('powerup.'+this.currentEffect); // point .apply() to effect method
}

function Powerup() {
  // powerup object - methods for powerups applied after capsule obtained
  this.currentEffect = null;
  this.lastEffect = null;
}

PlayerScore.prototype.addPoints = function(p) {
  this.points += p * pointMultiplier;
  this.update();
}

PlayerScore.prototype.update = function() {
  this.s.innerHTML = this.points;
}

PlayerScore.prototype.updateAndCheck = function() {
  delete hsNames;
  delete hsScores;
  jsLoad(baseHref+'highscores.js','setTimeout("player.score.checkHighScores()",500)');
}

PlayerScore.prototype.checkHighScores = function() {
  var html = '';
  if (level >= 33)
    html = '<span class="boldText">GAME OVER</span><br />THANKS FOR PLAYING!<br /><span class="smallText">Need more? Try the user-submitted levels under the Level Browser.</span><br />';
  else 
    html += '<span class="boldText">GAME OVER</span><br />';
  html += '<span class="smallText">Your score: ' + player.score.points + ' points</span><br />';
  var btnOKNewGame = '<img src="image/error_ok.gif" onclick="statusWindow.hide();newGame()" class="button" style="margin-top:3px" />';
  this.hsFlag = 0;
  if (!hsScores) {
    window.status = 'player.score.checkHighScores(): high score data undefined.';
    return false;
  }
  if (!hsScores.length) this.hsFlag = 1; // set flag if array empty
  for (this.i=0; this.i<hsScores.length && !this.hsFlag; this.i++) {
    if (this.points > hsScores[this.i] || hsScores.length<10) this.hsFlag = 1;
  }
  if (this.hsFlag == 1) {
    html += '<b class="red">New highscore!</b><br />Enter your name<br /><span class="vAlign"><input id="newHsName" type="text" maxlength="32" />&nbsp;<img src="image/error_ok.gif" onclick="player.score.hsName=document.getElementById(\'newHsName\').value;player.score.submitScore()" class="button" /></span>';
  } else {
    html += btnOKNewGame;
  }
  setTimeout("playSound('game_over')",1000);
  statusWindow.setWindowedMessage(html);
}

PlayerScore.prototype.submitScore = function() {
  jsLoad(baseHref+'hs.php?s='+this.points+'&n='+(this.hsName?this.hsName:'Anonymous'),'highScores.display(\'newGame\')');
  statusWindow.hide();
}

function PlayerScore() {
  this.hsName = null;
  this.points = 0;
  this.o = document.getElementById('pScore');
  if (!this.o) {
    this.o = document.createElement('div');
    with (this.o.style) {
      position = 'absolute';
      left = 112;
      top = 1;
      width = 250;
    }
    this.o.className = 'vAlign';
    this.o.innerHTML = '<span class="boldText" style="color:#555555">DHTML Arkanoid V1.2 Beta</span><span style="line-height:10px;">&nbsp; | &nbsp;Score:</span></span> <span id="s-score"></span>';
    document.getElementById('topBar').appendChild(this.o);
    // this.s = document.createElement('span');
    this.s = document.getElementById('s-score');
    this.s.innerHTML = this.points;
    // this.o.appendChild(this.s);
  }
}

HighScores.prototype.display = function(html) {
  this.tmp = '';
  this.titles = ['1st','2nd','3rd','4th','5th','6th','7th','8th','9th','10th'];
  if (html == 'newGame') {
    html = '<img src="image/new_game.gif" onclick="newGame()" alt="Start arcade game" title="Start arcade game" class="button" />';
  }
  this.tmp += '<table border="0" cellpadding="0" cellspacing="0">';
  for (this.i=0; this.i<hsNames.length; this.i++) {
    this.tmp += '<tr>';
    this.tmp += '<td><img src="image/highscores/'+(this.i+1)+'.gif" alt="'+this.titles[this.i]+'" width="32" height="24" style="margin-bottom:8px;margin-right:5px" /></td>';
    this.tmp += '<td valign="center"><span class="boldText">' + hsNames[this.i] + '</span><br />';
    this.tmp += '<span class="highscoreText grey" style="font-size:'+this.fontSize+'px;margin-bottom:2px">' + hsScores[this.i] + '</span></td>';
    this.tmp += '</tr>';
  }
  this.tmp += '</table>';
  this.content.innerHTML = this.tmp + (html?'<br />'+html:'') + '<div class="grey" style="position:absolute;left:5px;bottom:3px">Note: Highscores apply to Arcade Game scores only.</div>';
  this.o.style.display = 'block';
}

HighScores.prototype.hide = function(ignorePause) {
  this.o.style.display = 'none';
  resumeGame(ignorePause);
}

function HighScores() {
  this.tmp = '';
  this.fontMax = 15;
  this.fontDiff = 5;
  this.fontSize = 9;
  this.o = document.createElement('div');
  with (this.o.style) {
    backgroundColor = '#ffffff';
    border = '1px solid #999999';
    display = 'none';
    position = 'absolute';
    left = 34;
    top = 32;
    width = (ie?402:403);
    height = (ie?349:365);
    zIndex = 5; // force to top
  }
  this.header = document.createElement('div');
  with (this.header.style) {
    width = (ie?'100%':403);
    height = (ie?16:15);
    padding = (ie?5:0);
    backgroundColor = '#eeeeee';
    borderBottom = '1px solid #999999';
  }
  this.header.innerHTML = '<div style="position:absolute;left:4px;top:1px" class="vAlign"><span class="boldText">HIGHSCORES</span> | TOP 10</div>';
  this.imgClose = document.createElement('img');
  with (this.imgClose.style) {
    position = 'absolute';
    top = 3;
    right = 4;
    zIndex = 10;
  }
  this.imgClose.className = 'button';
  this.content = document.createElement('div');
  this.content.className = 'windowContainer';
  with (this.content.style) {
    if (ie) filter = 'alpha(opacity=95)';
  }
  this.imgClose.src = 'image/close.gif';
  this.imgClose.title = 'Close';
  this.imgClose.onclick = function() {highScores.hide()}
  this.o.appendChild(this.header);
  this.header.appendChild(this.imgClose);
  this.o.appendChild(this.content);
  arkanoidBody.appendChild(this.o);
  jsLoad(baseHref+'highscores.js');
}

GameControls.prototype.show = function() {
  for (i=0; i<this.buttons.length; i++) {
    this.buttonReset(this.buttons[i]);
    this.buttonShow(this.buttons[i]);
  }
}

GameControls.prototype.hide = function() {
  for (i=0; i<this.buttons.length; i++) {
    this.buttonHide(this.buttons[i]);
  }
}

GameControls.prototype.reset = function() {
  for (i=0; i<this.buttons.length; i++) {
    this.buttonReset(this.buttons[i]);
  }
}

GameControls.prototype.buttonShow = function(o) {
  if (o.style.display != 'block') o.style.display = 'block';
}

GameControls.prototype.buttonHide = function(o) {
  if (o.style.display != 'none') o.style.display = 'none';
}

GameControls.prototype.buttonReset = function(o) {
  if (o.buttonType == 'play') this.click(o);
  this.mouse(o,0);
  this.buttonShow(o);
  // for pause/play button
}

GameControls.prototype.mouse = function(o,state) {
  o.buttonState = state;
  o.src = 'image/btn_'+o.buttonType+'_'+o.buttonState+'.gif';
}

GameControls.prototype.click = function(o) {
  if (o.buttonType == 'pause' && gameActive) {
    o.buttonType = 'play';
    stopGame = 1;
    gamePaused = 1;
  } else if (o.buttonType == 'play' && gameActive) {
    o.buttonType = 'pause';
    stopGame = 0;
    gamePaused = 0;
    mainLoop();
  }
  o.buttonState = 1;
  o.src = 'image/btn_'+o.buttonType+'_'+o.buttonState+'.gif';
  o.title = o.alt = (o.buttonType == 'play'?'Resume':'Pause');
}

function GameControls() {
  // create game pause / level exit buttons
  this.buttons = [];
  this.buttons[0] = document.createElement('img');
  with (this.buttons[0].style) {
    position = 'absolute';
    left = 2;
    top = 481;
  }
  this.buttons[0].buttonType = 'pause';
  this.buttons[0].buttonState = 0;
  with (this.buttons[0]) {
    title = 'Click to pause';
    className = 'button';
  }
  // would throw this code in above block, but "this" reference fails within "with (this.buttons[0])" in Mozilla
  this.buttons[0].onmouseover = function() {gameControls.mouse(this,1)}
  this.buttons[0].onmouseout = function() {gameControls.mouse(this,0)}
  this.buttons[0].onclick = function() {gameControls.click(this)}
  this.mouse(this.buttons[0],0);
  arkanoidBody.appendChild(this.buttons[0]);
}

IPSCounter.prototype.cancel = function() {
  if (this.timeout) clearTimeout(this.timeout);
  this.ips.innerHTML = 'n/a';
}

IPSCounter.prototype.iterate = function(override) {
  ips = 0;
  if (override && !this.active) this.active = 1;
  if (this.active) {
    if (gameActive || override) this.timeout = setTimeout("ipsCounter.update()",1000);
  }
}

IPSCounter.prototype.toggle = function() {
  this.active = (!this.active?1:0);
  if (!this.active)
    this.cancel();
  else {
    this.ips.innerHTML = '?';
    this.iterate(1);
  }
}
  
IPSCounter.prototype.update = function() {
  // reset ipsCount, display ips
  this.ips.innerHTML = ips;
  this.iterate();
}

function IPSCounter() {
  this.o = document.createElement('div');
  this.active = 0;
  this.timeout = null;
  with (this.o.style) {
    position = 'absolute';
    left = 340;
    top = 2;
  }
  this.o.title = 'Iterations (loops) Per Second\nRelative to frames depending on CPU load\nClick to toggle.';
  this.o.className = 'button';
  this.o.onclick = function() {ipsCounter.toggle();}
  this.o.innerHTML = 'IPS: ';
  document.getElementById('topBar').appendChild(this.o);
  this.ips = document.createElement('div');
  this.ips.innerHTML = 'n/a';
  with (this.ips.style) {
    position = 'absolute';
    left = 20;
    top = 0;
    width = 32;
  }
  this.o.appendChild(this.ips);
  this.o.style.display = 'none';
}

LifeObject.prototype.update = function() {
  var tmpHTML = '';
  for (var i=0; i<this.lR; i++) {
    tmpHTML += '<img src="image/life_icon.gif" alt="" border="0" '+(i>0?'style="margin-left:3px" ':'')+'/>';
  }
  this.o.innerHTML = tmpHTML;
}

LifeObject.prototype.minusOne = function() {
  this.lR--;
}

LifeObject.prototype.plusOne = function() {
  this.lR++;
}

function LifeObject() {
  // player life object - tracks # of lives and displays life "icons"
  this.lR = 3; // lives remaining
  this.o = document.createElement('div');
  this.o.style.position = 'absolute';
  this.o.style.left = 25;
  this.o.style.top = screenY - 10;
  arkanoidBody.appendChild(this.o);
  this.update();
}

Player.prototype.die = function() {
  killActiveCapsules();
  killActiveLasers();
  powerup.reset();
  thePaddle.reset();
  playSound('player_die',40);
  thePaddle.explode();
  this.lives.minusOne();
  this.lives.update();
  if (this.lives.lR <= 0) {
    if ((''+level) == 'test' || (''+level).indexOf('usr')>=0)
      setTimeout("doWarp()",1000);
    else {
      gameOver();
    }
  } else {
    // start next life
    setTimeout("player.reSpawn()",2000);
  }
}

Player.prototype.reSpawn = function() {
  playSound('round_start',30);
  statusWindow.showCurrentLevel();
  setTimeout("thePaddle.reSpawn()",500);
  setTimeout("levelShimmer()",1000);
  setTimeout("setGameHandlers();statusWindow.hide()",2000);
}

function Player() {
  this.name = null;
  this.score = new PlayerScore();
  this.lives = new LifeObject();
}

LaserFire.prototype.die = function() {
  this.active = 0;
  arkanoidBody.removeChild(this.o);
}

LaserFire.prototype.animate = function() {
  this.y += this.vY;
  if (this.y > marginY) this.o.style.top = this.y;
  else {
    this.die();
    return false;
  }
  var col = this.getCol(this.x);
  var row = this.getRow(this.y);
  var i = map.brickExistsAt(col,row);
  if (i != -1) i = map.brickExistsAt(col,this.getRow(this.y+parseInt(this.vY/2)));
  if (i != -1 && bricks[i].canBeHit()) {
    bricks[i].registerHit();
    this.die();
  }
}

LaserFire.prototype.getCol = function(x) {
  return Math.floor((x+bOffX)/33);
}

LaserFire.prototype.getRow = function(y) {
  return Math.floor((y+bOffY)/17);
}

function LaserFire(x) {
  this.active = 1;
  this.col = null;
  this.row = null;
  this.x = (!x?thePaddle.x + 28:x);
  this.y = thePaddle.y - 12;
  this.vY = -10;
  this.o = document.createElement('img');
  this.o.style.position = 'absolute';
  this.o.style.left = this.x;
  this.o.style.top = this.y;
  this.o.style.zIndex = 3;
  this.o.src = 'image/laser.gif';
  this.o.style.width = 16;
  this.o.style.height = 9;
  arkanoidBody.appendChild(this.o);
}

StatusWindow.prototype.showCurrentLevel = function() {
  var levelTitle = 'Level ' + ((''+level).indexOf('usr')>=0?(parseInt(level.substr(3,level.length-3))+1)+': '+lN[level.substr(3,level.length-3)]:level);
  this.setMessage('<span class="boldText">DHTML ARKANOID</span><br /><span class="smallText">' + levelTitle + '</span>');
  setTitle(levelTitle);
}

StatusWindow.prototype.errorOK = function(html) {
  this.hide(1);
  if (stopGame)
    setTimeout("startMainLoop();killActiveBalls();doWarp()",500);
}

// Bad form, bad form! :) - should be all DOM.. intend to fix.
var errContainer = '<div id="errorContainer" style="display:block;border:1px solid #cccccc;color:#333333" class="smallText"><img src="image/error_title.gif" alt="" border="0" /><br />';

StatusWindow.prototype.setMessage = function(html,isErrorMessage) {
  if (this.statusType != 'error' || (this.statusType == 'error' && isErrorMessage)) {
    this.status.innerHTML = html;
    this.show();
  }
}

StatusWindow.prototype.setWindowedMessage = function(html) {
  var n = errContainer;
  n += '<div style="display:block;margin-left:5px;margin-bottom:5px">'+html+'</div>';
  n += '</div>';
  this.setMessage(n);
}  

StatusWindow.prototype.setWarning = function(html,stopFlag,exitLevelFlag) {
  this.statusType = 'error'; // make sure message is seen
  if (stopFlag)
    stopGame = 1;
  var n = errContainer;
  n += '<div style="margin-left:5px">'+html+'</div>';
  n += '<div style="display:block;margin-left:3px;margin-top:6px;margin-bottom:3px" class="vAlign"><img src="image/error_ok.gif" border="0" alt="" onclick="statusWindow.errorOK()'+(exitLevelFlag?';exitLevel()':'')+'" class="button" style="margin-right:3px" /></div>';
  n += '</div>';
  this.setMessage(n,1);
}

StatusWindow.prototype.errorHandler = function(msg,url,line) {
  this.statusType = 'error';
  stopGame = 1;
  var n = errContainer;
  n += '<div style="margin-left:5px;width:28px">Error</div>:&nbsp;&nbsp;'+msg+'<br />';
  // n += '<div style="margin-left:5px;width:28px">File</div>:&nbsp;&nbsp;'+url+'<br />';
  n += '<div style="margin-left:5px;width:28px">Line</div>:&nbsp;&nbsp;'+line+'<br />';
  n += '<div style="display:block;margin-left:3px;margin-top:6px;margin-bottom:3px" class="vAlign"><img src="image/error_ok.gif" border="0" alt="" onclick="statusWindow.errorOK()" class="button" style="margin-right:3px" /><img src="image/error_hide.gif" alt="" border="0" onclick="statusWindow.hide(1);window.onerror = function(){return true};setTimeout(\'startMainLoop()\',500)" class="button" style="margin-right:3px" />&nbsp;&nbsp; <a href="&#109;ailto'+':'+'bugs'+'@'+'schillmania.com'+'?'+'subject=DHTML Arkanoid bug&body=[ automated bug report ] | Error message: '+msg+' | URL: '+url+' | Line: '+line+' | Description (optional but may help): ... | (Note from Scott: Thanks for submitting this bug and helping to improve this mess! :] )" title="Auto-generated mailto: link .. This may or may not work!"> Report this bug via E-mail</a></div>';
  n += '</div>';
  this.setMessage(n,1);
  return true;
}

StatusWindow.prototype.show = function() {
  this.doTransition('visible');
}

StatusWindow.prototype.hide = function(isError) {
  if (this.statusType != 'error' || (isError && this.statusType == 'error')) {
    this.statusType = null;
    this.doTransition('hidden');
  }
}

StatusWindow.prototype.doTransition = function(state) {
  if (ie) {
    if (this.o.filters.blendTrans.state != 4) {
      this.o.filters.blendTrans.Apply();
      this.o.style.visibility = state;
      this.o.filters.blendTrans.Play();
    } else {
      this.o.style.visibility = state;
    }
  } else {
    if (state == 'hidden') {
      setTimeout("statusWindow.o.style.visibility='hidden'",500);
    } else {
      this.o.style.visibility = state;
    }
  }
}

function StatusWindow() {
  this.statusType = '';
  this.o = document.createElement('div');
  this.o.id = 'sWindow';
  with (this.o.style) {
    position = 'absolute';
    top = 380;
    width = 300;
    left = ((screenX/2)-150);
    textAlign = 'center';
    visibility = 'hidden';
    filter = 'blendTrans(duration=0.3)';
    backgroundColor = '#ffffff';
    color = '#4f4f4f';
    zIndex = 101;
  }
  arkanoidBody.appendChild(this.o);
  this.status = document.createElement('span');
  this.status.innerHTML = '[ status window ]';
  with (this.status.style) {
    backgroundColor = '#ffffff';
    fontFamily = (ie?'standard56,standard 07_56':'small fonts');
    fontSize = '8px';
    fontWeight = 'bold';
    cursor = 'default';
  }
  this.o.appendChild(this.status);
}

function killActiveBalls() {
  for (var i=0; i<balls.length; i++) {
    balls[i].remove(1);
  }
}

function killActiveCapsules() {
  // clean-up for player.die(), nextLevel() etc.
  for (var i=0; i<capsules.length; i++) {
    if (capsules[i].active) capsules[i].remove();
  }
}

function killActiveLasers() {
  for (var i=0; i<lasers.length; i++) {
    if (lasers[i].active) lasers[i].die();
  }
}

function getClientCoords() {
  bodyX = document.body.clientWidth;
  bodyY = document.body.clientHeight;
  if (thePaddle) thePaddle.vAlign();
} 

function brickRespawn(col,row) {
  // called by brick.die when type == funky
  var b = bricks[map.getBrickIndex(col,row)];
  if (b) {
    if (b.type == 'funky') b.reSpawn();
  }
}

function getNewestBall() {
  // return most-recently-created ball object
  var i = balls.length-1;
  if (i >= 0) {
    while (!balls[i].active && i>=0) {
      i--;
    }
  }
  return balls[i]; // -1 if none found (I think)
}

function getHighestBall() {
  // return ball with lowest y component
  var lowY = 999; // lowest Y
  var lowI = 0; // associated index
  for (var i=0; i<balls.length; i++) {
    if (balls[i].active) {
      if (balls[i].y < lowY) {
        lowY = balls[i].y;
        lowI = i;
      }
    }
  }
  return balls[lowI];
}

function gameOver() {
  // legacy: previously showed custom "game over" message
  // now calls highscore checking routine
  player.score.updateAndCheck();
}

function pauseGame() {
  if (gameActive && !gameControls.buttons[0].buttonState) {
    gameControls.click(gameControls.buttons[0]); // pause if active
    gameControls.mouse(gameControls.buttons[0],0); // set to flash (mouseout) state
  }
}

function resumeGame(ignorePause) {
  if (!ignorePause && gameActive && gameControls.buttons[0].buttonType.indexOf('play')>=0) { // unpause game if needed
    gameControls.click(gameControls.buttons[0]); // un-pause if paused
    gameControls.mouse(gameControls.buttons[0],0); // set to normal (mouseout) state
  }
}

function newGame() {
  highScores.hide(1); // hide if shown, don't pause game
  destroyLevelData();
  level = 1;
  warpSide = null;
  resetPlayerData();
  loadLevel(level);
}

function resetPlayerData() {
  player.lives.lR = 3;
  player.lives.update();
  player.score.points = 0;
  player.score.update();
}

function interfaceInit() {
  delete flash;
  flash = [document.getElementById('f0'+(!ie?'moz':'')),document.getElementById('f1'+(!ie?'moz':''))];
  var bg = ['bg1','bg2','bg3'];
  for (var i=0; i<bg.length; i++) {
    document.getElementById(bg[i]).style.visibility = 'visible';
  }
  for (var i=0; i<2; i++) { // create portals
    var p = document.createElement('img');
    if (!i) {
      portal0 = p;
      p.style.left = '0px';
    }
    else {
      portal1 = p;
      p.style.right = '0px';
    }
    with (p.style) {
      position = 'absolute';
      top = '464px';
    }
    p.src = 'image/portal'+(!i?'':'_1')+'_0.gif';
    arkanoidBody.appendChild(p);
  }
  menu = new Menu();
  ipsCounter = new IPSCounter();
  gameControls = new GameControls();
  highScores = new HighScores();
  help = new Help();
  var home = document.createElement('img');
  with (home.style) {
    position = 'absolute';
    right = 1;
    top = 481;
  }
  with (home) {
    src = 'image/home_0.gif';
    title = 'Visit schillmania.com'
    className = 'button';
  }
  home.onclick = function() { window.open('http://www.schillmania.com','wSchillmania') }
  home.onmouseover = function() { this.src = 'image/home_1.gif' }
  home.onmouseout = function() { this.src = 'image/home_0.gif' }

  arkanoidBody.appendChild(home);
  powerup = new Powerup(); // global "power up" object
  statusWindow = new StatusWindow();
  window.onerror = function(msg,file,line) {statusWindow.errorHandler(msg,file,line);return true}
  player = new Player();
  thePaddle = new Paddle();
  paddle = document.getElementById('usrPaddle');
}

function arkanoidInit() {
  loadLevel(level);
  gameActive = 1;
  startMainLoop();
}

function setGameHandlers() {
  setMouseMove();
  document.onkeypress = asdf;
}

function killGameHandlers() {
  document.onmousedown = null;
  document.onkeypress = null;
}

function setMouseMove() {
  document.onmousemove = mmH;
}

function setOnetimeHandler() {
  if (!preventOnetimeHandler) {
    document.onmousedown = resetOnetimeHandler;
    document.onmouseup = function() {if (document.onmousemove != mmH) document.onmousemove = mmH; document.onmouseup=null }
  }
}

function resetOnetimeHandler() {
  if (balls[balls.length-1]) {
    with (balls[balls.length-1]) {
      catchEnabled = 0;
      active = 1;
      caught = 0;
    }
  }
  caughtBalls = 0;
  document.onmousedown = function() {return false};
  // reset, seems to lose mmH when coming from editor after click on occasion (?)
  setMouseMove();
}

function levelShimmer() {
  for (var i=0; i<bricks.length; i++) {
    bricks[i].shimmer(1);
  }
  setTimeout("levelShimmerReset()",1500);
}

function levelShimmerReset() {
  for (var i=0; i<bricks.length; i++) {
    bricks[i].reset();
  }
}

function showPortal() {
  gameControls.hide();
  portalActive = 1;
  if (powerup.currentEffect != 'bypass') {
    powerup.reset(); // prevent laser fire etc.
    document.onmousemove = mmHWarp; // iphone
    iPhoneWarp();
  }
  portal0.src = 'image/portal_1.gif';
  portal1.src = 'image/portal_1.gif';
  setTimeout("portal0.src='image/portal_2.gif';portal1.src='image/portal_2.gif';warpEnabled=1;document.onmousedown='';document.onmousemove=mmHWarp;iPhoneWarp()",750); // iphone
}

function iPhoneWarp() {
  if (isIphone) {
    // user can't warp themselves via mouse move, so we'll just randomly choose left or right and give 'er. I'm too lazy to make left/right side-clicking work.
    var fakeEvent = {
	  clientX: (Math.random()>=0.5?1000:-1000) // pixel offset for left or right
    }
    mmHWarp(fakeEvent); // and off we go.	
  }
}

function doWarp(avoidNextLevel) {
  // last-minute cleanup, etc.
  gameControls.hide();
  thePaddle.hide();
  thePaddle.reset();
  warpEnabled = 0;
  warpActive = 0;
  portalActive = 0;
  setTimeout("portal0.src='image/portal_3.gif';portal1.src='image/portal_1_3.gif'",500);
  if (!avoidNextLevel)
    setTimeout("killActiveLasers();killActiveCapsules();nextLevel()",1000);
  else
    setTimeout("killActiveLasers();killActiveCapsules();stopGame=1;gameActive=0",1000);
}

function nextLevel() {
  destroyLevelData();
  if (level == 'test') {
    stopGame = 1;
    gameActive = 0;
    levelEditor.show();
    return false;
  } else if ((''+level).indexOf('usr') >= 0) {
    stopGame = 1;
    gameActive = 0;
    levelBrowser.show();
    return false;
  }
  if (level >= 33) {
    player.score.checkHighScores();
    stopGame = 1;
    gameActive = 0;
    document.onmousemove = '';
  } else {
    level++;
    loadLevel();
  }
}

function destroyLevelData() {
  if (map) map.destroy();
  delete bricks;
  bricks = [];
  delete balls;
  balls = [];
  delete capsules;
  capsules = [];
  delete lasers;
  lasers = [];
}

function loadHash(sHash) {
  window.location.hash = (!isNaN(sHash)?'level':'')+sHash;
}

function loadLevel(n,isUserDefined,levelName) {
  var base = 'leveldata/';
  statusWindow.setMessage('<div class="smallText"><img src="image/ohlookitsgottabeajax.gif" style="width:32px;height:32px"></div>');
  if (isUserDefined) {
    level = 'usr'+n; // level name array reference
    loadHash(n+1);
    warpSide = '';
    jsLoad(baseHref+base+'user/'+n+'.js','setTimeout("statusWindow.hide()",500);setTimeout("levelBrowser.startLevel()",1000)');
  } else {
    jsLoad(baseHref+base+(!n?(level<10?'0':''):n<10?'0':n)+level+'.js','statusWindow.hide();setTimeout("initializeLevel()",500);setTimeout("player.reSpawn()",1500)');
  }
}

function createXHR() {
  var xhr = null;
  if (!document.domain || ie) return null; // don't use XHR if it will be denied - and IE has some bug with the eval() on user levels, unterminated string constant - wtf.
  if (typeof window.XMLHttpRequest != 'undefined') {
    try {
      xhr = new XMLHttpRequest();
    } catch(e) {
      // d'oh
    }
  }
  if (!xhr) {
    try {
      xhr = new ActiveXObject('Msxml2.XMLHTTP');
    } catch(e) {
      try {
        xhr = new ActiveXObject('Microsoft.XMLHTTP');
      } catch(E) {
        xhr = null;
      }
    }
  }
  return xhr;
}

var hasXHR = createXHR();
var xhrObjects = [];

function jsLoad(jsFile,loadAction) {
  if (hasXHR) {
    var oX = createXHR();
    oX._onloadAction = loadAction;
    oX.onreadystatechange = function() {
      // this != oX, am I missing something?
      if (oX.readyState == 4) {
        eval(oX.responseText); // execute
        eval(oX._onloadAction);
      }
    }
    oX.open('GET',jsFile,true);
    oX.send(null);
  } else {
    jsLoadLegacy(jsFile,loadAction);
  }
}

var jsDataCounter = 0;
var oJS = null;

function jsLoadLegacy(jsFile,loadAction) {
  var head = document.getElementsByTagName('head').item(0);
  var oJS = document.createElement('script');
  oJS.type = 'text/javascript';
  oJS.id = 'jsData'+(jsDataCounter++);
  oJS._loadAction = (typeof loadAction != 'undefined')?loadAction:'';
  if (ie) {
    oJS.onreadystatechange = jsLoadHandler;
    oJS.onload = jsLoadHandler;
  } else {
    oJS.onload = jsLoadHandler; // function() {jsLoadHandler(this)}
  }
  oJS.src = jsFile;
  head.appendChild(oJS);
}

function jsLoadHandler() {
  if (ie) {
    if (this.readyState == 4 || this.readyState == 'complete') { // "loading/loaded" for remote load, "complete" for local/cached (?)
      this.onreadystatechange = null;
      this.onload = null;
      eval(this._LoadAction); // next action
    }
  } else {
    this.onload = null;
    eval(this._loadAction);
  }
}

function jsLoadIE(jsFile,loadAction) {
  jsLoadAction = loadAction; // what to "eval" when load is complete
  var oJS = document.getElementById('jsData');
  // var head = document.getElementsByTagName('head').item(0);
  // if (oJS) head.removeChild(oJS);
  if (oJS) oJS.parentNode.removeChild(oJS);
  oJS = document.createElement('script');
  oJS.type = 'text/javascript';
  oJS.id = 'jsData';
  oJS._loadAction = loadAction;
  oJS.onreadystatechange = function() {jsLoadHandlerIE(this)}
  oJS.src = jsFile;
  // head.appendChild(oJS);
  document.getElementsByTagName('head')[0].appendChild(oJS);
}

function jsLoadHandlerIE(oJS) {
  if (!oJS) oJS = (event?event.srcElement:window.event.sourceElement);
  if (oJS) {
    if (oJS.readyState == 'loaded' || oJS.readyState == 'complete') { // "loading/loaded" for fresh/remote load, "complete" for local/cached (?)
      oJS.onreadystatechange = function() { return false }
      eval(oJS._loadAction);
    }
  }
}

if (ie) {
  // stupid legacy crap
  jsLoad = jsLoadIE;
  jsLoadHandler = jsLoadHandlerIE;
}


function initializeLevel() {
  gameControls.show();
  var col = 1;
  var row = 2;
  var lName = level + warpSide; 
  for (var i=0; i<levelData[lName].length; i++) {
    for (var j=0; j<levelData[lName][i].length; j++) {
      if (levelData[lName][i][j] != null) bricks[bricks.length] = new Brick(col,row,brickTypes[levelData[lName][i][j]]);
      col++;
      if (col > 13) {
        col = 1;
        row++;
      }
    }
  }
  map = new BrickMap();
  if (!map.killableBricks) {
    resetOnetimeHandler();
    statusWindow.setWarning('No destroyable bricks found on this level (ie. it cannot be "finished").',0,1);
    preventOnetimeHandler = 1;
  }
  activeBalls = 0;
  gameActive = 1;
}

function exitLevel(avoidNextLevel) {
  preventOnetimeHandler = 0;
  document.onmousedown = '';
  killGameHandlers();
  killActiveLasers();
  killActiveBalls();
  killActiveCapsules();
  doWarp((avoidNextLevel?avoidNextLevel:0));
  destroyLevelData();
}

function startMainLoop() {
  // start ips counter, main loop
  ipsCounter.iterate();
  mainLoop();
}

var lastLoopExec = new Date();

function mainLoop() {
  // primary animation/collision detection loops
  var d = new Date();
  if (d-lastLoopExec<20) { // throttle to 20 fps
    if (!useInterval) callNextLoop(1);
    return false;
  }
  lastLoopExec = d;
  ips++;
  for (var i=0; i<balls.length; i++) {
    if (balls[i].active) {
      balls[i].collisionCheck();
      balls[i].move();
    } else if (balls[i].caught == 1) {
      balls[i].setLeft(thePaddle.x + balls[i].offX);
    }
  }
  for (i=0; i<capsules.length; i++) {
    if (capsules[i].active)
      capsules[i].animate();
  }
  for (i=0; i<lasers.length; i++) {
    if (lasers[i].active)
      lasers[i].animate();
  }
  thePaddle.animate();
  if (!stopGame) {
    callNextLoop();
  } else {
    if (doInterval) clearInterval(gameTimer);
    gameTimer = null;
    stopGame = 0;
  }
}

function doTimeout() {
  setTimeout(mainLoop,delay);
}

function doInterval(n) {
  var delay = n?n:20;
  if (!gameTimer) {
    gameTimer = setInterval(mainLoop,delay);
  }
}

var gameTimer = null;

var useInterval = (true); // determine best method to use
var callNextLoop = (useInterval)?doInterval:doTimeout;

function mmH(e) {
  if (e) {
    event = e; // mozilla
    e.stopPropagation();
  } else {
    window.event.cancelBubble = true;
  }
  var newX = event.clientX - paddleHalfWidth - arkanoidBodyOffX;
  if (newX > marginX + 3 && newX < screenX - paddleWidth - marginX - 3) thePaddle.setLeft(newX);
  else if (newX <= marginX-3) thePaddle.setLeft(marginX+3);
  else if (newX >= screenX - paddleWidth - marginX-3) thePaddle.setLeft(screenX - paddleWidth - marginX-3);
  if (isFirefox && !gamePaused) { // if Firefox (or Mozilla?), force animation update! (keep things smooth) - mousemove/mmH can block setInterval/timeout. Lame.
    var d = new Date();
    if (d-lastLoopExec>20) mainLoop(); // foce update if lagging
  }
  return false;
}

function mmHWarp(e) {
  if (e) event = e;
  var newX = event.clientX - paddleHalfWidth - arkanoidBodyOffX;
  if (newX > marginX + 3 && newX < screenX - paddleWidth - marginX - 3) thePaddle.setLeft(newX);
  else if (newX <= marginX-3) {
    thePaddle.setLeft(marginX+3);
    if (warpEnabled == 1) {
      warpActive = 1;
      document.onmousemove = '';
      warpSide = 'l';
      playSound('player_warp',50);
    }
  }
  else if (newX >= screenX - paddleWidth - marginX-3) {
    thePaddle.setLeft(screenX - paddleWidth - marginX-3);
    if (warpEnabled == 1) {
      warpActive = 1;
      document.onmousemove = '';
      warpSide = 'r';
      playSound('player_warp',50);
    }
  }
  return false;
}

function playSound(audioTarget,volume,isSFX) {
  if (NOSOUND) return false;
  if (!options.sound && isSFX) return false;
  if (!volume && volume != 0) volume = -1;
  if (flash[0].ReadyState == 4 || flash[0].PercentLoaded() == 100) {
    flash[0].TGotoLabel("/" + audioTarget, "start")
    flash[0].TPlay("/" + audioTarget)
    if (volume != -1) playFrame(audioTarget,'v'+volume); // play at 100% volume (default)
    else playFrame(audioTarget,'v30');
  }
}

function playFrame(audioTarget, label) {
  if (flash[0].ReadyState == 4 || flash[0].PercentLoaded() == 100) {
    flash[0].TGotoLabel("/"+audioTarget,label);
  }
}

function cancelBubble(e) {
  (e?e:window.event).cancelBubble = 'true';
}

OptionSet.prototype.setOption = function(opType) {
  if (opType == 'sfx') {
    this.sound = !this.sound;
  }
/*
  else if (opType == 'foo') {
    // other options
  }
*/
}

OptionSet.prototype.toggleOption = function(o,base) {
  var status = o.src.indexOf('_0.gif')>=0?1:0;
  o.src = 'image/'+base+'_'+status+'.gif';
  this.setOption(base);
}

function OptionSet() {
  this.sound = 1;
  // configuration parameters go here
}

var tmp = '';
function asdf() {
  var c = String.fromCharCode(event.keyCode);
  tmp += c;
  if (tmp.indexOf(' ')>=0) {
    gameControls.click(gameControls.buttons[0]);
    gameControls.mouse(gameControls.buttons[0],0);
    tmp = '';
  } else if (tmp.indexOf('boom')>=0) {
    boom();
    tmp = '';
  } else if (tmp.indexOf('asdf')>=0) {
    for (var i=0; i<bricks.length; i++) {
      bricks[i].die();
    }
    tmp = '';
  }
}

function boom() {
  for (var i=0; i<24; i++) {
    setTimeout("balls[balls.length]=new Ball(thePaddle.x+paddleHalfWidth,450,getRnd(4)*getPlusMinus(),-3-getRnd(5),0)",20*(i+1));
  }
}

// LEVEL EDITOR ENGINE

var mouseDown = 0;

function LEGrid(parentElement) {
  this.show = function() { this.o.style.display = 'block'; }
  this.hide = function() { this.o.style.display = 'none'; }
  this.o = document.createElement('img');
  this.o.style.position = 'absolute';
  this.o.style.display = 'block';
  this.o.style.left = bOffX + 7;
  this.o.style.top = bOffY + 1;
  this.o.src = 'image/level_editor/grid.gif';
  parentElement.appendChild(this.o);
}

var NONE = 'none';
var nullObject = new NullObject();

function NullObject() {
  this.mouseOver = function() {return false}
  this.mouseOut = function() {return false}
  this.mouseDown = function() {return false}
}

LEGridItem.prototype.setType = function(type) {
  this.type = type;
  this.o.src = 'image/brick/'+(this.type != -1?brickTypeData[brickTypes[this.type]][0]:'none')+'.gif';
}

function LEGridItem(parentElement,row,col) {
  // level editor brick object
  this.type = -1; // brick type
  this.row = row;
  this.col = col;
  this.width = 32;
  this.height = 16;
  this.mouseOver = function() {
    this.o.style.backgroundColor = '#eeeeee';
    if (mouseDown) {
      if (this.type != levelEditor.typeCursor.type) this.mouseDown();
    }
  }
  this.mouseOut = function() {
    this.o.style.backgroundColor = '';
  }
  this.mouseDown = function() {
    mouseDown = 1;
    document.onmouseup = function() {mouseDown=0;document.onmouseup=''}
    this.setType(levelEditor.typeCursor.type);
  }
  this.o = document.createElement('img');
  this.o.row = this.row;
  this.o.col = this.col;
  this.o.style.position = 'absolute';
  this.o.style.display = 'block';
  this.o.style.left = -bOffX + ((this.width+1)*this.col);
  this.o.style.top = 1+((this.height+1)*this.row); // bOffY
  this.o.style.width = this.width;
  this.o.style.height = this.height;
  this.o.src = 'image/brick/none.gif';
  if (!ie) {
    this.o.onmouseover = function() { levelEditor.map.getItem(this.col,this.row).mouseOver(this) }
    this.o.onmouseout = function() { levelEditor.map.getItem(this.col,this.row).mouseOut(this) }
    this.o.onmousedown = function(e) { levelEditor.map.getItem(this.col,this.row).mouseDown(this);e.stopPropagation();return false; }
  } else {
    this.o.onmouseover = function() { levelEditor.map.getItem().mouseOver(this) }
    this.o.onmouseout = function() { levelEditor.map.getItem().mouseOut(this) }
    this.o.onmousedown = function() { levelEditor.map.getItem().mouseDown(this) }
  }
  parentElement.appendChild(this.o);
}

function NullObject() {
  this.mouseOver = function() {return false}
  this.mouseOut = function() {return false}
  this.mouseDown = function() {return false}
}

nullObject = new NullObject();

LEMap.prototype.getItem = function(col,row) {
  if (row >= 0 && col >= 0) {
    return (this.data[row-1][col-1]?this.data[row-1][col-1]:nullObject);    
  } else { // row & col assumed to be passed
    return event.srcElement.col && event.srcElement.row?this.data[event.srcElement.row-1][event.srcElement.col-1]:nullObject;
  }
}

LEMap.prototype.wipe = function() {
  for (var i=0; i<20; i++) {
    for (var j=0; j<13; j++) {
      this.data[i][j].setType(-1);
    }
  }
}

function LEMap(parentElement) {
  this.data = [];
  for (var i=0; i<20; i++) {
    this.data[i] = [];
    for (var j=0; j<13; j++) {
      this.data[i][j] = new LEGridItem(parentElement,i+1,j+1);
    }
  }
}

LETypeCursor.prototype.show = function() {
  this.o.style.display = 'block';
}

LETypeCursor.prototype.hide = function() {
  this.o.style.display = 'none';
}

LETypeCursor.prototype.setType = function(type) {
  this.type = type;
  this.o.src = 'image/level_editor/'+(this.type != -1?brickTypeData[brickTypes[this.type]][0]:'none')+'.gif';
}

LETypeCursor.prototype.moveTo = function(x,y) {
  this.o.style.left = x - 22; // - arkanoidBodyOffX; // used when applied within arkanoid container
  this.o.style.top = y - 15; // - arkanoidBodyOffY;
}

function LETypeCursor(parentElement) {
  this.o = document.createElement('img');
  this.o.style.position = 'absolute';
  this.o.style.zIndex = 100;
  this.o.style.display = 'none';
  this.setType(-1);
  parentElement.appendChild(this.o);
  this.show();
}

var saveTimeout = 0;
var saveTimeoutRef = 0;

function saveTimeoutReset() {
  // canceled due to error or success
  saveTimeout = 0;
  if (saveTimeoutRef) {
    clearTimeout(saveTimeoutRef);
    saveTimeoutRef = 0;
  }
}

function saveHandler(js) {
  // save either successful or failed. (global variable: saveResult)
  if (saveResult.indexOf('Save OK')>=0) {
    saveTimeoutReset();
    statusWindow.setWarning('<span style="font-weight:bold" class="smallText">'+saveResult+'</span><br /><span class="smallText">&nbsp;&nbsp;Your submission should now appear in the level browser.</span>');
    setTimeout("levelEditor.map.wipe()",1000);
  } else {
    saveTimeout++;
    if (saveTimeout >= 5) {
      saveTimeoutReset();
      statusWindow.setWarning('Response received, but no confirmation - save may have failed.<br />&nbsp;&nbsp;Sorry! Try later, perhaps.');
    } else {
      var timeoutStr = "document.getElementById('"+js.id+"')";
      setTimeout("saveHandler("+timeoutStr+")",1000);
    }
  }
  js.onreadystatechange = '';
}

function saveTimeoutError() {
  saveTimeoutRef = 0;
  saveTimeout = 0;
  statusWindow.setWarning('Connection timed out (15 seconds). Save may have failed.<br />&nbsp;&nbsp;Sorry! Try later, perhaps.');
}

function saveLevel() {
  statusWindow.setWindowedMessage('<img src="image/ohlookitsgottabeajax.gif" style="width:32px;height:32px;vertical-align:middle">&nbsp;Submitting level, please wait...<br /><br /><br />');
  saveTimeoutRef = setTimeout("saveTimeoutError()",15000);
  saveResult = '';
  var oJS = document.getElementById('jsSaveData');
  var head = document.getElementsByTagName('head').item(0);
  if (oJS) head.removeChild(oJS);
  oJS = document.createElement('script');
  oJS.type = 'text/javascript';
  oJS.id = 'jsSaveData';
  head.appendChild(oJS);
  if (ie) {
    oJS.onreadystatechange = function() {if (this.readyState == 'complete' || this.readyState == 'loaded') setTimeout("saveHandler(document.getElementById('jsSaveData'))",1000)}
  } else {
    oJS.onload = function() {setTimeout("saveHandler(document.getElementById('jsSaveData'))",1000)}
  }
  var oUsrName = document.getElementById('usrName');
  params = '?lN='+(!oUsrName.value || oUsrName.value == 'Level name here'?'Untitled_'+parseInt(Math.random()*32768):oUsrName.value)+'&r=';
  for (var i=0; i<20; i++) {
    for (j=0; j<13; j++) {
      params += (levelEditor.map.data[i][j].type==-1?'X':levelEditor.map.data[i][j].type==10?'A':levelEditor.map.data[i][j].type);
    }
    params += (i<19?',':'');
  }
  oJS.src = baseHref+'leveldata/user/savelevel.php'+params;
}

LevelEditor.prototype.instructionsHTML = function() {
  html =  '<p class="smalltext"><span style="font-weight:bold">Level Editor</span>: Instructions</p>'
  html += '<p>Select a brick type from the Editor Toolbox (which is draggable), then click on the map area above to place a brick. IE 5.5+ users can click and drag to "paint" bricks on the map.</p>';
  html += '<p>When done building, you can click the "test level" button to try out your creation. If you want to submit it to the Web (where other people can play it through the Level Browser), fill out the Level Name field and click "Save". <span class="red">Note however:</span> Don\'t submit anything you wouldn\'t show proudly to your mother. Inappropriate submissions will be deleted, so keep it clean please! :)</p>';
  html += '<p>To go back to the Arcade Game or Level Browser, use the links at the top of this window.</p>';
  return html;
}

LevelEditor.prototype.save = function() {
  if (DEBUG) {
    this.saveDebug();
    return false;
  }
  // check map for destroyable bricks
  var notOK = 1;
  for (var i=0; i<20; i++) {
    for (j=0; j<13; j++) {
      if (this.map.data[i][j].type != -1 && this.map.data[i][j].type != 1 && this.map.data[i][j].type != 2) {
        notOK = 0;
      }
    }
  }
  if (notOK) {
    statusWindow.setWarning('No destroyable bricks in the level (ie. It cannot be "finished")');
  }
  else
    saveLevel();
}

LevelEditor.prototype.saveDebug = function() {
  newWin = window.open('about:blank','dataWindow','width=200,height=200,toolbar=no,status=no,resize=yes');
  newHTML = 'levelData[\'new\'] = [<br />';
  for (var i=0; i<20; i++) {
    newHTML += '&nbsp;&nbsp;['
    for (var j=0; j<13; j++) {
      newHTML += (this.map.data[i][j].type==-1?'X':this.map.data[i][j].type==10?'A':this.map.data[i][j].type)+(j<12?',':'');
    }
    newHTML += ']'+(i<19?',':'')+'<br />';
  }
  newHTML += ']';
  newWin.document.write('<span style="font-family:terminal;font-size:9px">'+newHTML+'</span>');
}

LevelEditor.prototype.show = function() {
  this.active = 1;
  this.o.style.display = 'block';
  this.typeCursor.show();
  this.toolbox.show();
  this.setEventHandlers();
}

LevelEditor.prototype.hide = function() {
  this.active = 0;
  this.o.style.display = 'none';
  this.typeCursor.hide();
  this.toolbox.hide();
}

function LevelEditor() {
  this.active = 1;
  this.o = document.createElement('div');
  this.o.style.position = 'absolute';
  this.o.style.left = 0;
  this.o.style.top = 0;
  arkanoidBody.appendChild(this.o);
  this.grid = new LEGrid(this.o);
  this.map = new LEMap(this.o);
  this.instructions = document.createElement('div');
  this.instructions.innerHTML = this.instructionsHTML();
  with (this.instructions.style) {
    position = 'absolute';
    left = 32;
    top = 365;
    width = 405;
  }
  this.o.appendChild(this.instructions);
  this.toolbox = new LevelEditorToolbox(document.body); // this.o
  this.toolbox.o.style.left = arkanoidBodyOffX-100;
  this.toolbox.o.style.top = arkanoidBodyOffY+225;
  this.toolbox.show();
  this.typeCursor = new LETypeCursor(document.body); // this.o
  this.setEventHandlers();
}

LevelEditor.prototype.setEventHandlers = function() {
  document.onmousemove = mmhLevelEditor;
}

LevelEditor.prototype.stopMouseDown = function() {
  document.onmousedown = '';
  document.onmousemove = '';
}

LevelEditor.prototype.allowSelect = function() {
  document.onselectstart = '';
  document.onmousedown = '';
}

LevelEditor.prototype.stopSelect = function() {
  document.onselectstart = function() {return false};
}

LevelEditorToolbox.prototype.show = function() {
  this.o.style.display = 'block';
  setTitle('Level Editor');
  loadHash('editor');
}

LevelEditorToolbox.prototype.hide = function() {
  this.o.style.display = 'none';
}

LevelEditorToolbox.prototype.toolboxDragSet = function(e) {
  if (e && !ie) {
    event=e;
    e.stopPropagation();
  }
  this.offX = event.clientX - parseInt(this.o.style.left);
  this.offY = event.clientY - parseInt(this.o.style.top);
  document.onmousemove = function(e) { if (e) event=e;levelEditor.toolbox.mmhToolbox(event.clientX, event.clientY);return false }
  document.onmouseup = function() { document.onmousemove = mmhLevelEditor }
}

LevelEditorToolbox.prototype.mmhToolbox = function(x,y) {
  // var evt = e?e:event;
  this.o.style.left = x - this.offX;
  this.o.style.top = y - this.offY;
  levelEditor.typeCursor.moveTo(x,y);
  event.cancelBubble = true;
  // window.event.stopPropagation();
  return false;
}

function LevelEditorToolbox(parentElement) {
  this.offX = 0;
  this.offY = 0;
  this.o = document.createElement('div');
  this.o.id = 'leToolbox';
  this.o.style.position = 'absolute';
  this.o.style.display = 'none';
  this.o.style.left = 0;
  this.o.style.top = 0;
  this.o.style.width = 138;
  this.o.style.height = 120;
  this.o.style.zIndex = 100;
  parentElement.appendChild(this.o);
  var html = '';
  // Bad form, bad form! :) - should be all DOM, ideally.
  html += '<img id="rShadow" src="image/level_editor/shadow_right.gif" class="windowShade" />';
  html += '<img src="image/level_editor/toolbox_title.gif" width="133" height="19" title="click and drag to move." onmousedown="this.title=\'\';levelEditor.toolbox.toolboxDragSet(event);return false" class="button" />';
  html += '<div id="brickContainer">';
  html += '<img src="image/brick/grey.gif" width="32" height="16" title="Grey" onclick="levelEditor.typeCursor.setType(0);this.src=\'image/brick/grey_1.gif\'" class="button" /><img src="image/brick/funky.gif" width="32" height="16" title="Funky (Reappears 1.5 sec after being destroyed)" onclick="levelEditor.typeCursor.setType(2);this.src=\'image/brick/funky_1.gif\'" class="button" /><img src="image/brick/gold.gif" width="32" height="16" title="Gold (Indestructible without mega balls)" onclick="levelEditor.typeCursor.setType(1);this.src=\'image/brick/gold_1.gif\'" class="button" /><img src="image/brick/none.gif" width="32" height="16" title="None (clear space)" style="background-color:#ffffff;border-right:none" onclick="levelEditor.typeCursor.setType(-1)" class="button" /><br />';
  html += '<img src="image/brick/white.gif" width="32" height="16" title="White" onclick="levelEditor.typeCursor.setType(8)" class="button" /><img src="image/brick/teal.gif" width="32" height="16" title="Teal" onclick="levelEditor.typeCursor.setType(4)" class="button" /><img src="image/brick/yellow.gif" width="32" height="16" title="Yellow" onclick="levelEditor.typeCursor.setType(6)" class="button" /><img src="image/brick/magenta.gif" width="32" height="16" title="Magenta" style="border-right:none" onclick="levelEditor.typeCursor.setType(A)" class="button" /><br />';
  html += '<img src="image/brick/red.gif" width="32" height="16" title="Red" onclick="levelEditor.typeCursor.setType(3)" class="button" /><img src="image/brick/green.gif" width="32" height="16" title="Green" onclick="levelEditor.typeCursor.setType(7)" class="button" /><img src="image/brick/blue.gif" width="32" height="16" title="Blue" onclick="levelEditor.typeCursor.setType(5)" class="button" /><img src="image/brick/brown.gif" width="32" height="16" title="Brown" style="border-right:none" onclick="levelEditor.typeCursor.setType(9)" class="button" /><br />';
  html += '</div>';
  html += '<div id="buttonContainer" style="background-color:#ffffff;padding-left:4px;padding-top:3px;padding-bottom:3px">';
  html += '<img src="image/level_editor/name_field_border.gif" style="margin-top:1px" /><input id="usrName" type="text" maxlength="32" title="Enter the level name here." onfocus="levelEditor.stopMouseDown();levelEditor.allowSelect();if (this.value==\'Level name here\')this.value=\'\'" onblur="levelEditor.stopSelect();levelEditor.setEventHandlers()" value="Level name here" style="width:75px;height:16px;border:none;background-image:url(image/level_editor/name_field_bg.gif);background-repeat:repeat-x;margin-bottom:2px" /><img src="image/level_editor/name_field_border.gif" style="margin-top:1px" /><img src="image/level_editor/save.gif" title="Submit this level to the Web." onmouseover="this.src=\'image/level_editor/save_1.gif\'" onmouseout="this.src=\'image/level_editor/save.gif\'" onclick="levelEditor.save()" class="button" style="margin-left:3px;margin-bottom:2px" /><br /><img src="image/level_editor/test_level.gif" title="Play this level." onmouseover="this.src=\'image/level_editor/test_level_1.gif\'" onmouseout="this.src=\'image/level_editor/test_level.gif\'" onclick="testLevel()" class="button" /><img src="image/level_editor/wipe.gif" title="Remove all bricks." onmouseover="this.src=\'image/level_editor/wipe_1.gif\'" onmouseout="this.src=\'image/level_editor/wipe.gif\'" onclick="levelEditor.map.wipe()" class="button" style="margin-left:2px" />';
  html += '</div>';
  html += '<img src="image/level_editor/shadow_bottom.gif" class="windowShade" />';
  this.o.innerHTML = html;
}

var toolboxOffX, toolboxOffY;

function testLevel() {
  level = 'test';
  warpSide = '';
  // copy data into levelData['test']
  for (var i=0; i<20; i++) {
    for (var j=0; j<13; j++) {
      levelData['test'][i][j] = (levelEditor.map.data[i][j].type != -1?levelEditor.map.data[i][j].type:null);
    }
  }
  levelEditor.hide();
  // initialize player, game handlers etc.
  // set "return to editor" flag for when done
  resetPlayerData();
  initializeLevel();
  document.onmousedown = function() {return false};
  setTimeout("player.reSpawn();startMainLoop()",500);
}

function mmhLevelEditor(e) {
  if (e) {
    event = e;
    e.stopPropagation();
  }
  levelEditor.typeCursor.moveTo(event.clientX,event.clientY);
  return false;
}

function startLevelEditor() {
  levelEditor = new LevelEditor();
}

LevelBrowserControls.prototype.refresh = function() {
  this.pageCount = Math.floor(lN.length/this.pageLimit) + ((lN.length/this.pageLimit)-Math.floor(lN.length/this.pageLimit)>0?1:0);
  this.tmp = '<img src="image/btn_divider.gif" alt="" style="margin-right:5px" />Pages <a href="#" onclick="levelBrowser.browserControls.showPage(\'?\');return false" title="View random levels."><img src="image/level_browser/random_0.gif" onmouseover="this.src=\'image/level_browser/random_1.gif\'" onmouseout="this.src=\'image/level_browser/random_0.gif\'" border="0" alt="" /></a>';
  for (this.i=0; this.i<this.pageCount; this.i++) {
    this.tmp += '<a href="#" onclick="levelBrowser.browserControls.showPage('+this.i+');return false" title="View page '+(this.i+1)+'"><img src="image/level_browser/page_0.gif" onmouseover="this.src=\'image/level_browser/page_1.gif\'" onmouseout="this.src=\'image/level_browser/page_0.gif\'" border="0" alt="View page '+(this.i+1)+'" /></a>';
  }
  this.controls.innerHTML = this.tmp;
  this.showPage(this.page);
}

LevelBrowserControls.prototype.setPageIconHighlight = function(n,bHighlight) {
  var o = this.controls.getElementsByTagName('img');
  var i = (n=='?'?1:n+2);
  if (o.length && o[i]) {
    o[i].style.marginTop = (bHighlight?'-2px':'0px');
    o[i].style.borderTop = (bHighlight?'2px solid #666':'none');
    o[i].style.borderBottom = (bHighlight?'2px solid #666':'none');
  }
}

LevelBrowserControls.prototype.showPage = function(n) {
  this.html = '';
  this.tdCount = 0;
  this.setPageIconHighlight(this.page,false);
  this.setPageIconHighlight(n,true);
  if (n == '?') {
    this.descriptor = 'Random Levels';
    for (this.i=1; this.i<16; this.i++) { // this.pageLimit+1
      this.randomLevel = parseInt(Math.random()*lN.length);
      if (typeof lN[this.randomLevel] != 'undefined') { // safety net, preventing common source of bug reports (!?)
        this.html += '<td><div class="lbPreview monospace"><a href="http://www.schillmania.com/arkanoid/arkanoid.html#level'+(this.randomLevel+1)+'" onmouseover="this.parentNode.className=\'lbPreviewOver monospace\'" onmouseout="this.parentNode.className=\'lbPreview monospace\'" onclick="level=levelBrowser.hide();loadLevel('+this.randomLevel+',1);return false"><img src="'+baseHref+'preview.php?level='+(this.randomLevel)+'" width="143" height="120" border="0" /><br />#'+ (this.randomLevel<10?'0'+(this.randomLevel+1):(this.randomLevel+1)) + ': ' + lN[this.randomLevel][0] + '</a></div></td>';
        if (this.i%2 == 0 && this.i != 0) this.html += '</tr><tr>';
      }
    }
  } else {
    this.page = n;
    this.levelNumber = null;
    this.offset = this.page?(this.page*this.pageLimit):0; // lN array index to start from
    this.offsetEnd = this.offset+(lN.length-this.offset>=this.pageLimit?this.pageLimit:lN.length-this.offset);
    this.descriptor = 'Page '+(this.page+1)+': Levels '+(this.offset+1)+'-'+this.offsetEnd;
    for (this.i=this.offset; this.i<this.offsetEnd; this.i++) {
      this.levelNumber = (this.i+1);
      if (lN[this.i][0]!='[null]') {
        this.tdCount++;
        this.html += '<td><div class="lbPreview monospace"><a href="http://www.schillmania.com/arkanoid/arkanoid.html#level'+(this.i+1)+'" onmouseover="this.parentNode.className=\'lbPreviewOver monospace\'" onmouseout="this.parentNode.className=\'lbPreview monospace\'" onclick="level=levelBrowser.hide();loadLevel('+this.i+',1);return false"><img src="'+baseHref+(this.levelNumber!=1?'preview.php?level='+(this.levelNumber-1):'image/level_browser/error.gif')+'" border="0" /><br />#'+ (parseInt(this.levelNumber)<10?'0'+this.levelNumber:this.levelNumber) + ': ' + lN[this.i][0] + '</a></div></td>';
        if (this.tdCount%2 == 0 && this.tdCount) this.html += '</tr><tr>';
      }
    }
  }
  this.baseHTML = '<table border="0" cellpadding="2" cellspacing="0" width="360" style="margin-left:10px"><tr><td colspan="2"><div class="grey">'+this.descriptor+'</div></td></tr><tr>';
  this.window.innerHTML = this.baseHTML + this.html + '</tr></table>';
}

function LevelBrowserControls(o,windowRef) {
  this.page = '?'; // default
  this.pageCount = 0;
  this.pageLimit = 256; // items per page
  this.window = windowRef;
  this.randomLevel = 0;
  this.controls = document.createElement('div');
  this.controls.className = 'vAlignTop';
  with (this.controls.style) {
    position = 'absolute';
    top = (ie?0:0);
    right = 0;
  }
  o.appendChild(this.controls);
}

LevelBrowser.prototype.instructionsHTML = function() {
  html =  '<p class="smalltext"><span style="font-weight:bold">Level Browser</span>: Instructions</p>'
  html += '<p>These are user-generated levels which you can play. Click a thumbnail to play a level. Levels you create (see top menu for editor) will also show up here.</p>';
  html += '<p>You can also "bookmark" a particular level by copying the URL (it\'s a normal web link.)</p>';
  html += '<p>To go back to the Arcade Game or Level Editor, use the links at the top of this window.</p>';
  return html;
}

LevelBrowser.prototype.hide = function() {
  this.active = 0;
  this.o.style.display = 'none';
}

LevelBrowser.prototype.show = function() {
  this.active = 1;
  this.o.style.display = 'block';
  setTitle('Level Browser');
  loadHash('browser');
}

LevelBrowser.prototype.startLevel = function() {
  if (gameActive==1) return false; // prevent double-init calls (from scripted init with user level param.)
  this.hide();
  initializeLevel();
  resetPlayerData();
  setTimeout("player.reSpawn();gameActive=1;startMainLoop()",500);
}

LevelBrowser.prototype.refreshComplete = function() {
  // write level names with links
  // Ideally this should be DOM-based and should have paging functionality, too.
  this.browserControls.refresh();
}

LevelBrowser.prototype.refreshList = function() {
  // delete lN;
  // lN = [];
  // delete current list if one exists
  this.window.innerHTML = '<div style="position:absolute;left:12px;top:130px"><span style="display:block;font-family:arial;font-weight:bold;font-size:56px;color:#dddddd">REFRESHING</span><div style="text-align:center"><img src="image/ohlookitsgottabeajax.gif" style="width:32px;height:32px"></div><span style="position:relative;left:1px;top:-10px;color:#999999;display:none">&nbsp;Please wait . . .</span></div>';
  jsLoad(baseHref+'leveldata/user/index.js?r='+parseInt(Math.random()*32768),'setTimeout("levelBrowser.refreshComplete()",1000)');
}

function LevelBrowser(nUserLevel) {
  // list and play custom (user-defined) levels
  this.active = 1;
  this.width = (ie?400:403);
  this.height = 365;
  this.o = document.createElement('div');
  with (this.o.style) {
    position = 'absolute';
    left = 0;
    top = 0;
  }
  this.browser = document.createElement('div');
  with (this.browser.style) {
    position = 'absolute';
    left = 34;
    top = 32;
    width = this.width;
    height = this.height;
    border = '1px solid #aaaaaa';
  }
  this.header = document.createElement('div');
  this.header.className = 'levelBrowserTitlebar';
  with (this.header.style) {
    width = this.width;
    height = (ie?18:17);
    paddingTop = (ie?0:0);
    borderBottom = '1px solid #999999';
  }
  this.header.innerHTML = '<div style="position:absolute;left:4px;top:1px;color:#555555"><span style="font-color:#555555" class="boldText">LEVEL BROWSER</span></div>';
  this.browser.appendChild(this.header);
  this.window = document.createElement('div');
  with (this.window.style) {
    position = 'absolute';
    left = 0;
    top = 19;
    width = this.width-7;
    height = this.height-23;
    overflow = 'auto';
    marginLeft = 4;
    marginRight = 4;
  }
  this.instructions = document.createElement('div');
  this.instructions.innerHTML = this.instructionsHTML();
  with (this.instructions.style) {
    position = 'absolute';
    left = 32;
    top = 409;
    width = 405;
  }
  this.browserControls = new LevelBrowserControls(this.browser,this.window);
  this.hide();
  arkanoidBody.appendChild(this.o);
  this.o.appendChild(this.browser);
  this.browser.appendChild(this.window);
  this.o.appendChild(this.instructions);
  this.refreshList();
}

Menu.prototype.setNewStateAllowed = function(n) {
  this.newStateAllowed = n;
  this.o.style.visibility = (n?'visible':'hidden');
}

Menu.prototype.setNewState = function(type) {
  if (!this.newStateAllowed) return false;
  gameControls.reset();
  var onUsrLevel = ((''+level).indexOf('usr')>=0?1:0);

  // 1:42 AM, 11.05.2002: This function code is ridiculous! (but it works)

  if (type == 'helpAbout') {
    // statusWindow.setWarning('Not yet implemented. Will be done shortly! :)');
    pauseGame();
    help.display();
    return false;
  }
  if (type == 'highscores') {
    pauseGame();
    highScores.display();
    return false;
  } else {
    highScores.hide();
  }
  if (levelEditor) {
    if (levelEditor.active) {
      levelEditor.hide();
    }
  } else {
    levelEditor = new LevelEditor();
    levelEditor.hide();
  }
  if (levelBrowser) {
    if (levelBrowser.active) {
      levelBrowser.hide();
    }
  }
  if (gameActive && onUsrLevel) {
    if (type == 'arcadeGame') {
      exitLevel(1); // exit without loading next level
      setTimeout("resetPlayerData();level=1;warpSide='';loadHash('arcade');arkanoidInit()",1500);
    }
  }
  if (gameActive) { //  && !onUsrLevel
    if (type == 'levelBrowser') {
      exitLevel(1);
      levelBrowser.show();
    }
  }
  if (gameActive && type == 'levelEditor') {
    exitLevel(1);
    levelEditor.show();
  }
  if (!gameActive && type == 'levelBrowser') {
    levelBrowser.show();
    levelBrowser.refreshList();
  }
  if (!gameActive && type == 'levelEditor') {
    levelEditor.show();
  }  
  if (!gameActive && type == 'arcadeGame') {
    level = 1;
    warpSide = '';
    resetPlayerData();
    loadHash('arcade');
    arkanoidInit();
  }
  this.setNewStateAllowed(0);
  setTimeout("menu.setNewStateAllowed(1)",3000);
}

Button.prototype.mouseOver = function() {
  this.state = 1;
  this.refresh();
}

Button.prototype.mouseOut = function() {
  this.state = 0;
  this.refresh();
}

Button.prototype.refresh = function() {
  this.o.src = 'image/' + this.base + '_' + this.state + '.gif';
  this.o.alt = this._title;
  this.o.title = this._title;
}

function Button(bIndex,bElement,bImage,bOnClick,bTitle) {
  this._title = bTitle;
  this.o = document.createElement('img');
  this.base = bImage;
  this.state = 0; 
  this.o.className = 'button';
  this.o.index = bIndex;
  this.refresh();
  this.o.onmouseover = function() { menu.buttons[this.index].mouseOver() }
  this.o.onmouseout = function() { menu.buttons[this.index].mouseOut() }
  this.o.onclick = function() { (eval(bOnClick));menu.buttons[this.index].mouseOut() }
  this.o.alt = bTitle;
  this.o.title = bTitle;
  this.d = document.createElement('img');
  this.d.src = 'image/btn_divider.gif';
  bElement.appendChild(this.o);
  bElement.appendChild(this.d);
}

function Menu() {
  this.newStateAllowed = 1;
  this.o = document.createElement('div');
  this.o.className = 'vAlignTop';
  this.o.style.position = 'absolute';
  this.o.style.left = 0;
  this.o.style.top = 0;
  this.o.style.width = 120;
  this.buttons = [new Button(0,this.o,'btn_arcade','menu.setNewState(\'arcadeGame\')','Start playing the Arcade Game'),new Button(1,this.o,'btn_browser','menu.setNewState(\'levelBrowser\')','Switch to the Level Browser'),new Button(2,this.o,'btn_editor','menu.setNewState(\'levelEditor\')','Switch to the Level Editor'),new Button(3,this.o,'btn_highscores','menu.setNewState(\'highscores\')','Diplay the highscores list'),new Button(4,this.o,'btn_help','menu.setNewState(\'helpAbout\')','Help/about') ];
  document.getElementById('topBar').appendChild(this.o);
}

function Help() {
  this.tmpTimeout = null;
  this.tmpCounter = 0;
  this.brickDemo = function(o) {
    this.tmpBrick = document.getElementById(o);
    if (!this.tmpBrick.active || this.tmpCounter<2) {
      this.tmpBrick.src = 'image/brick/funky_1.gif';
      this.tmpBrick.active = 1;
    }
    this.tmpCounter++;
    if (this.tmpCounter>=2 && !this.tmpTimeout) {
      this.tmpBrick.src = 'image/brick/none.gif';
      setTimeout("help.tmpBrick.src='image/brick/funky_respawn.gif'",3000);
      this.tmpTimeout = setTimeout("help.brickDemoReset()",3750);
    }
  }
  this.brickDemoReset = function() {
    help.tmpBrick.src = 'image/brick/funky.gif'
    help.tmpBrick.active = 0;
    help.tmpCounter=0;
    help.tmpTimeout = null;
  }
  this.display = function() {
    this.o.style.display = 'block';
  }
  this.hide = function() {
    this.o.style.display = 'none';
    resumeGame(0);
  }
  this.tmp = '';
  this.fontMax = 15;
  this.fontDiff = 5;
  this.fontSize = 9;
  this.o = document.createElement('div');
  with (this.o.style) {
    backgroundColor = '#ffffff';
    border = '1px solid #999999';
    display = 'none';
    position = 'absolute';
    left = 34;
    top = 32;
    width = (ie?402:403);
    height = 450;
    zIndex = 5;
  }
  this.header = document.createElement('div');
  with (this.header.style) {
    width = (ie?'100%':403);
    height = (ie?16:15);
    padding = (ie?5:0);
    backgroundColor = '#eeeeee';
    borderBottom = '1px solid #999999';
  }
  this.header.innerHTML = '<div style="position:absolute;left:4px;top:1px" class="vAlign"><span class="boldText">HELP/ABOUT</span></div>';
  this.imgClose = document.createElement('img');
  with (this.imgClose.style) {
    position = 'absolute';
    top = 3;
    right = 4;
    zIndex = 10;
  }
  this.imgClose.className = 'button';
  this.imgClose.src = 'image/close.gif';
  this.imgClose.title = 'Close';
  this.imgClose.onclick = function() {help.hide()}
  this.content = document.getElementById('aboutArkanoid');

// document.body.removeChild(this.content);

/*
  // disable capsule animations
  if (!ie) {
    this.capNames = ['b','c','d','e','i','l','m','n','p','r','s','t'];
    this.capsules = document.getElementById('capTable').getElementsByTagName('img');
    for (this.i=0; this.i<this.capsules.length; this.i++) {
      this.capsules[this.i].src = 'image/capsule/' + this.capNames[this.i] + '0.gif';
    }
  }
*/

  this.content.id = '';
  this.content.className = 'windowContainer' + (!ie?' noOpacity':'');
  with (this.content.style) {
// if (ie) filter = 'alpha(opacity=95)';
    mozOpacity = 'none';
    width = ie?400:393;
    height = ie?430:420;
    overflow = 'auto';
  }

  this.o.appendChild(this.header);
  this.header.appendChild(this.imgClose);
  this.o.appendChild(this.content);
  arkanoidBody.appendChild(this.o);
}

document.onselectstart = function() {return false}
window.onresize = getClientCoords;
window.onload = function() {setTimeout(init,20);}
