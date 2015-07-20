package cn.wensiqun.asmsupport.client;

import java.util.HashMap;
import java.util.Map;

import cn.wensiqun.asmsupport.standard.def.clazz.IClass;
import cn.wensiqun.asmsupport.standard.error.ASMSupportException;
import cn.wensiqun.asmsupport.standard.utils.AsmsupportClassLoader;

public class DummyImportable extends AbstractDummy {

	
	private SimpleClassNameClassLoader simpleClassNameClassLoader;
	
	DummyImportable(AsmsupportClassLoader classLoader) {
		super(new SimpleClassNameClassLoader(classLoader));
		simpleClassNameClassLoader = (SimpleClassNameClassLoader) this.getClassLoader();
	}

	public void importClass(String className) {
		simpleClassNameClassLoader.importClass(className);
	}
	
	private static class SimpleClassNameClassLoader extends AsmsupportClassLoader {
		
		private AsmsupportClassLoader target;
		
		/**
		 * Key : simpleName
		 * Valeu : fullName
		 */
		private Map<String, String> classSimpleNameMap = new HashMap<String, String>();
		
		public SimpleClassNameClassLoader(AsmsupportClassLoader target) {
			super();
			this.target = target;
		}

		@Override
		public IClass getType(Class<?> clazz) {
			return target.getType(clazz);
		}

		@Override
		public Class<?> defineClass(String name, byte[] classBytes, IClass itype)
				throws Exception {
			return target.defineClass(name, classBytes, itype);
		}

		@Override
		public IClass getType(String className) {
			IClass clazz;
			//get from import map
			if(classSimpleNameMap.containsKey(className)) {
				clazz = target.getType(classSimpleNameMap.get(className));
				if(clazz != null) {
					return clazz;
				}
			}
			clazz = target.getType(className);
			if(clazz == null) {
				if(className.indexOf('.') == -1) {
					clazz = target.getType("java.lang." + className);
				}
			}
			return clazz;
		}

		@Override
		public IClass getArrayClass(Class<?> root, int dim) {
			return target.getArrayClass(root, dim);
		}

		@Override
		public IClass getArrayClass(IClass root, int dim) {
			return target.getArrayClass(root, dim);
		}

		@Override
		public Class<?> doAsmsupportFindClass(String name)
				throws ClassNotFoundException {
			throw new UnsupportedOperationException("Never called.");
		}

		private String getSimpleName(String name) {
			if(name.indexOf('.') > 0) {
				return name.substring(name.lastIndexOf('.') + 1);
			}
			return name;
		}
		
		private void importClass(String qualifierName) {
			String simpleName = getSimpleName(qualifierName);
			if(classSimpleNameMap.containsKey(simpleName)) {
				throw new ASMSupportException("The simple name '" + simpleName + 
						"' of class '" + qualifierName + "' has already imported." + " It's exist class name is '" + classSimpleNameMap.get(simpleName) + "'");
			}
			classSimpleNameMap.put(simpleName, qualifierName);
		}
	}
}