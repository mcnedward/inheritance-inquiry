
package com.mcnedward.app.ui.form;

import com.mcnedward.app.InheritanceInquiry;
import com.mcnedward.app.ui.component.IIColorPicker;
import com.mcnedward.app.ui.dialog.ExportFileDialog;
import com.mcnedward.app.ui.listener.GraphPanelListener;
import com.mcnedward.app.ui.utils.ComponentUtils;
import com.mcnedward.ii.builder.GraphBuilder;
import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.exception.GraphBuildException;
import com.mcnedward.ii.listener.GraphExportListener;
import com.mcnedward.ii.listener.GraphLoadListener;
import com.mcnedward.ii.service.graph.IGraphService;
import com.mcnedward.ii.service.graph.element.GraphOptions;
import com.mcnedward.ii.service.graph.element.GraphShape;
import com.mcnedward.ii.service.graph.jung.JungGraph;
import com.mcnedward.ii.service.metric.element.Metric;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Created by Edward on 9/26/2016.
 */
public class GraphPanel {

    private static final String GRAPH_CARD = "GraphPanel";
    private static final String GRAPH_PROGRESS_CARD = "GraphProgress";

    private JPanel mRoot;
    private JPanel mGraphCards;
    private ProgressCard mGraphProgress;
    private JPanel mGraphContainer;
    private JButton mBtnExport;
    private JCheckBox mChkUseFullName;
    private JSpinner mSpnHDistance;
    private JSpinner mSpnVDistance;
    private JSpinner mSpnFontSize;
    private JButton mBtnLabelColor;
    private JButton mBtnFontColor;
    private JButton mBtnArrowColor;
    private JButton mBtnEdgeColor;
    private JPanel mOptions;
    private JComboBox<GraphShape> mCmbGraphShape;
    private JCheckBox mChkShowEdgeLabel;
    private JCheckBox mChkUpdateAll;
    private JPanel mGraphOptions;

    private JavaSolution mSolution;
    private IGraphService mGraphService;
    private GraphBuilder mGraphBuilder;
    private Collection<String> mFullyQualifiedNames;
    private GraphPanelListener mListener;
    private Map<String, JungGraph> mGraphMap;
    private JungGraph mCurrentGraph;
    private ExportFileDialog mExportDialog;
    private Color mFontColor, mLabelColor, mArrowColor, mEdgeColor;

    void update(JavaSolution solution, IGraphService graphService, Collection<String> fullyQualifiedNames, GraphPanelListener listener) {
        mSolution = solution;
        mGraphService = graphService;
        mFullyQualifiedNames = fullyQualifiedNames;
        mListener = listener;
        mGraphBuilder = new GraphBuilder(graphLoadListener(), graphExportListener());
        mGraphService = graphService;
        mGraphMap = null;   // Need to reset the graphs
        updateGraphs();
        checkResize();
    }

    private void updateHasGraphs(boolean hasGraphs) {
        if (!hasGraphs) {
            mGraphProgress.error("No graphs for this metric.");
        } else {
            showCard(GRAPH_CARD);
        }
        ComponentUtils.setEnabled(mGraphOptions, hasGraphs);
    }

    private void updateGraphs() {
        showCard(GRAPH_PROGRESS_CARD);
        if (mGraphService == null) {
            updateHasGraphs(false);
            return;
        }
        Integer xDistance = (Integer) mSpnHDistance.getValue();
        Integer yDistance = (Integer) mSpnVDistance.getValue();
        Integer fontSize = (Integer) mSpnFontSize.getValue();
        boolean useFullNames = mChkUseFullName.isSelected();
        if (mGraphMap == null) {
            // Called only when a new solution is loaded
            IILogger.info("Generating graphs for: %s", mFullyQualifiedNames);
            mGraphMap = new HashMap<>();
            mGraphBuilder.setupForBuild(mGraphService, new GraphOptions(mSolution, mFullyQualifiedNames, xDistance, yDistance, fontSize, useFullNames)).build();
        }
        else {
            GraphOptions options = new GraphOptions(xDistance, yDistance, fontSize, mFontColor, mLabelColor, mArrowColor, mEdgeColor, (GraphShape) mCmbGraphShape.getSelectedItem());
            options.setUseFullName(useFullNames);
            options.setShowEdgeLabel(mChkShowEdgeLabel.isSelected());

            if (!mChkUpdateAll.isSelected()) {
                IILogger.info("Updating single graph for: %s", mCurrentGraph.getFullyQualifiedElementName());
                updateGraph(mCurrentGraph, options);
            } else {
                IILogger.info("Updating all graphs");
                int i = 1;
                for (JungGraph graph : mGraphMap.values()) {
                    int progress = (int) (((double) i / mGraphMap.size()) * 100);
                    String message = String.format("Generating graphs [%s / %s]...", i, mGraphMap.size());
                    mGraphProgress.update(message, progress);
                    updateGraph(graph, options);
                    i++;
                }
            }
        }
        showCard(GRAPH_CARD);
    }

    private void updateGraph(JungGraph graph, GraphOptions options) {
        try {
            graph.update(options);
            updateGraph(graph);
        } catch (GraphBuildException e) {
            IILogger.error(e);
        }
    }

    void updateGraph(String fullyQualifiedName) {
        updateGraph(mGraphMap.get(fullyQualifiedName));
    }

