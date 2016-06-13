package com.mcnedward.app.classobject.statement;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 24, 2015
 *
 */
public class DoStatement extends BaseStatement {

	private String condition;

	public DoStatement() {
	}
	
	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * @param condition the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	
	
}
