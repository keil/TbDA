package dk.brics.tajs.test;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;

// TODO: assertEquals, assertTrue, assertFalse, assertThrows

public class TestV8 { // TODO: rename methods in TestV8?
	
	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestV8");
	}
	
	@Before
	public void init() {
        Options.reset();
		Options.setTest(true);
		// Options.set("-no-lazy");
	}

	@Ignore // TODO: 'finally'
	@Test
	public void api_call_after_bypassed_exception() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/api-call-after-bypassed-exception.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void apply() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/apply.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void arguments_call_apply() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/arguments-call-apply.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void arguments_enum() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/arguments-enum.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void arguments_indirect() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/arguments-indirect.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // uses nonstandard syntax
	@Test
	public void arguments_opt() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/arguments-opt.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void arguments() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/arguments.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void array_concat() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/array-concat.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void array_functions_prototype() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/array-functions-prototype.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void array_indexing() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/array-indexing.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void array_iteration() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/array-iteration.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void array_join() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/array-join.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: syntax error (!)
	@Test
	public void array_sort() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/array-sort.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void array_splice_webkit() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/array-splice-webkit.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void array_splice() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/array-splice.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void array_length() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/array_length.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void ascii_regexp_subject() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/ascii-regexp-subject.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void binary_operation_overwrite() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/binary-operation-overwrite.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: Function(...)
	@Test
	public void body_not_visible() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/body-not-visible.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void call_non_function_call() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/call-non-function-call.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void call_non_function() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/call-non-function.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void call() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/call.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void char_escape() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/char-escape.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void class_of_builtins() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/class-of-builtins.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void closure() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/closure.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void compare_nan() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/compare-nan.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void const_redecl() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/const-redecl.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'const'
	@Test
	public void cons_test() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/const.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void context_variable_assignments() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/context-variable-assignments.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void cyclic_array_to_string() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/cyclic-array-to-string.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void date_parse() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/date-parse.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void date() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/date.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: DEBUGGER
	@Test
	public void debug_backtrace_text() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-backtrace-text.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_backtrace() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-backtrace.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_breakpoints() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-breakpoints.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_changebreakpoint() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-changebreakpoint.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_clearbreakpoint() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-clearbreakpoint.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_conditional_breakpoints() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-conditional-breakpoints.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_constructed_by() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-constructed-by.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_constructor() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-constructor.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_continue() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-continue.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_enable_disable_breakpoints() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-enable-disable-breakpoints.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: DEBUGGER
	@Test
	public void debug_evaluate_arguments() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-evaluate-arguments.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: DEBUGGER
	@Test
	public void debug_evaluate_locals() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-evaluate-locals.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: DEBUGGER
	@Test
	public void debug_evaluate_recursive() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-evaluate-recursive.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: DEBUGGER
	@Test
	public void debug_evaluate_with() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-evaluate-with.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_evaluate() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-evaluate.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_event_listener() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-event-listener.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_ignore_breakpoints() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-ignore-breakpoints.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_multiple_breakpoints() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-multiple-breakpoints.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_referenced_by() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-referenced-by.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_script_breakpoints() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-script-breakpoints.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void debug_script() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-script.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: DEBUGGER
	@Test
	public void debug_scripts_request() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-scripts-request.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_setbreakpoint() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-setbreakpoint.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_sourceinfo() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-sourceinfo.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'eval'
	@Test
	public void debug_sourceslice() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-sourceslice.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: DEBUGGER
	@Test
	public void debug_step_stub_callfunction() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-step-stub-callfunction.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void debug_step() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-step.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: DEBUGGER
	@Test
	public void debug_stepin_constructor() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/debug-stepin-constructor.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: __proto__
	@Test
	public void declare_locally() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/declare-locally.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void deep_recursion() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/deep-recursion.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void delay_syntax_error() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/delay-syntax-error.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void delete_global_properties() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/delete-global-properties.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void delete_in_eval() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/delete-in-eval.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'with'
	@Test
	public void delete_in_with() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/delete-in-with.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'eval'
	@Test
	public void delete_vars_from_eval() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/delete-vars-from-eval.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void delete() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/delete.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void do_not_strip_fc() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/do-not-strip-fc.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void dont_enum_array_holes() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/dont-enum-array-holes.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void dont_reinit_global_var() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/dont-reinit-global-var.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void double_equals() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/double-equals.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void dtoa() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/dtoa.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void enumeration_order() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/enumeration_order.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void escape() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/escape.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void eval_typeof_non_existing() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/eval-typeof-non-existing.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void execScript_case_insensitive() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/execScript-case-insensitive.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void extra_arguments() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/extra-arguments.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'eval'
	@Test
	public void extra_commas() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/extra-commas.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void for_in_null_or_undefined() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/for-in-null-or-undefined.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void for_in_special_cases() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/for-in-special-cases.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void for_in() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/for-in.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: __proto__
	@Test
	public void fun_as_prototype() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/fun-as-prototype.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void fun_name() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/fun_name.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void function_arguments_null() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/function-arguments-null.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void function_caller() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/function-caller.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void function_names() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/function-names.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void function_property() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/function-property.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: __proto__
	@Test
	public void function_prototype() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/function-prototype.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void function_source() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/function-source.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: Function(..)
	@Test
	public void function() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/function.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void fuzz_accessors() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/fuzz-accessors.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: syntax error (!)
	@Test
	public void fuzz_natives() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/fuzz-natives.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void getter_in_value_prototype() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/getter-in-value-prototype.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'eval'
	@Test
	public void global_const_var_conflicts() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/global-const-var-conflicts.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'eval'
	@Test
	public void global_vars_eval() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/global-vars-eval.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'with'
	@Test
	public void global_vars_with() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/global-vars-with.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void greedy() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/greedy.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void has_own_property() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/has-own-property.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void html_comments() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/html-comments.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void html_string_funcs() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/html-string-funcs.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void if_in_undefined() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/if-in-undefined.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void in() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/in.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: '__proto__'
	@Test
	public void instance_of() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/instanceof.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void integer_to_string() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/integer-to-string.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void invalid_lhs() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/invalid-lhs.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void keyed_ic() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/keyed-ic.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void keyed_storage_extend() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/keyed-storage-extend.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void large_object_allocation() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/large-object-allocation.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'eval'
	@Test
	public void large_object_literal() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/large-object-literal.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void lazy_load() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/lazy-load.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void leakcheck() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/leakcheck.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void length() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/length.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void math_min_max() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/math-min-max.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void megamorphic_callbacks() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/megamorphic-callbacks.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void mirror_array() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/mirror-array.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void mirror_boolean() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/mirror-boolean.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void mirror_date() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/mirror-date.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void mirror_error() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/mirror-error.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void mirror_function() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/mirror-function.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void mirror_null() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/mirror-null.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void mirror_number() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/mirror-number.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void mirror_object() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/mirror-object.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void mirror_regexp() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/mirror-regexp.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void mirror_string() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/mirror-string.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void mirror_undefined() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/mirror-undefined.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void mirror_unresolved_function() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/mirror-unresolved-function.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	@Ignore //Does a multitude of different multiplications, takes a very long to execute. 
	public void mul_exhaustive() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/mul-exhaustive.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void negate_zero() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/negate-zero.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'const'
	@Test
	public void negate() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/negate.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void nested_repetition_count_overflow() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/nested-repetition-count-overflow.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void new_test() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/new.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void newline_in_string() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/newline-in-string.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void no_branch_elimination() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/no-branch-elimination.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void no_octal_constants_above_256() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/no-octal-constants-above-256.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void no_semicolon() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/no-semicolon.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void non_ascii_replace() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/non-ascii-replace.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void nul_characters() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/nul-characters.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void number_limits() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/number-limits.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void number_string_index_call() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/number-string-index-call.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void number_tostring_small() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/number-tostring-small.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void number_tostring() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/number-tostring.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void obj_construct() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/obj-construct.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void parse_int_float() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/parse-int-float.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void property_object_key() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/property-object-key.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: __proto__
	@Test
	public void proto() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/proto.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: __proto__
	@Test
	public void prototype() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/prototype.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void regexp_indexof() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/regexp-indexof.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void regexp_multiline_stack_trace() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/regexp-multiline-stack-trace.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void regexp_multiline() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/regexp-multiline.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void regexp_standalones() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/regexp-standalones.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void regexp_static() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/regexp-static.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void regexp() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/regexp.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void scanner() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/scanner.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void smi_negative_zero() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/smi-negative-zero.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'const'
	@Test
	public void smi_ops() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/smi-ops.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void sparse_array_reverse() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/sparse-array-reverse.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void sparse_array() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/sparse-array.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void str_to_num() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/str-to-num.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void stress_array_push() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/stress-array-push.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void strict_equals() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/strict-equals.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void string_case() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/string-case.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void string_charat() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/string-charat.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: syntax error (!)
	@Test
	public void string_charcodeat() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/string-charcodeat.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void string_compare_alignment() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/string-compare-alignment.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void string_flatten() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/string-flatten.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void string_index() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/string-index.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: syntax error (!)
	@Test
	public void string_indexof() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/string-indexof.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void string_lastindexof() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/string-lastindexof.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void string_localecompare() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/string-localecompare.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void string_search() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/string-search.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void string_split() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/string-split.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void substr() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/substr.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void switch_test() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/switch.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void this_in_callbacks() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/this-in-callbacks.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void this_test() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/this.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void throw_exception_for_null_access() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/throw-exception-for-null-access.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void to_precision() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/to-precision.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void tobool() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/tobool.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void toint32() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/toint32.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void touint32() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/touint32.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'finally'
	@Test
	public void try_finally_nested() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/try-finally-nested.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void try_test() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/try.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void try_catch_scopes() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/try_catch_scopes.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void unicode_string_to_number() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/unicode-string-to-number.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void unicode_test() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/unicode-test.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: __proto__
	@Test
	public void unusual_constructor() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/unusual-constructor.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void uri() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/uri.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void value_callic_prototype_change() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/value-callic-prototype-change.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void var() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/var.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void with_function_expression() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/with-function-expression.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'with'
	@Test
	public void with_leave() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/with-leave.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void with_parameter_access() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/with-parameter-access.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'with'
	@Test
	public void with_value() throws Exception
	{
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/v8tests/with-value.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}
}
