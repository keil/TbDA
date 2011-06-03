


/*! Copyright 2009,2010 the Rubicon Project.  All Rights Reserved.  No permission is granted to use, copy or extend this code */

oz_partner = "rubicon";


function RubiconInsight(){this.config={host:"http://tap.rubiconproject.com",statichost:"http://tap-cdn.rubiconproject.com"};this.default_context={oz_partner:"rubicon",oz_partner_user_id:null,oz_partner_channel:null,oz_partner_tracking:null,oz_api:"insight",oz_api_key:null,oz_view:null,oz_ad_server:null,oz_callback:null};
this.context=null;this.init=function(A){try{if(A){this.context=this.mergeProperties(A,this.default_context);}else{this.context=this.default_context;}if(this.context.oz_host){this.config.host=this.context.oz_host;}if(this.context.oz_statichost){this.config.statichost=this.context.oz_statichost;}}catch(B){}};
this.trim=function(A){return A.replace(/^\s+|\s+$/g,"");};this.mergeProperties=function(B,A){if(typeof (B)=="undefined"||!B){return A;}if(typeof (A)=="undefined"||!A){return new Object();}for(var C in A){if(typeof B[C]=="undefined"){B[C]=A[C];}}return B;};this.start=function(){var A;if(this.insightRetrieved){return ;
}this.insightRetrieved=true;try{A=this.config.host+"/partner/agent/"+this.context.oz_partner+"/"+this.context.oz_api+".js?";if(this.context.oz_api_key){A+="&ak="+this.context.oz_api_key;}if(this.context.oz_partner_user_id){A+="&afu="+this.context.oz_partner_user_id;}if(this.context.oz_partner_channel){A+="&pc="+this.context.oz_partner_channel;
}if(this.context.oz_partner_tracking){A+="&ptc="+this.context.oz_partner_tracking;}if(this.context.oz_view){A+="&uv="+this.context.oz_view;}if(this.context.oz_ad_server){A+="&as="+this.context.oz_ad_server;}A+="&cb=oz_onInsightLoaded";try{if(this.oz_source.referrer){host=this.oz_source.referrer.split("/")[2];
}if(host&&(host!=this.oz_source.location.host)){A+="&rd="+host;}}catch(B){}document.write("<scr"+"ipt type='text/javascript' src='"+A+"'></scr"+"ipt>");}catch(B){}};this.onPageLoad=function(){if(this.pageLoadHandled){return ;}this.pageLoadHandled=true;};this.getAsQueryTerms=function(A){var C="";if(typeof A!="undefined"&&A){for(var B in A){var D;
if(!A.hasOwnProperty(B)){continue;}D=new String(A[B]);D=D.replace(/\s/g,"+");D=D.replace(/&/g,"%26");C+="&"+B+"="+D;}}return C;};this.getAsDFPKeyValues=function(A){var D="";if(typeof A!="undefined"&&A){for(var B in A){var F;var E;if(!A.hasOwnProperty(B)){continue;}E=A[B];if(typeof E!="object"){E=new Array();
E[E.length]=A[B];}for(var C=0;C<E.length;C++){F=new String(E[C]);if(F.length>0){F=F.replace(/\s/g,"%20");F=F.replace(/&/g,"%26");D+=";"+B+"="+F;}}}}return D;};this.getAsAdTechKeyValues=function(A){var D="";if(typeof A!="undefined"&&A){for(var B in A){var F;var E;if(!A.hasOwnProperty(B)){continue;}E=A[B];
if(typeof E!="object"){E=new Array();E[E.length]=A[B];}D+=";kv"+B+"=";for(var C=0;C<E.length;C++){F=new String(E[C]);if(F.length>0){F=F.replace(/\s/g,"%20");F=F.replace(/&/g,"%26");D+=F;if(C<E.length-1){D+=":";}}}}}return D;};this.normalizeAttributeValue=function(A){return A;};this.normalize=function(A){var B;
for(B in A){if(!A.hasOwnProperty(B)){continue;}if(typeof A[B]=="string"){A[B]=this.normalizeAttributeValue(A[B]);}else{if(typeof A[B]=="object"){this.normalize(A[B]);}}}return A;};this.gamAdServerHook=function(B){var A=B.insight;try{for(var C in A){var F;var E;if(!A.hasOwnProperty(C)){continue;}E=A[C];
if(typeof E!="object"){E=new Array();E[E.length]=A[C];}for(var D=0;D<E.length;D++){F=new String(E[D]);if(F.length>0){F=F.replace(/\+/g,"plus");F=F.replace(/[^\w\d-_]+/g,"");F=F.substring(0,40);GA_googleAddAttr(C,F);}}}}catch(G){}};this.gam1AdServerHook=function(B){var A=B.insight;try{for(var C in A){var F;
var E;if(!A.hasOwnProperty(C)){continue;}E=A[C];if(typeof E!="object"){E=new Array();E[E.length]=A[C];}for(var D=0;D<E.length;D++){F=new String(E[D]);if(F.length>0){F=F.replace(/\+/g,"plus");F=F.replace(/[^\w\d-_]+/g,"");F=F.substring(0,10);GA_googleAddAttr(C,F);}}}}catch(G){}};this.onInsightLoaded=function(B){try{var A=B.insight;
var D;this.normalize(A);if(B.context.oz_ad_server&&typeof this[B.context.oz_ad_server.toLowerCase()+"AdServerHook"]=="function"){this[B.context.oz_ad_server.toLowerCase()+"AdServerHook"](B);}var F=new Array();for(D in A){if(!A.hasOwnProperty(D)){continue;}F[F.length]=D;}if(F.length>0){var C="";C+=this.config.host+"/stats/insight?";
C+="&p="+this.context.oz_partner;C+="&ak="+this.context.oz_api_key;if(B.context.oz_partner_channel){C+="&pc="+B.context.oz_partner_channel;}else{if(this.context.oz_partner_channel){C+="&pc="+this.context.oz_partner_channel;}}if(this.context.oz_partner_tracking){C+="&ptc="+this.context.oz_partner_tracking;
}if(this.context.oz_api){C+="&api="+this.context.oz_api;}if(this.context.oz_view){C+="&uv="+this.context.oz_view;}if(this.context.oz_ad_server){C+="&as="+this.context.oz_ad_server;}C+="&upn="+F.join(",");setTimeout(function(){new Image().src=C;},1000);}}catch(E){}try{var A=B.insight;var G=this.context.oz_callback;
if(G&&typeof G=="function"){G(A);}if(G&&typeof G=="string"&&window[G]&&typeof window[G]=="function"){window[G](A);}}catch(E){}};}function oz_insight(E){try{var D=new RubiconInsight();var C=new Object();var G=["oz_host","oz_statichost","oz_api","oz_api_key","oz_partner","oz_partner_user_id","oz_partner_channel","oz_partner_tracking","oz_view","oz_ad_server","oz_callback"];
var A;D.oz_source=document;for(var B=0;B<G.length;B++){A=G[B];if(window[A]){C[A]=window[A];}}D.init(C);oz_insight_partner_hook(D);window.oz_onInsightLoaded=function(H){D.onInsightLoaded(H);};D.start();if(E||D.autorun){D.onPageLoad();}}catch(F){}}function oz_insight_partner_hook(A){return A;}function oz_insight_adserver_hook(A){return A;
}

/*
	The requested resource (/oz/scripts/partners/rubicon/insight_hooks.js) is not available
*/


oz_insight();
