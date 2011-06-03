/* Pad naar de navigatie map */
var nav_path = 'http://www.4launch.nl/shop/blocks/block_nav/';

function addSlashes(str) {
	str = str.replace(/\'/g,'\\\'');
	str = str.replace(/\"/g,'\\"');
	str = str.replace(/\\/g,'\\\\');
	str = str.replace(/\0/g,'\\0');
	return str;
}
function NavigateMenu(uber, hoofd, sub, merk) {
	var nl = '#p-3';

	if (uber.length != 0) nl += '-subshop-' + ajDashEncode(uber);
	if (hoofd.length != 0) nl += '-hoofdcategorie-' + ajDashEncode(hoofd);
	if (sub.length != 0) nl += '-subcategorie-' + ajDashEncode(sub);
	if (merk.length != 0) nl += '-merk-' + ajDashEncode(merk);

	document.location.hash = nl;
}

function getElementsByClassName(className, tag, elm){
	var testClass = new RegExp("(^|\\s)" + className + "(\\s|$)");
	var tag = tag || "*";
	var elm = elm || document;
	var elements = (tag == "*" && elm.all)? elm.all : elm.getElementsByTagName(tag);
	var returnElements = [];
	var current;
	var length = elements.length;
	for(var i=0; i<length; i++){
		current = elements[i];
		if(testClass.test(current.className)){
			returnElements.push(current);
		}
	}
	return returnElements;
}

// cookie functions
function setPrefCookies(uber, hoofd, sub) {
	if (uber != '') {
		if ( (uber == 'DESTROY') || (typeof(uber) == "undefined") )
			eraseCookie('ubercat');
		else
			createCookie('ubercat', uber);
	}

	if (hoofd != '') {
		if ( (hoofd == 'DESTROY') || (typeof(hoofd) == "undefined") )
			eraseCookie('hoofdcat');
		else
			createCookie('hoofdcat', hoofd);
	}

	if (sub != '') {
		if ( (sub == 'DESTROY')  || (typeof(sub) == "undefined") )
			eraseCookie('subcat');
		else
			createCookie('subcat', sub);
	}
}
// / cookie functions

function loadHoofdCatsTree(uber, nr, hoofd) {
	var tempObj = new net.lDynMenuLoader('treehoofd', uber, '', hoofd, nr);
}
function loadSubCatsTree(uber, hoofd, nr) {
	var tempObj = new net.lDynMenuLoader('tree', uber, '', hoofd, nr);
}
function loadUberCatsDrop(uber, merk, hoofd, sub) {
	var tempObj = new net.lDynMenuLoader('dropuber', uber, merk, hoofd, sub);
}
function reloadDrops(uber, hoofd, sub) {
	var merk = '';
	if (document.getElementById('merkDropdown') && document.getElementById('merkDropdown').value != '') PreloadDropDown('', '', '', '');

	if (document.getElementById('uberDropdown') && document.getElementById('uberDropdown').value != uber) {
		var tempObj = new net.lDynMenuLoader('dropuber', uber, merk, hoofd, sub);
	} else if (document.getElementById('hoofdCatDropdown') && document.getElementById('hoofdCatDropdown').value != hoofd) {
		var tempObj = new net.lDynMenuLoader('drophoofd', uber, merk, hoofd, sub);
	} else if (document.getElementById('subCatDropdown') && document.getElementById('subCatDropdown').value != sub) {
		var tempObj = new net.lDynMenuLoader('drop', uber, merk, hoofd, sub);
	} else if (document.getElementById('merkDropdown') && document.getElementById('merkDropdown').value != merk) {
		var tempObj = new net.lDynMenuLoader('dropsubmerk', uber, merk, hoofd, sub);
	}
	if (hoofd == '') hoofd = 'DESTROY';
	if (sub == '') sub = 'DESTROY';
	setPrefCookies(uber, hoofd, sub);
}
function reloadDropsext(uber, merk, hoofd, sub) {
	if (document.getElementById('uberDropdown') && document.getElementById('uberDropdown').value != uber) {
		var tempObj = new net.lDynMenuLoader('dropuber', uber, merk, hoofd, sub);
	} else if (document.getElementById('hoofdCatDropdown') && document.getElementById('hoofdCatDropdown').value != hoofd) {
		var tempObj = new net.lDynMenuLoader('drophoofd', uber, merk, hoofd, sub);
	} else if (document.getElementById('subCatDropdown') && document.getElementById('subCatDropdown').value != sub) {
		var tempObj = new net.lDynMenuLoader('drop', uber, merk, hoofd, sub);
	} else if (document.getElementById('merkDropdown') && document.getElementById('merkDropdown').value != merk) {
		var tempObj = new net.lDynMenuLoader('dropsubmerk', uber, merk, hoofd, sub);
	}
	if (hoofd == '') hoofd = 'DESTROY';
	if (sub == '') sub = 'DESTROY';
	setPrefCookies(uber, hoofd, sub);
}
function loadHoofdCatsDrop(uber, merk, preloadHoofd, sub) {
	var tempObj = new net.lDynMenuLoader('drophoofd', uber, merk, preloadHoofd, sub, preloadHoofd != null);
}
function loadSubCatsDrop(uber, merk, hoofd, sub) {
	var tempObj = new net.lDynMenuLoader('drop', uber, merk, hoofd, sub);
}
function loadSubCatMerkenDrop(uber, hoofd, sub, merk) {
	var tempObj = new net.lDynMenuLoader('dropsubmerk', uber, merk, hoofd, sub);
}
function ResetDropMenu() {
	loadSubCatMerkenDrop('', '', '', '');
	loadUberCatsDrop('', '', '', '');
}
function PreloadDropDown(merk, uber, hoofd, sub) {
	var merkDropdown = document.getElementById('merkDropdown');
	if (merkDropdown) {
		for (var x = 0; x < merkDropdown.options.length; x++) {
			if (merkDropdown.options[x].value == merk) {
				merkDropdown.options.selectedIndex = x;
				var tempObj = new net.lDynMenuLoader('dropuber', uber, merk, hoofd, sub, true);
			}
		}
	}
}
function selectTree(uber, hoofd, sub) {
	var tempObj = new net.lDynMenuSelect(uber, hoofd, sub);
}
var treeResolvObj_uber = new Object();
var treeResolvObj_hoofd = new Object();
var treeResolvObj_sub = new Object();


function selectUberCat(nr) {
		var x = 0;
		while (document.getElementById('uberCatImage' + x)) {
			if (x == nr) {
				document.getElementById('uberCatImage' + x).src = "http://www.4launch.com/images/shop/menus/2.gif";
				document.getElementById('uberCatLink' + x).className = "ubercat_selected";
			} else {
				document.getElementById('uberCatImage' + x).src = "http://www.4launch.com/images/shop/menus/1.gif";
				document.getElementById('uberCatLink' + x).className = "ubercat";
			}
			x++;
		}
}

function selectHoofdCat(nr) {
		var splitNumber = nr.split("|");
		var prevNumber = splitNumber[0] + "|";
		var selNumber = splitNumber[1];
		var x = 0;
		while (document.getElementById('hoofdCatImage' + prevNumber + x)) {
			if (x == selNumber) {
				document.getElementById('hoofdCatImage' + prevNumber + x).src = "http://www.4launch.com/images/shop/menus/2.gif";
				document.getElementById('hoofdCatLink' + prevNumber + x).className = "hoofdcat_selected";
			} else {
				document.getElementById('hoofdCatImage' + prevNumber + x).src = "http://www.4launch.com/images/shop/menus/1.gif";
				document.getElementById('hoofdCatLink' + prevNumber + x).className = "hoofdcat";
			}
			x++;
		}
}

function selectSubcat(nr) {
	var els = getElementsByClassName('subcat_selected');
	for (var x = 0; x < els.length; x++) {	
		els[x].className = 'subcat';
	}
	var els = getElementsByClassName('subcatImg');
	for (var x = 0; x < els.length; x++) {	
		if (els[x].src.indexOf('4.gif') >= 0) els[x].src = "http://www.4launch.com/images/shop/menus/3.gif";
	}

	if (document.getElementById('subcatLink' + nr)) document.getElementById('subcatLink' + nr).className = 'subcat_selected';
	if (document.getElementById('subcatImg' + nr)) document.getElementById('subcatImg' + nr).src = "http://www.4launch.com/images/shop/menus/4.gif";
}
var net = new Object();

net.READY_STATE_UNINITIALIZED=0;
net.READY_STATE_LOADING=1;
net.READY_STATE_LOADED=2;
net.READY_STATE_INTERACTIVE=3;
net.READY_STATE_COMPLETE=4;

net.lDynMenuLoader = function(mode, uber, merk, hoofd, nr, preloadHoofd) {
	this.XMLreq = null;
	this.uberCat = uber;
	this.merk = merk;
	this.hoofdCat = hoofd;
	this.Number = nr; // also used as sub selection for merk
	this.mode = mode;
	this.preloadHoofd = preloadHoofd;

	// document.getElementById('loading').style.display = 'block';

	switch(mode) {
		case 'treeselect':
			this.selectTree();
		break;
		case 'treehoofd':
			this.requestTreeHoofdcats();
		break;
		case 'tree':
			this.requestTreeSubcats();
		break;
		case 'dropuber':
			this.requestDropUbercats();
		break;
		case 'drophoofd':
			this.requestDropHoofdcats();
		break;
		case 'dropsubmerk':
			this.requestDropSubMerk();
		break;
		default:
			this.requestDropSubcats();
		break;
	}

}
net.lDynMenuLoader.prototype = {
	TreeHideHoofdcatContainers:function() {
		var els = getElementsByClassName('hoofdcatContainer');
		for (var x = 0; x < els.length; x++) {	
			els[x].style.display = 'none';
		}
	}
,
	TreeHideSubcatContainers:function() {
		var els = getElementsByClassName('subcatContainer');
		for (var x = 0; x < els.length; x++) {	
			els[x].style.display = 'none';
		}
	}
,
	requestTreeHoofdcats:function() {
		var hoofdcatContainer = document.getElementById('hoofdcatContainer' + this.Number);
		if (hoofdcatContainer) {
			var prevVal = hoofdcatContainer.style.display;
			this.TreeHideHoofdcatContainers();
			this.TreeHideSubcatContainers();

			// needs content?
			if (hoofdcatContainer.innerHTML == '') {
				this.handleXMLHTTPRequest(nav_path + 'menuhandler.php', 'tree=Y&mode=hoofd&uber=' + escape(this.uberCat), this.TreeFillHoofdcat);
			} else {
				// document.getElementById('loading').style.display = 'none';
			}

			// toggle display
			if ((prevVal == 'none') || (prevVal == '')) {
				selectUberCat(this.Number);
				selectHoofdCat(this.Number + "|-1");
				hoofdcatContainer.style.display = 'inline';
			} else {
				setPrefCookies('DESTROY', 'DESTROY', 'DESTROY');
				selectUberCat(-1);
				selectHoofdCat(this.Number + "|-1");
				hoofdcatContainer.style.display = 'none';
			}
		}
	}
,
	requestTreeSubcats:function() {
		var subcatContainer = document.getElementById('subcatContainer' + this.Number);
		if (subcatContainer) {
			var prevVal = subcatContainer.style.display;
			this.TreeHideSubcatContainers();

			// needs content?
			if (subcatContainer.innerHTML == '') {
				this.handleXMLHTTPRequest(nav_path + 'menuhandler.php', 'tree=Y&uber=' + escape(this.uberCat) + '&hoofd=' + escape(this.hoofdCat), this.TreeFillSubcat);
			} else {
				// document.getElementById('loading').style.display = 'none';
			}

			// toggle display
			if ((prevVal == 'none') || (prevVal == '')) {
				selectHoofdCat(this.Number);
				subcatContainer.style.display = 'inline';
			} else {
				setPrefCookies('', 'DESTROY', 'DESTROY');
				selectHoofdCat(this.Number.split("|")[0] + "|-1");
				subcatContainer.style.display = 'none';
			}
		}
	}
,
	requestDropUbercats:function() {
		var ubercatDrop = document.getElementById('uberDropdown');
		var hoofdcatDrop = document.getElementById('hoofdCatDropdown');
		var subcatDrop = document.getElementById('subCatDropdown');

		ubercatDrop.options.length = 0;
		ubercatDrop.disabled = true;

		hoofdcatDrop.options.length = 0;
		hoofdcatDrop.disabled = true;

		subcatDrop.options.length = 0;
		subcatDrop.disabled = true;
		subcatDrop.style.display = 'none';
		document.getElementById('subcatHeader').style.display = 'none';

		this.handleXMLHTTPRequest(nav_path + 'menuhandler.php', 'mode=uber&uber=' + escape(this.uberCat) + '&merk=' + escape(this.merk), this.DropFillUbercat);
	}
,
	requestDropHoofdcats:function() {
		var hoofdcatDrop = document.getElementById('hoofdCatDropdown');
		var subcatDrop = document.getElementById('subCatDropdown');

		hoofdcatDrop.options.length = 0;
		hoofdcatDrop.disabled = true;

		subcatDrop.options.length = 0;
		subcatDrop.disabled = true;
		subcatDrop.style.display = 'none';
		document.getElementById('subcatHeader').style.display = 'none';

		this.handleXMLHTTPRequest(nav_path + 'menuhandler.php', 'mode=hoofd&uber=' + escape(this.uberCat) + '&merk=' + escape(this.merk), this.DropFillHoofdcat);
	}
,
	requestDropSubcats:function() {
		var subcatDrop = document.getElementById('subCatDropdown');

		subcatDrop.options.length = 0;
		subcatDrop.disabled = true;
		subcatDrop.style.display = 'inline';
					document.getElementById('subcatHeader').style.display = 'table-row';
					this.handleXMLHTTPRequest(nav_path + 'menuhandler.php', 'uber=' + escape(this.uberCat) + '&merk=' + escape(this.merk) + '&hoofd=' + escape(this.hoofdCat), this.DropFillSubcat);
	}
,
	requestDropSubMerk:function() {
		this.handleXMLHTTPRequest(nav_path + 'menuhandler.php', 'uber=' + escape(this.uberCat) + '&merk=' + escape(this.merk) + '&hoofd=' + escape(this.hoofdCat) + '&sub=' + escape(this.Number), this.DropFillSubMerk);
	}
,
	handleXMLHTTPRequest:function (url, params, handler) {
		this.XMLreq = null;
		if (window.XMLHttpRequest) {
			this.XMLreq = new XMLHttpRequest();
		} else if (typeof ActiveXObject != "undefined") {
			this.XMLreq = new ActiveXObject("Microsoft.XMLHTTP");
		}
		if (this.XMLreq) {
			this.fillFunc = handler;

			var loader = this;
			var caller = loader.ResponseHandler;
			this.XMLreq.onreadystatechange = function () {
				caller.call(loader);
			}
			this.XMLreq.open("POST",url,true);
			this.XMLreq.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			this.XMLreq.send(params);
		}
	}
,
	ResponseHandler:function () {
		var ready = this.XMLreq.readyState;
		var data = null;
		if (ready == net.READY_STATE_COMPLETE) {
			data = this.XMLreq.responseXML;
			this.XMLreq = null;
			this.fillFunc(data);
		}
	}
,
	TreeFillHoofdcat:function (data) {
		var hoofdcatContainer = document.getElementById('hoofdcatContainer' + this.Number);
		var buffer = '';
		var hoofdmenus = null;

		if ( (hoofdcatContainer) && (hoofdmenus = data.getElementsByTagName('hoofdmenu')) ) {
			for (var x = 0; x < hoofdmenus.length; x++) {
				var attr = hoofdmenus[x].attributes;
				var title = attr.getNamedItem('title').value;

				buffer += '<div style="cursor: pointer;" onclick="setPrefCookies(\'' + addSlashes(this.uberCat) + '\', \'' + addSlashes(title) + '\', \'DESTROY\'); loadSubCatsTree(\'' + addSlashes(this.uberCat) + '\', \'' + addSlashes(title) + '\', \'' + this.Number + '|' + x + '\')">';
				buffer += '&nbsp;&nbsp;<img src="http://www.4launch.com/images/shop/menus/1.gif" alt="" id="hoofdCatImage' + this.Number + '|' + x + '" />&nbsp;<a href="javascript:void(0)" class="hoofdcat" id="hoofdCatLink' + this.Number + '|' + x + '">';
				buffer += title;
				buffer += '</a></div>';
				buffer += '<div class="subcatContainer" id="subcatContainer' + this.Number + '|' + x + '"></div>';

				treeResolvObj_hoofd[this.Number + '|' + title] = x;

				if (this.hoofdCat == title) {
					setTimeout("loadSubCatsTree('" + addSlashes(this.uberCat) + "', '" + addSlashes(title) + "', '" + this.Number + '|' + x + "');", 10);
				}
			}

			hoofdcatContainer.innerHTML = buffer;
			// document.getElementById('loading').style.display = 'none';
		}
	}
,
	TreeFillSubcat:function (data) {
		var subcatContainer = document.getElementById('subcatContainer' + this.Number);
		var buffer = '';
		var submenus = null;

		if ( (subcatContainer) && (submenus = data.getElementsByTagName('submenu')) ) {
			for (var x = 0; x < submenus.length; x++) {
				var attr = submenus[x].attributes;
				var title = attr.getNamedItem('title').value;

				if (document.treeMenuPreloadSubcat == title) {
					document.treeMenuPreloadSubcat = '';
					var CSSclass = 'subcat_selected';
					var selImg = '4';
				} else {
					var CSSclass = 'subcat';
					var selImg = '3';
				}

				buffer += '<div onclick="setPrefCookies(\'' + addSlashes(this.uberCat) + '\', \'' + addSlashes(this.hoofdCat) + '\', \'' + addSlashes(title) + '\'); selectSubcat(\'' + this.Number + '|' + x + '\');">&nbsp;&nbsp;&nbsp;&nbsp;<img src="http://www.4launch.com/images/shop/menus/' + selImg + '.gif" alt="" id="subcatImg' + this.Number + '|' + x + '" class="subcatImg" />&nbsp;<a href="#p-3-subshop-' + ajDashEncode(this.uberCat) + '-hoofdcategorie-' + ajDashEncode(this.hoofdCat) + '-subcategorie-' + ajDashEncode(title) + '" class="' + CSSclass + '" id="subcatLink' + this.Number +'|' + x + '">' +  title + '</a></div>';

				treeResolvObj_sub[this.Number + '|' + title] = x;
			}
			subcatContainer.innerHTML = buffer;

			// document.getElementById('loading').style.display = 'none';
		}
	}
,
	DropFinishPreload:function () {
		var hoofdDropdown = document.getElementById('hoofdCatDropdown');

		if (hoofdDropdown) {
			for (var x = 0; x < hoofdDropdown.options.length; x++) {
				if (hoofdDropdown.options[x].value == this.hoofdCat) {
					hoofdDropdown.options.selectedIndex = x;
					loadSubCatsDrop(this.uberCat, this.merk, this.hoofdCat);
				}
			}
		}
	}
,
	DropFillUbercat:function (data) {
		var ubercatDrop = document.getElementById('uberDropdown');
		var ubermenus = null;
		var uberselect = -1;

		if ( (ubercatDrop) && (document.getElementById('merkDropdown')) ) {
			if ( (document.getElementById('merkDropdown').value == this.merk) || (document.getElementById('merkDropdown').value == '') ) {
				ubercatDrop.options.length = 0;
				ubercatDrop.options[0] = new Option('-- Geen voorkeur --', '');

				if (ubermenus = data.getElementsByTagName('ubermenu')) {
					for (var x = 0; x < ubermenus.length; x++) {
						var attr = ubermenus[x].attributes;
						var title = attr.getNamedItem('title').value;

						ubercatDrop.options[x + 1] = new Option(title, title);
	
						if (title == this.uberCat) uberselect = (x + 1);
					}
				}

				if (x == 1) uberselect = 1;

				// load merken
				this.DropFillMerken(data.getElementsByTagName('merk'));

				ubercatDrop.disabled = false;
				// document.getElementById('loading').style.display = 'none';

				if (uberselect != -1) {
					ubercatDrop.options.selectedIndex = uberselect;
				}
				loadHoofdCatsDrop(ubercatDrop.value, this.merk, this.hoofdCat, this.Number);
			}
		}
	}
,
	DropFillHoofdcat:function (data) {
		var hoofdcatDrop = document.getElementById('hoofdCatDropdown');
		var hoofdmenus = null;
		var hoofdselect = -1;

		if ( (hoofdcatDrop) && (document.getElementById('merkDropdown')) ) {
			if ( (document.getElementById('merkDropdown').value == this.merk) || (document.getElementById('merkDropdown').value == '') ) {
				hoofdcatDrop.options.length = 0;
				hoofdcatDrop.options[0] = new Option('-- Geen voorkeur --', '');

				if (hoofdmenus = data.getElementsByTagName('hoofdmenu')) {
					for (var x = 0; x < hoofdmenus.length; x++) {
						var attr = hoofdmenus[x].attributes;
						var title = attr.getNamedItem('title').value;
	
						hoofdcatDrop.options[x + 1] = new Option(title, title);
						if (this.hoofdCat == title) hoofdselect = x + 1;
					}
	
					if (x == 1) hoofdselect = 1;
				}

				// load merken
				this.DropFillMerken(data.getElementsByTagName('merk'));

				hoofdcatDrop.disabled = false;
				// document.getElementById('loading').style.display = 'none';

				if (hoofdselect != -1) {
					hoofdcatDrop.options.selectedIndex = hoofdselect;
					loadSubCatsDrop(document.getElementById('uberDropdown').value, this.merk, hoofdcatDrop.value, this.Number);
				}
			}
		}
	}
,
	DropFillSubcat:function (data) {
		var subcatDrop = document.getElementById('subCatDropdown');
		var submenus = null;
		var subselect = -1;

		if ( (subcatDrop) && (document.getElementById('hoofdCatDropdown')) ) {
			if (document.getElementById('hoofdCatDropdown').value == this.hoofdCat) {
				subcatDrop.options.length = 0;
				subcatDrop.options[0] = new Option('-- Geen voorkeur --', '');

				if (submenus = data.getElementsByTagName('submenu')) {
					for (var x = 0; x < submenus.length; x++) {
						var attr = submenus[x].attributes;
						var title = attr.getNamedItem('title').value;

						subcatDrop.options[x + 1] = new Option(title, title);
						if (this.Number == title) subselect = x + 1;
					}

					if (x == 1) subselect = 1;
				}

				// load merken
				this.DropFillMerken(data.getElementsByTagName('merk'));

				subcatDrop.disabled = false;
				// document.getElementById('loading').style.display = 'none';

				if (subselect != -1) {
					subcatDrop.options.selectedIndex = subselect;
					loadSubCatMerkenDrop(this.uberCat, this.hoofdCat, this.Number, this.merk)
				}
			}
		}
	}
,
	DropFillSubMerk:function (data) {
		if (document.getElementById('subCatDropdown')) {
			if (document.getElementById('subCatDropdown').value == this.Number) {
				// load merken
				this.DropFillMerken(data.getElementsByTagName('merk'));
			}
		}
	}
,
	DropFillMerken:function (merken) {
		var merkDrop = document.getElementById('merkDropdown');
		var merkselect = -1;

		if (merkDrop) {
			merkDrop.options.length = 0;
			merkDrop.options[0] = new Option('-- Geen voorkeur --', '');

			for (var x = 0; x < merken.length; x++) {
				var attr = merken[x].attributes;
				var title = attr.getNamedItem('title').value;

				merkDrop.options[x + 1] = new Option(title, title);

				if (this.merk == title) merkselect = (x + 1);
			}

			if (x == 1) merkselect = 1;

			if (merkselect != -1) {
				merkDrop.options.selectedIndex = merkselect;
			}
		}
	}
}


net.lDynMenuSelect = function(uber, hoofd, sub) {
	this.XMLreq = null;
	this.uberCat = uber;
	this.hoofdCat = hoofd;
	this.subCat = sub;

	this.selectUber();
}
net.lDynMenuSelect.prototype = {
	handleXMLHTTPRequest:function (url, params, handler) {
		this.XMLreq = null;
		if (window.XMLHttpRequest) {
			this.XMLreq = new XMLHttpRequest();
		} else if (typeof ActiveXObject != "undefined") {
			this.XMLreq = new ActiveXObject("Microsoft.XMLHTTP");
		}
		if (this.XMLreq) {
			this.fillFunc = handler;

			var loader = this;
			var caller = loader.ResponseHandler;
			this.XMLreq.onreadystatechange = function () {
				caller.call(loader);
			}
			this.XMLreq.open("POST",url,true);
			this.XMLreq.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			this.XMLreq.send(params);
		}
	}
,
	ResponseHandler:function () {
		var ready = this.XMLreq.readyState;
		var data = null;
		if (ready == net.READY_STATE_COMPLETE) {
			data = this.XMLreq.responseXML;
			this.XMLreq = null;
			this.fillFunc(data);
		}
	}
,
	setCookies:function() {
		if (this.hoofdCat == '') var lhoofd = 'DESTROY'; else lhoofd = this.hoofdCat;
		if (this.subCat == '') var lsub = 'DESTROY'; else lsub = this.subCat;
		setPrefCookies(this.uberCat, lhoofd, lsub);
	}
,
	TreeHideHoofdcatContainers:function() {
		var els = getElementsByClassName('hoofdcatContainer');
		for (var x = 0; x < els.length; x++) {	
			els[x].style.display = 'none';
		}
	}
,
	TreeHideSubcatContainers:function() {
		var els = getElementsByClassName('subcatContainer');
		for (var x = 0; x < els.length; x++) {	
			els[x].style.display = 'none';
		}
	}
,
	selectUber:function() {
		if (this.uberCat == '') {
			selectUberCat(-1);
			this.TreeHideHoofdcatContainers();
			this.setCookies();
		} else {
			var curUber = readCookie('ubercat');
			if (this.uberCat != curUber) {
				selectUberCat(treeResolvObj_uber[this.uberCat]);
				var hoofdcatContainer = document.getElementById('hoofdcatContainer' + treeResolvObj_uber[this.uberCat]);
				if (hoofdcatContainer) {
					this.TreeHideHoofdcatContainers();

					// needs content?
					if (hoofdcatContainer.innerHTML == '') {
						this.handleXMLHTTPRequest(nav_path + 'menuhandler.php', 'mode=hoofd&uber=' + escape(this.uberCat), this.fillHoofdcat);
					} else {
						this.selectHoofd();
					}

					// toggle display
					hoofdcatContainer.style.display = 'inline';
				}
			} else {
				this.selectHoofd();
			}
		}
	}
,
	fillHoofdcat:function(data) {
		var hoofdcatContainer = document.getElementById('hoofdcatContainer' + treeResolvObj_uber[this.uberCat]);
		var buffer = '';
		var hoofdmenus = null;

		if ( (hoofdcatContainer) && (hoofdmenus = data.getElementsByTagName('hoofdmenu')) ) {
			for (var x = 0; x < hoofdmenus.length; x++) {
				var attr = hoofdmenus[x].attributes;
				var title = attr.getNamedItem('title').value;

				buffer += '<div style="cursor: pointer;" onclick="setPrefCookies(\'' + addSlashes(this.uberCat) + '\', \'' + addSlashes(title) + '\', \'DESTROY\'); loadSubCatsTree(\'' + addSlashes(this.uberCat) + '\', \'' + addSlashes(title) + '\', \'' + treeResolvObj_uber[this.uberCat] + '|' + x + '\')">';
				buffer += '&nbsp;&nbsp;<img src="http://www.4launch.com/images/shop/menus/1.gif" alt="" id="hoofdCatImage' + treeResolvObj_uber[this.uberCat] + '|' + x + '" />&nbsp;<a href="javascript:void(0)" class="hoofdcat" id="hoofdCatLink' + treeResolvObj_uber[this.uberCat] + '|' + x + '">';
				buffer += title;
				buffer += '</a></div>';
				buffer += '<div class="subcatContainer" id="subcatContainer' + treeResolvObj_uber[this.uberCat] + '|' + x + '"></div>';

				treeResolvObj_hoofd[treeResolvObj_uber[this.uberCat] + '|' + title] = x;
			}

			hoofdcatContainer.innerHTML = buffer;
		}
		this.selectHoofd();
	}
,
	selectHoofd:function() {
		var uberNr = treeResolvObj_uber[this.uberCat];
		var hoofdNr = treeResolvObj_hoofd[uberNr + '|' + this.hoofdCat];
		if (this.hoofdCat == '') {
			selectHoofdCat(treeResolvObj_uber[this.uberCat] + '|-1');
			this.TreeHideSubcatContainers();
			this.setCookies();
		} else {
			var curHoofd = readCookie('hoofdcat');
			if (this.hoofdCat != curHoofd) {
				selectHoofdCat(uberNr + '|' + hoofdNr);
				var subcatContainer = document.getElementById('subcatContainer' + uberNr + '|' + hoofdNr);
				if (subcatContainer) {
					this.TreeHideSubcatContainers();

					// needs content?
					if (subcatContainer.innerHTML == '') {
						this.handleXMLHTTPRequest(nav_path + 'menuhandler.php', 'uber=' + escape(this.uberCat) + '&hoofd=' + escape(this.hoofdCat), this.fillSubcat);
					} else {
						this.selectSub();
					}

					// toggle display
					subcatContainer.style.display = 'inline';
				}
			} else {
				this.selectSub();
			}
		}
	}
,
	fillSubcat:function (data) {
		var uberNr = treeResolvObj_uber[this.uberCat];
		var hoofdNr = treeResolvObj_hoofd[uberNr + '|' + this.hoofdCat];

		var Number = uberNr + '|' + hoofdNr;
		var subcatContainer = document.getElementById('subcatContainer' + Number);
		var buffer = '';
		var submenus = null;

		if ( (subcatContainer) && (submenus = data.getElementsByTagName('submenu')) ) {
			for (var x = 0; x < submenus.length; x++) {
				var attr = submenus[x].attributes;
				var title = attr.getNamedItem('title').value;

				var CSSclass = 'subcat';
				var selImg = '3';

				buffer += '<div onclick="setPrefCookies(\'' + addSlashes(this.uberCat) + '\', \'' + addSlashes(this.hoofdCat) + '\', \'' + addSlashes(title) + '\'); selectSubcat(\'' + Number + '|' + x + '\');">&nbsp;&nbsp;&nbsp;&nbsp;<img src="http://www.4launch.com/images/shop/menus/' + selImg + '.gif" alt="" id="subcatImg' + Number + '|' + x + '" class="subcatImg" />&nbsp;<a href="#p-3-subshop-' + ajDashEncode(this.uberCat) + '-hoofdcategorie-' + ajDashEncode(this.hoofdCat) + '-subcategorie-' + ajDashEncode(title) + '" class="' + CSSclass + '" id="subcatLink' + Number +'|' + x + '">' +  title + '</a></div>';

				treeResolvObj_sub[Number + '|' + title] = x;
			}
			subcatContainer.innerHTML = buffer;
		}
		this.selectSub();
	}
,
	selectSub:function() {
		var uberNr = treeResolvObj_uber[this.uberCat];
		var hoofdNr = treeResolvObj_hoofd[uberNr + '|' + this.hoofdCat];
		var subNr = treeResolvObj_sub[uberNr + '|' + hoofdNr + '|' + this.subCat];
		if (this.subCat == '') {
			selectSubcat(uberNr + '|' + hoofdNr + '|-1');
			this.setCookies();
		} else {
			var curSub = readCookie('subcat');
			if (this.subCat != curSub) {
				selectSubcat(uberNr + '|' + hoofdNr + '|' + subNr);
				this.setCookies();
			}
		}
	}
}

var searchSelectedElement = -1;
var searchElementCount = 0;
var searchKinds = new Array();
var searchPrevSearch = '';
if (!Function.prototype.apply) {
	Function.prototype.apply = function bu_fix_apply(o,a) {
		var r;
		if (!o) o = {};
		o.___apply=this;
		switch((a && a.length) || 0) {
		case 0: r = o.___apply(); break;
		case 1: r = o.___apply(a[0]); break;
		case 2: r = o.___apply(a[0],a[1]); break;
		case 3: r = o.___apply(a[0],a[1],a[2]); break;
		case 4: r = o.___apply(a[0],a[1],a[2],a[3]); break;
		case 5: r = o.___apply(a[0],a[1],a[2],a[3],a[4]); break;
		case 6: r = o.___apply(a[0],a[1],a[2],a[3],a[4],a[5]); break;
		default:
			for (var i=0, s=""; i<a.length;i++) {
				if(i!=0) s += ",";
				s += "a[" + i +"]";
			}
			r = eval("o.___apply(" + s + ")");
		}
		o.__apply = null;
		return r;
	};
}
if (!Function.prototype.call) {
	Function.prototype.call = function bu_fix_call(o) {
		var args = new Array(arguments.length - 1);
		for(var i=1;i<arguments.length;i++) {args[i - 1] = arguments[i];}
		return this.apply(o, args);
	};
}
function myEscape(escapee) {
	var escapee = escape(escapee).replace(/[+]/, "%2B"); /* plus-teken */
	return escapee.replace(/(%u20AC)/, "%80"); /* euro teken */
}

function myUnEscape(unescapee)
{
	var unescapee = unescape(unescapee).replace(/[%][2][B]/, "+"); /* plus-teken */
	return unescapee.replace(/(%80)/, "%u20AC"); /* euro teken */
}

function NavigateSearch() {
	var tempObj = new net.lDynSearchLoader('recordHit', document.getElementById('fulltext').value, document.getElementById('searchKind').value);
	// alert('Navigatie zoekwoord ' + document.getElementById('fulltext').value + ' Type ' + document.getElementById('searchKind').value);
	// aanpassing: stuurt nu door naar productenoverzicht.php
	var searchKind		= document.getElementById('searchKind').value;
	var searchKey		= document.getElementById('fulltext').value;
	if(searchKind == '') // zoeken op zoekterm
	{
		document.location.hash = '#p-3-zoekterm-' + ajDashEncode(searchKey);
	}
	else // zoeken op een specifiek keyword
	{
		document.location.hash = '#p-3-' + ajDashEncode(searchKind) + '-' + ajDashEncode(searchKey);
		if(searchKind == 'ubercategorie')
		{
			open_tree(searchKey, '', '');
		}
		else if(searchKind == 'hoofdcategorie')
		{
			open_tree('', searchKey, '');
		}
		else if(searchKind == 'subcategorie')
		{
			open_tree('', '', searchKey);
		}
	}
}

function handleSearch(e) {
	if (!e) var e = window.event;

	switch (e.keyCode) {
		case 13:
		break;
		case 38:
			if (searchElementCount != 0) {
				if (searchSelectedElement > -1)
					searchHoverElement(searchSelectedElement - 1);
			}
		break;
		case 40:
			if (searchElementCount != 0) {
				if (searchSelectedElement < (searchElementCount - 1))
					searchHoverElement(searchSelectedElement + 1);
			}
		break;
		default:
			value = document.getElementById('fulltext').value;
			if ((value.length > 1) && (value != '-- zoekterm --')) {
				var tempObj = new net.lDynSearchLoader('requestKeywords', value, null);
			} else {
				document.getElementById('keywordContainer').style.visibility = 'hidden';
				document.getElementById('keywordContainer').innerHTML = '';
				searchElementCount = 0;
			}
		break;
	}
}

function handleSearchEnter(e) {
	if (!e) var e = window.event;

	switch (e.keyCode) {
		case 13:
			searchSelectElement();
		break;
	}
}

function searchHoverElement(elementNumber) {
	if (document.getElementById('searchElement' + searchSelectedElement)) 
		document.getElementById('searchElement' + searchSelectedElement).className = 'searchKeyword';
	searchSelectedElement = elementNumber;
	if ( (searchSelectedElement > -1) && (searchSelectedElement < searchElementCount) )
		document.getElementById('searchElement' + searchSelectedElement).className = 'searchKeywordActive';
}

function searchSelectElement() {
	if ( (searchElementCount != 0) && (searchSelectedElement > -1) ) {
		document.getElementById('fulltext').value = document.getElementById('searchElementValue' + searchSelectedElement).innerHTML;
		document.getElementById('searchKind').value = searchKinds[searchSelectedElement];
		searchPrevSearch = document.getElementById('fulltext').value;
		searchHideKeywords();
		NavigateSearch();
	} else {
		if (searchElementCount != 0) {
			for(var x = 0; x < searchElementCount; x++) {
				if (document.getElementById('searchElementValue' + x).innerHTML.toLowerCase() == document.getElementById('fulltext').value.toLowerCase()) {
					searchSelectedElement = x;
					document.getElementById('fulltext').value = document.getElementById('searchElementValue' + searchSelectedElement).innerHTML;
					document.getElementById('searchKind').value = searchKinds[searchSelectedElement];
					break;
				}
			}
			searchPrevSearch = document.getElementById('fulltext').value;
			searchHideKeywords();
			NavigateSearch();
		} else {
			NavigateSearch();
		}
	}
}

function eventAdd(aobject, aeventstring, afunction) {
	if(window.addEventListener){ // Mozilla, Netscape, Firefox
		aobject.addEventListener(aeventstring, afunction, false);
	} else { // IE
		aobject.attachEvent('on' + aeventstring, afunction);
	}
}

// attach to textbox
function attachSearchHandler() {
	var fulltext = document.getElementById('fulltext');
	if (!fulltext) {
		setTimeout('attachSearchHandler();', 500);
	} else {
		eventAdd(fulltext, 'keyup', handleSearch);
		if (fulltext.captureEvents) fulltext.captureEvents(Event.KEYUP);
		eventAdd(fulltext, 'keydown', handleSearchEnter);
		if (fulltext.captureEvents) fulltext.captureEvents(Event.KEYDOWN);
	}
}
setTimeout('attachSearchHandler();', 500);

function searchShowKeywords() {
	if (searchElementCount > 0) {
		document.getElementById('keywordContainer').style.visibility = 'visible';
	}
}

function searchHideKeywords() {
	document.getElementById('keywordContainer').style.visibility = 'hidden';
}

function getElementsByClassName(className, tag, elm){
	var testClass = new RegExp("(^|\\s)" + className + "(\\s|$)");
	var tag = tag || "*";
	var elm = elm || document;
	var elements = (tag == "*" && elm.all)? elm.all : elm.getElementsByTagName(tag);
	var returnElements = [];
	var current;
	var length = elements.length;
	for(var i=0; i<length; i++){
		current = elements[i];
		if(testClass.test(current.className)){
			returnElements.push(current);
		}
	}
	return returnElements;
}



net.lDynSearchLoader = function(mode, keyword, kind) {
	this.XMLreq = null;
	this.keyword = keyword;
	this.kind = kind;
	switch(mode) {
		case 'requestKeywords':
			if (searchPrevSearch != keyword) {
				// document.getElementById('loading').style.display = 'block';
				document.getElementById('searchKind').value = '';
				searchPrevSearch = keyword;
				this.requestKeywords();
			}
		break;
		case 'recordHit':
			this.recordHit();
		break;
	}

}
net.lDynSearchLoader.prototype = {
	requestKeywords:function() {
		var keywordContainer = document.getElementById('keywordContainer');
		if (keywordContainer) {
			this.handleXMLHTTPRequest(nav_path + 'searchhandler.php', 'mode=requestKeywords&keyword=' + this.keyword, this.fillKeywords);
		}
	}
,
	recordHit:function() {
		this.handleXMLHTTPRequest(nav_path + 'searchhandler.php', 'mode=recordHit&keyword=' + this.keyword + '&kind=' + this.kind, this.dummyFunc);
	}
,
	handleXMLHTTPRequest:function (url, params, handler) {
		this.XMLreq = null;
		if (window.XMLHttpRequest) {
			this.XMLreq = new XMLHttpRequest();
		} else if (typeof ActiveXObject != "undefined") {
			this.XMLreq = new ActiveXObject("Microsoft.XMLHTTP");
		}
		if (this.XMLreq) {
			this.fillFunc = handler;

			var loader = this;
			var caller = loader.ResponseHandler;
			this.XMLreq.onreadystatechange = function () {
				caller.call(loader);
			}
			this.XMLreq.open("POST",url,true);
			this.XMLreq.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			this.XMLreq.send(params);
		}
	}
,
	ResponseHandler:function () {
		var ready = this.XMLreq.readyState;
		var data = null;
		if (ready == net.READY_STATE_COMPLETE) {
			data = this.XMLreq.responseXML;
			this.fillFunc(data);
			this.XMLreq = null;
		}
	}
,
	dummyFunc:function (data) {
	}
,
	fillKeywords:function (data) {
		var keywordContainer = document.getElementById('keywordContainer');
		var buffer = '';
		var elements = null;

		searchSelectedElement = -1;
		searchElementCount = 0;
		searchKinds = new Array();
		if (document.getElementById('fulltext').value == this.keyword) {
			if (elements = data.getElementsByTagName('keywords')[0].getElementsByTagName('element')) {
				for (var x = 0; x < elements.length; x++) {
					var keyword = elements[x].getElementsByTagName('keyword')[0];
					var kind = elements[x].getElementsByTagName('kind')[0];

					var attr = keyword.attributes;
					var title = attr.getNamedItem('title').value;

					if (title.length > 18) var showtitle = title.substr(0, 18) + '...'; else showtitle = title;

					var attr = kind.attributes;
					var kind = attr.getNamedItem('title').value;
	

					searchKinds[x] = kind;

					buffer += '<div id="searchElementValue' + x + '" style="display:none;">';
					buffer += title;
					buffer += '</div>';
	
					buffer += '<div class="searchKeyword" id="searchElement' + x + '" onmouseover="searchHoverElement(' + x + ')" onclick="searchSelectElement()">';
					buffer += showtitle;
					buffer += '</div>';
				}
	
				searchElementCount = x;
	
				if (buffer == '') {
					keywordContainer.style.visibility = 'hidden';
					keywordContainer.innerHTML = buffer;
				} else {
					keywordContainer.innerHTML = buffer;
					keywordContainer.style.visibility = 'visible';
				}

			}
		}

		// document.getElementById('loading').style.display = 'none';
	}

}

/* Pad naar de navigatie map */
var nav_path = 'http://www.4launch.nl/shop/blocks/block_nav/';

function extNavigateMenu(uber, hoofd, sub, merk) {
	if (merk == null) {
		start_search('', uber, hoofd, sub, 'CONTENT_CENTER');
	} else {
		start_search(merk, uber, hoofd, sub, 'CONTENT_CENTER');
	}
}


function extLoadUberCatsDrop(uber, merk, hoofd, sub) {
	var tempObj = new ext.lDynMenuLoader('dropuber', uber, merk, hoofd, sub);
}
function extLoadHoofdCatsDrop(uber, merk, preloadHoofd, sub) {
	var tempObj = new ext.lDynMenuLoader('drophoofd', uber, merk, preloadHoofd, sub, preloadHoofd != null);
}
function extLoadSubCatsDrop(uber, merk, hoofd, sub) {
	var tempObj = new ext.lDynMenuLoader('drop', uber, merk, hoofd, sub);
}
function extLoadSubCatMerkenDrop(uber, hoofd, sub, merk) {
	var tempObj = new ext.lDynMenuLoader('dropsubmerk', uber, merk, hoofd, sub);
}
function extResetDropMenu() {
	extLoadSubCatMerkenDrop('', '', '', '');
	extLoadUberCatsDrop('', '', '', '');
}
function extPreloadDropDown(merk, uber, hoofd, sub) {
	var merkDropdown = document.getElementById('extmerkDropdown');
	for (var x = 0; x < merkDropdown.options.length; x++) {
		if (merkDropdown.options[x].value == merk) {
			merkDropdown.options.selectedIndex = x;
			var tempObj = new ext.lDynMenuLoader('dropuber', uber, merk, hoofd, sub, true);
		}
	}
}
var ext = new Object();

ext.READY_STATE_UNINITIALIZED=0;
ext.READY_STATE_LOADING=1;
ext.READY_STATE_LOADED=2;
ext.READY_STATE_INTERACTIVE=3;
ext.READY_STATE_COMPLETE=4;

ext.lDynMenuLoader = function(mode, uber, merk, hoofd, nr, preloadHoofd) {
	this.XMLreq = null;
	this.uberCat = uber;
	this.merk = merk;
	this.hoofdCat = hoofd;
	this.Number = nr; // also used as sub selection for merk
	this.mode = mode;
	this.preloadHoofd = preloadHoofd;

	// document.getElementById('loading').style.display = 'block';

	switch(mode) {
		case 'dropuber':
			this.requestDropUbercats();
		break;
		case 'drophoofd':
			this.requestDropHoofdcats();
		break;
		case 'dropsubmerk':
			this.requestDropSubMerk();
		break;
		default:
			this.requestDropSubcats();
		break;
	}

}
ext.lDynMenuLoader.prototype = {
	requestDropUbercats:function() {
		var ubercatDrop = document.getElementById('extuberDropdown');
		var hoofdcatDrop = document.getElementById('exthoofdCatDropdown');
		var subcatDrop = document.getElementById('extsubCatDropdown');

		ubercatDrop.options.length = 0;
		ubercatDrop.disabled = true;

		hoofdcatDrop.options.length = 0;
		hoofdcatDrop.disabled = true;

		subcatDrop.options.length = 0;
		subcatDrop.disabled = true;
		subcatDrop.style.display = 'none';
		document.getElementById('extsubcatHeader1').style.display = 'none';
		document.getElementById('extsubcatHeader2').style.display = 'none';

		this.handleXMLHTTPRequest(nav_path + 'menuhandler.php', 'mode=uber&uber=' + escape(this.uberCat) + '&merk=' + escape(this.merk), this.DropFillUbercat);
	}
,
	requestDropHoofdcats:function() {
		var hoofdcatDrop = document.getElementById('exthoofdCatDropdown');
		var subcatDrop = document.getElementById('extsubCatDropdown');

		hoofdcatDrop.options.length = 0;
		hoofdcatDrop.disabled = true;

		subcatDrop.options.length = 0;
		subcatDrop.disabled = true;
		subcatDrop.style.display = 'none';
		document.getElementById('extsubcatHeader1').style.display = 'none';
		document.getElementById('extsubcatHeader2').style.display = 'none';

		this.handleXMLHTTPRequest(nav_path + 'menuhandler.php', 'mode=hoofd&uber=' + escape(this.uberCat) + '&merk=' + escape(this.merk), this.DropFillHoofdcat);
	}
,
	requestDropSubcats:function() {
		var subcatDrop = document.getElementById('extsubCatDropdown');

		subcatDrop.options.length = 0;
		subcatDrop.disabled = true;
		subcatDrop.style.display = 'inline';
					document.getElementById('extsubcatHeader1').style.display = 'table-row';
			document.getElementById('extsubcatHeader2').style.display = 'table-row';
					this.handleXMLHTTPRequest(nav_path + 'menuhandler.php', 'uber=' + escape(this.uberCat) + '&merk=' + escape(this.merk) + '&hoofd=' + escape(this.hoofdCat), this.DropFillSubcat);
	}
,
	requestDropSubMerk:function() {
		this.handleXMLHTTPRequest(nav_path + 'menuhandler.php', 'uber=' + escape(this.uberCat) + '&merk=' + escape(this.merk) + '&hoofd=' + escape(this.hoofdCat) + '&sub=' + escape(this.Number), this.DropFillSubMerk);
	}
,
	handleXMLHTTPRequest:function (url, params, handler) {
		this.XMLreq = null;
		if (window.XMLHttpRequest) {
			this.XMLreq = new XMLHttpRequest();
		} else if (typeof ActiveXObject != "undefined") {
			this.XMLreq = new ActiveXObject("Microsoft.XMLHTTP");
		}
		if (this.XMLreq) {
			this.fillFunc = handler;

			var loader = this;
			var caller = loader.ResponseHandler;
			this.XMLreq.onreadystatechange = function () {
				caller.call(loader);
			}
			this.XMLreq.open("POST",url,true);
			this.XMLreq.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			this.XMLreq.send(params);
		}
	}
,
	ResponseHandler:function () {
		var ready = this.XMLreq.readyState;
		var data = null;
		if (ready == ext.READY_STATE_COMPLETE) {
			data = this.XMLreq.responseXML;
			this.fillFunc(data);
			this.XMLreq = null;
		}
	}
,
	DropFinishPreload:function () {
		var hoofdDropdown = document.getElementById('exthoofdCatDropdown');

		if (hoofdDropdown) {
			for (var x = 0; x < hoofdDropdown.options.length; x++) {
				if (hoofdDropdown.options[x].value == this.hoofdCat) {
					hoofdDropdown.options.selectedIndex = x;
					extLoadSubCatsDrop(this.uberCat, this.merk, this.hoofdCat);
				}
			}
		}
	}
,
	DropFillUbercat:function (data) {
		var ubercatDrop = document.getElementById('extuberDropdown');
		var ubermenus = null;
		var uberselect = -1;

		if ( (ubercatDrop) && (document.getElementById('extmerkDropdown')) ) {
			if (document.getElementById('extmerkDropdown').value == this.merk) {
				ubercatDrop.options.length = 0;
				ubercatDrop.options[0] = new Option('-- Geen voorkeur --', '');

				if (ubermenus = data.getElementsByTagName('ubermenu')) {
					for (var x = 0; x < ubermenus.length; x++) {
						var attr = ubermenus[x].attributes;
						var title = attr.getNamedItem('title').value;

						ubercatDrop.options[x + 1] = new Option(title, title);
	
						if (title == this.uberCat) uberselect = (x + 1);
					}
				}

				if (x == 1) uberselect = 1;

				// load merken
				this.DropFillMerken(data.getElementsByTagName('merk'));

				ubercatDrop.disabled = false;
				// document.getElementById('loading').style.display = 'none';

				if (uberselect != -1) {
					ubercatDrop.options.selectedIndex = uberselect;
				}
				extLoadHoofdCatsDrop(ubercatDrop.value, this.merk, this.hoofdCat, this.Number);
			}
		}
	}
,
	DropFillHoofdcat:function (data) {
		var hoofdcatDrop = document.getElementById('exthoofdCatDropdown');
		var hoofdmenus = null;
		var hoofdselect = -1;

		if ( (hoofdcatDrop) && (document.getElementById('extmerkDropdown')) ) {
			if (document.getElementById('extmerkDropdown').value == this.merk) {
				hoofdcatDrop.options.length = 0;
				hoofdcatDrop.options[0] = new Option('-- Geen voorkeur --', '');

				if (hoofdmenus = data.getElementsByTagName('hoofdmenu')) {
					for (var x = 0; x < hoofdmenus.length; x++) {
						var attr = hoofdmenus[x].attributes;
						var title = attr.getNamedItem('title').value;
	
						hoofdcatDrop.options[x + 1] = new Option(title, title);
						if (this.hoofdCat == title) hoofdselect = x + 1;
					}
	
					if (x == 1) hoofdselect = 1;
				}

				// load merken
				this.DropFillMerken(data.getElementsByTagName('merk'));

				hoofdcatDrop.disabled = false;
				// document.getElementById('loading').style.display = 'none';

				if (hoofdselect != -1) {
					hoofdcatDrop.options.selectedIndex = hoofdselect;
					extLoadSubCatsDrop(document.getElementById('extuberDropdown').value, this.merk, hoofdcatDrop.value, this.Number);
				}
			}
		}
	}
,
	DropFillSubcat:function (data) {
		var subcatDrop = document.getElementById('extsubCatDropdown');
		var submenus = null;
		var subselect = -1;

		if ( (subcatDrop) && (document.getElementById('exthoofdCatDropdown')) ) {
			if (document.getElementById('exthoofdCatDropdown').value == this.hoofdCat) {
				subcatDrop.options.length = 0;
				subcatDrop.options[0] = new Option('-- Geen voorkeur --', '');

				if (submenus = data.getElementsByTagName('submenu')) {
					for (var x = 0; x < submenus.length; x++) {
						var attr = submenus[x].attributes;
						var title = attr.getNamedItem('title').value;

						subcatDrop.options[x + 1] = new Option(title, title);
						if (this.Number == title) subselect = x + 1;
					}

					if (x == 1) subselect = 1;
				}

				// load merken
				this.DropFillMerken(data.getElementsByTagName('merk'));

				subcatDrop.disabled = false;
				// document.getElementById('loading').style.display = 'none';

				if (subselect != -1) {
					subcatDrop.options.selectedIndex = subselect;
				}
			}
		}
	}
,
	DropFillSubMerk:function (data) {
		if (document.getElementById('extsubCatDropdown')) {
			if (document.getElementById('extsubCatDropdown').value == this.Number) {
				// load merken
				this.DropFillMerken(data.getElementsByTagName('merk'));
			}
		}
	}
,
	DropFillMerken:function (merken) {
		var merkDrop = document.getElementById('extmerkDropdown');
		var merkselect = -1;

		if (merkDrop) {
			merkDrop.options.length = 0;
			merkDrop.options[0] = new Option('-- Geen voorkeur --', '');

			for (var x = 0; x < merken.length; x++) {
				var attr = merken[x].attributes;
				var title = attr.getNamedItem('title').value;

				merkDrop.options[x + 1] = new Option(title, title);

				if (this.merk == title) merkselect = (x + 1);
			}

			if (x == 1) merkselect = 1;

			if (merkselect != -1) {
				merkDrop.options.selectedIndex = merkselect;
			}
		}
	}

}

/* misc_functions.js
 * Diversie javascript functies voor algemeen gebruik
 */

/* open_tree()
 * Functie die als op een ubercattab geklikt wordt afgaat en ervoor moet gaan zorgen dat een tree of dropdown juist geopend wordt.
 */
function open_tree(ubercat, hoofdcat, subcat) {
	cookie_nav = readCookie('nav_state');
	if (cookie_nav == 'dropdownmenu') {
		reloadDrops(ubercat,hoofdcat,subcat);
	} else {
		selectTree(ubercat, hoofdcat, subcat);
	}
}

/* open_tree_ext()
 * Functie die als op een ubercattab geklikt wordt afgaat en ervoor moet gaan zorgen dat een tree of dropdown juist geopend wordt.
 */
function open_tree_ext(ubercat, hoofdcat, subcat, merk) {
	cookie_nav = readCookie('nav_state');
	if (cookie_nav == 'dropdownmenu') {
		if (document.getElementById('merkdropdown')) document.getElementById('merkdropdown').options.selectedIndex = 0;
		reloadDropsext(ubercat, merk, hoofdcat, subcat);
	} else {
		selectTree(ubercat, hoofdcat, subcat);
		
	}
}

/* Script om vanaf klantenservice weer verder te gaan naar een andere pagina
 */

function close_customer_support(new_page)
{
	open_url(new_page, 'CONTENT_CENTER');
	setTimeout('reset_style()', 20);
}

/* Hulp functie voor close_customer_support()
 */
function reset_style()
{
	document.getElementById('lock_website').style.display='none';	
	document.getElementById('popup_customersupport').style.display='none';
	showDropdowns();
}

/* change_login_form()
 * Pas login veld eigenschappen aan bij onFocus.
 * Als de focus op een loginveld komt dan moet het originele veld leeg gemaakt worden.
 * Als de focus op het wachtwoord veld komt dan moet het veld leeggemaakt worden en bovendien moet de achtergrondafbeelding weg.
 * Als de foucs weg gaat (onBlur) moeten de waarden herstelden worden. Dit gebeurt als fill ongelijk aan 'empty' is.
 */
 
function change_login_form(field, fill)
{
	var el = document.getElementById(field);
	if(fill == 'empty')
	{
		el.style.backgroundImage = '';
//		el.value = '';
	}
	else
	{
		if(field == 'wachtwoord' && el.value == '')
		{
			el.style.backgroundImage = 'url(' + imgsdir + '/buttons/password.gif)';	
		}
		else if(el.value == '')
		{
			el.style.backgroundImage = 'url(' + imgsdir + '/buttons/inlognaam.gif)';	
		}
	}
}

/* start_extended_search()
 * Bij uitgebreid zoeken wordt deze functie aangeroepen om alle waarden te sturen naar het producten overzicht.
 */

function start_extended_search(url, form)
{
	var default_value = 'null';
	var query_string  = '?extended=true';
	if(form.extuberDropdown.value != default_value)
	{
		query_string += '&ubercategorie=' + myEscape(form.extuberDropdown.value);
		createCookie('es_ubercategorie', myEscape(form.extuberDropdown.value));
	}
	
	if(form.exthoofdCatDropdown.value != default_value)
	{
		query_string += '&hoofdcategorie=' + myEscape(form.exthoofdCatDropdown.value);
		createCookie('es_hoofdcategorie', myEscape(form.exthoofdCatDropdown.value));
	}
	
	if(form.extsubCatDropdown.value != default_value)
	{
		query_string += '&subcategorie=' + myEscape(form.extsubCatDropdown.value);	
		createCookie('es_subcategorie', myEscape(form.extsubCatDropdown.value));
	}
	
	if(form.extmerkDropdown.value != default_value)
	{
		query_string += '&merk=' + myEscape(form.extmerkDropdown.value);	
		createCookie('es_merk', myEscape(form.extmerkDropdown.value));
	}
	
	if(form.direct_leverbaar.checked == true)
	{
		query_string += '&direct_leverbaar=1';
		createCookie('es_direct_leverbaar', 1);
	}
	
	if(form.prijs_vanaf.value != '')
	{
		query_string += '&prijs_vanaf=' + myEscape(form.prijs_vanaf.value);	
		createCookie('es_prijs_vanaf', myEscape(form.prijs_vanaf.value));
	}
	
	if(form.prijs_tot.value != '')
	{
		query_string += '&prijs_tot=' + myEscape(form.prijs_tot.value);
		createCookie('es_prijs_tot', myEscape(form.prijs_tot.value));
	}
	
	if(form.zoekterm.value != '')
	{
		query_string += '&zoekterm=' + myEscape(form.zoekterm.value);
		createCookie('es_zoekterm', myEscape(form.zoekterm.value));
	}
	
	//als er een vendorcode ingevuld is moet de rest genegeerd worden
	if(form.vendorcode.value != '')
	{
		query_string = '?extended=true&vendorcode=' + myEscape(form.vendorcode.value);
		createCookie('es_vendorcode', myEscape(form.vendorcode.value));
	}
	
	//als er een artikelnummer ingevuld is moet de rest ook genegeerd worden
	if(form.artikelnummer.value != '')
	{
		query_string = '?extended=true&artikelnummer=' + myEscape(form.artikelnummer.value);
		createCookie('es_artikelnummer', myEscape(form.artikelnummer.value));
	}

	open_url(url + query_string, 'CONTENT_CENTER');
}

/* switch_search_image()
 * Deze functie switcht tussen 2 afbeeldingen, op de ene afbeelding is de dropdown zoeken geselecteerd
 * en op de andere afbeelding is de tree zoeken geselcteerd 
 */
function switch_search_image(selected)
{
	if(selected == 'dropdownmenu')
	{
		createCookie('nav_state', 'dropdownmenu', 7);
		document.getElementById('categorie_zoeken').style.backgroundImage = "url(" + imgsdir + "/menus/zoek-cat.gif)";
		open_url('blocks/block_nav/dropdownmenu.php', 'BLOCK_NAV');
	}
	else
	{
		createCookie('nav_state', 'treemenu', 7);
		document.getElementById('categorie_zoeken').style.backgroundImage = "url(" + imgsdir + "/menus/cat-zoek.gif)";
		open_url('blocks/block_nav/treemenu.php', 'BLOCK_NAV');
	}
}

/* Cookie functies
 * Om cookies aan te maken, uit te lezen en te verwijderen
 */

function createCookie(name,value,days) {
	if (days) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = '4launch_config[' + name + "]="+encodeURIComponent(value)+expires+"; path=/";
}

function readCookie(name) {
	var nameEQ = '4launch_config[' + name + "]=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}

function eraseCookie(name) {
	createCookie(name,'',-1);
}

/* get_window_size()
 * Functie om de grootte van het scherm te bepalen. Niet de resolutie maar het scherm.
 * Dit is dus variabel.
 */
 
function get_window_size() {
  var myWidth = 0
  if( typeof( window.innerWidth ) == 'number' ) {
    //Non-IE
    myWidth = window.innerWidth;
  } else if( document.documentElement && ( document.documentElement.clientWidth) ) {
    //IE 6+ in 'standards compliant mode'
    myWidth = document.documentElement.clientWidth;
  } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
    //IE 4 compatible
    myWidth = document.body.clientWidth + 21;  /* Afwijking? */
  }
 return myWidth;
}

function process_tellafriend(client_name, client_email, recipient_email, recipient_name, subject, comments, product_id)
{
	var query_string = 'send=1&';
	var client_name_value		= myEscape(document.getElementById(client_name).value);
	var client_email_value 		= myEscape(document.getElementById(client_email).value);
	var recipient_name_value 	= myEscape(document.getElementById(recipient_name).value);
	var recipient_email_value 	= myEscape(document.getElementById(recipient_email).value);
	var comments_value 			= myEscape(document.getElementById(comments).value);
	
	query_string += 'client_name=' + client_name_value + '&client_email=' + client_email_value + '&recipient_name=' + recipient_name_value + '&recipient_email=' + recipient_email_value + '&comments=' + comments_value + '&productid=' + product_id;
	MessageService('tellafriend', query_string);
}

function process_transmit_cart_tellafriend(client_name, client_email, recipient_email, recipient_name, subject, comments, cartid) {
	var query_string = 'send=1&';
	var client_name_value = myEscape(document.getElementById(client_name).value);
	var client_email_value = myEscape(document.getElementById(client_email).value);
	var recipient_name_value = myEscape(document.getElementById(recipient_name).value);
	var recipient_email_value = myEscape(document.getElementById(recipient_email).value);
	var comments_value = myEscape(document.getElementById(comments).value);
	
	query_string += 'client_name=' + client_name_value + '&client_email=' + client_email_value + '&recipient_name=' + recipient_name_value + '&recipient_email=' + recipient_email_value + '&comments=' + comments_value + '&cartid=' + cartid;
	MessageService('transmit_cart_tellafriend', query_string);
}

function process_wekker(productid, levertijd) {
	var query_string = 'send=1';
	var any = false;

	if (document.getElementById('wekker_prijs_checkbox').checked) {
		query_string += '&wekker_prijs_checkbox=Y';
		query_string += '&wekker_prijs_dropto_textbox=' + myEscape(document.getElementById('wekker_prijs_dropto_textbox').value);
		any = true;
	} else
		query_string += '&wekker_prijs_checkbox=N';


	if (document.getElementById('wekker_levertijd_checkbox').checked) { 
		query_string += '&wekker_levertijd_checkbox=Y';
		any = true;
	} else
		query_string += '&wekker_levertijd_checkbox=N';


	query_string += '&wekker_naam_textbox=' + myEscape(document.getElementById('wekker_naam_textbox').value);
	query_string += '&wekker_email_textbox=' + myEscape(document.getElementById('wekker_email_textbox').value);

	query_string += '&productid=' + myEscape(productid) + '&levertijd=' + myEscape(levertijd);

//	POSTRequest('modules/producten/tabs/wekker.php', query_string, 'PRODUCTSINFOTAB');
	MessageService('wekker', query_string);
}

// verwerken Support aanvraag
function processSupportaanvraag() {
	if (true) {
		var query_string = 'send=1';
		query_string += '&soort=' + myEscape(document.getElementById('soortVal').value);
		query_string += '&productomschrijving=' + myEscape(document.getElementById('productomschrijving').value);
		query_string += '&probleemomschrijving=' + myEscape(document.getElementById('probleemomschrijving').value);
		query_string += '&opmerkingen=' + myEscape(document.getElementById('opmerkingen').value);

		POSTRequest('modules/account/supportverwerken.php', query_string, 'CONTENT_CENTER');
	}
}

/* update UpdateSoort
 * Update de soort particulier of zakelijk
 */
function UpdateSoort(soort, gotoVar)
{
if(gotoVar == 'bestelling') {
var query_string = '?action=update_soort&soort=' + soort.value;
open_url('modules/processing/bestelling/bestelling.php' + query_string, 'CONTENT_CENTER');
}
if(gotoVar == 'offerte') {
var query_string = '?action=update_soort&soort=' + soort.value;
open_url('modules/processing/offerte/offerte.php' + query_string, 'CONTENT_CENTER');
}
if(gotoVar == 'wenslijst') {
var query_string = '?action=update_soort&soort=' + soort.value;
open_url('modules/processing/wenslijst/wenslijst.php' + query_string, 'CONTENT_CENTER');
}
}


/* lock_website()
 * Kan worden gebruikt om de website helemaal af te schermen.
 */
function lock_website(noScroll)
{
	var lock = document.getElementById('lock_website');
	if(lock.style.display == 'none')
	{
		var content_height	= document.body.scrollHeight;
		if(content_height < document.body.clientHeight)
		{
			website_height		= document.body.clientHeight;	
		} else website_height = content_height;
		var client_width		= get_window_size();
		if(client_width > 1324)
		{
			lock.style.width	= client_width;
		}
		else
		{
			lock.style.width	= 1324;
		}
		
		hideDropdowns();

		lock.style.height		= website_height;
		lock.style.display	= '';

		if (!noScroll) {
			var scrOfX = 0, scrOfY = 0;
			if( typeof( window.pageYOffset ) == 'number' ) {
				//Netscape compliant
				scrOfY = window.pageYOffset;
				scrOfX = window.pageXOffset;
			} else if( document.body && ( document.body.scrollLeft || document.body.scrollTop ) ) {
				//DOM compliant
				scrOfY = document.body.scrollTop;
				scrOfX = document.body.scrollLeft;
			} else if( document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop ) ) {
				//IE6 standards compliant mode
				scrOfY = document.documentElement.scrollTop;
				scrOfX = document.documentElement.scrollLeft;
			}
			if (scrOfY > 140) window.scrollTo(scrOfX,0);
		}
	}
	else
	{
		lock.style.display = 'none';
		showDropdowns();
	}
}

