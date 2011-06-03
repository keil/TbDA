(function() {
	
	var globalObject = this;
	
	/**
	 * Version of Object.prototype.hasOwnProperty for browsers that don't
	 * support it (otherwise just use that).
	 * 
	 * @param {Object}
	 *            obj the object to test
	 * @param {Object}
	 *            prop the name of the property to test for
	 */
	var hasOwnProp = Object.prototype.hasOwnProperty || function(obj, prop) {
		var p = obj.constructor.prototype[prop];
		return typeof p === 'undefined' || p !== obj[prop];
	};

	if (!Object.create) {
		/**
		 * Modified version of Crockford/Cornford/Lasse Reichstein Nielsen's
		 * object function Modified to match EcmaScript 5th edtion Object.create
		 * (no support for setting other properties than value, though).
		 * 
		 * @see http://www.ecma-international.org/publications/standards/Ecma-262.htm
		 * @see http://groups.google.com/group/comp.lang.javascript/msg/e04726a66face2a2
		 * @param {Object} origin
		 * @param {Object} props a specification object describing overrides as in
		 *            EcmaScript 5th edition Object.create. Only supported
		 *            specification objects have a single property named value,
		 *            e.g. {value: 42}.
		 * @return object with <code>origin</code> as its prototype
		 */
		Object.create = (function() {
			function F() {}
			return function(o, props) {
				F.prototype = o;
				var res = new F(), 
				    p;
				if (props) {
					for (p in props) {
						if (hasOwnProp.call(props, p)) {
							res[p] = props[p].value;
						}
					}
				}
				return res;
			};
		})();
	}

	 
	var	validIdentifier = /^(?:[a-zA-Z_]\w*[.])*[a-zA-Z_]\w*$/;
	/**
	 * This function constructs a namespace using a namespace specification
	 * object.
	 * 
	 * <h3>Example 1</h3>
	 * <code>
	 * com.trifork.common.Common.namespace({
	 *    com: {
	 *      trifork: {
	 *          utils: {},
	 *          myapplication: ['model','view', 'controller']
	 *      }
	 *    }
	 * });
	 * </code>
	 * 
	 * <h3>Example 2</h3>
	 * <code>
	 * com.trifork.common.Common.namespace({
	 *    B1:{}, B2:["B21","B22"]
	 * },B);
	 * </code>
	 * Extends B with two properties: B1 and B2. B1 is an empty object, while B2
	 * has two properties B21 and B22, which in turn are empty objects.
	 * 
	 * @param {Object}
	 *            spec a namespace specification
	 * @param {Object}
	 *            context (Optional) used to extend namespaces (see example 2
	 *            above): defaults to window.
	 * @return the last/"deepest" object in the defined namespace (in case spec
	 *         is a string).
	 */
	function namespace(spec, context) {
		var i,N, // iteration vars
		    tmp;
		context = context || globalObject;
		spec = spec.valueOf();// to reduce e.g., new String("com") to "com"
		if (typeof spec === 'object') {
			if (typeof spec.length === 'number') {// assume an array-like object
				i = spec.length;
				while (i--) {
					tmp = namespace(spec[i], context);
				}
				return tmp;
			} else {// spec is a specification object e.g, 
				//{com: {trifork: ['model,view']}}
				for (i in spec) {
					if (hasOwnProp(spec,i)) {
						context[i] = context[i] || {};
						tmp = namespace(spec[i], context[i]);// recursively descend tree
					}
				}
				return tmp;
			}
		} else if (typeof spec === 'string') {
			if (!validIdentifier.test(spec)) {
				throw new Error('"' + spec + '" is not a valid name for a package.');
			}
            while (true) {
                i = spec.indexOf(".");
                if (i < 0) {
                    tmp = spec;
                } else {
                    tmp = spec.substring(0,i);
                    spec = spec.substring(i+1);
                }
                context[tmp] = context[tmp] || {};
                context = context[tmp];
                if (i < 0) {break;}
            }
			return context;
		} else {
			throw new TypeError(spec + " is not a valid specification for namespace");
		}
	}

	/**
	* Kind of a trivial function ;-) it is included for writing readable
	* JavaScript which uses packages.
	* 
	* using(X).run(f) is similar to f(X), but f is called with
	* <code>this</code> set to X.
	* 
	* More precisely <code>using(a1,a2,...,an).run(f)</code> is equivalent to
	* <code>f.apply(arguments[0],arguments)</code>.
	* 
	* This provides a form of syntactic sugar.
	* 
	* Example: Suppose you've done
	* <code>com.trifork.common.Common.namespace({Long:{Boring:"Namespace"}}});</code>
	* Now you want define an object in the Long.Boring.Namespace object, say
	* Widget. Normally you would do <code>
	*  Long.Boring.Namespace.Widget = function ()  {//constructor}
	*  Long.Boring.Namespace.prototype.fn1 = ...;
	*  Long.Boring.Namespace.prototype.fn2 = ...;
	* </code>
	* Alternatively you can do: <code>
	* var shrt = Long.Boring.Namespace.Widget;
	* shrt.Widget ...
	* shrt.prototype.fn1...
	* </code>
	* But this breaks global namespace. Alternatively <code>
	* function() {
    * var shrt = Long.Boring.Namespace.Widget;
	*  ...
	* }();
	*  </code>
	* 
	* The <code>using</code> function is similar, it just reads nicer. For
	* example. <code>
	* using(Long.Boring.Namespace).run(function(ns) {
	* 
	*  this.Widget = ...;//or ns.Widget =...
	*  this.Widget.prototype.fn1 = ...;
	*  ...	
	* });
	* </code>
	* 
	* Alternatively you can let <code>fn</code> take a parameter which is
	* bound to <code>ns</code> (so you can use that name instead of
	* <code>this</code>). Variable args: the namespace objects (i.e.,
	* package)
	* 
	* @return {Object} an object containing a run function which calls an input
	*         parameter <code>fn</code> with <code>arguments</code>.
	*/
	var using = (function() {
		var args;
		var obj = {
			run : function(fn) {
				return fn.apply(args[0], args);
			}
		};
		return function() {
			args = arguments;
			return obj;
		};
	})();

	/**
	 * Expose public methods
	 */
	namespace("com.trifork.common").Common = {
		globalObject : globalObject,
		hasOwnProperty: hasOwnProp,
		namespace : namespace,
		using : using
	};
})();
