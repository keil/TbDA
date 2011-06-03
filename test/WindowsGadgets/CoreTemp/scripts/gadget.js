/////////////////////////////////////////////////////////////////////////////////////
//                                                                                 //
//  Core Temp Gadget by Arthur Liberman © 2010                                     //
//                                                                                 //
//  Copyright © 2010 Arthur Liberman.  All rights reserved.                        //
//                                                                                 //
//  http://www.alcpu.com/CoreTemp                                                  //
//                                                                                 //
//  Email: arthur_liberman@hotmail.com                                             //   
//                                                                                 //
//  interface ICoreTempInterface                                                   //
//  {                                                                              //
//        void Refresh();                                                          //
//        int GetLoad(int Index);                                                  //
//        int GetTjMax(int Index);                                                 //
//        int CoreCount { get; }                                                   //
//        int CPUCount { get; }                                                    //
//        float GetTemp(int Index);                                                //
//        float VID { get; }                                                       //
//        float CPUSpeed { get; }                                                  //
//        float FSBSpeed { get; }                                                  //
//        float Multiplier { get; }                                                //
//        string CPUName { get; }                                                  //
//        bool Fahrenheit { get; }                                                 //
//        bool DeltaToTjMax { get; }                                               //
//        int RAMTotal { get; }                                                    //
//        int RAMUsed { get; }                                                     //
//        int RAMLoad { get; }                                                     //
//  }                                                                              //
//                                                                                 //
/////////////////////////////////////////////////////////////////////////////////////

var CoreTemp;
var currentVersion = parseFloat(System.Gadget.version), newVersion = 0;
var CoreTempActive = false;
var ElementsBuilt = false;
var historyArrays = new Array();
var maxValueArray = new Array();
var stockColors = new Array();
var cpuVendor = "";

var colors = ["#FFD800", "#B6FF00", "#4CFF00", "#00FF21", "#00FF90", "#00FFFF", "#00B2FF", "#0065FF",
              "#6B3AFF", "#B200FF", "#FF00DC", "#FF006E", "#FF0000", "#7F0000", "#7F3300", "#FF6A00", "#FFFFFF"];

function onLoad() {
}

function onTimer()
{
    refreshDisplay();
}

function createCoreInfoElements() {
    var coreCount, coresPerCpu;
    try {
        CoreTemp.Refresh();
        coreCount = CoreTemp.CoreCount;
        coresPerCpu = coreCount / CoreTemp.CPUCount;
    }
    catch (err) {
        cpuName.innerHTML = err.message;
        coreCount = 0;
        coresPerCpu = 1;
    }

    var colorOffset;
    switch (coreCount) {
        case 2:
            colorOffset = 4;
            break;
        case 3:
            colorOffset = 3;
            break;
        case 4:
            colorOffset = 2;
            break;
        default:
            colorOffset = Math.max(1, parseInt((colors.length / coreCount)));
            break;
    }
    var element = document.getElementById("Core0");
    stockColors.push(0);
    element.style.color = colors[0];
    element.childNodes[4].style.backgroundColor = colors[0];
    var i, newCore, newElement;
    for (i = 1; i <= coreCount; i++) {
        newCore = element.cloneNode(true);
        newCore.id = "Core" + i;
        newCore.style.pixelTop = element.style.pixelTop + (12 * i);
        stockColors.push((i * colorOffset) % colors.length);
        newCore.style.color = colors[stockColors[i]];
        newCore.childNodes[4].style.backgroundColor = colors[stockColors[i]];
        Cores.insertBefore(newCore, Cores.lastChild);
    }
    newCore.style.color = "white";
    newCore.childNodes[4].style.backgroundColor = "white";
    stockColors[coreCount] = colors.length - 1; // White

    var graphs = Graphs;
    graphs.style.pixelTop = newCore.style.pixelTop + 18;
    element = document.getElementById("Core0Graph");
    element.strokecolor = colors[0];
    historyArrays.push(new Array());
    for (i = 1; i <= coreCount; i++) {
        historyArrays.push(new Array());
        newCore = element.cloneNode(true);
        newCore.id = "Core" + i + "Graph";

        // Copy the custom fields over.
        newCore.strokecolor = colors[stockColors[i]];
        newCore.filled = element.filled;
        newCore.strokeweight = element.strokeweight;
        newCore.fillcolor = element.fillcolor;
        newCore.coordorigin = element.coordorigin;
        newCore.path = element.path;
        newCore.coordsize = element.coordsize;

        graphs.insertBefore(newCore, graphs.lastChild);
    }

    newCore.strokecolor = "white";
    ElementsBuilt = true;
}