/* Script dat buttons met een een hover functie 
 * goed laat laden in IE. IE heeft namelijk geen ondersteuning voor :hover events in CSS.
 * Deze functie kan worden uitgevoerd bij onLoad.
 */

function hover_buttons()
{
	input_fields = document.getElementsByTagName('input');
	var buttons = 0;
	for(i = 0; i < input_fields.length; i++)
	{
		/* Alleen de elementen met de class 'btn_' krijgen een onMouseOver en onMouseOut event.
		 */
		if(input_fields[i].className.indexOf('btn_') != -1) 	
		{
			buttons+=1;
			input_fields[i].name				= input_fields[i].className;
			input_fields[i].onmouseover	= function() { this.className = this.name + '_hover'};	
			input_fields[i].onmouseout		= function() { this.className = this.name};	
		}
	}
}

function show_ideal()
{
	/* Deze functie moet onder bepaalde condities een iDEAL popup weergeven.
	 * Als de betaal select op 'i' staat en send_select niet op 'g' staat.
	 */
	var ideal_img = document.getElementById('ideal');
	if (document.getElementById('pay_select')) {
		if ( (document.getElementById('pay_select').value == 'i') && (document.getElementById('send_select').value != 'g') )
		{
			ideal_img.style.display = '';
		}
		else
		{
			ideal_img.style.display = 'none';
		}
	}
	else
	{
		ideal_img.style.display = 'none';
	}
}

