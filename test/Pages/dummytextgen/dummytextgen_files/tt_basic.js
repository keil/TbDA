
// get left of s until first occurence of c
function tt_strUntil(s, c)
{
    if ( (pos = s.lastIndexOf(c)) > -1)
        return s.substr(pos+1);
    return s;
}


jQuery.fn.ttGetDataId = function(expr)
{
    var ancestor = $(this).parents(expr).get(0);
    return ancestor ? tt_strUntil(ancestor.id, '-') : '';
};


// -----------------------------------------------------------------------------
// cookie helpers
//
// Note: these parameters can be omitted: [expires], [path], [domain], [secure]
// -----------------------------------------------------------------------------

function tt_setJSONCookie(name, jsObject, expires, path, domain, secure)
{
    tt_setCookie(name, encodeURIComponent(toJSON(jsObject)), expires, path, domain, secure);
}


function tt_getJSONCookie(name)
{
    var json = decodeURIComponent(tt_getCookie(name));
    return eval( '('+json+')' );
}


function tt_setSessionCookie(name, value, path, domain, secure)   { return tt_setCookie(name, value, null, path, domain, secure); }
function tt_setOneMonthCookie(name, value, path, domain, secure)  { return tt_setCookie(name, value, tt_futureDate(30,0,0), path, domain, secure); }
function tt_setTenYearCookie(name, value, path, domain, secure)   { return tt_setCookie(name, value, tt_futureDate(3650,0,0), path, domain, secure); }

function tt_setCookie(name, value, expires, path, domain, secure)
{
//    console.info('tt_setCookie: ' + name + '=' + value);
    if (!domain)
    {
        var matches = window.location.hostname.match(/([^.]+[.][^.]+)$/);
        if (matches) {
            domain = matches[1] + ':';
            if ( (usesPort = domain.indexOf(':')) )
                domain = domain.substr(0, usesPort);
            domain = '.' + domain;
        }
    }

    var s = name + "=" + escape(value) +
        ((expires) ? "; expires=" + expires.toGMTString() : "") +
        ((path)    ? "; path=" + path : "") +
        ((domain)  ? "; domain=" + domain : "") +
        ((secure)  ? "; secure" : "");
    //alert('cookie: ' + name + ' length=' + s.length);
    document.cookie = s;
}


//
// Returns a string containing value of specified cookie, or null if cookie does not exist.
//
function tt_getCookie(name, defaultValue)
{
    var dc = document.cookie;
    var prefix = name + "=";
    var begin = dc.indexOf("; " + prefix);
    if (begin == -1)
    {
        begin = dc.indexOf(prefix);
        if (begin != 0) return (defaultValue != undefined ? defaultValue : null);
    }
    else
    {
        begin += 2;
    }
    var end = document.cookie.indexOf(";", begin);
    if (end == -1)
    {
        end = dc.length;
    }
    var value = unescape(dc.substring(begin + prefix.length, end));
//    console.info('tt_getCookie: ' + name + '=' + value);
    return value;
}


//
// Deletes the specified cookie.
// Parameters: name, [path], [domain]
//
function tt_deleteCookie(name, path, domain)
{
    if (tt_getCookie(name))
    {
        document.cookie = name + "=" +
            ((path) ? "; path=" + path : "") +
            ((domain) ? "; domain=" + domain : "") +
            "; expires=Thu, 01-Jan-70 00:00:01 GMT";
    }
}

//
// Returns a future Date object of days + hours + minutes from now
//
function tt_futureDate(days, hours, minutes)
{
    var expDate = new Date();
    expDate.setDate(expDate.getDate() + days);
    expDate.setHours(expDate.getHours() + hours);
    expDate.setMinutes(expDate.getMinutes() + minutes);
    return expDate;
}


// -----------------------------------------------------------------------------
// JSON util (by Tino Zijdel)
// -----------------------------------------------------------------------------

function toJSON(obj)
{
    if (typeof obj == 'undefined')
        return 'undefined';
    else if (obj === null)
        return 'null';

    var stringescape = {
        '\b': '\\b',
        '\t': '\\t',
        '\n': '\\n',
        '\f': '\\f',
        '\r': '\\r',
        '"' : '\\"',
        '\\': '\\\\'
        }

    var json = null, i, l, v, a = [];
    switch (obj.constructor)
    {
        case Array:
            l = obj.length;
            for (i = 0; i < l; i++)
            {
                if ((v = toJSON(obj[i])) !== null)
                    a.push(v);
            }
            json = '[' + a.join(',') + ']';
            break;
        case Object:
            for (i in obj)
            {
                if (obj.hasOwnProperty(i) && (v = toJSON(obj[i])) !== null)
                    a.push(toJSON(String(i)) + ':' + v);
            }
            json = '{' + a.join(',') + '}';
            break;
        case String:
            json = '"' + obj.replace(/[\x00-\x1f\\"]/g, function($0)
            {
                var c;
                if ((c = stringescape[$0]))
                    return c;
                c = $0.charCodeAt();
                return '\\u00' + Math.floor(c / 16).toString(16) + (c % 16).toString(16);
            }) + '"';
            break;
        case Number:
            json = isFinite(obj) ? String(obj) : 'null';
            break;
        case Boolean:
            json = String(obj);
            break;
    }

    return json;
}