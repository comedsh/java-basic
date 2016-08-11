package proxy.statics.javassist.modify;

import java.io.File;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

/**
 * 将 @See StringBuilder 源代码中修改为
 * 
 * private String buildString$impl(int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += (char)(i%26 + 'a');
        }
        return result;
   }
   
   private String buildString(int length) {
        long start = System.currentTimeMillis();
        String result = buildString$impl(length);
        System.out.println("Call to buildString took " + (System.currentTimeMillis()-start) + " ms.");
        return result;
   }
   
   buildString 的方法体被替换成新的方法体，用来监控执行时间。而 buildString 的原方法被重命名为 buildString$impl; -> Proxy 的一种实现。
   
   该测试用例是在编译时刻加入
   
 * @author 商洋
 *
 */
public class GenJassistTiming {
	
	static final String CLASSNAME = "proxy.statics.javassist.modify.StringBuilder";
	
	static final String METHODNAME = "buildString";
	
	public static void main(String[] argv) throws Exception{
		
		Class<?> clz;
		
		// 原来，这时时候 StringBuilder.class 才被加载，一种懒加载的策略吧。
		clz = ClassLoader.getSystemClassLoader().loadClass( CLASSNAME );
		
		clz.getDeclaredMethod("main", String[].class ).invoke( null, new Object[]{ new String[]{"10", "20"} });
		
		// start by getting the class file and method
		CtClass clas = ClassPool.getDefault().get( CLASSNAME );
		
		if (clas == null) {
			
			System.err.println("Class " + CLASSNAME + " not found");
			
		} else {

			// add timing intercepter to the class
			
			addTiming( clas, METHODNAME );
			
			/**
			 * ======>>>> 将新生成的 clas 替换原来的 StringBuild.class
			 */
			clas.writeFile( new File(".").getCanonicalPath() + "/bin" );
			
			System.out.println("Added timing to method " + CLASSNAME + "." + METHODNAME );
			
			clz = clas.toClass();
			
			Object o = clz.newInstance();
			
			clz.getDeclaredMethod("main", String[].class ).invoke( null, new Object[]{ new String[]{"10", "20"} });

		}


	}

	/**
	 * 
   private String buildString$impl(int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += (char)(i%26 + 'a');
        }
        return result;
   }
   
   private String buildString(int length) {
        long start = System.currentTimeMillis();
        String result = buildString$impl(length);
        System.out.println("Call to buildString took " +
            (System.currentTimeMillis()-start) + " ms.");
        return result;
   }
	 * 
	 * @param clas
	 * @param mname
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 */
	private static void addTiming(CtClass clas, String mname) throws NotFoundException, CannotCompileException {

		// get the method information (throws exception if method with
		// given name is not declared directly by this class, returns
		// arbitrary choice if more than one with the given name)
		/**
		 * nname -> buildString$impl
		 */
		String nname = mname + "$impl";
		
		try{
			if( clas.getDeclaredMethod(nname) != null ) return; // 如果字节码已经生成过一次，就不再重复生成了。
		}catch(NotFoundException e){}
		
		/**
		 *  mold -> 原来的方法 -> buildString()
		 */
		CtMethod mold = clas.getDeclaredMethod(mname);

		// rename old method to synthetic name, then duplicate the
		// method with original name for use as interceptor
		
		/**
		 * 将 buildString() 更名为 buildString$impl()
		 */
		mold.setName(nname);
		
		/**
		 * 使用更名后的 buildString$impl() 方法 mold 创建一个以原有方法名 buildString 的新方法 mnew。
		 * 这样做的好处是，参数，返回值可以直接重用；需要改动的部分就是方法体，body. 
		 */
		CtMethod mnew = CtNewMethod.copy(mold, mname, clas, null); // 将 mold -> buildString() 拷贝一份作为 mnew -> buildString$impl()

		// start the body text generation by saving the start time
		// to a local variable, then call the timed method; the
		// actual code generated needs to depend on whether the
		// timed method returns a value
		String type = mold.getReturnType().getName();
		
		/**
		 * 修改 mnew 新方法个的方法体
		 */
		StringBuffer body = new StringBuffer();
		body.append("{\nlong start = System.currentTimeMillis();\n");
		if (!"void".equals(type)) {
			body.append(type + " result = ");
		}
		body.append(nname + "($$);\n"); 

		// finish body text generation with call to print the timing
		// information, and return saved value (if not void)
		body.append("System.err.println(\"Call to method " + mname + " took \" +\n (System.currentTimeMillis()-start) + " + "\" ms.\");\n");
		
		if (!"void".equals(type)) {
			body.append("return result;\n");
		}
		body.append("}");

		// replace the body of the interceptor method with generated
		// code block and add it to class
		mnew.setBody(body.toString());
		
		/**
		 * 在原来的 StringBuild.class 中新增 mnew 方法
		 */
		clas.addMethod( mnew );

		// print the generated code block just to show what was done
		System.out.println("Interceptor method body:");
		
		System.out.println( body.toString()) ;
		
	}
}
