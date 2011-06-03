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
/////////////////////////////////////////////////////////////////////////////////////

var setting, showGraph, showTemps, coreColors, singleColor, zoomPercent;
var showCpuName, showClocks, showVid, showRam, showCpuLogo, showDefaultLogo, showUpdates;
var titleColor, cpuNameColor, clockColor, vidColor, ramColor;
var selectedCore;
var isLoading = true;
var CoreTemp;
var coreTempRunning = false;

var colors = ["#FFD800", "#B6FF00", "#4CFF00", "#00FF21", "#00FF90", "#00FFFF", "#00B2FF", "#0065FF",
              "#6B3AFF", "#B200FF", "#FF00DC", "#FF006E", "#FF0000", "#7F0000", "#7F3300", "#FF6A00", "#FFFFFF"];

function toggleDiv(div) {
    var div = div.parentNode.nextSibling;
    div.style.display = div.style.display == 'block' ? 'none' : 'block';
}

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

function showGraphChanged(state) {
    showGraph = state;
    if (showGraph) {
        chkTemps.disabled = "";
    }
    else {
        chkTemps.disabled = "disabled";
    }
}

function showTempsChanged(state) {
    showTemps = state;
}

function hideCpuLogoChanged(state) {
    showCpuLogo = state;
    if (showCpuLogo) {
        chkDefaultLogo.disabled = "";
    }
    else {
        chkDefaultLogo.disabled = "disabled";
    }
}

function showDefaultChanged(state) {
    showDefaultLogo = state;
}

function hideCpuNameChanged(state) {
    showCpuName = state;
}

function hideFrequencyChanged(state) {
    showClocks = state;
}

function hideVidChanged(state) {
    showVid = state;
}

function hideRamChanged(state) {
    showRam = state;
}

function showUpdatesChanged(state) {
    showUpdates = state;
}

function colorSettingChanged(index) {
    if (index != "1") {
        coreSelect.disabled = "disabled";
        multiColorSelect.disabled = "disabled";
        multiColorSelect.nextSibling;
        multiColorPicker.disabled = "disabled";
    }
    else {
        coreSelect.disabled = "";
        multiColorSelect.disabled = "";
        if (multiColorSelect.selectedIndex == multiColorSelect.length - 1) {
            multiColorPicker.disabled = "";
        }
        else {
            multiColorPicker.disabled = "disabled";
        }
    }
    if (index != "2") {
        singleColorSelect.disabled = "disabled";
        singleColorPicker.disabled = "disabled";
    }
    else {
        singleColorSelect.disabled = "";
        if (singleColorSelect.selectedIndex == singleColorSelect.length - 1) {
            singleColorPicker.disabled = "";
        }
        else {
            singleColorPicker.disabled = "disabled";
        }
    }
    setting = index;
}


function colorSelected(object) {
    switch (object.id) {
        case "titleColorSelect":
            titleColor = colorChange(object, titleColorPicker);
            break;
        case "cpuNameColorSelect":
            cpuNameColor = colorChange(object, cpuNameColorPicker);
            break;
        case "clockSpeedColorSelect":
            clockColor = colorChange(object, clockSpeedColorPicker);
            break;
        case "vidTjmaxColorSelect":
            vidColor = colorChange(object, vidTjmaxColorPicker);
            break;
        case "ramColorSelect":
            ramColor = colorChange(object, ramColorPicker);
            break;
        default:
            if (setting == 1) {
                coreColors[selectedCore] = colorChange(object, multiColorPicker);
            }
            else if (setting == 2) {
                singleColor = colorChange(object, singleColorPicker);
            }
            break;
    }
}

function colorChange(object, picker) {
    object.blur();
    if (object.selectedIndex == object.length - 1) {
        picker.disabled = "";
        if (!isLoading) {
            picker.color.showPicker();
        }
        return "#" + picker.color;
    }
    else {
        picker.color.fromString(object.value);
        picker.color.hidePicker();
        picker.disabled = "disabled";
        return object.value;
    }
}

