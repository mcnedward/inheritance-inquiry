package com.mcnedward.app.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import com.mcnedward.app.ui.cell_renderer.InheritanceListCellRenderer;
import com.mcnedward.app.ui.cell_renderer.InheritanceListModelItem;
import com.mcnedward.ii.element.JavaElement;
import com.mcnedward.ii.element.JavaProject;
import java.awt.Component;

/**
 * @author Edward - Jun 12, 2016
 *
 */
public class InheritancePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private DefaultListModel<InheritanceListModelItem> mInheritanceListModel;
	private JList<InheritanceListModelItem> mInheritanceList;
	private JPanel mSelectedInheritancePanel;
	private JLabel mSelectedInheritanceLabel;
	private JPanel mHeirarchyPanel;

	private JavaProject mProject;

	public InheritancePanel() {
		init();
	}

	public void load(JavaProject project) {
		mProject = project;
		mInheritanceListModel.clear();
		for (JavaElement javaElement : project.getAllElements()) {
			Stack<JavaElement> inheritanceTree = mProject.findDepthOfInheritanceTreeFor(javaElement);
			if (inheritanceTree.size() > 0)
				mInheritanceListModel.addElement(new InheritanceListModelItem(javaElement, inheritanceTree.size()));
		}
	}

	private void findInheritanceUsages(JavaElement selectedClass) {
		mSelectedInheritancePanel.setVisible(true);
		mSelectedInheritanceLabel.setText(selectedClass.getName());
		// Clear any old elements
		mHeirarchyPanel.removeAll();
		mHeirarchyPanel.validate();
		mHeirarchyPanel.repaint();

//		JLabel label = new JLabel(selectedClass.getName());
//		label.setAlignmentX(Component.CENTER_ALIGNMENT);
//		label.setBorder(new EmptyBorder(10, 0, 10, 0));
//		mHeirarchyPanel.add(label);

		Stack<JavaElement> classStack = mProject.findDepthOfInheritanceTreeFor(selectedClass);
		classStack.add(0, selectedClass);
		for (int i = 0; i < classStack.size(); i++) {
			JavaElement element = classStack.get(i);
			JLabel childLabel = new JLabel(element.getName());
			childLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			childLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
			mHeirarchyPanel.add(childLabel);
			if (i != classStack.size() - 1) {
				JLabel lineLabel = new JLabel("|");
				lineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				mHeirarchyPanel.add(lineLabel);
			}
		}
	}

	private void init() {
		setLayout(new BorderLayout(0, 0));

		JPanel gridPanel = new JPanel();
		add(gridPanel, BorderLayout.CENTER);
		gridPanel.setLayout(new GridLayout(0, 2, 0, 0));

		mInheritanceListModel = new DefaultListModel<>();
		mInheritanceList = new JList<>(mInheritanceListModel);
		mInheritanceList.setCellRenderer(new InheritanceListCellRenderer());
		JPanel inheritancePanel = createList(mInheritanceList, new JLabel("Select an item to inspect heirarchy"), new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = mInheritanceList.getSelectedIndex();
				JavaElement selectedClassObject = mInheritanceListModel.getElementAt(index).element;
				findInheritanceUsages(selectedClassObject);
			}
		});
		gridPanel.add(inheritancePanel);

		mSelectedInheritanceLabel = new JLabel();
		mSelectedInheritancePanel = new JPanel();
		mSelectedInheritancePanel.setLayout(new BorderLayout());
		mSelectedInheritancePanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		mSelectedInheritancePanel.add(mSelectedInheritanceLabel, BorderLayout.NORTH);

		mHeirarchyPanel = new JPanel();
		mHeirarchyPanel.setLayout(new BoxLayout(mHeirarchyPanel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(mHeirarchyPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView(mHeirarchyPanel);
		mSelectedInheritancePanel.add(scrollPane, BorderLayout.CENTER);

		gridPanel.add(mSelectedInheritancePanel);
		mSelectedInheritancePanel.setVisible(false);
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

		JPanel inheritancePanel = new JPanel();
		inheritancePanel.setLayout(new BorderLayout());
		inheritancePanel.setBorder(new EmptyBorder(0, 10, 0, 10));

		inheritancePanel.add(label, BorderLayout.NORTH);
		inheritancePanel.add(scrollPane, BorderLayout.CENTER);
		return inheritancePanel;
	}
}
