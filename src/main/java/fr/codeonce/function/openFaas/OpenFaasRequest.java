package fr.codeonce.function.openFaas;

import java.util.Map;

import fr.codeonce.function.dto.FunctionInvokeRequest;

public class OpenFaasRequest implements FunctionInvokeRequest {

	private String body;
	private String uri;
	private Map<String, Object> headers;

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the host
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param host the host to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the headers
	 */
	public Map<String, Object> getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}
}
