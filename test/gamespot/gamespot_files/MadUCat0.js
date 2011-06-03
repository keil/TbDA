



function CbsiMadUCat()
{
    var self = this;

    var $cvsid = '$id: $';
    var $maducatrev = '2.0.1';

    var $skipBehavioral;
    var $cookieMaxLen = 2000;
    var $cookieDelimiter = '&';

    var exResults;


    function jsAppend(script_filename)
    {
        var html_doc = document.getElementsByTagName('head')[0];
        var js = document.createElement('script');
        js.setAttribute('language', 'javascript');
        js.setAttribute('type', 'text/javascript');
        js.setAttribute('src', script_filename);
        html_doc.appendChild(js);
        return js;
    }


    function imgAppend(image_filename) {
        var html_doc = document.getElementsByTagName('body')[0];
        var img = document.createElement('img');
        img.setAttribute('src', image_filename);
        img.setAttribute('width', "1" );
        img.setAttribute('height', "1" );
        img.setAttribute('border', "0" );

        if (window.addEventListener) {
            window.addEventListener("load",function(){html_doc.appendChild(img);},false);
        } else {
            window.attachEvent("onload",function(){html_doc.appendChild(img);});
        }
        return img;
    }


    function getDateString(date)
    {
        var month = (date.getMonth() + 1) + "";
        if (1 == month.length) {
            month = "0" + month;
        }
        var day = date.getDate() + "";
        if (1 == day.length) {
            day = "0" + day;
        }
        return month + day;
    }


    function setCookie(nm,vl,expDt) {
        var dm = document.domain.split('.');

        var ckAttr = new Array(nm+'='+vl,'path=/','domain=.'+dm[dm.length-2]+'.'+dm[dm.length-1]);
        if (expDt) ckAttr.push('expires='+expDt.toGMTString());
        document.cookie = ckAttr.join(";");
    }



    var getCookieValue = function(ckNm) {
        var value = null;
        var allCookies = document.cookie;
        var pos = allCookies.indexOf(ckNm);
        if (pos != -1) {
            var start = pos + ckNm.length + 1;
            var end = allCookies.indexOf(";",start);
            if (end==-1) end = allCookies.length;
            value = allCookies.substring(start,end);
        }
        return value ? value : null;
    }


    this.loadEX = function()
    {
        var todayDate = new Date();
        var today = getDateString(todayDate);





        var maducat_cookie = getCookieValue("MADUCAT");
        var maducat_cookievalues;
        if(null != maducat_cookie)
        {


            maducat_cookievalues = maducat_cookie.split($cookieDelimiter);
        }


        if(null == maducat_cookie || maducat_cookievalues[1] != today)
        {
            imgAppend("http://load.exelator.com/load/?p=200&g=491&j=0");
            exResults="EX";
            updateMADUCATCookie();
        }
    }



    function updateMADUCATCookie()
    {

        if(!$skipBehavioral)
        {
            $skipBehavioral=1;

            if (typeof exResults!="undefined" && exResults.length>0)
            {
                var version = 1;
                var delimiter = $cookieDelimiter;
                var expiration = 2592000000;
                var today = new Date();

                var cookie = version;
                cookie += delimiter;
                cookie += getDateString(today);



                cookie = cookie + $cookieDelimiter + "EX";

                setCookie('MADUCAT', cookie, new Date(today.getTime()+expiration));
            } else {

            }
        }
    }

}


if (window.cbsiMadUCat == undefined) {
    window.cbsiMadUCat = new CbsiMadUCat();

    var cbsiLoadEX = cbsiMadUCat.loadEX;
}

cbsiLoadEX();
