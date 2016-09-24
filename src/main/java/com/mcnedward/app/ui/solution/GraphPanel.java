package com.mcnedward.app.ui.solution;

import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * Created by Edward on 9/24/2016.
 */
public class GraphPanel {
    private JRadioButton mGenerateAll;
    private JRadioButton mSelect;
    private JPanel panel1;
    private JTree mProjecTree;
    private JPanel mOptionPanel;
    private JButton mGenerateButton;
    private DefaultMutableTreeNode mTreeRoot;

    public void update(JavaSolution solution) {
        mOptionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        mGenerateButton.addActionListener(e -> generateGraphs());
        setupRadioButtons();
    }

    private void generateGraphs() {
        IILogger.info(mProjecTree.getSelectionPath().toString());
    }

    private void setupRadioButtons() {
        ButtonGroup group = new ButtonGroup();
        group.add(mGenerateAll);
        group.add(mSelect);
    }

    private void setupProjectTree() {

    }

    private void createUIComponents() {
        mTreeRoot = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");

        mTreeRoot.add(vegetableNode);
        mTreeRoot.add(fruitNode);
        mProjecTree = new JTree(mTreeRoot);
    }
}
