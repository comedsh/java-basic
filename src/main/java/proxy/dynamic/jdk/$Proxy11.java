package proxy.dynamic.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import proxy.ISubject;

/**
 * 通过 {@link DynamicProxyTest#testGenerateProxyClassFile()} 生成的字节码文件 
 * 
 * @author mac
 *
 */
public final class $Proxy11 extends Proxy implements ISubject {
	
	private static Method m1;
	private static Method m4;
	private static Method m3;
	private static Method m0;
	private static Method m2;

	static {
		try {
			m1 = Class.forName("java.lang.Object").getMethod("equals", new Class[] { Class.forName("java.lang.Object") });
			m4 = Class.forName("proxy.ISubject").getMethod("authorizedCall", new Class[0]);
			m3 = Class.forName("proxy.ISubject").getMethod("dealTask", new Class[] { Class.forName("java.lang.String") });
			m0 = Class.forName("java.lang.Object").getMethod("hashCode", new Class[0]);
			m2 = Class.forName("java.lang.Object").getMethod("toString", new Class[0]);
			// return;
		} catch (NoSuchMethodException localNoSuchMethodException) {
			throw new NoSuchMethodError(localNoSuchMethodException.getMessage());
		} catch (ClassNotFoundException localClassNotFoundException) {
			throw new NoClassDefFoundError(localClassNotFoundException.getMessage());
		}
	}
	
	public $Proxy11(InvocationHandler paramInvocationHandler) {
		super(paramInvocationHandler);
	}

	public final boolean equals(Object paramObject) {
		try {
			return ((Boolean) this.h.invoke(this, m1, new Object[] { paramObject })).booleanValue();
		} catch (Error | RuntimeException localError) {
			throw localError;
		} catch (Throwable localThrowable) {
			throw new UndeclaredThrowableException(localThrowable);
		}
	}

	public final void authorizedCall() throws Exception {
		try {
			this.h.invoke(this, m4, null);
			return;
		} catch (Error | Exception localError) {
			throw localError;
		} catch (Throwable localThrowable) {
			throw new UndeclaredThrowableException(localThrowable);
		}
	}

	public final void dealTask(String paramString) throws Exception {
		try {
			this.h.invoke(this, m3, new Object[] { paramString });
			return;
		} catch (Error | Exception localError) {
			throw localError;
		} catch (Throwable localThrowable) {
			throw new UndeclaredThrowableException(localThrowable);
		}
	}

	public final int hashCode() {
		try {
			return ((Integer) this.h.invoke(this, m0, null)).intValue();
		} catch (Error | RuntimeException localError) {
			throw localError;
		} catch (Throwable localThrowable) {
			throw new UndeclaredThrowableException(localThrowable);
		}
	}

	public final String toString() {
		try {
			return (String) this.h.invoke(this, m2, null);
		} catch (Error | RuntimeException localError) {
			throw localError;
		} catch (Throwable localThrowable) {
			throw new UndeclaredThrowableException(localThrowable);
		}
	}

	
}
