/*
 * package fr.codeonce.function.service;
 * 
 * import java.time.Duration; import java.util.concurrent.ExecutorService;
 * import java.util.concurrent.Executors; import java.util.concurrent.Future;
 * import java.util.concurrent.TimeUnit; import
 * java.util.concurrent.TimeoutException;
 * 
 * import javax.enterprise.context.ApplicationScoped;
 * 
 * import org.graalvm.polyglot.Context; import org.graalvm.polyglot.Engine;
 * import org.graalvm.polyglot.Value;
 * 
 * import fr.codeonce.function.utils.RuntimeResourceFunction;
 * 
 * @ApplicationScoped public class PythonCompilerService implements
 * RuntimeCompiler {
 * 
 * @Override public Object execute(RuntimeResourceFunction function, String
 * body, Engine engine) { // executor service to execute script asynchronously
 * in a new thread final ExecutorService executor =
 * Executors.newSingleThreadExecutor();
 * 
 * Context ctx = Context.newBuilder("python").engine(engine).build();
 * 
 * // execute the function asynchronously in a new thread to prevent infinite
 * loop final Future<Object> futureResult = executor .submit(() ->
 * ctx.eval(function.getLanguage(),
 * function.getFunction()).execute(Value.asValue(body)));
 * 
 * try { // if the function does not end in 10 sec stop script return
 * futureResult.get(10, TimeUnit.SECONDS); } catch (TimeoutException e) { try {
 * // stop the function ctx.interrupt(Duration.ZERO); } catch (TimeoutException
 * e1) { e1.printStackTrace(); }
 * System.out.println("Script not evaluated within 10 seconds, interrupted.");
 * return Value.asValue("Script not evaluated within 10 seconds, interrupted.");
 * 
 * } catch (Exception e) {
 * 
 * return Value.asValue("there are problems");
 * 
 * }
 * 
 * } }
 */