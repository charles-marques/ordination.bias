package codemining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.BlockStmt;


public class AppNavigator {
	protected static final Character QUEBRA_LINHA = '\n';
	protected static final CharSequence VAZIO = "";
	protected static final String VIRGULA = ",";
	protected static final String APP = "APP: ";
	protected static final String BARRA = "/";
	protected static final String BUSCANDO_CLASSES = "Buscando Classes...";
	protected static final String CLASSES_ENCONTRADAS = "Classes Encontradas:";
	protected static final String CLASSE = "Classe: ";
	protected static final String CURVA = "+";
	protected static final String ESPACO = " ";
	protected static final String FIM = "Fim";
	protected static final String JAVA = ".java";
	protected static final String LINHA_HORIZONTAL = "--------------------------------------------------------------------";
	protected static final String LATERAL = "|";
	protected static final String LIST = "List";
	protected static final String LISTAS_ENCONTRADAS = "Listas Encontradas:";
	protected static final String METODO = "Método: ";
	protected static final String TIPO = "Tipo: ";
	protected static final String VARIAVEL = "Variável: ";
	protected static List<String> nomesProcurados = Arrays.asList("person", "employer", "employee", "customer",
			"client", "user");
	protected static List<String> sortingTerms = Arrays.asList("sort(", "compareTo(", "compare(", "order(", "orderBy",
			"sortBy", "order by");
	protected static String repositoryPath = "/home/suporte/Documentos/java_projects/101repo";
	protected static String code;
	protected static String st;

	protected static List<String> classesList = new ArrayList<String>();
	protected static List<Classe> selectedClassesList = new ArrayList<Classe>();

	// Config:
	protected static BufferedReader br;
	protected static CompilationUnit compilationUnit;
	protected static File node;
	protected static String tipo;
	private static String projeto;
	protected static List<String[]> resultado = new ArrayList<String[]>();

	protected static void addResultado(String projeto, String classe, String bloco, String statment) throws Exception {
		if (resultado.parallelStream().anyMatch(
				f -> f[0].equals(projeto) && f[1].equals(classe) && f[2].equals(bloco)))
			return;
		resultado.add(new String[] { projeto, classe, statment });
		return;
	}

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

