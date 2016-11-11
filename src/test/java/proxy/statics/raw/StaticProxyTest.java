package proxy.statics.raw;

import org.junit.Test;

import proxy.AuthenticationHolder;
import proxy.ISubject;
import proxy.statics.raw.StaticProxyFactory;

public class StaticProxyTest{
	
	@Test 
	public void test() throws Exception{
		
		AuthenticationHolder.setUser("manager");
		
		ISubject proxy = StaticProxyFactory.getInstance();
		
		proxy.dealTask("task for testing");
		
		proxy.authorizedCall();
		
		AuthenticationHolder.setUser("admin");
		
		proxy.authorizedCall();
	
	}
	
}