function customColorSelected(object) {
    switch (object.id) {
        case "titleColorPicker":
            titleColor = "#" + object.color;
            break;
        case "cpuNameColorPicker":
            cpuNameColor = "#" + object.color;
            break;
        case "clockSpeedColorPicker":
            clockColor = "#" + object.color;
            break;
        case "vidTjmaxColorPicker":
            vidColor = "#" + object.color;
            break;
        case "ramColorPicker":
            ramColor = "#" + object.color;
            break;
        default:
            if (setting == 1) {
                coreColors[selectedCore] = "#" + object.color;
            }
            else if (setting == 2) {
                singleColor = "#" + object.color;
            }
            break;
    }
}

function coreSelected(index) {
    selectedCore = index;
    if (setting == 1) {
        colorToSelect(coreColors[selectedCore], multiColorSelect, multiColorPicker);
    }
    else if (setting == 2) {
        colorToSelect(singleColor, singleColorSelect, singleColorPicker);
    }
}

function colorToSelect(color, select, picker) {
    var colorFound = false;
    for (i = 0; i < select.options.length; i++) {
        if (select.options[i].value == color.toUpperCase()) {
            select.selectedIndex = i;
            colorFound = true;
            break;
        }
    }
    if (!colorFound) {
        select.selectedIndex = select.length - 1;
        try {
            picker.color.fromString(color);
        }
        catch (err) {
            debugLog("Error applying to color picker: " + err.name + " - Number: " + err.number + "; Description: " + err.description + "; Message: " + err.message);
        }
    }
    colorChange(select, picker);
}

