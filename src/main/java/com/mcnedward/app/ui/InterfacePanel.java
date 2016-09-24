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
import javax.swing.border.EmptyBorder;

import com.mcnedward.app.ui.cellRenderer.JavaElementListCellRenderer;
import com.mcnedward.ii.element.JavaElement;

/**
 * @author Edward - Jun 12, 2016
 *
 */
public class InterfacePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private DefaultListModel<JavaElement> mInterfaceListModel;
	private JList<JavaElement> mInterfaceList;
	private DefaultListModel<String> mSelectedInterfaceListModel;
	private JList<String> mSelectedInterfaceList;
	private JPanel mSelectedInterfacePanel;
	private JLabel mSelectedInterfaceLabel;

	private List<JavaElement> mElements;

	public InterfacePanel() {
		init();
	}

	public void load(List<JavaElement> elements) {
		this.mElements = elements;
		mInterfaceListModel.clear();
		for (JavaElement javaElement : elements) {
			if (javaElement.isInterface())
				mInterfaceListModel.addElement(javaElement);
		}
	}

	private void findInterfaceUsages(JavaElement selectedClass) {
		mSelectedInterfaceListModel.clear();
		for (JavaElement javaElement : mElements) {
			String selectedClassName = selectedClass.getName();
			if (javaElement.getInterfaces().contains(selectedClassName)) {
				mSelectedInterfaceListModel.addElement("implemented by " + javaElement.getName());
			}
			else if (javaElement.getSuperClasses().contains(selectedClassName)) {
				mSelectedInterfaceListModel.addElement("extended by " + javaElement.getName());
			}
		}
		mSelectedInterfacePanel.setVisible(true);
		mSelectedInterfaceLabel.setText(selectedClass.getName());
	}

	private void init() {
		setLayout(new BorderLayout(0, 0));

		JPanel gridPanel = new JPanel();
		add(gridPanel, BorderLayout.CENTER);
		gridPanel.setLayout(new GridLayout(0, 2, 0, 0));

		mInterfaceListModel = new DefaultListModel<>();
		mInterfaceList = new JList<>(mInterfaceListModel);
		mInterfaceList.setCellRenderer(new JavaElementListCellRenderer());
		JPanel interfacePanel = createList(mInterfaceList, new JLabel("Select an interface to inspect"), new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = mInterfaceList.getSelectedIndex();
				JavaElement selectedClassObject = mInterfaceListModel.getElementAt(index);
				findInterfaceUsages(selectedClassObject);
			}
		});
		gridPanel.add(interfacePanel);

		mSelectedInterfaceLabel = new JLabel();
		mSelectedInterfaceListModel = new DefaultListModel<>();
		mSelectedInterfaceList = new JList<>(mSelectedInterfaceListModel);
		mSelectedInterfacePanel = createList(mSelectedInterfaceList, mSelectedInterfaceLabel, new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		gridPanel.add(mSelectedInterfacePanel);
		mSelectedInterfacePanel.setVisible(false);
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
		listFiles.setFont(new Font("Segoe UI", Font.PLAIN, 12));
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
