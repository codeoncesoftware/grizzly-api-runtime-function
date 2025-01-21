/*
 * package fr.codeonce.function.service;
 * 
 * import java.io.ByteArrayOutputStream; import java.io.IOException; import
 * java.lang.reflect.Array; import java.lang.reflect.Field; import
 * java.lang.reflect.InvocationTargetException; import java.lang.reflect.Method;
 * import java.util.HashMap; import java.util.Map; import java.util.Vector;
 * 
 * import javax.enterprise.context.ApplicationScoped; import
 * javax.inject.Inject;
 * 
 * import org.graalvm.polyglot.Engine; import
 * org.jsonschema2pojo.AnnotationStyle; import
 * org.jsonschema2pojo.DefaultGenerationConfig; import
 * org.jsonschema2pojo.GenerationConfig; import
 * org.jsonschema2pojo.NoopAnnotator; import
 * org.jsonschema2pojo.SchemaGenerator; import org.jsonschema2pojo.SchemaMapper;
 * import org.jsonschema2pojo.SchemaStore; import
 * org.jsonschema2pojo.SourceType; import org.jsonschema2pojo.rules.RuleFactory;
 * import org.slf4j.Logger; import org.slf4j.LoggerFactory;
 * 
 * import com.fasterxml.jackson.core.JsonProcessingException; import
 * com.fasterxml.jackson.databind.JsonMappingException; import
 * com.fasterxml.jackson.databind.ObjectMapper; import
 * com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException; import
 * com.google.gson.Gson; import com.sun.codemodel.JCodeModel; import
 * com.sun.codemodel.writer.SingleStreamCodeWriter;
 * 
 * import fr.codeonce.function.dto.Response; import
 * fr.codeonce.function.dto.RuntimeResourceFunction; import
 * fr.codeonce.function.handler.FunctionArgumentParser; import
 * fr.codeonce.function.security.FunctionThread; import
 * net.openhft.compiler.CompilerUtils;
 * 
 * @ApplicationScoped public class JavaCompilerService implements
 * RuntimeCompiler {
 * 
 * @Inject FunctionArgumentParser parser;
 * 
 * final private Logger logger =
 * LoggerFactory.getLogger(JavaCompilerService.class);
 * 
 * @Override public Response execute(RuntimeResourceFunction function, String
 * body, String args, String executiontype, Engine engine) {
 * 
 * // final result variable, first colon for the result and second for
 * exceptions final Object[] result = new Object[2]; String[] exception = new
 * String[] { "" }; final int[] httpCode = new int[] { -1 };
 * 
 * try {
 * 
 * // required variables final Class<?> mainFunction; final Class<?> model;
 * Object bodyObject = null; Object inputObject = null; final Object
 * insertedBody; final Object insertedInput; Method method = null; Object
 * classInstance; // build the package to be unique based on lastupdate and
 * function's id String packageName = "grizzlyApi." + "p" + function.getId() +
 * function.getLastUpdate().getTime() / 1000;
 * System.out.println(CompilerUtils.addClassPath(
 * "C:\\Users\\ihebb\\.m2\\repository\\com\\fasterxml\\jackson\\core\\jackson-databind\\2.12.3\\"
 * )); // compile the inserted model and cache it model =
 * CompilerUtils.CACHED_COMPILER.loadFromJava(packageName + "." +
 * function.getModelName(), "package " + packageName + ";\n" +
 * function.getModel()); // compile the inserted main class and cache it
 * mainFunction = CompilerUtils.CACHED_COMPILER.loadFromJava(packageName + "." +
 * function.getClassName(), "package " + packageName + ";\n" +
 * "import grizzlyApi.*;\n" + function.getFunction()); classInstance =
 * mainFunction.newInstance(); Field f =
 * ClassLoader.class.getDeclaredField("classes"); f.setAccessible(true);
 * 
 * ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
 * Vector<Class> classes = (Vector<Class>) f.get(classLoader);
 * System.out.println(classes); // choose which method to fetch based on type of
 * execution ObjectMapper objectMapper = new ObjectMapper(); if
 * (executiontype.equalsIgnoreCase("in")) { method =
 * mainFunction.getMethod(function.getMethodName(), HashMap.class); inputObject
 * = objectMapper.readValue(args, Map.class);
 * 
 * } else { if (body.startsWith("[")) { method =
 * mainFunction.getMethod(function.getMethodName(), HashMap.class,
 * Array.newInstance(model, 10).getClass()); bodyObject =
 * objectMapper.readValue(body, Array.newInstance(model, 0).getClass());
 * 
 * } else { method = mainFunction.getMethod(function.getMethodName(),
 * HashMap.class, model); bodyObject = objectMapper.readValue(body, model);
 * 
 * } inputObject = objectMapper.readValue(args, Map.class); }
 * 
 * // this variable to ensure thread safe variable to our intern thread Method
 * method2 = method; insertedBody = inputObject; insertedInput = bodyObject;
 * 
 * // execute the function inside a new thread for security purpose
 * FunctionThread functionThread = new FunctionThread(new Runnable() {
 * 
 * @Override public void run() {
 * 
 * // choose which function to invoke based on type of the execution try {
 * 
 * if (executiontype.equalsIgnoreCase("in")) { result[0] =
 * method2.invoke(classInstance, insertedBody); } else { result[0] =
 * method2.invoke(classInstance, insertedBody, insertedInput); }
 * logger.info("Function with ID: {} has been executed successfully",
 * function.getId());
 * 
 * } catch (ThreadDeath e) { e.printStackTrace();
 * 
 * } catch (SecurityException e) { // TODO Auto-generated catch block
 * e.printStackTrace();
 * 
 * } catch (IllegalAccessException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } catch (IllegalArgumentException e) { // TODO
 * Auto-generated catch block e.printStackTrace(); } catch
 * (InvocationTargetException e) { // TODO Auto-generated catch block if
 * (e.getCause() != null && e.getCause().getClass().getCanonicalName()
 * .equalsIgnoreCase("grizzlyApi.ErrorException")) { exception[0] =
 * e.getCause().getMessage(); try { Field declaredField =
 * e.getCause().getClass().getDeclaredField("httpCode"); httpCode[0] = (int)
 * declaredField.get(e.getCause()); logger.info(
 * "Client Exception inside the function with ID: {} has been thrown successfully with httpCode: {}"
 * , function.getId(), httpCode[0]);
 * 
 * } catch (NoSuchFieldException | SecurityException e1) { // TODO
 * Auto-generated catch block e1.printStackTrace(); } catch
 * (IllegalArgumentException e1) { // TODO Auto-generated catch block
 * e1.printStackTrace(); } catch (IllegalAccessException e1) { // TODO
 * Auto-generated catch block e1.printStackTrace(); }
 * 
 * } else { e.printStackTrace();
 * 
 * } } } }); functionThread.start(); // synchronize our threads
 * functionThread.join();
 * 
 * } catch (UnrecognizedPropertyException e) { // if there is mismatch between
 * input and our function's parameters return new
 * Response.ResponseBuilder().type("exception").response(new
 * Gson().toJson(e.getMessage())).build();
 * 
 * }
 * 
 * catch (IllegalArgumentException e) { return new
 * Response.ResponseBuilder().type("exception").response(new
 * Gson().toJson(e.getMessage())).build();
 * 
 * } catch (SecurityException e) {// in case of security issues return new
 * Response.ResponseBuilder().type("exception")
 * .response("operation not permitted, change your function !").build(); } catch
 * (InterruptedException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } catch (ClassNotFoundException e1) { return new
 * Response.ResponseBuilder().type("exception").response(
 * "Please check if inserted information matches your class declaration, and verify if the class and method are well defined "
 * ) .build(); } catch (NoSuchMethodException e1) { return new
 * Response.ResponseBuilder().type("exception")
 * .response("Verify if you are using the convetional syntax" +
 * e1.getMessage()).build(); } catch (InstantiationException e1) { // TODO
 * Auto-generated catch block e1.printStackTrace(); } catch
 * (IllegalAccessException e1) { // TODO Auto-generated catch block
 * e1.printStackTrace(); } catch (JsonMappingException e1) { // TODO
 * Auto-generated catch block e1.printStackTrace(); } catch
 * (JsonProcessingException e1) { // TODO Auto-generated catch block
 * 
 * } catch (NoSuchFieldException e2) { // TODO Auto-generated catch block
 * e2.printStackTrace(); } if (result[1] != null) {// result[1] contains server
 * exceptions and errors // response type is an intern type allow us to delegate
 * exception handler to the // controller return new
 * Response.ResponseBuilder().type("security").response(new
 * Gson().toJson(result[1])).build();
 * 
 * } else { // if output's type is among those type we can consider it valid
 * response if (result[0] != null && (result[0] instanceof Number || result[0]
 * instanceof String || result[0] instanceof Boolean || result[0] instanceof
 * Character)) { return new
 * Response.ResponseBuilder().type("valid").response(result[0]).build();
 * 
 * } else if (httpCode[0] != -1) {// if there is exception thrown by the client
 * return new
 * Response.ResponseBuilder().type("Exception").httpCode(httpCode[0]).response(
 * exception[0]) .build(); } else if (result[0] != null) {// if our result is a
 * custom object or // Map or an array so we can serialize it to json and send
 * it return new Response.ResponseBuilder().type("valid").response(new
 * Gson().toJson(result[0])).build(); } else { // if response is null, we can
 * consider it as function's logical or syntaxical // problem and we return 400
 * return new Response.ResponseBuilder().type("exception").httpCode(400)
 * .response("there is problem with the function :  " +
 * function.getName()).build(); } }
 * 
 * }
 * 
 *//**
	 * 
	 * 
	 * function to choose which function to invoke based on given inputs
	 * 
	 * 
	 * @param body
	 * @param args
	 * @param method
	 * @param readValue
	 * @param readValue2
	 * @param function
	 * @param model
	 * @param mainFunction
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 *//*
		 * private void chooseTheFunction(String body, String args, Method method,
		 * Object readValue, Object readValue2, RuntimeResourceFunction function,
		 * Class<?> model, Class<?> mainFunction) throws NoSuchMethodException,
		 * SecurityException, JsonMappingException, JsonProcessingException {
		 * ObjectMapper objectMapper = new ObjectMapper();
		 * 
		 * if (body.equalsIgnoreCase("{}") && args.equalsIgnoreCase("{}")) {// if there
		 * is body and inserted input method =
		 * mainFunction.getMethod(function.getMethodName()); } else if
		 * (!body.equalsIgnoreCase("{}") && !args.equalsIgnoreCase("{}")) {// if there
		 * is no input method = mainFunction.getMethod(function.getMethodName(),
		 * Array.newInstance(model, 10).getClass(), HashMap.class); readValue2 =
		 * objectMapper.readValue(args, Map.class); readValue =
		 * objectMapper.readValue(body, Array.newInstance(model, 0).getClass()); } else
		 * if (body.equalsIgnoreCase("{}")) { method =
		 * mainFunction.getMethod(function.getMethodName(), HashMap.class); readValue2 =
		 * objectMapper.readValue(args, Map.class); } else { method =
		 * mainFunction.getMethod(function.getMethodName(), Array.newInstance(model,
		 * 10).getClass()); readValue = objectMapper.readValue(body,
		 * Array.newInstance(model, 0).getClass());
		 * 
		 * }
		 * 
		 * }
		 * 
		 * public void test() {
		 * 
		 * final GenerationConfig config = new DefaultGenerationConfig() {
		 * 
		 * @Override public SourceType getSourceType() { return SourceType.JSON; }
		 * 
		 * @Override public boolean isIncludeTypeInfo() { return false;
		 * 
		 * }
		 * 
		 * @Override public boolean isIncludeConstructorPropertiesAnnotation() { return
		 * false;
		 * 
		 * }
		 * 
		 * @Override public boolean isIncludeJsr303Annotations() { return false;
		 * 
		 * }
		 * 
		 * @Override public boolean isIncludeAdditionalProperties() { return false;
		 * 
		 * }
		 * 
		 * @Override public boolean isIncludeGeneratedAnnotation() { return false; }
		 * 
		 * @Override public AnnotationStyle getAnnotationStyle() { return
		 * AnnotationStyle.NONE; }
		 * 
		 * };
		 * 
		 * final JCodeModel codeModel = new JCodeModel(); final RuleFactory ruleFactory
		 * = new RuleFactory(config, new NoopAnnotator(), new SchemaStore()); final
		 * SchemaMapper schemaMapper = new SchemaMapper(ruleFactory, new
		 * SchemaGenerator());
		 * 
		 * try { schemaMapper.generate(codeModel, "Product", "codeonce",
		 * "{\"titulo\": 4,\"_id\":\"465\",\"table\":[{\"name\":\"iheb\"}] }"); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 * 
		 * try { ByteArrayOutputStream s = new ByteArrayOutputStream();
		 * codeModel.build(new SingleStreamCodeWriter(s)); String finalString = new
		 * String(s.toByteArray()); finalString =
		 * "package codeonce; \n".concat(finalString.substring(finalString.
		 * indexOf("public class "))); System.out.println(finalString);
		 * 
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * }
		 * 
		 * }
		 */