package com.mcnedward.app.ui.diagram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import com.mcnedward.app.classobject.method.MethodObject;
import com.mcnedward.app.ui.builder.StatementBuilder;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Nov 1, 2015
 *
 */
public class FlowGraphDiagram extends JPanel {
	private static final long serialVersionUID = 281516601638641222L;

	private MethodObject methodObject;

	private JPanel mainPanel;
	private JPanel headerPanel;
	private JPanel flowGraphPanel;

	private StatementBuilder statementBuilder;

	public FlowGraphDiagram(JPanel parent, MethodObject methodObject) {
		this.methodObject = methodObject;

		int x = (parent.getWidth() / 2) - (450 / 2);
		int y = (parent.getHeight() / 2) - (parent.getHeight() / 2);
		setBounds(x, y, 450, parent.getHeight());
		setLayout(null);

		JButton button = new JButton("Expand");
		button.setBounds(0, 0, 85, 30);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFlowDiagramFrame();
			}
		});
		add(button);

		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, getWidth(), getHeight());
		add(mainPanel);

		createHeaderPanel();
		statementBuilder = new StatementBuilder(methodObject);
		createFlowGraphDiagram();
	}

	public void showFlowDiagramFrame() {
		FlowGraphFrame frame = new FlowGraphFrame(methodObject);
		frame.setVisible(true);
	}

	private void createHeaderPanel() {
		mainPanel.setLayout(new BorderLayout(0, 0));
		headerPanel = new JPanel();
		headerPanel.setBorder(new LineBorder(Color.BLACK));

		JLabel lblIf = new JLabel("IF");
		lblIf.setForeground(Color.GREEN);
		headerPanel.add(lblIf);
		JLabel lblFor = new JLabel("FOR");
		lblFor.setForeground(Color.BLUE);
		headerPanel.add(lblFor);
		JLabel lblWhile = new JLabel("WHILE");
		lblWhile.setForeground(Color.CYAN);
		headerPanel.add(lblWhile);
		JLabel lblSwitch = new JLabel("SWITCH");
		lblSwitch.setForeground(Color.RED);
		headerPanel.add(lblSwitch);

		mainPanel.add(headerPanel, BorderLayout.NORTH);
	}

	private void createFlowGraphDiagram() {
		JScrollPane flowGraphScrollPane = new JScrollPane();
		flowGraphScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		flowGraphScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		flowGraphPanel = new JPanel();
		flowGraphPanel.setLayout(new BoxLayout(flowGraphPanel, BoxLayout.Y_AXIS));
		flowGraphPanel.setBorder(new LineBorder(Color.BLUE));

		statementBuilder.build(flowGraphPanel);

		flowGraphScrollPane.setViewportView(flowGraphPanel);
		mainPanel.add(flowGraphScrollPane);
	}

}