function refreshDisplay() {

    try {
        CoreTemp.Refresh();
        coreCount = CoreTemp.CoreCount;
    }
    catch (err) {
        coreCount = 0;
        cpuName.innerHTML = err.message;
    }

    if (coreCount == 0 && CoreTempActive) {
        CoreTempActive = false;
        buildGUI(false);
    }
    else if (coreCount > 0 && !CoreTempActive) {
        CoreTempActive = true;
        buildGUI(true);
    }

    if (!CoreTempActive) {
        cpuName.innerHTML = showCpuLogo ? "Core Temp is off." : "Core Temp is not running.";
        CoreTempActive = false;
        return;
    }

    // Core temperature and load.
    var i;
    var temp, load;
    var tempLabel, loadLabel, bar;
    var ratio = (zoomPercent / 100.0) * 0.6;
    // CPU speed, VID and RAM information
    try {
        cpuName.innerHTML = CoreTemp.CPUName;
        if (showCpuLogo) {
            switch (cpuVendor) {
                case "Intel":
                    cpuName.innerHTML = cpuName.innerHTML.replace("Intel ", "");
                    break;
                case "AMD":
                    cpuName.innerHTML = cpuName.innerHTML.replace("AMD ", "");
                    break;
                case "VIA":
                    cpuName.innerHTML = cpuName.innerHTML.replace("VIA ", "");
                    break;
            }
        }
        cpuClock.innerHTML = "Clock: " + CoreTemp.CPUSpeed.toFixed(0) + "Mhz(" + CoreTemp.FSBSpeed.toFixed(2) + "x" + CoreTemp.Multiplier.toFixed(1) + ")";
        cpuInfo.innerHTML = "VID: " + CoreTemp.VID.toFixed(4) + "V - TjMax: " + CoreTemp.GetTjMax(0) + (CoreTemp.Fahrenheit ? "&ordm;F" : "&ordm;C");
    }
    catch (err) {
        cpuName.innerHTML = err.message;
    }

    for (i = 0; i <= coreCount; i++) {
        try {
            tempLabel = document.getElementById("Core" + i).childNodes[0];
            loadLabel = document.getElementById("Core" + i).childNodes[1];
            bar = document.getElementById("Core" + i).childNodes[4];

            if (i < coreCount) {
                temp = parseInt(CoreTemp.GetTemp(i));
                load = CoreTemp.GetLoad(i);
                if (showTemps) {
                    historyArrays[i].unshift(CoreTemp.GetTemp(i));
                }
                else {
                    historyArrays[i].unshift(CoreTemp.GetLoad(i));
                }

                tempLabel.innerHTML = (temp == 0) ? "Core #" + i : "[ " + temp + (CoreTemp.Fahrenheit ? "&ordm;F" : "&ordm;C") + " ] ";
            }
            else {
                load = CoreTemp.RAMLoad;
                historyArrays[coreCount].unshift(load);
                tempLabel.innerHTML = "[ RAM ]";
                ramInfo.innerHTML = "RAM Used: " + CoreTemp.RAMUsed + " / " + CoreTemp.RAMTotal + "MB";
            }
            loadLabel.innerHTML = load + "%";
            bar.style.pixelWidth = parseInt(load * ratio);
            //var anim = new animation(bar, parseInt(load), 300);
            //anim.start();
            drawGraph(i);
        }
        catch (err) {
            cpuName.innerHTML = err.message;
        }
    }
}

function drawGraph(index) {
    var graph = document.getElementById("Core" + index + "Graph");
    if (graph) {
        var array = historyArrays[index];
        var maxValue = graph.coordsize.y;
        var maxCount = (array.length < 140) ? array.length : 140;
        var path = "m 140," + (maxValue - array[0]) + " l ";
        for (var i = 1; i <= maxCount; i++) {
            path += (140 - i) + "," + (maxValue - array[i - 1]) + ", ";
        }
        graph.path = path;
    }
}

