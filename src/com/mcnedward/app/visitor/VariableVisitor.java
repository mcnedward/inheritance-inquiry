package com.mcnedward.app.visitor;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.mcnedward.app.classobject.VariableObject;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 22, 2015
 *
 */
public class VariableVisitor extends BaseVisitor<VariableObject> {

	private List<VariableObject> variableObjects;
	
	public VariableVisitor() {
		super();
		variableObjects = new ArrayList<VariableObject>();
	}
	
	@Override
	public void reset() {
		variableObjects = new ArrayList<VariableObject>();
	}

	@Override
	public void visit(FieldDeclaration n, VariableObject arg) {
		VariableObject variableObject = new VariableObject();
		variableObject.setType(n.getType().toString());
		variableObject.setModifiers(decodeModifiers(n.getModifiers()));
		variableObject.setLineNumber(n.getEndLine());
		for (VariableDeclarator var : n.getVariables()) {
			var.accept(this, variableObject);
		}
	}

	@Override
	public void visit(VariableDeclaratorId n, VariableObject arg) {
		arg.setName(n.getName());
		variableObjects.add(arg);
	}

	/**
	 * @return the variableObjects
	 */
	public List<VariableObject> getVariableObjects() {
		return variableObjects;
	}
	
}
