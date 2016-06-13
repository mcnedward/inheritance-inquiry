package com.mcnedward.app.classobject.statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mcnedward.app.classobject.LineObject;
import com.mcnedward.app.classobject.LineType;
import com.mcnedward.app.comparator.LineObjectComparator;
import com.mcnedward.app.comparator.StatementComparator;

/**
 * @author Edward - Feb 28, 2016
 *
 */
public class BaseStatement implements IStatement {

	protected List<LineObject> lines;
	protected List<LineObject> cleanLines;
	protected List<BaseStatement> statements;
	protected int beginLine;
	protected int endLine;

	public BaseStatement() {
		lines = new ArrayList<LineObject>();
		cleanLines = new ArrayList<LineObject>();
		statements = new ArrayList<BaseStatement>();
	}

	public void addLine(String line, int lineNumber) {
		lines.add(new LineObject(line, lineNumber));
	}

	public Object getObjectAtCurrentLine(int currentLine) {
		for (LineObject line : cleanLines) {
			if (line.getLineNumber() == currentLine)
				return line;
		}
		for (BaseStatement statement : statements) {
			if (statement.getBeginLine() == currentLine)
				return statement;
		}
		return null;
	}

	public List<LineObject> update() {
		updateLineTypes(this);
		updateLines(statements);
		updateNodeNumbers(lines);
		updateStatementNodeNumbers(statements);
		orderStatements();
		Collections.sort(lines, new LineObjectComparator());
		int currentLine, previousLine = 0;
		int y = lines.size();
		for (int x = 0; x < y; x++) {
			LineObject line = lines.get(x);
			currentLine = line.getLineNumber();
			if (x == 0) {
				previousLine = currentLine;
				continue;
			}
			if (currentLine != previousLine + 1) {
				while (previousLine != currentLine - 1) {
					lines.add(new LineObject("", ++previousLine));
				}
			}
			previousLine = currentLine;
		}
		Collections.sort(lines, new LineObjectComparator());
		// TODO Don't know why I need this...
//		cleanLines = new ArrayList<LineObject>(lines);
//		cleanLines(statements);
		return lines;
	}

	private void updateLineTypes(BaseStatement bs) {
		for (LineObject line : bs.getLines()) {
			BaseStatement statement = getStatementAtLineNumber(bs, line.getLineNumber());
			if (statement != null) {
				line.setLineType(statement);
			} else if (!bs.getStatements().isEmpty()) {
				for (BaseStatement s : bs.getStatements())
					updateLineTypes(s);
			}
		}
	}

	private void updateStatementNodeNumbers(List<BaseStatement> statements) {
		if (!statements.isEmpty()) {
			for (BaseStatement statement : statements) {
				for (LineObject statementLine : statement.getLines()) {
					for (LineObject baseLine : lines) {
						if (statementLine.getLineNumber() == baseLine.getLineNumber()) {
							statementLine.setNodeNumber(baseLine.getNodeNumber());
						}
					}
				}
				updateStatementNodeNumbers(statement.getStatements());
			}
		}
	}

	private void updateLines(List<BaseStatement> statements) {
		Set<LineObject> lineObjectSet = new HashSet<LineObject>(lines);
		updateLineObjectSetForStatement(this, lineObjectSet);

		// Find all the lines that have a LineType
		List<LineObject> linesWithType = new ArrayList<LineObject>();
		for (LineObject line : lineObjectSet) {
			if (line.getLineType() != null) {
				linesWithType.add(line);
			}
		}
		Iterator<LineObject> iterator = lines.iterator();
		while (iterator.hasNext()) {
			LineObject originalLine = iterator.next();
			for (LineObject lineWithType : linesWithType) {
				if (lineWithType.getLineNumber() == originalLine.getLineNumber()) {
					lineWithType.setNodeNumber(originalLine.getNodeNumber());
					iterator.remove();
				}
			}
		}
		// Convert back
		lines.addAll(linesWithType);
		Collections.sort(lines, new LineObjectComparator());
	}

	private void addLinesToSetFromStatement(List<BaseStatement> statements, Set<LineObject> lineObjectSet) {
		if (!statements.isEmpty()) {
			for (BaseStatement statement : statements) {
				updateLineObjectSetForStatement(statement, lineObjectSet);
			}
		}
	}

	private void updateLineObjectSetForStatement(BaseStatement statement, Set<LineObject> lineObjectSet) {
		for (LineObject line : statement.getLines()) {
			boolean exists = false;
			LineObject[] array = lineObjectSet.toArray(new LineObject[lineObjectSet.size()]);
			for (int x = 0; x < array.length; x++) {
				if (array[x].getLineNumber() == line.getLineNumber()) {
					// Remove nested Else-If statements
					boolean o1HasElseIf = (line.getLineType() != null && line.getLineType().equals(LineType.IF))
							&& (array[x].getLineType() != null && array[x].getLineType().equals(LineType.ELSE_IF));
					if (o1HasElseIf) {
						lineObjectSet.remove(array[x]);
						lineObjectSet.add(line);
						exists = true;
					} else if (line.getLineType() != null && line.getLineType().equals(LineType.ELSE)) {
						lineObjectSet.remove(array[x]);
						lineObjectSet.add(line);
						exists = true;
					} else {
						exists = true;
					}
				}
			}
			if (!exists)
				lineObjectSet.add(line);
		}
		addLinesToSetFromStatement(statement.getStatements(), lineObjectSet);
	}

