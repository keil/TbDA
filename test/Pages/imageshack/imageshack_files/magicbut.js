function _magic_toolbar_required()
{
    var _e = {type:'click'};
    var _html = '<table>';
    _html += '<tr><td align="right" style="width:400px;">&nbsp;</td></tr>';
    _html += '<tr><td align="center">To upload videos you need to install the ImageShack Toolbar version 4.0.2 or later.<br/>Click <a href="http://toolbar.imageshack.us/" target="_blank" class="ser">here</a> to download and install the latest version</td></tr>';
    _html += '<tr><td>';
    _html += '<div style="text-align:center;">';
    _html += '&nbsp;<input style="width: 100px;" type="button" value="Close" onclick="light();hidetip();"/>';
    _html += '</div>';
    _html += '</td></tr>';
    _html += '</table>';
    _magic_tt(_html, null, _e, '400px', false);
}

function _magic_toolbar_wrong_browser()
{
    var _e = {type:'click'};
    var _html = '<table>';
    _html += '<tr><td align="right" style="width:400px;">&nbsp;</td></tr>';
    _html += '<tr><td>';
    _html += 'Sorry, in order to upload videos you need to have ImageShack Toolbar installed.<br/>';
    _html += 'However, your browser is not yet supported.<br/>';
    _html += 'Supported browsers are:<ul>';
    _html += '<li>Microsoft Internet Explorer 6.0 and later for Windows</li>';
    _html += '<li>Mozilla Firefox 1.5 and later for Windows</li>';
    _html += '</ul>';
    _html += '<br/>Click <a href="http://toolbar.imageshack.us/" target="_blank" class="ser">here</a> to download and install the latest version';
    _html += '</td></tr>';
    _html += '<tr><td>';
    _html += '<div style="text-align:center;">';
    _html += '&nbsp;<input style="width: 100px;" type="button" value="Close" onclick="light();hidetip();"/>';
    _html += '</div>';
    _html += '</td></tr>';
    _html += '</table>';
    _magic_tt(_html, null, _e, '500px', false);
}

function _magic_toolbar_version()
{   
    var _search = 'ImageShackToolbar/';
    var _pos = navigator.userAgent.indexOf(_search);
    if (_pos < 0)
    {
        _search = 'ImageShack Toolbar ';
        _pos = navigator.userAgent.indexOf(_search);
        if (_pos < 0)
                   return false;
    }
    var _s = navigator.userAgent.substring(_pos + _search.length);
    var _tokens = _s.substring(0, _pos).split('.');
    if (_tokens.length < 3)
        return false;
    var _maj = parseInt(_tokens[0], 10);
    var _min = parseInt(_tokens[1], 10);
    var _bld = parseInt(_tokens[2], 10);
    return [_maj || 0, _min || 0, _bld || 0];
}

function _magic_known_browser()
{
    if (navigator.platform.indexOf('Win') < 0 && navigator.platform.indexOf('MacIntel') < 0)
        return false;
    var _ver = 0;
    var _is_ff = navigator.userAgent.indexOf('Firefox/') > 0;
    if (_is_ff)
        _ver = parseFloat(navigator.userAgent.substring(navigator.userAgent.indexOf('Firefox/') + 8)); 
    var _is_ie = navigator.userAgent.indexOf('MSIE') > 0;
    if (_is_ie)
        _ver = parseFloat(navigator.userAgent.substring(navigator.userAgent.indexOf('MSIE') + 4)); 
    if (isNaN(_ver))
        _ver = 0;
    return _is_ie && _ver >= 6.0 || _is_ff && _ver >= 1.5 ? [_is_ie, _ver] : false;
}

function _magic_button_html(style, hOnclick)
{
    var _ret = '<input type="button"';
    if (style)
        _ret += ' style="' + style + '"';
    _ret += ' value="Browse.."';
    if (!hOnclick)
        _ret += ' class="ImageShack-Upload"';
    else
        _ret += ' onclick="' + hOnclick + '();return false"';
    _ret += '/>';
    return _ret;
}

function _magic_tt(html, o, e, width, noborder)
{
    mask();
    dark();
    tt(html, o, e, width);
}

function _magic_toolbar_ie()
{
    try
    {
        var uploader = new ActiveXObject("ImageShackToolbar.FileUploader");
        uploader.OpenUploadDialog();
    }
    catch(e)
    {
    }
}


function _magic_button(style)
{
    var _ver     = _magic_toolbar_version();
    var _browser = _magic_known_browser();
    if (!_ver)
    {
        if (_browser)
            document.write(_magic_button_html(style, '_magic_toolbar_required'));
        else
            document.write(_magic_button_html(style, '_magic_toolbar_wrong_browser'));
        return;
    }
    else
    {
        if ((_ver[0] == 4 && (_ver[1] > 0 || _ver[2] > 1)) || _ver[0] > 4)
        {
            document.write(_magic_button_html(style, _browser[0] ? '_magic_toolbar_ie' : null));
            return;
        }
        else
        {
            document.write(_magic_button_html(style, '_magic_toolbar_required'));
            return;
        }
    }
}
