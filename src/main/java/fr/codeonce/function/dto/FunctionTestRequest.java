package fr.codeonce.function.dto;

public class FunctionTestRequest {

	private String body;
	private Object expectedResult;
	private String expectedType;
	private RuntimeResourceFunction function;

	public String getExpectedType() {
		return expectedType;
	}

	public void setExpectedType(String expectedType) {
		this.expectedType = expectedType;
	}

	public Object getExpectedResult() {
		return expectedResult;
	}

	public void setExpectedResult(Object expectedResult) {
		this.expectedResult = expectedResult;
	}

	public RuntimeResourceFunction getFunction() {
		return function;
	}

	public void setFunction(RuntimeResourceFunction function) {
		this.function = function;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
