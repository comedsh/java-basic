package proxy.dynamic.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;

import proxy.AuthenticationHolder;

public class InvocationHandlerImpl implements InvocationHandler {

	// 代理类持有一个委托类的对象引用
	private Object source;

	public InvocationHandlerImpl(Object source) {
		this.source = source;
	}
	
	/**
	 * 
	 * 
	 * @param proxy { @link java.lang.reflect.Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler) } 生成的代理类
	 * @param method { @link proxy.SubjectImpl#dealTask(String) } 方法
	 * @param args @param method 的参数
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		long stime = System.currentTimeMillis();
		
		// indicates if this is the authentication needed call.
		if( AnnotationUtils.findAnnotation( method, Authorized.class ) != null ){
			
			if( AuthenticationHolder.isAdmin() ){
				
				// 利用反射机制将请求分派给委托类处理。Method的invoke返回Object对象作为方法执行结果。
				// 因为示例程序没有返回值，所以这里忽略了返回值处理
				method.invoke( source, args );
						
			}else{
				
				System.err.println( "you don't have the permission to call authorizedCall();" );
				
			}
			
			return null; // authentication call ended ....
			
		};
		
		method.invoke( source, args );
		
		long ftime = System.currentTimeMillis();
		
		System.out.println("dynamic proxy 切入，执行任务耗时" + (ftime - stime) + "毫秒");	

		return null;
	}
}
