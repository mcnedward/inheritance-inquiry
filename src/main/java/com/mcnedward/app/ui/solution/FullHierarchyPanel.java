package com.mcnedward.app.ui.solution;

import com.mcnedward.app.ui.component.IITreeNode;
import com.mcnedward.app.ui.component.PlaceholderTextField;
import com.mcnedward.app.ui.listener.GraphPanelListener;
import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.service.graph.IGraphService;
import com.mcnedward.ii.service.graph.element.FullHierarchy;
import com.mcnedward.ii.service.graph.jung.JungGraph;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.*;

/**
 * Created by Edward on 9/24/2016.
 */
public class FullHierarchyPanel implements GraphPanelListener {

    private JPanel mRoot;
    private JTree mProjectTree;
    private JPanel mTreePanel;
    private JTextField mTxtFilter;
    private GraphPanel mGraphPanel;

    private TreeModel mTreeModel;
    private IITreeNode mTreeRoot;
    private Map<String, IITreeNode> mPackageMap;
    private boolean mTreeCreated;
    private boolean mFilterFocused;

    void update(JavaSolution solution, IGraphService graphService) {
        if (mPackageMap == null)
            mPackageMap = new HashMap<>();
        checkTreeCreated();
        try {
            mTreeRoot = createProjectTree(solution);
            mTreeModel = new DefaultTreeModel(mTreeRoot);
            mProjectTree.setModel(mTreeModel);
        } catch (Exception e) {
            IILogger.error(e);
        }
        Collection<String> fullyQualifiedNames = new ArrayList<>();
        for (FullHierarchy f : solution.getFullHierarchies())
            fullyQualifiedNames.add(f.getFullElementName());
        mGraphPanel.update(solution, graphService, fullyQualifiedNames, this);
    }

    private IITreeNode createProjectTree(JavaSolution solution) {
        IITreeNode root = new IITreeNode(solution.getProjectName());
        for (String packageName : solution.getFullyQualifiedElementNames()) {
            String[] packages = packageName.split("\\.");

            if (packages.length > 0) {
                // There are packages
                IITreeNode node = root; // Start at the root
                String elementName = packages[packages.length - 1];
                // Create nodes for each package, if they don't already exist
                for (int i = 0; i < packages.length; i++) {
                    if (i == packages.length - 1) break;
                    String p = packages[i];
                    node = checkNodes(p, node); // Check the node, creating a new one if necessary
                }
                if (node == null) {
                    throw new IllegalStateException("FullHierarchyPanel project tree has a null node...");
                }
                node.add(new IITreeNode(elementName, packageName));
            } else {
                // There are no packages
                IITreeNode node = new IITreeNode(packageName);
                root.add(node);
            }
        }
        return root;
    }

    private IITreeNode checkNodes(String packageName, IITreeNode root) {
        IITreeNode p = mPackageMap.get(packageName);
        if (p == null) {
            p = new IITreeNode(packageName);
            root.add(p);
            mPackageMap.put(packageName, p);
        }
        return p;
    }

    private void checkTreeCreated() {
        if (!mTreeCreated) {
            mTreeCreated = true;
            mProjectTree = new JTree();
            mProjectTree.setFont(mProjectTree.getFont().deriveFont(16f));
            mProjectTree.setRowHeight(18);
            mProjectTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
            mProjectTree.addTreeSelectionListener(e -> {
                TreePath newTreePath = e.getNewLeadSelectionPath();
                moveTreeTo(newTreePath);
            });

            Dimension d = mTreePanel.getPreferredSize();
            d.setSize(300, d.getHeight());
            mTreePanel.setPreferredSize(d);
            JScrollPane scrollPane = new JScrollPane(mProjectTree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            mTreePanel.add(scrollPane);
        }
    }

    private void updateFilter(String text) {
        TreePath treePath = null;
        @SuppressWarnings("unchecked")
        Enumeration<DefaultMutableTreeNode> e = mTreeRoot.depthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = e.nextElement();
            if (node.toString().toLowerCase().startsWith(text.toLowerCase())) {
                treePath = new TreePath(node.getPath());
                break;
            }
        }
        moveTreeTo(treePath);
    }

    private void moveTreeTo(TreePath treePath) {
        if (treePath != null) {
            mProjectTree.setSelectionPath(treePath);
            mProjectTree.scrollPathToVisible(treePath);
            IITreeNode lastPathComponent = (IITreeNode) treePath.getLastPathComponent();
            if (mTreeModel.isLeaf(lastPathComponent))
                mGraphPanel.updateGraph(lastPathComponent.getFullyQualifiedElementName());
        }
    }

    private void createUIComponents() {
        mTxtFilter = new PlaceholderTextField("", "Filter classes");
        mTxtFilter.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
            }

            public void removeUpdate(DocumentEvent e) {
                updateFilter(mTxtFilter.getText());
            }

            public void insertUpdate(DocumentEvent e) {
                updateFilter(mTxtFilter.getText());
            }
        });
        mTxtFilter.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                mFilterFocused = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                mFilterFocused = false;
            }
        });
    }

    @Override
    public void onGraphsLoaded(JungGraph firstGraph) {
        updateFilter(firstGraph.getElementName());
    }

    @Override
    public Collection<JungGraph> requestGraphs(Map<String, JungGraph> graphMap, boolean downloadAll) {
        Collection<JungGraph> graphs;
        if (downloadAll) {
            graphs = graphMap.values();
        } else {
            graphs = new ArrayList<>();
            @SuppressWarnings("unchecked")
            Enumeration<IITreeNode> e = mTreeRoot.depthFirstEnumeration();
            while (e.hasMoreElements()) {
                IITreeNode node = e.nextElement();
                if (node.isLeaf()) {
                    JungGraph graph = graphMap.get(node.getFullyQualifiedElementName());
                    if (graph != null) {
                        graphs.add(graph);
                    }
                }
            }
        }
        return graphs;
    }
}
