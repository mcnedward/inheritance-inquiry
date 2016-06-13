package com.mcnedward.app.ui.diagram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import com.mcnedward.app.classobject.ClassObject;
import com.mcnedward.app.classobject.method.MethodCallObject;
import com.mcnedward.app.classobject.method.MethodObject;
import com.mcnedward.app.ui.panel.ContentPanel;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 23, 2015
 *
 */
public class MetricsDiagram extends JPanel {
	private static final long serialVersionUID = -523408531976611140L;

	private JPanel metricContent;

	private ClassObject classObject;
	List<ClassObject> classObjects;

	public MetricsDiagram() {
	}

	public MetricsDiagram(ContentPanel parent, ClassObject classObject) {
		this.classObject = classObject;
		classObjects = parent.getClassObjects();
		initialize();

		updateWMCPanel();
		updateDITPanel();
		updateNOCPanel();
		updateRFCPanel();
	}

	private void initialize() {
		setLayout(new BorderLayout(0, 0));

		JPanel headerPanel = new JPanel();
		add(headerPanel, BorderLayout.NORTH);
		headerPanel.add(new JLabel("Metrics"));
		headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));

		metricContent = new JPanel();
		add(metricContent, BorderLayout.CENTER);
		metricContent.setLayout(new BoxLayout(metricContent, BoxLayout.Y_AXIS));
	}

	private void updateWMCPanel() {
		JPanel wmcPanel = new JPanel();

		int wmc = classObject.getMethods().size();
		JLabel label = new JLabel("WMC = " + wmc);
		label.setBounds(0, 0, wmcPanel.getWidth(), 30);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		wmcPanel.add(label);

		metricContent.add(wmcPanel);
	}

	private void updateDITPanel() {
		JPanel ditPanel = new JPanel();

		// Plus one for the top-level (Object.class)
		int dit = classObject.getExtends().size() + 1;
		JLabel label = new JLabel("DIT = " + dit);
		label.setBounds(0, 0, ditPanel.getWidth(), 30);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		ditPanel.add(label);

		metricContent.add(ditPanel);
	}

	private void updateNOCPanel() {
		JPanel nocPanel = new JPanel();

		int noc = calculateNOC();
		JLabel label = new JLabel("NOC = " + noc);
		label.setBounds(0, 0, nocPanel.getWidth(), 30);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		nocPanel.add(label);

		metricContent.add(nocPanel);
	}

	private int calculateNOC() {
		int noc = 0;

		for (ClassObject parentCO : classObjects) {
			if (!parentCO.equals(classObject)) {
				if (parentCO.getExtends().contains(classObject.getName())) {
					System.out.println("Superclass " + classObject.getName() + " is extended by " + parentCO.getName());
					noc++;
				}
			}
		}
		return noc;
	}

	private void updateRFCPanel() {
		JPanel rfcPanel = new JPanel();

		int rfc = calculateRFC(false);
		JLabel label1 = new JLabel("RFC = " + rfc);
		label1.setBounds(0, 0, rfcPanel.getWidth(), 30);
		label1.setHorizontalAlignment(SwingConstants.LEFT);
		rfcPanel.add(label1);

		metricContent.add(rfcPanel);

		JPanel rfcPrimePanel = new JPanel();

		int rfcPrime = calculateRFC(true);
		JLabel label2 = new JLabel("RFC' = " + rfcPrime);
		label2.setBounds(0, 0, rfcPrimePanel.getWidth(), 30);
		label2.setHorizontalAlignment(SwingConstants.LEFT);
		rfcPrimePanel.add(label2);

		metricContent.add(rfcPrimePanel);
	}

	/**
	 * Calculate the RFC response for a class. This will find the number of methods in a class plus the number of remote
	 * methods called by those methods.
	 * 
	 * @param isPrime
	 *            If true, then this will calculate the number of remote methods called recursively throughout the
	 *            method's call stack.
	 * @return The RFC response set.
	 */
	private int calculateRFC(boolean isPrime) {
		int rfc = 0;
		int m = classObject.getMethods().size();
		int r = 0;

		if (!classObject.getMethods().isEmpty()) {
			for (MethodObject methodObject : classObject.getMethods()) {
				r += calculateRemoteMethods(methodObject, isPrime);
			}
		}

		rfc = m + r;

		return rfc;
	}

	/**
	 * Calculates the number of remote methods in a class. When trying to find the RFC', this will search through all
	 * the methods of the class that calls a method inside this method object.
	 * 
	 * @param methodObject
	 *            The top level method object.
	 * @param isPrime
	 *            True if you want to calculate the remote prime.
	 * @return The number of remote methods.
	 */
	private int calculateRemoteMethods(MethodObject methodObject, boolean isPrime) {
		int r = 0;

		List<MethodCallObject> mcoList = new ArrayList<MethodCallObject>();
		for (MethodCallObject methodCallObject : methodObject.getMethodCallObjects()) {
			if (!mcoList.contains(methodCallObject)) {
				mcoList.add(methodCallObject);
				if (isPrime) {
					for (ClassObject co : classObjects) {
						String sourceFile = co.getSourceFile().getName().replaceAll(".java", "");
						if (sourceFile.equals(methodCallObject.getParentClass())) {
							r += calculateRemotePrimeMethods(co, methodCallObject);
						}
					}
				}
			}
		}

		r += mcoList.size();
		return r;
	}

	/**
	 * Calculates the amount of methods called recursively throughout a methods entire call stack.
	 * 
	 * @param classObject
	 *            The class object for the method's call.
	 * @param originalMethodCallObject
	 *            The methodCallObject to find in the remote class.
	 */
	private int calculateRemotePrimeMethods(ClassObject classObject, MethodCallObject originalMethodCallObject) {
		MethodObject parentMethodObject = classObject.getMethodByName(originalMethodCallObject.getName());
		int r = 0;
		if (parentMethodObject == null) {
			System.out.println("The class [" + classObject.getName() + "] does not contain the method \"" + originalMethodCallObject.getName()
					+ "\". Might be a Java method.");
		} else {
			r += calculateRemoteMethods(parentMethodObject, true);
		}
		return r;
	}

}
