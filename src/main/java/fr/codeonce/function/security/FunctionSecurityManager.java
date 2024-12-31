package fr.codeonce.function.security;

import java.security.Permission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionSecurityManager extends SecurityManager {
	final Logger logger = LoggerFactory.getLogger(FunctionSecurityManager.class);

	@Override
	public void checkPermission(Permission perm) {
		if (Thread.currentThread() instanceof FunctionThread && !perm.getName().equals("createClassLoader")
				&& !perm.getName().equals("suppressAccessChecks")
				&& !perm.getName().equalsIgnoreCase("accessDeclaredMembers")) {
			logger.error("The following permession is not allowed : " + perm);
			Thread.currentThread().stop();

		}
	}

	@Override
	public void checkPermission(Permission perm, Object context) {
		if (Thread.currentThread() instanceof FunctionThread)
			check(perm);
	}

	private void check(Permission perm) {
		System.out.println(perm.getName());
		if (!perm.getName().equalsIgnoreCase("accessDeclaredMembers"))

			throw new SecurityException("Not permitted: " + perm);

	}

}