package proxy.statics;

import org.junit.Test;

import proxy.AuthenticationHolder;
import proxy.Subject;

public class StaticProxyTest {
	
	@Test 
	public void test(){
		
		AuthenticationHolder.setUser("manager");
		
		Subject proxy = SubjectStaticFactory.getInstance();
		
		proxy.dealTask("task for testing");
		
		proxy.authorizedCall();
		
		AuthenticationHolder.setUser("admin");
		
		proxy.authorizedCall();
	
	}
	
}