function load_block_config()
{
	load_config(
		'collapse_top5',
		'TOP',
		'TOP1'
	);
	load_config(
		'collapse_news',
		'NEWS',
		'NEWS1'
	);
	load_config(
		'collapse_hot',
		'HOT',
		'HOT1'
	);
	load_config(
		'collapse_login',
		'login_overview'
	);
	load_config(
		'collapse_winkelwagen',
		'WINKELWAGEN'
	);
	load_config(
		'collapse_nieuwsbrief',
		'nieuwsbrief_show'
	);	
}

function getTrueHeight(element) {
	var fullheight = 0;
	var tmpheight = 0;
	for (var i = 0; i < element.childNodes.length; i++) {
		tmpheight = 0;
		if (!isNaN(element.childNodes[i].offsetHeight)) tmpheight = element.childNodes[i].offsetHeight;
		if (tmpheight < 25) tmpheight = getTrueHeight(element.childNodes[i]);
		fullheight += tmpheight;
	}
	return fullheight;
}

// event handler voor resizen van het venster
function resizedWindow() {
	// correcte plaatsing van popupcontainer
	if (document.getElementById('popup_contact')) document.getElementById('popup_contact').style.left = (document.getElementById('space_holder').offsetLeft + 151) + 'px';
	if (document.getElementById('popup_offerte')) document.getElementById('popup_offerte').style.left = (document.getElementById('space_holder').offsetLeft + 151) + 'px';
	if (document.getElementById('popup_customersupport')) document.getElementById('popup_customersupport').style.marginLeft = (document.getElementById('space_holder').offsetLeft + 151) + 'px';
	if (document.getElementById('ideal_popup')) document.getElementById('ideal_popup').style.marginLeft = (document.getElementById('space_holder').offsetLeft + 151) + 'px';
	if (document.getElementById('CONTENT_CENTER')) {
					/* Block hoogte aanpassen  */
					var content_height = document.getElementById('CONTENT_CENTER').offsetHeight;
					if (content_height < 30) {
						// this is very small, please check all child elements to see if this is correct
						content_height = getTrueHeight(document.getElementById('CONTENT_CENTER'));
					}
					if (document.getElementById('block_right_wrapper')) {
						if (document.getElementById('block_right_wrapper').offsetHeight > (content_height + 30)) {
							content_height = document.getElementById('block_right_wrapper').offsetHeight;
						}
					}
					if(content_height + 30 > 910) //lange pagina
					{
						if (document.getElementById('blocks_right')) document.getElementById('blocks_right').style.height			= (content_height + 30) + 'px';	
						if (document.getElementById('NAVIGATION')) document.getElementById('NAVIGATION').style.height				= (content_height + 30) + 'px';
						if (document.getElementById('center_wrapper')) document.getElementById('center_wrapper').style.height		= (content_height + 30) + 'px';
							if (document.getElementById('left_space')) document.getElementById('left_space').style.height				= (content_height + 232) + 'px';
							if (document.getElementById('right_space')) document.getElementById('right_space').style.height			= (content_height + 232) + 'px';
					}
					else //korte pagina
					{
						if (document.getElementById('blocks_right')) document.getElementById('blocks_right').style.height = '930px';
						if (document.getElementById('NAVIGATION')) document.getElementById('NAVIGATION').style.height = '930px';
						if (document.getElementById('center_wrapper')) document.getElementById('center_wrapper').style.height	= '930px';
						if (document.getElementById('left_space')) document.getElementById('left_space').style.height = '1132px';
						if (document.getElementById('right_space')) document.getElementById('right_space').style.height = '1132px';
					}
	}
}

