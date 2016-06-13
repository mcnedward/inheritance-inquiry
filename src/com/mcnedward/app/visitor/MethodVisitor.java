package com.mcnedward.app.visitor;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.mcnedward.app.classobject.LineObject;
import com.mcnedward.app.classobject.method.MethodCallObject;
import com.mcnedward.app.classobject.method.MethodObject;
import com.mcnedward.app.classobject.method.MethodParameter;
import com.mcnedward.app.classobject.statement.BaseStatement;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 23, 2015
 *
 */
public class MethodVisitor extends BaseVisitor<MethodObject> {

	private List<MethodObject> methodObjects;

	public MethodVisitor() {
		super(new StatementVisitor());
		methodObjects = new ArrayList<MethodObject>();
	}

	@Override
	public void reset() {
		methodObjects = new ArrayList<MethodObject>();
		statementVisitor.reset();
	}

	@Override
	public void visit(MethodDeclaration n, MethodObject arg) {
		MethodObject methodObject = new MethodObject();
		methodObject.setName(n.getName());
		methodObject.setReturnType(n.getType().toString());
		methodObject.setModifiers(decodeModifiers(n.getModifiers()));
		for (Parameter p : n.getParameters()) {
			p.accept(this, methodObject);
		}
		Node parent = n.getParentNode();
		if (parent instanceof ClassOrInterfaceDeclaration) {
			if (!((ClassOrInterfaceDeclaration) parent).isInterface() && !methodObject.isAbstract()) {
				n.getBody().accept(this, methodObject);
			}
		}
		methodObjects.add(methodObject);
	}

	@Override
	public void visit(Parameter p, MethodObject arg) {
		MethodObject methodObject = (MethodObject) arg;
		MethodParameter methodParameter = new MethodParameter();
		methodParameter.setParameterName(p.getId().getName());
		methodParameter.setParameterType(p.getType().toString());
		methodObject.addParameter(methodParameter);
	}

	@Override
	public void visit(BlockStmt b, MethodObject arg) {
		List<Node> childrenNodes = b.getChildrenNodes();

		BaseStatement statement = new BaseStatement();
		List<LineObject> lineObjects = convertToLineObjects(b);
		statement.setLines(lineObjects);
		// For the method body, remove the first and last line, which are the curly brackets
		lineObjects.remove(0);
		lineObjects.remove(lineObjects.size() - 1);
		for (LineObject line : lineObjects) {
			arg.addMethodLine(line);
		}

		statement.setBeginLine(b.getBeginLine() + 1);
		statement.setEndLine(b.getEndLine() - 1);

		if (!childrenNodes.isEmpty()) {
			checkNodesForStatement(statementVisitor, childrenNodes, statement);
		}
		findChildrenMethodCallExpr(b.getChildrenNodes(), arg);
		
		arg.setStatement(statement);
	}

	private void findChildrenMethodCallExpr(List<Node> nodes, MethodObject arg) {
		for (Node n : nodes) {
			if (n instanceof MethodCallExpr) {
				n.accept(this, arg);
			}
			if (!n.getChildrenNodes().isEmpty()) {
				findChildrenMethodCallExpr(n.getChildrenNodes(), arg);
			}
		}
	}

	@Override
	public void visit(MethodCallExpr m, MethodObject arg) {
		List<String> arguments = new ArrayList<String>();
		if (m.getArgs() != null && !m.getArgs().isEmpty()) {
			for (Expression e : m.getArgs()) {
				arguments.add(e.toString());
			}
		}
		String scope = m.getScope() != null ? m.getScope().toString() : "";
		int lineNumber = m.getBeginLine();
		boolean isThisExpr = false;
		if (m.getScope() instanceof FieldAccessExpr) {
			isThisExpr = ((FieldAccessExpr) m.getScope()).getScope() instanceof ThisExpr;
		}

		String methodCallClass = findClassForMethodCall(arg, m);
		MethodCallObject methodCallObject = new MethodCallObject();
		methodCallObject.setName(m.getName());
		methodCallObject.setArguments(arguments);
		methodCallObject.setScope(scope);
		methodCallObject.setThisExpr(isThisExpr);
		methodCallObject.setLineNumber(lineNumber);
		methodCallObject.setParentClass(methodCallClass);

		arg.addMethodCallObject(methodCallObject);
	}

