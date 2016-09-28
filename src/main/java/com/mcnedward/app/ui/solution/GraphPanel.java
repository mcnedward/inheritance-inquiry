package com.mcnedward.app.ui.solution;

import com.mcnedward.app.InheritanceInquiry;
import com.mcnedward.app.ui.component.IIColorPicker;
import com.mcnedward.app.ui.dialog.ExportFileDialog;
import com.mcnedward.app.ui.listener.GraphPanelListener;
import com.mcnedward.app.ui.main.MainPage;
import com.mcnedward.app.ui.main.ProgressCard;
import com.mcnedward.app.ui.utils.FontUtils;
import com.mcnedward.ii.builder.GraphBuilder;
import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.exception.GraphBuildException;
import com.mcnedward.ii.listener.GraphExportListener;
import com.mcnedward.ii.listener.GraphLoadListener;
import com.mcnedward.ii.service.graph.IGraphService;
import com.mcnedward.ii.service.graph.element.GraphOptions;
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
public class GraphPanel<T extends Metric> {

    private static final String GRAPH_CARD = "GraphPanel";
    private static final String GRAPH_PROGRESS_CARD = "GraphProgress";

    private JPanel mRoot;
    private JPanel mGraphCards;
    private ProgressCard mGraphProgress;
    private JPanel mGraphContainer;
    private JRadioButton mRdExportAll;
    private JRadioButton mRdExportSelected;
    private JButton mBtnExport;
    private JCheckBox mUseFullName;
    private JButton mBtnUpdate;
    private JSpinner mSpnHDistance;
    private JSpinner mSpnVDistance;
    private JSpinner mSpnFontSize;
    private JButton mBtnLabelColor;
    private JButton mBtnFontColor;
    private JButton mBtnArrowColor;
    private JButton mBtnEdgeColor;
    private JPanel mExportOptions;
    private JPanel mUpdateOptions;

    private JavaSolution mSolution;
    private IGraphService mGraphService;
    private GraphBuilder mGraphBuilder;
    private Collection<String> mFullyQualifiedNames;
    private GraphPanelListener mListener;
    private Map<String, JungGraph> mGraphMap;
    private JungGraph mCurrentGraph;
    private ExportFileDialog mExportDialog;
    private Color mLabelColor, mFontColor, mArrowColor, mEdgeColor;

    void update(JavaSolution solution, IGraphService graphService, Collection<String> fullyQualifiedNames, GraphPanelListener listener) {
        mSolution = solution;
        mGraphService = graphService;
        mFullyQualifiedNames = fullyQualifiedNames;
        mListener = listener;
        mGraphBuilder = new GraphBuilder(graphLoadListener(), graphExportListener());
        mGraphService = graphService;
        updateGraphs();
        checkResize();
    }

    private void updateHasGraphs(boolean hasGraphs) {
        if (!hasGraphs) {
            mGraphProgress.error("No graphs for this metric.");
        } else {
            showCard(GRAPH_CARD);
        }
        mBtnExport.setEnabled(hasGraphs);
        mBtnUpdate.setEnabled(hasGraphs);
        mRdExportAll.setEnabled(hasGraphs);
        mRdExportSelected.setEnabled(hasGraphs);
        mUseFullName.setEnabled(hasGraphs);
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
        if (mGraphMap == null) {
            IILogger.info("Generating graphs for: %s", mFullyQualifiedNames);
            mGraphMap = new HashMap<>();
            mGraphBuilder.setupForBuild(mGraphService, new GraphOptions(mSolution, mFullyQualifiedNames, xDistance, yDistance, fontSize, mUseFullName.isSelected())).build();
        }
        else {
            IILogger.info("Updating graphs for: %s", mFullyQualifiedNames);
            int i = 1;
            for (JungGraph graph : mGraphMap.values()) {
                int progress = (int) (((double) i / mGraphMap.size()) * 100);
                String message = String.format("Generating graphs [%s / %s]...", i, mGraphMap.size());
                mGraphProgress.update(message, progress);
                try {
                    GraphOptions options = new GraphOptions(xDistance, yDistance, fontSize, mLabelColor, mFontColor, mArrowColor, mEdgeColor);
                    graph.update(options);
                    updateGraph(graph);
                } catch (GraphBuildException e) {
                    IILogger.error(e);
                }
                i++;
            }
            showCard(GRAPH_CARD);
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
        boolean downloadAll = mRdExportAll.isSelected();
        mExportDialog.setVisible(true);
        if (mExportDialog.isSuccessful()) {
            boolean usePackages = mExportDialog.usePackages();
            boolean useProjectName = mExportDialog.useProjectName();
            File directory = mExportDialog.getDirectory();

            Collection<JungGraph> graphs = mListener.requestGraphs(mGraphMap, downloadAll);
            showCard(GRAPH_PROGRESS_CARD);
            String projectName = useProjectName ? mSolution.getProjectName() : null;
            mGraphBuilder.setupForExport(mGraphService, graphs, directory, usePackages, projectName).build();
            IILogger.debug("Downloading %s %s graphs to %s. Use packages? %s", downloadAll ? "all" : "selected", graphs.size(), directory, usePackages);
        }
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
        mBtnUpdate = new JButton("Generate");
        mBtnUpdate.addActionListener(e -> updateGraphs());

        mUseFullName = new JCheckBox("Use full name");
        mUseFullName.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        mRdExportAll = new JRadioButton("Export all");
        mRdExportAll.setToolTipText("Export graphs for all classes.");
        mRdExportAll.setSelected(true);
        group.add(mRdExportAll);
        mRdExportSelected = new JRadioButton("Export selected");
        mRdExportSelected.setToolTipText("Export graphs for only those classes that are selected in the list.");
        group.add(mRdExportSelected);

        mBtnExport = new JButton("Export");
        mBtnExport.addActionListener(e -> exportGraphs());
        mExportDialog = new ExportFileDialog(MainPage.PARENT_FRAME);

        mSpnHDistance = new JSpinner(new SpinnerNumberModel(GraphOptions.DEFAULT_X_DIST, 0, 500, 10));
        mSpnVDistance = new JSpinner(new SpinnerNumberModel(GraphOptions.DEFAULT_Y_DIST, 0, 500, 10));
        mSpnFontSize = new JSpinner(new SpinnerNumberModel(GraphOptions.DEFAULT_FONT_SIZE, 6, 72, 2));

        mBtnLabelColor = new IIColorPicker(GraphOptions.DEFAULT_LABEL_COLOR);
        ((IIColorPicker) mBtnLabelColor).addColorChangedListener(newColor -> mLabelColor = newColor);
        mBtnFontColor = new IIColorPicker(GraphOptions.DEFAULT_FONT_COLOR);
        ((IIColorPicker) mBtnFontColor).addColorChangedListener(newColor -> mFontColor = newColor);
        mBtnArrowColor = new IIColorPicker(GraphOptions.DEFAULT_ARROW_COLOR);
        ((IIColorPicker) mBtnArrowColor).addColorChangedListener(newColor -> mArrowColor = newColor);
        mBtnEdgeColor = new IIColorPicker(GraphOptions.DEFAULT_EDGE_COLOR);
        ((IIColorPicker) mBtnEdgeColor).addColorChangedListener(newColor -> mEdgeColor = newColor);
    }

    private void checkResize() {
        FontUtils.changeFont(mExportOptions, new Font(InheritanceInquiry.FONT_NAME, Font.PLAIN, 12));
        FontUtils.changeFont(mUpdateOptions, new Font(InheritanceInquiry.FONT_NAME, Font.PLAIN, 12));
    }
}