function buildGUI(Show) {
    var coreCount, coresPerCpu;
    var showTempsOld = showTemps;
    try {
        loadSettings();
        coreCount = CoreTemp.CoreCount;
        coresPerCpu = coreCount / CoreTemp.CPUCount;
    }
    catch (err) {
        cpuName.innerHTML = err.message;
        coreCount = coresPerCpu = 0;
    }

    if (Show && coreCount && !ElementsBuilt) {
        createCoreInfoElements();
    }

    setCpuLogo();
    if (showTemps != showTempsOld) {
        for (i = 0; i < coreCount; i++) {
            historyArrays[i] = new Array();
        }
    }

    gadgetTitle.style.color = titleColor;
    cpuName.style.color = cpuNameColor;
    cpuClock.style.color = clockColor;
    cpuInfo.style.color = vidColor;
    ramInfo.style.color = ramColor;

    var gadgetHeight = 151;
    var visibility = Show ? "visible" : "hidden"
    getCoreTemp.style.visibility = Show ? "hidden" : "visible";
    Cores.style.visibility = visibility;
    Graphs.style.visibility = visibility;
    if (!showGraph) {
        Graphs.style.visibility = "hidden";
    }
    gadgetHeight = Show ? gadgetHeight + 12 * coreCount : 74;
    try {
        var maxTemp;
        for (i = 0; i <= coreCount; i++) {
            var core = document.getElementById("Core" + i);
            var coreGraph = document.getElementById("Core" + i + "Graph");
            switch (setting) {
                case 0:
                    core.style.color = colors[stockColors[i]];
                    core.childNodes[4].style.backgroundColor = colors[stockColors[i]];
                    coreGraph.strokecolor = colors[stockColors[i]];
                    break;
                case 1:
                    core.style.color = coreColors[i];
                    core.childNodes[4].style.backgroundColor = coreColors[i];
                    coreGraph.strokecolor = coreColors[i];
                    break;
                case 2:
                    core.style.color = singleColor;
                    core.childNodes[4].style.backgroundColor = singleColor;
                    coreGraph.strokecolor = singleColor;
                    break;
            }
            if (i == coreCount) {
                continue;
            }
            if (showTemps) {
                try {
                    maxTemp = CoreTemp.GetTjMax(i / coresPerCpu);
                }
                catch (err) {
                    maxTemp = 100;
                    cpuName.innerHTML = err.message;
                }
                maxTemp = (maxTemp > 0) ? maxTemp : 100;
                coreGraph.coordsize.y = maxTemp;
            }
            else {
                coreGraph.coordsize.y = 100;
            }
        }

        zoom(zoomPercent);
        cpuName.style.visibility = showCpuName ? "visible" : "hidden";
        cpuClock.style.visibility = showClocks ? "visible" : "hidden";
        cpuInfo.style.visibility = showVid ? "visible" : "hidden";
        ramInfo.style.visibility = showRam ? "visible" : "hidden";
        if (!Show) {
            var ratio = (zoomPercent / 100.0);
            background.style.pixelHeight = gadgetHeight * ratio;
            document.body.style.pixelHeight = gadgetHeight * ratio;
            resize(cpuName, 152, 16, 4, 23, 12, null, ratio);
            cpuName.style.visibility = "visible";
            cpuName.innerHTML = showCpuLogo ? "Core Temp is off." : "Core Temp is not running.";
            cpuClock.innerHTML = "";
            cpuInfo.innerHTML = "";
            ramInfo.innerHTML = "";
        }
        if (newVersion > 0) {
            gadgetVersion.style.visibility = "visible";
        }
        else {
            gadgetVersion.style.visibility = "hidden";
        }
    }
    catch (err) {
        cpuName.innerHTML = err.message;
    }
}

