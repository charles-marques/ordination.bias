package discoverypattern;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Stream;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;

public class TestProject {
	public static void main(String[] args) {
		String classPath = "/home/suporte/Workspace/BaiasOrdenacao/code-example/src/main/java/code/example/source/Client.java";
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

			CompilationUnit cu = StaticJavaParser.parse(code);

			String packageDeclaration = cu.getPackageDeclaration().get().getNameAsString();
			String[] caminho = classPath.replace(".java", "").split(File.separator);
			String className = caminho[caminho.length - 1];

			System.out.println("ClassName: " + className);
			System.out.println("Package: " + packageDeclaration);
			System.out.println("Path: " + classPath);

			// XXX verificar importação java.util.List
			System.out.println("================================================");

			Stream<ImportDeclaration> imports = cu.getImports().stream();

			if (imports.anyMatch(i -> i.getNameAsString().equals("java.util.List"))) {
				System.out.println("Importa java.util.List");
			}

			// XXX verificar fields
			System.out.println("================================================");
			List<FieldDeclaration> fields = cu.getClassByName(className).get().getFields();

//			fields.forEach(f -> {
//				f.getVariables().forEach(v -> {
//					v.getChildNodes().forEach(c -> {
//						if (c instanceof ClassOrInterfaceType && ((ClassOrInterfaceType) c).getTypeArguments().isPresent()) {
//							System.out.println("c:");
//							System.out.println(c);
//							System.out.println("ta:");
//							System.out.println( ((ClassOrInterfaceType) c).getTypeArguments() );
//						}
//					});
//				});
//			});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
