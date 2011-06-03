//----------------------------------------------------------------------
// Fallback implementations of "Future" JavaScript functionality
//----------------------------------------------------------------------
// http://developer.mozilla.org/En/Core_JavaScript_1.5_Reference/Objects/Array
//----------------------------------------------------------------------
// Also includes functions from ECMA TC39 draft for ECMAScript 5 (ES5)
//
// A copy of http://www.json.org/json2.js should be included as well
// to pull in JSON.stringify(), JSON.parse(), and *.toJSON()
//
// Good informal reference:
// http://markcaudill.com/index.php/2009/04/javascript-new-features-ecma5/
//----------------------------------------------------------------------


//----------------------------------------------------------------------
// Introduced in JavaScript 1.6
//----------------------------------------------------------------------

// http://developer.mozilla.org/en/Core_JavaScript_1.5_Reference/Objects/Array/every
// Also present in ES5 DRAFT
if (!Array.prototype.every) {
    Array.prototype.every = function(fun /*, thisp*/) {
        var len = this.length;
        if (typeof fun != "function")
            throw new TypeError();

        var thisp = arguments[1];
        for (var i = 0; i < len; i++) {
            if (i in this &&
          !fun.call(thisp, this[i], i, this))
                return false;
        }

        return true;
    };
}


// http://developer.mozilla.org/en/Core_JavaScript_1.5_Reference/Objects/Array/filter
// Also present in ES5 DRAFT
if (!Array.prototype.filter) {
    Array.prototype.filter = function(fun /*, thisp*/) {
        var len = this.length;
        if (typeof fun != "function")
            throw new TypeError();

        var res = new Array();
        var thisp = arguments[1];
        for (var i = 0; i < len; i++) {
            if (i in this) {
                var val = this[i]; // in case fun mutates this
                if (fun.call(thisp, val, i, this))
                    res.push(val);
            }
        }

        return res;
    };
}


// http://developer.mozilla.org/en/Core_JavaScript_1.5_Reference/Objects/Array/forEach
// Also present in ES5 DRAFT
if (!Array.prototype.forEach) {
    Array.prototype.forEach = function(fun /*, thisp*/) {
        var len = this.length;
        if (typeof fun != "function")
            throw new TypeError();

        var thisp = arguments[1];
        for (var i = 0; i < len; i++) {
            if (i in this)
                fun.call(thisp, this[i], i, this);
        }
    };
}

// http://developer.mozilla.org/En/Core_JavaScript_1.5_Reference:Objects:Array:map
// Also present in ES5 DRAFT
if (!Array.prototype.map) {
    Array.prototype.map = function(fun /*, thisp*/) {
        var len = this.length;
        if (typeof fun != "function")
            throw new TypeError();

        var res = new Array(len);
        var thisp = arguments[1];
        for (var i = 0; i < len; i++) {
            if (i in this)
                res[i] = fun.call(thisp, this[i], i, this);
        }

        return res;
    };
}

// http://developer.mozilla.org/en/Core_JavaScript_1.5_Reference/Objects/Array/some
// Also present in ES5 DRAFT
if (!Array.prototype.some) {
    Array.prototype.some = function(fun /*, thisp*/) {
        var len = this.length;
        if (typeof fun != "function")
            throw new TypeError();

        var thisp = arguments[1];
        for (var i = 0; i < len; i++) {
            if (i in this &&
          fun.call(thisp, this[i], i, this))
                return true;
        }

        return false;
    };
}


//----------------------------------------------------------------------
// Introduced in JavaScript 1.8
//----------------------------------------------------------------------

// http://developer.mozilla.org/En/Core_JavaScript_1.5_Reference:Objects:Array:reduce
// Also present in ES5 DRAFT
if (!Array.prototype.reduce) {
    Array.prototype.reduce = function(fun /*, initial*/) {
        var len = this.length;
        if (typeof fun != "function")
            throw new TypeError();

        // no value to return if no initial value and an empty array
        if (len == 0 && arguments.length == 1)
            throw new TypeError();

        var i = 0;
        if (arguments.length >= 2) {
            var rv = arguments[1];
        }
        else {
            do {
                if (i in this) {
                    rv = this[i++];
                    break;
                }

                // if array contains no values, no initial value to return
                if (++i >= len)
                    throw new TypeError();
            }
            while (true);
        }

        for (; i < len; i++) {
            if (i in this)
                rv = fun.call(null, rv, this[i], i, this);
        }

        return rv;
    };
}