function DisplayRecent()
{
	var search_container = document.getElementById('recent_products');
	var image_status		= document.getElementById('search_switch_image');
	if(search_container.style.display == 'none')
	{
		hideDropdowns();
		image_status.src					 = imgsdir + '/menus/menu_active.gif';
		search_container.style.display = '';
	}
	else
	{
		image_status.src					 = imgsdir + '/menus/menu_close.gif';
		search_container.style.display = 'none';
		showDropdowns();
	}
}

function goHTTPS(page)
{
	window.location = 'https://www.4launch.nl/shop/get/' + page; 
}
function openImgSmallPopup(el, url) {
	var bpop = document.getElementById('imgpopupdiv');
	if (bpop) {
		var pos = findPos(el);
		var orgpos = findPos(document.getElementById('imgpopupdivorgpos'));
		bpop.style.left = ((pos[0] - 3) - orgpos[0]) + 'px';
		bpop.style.top = ((pos[1] - 1) - orgpos[1]) + 'px';
		bpop.innerHTML = '<div style="width: 200px; height: 150px; overflow: hidden; background-color: #FFF; text-align: left;"><img src="' + url + '" alt="" id="imgpopupdivimg" /></div>';
		bpop.style.display = '';
	}
}
function findPos(obj) {
	var curleft = curtop = 0;
	if (obj.offsetParent) {
		curleft = obj.offsetLeft
		curtop = obj.offsetTop
		while (obj = obj.offsetParent) {
			curleft += obj.offsetLeft
			curtop += obj.offsetTop
		}
	}
	return [curleft,curtop];
}


