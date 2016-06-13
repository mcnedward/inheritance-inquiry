package com.mcnedward.app.classobject.statement;

import com.mcnedward.app.classobject.LineObject;

/**
 * @author Edward - Feb 28, 2016
 *
 */
public class IfStatement extends BaseStatement {

	private LineObject condition;
	private int thenBeginLine;
	private int thenEndLine;
	private int elseBeginLine;
	private int elseEndLine;

	public IfStatement() {
		super();
	}

	/**
	 * @return the condition
	 */
	public LineObject getCondition() {
		return condition;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(LineObject condition) {
		this.condition = condition;
	}
	
	/**
	 * @return the thenBeginLine
	 */
	public int getThenBeginLine() {
		return thenBeginLine;
	}

	/**
	 * @param thenBeginLine the thenBeginLine to set
	 */
	public void setThenBeginLine(int thenBeginLine) {
		this.thenBeginLine = thenBeginLine;
	}

	/**
	 * @return the thenEndLine
	 */
	public int getThenEndLine() {
		return thenEndLine;
	}

	/**
	 * @param thenEndLine the thenEndLine to set
	 */
	public void setThenEndLine(int thenEndLine) {
		this.thenEndLine = thenEndLine;
	}

	/**
	 * @return the elseBeginLine
	 */
	public int getElseBeginLine() {
		return elseBeginLine;
	}

	/**
	 * @param elseBeginLine the elseBeginLine to set
	 */
	public void setElseBeginLine(int elseBeginLine) {
		this.elseBeginLine = elseBeginLine;
	}

	/**
	 * @return the elseEndLine
	 */
	public int getElseEndLine() {
		return elseEndLine;
	}

	/**
	 * @param elseEndLine the elseEndLine to set
	 */
	public void setElseEndLine(int elseEndLine) {
		this.elseEndLine = elseEndLine;
	}

	@Override
	public String toString() {
		return "IfStatement[" +beginLine + "-" +endLine + "] " + condition;
	}

}
