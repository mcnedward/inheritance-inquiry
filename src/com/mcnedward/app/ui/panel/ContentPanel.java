package com.mcnedward.app.ui.panel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.mcnedward.app.classobject.ClassObject;
import com.mcnedward.app.classobject.VariableObject;
import com.mcnedward.app.classobject.method.MethodObject;
import com.mcnedward.app.ui.diagram.ClassDiagram;
import com.mcnedward.app.ui.diagram.FlowGraphDiagram;
import com.mcnedward.app.ui.diagram.MethodDiagram;
import com.mcnedward.app.ui.diagram.MetricsDiagram;
import com.mcnedward.app.ui.diagram.VariableAccessDiagram;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 20, 2015
 *
 */
public class ContentPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JPanel parent;
	private JPanel diagramPanel;
	private JPanel methodPanel;
	private JPanel metricsPanel;
	private JPanel flowGraphPanel;
	private JPanel variablePanel;

	private JPanel wmcPanel;
	private JPanel ditPanel;

	private ClassDiagram classDiagram;
	private MethodDiagram methodDiagram;
	@SuppressWarnings("unused")	// TODO REMOVE
	private MetricsDiagram metricsDiagram;
	private FlowGraphDiagram flowGraphDiagram;
	private VariableAccessDiagram variableAccessDiagram;

	private ClassObject classObject;
	private List<ClassObject> classObjects;

	public ContentPanel(JPanel parent) {
		this.parent = parent;

		initialize();
	}

	private void initialize() {
		setBounds(parent.getX(), parent.getY(), parent.getWidth(), parent.getHeight());

		GridBagLayout g = new GridBagLayout();
		g.columnWidths = new int[] { parent.getWidth() / 2, parent.getWidth() / 2 };
		g.rowHeights = new int[] { parent.getHeight() / 2, parent.getHeight() / 2 };
		g.columnWeights = new double[] { 1.0, 1.0 };
		g.rowWeights = new double[] { 1.0, 1.0 };
		setLayout(g);

		addDiagramPanel();
		addMethodPanel();
		addMetricsPanel();
		addVariableAccessPanel();
		addFlowGraphPanel();
	}

	public void loadClassObjects(List<ClassObject> classObjects) {
		this.classObjects = classObjects;
		loadClassObject(classObjects.get(0));
	}

	public void loadClassObjectForFile(File file) {
		if (classObjects == null || classObjects.isEmpty()) {
			System.out.println("No class objects...");
		}
		for (ClassObject classObject : classObjects) {
			if (classObject.getSourceFile().equals(file)) {
				loadClassObject(classObject);
				return;
			}
		}
		System.out.println("File " + file.getName() + " is not found in class objects.");
	}

	private void loadClassObject(ClassObject classObject) {
		this.classObject = classObject;
		if (classDiagram != null) {
			System.out.println("Removing class diagram for: " + classObject.getName());
			diagramPanel.removeAll();

			methodPanel.removeAll();
			addPanelHeader(methodPanel, "Select a method to view the definition.");

			flowGraphPanel.removeAll();

			metricsPanel.removeAll();
		}

		classDiagram = new ClassDiagram(this, classObject);
		diagramPanel.add(classDiagram);
		updatePanelHeader(methodPanel, "Select a method to view the definition.");

		metricsDiagram = new MetricsDiagram(this, classObject);
//		MainWindow2.metricsPanel.add(metricsDiagram);

		revalidate();
		repaint();
		System.out.println("Loading class diagram for: " + classObject.getName());
	}

	public void showMethod(MethodObject methodObject) {
		if (methodDiagram != null) {
			System.out.println("Removing method diagram for: " + methodObject.getName());
			methodPanel.remove(methodDiagram);
		}
		if (flowGraphDiagram != null) {
			flowGraphPanel.remove(flowGraphDiagram);
		}

		methodObject.reset();

		methodDiagram = new MethodDiagram(this, methodObject);
		methodPanel.add(methodDiagram);
		removePanelHeader(methodPanel);

		flowGraphDiagram = new FlowGraphDiagram(flowGraphPanel, methodObject);
		flowGraphPanel.add(flowGraphDiagram);

		revalidate();
		repaint();
		System.out.println("Loading method diagram for: " + methodObject.getName());
	}

	public void showVariableAccess(VariableObject variableObject) {
		if (variableAccessDiagram != null) {
			System.out.println("Removing method diagram for: " + variableObject.getName());
			variablePanel.remove(variableAccessDiagram);
		}
		
		variableAccessDiagram = new VariableAccessDiagram(this, variableObject, classObject);
		variablePanel.add(variableAccessDiagram);
		
		revalidate();
		repaint();
		System.out.println("Loading variable access diagram for: " + variableObject.getName());
	}

	private void removePanelHeader(JPanel parent) {
		JPanel panel = (JPanel) parent.getClientProperty("header");
		if (panel != null)
			parent.remove(panel);
	}

	private void updatePanelHeader(JPanel parent, String text) {
		JPanel panel = (JPanel) parent.getClientProperty("header");
		JLabel label = (JLabel) panel.getComponent(0);
		label.setText(text);
	}

	private void addDiagramPanel() {
		diagramPanel = new JPanel();
		setPanelBounds(diagramPanel);
		diagramPanel.setLayout(null); // Set to AbsoluteLayout

		GridBagConstraints g = new GridBagConstraints();
		g.gridheight = 1;
		g.gridwidth = 1;
		g.gridx = 0;
		g.gridy = 0;
		g.fill = GridBagConstraints.BOTH;

		add(diagramPanel, g);
	}

	private void addMethodPanel() {
		methodPanel = new JPanel();
		setPanelBounds(methodPanel);
		methodPanel.setLayout(null); // Set to AbsoluteLayout

		GridBagConstraints g = new GridBagConstraints();
		g.gridheight = 1;
		g.gridwidth = 1;
		g.gridx = 1;
		g.gridy = 0;
		g.fill = GridBagConstraints.BOTH;

		add(methodPanel, g);
		addPanelHeader(methodPanel, "");
	}

	private void addVariableAccessPanel() {
		variablePanel = new JPanel();
		setPanelBounds(variablePanel);
		variablePanel.setLayout(null);

		GridBagConstraints g = new GridBagConstraints();
		g.gridheight = 1;
		g.gridwidth = 1;
		g.gridx = 0;
		g.gridy = 1;
		g.fill = GridBagConstraints.BOTH;

		add(variablePanel, g);
	}

	// TODO Maybe update this to display in MainWindow
	private void addMetricsPanel() {
		metricsPanel = new JPanel();
		setPanelBounds(metricsPanel);
		methodPanel.setLayout(null);

		GridBagConstraints g = new GridBagConstraints();
		g.gridheight = 1;
		g.gridwidth = 1;
		g.gridx = 0;
		g.gridy = 1;
		g.fill = GridBagConstraints.BOTH;

		add(metricsPanel, g);
	}

	private void addFlowGraphPanel() {
		flowGraphPanel = new JPanel();
		setPanelBounds(flowGraphPanel);
		flowGraphPanel.setLayout(null);

		GridBagConstraints g = new GridBagConstraints();
		g.gridheight = 1;
		g.gridwidth = 1;
		g.gridx = 1;
		g.gridy = 1;
		g.fill = GridBagConstraints.BOTH;

		add(flowGraphPanel, g);
	}

	private void addPanelHeader(JPanel parent, String text) {
		JPanel panel = new JPanel();
		parent.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		JLabel label = new JLabel(text);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(label);
		panel.setBounds(0, 0, parent.getWidth(), 30);

		// Save a reference to the header to allow for updates and removal
		parent.putClientProperty("header", panel);
	}

	private void setPanelBounds(JPanel panel) {
		int width = (getWidth() / 2);
		int height = getHeight() / 2;
		int x = (width / 2) - (width / 4);
		int y = (height / 2) - (height / 4);
		panel.setBounds(x, y, width, height);
	}

	/**
	 * @return the diagramPanel
	 */
	public JPanel getDiagramPanel() {
		return diagramPanel;
	}

	/**
	 * @param diagramPanel
	 *            the diagramPanel to set
	 */
	public void setDiagramPanel(JPanel diagramPanel) {
		this.diagramPanel = diagramPanel;
	}

	/**
	 * @return the methodPanel
	 */
	public JPanel getMethodPanel() {
		return methodPanel;
	}

	/**
	 * @param methodPanel
	 *            the methodPanel to set
	 */
	public void setMethodPanel(JPanel methodPanel) {
		this.methodPanel = methodPanel;
	}

	/**
	 * @return the flowGraphPanel
	 */
	public JPanel getFlowGraphPanel() {
		return flowGraphPanel;
	}

	/**
	 * @param flowGraphPanel
	 *            the flowGraphPanel to set
	 */
	public void setFlowGraphPanel(JPanel flowGraphPanel) {
		this.flowGraphPanel = flowGraphPanel;
	}

	/**
	 * @return the wmcPanel
	 */
	public JPanel getWmcPanel() {
		return wmcPanel;
	}

	/**
	 * @param wmcPanel
	 *            the wmcPanel to set
	 */
	public void setWmcPanel(JPanel wmcPanel) {
		this.wmcPanel = wmcPanel;
	}

	/**
	 * @return the ditPanel
	 */
	public JPanel getDitPanel() {
		return ditPanel;
	}

	/**
	 * @param ditPanel
	 *            the ditPanel to set
	 */
	public void setDitPanel(JPanel ditPanel) {
		this.ditPanel = ditPanel;
	}

	/**
	 * @return the classDiagram
	 */
	public ClassDiagram getClassDiagram() {
		return classDiagram;
	}

	/**
	 * @param classDiagram
	 *            the classDiagram to set
	 */
	public void setClassDiagram(ClassDiagram classDiagram) {
		this.classDiagram = classDiagram;
	}

	/**
	 * @return the classObjects
	 */
	public List<ClassObject> getClassObjects() {
		return classObjects;
	}

	/**
	 * @param classObjects
	 *            the classObjects to set
	 */
	public void setClassObjects(List<ClassObject> classObjects) {
		this.classObjects = classObjects;
	}

}
