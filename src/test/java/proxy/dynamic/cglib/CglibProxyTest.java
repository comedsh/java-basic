package proxy.dynamic.cglib;

import org.junit.Test;

import proxy.AuthenticationHolder;
import proxy.Subject;
import proxy.SubjectImpl;
import proxy.dynamic.cglib.MyCglibProxy;

public class CglibProxyTest {
	
	@Test
	public void test() throws Exception{
		
		AuthenticationHolder.setUser("manager");
		
		MyCglibProxy<Subject> proxy = new MyCglibProxy<Subject>();
		
		Subject subject = proxy.getBean( SubjectImpl.class );
		
		subject.dealTask("task for testing");
		
		subject.authorizedCall();
		
		AuthenticationHolder.setUser("admin");
		
		subject.authorizedCall();
		
	}
	
}
