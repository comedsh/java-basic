package proxy.dynamic.cglib;

import org.junit.Test;

import proxy.AuthenticationHolder;
import proxy.ISubject;
import proxy.SubjectImpl;
import proxy.dynamic.cglib.MyCglibProxy;

public class CglibProxyTest {
	
	@Test
	public void test() throws Exception{
		
		AuthenticationHolder.setUser("manager");
		
		MyCglibProxy<ISubject> proxy = new MyCglibProxy<ISubject>();
		
		ISubject subject = proxy.getBean( SubjectImpl.class );
		
		subject.dealTask("task for testing");
		
		subject.authorizedCall();
		
		AuthenticationHolder.setUser("admin");
		
		subject.authorizedCall();
		
	}
	
}
