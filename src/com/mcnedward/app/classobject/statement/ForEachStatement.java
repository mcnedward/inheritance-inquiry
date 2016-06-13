package com.mcnedward.app.classobject.statement;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 24, 2015
 *
 */
public class ForEachStatement extends BaseStatement {

	private String variableType;
	private String variableValue;
	
	public ForEachStatement() {
		super();
	}

	/**
	 * @return the variableType
	 */
	public String getVariableType() {
		return variableType;
	}

	/**
	 * @param variableType
	 *            the variableType to set
	 */
	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}

	/**
	 * @return the variableValue
	 */
	public String getVariableValue() {
		return variableValue;
	}

	/**
	 * @param variableValue
	 *            the variableValue to set
	 */
	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
	}

}