	/**
	 * Finds the class that calls a certain method.
	 * 
	 * @param methodObject
	 *            The method object to find the calling class for.
	 * @param m
	 *            The MethodCallExpr.
	 * @return The name of class that calls the method object.
	 */
	private String findClassForMethodCall(MethodObject methodObject, MethodCallExpr m) {
		String methodCallClass = "";
		String scope = getMethodCallExprScope(m);

		// Search method parameters first
		List<MethodParameter> methodParameters = methodObject.getMethodParameters();
		if (methodParameters != null && !methodParameters.isEmpty()) {
			for (MethodParameter parameter : methodObject.getMethodParameters()) {
				if (parameter.getParameterName().equals(scope)) {
					methodCallClass = parameter.getParameterType();
					return methodCallClass;
				}
			}
		}

		// Determine if this method is part of a for each statement
		ForeachStmt stmt = getForeachStmt(m);
		if (stmt != null) {
			String foreachVarClass = stmt.getVariable().getType().toString();
			if (foreachVarClass.equals(scope))
				return foreachVarClass;
		}

		// Check the variables for the parent class
		ClassOrInterfaceDeclaration classDeclaration = getClassDeclaration(m);
		List<BodyDeclaration> members = classDeclaration.getMembers();
		if (members != null && !members.isEmpty()) {
			for (BodyDeclaration member : members) {
				if (member instanceof FieldDeclaration) {
					List<VariableDeclarator> variables = ((FieldDeclaration) member).getVariables();
					for (VariableDeclarator v : variables) {
						if (v.getId().toString().equals(scope)) {
							methodCallClass = ((FieldDeclaration) member).getType().toString();
							return methodCallClass;
						}
					}
				}
			}
		}

		// If scope is empty at this point, then the method is part of the current class
		if (!"".equals(scope)) {
			CompilationUnit parent = (CompilationUnit) classDeclaration.getParentNode();
			if (parent.getImports() != null && !parent.getImports().isEmpty()) {
				for (ImportDeclaration importDeclaration : parent.getImports()) {
					if (importDeclaration.getName().toString().contains(scope)) {
						methodCallClass = importDeclaration.getName().getName();
						return methodCallClass;
					}
				}
			}
		}

		return methodCallClass;
	}

	/**
	 * Find the scope of the method call. This is the variable or class that calls the method.
	 * 
	 * @param m
	 *            The MethodCallExpr
	 * @return The name of the variable or class that calls the method.
	 */
	private String getMethodCallExprScope(MethodCallExpr m) {
		String scope = "";
		if (m.getScope() instanceof FieldAccessExpr) {
			scope = ((FieldAccessExpr) m.getScope()).getField();
		}
		if (m.getScope() instanceof NameExpr) {
			scope = ((NameExpr) m.getScope()).getName();
		}
		if (m.getScope() instanceof MethodCallExpr) {
			return getMethodCallExprScope((MethodCallExpr) m.getScope());
		}
		return scope;
	}

	/**
	 * Find if a node is part of a for each statement.
	 * 
	 * @param n
	 *            The node to find the parent of.
	 * @return A ForeachStmt if it is part of a for each, or null if not.
	 */
	private ForeachStmt getForeachStmt(Node n) {
		if (n.getParentNode() instanceof ForeachStmt) {
			return (ForeachStmt) n.getParentNode();
		} else if (n.getParentNode() instanceof MethodDeclaration)
			return null;
		else
			return getForeachStmt(n.getParentNode());
	}

	/**
	 * Find the parent class node for a node.
	 * 
	 * @param n
	 *            The node to search through.
	 * @return The ClassOrInterfaceDeclaration of the node.
	 */
	private ClassOrInterfaceDeclaration getClassDeclaration(Node n) {
		if (n.getParentNode() instanceof ClassOrInterfaceDeclaration) {
			return (ClassOrInterfaceDeclaration) n.getParentNode();
		} else
			return getClassDeclaration(n.getParentNode());
	}

	/**
	 * @return the methodObjects
	 */
	public List<MethodObject> getMethodObjects() {
		return methodObjects;
	}

}
