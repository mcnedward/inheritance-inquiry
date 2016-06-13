package com.mcnedward.app.classobject;

/**
 * @author Edward - Feb 28, 2016
 *
 */
public class VariableObject extends BaseObject {
	
	private String type;
	private int lineNumber;
	
	public VariableObject() {
		super("Variable");
	}
	
	public VariableObject(String variableName) {
		this();
		name = variableName;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < modifiers.size(); i++) {
			Modifier modifier = modifiers.get(i);
			builder.append(modifier.name + " ");
		}
		return builder.toString() + type + " " + name;
	}
}
