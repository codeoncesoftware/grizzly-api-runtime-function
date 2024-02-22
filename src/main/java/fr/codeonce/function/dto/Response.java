package fr.codeonce.function.dto;

/**
 * 
 * Response is a way to represent the output of the executed scripts
 * 
 *
 */
public class Response {
	private String type;
	private Object response;
	private int httpCode;

	public Response(ResponseBuilder responseBuilder) {
		super();
		this.type = responseBuilder.type;
		;
		this.response = responseBuilder.response;
		this.httpCode = responseBuilder.httpCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public int getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}

	public static class ResponseBuilder {

		private String type;
		private Object response;
		private int httpCode;

		public ResponseBuilder() {

		}

		public ResponseBuilder type(String type) {
			this.type = type;
			return this;
		}

		public ResponseBuilder response(Object response) {
			this.response = response;
			return this;
		}

		public ResponseBuilder httpCode(int httpCode) {
			this.httpCode = httpCode;
			return this;
		}

		public Response build() {
			return new Response(this);
		}

	}

	public static Response ResponseBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

}
