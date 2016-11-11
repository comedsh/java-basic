package proxy.dynamic.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 生成动态代理对象的工厂.
 */
public class DynamicProxyFactory<T> {
	
	// 客户类调用此工厂方法获得代理对象。	
	// 对客户类来说，其并不知道返回的是代理类对象还是委托类对象。
	public static <T> T getInstance( T o ) {
		
		InvocationHandler handler = new InvocationHandlerImpl(o);
		
		@SuppressWarnings("unchecked")
		T proxy = (T) Proxy.newProxyInstance( o.getClass().getClassLoader(), o.getClass().getInterfaces(), handler ); 
		
		return proxy; 
				
	}

}
