package codemining;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class CodeAnalyser {
	
	public static void main(String[] args) {
		CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
		combinedTypeSolver.add(new ReflectionTypeSolver());
		combinedTypeSolver.add(new JavaParserTypeSolver("/home/suporte/Workspace/BaiasOrdenacao/resources/sistemas/java_projects"));
		
		List<String> nome = Arrays.asList("User","employer","employee","client","customer");
		Boolean resultado;
		String separador = " ";
		for (String texto : nome) {
			resultado = combinedTypeSolver.hasType(texto);
			System.out.println(texto + separador + resultado.toString());
		}
		
		CompilationUnit compilationUnit = StaticJavaParser.parse("class UserVO { }");
		Optional<ClassOrInterfaceDeclaration> classA;
		for (String texto : nome) {
			classA = compilationUnit.getClassByName(texto);
			System.out.println(classA.toString());
		}
		compilationUnit.findAll(FieldDeclaration.class).stream()
			.filter(f -> f.isPublic() && !f.isStatic())
			.forEach(f -> 
				System.out.println("Check field at line " + f.getRange().map(r -> r.begin.line).orElse(-1)));
		return;
	}

}
