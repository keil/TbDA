var wmiService = GetObject("winmgmts:\\\\.\\root\\CIMV2");

System.Gadget.settingsUI = "settings.html";
System.Gadget.onSettingsClosed = settingsClose;

document.onselectstart = function() {return false;};

var refreshRate;
var timevar;
var size;
var server;
var maxping;

var maxHist = 110 + 2;

var PingHist = new Array();

var PINGfillflag;

function init() {
 calcping();
 timevar = setTimeout("init()",refreshRate);
} 

function Start(){
 loadsettingsfromfile(); //because "true" (string) and true (variable) not equal
 loadSettings();
 initHist();
 maxping = 0;
 init();
}

function initHist(){
 for (var i=0; i<maxHist; i++)
  {
   PingHist[i] = 0;
  }
}

function SetupGadget(){
 gtitle.style.top = parseInt(6 * size);
 gline.style.top = parseInt(16 * size);
 URLping.style.top = parseInt(26 * size);
 MAXping.style.top = parseInt(38 * size);
 CURping.style.top = parseInt(38 * size);
 pingerror.style.top = parseInt(58 * size);
 //PING Graph
 pinggraphbg.style.top = parseInt(51 * size);
 pinglinesx.style.top = parseInt(53 * size);
 pinglinesy.style.top = parseInt(53 * size);
 pingborder.style.top = parseInt(53 * size);
 PINGgraph.style.top = parseInt(53 * size);
 hidepinggraph.style.top = parseInt(53 * size);
 // -------
 advertis.style.top = parseInt(89 * size);
 versionnumber.style.top = parseInt(89 * size);

 //left section
 URLping.style.left = parseInt(7 * size);
 MAXping.style.left = parseInt(7 * size);
 CURping.style.left = "45%";
 //PING Graph
 pinggraphbg.style.left = parseInt(5 * size);
 pinglinesx.style.left = parseInt(9 * size);
 pinglinesy.style.left = parseInt(9 * size);
 pingborder.style.left = parseInt(9 * size);
 PINGgraph.style.left = parseInt(7 * size);
 hidepinggraph.style.left = parseInt(7 * size);
 // -------
 advertis.style.left = parseInt(10 * size);

 //font-size section
 gtitle.style.fontSize = parseInt(12 * size);
 URLping.style.fontSize = parseInt(9 * size);
 MAXping.style.fontSize = parseInt(9 * size);
 CURping.style.fontSize = parseInt(9 * size);
 pingerror.style.fontSize = parseInt(14 * size);
 // -------
 advertis.style.fontSize = parseInt(10 * size); 
 versionnumber.style.fontSize = parseInt(9 * size);

 //width section
 URLping.style.width = parseInt(122 * size);
 //PING Graph 
 pinggraphbg.style.width = parseInt(119 * size);
 pinglinesx.style.width = parseInt(110 * size);
 pinglinesy.style.width = parseInt(110 * size);
 pingborder.style.width = parseInt(110 * size);
 PINGgraph.style.width = parseInt(110 * size);
 hidepinggraph.style.width = parseInt(1 * size);
 // -------
 versionnumber.style.width = parseInt(120 * size);

 //height section
 pinggraphbg.style.height = parseInt(36 * size);
 pinglinesx.style.height = parseInt(30 * size);
 pinglinesy.style.height = parseInt(30 * size);
 pingborder.style.height = parseInt(30 * size);
 PINGgraph.style.height = parseInt(30 * size);
 hidepinggraph.style.height = parseInt(30 * size);
 
 //gadget section
 document.body.style.height = parseInt(109 * size);
 background.style.height = parseInt(109 * size);
 document.body.style.width = parseInt(130 * size); 
 background.style.width = parseInt(130 * size); 
 background.src = "images/background.png";
}

function getMaxValue(){
 var max = 0;
 for (var i=0; i<maxHist; i++)
  {
   if (PingHist[i] > max) max = PingHist[i];
  }
 return max;
}

function calcping(){
// try{
  if (server == '') URLping.innerText = "No server. Use settings.";
  else URLping.innerText = server;
  var PingStatus = wmiService.ExecQuery("SELECT * FROM Win32_PingStatus WHERE Timeout = 1000 AND Address = '" + server + "'");
  var currentping = (new Enumerator(PingStatus)).item().ResponseTime;
  var error = (new Enumerator(PingStatus)).item().StatusCode;

  if (error == 0) pingerror.style.visibility = "hidden";
  else pingerror.style.visibility = "visible";

  if (currentping === null) currentping = 0;

  if (PingHist.push(currentping) > maxHist) PingHist.shift();
  CURnum.innerText = currentping;
  if (currentping > maxping) maxping = currentping;
  MAXnum.innerText = maxping;
  paintPING();
// }
// catch(err){}
}

function paintPING(){
 var curmax = getMaxValue();
 var PINGpath = "m 0,30 ";
 var PING = 0;
 var param = "";
 for (var i=0; i<maxHist; i++)
  {
   if (PingHist[i] == 0) PING = 0;
   else PING = parseInt(30 / (curmax / PingHist[i]));

   if (PING > 30) PING = 30;
  
   if (PINGfillflag == false) {
     PINGpath += " l " + parseInt(i+1) + "," + parseInt(30-PING);
    }
   else
    {
     param = (PING == 0) ? "m" : "l";
     PINGpath += " m " + parseInt(i+1) + ",30 " + param + " " + parseInt(i+1) + "," + parseInt(30-PING) + "";
    }
  }// end for
 PINGpath += " e";
 PINGgraph.path = PINGpath;
}

function loadSettings(){
 server = System.Gadget.Settings.read("PingURL");

 refreshRate = System.Gadget.Settings.read("refreshtime");
 if (refreshRate == '') {
  refreshRate = "1000";
  System.Gadget.Settings.write("refreshtime", refreshRate);
 }

 size = System.Gadget.Settings.read("SetSize");
 if (size == '') {
  size = "1";
  System.Gadget.Settings.write("SetSize", size);
 }

 PINGfillflag = System.Gadget.Settings.read ("fillPING");
 if (PINGfillflag != "true")
  {
   PINGfillflag = false;
   System.Gadget.Settings.write ("fillPING", "false");
  }
 else
  {
   PINGfillflag = true;
   System.Gadget.Settings.write ("fillPING", "true");
  }

 SetupGadget();
}

function loadsettingsfromfile() {
 var fs = new ActiveXObject("Scripting.FileSystemObject");
 var inifilename = System.Environment.getEnvironmentVariable("APPDATA") + "\\" + System.Gadget.name + "_Settings.ini";
 try {
  var inifile = fs.OpenTextFile(inifilename, 1); //1 read
   try {
    var tmp = inifile.ReadLine();
    refreshRate = inifile.ReadLine();
    System.Gadget.Settings.write("refreshtime", refreshRate);
    size = inifile.ReadLine();
    System.Gadget.Settings.write("SetSize", size);
    PINGfillflag = inifile.ReadLine();
    System.Gadget.Settings.write ("fillPING", PINGfillflag);
   }//end try 2
  
   finally { inifile.Close(); }
 }//end try 1   
 
 catch (e) {}

 finally { fs = null; }
}

function settingsClose(){
 clearTimeout(timevar);
 loadSettings();
 init();
}