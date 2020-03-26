package codemining.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class TestRecursive {

	public static void main(String[] args) {
		String classPath = "/home/suporte/Workspace/BaiasOrdenacao/codemining/src/main/java/repository/projeto/Client.java";
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
