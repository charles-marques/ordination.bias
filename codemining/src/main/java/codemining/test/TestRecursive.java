package codemining.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class TestRecursive {

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

			String[] caminho = classPath.replace(".java", "").split(File.separator);
			System.out.println("================================================");
			CompilationUnit cu = StaticJavaParser.parse(code);
			System.out.println("ClassName: " + caminho[caminho.length - 1]);
			System.out.println("Package: " + cu.getPackageDeclaration().get().getNameAsString());
			System.out.println("Path: " + classPath);
			Optional<ImportDeclaration> imports = cu.getImports().stream().findFirst();
			System.out.println("PrimaryTypeName: " + imports.get());

//			System.out.println("================================================");
//			System.out.println(cu);
//			System.out.println("================================================");
//			YamlPrinter yamlPrinter = new YamlPrinter(true);
//			System.out.println(yamlPrinter.output(cu));
//			System.out.println("================================================");

			extracted(cu);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void extracted(CompilationUnit cu) {
		cu.accept(new VoidVisitorAdapter<List<String>>() {
			public void visit(FieldDeclaration fd, List<String> f) {
				System.out.println(typeOf(fd.getElementType()));
			}
		}, null);
	}

	protected static String typeOf(Type type) {
		if (type instanceof PrimitiveType) {
			String typeName = ((PrimitiveType) type).getType().name();
			return typeName; // primitiveType( typeName );
		} else if (type instanceof ClassOrInterfaceType) {
			try {
				return ((ClassOrInterfaceType) type).getName().asString();
			} catch (Exception e) {
				// class not found, ignore
			}
		}
		return null;
	}
}
