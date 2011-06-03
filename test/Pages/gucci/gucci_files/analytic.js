/* analytics.js
* v 0.1
*	
* Implentation of Coremetrics wrapper.
--------------------------------------------------------------------------------------- */

GUCCI.Analytics = {
    PRODUCT_SEGMENT: 'PRO',
    connection: false,
    production: false,
    region: '',
    country: '',
    language: '',
    promo: '',
    sendToCoremetrics: function() { return GUCCI.Analytics.connection && GUCCI.Analytics.region && GUCCI.Analytics.country && GUCCI.Analytics.language; },
    prefix: '',
    attributeString: '',
    formatValue: function(value) { return value.replace(/-/g,'').replace(/_/g,'').replace(/'/g,''); },
    initialize: function(region,country,language,production) 
    {
        if (typeof cmCreatePageviewTag != 'function' || region == null || country == null || language == null) return;
        GUCCI.Analytics.region = region;
        GUCCI.Analytics.country = country;
        GUCCI.Analytics.language = language;

        if (production != null)
            GUCCI.Analytics.production = production;
            
        if (document.location.href.indexOf('www.gucci.com') > -1)
            GUCCI.Analytics.production = true;
            
        if (GUCCI.Analytics.production)
            cmSetClientID("90253055",true,"data.coremetrics.com","gucci.com");
        else
            cmSetClientID("60253055",true,"testdata.coremetrics.com","gucci.com");
            
        GUCCI.Analytics.prefix = (region + ':' + country + ':' + language).toUpperCase();
                
        //TODO: Add reference to pull final defined promotional cookie as available.
        GUCCI.Analytics.attributeString = GUCCI.Analytics.promo+"-_-"+GUCCI.viewport.width()+"x"+GUCCI.viewport.height();
    },
    pageView: function(name,segment)
    {
        if (!GUCCI.Analytics.sendToCoremetrics || name == null) return;
        name = GUCCI.Analytics.formatValue(name);
		if (typeof segment == 'string' && segment.length > 0) 
		{
		    segment = GUCCI.Analytics.formatValue(segment);
			cmCreatePageviewTag(GUCCI.Analytics.prefix + ':' + segment.toUpperCase() + ':' + name.toUpperCase(),null,null,null,GUCCI.Analytics.attributeString);
             // console.log("create pageview tag: " + GUCCI.Analytics.prefix + ':' + segment.toUpperCase() + ':' + name.toUpperCase() + " attr:" + GUCCI.Analytics.attributeString);
		}
		else 
		{
			cmCreatePageviewTag(GUCCI.Analytics.prefix + ':' + name.toUpperCase(),null,null,null,GUCCI.Analytics.attributeString);
            // console.log("create pageview tag: " + GUCCI.Analytics.prefix + ':' + name.toUpperCase() + " attr:" + GUCCI.Analytics.attributeString);
		}
    },
    
    rtwView: function(look)
   	{
   		if (!GUCCI.Analytics.sendToCoremetrics|| look == null) return;
   		    cmCreatePageviewTag(GUCCI.Analytics.prefix + ':' + GUCCI.Analytics.PRODUCT_SEGMENT + ":RTW LOOK" + look,null,null,null,GUCCI.Analytics.attributeString);
            //console.log("create rtwview tag: " + GUCCI.Analytics.prefix + ':' + GUCCI.Analytics.PRODUCT_SEGMENT + ":RTW LOOK" + look,null,null,null,GUCCI.Analytics.attributeString);
   	},
    
    productView: function(style)
	{
		if (!GUCCI.Analytics.sendToCoremetrics|| style == null) return;
		if (style.indexOf(' ') == -1 && style.length == 15)
			style = style.substring(0,6) + ' ' + style.substring(6,11) + ' ' + style.substring(11,15);
		cmCreatePageviewTag(GUCCI.Analytics.prefix + ':' + GUCCI.Analytics.PRODUCT_SEGMENT + ":" + style,null,null,null,GUCCI.Analytics.attributeString);
         //console.log("create productview tag: " + GUCCI.Analytics.prefix + ':' + GUCCI.Analytics.PRODUCT_SEGMENT + ':' + style + " attr:" + GUCCI.Analytics.attributeString);
	},
	
	order: function(orderid,ordertotal,ordershipping,customerid,customercity,customerstate,customerpostalcode,ccnumber,currency) 
	{

		if (typeof orderid != 'string' || typeof ordertotal != 'string' || typeof ordershipping != 'string' || typeof customerid != 'string') return;
         // if (typeof ccnumber == 'string' && ccnumber == '5262444444444444') return;
		
		if (!customercity)				
			cmCreateOrderTag(orderid,ordertotal,ordershipping,customerid,null,null,null,currency+"-_-"+GUCCI.Analytics.country);
		else		
			cmCreateOrderTag(orderid,ordertotal,ordershipping,customerid,customercity,customerstate,customerpostalcode,currency+"-_-"+GUCCI.Analytics.country);
            // console.log("create thank you tag: " + orderid,ordertotal,ordershipping,customerid,customercity,customerstate,customerpostalcode,currency+"-_-"+GUCCI.Analytics.country);
	},

	searchResult: function(progression, criteria, resultnum)
	{
		// user has to set the progression, terms in above method first at least
		if (progression.length > 0 && criteria.length > 0) 
		{
			progression = GUCCI.Analytics.formatValue(progression);
			criteria = GUCCI.Analytics.formatValue(criteria);
			resultnum = GUCCI.Analytics.formatValue(resultnum);
		    // if (resultnum == 0)
		  //          progression += ' UNSUCC';
			cmCreatePageviewTag(GUCCI.Analytics.prefix +  ":SEA:" + progression,null,criteria,resultnum,null,null,null,GUCCI.Analytics.attributeString);
            // console.log("create search tag:" + GUCCI.Analytics.prefix +  ":SEA:" + progression,null,criteria,resultnum,null,null,null,GUCCI.Analytics.attributeString)
		}
	}
};

$(document).ready(function()
{
  
});