package spyclass.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;

import spyclass.Classe;
import spyclass.Resultado;

public class SpyClass {

	private static final String SRC_MAIN_JAVA = "/src/main/java/";
	private static final String SRC_TEST_JAVA = "/src/test/java/";
	private static final String PONTO = ".";
	private static final String SRC = "src";
	private final Character QUEBRA_LINHA = '\n';
	private final static String JAVA = ".java";
	private final static CharSequence VAZIO = "";
	private final List<String> classesProcuradas = Arrays.asList("person", "employee", "customer", "client", "user",
			"pessoa", "empregado", "cliente", "usuario");
	private final List<String> nomesProdurados = Arrays.asList("name", "nome", "nick", "username", "firstName",
			"fullName");
	private final List<String> atributosProcurados = Arrays.asList("age", "sex", "birthday", "password", "idade",
			"sexo", "nascimento", "contato", "senha");

	private Resultado resultado;

//	private static String code;
	private static BufferedReader br;
	private static String st;

	public Resultado getResultado() {
		return resultado;
	}

	public void searchClasses(File node) throws Exception {
		// VERIFY: IS CLASS NAMED WITH ONE OF CLASSES NAMES?
		if (node.isFile() && node.getPath().endsWith(JAVA)) {
			try {
				if (isRepresentative(node)) {
					resultado.addClass(new Classe(node.getName(), node.getAbsolutePath(), Boolean.TRUE));
				}
			} catch (Exception e) {
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

	private Boolean isRepresentative(File node) throws FileNotFoundException, IOException {
		String className = node.getName().replace(JAVA, VAZIO);
		try {
			if (classesProcuradas.stream().parallel()
					.anyMatch(cp -> className.toLowerCase().equals(cp.toLowerCase()))) {
				CompilationUnit compilationUnit = getCompilationUnit(node);
				Optional<ClassOrInterfaceDeclaration> classe = compilationUnit.getClassByName(className);
				ClassOrInterfaceDeclaration instancia = classe.get();
				Stream<FieldDeclaration> fields = instancia.getFields().stream();
				Optional<FieldDeclaration> fieldPrincipal = fields
						.filter(f -> nomesProdurados.stream()
								.anyMatch(n -> f.getTokenRange().toString().toLowerCase().contains(n.toLowerCase())))
						.findFirst();
				if (!fieldPrincipal.isPresent())
					return false;
				boolean possuiAttibutoProcurado = instancia.getFields().stream().anyMatch(f -> atributosProcurados
						.stream().anyMatch(n -> f.getTokenRange().toString().toLowerCase().contains(n.toLowerCase())));
//						.stream().anyMatch(n -> f.getTokenRange().toString().toLowerCase().contains(n.toLowerCase()) && !f.getTokenRange().toString().equals(fieldPrincipal.get().getTokenRange().toString())));
				return possuiAttibutoProcurado;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	private CompilationUnit getCompilationUnit(File node) throws FileNotFoundException, IOException {
		FileInputStream inputStream = new FileInputStream(node);
		CompilationUnit compilationUnit = StaticJavaParser.parse(inputStream);
		return compilationUnit;
	}

	/**
	 * @param args = new String[] {
	 *             "/home/suporte/Workspace/BaiasOrdenacao/codemining/src/main/java/repository"};
	 *             args = new String[] {
	 *             "/home/suporte/Workspace/BaiasOrdenacao/code-example" }; args =
	 *             new String[] { "/home/suporte/Documentos/java_projects/cafeweb"
	 *             }; args = new String[] {
	 *             "/home/suporte/Documentos/java_projects/networkTalk" }; args =
	 *             new String[] {
	 *             "/home/suporte/Documentos/java_projects/the-k-network/the-k-network"
	 *             };
	 */
	public void inspect(String projeto) {
		if (projeto == null || projeto.trim().isEmpty()) {
			System.err.println("Nenhum diretório informado");
			return;
		} else {
		}
		try {
			resultado = new Resultado();
			searchClasses(new File(projeto));
//			resultado.writeCsvFiles();
		} catch (Exception e) {

		} finally {
		}
	}

	private static String projectPath(Classe c) {
		List<String> arrayPath = Arrays.asList(c.getClassPath().split(File.separator));
		int indice = 0;
		for (int i = arrayPath.size() - 1; i > 0; i--) {
			if (arrayPath.get(i).equals(SRC)) {
				indice = i;
			}
		}
		List<String> arrayProjectPath = arrayPath.subList(0, indice);
		String projectPath = String.join(File.separator, arrayProjectPath);
		return projectPath;
	}

	private static String canonicalName(Classe c, String projectPath) {
		return c.getClassPath().replace(SRC_TEST_JAVA, VAZIO).replace(SRC_MAIN_JAVA, VAZIO).replace(projectPath, VAZIO)
				.replace(JAVA, VAZIO).replace(File.separator, PONTO);
	}

	public static void main(String[] args) {
		try {
			String rootPath = "/home/suporte/Documentos/java_projects/";
//			String rootPath = "/home/suporte/Workspace/Teste/projetos";
			File root = new File(rootPath);
			SpyClass spy = new SpyClass();
			List<String> excessoes = Arrays.asList("warlock2", "sojo", "JTwitter", "prjReborn", "head",
					"picketbox-solder", "cometd", "chililog-server", "gisgraphy-mirror", "eXist-1.4.x", "netty",
					"abiquo", "VT-Class-Scheduler", "classpath");
			PrintWriter writer = new PrintWriter("projetos_selecionados.csv");
			if (root.isDirectory()) {
				String[] projectList = root.list();
				int i = 0;
				for (String projeto : projectList) {
					i = i + 1;
					System.out.printf("%5s - %s \n", i, projeto);
					if (excessoes.contains(projeto))
						continue;
					spy.inspect(root.getPath() + File.separator + projeto);
					if (!spy.getResultado().getClassesList().isEmpty()) {
						List<Classe> classes = spy.getResultado().getClassesList();
						classes.forEach(c -> {
							String projectPath = projectPath(c);
							writer.write(
									projectPath + "," + canonicalName(c, projectPath) + "," + c.getClassPath() + "\n");
						});
					}
				}
				System.out.println("============================================================================");
				System.out.println("Escrevendo arquivo...");
				// projetosSelecionados.forEach(p ->{
				// System.out.printf("%20s %20s %20s\n", p[0], p[1], p[2]);
				// });
				writer.close();
				System.out.println("Fim!");
			} else {
				// NÃO É ARQUIVO '.JAVA'
			}
		} catch (Exception e) {

		}
	}
}
