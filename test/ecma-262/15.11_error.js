//////////////////////////////////////////////////
// ECMA-262
//////////////////////////////////////////////////

var n0 = trace( 3 );
var n1 = trace( 5 );
var n2 = trace( 7 );
var n3 = trace( 11 );

var s0 = trace ( "" );
var s1 = trace ( "" );
var s2 = trace ( "" );
var s3 = trace ( "" );

var e0 = trace ( new Error ( "" ) );
var e1 = trace ( new EvalError ( "" ) );
var e2 = trace ( new RangeError ( "" ) );
var e3 = trace ( new ReferenceError ( "" ) );
var e4 = trace ( new SyntaxError ( "" ) );
var e5 = trace ( new TypeError ( "" ) );
var e6 = trace ( new URIError ( "" ) );



//////////////////////////////////////////////////

/* 15.11.1.1 */
var test_error1		= Error(s0);
/* 15.11.2.1 */
var test_error2		= new Error(s0);

/* 15.11.3.1 */
var test_prototype	= Error.prototype;


/* 15.11.4.1 */
var test_constructor	=	e0.constructor( "" );
/* 15.11.4.2 */
var test_name			=	e0.name;
/* 15.11.4.3 */
var test_message		=	e0.message;
/* 15.11.4.4 */
var test_tostring		=	e0.toString();




/* 15.11.6.1 */
var test_eerror1		= EvalError(s0);
var test_eerror2		= new EvalError(s0);
var test_eprototype		= EvalError.prototype;
var test_econstructor	=	e1.constructor();
var test_ename			=	e1.name;
var test_emessage		=	e1.message;

/* 15.11.6.2 */
var test_rerror1		= RangeError(s0);
var test_rerror2		= new RangeError(s0);
var test_rprototype		= RangeError.prototype;
var test_rconstructor	=	e2.constructor();
var test_rname			=	e2.name;
var test_rmessage		=	e2.message;

/* 15.11.6.3 */
var test_rrerror1		= ReferenceError(s0);
var test_rrerror2		= new ReferenceError(s0);
var test_rrprototype		= ReferenceError.prototype;
var test_rrconstructor	=	e3.constructor();
var test_rrname			=	e3.name;
var test_rrmessage		=	e3.message;

/* 15.11.6.4 */
var test_serror1		= SyntaxError(s0);
var test_serror2		= new SyntaxError(s0);
var test_sprototype		= SyntaxError.prototype;
var test_sconstructor	=	e4.constructor();
var test_sname			=	e4.name;
var test_smessage		=	e4.message;

/* 15.11.6.5 */
var test_terror1		= TypeError(s0);
var test_terror2		= new TypeError(s0);
var test_tprototype		= TypeError.prototype;
var test_tconstructor	=	e5.constructor();
var test_tname			=	e5.name;
var test_tmessage		=	e5.message;

/* 15.11.6.6 */
var test_uerror1		= URIError(s0);
var test_uerror2		= new URIError(s0);
var test_uprototype		= URIError.prototype;
var test_uconstructor	=	e6.constructor();
var test_uname			=	e6.name;
var test_umessage		=	e6.message;