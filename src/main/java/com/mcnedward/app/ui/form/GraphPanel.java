
package com.mcnedward.app.ui.form;

import com.mcnedward.app.InheritanceInquiry;
import com.mcnedward.app.ui.component.IIColorPicker;
import com.mcnedward.app.ui.dialog.results.ExportGraphResults;
import com.mcnedward.app.ui.listener.GraphPanelListener;
import com.mcnedward.app.utils.ComponentUtils;
import com.mcnedward.app.utils.Constants;
import com.mcnedward.app.utils.DialogUtils;
import com.mcnedward.app.utils.PrefUtils;
import com.mcnedward.ii.builder.GraphBuilder;
import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.exception.GraphBuildException;
import com.mcnedward.ii.listener.GraphExportListener;
import com.mcnedward.ii.listener.GraphLoadListener;
import com.mcnedward.ii.service.graph.IGraphService;
import com.mcnedward.ii.service.graph.element.GraphOptions;
import com.mcnedward.ii.service.graph.element.GraphShape;
import com.mcnedward.ii.service.graph.jung.JungGraph;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        GraphOptions options = new GraphOptions(xDistance, yDistance, fontSize, mFontColor, mLabelColor, mArrowColor, mEdgeColor, (GraphShape) mCmbGraphShape.getSelectedItem());
        options.setUseFullName(useFullNames);
        options.setShowEdgeLabel(mChkShowEdgeLabel.isSelected());
        if (mGraphMap == null) {
            // Called only when a new solution is loaded
            IILogger.info("Generating graphs for: %s", mFullyQualifiedNames);
            mGraphMap = new HashMap<>();
            options.setSolution(mSolution);
            options.setFullyQualifiedNames(mFullyQualifiedNames);
            mGraphBuilder.setupForBuild(mGraphService, options).build();
        }
        else {
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
        if (DialogUtils.openExportGraphDialogForSuccess()) {
            ExportGraphResults results = DialogUtils.getExportGraphDialogResults();
            boolean exportAll = results.exportAll();
            boolean usePackages = results.usePackages();
            boolean useProjectName = results.useProjectName();
            File directory = results.getDirectory();

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
                DialogUtils.openMessageDialog( message, "Graph Build Error");
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
                DialogUtils.openMessageDialog( message, "Graph Export Error");
                IILogger.error(exception);
            }
        };
    }

    private void saveInt(String name, int value) {
        PrefUtils.putPreference(name, value, GraphPanel.class);
    }

    private void saveBool(String checkName, boolean value) {
        PrefUtils.putPreference(checkName, value, GraphPanel.class);
    }

    private void saveColor(String name, Color color) {
        PrefUtils.putPreference(name, color, GraphPanel.class);
    }

    private int getPreferenceInt(String name) {
        return PrefUtils.getPreferenceInt(name, GraphPanel.class);
    }

    private boolean getPreferenceBool(String name) {
        return PrefUtils.getPreferenceBool(name, GraphPanel.class);
    }

    private Color getPreferenceColor(String name) {
        return PrefUtils.getPreferenceColor(name, GraphPanel.class);
    }

    private void createUIComponents() {
        mBtnExport = new JButton("Export");
        mBtnExport.addActionListener(e -> exportGraphs());

        mChkUseFullName = new JCheckBox("Use full name");
        mChkUseFullName.addActionListener(e -> { updateGraphs(); saveBool(Constants.USE_FULL_NAME, mChkUseFullName.isSelected()); });
        mChkUseFullName.setSelected(getPreferenceBool(Constants.USE_FULL_NAME));

        mChkUpdateAll = new JCheckBox("Update all");
        mChkUpdateAll.addActionListener(e -> { updateGraphs(); saveBool(Constants.UPDATE_ALL, mChkUpdateAll.isSelected()); });
        mChkUpdateAll.setSelected(getPreferenceBool(Constants.UPDATE_ALL));

        mChkShowEdgeLabel = new JCheckBox("Edge label");
        mChkShowEdgeLabel.addActionListener(e -> { updateGraphs(); saveBool(Constants.EDGE_LABEL, mChkShowEdgeLabel.isSelected()); });
        mChkShowEdgeLabel.setSelected(getPreferenceBool(Constants.EDGE_LABEL));

        mSpnHDistance = new JSpinner(new SpinnerNumberModel(getPreferenceInt(Constants.H_DISTANCE), 0, 600, 10));
        mSpnVDistance = new JSpinner(new SpinnerNumberModel(getPreferenceInt(Constants.V_DISTANCE), 0, 600, 10));
        mSpnFontSize = new JSpinner(new SpinnerNumberModel(getPreferenceInt(Constants.FONT_SIZE), 6, 72, 2));
        mSpnHDistance.addChangeListener(e -> {updateGraphs(); saveInt(Constants.H_DISTANCE, (Integer) mSpnHDistance.getValue()); });
        mSpnVDistance.addChangeListener(e -> {updateGraphs(); saveInt(Constants.V_DISTANCE, (Integer) mSpnVDistance.getValue()); });
        mSpnFontSize.addChangeListener(e -> {updateGraphs(); saveInt(Constants.FONT_SIZE, (Integer) mSpnFontSize.getValue()); });

        mFontColor = getPreferenceColor(Constants.FONT_COLOR);
        mBtnFontColor = new IIColorPicker(mFontColor);
        ((IIColorPicker) mBtnFontColor).addColorChangedListener(newColor -> {mFontColor = newColor; saveColor(Constants.FONT_COLOR, newColor); updateGraphs();});

        mLabelColor = getPreferenceColor(Constants.LABEL_COLOR);
        mBtnLabelColor = new IIColorPicker(mLabelColor);
        ((IIColorPicker) mBtnLabelColor).addColorChangedListener(newColor -> {mLabelColor = newColor; saveColor(Constants.LABEL_COLOR, newColor); updateGraphs();});

        mArrowColor = getPreferenceColor(Constants.ARROW_COLOR);
        mBtnArrowColor = new IIColorPicker(mArrowColor);
        ((IIColorPicker) mBtnArrowColor).addColorChangedListener(newColor -> {mArrowColor = newColor; saveColor(Constants.ARROW_COLOR, newColor); updateGraphs();});

//        mEdgeColor = getPreferenceColor(Constants.EDGE_COLOR);
//        mBtnEdgeColor = new IIColorPicker(mEdgeColor);
//        ((IIColorPicker) mBtnEdgeColor).addColorChangedListener(newColor -> {mEdgeColor = newColor; saveColor(Constants.EDGE_COLOR, newColor).updateGraphs();});

        ComboBoxModel<GraphShape> comboBoxModel = new DefaultComboBoxModel<>(GraphShape.values());
        mCmbGraphShape = new JComboBox<>(comboBoxModel);
        mCmbGraphShape.setSelectedIndex(getPreferenceInt(Constants.GRAPH_SHAPE));
        mCmbGraphShape.addActionListener(e -> { updateGraphs(); saveInt(Constants.GRAPH_SHAPE, mCmbGraphShape.getSelectedIndex()); });
    }

    private void checkResize() {
        ComponentUtils.changeFont(mOptions, new Font(InheritanceInquiry.FONT_NAME, Font.PLAIN, 12));
    }
}
