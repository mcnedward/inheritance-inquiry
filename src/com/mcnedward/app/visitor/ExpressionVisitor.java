package com.mcnedward.app.visitor;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.mcnedward.app.classobject.ClassObject;
import com.mcnedward.app.classobject.ExpressionObject;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Nov 1, 2015
 *
 */
public class ExpressionVisitor extends BaseVisitor<ClassObject> {

	private List<ExpressionObject> expressions;

	public ExpressionVisitor() {
		expressions = new ArrayList<ExpressionObject>();
	}

	@Override
	public void reset() {
		expressions.clear();
	}

	@Override
	public void visit(NameExpr e, ClassObject arg) {
		handle(e);
	}
	@Override
	public void visit(FieldAccessExpr e, ClassObject arg) {
		handle(e);
	}
	@Override
	public void visit(AssignExpr e, ClassObject arg) {
		handle(e);
	}
	@Override
	public void visit(BooleanLiteralExpr e, ClassObject arg) {
		handle(e);
	}
	
	private void handle(Node node) {
		if (node.getParentNode() instanceof AnnotationExpr == false) {
			if (expressions.size() == 0)
				expressions.add(new ExpressionObject(node));
			else {
				boolean add = false;
				for (ExpressionObject expression : expressions) {
					if (expression.getLine().getLineNumber() != node.getBeginLine()) {
						add = true;
						break;
					}
				}
				if (add)
					expressions.add(new ExpressionObject(node));
			}
		}
	}

	/**
	 * 
	 */
	public List<ExpressionObject> getNameExpressions() {
		return expressions;
	}

}
