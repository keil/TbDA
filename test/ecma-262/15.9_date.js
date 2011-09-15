//////////////////////////////////////////////////
// ECMA-262
//////////////////////////////////////////////////

var n0 = trace( 3 );
var n1 = trace( 5 );
var n2 = trace( 7 );
var n3 = trace( 11 );
var n4 = trace( 3 );
var n5 = trace( 5 );
var n6 = trace( 7 );
var n7 = trace( 11 );
var n8 = trace( 3 );
var n9 = trace( 5 );
var n10 = trace( 7 );
var n11 = trace( 11 );
var n12 = trace( 3 );
var n13 = trace( 5 );
var n14 = trace( 7 );

var s0 = trace( "17" );

var d0 = trace( new Date() );

var year1 = trace(1961);
var year2 = trace(2011);

//////////////////////////////////////////////////


/* 15.9.1.11 */
// var test_maketime	=	MakeTime( n0, n0, n0, n0 );			
/* 15.9.1.12 */
// var test_makeday	=	MakeDay( n0, n0, n0 );		
/* 15.9.1.13 */
// var test_makedate	=	MakeDate( n0, n0 );		
/* 15.9.1.14 */
// var test_timeclip	=	TimeClip( n0 );



/* 15.9.2.1 */
var test_date1		=	Date( n0 , n1 );
/* 15.9.3.1 */
var test_date2		=	new Date( n0 , n1 );
/* 15.9.3.2 */
var test_date3		=	new Date( s0 );
/* 15.9.3.3 */
var test_date4		=	new Date( );




/* 15.9.4.1 */
var test_prototype = Date.prototype;
/* 15.9.4.2 */
var test_parse = Date.parse( s0 );
/* 15.9.4.3 */
var test_utc = Date.UTC( n0, n1 );
/* 15.9.4.4 */
// var test_now = Date.now();


var test_do = d0;


/* 15.9.5.1 */
var test_constructor 			= d0.constructor();
/* 15.9.5.2 */
var test_tostring 				= d0.toString();
/* 15.9.5.3 */
var test_todatestring 			= d0.toDateString();
/* 15.9.5.4 */
var test_totimestring 			= d0.toTimeString();
/* 15.9.5.5 */
var test_tolocalestring 		= d0.toLocaleString();
/* 15.9.5.6 */
var test_tolocaledatestring 	= d0.toLocaleDateString();
/* 15.9.5.7 */
var test_tolacaletime			= d0.toLocaleTimeString();
/* 15.9.5.8 */
var test_valueof 				= d0.valueOf();
/* 15.9.5.9 */
var test_gettime				= d0.getTime();
/* 15.9.5.10 */
var test_getfullyear			= d0.getFullYear();
/* 15.9.5.11 */
var test_getutcfullyear			= d0.getUTCFullYear();
/* 15.9.5.12 */
var test_getmonth				= d0.getMonth();
/* 15.9.5.13 */
var test_getutcmonth			= d0.getUTCMonth();
/* 15.9.5.14 */
var test_getdate				= d0.getDate();
/* 15.9.5.15 */
var test_getutcdate				= d0.getUTCDate();
/* 15.9.5.16 */
var test_getday					= d0.getDay();
/* 15.9.5.17 */
var test_getutcday				= d0.getUTCDay();
/* 15.9.5.18 */
var test_gethours				= d0.getHours();
/* 15.9.5.19 */
var test_getutcgourse			= d0.getUTCHours();
/* 15.9.5.20 */
var test_getminutes				= d0.getMinutes();
/* 15.9.5.21 */
var test_getutcminutes			= d0.getUTCMinutes();
/* 15.9.5.22 */
var test_getseconds				= d0.getSeconds();
/* 15.9.5.23 */
var test_getutcseconds			= d0.getUTCSeconds();
/* 15.9.5.24 */
var test_getmilliseconds		= d0.getMilliseconds();
/* 15.9.5.25 */
var test_getutcmillisenconds	= d0.getUTCMilliseconds();
/* 15.9.5.26 */
var test_gettimezoneoffset		= d0.getTimezoneOffset();
/* 15.9.5.27 */
var test_settime				= d0.setTime( n0 );
/* 15.9.5.28 */
var test_setmilliseconds		= d0.setMilliseconds( n1 );
/* 15.9.5.29 */
var test_setutsmilliseconds		= d0.setUTCMilliseconds( n2 );
/* 15.9.5.30 */
var test_setseconds				= d0.setSeconds( n3 );
/* 15.9.5.31 */
var test_setutsseconds			= d0.setUTCSeconds( n4 );
/* 15.9.5.32 */
var test_setminutes				= d0.setMinutes( n5 );
/* 15.9.5.33 */
var test_setutsminutes			= d0.setUTCMinutes( n6 );
/* 15.9.5.34 */
var test_sethours				= d0.setHours( n7 );
/* 15.9.5.35 */
var test_seturshours			= d0.setUTCHours( n8 );
/* 15.9.5.36 */
var test_setdate				= d0.setDate( n9 );
/* 15.9.5.37 */
var test_setutcdate				= d0.setUTCDate( n10 );
/* 15.9.5.38 */
var test_setmonth				= d0.setMonth( n11 );
/* 15.9.5.39 */
var test_setutcmonth			= d0.setUTCMonth( n12 );
/* 15.9.5.40 */
var test_setfullyear			= d0.setFullYear( n13 );
/* 15.9.5.41 */
var test_setutcfulljear			= d0.setUTCFullYear( n14 );



