package com.mcnedward.app.classobject.statement;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 24, 2015
 *
 */
public class ForStatement extends BaseStatement {

	private String init;
	private String compare;
	private String update;

	public ForStatement() {
		super();
	}

	/**
	 * @return the init
	 */
	public String getInit() {
		return init;
	}

	/**
	 * @param init
	 *            the init to set
	 */
	public void setInit(String init) {
		this.init = init;
	}

	/**
	 * @return the compare
	 */
	public String getCompare() {
		return compare;
	}

	/**
	 * @param compare
	 *            the compare to set
	 */
	public void setCompare(String compare) {
		this.compare = compare;
	}

	/**
	 * @return the update
	 */
	public String getUpdate() {
		return update;
	}

	/**
	 * @param update
	 *            the update to set
	 */
	public void setUpdate(String update) {
		this.update = update;
	}

}
