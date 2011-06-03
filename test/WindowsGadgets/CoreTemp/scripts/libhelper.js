/////////////////////////////////////////////////////////////////////////////////////
//                                                                                 //
//  Libhelper - .Net ActiveX Object loader by Arthur Liberman © 2010               //
//                                                                                 //
//  Copyright © 2010 Arthur Liberman. All rights reserved.                         //
//                                                                                 //
//  http://www.alcpu.com/CoreTemp                                                  //
//                                                                                 //
//  Email: arthur_liberman@hotmail.com                                             //   
//                                                                                 //
/////////////////////////////////////////////////////////////////////////////////////

// ################## CONFIGURATION ###################
var dllCLSID = "{083f5ae0-2b0a-11dd-bd0b-0800200c9a66}";
var Classname = "CoreTempReader.Reader";
var LibPath = "file:///" + System.Gadget.path.replace(new RegExp("\\\\", "g"), "/") + "/CoreTempReader.dll";
var LibName = "CoreTempReader";
// ####################################################

var debugOn = false;
var oShell = new ActiveXObject("WScript.Shell");

function RegisterLibrary(regRoot) {
    debugLog("Register " + regRoot);
    var classRoot = regRoot + "\\Software\\Classes\\" + Classname + "\\";
    var clsidRoot = regRoot + "\\Software\\Classes\\CLSID\\" + dllCLSID + "\\";

    var object;
    try {        
        oShell.RegWrite(classRoot, Classname, "REG_SZ");        
        oShell.RegWrite(classRoot + "CLSID\\", dllCLSID, "REG_SZ");        
        oShell.RegWrite(clsidRoot, Classname, "REG_SZ");        
        oShell.RegWrite(clsidRoot + "InprocServer32\\", "mscoree.dll", "REG_SZ");        
        oShell.RegWrite(clsidRoot + "InprocServer32\\ThreadingModel", "Both", "REG_SZ");        
        oShell.RegWrite(clsidRoot + "InprocServer32\\Class", Classname, "REG_SZ");        
        oShell.RegWrite(clsidRoot + "InprocServer32\\Assembly", LibName + ", Version=1.0.2588.9125, Culture=neutral, PublicKeyToken=null", "REG_SZ");        
        oShell.RegWrite(clsidRoot + "InprocServer32\\RuntimeVersion", "v2.0.50727", "REG_SZ");        
        oShell.RegWrite(clsidRoot + "InprocServer32\\CodeBase", LibPath, "REG_SZ");        
        oShell.RegWrite(clsidRoot + "InprocServer32\\1.0.2588.9125\\Class", Classname, "REG_SZ");        
        oShell.RegWrite(clsidRoot + "InprocServer32\\1.0.2588.9125\\Assembly", LibName + ", Version=1.0.2588.9125, Culture=neutral, PublicKeyToken=null", "REG_SZ");        
        oShell.RegWrite(clsidRoot + "InprocServer32\\1.0.2588.9125\\RuntimeVersion", "v2.0.50727", "REG_SZ");        
        oShell.RegWrite(clsidRoot + "InprocServer32\\1.0.2588.9125\\CodeBase", LibPath, "REG_SZ");        
        oShell.RegWrite(clsidRoot + "ProgId\\", Classname, "REG_SZ");        
        oShell.RegWrite(clsidRoot + "ProgId\\Implemented Categories\\{df21d370-2b10-11dd-bd0b-0800200c9a66}\\", "", "REG_SZ");
        object = new ActiveXObject(Classname);
    }
    catch (err) {
        object = null;
        debugLog("RegisterLibrary: " + err.name + " - Number: " +  err.number + "; Description: " + err.description + "; Message: " + err.message);
    }
    return object;
}

function UnregisterLibrary(regRoot) {
    var classRoot = regRoot + "\\Software\\Classes\\" + Classname + "\\";
    var clsidRoot = regRoot + "\\Software\\Classes\\CLSID\\" + dllCLSID + "\\";
    try {
        oShell.RegDelete(clsidRoot + "ProgId\\Implemented Categories\\{df21d370-2b10-11dd-bd0b-0800200c9a66}\\");
        oShell.RegDelete(clsidRoot + "ProgId\\Implemented Categories\\");
        oShell.RegDelete(clsidRoot + "ProgId\\");
        oShell.RegDelete(clsidRoot + "InprocServer32\\1.0.2588.9125\\");
        oShell.RegDelete(clsidRoot + "InprocServer32\\");
        oShell.RegDelete(clsidRoot);
        oShell.RegDelete(classRoot + "CLSID\\");
        oShell.RegDelete(classRoot);
    }
    catch (err) {
        return false;
    }
    return true;
}

function FreeLibrary() {
    var ret;
    ret = UnregisterLibrary("HKCU");
    if (!ret) {
        ret = UnregisterLibrary("HKLM");
    }
    return ret;
}

function GetLibrary() {
    var Lib;
    debugLog("");
    debugLog("GetLibrary");
    Lib = RegisterLibrary("HKCU");
    if (Lib == null) {
        UnregisterLibrary("HKCU");
        Lib = RegisterLibrary("HKLM");
    }
    if (Lib == null) {
        UnregisterLibrary("HKLM");
    }
    return Lib;
}

// Debuglog
function debugLog(str) {
    try {
        System.Debug.outputString(str);
        if (debugOn) {
            var oFSO = new ActiveXObject("Scripting.FileSystemObject");
            var iFlag = 2;
            if (str != "")
                iFlag = 8;

            var debugLogFile = oFSO.OpenTextFile(System.Gadget.path + "\\debug.txt", iFlag);
            debugLogFile.WriteLine(new Date().toLocaleString() + ": " + str);
            debugLogFile.Close();
            debugLogFile = null;
        }
    }
    catch (err) {
    }
}