package proxy.statics.javassist.bytecode;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import classloader.MyClassLoader;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

/**
 * 该测试用例通过 Javassist 在 target/classes/com/samples 目录下生成 Programmer.class
 * 
 * 然后通过 @Seee MyClassLoader 进行加载并实例化
 * 
 * @author 商洋
 *
 */
public class ClassByteCodeGenerator {  
	  
    public static void main(String[] args) throws Exception {  
    	
        ClassPool pool = ClassPool.getDefault();
        
        //创建Programmer类       
        CtClass cc= pool.makeClass("com.samples.Programmer");
        
        //定义code方法  
        CtMethod method = CtNewMethod.make("public void code(){}", cc);
        
        //插入方法代码  
        method.insertBefore("System.out.println(\"I'm a Programmer,Just Coding.....\");");
        
        cc.addMethod( method );
        
        //保存生成的 class 字节码  
        cc.writeFile( new File(".").getCanonicalPath() + "/target/classes/" ); // 生成 target/classes/com/samples/Programmer.class
        
        
        // 一下就是动态加载字节码的部分了，于该字节码生成无关了。
        MyClassLoader classLoader = new MyClassLoader();
        
        InputStream classStream = new FileInputStream(new File(".").getCanonicalPath() + "/target/classes/com/samples/Programmer.class");
        
        byte[] classByte = new byte[1024];
        
        classStream.close();
        
        int count = classStream.read( classByte );
        
        Class<?> clz =  classLoader.defineClazz(classByte, 0, count );
        
        Object o = clz.newInstance();
        
        // 发射调用由 javassist 所生成的 class 的方法。
        // 由于是在运行时刻生成的类，不能获取 TYPE ( Programmer )，只能通过反射调用
        clz.getMethod("code", (Class<?>[]) null).invoke(o, (Object[]) null);
        
    }  
}  