package com.mcnedward.app.classobject.method;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.mcnedward.app.classobject.BaseObject;
import com.mcnedward.app.classobject.LineObject;
import com.mcnedward.app.classobject.Modifier;
import com.mcnedward.app.classobject.statement.BaseStatement;

/**
 * @author Edward - Feb 28, 2016
 *
 */
public class MethodObject extends BaseObject {

	private String returnType;
	private List<LineObject> methodLines;
	private List<MethodParameter> methodParameters;

	// Lines and Statements
	private BaseStatement statement;

	private List<MethodCallObject> methodCallObjects;

	public MethodObject() {
		super("Method");
		methodLines = new ArrayList<LineObject>();
		methodParameters = new ArrayList<MethodParameter>();
		methodCallObjects = new ArrayList<MethodCallObject>();
	}

	public void addParameter(MethodParameter methodParameter) {
		methodParameters.add(methodParameter);
	}

	public void addMethodCallObject(MethodCallObject methodCallObject) {
		methodCallObjects.add(methodCallObject);
	}

	public BaseStatement getStatementAtLineNumber(int lineNumber) {
		return statement.getStatementAtLineNumber(statement, lineNumber);
	}

	public List<BaseStatement> getStatements() {
		return statement.getStatements();
	}

	/**
	 * Reset this method so all nodes can be recreated.
	 */
	public void reset() {
		// Probably could be a much better way to do this part...
		for (LineObject line : methodLines) {
			setNodeCreated(line.getLineNumber(), false);
		}
	}

	public void update() {
		if (!isAbstract)
			methodLines = statement.update();
		updateNodeNumbers(methodLines);
	}

	/**
	 * Determines if this is the top level of an if statement.
	 * 
	 * Finds the line at the beginning of this statement, then checks to determine if the line contains else. If it
	 * does, then this statement is an else-if.
	 * 
	 * @param statement
	 *            The statement to check.
	 * @return True if it is the top level if statement, false otherwise.
	 */
	public boolean isParentElse(BaseStatement statement) {
		for (LineObject line : methodLines) {
			if (line.getLineNumber() == statement.getBeginLine()) {
				return Pattern.compile("\\s*\\}?\\s*\\belse\\b\\s*\\{?").matcher(line.getLine()).find();
			}
		}
		return false;
	}

	private void updateNodeNumbers(List<LineObject> lines) {
		int nodeNumber = 1;
		for (LineObject line : lines) {
			line.setNodeNumber(nodeNumber++);
		}
	}

	public boolean isNodeCreatedAtLine(int lineNumber) {
		for (LineObject line : methodLines) {
			if (line.getLineNumber() == lineNumber)
				if (line.isNodeCreated()) {
					return true;
				}
		}
		return checkStatementsForNode(statement.getStatements(), lineNumber);
	}

	private boolean checkStatementsForNode(List<BaseStatement> statements, int lineNumber) {
		if (!statements.isEmpty()) {
			for (BaseStatement statement : statements) {
				for (LineObject line : statement.getLines()) {
					if (line.getLineNumber() == lineNumber) {
						if (line.isNodeCreated()) {
							return true;
						}
					}
				}
				return checkStatementsForNode(statement.getStatements(), lineNumber);
			}
		}
		return false;
	}

	public void setNodeCreated(int lineNumber, boolean isCreated) {
		for (LineObject line : methodLines) {
			if (line.getLineNumber() == lineNumber) {
				line.setNodeCreated(isCreated);
			}
		}
		setStatementNodesCreated(statement.getStatements(), lineNumber, isCreated);
	}

	private void setStatementNodesCreated(List<BaseStatement> statements, int lineNumber, boolean isCreated) {
		if (!statements.isEmpty()) {
			for (BaseStatement statement : statements) {
				for (LineObject line : statement.getLines()) {
					if (line.getLineNumber() == lineNumber) {
						line.setNodeCreated(isCreated);
					}
				}
				setStatementNodesCreated(statement.getStatements(), lineNumber, isCreated);
			}
		}
	}

	public void addMethodLine(LineObject line) {
		methodLines.add(line);
	}

	/**
	 * @return the returnType
	 */
	public String getReturnType() {
		return returnType;
	}

	/**
	 * @param returnType
	 *            the returnType to set
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	/**
	 * @return the methodLines
	 */
	public List<LineObject> getMethodLines() {
		return methodLines;
	}

	/**
	 * @return the methodParameters
	 */
	public List<MethodParameter> getMethodParameters() {
		return methodParameters;
	}

	/**
	 * @param methodParameters
	 *            the methodParameters to set
	 */
	public void setMethodParameters(List<MethodParameter> methodParameters) {
		this.methodParameters = methodParameters;
	}

	/**
	 * @return the statement
	 */
	public BaseStatement getStatement() {
		return statement;
	}

	/**
	 * @param statement
	 *            the statement to set
	 */
	public void setStatement(BaseStatement statement) {
		this.statement = statement;
	}

	/**
	 * @return the methodCallObjects
	 */
	public List<MethodCallObject> getMethodCallObjects() {
		return methodCallObjects;
	}

	/**
	 * @param methodCallObjects
	 *            the methodCallObjects to set
	 */
	public void setMethodCallObjects(List<MethodCallObject> methodCallObjects) {
		this.methodCallObjects = methodCallObjects;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < modifiers.size(); i++) {
			Modifier modifier = modifiers.get(i);
			builder.append(modifier.name + " ");
		}
		builder.append(returnType + " " + name + "(");
		for (int i = 0; i < methodParameters.size(); i++) {
			MethodParameter param = methodParameters.get(0);
			builder.append(param.toString());
			if (i != methodParameters.size() - 1) {
				builder.append(", ");
			}
		}
		builder.append(")");
		return builder.toString();
	}

}
