package spyclass.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spyclass.Classe;

public class SpyClass {

	private static final String APP = "APP: ";
	private static final String CLASSES_ENCONTRADAS = "Classes Encontradas:";
	private static final String CLASSES_NAO_ENCONTRADAS = "Classes não encontradas";
	private static final Character QUEBRA_LINHA = '\n';
	private static final String JAVA = ".java";
	private static final CharSequence VAZIO = "";
	private static String projeto;

	protected static String repositoryPath = "/home/suporte/Workspace/BaiasOrdenacao/codemining/src/main/java/repository";
	protected static List<String> classesList = new ArrayList<String>();
	protected static List<Classe> selectedClassesList = new ArrayList<Classe>();
	protected static List<String> nomesProcurados = Arrays.asList("person", "employer", "employee", "customer", "client", "user");

	protected static String code;
	protected static BufferedReader br;
	protected static String st;

	private static void addClass(String absolutePath) {
		if (classesList.stream().parallel().anyMatch(f -> f.contains(absolutePath)))
			return;
		classesList.add(absolutePath);
	}

	private static void addSelectedClass(String className, String codigo) {
		if (selectedClassesList.stream().parallel()
				.anyMatch(f -> f.getNome().contains(className.replace(JAVA, VAZIO)) && f.getCodigo().equals(codigo)))
			return;
		selectedClassesList.add(new Classe(projeto, className, codigo));
	}

	public static void searchClasses(File node) throws Exception {
		// VERIFY: IS CLASS NAMED WITH ONE OF CLASSES NAMES?
		if (node.isFile() && node.getPath().endsWith(JAVA)) {
			String[] path = node.getAbsolutePath().split("/");
			String className = path[path.length - 1].replace(JAVA, VAZIO);
			addClass(node.getAbsolutePath());
			if (nomesProcurados.stream().parallel().anyMatch(className.toLowerCase()::contains)) {
				code = new String();
				br = new BufferedReader(new FileReader(node));
				while ((st = br.readLine()) != null) {
					code = code + st + QUEBRA_LINHA;
				}
				br.close();
				addSelectedClass(className, code);
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

	protected static void printFoundClasses(String projeto) {
		System.out.println("******************************************");
		System.out.println(APP + projeto);
		if (selectedClassesList.isEmpty()) {
			System.out.println(CLASSES_NAO_ENCONTRADAS);
		} else {
			System.out.println(CLASSES_ENCONTRADAS);
			selectedClassesList.forEach(c -> System.out.println(c.getNome()));
		}
		System.out.println("******************************************");
	}

	protected static void writeCsvFiles() {
		PrintWriter writerClasses;
		PrintWriter writerSelected;
		try {
			writerClasses = new PrintWriter("classes.csv");
			classesList.forEach(c -> writerClasses.write(c + System.lineSeparator()));
			writerClasses.close();
			writerSelected = new PrintWriter("selected_classes.csv");
			selectedClassesList.forEach(s -> writerSelected.write(s.getNome() + System.lineSeparator()));
			writerSelected.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 * Ex: "/home/suporte/Workspace/BaiasOrdenacao/codemining/src/main/java/repository"
	 */
	public static void main(String[] args) {
//		args = new String[] {"/home/suporte/Workspace/BaiasOrdenacao/spyclass/src/main/java/spyclass/projeto"};
		if (args == null || args.length == 0) {
			System.out.println("Nenhum diretório informado");
			return;
		} else {
			System.out.println(args);
			System.out.println(args.length);
		}
		repositoryPath = args[0];
		String[] path = repositoryPath.split(File.separator);
		projeto = path[path.length - 1];
		try {
			File dirStart = new File(repositoryPath);
			String[] diretorios = dirStart.list();
			searchClasses(new File(repositoryPath));
			printFoundClasses(projeto);
			writeCsvFiles();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return;
	}

}
