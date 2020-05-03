package discoverypattern;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import spoon.Launcher;
import spoon.reflect.declaration.CtClass;

public class TestLoad {

	private static final char QUEBRA_LINHA = '\n';

	public static void main(String[] args) {
		try {
			String classPath = "/home/suporte/Workspace/BaiasOrdenacao/code-example/src/main/java/code/example/source/Client.java";
			File node = new File(classPath);
			String code = new String();
			BufferedReader br = new BufferedReader(new FileReader(node));
			String st;
			while ((st = br.readLine()) != null) {
				code = code + st + QUEBRA_LINHA;
			}
			br.close();
			CtClass clazz = Launcher.parseClass(code);
			System.out.println(clazz.getSimpleName());
			System.out.println(clazz.getConstructors());
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