function DisplayBanner()
{
	if(document.getElementById('advertisement_banner'))
	{
		var width 	= get_window_size();
		var banner  = document.getElementById('advertisement_banner');
		if(width >= 1400 && banner.style.display == 'none') /* Banner tonen */
		{
			banner.style.display = '';
		}
		else if(width < 1400 && banner.style.display == '') /* Banner sluiten */
		{
			banner.style.display = 'none';
		}
	}
}

// deze functie wordt uitgevoerd als je direct een product in je winkelwagentje dumpt (met dat plusje)
function DirectCart(image_el, product_id)
{
	if(document.getElementById(image_el))
	{
		 // stel de nieuwe afbeelding in
		var image	= document.getElementById(image_el);
		image.src	= 'http://www.4launch.com/images/shop/icons/plus_ani.gif';
		// voeg het product toe
		open_url('includes/order/quickadd.php?productid=' + myEscape(product_id));
		// open de  originele afbeelding na een vertraging van 2sec
		setTimeout('if(document.getElementById(\'' + myEscape(image_el) + '\')) document.getElementById(\'' + myEscape(image_el) + '\').src=\'http://www.4launch.com/images/shop/icons/plus.gif\'', 2000);
	}
}

/* Functie om de scroll balk te laten zien indien het scherm niet op 100% is.
 */
function setScroll()
{
	var window_width = get_window_size();
	var screen_width = screen.width;		
	var body_el		 = document.getElementsByTagName('body')[0];
	
	if(screen_width	 > window_width)
	{
		body_el.style.overflowX = ''; 
	}
	else
	{
		body_el.style.overflowX = 'hidden'; 
		
		var scrOfX = 0, scrOfY = 0;
		if( typeof( window.pageYOffset ) == 'number' ) {
			//Netscape compliant
			scrOfY = window.pageYOffset;
			scrOfX = window.pageXOffset;
		} else if( document.body && ( document.body.scrollLeft || document.body.scrollTop ) ) {
			//DOM compliant
			scrOfY = document.body.scrollTop;
			scrOfX = document.body.scrollLeft;
		} else if( document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop ) ) {
			//IE6 standards compliant mode
			scrOfY = document.documentElement.scrollTop;
			scrOfX = document.documentElement.scrollLeft;
		}
		window.scrollTo(0, scrOfY);
	} 
}

function refreshBanner() {
	if(refresh_banner == true) { // het aftellen is klaar
		open_url('includes/rsbanner.php', 'right_space');
		setTimeout("DisplayBanner();", 500);
		// banner tijd weer resetten
		refresh_banner = false;
		setTimeout('refresh_banner = true', 60000);
	}
}

// IMAGE SWAP Functies (mouse over)

function MM_swapImgRestore() { //v3.0

  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;

}

function MM_preloadImages() { //v3.0

  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();

    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)

    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}

}



function MM_findObj(n, d) { //v4.01

  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {

    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}

  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];

  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);

  if(!x && d.getElementById) x=d.getElementById(n); return x;

}



function MM_swapImage() { //v3.0

  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)

   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}

}



function MM_openBrWindow(theURL,winName,features) { //v2.0

  window.open(theURL,winName,features);

}



/* category_tabs()

 * getFilename()

 * setNewPath()

 * 3 functies om de buttons bovenaan de pagina (met de categorien) juist te kleuren bij mouse over, on click etc..

 * Is redelijk complex omdat voorkomen moet worden dat er tegelijkertijd meerdere buttons rood worden (pagina vernieuwd niet)

 */

 

function category_tabs() {

	var imgs = document.getElementsByTagName('img');

	for(var i = 0; i < imgs.length; i++) {

		if(imgs[i].className.indexOf('ubercat_tab') > -1) {

			imgs[i].onmouseover = function() {

				var parts = getFilename(this.src); 

				if(parts[0].indexOf('_active')>-1) //als '_active' niet in de file source voorkomt

					return;

				this.src = setNewPath(this.src, parts[0]+'.'+parts[1], parts[0]+'_over.'+parts[1]);

			};

			imgs[i].onmouseout = function() {

				var parts = getFilename(this.src);

				this.src = setNewPath(this.src, parts[0]+'.'+parts[1], parts[0].replace('_over','')+'.'+parts[1]);

			};

			imgs[i].onclick = function() {
				tabSelect(this);
			};

		}

	}

}

function tabSelect(elementObj) {
	var buttons = document.getElementById('top_buttons').getElementsByTagName('li');
	for (var j = 0; j < buttons.length; j++) {
		var elements = buttons[j].getElementsByTagName('a');
		for (var i = 0; i < elements.length; i++) {
			elements[i].style.backgroundPosition = '0px 0px';
		}

		var elements = buttons[j].getElementsByTagName('span');
		for (var i = 0; i < elements.length; i++) {
			elements[i].style.backgroundPosition = 'right 0px';
		}
	}

	if (elementObj) {
		var elements = elementObj.getElementsByTagName('a');
		for (var i = 0; i < elements.length; i++) {
			elements[i].style.backgroundPosition = '0px -50px';
		}

		var elements = elementObj.getElementsByTagName('span');
		for (var i = 0; i < elements.length; i++) {
			elements[i].style.backgroundPosition = 'right -50px';
		}
	}
}

function getFilename(path) {

	var parts = path.split('/');

	return parts[parts.length-1].split('.');

}

function setNewPath(path, previous, newpath) {

	return path.replace(previous, newpath);

}

function switch_login_cart_image(image)
{
	if(document.getElementById('heading_login_cart'))
	{
		var login_cart	= document.getElementById('heading_login_cart');
		var block_login	= document.getElementById('block_1');
		var block_cart	= document.getElementById('block_2');
		
		if(image == 'cart') {
			if (login_cart.style.backgroundImage.indexOf('account') > 0)
				login_cart.style.backgroundImage = 'url(http://www.4launch.com/images/shop/menus/ww_account.gif)';
			else
				login_cart.style.backgroundImage = 'url(http://www.4launch.com/images/shop/menus/ww_inloggen.gif)';

			block_login.style.display = 'none';
			block_cart.style.display  = '';
			createCookie('login_cart_state', 'cart');
		} else {
			if (login_cart.style.backgroundImage.indexOf('account') > 0)
				login_cart.style.backgroundImage = 'url(http://www.4launch.com/images/shop/menus/account_ww.gif)';
			else
				login_cart.style.backgroundImage = 'url(http://www.4launch.com/images/shop/menus/inloggen_ww.gif)';

			block_login.style.display = '';
			block_cart.style.display  = 'none';
			createCookie('login_cart_state', 'login');
		}
	}
}	

/* Maak een winkelwagentje leeg.
 */
function cart_empty()
{
	// open cart.php opnieuw en geef een parameter mee om het winkelwagentje leeg te maken
	open_url('modules/processing/cart.php?cart_empty=1', 'CONTENT_CENTER');
}

/* Verwijder een product uit het winkelwagentje
 */
 
function cart_delete_item(product_id, page)
{
	if(page == 'block_winkelwagen')
	{
		// verwijderen via request naar block winkelwagen
		POSTRequest('blocks/block_winkelwagen.php', 'delete_item=' + product_id, 'block_2');
	}
	else
	{
		// verwijderen via request naar winkelwagen
		POSTRequest('modules/processing/cart.php', 'delete_item=' + product_id, 'CONTENT_CENTER');
	}
}

/* Winkelwagen updaten.
 * Eerst worden alle elementen opgehaald met de tag <input> en daarna wordt gekeken
 * of er 'cart_num_products' in voor komt. Zo worden alle velden doorlopen
 */
function cart_update(){
	var input = document.getElementsByTagName('input');
	var query_string = '';
	for(i = 0; i < input.length; i++)
	{
		/* Kijk of het wel een update-veld is.
		 */
		if(input[i].name.indexOf('cart_num_products') != -1)
		{
			/* Voeg de waarden als array toe aan de query string die met post verstuurd kan worden.
			 */
			var ampers = '&';
			if(i == input.length)
			{
				ampers = '';
			}
			query_string += 'num_products[' + input[i].id + ']=' + input[i].value + ampers;
		}
	}
	/* Verstuur de gegevens naar cart.php zodat het winkelwagentje ge-update kan worden.
	 */
	POSTRequest('modules/processing/cart.php', query_string, 'CONTENT_CENTER');}

/* Voeg een lijst producten aan een winkelwagentje toe als accessoire */
function cart_add(productid, num_products)
{
	// probeer accessoires toe te voegen aan het winkelwagentje
	var input = document.getElementsByTagName('input');
	var query_string = '';	
	var num_extra_products = num_products;
	
	if(document.getElementById('extra_products') && document.getElementById('extra_products').innerHTML == 'aanbiedingen')
	{
		num_extra_products = 1;
	}
	
	for(i = 0; i < input.length; i++)
	{
		/* Kijk of het wel een update-veld is.
		 */
		if(input[i].name.indexOf('cart_add_products') != -1)
		{
			/* Voeg de waarden als array toe aan de query string die met post verstuurd kan worden.
			 */
			if(input[i].checked == true)
			{
				var ampers = '&';
				if(i == input.length)
				{
					ampers = '';
				}
				query_string += 'add_product[' + input[i].value + ']=' + num_extra_products + ampers;
			}
		}
	}
	// voeg nu het gewone product toe aan de winkelwagen, inclusief het aantal	
	query_string += 'add_product[' + productid + ']=' + num_products;
	/* Verstuur de gegevens naar cart.php zodat het winkelwagentje ge-update kan worden.
	 */
	POSTRequest('modules/processing/cart.php', query_string, 'CONTENT_CENTER');
	/* update winkelwagentje ook (wel met vertraging)
	 * Omdat de PHP-code uitgevoerd moet worden voordat het winkelwagentje update
	 */
}

function open_checkout(valid_checkout, button)
{
	if(valid_checkout == 'true')
	{
		switch(button)
		{			
			case 'offerte':
			document.location.hash = 'p-36';
			break;
			
			case 'wenslijst':
			document.location.hash = 'p-37';
			break;
			
			default: // bestellen
			document.location.hash = 'p-35';
			break;
		}
	}
	else
	{
		switch(button)
		{	
			case 'offerte':
			MessageService('checkout', 'button=offerte');		
			break;
			
			case 'wenslijst':
			MessageService('checkout', 'button=wenslijst');	
			break;
			
			default: // bestelling
			MessageService('checkout', 'button=afronden');		
			break;
		}
	}
}

/* Afrekenen button aanpassen aan dropdown status  */

function update_checkout(state)
{
	if(document.getElementById('cart_checkout_button') && document.getElementById('cart_offerte_button') && document.getElementById('cart_wishlist_button'))
	{
		if(state == 'checkout')
		{
			document.getElementById('cart_checkout_button').onclick = function() { open_checkout('true', 'afronden'); }	
			document.getElementById('cart_offerte_button').onclick = function() { open_checkout('true', 'offerte'); }	
			document.getElementById('cart_wishlist_button').onclick = function() { open_checkout('true', 'wenslijst'); }	
		}
		else
		{
			document.getElementById('cart_checkout_button').onclick = function() { open_checkout('false', 'checkout'); }	
			document.getElementById('cart_offerte_button').onclick = function() { open_checkout('false', 'offerte'); }	
			document.getElementById('cart_wishlist_button').onclick = function() { open_checkout('false', 'wishlist'); }	
		}
	}
}

var contract_symbol			= imgsdir + '/menus/menu_close.gif';		
var expand_symbol				= imgsdir + '/menus/menu_active.gif';		
var semi_expand_symbol		= imgsdir + '/menus/menu_one.gif'; 	
																				
/* Menu functies */
function open_one(event_div, all_element, one_element)
{
	var full_el = document.getElementById(all_element);
	var semi_el = document.getElementById(one_element);
	var bar		= document.getElementById(event_div);
	var image   = document.getElementById(event_div + '_state');
	//open het gedeelde element
	semi_el.style.display = '';
	//sluit het complete element
	full_el.style.display = 'none';
	//pas het plaatje aan
	image.src  = semi_expand_symbol;
	//de volgende modus 
	bar.onclick = function () { close_all(event_div, all_element, one_element) }
	//sla de nieuwe waarde op in een cookie
	createCookie('collapse_config_' + full_el.id, 'open_one', 7);

	resizedWindow();
}

