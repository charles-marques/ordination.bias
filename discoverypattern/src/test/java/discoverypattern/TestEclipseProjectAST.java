package discoverypattern;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class TestEclipseProjectAST {
	public static void parse(String str, String name) {
		ASTParser parser = ASTParser.newParser(AST.JLS13);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {

			Set names = new HashSet();

//			@Override
//			public boolean visit(CompilationUnit node) {
//				System.out.println("CompilationUnit:");
//				TypeDeclaration ultimo = (TypeDeclaration) node.types().get(0);
//				System.out.println(ultimo.getName());
//				return false;
//			}

			@Override
			public boolean visit(TypeDeclaration node) {
				System.out.println("Node:");
				System.out.println(node.getName());
				return false;
			}

			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				this.names.add(name.getIdentifier());
				System.out.println("Declaration of '" + name + "' at line" + cu.getLineNumber(name.getStartPosition()));
				return false; // do not continue
			}

			public boolean visit(SimpleName node) {
				if (this.names.contains(node.getIdentifier())) {
					System.out.println("Usage of '" + node + "' at line " + cu.getLineNumber(node.getStartPosition()));
				}
				return true;
			}
		});
	}

	// read file content into a string
	public static String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			System.out.println(numRead);
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}

		reader.close();

		return fileData.toString();
	}

	public static void main(String[] args) {
		try {
//			String projectPath = "/home/suporte/Workspace/BaiasOrdenacao/code-example/src/main/java/code/example/source/Client.java";
			String projectPath = "/home/suporte/Workspace/BaiasOrdenacao/code-example";
			File dirs = new File(projectPath);
			String dirPath = dirs.getCanonicalPath() + File.separator + "src" + File.separator;

			File root = new File(dirPath);
			// System.out.println(rootDir.listFiles());
			loadFile(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadFile(File root) throws IOException {
		File[] files = root.listFiles();
		String filePath = null;

		for (File file : files) {
			filePath = file.getAbsolutePath();
			if (file.isFile()) {
				System.out.println("FileName:");
				System.out.println(file.getName());
				parse(readFileToString(filePath), file.getName().replace(".java", ""));
			} else {
				loadFile(file);
			}
		}
	}
}
