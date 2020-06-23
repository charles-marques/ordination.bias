package discoverypattern.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarLoad {

	// String path = "/home/suporte/Workspace/Teste/spyclass.jar";
	public boolean contains(String path, String canonicalName) {
		try {
			File meuRecurso = new File(path);
			URLClassLoader child;
			child = new URLClassLoader(
			        new URL[] {meuRecurso.toURI().toURL()},
			        JarLoad.class.getClassLoader()
			);
			Class<?> classToLoad = Class.forName(canonicalName, true, child);
			if (classToLoad != null) {
				return false;
			} else {
				return false;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

}
