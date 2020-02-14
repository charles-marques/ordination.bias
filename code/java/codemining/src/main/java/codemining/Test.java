package codemining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class Test {
	public static void main(String[] args) {
		// List<String> classesToFindList = Arrays.asList("person", "employer",
		// "employee", "customer", "client", "user");
		String classPath = "/home/suporte/Documentos/Teste/head/appdomain/src/main/java/org/mifos/application/servicefacade/SaveCollectionSheetStructureValidator.java";
		char quebraLinha = '\n';
		try {
			File node = new File(classPath);
			String code = new String();
			BufferedReader br = new BufferedReader(new FileReader(node));
			String st;
			while ((st = br.readLine()) != null) {
				code = code + st + quebraLinha;
			}
			br.close();
			CompilationUnit compilationUnit = StaticJavaParser.parse(code);
			Optional<ClassOrInterfaceDeclaration> classe = compilationUnit
					.getClassByName(node.getName().replace(".java", ""));
			if (classe.isPresent()) {
				ClassOrInterfaceDeclaration objeto = classe.get();
				List<FieldDeclaration> fields = objeto.getFields();
				List<MethodDeclaration> methods = objeto.getMethods();
				System.out.println(fields.size());
				System.out.println(methods.size());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
