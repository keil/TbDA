var GUCCI={animation:{megamenu_delay:200,megamenu_show:300,megamenu_hide:300,minibag_delay:200,minibag_show:300,minibag_hide:300,minibag_highlight:750},back:-1,noLoveBack:true,registration:{offsite:false,initSignup:function()
{$('#email_signup').click(function()
{var regdate=new Date();regdate.setDate(regdate.getDate()+1);document.cookie="clickedFromGUCCI="+escape('true')+((1==null)?"":";expires="+regdate.toUTCString());});},deleteSignupCookie:function()
{document.cookie="clickedFromGUCCI="+escape('true')+';expires=Thu, 01-Jan-1970 00:00:01 GMT';}},megapromo:{width:130},getCookie:function(cookiename)
{if(document.cookie.length>0)
{var cookiestart=document.cookie.indexOf(cookiename+"=");if(cookiestart!=-1)
{cookiestart=cookiestart+cookiename.length+1;var cookieend=document.cookie.indexOf(";",cookiestart);if(cookieend==-1)cookieend=document.cookie.length;return unescape(document.cookie.substring(cookiestart,cookieend));}}
return"";},activeOverlayApi:null,canresize:true,resizeevent:null,lastViewWidth:0,lastViewHeight:0,isIE6:($.browser.msie&&$.browser.version=="6.0")?true:false,isIE:($.browser.msie)?true:false,IEversion:($.browser.msie)?$.browser.version:-1,isIPad:navigator.platform=="iPad",isMobile:(/Android|iPhone|iPad/i).test(navigator.userAgent),viewport:{height:function(){return $(window).height();},width:function(){return $(window).width();},scrollTop:function(){return $(window).scrollTop();},scrollLeft:function(){return $(window).scrollLeft();}},makeUrl:function(path,params){var newurl=GUCCI.siteABBR();if(typeof params=='string'){path=path+"?"+params;}else if(params){path=path+"?"+$.param(params,true);}
var path_match=location.href.split('/');if(path_match.length>2)
newurl=path_match[0]+"//"+path_match[2]+"/"+newurl+path;else
newurl=window.location.protocol+"//"+window.location.hostname+"/"+newurl+path;return newurl;},parseParams:function(url){var qs=url.split("?")[1];if(qs){qs=qs.split("#")[0];var parts=qs.split("&");var params={};var len=parts.length;for(var i=0;i<len;i++){var part=parts[i].split("=");var key=unescape(part[0].replace(/\+/g," "));var val=unescape(part[1].replace(/\+/g," "));if(key.indexOf("[]")==-1){params[key]=val;}else if(params[key]){params[key].push(val);}else{params[key]=[val];}}
return params;}
return null;},resetRecentlyViewedLocation:function(speed){var rv=$("#recently_viewed");if(rv.is(":visible")){if(speed){setTimeout(GUCCI.resetRecentlyViewedLocation,speed);return;}
var pos=$("a.recently_viewed_link").offset();rv.css({top:pos.top-(rv.height()+8),left:pos.left-10});}},resizeShell:function(speed){if(!this.canresize||GUCCI.isIPad)return;if(GUCCI.isIE6){var viewwidth=this.viewport.width();var viewheight=this.viewport.height();if(Math.abs(viewheight-this.lastViewHeight)<10&&Math.abs(viewwidth-this.lastViewWidth)<10)return;this.lastViewHeight=viewheight;this.lastViewWidth=viewwidth;if(viewheight>680)
{$('#bg_surfacefooter,#bg_surfacefooter .center').css({height:'2000px'});$('html').css({'overflow-y':'hidden'});}
else
{var newfooterheight=viewheight-580;if(newfooterheight>136)
$('#bg_surfacefooter,#bg_surfacefooter .center').css({height:newfooterheight+'px'});else
$('#bg_surfacefooter,#bg_surfacefooter .center').css({height:'136px'});$('html').css({'overflow-y':'auto'});}
viewwidth=this.viewport.width();viewheight=this.viewport.height();if(viewwidth>960){$('#bg_full').css({width:viewwidth});$('#bg_header,#bg_full div.bg_headertop,#bg_headercontent,#bg_content,#bg_surfacefooter').css({width:viewwidth});$('html').css({'overflow-x':'hidden'});}
else{$('#bg_full').css({width:'960px'});$('#bg_header,#bg_full div.bg_headertop,#bg_headercontent,#bg_content,#bg_surfacefooter').css({width:'960px'});$('html').css({'overflow-x':'auto'});}}
if(GUCCI.Category){GUCCI.Category.resizeScrollbar();GUCCI.Category.resizeZoom();}
if(GUCCI.Product){GUCCI.Product.resizeZoom();}
GUCCI.resetRecentlyViewedLocation(speed);},initPixelTruncations:function(){if(typeof $.fn.pixeltruncate=='function')
{var samplefonts=jQuery('<div/>',{id:'samplefonts'});$('#bg_content').append(samplefonts);var path_match=location.pathname.split('/');var site_abbr=path_match[1];var asianset=site_abbr=="jp";var megamenus=$('#header_main li.mega_menu').not('.worldofg');var headerstuffwidth=145;var addpromolinkswidth=152;for(var i=0;i<megamenus.length;i++)
{var currmegamenu=megamenus.eq(i);var megaitems=currmegamenu.find('.col1 a,.col2 a');var longest=megaitems.pixeltruncate({pixellimit:headerstuffwidth,checkcontainer:samplefonts,truncfullwords:false});var linkwidthincrease=longest-105;var megatotal=currmegamenu.children('.mega_col');var megatotalwidth=parseInt(megatotal.css('width'),10);if(linkwidthincrease>0)
{var megacol1=currmegamenu.find('.col1');var megacol2=currmegamenu.find('.col2');var megacolcurrwidth=parseInt(megacol1.css('width'),10);var megalinkwidth=parseInt(megaitems.css('width'),10);megacol1.css('width',((megacolcurrwidth+linkwidthincrease)+'px'));megacol2.css('width',((megacolcurrwidth+linkwidthincrease)+'px'));megatotal.css('width',(megatotalwidth+(linkwidthincrease*2))+'px');megaitems.css('width',(megalinkwidth+linkwidthincrease)+'px');}
var addlinkitems=currmegamenu.find('.col3 .mega_promo a.addlink');if(addlinkitems.length>1)
longest=addlinkitems.pixeltruncate({pixellimit:addpromolinkswidth,checkcontainer:samplefonts,truncfullwords:true,asiancharset:asianset});else
longest=addlinkitems.pixeltruncate({pixellimit:addpromolinkswidth,checkcontainer:samplefonts,numlines:2,asiancharset:asianset});linkwidthincrease=longest-132;megatotalwidth=parseInt(megatotal.css('width'),10);if(linkwidthincrease>0)
{var megacol3=currmegamenu.find('.col3');var imgtitle=currmegamenu.find('.col3 span');var megapromo=currmegamenu.find('.mega_promo');var megacol3currwidth=parseInt(megacol3.css('width'),10);var imgtitlecurrleft=20;var megapromocurrwidth=parseInt(megapromo.css('width'),10);var addlinkwidth=parseInt(addlinkitems.css('width'),10);megacol3.css('width',((megacol3currwidth+linkwidthincrease)+'px'));imgtitle.css('left',((imgtitlecurrleft+Math.floor(linkwidthincrease/2))+'px'));megapromo.css('width',((megapromocurrwidth+linkwidthincrease)+'px'));megatotal.css('width',(megatotalwidth+linkwidthincrease)+'px');addlinkitems.css('width',(addlinkwidth+linkwidthincrease)+'px');}}
if(typeof Cufon=='function'&&!GUCCI.isMobile)
{Cufon.replace(megamenus.find('.col1 a,.col2 a'));Cufon.replace(megamenus.find('.col3 .mega_promo a.addlink'));}
var worldofgheaders=$('#header_main li.worldofg div.mega_col ul.col1 a span.header');var worldofgheaderswidth=160;worldofgheaders.pixeltruncate({pixellimit:worldofgheaderswidth,checkcontainer:samplefonts,truncfullwords:false,asiancharset:asianset});if(typeof Cufon=='function'&&!GUCCI.isMobile)
Cufon.replace(worldofgheaders);var megaimgdesc=$('#header_main li .col3 .mega_promo span');var megaimgdescwidth=120;if(GUCCI.IEversion!=8)
megaimgdesc.pixeltruncate({pixellimit:megaimgdescwidth,checkcontainer:samplefonts,truncfullwords:false,asiancharset:asianset});if(typeof Cufon=='function'&&!GUCCI.isMobile)
Cufon.replace(megaimgdesc);var pageclass=$('body').attr('class');if(pageclass.indexOf('styles')>-1)
{var breadcrumb=$('#breadcrumb p');var breadcrumbwidth=800;breadcrumb.pixeltruncate({pixellimit:breadcrumbwidth,checkcontainer:samplefonts,truncfullwords:true,asiancharset:asianset});}
if(pageclass.indexOf('carts')>-1){var summarytitles=$('#order_lines li h6 a');if(summarytitles.length>0){var linecontainerwidth=$('#order_lines li').width();var linelinkwidth=$('#order_lines li a.img_link').width();var availwidth=linecontainerwidth-linelinkwidth-10;summarytitles.pixeltruncate({pixellimit:availwidth,numlines:2,checkcontainer:samplefonts,asiancharset:asianset});}
if($('#main_content .container_orderdetails').length>0){var orderdetails=$('#main_content .container_orderdetails p cite');var orderdetailscontainerwidth=$('#main_content .container_orderdetails p').width()-5;orderdetails.pixeltruncate({pixellimit:orderdetailscontainerwidth,checkcontainer:samplefonts,asiancharset:asianset});}
var itemtitles=$('#main_content .items h6');if(itemtitles.length>0){var itemtitleslink=$('#main_content .items h6 a');var itemtitlescontainerwidth=itemtitles.width();var itemtitlesfont="Verdana,sans-serif";var itemtitlessize="11px";itemtitleslink.pixeltruncate({pixellimit:itemtitlescontainerwidth,checkcontainer:samplefonts,asiancharset:asianset,fontfamily:itemtitlesfont,fontsize:itemtitlessize});}
var labels=$('#bill_to_information label, body.info #ship_to_information label');if(labels.length>0){labels.pixeltruncate({pixellimit:150,checkcontainer:samplefonts,tstring:'...*',asiancharset:asianset});}
var shippinglistings=$('#delivery .current_address');if(shippinglistings.length>0){var listings=$('#delivery .current_address cite');var listingswidth=shippinglistings.width()-10;listings.pixeltruncate({pixellimit:listingswidth,checkcontainer:samplefonts,asiancharset:asianset});}}
samplefonts.remove();}},resizeBinding:function(){$(window).bind('resize',function(){clearTimeout(GUCCI.resizeevent);GUCCI.resizeevent=setTimeout(function(){GUCCI.resizeShell();},500);});},showNav:function(){$(this).addClass("is_active").children('div.mega_col').fadeIn(GUCCI.animation.megamenu_show,function(){var featuretext=$('header nav #header_main div.mega_col ul.col3 strong');featuretext.css('white-space','nowrap');if(featuretext.width()>GUCCI.megapromo.width){var span=$('header nav #header_main div.mega_col ul.col3 span');span.css({height:'44px',top:'105px'});}
featuretext.css('white-space','normal');});},hideNav:function(){$(this).removeClass("is_active").children('div.mega_col').fadeOut(GUCCI.animation.megamenu_hide);},showBag:function(){$('#header_userbuyflow li.minibag_container').addClass("is_active").children('#minibag').fadeIn(GUCCI.animation.minibag_show);},hideBag:function(){$('#header_userbuyflow li.minibag_container').removeClass("is_active").children('#minibag').fadeOut(GUCCI.animation.minibag_hide,function(){var firstitem=$(this).find('li.item:first');firstitem.removeClass('item_highlight');firstitem.children('.highlight').css({display:'none',visibility:'hidden'});});},minibagInitialize:function(highlightlatestadd){var minibag=$('#minibag');var minibag_items=$("#minibag .items");if(typeof Cufon=='function')
Cufon.replace(minibag.find('a.send_btn,a.inactive_send_btn'));if(minibag_items.length>0){minibag.css({display:'block',visibility:'hidden'});if(typeof $.fn.pixelinitalphabet=='function'){var samplefonts=jQuery('<div/>',{id:'samplefonts'});$('#bg_content').append(samplefonts);var minibagtitles=$('#minibag h6 a');var linecontainerwidth=$('#minibag li.item').width();var linelinkwidth=$('#minibag li.item a.img_link').width();var availwidth=linecontainerwidth-linelinkwidth-5;if(!availwidth||availwidth<90)availwidth=145;if($.browser.msie&&$.browser.version.substr(0,1)<7)
availwidth=145;minibagtitles.pixeltruncate({pixellimit:availwidth,checkcontainer:samplefonts,numlines:2});var minibagbulletsdouble=$('#minibag .bullets .desc.double');var minibagbulletssingle=$('#minibag .bullets .desc.single');var minibagbulletswidth=availwidth-45;if($.browser.msie&&$.browser.version.substr(0,1)<7)
minibagbulletswidth=100;minibagbulletsdouble.pixeltruncate({pixellimit:minibagbulletswidth,checkcontainer:samplefonts,numlines:2});minibagbulletssingle.pixeltruncate({pixellimit:minibagbulletswidth,checkcontainer:samplefonts});samplefonts.remove();var removelinks=$('#minibag a.remove');removelinks.one('click',function(){$(this).parent().submit();});}
if(!minibag.hasClass('reduced'))
minibag_items.jScrollPane({scrollbarWidth:7});minibag.css({display:'none',visibility:'visible'});if(highlightlatestadd){var minibagcontainer=$('#header_userbuyflow li.minibag_container');var minibaglatestitem=$('#minibag li.item:first');minibaglatestitem.addClass('item_highlight');minibagcontainer.doTimeout('minibagaddshow',GUCCI.animation.minibag_delay,function(){GUCCI.showBag();minibaglatestitem.doTimeout('minibaghighlight',GUCCI.animation.minibag_show,function(){if($.browser.msie&&$.browser.version.substr(0,1)<8)
$(this).children('.highlight').css('display','block');else
$(this).children('.highlight').fadeIn(GUCCI.animation.minibag_highlight);});});minibagcontainer.doTimeout('minibagshow',5000,GUCCI.hideBag);}}
$('#header_userbuyflow li.minibag_container').hover(function(){$(this).doTimeout('minibagshow',GUCCI.animation.minibag_delay,GUCCI.showBag);$(this).addClass("is_active");},function(){$(this).doTimeout('minibagshow',0,GUCCI.hideBag);$(this).removeClass("is_active");});$('#minibag .container_img').hover(function(){$(this).find('a.remove').stop(true,true).fadeIn(300);},function(){$(this).find('a.remove').stop(true,true).fadeOut(300);});if(GUCCI.isMobile)
{var mbaglink=$('#minibag_link');if(mbaglink.length>0)
mbaglink.attr('href','#');}},cufonInitialize:function(){if(typeof Cufon=='function')
Cufon.replace('#header_main li.mega_menu[class!="mega_menu worldofg"] a,#header_main li .mega_link,#header_main li.mega_menu.worldofg ul.col1 li a span.header,#header_main li.mega_menu.worldofg ul.col3 li a,.quick_view em.price,.home #sidebar li span,.home #mainfeature a,a.send_btn, #header_main div.mega_col ul.col3 strong, div#countries h5, div#countries li.cufon, div#assistance_card h3,div#unsubscribe_card h3, #size-guide h3, div.page_error_card  h1, .email_friend_overlay h3,.email_friend_overlay h4');},emailFriend:function(linktarget,targetOverlay,targetId,pageType){var currlink=$(linktarget);var editoverlay=targetOverlay.find('div.email_friend_overlay');var uniqueId=targetId;currlink.attr('rel',currlink.attr('href'));currlink.attr('href','#');currlink.goldoverlay({target:editoverlay,onBeforeLoad:function(){GUCCI.activeOverlayApi=currlink.data('overlay');var overlay=GUCCI.activeOverlayApi.getOverlay();$('#overlay_screen').css('visibility','hidden');overlay.css('visibility','hidden');},onLoad:function(){$.get(currlink.attr('rel'),function(data){var overlay=GUCCI.activeOverlayApi.getOverlay();$('#overlay_screen').hide().css('visibility','visible');overlay.hide().css('visibility','visible');$('#overlay_screen').fadeIn();overlay.fadeIn();editoverlay.html(data);GUCCI.cufonInitialize();overlay.find('.close, .cancel_overlay').click(function(){GUCCI.activeOverlayApi.close();return false;});overlay.find(".error, .thankyou ").hide();$('div.add_another a').toggle(function(){$(".email_friend_overlay").css("height","510px");$("div.email_friend_overlay div.contact_form").css("height","400px");$("div.add_more").fadeIn(250);},function(){$(".email_friend_overlay").css("height","400px");$("div.email_friend_overlay div.contact_form").css("height","285px");$("div.add_more").fadeOut(250);});$("#email_friend_message").click(function(){$(this).empty();});$("#email_friend_submit_button").click(function(){$(".email_friend_overlay input").removeClass("field_with_errors");$('.email_friend_overlay .friend_info_fields input, .email_friend_overlay .your_info_fields input').each(function(i)
{var inputTxt=$(this).val();if(!IsValidEmail(inputTxt)&&$(this).parent().attr("class")=="field_wrap email"||inputTxt=='')
{$(this).addClass("field_with_errors");$(".email_friend_form span.error").show();}});if($("div.add_more").length!=0)
{var anotherFriendField1=$("#email_friend_web_user_friend_email_1").val();var anotherFriendField2=$("#email_friend_web_user_friend_email_2").val();if($("#email_friend_web_user_friend_name_1").val()!=""){if(!IsValidEmail(anotherFriendField1))
{$("#email_friend_web_user_friend_email_1").addClass("field_with_errors");if($(".email_friend_form span.error").length==0)
{$(".email_friend_form span.error").show();}}}
if($("#email_friend_web_user_friend_name_2").val()!=""){if(!IsValidEmail(anotherFriendField2))
{$("#email_friend_web_user_friend_email_2").addClass("field_with_errors");if($(".email_friend_form span.error").length==0)
{$(".email_friend_form span.error").show();}}}}
var errorList=$('.email_friend_form .field_with_errors').each(function(){return;});if(errorList.length==0){var url=GUCCI.makeUrl("/emailfriends/send_email");var formData={"web_user[given_name]":$("#email_friend_web_user_your_name").val(),"web_user[email]":$("#email_friend_web_user_your_email").val(),"web_user[friends][0][friend_name]":$("#email_friend_web_user_friend_name").val(),"web_user[friends][0][friend_email]":$("#email_friend_web_user_friend_email").val(),"web_user[friends][1][friend_name]":$("#email_friend_web_user_friend_name_1").val(),"web_user[friends][1][friend_email]":$("#email_friend_web_user_friend_email_1").val(),"web_user[friends][2][friend_name]":$("#email_friend_web_user_friend_name_2").val(),"web_user[friends][2][friend_email]":$("#email_friend_web_user_friend_email_2").val(),"send_copy":$('#send_copy').is(':checked'),"message":$("#email_friend_message").val(),"page_type":pageType,"unique_id":uniqueId,"style_loc":window.location.href};$.ajax({type:"POST",url:url,data:formData,success:function(data)
{overlay.find('.friend_info_fields input').each(function(){$(this).val("");});$(".email_friend_overlay").find("form").fadeOut("slow");$(".email_friend_overlay").find(".thankyou").fadeIn();$(".send_another").click(function(){$("#email_friend_message").val("");$(".email_friend_overlay").css("height","400px");$("div.email_friend_overlay div.contact_form").css("height","285px");$("div.add_more, span.error").hide();$(".email_friend_overlay").find("form").fadeIn("fast");});}});}
function IsValidEmail(email)
{var filter=/^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;return filter.test(email);}});});},onClose:function(){var overlay=GUCCI.activeOverlayApi.getOverlay();var errorbox=overlay.find('.error');if(errorbox.length>0)
errorbox.hide();var form=overlay.find('form');GUCCI.clearForm(form);$("#overlay_screen").removeClass("overlayScreenFix");currlink.attr('href',currlink.attr('rel'));currlink.attr('rel','#');$(".email_friend_overlay").css("height","400px");$("div.email_friend_overlay div.contact_form").css("height","285px");$("#email_friend_message").val("");overlay.remove();}});},clearForm:function(form){form.find(':input').each(function(){var $this=$(this);switch(this.type){case'select-one':$this.val(0);break;case'text':$this.val('');break;case'textarea':$this.val('');break;case'checkbox':case'radio':this.checked=false;}
$this.removeClass('field_with_errors');var parent=$this.parent('.field_with_errors');if(parent.length>0){if(parent.hasClass('title_fields'))
parent.removeClass('field_with_errors');else
$this.unwrap();}});},assistanceInitialize:function(){if($("#assistance_card .assistance_content").length>0)
$("#assistance_card .assistance_content").jScrollPane({scrollbarWidth:9,scrollbarMargin:10,animateTo:true});var myColumn;var section;var sectionLink;var subLink=$("#assistance_card").attr("class");$("#sub_nav ul li").each(function(){$(this).removeClass("on");if($(this).attr("class")==subLink)
{$(this).addClass("on");}});var path_match=location.pathname.split('/');var site_abbr=path_match[1];if(site_abbr=="int"||site_abbr=="cn"||site_abbr=="cn-en"||site_abbr=="jp"||site_abbr=="kr"){$("body.assistance li.assistance").css("display","none");}
$('#assistance_card div.nav_col > ul').each(function(myColumn){if(myColumn>0){$('.nav_col').css('margin','auto');}});var $scrolling=$('.assistance_content');var scrollingTop=parseInt($scrolling.offset().top,10);$('.assistance_content h1').each(function(index){$('#assistance_card  a.go_top').click(function(){$scrolling[0].scrollTo(parseInt(-(scrollingTop),10));$('#breadcrumb h6').html(" ");});var h1Text=$.trim($(this).text());var h1Pos=$(this).offset().top;$('#assistance_card div.nav_col a ').each(function(){if($(this).text()==h1Text){$(this).attr({'href':'javascript:;','rel':h1Pos}).bind('click',function()
{$scrolling[0].scrollTo(parseInt($(this).attr('rel'),10)-scrollingTop);$('#breadcrumb h6').html(" &gt; "+h1Text);});}});$('#assistance_card  .language_content ').each(function(i){var languageDisplay=$(this).attr("id");var langPos=$(this).offset().top;$('#assistance_card .title a').each(function(){if($(this).attr("class")==languageDisplay){$(this).attr({'href':'javascript:;','rel':langPos}).bind('click',function()
{$scrolling[0].scrollTo(parseInt($(this).attr('rel'),10)-scrollingTop);});}});});});},siteABBR:function()
{var path_match=location.pathname.split('/');return path_match[1];}};$(document).ready(function(){if(typeof $.Broadcaster=='object')
$.Broadcaster.namespace="com.gucci";GUCCI.resizeBinding();GUCCI.resizeShell();GUCCI.initPixelTruncations();GUCCI.registration.initSignup();if($.browser.msie&&$.browser.version.substr(0,1)<7){$("#overlay_screen").css("height",GUCCI.viewport.height());}
$('#countries_link').goldoverlay();$('#freeshipping_link').goldoverlay();$('#header_main li.mega_menu').hover(function(){$(this).doTimeout('megashow',GUCCI.animation.megamenu_delay,GUCCI.showNav);$(this).addClass("is_active");},function(){$(this).doTimeout('megashow',0,GUCCI.hideNav);$(this).removeClass("is_active");});$('#header_main div.mega_col ul.col3 span').click(function()
{var $this=$(this);var clink=$this.siblings('a:first');if(clink.length>0)
document.location=clink.attr('href');});if(GUCCI.isMobile){$('#content').bind('touchstart',function(){var megamenus=$('#header_main li.mega_menu.is_active');megamenus.children('div.mega_col').fadeOut(GUCCI.animation.megamenu_hide);megamenus.removeClass('is_active');megamenus.one('touchstart',function(){$(this).trigger('mouseover');});GUCCI.hideBag();$('#header_userbuyflow li.minibag_container').one('touchstart',function(){$(this).trigger('mouseover');});});}
$(document).register('header.buyflow.loaded',function(){GUCCI.minibagInitialize(false);});$(document).register('header.buyflow.addtobag',function(){GUCCI.minibagInitialize(true);});var megas=$('#header_main > li');var containerwidth=$('#header_main').width();var megawidth=0;for(var i=0;i<megas.length;i++){var curr=$(megas[i]);megawidth+=curr.width();}
if(megawidth>containerwidth){for(var i=megas.length-1;i>=0;i--){var curr=$(megas[i]);if(curr.hasClass('quick_link')){megawidth-=curr.width();curr.css('display','none');}
if(megawidth<=containerwidth){if(i>0&&$(megas[i-1]).hasClass('quick_link'))
$(megas[i-1]).addClass('last_quick_link');break;}}}
$('#header_megas').css('overflow','visible');var site_abbr=GUCCI.siteABBR();if(site_abbr==""||site_abbr==undefined){site_abbr="us";}
headeruser=$("#header_user");if(headeruser.length>0){headeruser.load(GUCCI.makeUrl("/home/get_header"),function(){$.broadcast("header.user.loaded");});};buyflow=$('#header_buyflow');if(buyflow.length>0&&!buyflow.hasClass('nonselling')){buyflow.load(GUCCI.makeUrl("/cart/get_minibag_header"),function(){$.broadcast("header.buyflow.loaded");});};if($("#assistance_card").length>0){GUCCI.assistanceInitialize();}
var initRecentlyViewed=function(){if($("#recently_viewed").length==0){return;}
var updateNowViewing=function(){$("#recently_viewed span.current-start").html(minNowViewing);$("#recently_viewed span.current-end").html(maxNowViewing);if(maxNowViewing==upperBound){$next.hide();}else{$next.show();}
if(minNowViewing==lowerBound){$prev.hide();}else{$prev.show();}};var $prev=$("#recently_viewed a.prev");var $next=$("#recently_viewed a.next");var upperBound=$("#recently_viewed li").length;var lowerBound=1;var minNowViewing=1;var maxNowViewing=upperBound>3?3:upperBound;var li_width=$("ul.recently_viewed img").outerWidth({margin:true});var width=upperBound*(li_width+10);$("ul.recently_viewed").css("width",width);updateNowViewing();if(typeof $.fn.tipTip=='function')
{$("ul.recently_viewed a").tipTip({defaultPosition:'top'});if(GUCCI.isIE6)
{$('#tiptip_content').css('backgroundColor',$('#sub_nav').css('backgroundColor'));}}
if(width>$("#recently_viewed div.viewport").width()){var move=function(direction){return function(){var $this=$(this);$this.unbind('click');var wrapper=$("#recently_viewed ul");var distance=wrapper.find("li").outerWidth({margin:true});var motion=direction=='left'?'-=':'+=';wrapper.animate({left:motion+distance,duration:'fast'},function(){if(direction=='left'){maxNowViewing++;minNowViewing++;}else{maxNowViewing--;minNowViewing--;}
updateNowViewing();$this.click(move(direction));});};};$prev.click(move('right'));$next.click(move('left'));}else{$("#recently_viewed a.move").hide();}
$("#recently_viewed .close").click(function(){$("#recently_viewed").fadeOut();});};var showRecentlyViewed=function(){var $anchor=$(this);$("#recently_viewed_container").load(GUCCI.makeUrl("/recently_viewed"),function(){$(this).show();var rv=$("#recently_viewed");var pos=$anchor.offset();rv.css({top:pos.top-(rv.height()+8),left:pos.left-10});rv.fadeIn('fast',initRecentlyViewed);});};$("a.recently_viewed_link").click(showRecentlyViewed);var tempURL=window.location.href.split("/");var tempPath="";for(var i=0;i<4;i++){tempPath+=tempURL[i]+"/";}
$("div#unsubscribe_card form").css("display","block");$("div#unsubscribe_card div.unsubscribe_confirmation").css("display","none");$("div.error").hide();$('div#unsubscribe_btn a').click(function(){var unsubscribeEmail={"unsubscribe_email_address":$("#unsubscribe_email_address").val()};var unsubscribePath=GUCCI.makeUrl("/unsubscription/unsubscribe_email");$.ajax({type:"POST",url:unsubscribePath,data:unsubscribeEmail,success:function(myData){if(myData.status=="success"){$("div#unsubscribe_card form").css("display","none");$("div#unsubscribe_card div.unsubscribe_confirmation").css("display","block");}else{$("div.error").show();$("#unsubscribe_email_address").addClass("field_with_errors");}}});});});GUCCI.Home={animation:{fade_bg:1000,fade_sidebar:500,fade_mainimg:1000,fade_headline:300,fade_changefeature:1000,feature_cycle:10000,mouseover:300,sidebar_activeopacity:0.6},isAnimating:false,animatingTowardFeature:0,animatingFromFeature:0,hoveringOnSidebarFeature:0,currentfeature:1,runningFeatureCycle:true,addRemainingReflections:function(){$('#mainfeature .feature:gt(0) div.imgcontainer').each(function(){var $this=$(this);if($this.hasClass('imgcontainerreflect'))
GUCCI.Home.addReflection($this.find('img')[0]);});},addReflection:function(img){if(typeof Reflection=='object')
Reflection.add(img,{height:0.04,opacity:0.5});},mainimgload:function(){var loaded=false;$('#mainfeature .active .imgcontainer img').onImagesLoaded(function(){$('#mainfeature .active .imgcontainer').css('opacity','1');loaded=true;});if(!loaded)
setTimeout(GUCCI.Home.mainimgload,500);},changeFeature:function(featurenum){if(featurenum==GUCCI.Home.currentfeature||GUCCI.Home.isAnimating)return;var sidebars=$('#sidebar li.feature');if(sidebars.length<featurenum)return;var sidecurractive=sidebars.filter('.active');var sidenewactive=$(sidebars[featurenum-1]);var mainfeatures=$('#mainfeature div.feature');if(mainfeatures.length<featurenum)return;var maincurractive=mainfeatures.filter('.active');var mainnewactive=$(mainfeatures[featurenum-1]);var bgs=$('#bg_content div');if(bgs.length<featurenum)return;var bgcurractive=bgs.filter('.active');var bgnewactive=$(bgs[featurenum-1]);sidenewactive.trigger('mouseleave');GUCCI.Home.isAnimating=true;GUCCI.Home.animatingTowardFeature=featurenum;GUCCI.Home.animatingFromFeature=GUCCI.Home.currentfeature;var delaytime=0;if($.browser.msie&&$.browser.version.substr(0,1)<8){sidecurractive.children('img').fadeTo(GUCCI.Home.animation.fade_changefeature,1,function(){sidecurractive.removeClass('active');});}
else{sidecurractive.fadeTo(GUCCI.Home.animation.fade_changefeature,1,function(){sidecurractive.removeClass('active');});}
if($.browser.msie&&$.browser.version.substr(0,1)<7){maincurractive.hide();maincurractive.removeClass('active');}
else
maincurractive.fadeOut(GUCCI.Home.animation.fade_changefeature,function(){maincurractive.removeClass('active');});delaytime+=GUCCI.Home.animation.fade_changefeature;$('#bg_content div.bg_homefeature:not(.active)').css('display','none');bgcurractive.delay(delaytime/2).fadeOut(GUCCI.Home.animation.fade_changefeature/2,function(){bgcurractive.removeClass('active');});bgnewactive.delay(delaytime).fadeIn(GUCCI.Home.animation.fade_changefeature,function(){bgnewactive.addClass('active');});delaytime+=GUCCI.Home.animation.fade_changefeature;if($.browser.msie&&$.browser.version.substr(0,1)<8){sidenewactive.addClass('active');GUCCI.Home.currentfeature=featurenum;sidenewactive.children('img').delay(delaytime).fadeTo(GUCCI.Home.animation.fade_changefeature,GUCCI.Home.animation.sidebar_activeopacity);}
else{sidenewactive.delay(delaytime).fadeTo(GUCCI.Home.animation.fade_changefeature,GUCCI.Home.animation.sidebar_activeopacity,function(){sidenewactive.addClass('active');GUCCI.Home.currentfeature=featurenum;});}
if(!$.browser.msie&&mainnewactive.find('div.imgcontainer').hasClass('imgcontainerreflect'))
GUCCI.Home.addReflection(mainnewactive.find('img')[0]);if($.browser.msie&&$.browser.version.substr(0,1)<7){setTimeout(function(){mainnewactive.show();mainnewactive.addClass('active');if($.browser.version.substr(0,1)<7)
mainnewactive.find('div.imgcontainer img').pngfix();GUCCI.Home.isAnimating=false;GUCCI.Home.checkSidebarHover();},delaytime);}
else{mainnewactive.delay(delaytime).fadeIn(GUCCI.Home.animation.fade_changefeature,function(){mainnewactive.addClass('active');GUCCI.Home.isAnimating=false;GUCCI.Home.checkSidebarHover();});}},checkSidebarHover:function(){if(this.hoveringOnSidebarFeature==0)return;var sidebars=$('#sidebar li');$(sidebars[this.hoveringOnSidebarFeature-1]).trigger('mouseenter');},sidebarMouseOver:function(){if(GUCCI.Home.isAnimating)return;var $this=$(this);if($this.hasClass('active'))return;if($.browser.msie&&$.browser.version.substr(0,1)<8){$this.children('div.highlight').stop(true,true).fadeIn(GUCCI.Home.mouseoverspeed);}
else{$this.children('div.highlight').stop(true,true).fadeIn(GUCCI.Home.mouseoverspeed);$this.find('span div.bg').stop(true,true).fadeTo(GUCCI.Home.mouseoverspeed,0.7);}},sidebarMouseOff:function(){if(GUCCI.Home.isAnimating)return;var $this=$(this);if($this.hasClass('active'))return;if($.browser.msie&&$.browser.version.substr(0,1)<8){$this.children('div.highlight').stop(true,true).fadeOut(GUCCI.Home.mouseoverspeed);}
else{$this.children('div.highlight').stop(true,true).fadeOut(GUCCI.Home.mouseoverspeed);$this.find('span div.bg').stop(true,true).fadeTo(GUCCI.Home.mouseoverspeed,0.6);}},sidebarMobileTap:function(){if(GUCCI.Home.isAnimating)return;var $this=$(this);if($this.hasClass('active'))return;$this.children('div.highlight').stop(true,true).fadeIn(GUCCI.Home.mouseoverspeed,function(){$(this).fadeOut(GUCCI.Home.mouseoverspeed);});$this.find('span div.bg').stop(true,true).fadeTo(GUCCI.Home.mouseoverspeed,0.7,function(){$(this).fadeTo(GUCCI.Home.mouseoverspeed,0.6);});},featureCycle:function(){if(!GUCCI.Home.runningFeatureCycle)return;var newfeature=GUCCI.Home.currentfeature+1;if(GUCCI.Home.currentfeature>2)
newfeature=1;GUCCI.Home.changeFeature(newfeature);setTimeout(GUCCI.Home.featureCycle,GUCCI.Home.animation.feature_cycle);},cufonInitialize:function(){if(typeof Cufon=='function')
Cufon.replace('h2,h3,h4,body.home #sidebar li span,body.home #mainfeature a');}};$(document).ready(function(){if($('body.home.show').length==0&&$('body.home.quick_view_test').length==0)return;var lazyloadimages=$('#mainfeature .feature:gt(0) div.imgcontainer img');lazyloadimages.lazyload({event:'manual',pngfix:false});if($.browser.msie&&$.browser.version.substr(0,1)<7)
$("#overlay_screen").css("height",GUCCI.viewport.height());if(!GUCCI.isMobile){$('#sidebar li').hover(GUCCI.Home.sidebarMouseOver,GUCCI.Home.sidebarMouseOff);$('#sidebar li').each(function(i){$(this).mouseenter(function(){GUCCI.Home.hoveringOnSidebarFeature=i+1;});$(this).mouseleave(function(){if(GUCCI.Home.hoveringOnSidebarFeature==i+1)
GUCCI.Home.hoveringOnSidebarFeature=0;});});}
$('#sidebar li').each(function(i){$(this).click(function(){GUCCI.Home.runningFeatureCycle=false;GUCCI.Home.changeFeature(i+1);});});if(GUCCI.isMobile){$('#sidebar li').bind('touchstart',GUCCI.Home.sidebarMobileTap);}
$('#mainfeature .text').click(function(){var $this=$(this);$this.parent('a').trigger('click');}).hover(function(){var $this=$(this);if(!Cufon)
$this.addClass('highlight');else
Cufon.replace($this,{color:'#fff'});},function(){var $this=$(this);if(!Cufon)
$this.removeClass('highlight');else
Cufon.replace($this,{color:$this.parent('a').css('color')});});if(GUCCI.isMobile){$('#mainfeature .imgcontainer a').attr('href','#');$('#mainfeature .feature > a').bind('mouseover',function()
{document.location=$(this).attr('href');});}
var speed=GUCCI.Home.animation;if($.browser.msie&&$.browser.version.substr(0,1)<8){$('#bg_homefeature1,#sidebar li').css('display','block');}
else
$('#bg_homefeature1,#sidebar li').fadeIn(speed.fade_bg);if($.browser.msie&&$.browser.version.substr(0,1)>=8){var link=$('#mainfeature a');link.css('width','400px');}
$('#bg_homefeature1').addClass('active');var mactive=$('#mainfeature .active');if($.browser.msie&&$.browser.version.substr(0,1)<8){mactive.css('display','block');$('#mainfeature .feature').children('.imgcontainer').css({'display':'block','opacity':'0'});}
if(mactive.find('div.imgcontainer').hasClass('imgcontainerreflect')&&($.browser.msie&&$.browser.version.substr(0,1)==7))
GUCCI.Home.addReflection(mactive.find('img')[0]);if($.browser.msie&&$.browser.version.substr(0,1)<8){if($.browser.version.substr(0,1)<7)
$('#mainfeature .active div.imgcontainer img').pngfix();$('#mainfeature div.imgcontainer').css({'display':'block','opacity':'1'});GUCCI.Home.isAnimating=false;GUCCI.Home.checkSidebarHover();lazyloadimages.trigger('appear');setTimeout(GUCCI.Home.featureCycle,GUCCI.Home.animation.feature_cycle);}
else{mactive.fadeIn(speed.fade_headline);mactive.children('.imgcontainer').delay(speed.fade_mainimg).fadeIn(speed.fade_mainimg,function(){GUCCI.Home.isAnimating=false;GUCCI.Home.checkSidebarHover();$('#mainfeature .feature div.imgcontainer').css('display','block');lazyloadimages.trigger('appear');setTimeout(GUCCI.Home.featureCycle,GUCCI.Home.animation.feature_cycle);});}});GUCCI.Category={scrollwidth:0,grooveposition:0,panelnumber:0,paneloffset:0,panelwidth:0,panelwidthdata:null,panelcountdata:null,singlecontentwidth:264,triptychcontentwidth:812,singlewidth:274,triptychwidth:822,modeldisplaywidth:230,modelcenterline:500,scrollbarspace:0,lookdetailopen:false,initialpositionoffset:0,readytoload:true,lazyloaders:'.sixup_image, .fourup_image, .threeup_image, .look img',firstpaneloffset:0,onquickview:false,samplefontscontainer:null,asianset:false,hasquickviewhighlight:null,wg:{title_pace:400,titlebg_pace:1000,titlebox_pace:1000,fudge_factor:0.4},panel:{first:0,second:0,third:0,pace:500,fastpace:200,currentstyle:'',ignore:false,rememberposition:true},position:{prefix:'lk'},scroll:{sustain:0,interval:1500,ok:true,autoBack:function()
{if($('#pagination div.back').css('display')!='none'&&GUCCI.Category.scroll.ok)GUCCI.Category.pageBack();},autoForward:function()
{if($('#pagination div.forward').css('display')!='none'&&GUCCI.Category.scroll.ok)GUCCI.Category.pageForward();}},zoom:{initial_zoom:false,dragged:false,activelooks:new Array(),factor:4,window_width:432,window_height:408,viewable_width:1414,viewable_height:1456,actual_width:1480,actual_height:1632,theoretical_width:2400,theoretical_height:1772,out_img_width:600,out_img_height:443,out_offset:460,viewable_x_offset:33,viewable_y_offset:88,last_x:0,last_y:0,baseline:360,origin_x:-76,origin_y:0,pace:750},categoryIFF:function()
{if($.browser.msie&&$.browser.version.substr(0,1)<7)
{if(typeof $.fn.supersleight=='function')
{$('ul.hero_panel li div.imgcontainer a img').pngfix();}}},calcScrollWidth:function()
{var scrollwidth=0;for(var i=0;i<GUCCI.Category.panelwidthdata.length;i++)
{scrollwidth+=GUCCI.Category.panelwidthdata[i]*this.singlewidth;}
return scrollwidth;},heroOrigins:function()
{if(typeof $.fn.pixelinitalphabet=='function'){var samplefonts=jQuery('<div/>',{id:'samplefonts'});$('#bg_content').append(samplefonts);samplefonts.css('visibilty','hidden');var herotitles=$('body.category ul.hero_panel h2 a');var linecontainerwidth=$('ul.hero_panel').width();var linelinkwidth=$('body.category ul.hero_panel h2 a').width();var availwidth=linecontainerwidth-linelinkwidth-5;if(!availwidth||availwidth<90)availwidth=252;if($.browser.msie&&$.browser.version.substr(0,1)<7)
availwidth=252;herotitles.pixeltruncate({pixellimit:availwidth,checkcontainer:samplefonts,numlines:2});}},initPixelTruncations:function()
{var heropanels=$('body.category ul.hero_panel');var singleherotitles=heropanels.not('.double_wide').find('h2 a');var doubleherotitles=heropanels.filter('.double_wide').find('h2 a');var availwidthdouble=500;var availwidthsingle=245;singleherotitles.pixeltruncate({pixellimit:availwidthsingle,checkcontainer:GUCCI.Category.samplefontscontainer,numlines:2,asiancharset:GUCCI.Category.asianset});doubleherotitles.pixeltruncate({pixellimit:availwidthdouble,checkcontainer:GUCCI.Category.samplefontscontainer,numlines:2,asiancharset:GUCCI.Category.asianset});if(typeof Cufon=='function'&&!GUCCI.isMobile)
Cufon.replace(singleherotitles.add(doubleherotitles));var quickviewtitles=$('body.category ul.hero_panel .quick_view h3 a, body.category ul.oneup_panel .quick_view h3 a');var quickviewwidth=147;quickviewtitles.pixeltruncate({pixellimit:quickviewwidth,checkcontainer:GUCCI.Category.samplefontscontainer,numlines:3,asiancharset:GUCCI.Category.asianset});if(typeof Cufon=='function'&&!GUCCI.isMobile)
Cufon.replace(quickviewtitles);},lazyLoadImages:function()
{if(typeof $.fn.lazyload=='function')
{var lazyimages=$(GUCCI.Category.lazyloaders);var placeholder='/images/ecommerce/transparent.gif';lazyimages.lazyload({event:'manual',pngfix:true,placeholder:placeholder,transition:'fade'});}},basesLoaded:function()
{GUCCI.Category.panel.first=this.paneloffset;GUCCI.Category.panel.second=this.paneloffset+GUCCI.Category.singlewidth;GUCCI.Category.panel.third=this.paneloffset+2*GUCCI.Category.singlewidth;},resizeScrollbar:function()
{if(!this.paneloffset||!this.panelwidth)return;this.paneloffset=$(window).width()/2-(1.5*this.panelwidth);this.scrollwidth=this.calcScrollWidth();GUCCI.Category.basesLoaded();$('#scrollbar').goldscrollbar({container:$('#content_slider'),handle:$('#scrollbar .handle'),offset:this.paneloffset,scrollwidth:this.scrollwidth,effect:'drag',columnwidth:this.panelwidth,resize:true,panelwidthdata:this.panelwidthdata,panelcountdata:this.panelcountdata});},cufonInitialize:function()
{if(typeof Cufon=='function')Cufon.replace('h2, h4, body.category .quick_view em.price, '+'body.category ul.oneup_panel p a, '+'body.category .wg_panel div.blurb h6,'+'body.category .wg_panel div.blurb a,'+'body.category .news_panel h6');},setPanelInfo:function()
{var pinfo=$('#panel_info');var paneltypes=pinfo.children('span.panel_types');var panelcounts=pinfo.children('span.number_of_items');var numpanels=pinfo.children('span.number_of_panels');if(paneltypes.length==0||numpanels.length==0)return;GUCCI.Category.panelwidthdata=paneltypes.html().split(',');GUCCI.Category.panelcountdata=panelcounts.html().split(',');var columnnumber=0;for(var i=0;i<GUCCI.Category.panelwidthdata.length;i++)
{columnnumber+=parseInt(GUCCI.Category.panelwidthdata[i],10);}
GUCCI.Category.panelnumber=jQuery.trim(numpanels.html());if(GUCCI.Category.panelnumber==null||GUCCI.Category.panelnumber=='')return;if(GUCCI.Category.panelwidthdata.length==0)return;GUCCI.Category.panelwidth=GUCCI.Category.singlewidth;GUCCI.Category.paneloffset=($(window).width()-GUCCI.Category.triptychwidth)/2;GUCCI.Category.scrollwidth=GUCCI.Category.calcScrollWidth(GUCCI.Category.paneloffset);$('#scrollbar').goldscrollbar({container:$('#content_slider'),offset:GUCCI.Category.paneloffset,scrollwidth:GUCCI.Category.scrollwidth,effect:'drag',columnwidth:GUCCI.Category.panelwidth,panelwidthdata:GUCCI.Category.panelwidthdata,panelcountdata:GUCCI.Category.panelcountdata,lazyreset:true});if(GUCCI.IEversion!=6&&GUCCI.IEversion!=7)
{$('#category_results').animate({opacity:1});}
var pagination=$('#pagination');pagination.find('.back span').click(function(){if(!GUCCI.Category.panel.ignore)GUCCI.Category.pageBack();});pagination.find('.forward span').click(function(){if(!GUCCI.Category.panel.ignore)GUCCI.Category.pageForward();});if(GUCCI.isMobile){if(typeof $.fn.goldswipe=='function')
{$('#content_slider').goldswipe({thresholdX:GUCCI.Category.panelwidth/2,thresholdY:999999,swipeLeft:function(){var colstomove=Math.ceil($.fn.goldswipe.vars.change.x/GUCCI.Category.panelwidth);$('#scrollbar .handle').goldscrollcontainer(colstomove*GUCCI.Category.panelwidth);},swipeRight:function(){var colstomove=Math.floor($.fn.goldswipe.vars.change.x/GUCCI.Category.panelwidth);$('#scrollbar .handle').goldscrollcontainer(colstomove*GUCCI.Category.panelwidth);}});}}
GUCCI.Category.readytoload=true;$('#sub_nav').find('.blind').hide();},pageBack:function(){$('#scrollbar .handle').goldscrollcontainer(-3*GUCCI.Category.panelwidth);},pageForward:function(){$('#scrollbar .handle').goldscrollcontainer(3*GUCCI.Category.panelwidth);},getCategoryNavHref:function(catlink)
{var catsplit=catlink.split('#');var suffix=catsplit[catsplit.length-1];return(catsplit[0]+"/"+suffix).replace(/look/g,'lk');},setCategoryNavHref:function(catlink)
{catlink=catlink.replace(/lk/g,'look');var catsplit=catlink.split('/');var existinghash=window.location.href.indexOf('#');var suffix='#'+catsplit[catsplit.length-1];if(existinghash>-1)window.location.href=window.location.href.substring(0,existinghash)+suffix;else window.location.href+=suffix;},initZoomBindings:function(thislook)
{var show_zoomout=function(view)
{var view_zoomout=view.html().replace(/web_look_minithumb/g,'web_look_zoomout');var zoomout=thislook.find('.zoom_out');zoomout.html(view_zoomout);thislook.find('.zoom_in_window').hide();thislook.find('.zoom_out').show();};var toggle_right_column=function(look,tgl)
{if(tgl=='hide')
{look.find('.wg_carousel').hide();look.find('.look_icons').hide();}
else if(tgl=='show')
{look.find('.wg_carousel').show();look.find('.look_icons').show();}};thislook.find('.zoom_close').click(function()
{if(!GUCCI.isIE)
{thislook.find('.zoom_overlay').fadeOut('fast');}
else
{thislook.find('.zoom_overlay').hide();}
toggle_right_column(thislook,'show');});thislook.find('.zoomlook_button').click(function()
{var vz=thislook.find('.view.main');GUCCI.Category.zoom.activelooks.push(thislook);toggle_right_column(thislook,'hide');show_zoomout(vz);vz.click();thislook.find('.zoom_overlay').show();});var select_alternate_view=function(zoom_view)
{var alternate_views=thislook.find('.zoom_tools li');alternate_views.removeClass('active_view');zoom_view.removeClass('inactive_view');zoom_view.addClass('active_view');};thislook.find('.view').click(function()
{if($(this).hasClass('active_view'))return;var view_zoomout="";var view_zoomin="";view_zoomout=$(this).html().replace(/web_look_minithumb/g,'web_look_zoomout');view_zoomin=view_zoomout.replace(/web_look_zoomout/g,'web_look_zoomin');GUCCI.Category.zoom.dragged=false;thislook.find('.zoom_tools li').removeClass('active_view');thislook.find('.zoom_tools li').addClass('inactive_view');$(this).removeClass('inactive_view');$(this).addClass('active_view');thislook.find('.zoom_in').fadeOut("slow");thislook.find('.zoom_out').html(view_zoomout);thislook.find('.zoom_out').fadeIn("slow");thislook.find('.zoom_in_window').fadeOut("slow");thislook.find('.zoom_in').html(view_zoomin);});thislook.find('.zoom_out').click(function(evn)
{GUCCI.Category.resizeZoom();var zoom_left=Math.round(thislook.find('.zoom_out img').offset().left);var zoom_top=Math.round(thislook.find('.zoom_out img').offset().top);var mousex=evn.clientX-zoom_left;var mousey=evn.clientY-zoom_top;var actual_offset_x=(GUCCI.Category.zoom.theoretical_width-GUCCI.Category.zoom.actual_width)/2;var target_x=(3*mousex)-actual_offset_x;var target_y=(3*mousey);thislook.find('.zoom_in').css({'left':GUCCI.Category.zoom.origin_x-target_x});thislook.find('.zoom_in').css({'top':GUCCI.Category.zoom.origin_y-target_y});animateZoom(mousex,mousey,'zoom_in');});var toggle_zoom_windows=function(zoom_window)
{if(zoom_window=='zoom_in')
{thislook.find('.zoom_out').hide();thislook.find('.zoom_in').show();thislook.find('.zoom_in_window').show();}
else
{thislook.find('.zoom_out').show();thislook.find('.zoom_in').hide();thislook.find('.zoom_in_window').hide();}};var animateZoom=function(x,y,direction)
{var zoom_width=0;var zoom_height=0;var zoom_direction=0;if(direction=='zoom_in')
{zoom_width=GUCCI.Category.zoom.theoretical_width;zoom_height=GUCCI.Category.zoom.theoretical_height;zoom_direction=-1;var zoom_target_x=zoom_direction*((GUCCI.Category.zoom.factor-1)*x);var zoom_target_y=zoom_direction*((GUCCI.Category.zoom.factor-1)*y);thislook.find('.zoom_out img').animate({left:zoom_target_x+GUCCI.Category.zoom.origin_x,top:zoom_target_y,height:zoom_height,width:zoom_width},GUCCI.Category.zoom.pace,function(){toggle_zoom_windows(direction);});}
else
{toggle_zoom_windows(direction);zoom_width=GUCCI.Category.zoom.out_img_width;zoom_height=GUCCI.Category.zoom.out_img_height;zoom_direction=1;thislook.find('.zoom_out img').animate({left:GUCCI.Category.zoom.origin_x,top:GUCCI.Category.zoom.origin_y,height:zoom_height,width:zoom_width},GUCCI.Category.zoom.pace,function(){GUCCI.Category.zoom.dragged=false;});}};thislook.find('.zoom_reset').click(function(evn)
{var zoom_left=Math.round(thislook.find('.zoom_view').offset().left);var zoom_top=Math.round(thislook.find('.zoom_view').offset().top);var mousex=evn.clientX-zoom_left;var mousey=evn.clientY-zoom_top;if(GUCCI.Category.zoom.dragged)
{thislook.find('.zoom_out img').css({left:GUCCI.Category.zoom.last_x});thislook.find('.zoom_out img').css({top:GUCCI.Category.zoom.last_y});}
animateZoom(mousex,mousey,'zoom_out');});},initZoom:function(look)
{look.find('.zoom_in').draggable({start:function(evn,ui)
{if($.browser.mozilla)$(this).css('cursor','-moz-grabbing');if($.browser.msie)$(this).css('cursor','url(../../images/ecommerce/grabbing.cur)');},stop:function(evn,ui)
{if($.browser.mozilla)$(this).css('cursor','-moz-grab');if($.browser.msie)$(this).css('cursor','url(../../images/ecommerce/grab.cur)');GUCCI.Category.zoom.last_x=look.find('.zoom_in').position().left-GUCCI.Category.zoom.out_offset;GUCCI.Category.zoom.last_y=look.find('.zoom_in').position().top;GUCCI.Category.zoom.dragged=true;}});GUCCI.Category.resizeZoom();},resizeZoom:function()
{if($('body.category').length==0||GUCCI.Category.zoom.activelooks.length==0)return;for(var l=0;l<GUCCI.Category.zoom.activelooks.length;l++)
{var zoom_left=Math.round(GUCCI.Category.zoom.activelooks[l].find('.zoom_view').offset().left);var zoom_top=Math.round(GUCCI.Category.zoom.activelooks[l].find('.zoom_view').offset().top);var zoom_width=GUCCI.Category.zoom.window_width;var zoom_height=GUCCI.Category.zoom.window_height;var viewable_x_offset=GUCCI.Category.zoom.viewable_x_offset;var viewable_y_offset=GUCCI.Category.zoom.viewable_y_offset;var viewable_width=GUCCI.Category.zoom.viewable_width-zoom_width;var viewable_height=GUCCI.Category.zoom.viewable_height-zoom_height;var x1=(zoom_left-viewable_x_offset)-viewable_width;var y1=(zoom_top-viewable_y_offset)-viewable_height;var x2=zoom_left-viewable_x_offset;var y2=zoom_top-viewable_y_offset;GUCCI.Category.zoom.activelooks[l].find('.zoom_in').draggable('option','containment',[x1,y1,x2,y2]);}},showQuickView:function()
{var $this=$(this);if(GUCCI.Category.moveQuickView($this))
$('#container_quick_view').fadeIn('fast');},hideQuickView:function()
{if(GUCCI.Category.onquickview)return;$(this).add(GUCCI.Category.hasquickviewhighlight).removeClass('qview_active').css('zIndex','0');$('#container_quick_view').fadeOut('fast');GUCCI.Category.onquickview=false;},showQuickViewIE6:function()
{var $this=$(this);if(GUCCI.Category.moveQuickView($this))
$('#container_quick_view').show();},hideQuickViewIE6:function()
{if(GUCCI.Category.onquickview)return;$(this).add(GUCCI.Category.hasquickviewhighlight).removeClass('qview_active').css('zIndex','0');$('#container_quick_view').hide();GUCCI.Category.onquickview=false;},showQuickViewOld:function()
{var $this=$(this);$this.parents('.ggpanel').css({"z-index":"1900"});$this.parents('.ggpanel').hide().show();$this.css({"z-index":"2000"});var qView=$this.find(".quick_view");qView.stop().css({"z-index":"2001"});qView.fadeIn("fast");},hideQuickViewOld:function()
{var $this=$(this);var qView=$this.find(".quick_view");qView.stop();qView.css({"opacity":"1"});qView.fadeOut("fast");$this.css({"z-index":"0"});$this.parents('.ggpanel').css({"z-index":"0"});$this.parents('.ggpanel').hide().show();},showQuickViewIE6Old:function()
{var $this=$(this);$this.parents('.ggpanel').css({"z-index":"1900"});$this.parents('.ggpanel').hide().show();$this.css({"z-index":"2000"});var qView=$this.find(".quick_view");qView.css({"z-index":"1000"}).stop();qView.show();},hideQuickViewIE6Old:function()
{var $this=$(this);var qView=$this.find(".quick_view");qView.stop();qView.hide();$this.css({"z-index":"0"});$this.parents('.ggpanel').css({"z-index":"0"});$this.parents('.ggpanel').hide().show();},moveQuickView:function(productele)
{var qview=$('#container_quick_view');var img=productele.find('.threeup_image,.sixup_image,.fourup_image');if(img.length==0)return false;var qviewhtml=img.attr('rel');if(qviewhtml==null)return false;qview.html(qviewhtml);var link=qviewhtml.match(/href="[^"]*/ig);if(link.length>0)
link=link[0].replace('href="','');productele.css('cursor','pointer');productele.click(function(){document.location=link;});var quickviewwidth=147;var quickviewtitles=qview.find('h3 a');var price=qview.find('.price');quickviewtitles.pixeltruncate({pixellimit:quickviewwidth,checkcontainer:GUCCI.Category.samplefontscontainer,numlines:3,asiancharset:GUCCI.Category.asianset});if(typeof Cufon=='function'&&!GUCCI.isMobile)
Cufon.replace(quickviewtitles.add(price));qview.attr('class',img.attr('class'));var parentpanel=productele.parents('.ggpanel');var productwidth=productele.width();var qviewwidth=qview.width();var newleft=productele.offset().left+$('#content_slider').scrollLeft();var newtop=productele.position().top+25;if(parentpanel.hasClass('edge_left'))
{newleft+=productwidth;qview.addClass('right');}
else
{newleft-=qviewwidth;qview.addClass('left');}
productele.addClass('qview_active').css('zIndex','500');GUCCI.Category.hasquickviewhighlight=productele;qview.css({'left':newleft+'px','top':newtop+'px'});return true;},initQuickViews:function()
{var productareas=$('#panel_wrapper .ggpanel ul');var newquickviewareas=productareas.filter('.sixup_panel,.threeup_panel,.fourup_panel').children('li');var oldquickviewareas=productareas.not('.sixup_panel,.threeup_panel,.fourup_panel').children('li');var containerqview=$('#container_quick_view');newquickviewareas.not('.no_quickview').hover(function()
{if(GUCCI.isIE6)$(this).doTimeout('quickview',400,GUCCI.Category.showQuickViewIE6);else $(this).doTimeout('quickview',400,GUCCI.Category.showQuickView);},function()
{if(GUCCI.isIE6)$(this).doTimeout('quickview',0,GUCCI.Category.hideQuickViewIE6);else $(this).doTimeout('quickview',0,GUCCI.Category.hideQuickView);});containerqview.hover(function()
{GUCCI.Category.onquickview=true;},function()
{GUCCI.Category.onquickview=false;if(GUCCI.isIE6)
$('#container_quick_view').hide();else
$('container_quick_view').fadeOut('fast');if(GUCCI.isIE6)$(this).doTimeout('quickview',0,GUCCI.Category.hideQuickViewIE6);else $(this).doTimeout('quickview',0,GUCCI.Category.hideQuickView);});oldquickviewareas.not('.no_quickview').hover(function()
{if(GUCCI.isIE6)$(this).doTimeout('quickview',400,GUCCI.Category.showQuickViewIE6Old);else $(this).doTimeout('quickview',400,GUCCI.Category.showQuickViewOld);},function()
{if(GUCCI.isIE6)$(this).doTimeout('quickview',0,GUCCI.Category.hideQuickViewIE6Old);else $(this).doTimeout('quickview',0,GUCCI.Category.hideQuickViewOld);});},initMobileGestures:function()
{var productareas=$('#panel_wrapper .ggpanel ul');var allproducts=productareas.children('li');allproducts.click(function(e)
{if($(e.target).hasClass('quick_view')||($(e.target).parents('.quick_view').length>0))
e.stopPropagation();else
{$this=$(this);var finallink=null;var img=$this.find('.threeup_image,.sixup_image,.fourup_image');if(img.length>0)
{var qviewhtml=img.attr('rel');if(qviewhtml!=null)
{var link=qviewhtml.match(/href="[^"]*/ig);if(link.length>0)
finallink=link[0].replace('href="','');}}
else
{var qviewlink=$this.find('.quick_view a');finallink=qviewlink.attr('href');}
if(finallink!=null)
{$this.css('background','#000000');$this.children().css('opacity',0.7);document.location=finallink;}}});var newquickviewareas=productareas.filter('.sixup_panel,.threeup_panel,.fourup_panel').children('li');var oldquickviewareas=productareas.not('.sixup_panel,.threeup_panel,.fourup_panel').children('li');var containerqview=$('#container_quick_view');newquickviewareas.not('.no_quickview').bind('gesturestart',function()
{var childimg=$(this).children('img');var existingimg=GUCCI.Category.hasquickviewhighlight!=null?GUCCI.Category.hasquickviewhighlight.children('img'):null;if(childimg!=null&&existingimg!=null&&childimg.attr('src')==existingimg.attr('src')){GUCCI.Category.onquickview=false;$(this).doTimeout('quickview',0,GUCCI.Category.hideQuickView);}
else
$(this).doTimeout('quickview',400,GUCCI.Category.showQuickView);});containerqview.bind('gesturestart',function()
{GUCCI.Category.onquickview=false;$('container_quick_view').fadeOut('fast');$(this).doTimeout('quickview',0,GUCCI.Category.hideQuickView);});oldquickviewareas.not('.no_quickview').bind('gesturestart',function()
{$(this).doTimeout('quickview',400,GUCCI.Category.showQuickViewOld);var panelitem=$(this);$(this).find(".quick_view").one('gesturestart',function(event)
{event.stopPropagation();panelitem.doTimeout('quickview',0,GUCCI.Category.hideQuickViewOld);});});},updateLookUrl:function(looks)
{var newurl=document.location.href.split('#')[0]+'#';for(var l=0;l<looks.length;l++)
{if(looks[l]!=undefined)
newurl+=looks[l];else
newurl+=GUCCI.Category.position.prefix+"B224";}
document.location.href=newurl.replace(/lk/g,'look');},shareDialog:function(overlay,myLookid)
{overlay.find('.share a.share').goldoverlay({onBeforeLoad:function()
{shareLink=overlay.find('.share a.share');shareApi=$(shareLink).data('overlay');shareOverlay=shareApi.getOverlay();shareOverlay.find('.close').click(function(){shareApi.close();return false;});$("#overlay_screen").addClass("overlayScreenFix");},onLoad:function()
{if(typeof Cufon=='function')
Cufon.replace('.share_overlay h3');var emailLink=shareOverlay.find('.email_friend_link');var targetOverlay=shareOverlay;GUCCI.emailFriend(emailLink,targetOverlay,myLookid,"look");$('div.share_overlay  a.cancel_overlay, div.share_overlay .close').click(function()
{$("div.share_overlay").remove();$("#overlay_screen").removeClass("overlayScreenFix");});}});},initStyleActions:function()
{$('.look_detail ul.style_list li').mouseover(function()
{var container=$(this).parents('.associated_styles_container');var offset=container.find('.style_list').position().top?container.find('.style_list').position().top:0;var newposition=$(this).position().top-1+offset;container.css('background-position','0px '+newposition+"px");GUCCI.Category.panel.currentstyle=$(this);});$('.look_detail ul.style_list li').mouseout(function()
{var container=$(this).parents('.associated_styles_container');container.css('background-position','0px -300px');});$('#panel_wrapper .associated_styles_container').scroll(function(){if(typeof GUCCI.Category.panel.currentstyle=='object')GUCCI.Category.panel.currentstyle.mouseover();});},personalShopper:function(overlay)
{var psLink=overlay.find('a.personal_shopperlink');var psOverlay=overlay.find('.contact_personal_shopper_overlay');GUCCI.PersonalShopper.setupPersonalShopper(psLink,psOverlay);},hasLeftTheBuilding:function()
{if(!GUCCI.isIE6)
{$('#panel_wrapper').fadeOut(GUCCI.Category.panel.pace);$('#pagination').fadeOut(GUCCI.Category.panel.pace);}},makesAnEntrance:function()
{if(!GUCCI.isIE6)
{$('#panel_wrapper').fadeIn(GUCCI.Category.panel.pace);$('#pagination').fadeIn(GUCCI.Category.panel.pace);}
else
{$('#panel_wrapper').show();$('#pagination').show();}},initArrowKeyScrolling:function()
{$(document).keydown(function(evn)
{if(evn.keyCode==37&&$('#pagination div.back').css('display')!='none')GUCCI.Category.pageBack();else if(evn.keyCode==39&&$('#pagination div.forward').css('display')!='none')GUCCI.Category.pageForward();});}};$(document).ready(function()
{if($('body.category').length==0)return;GUCCI.Category.setPanelInfo();GUCCI.Category.initArrowKeyScrolling();GUCCI.Category.lazyLoadImages();GUCCI.Category.categoryIFF();GUCCI.Category.samplefontscontainer=jQuery('<div/>',{id:'samplefonts'});GUCCI.Category.samplefontscontainer.css('visibilty','hidden');$('#bg_content').append(GUCCI.Category.samplefontscontainer);GUCCI.Category.initPixelTruncations();$('#sub_nav .category_nav_remote_link').click(function()
{if(GUCCI.Category.readytoload)
{GUCCI.Category.readytoload=false;$('#sub_nav').find('.blind').show();$('#category_results').remove();var newhref=this.href;var withhash=newhref.substr(newhref.lastIndexOf('#'));var nohash=withhash.replace('#','/');newhref=newhref.replace(withhash,nohash);$.get(newhref,{},function(data)
{$('#content').append(data);if(!GUCCI.isMobile)
{GUCCI.Category.initQuickViews();}
else
GUCCI.Category.initMobileGestures();GUCCI.Category.lazyLoadImages();GUCCI.Category.setPanelInfo();GUCCI.Category.makesAnEntrance();GUCCI.Category.cufonInitialize();GUCCI.Category.initPixelTruncations();});if(GUCCI.Category.panelnumber<=3)$('#category_tools').hide();$('#sub_nav ul#topsort li.full_row a,#sub_nav ul li a.category_nav_remote_link').removeClass("selected");$(this).addClass('selected');nohash=$(this).attr('href').replace('#','/');GUCCI.Category.setCategoryNavHref(nohash);return false;}});$('#panel_wrapper .shopthelook_button').css({left:(GUCCI.Category.singlewidth-$('.shopthelook_button').width())/2});$('#panel_wrapper .look').click(function()
{var thislook=$(this);var base="";var panel=thislook.parents('.ggpanel');var lookurl=thislook.find('.look_url').attr('href');var lookid=$(this).attr("id");var lookid_only=lookid.slice(2,6);if(lookid_only){$.ajax({type:'get',dataType:'json',complete:function(request){$('#look_'+lookid_only).html(request.responseText);},url:'/looks/'+lookid_only+'/lovers'});}
GUCCI.Category.updateLookUrl([lookid]);var lookForAnalytics=lookid.split('k')[1];if(typeof GUCCI.Analytics!="undefined")
GUCCI.Analytics.rtwView(lookForAnalytics);if(panel.find('.look_detail').html()=="")
{if(!GUCCI.isIE)
{thislook.parent().find('.load_look').fadeIn();}
else
{thislook.parent().find('.load_look').show();}
thislook.find('.look_detail').load(lookurl,function(response,status,xhr)
{if(!GUCCI.isIE)
{thislook.parent().find('.load_look').fadeOut();}
else
{thislook.parent().find('.load_look').hide();}
if(xhr.status==200)
{panelOpen(panel);assignClose(panel);GUCCI.Category.initStyleActions();}});}
else
{if(thislook.find('.look_detail').css('display')=='none')
{panelOpen(panel);assignClose(panel);}}});var assignClose=function(panel)
{panel.find('.look_detail_close').click(function()
{var panelindex=parseInt(panel.find('.panel_index').html(),10);var sanstriptych=$('.ggpanel').length-4;var finaltrio=false;var overlay=$(this).parent();overlay.css({"z-index":'0'});if(panelindex>=sanstriptych)
{returnToBase(panel);}
if(!GUCCI.isIE)
{overlay.fadeOut('slow',hidedetails);}
else
{overlay.hide(1,hidedetails);}
overlay.parents('.ggpanel').animate({width:GUCCI.Category.singlewidth},GUCCI.Category.panel.pace);});};var panelOpen=function(panel)
{GUCCI.Category.panelwidthdata[parseInt(panel.find('.panel_index').html(),10)]=3;GUCCI.Category.resizeScrollbar();panel.animate({width:GUCCI.Category.triptychwidth},GUCCI.Category.panel.pace,showdetails);};var goToBase=function(panel,base)
{var target=panel.offset().left-GUCCI.Category.panel[base];$('#scrollbar .handle').goldscrollcontainer(target);};var returnToBase=function(panel)
{var panelindex=parseInt(panel.find('.panel_index').html(),10);var iseven=$('.ggpanel').length%2;var sanstriptych=$('.ggpanel').length-4;if(panelindex>sanstriptych&&panel.offset().left<=GUCCI.Category.panel.first)
{if(iseven!==0)
{panelindex=panelindex+1;}
else
{panelindex=panelindex+2;}
var tychindex=panelindex%3;switch(tychindex)
{case 0:goToBase(panel,"first");break;case 1:goToBase(panel,"second");break;case 2:goToBase(panel,"third");break;}}
else if(panelindex==sanstriptych&&panel.offset().left<(GUCCI.Category.panel.first-GUCCI.Category.singlewidth))
{$('#scrollbar .handle').goldscrollcontainer(-1*GUCCI.Category.panelwidth);}};var showdetails=function()
{var panel=$(this);var overlay=$(this).find('.look_detail');var shareOverlay;var shareApi;var myLookid=panel.find('.look').attr("id").split(GUCCI.Category.position.prefix)[1];if(typeof $.fn.tipTip=='function')
{panel.find('.share').tipTip({maxWidth:'150px',content:$('.icon.share').find('.bg').html(),defaultPosition:'top'});panel.find('.print').tipTip({maxWidth:'150px',content:$('.icon.print').find('.bg').html(),defaultPosition:'top'});}
if($.fn.love)
{if(typeof $.fn.tipTip=='function'&&!GUCCI.isMobile)
{overlay.find('.icon.loveit').tipTip({maxWidth:'150px',content:overlay.find('.icon.loveit').find('.mylove').html(),defaultPosition:'top'});}
overlay.find('.loveit .love-button').love({caller:overlay});}
if($.browser.msie&&$.browser.version.substr(0,1)<7)
{$('.look_icons ul li').click(function()
{if($(this).attr("id")=="print")
{$(this).find("a").attr('target','_blank');var url=$(this).find("a").attr("href");window.open(url);return false;}
$(this).children('a').trigger('click');});}
if($.browser.msie&&$.browser.version.substr(0,1)<7)
{$('.look_detail .loveit .love-button').click(function(){$(this).children('a').trigger('click');});}
GUCCI.Category.personalShopper(overlay);GUCCI.Category.shareDialog(overlay,myLookid);GUCCI.Category.initZoomBindings(overlay);GUCCI.Category.initZoom(overlay);overlay.css({left:"0"});if(!GUCCI.isIE)
{overlay.fadeIn('slow',function()
{var stylelist=$(this).find(".style_list");if(stylelist.length>0)stylelist.jScrollPane({scrollbarWidth:9});var look_button=overlay.find('.zoomlook_button');look_button.css({top:'370px'});look_button.css({left:GUCCI.Category.modelcenterline-look_button.width()/2});goToBase($(this).parent().parent().parent().parent(),"first");});}
else
{overlay.show(1,function()
{var stylelist=$(this).find(".style_list");if(stylelist.length>0)stylelist.jScrollPane({scrollbarWidth:7});var look_button=overlay.find('.zoomlook_button');look_button.css({top:'370px'});look_button.css({left:'429px'});look_button.css({width:'155px'});goToBase($(this).parent().parent().parent().parent(),"first");});}};var they_love_me=function(lookoverlay)
{if(typeof $.fn.tipTip=='function'&&!GUCCI.isMobile)
{lookoverlay.find('.icon.loveit').tipTip({maxWidth:'150px',content:lookoverlay.find('.ilovedit').html(),defaultPosition:'top'});}};var hidedetails=function()
{var panel=$(this).parents('.ggpanel');var lookid=panel.find('.look').attr("id");GUCCI.Category.updateLookUrl([lookid,GUCCI.Category.position.prefix+"A113"]);GUCCI.Category.panelwidthdata[parseInt(panel.find('.panel_index').html(),10)]=1;GUCCI.Category.resizeScrollbar();};var showstl=function()
{var stlbutton=$(this).find('.shopthelook_button');if(GUCCI.isIE6)stlbutton.show();else stlbutton.stop();stlbutton.fadeIn('fast');};var hidestl=function()
{var stlbutton=$(this).find('.shopthelook_button');if(GUCCI.isIE6)stlbutton.hide();else stlbutton.stop();stlbutton.css({"opacity":"1"});stlbutton.fadeOut('fast');};var hoveronstl=function(){$(this).doTimeout('shopthelook_button',200,showstl);};var hoveroffstl=function(){$(this).doTimeout('shopthelook_button',200,hidestl);};if(!GUCCI.isMobile){var path_match=location.pathname.split('/');var site_abbr=path_match[1];GUCCI.Category.asianset=site_abbr=="jp";GUCCI.Category.initQuickViews();$('#panel_wrapper .look').hover(function()
{$(this).doTimeout('shopthelook_button',200,showstl);},function()
{$(this).doTimeout('shopthelook_button',0,hidestl);});$('.wg_panel').hover(function()
{var $this=$(this);var title=$this.find('div.container_title');if(typeof title=='undefined')return;var text=$this.find('div.blurb').children().not('h6');if(typeof text=='undefined')return;$this.find('body.category ul.wg_panel li').addClass('active');title.stop(true,true).addClass('active',GUCCI.Category.wg.title_pace);text.stop(true,true).delay(GUCCI.Category.wg.title_pace*GUCCI.Category.wg.fudge_factor).fadeIn(GUCCI.Category.wg.title_pace*GUCCI.Category.wg.fudge_factor);},function()
{var $this=$(this);var title=$this.find('div.container_title');if(typeof title=='undefined')return;var text=$this.find('div.blurb').children().not('h6');if(typeof text=='undefined')return;text.stop(true,true).fadeOut(GUCCI.Category.wg.title_pace*GUCCI.Category.wg.fudge_factor);$this.find('body.category ul.wg_panel li').removeClass('active');title.stop(true,true).delay(GUCCI.Category.wg.title_pace*GUCCI.Category.wg.fudge_factor).removeClass('active',GUCCI.Category.wg.title_pace*GUCCI.Category.wg.fudge_factor);});}
else
GUCCI.Category.initMobileGestures();$('#sub_nav ul#topsort li.full_row a').addClass('selected');if(window.location.href.indexOf('#')>-1)
{var nav_ref_url=GUCCI.Category.getCategoryNavHref(window.location.href);$("section#sub_nav ul#topsort li a[href='"+nav_ref_url+"']").trigger('click');}
var goToLook=function(look,openlook)
{var nextid=look.indexOf(GUCCI.Category.position.prefix,3);var lookid=0;if(nextid>3)
lookid=look.slice(2,nextid);else
lookid=look.slice(2);var panel=$('#panel_wrapper .ggpanel').filter("."+lookid);var panelindex=(panel.find('.panel_index').length>0)?parseInt(panel.find('.panel_index').html(),10):-1;var panelindexes=$('.ggpanel').length-1;GUCCI.Category.basesLoaded();if(openlook&&panelindex>=0)
{if(panelindex>1&&panelindex<panelindexes-1)
goToBase(panel,'first');else if(panelindex>=panelindexes-1)
goToBase($('#panel_wrapper .ggpanel').eq(-2),'second');panel.find('.look').click();}
else
{if(panelindex>=0)
{if(panelindex>=panelindexes-2)
goToBase($('.ggpanel').eq(-2),'first');else
goToBase(panel,'first');}}};var categoryurl=document.location.href;categoryurl=categoryurl.replace(/look/g,'lk');var hashvalue=categoryurl.split('/').pop().split('#')[1];var lookid="";var panel="";var panelindex=0;var navlinks=$('#sub_nav .category_nav_remote_link');if(hashvalue!=undefined)
{if(hashvalue.indexOf(GUCCI.Category.position.prefix)>-1)
{var opentoggle=(hashvalue.indexOf(GUCCI.Category.position.prefix)==hashvalue.lastIndexOf(GUCCI.Category.position.prefix));goToLook(hashvalue,opentoggle);}
else
{var sort=new Array();for(var n=0;n<navlinks.length;n++)
{sort=navlinks.eq(n).attr('href').split('/');if(sort[sort.length-1]==hashvalue)navlinks.eq(n).click();}}}
if(navlinks.length>1)
{var hashedurl="";var nohash="";var withhash="";for(var x=0;x<navlinks.length;x++)
{hashedurl=navlinks.eq(x).attr('href');nohash=hashedurl.substr(hashedurl.lastIndexOf('/'));withhash=nohash.replace('/','#');navlinks.eq(x).attr('href',hashedurl.replace(nohash,withhash));}}
if(typeof Cufon=='function'&&!GUCCI.isMobile)
Cufon.replace('h4, body.category ul.hero_panel p a, body.category ul.oneup_panel p a, body.category .wg_panel div.blurb h6, body.category .wg_panel div.blurb a, body.category .news_panel h6');});GUCCI.Checkout={animation:{error_overlay:1000,gifting_overlay:500,overlay_csc_show:300,overlay_csc_hide:300},signUpForgotPasswordDetails:"",cufonInitialize:function(){if(typeof Cufon=='function'){Cufon.replace('h2,h3,h4,a.send_btn, #order_lines h5, h5.overlay span, h6.cufon, .h7, h3.cufon');}},toggleActiveClass:function(link,currObject,checkoutToggle){if($(currObject+' .is_active').length>0){$(link).removeClass("is_active");}else{$(link).addClass("is_active");}
$(checkoutToggle).toggle();},estimateCloseBind:function(hideObj,link,e){$(hideObj).hide();$(link).removeClass("is_active");if($(hideObj).css('display')=='none'&&(e!=null)){e.stopPropagation();}},showOverlayCSC:function(){var link=$('#overlay_csc_link');var overlay=$('#overlay_csc');if(link.length==0||overlay.length==0)return;overlay.fadeIn(GUCCI.Checkout.animation.overlay_csc_show);link.addClass('is_active',GUCCI.Checkout.animation.overlay_csc_show);},hideOverlayCSC:function(){var link=$('#overlay_csc_link');var overlay=$('#overlay_csc');if(link.length==0||overlay.length==0)return;link.removeClass('is_active');overlay.fadeOut(GUCCI.Checkout.animation.overlay_csc_hide);},initPasswordOverlay:function(){if($('#passoverlay_link').length==0)return;var api=$('#passoverlay_link').data('overlay');if(!api)return;var overlay=api.getOverlay();if(typeof Cufon=='function')
Cufon.replace(overlay.find('h3'));overlay.find('.close').click(function(){api.close();return false;});},initAssistanceOverlay:function(link,section){$(link).goldoverlay({target:$(link).attr("rel"),onLoad:function(){var overlay=$(this.target);overlay.find('.scroll_content').jScrollPane({scrollbarWidth:8,animateTo:true});Cufon.replace(overlay.find('h3'));var $scrolling=overlay.find('.assistance_content');var scrollingTop=parseInt($scrolling.offset().top);overlay.find('.assistance_content h1').each(function(index){var h1Text=$.trim($(this).text());var h1Pos=$(this).offset().top;overlay.find('.assistance_content div.nav_col a').each(function(){if($(this).text()==h1Text){$(this).attr({'href':'javascript:;','rel':h1Pos}).bind('click',function(){$scrolling[0].scrollTo(parseInt($(this).attr('rel'))-scrollingTop);})}});});if(overlay.attr("id")=="assistance_overlay")
{var assisLinkPos=$("li"+section).find("a").attr("rel");$scrolling[0].scrollTo(parseInt(assisLinkPos)-scrollingTop);}
overlay.find(' .language_content ').each(function(i){var languageDisplay=$(this).attr("id");var langPos=$(this).offset().top;overlay.find('.title a').each(function(){if($(this).attr("class")==languageDisplay){$(this).attr({'href':'javascript:;','rel':langPos}).bind('click',function()
{$scrolling[0].scrollTo(parseInt($(this).attr('rel'))-scrollingTop);})}});});},onClose:function(){var overlay=$(this.target);var $scrolling=overlay.find('.assistance_content');var scrollingTop=parseInt($scrolling.offset().top);if(overlay.attr("id")=="assistance_overlay")
{$scrolling[0].scrollTo(0);}
else
{$scrolling[0].scrollTo(0);}
if($("body.review").length>0)
{$("input#order_collection_terms").attr('checked',true);}}});}};$(document).ready(function(){if($('body.carts').length==0)return;$('#passoverlay_link').goldoverlay({onBeforeLoad:function(){GUCCI.Checkout.signUpForgotPasswordDetails=$("#forgot_password_content").html();GUCCI.Checkout.initPasswordOverlay();},onClose:function(){setTimeout(function(){$('#forgot_password_content').html(GUCCI.Checkout.signUpForgotPasswordDetails);},500);}});GUCCI.Checkout.initAssistanceOverlay("#authentic_link",".guarantee");GUCCI.Checkout.initAssistanceOverlay("#shipping_link",".shipping");GUCCI.Checkout.initAssistanceOverlay("#gift_link",".gift_wrap");GUCCI.Checkout.initAssistanceOverlay("#privacy_link","");GUCCI.Checkout.initAssistanceOverlay("#assistance_link","");var items=$("#main_content .items");if(items.length>0)
items.jScrollPane({scrollbarWidth:11});var thanks_items=$("#main_content .included_items");if(thanks_items.length>0){thanks_items.jScrollPane({scrollbarWidth:11});}
$('a#proceed_to_delivery span').click(function(){$('#bill_ship_to').submit();return false;});$('a#continue_to_review span').click(function(){$('#new_payment').submit();return false;});if($("#order_collection_shipper_type_id").length>0){$('#order_collection_shipper_type_id').change(function(){$(".edit_order_collection").submit();});}
if($('#new_payment').length>0){$('#new_payment input:radio').click(function(){value=$(this).val();if(value==6||value==7){$("#extras").show();}else{$("#extras").hide();}});}
if($('#order_gift_false').length>0){$('#order_gift_false').click(function(){$('#gift_form .fake').attr('checked','');if($('#order_gift_from').val().length>0||$('#order_gift_message').val().length>0){var overlay=$('#gift_form .overlay_warning');overlay.addClass('active',GUCCI.Checkout.animation.gifting_overlay);}
else{$("#gift_form").hide();}});$('#order_gift_true').click(function(){$('#gift_form .fake').attr('checked','checked');var overlay=$('#gift_form .overlay_warning');overlay.removeClass('active');$("#gift_form").show();});$('#gift_form .option.ok').click(function(){$('#gift_form .overlay_warning').removeClass('active');$('#order_gift_from').val('');$('#order_gift_message').val('');$('#gift_form').hide();});$('#gift_form .option.cancel').click(function(){$('#order_gift_false').attr('checked','');$('#order_gift_true').attr('checked','checked');$('#gift_form .fake').attr('checked','checked');$('#gift_form .overlay_warning').removeClass('active',GUCCI.Checkout.animation.gifting_overlay/2);});};$('.estimate').click(function(){GUCCI.Checkout.toggleActiveClass(".estimate","#estimate_tax","#tax_estimator");});$('.edit_shipping').click(function(){GUCCI.Checkout.toggleActiveClass(".edit_shipping","#edit_shipping","#shipping_type_select");});$("body.carts, #tax_estimator img, #shipping_type_select img ").click(function(){if($('#tax_estimator').css('display')=='block'){GUCCI.Checkout.estimateCloseBind("#tax_estimator",".estimate");}
if($('#shipping_type_select').css('display')=='block'){GUCCI.Checkout.estimateCloseBind("#shipping_type_select",".edit_shipping");}});$("#tax_estimator, #estimate_tax").bind("click",function(e){GUCCI.Checkout.estimateCloseBind("#shipping_type_select",".edit_shipping",e);});$("#edit_shipping, #shipping_type_select").bind("click",function(e){GUCCI.Checkout.estimateCloseBind("#tax_estimator",".estimate",e);});if($("#order_collection_shipper_type_id").length>0){$("#order_collection_shipper_type_id").parent().submit(function(){return false;});var targeturl='cart/set_shipper_type';if($('body.delivery').length>0)
targeturl='../cart/set_shipper_type';$('#order_collection_shipper_type_id').change(function(){$.ajax({type:"POST",url:targeturl,data:{"order_collection[shipper_type_id]":$("#order_collection_shipper_type_id").val()},success:function(data){$('#shipper_type_name,#totals td.shipping_name').html(data.shipping_name);$('#est_shipping,#totals td.shipping_price').html(data.shipping_price);$('#est_tax,#totals td.tax').html(data.tax);$('#order_total,#totals td.total').html(data.total);GUCCI.Checkout.toggleActiveClass(".edit_shipping","#edit_shipping","#shipping_type_select");}});});}
if($("#update_tax").length>0){$("#update_tax").parent().parent().submit(function(){return false;});$('#update_tax').click(function(){$.ajax({type:"POST",url:'cart/estimate',data:{"web_user[postal_code]":$("#web_user_postal_code").val()},success:function(data){if(data.shipping_name!=""){$('#shipper_type_name,#totals td.shipping_name').html(data.shipping_name);$('#est_shipping,#totals td.shipping_price').html(data.shipping_price);}
$('#est_tax').html(data.tax);$('#order_total').html(data.total);GUCCI.Checkout.toggleActiveClass(".estimate","#estimate_tax","#tax_estimator");}});});}
var csclink=$('#overlay_csc_link');if(csclink.length>0){csclink.click(function(){var $this=$(this);var active=$this.hasClass('is_active');var overlay=$('#overlay_csc');if(!active)
GUCCI.Checkout.showOverlayCSC();else
GUCCI.Checkout.hideOverlayCSC();});$('#overlay_csc a.close').click(GUCCI.Checkout.hideOverlayCSC);$('#main_content').click(function(e){if($('#overlay_csc_link').hasClass('is_active')&&$(e.target).parents('#overlay_csc').length==0)
GUCCI.Checkout.hideOverlayCSC();});}
if($('body.carts.complete').length>0){$('#main_content div.share > a').each(function(){var $this=$(this);$this.goldoverlay({target:$this.attr('rel'),onBeforeLoad:function(){var api=$this.data('overlay');var overlay=api.getOverlay();overlay.find('.close').click(function(){api.close();return false;});$("#overlay_screen").addClass("overlayScreenFix");},onLoad:function(){if(typeof Cufon=='function')
Cufon.replace('.share_overlay h3');var api=$this.data('overlay');var overlay=api.getOverlay();var emailLink=overlay.find('.email_friend_link');var targetOverlay=overlay;var styleid=$this.attr('rel').replace('#share_dialog_','');GUCCI.emailFriend(emailLink,targetOverlay,styleid,"style");$('div.share_overlay  a.cancel_overlay, div.share_overlay .close').click(function()
{$("div.share_overlay").remove();$("#overlay_screen").removeClass("overlayScreenFix");});}});});}
$('.checkouts #web_user_session_submit, #forgot_password_submit').attr({'value':''});$('a#sign_and_checkout').click(function(){$(this).parent('form').submit();});if($('#ship_to_address').length>0)
$('#ship_to_address').click(function(){if(this.checked&&$('#ship_to_not_billing').css('display')=='none');$('#ship_to_not_billing').fadeIn('fast');});if($('#bill_to_address').length>0)
$('#bill_to_address').click(function(){if(this.checked&&$('#ship_to_not_billing').css('display')=='block');$('#ship_to_not_billing').fadeOut('fast');});if($('#ship_to_address:checked').length>0)
$('#ship_to_not_billing').fadeIn('fast');if($('#update_gift_information').length>0){var btn=$('#update_gift_information');var inp=$("#order_gift_message");var frm=$(".edit_order");btn.click(function(){var length=inp.val().length;if(length>200||length<0){return false;}else{frm.submit();};});}
if($("#create_account").length>0){var btn=$("#create_account");btn.click(function(){$('#web_user_form').submit();});}
if($(".edit_order_collection").length>0){$("#continue").click(function(){$(".edit_order_collection").submit();});}
if($("#order_gift_from").length>0){$("#order_gift_from").textlimit('span.from_counter',30);}
if($("#order_gift_message").length>0){$("#order_gift_message").textlimit('span.counter',200);}
var reviewcheck=$('#main_content .container_orderdetails ul li.checksubmit span.acceptterms span.field_with_errors');if(reviewcheck.length>0)
reviewcheck.parent('').find('span').addClass('field_with_errors');var errorbox=$('#main_content .error');if(errorbox.length>0){$.doTimeout('errorbox',GUCCI.Checkout.animation.error_overlay/2,function(){errorbox.fadeIn(GUCCI.Checkout.animation.error_overlay);});}});GUCCI.Product={animation:{add_error:250,accordion_headerbg:400,accordion_content:400,icons_mouseover:200,icons_tooltip:300,variation_mouseover:200,variation_tooltip:300,styleguide:400,shoppingbutton_processingstart:100,shoppingbutton_processingend:500,variation_fadeout:500,variation_fadein:500},variationsProcessing:false,shoppingBagProcessing:false,skuDetails:[],selectSizeMessage:'',currentSiteAbbreviation:'',zoom:{initial_zoom:false,dragged:false,last_x:0,last_y:0,fudge_factor:0,baseline:360},cufonInitialize:function()
{if(typeof Cufon=='function')
Cufon.replace('h2,h3,h4, #container_style_price #price, body.styles span.style.id, body.styles #container_wog p, body.styles a.addbag_btn, body.styles #size-guide h3');},pixelTruncation:function()
{if(typeof $.fn.pixelinitalphabet=='function')
{var samplefonts=jQuery('<div/>',{id:'samplefonts'});$('#bg_content').append(samplefonts);var title=$('#product_card h2');title.pixeltruncate({pixellimit:title.width(),checkcontainer:samplefonts,numlines:3});samplefonts.remove();}},pngFixes:function()
{if($.browser.msie&&$.browser.version.substr(0,1)<7)
{if(typeof $.fn.supersleight=='function')
$('body.styles #product_cardreflect, body.styles .accordion h5, body.styles .accordion span.bg, body.styles .jScrollPaneTrack, body.styles #container_variations ul.items li div.bg, body.styles #container_icons_personalshopper ul li').supersleight({shim:'/images/ecommerce/transparent.gif'});$('body.styles .accordion h5').not('h5.active').children('span.bg').css('display','none');$('#container_icons_personalshoppers_personalshopper ul li div.bg').css('display','none');}},initAccordions:function()
{if(typeof $.fn.accordion=='function')
{$('#accordion_left,#accordion_right').accordion({autoHeight:false,collapsible:true});$('#accordion_left,#accordion_right').bind('accordionchange',function(event,ui)
{ui.newHeader.addClass('active');if($.browser.msie&&$.browser.version.substr(0,1)<7)
ui.newHeader.children('span.bg').css('display','block');ui.oldHeader.removeClass('active');if($.browser.msie&&$.browser.version.substr(0,1)<7)
ui.oldHeader.children('span.bg').css('display','none');var newcontent=ui.newContent.children('div.accordion_content');var oldcontent=ui.oldContent.children('div.accordion_content');if(newcontent.length>0)
{newcontent.addClass('active');if(newcontent.hasClass('last'))
{var bordercontainer;if($(this).attr('id')=='accordion_left')
bordercontainer=$('body.styles #container_other_detail');else
bordercontainer=$('body.styles #container_wog');bordercontainer.addClass('active',GUCCI.Product.animation.accordion_content);}}
if(oldcontent.length>0)
oldcontent.removeClass('active');});$('#accordion_left,#accordion_right').bind('accordionchangestart',function(event,ui)
{if(!$.browser.msie)
{ui.newHeader.children('span.bg').fadeIn(GUCCI.Product.animation.accordion_headerbg);ui.oldHeader.children('span.bg').fadeOut(GUCCI.Product.animation.accordion_headerbg);}
var newcontent=ui.newContent.children();var oldcontent=ui.oldContent.children();if($.browser.msie)
{newcontent.not('.jScrollPaneTrack,.jScrollCap').fadeIn(GUCCI.Product.animation.accordion_content);oldcontent.not('.jScrollPaneTrack,.jScrollCap').fadeOut(GUCCI.Product.animation.accordion_content);}
else
{newcontent.fadeIn(GUCCI.Product.animation.accordion_content);oldcontent.fadeOut(GUCCI.Product.animation.accordion_content);}
var lastcontent=ui.oldContent.children('div.accordion_content.last');if(lastcontent.length>0)
{var bordercontainer;if($(this).attr('id')=='accordion_left')
bordercontainer=$('body.styles #container_other_detail');else
bordercontainer=$('body.styles #container_wog');bordercontainer.removeClass('active',GUCCI.Product.animation.accordion_content);}});$('body.styles #container_other_detail').addClass('active');}},initScrollpanes:function()
{if(typeof $.fn.jScrollPane=='function')
{$('#accordion_left div.accordion_content').jScrollPane({scrollbarWidth:11});var header=$('#product_card h2');if(parseInt(header.css('height'),10)<header[0].scrollHeight-12)
header.jScrollPane({scrollbarWidth:11});}},initTools:function()
{if(!$.browser.msie)
{$('#container_icons_personalshopper .icon').hover(function()
{$(this).children('div.bg').stop(true,true).fadeIn(GUCCI.Product.icons_mouseover);},function()
{$(this).children('div.bg').stop(true,true).fadeOut(GUCCI.Product.icons_mouseover);});}
else
{$('#container_icons_personalshopper .icon').hover(function()
{if($.browser.version.substr(0,1)<7)
{$this=$(this);var currentstyle=$this.attr('style');$this.attr('style',currentstyle.replace(/(womens|mens)/i,'active$1'));}
else
$(this).children('div.bg').css('display','block');},function(){if($.browser.version.substr(0,1)<7)
{$this=$(this);var currentstyle=$this.attr('style');$this.attr('style',currentstyle.replace(/active(womens|mens)/i,'$1'));}
else
$(this).children('div.bg').css('display','none');});}
if(!GUCCI.isMobile)
{if(typeof $.fn.tipTip=='function')
{$('#container_icons_personalshopper #share, #container_icons_personalshopper #print').tipTip({maxWidth:'150px',defaultPosition:'top'});}}
if($.fn.love)
{if(typeof $.fn.tipTip=='function'&&!GUCCI.isMobile)
{$('#container_icons_personalshopper #loveit').tipTip({maxWidth:'150px',content:$('#container_icons_personalshopper #loveit .mylove').text(),defaultPosition:'top'});}
$('#container_icons_personalshopper #loveit .love-button').love({caller:$('#container_icons_personalshopper')});}
if($.browser.msie&&$.browser.version.substr(0,1)<7)
{$('#container_icons_personalshopper ul li').click(function()
{if($(this).attr("id")=="print")
{$(this).find("a").attr('target','_blank');var url=$(this).find("a").attr("href");window.open(url);return false;}
$(this).children('a').trigger('click');});}
var styleid=window.location.href.split('/')[5];GUCCI.Product.shareDialog(styleid);},checkForPhoto:function()
{return $('#imagery_main div').hasClass('no_photo');},maybeAlternateButton:function(element)
{return $('#imagery_main div').hasClass('no_photo')&&element.attr('id')=='zoom_alternate';},initZoomBindings:function()
{if(GUCCI.Product.checkForPhoto())return;var show_zoomout=function(view)
{var view_zoomout=view.html().replace(/web_minithumb/g,'web_zoomout');$('.zoom_out').html(view_zoomout);$('#zoom_in_window').hide();$('.zoom_out').show();};var toggle_right_column=function(tgl)
{if(tgl=='hide')
{$('#accordion_right').hide();$('#container_icons_personalshopper').hide();}
else if(tgl=='show')
{$('#accordion_right').show();$('#container_icons_personalshopper').show();}};$('#zoom_overlay').mousemove(function()
{if(!GUCCI.Product.zoom.initial_zoom)
{GUCCI.Product.resizeZoom();GUCCI.Product.zoom.initial_zoom=true;}});$('#zoom_close').click(function()
{$('#zoom_overlay').hide();toggle_right_column('show');});$('#zoom_alternate').click(function()
{var vz=$('.view.main');toggle_right_column('hide');show_zoomout(vz);vz.click();$('#zoom_overlay').show();});var select_alternate_view=function(zoom_view)
{var alternate_views=$('#zoom_tools li');alternate_views.removeClass('active_view');zoom_view.removeClass('inactive_view');zoom_view.addClass('active_view');};$('.view').click(function()
{if($(this).hasClass('active_view'))return;var view_zoomout="";var view_zoomin="";if($(this).hasClass('look'))
{view_zoomout=$(this).html().replace(/web_look_minithumb/g,'web_look_zoomout');view_zoomin=view_zoomout.replace(/web_look_zoomout/g,'web_look_zoomin');}
else
{view_zoomout=$(this).html().replace(/web_minithumb/g,'web_zoomout');view_zoomin=view_zoomout.replace(/web_zoomout/g,'web_zoomin');}
GUCCI.Product.resizeZoom();GUCCI.Product.zoom.dragged=false;$('#zoom_tools li').removeClass('active_view');$('#zoom_tools li').addClass('inactive_view');$(this).removeClass('inactive_view');$(this).addClass('active_view');$('.zoom_in').hide();$('.zoom_out').html(view_zoomout);$('.zoom_out').show();$('#zoom_in_window').hide();$('.zoom_in').html(view_zoomin);});$('.zoom_out').click(function(evn)
{var zoom_left=Math.round($('#zoom_view').offset().left);var zoom_top=Math.round($('#zoom_view').offset().top);var mousex=evn.clientX-zoom_left;var mousey=evn.clientY-zoom_top;var zoom_width=600;var zoom_height=443;var actual_offset_x=(2400-1480)/2;var actual_offset_y=(1772-1456)/2;var viewable_width=1414-zoom_width;var viewable_height=1456-zoom_height;if((mousex<155||mousex>445)||(mousey>408))
{mousex=300;mousey=GUCCI.Product.zoom.baseline;}
var target_x=(3*mousex)-actual_offset_x;var target_y=(3*mousey);$('.zoom_in').css({'left':0-target_x});$('.zoom_in').css({'top':0-target_y});animateZoom(mousex,mousey,'zoom_in');});var toggle_zoom_windows=function(zoom_window)
{if(zoom_window=='zoom_in')
{$('.zoom_out').hide();$('.zoom_in').show();$('#zoom_in_window').show();}
else
{$('.zoom_out').show();$('.zoom_in').hide();$('#zoom_in_window').hide();}};var animateZoom=function(x,y,direction)
{var zoom_width=0;var zoom_height=0;var zoom_direction=0;if(direction=='zoom_in')
{zoom_width=2400;zoom_height=1772;zoom_direction=-1;$('.zoom_out img').animate({left:zoom_direction*(3*x)-GUCCI.Product.zoom.fudge_factor,top:zoom_direction*(3*y)-GUCCI.Product.zoom.fudge_factor,height:zoom_height,width:zoom_width},750,function(){toggle_zoom_windows(direction);});}
else
{toggle_zoom_windows(direction);zoom_width=600;zoom_height=443;zoom_direction=1;$('.zoom_out img').animate({left:0,top:0,height:zoom_height,width:zoom_width},750,function(){GUCCI.Product.zoom.dragged=false;});}};$('#zoom_reset').click(function(evn)
{var zoom_left=Math.round($('#zoom_view').offset().left);var zoom_top=Math.round($('#zoom_view').offset().top);var mousex=evn.clientX-zoom_left;var mousey=evn.clientY-zoom_top;if(GUCCI.Product.zoom.dragged)
{$('.zoom_out img').css({left:GUCCI.Product.zoom.last_x});$('.zoom_out img').css({top:GUCCI.Product.zoom.last_y});}
animateZoom(mousex,mousey,'zoom_out');});},initZoom:function()
{if(GUCCI.Product.checkForPhoto())return;$('#zoom_alternate').css({display:'block',left:(430-$('#zoom_alternate').width())/2+293});if(!GUCCI.isMobile)
{$('.zoom_in').draggable({start:function(evn,ui)
{if($.browser.mozilla)
$(this).css('cursor','-moz-grabbing');if($.browser.msie)
$(this).css('cursor','url(../../images/ecommerce/grabbing.cur)');},stop:function(evn,ui)
{if($.browser.mozilla)
$(this).css('cursor','-moz-grab');if($.browser.msie)
$(this).css('cursor','url(../../images/ecommerce/grab.cur)');var zoom_left=Math.round($('#zoom_view').offset().left);var zoom_top=Math.round($('#zoom_view').offset().top);var mousex=evn.clientX-zoom_left;var mousey=evn.clientY-zoom_top;GUCCI.Product.zoom.last_x=$('.zoom_in').position().left-460;GUCCI.Product.zoom.last_y=$('.zoom_in').position().top;GUCCI.Product.zoom.dragged=true;}});}
else
{if(typeof $.fn.goldswipe=='function')
{var movefunction=function(){var zoomer=$('.zoom_in img');var newleft=(zoomer.position().left-$.fn.goldswipe.vars.change.x)+'px';var newtop=(zoomer.position().top-$.fn.goldswipe.vars.change.y)+'px';zoomer.animate({'left':newleft,'top':newtop},500);};$('.zoom_in').goldswipe({thresholdX:50,thresholdY:50,swipeLeft:movefunction,swipeRight:movefunction,swipeUp:movefunction,swipeDown:movefunction});}}
GUCCI.Product.resizeZoom();},resizeZoom:function()
{if($('body.styles').length==0)return;var zoom_left=Math.round($('#zoom_view').offset().left);var zoom_top=Math.round($('#zoom_view').offset().top);var zoom_width=600;var zoom_height=443;var viewable_x_offset=33;var viewable_y_offset=88;var viewable_width=1414-zoom_width;var viewable_height=1456-zoom_height;var x1=(zoom_left-viewable_x_offset)-viewable_width;var y1=(zoom_top-viewable_y_offset)-viewable_height;var x2=zoom_left-viewable_x_offset;var y2=zoom_top-viewable_y_offset;$('.zoom_in').draggable('option','containment',[x1,y1,x2,y2]);},initVariations:function()
{if(typeof $.fn.scrollable=="function")
{$('#container_variations div.scrollable').scrollable({size:3,keyboard:false,clickable:false,onSeek:function(event,index)
{if(this.getIndex()>=(this.getSize()-3))
{this.getNaviButtons().filter('a.next').addClass('disabled');}}});$('#container_variations a.next').unbind();$('#container_variations a.next').click(function()
{var api=$('#container_variations div.scrollable').data('scrollable');if(api.getIndex()<(api.getSize()-3)&&!$(this).hasClass('disabled'))
api.next();});}
if(!GUCCI.isMobile)
{if(typeof $.fn.tipTip=='function')
{$('#container_variations ul li').each(function(){var curritem=$(this);curritem.tipTip({maxWidth:'150px',defaultPosition:'top',enter:function(){if(curritem.hasClass('selected'))
$('#tiptip_holder').css('visibility','hidden');else
$('#tiptip_holder').css('visibility','visible');}});});}}
if(!GUCCI.isMobile)
{$('#container_variations ul li').hover(function()
{var $this=$(this);if(!$this.hasClass('selected'))
$this.children('div.bg').stop(true,true).fadeIn(GUCCI.Product.variation_mouseover);},function()
{var $this=$(this);if(!$this.hasClass('selected'))
$this.children('div.bg').stop(true,true).fadeOut(GUCCI.Product.variation_mouseover);});}
var varlinks=$('#container_variations ul.items li a');varlinks.each(function(i)
{var $this=$(this);var styleid=$this.attr('href').split('/').pop();if(styleid!='#')
{$this.attr('href','#'+styleid);$this.parents('li').one('click',GUCCI.Product.variationsSelect);}});},variationsSelect:function()
{var $this=$(this);var styleid=$this.children('a').attr('href').replace('#','');if(GUCCI.Product.variationsProcessing||$this.hasClass('selected'))
{$this.one('click',GUCCI.Product.variationsSelect);return;}
GUCCI.Product.variationsProcessing=true;var oldselected=$('#container_variations ul.items li.selected');if(!GUCCI.isMobile)
oldselected.removeClass('selected').one('click',GUCCI.Product.variationsSelect).children('.bg').hide();else
oldselected.removeClass('selected').one('click',GUCCI.Product.variationsSelect).children('.bg').hide();if(!GUCCI.isMobile)
{$this.addClass('selected').children('.bg').show();if(typeof $.fn.supersleight=='function'&&GUCCI.isIE6)
$('body.styles #container_variations ul.items li div.bg').supersleight({shim:'/images/ecommerce/transparent.gif'});}
else
$this.addClass('selected').children('.bg').show();GUCCI.Product.loadStyle(styleid);},loadStyle:function(styleid)
{var allchanges=$('#container_title_description,#container_other_detail');allchanges=allchanges.add($('#column_imagery').children());if(allchanges.length>0)
{if(!$.browser.msie||($.browser.msie&&$.browser.version>7))
allchanges.fadeOut(GUCCI.Product.animation.variation_fadeout);else
allchanges.css('display','none');$.get(GUCCI.makeUrl('/styles/'+styleid+'/load_style.js'),GUCCI.Product.finishLoadStyle(styleid));}
var origid=document.location.href.split('/').pop().split('#')[0];if(origid==styleid)
document.location.href=document.location.href.split('#')[0]+'#';else
document.location.href=document.location.href.split('#')[0]+'#'+styleid;return false;},finishLoadStyle:function(styleid)
{if($.browser.msie&&$.browser.version.substr(0,1)<7)
{$('body.styles .accordion h5, body.styles .accordion span.bg, body.styles .jScrollPaneTrack, body.styles #container_icons_personalshopper ul li').supersleight({shim:'/images/ecommerce/transparent.gif'});$('body.styles .accordion h5').not('h5.active').children('span.bg').css('display','none');}
GUCCI.Product.cufonInitialize();if(!$.browser.msie||($.browser.msie&&$.browser.version>7))
$('#container_title_description').fadeIn(GUCCI.Product.animation.variation_fadein);else
$('#container_title_description').css('display','block');GUCCI.Product.initScrollpanes();GUCCI.Product.initAccordions();GUCCI.back--;if(!$.browser.msie||($.browser.msie&&$.browser.version>7))
$('#container_other_detail').fadeIn(GUCCI.Product.animation.variation_fadein);else
$('#container_other_detail').css('display','block');var url="../styles/"+styleid+"/populate_dynamic_content";$.ajax({type:'GET',dataType:'script',url:url});if(!$.browser.msie||($.browser.msie&&$.browser.version>7))
$('#column_imagery').children().not(function(){return GUCCI.Product.maybeAlternateButton($(this));}).fadeIn(GUCCI.Product.animation.variation_fadein);else
$('#column_imagery').children().not(function(){return GUCCI.Product.maybeAlternateButton($(this));}).css('display','block');GUCCI.Product.initZoomBindings();GUCCI.Product.initZoom();GUCCI.Product.initTools();GUCCI.PersonalShopper.setupPersonalShopper();GUCCI.Product.variationsProcessing=false;if(typeof GUCCI.Analytics!="undefined")
GUCCI.Analytics.productView(styleid);},initSizeDropDownAvailability:function()
{if($('#style_dynamic_url').length==0)return;var url=$('#style_dynamic_url').attr('href');$.ajax({type:'GET',dataType:'script',url:url});$('#style_wrapper_dropdown').change(function()
{var selected=$('#style_wrapper_dropdown option:selected').attr('id');});var selected_on_page_load=$('#style_wrapper_dropdown option:selected').attr('id');GUCCI.Product.setupShoppingBagButton(GUCCI.Product.currentSiteAbbreviation);GUCCI.Product.update_availability(selected_on_page_load,$('#style_wrapper_dropdown'));},update_availability:function(selected)
{$('#container_availability p, #size_message').removeClass('error');if(selected=="")
$('#shopping_bag_button').attr('class','inactive_addbag_btn').attr('href','#');var updatesku=null;for(i=0;i<GUCCI.Product.skuDetails.length;i++)
{var currsku=GUCCI.Product.skuDetails[i];if(jQuery.trim(currsku.id)==selected)
updatesku=currsku;}
if(updatesku==null)return;GUCCI.Product.setupShoppingLinks(updatesku.shopping_links,GUCCI.Product.currentSiteAbbreviation);if(typeof Cufon=='function'&&!GUCCI.isMobile)
Cufon.replace('#shopping_bag_button');GUCCI.Product.dynamicAvailabilityMessage(updatesku);if(currsku.show_fee_link)
GUCCI.Product.setupShippingFeesLink();},dynamicAvailabilityMessage:function(sku)
{var html_inject=sku.message_partial;if(html_inject!=""){$('#container_availability').html(html_inject);}else{$('#container_availability').html('');}},setupShippingFeesLink:function()
{$('#shippingmethod_link').goldoverlay({target:$("#shippingmethod_link").attr("rel"),onLoad:function(){var overlay=$(this.target);overlay.fadeIn('slow',function()
{});overlay.find('.scroll_content').jScrollPane({scrollbarWidth:11,animateTo:true});Cufon.replace(overlay.find('h3'));var $scrolling=overlay.find('.assistance_content');var scrollingTop=parseInt($scrolling.offset().top);overlay.find('.assistance_content h1').each(function(index){var h1Text=$.trim($(this).text());var h1Pos=$(this).offset().top;overlay.find('.assistance_content div.nav_col a').each(function(){if($(this).text()==h1Text){$(this).attr({'href':'javascript:;','rel':h1Pos}).bind('click',function(){$scrolling[0].scrollTo(parseInt($(this).attr('rel'))-scrollingTop);})}});});var assisLinkPos=$("li.shipping").find("a").attr("rel");$scrolling[0].scrollTo(parseInt(assisLinkPos)-scrollingTop);overlay.find(' .language_content ').each(function(i){var languageDisplay=$(this).attr("id");var langPos=$(this).offset().top;overlay.find('.title a').each(function(){if($(this).attr("class")==languageDisplay){$(this).attr({'href':'javascript:;','rel':langPos}).bind('click',function()
{$scrolling[0].scrollTo(parseInt($(this).attr('rel'))-scrollingTop);})}});});},onClose:function(){var overlay=$(this.target);var $scrolling=overlay.find('.assistance_content');var scrollingTop=parseInt($scrolling.offset().top);var assisLinkPos=$("li.shipping").find("a").attr("rel");$scrolling[0].scrollTo(parseInt(assisLinkPos)-scrollingTop);}});},setupSizeGuideAnimation:function()
{$('#size_guidelink').goldoverlay({onLoad:function(){if(typeof Cufon=='function')
Cufon.replace('#size-guide h3');}});},setupShoppingLinks:function(html,current_site_abbreviation)
{$('#container_addtobag').html(html);$('#wishlist').click(function()
{$.post(this.href,function(data)
{$("#header_buyflow").load("/"+current_site_abbreviation+"/cart/get_minibag_header",function(){$.broadcast("header.buyflow.addtobag");});$("#wishlist").fadeOut(600);});return false;});GUCCI.Product.setupShoppingBagButton(current_site_abbreviation);},setupShoppingBagButton:function(site_abbr)
{$('#shopping_bag_button').click(function()
{if(GUCCI.Product.shoppingBagProcessing)return false;if($(this).hasClass('gold_inactive_addbag_btn')||$(this).hasClass('inactive_addbag_btn'))
{if(('#size_message').length>0)
$('#size_message').addClass('error',GUCCI.Product.animation.add_error);else
$('#container_availability p').addClass('error',GUCCI.Product.animation.add_error);return false;}
GUCCI.Product.addShoppingButtonProcessing();$.post(this.href,function(data)
{$("#header_buyflow").load("/"+site_abbr+"/cart/get_minibag_header",function(){$.broadcast("header.buyflow.addtobag");});GUCCI.Product.removeShoppingButtonProcessing();});return false;});},shareDialog:function(styleid)
{var api;var overlay;$('#share a.share').goldoverlay({onBeforeLoad:function(){api=$('#share a').data('overlay');overlay=api.getOverlay();overlay.find('.close').click(function(){api.close();return false;});$("#overlay_screen").addClass("overlayScreenFix");},onLoad:function(){if(typeof Cufon=='function')
Cufon.replace('.share_overlay h3');var emailLink=overlay.find('.email_friend_link');var targetOverlay=overlay;GUCCI.emailFriend(emailLink,targetOverlay,styleid,"style");$('div.share_overlay  a.cancel_overlay, div.share_overlay .close').click(function()
{$("div.share_overlay").remove();$("#overlay_screen").removeClass("overlayScreenFix");});}});},addShoppingButtonProcessing:function()
{var button=$('#shopping_bag_button');button.css('cursor','wait');GUCCI.Product.shoppingBagProcessing=true;button.addClass('processing_btn');button.stop(true,false).fadeTo(GUCCI.Product.animation.shoppingbutton_processingstart,0.4,function()
{});},removeShoppingButtonProcessing:function()
{var button=$('#shopping_bag_button');button.stop(true,false).fadeTo(GUCCI.Product.animation.shoppingbutton_processingend,1,function()
{button.removeClass('processing_btn');GUCCI.Product.shoppingBagProcessing=false;button.css('cursor','pointer');});},showBackButton:function()
{if(GUCCI.isIE6)
$('#back_to a').show();else
$('#back_to a').fadeIn('slow');},initBackButton:function()
{GUCCI.backCounter=-1;GUCCI.backLove=false;if(document.referrer.search(/gucci.com/)>0)
{GUCCI.Product.showBackButton();}
else if(GUCCI.isIE)
{var home=GUCCI.makeUrl("/home");$('#back_to a').attr('href',home);GUCCI.Product.showBackButton();}}};$(document).ready(function()
{if($('body.styles').length==0)return;GUCCI.Product.pngFixes();GUCCI.Product.initScrollpanes();GUCCI.Product.initAccordions();GUCCI.Product.initTools();GUCCI.Product.initZoomBindings();GUCCI.Product.initZoom();GUCCI.Product.initVariations();GUCCI.Product.setupSizeGuideAnimation();GUCCI.Product.initSizeDropDownAvailability();GUCCI.Product.initBackButton();});GUCCI.PersonalShopper={cufonInitialize:function()
{if(typeof Cufon=='function')
Cufon.replace('#contact_personal_shopper_overlay h3, div.contact_personal_shopper_overlay h3, div.contact_personal_shopper_overlay .send_btn span,#contact_personal_shopper_overlay .send_btn span, div.contact_personal_shopper_overlay h2, #contact_personal_shopper_overlay h2, div.contact_personal_shopper_overlay h4, #contact_personal_shopper_overlay h4');},setupPersonalShopper:function(linktarget,theOverlay)
{var whichOverlay;var currlink;var tempLook;if(linktarget!=null)
{currlink=$(linktarget);whichOverlay=theOverlay;}
else
{currlink="a#personal_shopperlink";whichOverlay="#contact_personal_shopper_overlay";}
$(currlink).goldoverlay({onBeforeLoad:function(){$(whichOverlay).find('form').show();$(whichOverlay).find(".ps_details").fadeIn("fast");$(whichOverlay).find('.thankyou').hide();GUCCI.PersonalShopper.cufonInitialize();},onLoad:function()
{var tempURL=window.location.href.split("/");var tempPath="";for(var i=0;i<4;i++)
{tempPath+=tempURL[i]+"/";}
$.ajax({type:'GET',dataType:'json',url:tempPath+"personal_shopper/populate_form",success:function(dataType)
{if(dataType.user!="new_user")
{$('#new_web_user input[name="web_user[title_id]"]')[dataType.title_id].checked=true;$("#contact_ps_web_user_given_name").val(dataType.given_name);$("#contact_ps_web_user_family_name").val(dataType.family_name);$("#contact_ps_web_user_email").val(dataType.email);$("#contact_ps_web_user_email_confirmation").val(dataType.email);}}});var items=$(".personal_shopper_form .items");if(items.length>0)
items.jScrollPane({scrollbarWidth:8});if($("body.category").length>0)
{$(whichOverlay).find("li.no_quickview div.highlight").click(function()
{if($(this).attr("class")=="highlight selected")
{$(this).removeClass("selected");$(whichOverlay).find("a.select_all").css("display","inline");$(whichOverlay).find("a.deselect_all").css("display","none");}
else
{$(this).addClass("selected");}});$('a.select_all').click(function()
{$(this).css("display","none");$(whichOverlay).find("a.deselect_all").css("display","inline");$(whichOverlay).find("li.no_quickview div.highlight").each(function()
{if($(this).attr("class")!="highlight selected")
{$(this).addClass("selected");}});});$('a.deselect_all').click(function()
{$(this).css("display","none");$(whichOverlay).find("a.select_all").css("display","inline");$(whichOverlay).find("li.no_quickview div.highlight").each(function()
{if($(this).attr("class")=="highlight selected")
{$(this).removeClass("selected");}});});}
$('a#submit_shopper').click(function()
{var styleid="";if($("body.styles").length>0)
{var pathname=window.location.href.split("#");if(pathname!="")
{styleid=pathname[1];}
else
{styleid=""}}
else if($("body.category").length>0)
{$(whichOverlay).find(".selected").each(function()
{styleid=styleid+$(this).attr("id")+", ";});if(styleid=="")
{styleid=tempLook;}}
var path=tempPath+'personal_shopper/send_msg';var formData={"web_user[title_id]":$('#new_web_user input:radio[name="web_user[title_id]"]:checked').val(),"web_user[given_name]":$("#contact_ps_web_user_given_name").val(),"web_user[family_name]":$("#contact_ps_web_user_family_name").val(),"web_user[email]":$("#contact_ps_web_user_email").val(),"web_user[email_confirmation]":$("#contact_ps_web_user_email_confirmation").val(),"subject":$("#subject").val(),"message":$("#message").val(),"style":styleid};$.ajax({type:"POST",url:path,data:formData,success:function(data)
{if(data.status=="failure")
{$("span.error").show();fieldList=["contact_ps_web_user_title_id","contact_ps_web_user_given_name","contact_ps_web_user_family_name","contact_ps_web_user_email","contact_ps_web_user_email_confirmation","subject","message"]
for(var i=0;i<fieldList.length;i++)
{var field="#"+fieldList[i];$(field).removeClass("field_with_errors");}
errorList=data.error_fields.split(",");for(var i=0;i<errorList.length;i++)
{var errorField="#"+errorList[i];$(errorField).addClass("field_with_errors");}}
if(data.status=="success")
{$(whichOverlay).find("form").fadeOut("slow");$(whichOverlay).find(".ps_details").fadeOut("slow");$(whichOverlay).find(".thankyou").fadeIn();}}});});$('div.contact_personal_shopper_overlay  a.cancel_overlay, div.contact_personal_shopper_overlay .close').click(function()
{GUCCI.PersonalShopper.removeErrors(whichOverlay);$(currlink).data("overlay").close();});},onClose:function()
{$("#new_web_user select#subject").attr('selectedIndex',0);$("#new_web_user textarea#message").val("");$('#new_web_user input[name="web_user[title_id]"]').each(function(i)
{$('#new_web_user input[name="web_user[title_id]"]')[i].checked=false;});$("#contact_ps_web_user_given_name").val("");$("#contact_ps_web_user_family_name").val("");$("#contact_ps_web_user_email").val("");$("#contact_ps_web_user_email_confirmation").val("");GUCCI.PersonalShopper.removeErrors(whichOverlay);$(whichOverlay).find(".selected").each(function()
{$(this).removeClass("selected");});$(whichOverlay).find(".items").css("top","0");$(whichOverlay).remove();}});},removeErrors:function(theOverlay)
{if($(theOverlay).find(".error").length>0)
{$(theOverlay).find(".error").hide();$(theOverlay).find(".field_with_errors").removeClass("field_with_errors");}}};$(document).ready(function()
{GUCCI.PersonalShopper.setupPersonalShopper();});GUCCI.Webuser={animation:{error_overlay:1000,loading:500,socialicons_show:500,homecontentdivider_show:500},forgotPasswordDetails:"",activeOverlayApi:null,cancelpos:30,initCancelButton:function()
{var cancelbutton=$('#myaccount_card .cancel_btn');var sendbutton=$('#myaccount_card .send_btn');var cancelleft=sendbutton.position().left-cancelbutton.width()-GUCCI.Webuser.cancelpos;cancelbutton.css({left:cancelleft});cancelbutton.css({bottom:sendbutton.css('bottom')});cancelbutton.click(function()
{GUCCI.registration.deleteSignupCookie();});sendbutton.click(function()
{GUCCI.registration.deleteSignupCookie();});var clickedFromGUCCI=GUCCI.getCookie('clickedFromGUCCI');if(clickedFromGUCCI!='true')
{var home_url="http://www.gucci.com/"+GUCCI.siteABBR();cancelbutton.attr("href",home_url);}},initRegisterCreate:function(){$('a#submit_btn').click(function(){$('#myaccount_card form:first').submit();return false;});var plink=$('#privacy_link');if(plink.length>0){plink.goldoverlay({target:plink.attr('rel'),onLoad:function(){var overlay=$(this.target);overlay.find('.scroll_content').jScrollPane({scrollbarWidth:8});var $scrolling=overlay.find('.assistance_content');var scrollingTop=parseInt($scrolling.offset().top,10);overlay.find('.assistance_content h1').each(function(index){var h1Text=$.trim($(this).text());var h1Pos=$(this).offset().top;overlay.find('.assistance_content div.nav_col a').each(function(){if($(this).text()==h1Text){$(this).attr({'href':'javascript:;','rel':h1Pos}).bind('click',function(){$scrolling[0].scrollTo(parseInt($(this).attr('rel'),10)-scrollingTop);});}});});if(typeof Cufon=='function')
Cufon.replace(overlay.find('h3'));},onClose:function(){var overlay=$(this.target);var $scrolling=overlay.find('.assistance_content');$scrolling[0].scrollTo(0);}});}
if(typeof $.fn.tipTip=='function')
{$('#web_user_password').tipTip({maxWidth:'146px',activation:'focus',defaultPosition:'bottom'});$('#web_user_session_password').tipTip({maxWidth:'146px',activation:'focus',defaultPosition:'bottom'});}
var errorbox=$('#myaccount_card .error');if(errorbox.length>0)
$.doTimeout('errorbox',GUCCI.Webuser.animation.error_overlay/2,function(){errorbox.fadeIn(GUCCI.Webuser.animation.error_overlay);});},initRegisterThankYou:function(){var socialmediaicons=$('#socialicons li');if(socialmediaicons.length>0){socialmediaicons.hover(function(){if($.browser.msie&&$.browser.version.substr(0,1)<7)return;$(this).children('.bg').stop(true,true).fadeIn(GUCCI.Webuser.animation.error_overlay);},function(){if($.browser.msie&&$.browser.version.substr(0,1)<7)return;$(this).children('.bg').stop(true,true).fadeOut(GUCCI.Webuser.animation.error_overlay);});}},initForgetPasswordOverlay:function(){if($('#passoverlay_link').length>0)
$('#passoverlay_link').goldoverlay({onBeforeLoad:function(){GUCCI.Webuser.forgotPasswordDetails=$("#forgot_password_content").html();GUCCI.Webuser.initPasswordOverlayDetails();$("form#forgot_password_form").submit(function(){$.post($("form#forgot_password_form").attr("action"),$('form#forgot_password_form').serialize(),function(data){success:$('#forgot_password').find('p').eq(0).empty();$('#forgot_password').empty().append(data);GUCCI.Checkout.initPasswordOverlay();});return false;});},onClose:function(){setTimeout(function(){$('#forgot_password_content').html(GUCCI.Webuser.forgotPasswordDetails);},500);}});},initTrackingPage:function(){$('#main_content .scroll_content').jScrollPane({scrollbarWidth:11,scrollbarMargin:10,animateTo:true});var $scrolling=$('#main_content .scroll_content');var scrollingTop=parseInt($scrolling.offset().top,10);$('#myaccount_card .delivery_details h5').each(function(){var headertext=$.trim($(this).text());var offset=$(this).offset().top;$('#myaccount_card .item_list table th h5').each(function(){$this=$(this);if($.trim($this.text())==headertext){var link=$this.siblings('a');if(link.length>0){link.attr({'href':'#','rel':offset});link.click(function(){$scrolling[0].scrollTo(parseInt($(this).attr('rel'),10)-scrollingTop);});}}});});},initAddressBook:function(){var editlinks=$('#addressadd_link, #addresses .address .edit a, #addresses .address .delete a');var editoverlay=$('#addressoverlay');editlinks.each(function(){var currlink=$(this);currlink.attr('rel',currlink.attr('href'));currlink.attr('href','#');var currlinkcontainer;if(currlink.attr('id')=="addressadd_link")
currlinkcontainer=currlink;else
currlinkcontainer=currlink.parents('.address');currlink.goldoverlay({target:editoverlay,onBeforeLoad:function(){currlinkcontainer.addClass('inactive',GUCCI.Webuser.animation.loading);if(currlink.attr('id')=="addressadd_link")
currlink.siblings('.loading').fadeIn(GUCCI.Webuser.animation.loading);GUCCI.Webuser.activeOverlayApi=currlink.data('overlay');var overlay=GUCCI.Webuser.activeOverlayApi.getOverlay();$('#overlay_screen').css('visibility','hidden');overlay.css('visibility','hidden');if(currlink.parent().hasClass('delete'))
overlay.addClass('smaller');else
overlay.removeClass('smaller');},onLoad:function(){$.get(currlink.attr('rel'),function(data){currlinkcontainer.removeClass('inactive');if(currlink.attr('id')=="addressadd_link")
currlink.siblings('.loading').hide();var overlay=GUCCI.Webuser.activeOverlayApi.getOverlay();$('#overlay_screen').hide().css('visibility','visible');overlay.hide().css('visibility','visible');$('#overlay_screen').fadeIn();overlay.fadeIn();editoverlay.html(data);GUCCI.Webuser.initAddressOverlay();});},onClose:function(){var overlay=GUCCI.Webuser.activeOverlayApi.getOverlay();var errorbox=overlay.find('.error');if(errorbox.length>0)
errorbox.hide();var form=overlay.find('form');GUCCI.Webuser.clearForm(form);}});});var addresses=$("#addresses");if(addresses.length>0)
addresses.jScrollPane({scrollbarWidth:11});var noticebox=$('#myaccount_card .noticebox');if(noticebox.length>0)
$.doTimeout('noticebox',GUCCI.Webuser.animation.error_overlay,function(){noticebox.fadeIn(GUCCI.Webuser.animation.error_overlay);});},initAddressOverlay:function(){var overlay=GUCCI.Webuser.activeOverlayApi.getOverlay();if(typeof Cufon=='function')
Cufon.replace(overlay.find('h3, a.send_btn'));overlay.find('.close, .brown_btn.cancel').click(function(){GUCCI.Webuser.activeOverlayApi.close();return false;});var sendbtn=overlay.find('.send_btn, .brown_btn.send');sendbtn.unbind();sendbtn.one('click',function(){sendbtn.addClass('inactive');overlay.find('.loading').fadeIn(GUCCI.Webuser.animation.loading);var form=overlay.find('form');$.post(form.attr('action'),form.serialize(),function(data){var successfulsave=data.substring(0,20).indexOf('script')==-1;if(!successfulsave)
$('#addressoverlay').html(data);else{$('#content').html(data);GUCCI.Webuser.pngFixes();GUCCI.Webuser.cufonInitialize();}
var errorbox=overlay.find('.error');if(errorbox.length>0)
$.doTimeout('errorbox',GUCCI.Webuser.animation.error_overlay/2,function(){errorbox.fadeIn(GUCCI.Webuser.animation.error_overlay);});if(!successfulsave)
GUCCI.Webuser.initAddressOverlay();else{sendbtn.removeClass('inactive');overlay.find('.loading').hide();GUCCI.Webuser.activeOverlayApi.close();GUCCI.Webuser.initAddressBook();}});});},initMyProfile:function(){$('a#submitpwd_btn').click(function(){$('#myaccount_card form:last').submit();return false;});},initMyAccountHome:function(){var contentoptions=$('#myaccount_card .content_divider').not('.inactive');contentoptions.hover(function(){$(this).stop(true,true).addClass('active',GUCCI.Webuser.animation.homecontentdivider_show);},function(){$(this).stop(true,true).removeClass('active',GUCCI.Webuser.animation.homecontentdivider_show);});contentoptions.click(function(){window.location.href=$(this).find('a').attr('href');});var noticebox=$('#myaccount_card .noticebox');if(noticebox.length>0)
$.doTimeout('noticebox',GUCCI.Webuser.animation.error_overlay/2,function(){noticebox.fadeIn(GUCCI.Webuser.animation.error_overlay);});},initPasswordOverlayDetails:function(){if($('#passoverlay_link').length==0)return;var api=$('#passoverlay_link').data('overlay');if(!api)return;var overlay=api.getOverlay();Cufon.replace(overlay.find('h3'));overlay.find('.close').click(function(){api.close();return false;});},clearForm:function(form){form.find(':input').each(function(){var $this=$(this);switch(this.type){case'select-one':$this.val(0);break;case'text':$this.val('');break;case'textarea':$this.val('');break;case'checkbox':case'radio':this.checked=false;}
$this.removeClass('field_with_errors');var parent=$this.parent('.field_with_errors');if(parent.length>0){if(parent.hasClass('title_fields'))
parent.removeClass('field_with_errors');else
$this.unwrap();}});},pngFixes:function(){if($.browser.msie&&$.browser.version.substr(0,1)<7)
$('#myaccount_card .myaccount_cardreflect').supersleight({shim:'/images/ecommerce/transparent.gif'});},cufonInitialize:function(){if(typeof Cufon=='function'){Cufon.replace('#myaccount_card .title h3,h4,#myaccount_card .cancel_btn span, #myaccount_card .send_btn span,#privacy_overlay h3,#contact_personal_shopper_overlay h3,#addressoverlay h3,#submit_shopper');}}};$(document).ready(function(){if($('body.web_users').length==0)return;GUCCI.Webuser.initRegisterCreate();GUCCI.Webuser.initRegisterThankYou();GUCCI.Webuser.initForgetPasswordOverlay();if($('body.web_users.new').length>0||$('body.web_users.create').length>0)
GUCCI.Webuser.initCancelButton();if($('body.web_users.tracking').length>0)
GUCCI.Webuser.initTrackingPage();GUCCI.Webuser.pngFixes();if($('body.web_users.show').length>0)
GUCCI.Webuser.initMyAccountHome();if($('body.web_users.addresses').length>0)
GUCCI.Webuser.initAddressBook();if($('body.web_users.edit').length>0)
GUCCI.Webuser.initMyProfile();$('form.edit_web_user #submit_shopper').click(function(){$('form.edit_web_user').submit();});});$(document).ready(function(){$(document).register("header.user.loaded",function()
{var $search=$("#search");var $toggleSearch=$("#toggle_search");var $params={limit:0,"fields[]":[]};var $progression="";var $searchCriteria="";var loadSearch=function()
{if($("body.category").length>0)
{$("body.category #pagination").css("z-index","0");}
$("div.loading").css("display","block");$search.load(GUCCI.makeUrl("/search",$params),function()
{if(GUCCI.isIE)
$search.find("#content").css("display","block");else
$search.find("#content").fadeIn();if(typeof $.fn.tipTip=='function')
{$('#number').tipTip({maxWidth:'129px',activation:'focus',defaultPosition:'bottom'});}
$("div.loading").css("display","none");$("#search a.criteria").click(function()
{var href=$(this).attr("href");if(href.indexOf("http")>-1)
{href=href.split("/");href=href[href.length-1];}
if(href.indexOf("_")>-1)
{href=href.split("_");$params.category_id=href[1];href=href[0];}
$params['fields[]'].push(escape($(this).parents("ul").attr("data-field"))+","+href);loadSearch();return false;});var active_criteria=$("#search li.active a").unbind("click").click(function()
{var len=$params['fields[]'].length;var buffer=[];var href=$(this).attr("href");if(href.indexOf("http")>-1)
{href=href.split("/");href=href[href.length-1];}
if(href.indexOf("_")>-1){href=href.split("_")[0];$params.category_id=null;}
var entry=escape($(this).parents("ul").attr("data-field"))+","+href;for(var i=0;i<len;i++)
{var check=$params['fields[]'][i];if(check!=entry){buffer.push(check);}}
$params['fields[]']=buffer;loadSearch();return false;});if(typeof Cufon=='function')
{Cufon.replace('div.results a.send_btn');}
if(active_criteria.length>0)
{if($("div.results").css("display")=="none"){if(GUCCI.isIE)
$("div.results").css('display','block');else
$("div.results").fadeIn();}
else{if(GUCCI.isIE)
$("div.count").css('display','block');else
$("div.count").fadeIn();}
if(GUCCI.isIE)
$("div#reset").css('display','block');else
$("div#reset").fadeIn();$("div#reset a").click(function()
{$params={limit:0,"fields[]":[]};loadSearch();});}
$("a.send_btn").click(function()
{$progression="criteria";$("div#content li.active").each(function(i)
{var activeCat=$(this).parent().prev("h6").text();var activeCrit=$(this).find("a").text();$searchCriteria=$searchCriteria+"/"+activeCat+":"+activeCrit;});$params.limit=240;$resultsNum=$("div.results div.count").text().split(" ")[0];window.location.href=GUCCI.makeUrl("/search/results",$params);});var styleSearch=function()
{var fields=["style_id,"+$("#number").val()];var newval=$('#number').val();newval=newval.replace(/\s/g,'');if(newval.length!=6&&newval.length!=11&&newval.length!=15)
{$('#search #by_style form .error.standard').show();$('#search #by_style form .error.no_results').hide();if(GUCCI.IEversion!=6&&GUCCI.IEversion!=7)
$('#search #by_style form input').addClass('error');return;}
$progression="stylenum";$searchCriteria=$("#number").val();$.get(GUCCI.makeUrl("/search/results",{limit:2,"fields[]":fields}),function(response)
{if(response==null)return false;if(response.style_ids.length==1)
{window.location.href=GUCCI.makeUrl("/styles/"+response.style_ids[0]);}else if(response.style_ids.length>1)
{window.location.href=GUCCI.makeUrl("/search/results",{"fields[]":fields});}else
{$('#search #by_style form .error.standard').hide();$('#search #by_style form .error.no_results').show();if(GUCCI.IEversion!=6&&GUCCI.IEversion!=7)
$('#search #by_style form input').addClass('error');}
$resultsNum=response.style_ids.length;});return false;};$("#style_search_button").click(styleSearch);$("#by_style form").submit(styleSearch);$("#search_close").click(function()
{$search.data("on",false);$search.fadeOut();$search.children("#content").html("");});});};$toggleSearch.click(function()
{if($search.data("on"))
{$search.data("on",false);$search.fadeOut();$("div#reset").hide();if($("body.category").length>0)
{$("body.category #pagination").css("z-index","5000");}}else
{var tmp=GUCCI.parseParams(window.location.href);GUCCI.Analytics.pageView("SEA:START","");if((tmp||{})["fields[]"])
{$params=tmp;$params.limit=0;var buffer=[];var fields=$params['fields[]'];var len=fields.length;for(var i=0;i<len;i++)
{if(fields[i].indexOf("style_id")==-1)
buffer.push(fields[i]);}
$params["fields[]"]=buffer;}
else
{$params={limit:0,"fields[]":[]};}
$search.data("on",true);$search.show();$search.fadeTo(500,1);loadSearch();}});});});GUCCI.Worldofgucci={animation:{accordion_headerbg:400,accordion_content:400,icons_show:400,icons_hide:400,mosaic_titleshow:400,mosaic_bgshow:1000,mosaic_boxshow:1000,mosaic_videoshowhide:1000,navigation_show:1000,navigation_hide:1000,overlay_show:500,page_hide:700,page_show:700,tooltip_show:500},currentPage:1,totalPages:1,paginationActive:true,thumbnailsActive:false,useHTML5:false,isAnimating:false,currvid:"",debugCount:0,connect:{current_panel:"#connect_to_twitter"},checkHeightScrolling:function(){var accordions=$('#accordion_details,#accordion_shopping,#accordion_relatedcontent');for(var i=0;i<accordions.size();i++){var acc=$(accordions[i]);var acccontent=acc.find('.accordion_content');var maxheight=acccontent.css('maxHeight');if(maxheight==null)
maxheight=acccontent.css('borderRightWidth');var maxheightdetails=parseInt(maxheight,10);acccontent.css('borderRightWidth','0');if(acccontent.height()>=maxheightdetails){acccontent.css('height',maxheightdetails);if(acc.attr('id')=='accordion_details')
acccontent.css('maxHeight','none').jScrollPane({scrollbarWidth:11});}}},initAccordions:function(){if(typeof $.fn.accordion=='function'){var accordions=$('#accordion_details,#accordion_shopping,#accordion_relatedcontent');for(var i=0;i<accordions.size();i++){var acc=$(accordions[i]);var acccontent=acc.find('.accordion_content');var maxheight=acccontent.css('maxHeight');if(typeof maxheight!='object')
maxheight=acccontent.data('maxHeight');if(maxheight==null)
maxheight=40000;var maxheightdetails=parseInt(acccontent.css('maxHeight'),10);if(acccontent.height()>=maxheightdetails){acccontent.css('height',maxheightdetails);}}
$('#accordion_shopping ul li a').show();accordions.accordion({autoHeight:false,collapsible:true});accordions.bind('accordionchange',function(event,ui){ui.newHeader.addClass('active');if($.browser.msie&&$.browser.version.substr(0,1)<7)
ui.newHeader.children('span.bg').css('display','block');ui.oldHeader.removeClass('active');if($.browser.msie&&$.browser.version.substr(0,1)<7)
ui.oldHeader.children('span.bg').css('display','none');var newcontent=ui.newContent.children('div.accordion_content');var oldcontent=ui.oldContent.children('div.accordion_content');if(newcontent.length>0)
newcontent.addClass('active');if(oldcontent.length>0)
oldcontent.removeClass('active');});accordions.bind('accordionchangestart',function(event,ui){if(!$.browser.msie){ui.newHeader.children('span.bg').fadeIn(GUCCI.Worldofgucci.animation.accordion_headerbg);ui.oldHeader.children('span.bg').fadeOut(GUCCI.Worldofgucci.animation.accordion_headerbg);}
var newcontent=ui.newContent.children();var oldcontent=ui.oldContent.children();if($.browser.msie){newcontent.not('.jScrollPaneTrack,.jScrollCap').fadeIn(GUCCI.Worldofgucci.animation.accordion_content);oldcontent.not('.jScrollPaneTrack,.jScrollCap').fadeOut(GUCCI.Worldofgucci.animation.accordion_content);}
else{newcontent.fadeIn(GUCCI.Worldofgucci.animation.accordion_content);oldcontent.fadeOut(GUCCI.Worldofgucci.animation.accordion_content);}});}},initRelatedContent:function(){if(typeof $.fn.tipTip=='function')
{$('#accordion_relatedcontent ul li').tipTip({maxWidth:'150px',defaultPosition:'top'});}
$('#accordion_relatedcontent ul li').hover(function(){$(this).stop(true,true).addClass('active',GUCCI.Worldofgucci.animation.tooltip_show).find('img').stop(true,true).fadeTo(GUCCI.Worldofgucci.animation.tooltip_show,1);},function(){$(this).stop(true,true).removeClass('active',0).find('img').stop(true,true).fadeTo(0,0.9);});},initPagination:function(){$('#plink_first').click(function(){if(!$(this).hasClass('inactive'))GUCCI.Worldofgucci.changePage(1);});$('#plink_last').click(function(){if(!$(this).hasClass('inactive'))GUCCI.Worldofgucci.changePage(GUCCI.Worldofgucci.totalPages);});$('#plink_prev').click(function(){if(!$(this).hasClass('inactive'))GUCCI.Worldofgucci.changePage(GUCCI.Worldofgucci.currentPage-1);});$('#plink_next').click(function(){if(!$(this).hasClass('inactive'))GUCCI.Worldofgucci.changePage(GUCCI.Worldofgucci.currentPage+1);});if(GUCCI.isMobile)
{if(typeof $.fn.goldswipe=='function')
{$('#worldofgucci_content').goldswipe({thresholdX:100,thresholdY:999999,swipeLeft:function(){if(!$('#plink_next').hasClass('inactive'))
GUCCI.Worldofgucci.changePage(GUCCI.Worldofgucci.currentPage+1);},swipeRight:function(){if(!$('#plink_prev').hasClass('inactive'))
GUCCI.Worldofgucci.changePage(GUCCI.Worldofgucci.currentPage-1);}});}}},initThumbnails:function(){var scrollable=$('#container_navigation div.scrollable');var scrollablewidth=scrollable.width();var itemwidth=scrollable.find('img').width();var visibleitems=Math.floor(scrollablewidth/itemwidth);scrollable.css('width',itemwidth*visibleitems+'px');scrollable.scrollable({size:visibleitems,activeClass:'selected',keyboard:false,clickable:true,speed:GUCCI.Worldofgucci.animation.page_show});$('#container_navigation div.scrollable ul li').each(function(i){$this=$(this);$this.click(function(){GUCCI.Worldofgucci.changePage(i+1);});});$('#worldofgucci_content').hover(function(){$('#container_navigation').stop(true,true).addClass('active',GUCCI.Worldofgucci.animation.navigation_show);},function(){$('#container_navigation').stop(true,true).removeClass('active',GUCCI.Worldofgucci.animation.navigation_hide);});},initIndexOverlay:function(){$('#container_indexoverlay ul li').each(function(i){$this=$(this);$this.click(function(){GUCCI.Worldofgucci.changePage(i+1);GUCCI.Worldofgucci.indexOverlayHide();});});$('#plink_indexoverlay').click(GUCCI.Worldofgucci.indexOverlayShow);},initIcons:function(){if(typeof $.fn.tipTip=='function')
{$('#container_icons #share').tipTip({maxWidth:'150px',defaultPosition:'top'});}
if($.fn.love)
{if(typeof $.fn.tipTip=='function'&&!GUCCI.isMobile)
{$('#container_icons #loveit').tipTip({maxWidth:'150px',content:$('#loveit').find('.mylove').text(),defaultPosition:'top'});}
$('#container_icons #loveit .love-button').love({caller:$('#container_icons')});}
if(GUCCI.Worldofgucci.thumbnailsActive||$('#container_pages .page .video').length>0){$('#worldofgucci_content').hover(function(){$('#container_icons').stop(true,true).addClass('active',GUCCI.Worldofgucci.animation.icons_show);},function(){$('#container_icons').stop(true,true).removeClass('active',GUCCI.Worldofgucci.animation.icons_hide);});}
else
$('#container_icons').addClass('active');},initShare:function(){},initVideos:function(){var vids=$('#container_pages .page .video.flash');for(i=0;i<vids.length;i++){var currvid=$(vids[i]);GUCCI.Worldofgucci.initVideoNew(currvid);}},initVideoNew:function(video){var videoid=video.attr('id');if(!videoid)return;if(typeof flowplayer!='function')return;var config={key:'#@f7d674c7ec199e0dda0',plugins:{controls:{url:'flowplayer.controls-3.2.2.swf',bottom:0,height:24,backgroundColor:'#000000',backgroundGradient:'low',opacity:0.7,fontColor:'#ffffff',timeColor:'#eeeeee',durationColor:'#ffffff',progressColor:'#2a2927',progressGradient:'low',bufferColor:'#4c483a',bufferGradient:'low',buttonColor:'#ffffff',buttonOverColor:'#ffffff',tooltipColor:'#2a2927',autoHide:{enabled:false,fullsccreenOnly:true,hideStyle:'fade'},hideDelay:'4000'}},logo:{opacity:0,url:'',fullScreenOnly:false,displayTime:0},clip:{accelerated:false,fadeInSpeed:1000,fadeOutSpeed:1000,autoPlay:true,scaling:'fit',onStart:function(){if($.browser.msie&&$.browser.version.substr(0,1)<7)return;GUCCI.Worldofgucci.currvid=$('#'+this.id());var mTop=parseInt(GUCCI.Worldofgucci.currvid.css('marginTop'),10);mTop=27-mTop;if(GUCCI.Worldofgucci.currvid.hasClass('highscrubber'))
{$f().getControls().animate('bottom','27px');}}},play:{width:64,height:64},canvas:{backgroundColor:'#000000',backgroundGradient:'none'}};flowplayer(video.attr('id'),{src:"/flowplayer/flowplayer.commercial-3.2.4.swf",wmode:"transparent"},config).ipad();},checkLetterboxing:function(video){vidcontainer=video.parent();if(!vidcontainer.hasClass('col_full'))return;vidwidth=video.width();containerwidth=vidcontainer.width();newheight=parseInt(((containerwidth/vidwidth)*video.height()),10);newmargintop=parseInt((vidcontainer.height()-newheight)/2,10);if(vidwidth>containerwidth){video.css({'width':containerwidth+'px','height':newheight+'px','marginTop':newmargintop+'px'});}},indexOverlayShow:function(){$('#container_indexoverlay').fadeIn(GUCCI.Worldofgucci.animation.overlay_show,function(){var thumbs=$('#container_indexoverlay ul li');for(var i=0;i<thumbs.length;i++){$(thumbs[i]).delay(i*(GUCCI.Worldofgucci.animation.overlay_show/4)).fadeIn(GUCCI.Worldofgucci.animation.overlay_show);}});},indexOverlayHide:function(){$('#container_indexoverlay ul li, #container_indexoverlay').fadeOut(GUCCI.Worldofgucci.animation.overlay_show);},lazyLoadImages:function(){if(typeof $.fn.lazyload=='function'){var inactiveimgs=$('#container_pages .page[class!="page active"] img');inactiveimgs.lazyload({event:'manual',pngfix:false});}},shareDialog:function()
{var api;var overlay;$('#share a.share').goldoverlay({onBeforeLoad:function(){api=$('#share a').data('overlay');overlay=api.getOverlay();overlay.find('.close').click(function(){api.close();return false;});$("#overlay_screen").addClass("overlayScreenFix");},onLoad:function(){if(typeof Cufon=='function')
Cufon.replace('.share_overlay h3');var emailLink=overlay.find('.email_friend_link');var targetOverlay=overlay;GUCCI.emailFriend(emailLink,targetOverlay,"","worldofgucci");$('div.share_overlay  a.cancel_overlay, div.share_overlay .close').click(function()
{$("div.share_overlay").remove();$("#overlay_screen").removeClass("overlayScreenFix");});}});if($.browser.msie&&$.browser.version.substr(0,1)<7)
$('#share').click(function(){$(this).find('.share').trigger('click');});},truncateText:function(){if(typeof $.fn.pixelinitalphabet=='function'){var samplefonts=jQuery('<div/>',{id:'samplefonts'});$('#bg_content').append(samplefonts);var wogstuffshopping=$('#accordion_shopping ul li');if(wogstuffshopping.length>0){var shoppingwidth=wogstuffshopping.width();var links=wogstuffshopping.children('a');if(links.length>0)
links.eq(0).pixeltruncate({pixellimit:shoppingwidth,checkcontainer:samplefonts,numlines:2});if(links.length>1)
links.eq(1).pixeltruncate({pixellimit:shoppingwidth,checkcontainer:samplefonts,numlines:1});}
samplefonts.remove();}},changePage:function(newpage){if(newpage==GUCCI.Worldofgucci.currentPage||GUCCI.Worldofgucci.isAnimating)return;GUCCI.Worldofgucci.isAnimating=true;var oldcontent=$('#page_'+GUCCI.Worldofgucci.currentPage);var newcontent=$('#page_'+newpage);oldcontent.addClass('last-active').removeClass('active');if(GUCCI.Worldofgucci.thumbnailsActive)
$('#thumb_'+newpage).trigger('click');newcontent.css('opacity','0').addClass('active');GUCCI.Worldofgucci.matchCaptiontoImg(newcontent);var newcontentchildren=newcontent.children();var newcontentvideo;if(GUCCI.Worldofgucci.useHTML5)
newcontentvideo=newcontent.find('.video.html5');else
newcontentvideo=newcontent.find('.video.flash');if(newcontentchildren.hasClass('col_full')&&newcontentvideo.length>0){GUCCI.Worldofgucci.checkLetterboxing(newcontentvideo);}
newcontent.animate({'opacity':'1.0'},GUCCI.Worldofgucci.animation.page_show,function(){oldcontent.removeClass('active last-active');GUCCI.Worldofgucci.isAnimating=false;if(GUCCI.Worldofgucci.currentPage==1)
$('#plink_first,#plink_prev').removeClass('inactive');if(GUCCI.Worldofgucci.currentPage==GUCCI.Worldofgucci.totalPages)
$('#plink_last,#plink_next').removeClass('inactive');if(newpage==1)
$('#plink_first,#plink_prev').addClass('inactive');if(newpage==GUCCI.Worldofgucci.totalPages)
$('#plink_last,#plink_next').addClass('inactive');var prevpage=newpage-1;if(prevpage<1)
prevpage=1;var nextpage=newpage+1;if(nextpage>GUCCI.Worldofgucci.totalPages)
nextpage=GUCCI.Worldofgucci.totalPages;$('#plink_prev').attr('href','#'+prevpage);$('#plink_next').attr('href','#'+nextpage);$('#currpage').html(newpage);GUCCI.Worldofgucci.currentPage=newpage;});},changePageFromUrl:function(){var newpage=1;var splithash=window.location.href.split('#');if(splithash.length>0)
newpage=parseInt(jQuery.trim(splithash[1]),10);if(newpage>1)
GUCCI.Worldofgucci.changePage(newpage);},mosaicIntroAnimation:function(){var boxes=$('#mosaic .mosaic_widget,#mosaic .empty');setTimeout(function(){$('#mosaic').delay(boxes.length*GUCCI.Worldofgucci.animation.mosaic_bgshow/4).addClass('active',GUCCI.Worldofgucci.animation.mosaic_bgshow);if($.browser.msie&&$.browser.version.substr(0,1)<7){for(var i=0;i<boxes.length;i++)
if(i==(boxes.length-1))
$(boxes[i]).delay(GUCCI.Worldofgucci.animation.mosaic_boxshow*i/4).fadeTo(GUCCI.Worldofgucci.animation.mosaic_boxshow,1,function(){var introimg=$('#mosaic_introvideoimg');if(introimg.length>0)
introimg.hide();});else
$(boxes[i]).delay(GUCCI.Worldofgucci.animation.mosaic_boxshow*i/4).fadeTo(GUCCI.Worldofgucci.animation.mosaic_boxshow,1);}
else{for(var i=0;i<boxes.length;i++)
if(i==(boxes.length-1))
$(boxes[i]).delay(GUCCI.Worldofgucci.animation.mosaic_bgshow/4*i).fadeIn(GUCCI.Worldofgucci.animation.mosaic_bgshow,function(){var introimg=$('#mosaic_introvideoimg');if(introimg.length>0)
introimg.hide();});else
$(boxes[i]).delay(GUCCI.Worldofgucci.animation.mosaic_bgshow/4*i).fadeIn(GUCCI.Worldofgucci.animation.mosaic_bgshow);}},GUCCI.Worldofgucci.animation.mosaic_videoshowhide/2);},matchCaptiontoImg:function(pagecontent){var caption=pagecontent.find('.reduced p.caption');if(caption.length>0){var newwidth=caption.siblings('img').width();if(newwidth==null)
newwidth=caption.siblings('.video').width();if(newwidth>0)
caption.css('width',newwidth+'px');}},cufonInitialize:function(){if(typeof Cufon=='function')
Cufon.replace('#worldofgucci_sidebar h2,#worldofgucci_content h2,#worldofgucci_content h3, #gg_connect_card h6');}};$(document).ready(function(){if($('body.worldofgucci').length==0)return;if(!($('#worldofgucci_content').attr('art_id')===undefined)){$.ajax({type:'get',dataType:'json',complete:function(data,textStatus,XMLHttpRequest){article=JSON.parse(data.responseText).article;$('#loveit a').html(article.love.lovers);},url:'/articles/'+$('#worldofgucci_content').attr('art_id')+'.json'});}
$('#worldofgucci_content .text a').each(function(){$(this).attr("target","_blank");});if($('body.worldofgucci.show').length>0){GUCCI.Worldofgucci.lazyLoadImages();GUCCI.Worldofgucci.checkHeightScrolling();GUCCI.Worldofgucci.initAccordions();GUCCI.Worldofgucci.initRelatedContent();if(GUCCI.Worldofgucci.paginationActive)
GUCCI.Worldofgucci.initPagination();if(GUCCI.Worldofgucci.thumbnailsActive){GUCCI.Worldofgucci.initThumbnails();GUCCI.Worldofgucci.initIndexOverlay();}
if($.browser.msie&&$.browser.version.substr(0,1)<7){if(typeof $.fn.supersleight=='function')
$('body.worldofgucci #worldofgucci_cardreflect, body.worldofgucci .accordion h5, body.worldofgucci .accordion span.bg, body.worldofgucci .jScrollPaneTrack, body.worldofgucci #container_icons ul li').supersleight({shim:'/images/ecommerce/transparent.gif'});$('body.worldofgucci .accordion h5').not('h5.active').children('span.bg').css('display','none');}
GUCCI.Worldofgucci.shareDialog();GUCCI.Worldofgucci.initIcons();GUCCI.Worldofgucci.initVideos();if(typeof $.fn.lazyload=='function'){var inactiveimgs=$('#container_pages .page[class!="page active"] img');setTimeout(function(){inactiveimgs.trigger('appear');},500);}
if(GUCCI.Worldofgucci.totalPages>0)
GUCCI.Worldofgucci.matchCaptiontoImg($('#page_1'));GUCCI.Worldofgucci.changePageFromUrl();}
if($('body.worldofgucci.mosaic').length>0){var tfeed=$('#twitter_feed');var tfeedparent=tfeed.parent();tfeedparent.css('opacity','0');tfeedparent.show();var totalheight=tfeed.height();totalheight-=tfeed.children('h3').outerHeight(true);var hidetweets=false;tfeed.find('li').each(function(){if(!hidetweets){totalheight-=$(this).outerHeight(true);if(totalheight<0)
hidetweets=true;}
if(hidetweets)
$(this).hide();});tfeedparent.hide();tfeedparent.css('opacity','1');if($('#mosaic_introvideo').length>0){var config={key:'#@f7d674c7ec199e0dda0',plugins:{controls:null},clip:{accelerated:false,fadeInSpeed:GUCCI.Worldofgucci.animation.mosaic_videoshowhide,fadeOutSpeed:GUCCI.Worldofgucci.animation.mosaic_videoshowhide,autoPlay:true,onCuepoint:[5000,function(clip,point){this.pause();$('#mosaic_introvideo,#mosaic_introvideoskip').hide();$('#mosaic_introvideoimg').addClass('active',GUCCI.Worldofgucci.animation.mosaic_videoshowhide/2);GUCCI.Worldofgucci.mosaicIntroAnimation();this.stop();}]},canvas:{backgroundColor:'#000000',backgroundGradient:'none'},play:{opacity:0},onLoad:function(){$f().setVolume(0);var skipbutton=$('#mosaic_introvideoskip');skipbutton.click(function(){$f().pause();$('#mosaic_introvideoimg').hide();$('#mosaic_introvideo,#mosaic_introvideoimg').add(skipbutton).fadeOut(GUCCI.Worldofgucci.animation.mosaic_videoshowhide/2);$f().stop();GUCCI.Worldofgucci.mosaicIntroAnimation();});skipbutton.delay(GUCCI.Worldofgucci.animation.mosaic_videoshowhide/2).fadeIn(GUCCI.Worldofgucci.animation.mosaic_videoshowhide);}};if(GUCCI.isMobile)
flowplayer('mosaic_introvideo',{src:"/flowplayer/flowplayer.commercial-3.2.4.swf",wmode:"transparent"},config).ipad();else
flowplayer('mosaic_introvideo',{src:"/flowplayer/flowplayer.commercial-3.2.4.swf",wmode:"transparent"},config);}
else{var boxes=$('#mosaic .mosaic_widget,#mosaic .empty');setTimeout(function(){$('#mosaic').delay(boxes.length*250).addClass('active',GUCCI.Worldofgucci.animation.mosaic_bgshow);if($.browser.msie&&$.browser.version.substr(0,1)<7){for(var i=0;i<boxes.length;i++)
$(boxes[i]).delay(GUCCI.Worldofgucci.animation.mosaic_boxshow*i/4).fadeTo(GUCCI.Worldofgucci.animation.mosaic_boxshow,1);}
else{for(var i=0;i<boxes.length;i++)
$(boxes[i]).delay(GUCCI.Worldofgucci.animation.mosaic_bgshow/4*i).fadeIn(GUCCI.Worldofgucci.animation.mosaic_bgshow);}},2000);}
$('body.worldofgucci #mosaic .mosaic_widget').hover(function(){var $this=$(this);var title=$this.find('div.container_title');if(typeof title=='undefined')return;var text=title.children('p');if(typeof text=='undefined')return;title.stop(true,true).addClass('active',GUCCI.Worldofgucci.animation.mosaic_titleshow);text.stop(true,true).delay(GUCCI.Worldofgucci.animation.mosaic_titleshow/2).fadeIn(GUCCI.Worldofgucci.animation.mosaic_titleshow/2);},function(){var $this=$(this);var title=$this.find('div.container_title');if(typeof title=='undefined')return;var text=title.children('p');if(typeof text=='undefined')return;text.stop(true,true).fadeOut(GUCCI.Worldofgucci.animation.mosaic_titleshow/2);title.stop(true,true).delay(GUCCI.Worldofgucci.animation.mosaic_titleshow/4).removeClass('active',GUCCI.Worldofgucci.animation.mosaic_titleshow);});}
if($('body.connect').length>0){var twitterpane=$('#twitter_feed ul');if(twitterpane.length>0)twitterpane.jScrollPane({scrollbarWidth:7});$('body.worldofgucci #gg_connect_mosaic .mosaic_widget').click(function(){var panes=$('.detail');var newpaneid='#'+$(this).attr('id').replace(/thumb_/,'');if(GUCCI.Worldofgucci.connect.current_panel!=newpaneid){if(GUCCI.isIE){panes.hide();$(newpaneid).show();}else{panes.fadeOut(GUCCI.Worldofgucci.animation.tooltip_show);$(newpaneid).fadeIn(GUCCI.Worldofgucci.animation.tooltip_show);}
GUCCI.Worldofgucci.connect.current_panel=newpaneid;}});$('body.worldofgucci #gg_connect_mosaic .mosaic_widget').hover(function(){var $this=$(this);var title=$this.find('div.container_title');if(typeof title=='undefined')return;var text=title.children('p');if(typeof text=='undefined')return;title.stop(true,true).addClass('active',GUCCI.Worldofgucci.animation.mosaic_titleshow);text.stop(true,true).delay(GUCCI.Worldofgucci.animation.mosaic_titleshow/2).fadeIn(GUCCI.Worldofgucci.animation.mosaic_titleshow/2);},function(){var $this=$(this);var title=$this.find('div.container_title');if(typeof title=='undefined')return;var text=title.children('p');if(typeof text=='undefined')return;text.stop(true,true).fadeOut(GUCCI.Worldofgucci.animation.mosaic_titleshow/2);title.stop(true,true).delay(GUCCI.Worldofgucci.animation.mosaic_titleshow/4).removeClass('active',GUCCI.Worldofgucci.animation.mosaic_titleshow);});}});$(document).ready(function(){});if(!this.JSON){this.JSON={};}
(function(){function f(n){return n<10?'0'+n:n;}
if(typeof Date.prototype.toJSON!=='function'){Date.prototype.toJSON=function(key){return isFinite(this.valueOf())?this.getUTCFullYear()+'-'+
f(this.getUTCMonth()+1)+'-'+
f(this.getUTCDate())+'T'+
f(this.getUTCHours())+':'+
f(this.getUTCMinutes())+':'+
f(this.getUTCSeconds())+'Z':null;};String.prototype.toJSON=Number.prototype.toJSON=Boolean.prototype.toJSON=function(key){return this.valueOf();};}
var cx=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,escapable=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,gap,indent,meta={'\b':'\\b','\t':'\\t','\n':'\\n','\f':'\\f','\r':'\\r','"':'\\"','\\':'\\\\'},rep;function quote(string){escapable.lastIndex=0;return escapable.test(string)?'"'+string.replace(escapable,function(a){var c=meta[a];return typeof c==='string'?c:'\\u'+('0000'+a.charCodeAt(0).toString(16)).slice(-4);})+'"':'"'+string+'"';}
function str(key,holder){var i,k,v,length,mind=gap,partial,value=holder[key];if(value&&typeof value==='object'&&typeof value.toJSON==='function'){value=value.toJSON(key);}
if(typeof rep==='function'){value=rep.call(holder,key,value);}
switch(typeof value){case'string':return quote(value);case'number':return isFinite(value)?String(value):'null';case'boolean':case'null':return String(value);case'object':if(!value){return'null';}
gap+=indent;partial=[];if(Object.prototype.toString.apply(value)==='[object Array]'){length=value.length;for(i=0;i<length;i+=1){partial[i]=str(i,value)||'null';}
v=partial.length===0?'[]':gap?'[\n'+gap+
partial.join(',\n'+gap)+'\n'+
mind+']':'['+partial.join(',')+']';gap=mind;return v;}
if(rep&&typeof rep==='object'){length=rep.length;for(i=0;i<length;i+=1){k=rep[i];if(typeof k==='string'){v=str(k,value);if(v){partial.push(quote(k)+(gap?': ':':')+v);}}}}else{for(k in value){if(Object.hasOwnProperty.call(value,k)){v=str(k,value);if(v){partial.push(quote(k)+(gap?': ':':')+v);}}}}
v=partial.length===0?'{}':gap?'{\n'+gap+partial.join(',\n'+gap)+'\n'+
mind+'}':'{'+partial.join(',')+'}';gap=mind;return v;}}
if(typeof JSON.stringify!=='function'){JSON.stringify=function(value,replacer,space){var i;gap='';indent='';if(typeof space==='number'){for(i=0;i<space;i+=1){indent+=' ';}}else if(typeof space==='string'){indent=space;}
rep=replacer;if(replacer&&typeof replacer!=='function'&&(typeof replacer!=='object'||typeof replacer.length!=='number')){throw new Error('JSON.stringify');}
return str('',{'':value});};}
if(typeof JSON.parse!=='function'){JSON.parse=function(text,reviver){var j;function walk(holder,key){var k,v,value=holder[key];if(value&&typeof value==='object'){for(k in value){if(Object.hasOwnProperty.call(value,k)){v=walk(value,k);if(v!==undefined){value[k]=v;}else{delete value[k];}}}}
return reviver.call(holder,key,value);}
text=String(text);cx.lastIndex=0;if(cx.test(text)){text=text.replace(cx,function(a){return'\\u'+
('0000'+a.charCodeAt(0).toString(16)).slice(-4);});}
if(/^[\],:{}\s]*$/.test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,'@').replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,']').replace(/(?:^|:|,)(?:\s*\[)+/g,''))){j=eval('('+text+')');return typeof reviver==='function'?walk({'':j},''):j;}
throw new SyntaxError('JSON.parse');};}}());