	private void orderStatements() {
		StatementComparator statementComparator = new StatementComparator();
		sortStatementAndChildren(statements, statementComparator);
	}

	/**
	 * Sort a collection of statements and all of their children by starting
	 * line.
	 * 
	 * @param statements
	 *            The collection of statements to sort.
	 * @param statementComparator
	 *            The comparator to sort with.
	 */
	private void sortStatementAndChildren(List<BaseStatement> statements, StatementComparator statementComparator) {
		if (!statements.isEmpty()) {
			Collections.sort(statements, statementComparator);
			for (BaseStatement statement : statements) {
				sortStatementAndChildren(statement.getStatements(), statementComparator);
			}
		}
	}

//	private void cleanLines(List<BaseStatement> statements) {
//		try {
//		if (!statements.isEmpty()) {
//			for (BaseStatement statement : statements) {
//				statement.setCleanLines(new ArrayList<LineObject>(statement.getLines()));
//				for (LineObject line : statement.getLines()) {
//					BaseStatement s = statement.getStatementAtLineNumber(statement, line.getLineNumber());
//					if (s != null) {
//						int beginLine = s.getBeginLine();
//						int endLine = s.getEndLine();
//						LineObject l = getCleanLineObjectByLineNumber(statement, beginLine);
//						if (l != null) {
//							int index = statement.getCleanLines().indexOf(l);
//							for (int x = 0; x <= endLine - beginLine; x++) {
//								statement.getCleanLines().remove(index);
//							}
//						}
//						cleanLines(statement.getStatements());
//					}
//				}
//			}
//		} }catch(Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}

//	private LineObject getCleanLineObjectByLineNumber(BaseStatement statement, int lineNumber) {
//		for (LineObject line : statement.getCleanLines()) {
//			if (line.getLineNumber() == lineNumber)
//				return line;
//		}
//		return null;
//	}

	public LineObject getLineObjectByLineNumber(int lineNumber) {
		for (LineObject line : lines) {
			if (line.getLineNumber() == lineNumber)
				return line;
		}
		return null;
	}

	public BaseStatement getStatementAtLineNumber(BaseStatement statement, int lineNumber) {
		for (BaseStatement s : statement.getStatements()) {
			if (s.getLines().get(0).getLineNumber() == lineNumber) {
				return s;
			} else if (!s.getStatements().isEmpty()) {
				// If that statement has children, check them too
				BaseStatement childStatement = getStatementAtLineNumber(s, lineNumber);
				if (childStatement != null)
					return childStatement;
			}
		}
		return null;
	}

	private void updateNodeNumbers(List<LineObject> lines) {
		int nodeNumber = 1;
		for (LineObject line : lines) {
			line.setNodeNumber(nodeNumber++);
		}
	}

	public void addStatement(BaseStatement statement) {
		statements.add(statement);
	}

	/**
	 * @return the lines
	 */
	@Override
	public List<LineObject> getLines() {
		return lines;
	}

	/**
	 * @param lines
	 *            the lines to set
	 */
	public void setLines(List<LineObject> lines) {
		this.lines = lines;
	}

	/**
	 * @return the cleanLines
	 */
	public List<LineObject> getCleanLines() {
		return cleanLines;
	}

	/**
	 * @param cleanLines
	 *            the cleanLines to set
	 */
	public void setCleanLines(List<LineObject> cleanLines) {
		this.cleanLines = cleanLines;
	}

	/**
	 * @return the statements
	 */
	public List<BaseStatement> getStatements() {
		return statements;
	}

	/**
	 * @param statements
	 *            the statements to set
	 */
	public void setStatements(List<BaseStatement> statements) {
		this.statements = statements;
	}

	/**
	 * @return the beginLine
	 */
	public int getBeginLine() {
		return beginLine;
	}

	/**
	 * @param beginLine
	 *            the beginLine to set
	 */
	public void setBeginLine(int beginLine) {
		this.beginLine = beginLine;
	}

	/**
	 * @return the endLine
	 */
	public int getEndLine() {
		return endLine;
	}

	/**
	 * @param endLine
	 *            the endLine to set
	 */
	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	@Override
	public String toString() {
		return "BaseStatement - Number of Lines[" + lines.size() + "]; Number of Statements[" + statements.size() + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + beginLine;
		result = prime * result + ((cleanLines == null) ? 0 : cleanLines.hashCode());
		result = prime * result + endLine;
		result = prime * result + ((lines == null) ? 0 : lines.hashCode());
		result = prime * result + ((statements == null) ? 0 : statements.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseStatement other = (BaseStatement) obj;
		if (beginLine != other.beginLine)
			return false;
		if (cleanLines == null) {
			if (other.cleanLines != null)
				return false;
		} else if (!cleanLines.equals(other.cleanLines))
			return false;
		if (endLine != other.endLine)
			return false;
		if (lines == null) {
			if (other.lines != null)
				return false;
		} else if (!lines.equals(other.lines))
			return false;
		if (statements == null) {
			if (other.statements != null)
				return false;
		} else if (!statements.equals(other.statements))
			return false;
		return true;
	}
}