// http://developer.mozilla.org/en/Core_JavaScript_1.5_Reference/Objects/Array/reduceRight
// Also present in ES5 DRAFT
if (!Array.prototype.reduceRight) {
    Array.prototype.reduceRight = function(fun /*, initial*/) {
        var len = this.length;
        if (typeof fun != "function")
            throw new TypeError();

        // no value to return if no initial value, empty array
        if (len == 0 && arguments.length == 1)
            throw new TypeError();

        var i = len - 1;
        if (arguments.length >= 2) {
            var rv = arguments[1];
        }
        else {
            do {
                if (i in this) {
                    rv = this[i--];
                    break;
                }

                // if array contains no values, no initial value to return
                if (--i < 0)
                    throw new TypeError();
            }
            while (true);
        }

        for (; i >= 0; i--) {
            if (i in this)
                rv = fun.call(null, rv, this[i], i, this);
        }

        return rv;
    };
}


//----------------------------------------------------------------------
// Introduced in JavaScript 1.8.1
//----------------------------------------------------------------------

// https://developer.mozilla.org/en/New_in_JavaScript_1.8.1

// See http://blog.stevenlevithan.com/archives/faster-trim-javascript
// for explanation of \s\s* syntax (short answer: triggers optimizations)

if (!String.prototype.trimLeft) {
    String.prototype.trimLeft = function() {
        return this.replace(/^\s\s*/, "");
    };
}

if (!String.prototype.trimRight) {
    String.prototype.trimRight = function() {
        return this.replace(/\s\s*$/, "");
    };
}

// String.prototype.trim() also present in ES5 DRAFT
if (!String.prototype.trim) {
    String.prototype.trim = function() {
        return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
    };
}


//----------------------------------------------------------------------
// Introduced in ES5 DRAFT
//----------------------------------------------------------------------

// ES5 DRAFT: 15.2.3.14 Object.keys( O )
// http://ejohn.org/blog/ecmascript-5-objects-and-properties/
if (!Object.keys) {
    Object.keys = function(obj) {
        var array = [];
        for (var prop in obj) {
            if (obj.hasOwnProperty(prop)) {
                array.push(prop);
            }
        }
        return array;
    };
}

// ES5 DRAFT: 15.4.3.2 Array.isArray( arg )
// http://ejohn.org/blog/ecmascript-5-strict-mode-json-and-more/
if (!Array.isArray) {
    Array.isArray = function(array) {
        return Object.prototype.toString.call(array) === "[object Array]";
    };
}

// ES5 DRAFT: 15.3.4.5 Function.prototype.bind( thisArg [, arg1 [, arg2, ... ]] )
// Inspired by http://www.prototypejs.org/api/function/bind
if (!Function.prototype.bind) {
    Function.prototype.bind = function() {

        function toArray(iter) {
            var array = [];
            for (var i = 0, len = iter.length; i < len; i += 1) {
                array.push(iter[i]);
            }
            return array;
        }

        var target = this;
        var boundArgs = toArray(arguments);
        var thisObj = boundArgs.shift();
        return function() {
            var args = boundArgs.concat(toArray(arguments));
            return target.apply(thisObj, args);
        };
    };
}

// ES5 DRAFT: 15.9.4.44: Date.prototype.toISOString()
// Inspired by http://www.json.org/json2.js
if (!Date.prototype.toISOString) {
    Date.prototype.toISOString = function(key) {
        function pad(n, v) {
            v = '' + v;
            while (v.length < n) {
                v = '0' + v;
            }
            return v;
        }

        return this.getUTCFullYear() + '-' +
            pad(2, this.getUTCMonth() + 1) + '-' +
            pad(2, this.getUTCDate()) + 'T' +
            pad(2, this.getUTCHours()) + ':' +
            pad(2, this.getUTCMinutes()) + ':' +
            pad(2, this.getUTCSeconds()) + '.' +
            pad(3, this.getUTCMilliseconds()) + 'Z';
    };
}