    void updateGraph(JungGraph graph) {
        if (graph == null) {
            IILogger.error(new IllegalStateException("Graph was null."));
            return;
        }
        mCurrentGraph = graph;
        if (mGraphContainer.getComponents().length > 0) {
            mGraphContainer.remove(0);
        }
        mGraphContainer.add(graph.getGraphPane(), BorderLayout.CENTER);
        mGraphContainer.revalidate();
        mGraphContainer.repaint();
        IILogger.debug("Loading graph: " + graph.getFullyQualifiedElementName());
    }

    private void exportGraphs() {
        mExportDialog.setVisible(true);
        if (mExportDialog.isSuccessful()) {
            boolean exportAll = mExportDialog.exportAll();
            boolean usePackages = mExportDialog.usePackages();
            boolean useProjectName = mExportDialog.useProjectName();
            File directory = mExportDialog.getDirectory();

            Collection<JungGraph> graphs = mListener.requestGraphs(mGraphMap, exportAll);
            showCard(GRAPH_PROGRESS_CARD);
            String projectName = useProjectName ? mSolution.getProjectName() : null;
            mGraphBuilder.setupForExport(mGraphService, graphs, new GraphOptions(directory, projectName, usePackages)).build();
            IILogger.debug("Downloading %s %s graphs to %s. Use packages? %s", exportAll ? "all" : "selected", graphs.size(), directory, usePackages);
        }
    }

    protected Collection<JungGraph> getGraphs() {
        return mGraphMap.values();
    }

    private void showCard(String card) {
        ((CardLayout) mGraphCards.getLayout()).show(mGraphCards, card);
    }

    private GraphLoadListener graphLoadListener() {
        return new GraphLoadListener() {
            @Override
            public void onGraphsLoaded(List<JungGraph> graphs) {
                if (graphs.size() == 0) {
                    updateHasGraphs(false);
                    return;
                }
                updateHasGraphs(true);
                for (JungGraph graph : graphs) {
                    mGraphMap.put(graph.getFullyQualifiedElementName(), graph);
                }
                mListener.onGraphsLoaded(graphs.get(0));
                updateGraph(graphs.get(0));
            }

            @Override
            public void onProgressChange(String message, int progress) {
                mGraphProgress.update(message, progress);
            }

            @Override
            public void onBuildError(String message, Exception exception) {
                JOptionPane.showMessageDialog(mRoot, message, "Graph Build Error", JOptionPane.ERROR_MESSAGE);
                IILogger.error(exception);
            }
        };
    }

    private GraphExportListener graphExportListener() {
        return new GraphExportListener() {
            @Override
            public void onGraphsExport() {
                showCard(GRAPH_CARD);
            }

            @Override
            public void onProgressChange(String message, int progress) {
                mGraphProgress.update(message, progress);
            }

            @Override
            public void onBuildError(String message, Exception exception) {
                JOptionPane.showMessageDialog(mRoot, message, "Graph Export Error", JOptionPane.ERROR_MESSAGE);
                IILogger.error(exception);
            }
        };
    }

    private void createUIComponents() {
        mChkUseFullName = new JCheckBox("Use full name");
        mChkUseFullName.setSelected(true);
        mChkUseFullName.addActionListener(e -> updateGraphs());

        mChkShowEdgeLabel = new JCheckBox("Edge label");
        mChkShowEdgeLabel.addActionListener(e -> updateGraphs());

        mBtnExport = new JButton("Export");
        mBtnExport.addActionListener(e -> exportGraphs());
        mExportDialog = new ExportFileDialog(InheritanceInquiry.PARENT_FRAME);

        mSpnHDistance = new JSpinner(new SpinnerNumberModel(GraphOptions.DEFAULT_X_DIST, 0, 600, 10));
        mSpnVDistance = new JSpinner(new SpinnerNumberModel(GraphOptions.DEFAULT_Y_DIST, 0, 600, 10));
        mSpnFontSize = new JSpinner(new SpinnerNumberModel(GraphOptions.DEFAULT_FONT_SIZE, 6, 72, 2));
        mSpnHDistance.addChangeListener(e -> updateGraphs());
        mSpnVDistance.addChangeListener(e -> updateGraphs());
        mSpnFontSize.addChangeListener(e -> updateGraphs());

        mBtnFontColor = new IIColorPicker(GraphOptions.DEFAULT_FONT_COLOR);
        ((IIColorPicker) mBtnFontColor).addColorChangedListener(newColor -> {mFontColor = newColor; updateGraphs();});
        mBtnLabelColor = new IIColorPicker(GraphOptions.DEFAULT_LABEL_COLOR);
        ((IIColorPicker) mBtnLabelColor).addColorChangedListener(newColor -> {mLabelColor = newColor; updateGraphs();});
        mBtnArrowColor = new IIColorPicker(GraphOptions.DEFAULT_ARROW_COLOR);
        ((IIColorPicker) mBtnArrowColor).addColorChangedListener(newColor -> {mArrowColor = newColor; updateGraphs();});
        mBtnEdgeColor = new IIColorPicker(GraphOptions.DEFAULT_EDGE_COLOR);
        ((IIColorPicker) mBtnEdgeColor).addColorChangedListener(newColor -> {mEdgeColor = newColor; updateGraphs();});

        ComboBoxModel<GraphShape> comboBoxModel = new DefaultComboBoxModel<>(GraphShape.values());
        mCmbGraphShape = new JComboBox<>(comboBoxModel);
        mCmbGraphShape.addActionListener(e -> updateGraphs());
    }

    private void checkResize() {
        ComponentUtils.changeFont(mOptions, new Font(InheritanceInquiry.FONT_NAME, Font.PLAIN, 12));
    }
}