function close_all(event_div, all_element, one_element)
{
	var full_el = document.getElementById(all_element);
	var semi_el = document.getElementById(one_element);
	var bar		= document.getElementById(event_div);
	var image   = document.getElementById(event_div + '_state');
	// kijk of het element bestaat, dan moet ook het semi element gesloten worden
	if(one_element != null)
	{
		//sluit het element
		semi_el.style.display = 'none';
	}
	//sluit het hoofd element ook
	full_el.style.display = 'none';
	//pas het plaatje aan
	image.src  = contract_symbol;
	//de volgende modus 
	bar.onclick = function () { open_all(event_div, all_element, one_element) }
	//sla de nieuwe waarde op in een cookie
	createCookie('collapse_config_' + full_el.id, 'close_all', 7);

	resizedWindow();
}

//num_modes is 2 of 3, als hij twee is dan wordt er na deze functie verwezen naar close_all in plaats van open_one
function open_all(event_div, all_element, one_element)
{
	var full_el = document.getElementById(all_element);
	var semi_el = document.getElementById(one_element);
	var bar		= document.getElementById(event_div);
	var image   = document.getElementById(event_div + '_state');
	// kijk of het element bestaat, dan hebben we 3 mogelijkheden
	if(one_element != null)
	{
		//sluit het element
		semi_el.style.display = 'none';
		// 3 verschillende modi, dus de volgende is open one
		bar.onclick = function() { open_one(event_div, all_element, one_element) }
	}
	else // 2 verschillende modi, de volgende is close all
	{
		bar.onclick = function () { close_all(event_div, all_element, one_element) }
	}

	//sluit beide elementen
	full_el.style.display = '';
	//pas het plaatje aan
	image.src  = expand_symbol;
	//sla de nieuwe waarde op in een cookie
	createCookie('collapse_config_' + full_el.id, 'open_all', 7);

	resizedWindow();
}

//deze functie moet bij de onload geladen worden om alle plaatjes en standen goed te zetten
function load_config(event_div, all_element, one_element)
{
	var cookie = readCookie('collapse_config_' + document.getElementById(all_element).id);
	if(cookie == 'open_one')
	{
		open_one(event_div, all_element, one_element);
	}
	else if(cookie == 'close_all') /* Alles sluiten */
	{
		close_all(event_div, all_element, one_element);
	}
	else /* Cookie leeg of gelijk aan open_all? Dan moet alles open gegooid worden */
	{
		open_all(event_div, all_element, one_element);
	}
}

function register_user(user, password, password_repeat, email, email_repeat, https)
{
	if(https == null) // page is pagina waarnaar toe gestuurd wordt{
	{
		https = 'p-35';
	}

	var query_string				= '';
	var user_data					= myEscape(document.getElementById(user).value);
	var password_data				= myEscape(document.getElementById(password).value);
	var password_data_repeat		= myEscape(document.getElementById(password_repeat).value);
	var email_data					= myEscape(document.getElementById(email).value);
	var email_data_repeat			= myEscape(document.getElementById(email_repeat).value);

	/* Alle gegevens met een POST request versturen naar step3.php
	 */

	query_string = 'username=' + user_data + '&password=' + password_data + '&password_repeat=' + password_data_repeat + '&email=' + email_data + '&email_repeat=' + email_data_repeat + '&https=' + https;
	MessageService('register', query_string);
}

function login_user(user, password, https)
{
	if(https == null) // page is pagina waarnaar toe gestuurd wordt
	{
		https = 'p-18';
	}

	var query_string				= '';
	var user_data					= myEscape(document.getElementById(user).value);
	var password_data				= myEscape(document.getElementById(password).value);

	/* Alle gegevens met een POST versturen naar ste3p.php
	 */
	query_string = 'refresh=step3&username=' + user_data + '&password=' + password_data + '&https=' + https;
	MessageService('login', query_string);
}

function newsletter_subscribe(email_id, mode)
{
	var email = myEscape(document.getElementById(email_id).value);
	if(mode == 'in') {
		open_url('modules/account/register/register.php?preset_email=' + email, 'CONTENT_CENTER');
	} else {
		MessageService('newsletter', 'email=' + email + '&mode=' + mode);
	}
}

function newsletter_form(field_id)
{

	var field = document.getElementById(field_id);
	if(field.value == '-- e-mailadres --')
	{
		field.value = '';
	}

	else if(field.value == '')

	{

		field.value = '-- e-mailadres --';

	}

}



function close_message_popup()

{

	var div_el	= document.getElementsByTagName('div');



	/* Sluit eerst

	 * alle huidige popups

	 */

	for(var i = 0; i < div_el.length; i++)

	{

		/* Als 'popup' 

		 * in de class naam voorkomt

		 * moet het (meestal) element worden aangepast.

		 * Met uitzondering van de container en element id.

		 */

		 

		if(div_el[i].className.indexOf('popup') != -1)

		{

			/* Sluit alleen 

			 * de popups die open zijn.

			 */

			 

			if(div_el[i].style.display != 'none' && div_el[i].id != 'popup_container' && div_el[i].className != 'popup_product_enlarge')

			{

				div_el[i].style.display = 'none';

			}

		}

	}

	lock_website();

}

function register_user_standalone(customer_id, email, username, password, password_repeat)
{
  var	username_val				= myEscape(document.getElementById(username).value);
	var password_val				= myEscape(document.getElementById(password).value);
	var password_repeat_val = myEscape(document.getElementById(password_repeat).value);
	
	var query_string		= 'customer_id=' + customer_id + '&email=' + email + '&username=' + username_val + '&password=' + password_val + '&password_repeat=' + password_repeat_val;
	MessageService('register_standalone', query_string);
}

function showPollResults(id) {
	processForm('poll_', 'poll'); // verwerk poll resultaten
	open_url('http://www.4launch.nl/shop/blocks/block_poll.php?poll=' + id, 'block_7');
}

/* show_popup()
 * Laat soort van pop up zien als tabel over de website heen.
 * Is dus geen nieuw venster.
 * Heeft wel CSS nodig om te functioneren (position: absolute; z-index: 1; etc..)
 */
function show_popup(element_id)
{
	document.currentPopup = element_id;

	var popup_container	= document.getElementById('popup_container');
	var el		= document.getElementById(element_id);
	var div_el	= document.getElementsByTagName('div');
	/* Open dan eventueel een nieuwe popup
	 */
	
	if(el.style.display == 'none')
	{
		/*
		 Geef het element weer
		*/
		popup_container.style.display = '';
		el.style.display	= '';

		hideDropdowns();
	}
	else
	{
		popup_container.style.display = 'none';
		el.style.display = 'none';	

		showDropdowns();
	}
	
	/* Sluit eerst
	 * alle huidige popups
	 */
	for(var i = 0; i < div_el.length; i++)
	{
		/* Als 'popup' 
		 * in de class naam voorkomt
		 * moet het (meestal) element worden aangepast.
		 * Met uitzondering van de container en element id.
		 */
		 
		if(div_el[i].className.indexOf('popup') != -1)
		{
			/* Sluit alleen 
			 * de popups die open zijn.
			 */
			 
			if(div_el[i].style.display != 'none' && div_el[i].id != element_id && div_el[i].id != 'popup_container' && div_el[i].className != 'popup_product_enlarge')
			{
				div_el[i].style.display = 'none';
			}
		}
	}
}
function getIEVersionNumber() {
    var ua = navigator.userAgent;
    var MSIEOffset = ua.indexOf("MSIE ");
    
    if (MSIEOffset == -1) {
        return 0;
    } else {
        return parseFloat(ua.substring(MSIEOffset + 5, ua.indexOf(";", MSIEOffset)));
    }
}
// hide dropdowns if IE6 or below
function hideDropdowns() {
	if ( (navigator.appName == 'Microsoft Internet Explorer') && (getIEVersionNumber() < 7) ) {
		hideAllByTag('select');
	}
}
function showDropdowns() {
	if ( (navigator.appName == 'Microsoft Internet Explorer') && (getIEVersionNumber() < 7) ) {
		showAllByTag('select','inline');
	}
}
function showAllByTag(tagName,dispType) {
        var elements = document.getElementsByTagName(tagName);
        var i = 0;
        if (dispType == "") {
                dispType = inline;
        }
        while (i < elements.length) {
		if (elements[i].style)
	                elements[i].style.display = dispType;
                i++;
                }
}
function hideAllByTag(tagName) {
        var elements = document.getElementsByTagName(tagName);
        var i = 0;
        while (i < elements.length) {
		if (elements[i].style)
	                elements[i].style.display = "none";
                i++;
                }
}

// JavaScript Document

/* text_form()
 * Functie om een veld steeds een juiste 'default' waarde te geven.
 * Deze functie wordt aangeroepen bij onBlur en onFocus.
 * Er wordt gekeken of de default waarde ingesteld is, als dit het geval is betekent dit dat er focus is en
 * moet het veld leeggemaakt is, als dit niet zo is en het veld niet leeg is moet de default waarde ingesteld worden.
 */
function text_form(field_id, field_default_value)
{
	var field = document.getElementById(field_id);
	/* Als de huidige waarde gelijk
	 * aan de default waarde is moet deze leeggemaakt worden.
	 */
	if(field.value == field_default_value)
	{
		field.value = '';
	}
	else if(field.value == '')
	{
		field.value = field_default_value;
	}
}

function process_change_password(current_password, new_password, repeat_new_password)
{
	var c_pass = myEscape(document.getElementById(current_password).value);
	var n_pass = myEscape(document.getElementById(new_password).value);
	var r_pass = myEscape(document.getElementById(repeat_new_password).value);
	
	query_string = 'current_password=' + c_pass + '&new_password=' + n_pass + '&repeat_new_password=' + r_pass;
	MessageService('change_password', query_string);
}

function update_factuuradres(identical, country, kind, order, lock_identical)
{
	var query_string = '?identical=' + identical + '&kind=' + kind;
	if(identical) { /* Gegevens meesturen */
		var huisnummer			= document.getElementById('house_number');
		var plaats					= document.getElementById('city');
		var postcode				= document.getElementById('postcode');
		var land 						= document.getElementById('country');
		var soort						= document.getElementById('kind');
		var voorletters			= document.getElementById('first');
		var tussenvoegsels	= document.getElementById('between');
		var achternaam			= document.getElementById('lastname');
		var straat					= document.getElementById('street');
		// als er sprake is van een NL adres proberen mee te sturen
		if(straat) { // apart straat veld
				query_string += '&street=' + straat.value;
		} else {
			query_string += '&address=' + document.getElementById('address').value;
			 	
		}
		
		if(kind == 'rma') {
			query_string += '&house_number=' + huisnummer.value + '&postcode=' + postcode.value +
			'&city=' + plaats.value + '&country=' + land.value;
		} else {
			query_string += '&house_number=' + huisnummer.value + '&postcode=' + postcode.value +
			'&city=' + plaats.value + '&country=' + land.value + '&first=' + voorletters.value + '&lastname=' + achternaam.value;
		}
		
	}
	
	// als de gebruiker geen aflevergelijk == y mag kiezen omdat men dan buitenland zou afleveren.
	if(lock_identical) {
		query_string += '&lock_identical=true';
	}

	if(kind == 'rma') {
		query_string += '&order=' + order;
	}
	
	if(soort)
	{
		 query_string += '&registration_kind=' + soort.value + '&firstltr=' + voorletters.value +
		'&mid=' + tussenvoegsels.value + '&lastname=' + achternaam.value;
	}
	
	if(country != null)
	{
		query_string += '&selected_country=' + country;
	}
	
	if (!identical && country == 'Nederland') 
	{
		open_url('modules/processing/bestelling/delivery_auto_address.php' + query_string, 'delivery_address_form');	
	}
	else
	{
		open_url('modules/processing/bestelling/delivery_address.php' + query_string, 'delivery_address_form');	 
	}
}

function checkRMARequest(product_listed)
{
	var soort_val		  	= myEscape(document.getElementById('set_soort').value);
	var bestel_id_val		= myEscape(document.getElementById('bestel_id').value);
	var factuur_id_val		= myEscape(document.getElementById('factuur_id').value);
	var product_id_val		= myEscape(document.getElementById('product_id').value);
	var serienummer_val		= myEscape(document.getElementById('serienummer').value);
	var omschrijving_val		= '';
	if(document.getElementById('product_description'))
	{
		omschrijving_val = myEscape(document.getElementById('product_description').value);
	}
	
	var query_string  = 'soort=' + soort_val + '&bestel_id=' + bestel_id_val + '&factuur_id=' + factuur_id_val + '&product_id=' + product_id_val + '&serienummer=' + serienummer_val + '&product_description=' + omschrijving_val + '&product_listed=' + product_listed;
	POSTRequest('modules/account/rma_aanvraag/controle.php', query_string, 'rma_producten');
	if(document.getElementById('rma_fields')) { document.getElementById('rma_fields').style.display = 'none'; }
}

function processRMARequest(soort, bestel_id, set_rma_product, set_rma_bestel, product_id, reden, opmerkingen, factuur_id, serienummer, verzending, afhandeling, rekeninghouder, rekeningnummer, aflever_gelijk, aflever_naam, aflever_adres, aflever_postcode, aflever_plaats, aflever_land, iban, serienummer_klant)
{	
	var query_string			= '';
	var soort_val				= myEscape(document.getElementById(soort).value);
	// de zoekveld waarden (STAP 1)
	var field_bestel_val		= myEscape(document.getElementById(bestel_id).value);
	var field_product_val		= myEscape(document.getElementById(product_id).value);
	var field_factuur_val		= myEscape(document.getElementById(factuur_id).value);
	var field_serienummer_val	= myEscape(document.getElementById(serienummer).value);
	var field_description_val = '';
	if(document.getElementById('product_description'))
	{
		field_description_val  = myEscape(document.getElementById('product_description').value);
	}
	var serienummer_klant_val	= myEscape(document.getElementById(serienummer_klant).value);
	var product_listed				= myEscape(document.getElementById('product_listed').value);
	var afhandeling_val			= '';
	var iban_val						= '';
	var aflever_gelijk_val 	= '';
	var rekeninghouder_val  = '';
	var rekeningnummer_val  = '';
	
	query_string += '&soort=' + soort_val + '&field_bestel_id=' + field_bestel_val + '&field_product_id=' + field_product_val + '&field_factuur_id=' + field_factuur_val + '&field_serienummer_val=' + field_serienummer_val + '&field_description=' + field_description_val + '&serienummer_klant=' + serienummer_klant_val + '&product_listed=' + product_listed;
	// de eindkeuze velden (STAP2)

	if(document.getElementById(set_rma_product)) query_string += '&set_product_id=' + myEscape(document.getElementById(set_rma_product).value);
	if(document.getElementById(set_rma_bestel)) query_string += '&set_bestel_id=' + myEscape(document.getElementById(set_rma_bestel).value);
	
	if(document.getElementById('wrong_description'))
	{
		query_string += '&wrong_description=' + myEscape(document.getElementById('wrong_description').value);
	}
	
	// Rest van het formulier (Reden/ opmerking)
	var reden_val					= myEscape(document.getElementById(reden).value);
	var opmerkingen_val		= myEscape(document.getElementById(opmerkingen).value);
	var verzending_val		= myEscape(document.getElementById(verzending).value);
	if(document.getElementById(afhandeling))
	{
		afhandeling_val		= myEscape(document.getElementById(afhandeling).value);
	}
	if(document.getElementById(rekeninghouder))
	{
		var rekeninghouder_val	= myEscape(document.getElementById(rekeninghouder).value); 
		var rekeningnummer_val	= myEscape(document.getElementById(rekeningnummer).value); 
	}
	// aflevergegevens
	if(document.getElementById(aflever_adres)) { 
		query_string += '&aflever_adres=' + myEscape(document.getElementById(aflever_adres).value) 
	} else if(document.getElementById('delivery_street')) {
		query_string += '&aflever_adres=' + myEscape(document.getElementById('delivery_street').value + ' ' + document.getElementById('delivery_house_number').value);
	}
	if(document.getElementById(aflever_postcode)) query_string += '&aflever_postcode=' + myEscape(document.getElementById(aflever_postcode).value);
	if(document.getElementById(aflever_plaats)) query_string += '&aflever_plaats=' + myEscape(document.getElementById(aflever_plaats).value);
	if(document.getElementById(aflever_land)) query_string += '&aflever_land=' + myEscape(document.getElementById(aflever_land).value);
	if(document.getElementById('delivery_remarks')) query_string += '&aflever_opmerkingen=' + myEscape(document.getElementById('delivery_remarks').value);
	if(document.getElementById('delivery_recipient')) query_string += '&aflever_naam=' + myEscape(document.getElementById('delivery_recipient').value);
	if(document.getElementById(aflever_gelijk))
	{
		aflever_gelijk_val = myEscape(document.getElementById(aflever_gelijk).value);
	}
	if(document.getElementById(iban))
	{
		iban_val			= myEscape(document.getElementById(iban).value);
	}
	query_string += '&iban=' + iban_val + '&aflever_gelijk=' + aflever_gelijk_val;
	query_string += '&reden=' + reden_val + '&opmerkingen=' + opmerkingen_val + '&verzending=' + verzending_val + '&afhandeling=' + afhandeling_val + '&rekeninghouder=' + rekeninghouder_val + '&rekeningnummer=' + rekeningnummer_val;
	
	MessageService('rma_process', query_string, 'rma_send'); 
}

