var username, password;
var chirp = true;
var xml;
var statusUrl="http://twitter.com/statuses/friends_timeline.json";
var updateUrl="http://twitter.com/statuses/update.json";
var refreshTime = 2 * 60 * 1000;
var MAX_LENGTH = 140;

var urlRegExp = /(^| )((http|https|mailto):(([A-Za-z0-9$_.+!*(),;/?:@&~=-])|%[A-Fa-f0-9]{2}){2,}(#([a-zA-Z0-9][a-zA-Z0-9$_.+!*(),;/?:@&~=%-]*))?([A-Za-z0-9$_+!*();/?:~-]))/g;
var atRegExp = /(^| )@([a-z0-9_]+)/g;

function getWithAuth(url, callback) {
    // Sadly, can't use JQuery's handy $.get, since it don't handle HTTP Auth
    // hence this hacked-up, not very bulletproof replacement.
    xml = new XMLHttpRequest();
    xml.open("GET", url, true, username, password);
	xml.setRequestHeader("If-Modified-Since", "Sat, 1 Jan 2000 00:00:00 GMT");
	xml.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xml.onreadystatechange = callback;
    xml.send("");
}

function postWithAuth(url, topost, callback) {
    // Sadly, can't use JQuery's handy $.get, since it don't handle HTTP Auth
    // hence this hacked-up, not very bulletproof replacement.
    xml = new XMLHttpRequest();
    xml.open("POST", url, true, username, password);
	xml.setRequestHeader("If-Modified-Since", "Sat, 1 Jan 2000 00:00:00 GMT");
	xml.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xml.onreadystatechange = callback;
	xml.send(topost);
}

function createEntry(data) {
    var li = document.createElement("li");
    li.className = "entry";
    li.id = data.id;

    var text = document.createElement("span");
    text.className = "text";
    text.innerHTML = data.text.replace(urlRegExp,
                                       ' <a href="$2">$2</a>').replace(atRegExp,
                                                                       ' @<a href="http://twitter.com/$2">$2</a>');

    var info = document.createElement("div");
    info.className = "info";
    info.innerHTML = "&mdash;&nbsp;";

    var author = document.createElement("a");
    author.className = "author";
    author.href = "javascript:messageAt('" + data.user.screen_name + "')"
    author.innerHTML = data.user.name;
    author.id = data.user.id;

    var timestamp = document.createElement("span");
    timestamp.className = "timestamp";
    timestamp.id = data.id + "-timestamp";
	relative_created_at = relative_time(data.created_at)
    timestamp.innerHTML = ", " + relative_created_at;

    li.appendChild(text);
    info.appendChild(author);
    info.appendChild(timestamp);
    li.appendChild(info);

    return li;
}

function fetchLatest(clear) {
    showStatus("Refreshing...");

    getWithAuth(statusUrl, function(istimeout){
        if ( xml && (xml.readyState == 4 || istimeout == "timeout") ) {
            data = jQuery.httpData(xml, "json");
			// We only care about the first ten entries
			data = data.slice(0,10).reverse()
            var ul;

            if (clear) {
                $("#gadgetContent")[0].innerHTML = "";
                ul = document.createElement("ul");
                ul.id = "entries";
                $("#gadgetContent")[0].appendChild(ul);
            } else {
                ul = $("#entries")[0];
            }

            $.each(data, function() {
                var id = this.id;
				// Look for existing instances of this entry
                if ($("#"+id).length != 0) {
					// Just update the timestamp
					relative_created_at = relative_time(this.created_at)
                    $("#" + id + "-timestamp")[0].innerHTML = ", " + relative_created_at;
                } else {
					// Create a new entry
                    li = createEntry(this);
                    $(ul).prepend(li);
					if (chirp) {
					    System.Sound.playSound("sounds/canary.wav");
					}
	                $(li).hide();
	                $(li).slideDown("fast");
                }
            });
        } else {
	    	//$("#status")[0].innerHTML = "XMLReadyState: " + xml.readyState;
		}  
    });
}
    
$(document).ready(function() {
    checkState();
	$("#div-post").hide();
	$("#form-post").hide();
    username = System.Gadget.Settings.read("username");
    password = System.Gadget.Settings.read("password");
    chirp = System.Gadget.Settings.read("chirp");

    alert(username);
    if (username == "") {
        $("#status")[0].innerHTML = "set your username";
		$("#status").fadeIn("fast")
    } else {
		restartTimer();
    }
	
	/*
	 *  Stuff to handle the Post form
	 */
	
	$("#heading-post").toggle(function() {
		$("#form-post").slideDown("fast");
		$("#textarea-post")[0].focus();
	}, function() {
		$("#form-post").slideUp("fast");
	})
	
	$("body").hover(function() {
		$("#div-post:hidden").fadeIn("fast");
	}, function() {
		// Only fade out if the form is not visible.
		if ($("#form-post:visible").length == 0) {
			$("#div-post").fadeOut("fast");
		}
	})
	
	$("#textarea-post").keyup(countChars)
	
	$("#button-post").click(function() {
		var message = $("#textarea-post").val();
		
		if (message.length == 0) {
			showStatus("Status too short");
			return false;
		} else if (message.length > MAX_LENGTH) {
			showStatus("Status too long");
			return false;
		}
		
		var topost = "source=twadget&status=" + encodeURIComponent(message);
    	showStatus("POSTING...");

	    postWithAuth(updateUrl, topost, function(istimeout){
	        if ( xml && (xml.readyState == 4 || istimeout == "timeout") ) {
		    	showStatus("POSTED!");
				$("#heading-post").click();
				$("#textarea-post").val("");
				$("#span-characters")[0].innerHTML = "0";
				// Do this in 2 seconds to allow Twitter time to update.
				$(document).doin(2000, function() {
					fetchLatest(false);
				});
	        } else {
		    	//$("#status")[0].innerHTML = "XMLReadyState: " + xml.readyState;
			}  
	    });		
	});
});

function showStatus(message) {
	$("#status")[0].innerHTML = message;
	$("#status:hidden").fadeIn("fast");
	$(document).doin(3000, "fade", function() {
		$("#status:visible").fadeOut("slow")
	});
}

System.Gadget.settingsUI = "Settings.html";
System.Gadget.onSettingsClosed = SettingsClosed;
System.Gadget.onUndock = checkState;
System.Gadget.onDock = checkState;
function SettingsClosed() {
    username = System.Gadget.Settings.read("username");
    password = System.Gadget.Settings.read("password");
    chirp = System.Gadget.Settings.read("chirp");
	restartTimer();
}

function restartTimer() {
    if (username != "") {
        fetchLatest(true);
		$(document).every(refreshTime, "refresh", function() {
            fetchLatest(false);
        });
    }
}

/*
 * Check if we're docked in the Sidebar, and resize appropriately
 */
function checkState()
{
    docked = System.Gadget.docked;
    $("#dockedStyle")[0].disabled = !docked;
    $("#undockedStyle")[0].disabled = docked;
    if (docked) {
		with(document.body.style)
			width = 130,
			height = 350;
		with(gadgetBackground.style)
			width = 130,
			height = 350;
		gadgetBackground.src = "url(images/background-docked.png)";
    } else {
		with(document.body.style)
			width = 300,
			height = 450;
		with(gadgetBackground.style)
			width = 300,
			height = 450;
		gadgetBackground.src = "url(images/background-undocked.png)";
    };
}


// relative_time by Mike Demers
// taken from http://twitter.pbwiki.com/RelativeTimeScripts
function relative_time(time_value) {
   time_value = time_value.replace("+0000", "GMT"); // Stupid IE tricks
   var parsed_date = Date.parse(time_value);

   var relative_to = new Date();
   var delta = parseInt((relative_to.getTime() - parsed_date) / 1000);

   if(delta < 60) {
       return 'less than a minute ago';
   } else if(delta < 120) {
       return 'about a minute ago';
   } else if(delta < (45*60)) {
       return (parseInt(delta / 60)).toString() + ' minutes ago';
   } else if(delta < (90*60)) {
       return 'about an hour ago';
   } else if(delta < (24*60*60)) {
       return 'about ' + (parseInt(delta / 3600)).toString() + ' hours ago';
   } else if(delta < (48*60*60)) {
       return '1 day ago';
   } else {
       return (parseInt(delta / 86400)).toString() + ' days ago';
   }
}

function countChars() {
	var chars = $("#textarea-post").val().length;
	$("#span-characters")[0].innerHTML = chars;
	if (chars > MAX_LENGTH) {
		$("#span-characters").addClass("warn");
	} else {
		$("#span-characters").removeClass("warn");
	}
}

function messageAt(screenname) {
	$("#textarea-post").val("@" + screenname + " ");
    countChars();
 	if ($("#form-post:visible").length == 0) {
   	    $("#heading-post").click();
    }
	$("#textarea-post")[0].focus();
}