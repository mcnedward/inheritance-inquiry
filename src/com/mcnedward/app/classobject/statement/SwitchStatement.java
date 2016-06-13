package com.mcnedward.app.classobject.statement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 24, 2015
 *
 */
public class SwitchStatement extends BaseStatement {
	
	private String selector;
	private List<SwitchEntry> switchEntries;
	
	public SwitchStatement() {
		switchEntries = new ArrayList<SwitchEntry>();
	}
	
	public void addSwitchEntry(String label, int lineNumber, boolean isDefault) {
		switchEntries.add(new SwitchEntry(label, lineNumber, isDefault));
	}

	/**
	 * @return the selector
	 */
	public String getSelector() {
		return selector;
	}

	/**
	 * @param selector the selector to set
	 */
	public void setSelector(String selector) {
		this.selector = selector;
	}

	/**
	 * @return the switchEntries
	 */
	public List<SwitchEntry> getSwitchEntries() {
		return switchEntries;
	}

	/**
	 * @param switchEntries the switchEntries to set
	 */
	public void setSwitchEntries(List<SwitchEntry> switchEntries) {
		this.switchEntries = switchEntries;
	}

}

class SwitchEntry {
	protected String label;
	protected int lineNumber;
	protected boolean isDefault;
	public SwitchEntry(String label, int lineNumber, boolean isDefault) {
		this.label = label;
		this.lineNumber = lineNumber;
		this.isDefault = isDefault;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}
	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}
	/**
	 * @param isDefault the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	@Override
	public String toString() {
		return "Label: " + label + " - Is Default? " + isDefault + " [" + lineNumber + "]";
	}
}