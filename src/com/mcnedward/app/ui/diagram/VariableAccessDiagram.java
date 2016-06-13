package com.mcnedward.app.ui.diagram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.mcnedward.app.classobject.ClassObject;
import com.mcnedward.app.classobject.ExpressionObject;
import com.mcnedward.app.classobject.VariableObject;
import com.mcnedward.app.ui.panel.ContentPanel;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Nov 1, 2015
 *
 */
public class VariableAccessDiagram extends JPanel {
	private static final long serialVersionUID = 281516601638641222L;

	private VariableObject variableObject;
	private ClassObject classObject;

	private JPanel mainPanel;
	private JPanel headerPanel;
	private JPanel variablePanel;
	private JLabel lblHeader;

	private Font font;

	public VariableAccessDiagram(ContentPanel parent, VariableObject variableObject, ClassObject classObject) {
		this.variableObject = variableObject;
		this.classObject = classObject;
		font = new Font("Segoe UI", Font.PLAIN, 12);

		int width = 450;
		int height = parent.getDiagramPanel().getHeight();
		int x = (parent.getDiagramPanel().getWidth() / 2) - (width / 2);
		int y = (parent.getDiagramPanel().getHeight() / 2) - (height / 2);
		setBounds(x, y, width, height);
		setLayout(null);

		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, getWidth(), getHeight());
		add(mainPanel);

		createHeaderPanel();
		createDiagram();
	}

	private void createHeaderPanel() {
		mainPanel.setLayout(new BorderLayout(0, 0));
		headerPanel = new JPanel();
		headerPanel.setBorder(new LineBorder(Color.BLACK));

		lblHeader = new JLabel("Variable Access - " + variableObject.getName());
		headerPanel.add(lblHeader);

		mainPanel.add(headerPanel, BorderLayout.NORTH);
	}

	private void updateVariableCount(String text) {
		lblHeader.setText("Variable Access - " + variableObject.getName() + " [" + text + "]");
	}

	private void createDiagram() {
		JScrollPane flowGraphScrollPane = new JScrollPane();
		flowGraphScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		flowGraphScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		variablePanel = new JPanel();
		variablePanel.setLayout(new BoxLayout(variablePanel, BoxLayout.Y_AXIS));

		displayVariables();

		flowGraphScrollPane.setViewportView(variablePanel);
		mainPanel.add(flowGraphScrollPane);
	}

	private void displayVariables() {
		int variableCount = 0;

		List<ExpressionObject> nameExpressions = classObject.getExpressions();
		if (!nameExpressions.isEmpty()) {
			for (ExpressionObject expression : nameExpressions) {
				// TODO Needs to be updated a lot...
				String expressionName;
				if (expression.getName().contains("this."))
					expressionName = expression.getName().substring(expression.getName().indexOf("this.") + "this.".length());
				else
					expressionName = expression.getName();
				String variableName;
				if (variableObject.getName().contains("this."))
					variableName = variableObject.getName().substring(variableObject.getName().indexOf("this.") + "this.".length());
				else
					variableName = variableObject.getName();

				if (expressionName.equals(variableName)) {
					variableCount++;
					JPanel panel = new JPanel();
					panel.setLayout(new BorderLayout());

					JLabel label = new JLabel(expression.toString());
					label.setFont(font);
					label.setHorizontalAlignment(SwingConstants.LEFT);

					panel.add(label);
					variablePanel.add(panel);
				}
			}
		}
		updateVariableCount(String.valueOf(variableCount));
	}

}
