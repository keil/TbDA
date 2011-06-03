/**************************************************
 * PAGE
 **************************************************/

/**************************************************
 * @package webpage
 * @subpackage javascript
 **************************************************/

/**************************************************
 * @author: Roman Matthias Keil
 * @copyright: Roman Matthias Keil
 **************************************************/

/*******************************************************************************
 * $Id: effects.js 617 2010-11-28 15:36:38Z tvbadmin $ $HeadURL:
 * http://svn.rm-keil.de/rm-keil/webpages/rm-keil.de/Release%20(1.0)/httpdocs/javascripts/functions.js $
 * $Date: 2010-11-28 16:36:38 +0100 (So, 28 Nov 2010) $ $Author: tvbadmin $
 * $Revision: 617 $
 ******************************************************************************/


window.onload = function() {
	loadStylesheet();
}



//////////////////////////////////////////////////
//////////////////////////////////////////////////

/**
 * Query lightBox plugin
 */
$(function() {
	$('.picturebrowser a').lightBox();
	$('.picture a').lightBox();
	$('.sidepicture a').lightBox();
});



//////////////////////////////////////////////////
//////////////////////////////////////////////////

/**
 * Font-Size
 */

function setActiveStyleSheet(title) {
	var i, a, main;
	for(i=0; (a = document.getElementsByTagName("link")[i]); i++) {
		if(a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title")) {
			a.disabled = true;
			if(a.getAttribute("title") == title) a.disabled = false;
		}
	}
}

function setStylesheet(mode) {
	if(mode == 'small')		
		setSmall();
	else if(mode == 'large')
		setLarge();
	else
		setNormal();
}

function loadStylesheet() {
	$('#fontsize').css('display', 'block');
	
	if($.cookie('de_turnverein-boetzingen_fontsize') == 'small')
		setSmall();
	else if($.cookie('de_turnverein-boetzingen_fontsize') == 'large')
		setLarge();
	else
		setNormal();
}

function setNormal() {
	$.cookie('de_turnverein-boetzingen_fontsize', 'normal', { path: '/' });
	$.cookie('de_turnverein-boetzingen_fontsize', '', { expires: -1, path: '/' });

	$('#fontsize .small a').css('text-decoration', 'none');
	$('#fontsize .normal a').css('text-decoration', 'underline');
	$('#fontsize .large a').css('text-decoration', 'none');
	
	$('#fontsize_large').attr('rel', 'alternate stylesheet');
	$('#fontsize_small').attr('rel', 'alternate stylesheet');
	
	setActiveStyleSheet('');
}

function setSmall() {
	$.cookie('de_turnverein-boetzingen_fontsize', 'small', { path: '/' });
	
	$('#fontsize .small a').css('text-decoration', 'underline');
	$('#fontsize .normal a').css('text-decoration', 'none');
	$('#fontsize .large a').css('text-decoration', 'none');

	setActiveStyleSheet('fontsize_small');
}

function setLarge() {
	$.cookie('de_turnverein-boetzingen_fontsize', 'large', { path: '/' });

	$('#fontsize .small a').css('text-decoration', 'none');	
	$('#fontsize .normal a').css('text-decoration', 'none');
	$('#fontsize .large a').css('text-decoration', 'underline');
	
	setActiveStyleSheet('fontsize_large');
}



//////////////////////////////////////////////////
//////////////////////////////////////////////////

$(document).ready(
		function(){
			$('#homeheadline').innerfade({
				animationtype: 'fade',
				speed: 'slow',
				timeout: 4000,
				type: 'sequence',
				containerheight: '300px'
			}); 
		}
); 