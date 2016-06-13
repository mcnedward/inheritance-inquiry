package com.mcnedward.app.ui.diagram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.mcnedward.app.classobject.ClassObject;
import com.mcnedward.app.classobject.VariableObject;
import com.mcnedward.app.classobject.method.MethodObject;
import com.mcnedward.app.classobject.method.MethodParameter;
import com.mcnedward.app.ui.panel.ContentPanel;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 18, 2015
 *
 */
public class ClassDiagram extends BaseDiagram<ClassObject> {
	private static final long serialVersionUID = -523408531976611140L;

	public ClassDiagram() {
		super();
	}

	public ClassDiagram(ContentPanel parent, ClassObject classObject) {
		super(parent, classObject);

		createVariablePanels();
		createMethodPanels();
	}

	private void createVariablePanels() {
		List<VariableObject> variables = typeObject.getVariables();
		if (variables.isEmpty())
			return;
		JPanel variablePanel = new JPanel();
		variablePanel.setLayout(new BoxLayout(variablePanel, BoxLayout.Y_AXIS));
		variablePanel.setBorder(new LineBorder(Color.BLACK));

		GridBagConstraints constraints2 = new GridBagConstraints();
		constraints2.gridwidth = 1;
		constraints2.gridx = 0;
		constraints2.gridy = 1;
		constraints2.fill = GridBagConstraints.BOTH;

		for (VariableObject variableObject : variables) {
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());

			String variable = "- " + variableObject.getName() + ": " + variableObject.getType();
			JLabel label = new JLabel(variable);
			label.setFont(font);
			label.setHorizontalAlignment(SwingConstants.LEFT);

			panel.add(label);
			addVariableListener(panel);
			label.putClientProperty("variableObject", variableObject);
			variablePanel.add(panel);
		}
		mainPanel.add(variablePanel, constraints2);
	}

	private void createMethodPanels() {
		List<MethodObject> methods = typeObject.getMethods();
		if (methods.isEmpty())
			return;
		JPanel methodPanel = new JPanel();
		methodPanel.setLayout(new BoxLayout(methodPanel, BoxLayout.Y_AXIS));
		methodPanel.setBorder(new LineBorder(Color.BLACK));

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.fill = GridBagConstraints.BOTH;

		for (MethodObject methodObject : methods) {
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());

			String method = methodObject.getName();
			List<MethodParameter> parameters = methodObject.getMethodParameters();
			method += "(";
			if (!parameters.isEmpty()) {
				for (int x = 0; x < parameters.size(); x++) {
					MethodParameter methodParameter = parameters.get(x);
					String parameter = methodParameter.getParameterName() + ":" + methodParameter.getParameterType();
					method += parameter;
					if (x != parameters.size() - 1)
						method += ", ";
				}
			}
			method += ")";
			JLabel label = new JLabel(method);
			label.setFont(font);
			label.setHorizontalAlignment(SwingConstants.LEFT);
			panel.setBorder(new EmptyBorder(2, 5, 2, 5));
			panel.add(label);
			addMethodListener(panel);
			label.putClientProperty("methodObject", methodObject);
			methodPanel.add(panel);
		}
		mainPanel.add(methodPanel, constraints);
	}

	private void addVariableListener(JPanel panel) {
		JLabel label = (JLabel) panel.getComponents()[0];
		panel.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				label.setForeground(Color.BLACK);
				label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				label.setForeground(Color.BLUE);
				label.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				VariableObject variable = (VariableObject) label.getClientProperty("variableObject");
				parent.showVariableAccess(variable);
			}
		});
	}

	private void addMethodListener(JPanel panel) {
		JLabel label = (JLabel) panel.getComponents()[0];
		panel.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				label.setForeground(Color.BLACK);
				label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				label.setForeground(Color.BLUE);
				label.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(label.getText());
				parent.showMethod((MethodObject) label.getClientProperty("methodObject"));
			}
		});
	}

	@Override
	protected void setDimension() {
		height = ((typeObject.getVariables().size() + typeObject.getMethods().size()) * 30) + 30;
		if (height > parent.getDiagramPanel().getHeight())
			height = parent.getDiagramPanel().getHeight();
		width = 450;
	}
}
