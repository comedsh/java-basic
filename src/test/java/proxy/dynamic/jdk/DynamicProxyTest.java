package proxy.dynamic.jdk;

import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import proxy.AuthenticationHolder;
import proxy.ISubject;
import proxy.SubjectImpl;
import sun.misc.ProxyGenerator;

public class DynamicProxyTest {
	
	@Test 
	public void test() throws Exception{
		
		AuthenticationHolder.setUser("manager");
		
		ISubject subject = new SubjectImpl();
		
		ISubject proxy = DynamicProxyFactory.getInstance(subject);  
		
		proxy.dealTask("task for testing");  	
		
		proxy.authorizedCall();
		
		AuthenticationHolder.setUser("admin");
		
		proxy.authorizedCall();
	
	}
	
	/**
	 * 生成字节码文件，看看到底生成了什么？
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGenerateProxyClassFile() throws Exception{
		
		byte[] classFile = ProxyGenerator.generateProxyClass("$Proxy11", new SubjectImpl().getClass().getInterfaces());  
        
        FileOutputStream out = null;  
          
        try {  
            out = new FileOutputStream("/Users/mac/tmp/$Proxy11.class");  
            out.write(classFile);  
            out.flush();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                out.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  		

}