function loadSettings() {
    var element = document.getElementById("coreSelect")
    for (i = 0; i < CoreTemp.CoreCount; i++) {
        element.options.add(new Option("Core #" + i, i, 0, 0));
    }
    element.options.add(new Option("RAM", CoreTemp.CoreCount, 0, 0));
    element.selectedIndex = 0;
    selectedCore = 0;

    setting = System.Gadget.Settings.read("setting");
    if (setting == "") {
        setting = 0;
    }

    showGraph = System.Gadget.Settings.read("showGraph");
    if (0 == showGraph.length) {
        showGraph = true;
    }
    showGraphChanged(showGraph);

    showTemps = System.Gadget.Settings.read("showTemps");
    if (0 == showTemps.length) {
        showTemps = false;
    }

    showCpuLogo = System.Gadget.Settings.read("showCpuLogo");
    if (0 == showCpuLogo.length) {
        showCpuLogo = true;
    }
    hideCpuLogoChanged(showCpuLogo);

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

    singleColor = System.Gadget.Settings.read("singleColor");
    if (0 == singleColor.length) {
        singleColor = colors[0];
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

    zoomPercent = System.Gadget.Settings.read("zoomPercent");
    if (0 == zoomPercent.length) {
        zoomPercent = 100;
    }

}

function applySettings() {
    if (showGraph) {
        chkGraph.checked = "checked";
    }
    if (showTemps) {
        chkTemps.checked = "checked";
    }
    if (showCpuLogo) {
        chkHideLogo.checked = "checked";
    }
    if (showDefaultLogo) {
        chkDefaultLogo.checked = "checked";
    }
    if (showCpuName) {
        chkHideCpuName.checked = "checked";
    }
    if (showClocks) {
        chkHideFrequency.checked = "checked";
    }
    if (showVid) {
        chkHideVid.checked = "checked";
    }
    if (showRam) {
        chkHideRam.checked = "checked";
    }
    if (showUpdates) {
        chkHideUpdates.checked = "checked";
    } 
    colorToSelect(titleColor, titleColorSelect, titleColorPicker);
    colorToSelect(cpuNameColor, cpuNameColorSelect, cpuNameColorPicker);
    colorToSelect(clockColor, clockSpeedColorSelect, clockSpeedColorPicker);
    colorToSelect(vidColor, vidTjmaxColorSelect, vidTjmaxColorPicker);
    colorToSelect(ramColor, ramColorSelect, ramColorPicker);
    colorToSelect(singleColor, singleColorSelect, singleColorPicker);
    colorToSelect(coreColors[0], multiColorSelect, multiColorPicker);
    document.getElementsByName("Colors")[setting].checked = "checked";
    document.getElementById("zoom").value = zoomPercent;
    colorSettingChanged(setting);
}

document.onreadystatechange = function () {
    if (document.readyState == "complete") {
        jscolor.init();
        version.innerHTML = System.Gadget.version;
        System.Gadget.onSettingsClosing = settingsClosing;
        CoreTemp = GetLibrary();
        if (CoreTemp == null) {
            document.getElementById("mainDiv").innerHTML = "Failed to load Core Temp interface library.<br />Settings window could not be initiated.";
        }
        try {
            CoreTemp.Refresh();
            loadSettings();
            applySettings();
            //                        addFacebook();
            coreTempRunning = true;
        }
        catch (err) {
            document.getElementById("mainDiv").innerHTML = "Core Temp is not running.<br />Settings window could not be initiated.<br /><br />" + err.description;
        }
        isLoading = false;
    }
}

// --------------------------------------------------------------------
// Handle the Settings dialog closed event.
// event = System.Gadget.Settings.ClosingEvent argument.
// --------------------------------------------------------------------
function settingsClosing(event) {
    // Save the settings if the user clicked OK.
    if (event.closeAction == event.Action.commit && coreTempRunning) {
        // Allow the Settings dialog to close.
        event.cancel = false; showDefaultLogo
        System.Gadget.Settings.write("setting", setting);
        System.Gadget.Settings.write("showGraph", showGraph);
        System.Gadget.Settings.write("showTemps", showTemps);
        System.Gadget.Settings.write("showCpuLogo", showCpuLogo);
        System.Gadget.Settings.write("showDefaultLogo", showDefaultLogo);
        System.Gadget.Settings.write("showCpuName", showCpuName);
        System.Gadget.Settings.write("showClocks", showClocks);
        System.Gadget.Settings.write("showVid", showVid);
        System.Gadget.Settings.write("showRam", showRam);
        System.Gadget.Settings.write("singleColor", singleColor);
        System.Gadget.Settings.write("titleColor", titleColor);
        System.Gadget.Settings.write("cpuNameColor", cpuNameColor);
        System.Gadget.Settings.write("clockColor", clockColor);
        System.Gadget.Settings.write("vidColor", vidColor);
        System.Gadget.Settings.write("ramColor", ramColor);
        System.Gadget.Settings.write("showUpdates", showUpdates);
        var colors = "";
        for (var i = 0; i < coreColors.length; i++) {
            colors += coreColors[i] + ",";
        }
        colors = colors.substr(colors, colors.length - 1);
        System.Gadget.Settings.write("colors", colors);
        System.Gadget.Settings.write("zoomPercent", getZoom());
    }
}

function getZoom() {
    var regExpr = new RegExp("^\\d\\.?\\d*$");
    if (!regExpr.test(document.getElementById("zoom").value)) {
        return 100;
    }
    else {
        return parseInt(document.getElementById("zoom").value);
    }
}

function addFacebook() {
    var oRequest = new XMLHttpRequest();
    var sURL = "http://www.facebook.com/";
    oRequest.onreadystatechange = function () {
        if (oRequest.readyState == 4) {
            if (oRequest.status == 200) {
                var iframe = document.createElement('iframe');
                iframe.setAttribute("src", "http://www.facebook.com/plugins/like.php?href=http%3A%2F%2Fwww.facebook.com%2Fpages%2FCoreTemp%2F98414141360&amp;layout=standard&amp;show_faces=false&amp;width=260&amp;action=like&amp;colorscheme=light&amp;height=40");
                iframe.scrolling = "no";
                iframe.frameborder = "0";
                iframe.style.border = "none";
                iframe.style.overflow = "hidden";
                iframe.style.pixelWidth = "265";
                iframe.style.pixelHeight = "40";
                iframe.allowTransparency = "true";
                iframe.disabled = "disabled";
                facebookOffline.innerHTML = iframe.outerHTML;
            }
        }
    }

    oRequest.open("HEAD", sURL, true);
    oRequest.setRequestHeader("User-Agent", navigator.userAgent);
    oRequest.setRequestHeader("Cache-Control", "no-cache");
    oRequest.send(null);
}