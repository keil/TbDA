System.Gadget.onSettingsClosing = SettingsClosing;

function validateNumber(field) {
    var key = window.event ? event.keyCode : event.which;

    if (event.keyCode == 8 || event.keyCode == 46
     || event.keyCode == 37 || event.keyCode == 39) {
        window.event.returnValue = true;
        return true;
    }
    else if (key < 48 || key > 57) {
        window.event.returnValue = false;
        return false;
    }
    else
        window.event.returnValue = true;
    return true;
}

function LoadSettings(){

 server.value=System.Gadget.Settings.read("PingURL");
 timetorefresh.value = System.Gadget.Settings.read("refreshtime") / 1000;
 size.value = System.Gadget.Settings.read("SetSize") * 100;
 

 var tmp;
 tmp = System.Gadget.Settings.read ("fillPING");
 if (tmp == "false") fillgraph.checked = false;
 else fillgraph.checked = true;
}

function SettingsClosing(event){
 if (event.closeAction == event.Action.commit)
  {
   System.Gadget.Settings.write("PingURL", server.value);
   System.Gadget.Settings.write("refreshtime", timetorefresh.value * 1000);
   System.Gadget.Settings.write("SetSize", size.value / 100);

   if (fillgraph.checked) {
     System.Gadget.Settings.write ("fillPING", "true");}
   else {
     System.Gadget.Settings.write ("fillPING", "false");}

   savesettingstofile();
  }
  
 event.cancel = false;
}

function savesettingstofile() {
 var fs = new ActiveXObject("Scripting.FileSystemObject");
 var inifilename = System.Environment.getEnvironmentVariable("APPDATA") + "\\" + System.Gadget.name + "_Settings.ini";
 try {
  var inifile = fs.OpenTextFile(inifilename, 2, true); //2 write file true - create file

  try {
   inifile.WriteLine(";Ping Monitor (c) 2010 by Igor 'Igogo' Bushin");
   inifile.WriteLine(timetorefresh.value * 1000);
   inifile.WriteLine(size.value / 100);
   if (fillgraph.checked) { inifile.WriteLine("true"); }
   else { inifile.WriteLine("false"); }
  }//end try 2

  finally { inifile.Close(); }
 }//end try 1

 catch (e) {}
}