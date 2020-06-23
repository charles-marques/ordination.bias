package discoverypattern.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.utils.SourceRoot;

import discoverypattern.Classe;

public class Discovery {
	private static final String VARIAVEL = "VARIÁVEL";
	private static final String PARAMETRO = "PARAMETRO";
	private static final String CLASSE = "CLASSE";
	private final String MSG_EXISTE_CLASSE = "Não foi possível identificar, já existe uma classe List, em pacote java.util, declarada no projeto que não é da JDK.";
	private final String CHAVE_FECHANDO = ">";
	private final String LIST_TYPE = "List<";
	private List<Classe> resultado = new ArrayList<Classe>();

	private List<String> listaJars = new ArrayList<String>();
	private List<String> listaZip = new ArrayList<String>();
	private List<String> listaWar = new ArrayList<String>();
	private List<String> listaEar = new ArrayList<String>();
	private List<String> listaRar = new ArrayList<String>();

	private List<String> atributosDeClasseProcurados = new ArrayList<String>();

	private void addResultado(Classe classe) throws Exception {
		if (resultado.contains(classe))
			return;
		resultado.add(classe);
		return;
	}

	private void addAtributoProcurado(String atributo) {
		atributosDeClasseProcurados.add(atributo);
	}

	/**
	 * busca no projeto os recuros do tipo jar,rar,war, zip e ear
	 */
	private void buscaRecursos(String endereco) {
		File root = new File(endereco);
		if (root.isDirectory()) {
			String[] enderecos = root.list();
			for (String filho : enderecos) {
				buscaRecursos(filho);
			}
		} else {
			if (endereco.endsWith(".jar")) {
				listaJars.add(endereco);
			}
			if (endereco.endsWith(".zip")) {
				listaZip.add(endereco);
			}
			if (endereco.endsWith(".rar")) {
				listaRar.add(endereco);
			}
			if (endereco.endsWith(".ear")) {
				listaEar.add(endereco);
			}
			if (endereco.endsWith(".war")) {
				listaWar.add(endereco);
			}
		}

	}

	private void verificarRecursosExternos(String path) throws Exception {
		buscaRecursos(path);
		JarLoad jarLoad = new JarLoad();
		ZipLoad zipLoad = new ZipLoad();

		// XXX verificar a existência de uma classe List em um pacote java.util;
		// - verificar os recursos .jar, .zip, .rar, .war e .ear
		for (String jar : listaJars) {
			if (jarLoad.contains(jar, List.class.getCanonicalName())) {
				throw new Exception(MSG_EXISTE_CLASSE);
			}
		}
		for (String zip : listaZip) {
			if (zipLoad.contains(zip, List.class.getCanonicalName())) {
				throw new Exception(MSG_EXISTE_CLASSE);
			}
		}
		if (!listaEar.isEmpty() || !listaRar.isEmpty() || !listaWar.isEmpty()) {
			throw new Exception(
					"Não foi possível identificar: recurso (.ear, .rar ou .war) encontrado pode conter outra implementação da java.util.List da JDK.");
		}
	}

	private void verificarRecursosInterno(List<ParseResult<CompilationUnit>> parseResults) throws Exception {
		Stream<ParseResult<CompilationUnit>> pacotes = parseResults.stream()
				.filter(pr -> pr.getResult().get().getPrimaryTypeName().get().equals(List.class.getSimpleName())
						&& pr.getResult().get().getPackageDeclaration().get().getNameAsString()
								.equals(List.class.getPackage().getName()));
		Long zero = 0L;
		if (!zero.equals(pacotes.count())) {
			throw new Exception(MSG_EXISTE_CLASSE);
		}
	}