function processRMADropDown(soort)
{
	var aanleveren 		= document.getElementById('aanleveren_product');
	var afhandelen		= false;
	if(document.getElementById('afhandelen_rma'))
	{
		afhandelen 		= document.getElementById('afhandelen_rma');
	}
	var aflever_form	= document.getElementById('rma_afleveradres');

	/* Als het RMA product wordt verzonden en
	 * het product moet omgeruild worden. Dan mag er een afleveradres gekozen worden.
	 * Tenzij er gekozen is voor retourzending. Dan hoeft er niet afgeleverd te worden.
	 */
	if(afhandelen && aanleveren.value == 'y' && (afhandelen.value == 'n' || afhandelen.value == 'a') && soort != 'r')
	{
		aflever_form.style.display = '';	
	}
	else
	{
		aflever_form.style.display = 'none';	
	}
	resizedWindow(); // scherm opnieuw uitrekenen.
}
/* - Stelt de waarde van het RMA type in
 * - Geef de zoekvelden weer.
 * - Vernieuw het formulier als er al een bestel/factuur/artikel/serie nummer gekozen is
 * - Aan de hand van de nieuwe formulier velden, stel de schermhoogte opnieuw in met resizedWindow().
 */
function SendRMAType(type)
{
	var bestel_id	= document.getElementById('bestel_id').value;
	var factuur_id	= document.getElementById('factuur_id').value;
	var product_id	= document.getElementById('product_id').value;
	var serienummer	= document.getElementById('serienummer').value;
	var soort		= document.getElementById('set_soort');
	var omschrijving = '';
	if(document.getElementById('product_description'))
	{
		omschrijving = document.getElementById('product_description').value;
	}
	
	soort.value		= type;
	if(soort.value != '')
	{
		document.getElementById('rma_product_fields').style.display = ''; 
		resizedWindow();
	}	
	
	if(bestel_id != '' || factuur_id != '' || serienummer != '' || product_id != '' || omschrijving != '')
	{
		checkRMARequest('yes');	
	} 
}

function processEticket(kind, linked_id, day_field, time_field, mode)
{
	var day = myEscape(document.getElementById(day_field).value);
	var time = myEscape(document.getElementById(time_field).value);
	
	MessageService('eticket', '&kind=' + kind + '&linked_id=' + linked_id + '&day=' + day + '&time=' + time, 'eticket');
}

function narrowRMASelection(bestel_el, product_el, product_listed)
{
	var bestel_id = myEscape(document.getElementById(bestel_el).value);
	var factuur_id = myEscape(document.getElementById('factuur_id').value);
	var product_id = myEscape(document.getElementById(product_el).value);
	var serienummer = myEscape(document.getElementById('serienummer').value);
	var soort = myEscape(document.getElementById('set_soort').value);
	var product_description = '';
	if(document.getElementById('product_description'))
	{
		product_description = myEscape(document.getElementById('product_description').value);
	}
	
	var query_string = 'bestel_id=' + bestel_id + '&factuur_id=' + factuur_id + '&product_id=' + product_id + '&serienummer=' + serienummer + '&product_description=' + product_description + '&product_listed=' + product_listed + '&soort=' + soort;
	if(bestel_el == 'set_rma_bestel' && product_el == 'set_rma_product')
	{ /* Klant heeft gezocht op productomschrijving */
		query_string += '&search_way=description';
	}
	else if(bestel_el == 'bestel_id' && product_el == 'set_rma_product') // bestelnummer zoeken
	{
		query_string += '&search_way=order';	
	}
	else if(bestel_el == 'set_rma_bestel' && product_el == 'product_id') // artikelnummer zoeken
	{
		query_string += '&search_way=product';
	}
	else
	{
		query_string += '&search_way=invoice';
	}
	POSTRequest('modules/account/rma_aanvraag/rma_aanvulling.php', query_string, 'rma_fields');
}

function processForm(prefix, page)
{
	var query_string = '';
	var field = document.getElementsByTagName('input');
	var drop	= document.getElementsByTagName('select');
	var text	= document.getElementsByTagName('textarea');
	
	// voeg alle input velden toe aan de query string
	for(i = 0; i < field.length; i++)
	{
		if(field[i].name.substring(0, prefix.length) == prefix) // veld moet worden verstuurd
		{
			switch(field[i].type) {
				case 'radio':
					if(field[i].checked) {
						query_string += field[i].name.substring(prefix.length) + '=' + myEscape(field[i].value) + '&';
					}
				break;
				
				case 'checkbox':
					if(field[i].checked) {
						query_string += field[i].name.substring(prefix.length) + '=' + myEscape(field[i].value) + '&';
					} else {
						query_string += field[i].name.substring(prefix.length) + '=&';
					}
				break;
				
				default: 
				case 'text':
					query_string += field[i].name.substring(prefix.length) + '=' + myEscape(field[i].value) + '&';
				break;
			}
		}
	}
	// voeg alle dropdown velden toe aan de query string
	for(i = 0; i < drop.length; i++)
	{
		if(drop[i].name.substring(0, prefix.length) == prefix) // veld moet worden verstuurd
		{
			query_string += drop[i].name.substring(prefix.length) + '=' + myEscape(drop[i].value) + '&';
		}
	}
	// voeg alle textareas toe
	for(i = 0; i < text.length; i++)
	{
		if(text[i].name.substring(0, prefix.length) == prefix) // veld moet worden verstuurd
		{
			query_string += text[i].name.substring(prefix.length) + '=' + myEscape(text[i].value) + '&';
		}
	}
	MessageService(page, query_string); 
}

function insertAddress(rule_id)
{
	startRequest('insert_address', 'http://www.4launch.nl/shop/includes/functions/php/process/insert_address.php', 'address_id=' + rule_id);  
}

function processInsertAddress()
{
	if(xmlreqpc.readyState == 4)		
	{
		if (document.getElementById('update_address_button')) document.getElementById('update_address_button').src = 'http://www.4launch.com/images/shop/buttons/update.gif';
		if(xmlreqpc.status == 200)
		{
			var response			= xmlreqpc.responseXML;
			var recipient 		= '';
			var street				= '';
			var house_number	= '';
			var remarks				= '';
			var postcode			= '';
			var city					= '';
			var country				=	'';
			if(response.getElementsByTagName('address')[0])
			{
				if(response.getElementsByTagName('recipient')[0].childNodes[0]) recipient	= response.getElementsByTagName('recipient')[0].childNodes[0].nodeValue;
				if(response.getElementsByTagName('house_number')[0].childNodes[0]) house_number = response.getElementsByTagName('house_number')[0].childNodes[0].nodeValue;
				if(response.getElementsByTagName('remarks')[0].childNodes[0]) remarks = response.getElementsByTagName('remarks')[0].childNodes[0].nodeValue;
				
				street				= response.getElementsByTagName('street')[0].childNodes[0].nodeValue;
				house_number	= response.getElementsByTagName('house_number')[0].childNodes[0].nodeValue;
				postcode			= response.getElementsByTagName('postcode')[0].childNodes[0].nodeValue;
				city					= response.getElementsByTagName('city')[0].childNodes[0].nodeValue;
				country				= response.getElementsByTagName('country')[0].childNodes[0].nodeValue;
			}
			
			document.getElementById('delivery_recipient').value = recipient;
			if(country == 'Nederland') {
				document.getElementById('delivery_address').value = street + ' ' + house_number;
			} else {
				document.getElementById('delivery_street').value = street;
			}
			document.getElementById('delivery_postcode').value = postcode;
			document.getElementById('delivery_city').value = city;
			document.getElementById('delivery_country').value = country;
			document.getElementById('delivery_remarks').value = remarks;
			document.getElementById('delivery_house_number').value = house_number;
		}
	}
}

function startRequest(page, file_name, query_string) {
	if(window.XMLHttpRequest)
		xmlreqpc = new XMLHttpRequest();
	else if(window.ActiveXObject)
		xmlreqpc = new ActiveXObject("Microsoft.XMLHTTP");
	
	if(xmlreqpc) {
		xmlreqpc.onreadystatechange = function() { locateRequest(page); };
		xmlreqpc.open("POST", file_name, true);
		xmlreqpc.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		xmlreqpc.send(query_string)
	} 
}

function locateRequest(page)
{
	switch(page)
	{
		case 'insert_address':
		processInsertAddress();
		break;
		
		case 'check_address':
		processCheckAddress();		
		break;
	}
}

function CheckAddress(form_prefix, postcode, house_number)
{
	startRequest('check_address', 'http://www.4launch.nl/shop/includes/functions/php/process/check_address.php', 'form_prefix=' + myEscape(form_prefix) + '&postcode=' + myEscape(postcode) + '&house_number=' + myEscape(house_number));  
}

function processCheckAddress()
{
	if(xmlreqpc.readyState == 4)		
	{
		if (document.getElementById('update_address_button')) document.getElementById('update_address_button').src = 'http://www.4launch.com/images/shop/buttons/update.gif';
		if(xmlreqpc.status == 200)
		{
			var response			= xmlreqpc.responseXML;
			var street				= '';
			var city					= '';
			var house_number 	= '';
			var prefix				= ''; // formulier voorvoegsel
			if(response.getElementsByTagName('form_prefix')[0].childNodes[0])
			{
				prefix		= response.getElementsByTagName('form_prefix')[0].childNodes[0].nodeValue;
			}
			if(response.getElementsByTagName('city')[0].childNodes[0])
			{
				street				= response.getElementsByTagName('street')[0].childNodes[0].nodeValue;
				city					= response.getElementsByTagName('city')[0].childNodes[0].nodeValue;
				house_number	= response.getElementsByTagName('house_number')[0].childNodes[0].nodeValue;
			}
			
			if(city != '' && street != '')
			{
				document.getElementById(prefix + 'address').value = street + ' ' + house_number;
				document.getElementById(prefix + 'city').value = city;
			}
			else
			{
				document.getElementById(prefix + 'address').value = '-- adres niet gevonden --';
				document.getElementById(prefix + 'city').value = '-- plaats niet gevonden --';
			}
			
			if(document.getElementById('set_identical') && document.getElementById('set_identical').value == 'y')
			{
				document.getElementById('delivery_house_number').value = house_number;
				document.getElementById('delivery_city').value = city;
				if(document.getElementById('delivery_street')) {
					document.getElementById('delivery_street').value = street;
				} else {
					document.getElementById('delivery_address').value = street + ' ' + house_number;
				}
			}
		}
	}
}

function changeAddressForm(country, page)
{
	var complete_address	= 'modules/account/addressbook/complete_address.php';
	var manual_address		= 'modules/account/addressbook/manual_address.php';
	var identical					= document.getElementById('set_identical');
	if(page == 'order')
	{
		complete_address = 'modules/processing/complete_address.php';
		manual_address	 = 'modules/processing/manual_address.php';
	}
	if(country == 'NL')
	{
		open_url(complete_address, 'address_fields');		
	}
	else
	{
		open_url(manual_address, 'address_fields');		
	}
	
	/* als men verzenden binnen Nederland gekozen heeft en aflevergelijk == y
			- en men kiest voor een niet NL land. Dient met automatisch aflevergelijk == n te krijgen
	*/
	if(identical) { // er is een afleveradres in te vullen
		if(country != 'NL' && document.getElementById('spc_method').value == 'v') {
			if(identical.value == 'y') {
				update_factuuradres(false, 'Nederland', null, null, true);
			} else {
				document.getElementById('identical_yes').disabled = true;
			}
		} else if(document.getElementById('identical_yes').disabled) {
			update_factuuradres(true);
		}
	}
}

function setRecipient() {
	if(document.getElementById('delivery_recipient'))
	{
		if(document.getElementById('set_identical') && document.getElementById('set_identical').value == 'y')
		{
			var first = document.getElementById('first');
			var mid		= document.getElementById('between');
			var last	= document.getElementById('lastname');
			var client_name	= first.value;
			if(mid)
			{
				client_name += ' ' + mid.value;
			}
			client_name += ' ' + last.value;
			document.getElementById('delivery_recipient').value = client_name;
		}
	}	
}

function process_contactform(recipient, subject, message, name, email, phone, cellphone, order, rma)
{
	var recipient_value	= myEscape(document.getElementById(recipient).value);
	var subject_value		= myEscape(document.getElementById(subject).value);
	var message_value		= myEscape(document.getElementById(message).value);
	var name_value			= myEscape(document.getElementById(name).value);
	var email_value			= myEscape(document.getElementById(email).value);
	var phone_value			= myEscape(document.getElementById(phone).value);
	var cellphone_value	= myEscape(document.getElementById(cellphone).value);
	var order_value			= myEscape(document.getElementById(order).value); 
	var rma_value				= myEscape(document.getElementById(rma).value);
	var query_string = 'send=1&recipient=' + recipient_value + '&subject=' + subject_value + '&message=' + message_value + '&name=' + name_value + '&email=' + email_value + '&phone=' + phone_value + '&cellphone=' + cellphone_value + '&order=' + order_value + '&rma=' + rma_value;
	POSTRequest('modules/popup/contact/process.php', query_string, 'popup_contact');
}

function process_offertemail()
{
	var query_string = 'omschrijving=' + myEscape(document.getElementById('omschrijving').value);
	if(document.getElementById('bedrijfsnaam')) {
		query_string += '&bedrijfsnaam=' + myEscape(document.getElementById('bedrijfsnaam').value);
		query_string += '&naam=' + myEscape(document.getElementById('naam').value);
		query_string += '&emailadres=' + myEscape(document.getElementById('emailadres').value);
		query_string += '&telefoonnummer=' + myEscape(document.getElementById('telefoonnummer').value);
	}
	
	POSTRequest('modules/popup/offerte/process.php', query_string, 'popup_offerte');
}

function switchCustomerType(type) {
	var query_string = '';
	query_string += 'first=' + document.getElementById('first').value + '&';
	query_string += 'firstname=' + document.getElementById('firstname').value + '&';
	query_string += 'between=' + document.getElementById('between').value + '&';
	query_string += 'lastname=' + document.getElementById('lastname').value + '&';
	query_string += 'gender=' + document.getElementById('set_gender').value + '&';
	query_string += 'year=' + document.getElementById('year').value + '&';
	query_string += 'month=' + document.getElementById('month').value + '&';
	query_string += 'day=' + document.getElementById('day').value + '&';
	query_string += 'country=' + document.getElementById('country').value + '&';
	query_string += 'phone=' + document.getElementById('register_phone').value + '&';
	query_string += 'cellphone=' + document.getElementById('register_cellphone').value + '&';
	query_string += 'postcode=' + document.getElementById('postcode').value + '&';
	query_string += 'house_number=' + document.getElementById('house_number').value + '&';
	if(document.getElementById('street')) query_string += 'street=' + document.getElementById('street').value + '&';
	if(document.getElementById('address')) query_string += 'address=' + document.getElementById('address').value + '&';
	query_string += 'city=' + document.getElementById('city').value;

	if (document.getElementById('newsletter')) query_string += '&newsletter=' + document.getElementById('newsletter').value;
	
	if(document.getElementById('opt_out') && document.getElementById('opt_out').value == 'no') {
		query_string += '&opt_out=no';			
	}

	if(type == 'z') {// business
		POSTRequest('modules/processing/business_form.php', query_string, 'customer_data');
	} else {
		POSTRequest('modules/processing/private_form.php', query_string, 'customer_data');
	}
	
}

function open_url(url, el) {
	if(url.indexOf('http://') == -1)
		var url = 'http://www.4launch.nl/shop/' + url;
	
	if(url.indexOf('?') == -1) 
		url = url + '?sid=' + Math.random();		
	else
		url = url + '&sid=' + Math.random();	
	
	var tempObj = new net.get_request(url, el);
}

net.get_request_busy = Array();
net.get_request_busy_number = Array();

net.get_request = function(url, el) {
	this.url = url;
	this.el = el;
	this.number = this.setBusyflag();

	// create XML request
	this.XMLreq = null;
	if (window.XMLHttpRequest) {
		this.XMLreq = new XMLHttpRequest();
	} else if (typeof ActiveXObject != "undefined") {
		this.XMLreq = new ActiveXObject("Microsoft.XMLHTTP");
	}
	if (this.XMLreq) {
		var loader = this;
		var caller = loader.ResponseHandler;
		this.XMLreq.onreadystatechange = function () {
			caller.call(loader);
		}
		this.XMLreq.open("GET",url,true);
		this.XMLreq.send(null);
	}
}

