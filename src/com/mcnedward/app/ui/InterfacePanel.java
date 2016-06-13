package com.mcnedward.app.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.mcnedward.app.classobject.ClassObject;

/**
 * @author Edward - Jun 12, 2016
 *
 */
public class InterfacePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private DefaultListModel<ClassObject> interfaceListModel;
	private JList<ClassObject> interfaceList;
	private DefaultListModel<String> selectedInterfaceListModel;
	private JList<String> selectedInterfaceList;
	private JPanel selectedInterfacePanel;
	private JLabel selectedInterfaceLabel;

	private List<ClassObject> classes;

	public InterfacePanel() {
		init();
	}

	public void load(List<ClassObject> classes) {
		this.classes = classes;
		interfaceListModel.clear();
		for (ClassObject classObject : classes) {
			if (classObject.isInterface())
				interfaceListModel.addElement(classObject);
		}
	}

	private void findInterfaceUsages(ClassObject selectedClass) {
		selectedInterfaceListModel.clear();
		for (ClassObject classObject : classes) {
			String selectedClassName = selectedClass.getName();
			if (classObject.getInterfaces().contains(selectedClassName)) {
				selectedInterfaceListModel.addElement("implemented by " + classObject.getName());
			}
			else if (classObject.getExtends().contains(selectedClassName)) {
				selectedInterfaceListModel.addElement("extended by " + classObject.getName());
			}
		}
		selectedInterfacePanel.setVisible(true);
		selectedInterfaceLabel.setText(selectedClass.getName());
	}

	private void init() {
		setLayout(new BorderLayout(0, 0));

		JLabel lblInterfaces = new JLabel("Interfaces");
		lblInterfaces.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblInterfaces, BorderLayout.NORTH);

		JPanel gridPanel = new JPanel();
		add(gridPanel, BorderLayout.CENTER);
		gridPanel.setLayout(new GridLayout(0, 2, 0, 0));

		interfaceListModel = new DefaultListModel<>();
		interfaceList = new JList<>(interfaceListModel);
		interfaceList.setCellRenderer(new ClassObjectListCellRenderer());
		JPanel interfacePanel = createList(interfaceList, new JLabel("Select an interface to inspect"), new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = interfaceList.getSelectedIndex();
				ClassObject selectedClassObject = interfaceListModel.getElementAt(index);
				findInterfaceUsages(selectedClassObject);
			}
		});
		gridPanel.add(interfacePanel);

		selectedInterfaceLabel = new JLabel();
		selectedInterfaceListModel = new DefaultListModel<>();
		selectedInterfaceList = new JList<>(selectedInterfaceListModel);
		selectedInterfacePanel = createList(selectedInterfaceList, selectedInterfaceLabel, new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		gridPanel.add(selectedInterfacePanel);
		selectedInterfacePanel.setVisible(false);
	}

	/**
	 * Creates a list with a MouseAdapter action.
	 * 
	 * @param listModel
	 *            The DefaultListModel of ClassObjects that will make up the list.
	 * @param mouseAdapter
	 *            The MouseAdapter defining the action to happen when an item in the list is selected.
	 * @return The panel containing the list.
	 */
	private JPanel createList(JList<?> listFiles, JLabel label, MouseAdapter mouseAdapter) {
		listFiles.setFont(new Font("Tahoma", Font.PLAIN, 10));
		listFiles.setVisibleRowCount(10);
		listFiles.setSelectedIndex(0);
		listFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listFiles.addMouseListener(mouseAdapter);
		JScrollPane scrollPane = new JScrollPane(listFiles);

		JPanel interfacePanel = new JPanel();
		interfacePanel.setLayout(new BorderLayout());
		interfacePanel.setBorder(new EmptyBorder(0, 10, 0, 10));

		interfacePanel.add(label, BorderLayout.NORTH);
		interfacePanel.add(scrollPane, BorderLayout.CENTER);
		return interfacePanel;
	}

}
