package com.mcnedward.app.classobject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mcnedward.app.classobject.method.MethodObject;

/**
 * @author Edward - Feb 28, 2016
 *
 */
public class ClassObject extends BaseObject {

	private int id;
	private String accessModifier;
	private String nonAccessModifier;
	private List<String> extendsList;
	private List<String> interfaces;
	private List<MethodObject> methods;
	private List<VariableObject> variables;
	private List<ExpressionObject> expressions;
	private String classContent;
	private boolean isInterface;

	private File sourceFile;

	public ClassObject() {
		super("class");
		extendsList = new ArrayList<String>();
		interfaces = new ArrayList<String>();
		methods = new ArrayList<MethodObject>();
		variables = new ArrayList<VariableObject>();
	}

	public void addExtends(String extendsName) {
		extendsList.add(extendsName);
	}

	public void addInterface(String interfaceName) {
		interfaces.add(interfaceName);
	}

	public void addMethod(MethodObject method) {
		methods.add(method);
	}

	public void addVariable(VariableObject variable) {
		variables.add(variable);
	}

	public MethodObject getMethodByName(String methodName) {
		for (MethodObject method : methods) {
			if (method.getName().equals(methodName))
				return method;
		}
		return null;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the accessModifier
	 */
	public String getAccessModifier() {
		return accessModifier;
	}

	/**
	 * @param accessModifier
	 *            the accessModifier to set
	 */
	public void setAccessModifier(String accessModifier) {
		this.accessModifier = accessModifier;
	}

	/**
	 * @return the nonAccessModifier
	 */
	public String getNonAccessModifier() {
		return nonAccessModifier;
	}

	/**
	 * @param nonAccessModifier
	 *            the nonAccessModifier to set
	 */
	public void setNonAccessModifier(String nonAccessModifier) {
		this.nonAccessModifier = nonAccessModifier;
	}

	/**
	 * @return the extendsList
	 */
	public List<String> getExtends() {
		return extendsList;
	}

	/**
	 * @param extendsList
	 *            the extendsList to set
	 */
	public void setExtendsList(List<String> extendsList) {
		this.extendsList = extendsList;
	}

	/**
	 * @return the interfaces
	 */
	public List<String> getInterfaces() {
		return interfaces;
	}

	/**
	 * @param interfaces
	 *            the interfaces to set
	 */
	public void setInterfaces(List<String> interfaces) {
		this.interfaces = interfaces;
	}

	/**
	 * @return the methods
	 */
	public List<MethodObject> getMethods() {
		return methods;
	}

	/**
	 * @param methods
	 *            the methods to set
	 */
	public void setMethods(List<MethodObject> methods) {
		this.methods = methods;
	}

	/**
	 * @return the variables
	 */
	public List<VariableObject> getVariables() {
		return variables;
	}

	/**
	 * @param variables
	 *            the variables to set
	 */
	public void setVariables(List<VariableObject> variables) {
		this.variables = variables;
	}

	/**
	 * @return the expressions
	 */
	public List<ExpressionObject> getExpressions() {
		return expressions;
	}

	/**
	 * @param expressions
	 *            the expressions to set
	 */
	public void setExpressions(List<ExpressionObject> expressions) {
		this.expressions = expressions;
	}
	
	/**
	 * @return the classContent
	 */
	public String getClassContent() {
		return classContent;
	}

	/**
	 * @param classContent the classContent to set
	 */
	public void setClassContent(String classContent) {
		this.classContent = classContent;
	}
	
	/**
	 * @return the isInterface
	 */
	public boolean isInterface() {
		return isInterface;
	}

	/**
	 * @param isInterface the isInterface to set
	 */
	public void setIsInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	/**
	 * @return the sourceFile
	 */
	public File getSourceFile() {
		return sourceFile;
	}

	/**
	 * @param sourceFile
	 *            the sourceFile to set
	 */
	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < modifiers.size(); i++) {
			Modifier modifier = modifiers.get(i);
			builder.append(modifier.name + " ");
		}
		builder.append(name);
		if (!extendsList.isEmpty()) {
			builder.append(" extends ");
			for (int i = 0; i < extendsList.size(); i++) {
				builder.append(extendsList.get(i));
				if (i != extendsList.size() - 1) {
					builder.append(", ");
				}
			}
		}
		if (!interfaces.isEmpty()) {
			builder.append(" implements ");
			for (int i = 0; i < interfaces.size(); i++) {
				builder.append(interfaces.get(i));
				if (i != interfaces.size() - 1)
					builder.append(", ");
			}
		}
		return builder.toString().trim();
	}


}
