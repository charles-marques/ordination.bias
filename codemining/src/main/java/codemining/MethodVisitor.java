package codemining;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

class MethodVisitor extends VoidVisitorAdapter<Void> {

	@Override
	public void visit(BlockStmt n, Void arg) {
		System.out.println("MethodVisitor.visit()");
		System.out.println(n.isBlockStmt());
		super.visit(n, arg);
	}

	@Override
	public void visit(VariableDeclarator n, Void arg) {
		System.out.println("MethodVisitor.visit()");
		System.out.println(n.getNameAsString());
		super.visit(n, arg);
	}

	@Override
	public void visit(VarType n, Void arg) {
		System.out.println("MethodVisitor.visit()");
		System.out.println(n.asString());
		super.visit(n, arg);
	}

	@Override
	public void visit(VariableDeclarationExpr n, Void arg) {
		System.out.println(n.asStringLiteralExpr());
		super.visit(n, arg);
	}

}