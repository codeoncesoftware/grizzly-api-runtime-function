package fr.codeonce.function.security;

public class FunctionThread extends Thread {
	static boolean isFirst = true;

	public FunctionThread(Runnable target) {
		super(target);
	}

	@Override
	public void run() {
		if (isFirst) {
			FunctionSecurityManager psm = new FunctionSecurityManager();
			System.setSecurityManager(psm);
			isFirst = !isFirst;
		}
		super.run();

	}
}