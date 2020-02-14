package repository.projeto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.github.javaparser.ast.stmt.Statement;

import codemining.Classe;

public class Test {
	protected static final Character QUEBRA_LINHA = '\n';
	protected static final String JAVA = ".java";
	protected static final CharSequence VAZIO = "";
	protected static List<Classe> selectedClassesList = new ArrayList<Classe>();
	protected static List<String> sortingTerms = Arrays.asList("sort(", "compareTo(", "compare(", "order(", "orderBy",
			"sortBy", "order by");
	protected static List<String[]> resultado = new ArrayList<String[]>();
	protected static void addResultado(String projeto, String classe, String bloco, String statment) throws Exception {
		String[] objeto = new String[] { projeto, classe, statment };
		resultado.add(objeto);
	}
	public static void main(String[] args) {
		String classPath = "/home/suporte/Workspace/BaiasOrdenacao/code/java/codemining/src/main/java/repository/projeto/Business.java";
		selectedClassesList.add(new Classe("projeto", "User", null));
		selectedClassesList.add(new Classe("projeto", "Client", null));
		try {
			File node = new File(classPath);
			String code = new String();
			BufferedReader br = new BufferedReader(new FileReader(node));
			String st;
			while ((st = br.readLine()) != null) {
				code = code + st + QUEBRA_LINHA;
			}
			br.close();
			CompilationUnit compilationUnit = StaticJavaParser.parse(code);
			Optional<ClassOrInterfaceDeclaration> classe = compilationUnit
					.getClassByName(node.getName().replace(JAVA, VAZIO));
			if (classe.isPresent()) {
				List<String> variaveis = new ArrayList<>();
				// BUSCANDO FIELDS DOS TIPOS LIST<CLASSE PERSON, CLIENT, USER...>
				List<FieldDeclaration> atributos = classe.get().getFields();
				List<MethodDeclaration> metodos = classe.get().getMethods();
				List<FieldDeclaration> atributosSelecionados;
				List<Parameter> parametrosSelecionados;
				List variaveisSelecionadas;
				for (Classe classeSelecionada : selectedClassesList) {
					// Classes List fields - OK
					atributosSelecionados = atributos.stream()
							.filter(f -> f.getCommonType().toString().contains(List.class.getSimpleName())
									&& f.getCommonType().toString().contains(classeSelecionada.getNome()))
							.collect(Collectors.toList());

					// Methods Lists Params and Variables
					for (MethodDeclaration methodDeclaration : metodos) {
						// List Parameters
						parametrosSelecionados = methodDeclaration.getParameters().stream()
								.filter(f -> f.getTypeAsString().contains(List.class.getSimpleName())
										&& f.getTypeAsString().contains(classeSelecionada.getNome()))
								.collect(Collectors.toList());
						// List Variables
//						visit(methodDeclaration.getNameAsExpression(), null);
						BlockStmt blockStmt = methodDeclaration.getBody().get();
						NodeList<Statement> statements = blockStmt.getStatements();
						for (Statement statement : statements) {
							System.out.println("statement");
							System.out.println(statement);
						}
					}
				}

				// INSPECIONAR PARAMETROS DECLADAS NO ESCOPO DO MÉTODO

				for (MethodDeclaration metodo : metodos) {
					NodeList<Parameter> parametrosMetodo = metodo.getParameters();
					// XXX verificar existência de parâmetros do tipos classe person, client...
					parametrosSelecionados = parametrosMetodo.stream()
							.filter(f -> f.getTypeAsString().contains(List.class.getSimpleName()) && selectedClassesList
									.stream().anyMatch(c -> f.getTypeAsString().contains(c.toString())))
							.collect(Collectors.toList());
					// XXX verificar se há ordenação em algum dos parametros do tipo pesquisado
//					if (!parametrosSelecionados.isEmpty()) {
//						for (Parameter parametro : parametrosSelecionados) {
//							variaveis.add(parametro.getNameAsString());
//						}
//					}
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
						for (Node statment : blockNodes) {
							addResultado("projeto", node.getName(), blockStmt.toString(), statment.toString());
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
}
