package classloader;

import java.io.InputStream;

public class ClassLoaderMainTest {
	
	public static void main(String[] args) throws Exception{
		
		MyClassLoader myLoader1 = new MyClassLoader();
		
		MyClassLoader myLoader2 = new MyClassLoader();
		
		InputStream stream = ClassLoaderMainTest.class.getResourceAsStream("ClassLoaderTest.class");
		
		byte[] bytes = new byte[ stream.available() ];
		
		stream.read(bytes);
		
		Class<?> c1 = myLoader1.defineClazz("classloader.ClassLoaderTest", bytes, 0, bytes.length );
		
		Class<?> c2 = myLoader2.defineClazz("classloader.ClassLoaderTest", bytes, 0, bytes.length );
		
		Object obj1 = c1.newInstance();
		
		Object obj2 = c2.newInstance();
		
		System.out.println(obj1.getClass());
		
		@SuppressWarnings("unused")
		Class<?> c3 = classloader.ClassLoaderMainTest.class;
		
		System.out.println(obj1 instanceof classloader.ClassLoaderMainTest);
		
		System.out.println(obj2 instanceof classloader.ClassLoaderMainTest);
		
	}
	
}
