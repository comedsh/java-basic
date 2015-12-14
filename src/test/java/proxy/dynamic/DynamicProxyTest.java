package proxy.dynamic;

import org.junit.Test;

import proxy.RealSubject;
import proxy.Subject;

public class DynamicProxyTest {
	
	@Test 
	public void test(){
		
		Subject subject = new RealSubject();
		
		Subject proxy = DynamicProxyFactory.getInstance(subject);  
		
		proxy.dealTask("task for testing");  		
	
	}
	
}
