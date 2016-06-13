package com.mcnedward.app.ui.panel.ifpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 29, 2015
 *
 */
public class IfPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static int ID;

	private JPanel statementPanel;

	public IfPanel(int height, JPanel node) {
		ID++;
		initialize(height, node);
	}

	private void initialize(int height, JPanel node) {
		setLayout(new BorderLayout(0, 0));
		setBorder(new LineBorder(Color.CYAN));
		
		setPreferredSize(new Dimension(100, 800));
		
		JPanel conditionPanel = new JPanel();
		// Create Statement panels
		statementPanel = new JPanel();
		statementPanel.setLayout(new GridLayout());
		statementPanel.setBorder(new LineBorder(Color.GRAY));
		statementPanel.putClientProperty("statementPanel", true);
		add(conditionPanel, BorderLayout.NORTH);
		add(statementPanel, BorderLayout.CENTER);
		
		addConditionalStatementPanel("then");

		conditionPanel.add(node);
	}

	public JPanel addConditionalStatementPanel(String panelCondition) {
		JPanel conditionalPanel = new JPanel();
		conditionalPanel.setBorder(new LineBorder(Color.GRAY));
		conditionalPanel.setLayout(new BoxLayout(conditionalPanel, BoxLayout.Y_AXIS));
		statementPanel.add(conditionalPanel);
		statementPanel.putClientProperty(panelCondition, conditionalPanel);
		return conditionalPanel;
	}
	
	public void addToThenPanel(JPanel node) {
		JPanel thenPanel = (JPanel) statementPanel.getClientProperty("then");
		thenPanel.add(node);
	}
	
	public void addToElsePanel(JPanel node) {
		JPanel elsePanel = (JPanel) statementPanel.getClientProperty("else");
		if (elsePanel == null)
			elsePanel = addConditionalStatementPanel("else");
		elsePanel.add(node);
	}
	
	public void addToElseIfPanel(JPanel node) {
		JPanel elseIfPanel = addConditionalStatementPanel("elseIf");
		elseIfPanel.add(node);		
	}
}
