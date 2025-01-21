package fr.codeonce.function.predefined.javascript.function.jwt;

import javax.enterprise.context.ApplicationScoped;

import org.graalvm.polyglot.Context;

@ApplicationScoped
public class ParseJwtTokenFunction {

	public void evalJwtDecoderAndVerifier(Context context) {

		ParseJwtTokenService service = new ParseJwtTokenService();
		context.getBindings("js").putMember("jwtTokenService", service);
		context.eval("js",
				"_decryptJwt=(secretKey,token)=>{return JSON.parse(jwtTokenService.decryptJwtToken(secretKey,token));}");
		context.eval("js", "_verifyJwt=(secretKey,token)=>{return jwtTokenService.verifytJwtToken(secretKey,token);}");

	}

}