	protected static void printFoundClasses(String projeto) {
		if (selectedClassesList.isEmpty())
			return;
		System.out.println("******************************************");
		System.out.println(APP + projeto);
		System.out.println(CLASSES_ENCONTRADAS);
		selectedClassesList.forEach(c -> System.out.println(c.getNome()));
		System.out.println("******************************************");
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

	protected static List<String> getClassFields(Classe classeSelecionada, List<FieldDeclaration> atributos) {
		List<FieldDeclaration> atributosSelecionados = atributos.stream()
				.filter(f -> f.getCommonType().toString().contains(List.class.getSimpleName())
						&& f.getCommonType().toString().contains(classeSelecionada.getNome()))
				.collect(Collectors.toList());
		List<String> variaveis = new ArrayList<String>();

		if (!atributosSelecionados.isEmpty()) {
			for (FieldDeclaration fieldDeclaration : atributosSelecionados) {
				variaveis.add(fieldDeclaration.getVariable(0).getNameAsString());
			}
		}
		return variaveis;
	}

	protected static List<String> getMethodParameters(Classe classeSelecionada, MethodDeclaration methodDeclaration) {
		List<Parameter> parametrosSelecionados = methodDeclaration.getParameters().stream()
				.filter(f -> f.getTypeAsString().contains(List.class.getSimpleName())
						&& f.getTypeAsString().contains(classeSelecionada.getNome()))
				.collect(Collectors.toList());

		List<String> parametros = new ArrayList<String>();
		if (!parametrosSelecionados.isEmpty()) {
			for (Parameter parameterDeclaration : parametrosSelecionados) {
				List<? extends Parameter> corpoMetodo = methodDeclaration.getBody().get()
						.findAll(parameterDeclaration.getClass());
				parametros.add(parameterDeclaration.getNameAsString());
			}
		}
		return parametros;
	}

	protected static void searchLists(String projeto) throws Exception {
		// VERIFY: IS THERE IN CLASS ANY ATTRIBUTE WHICH IS LIST OF ONE IDENTIFIED
		// CLASSES RESEARCHED?
		for (String classPath : classesList) {
			try {
				node = new File(classPath);
				code = new String();
				br = new BufferedReader(new FileReader(node));
				while ((st = br.readLine()) != null) {
					code = code + st + QUEBRA_LINHA;
				}
				br.close();
				compilationUnit = StaticJavaParser.parse(code);
				Optional<ClassOrInterfaceDeclaration> classe = compilationUnit
						.getClassByName(node.getName().replace(JAVA, VAZIO));
				if (classe.isPresent()) {
					List<String> variaveis = new ArrayList<>();
					List<String> parametros = new ArrayList<>();
					// BUSCANDO FIELDS DOS TIPOS LIST<CLASSE PERSON, CLIENT, USER...>
					List<MethodDeclaration> metodos = classe.get().getMethods();
					List<Parameter> parametrosSelecionados;
					for (Classe classeSelecionada : selectedClassesList) {
						// Classes List fields - OK
						variaveis.addAll(getClassFields(classeSelecionada, classe.get().getFields()));

						// Methods Lists Params and Variables
						for (MethodDeclaration methodDeclaration : metodos) {
							// List Parameters
							parametros.addAll(getMethodParameters(classeSelecionada, methodDeclaration));
						}
						// Method List Variables sorted
					}

					// INSPECIONAR PARAMETROS DECLADAS NO ESCOPO DO MÉTODO

					for (MethodDeclaration metodo : metodos) {
						NodeList<Parameter> parametrosMetodo = metodo.getParameters();
						// XXX verificar existência de parâmetros do tipos classe person, client...
						parametrosSelecionados = parametrosMetodo.stream()
								.filter(f -> f.getTypeAsString().contains(LIST) && selectedClassesList.stream()
										.anyMatch(c -> f.getTypeAsString().contains(c.toString())))
								.collect(Collectors.toList());
						// XXX verificar se há ordenação em algum dos parametros do tipo pesquisado
						if (!parametrosSelecionados.isEmpty()) {
							for (Parameter parametro : parametrosSelecionados) {
								variaveis.add(parametro.getNameAsString());
							}
						}
					}

					// INSPECIONAR MÉTODOS PARA VERIFICAR OCORRENCIAS DAS FIELDS
					for (MethodDeclaration metodo : metodos) {
						Optional<BlockStmt> body = metodo.getBody();
						List<Node> bodyArray = body.get().stream()
								.filter(b -> variaveis.stream().anyMatch(v -> b.toString().contains(v))
										&& sortingTerms.stream().anyMatch(o -> b.toString().contains(o)))
								.collect(Collectors.toList());
						List<Node> statements = bodyArray.stream()
								.filter(b -> sortingTerms.stream().anyMatch(o -> b.toString().contains(o)))
								.collect(Collectors.toList());
						if (statements.isEmpty())
							continue;
						for (Node blockStmt : statements) {
							List<Node> blockNodes = blockStmt.getChildNodes().stream().filter(
									// n -> variaveis.stream().anyMatch(v ->
									// n.toString().contains("Collections.sort(" + v) || n.toString().contains(v +
									// ".sort(") )
									n -> sortingTerms.stream().anyMatch(s -> n.toString().contains(s))
											&& variaveis.stream().anyMatch(v -> n.toString().contains(v)))
									.collect(Collectors.toList());
//							addResultado(projeto, node.getName(), blockStmt.toString());
							for (Node statment : blockNodes) {
								addResultado(projeto, node.getName(), blockStmt.toString(), statment.toString());
							}
						}
					}
				}
			} catch (ParseProblemException e) {
				System.err.println("Classe: " + classPath);
			} catch (AssertionError e) {
				System.err.println("Classe: " + classPath);
			} catch (Exception e) {
				System.err.println("Classe: " + classPath);
			}
		}
		return;
	}

	public static void main(String[] args) {
//		repositoryPath = "/home/suporte/Documentos/java_projects/";
		repositoryPath = "/home/suporte/Workspace/BaiasOrdenacao/codemining/src/main/java/repository";
		File dirStart = new File(repositoryPath);
		String[] diretorios = dirStart.list();
		List<String> excecao = Arrays.asList("DPJ", "openjdk-fontfix", "RawClient", "ceylon-compiler",
				"eclipse.jdt.core", "openjdk7-langtools", "groovy-eclipse");
//		int i = 0;
		for (int k = 0; k < diretorios.length; k++) {
//		for (String projeto : diretorios) {
			try {
				projeto = diretorios[k];
//				i = i + 1;
//				System.out.println(APP + i);
				if (excecao.parallelStream().anyMatch(f -> projeto.contains(f)))
					continue;
				// config variables
				classesList = new ArrayList<>();
				selectedClassesList = new ArrayList<>();
				searchClasses(new File(repositoryPath, projeto));
				if (selectedClassesList.isEmpty())
					continue;
				printFoundClasses(projeto);
				searchLists(projeto);
//				if (i > 1000)
//					break;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
		System.out.println("Ordenações encontradas em: ");
		System.out.println(resultado.size());
		resultado.sort(new Comparator<String[]>() {

			@Override
			public int compare(String[] o1, String[] o2) {
				return (o1[0] + o1[1] + o1[2]).toString().compareTo((o2[0] + o2[1] + o2[2]).toString());
			}
		});
		resultado.forEach(o1 -> {
			System.out.println(o1[0] + ", " + o1[1] + ", " + o1[2]);
		});
		System.out.println(FIM);
	}
}