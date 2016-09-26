package com.mcnedward.app.ui.component;

import com.mcnedward.ii.service.graph.jung.JungGraph;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by Edward on 9/26/2016.
 */
public class IITreeNode extends DefaultMutableTreeNode {

    private String mFullyQualifiedElementName;
    private JungGraph mGraph;

    public IITreeNode(String nodeName) {
        super(nodeName);
    }

    public IITreeNode(String nodeName, String fullyQualifiedElementName) {
        super(nodeName);
        mFullyQualifiedElementName = fullyQualifiedElementName;
    }

    public String getFullyQualifiedElementName() {
        return mFullyQualifiedElementName;
    }

    public void setGraph(JungGraph graph) {
        mGraph = graph;
    }

    public JungGraph getGraph() {
        return mGraph;
    }
}
