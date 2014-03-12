package cn.wensiqun.asmsupport.loader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;

import cn.wensiqun.asmsupport.utils.lang.StringUtils;


/**
 * 
 * <h3>Release notes</h3>
 * <ul>
 * <li><b>version 0.4</b> : Thanks for "aruanruan at vip.sina.com"(oschina account "aruan") suggestion. for detail see <a href="http://code.taobao.org/p/asmsupport/issue/31553/">http://code.taobao.org/p/asmsupport/issue/31553/</a> </li>
 * </ul>
 * 
 * @author <b>wensiqun</b> at gmail, <b>aruanruan</b> at vip.sina.com
 * @version 0.4
 * 
 */
public class ASMClassLoader extends ClassLoader {
	
	private static Map<Key, byte[]> classByteMap = new ConcurrentHashMap<Key, byte[]>();
	
	private ASMClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	public static ASMClassLoader getInstance() {
		ASMClassLoader classloader = null;
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl != null) {
			if (cl instanceof ASMClassLoader) {
				classloader = (ASMClassLoader) cl;
			} else {
				classloader = new ASMClassLoader(Thread.currentThread()
						.getContextClassLoader());
			}
		} else {
			classloader = new ASMClassLoader(
					ClassLoader.getSystemClassLoader());
		}
		return classloader;
	}
	
	public final Class<?> defineClass(String name, byte[] b) throws ClassFormatError {
		Class<?> clazz = super.defineClass(name, b, 0, b.length);
		classByteMap.put(new Key(clazz), b);
		return clazz;
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		Set<Key> keySet = classByteMap.keySet();
		if(CollectionUtils.isNotEmpty(keySet)){
			for(Key k : keySet){
				if(k.equals(new Key(name))){
					return k.clazz;
				}
			}
		}
		return super.findClass(name);
	}
	
	@Override
	public InputStream getResourceAsStream(String name) {
		byte[] byteArray = classByteMap.get(new Key(name));
		InputStream stream = null;
		if(byteArray != null){
			stream = new ByteArrayInputStream(byteArray);
		}
		
		if(stream == null){
			stream = super.getResourceAsStream(name);
		}
		
		if (stream == null) {
			stream = ClassLoader.getSystemClassLoader().getResourceAsStream(name);
		}
		return stream;
	}
	
	/**
	 * e.g : "<b>jw.asmsupport.loader.ASMClassLoader<b>" convert to "<b>jw/asmsupport/loader/ASMClassLoader.class<b>"
	 * 
	 * @param classQualifiedName
	 * @return
	 */
	private String translateClassName(String classQualifiedName){
		return classQualifiedName.replace('.', '/') + ".class";
	}

	private class Key {
		
		private Class<?> clazz;

		/**
		 * class qualified name
		 */
		private String name;

		public Key(Class<?> clazz) {
			this.clazz = clazz;
		}

		public Key(String name) {
			this.name = name;
		}
		
		@Override
		public int hashCode() {
			if(clazz != null){
				return translateClassName(clazz.getName()).hashCode();
			}else{
				return name.hashCode();
			}
		}

		@Override
		public boolean equals(Object obj) {
			if(obj != null && obj instanceof Key){
			    Key k = (Key) obj;
			    String currentName = clazz == null ? name : translateClassName(clazz.getName());
			    String compareName = k.clazz == null ? k.name : translateClassName(k.clazz.getName());
			    if(StringUtils.equals(currentName, compareName)){
			    	return true;
			    }
			}
			return false;
		}
	}
	
	
}
