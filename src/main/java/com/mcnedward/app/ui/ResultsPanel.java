package com.mcnedward.app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.MatteBorder;

import com.mcnedward.app.ui.utils.FontUtil;
import com.mcnedward.ii.element.JavaProject;

/**
 * @author Edward - Jun 12, 2016
 *
 */
public class ResultsPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel mLblHeader;

	private InterfacePanel mInterfacePanel;
	private InheritancePanel mInheritancePanel;

	public ResultsPanel() {
		init();
		FontUtil.changeFont(this, new Font("Segue UI", Font.PLAIN, 10));
	}

	public void loadProject(JavaProject project) {
		String headerMessage = String.format("%s - Classes [%s] - Interfaces [%s]", project.getName(), project.getClasses().size(),
				project.getInterfaces().size());
		mLblHeader.setText(headerMessage);
		mInterfacePanel.load(project.getInterfaces());
		mInheritancePanel.load(project);
	}

	private void init() {
		setLayout(new BorderLayout(0, 0));
		JPanel headerPanel = new JPanel();
		add(headerPanel, BorderLayout.NORTH);

		mLblHeader = new JLabel();
		headerPanel.add(mLblHeader);
		headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		add(tabbedPane, BorderLayout.CENTER);

		mInterfacePanel = new InterfacePanel();
		tabbedPane.addTab("Interface", null, mInterfacePanel, "View interface usage");

		mInheritancePanel = new InheritancePanel();
		tabbedPane.addTab("Inheritance", null, mInheritancePanel, "View inheritance usage");
	}

}
