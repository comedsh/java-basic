package proxy.cglib;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import proxy.AuthenticationHolder;
import proxy.dynamic.Authorized;

public class MyCglibProxy<T> implements MethodInterceptor {

	public Enhancer enhancer = new Enhancer();
	
	/**
	 * 
	 * Enhancer 返回代理对象
	 * 
	 * @author shang yang
	 *
	 * @version
	 *
	 * @createTime: 2015年12月14日 下午3:41:27
	 *
	 */
	@SuppressWarnings("unchecked")
	public T getBean(Class<?> clz) {
		
		enhancer.setSuperclass( clz ); // 将原class类设置为代理类的父类
		
		enhancer.setCallback( this ); // 加入切入点既下面的intercepter()
		
		T delegate = (T) enhancer.create();
		
		return delegate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.cglib.proxy.MethodInterceptor#intercept(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], net.sf.cglib.proxy.MethodProxy)
	 */
	public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

		long stime = System.currentTimeMillis();
		
		// indicates if this is the authentication needed call.
		if( AnnotationUtils.findAnnotation( method, Authorized.class ) != null ){
			
			if( AuthenticationHolder.isAdmin() ){
				
				// 利用反射机制将请求分派给委托类处理。Method的invoke返回Object对象作为方法执行结果。
				// 因为示例程序没有返回值，所以这里忽略了返回值处理
				methodProxy.invokeSuper( object, args );				
						
			}else{
				
				System.err.println( "you don't have the permission to call authorizedCall();" );
				
			}
			
			return null; // authentication call ended ....
			
		};
		
		// process all other method ..
		methodProxy.invokeSuper( object, args );
		
		long ftime = System.currentTimeMillis();
		
		System.out.println("cglib proxy 切入，执行任务耗时" + (ftime - stime) + "毫秒");

		return null;

	}

}
