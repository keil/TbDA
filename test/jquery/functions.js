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
 * $Id: functions.js 617 2010-11-28 15:36:38Z tvbadmin $ $HeadURL:
 * http://svn.rm-keil.de/rm-keil/webpages/rm-keil.de/Release%20(1.0)/httpdocs/javascripts/functions.js $
 * $Date: 2010-11-28 16:36:38 +0100 (So, 28 Nov 2010) $ $Author: tvbadmin $
 * $Revision: 617 $
 ******************************************************************************/

/*
 * deletes the standard value of the user inputfield
 */
function onFocusUser(field) {
	if (field.value == 'Benutzer')
		field.value = '';
}

/*
 * sets the standard value of the user inputfield
 */
function onBlurUser(field) {
	if (field.value == '')
		field.value = 'Benutzer';
}

/*
 * deletes the standard value of the password inputfield
 */
function onFocusPasswd(field) {
	if (field.value == 'Kennwort')
		field.value = '';
}

/*
 * sets the standard value of the password inputfield
 */
function onBlurPasswd(field) {
	if (field.value == '')
		field.value = 'Kennwort';
}

/*
 * deletes the standard value of the search inputfield
 */
function onFocusSearch(field) {
	if (field.value == 'Suchen')
		field.value = '';
}

/*
 * sets the standard value of the search inputfield
 */
function onBlurSearch(field) {
	if (field.value == '')
		field.value = 'Suchen';
}

/*
 * catch the pressed enter-key an submit the form values
 */
function enterEventLogin(event) {
	if (!event)
		var e = window.event
	if (event.keyCode)
		code = event.keyCode;
	else if (event.which)
		code = event.which;

	if (code == 13)
		document.getElementById('login').submit();
}

/*
 * catch the pressed enter-key an submit the form values
 */
function enterEventSearch(event) {
	if (!event)
		var e = window.event
	if (event.keyCode)
		code = event.keyCode;
	else if (event.which)
		code = event.which;

	if (code == 13)
		document.getElementById('search').submit();
}

/*
 * display state of edit button
 */
var editMode = false;

/*
 * disabels css effect
 */
function onMouseoverEdit(field) {
	if (!editMode)
		document.getElementById('editlist').style.display = 'none';
}

/*
 * change display state when click
 */
function onClickEdit(field) {
	if (editMode) {
		editMode = !editMode;
		document.getElementById('editlist').style.display = 'none';
	} else if (!editMode) {
		editMode = !editMode;
		document.getElementById('editlist').style.display = 'block';
	}
}

/*
 * hide error messages
 */
function hideError(element) {
	$('#error_wrapper').slideUp('slow', function() {});
}

/*
 * hide information messages
 */
function hideInformation(element) {
	$('#information_wrapper').slideUp('slow', function() {});
}

/*
 * hide messages
 */
function hideMessage(element) {
	$('#message_wrapper').slideUp('slow', function() {});
}