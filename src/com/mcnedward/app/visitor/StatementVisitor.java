package com.mcnedward.app.visitor;

import java.util.List;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.mcnedward.app.classobject.LineObject;
import com.mcnedward.app.classobject.LineType;
import com.mcnedward.app.classobject.statement.BaseStatement;
import com.mcnedward.app.classobject.statement.DoStatement;
import com.mcnedward.app.classobject.statement.ForEachStatement;
import com.mcnedward.app.classobject.statement.ForStatement;
import com.mcnedward.app.classobject.statement.IfStatement;
import com.mcnedward.app.classobject.statement.SwitchStatement;
import com.mcnedward.app.classobject.statement.WhileStatement;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 24, 2015
 *
 */
public class StatementVisitor extends BaseVisitor<BaseStatement> {

	public StatementVisitor() {
	}

	@Override
	public void reset() {
	}

	@Override
	public void visit(ForeachStmt f, BaseStatement arg) {
		ForEachStatement statement = new ForEachStatement();
		statement.setBeginLine(f.getBeginLine());
		statement.setEndLine(f.getEndLine());
		statement.setVariableType(f.getVariable().getType().toString());
		statement.setVariableValue(f.getVariable().getVars().get(0).toString());
		List<LineObject> lineObjects = convertToLineObjects(f);
		statement.setLines(lineObjects);
		arg.addStatement(statement);
	}

	@Override
	public void visit(ForStmt f, BaseStatement arg) {
		ForStatement statement = new ForStatement();
		statement.setBeginLine(f.getBeginLine());
		statement.setEndLine(f.getEndLine());
		statement.setInit(f.getInit().get(0).toString());
		statement.setCompare(f.getCompare().toString());
		statement.setUpdate(f.getUpdate().get(0).toString());
		List<LineObject> lineObjects = convertToLineObjects(f);
		statement.setLines(lineObjects);
		arg.addStatement(statement);
	}

	@Override
	public void visit(IfStmt i, BaseStatement arg) {
		List<Node> childrenNodes = i.getChildrenNodes();
		
		IfStatement statement = new IfStatement();
		statement.setBeginLine(i.getBeginLine());
		statement.setEndLine(i.getEndLine());
		statement.setThenBeginLine(i.getThenStmt().getBeginLine());
		statement.setThenEndLine(i.getThenStmt().getEndLine());
		if (i.getElseStmt() != null) {
			statement.setElseBeginLine(i.getElseStmt().getBeginLine());
			statement.setElseEndLine(i.getElseStmt().getEndLine());
		}

		List<LineObject> lineObjects = convertToLineObjects(i);

		// First line of the IfStmt is the condition statement
		LineObject conditionLine = findLineByNumber(lineObjects, i.getBeginLine());
		conditionLine.setLineType(LineType.IF);
		statement.setCondition(conditionLine);

		checkNodesForStatement(this, childrenNodes, statement);
		
		Statement elseStatement = i.getElseStmt();
		// Else statements in a block have the "else" line included in the statement, so the "else" line can be found
		// right away. Non-block else statements do not include the "else", so find the line directly above
		if (elseStatement != null) {
			LineObject elseLine = null;
			if (elseStatement instanceof BlockStmt) {
				elseLine = findLineByNumber(lineObjects, elseStatement.getBeginLine());
				elseLine.setLineType(LineType.ELSE);
			}
			else if (elseStatement instanceof IfStmt) {
				elseLine = findLineByNumber(lineObjects, elseStatement.getBeginLine());
				elseLine.setLineType(LineType.ELSE_IF);
			}
			else {
				elseLine = findLineByNumber(lineObjects, elseStatement.getBeginLine() - 1);
				elseLine.setLineType(LineType.ELSE);
			}
		}
		
		statement.setLines(lineObjects);
		arg.addStatement(statement);
	}

	private LineObject findLineByNumber(List<LineObject> lines, int lineNumber) {
		for (LineObject line : lines) {
			if (line.getLineNumber() == lineNumber)
				return line;
		}
		return null;
	}

	@Override
	public void visit(WhileStmt w, BaseStatement arg) {
		WhileStatement statement = new WhileStatement();
		statement.setBeginLine(w.getBeginLine());
		statement.setEndLine(w.getEndLine());
		statement.setCondition(w.getCondition().toString());
		List<LineObject> lineObjects = convertToLineObjects(w);
		statement.setLines(lineObjects);
		arg.addStatement(statement);
	}

	@Override
	public void visit(DoStmt d, BaseStatement arg) {
		DoStatement statement = new DoStatement();
		statement.setBeginLine(d.getBeginLine());
		statement.setEndLine(d.getEndLine());
		statement.setCondition(d.getCondition().toString());
		List<LineObject> lineObjects = convertToLineObjects(d);
		statement.setLines(lineObjects);
		arg.addStatement(statement);
	}

	@Override
	public void visit(SwitchStmt s, BaseStatement arg) {
		SwitchStatement statement = new SwitchStatement();
		statement.setBeginLine(s.getBeginLine());
		statement.setEndLine(s.getEndLine());
		statement.setSelector(s.getSelector().toString());
		List<SwitchEntryStmt> entries = s.getEntries();
		for (SwitchEntryStmt entry : entries) {
			Expression labelExpression = entry.getLabel();
			String label = "";
			boolean isDefault = false;
			if (labelExpression == null) {
				isDefault = true;
				label = "default";
			} else
				label = labelExpression.toString();
			statement.addSwitchEntry(label, entry.getBeginLine(), isDefault);
		}
		List<LineObject> lineObjects = convertToLineObjects(s);
		statement.setLines(lineObjects);
		arg.addStatement(statement);
	}
}