function setCpuLogo() {
    if (showCpuLogo) {
        cpuLogo.style.visibility = "visible";
        var size = 32, logoFile = "cpu";
        if (zoomPercent < 125) {
            size = 32;
        }
        else if (zoomPercent < 175) {
            if (showCpuName) {
                size = 48;
            }
            else {
                size = 32;
            }
        }
        else if (zoomPercent < 300) {
            if (showCpuName) {
                size = 64;
            }
            else {
                size = 32;
            }
        }
        else {
            if (showCpuName) {
                size = 128;
            }
            else {
                size = 64;
            }
        }
        if (!showDefaultLogo) {
            try {
                cpuVendor = CoreTemp.CPUName;
                if (cpuVendor.indexOf("Intel") >= 0) {
                    cpuVendor = "Intel";
                }
                else if (cpuVendor.indexOf("AMD") >= 0) {
                    cpuVendor = "AMD";
                }
                else if (cpuVendor.indexOf("VIA") >= 0) {
                    cpuVendor = "VIA";
                }
            }
            catch (err) {
                cpuVendor = "";
            }
            switch (cpuVendor) {
                case "Intel":
                    logoFile = "intel";
                    break;
                case "AMD":
                    logoFile = "amd";
                    break;
                case "VIA":
                    logoFile = "via";
                    break;
                default:
                    logoFile = "cpu";
                    break;
            }
        }

        cpuLogo.src = "images/logos/" + logoFile + size + ".png";
    }
    else {
        cpuLogo.style.visibility = "hidden";
    }
}

function zoom(percent) {
    var core, coreCount;
    var ratio = (percent / 100.0);
    try {
        coreCount = CoreTemp.CoreCount;
    }
    catch (err) {
        coreCount = 0;
        cpuName.innerHTML = err.message;
    }
    resize(getCoreTemp, 144, null, 8, 40, 13, null, ratio);
    var top = 7;
    resize(gadgetTitle, 152, null, 4, top, 14, null, ratio);
    top += 14;
    if (showCpuLogo) {
        if (showCpuName) {
            cpuName.style.paddingLeft = 32 * ratio;
            gadgetTitle.style.paddingLeft = 32 * ratio;
            resize(cpuLogo, 32, 32, 8, 8, null, null, ratio);
        }
        else {
            cpuName.style.paddingLeft = 16 * ratio;
            gadgetTitle.style.paddingLeft = 16 * ratio;
            resize(cpuLogo, 16, 16, 8, 8, null, null, ratio);
        }
    }
    else {
        cpuName.style.paddingLeft = 0;
        gadgetTitle.style.paddingLeft = 0;
    }
    if (showCpuName) {
        top += 3;
        resize(cpuName, 152, 16, 4, top, 12, null, ratio);
        top += 12;
    }
    if (showClocks) {
        top += 3;
        resize(cpuClock, 152, null, 4, top, 11, null, ratio);
        top += 11;
    }
    if (showVid) {
        top += 3;
        resize(cpuInfo, 152, null, 4, top, 11, null, ratio);
        top += 11;
    }
    if (showRam) {
        top += 3;
        resize(ramInfo, 152, null, 4, top, 11, null, ratio);
        top += 11;
    }

    top += 3;
    for (var i = 0; i <= coreCount; i++) {
        core = document.getElementById("Core" + i);
        resize(core, 152, null, 0, top, 11, null, ratio);
        resize(core.childNodes[0], null, null, 79, 0, null, null, ratio);
        resize(core.childNodes[1], 152, null, 0, 0, null, null, ratio);
        resize(core.childNodes[2], 60, 8, 8, 4, null, null, ratio);
        resize(core.childNodes[4], 25, 8, 8, 4, null, null, ratio);
        core.childNodes[4].tag = 25;
        resize(core.childNodes[4].childNodes[0], 60, 8, null, null, null, null, ratio);
        top += 12;
    }
    var coreGraphs = Graphs;
    var graph;
    top += 6;
    resize(coreGraphs, 160, 42, 0, top, null, null, ratio);
    for (var i = 0; i < coreGraphs.childNodes.length; i++) {
        graph = coreGraphs.childNodes[i];
        resize(graph, 142, 42, 8, 0, null, null, ratio);;
        graph.style.pixelWidth -= 2;
        graph.style.pixelHeight -= 2;
        graph.style.pixelTop++;
        graph.style.pixelLeft++;
    }
    resize(graphsFrame, 142, 42, 8, 0, null, null, ratio);
    if (coreGraphs.style.visibility == "visible") {
        top += 51;
    }
    top += 2;
    if (newVersion > 0) {
        top -= 8;
        resize(gadgetVersion, 144, null, 8, top, 13, null, ratio);
        top += 25;
    }
        
    resize(background, 160, top, null, null, null, null, ratio);
    resize(document.body, 160, top, null, null, null, null, ratio);
}

