package br.inpe.dga.utils;


public class DynamicClassLoader extends ClassLoader{	

	public DynamicClassLoader(){
	}
	
	public DynamicClassLoader(ClassLoader cl){
		super(cl);
	}
	
	public Class defineClass(String name, byte[] b) {
		return defineClass(name, b, 0, b.length);
	}

//  Model for defineClass:
//	
//	protected final Class<?> defineClass(String name, byte[] b, int off, int len)
//	        throws ClassFormatError
//	    {
//	        return defineClass(name, b, off, len, null);
//	    }
	
	
	
}
