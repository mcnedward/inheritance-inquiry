package com.mcnedward.app.ui.builder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.mcnedward.app.classobject.LineObject;
import com.mcnedward.app.classobject.LineType;
import com.mcnedward.app.classobject.method.MethodObject;
import com.mcnedward.app.classobject.statement.BaseStatement;
import com.mcnedward.app.classobject.statement.DoStatement;
import com.mcnedward.app.classobject.statement.ForEachStatement;
import com.mcnedward.app.classobject.statement.ForStatement;
import com.mcnedward.app.classobject.statement.IfStatement;
import com.mcnedward.app.classobject.statement.SwitchStatement;
import com.mcnedward.app.classobject.statement.WhileStatement;
import com.mcnedward.app.ui.panel.NodePanel;
import com.mcnedward.app.ui.panel.ifpanel.IfPanel;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 29, 2015
 *
 */
public class StatementBuilder {

	private MethodObject methodObject;
	private boolean smallIcons;

	public StatementBuilder(MethodObject methodObject, boolean smallIcons) {
		this.methodObject = methodObject;
		this.smallIcons = smallIcons;
	}

	public StatementBuilder(MethodObject methodObject) {
		this(methodObject, false);
	}

	public void build(JPanel parent) {
		BaseStatement methodStatement = methodObject.getStatement();

		for (LineObject line : methodStatement.getLines()) {
			if (!methodObject.isNodeCreatedAtLine(line.getLineNumber())) {
				BaseStatement statement = methodObject.getStatementAtLineNumber(line.getLineNumber());
				if (statement != null) {
					if (statement instanceof IfStatement) {
						IfStatement ifStatement = (IfStatement) statement;
						IfPanel ifPanel = new IfPanel(parent.getHeight(), createNode(ifStatement.getCondition(), Color.GREEN));
						methodObject.setNodeCreated(((IfStatement) statement).getCondition().getLineNumber(), true);
						fillIfPanel(ifStatement, ifPanel);
						parent.add(ifPanel);
					}
					if (statement instanceof ForStatement || statement instanceof ForEachStatement) {
						createStatementNode(statement, parent, Color.BLUE);
					}
					if (statement instanceof WhileStatement || statement instanceof DoStatement) {
						createStatementNode(statement, parent, Color.CYAN);
					}
					if (statement instanceof SwitchStatement) {
						createStatementNode(statement, parent, Color.RED);
					}
				} else {
					parent.add(createNode(line, Color.BLACK));
					methodObject.setNodeCreated(line.getLineNumber(), true);
				}
			}
		}

		// Reset for reuse!
		methodObject.reset();
	}

	private BaseStatement fillIfPanel(IfStatement ifStatement, IfPanel ifPanel) {
		boolean isElse = false;
		for (LineObject line : ifStatement.getLines()) {
			if (!methodObject.isNodeCreatedAtLine(line.getLineNumber())) {
				if (line.getLineType() == null) {
					if ((isElse) || Pattern.compile("\\s*\\}?\\s*\\belse\\b\\s*\\{?").matcher(line.getLine()).find()) {
						JPanel nodeContainer = createNode(line, Color.GREEN);
						ifPanel.addToElsePanel(nodeContainer);
						methodObject.setNodeCreated(line.getLineNumber(), true);
					} else {
						JPanel nodeContainer = createNode(line, Color.GREEN);
						ifPanel.addToThenPanel(nodeContainer);
						methodObject.setNodeCreated(line.getLineNumber(), true);
					}
				} else if ((line.getLineType() != null && line.getLineType().equals(LineType.ELSE)) || isElse) {
					if (!methodObject.isParentElse(ifStatement)) {
						isElse = true;
						JPanel nodeContainer = createNode(line, Color.GREEN);
						ifPanel.addToElsePanel(nodeContainer);
						methodObject.setNodeCreated(line.getLineNumber(), true);
					} else {
						return null;
					}
				} else if (line.isStatement()) {
					BaseStatement statement = ifStatement.getStatementAtLineNumber(ifStatement, line.getLineNumber());
					if (statement instanceof IfStatement) {
						if (line.getLineType().equals(LineType.ELSE_IF)) {
							// New ElseIf Panel
							IfPanel newIfPanel = new IfPanel(ifPanel.getHeight(), createNode(((IfStatement) statement).getCondition(), Color.GREEN));
							methodObject.setNodeCreated(((IfStatement) statement).getCondition().getLineNumber(), true);
							fillIfPanel((IfStatement) statement, newIfPanel);
							ifPanel.addToElseIfPanel(newIfPanel);
							isElse = true;
						} else if (isElse) {
							JPanel branchPanel = new JPanel();
							branchPanel.setLayout(new BorderLayout(0, 0));
							branchPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
							branchPanel.setBorder(new LineBorder(Color.BLUE));
						} else {
							// New If Panel
							IfPanel newIfPanel = new IfPanel(ifPanel.getHeight(), createNode(((IfStatement) statement).getCondition(), Color.GREEN));
							methodObject.setNodeCreated(((IfStatement) statement).getCondition().getLineNumber(), true);
							fillIfPanel((IfStatement) statement, newIfPanel);
							ifPanel.addToThenPanel(newIfPanel);
						}
					}
				}
			}
		}
		return null;
	}

	private void createStatementNode(BaseStatement statement, JPanel container, Color color) {
		for (LineObject line : statement.getLines()) {
			container.add(createNode(line, color));
			methodObject.setNodeCreated(line.getLineNumber(), true);
		}
	}

	private JPanel createNode(LineObject line, Color color) {
		JPanel container = new JPanel();
		String nodeText = "m" + String.valueOf(line.getNodeNumber());
		NodePanel panel = new NodePanel(nodeText, color, smallIcons);
		container.add(panel);
		return container;
	}
	
}
