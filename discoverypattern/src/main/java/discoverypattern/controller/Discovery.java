package discoverypattern.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
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
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;

public class Discovery {
	private static final String CHAVE_FECHANDO = ">";
	private static final String LIST_TYPE = "List<";
	private static final String ARRAYLIST_TYPE = "ArrayList<";
	private static final String LINKEDLIST_TYPE = "LinkedList<";
	private static final String FIM = "Fim";
	private static final String JAVA = ".java";
	private static final String LIST = "List";
	private static final Character QUEBRA_LINHA = '\n';
	private static final CharSequence VAZIO = "";

	protected static List<String> classesList = new ArrayList<String>();
	protected static List<String[]> resultado = new ArrayList<String[]>();
	protected static CompilationUnit compilationUnit;
	protected static BufferedReader br;
	protected static String code;
	protected static File node;
	protected static String st;

	protected static List<String> sortingTerms = Arrays.asList("sort(", "compareTo(", "compare(", "order(", "orderBy", "sortBy", "order by");
	protected static List<Classe> selectedClassesList = new ArrayList<Classe>();

	protected static void addResultado(String projeto, String classe, String bloco, String statment) throws Exception {
		if (resultado.parallelStream().anyMatch(
				f -> f[0].equals(projeto) && f[1].equals(classe) && f[2].equals(bloco)))
			return;
		resultado.add(new String[] { projeto, classe, statment });
		return;
	}

	/**
	 * @param classeSelecionada
	 * @param atributos
	 * @return {@link List}<{@link String}> de variáveis da classe
	 */
	protected static List<String> getClassFields(Classe classeSelecionada, List<FieldDeclaration> atributos) {
		List<FieldDeclaration> atributosSelecionados = atributos.stream().filter(
				f -> getListTypes(classeSelecionada).stream().anyMatch(
						cs -> f.getElementType().toClassOrInterfaceType().get().asString().equals(cs)
				)
			).collect(Collectors.toList());
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
						&& f.getClass().isInstance(List.class)
						&& f.getTypeAsString().contains(classeSelecionada.getNome()))
				.collect(Collectors.toList());

		List<String> parametros = new ArrayList<String>();
		if (!parametrosSelecionados.isEmpty()) {
			for (Parameter parameterDeclaration : parametrosSelecionados) {
				parametros.add(parameterDeclaration.getNameAsString());
			}
		}
		return parametros;
	}

	private static List<String> getListTypes(Classe classeSelecionada) {
		List<String> tipos = new ArrayList<String>();
		tipos.add(LIST_TYPE + classeSelecionada.getNome() + CHAVE_FECHANDO);
		tipos.add(ARRAYLIST_TYPE + classeSelecionada.getNome() + CHAVE_FECHANDO);
		tipos.add(LINKEDLIST_TYPE + classeSelecionada.getNome() + CHAVE_FECHANDO);
		return tipos;
	}

	private static void searchLists(String projeto) throws Exception {
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

				// XXX CONFIGURAÇÃO
				List<String> variaveis = new ArrayList<>();
				List<String> parametros = new ArrayList<>();
				List<MethodDeclaration> metodos = classe.get().getMethods();
				List<Parameter> parametrosSelecionados;

				if (classe.isPresent()) {

					// XXX BUSCANDO FIELDS DA CLASSE DO TIPOS LIST<PERSON, CLIENT, USER...>
					selectedClassesList.forEach(classeSelecionada -> {
						variaveis.addAll(getClassFields(classeSelecionada, classe.get().getFields()));
					});

					// Methods Lists Params and Variables
					selectedClassesList.forEach(classeSelecionada -> {
						for (MethodDeclaration methodDeclaration : metodos) {
							// List Parameters
							parametros.addAll(getMethodParameters(classeSelecionada, methodDeclaration));
							variaveis.addAll(getMethodParameters(classeSelecionada, methodDeclaration));
						}
					});
					// Method List Variables sorted

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
		if (args == null || args.length == 0) {
			System.out.println("Nome do Projeto não informado");
			return;
		}
		try {
			searchLists(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
