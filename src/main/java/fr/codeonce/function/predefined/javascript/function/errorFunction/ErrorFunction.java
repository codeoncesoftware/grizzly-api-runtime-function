package fr.codeonce.function.predefined.javascript.function.errorFunction;

import javax.enterprise.context.ApplicationScoped;

import org.graalvm.polyglot.Context;

@ApplicationScoped
public class ErrorFunction {

	public void errorJs(Context ctx) {
		ctx.getBindings("js").putMember("isException", false);
		ctx.getBindings("js").putMember("error", ctx.eval("js",
				"_throwException=(message,code)=>{ isException=true;   return {httpCode:code,response:message,type:\"exception\" }; }"));

	}
}
