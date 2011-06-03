
var updateTimerId = null;
var xmlVer = null;
function onTimerCheckUpdates() {
    // Update once a day, 24H interval.
    updateTimerId = setInterval("onTimerCheckUpdates()", 86400000);
    newVersion = 0;

    var oRequest = new XMLHttpRequest();
    var sURL = "http://www.alcpu.com/CoreTemp/AddOns/gadgetver.xml";
    oRequest.open("GET", sURL, false);
    oRequest.setRequestHeader("User-Agent", navigator.userAgent);
    oRequest.setRequestHeader("Cache-Control", "no-cache");
    oRequest.send(null)
    if (oRequest.status == 200) {
        xmlVer = oRequest.responseXML;
        xmlVer = xmlVer.getElementsByTagName("gadget");
        if (xmlVer.length > 0) {
            xmlVer = xmlVer[0];
            try {
                newVersion = parseFloat(xmlVer.childNodes[1].text);
            }
            catch (err) {
                newVersion = 0;
            }
            if (newVersion > currentVersion) {
                gadgetVersion.innerHTML = "Version <b>" + newVersion + "</b> is available!";
                buildGUI(CoreTempActive);
            }
            else {
                newVersion = 0;
            }
        }
    }

    return newVersion;
}

function loadInfo() {
    try {
        if (System.Gadget.Flyout.show) {
            var flyoutDoc = System.Gadget.Flyout.document;
            var info = flyoutDoc.getElementById("info");
            var notes = flyoutDoc.getElementById("notes");
            flyoutDoc.getElementById("title").innerHTML = "<u>" + xmlVer.getElementsByTagName("name")[0].text + " Gadget</u>";
            flyoutDoc.getElementById("version").innerHTML = "Version: <b>" + xmlVer.getElementsByTagName("version")[0].text + "</b>";
            flyoutDoc.getElementById("date").innerHTML = "Release date: <b>" + getDateString(xmlVer.getElementsByTagName("releaseDate")[0]) + "</b>";
            flyoutDoc.getElementById("download").href = xmlVer.getElementsByTagName("url")[0].text;
            info.innerHTML = xmlVer.getElementsByTagName("changes")[0].text;
            if (parseFloat(xmlVer.getAttribute("ver")) > 1.0 && xmlVer.getElementsByTagName("notes")[0].text.length > 0) {
                notes.innerHTML = xmlVer.getElementsByTagName("notes")[0].text;
                notes.style.pixelHeight = Math.min(60, notes.offsetHeight);
                info.style.pixelHeight += (55 - notes.style.pixelHeight);
            }
            else {
                notes.style.display = flyoutDoc.getElementById("infoNotesSped").style.display = "none";
                info.style.pixelHeight = 265;
            }
        }
    }
    catch (ex) {
        var err = ex.message;
    }
}

function getDateString(xmlDate) {
    var m_names = new Array("January", "February", "March",
"April", "May", "June", "July", "August", "September",
"October", "November", "December");

    var day = parseInt(xmlDate.childNodes[0].text);
    var month = parseInt(xmlDate.childNodes[1].text) - 1;
    var sup = "";
    if (day == 1 || day == 21 || day == 31) {
        sup = "st";
    }
    else if (day == 2 || day == 22) {
        sup = "nd";
    }
    else if (day == 3 || day == 23) {
        sup = "rd";
    }
    else {
        sup = "th";
    }

    return day + "<SUP>" + sup + "</SUP> " + m_names[month] + ", " + xmlDate.childNodes[2].text;
}

function openUpdateInfo() {
    if (xmlVer != null && !System.Gadget.Flyout.show) {
        System.Gadget.Flyout.show = true;
        System.Gadget.Flyout.onShow = function () {
            loadInfo();
        }
    }
}

function closeUpdateInfo() {
    System.Gadget.Flyout.show = false;
}

function updateMouseIn(label) {
    label.style.textDecoration = "underline";
}

function updateMouseOut(label) {
    label.style.textDecoration = "none";
}