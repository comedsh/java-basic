package proxy.statics;

import org.junit.Test;

import proxy.AuthenticationHolder;
import proxy.Subject;

public class StaticProxyTest{
	
	@Test 
	public void test() throws Exception{
		
		AuthenticationHolder.setUser("manager");
		
		Subject proxy = StaticProxyFactory.getInstance();
		
		proxy.dealTask("task for testing");
		
		proxy.authorizedCall();
		
		AuthenticationHolder.setUser("admin");
		
		proxy.authorizedCall();
	
	}
	
}