/* 15.9.5.42 */
// var test_toutcstring			= d0.toUTCString();
/* 15.9.5.43 */
// var test_toisostring			= d0.toISOString();
/* 15.9.5.44 */
// var test_tojson				= d0.toJSON( n0 );


// SET TEST

/* 15.9.5.1 */
var test_constructor1 			= d0.constructor();
/* 15.9.5.2 */
var test_tostring1 				= d0.toString();
/* 15.9.5.3 */
var test_todatestring1 			= d0.toDateString();
/* 15.9.5.4 */
var test_totimestring1 			= d0.toTimeString();
/* 15.9.5.5 */
var test_tolocalestring1 		= d0.toLocaleString();
/* 15.9.5.6 */
var test_tolocaledatestring1 	= d0.toLocaleDateString();
/* 15.9.5.7 */
var test_tolacaletime1			= d0.toLocaleTimeString();
/* 15.9.5.8 */
var test_valueof1 				= d0.valueOf();
/* 15.9.5.9 */
var test_gettime1				= d0.getTime();
/* 15.9.5.10 */
var test_getfullyear1			= d0.getFullYear();
/* 15.9.5.11 */
var test_getutcfullyear1		= d0.getUTCFullYear();
/* 15.9.5.12 */
var test_getmonth1				= d0.getMonth();
/* 15.9.5.13 */
var test_getutcmonth1			= d0.getUTCMonth();
/* 15.9.5.14 */
var test_getdate1				= d0.getDate();
/* 15.9.5.15 */
var test_getutcdate1			= d0.getUTCDate();
/* 15.9.5.16 */
var test_getday1				= d0.getDay();
/* 15.9.5.17 */
var test_getutcday1				= d0.getUTCDay();
/* 15.9.5.18 */
var test_gethours1				= d0.getHours();
/* 15.9.5.19 */
var test_getutcgourse1			= d0.getUTCHours();
/* 15.9.5.20 */
var test_getminutes1			= d0.getMinutes();
/* 15.9.5.21 */
var test_getutcminutes1			= d0.getUTCMinutes();
/* 15.9.5.22 */
var test_getseconds1			= d0.getSeconds();
/* 15.9.5.23 */
var test_getutcseconds1			= d0.getUTCSeconds();
/* 15.9.5.24 */
var test_getmilliseconds1		= d0.getMilliseconds();
/* 15.9.5.25 */
var test_getutcmillisenconds1	= d0.getUTCMilliseconds();
/* 15.9.5.26 */
var test_gettimezoneoffset1		= d0.getTimezoneOffset();


/* 15.9.5.42 */
// var test_toutcstring1		= d0.toUTCString();
/* 15.9.5.43 */
// var test_toisostring1		= d0.toISOString();
/* 15.9.5.44 */
// var test_tojson1				= d0.toJSON( n0 );
