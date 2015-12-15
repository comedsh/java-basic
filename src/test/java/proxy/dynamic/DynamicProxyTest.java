package proxy.dynamic;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Test;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import proxy.AuthenticationHolder;
import proxy.Subject;
import proxy.SubjectImpl;

public class DynamicProxyTest {
	
	@Test 
	public void test() throws Exception{
		
		AuthenticationHolder.setUser("manager");
		
		Subject subject = new SubjectImpl();
		
		Subject proxy = DynamicProxyFactory.getInstance(subject);  
		
		proxy.dealTask("task for testing");  	
		
		proxy.authorizedCall();
		
		AuthenticationHolder.setUser("admin");
		
		proxy.authorizedCall();
	
	}
	
	@Test
	public void testAny(){
		
		Method method = ReflectionUtils.findMethod( Subject.class, "authorizedCall" );
		
		Annotation ann = method.getAnnotation( Authorized.class );
		
		Assert.notNull( ann );
		
	}
	
}
