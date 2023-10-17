/*
 * package fr.codeonce.function.handler;
 * 
 * import java.util.HashMap; import java.util.Map;
 * 
 * import javax.enterprise.context.ApplicationScoped; import
 * javax.inject.Inject;
 * 
 * import
 * org.inferred.freebuilder.shaded.com.google.common.collect.MapDifference;
 * import org.inferred.freebuilder.shaded.com.google.common.collect.Maps;
 * 
 * import com.fasterxml.jackson.core.JsonProcessingException; import
 * com.fasterxml.jackson.core.type.TypeReference; import
 * com.fasterxml.jackson.databind.JsonMappingException; import
 * com.fasterxml.jackson.databind.ObjectMapper; import com.google.gson.Gson;
 * 
 * import fr.codeonce.function.dto.FunctionTestRequest; import
 * fr.codeonce.function.dto.FunctionsExecuteRequest; import
 * fr.codeonce.function.dto.Response;
 * 
 * @ApplicationScoped public class FunctionTester {
 * 
 * @Inject private FunctionExecutor functionExecutor;
 * 
 * public Object testFunction(FunctionTestRequest functionTestRequest) {
 * 
 * FunctionsExecuteRequest functionsExecuteRequest = new
 * FunctionsExecuteRequest();
 * functionsExecuteRequest.setBody(functionTestRequest.getBody());
 * functionsExecuteRequest.setFunction(functionTestRequest.getFunction());
 * Response actualResponse =
 * functionExecutor.executeFunction(functionsExecuteRequest);
 * 
 * if (!(functionTestRequest.getExpectedResult() instanceof Number &&
 * actualResponse.getResponse() instanceof Number) &&
 * (functionTestRequest.getExpectedResult().getClass() !=
 * actualResponse.getResponse().getClass())) {
 * 
 * return "Test failed :\n" + " expected type: " +
 * functionTestRequest.getExpectedResult().getClass() + "\n actual type:  " +
 * actualResponse.getResponse().getClass(); }
 * 
 * // test valid json values if (functionTestRequest.getExpectedResult()
 * instanceof String && functionTestRequest.getExpectedType() != null &&
 * functionTestRequest.getExpectedType().equals("json")) {
 * 
 * try { ObjectMapper mapper = new ObjectMapper(); TypeReference<HashMap<String,
 * Object>> type = new TypeReference<HashMap<String, Object>>() { };
 * 
 * Map<String, Object> leftMap =
 * mapper.readValue(functionTestRequest.getExpectedResult().toString(), type);
 * Map<String, Object> rightMap =
 * mapper.readValue(actualResponse.getResponse().toString(), type);
 * MapDifference<String, Object> difference = Maps.difference(leftMap,
 * rightMap); if (difference.toString().equals("equal")) { return
 * "test passed successfully"; } else { return " Test failed \n  expected : \""
 * + new Gson().toJson(leftMap) + "\"\n  returned : \"" + new
 * Gson().toJson(rightMap) + "\""; } // JSONAssert.assertEquals((String)
 * functionTestRequest.getExpectedResult(), // (String)
 * actualResponse.getResponse(), JSONCompareMode.LENIENT);
 * 
 * } catch (java.lang.AssertionError e) { return e.getMessage(); } catch
 * (JsonMappingException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); return e.getMessage(); } catch (JsonProcessingException
 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
 * 
 * }
 * 
 * // test Strings else if (functionTestRequest.getExpectedResult() instanceof
 * String && (functionTestRequest.getExpectedType() == null ||
 * !functionTestRequest.getExpectedType().equals("json"))) {
 * 
 * if
 * (actualResponse.getResponse().equals(functionTestRequest.getExpectedResult())
 * ) { return "test passed successfully"; } return
 * " Test failed \n  expected : \"" + functionTestRequest.getExpectedResult() +
 * "\"\n  returned : \"" + actualResponse.getResponse() + "\""; } // test
 * numbers else if (functionTestRequest.getExpectedResult() instanceof Number) {
 * if (functionTestRequest.getExpectedResult() instanceof Integer) { if
 * (Double.valueOf( (int) (functionTestRequest.getExpectedResult())) == (double)
 * actualResponse.getResponse()) { return "test passed successfully";
 * 
 * } else { return " Test failed \n  expected : \"" +
 * functionTestRequest.getExpectedResult() + "\"\n  returned : \"" +
 * actualResponse.getResponse() + "\""; } }
 * 
 * if ((double) functionTestRequest.getExpectedResult() == (double)
 * actualResponse.getResponse()) { return "test passed successfully"; } else {
 * return " Test failed \n  expected : \"" +
 * functionTestRequest.getExpectedResult() + "\"\n  returned : \"" +
 * actualResponse.getResponse() + "\""; } } // test boolean else if
 * (functionTestRequest.getExpectedResult() instanceof Boolean) { if ((boolean)
 * functionTestRequest.getExpectedResult() == (boolean)
 * actualResponse.getResponse()) { return "test passed successfully";
 * 
 * } else { return " Test failed \n  expected : \"" +
 * functionTestRequest.getExpectedResult() + "\"\n  returned : \"" +
 * actualResponse.getResponse() + "\""; } } // test arrays
 * 
 * return null;
 * 
 * }
 * 
 * }
 */