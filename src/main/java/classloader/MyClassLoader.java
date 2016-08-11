package classloader;

/**
 * 
 * 通过自定义的 Class Loader 可以在运行时刻将字节码载入 JVM
 * 
 * 
 * @author 商洋
 *
 */
public class MyClassLoader extends ClassLoader{

	public Class<?> defineClazz(byte[] b, int off, int len){  
		
        return super.defineClass(null, b, off, len);  
    
	}  

	public Class<?> defineClazz(String name, byte[] b, int off, int len){  
		
        return super.defineClass(name, b, off, len);  
    
	} 
	
	public Class<?> findLoadedClass1(String name){
		
		return super.findLoadedClass(name);
		
	}
	
}
