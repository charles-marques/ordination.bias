package spyclass.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import spyclass.Resultado;

public class SpyClass {

	private static final Character QUEBRA_LINHA = '\n';
	private static final String JAVA = ".java";
	private static final CharSequence VAZIO = "";
	private static String projeto;

	protected static String repositoryPath = "/home/suporte/Workspace/BaiasOrdenacao/codemining/src/main/java/repository";
	protected static Resultado resultado = new Resultado();
	protected static List<String> nomesProcurados = Arrays.asList("person", "employer", "employee", "customer",
			"client", "user");

	protected static String code;
	protected static BufferedReader br;
	protected static String st;

	public static void searchClasses(File node) throws Exception {
		// VERIFY: IS CLASS NAMED WITH ONE OF CLASSES NAMES?
		if (node.isFile() && node.getPath().endsWith(JAVA)) {
			String[] path = node.getAbsolutePath().split("/");
			String className = path[path.length - 1].replace(JAVA, VAZIO);
			resultado.addClass(node.getAbsolutePath());
			if (nomesProcurados.stream().parallel().anyMatch(className.toLowerCase()::contains)) {
				code = new String();
				br = new BufferedReader(new FileReader(node));
				while ((st = br.readLine()) != null) {
					code = code + st + QUEBRA_LINHA;
				}
				br.close();
				resultado.addSelectedClass(projeto, className, code);
			}
			return;
		} else if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				searchClasses(new File(node, filename));
			}
		} else {
			// NÃO É ARQUIVO '.JAVA'
		}
		return;
	}

	/**
	 * @param args Ex:
	 *             "/home/suporte/Workspace/BaiasOrdenacao/codemining/src/main/java/repository"
	 *             "/home/suporte/Workspace/BaiasOrdenacao/code-example"
	 */
	public static void main(String[] args) {
//		args = new String[] {"/home/suporte/Workspace/BaiasOrdenacao/spyclass/src/main/java/spyclass/projeto"};
		if (args == null || args.length == 0) {
			System.out.println("Nenhum diretório informado");
//			return;
			args = new String[] { "/home/suporte/Workspace/BaiasOrdenacao/code-example" };
		} else {
			System.out.println(args);
			System.out.println(args.length);
		}
		repositoryPath = args[0];
		String[] path = repositoryPath.split(File.separator);
		projeto = path[path.length - 1];
		try {
//			File dirStart = new File(repositoryPath);
//			String[] diretorios = dirStart.list();
			searchClasses(new File(repositoryPath));
			resultado.printFoundClasses(projeto);
			resultado.writeCsvFiles();
			resultado.writeJsonFile();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return;
	}
}