net.loadContent = function(element, response) {
		if (document.getElementById(element)) document.getElementById(element).innerHTML = response;
		loadJSLinks(element);

		if (element == 'block_2') {
			switch_login_cart_image('cart');
		}

		// reset de scroll positie als we in CONTENT CENTER laden
		if (element == 'CONTENT_CENTER') {
			document.getElementById('CONTENT_CENTER').style.visibility = 'visible';
			var scrOfX = 0, scrOfY = 0;
			if( typeof( window.pageYOffset ) == 'number' ) {
				//Netscape compliant
				scrOfY = window.pageYOffset;
				scrOfX = window.pageXOffset;
			} else if( document.body && ( document.body.scrollLeft || document.body.scrollTop ) ) {
				//DOM compliant
				scrOfY = document.body.scrollTop;
				scrOfX = document.body.scrollLeft;
			} else if( document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop ) ) {
				//IE6 standards compliant mode
				scrOfY = document.documentElement.scrollTop;
				scrOfX = document.documentElement.scrollLeft;
			}
			if (scrOfY > 140) window.scrollTo(scrOfX,0);
		}
				
		setTimeout('resizedWindow()', 10);

		// execute script elements
		if(!window.execScript)
			window.execScript = function(script) { setTimeout(script, 0);}
				

//		var re = /<script(\s[^>]*)?>([\s\S]*?)<\/script>/gi, match;
		var re = /<script(\s[^>]*)?>([^¤]*)¤?<\/script>/gi, match;

		response = response.replace(/<\/script>/gi, "¤<\/script>");
		while(match = re.exec(response)) {
			window.execScript(match[2], 'javascript');
		}		
}

net.get_request.prototype = {
	setBusyflag:function() {
		if (!net.get_request_busy_number[this.el]) net.get_request_busy_number[this.el] = 0;
		net.get_request_busy_number[this.el]++;
		net.get_request_busy[this.el] = true;

		if (this.el == 'CONTENT_CENTER')
			setTimeout("if (net.get_request_busy['CONTENT_CENTER']) { if (document.getElementById('LOADING')) { document.getElementById('LOADING').style.visibility = 'visible'; } }", 500);

		return net.get_request_busy_number[this.el];
	}
,
	clearBusyflag:function() {
		if (net.get_request_busy_number[this.el] == this.number) {
			net.get_request_busy[this.el] = false;

			if (this.el == 'CONTENT_CENTER')
				if (document.getElementById('LOADING')) 
					document.getElementById('LOADING').style.visibility = 'hidden';
		}
	}
,
	ResponseHandler:function() {
		if (net.get_request_busy_number[this.el] != this.number) 
			this.XMLreq = null;
		else {
			var ready = this.XMLreq.readyState;
			var data = null;
			if (ready == net.READY_STATE_COMPLETE) {
				data = this.XMLreq.responseText;
				if (net.get_request_busy_number[this.el] == this.number)
					if (document.documentLoaded == 'Y')
						this.loadContent(data);

				this.clearBusyflag();
				this.XMLreq = null;
			}
		}
	}
,
	loadContent:function(response) {
		net.loadContent(this.el, response);
	}
}

var bezigPost = false;

function createXMLHttpRequest() {
    if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    else if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
}
 
function POSTRequest(file, query, element) {
	 if (!bezigPost){
        bezigPost = true;
	setTimeout("if (bezigPost) { if (document.getElementById('LOADING')) { document.getElementById('LOADING').style.visibility = 'visible'; } }", 500);	
	
	if(file.indexOf('http://') == -1)
	{
		var file = 'http://www.4launch.nl/shop/' + file;
	}

    createXMLHttpRequest();
   
    xmlHttp.open("POST", file, true);
	if(document.getElementById(element))
	{
		xmlHttp.onreadystatechange = function() { handleStateChange(element) };
    }
	xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");    
    xmlHttp.send(query);

    }
    else{
        setTimeout('POSTRequest("'+file+'","'+addSlashes(query)+'","'+element+'")',100); 
    }
}
    
function handleStateChange(element) {
    if(xmlHttp.readyState == 4) {
        if(xmlHttp.status == 200) {
        	bezigPost = false;
		if (document.getElementById('LOADING')) 
			document.getElementById('LOADING').style.visibility = 'hidden';
            parseResults(element);
        }
    }
}

function parseResults(element) {
	var response = xmlHttp.responseText;
	// zie get request voor loadcontent code

	net.loadContent(element, response);
}

function MessageService(page, query_string, hide_button)
{
	// button weghalen indien nodig
	if(hide_button != null)
	{
		// button weghalen, tekst weergeven
		document.getElementById(hide_button + '_btn').style.display = 'none';
		document.getElementById(hide_button + '_text').style.display = '';
	}
	// oude popup leegmaken
	document.getElementById('popup_message').innerHTML = '';
	// de pagina waar we naartoe gaan
	url = 'http://www.4launch.nl/shop/includes/functions/php/request.php';
	query_string += '&page=' + page;
	
	if(window.XMLHttpRequest)
	{
		xmlreq = new XMLHttpRequest()
		xmlreq.onreadystatechange = function () { ProcessMessageService(hide_button) };
		xmlreq.open("POST",url,true)
		xmlreq.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		xmlreq.setRequestHeader("Content-length", query_string.length);
		xmlreq.setRequestHeader("Connection", "close");
		xmlreq.send(query_string)
	}
	else if(window.ActiveXObject)
	{
		xmlreq = new ActiveXObject("Microsoft.XMLHTTP")
		if(xmlreq)
		{
			xmlreq.onreadystatechange = function () { ProcessMessageService(hide_button) };
			xmlreq.open("POST",url,true)
			xmlreq.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			xmlreq.setRequestHeader("Content-length", query_string.length);
			xmlreq.setRequestHeader("Connection", "close");
			xmlreq.send(query_string)
		}	
	}
}

function ProcessMessageService(hide_button) 
{
	if(xmlreq.readyState == 4)		
	{
		if(xmlreq.status == 200)
		{
			// haal uit de XML response de berichten op
			var response		= xmlreq.responseXML;
			if(response.getElementsByTagName('type')[0]) 
			{
				var type				= response.getElementsByTagName('type')[0].childNodes[0].nodeValue;
				var count_type	= response.getElementsByTagName('content').length;
				var page				= '';
				var element			= '';
				var end 				= '&';
				var action			= '';
	
				if(count_type == 1) // een bericht
				{
					var content			 = response.getElementsByTagName('content')[0].childNodes[0].nodeValue;
					query_string_content = 'message=' + content;
				}
				else // meerdere berichten
				{
					query_string_content = ''; 
					for(i = 0; i < count_type; i++)
					{
						if(i == count_type)
						{
							end = '';
						}
						query_string_content += 'message[' + i + ']=' + response.getElementsByTagName('content')[i].childNodes[0].nodeValue + end;	
					}
				}
				
				if(response.getElementsByTagName('action')[0]) /* extra acties zoals uitloggen. Moet doorgestuurd worden naar het bericht. */
				{
					for(i = 0; i < response.getElementsByTagName('action').length; i++)
					{
						action += '&action[' + i + ']=' + response.getElementsByTagName('action')[i].childNodes[0].nodeValue;
						action += '&action_element[' + i + ']=' + response.getElementsByTagName('action_element')[i].childNodes[0].nodeValue;
					}
				}
				
				var first = null;
				if(response.getElementsByTagName('https')[0]) /* extra acties zoals uitloggen. Moet doorgestuurd worden naar het bericht. */
				{
					for(i = 0; i < response.getElementsByTagName('https').length; i++)
					{
						if (first == null) first = response.getElementsByTagName('https')[i].childNodes[0].nodeValue;
						query_string_content += '&https[' + i + ']=' + response.getElementsByTagName('https')[i].childNodes[0].nodeValue;
					}
				}
	
				query_string_content += action;
				
				// kijk of er ook een nieuwe pagina geladen moet worden
				if(response.getElementsByTagName('page')[0])
				{
					for(i = 0; i < response.getElementsByTagName('page').length; i++)
					{
						page			= response.getElementsByTagName('page')[i].childNodes[0].nodeValue;
						if (response.getElementsByTagName('element')[i])
							element		= response.getElementsByTagName('element')[i].childNodes[0].nodeValue;
						else
							element		= response.getElementsByTagName('action_element')[i].childNodes[0].nodeValue;
						if (element == 'CONTENT_CENTER') {
							document.location.hash = myUnEscape(page);
						} else {
							open_url(myUnEscape(page), element);
						}
					}
				}
				else if(hide_button != null) 
				{
					/* Wanneer een RMA een fout geeft de button weer weergeven. */
					document.getElementById(hide_button + '_btn').style.display = '';
					document.getElementById(hide_button + '_text').style.display = 'none';		
				}
	
				if (first != null) {
					// als inloggen alleen redirecten, geen box laten zien
					first = myUnEscape(first);
					goHTTPS(first);
				} else {
	
					// verstuur een nieuw request met het bericht
					POSTRequest('http://www.4launch.nl/shop/modules/popup/message.php', query_string_content + '&type=' + myEscape(type), 'popup_message');
					PositionPopup(document.getElementById('popup_message'));
	
					document.getElementById('popup_container').style.display = '';
					document.getElementById('popup_message').style.display = '';
					if(page != 'email_offerte') lock_website(true);
				}
			}
		}
	}
}
function PositionPopup(popup) {
  var myWidth = 0, myHeight = 0;
  if( typeof( window.innerWidth ) == 'number' ) {
    //Non-IE
    myWidth = window.innerWidth;
    myHeight = window.innerHeight;
  } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
    //IE 6+ in 'standards compliant mode'
    myWidth = document.documentElement.clientWidth;
    myHeight = document.documentElement.clientHeight;
  } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
    //IE 4 compatible
    myWidth = document.body.clientWidth;
    myHeight = document.body.clientHeight;
  }
  var scrOfX = 0, scrOfY = 0;
  if( typeof( window.pageYOffset ) == 'number' ) {
    //Netscape compliant
    scrOfY = window.pageYOffset;
    scrOfX = window.pageXOffset;
  } else if( document.body && ( document.body.scrollLeft || document.body.scrollTop ) ) {
    //DOM compliant
    scrOfY = document.body.scrollTop;
    scrOfX = document.body.scrollLeft;
  } else if( document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop ) ) {
    //IE6 standards compliant mode
    scrOfY = document.documentElement.scrollTop;
    scrOfX = document.documentElement.scrollLeft;
  }
	popup.style.left = ( (myWidth / 2) - (350 / 2) ) + 'px';
	popup.style.top = ( (myHeight / 2) - (200 / 2) + scrOfY) + 'px';
}

// The constructor should be called with
// the parent object (optional, defaults to window).

function Timer(){
    this.obj = (arguments.length)?arguments[0]:window;
    return this;
}

// The set functions should be called with:
// - The name of the object method (as a string) (required)
// - The millisecond delay (required)
// - Any number of extra arguments, which will all be
//   passed to the method when it is evaluated.

Timer.prototype.setInterval = function(func, msec){
    var i = Timer.getNew();
    var t = Timer.buildCall(this.obj, i, arguments);
    Timer.set[i].timer = window.setInterval(t,msec);
    return i;
}
Timer.prototype.setTimeout = function(func, msec){
    var i = Timer.getNew();
    Timer.buildCall(this.obj, i, arguments);
    Timer.set[i].timer = window.setTimeout("Timer.callOnce("+i+");",msec);
    return i;
}

// The clear functions should be called with
// the return value from the equivalent set function.

Timer.prototype.clearInterval = function(i){
    if(!Timer.set[i]) return;
    window.clearInterval(Timer.set[i].timer);
    Timer.set[i] = null;
}
Timer.prototype.clearTimeout = function(i){
    if(!Timer.set[i]) return;
    window.clearTimeout(Timer.set[i].timer);
    Timer.set[i] = null;
}

// Private data

Timer.set = new Array();
Timer.buildCall = function(obj, i, args){
    var t = "";
    Timer.set[i] = new Array();
    if(obj != window){
        Timer.set[i].obj = obj;
        t = "Timer.set["+i+"].obj.";
    }
    t += args[0]+"(";
    if(args.length > 2){
        Timer.set[i][0] = args[2];
        t += "Timer.set["+i+"][0]";
        for(var j=1; (j+2)<args.length; j++){
            Timer.set[i][j] = args[j+2];
            t += ", Timer.set["+i+"]["+j+"]";
    }}
    t += ");";
    Timer.set[i].call = t;
    return t;
}
Timer.callOnce = function(i){
    if(!Timer.set[i]) return;
    eval(Timer.set[i].call);
    Timer.set[i] = null;
}
Timer.getNew = function(){
    var i = 0;
    while(Timer.set[i]) i++;
    return i;
}

if (typeof encodeURIComponent != "function") {

	function encodeURIComponent(s) {
		var okURIchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-";
		var retval = '';
		for (var i = 0; i < s.length; i++) {
			if (okURIchars.indexOf(s.charAt(i)) == -1)
				retval += "%" + s.charCodeAt(i).toString(16);
    			else
				retval += s.charAt(i);
		}
		return retval;
	}
}
	
function jsTicker() {
	this.timer = new Timer(this);
	this.curpos = document.location.hash;
}

jsTicker.prototype.tick = function(randomNr) {
	if (this.curpos != document.location.hash) {
		this.curpos = document.location.hash;
		navigateJS(this.curpos);
	}
	if (document.jsTimerRandom == randomNr)
		this.timer.setTimeout("tick", 50, randomNr);
}

function initLoad() {
	if  ( (document.location.hash != '') && (document.location.hash != '#') ) {
		document.getElementById('CONTENT_CENTER').innerHTML = '';
		POSTRequest('http://www.4launch.nl/shop/ajaxcontent.php', 'location=' + document.location.hash.replace('&', '%26'), 'CONTENT_CENTER');
	}

	// init hm
	var hm = document.getElementById('historyManager');

	if (hm) {
		var location = document.location.hash;
		if ( (location == '') || (location == '#') ) {
			var match = document.location.href.match(/get\/(.*)/);
			if (match)
				if (match[1])
					location = match[1];
		}
		hm.src = 'http://www.4launch.nl/shop/historymanager.php?location=' + encodeURIComponent(location);
	}
}
               
function startJSWatch() {
	var randomNr = Math.floor(Math.random() * 1000);
	var jsticker = new jsTicker();
	document.jsTimerRandom = randomNr;
	jsticker.tick(randomNr);
}

function navigateJS(location) {
	// vernieuw banner:
	refreshBanner();
	var hm = document.getElementById('historyManager');
	if (hm) {
		hm.src = 'http://www.4launch.nl/shop/historymanager.php?location=' + encodeURIComponent(location);
	}

	POSTRequest('http://www.4launch.nl/shop/ajaxcontent.php', 'location=' + document.location.hash.replace('&', '%26'), 'CONTENT_CENTER');

	return false;
}

function loadJSLinks(element) {
	if (!element)
		var links = document.getElementsByTagName('a');
	else
		var links = document.getElementById(element).getElementsByTagName('a');
	for (var x = 0, link; link = links[x]; x++) {
		var matchCheck = link.href.match(/[#]/);
		if (!matchCheck) {
			var match = link.href.match(/\/get\/(.*)/);
			if (match)
				if (match[1])
					link.href = '#' + match[1]
				else
					link.href = '#';
		}
	}
}

function startJS() {
	initLoad();
	loadJSLinks();
	if (history.navigationMode)
		history.navigationMode="fast";
}

function ajSubmitForm(formObj) {
	var requestString  = '';

	var x = 0;

	var childObj = formObj.getElementsByTagName('input');

	for (x = 0; x < childObj.length; x++) {
		if ( (childObj[x].name.length != 0) && (childObj[x].value.length != 0) && (childObj[x].value.substr(0, 2) != '--') ) {
			if (childObj[x].type == 'checkbox') {
				if (childObj[x].checked) {
					if (requestString.length != 0) requestString += '-';

					requestString += ajDashEncode(childObj[x].name) + '-' + ajDashEncode(childObj[x].value);
				}
			} else {
				if (requestString.length != 0) requestString += '-';

				if (childObj[x].name == 'location')
					requestString += childObj[x].value;
				else
					requestString += ajDashEncode(childObj[x].name) + '-' + ajDashEncode(childObj[x].value);
			}
		}
	}

	var childObj = formObj.getElementsByTagName('select');

	for (x = 0; x < childObj.length; x++) {
		if ( (childObj[x].name.length != 0) && (childObj[x].value.length != 0) ) {
			if (requestString.length != 0) requestString += '-';

			requestString += ajDashEncode(childObj[x].name) + '-' + ajDashEncode(childObj[x].value);
		}
	}

	document.location.hash = requestString;

	return false;

}

function ajDashEncode(value) {
	return encodeURIComponent(value.replace(/-/g, '--'));
}