function resize(element, width, height, left, top, fontsize, coordsize, zoomRatio) {
    if (element) {
        if (width != null) {
            element.style.pixelWidth = width * zoomRatio;
        }
        if (height != null) {
            element.style.pixelHeight = height * zoomRatio;
        }
        if (left != null) {
            element.style.pixelLeft = left * zoomRatio;
        }
        if (top != null) {
            element.style.pixelTop = top * zoomRatio;
        }
        if (fontsize != null) {
            element.style.fontSize = fontsize * zoomRatio;
        }
        if (coordsize != null) {
            element.coordsize = coordsize * zoomRatio;
        }
    }
}

function enableUpdateNotifications(value) {
    if (value && updateTimerId == null) {
        updateTimerId = setTimeout("onTimerCheckUpdates()", 5000);
    }
    else if (!value && updateTimerId != null) {
        clearInterval(updateTimerId);
        updateTimerId = null;
        newVersion = 0;
        buildGUI(CoreTempActive);
    }
}

document.onreadystatechange = function () {
    if (document.readyState == "complete") {
        CoreTemp = GetLibrary();
        if (CoreTemp == null) {
            buildGUI(false);
            cpuName.innerHTML = "Load library error!";
            getCoreTemp.style.visibility = "hidden";
            return;
        }

        System.Gadget.settingsUI = "settings.html";
        System.Gadget.Flyout.file = "flyout.html";
        System.Gadget.onSettingsClosed = settingsClosed;
        System.Gadget.onDock = dockStateChanged;
        System.Gadget.onUndock = dockStateChanged;
        try {
            CoreTemp.Refresh();
            buildGUI(true);
        }
        catch (err) {
            buildGUI(false);
        }
        onTimer();
        setInterval("onTimer()", 1000);
        enableUpdateNotifications(showUpdates);
    }
}

var firstDockStateChange = true;
function dockStateChanged() {
    background.src = "url(images/orb_back.png)";
    if (firstDockStateChange) {
        firstDockStateChange = false;
        return;
    }
    System.Gadget.beginTransition();

    if (System.Gadget.docked) {
        System.Gadget.Settings.write("showGraph", false);
    }
    else {
        System.Gadget.Settings.write("showGraph", true);
    }
    buildGUI(CoreTempActive);
    var timeTransition = 2;
    System.Gadget.endTransition(System.Gadget.TransitionType.morph, timeTransition);
}

// --------------------------------------------------------------------
// Handle the Settings dialog closed event.
// event = System.Gadget.Settings.ClosingEvent argument.
// --------------------------------------------------------------------
function settingsClosed(event) {
    // User hits OK on the settings page.
    if (event.closeAction == event.Action.commit) {
        buildGUI(CoreTempActive);
        refreshDisplay();
        enableUpdateNotifications(showUpdates);
    }
    // User hits Cancel on the settings page.
    else if (event.closeAction == event.Action.cancel) {
    }
}

var setting, showGraph, showTemps, coreColors, singleColor, zoomPercent;
var showCpuName, showClocks, showVid, showRam, showCpuLogo, showDefaultLogo, showUpdates;
var titleColor, cpuNameColor, clockColor, vidColor, ramColor;

