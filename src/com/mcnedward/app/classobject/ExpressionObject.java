package com.mcnedward.app.classobject;

import com.github.javaparser.ast.Node;

/**
 * @author Edward - Feb 28, 2016
 *
 */
public class ExpressionObject {
	
	private String name;
	private LineObject line;

	public ExpressionObject() {

	}
	
	public ExpressionObject(Node node) {
		name = node.toString();
		line = new LineObject(node.toString(), node.getBeginLine());
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the line
	 */
	public LineObject getLine() {
		return line;
	}

	/**
	 * @param line
	 *            the line to set
	 */
	public void setLine(LineObject line) {
		this.line = line;
	}

	@Override
	public String toString() {
		return name + " [" + line.getLineNumber() + "]";
	}

}
