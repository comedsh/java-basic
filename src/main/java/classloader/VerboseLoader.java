package classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class VerboseLoader extends URLClassLoader{
	
    protected VerboseLoader(URL[] urls, ClassLoader parent) {
    	
        super(urls, parent);
        
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Class loadClass(String name) throws ClassNotFoundException {
    	
        System.out.println("loadClass: " + name);
        
        return super.loadClass(name);
        
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected Class findClass(String name) throws ClassNotFoundException {
    	
        Class clas = super.findClass(name);
        
        System.out.println("findclass: loaded " + name + " from this loader");
        
        return clas;
        
    }
    
}