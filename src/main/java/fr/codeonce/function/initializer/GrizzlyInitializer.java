package fr.codeonce.function.initializer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.logging.Logger;

import io.quarkus.runtime.StartupEvent;
import net.openhft.compiler.CompilerUtils;

@ApplicationScoped
public class GrizzlyInitializer {

	private static final Logger LOGGER = Logger.getLogger("ListenerBean");

//	void onStart(@Observes StartupEvent ev) {
//
//		LOGGER.info("The application is starting...");
//		LOGGER.info("Compiling Java Error Exception...");
//		String packageName = "grizzlyApi";
//
//		try {
//			CompilerUtils.CACHED_COMPILER.loadFromJava(packageName + "." + "ErrorException",
//					"package " + packageName + ";\n" + "public class ErrorException extends Exception { \r\n"
//							+ "public int httpCode;" + "public ErrorException(String errorMessage,int code) {\r\n"
//							+ "        super(errorMessage);\r\n" + "         httpCode=code;\r\n" + "    }\r\n" + "} ");
//
//		} catch (ClassNotFoundException e) {
//			LOGGER.info("Error while compiling error exception for java");
//			e.printStackTrace();
//		}
//
//	}

}