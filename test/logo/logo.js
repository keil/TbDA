//
// Logo Interpreter in Javascript
//

// Copyright 2009 Joshua Bell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/*jslint browser: true, sub: true, undef: true */
/*global window */

// Based on: http://www.jbouchard.net/chris/blog/2008/01/currying-in-javascript-fun-for-whole.html
if (!Function.prototype.toArity) {

    Function.prototype.toArity = function(arity) {
        var func = this;
        var parms = [];

        if (func.length === arity) {
            return func;
        }

        for (var i = 0; i < arity; i += 1) {
            parms.push('a' + i);
        }

        /*jslint evil: true */
        var f = eval('(function (' + parms.join(',') + ') { return func.apply(this, arguments); })');
        /*jslint evil: false */
        return f;
    };
}

//----------------------------------------------------------------------
function LogoInterpreter(turtle, stream)
//----------------------------------------------------------------------
{
    var self = this;

    var UNARY_MINUS = '<UNARYMINUS>'; // Must not match regexIdentifier

    //----------------------------------------------------------------------
    //
    // Interpreter State
    //
    //----------------------------------------------------------------------

    self.turtle = turtle;
    self.stream = stream;
    self.routines = {};
    self.scopes = [{}];


    //----------------------------------------------------------------------
    //
    // Parsing
    //
    //----------------------------------------------------------------------

    // Used to return values from routines (thrown/caught)
    function Output(output) { this.output = output; }
    Output.prototype.toString = function() { return this.output; };
    Output.prototype.valueOf = function() { return this.output; };

    // Used to stop processing cleanly
    function Bye() { }


    function Type(atom) {
        if (typeof atom === 'undefined') {
            throw "Undefined";
        }
        else if (typeof atom === 'string') {
            return 'word';
        }
        else if (typeof atom === 'number') {
            return 'number';
        }
        else if (Array.isArray(atom)) {
            return 'list';
        }
        else if (!atom) {
            throw "Null type!";
        }
        else {
            throw "Unknown type!";
        }
    } // Type

    var regexIdentifier = /^(\.?[A-Za-z][A-Za-z0-9_.\?]*)(.*?)$/;
    var regexStringLiteral = /^("[^ \[\]\(\)]*)(.*?)$/;
    var regexVariableLiteral = /^(:[A-Za-z][A-Za-z0-9]*)(.*?)$/;
    var regexNumberLiteral = /^([0-9]*\.?[0-9]+(?:[eE]\s*[\-+]?\s*[0-9]+)?)(.*?)$/;
    var regexListDelimiter = /^(\[|\])(.*?)$/;
    var regexOperator = /^(\+|\-|\*|\/|%|\^|>=|<=|<>|=|<|>|\[|\]|\(|\))(.*?)$/;
    var regexInfix = /^(\+|\-|\*|\/|%|\^|>=|<=|<>|=|<|>)$/;

    //
    // Construct a parse tree
    //
    // Input: string
    // Output: atom list (e.g. "to", "jump", "repeat", "random", 10, [ "fd", 10, "rt" 10 ], "end"
    //
    // TODO: Move this into expression parsing; should be '[' operator's job!

    //----------------------------------------------------------------------
    function parse(string)
    //----------------------------------------------------------------------
    {
        if (typeof string === 'undefined') {
            return undefined; // TODO: Replace this with ...?
        }

        var atoms = [];
        var prev;

        // Filter out comments
        string = string.replace(/;.*\n/g, '');

        // Treat newlines as whitespace (so \s will match)
        string = string.replace(/\r/g, '').replace(/\n/g, ' ');
        string = string.replace(/^\s+/, '').replace(/\s+$/, '');

        while (typeof string !== 'undefined' && string !== '') {
            var atom;

            // Ignore (but track) leading space - needed for unary minus disambiguation
            var leading_space = /^\s+/.test(string);
            string = string.replace(/^\s+/, '');

            if (string.match(regexIdentifier) ||
                string.match(regexStringLiteral) ||
                string.match(regexVariableLiteral)) {
                // Word
                atom = RegExp.$1;
                string = RegExp.$2;
            }
            else if (string.match(regexNumberLiteral)) {
                // Number literal
                atom = RegExp.$1;
                string = RegExp.$2;

                // The following dirties RegExp.$n so it is kept separate
                atom = parseFloat(atom.replace(/\s+/g, ''), 10);
            }
            else if (string.match(regexListDelimiter)) {

                if (RegExp.$1 === '[') {
                    // Start of list - recurse!
                    var r = parse(RegExp.$2);
                    if (!r.list) { throw "Expected end-of-list"; }
                    atom = r.list;
                    string = r.string;
                }
                else { // (RegExp.$1 === ']')
                    // End of list - return list and the remaining input
                    return { list: atoms, string: RegExp.$2 };
                }

            }
            else if (string.match(regexOperator)) {

                atom = RegExp.$1;
                string = RegExp.$2;

                // From UCB Logo:

                // Minus sign means infix difference in ambiguous contexts
                // (when preceded by a complete expression), unless it is
                // preceded by a space and followed by a nonspace.

                // Minus sign means unary minus if the previous token is an
                // infix operator or open parenthesis, or it is preceded by
                // a space and followed by a nonspace.

                if (atom === '-') {

                    var trailing_space = /^\s+/.test(string);

                    if (typeof prev === 'undefined' ||
                            (Type(prev) === 'word' && regexInfix.test(prev)) ||
                            (Type(prev) === 'word' && prev === '(') ||
                            (leading_space && !trailing_space)
                           ) {
                        atom = UNARY_MINUS;
                    }

                }
            }
            else {
                throw "Couldn't parse: '" + string + "'";
            }


            atoms.push(atom);
            prev = atom;
        }

        return atoms;

    } // parse


    //----------------------------------------------------------------------
    self.getvar = function(name)
    //----------------------------------------------------------------------
    {
        name = name.toLowerCase();

        for (var i = 0; i < self.scopes.length; ++i) {
            if (self.scopes[i].hasOwnProperty(name)) {
                return self.scopes[i][name];
            }
        }
        throw "Don't know about variable " + name;
    };

    //----------------------------------------------------------------------
    self.setvar = function(name, value)
    //----------------------------------------------------------------------
    {
        name = name.toLowerCase();

        // Find the variable in existing scope
        for (var i = 0; i < self.scopes.length; ++i) {
            if (self.scopes[i].hasOwnProperty(name)) {
                self.scopes[i][name] = value;
                return;
            }
        }

        // Otherwise, define a global
        self.scopes[self.scopes.length - 1][name] = value;
    };

    //----------------------------------------------------------------------
    //
    // Expression Evaluation
    //
    //----------------------------------------------------------------------

    // Expression               := RelationalExpression
    // RelationalExpression     := AdditiveExpression [ ( '=' | '<' | '>' | '<=' | '>=' | '<>' ) AdditiveExpression ... ]
    // AdditiveExpression       := MultiplicativeExpression [ ( '+' | '-' ) MultiplicativeExpression ... ]
    // MultiplicativeExpression := PowerExpression [ ( '*' | '/' | '%' ) PowerExpression ... ]
    // PowerExpression          := UnaryExpression [ '^' UnaryExpression ]
    // UnaryExpression          := ( '-' ) UnaryExpression
    //                           | FinalExpression
    // FinalExpression          := string-literal
    //                           | number-literal
    //                           | list
    //                           | variable-reference
    //                           | procedure-call
    //                           | '(' Expression ')'

    //----------------------------------------------------------------------
    // Peek at the list to see if there are additional atoms from a set 
    // of options. 
    //----------------------------------------------------------------------
    function peek(list, options)
    //----------------------------------------------------------------------
    {
        if (list.length < 1) { return false; }
        var next = list[0];
        return options.some(function(x) { return next === x; });

    } // peek

    //----------------------------------------------------------------------
    self.evaluateExpression = function(list)
    //----------------------------------------------------------------------
    {
        return (self.expression(list))();

    };

    //----------------------------------------------------------------------
    self.expression = function(list)
    //----------------------------------------------------------------------
    {
        return self.relationalExpression(list);

    };

    //----------------------------------------------------------------------
    self.relationalExpression = function(list)
    //----------------------------------------------------------------------
    {
        var lhs = self.additiveExpression(list);
        var op;
        while (peek(list, ['=', '<', '>', '<=', '>=', '<>'])) {
            op = list.shift();

            lhs = function(lhs) {
                var rhs = self.additiveExpression(list);

                switch (op) {
                    case "<": return function() { return (aexpr(lhs()) < aexpr(rhs())) ? 1 : 0; };
                    case ">": return function() { return (aexpr(lhs()) > aexpr(rhs())) ? 1 : 0; };
                    case "=": return function() { return self.equal(lhs(), rhs()) ? 1 : 0; };

                    case "<=": return function() { return (aexpr(lhs()) <= aexpr(rhs())) ? 1 : 0; };
                    case ">=": return function() { return (aexpr(lhs()) >= aexpr(rhs())) ? 1 : 0; };
                    case "<>": return function() { return !self.equal(lhs(), rhs()) ? 1 : 0; };
                }
            } (lhs);
        }

        return lhs;
    };

    //----------------------------------------------------------------------
    self.additiveExpression = function(list)
    //----------------------------------------------------------------------
    {
        var lhs = self.multiplicativeExpression(list);
        var op;
        while (peek(list, ['+', '-'])) {
            op = list.shift();

            lhs = function(lhs) {
                var rhs = self.multiplicativeExpression(list);
                switch (op) {
                    case "+": return function() { return aexpr(lhs()) + aexpr(rhs()); };
                    case "-": return function() { return aexpr(lhs()) - aexpr(rhs()); };
                }
            } (lhs);
        }

        return lhs;
    };

    //----------------------------------------------------------------------
    self.multiplicativeExpression = function(list)
    //----------------------------------------------------------------------
    {
        var lhs = self.powerExpression(list);
        var op;
        while (peek(list, ['*', '/', '%'])) {
            op = list.shift();

            lhs = function(lhs) {
                var rhs = self.powerExpression(list);
                switch (op) {
                    case "*": return function() { return aexpr(lhs()) * aexpr(rhs()); };
                    case "/": return function() {
                        var n = aexpr(lhs()), d = aexpr(rhs());
                        if (d === 0) { throw "Division by zero"; }
                        return n / d;
                    };
                    case "%": return function() {
                        var n = aexpr(lhs()), d = aexpr(rhs());
                        if (d === 0) { throw "Division by zero"; }
                        return n % d;
                    };
                }
            } (lhs);
        }

        return lhs;
    };

    //----------------------------------------------------------------------
    self.powerExpression = function(list)
    //----------------------------------------------------------------------
    {
        var lhs = self.unaryExpression(list);
        var op;
        while (peek(list, ['^'])) {
            op = list.shift();
            lhs = function(lhs) {
                var rhs = self.unaryExpression(list);
                return function() { return Math.pow(aexpr(lhs()), aexpr(rhs())); };
            } (lhs);
        }

        return lhs;
    };

    //----------------------------------------------------------------------
    self.unaryExpression = function(list)
    //----------------------------------------------------------------------
    {
        var rhs, op;

        if (peek(list, [UNARY_MINUS])) {
            op = list.shift();
            rhs = self.unaryExpression(list);
            return function() { return -aexpr(rhs()); };
        }
        else {
            return self.finalExpression(list);
        }
    };


    //----------------------------------------------------------------------
    self.finalExpression = function(list)
    //----------------------------------------------------------------------
    {
        if (!list.length) {
            throw "Unexpected end of instructions";
        }

        var atom = list.shift();

        var args, i, routine, result;
        var literal, varname;

        switch (Type(atom)) {
            case 'number':
            case 'list':
                return function() { return atom; };

            case 'word':
                if (atom.charAt(0) === '"') {
                    // string literal
                    literal = atom.substring(1);
                    return function() { return literal; };
                }
                else if (atom.charAt(0) === ':') {
                    // variable
                    varname = atom.substring(1);
                    return function() { return self.getvar(varname); };
                }
                else if (atom === '(') {
                    // parenthesized expression/procedure call
                    if (list.length && Type(list[0]) === 'word' && 
                        self.routines[list[0].toString().toLowerCase()]) {

                        // Lisp-style (procedure input ...) calling syntax
                        atom = list.shift();
                        return self.dispatch(atom, list, false);
                    }
                    else {
                        // Standard parenthesized expression
                        result = self.expression(list);

                        if (!peek(list, [')'])) {
                            throw "Expected ')', saw " + list.shift();
                        }
                        list.shift();
                        return result;
                    }
                }
                else {
                    // Procedure dispatch
                    return self.dispatch(atom, list, true);
                }
                break;

            default:
                throw "Unexpected: " + atom;
        }
    };

    //----------------------------------------------------------------------
    self.dispatch = function(name, tokenlist, natural)
    //----------------------------------------------------------------------
    {
        var procedure = self.routines[name.toLowerCase()];
        if (!procedure) { throw "Don't know how to " + name; }

        if (procedure.special) {
            // Special routines are built-ins that get handed the token list:
            // * workspace modifiers like TO that special-case varnames
            procedure(tokenlist);
            return function() { };
        }

        var args = [];
        if (natural) {
            // Natural arity of the function
            for (var i = 0; i < procedure.length; ++i) {
                args.push(self.expression(tokenlist));
            }
        }
        else {
            // Caller specified argument count
            while (tokenlist.length && !peek(tokenlist, [')'])) {
                args.push(self.expression(tokenlist));
            }
            tokenlist.shift(); // Consume ')'
        }

        if (procedure.noeval) {
            return function() {
                return procedure.apply(procedure, args);
            };
        }
        else {
            return function() {
                return procedure.apply(procedure, args.map(function(a) { return a(); }));
            };
        }
    };

    //----------------------------------------------------------------------
    // Arithmetic expression convenience function
    //----------------------------------------------------------------------
    function aexpr(atom)
    //----------------------------------------------------------------------
    {
        if (Type(atom) === 'number') { return atom; }
        if (Type(atom) === 'word') { return parseFloat(atom); } // coerce

        throw "Expected number";
    }

    //----------------------------------------------------------------------
    // String expression convenience function
    //----------------------------------------------------------------------
    function sexpr(atom)
    //----------------------------------------------------------------------
    {
        if (Type(atom) === 'word') { return atom; }
        if (Type(atom) === 'number') { return atom.toString(); } // coerce

        throw "Expected string";
    }

    //----------------------------------------------------------------------
    // List expression convenience function
    //----------------------------------------------------------------------
    function lexpr(atom) {
        // TODO: If this is an input, output needs to be re-stringified
        if (Type(atom) === 'number') { return Array.prototype.map.call(atom.toString(), function(x) { return x; }); }
        if (Type(atom) === 'word') { return Array.prototype.map.call(atom, function(x) { return x; }); }
        if (Type(atom) === 'list') { return atom; }

        throw "Expected list";
    }

    //----------------------------------------------------------------------
    // Deep compare of values (numbers, strings, lists)
    // (with optional epsilon compare for numbers)
    //----------------------------------------------------------------------
    self.equal = function(a, b, epsilon)
    //----------------------------------------------------------------------
    {
        if (Array.isArray(a)) {
            if (!Array.isArray(b)) {
                return false;
            }
            if (a.length !== b.length) {
                return false;
            }
            for (var i = 0; i < a.length; i += 1) {
                if (!self.equal(a[i], b[i])) {
                    return false;
                }
            }
            return true;
        }
        else if (typeof a !== typeof b) {
            return false;
        }
        else if (typeof epsilon !== 'undefined' && typeof a === 'number') {
            return Math.abs(a - b) < epsilon;
        }
        else {
            return a === b;
        }
    };

    //----------------------------------------------------------------------
    //
    // Execute a script
    //
    //----------------------------------------------------------------------

    //----------------------------------------------------------------------
    // Execute a sequence of statements
    //----------------------------------------------------------------------
    self.execute = function(statements)
    //----------------------------------------------------------------------
    {
        // Operate on a copy so the original is not destroyed
        statements = statements.slice();

        var result;
        while (statements.length) {
            result = self.evaluateExpression(statements);
        }

        // Return last result
        return result;

    };


    //----------------------------------------------------------------------
    self.run = function(string)
    //----------------------------------------------------------------------
    {
        if (self.turtle) { self.turtle.begin(); }

        try {
            // Parse it
            var atoms = parse(string);

            // And execute it!
            return self.execute(atoms);
        }
        catch (e) {
            if (e instanceof Bye) {
                // clean exit
                return;
            }
            else {
                throw e;
            }
        }
        finally {
            if (self.turtle) { self.turtle.end(); }
        }
    };


    //----------------------------------------------------------------------
    //
    // Built-In Proceedures
    //
    //----------------------------------------------------------------------

    // Basic form:
    //
    //  self.routines["procname"] = function(input1, input2, ...) { ... return output; }
    //   * inputs are JavaScript strings, numbers, or Arrays
    //   * output is string, number, Array or undefined/no output
    //
    // Special forms:
    //
    //   self.routines["procname"] = function(tokenlist) { ... }
    //   self.routines["procname"].special = true
    //    * input is Array (list) of tokens (words, numbers, Arrays)
    //    * used for implementation of special forms (e.g. TO inputs... statements... END)
    //
    //   self.routines["procname"] = function(finput1, finput2, ...) { ... return output; }
    //   self.routines["procname"].noeval = true
    //    * inputs are arity-0 functions that evaluate to string, number Array
    //    * used for short-circuiting evaluation (AND, OR)
    //    * used for repeat evaluation (DO.WHILE, WHILE, DO.UNTIL, UNTIL)
    //


    function mapreduce(list, mapfunc, reducefunc, initial) {
        // NOTE: Uses Array.XXX format to handle array-like types: arguments and strings
        if (typeof initial === 'undefined') {
            return Array.prototype.reduce.call(Array.prototype.map.call(list, mapfunc), reducefunc);
        }
        else {
            return Array.prototype.reduce.call(Array.prototype.map.call(list, mapfunc), reducefunc, initial);
        }
    }

    function stringify(thing) {

        if (Type(thing) === 'list') {
            return "[ " + thing.map(stringify).join(" ") + " ]";
        }
        else {
            return sexpr(thing);
        }
    }

    function stringify_nodecorate(thing) {

        if (Type(thing) === 'list') {
            return thing.map(stringify).join(" ");
        }
        else {
            return stringify(thing);
        }
    }

    //
    // Procedures and Flow Control
    //
    self.routines["to"] = function(list) {
        var name = list.shift();
        if (!name.match(regexIdentifier)) {
            throw "Expected identifier";
        }
        name = name.toLowerCase();

        var inputs = [];
        var block = [];

        // Process inputs, then the statements of the block
        var state_inputs = true;
        while (list.length) {
            var atom = list.shift();
            if (Type(atom) === 'word' && atom === 'end') {
                break;
            }
            else if (state_inputs && Type(atom) === 'word' && atom.charAt(0) === ':') {
                inputs.push(atom.substring(1));
            }
            else {
                state_inputs = false;
                block.push(atom);
            }
        }

        // Closure over inputs and block to handle scopes, arguments and outputs
        var func = function() {

            // Define a new scope
            var scope = {};
            for (var i = 0; i < inputs.length && i < arguments.length; i += 1) {
                scope[inputs[i]] = arguments[i];
            }
            self.scopes.unshift(scope);

            try {
                // Execute the block
                try {
                    return self.execute(block);
                }
                catch (e) {
                    // From OUTPUT
                    if (e instanceof Output) {
                        return e.output;
                    }
                    else {
                        throw e;
                    }
                }
            }
            finally {
                // Close the scope
                self.scopes.shift();
            }
        };

        self.routines[name] = func.toArity(inputs.length);

        // For DEF de-serialization
        self.routines[name].inputs = inputs;
        self.routines[name].block = block;
    };
    self.routines["to"].special = true;

    self.routines["def"] = function(list) {

        function defn(atom) {
            switch (Type(atom)) {
                case 'word': return atom;
                case 'number': return atom.toString();
                case 'list': return '[ ' + atom.map(defn).join(' ') + ' ]';
            }
        }

        var name = sexpr(list);
        var proc = self.routines[name.toLowerCase()];
        if (!proc) { throw "Don't know how to " + name; }
        if (!proc.inputs) { throw "Can't show definition of intrinsic " + name; }

        var def = "to " + name + " ";
        if (proc.inputs.length) {
            def += proc.inputs.map(function(a) { return ":" + a; }).join(" ");
            def += " ";
        }
        def += proc.block.map(defn).join(" ").replace(new RegExp(UNARY_MINUS + ' ', 'g'), '-');
        def += " end";

        return def;
    };


    //----------------------------------------------------------------------
    //
    // 2. Data Structure Primitives
    //
    //----------------------------------------------------------------------

    //
    // 2.1 Constructors
    //

    self.routines["word"] = function(word1, word2) {
        return arguments.length ? mapreduce(arguments, sexpr, function(a, b) { return a + " " + b; }) : "";
    };

    self.routines["list"] = function(thing1, thing2) {
        return Array.prototype.map.call(arguments, function(x) { return x; }); // Make a copy
    };

    self.routines["sentence"] = self.routines["se"] = function(thing1, thing2) {
        var list = [];
        for (var i = 0; i < arguments.length; i += 1) {
            var thing = arguments[i];
            if (Type(thing) === 'list') {
                thing = lexpr(thing);
                list = list.concat(thing);
            }
            else {
                list.push(thing);
            }
        }
        return list;
    };

    self.routines["fput"] = function(thing, list) { list = lexpr(list); list.unshift(thing); return list; };

    self.routines["lput"] = function(thing, list) { list = lexpr(list); list.push(thing); return list; };

    // Not Supported: array
    // Not Supported: mdarray
    // Not Supported: listtoarray
    // Not Supported: arraytolist

    self.routines["combine"] = function(thing1, thing2) {
        if (Type(thing2) !== 'list') {
            return self.routines['word'](thing1, thing2);
        }
        else {
            return self.routines['fput'](thing1, thing2);
        }
    };

    self.routines["reverse"] = function(list) { return lexpr(list).slice().reverse(); };

    var gensym_index = 0;
    self.routines["gensym"] = function() {
        gensym_index += 1;
        return 'G' + gensym_index;
    };

    //
    // 2.2 Data Selectors
    //

    self.routines["first"] = function(list) { return lexpr(list)[0]; };

    self.routines["firsts"] = function(list) {
        return lexpr(list).map(function(x) { return x[0]; });
    };

    self.routines["last"] = function(list) { list = lexpr(list); return list[list.length - 1]; };

    self.routines["butfirst"] = self.routines["bf"] = function(list) { return lexpr(list).slice(1); };

    self.routines["butfirsts"] = self.routines["bfs"] = function(list) {
        return lexpr(list).map(function(x) { return lexpr(x).slice(1); });
    };

    self.routines["butlast"] = self.routines["bl"] = function(list) { return lexpr(list).slice(0, -1); };

    self.routines["item"] = function(index, list) {
        index = aexpr(index);
        if (index < 1 || index > list.length) {
            throw "index out of bounds";
        }
        return lexpr(list)[index - 1];
    };

    // Not Supported: mditem

    self.routines["pick"] = function(list) {
        list = lexpr(list);
        var i = Math.floor(Math.random() * list.length);
        return list[i];
    };

    self.routines["remove"] = function(thing, list) {
        return lexpr(list).filter(function(x) { return x !== thing; });
    };

    self.routines["remdup"] = function(list) {
        var dict = {};
        return lexpr(list).filter(function(x) { if (!dict[x]) { dict[x] = true; return true; } else { return false; } });
    };

    // TODO: quoted

    //
    // 2.3 Data Mutators
    //

    // Not Supported: setitem
    // Not Supported: mdsetitem
    // Not Supported: .setfirst
    // Not Supported: .setbf
    // Not Supported: .setitem

    self.routines["push"] = function(stackname, thing) {
        var stack = lexpr(self.getvar(stackname));
        stack.unshift(thing);
        self.setvar(stackname, stack);
    };

    self.routines["pop"] = function(stackname) {
        return self.getvar(stackname).shift();
    };

    self.routines["queue"] = function(stackname, thing) {
        var stack = lexpr(self.getvar(stackname));
        stack.push(thing);
        self.setvar(stackname, stack);
    };

    // NOTE: Same as "pop" (!?!)
    self.routines["dequeue"] = function(stackname) {
        return self.getvar(stackname).shift();
    };

    //
    // 2.4 Predicates
    //


    self.routines["wordp"] = self.routines["word?"] = function(thing) { return Type(thing) === 'word' ? 1 : 0; };
    self.routines["listp"] = self.routines["list?"] = function(thing) { return Type(thing) === 'list' ? 1 : 0; };
    // Not Supported: arrayp
    self.routines["numberp"] = self.routines["number?"] = function(thing) { return Type(thing) === 'number' ? 1 : 0; };
    self.routines["numberwang"] = function(thing) { return Math.random() < 0.5 ? 1 : 0; };

    self.routines["equalp"] = self.routines["equal?"] = function(a, b) { return self.equal(a, b) ? 1 : 0; };
    self.routines["notequalp"] = self.routines["notequal?"] = function(a, b) { return !self.equal(a, b) ? 1 : 0; };

    self.routines["emptyp"] = self.routines["empty?"] = function(thing) { return lexpr(thing).length === 0 ? 1 : 0; };
    self.routines["beforep"] = self.routines["before?"] = function(word1, word2) { return sexpr(word1) < sexpr(word2) ? 1 : 0; };

    // Not Supported: .eq
    // Not Supported: vbarredp

    self.routines["memberp"] = self.routines["member?"] =
        function(thing, list) {
            return lexpr(list).some(function(x) { return self.equal(x, thing); }) ? 1 : 0;
        };


    self.routines["substringp"] = self.routines["substring?"] =
        function(word1, word2) {
            return sexpr(word2).indexOf(sexpr(word1)) !== -1 ? 1 : 0;
        };

    //
    // 2.5 Queries
    //

    self.routines["count"] = function(thing) { return lexpr(thing).length; };
    self.routines["ascii"] = function(chr) { return sexpr(chr).charCodeAt(0); };
    // Not Supported: rawascii
    self.routines["char"] = function(integer) { return String.fromCharCode(aexpr(integer)); };
    self.routines["lowercase"] = function(word) { return sexpr(word).toLowerCase(); };
    self.routines["uppercase"] = function(word) { return sexpr(word).toUpperCase(); };
    self.routines["standout"] = function(word) { return sexpr(word); }; // For compat
    // Not Supported: parse
    // Not Supported: runparse

    //----------------------------------------------------------------------
    //
    // 3. Communication
    //
    //----------------------------------------------------------------------

    // 3.1 Transmitters

    self.routines["print"] = self.routines["pr"] = function(thing) {
        var s = Array.prototype.map.call(arguments, stringify_nodecorate).join(" ");
        self.stream.write(s, "\n");
        return s;
    };
    self.routines["type"] = function(thing) {
        var s = Array.prototype.map.call(arguments, stringify_nodecorate).join("");
        self.stream.write(s);
        return s;
    };
    self.routines["show"] = function(thing) {
        var s = Array.prototype.map.call(arguments, stringify).join(" ");
        self.stream.write(s, "\n");
        return s;
    };

    // 3.2 Receivers

    // Not Supported: readlist

    self.routines["readword"] = function() {
        if (arguments.length > 0) {
            return stream.read(sexpr(arguments[0]));
        }
        else {
            return stream.read();
        }
    };


    // Not Supported: readrawline
    // Not Supported: readchar
    // Not Supported: readchars
    // Not Supported: shell

    // 3.3 File Access

    // Not Supported: setprefix
    // Not Supported: prefix
    // Not Supported: openread
    // Not Supported: openwrite
    // Not Supported: openappend
    // Not Supported: openupdate
    // Not Supported: close
    // Not Supported: allopen
    // Not Supported: closeall
    // Not Supported: erasefile
    // Not Supported: dribble
    // Not Supported: nodribble
    // Not Supported: setread
    // Not Supported: setwrite
    // Not Supported: reader
    // Not Supported: writer
    // Not Supported: setreadpos
    // Not Supported: setwritepos
    // Not Supported: readpos
    // Not Supported: writepos
    // Not Supported: eofp
    // Not Supported: filep

    // 3.4 Terminal Access

    // Not Supported: keyp

    self.routines["cleartext"] = self.routines["ct"] = function() {
        self.stream.clear();
    };

    // Not Supported: setcursor
    // Not Supported: cursor
    // Not Supported: setmargins
    // Not Supported: settextcolor
    // Not Supported: increasefont
    // Not Supported: settextsize
    // Not Supported: textsize
    // Not Supported: setfont
    // Not Supported: font

    //----------------------------------------------------------------------
    //
    // 4. Arithmetic
    //
    //----------------------------------------------------------------------
    // 4.1 Numeric Operations


    self.routines["sum"] = function(a, b) {
        return mapreduce(arguments, aexpr, function(a, b) { return a + b; }, 0);
    };

    self.routines["difference"] = function(a, b) {
        return aexpr(a) - aexpr(b);
    };

    self.routines["minus"] = function(a) { return -aexpr(a); };

    self.routines["product"] = function(a, b) {
        return mapreduce(arguments, aexpr, function(a, b) { return a * b; }, 1);
    };

    self.routines["quotient"] = function(a, b) {
        if (typeof b !== 'undefined') {
            return aexpr(a) / aexpr(b);
        }
        else {
            return 1 / aexpr(a);
        }
    };

    self.routines["remainder"] = function(num1, num2) {
        return aexpr(num1) % aexpr(num2);
    };
    self.routines["modulo"] = function(num1, num2) {
        num1 = aexpr(num1);
        num2 = aexpr(num2);
        return Math.abs(num1 % num2) * (num2 < 0 ? -1 : 1);
    };

    self.routines["power"] = function(a, b) { return Math.pow(aexpr(a), aexpr(b)); };
    self.routines["sqrt"] = function(a) { return Math.sqrt(aexpr(a)); };
    self.routines["exp"] = function(a) { return Math.exp(aexpr(a)); };
    self.routines["log10"] = function(a) { return Math.log(aexpr(a)) / Math.LN10; };
    self.routines["ln"] = function(a) { return Math.log(aexpr(a)); };


    function deg2rad(d) { return d / 180 * Math.PI; }
    function rad2deg(r) { return r * 180 / Math.PI; }

    self.routines["arctan"] = function(a) {
        if (arguments.length > 1) {
            var x = aexpr(arguments[0]);
            var y = aexpr(arguments[1]);
            return rad2deg(Math.atan2(y, x));
        }
        else {
            return rad2deg(Math.atan(aexpr(a)));
        }
    };

    self.routines["sin"] = function(a) { return Math.sin(deg2rad(aexpr(a))); };
    self.routines["cos"] = function(a) { return Math.cos(deg2rad(aexpr(a))); };
    self.routines["tan"] = function(a) { return Math.tan(deg2rad(aexpr(a))); };

    self.routines["radarctan"] = function(a) {
        if (arguments.length > 1) {
            var x = aexpr(arguments[0]);
            var y = aexpr(arguments[1]);
            return Math.atan2(y, x);
        }
        else {
            return Math.atan(aexpr(a));
        }
    };

    self.routines["radsin"] = function(a) { return Math.sin(aexpr(a)); };
    self.routines["radcos"] = function(a) { return Math.cos(aexpr(a)); };
    self.routines["radtan"] = function(a) { return Math.tan(aexpr(a)); };

    self.routines["abs"] = function(a) { return Math.abs(aexpr(a)); };


    function truncate(x) { return parseInt(x, 10); }

    self.routines["int"] = function(a) { return truncate(aexpr(a)); };
    self.routines["round"] = function(a) { return Math.round(aexpr(a)); };

    self.routines["iseq"] = function(a, b) {
        a = truncate(aexpr(a));
        b = truncate(aexpr(b));
        var step = (a < b) ? 1 : -1;
        var list = [];
        for (var i = a; (step > 0) ? (i <= b) : (i >= b); i += step) {
            list.push(i);
        }
        return list;
    };


    self.routines["rseq"] = function(from, to, count) {
        from = aexpr(from);
        to = aexpr(to);
        count = truncate(aexpr(count));
        var step = (to - from) / (count - 1);
        var list = [];
        for (var i = from; (step > 0) ? (i <= to) : (i >= to); i += step) {
            list.push(i);
        }
        return list;
    };

    // 4.2 Numeric Predicates

    self.routines["greaterp"] = self.routines["greater?"] = function(a, b) { return aexpr(a) > aexpr(b) ? 1 : 0; };
    self.routines["greaterequalp"] = self.routines["greaterequal?"] = function(a, b) { return aexpr(a) >= aexpr(b) ? 1 : 0; };
    self.routines["lessp"] = self.routines["less?"] = function(a, b) { return aexpr(a) < aexpr(b) ? 1 : 0; };
    self.routines["lessequalp"] = self.routines["lessequal?"] = function(a, b) { return aexpr(a) <= aexpr(b) ? 1 : 0; };

    // 4.3 Random Numbers

    self.routines["random"] = function(max) {
        max = aexpr(max);
        return Math.floor(Math.random() * max);
    };

    // Not Supported: rerandom

    // 4.4 Print Formatting

    // Not Supported: form

    // 4.5 Bitwise Operations


    self.routines["bitand"] = function(num1, num2) {
        return mapreduce(arguments, aexpr, function(a, b) { return a & b; }, -1);
    };
    self.routines["bitor"] = function(num1, num2) {
        return mapreduce(arguments, aexpr, function(a, b) { return a | b; }, 0);
    };
    self.routines["bitxor"] = function(num1, num2) {
        return mapreduce(arguments, aexpr, function(a, b) { return a ^ b; }, 0);
    };
    self.routines["bitnot"] = function(num) {
        return ~aexpr(num);
    };


    self.routines["ashift"] = function(num1, num2) {
        num1 = truncate(aexpr(num1));
        num2 = truncate(aexpr(num2));
        return num2 >= 0 ? num1 << num2 : num1 >> -num2;
    };

    self.routines["lshift"] = function(num1, num2) {
        num1 = truncate(aexpr(num1));
        num2 = truncate(aexpr(num2));
        return num2 >= 0 ? num1 << num2 : num1 >>> -num2;
    };


    //----------------------------------------------------------------------
    //
    // 5. Logical Operations
    //
    //----------------------------------------------------------------------

    self.routines["true"] = function() { return 1; };
    self.routines["false"] = function() { return 0; };

    self.routines["and"] = function(a, b) {
        return Array.prototype.every.call(arguments, function(f) { return f(); }) ? 1 : 0;
    };
    self.routines["and"].noeval = true;

    self.routines["or"] = function(a, b) {
        return Array.prototype.some.call(arguments, function(f) { return f(); }) ? 1 : 0;
    };
    self.routines["or"].noeval = true;

    self.routines["xor"] = function(a, b) {
        function bool(x) { return !!x; }
        return mapreduce(arguments, aexpr, function(a, b) { return bool(a) !== bool(b); }, 0) ? 1 : 0;
    };
    self.routines["not"] = function(a) {
        return !aexpr(a) ? 1 : 0;
    };

    //----------------------------------------------------------------------
    //
    // 6. Graphics
    //
    //----------------------------------------------------------------------
    // 6.1 Turtle Motion

    self.routines["forward"] = self.routines["fd"] = function(a) { turtle.move(aexpr(a)); };
    self.routines["back"] = self.routines["bk"] = function(a) { turtle.move(-aexpr(a)); };
    self.routines["left"] = self.routines["lt"] = function(a) { turtle.turn(-aexpr(a)); };
    self.routines["right"] = self.routines["rt"] = function(a) { turtle.turn(aexpr(a)); };

    self.routines["setpos"] = function(l) { l = lexpr(l); turtle.setposition(aexpr(l[0]), aexpr(l[1])); };
    self.routines["setxy"] = function(x, y) { turtle.setposition(aexpr(x), aexpr(y)); };
    self.routines["setx"] = function(x) { turtle.setposition(aexpr(x), undefined); }; // TODO: Replace with ...?
    self.routines["sety"] = function(y) { turtle.setposition(undefined, aexpr(y)); };
    self.routines["setheading"] = self.routines["seth"] = function(a) { turtle.setheading(aexpr(a)); };

    self.routines["home"] = function() { turtle.home(); };

    // Not Supported: arc

    //
    // 6.2 Turtle Motion Queries
    //

    self.routines["pos"] = function() { var l = turtle.getxy(); return [l[0], l[1]]; };
    self.routines["xcor"] = function() { var l = turtle.getxy(); return l[0]; };
    self.routines["ycor"] = function() { var l = turtle.getxy(); return l[1]; };
    self.routines["heading"] = function() { return turtle.getheading(); };
    self.routines["towards"] = function(l) { l = lexpr(l); return turtle.towards(aexpr(l[0]), aexpr(l[1])); };

    // Not Supported: scrunch

    //
    // 6.3 Turtle and Window Control
    //

    self.routines["showturtle"] = self.routines["st"] = function() { turtle.showturtle(); };
    self.routines["hideturtle"] = self.routines["ht"] = function() { turtle.hideturtle(); };
    self.routines["clean"] = function() { turtle.clear(); };
    self.routines["clearscreen"] = self.routines["cs"] = function() { turtle.clearscreen(); };

    // Not Supported: wrap
    // Not Supported: window
    // Not Supported: fence
    // Not Supported: fill
    // Not Supported: filled

    self.routines["label"] = function(a) {
        var s = Array.prototype.map.call(arguments, stringify_nodecorate).join(" ");
        turtle.drawtext(s);
        return s;
    };

    self.routines["setlabelheight"] = function(a) { turtle.setfontsize(aexpr(a)); };

    // Not Supported: testscreen
    // Not Supported: fullscreen
    // Not Supported: splitscreen
    // Not Supported: setcrunch
    // Not Supported: refresh
    // Not Supported: norefresh

    //
    // 6.4 Turtle and Window Queries
    //

    self.routines["shownp"] = self.routines["shown?"] = function() {
        return turtle.isturtlevisible() ? 1 : 0;
    };

    // Not Supported: screenmode
    // Not Supported: turtlemode

    self.routines["labelsize"] = function() {
        return [turtle.getfontsize(), turtle.getfontsize()];
    };

    //
    // 6.5 Pen and Background Control
    //
    self.routines["pendown"] = self.routines["pd"] = function() { turtle.pendown(); };
    self.routines["penup"] = self.routines["pu"] = function() { turtle.penup(); };

    self.routines["penpaint"] = self.routines["ppt"] = function() { turtle.setpenmode('paint'); };
    self.routines["penerase"] = self.routines["pe"] = function() { turtle.setpenmode('erase'); };
    self.routines["penreverse"] = self.routines["px"] = function() { turtle.setpenmode('reverse'); };



    // Not Supported: penpaint
    // Not Supported: penerase
    // Not Supported: penreverse

    self.routines["setpencolor"] = self.routines["setpc"] = self.routines["setcolor"] = function(a) {
        if (arguments.length === 3) {
            var r = Math.round(aexpr(arguments[0]) * 255 / 99);
            var g = Math.round(aexpr(arguments[1]) * 255 / 99);
            var b = Math.round(aexpr(arguments[2]) * 255 / 99);
            var rr = (r < 16 ? "0" : "") + r.toString(16);
            var gg = (g < 16 ? "0" : "") + g.toString(16);
            var bb = (b < 16 ? "0" : "") + b.toString(16);
            turtle.setcolor('#' + rr + gg + bb);
        }
        else {
            turtle.setcolor(sexpr(a));
        }
    };

    // Not Supported: setpallete

    self.routines["setpensize"] = self.routines["setwidth"] = self.routines["setpw"] = function(a) {
        if (Type(a) === 'list') {
            turtle.setwidth(aexpr(a[0]));
        }
        else {
            turtle.setwidth(aexpr(a));
        }
    };

    // Not Supported: setpenpattern
    // Not Supported: setpen
    // Not Supported: setbackground

    //
    // 6.6 Pen Queries
    //

    self.routines["pendownp"] = self.routines["pendown?"] = function() {
        return turtle.ispendown() ? 1 : 0;
    };

    self.routines["penmode"] = self.routines["pc"] = function() {
        return turtle.getpenmode().toUpperCase();
    };

    self.routines["pencolor"] = self.routines["pc"] = function() {
        return turtle.getcolor();
    };

    // Not Supported: palette

    self.routines["pensize"] = function() {
        return [turtle.getwidth(), turtle.getwidth()];
    };

    // Not Supported: pen
    // Not Supported: background

    // 6.7 Saving and Loading Pictures

    // Not Supported: savepict
    // Not Supported: loadpict
    // Not Supported: epspict

    // 6.8 Mouse Queries

    // Not Supported: mousepos
    // Not Supported: clickpos
    // Not Supported: buttonp
    // Not Supported: button

    //----------------------------------------------------------------------
    //
    // 7. Workspace Management
    //
    //----------------------------------------------------------------------
    // 7.1 Procedure Definition
    // 7.2 Variable Definition

    self.routines["make"] = function(varname, value) {
        self.setvar(sexpr(varname), value);
    };

    self.routines["name"] = function(value, varname) {
        self.setvar(sexpr(varname), value);
    };

    self.routines["local"] = function(varname) {
        var localscope = self.scopes[0];
        Array.prototype.forEach.call(arguments, function(name) { localscope[sexpr(name).toLowerCase()] = undefined; });
    };

    self.routines["localmake"] = function(varname, value) {
        var localscope = self.scopes[0];
        localscope[sexpr(varname).toLowerCase()] = value;
    };

    self.routines["thing"] = function(varname) {
        return self.getvar(sexpr(varname));
    };

    self.routines["global"] = function(varname) {
        var globalscope = self.scopes[self.scopes.length - 1];
        Array.prototype.forEach.call(arguments, function(name) { globalscope[sexpr(name).toLowerCase()] = undefined; });
    };

    // 7.3 Property Lists

    //
    // 7.4 Workspace Predicates
    //

    self.routines["procedurep"] = self.routines["procedure?"] = function(name) {
        name = sexpr(name).toLowerCase();
        return typeof self.routines[name] === 'function' ? 1 : 0;
    };

    self.routines["primitivep"] = self.routines["primitive?"] = function(name) {
        name = sexpr(name).toLowerCase();
        return (typeof self.routines[name] === 'function' &&
            self.routines[name].primitive) ? 1 : 0;
    };

    self.routines["definedp"] = self.routines["defined?"] = function(name) {
        name = sexpr(name).toLowerCase();
        return (typeof self.routines[name] === 'function' &&
            !self.routines[name].primitive) ? 1 : 0;
    };

    self.routines["namep"] = self.routines["name?"] = function(varname) {
        try {
            return typeof self.getvar(sexpr(varname)) !== 'undefined' ? 1 : 0;
        }
        catch (e) {
            return 0;
        }
    };

    // Not Supported: plistp

    //
    // 7.5 Workspace Queries
    //

    self.routines["contents"] = function() {
        return [
            Object.keys(self.routines).filter(function(x) { return !self.routines[x].primitive; }),
            self.scopes.reduce(function(list, scope) { return list.concat(Object.keys(scope)); }, [])
            ];
    };

    // Not Supported: buried
    // Not Supported: traced
    // Not Supported: stepped

    self.routines["procedures"] = function() {
        return Object.keys(self.routines).filter(function(x) { return !self.routines[x].primitive; });
    };

    self.routines["primitives"] = function() {
        return Object.keys(self.routines).filter(function(x) { return self.routines[x].primitive; });
    };

    self.routines["globals"] = function() {
        var globalscope = self.scopes[self.scopes.length - 1];
        return Object.keys(globalscope);
    };

    self.routines["names"] = function() {
        return [[], self.scopes.reduce(function(list, scope) { return list.concat(Object.keys(scope)); }, [])];
    };

    // Not Supported: plists
    // Not Supported: namelist
    // Not Supported: pllist
    // Not Supported: arity
    // Not Supported: nodes

    // 7.6 Workspace Inspection

    //
    // 7.7 Workspace Control
    //

    self.routines["erase"] = self.routines["erase"] = function(list) {
        list = lexpr(list);

        // Delete procedures
        if (list.length) {
            var procs = lexpr(list.shift());
            procs.forEach(function(name) {
                name = name.toLowerCase();
                if (self.routines.hasOwnProperty(name) && !self.routines[name].primitive) {
                    delete self.routines[name];
                }
            });
        }

        // Delete variables
        if (list.length) {
            var vars = lexpr(list.shift());
            // TODO: global only?
            self.scopes.forEach(function(scope) {
                vars.forEach(function(name) {
                    if (scope.hasOwnProperty(name)) {
                        delete scope[name];
                    }
                });
            });
        }
    };

    self.routines["erall"] = function() {

        Object.keys(self.routines).forEach(function(name) {
            if (!self.routines[name].primitive) {
                delete self.routines[name];
            } 
        });

        self.scopes.forEach(function(scope) {
            Object.keys(scope).forEach(function(name) { delete scope[name]; });
        });
    };

    //----------------------------------------------------------------------
    //
    // 8. Control Structures
    //
    //----------------------------------------------------------------------

    //
    // 8.1 Control
    //


    self.routines["run"] = function(statements) {
        statements = lexpr(statements);
        return self.execute(statements);
    };

    self.routines["runresult"] = function(statements) {
        statements = lexpr(statements);
        var result = self.execute(statements);
        if (typeof result !== 'undefined') {
            return [result];
        }
        else {
            return [];
        }
    };

    self.routines["repeat"] = function(count, statements) {
        count = aexpr(count);
        statements = lexpr(statements);
        var last;
        for (var i = 1; i <= count; ++i) {
            var old_repcount = self.repcount;
            self.repcount = i;
            try {
                last = self.execute(statements);
            } finally {
                self.repcount = old_repcount;
            }
        }
        return last;
    };

    self.routines["forever"] = function(statements) {
        statements = lexpr(statements);
        for (var i = 1; true; ++i) {
            var old_repcount = self.repcount;
            self.repcount = i;
            try {
                self.execute(statements);
            }
            finally {
                self.repcount = old_repcount;
            }
        }
    };

    self.routines["repcount"] = function() {
        return self.repcount;
    };

    self.routines["if"] = function(test, statements) {
        test = aexpr(test);
        statements = lexpr(statements);

        return test ? self.execute(statements) : test;
    };

    self.routines["ifelse"] = function(test, statements1, statements2) {
        test = aexpr(test);
        statements1 = lexpr(statements1);
        statements2 = lexpr(statements2);

        return self.execute(test ? statements1 : statements2);
    };

    self.routines["test"] = function(tf) {
        tf = aexpr(tf);
        self.scopes[0]._test = tf;
        return tf;
    };

    self.routines["iftrue"] = self.routines["ift"] = function(statements) {
        statements = lexpr(statements);
        return self.scopes[0]._test ? self.execute(statements) : self.scopes[0]._test;
    };

    self.routines["iffalse"] = self.routines["iff"] = function(statements) {
        statements = lexpr(statements);
        return !self.scopes[0]._test ? self.execute(statements) : self.scopes[0]._test;
    };


    self.routines["stop"] = function() {
        throw new Output();
    };

    self.routines["output"] = self.routines["op"] = function(atom) {
        throw new Output(atom);
    };

    // TODO: catch
    // TODO: throw
    // TODO: error
    // Not Supported: pause
    // Not Supported: continue
    // Not Supported: wait

    self.routines["bye"] = function() {
        throw new Bye();
    };

    self.routines[".maybeoutput"] = function(value) {
        if (typeof value !== 'undefined') {
            throw new Output(value);
        } else {
            throw new Output();
        }
    };

    // Not Supported: goto
    // Not Supported: tag

    self.routines["ignore"] = function(value) {
    };

    // Not Supported: `

    self.routines["for"] = function(control, statements) {
        control = lexpr(control);
        statements = lexpr(statements);

        function sign(x) { return x < 0 ? -1 : x > 0 ? 1 : 0; }

        var varname = sexpr(control.shift());
        var start = aexpr(self.evaluateExpression(control));
        var limit = aexpr(self.evaluateExpression(control));
        var step = (control.length) ? aexpr(self.evaluateExpression(control)) : sign(limit - start);

        var last;
        for (var current = start; sign(current - limit) !== sign(step); current += step) {
            self.setvar(varname, current);
            last = self.execute(statements);
        }

        return last;
    };

    function checkevalblock(block) {
        block = block();
        if (Type(block) === 'list') { return block; }
        throw "Expected block";
    }

    self.routines["do.while"] = function(block, tf) {
        block = checkevalblock(block);

        do {
            self.execute(block);
        } while (tf());
    };
    self.routines["do.while"].noeval = true;

    self.routines["while"] = function(tf, block) {
        block = checkevalblock(block);

        while (tf()) {
            self.execute(block);
        }
    };
    self.routines["while"].noeval = true;

    self.routines["do.until"] = function(block, tf) {
        block = checkevalblock(block);

        do {
            self.execute(block);
        } while (!tf());
    };
    self.routines["do.until"].noeval = true;

    self.routines["until"] = function(tf, block) {
        block = checkevalblock(block);

        while (!tf()) {
            self.execute(block);
        }
    };
    self.routines["until"].noeval = true;

    // Not Supported: case
    // Not Supported: cond


    //
    // 8.2 Template-based Iteration
    //


    //
    // Higher order functions
    //

    // TODO: multiple inputs

    self.routines["apply"] = function(procname, list) {
        procname = sexpr(procname).toLowerCase();

        var routine = self.routines[procname];
        if (!routine) { throw "Don't know how to " + procname; }
        if (routine.special) { throw "Can't apply over special " + procname; }
        if (routine.noeval) { throw "Can't apply over special " + procname; }

        return routine.apply(null, lexpr(list));
    };

    self.routines["invoke"] = function(procname) {
        procname = sexpr(procname).toLowerCase();

        var routine = self.routines[procname];
        if (!routine) { throw "Don't know how to " + procname; }
        if (routine.special) { throw "Can't invoke over special " + procname; }
        if (routine.noeval) { throw "Can't invoke over special " + procname; }

        var args = [];
        for (var i = 1; i < arguments.length; i += 1) {
            args.push(arguments[i]);
        }

        return routine.apply(null, args);
    };

    self.routines["foreach"] = function(procname, list) {
        procname = sexpr(procname).toLowerCase();

        var routine = self.routines[procname];
        if (!routine) { throw "Don't know how to " + procname; }
        if (routine.special) { throw "Can't foreach over special " + procname; }
        if (routine.noeval) { throw "Can't foreach over special " + procname; }

        return lexpr(list).forEach(routine);
    };


    self.routines["map"] = function(procname, list) {
        procname = sexpr(procname).toLowerCase();

        var routine = self.routines[procname];
        if (!routine) { throw "Don't know how to " + procname; }
        if (routine.special) { throw "Can't map over special " + procname; }
        if (routine.noeval) { throw "Can't map over special " + procname; }

        return lexpr(list).map(routine);
    };

    // Not Supported: map.se

    self.routines["filter"] = function(procname, list) {
        procname = sexpr(procname).toLowerCase();

        var routine = self.routines[procname];
        if (!routine) { throw "Don't know how to " + procname; }
        if (routine.special) { throw "Can't filter over special " + procname; }
        if (routine.noeval) { throw "Can't filter over special " + procname; }

        return lexpr(list).filter(function(x) { return routine(x); });
    };

    self.routines["find"] = function(procname, list) {
        procname = sexpr(procname).toLowerCase();

        var routine = self.routines[procname];
        if (!routine) { throw "Don't know how to " + procname; }
        if (routine.special) { throw "Can't filter over special " + procname; }
        if (routine.noeval) { throw "Can't filter over special " + procname; }

        list = lexpr(list);
        for (var i = 0; i < list.length; i += 1) {
            var item = list[i];
            if (routine(item)) {
                return item;
            }
        }
        return [];
    };

    self.routines["reduce"] = function(procname, list) {
        procname = sexpr(procname).toLowerCase();
        list = lexpr(list);
        var value = typeof arguments[2] !== 'undefined' ? arguments[2] : list.shift();

        var procedure = self.routines[procname];
        if (!procedure) { throw "Don't know how to " + procname; }
        if (procedure.special) { throw "Can't reduce over special " + procname; }
        if (procedure.noeval) { throw "Can't reduce over special " + procname; }

        // NOTE: Can't use procedure directly as reduce calls 
        // targets w/ additional args and defaults initial value to undefined
        return list.reduce(function(a, b) { return procedure(a, b); }, value);
    };

    // Not Supported: crossmap
    // Not Supported: cascade
    // Not Supported: cascade.2
    // Not Supported: transfer

    //----------------------------------------------------------------------
    // Mark built-ins as such
    //----------------------------------------------------------------------

    Object.keys(self.routines).forEach(function(x) { self.routines[x].primitive = true; });

} // LogoInterpreter

