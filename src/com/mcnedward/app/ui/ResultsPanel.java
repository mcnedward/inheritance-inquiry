package com.mcnedward.app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import com.mcnedward.app.classobject.AnalysisResult;
import com.mcnedward.app.classobject.ClassObject;

/**
 * @author Edward - Jun 12, 2016
 *
 */
public class ResultsPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel lblHeader;
	private JLabel lblNumClasses;
	private JLabel lblNumInterfaces;
	private JLabel lblNumExtends;

	private InterfacePanel interfacePanel;

	public ResultsPanel() {
		init();
	}

	public void loadClasses(AnalysisResult result) {
		File analysisFile = result.getFile();
		String fileName = analysisFile.getName();
		lblHeader.setText(fileName);
		List<ClassObject> classes = result.getClassObjects();
		
		loadNumberOfClasses(classes);
		loadNumberOfInterfaces(classes);
		loadNumberOfExtendsPanel(classes);
		interfacePanel.load(classes);
	}

	private void init() {
		setLayout(new BorderLayout(0, 0));
		JPanel headerPanel = new JPanel();
		add(headerPanel, BorderLayout.NORTH);

		lblHeader = new JLabel();
		headerPanel.add(lblHeader);
		headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		interfacePanel = new InterfacePanel();
		panel.add(interfacePanel, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));

		// Number of Classes
		JPanel numberOfClassesPanel = new JPanel();
		panel_1.add(numberOfClassesPanel);
		numberOfClassesPanel.setLayout(new BorderLayout(0, 0));
		JLabel lblNumberOfClasses = new JLabel("Number of Classes:");
		numberOfClassesPanel.add(lblNumberOfClasses, BorderLayout.WEST);
		lblNumClasses = new JLabel();
		numberOfClassesPanel.add(lblNumClasses);

		// Number of Interfaces
		JPanel numberOfInterfacesPanel = new JPanel();
		panel_1.add(numberOfInterfacesPanel);
		numberOfInterfacesPanel.setLayout(new BorderLayout(0, 0));
		JLabel label = new JLabel("Number of Interfaces:");
		numberOfInterfacesPanel.add(label, BorderLayout.WEST);
		lblNumInterfaces = new JLabel();
		numberOfInterfacesPanel.add(lblNumInterfaces, BorderLayout.CENTER);

		// Number of Extends
		JPanel numberOfExtendsPanel = new JPanel();
		panel_1.add(numberOfExtendsPanel);
		numberOfExtendsPanel.setLayout(new BorderLayout(0, 0));
		JLabel label_2 = new JLabel("Number of Extends:");
		numberOfExtendsPanel.add(label_2, BorderLayout.WEST);
		lblNumExtends = new JLabel();
		numberOfExtendsPanel.add(lblNumExtends, BorderLayout.CENTER);
	}

	private void loadNumberOfClasses(List<ClassObject> classes) {
		lblNumClasses.setText(String.valueOf(classes.size()));
	}

	private void loadNumberOfInterfaces(List<ClassObject> classes) {
		int numberOfInterfaces = 0;
		for (ClassObject classObject : classes) {
			numberOfInterfaces += classObject.getInterfaces().size();
		}
		lblNumInterfaces.setText(String.valueOf(numberOfInterfaces));
	}

	private void loadNumberOfExtendsPanel(List<ClassObject> classes) {
		int numberOfExtends = 0;
		for (ClassObject classObject : classes) {
			numberOfExtends += classObject.getExtends().size();
		}
		lblNumExtends.setText(String.valueOf(numberOfExtends));
	}

}
