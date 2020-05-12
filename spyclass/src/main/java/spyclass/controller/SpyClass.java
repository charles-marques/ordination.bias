package spyclass.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import spyclass.Classe;
import spyclass.Resultado;

public class SpyClass {

	protected static final Character QUEBRA_LINHA = '\n';
	protected static final String JAVA = ".java";
	protected static final CharSequence VAZIO = "";
	protected static final List<String> nomesProcurados = Arrays.asList("person", "employee", "customer", "client",
			"user", "pessoa", "empregado", "cliente", "usuário");
	protected static final List<String> atributosNome = Arrays.asList("name", "nome", "nick");
	protected static final List<String> atributosProcurados = Arrays.asList("age", "sex", "birthday", "idade", "sexo",
			"nascimento", "telefone", "contato");

	protected static Resultado resultado = new Resultado();

	private static String code;
	private static BufferedReader br;
	private static String st;

	public static void searchClasses(File node) throws Exception {
		// VERIFY: IS CLASS NAMED WITH ONE OF CLASSES NAMES?
		if (node.isFile() && node.getPath().endsWith(JAVA)) {
			resultado.addClass(new Classe(node.getName(), node.getAbsolutePath(), isRepresentative(node)));
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

	private static Boolean isRepresentative(File node) throws FileNotFoundException, IOException {
		String[] path = node.getAbsolutePath().split("/");
		String className = path[path.length - 1].replace(JAVA, VAZIO);
		if (nomesProcurados.stream().parallel().anyMatch(className.toLowerCase()::contains)) {
			code = new String();
			br = new BufferedReader(new FileReader(node));
			while ((st = br.readLine()) != null) {
				code = code + st + QUEBRA_LINHA;
			}
			br.close();

			CompilationUnit compilationUnit = StaticJavaParser.parse(code);
			Optional<ClassOrInterfaceDeclaration> classe = compilationUnit.getClassByName(className);
			ClassOrInterfaceDeclaration instancia = classe.get();
//			Stream<FieldDeclaration> campos = instancia.getFields().stream();
			boolean possuiAtributoNome = instancia.getFields().stream()
					.anyMatch(f -> atributosNome.stream().anyMatch(n -> f.getTokenRange().toString().contains(n)));
			boolean possuiAttibutoProcurado = instancia.getFields().stream().anyMatch(
					f -> atributosProcurados.stream().anyMatch(n -> f.getTokenRange().toString().contains(n)));
			return possuiAtributoNome && possuiAttibutoProcurado;
		} else {
			return false;
		}
	}

	/**
	 * @param args Ex: args = new String[] {
	 *             "/home/suporte/Workspace/BaiasOrdenacao/codemining/src/main/java/repository"};
	 *             args = new String[] {
	 *             "/home/suporte/Workspace/BaiasOrdenacao/code-example" };
	 */
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			System.err.println("Nenhum diretório informado");
			return;
		} else {
			System.out.println(args);
			System.out.println(args.length);
		}
		try {
			searchClasses(new File(args[0]));
			resultado.writeCsvFiles();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return;
	}
}
