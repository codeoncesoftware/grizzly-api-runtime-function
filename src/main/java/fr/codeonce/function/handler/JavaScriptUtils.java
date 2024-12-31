package fr.codeonce.function.handler;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class JavaScriptUtils {

	/**
	 * this method used to parse argument from string to Graalvm Value to be used in
	 * the function
	 * 
	 * @param argument This is function's argument
	 * @param context  This is the context in which we will parse the argument
	 * 
	 */
	public static Value jsonToJsObject(String argument, Context ctx) {
		Value parser = ctx.eval("js", "(jsonString)=> { return JSON.parse(jsonString) ;}");
		Value jsValue = parser.execute(argument);
		System.out.println(jsValue);
		return jsValue;

	}

}
