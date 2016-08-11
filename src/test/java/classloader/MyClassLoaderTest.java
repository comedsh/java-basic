package classloader;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 通过自定义的 ClassLoader 可以将字节码在运行时刻动态的注入 JVM
 * 
 * 注意，这个测试用例中有两层Class Loader，一层是 System Class Loader，一层是用户自定义的 My Class Loader..
 * MyClassLoader 中加载的实例只有在 MyClassLoader 中有效
 * 
 * @author 商洋
 *
 */
public class MyClassLoaderTest {

	InputStream classStream;

	byte[] classByte = new byte[1024];

	int count;

	@Before
	public void before() throws Exception {

		// 读取本地的class文件内的字节码，转换成字节码数组

		classStream = new FileInputStream(new File(".").getCanonicalPath() + "/bin/classloader/Programmer.class");

		count = classStream.read(classByte);

	}
	
	@Test
	public void testOverride(){
		
		Programmer p = new Programmer();
		
		p.code();
		
	}
	
	@Test
	public void testLoad() throws Exception {

		// 使用自定义的类加载器将 byte字节码数组转换为对应的class对象
		MyClassLoader loader = new MyClassLoader();

		Class<?> clazz = loader.defineClazz(classByte, 0, count);

		// 测试加载是否成功，打印 class 对象的名称
		System.out.println(clazz.getCanonicalName());

		// 实例化一个Programmer对象
		Object o = clazz.newInstance();

		// 调用Programmer的code方法
		clazz.getMethod("code", (Class<?>[]) null).invoke(o, (Object[]) null);
	}

	/**
	 * 类字节码只能在同一个 ClassLoader 中被加载一次，重复加载会抛出如下异常
	 * 
	 * java.lang.LinkageError: loader (instance of classloader/MyClassLoader):
	 * attempted duplicate class definition for name: "classloader/Programmer"
	 */
	@Test
	public void testDuplicateLoad() {

		MyClassLoader loader = new MyClassLoader();

		loader.defineClazz(classByte, 0, count);

		try {

			loader.defineClazz(classByte, 0, count); // 同一个重复加载同一个类的字节码。

		} catch (java.lang.LinkageError e) {

			assertTrue("the exception MUST thrown", true);

			return;

		}

		assertFalse("the exception MUST thrown", true);

	}

	@Test
	public void testDuplicateLoad2() {

		MyClassLoader loader = new MyClassLoader();

		loader.defineClazz(classByte, 0, count);

		MyClassLoader loader2 = new MyClassLoader();

		loader2.defineClazz(classByte, 0, count);

	}
	
	@Test
	public void testDuplicateLoad3(){
		
		
		
	}

	/**
	 * Uses @See VerboseLoader
	 * @throws Exception 
	 */
	@Test
	public void testDuplicateLoad4() throws Exception {

		ClassLoader base = ClassLoader.getSystemClassLoader();

		URL[] urls = null;
		
		if (base instanceof URLClassLoader) {
			
			urls = ((URLClassLoader) base).getURLs();
			
		} else {
			
			urls = new URL[] { new File(".").toURI().toURL() };
			
		}
		
        // list the paths actually being used
        System.out.println("Loading from paths:");
        
        for (int i = 0; i < urls.length; i++) {
        	
            System.out.println(" " + urls[i]);
            
        }		
        
        VerboseLoader loader = new VerboseLoader(urls, base.getParent());    
        
        Class<?> clazz = loader.loadClass("classloader.Programmer");
        
        loader.loadClass("classloader.Programmer");
        
        Class.forName("classloader.Programmer");
		
	}

	@After
	public void after() throws Exception {

		classStream.close();

	}

}
