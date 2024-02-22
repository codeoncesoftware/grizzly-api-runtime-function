package fr.codeonce.function.predefined.javascript.function.httpRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.graalvm.polyglot.Context;

@ApplicationScoped
public class HttpClientFunction {
	private Client client;

	public HttpClientFunction() {
		client = ClientBuilder.newBuilder().build();
	}

	public void evalHttpClient(Context context) {
		HttpClientService httpClientService = new HttpClientService(client);
		context.getBindings("js").putMember("_httpService", httpClientService);

		context.eval("js", "_httpGet=(option)=>{ let entity=_httpService.httpRequestGet(option); return entity; }");

		context.eval("js",
				"_httpPost=(option,object)=>{ object=JSON.stringify(object); let entity=_httpService.httpRequestPost(option,object); return entity; }");

		context.eval("js",
				"_httpPut=(option,object)=>{ object=JSON.stringify(object); let entity=_httpService.httpRequestPut(option,object); return entity; }");

		context.eval("js",
				"_httpDelete=(option)=>{  let entity=_httpService.httpRequestDelete(option,null); return entity; }");

	}
}
