package proxy.statics;

import org.junit.Test;

import proxy.Subject;

public class StaticProxyTest {
	
	@Test 
	public void test(){
		
		Subject proxy = SubjectStaticFactory.getInstance();
		
		proxy.dealTask("task for testing");
	
	}
	
}
