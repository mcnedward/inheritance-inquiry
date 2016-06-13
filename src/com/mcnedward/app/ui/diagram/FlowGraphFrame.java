package com.mcnedward.app.ui.diagram;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.mcnedward.app.classobject.method.MethodObject;
import com.mcnedward.app.ui.MainWindow2;
import com.mcnedward.app.ui.builder.StatementBuilder;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 29, 2015
 *
 */
public class FlowGraphFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel mainPanel;
	private JPanel flowGraphPanel;
	protected JLabel lblHeader;

	private StatementBuilder statementBuilder;

	public FlowGraphFrame(MethodObject methodObject) {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
			System.out.println("Something went wrong when trying to use the System Look and Feel...");
		}
		setBounds(100, 100, (MainWindow2.WIDTH / 2) + 60, MainWindow2.HEIGHT);

		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);

		createHeaderPanel();
		statementBuilder = new StatementBuilder(methodObject);
		createFlowGraphDiagram();
	}

	private void createHeaderPanel() {
		mainPanel.setLayout(new BorderLayout(0, 0));
		JPanel headerPanel = new JPanel();
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