function loadSettings() {
    setting = System.Gadget.Settings.read("setting");
    if (setting == "") {
        setting = 0;
    }

    showGraph = System.Gadget.Settings.read("showGraph");
    if (0 == showGraph.length) {
        showGraph = true;
    }

    showTemps = System.Gadget.Settings.read("showTemps");
    if (0 == showTemps.length) {
        showTemps = false;
    }

    showCpuLogo = System.Gadget.Settings.read("showCpuLogo");
    if (0 == showCpuLogo.length) {
        showCpuLogo = true;
    }

    showDefaultLogo = System.Gadget.Settings.read("showDefaultLogo");
    if (0 == showDefaultLogo.length) {
        showDefaultLogo = false;
    }

    showCpuName = System.Gadget.Settings.read("showCpuName");
    if (0 == showCpuName.length) {
        showCpuName = true;
    }

    showClocks = System.Gadget.Settings.read("showClocks");
    if (0 == showClocks.length) {
        showClocks = true;
    }

    showRam = System.Gadget.Settings.read("showRam");
    if (0 == showRam.length) {
        showRam = true;
    }

    showVid = System.Gadget.Settings.read("showVid");
    if (0 == showVid.length) {
        showVid = true;
    }

    showUpdates = System.Gadget.Settings.read("showUpdates");
    if (0 == showUpdates.length) {
        showUpdates = true;
    }

    zoomPercent = System.Gadget.Settings.read("zoomPercent");
    if (0 == zoomPercent.length) {
        zoomPercent = 100;
    }

    singleColor = System.Gadget.Settings.read("singleColor");
    if (0 == showTemps.length) {
        singleColor = colors[0];
    }

    var colorsStr = System.Gadget.Settings.readString("colors");
    if (colorsStr == "") {
        coreColors = new Array();
    }
    else {
        coreColors = colorsStr.split(',');
    }
    for (var i = coreColors.length; i < CoreTemp.CoreCount; i++) {
        coreColors.push(colors[i % colors.length]);
    }
    if (coreColors.length == CoreTemp.CoreCount) {
        coreColors.push(colors[16]); // Add white
    }

    titleColor = System.Gadget.Settings.read("titleColor");
    if (0 == titleColor.length) {
        titleColor = "#FFFFFF";
    }

    cpuNameColor = System.Gadget.Settings.read("cpuNameColor");
    if (0 == cpuNameColor.length) {
        cpuNameColor = "#7CCDFF";
    }

    clockColor = System.Gadget.Settings.read("clockColor");
    if (0 == clockColor.length) {
        clockColor = "#7CCDFF";
    }

    vidColor = System.Gadget.Settings.read("vidColor");
    if (0 == vidColor.length) {
        vidColor = "#7CCDFF";
    }

    ramColor = System.Gadget.Settings.read("ramColor");
    if (0 == ramColor.length) {
        ramColor = "#7CCDFF";
    }
}


function animation(bar, value, timeSpan) {
    this.bar = bar;
    this.ratio = (zoomPercent / 100.0) * 0.6;
    this.numOfSteps = Math.round((parseInt(value * this.ratio) - bar.style.pixelWidth) / this.ratio);
    if (1 > this.ratio) {
        this.numOfSteps = Math.round(this.numOfSteps * this.ratio);
    }
    this.stepSize = Math.max(1, this.ratio);
    if (this.numOfSteps > 20) {
        this.stepSize /= (this.numOfSteps / 20);
        this.numOfSteps = 20;
    }
    if (this.numOfSteps < 0) {
        this.stepSize *= -1;
    }
    this.numOfSteps = Math.abs(this.numOfSteps);
    this.stepSize = Math.round(this.stepSize);
    this.active = 0;
    this.timer = null;
    this.targetValue = value;                       // pointer to path object
    this.pathIdx = 0;                               // step counter
    this.tickInt = Math.min(25, Math.round(timeSpan / this.numOfSteps));      // tick interval in msec
    this.onStart = null;                            // hook for external action
    this.onStop = null;
}

animation.prototype.start = function () {
    if (this.active || this.numOfSteps == 0)
        return;

    var savThis = this;  // make a local copy for closure

    this.pathIdx = 0;  // start at beginning of path
    this.active = 1;
    this.step();
    if (this.numOfSteps > 1) {
        // create closure
        this.timer = setInterval(function () { savThis.step() }, savThis.tickInt);
    }
    if (this.onStart)
        this.onStart();
}

animation.prototype.resume = function () {
    if (this.active)
        return;

    var savThis = this;

    // this.pathIdx resumes where it was stopped
    this.step();
    this.active = 1;
    // create closure
    this.timer = setInterval(function () { savThis.step() }, savThis.tickInt);
    if (this.onStart)
        this.onStart();
}

animation.prototype.stop = function ()    // same as pause, pathIdx not reset to 0
{
    if (!this.timer)
        return false;
    clearInterval(this.timer);
    this.active = 0;
    if (this.onStop)
        this.onStop();
}

animation.prototype.step = function () {
    this.pathIdx++;
    if (this.pathIdx < this.numOfSteps) {
        this.bar.style.pixelWidth += this.stepSize;
    }
    else {
        this.bar.style.pixelWidth = parseInt(this.targetValue * this.ratio);
        this.stop();
    }
}