	private void inspect(String[] args) {
		try {
			if (args == null || args.length != 2) {
				throw new Exception(
						"Parametros não informados: deve ser informado o projeto e arquivo de classes de interesse ");
			}
			// TODO adicinar opção de dentificação a classe alvo:
			// - adicionar o SpyClass aqui para uma forma padrão caso não seja informada a
			// classe pessoa ou o usuário não saiba

			File javaProject = new File(args[0]);
			if (!javaProject.isDirectory()) {
				throw new FileNotFoundException("Endereço informado não é um diretório/projeto.");
			}
			File rooProjectRoot = new File(
					args[0] + File.separator + "src" + File.separator + "main" + File.separator + "java");
			if (!rooProjectRoot.exists()) {
				throw new FileNotFoundException(
						"Diretório java padrão 'project_name/src/main/java' não encontrado: não é projeto java padrão");
			}

//			loadClassesDeInteresse(args[1]);
			verificarRecursosExternos(javaProject.getAbsolutePath());

			// XXX Load Project Source Code
			URI uri = rooProjectRoot.toURI();
			Path projectRoot = Paths.get(uri);
			SourceRoot sourceRoot = new SourceRoot(projectRoot);

			List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse();

			// XXX verificar a existência de uma classe List em um pacote java.util no
			// próprio projeto;
			verificarRecursosInterno(parseResults);

			// XXX SETUP
			String[] projetoArray = javaProject.getPath().split(File.separator);
			String projeto = projetoArray[projetoArray.length - 1];
			String[] classeInteresse = args[1].split("\\.");
			int tamanho = classeInteresse.length;
			String nomeClasseInteresse = classeInteresse[tamanho - 1];
			String pacoteClasseInteresse = String.join(".",
					Arrays.asList(classeInteresse).subList(0, classeInteresse.length - 2));
			System.out.println("Classe: ");
			System.out.println("Classe de Interesse: " + nomeClasseInteresse);
			System.out.println("Pacote de Interesse: " + pacoteClasseInteresse);
			resultado = new ArrayList<Classe>();

			parseResults.forEach(pr -> {
				CompilationUnit cu = pr.getResult().get();
				String classeLocal = cu.getPrimaryTypeName().get();
				// XXX verificar a existência de propriedades List<ClasseDeInteresse>
				verifyFields(cu, nomeClasseInteresse);
				// XXX verificar métodos:
				List<MethodDeclaration> lista = cu.findAll(MethodDeclaration.class);
				lista.forEach(md -> inspectMethods(projeto, nomeClasseInteresse, classeLocal, md));
			});

//			printResults(parseResults);
//			parseResults.forEach(Discovery::show);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadClassesDeInteresse(String csvClassesPath) {
		String line = "";
		String cvsSplitBy = ",";
		try (BufferedReader br = new BufferedReader(new FileReader(csvClassesPath))) {
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] country = line.split(cvsSplitBy);
				System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void verifyFields(CompilationUnit cu, String nomeClasseInteresse) {
		System.out.println("Classe: " + cu.getPrimaryTypeName().get());
		System.out.println("Packag: " + cu.getPackageDeclaration().get().getNameAsString());
		List<FieldDeclaration> lista = cu.findAll(FieldDeclaration.class).stream()
				.filter(fd -> fd.getElementType().asString().equals(LIST_TYPE + nomeClasseInteresse + CHAVE_FECHANDO))
				.collect(Collectors.toList());
		if (lista.isEmpty())
			return;
		for (FieldDeclaration n : lista) {
			FieldDeclaration fieldDeclaration = n.asFieldDeclaration();
//			System.out.println(fieldDeclaration);
//			System.out.print("Line: ");
//			System.out.println(n.getBegin().get().line);
//			System.out.println("ElementType:");
//			System.out.println(fieldDeclaration.getElementType());
//			System.out.println("Name:");
//			System.out.println(n.getVariables());
//			n.getVariables().forEach(v -> System.out.println(v.getName().asString()));
			addAtributoProcurado(fieldDeclaration.getVariables().get(0).getNameAsString());
		}
		;
	}

	/**
	 * @param identificador
	 * @return * Exemplos de ordenação: "Collections.sort(" ${IDENTIFYER}");"
	 *         Collections.sort(${IDENTIFYER}"," ${IDENTIFYER}".sort("
	 */
	private List<String> ordenacoes(String identificador) {
		return Arrays.asList("Collections.sort(" + identificador + ");", "Collections.sort(" + identificador + ",",
				identificador + ".sort(");
	}

	private void inspectMethods(String projeto, String nomeClasseInteresse, String classeLocal,
			MethodDeclaration methodDeclaration) {
		// XXX verificar a existência de parametros List<ClasseDeInteresse>
//		List<Parameter> parametros = methodDeclaration.getParameters().stream()
//				.filter(p -> p.getTypeAsString().equals(LIST_TYPE + nomeClasseInteresse + CHAVE_FECHANDO))
//				.collect(Collectors.toList());
		List<Parameter> parametros = methodDeclaration.findAll(Parameter.class).stream()
				.filter(p -> p.getTypeAsString().equals(LIST_TYPE + nomeClasseInteresse + CHAVE_FECHANDO))
				.collect(Collectors.toList());
		List<Parameter> parametrosProcurados = parametros.stream()
				.filter(p -> !atributosDeClasseProcurados.contains(p.getNameAsString())).collect(Collectors.toList());
		// XXX verificar a existência de variaveis List<ClasseDeInteresse>
		List<VariableDeclarator> variaveis = methodDeclaration.findAll(VariableDeclarator.class).stream()
				.filter(p -> p.getTypeAsString().equals(LIST_TYPE + nomeClasseInteresse + CHAVE_FECHANDO))
				.collect(Collectors.toList());
		List<VariableDeclarator> variaveisProcuradas = variaveis.stream()
				.filter(v -> !atributosDeClasseProcurados.contains(v.getNameAsString())).collect(Collectors.toList());
		// XXX verificar a existência de atributos de classe List<ClasseDeInteresse>
		List<String> atributosProcurados = atributosDeClasseProcurados.stream()
				.filter(a -> !parametros.stream().anyMatch(p -> p.getNameAsString().equals(a))
						&& !variaveis.stream().anyMatch(v -> v.getNameAsString().equals(a)))
				.collect(Collectors.toList());
		if (methodDeclaration.getBody().isPresent()) {
			BlockStmt blockStmt = methodDeclaration.getBody().get();
			System.out.println("Block");
			System.out.println(blockStmt);

			// XXX verificar a existência de ordenações em algum dos atributos, parametros
			// ou variaveis no corpo do método
			blockStmt.getStatements().forEach(s -> {
				atributosProcurados.forEach(a -> {
					List<String> ordenacoes = ordenacoes(a);
					if (ordenacoes.stream().anyMatch(o -> s.toString().startsWith(o))) {
						try {
							System.out.printf("%20s %20s %20s %20s %20s %20s \n", projeto, nomeClasseInteresse,
									classeLocal, s.getBegin().get().line, CLASSE, s.toString());
							addResultado(new Classe(projeto, nomeClasseInteresse, classeLocal, s.getBegin().get().line,
									CLASSE, s.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				parametrosProcurados.forEach(p -> {
					String pname = p.getNameAsString();
					List<String> ordenacoes = ordenacoes(pname);
					if (ordenacoes.stream().anyMatch(o -> s.toString().startsWith(o))) {
						try {
							System.out.printf("%20s %20s %20s %20s %20s %20s \n", projeto, nomeClasseInteresse,
									classeLocal, s.getBegin().get().line, PARAMETRO, s.toString());
							addResultado(new Classe(projeto, nomeClasseInteresse, classeLocal, s.getBegin().get().line,
									PARAMETRO, s.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				variaveisProcuradas.forEach(v -> {
					String vname = v.getNameAsString();
					List<String> ordenacoes = ordenacoes(vname);
					if (ordenacoes.stream().anyMatch(o -> s.toString().startsWith(o))) {
						try {
							System.out.printf("%20s %20s %20s %20s %20s %20s \n", projeto, nomeClasseInteresse,
									classeLocal, s.getBegin().get().line, VARIAVEL, s.toString());
							addResultado(new Classe(projeto, nomeClasseInteresse, classeLocal, s.getBegin().get().line,
									VARIAVEL, s.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			});
		}
		;

	}

	/**
	 * Devem ser passados dois parâmetros: path do projeto, path arquivo com as
	 * classe pessoa dos sistema caso não seja passado a classe pessoa, o sistema
	 * irá rodar um algoritmo padrão, o spyclass, para identificar a mesma.
	 *
	 * @param args
	 */
	// args = new String[] {
	// "/home/suporte/Documentos/java_projects/cafeweb","com.ondei.webdelivery.domain.Usuario"
	// };
	// args = new String[] { "/home/suporte/Documentos/java_projects/networkTalk" };
	// args = new String[] {
	// "/home/suporte/Workspace/BaiasOrdenacao/code-example","code.example.source.User"
	// };
	// args = new String[] {
	// "/home/suporte/Documentos/java_projects/the-k-network/the-k-network" };
	public static void main(String[] args) {
		args = new String[] { "/home/suporte/Workspace/code-example-simple", "java.lang.String" };
		Discovery discovery = new Discovery();
		discovery.inspect(args);
	}
}
