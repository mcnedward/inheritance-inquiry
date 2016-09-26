package com.mcnedward.app.ui.solution;

import com.mcnedward.app.ui.dialog.ExportFileDialog;
import com.mcnedward.app.ui.listener.GraphPanelListener;
import com.mcnedward.app.ui.main.MainPage;
import com.mcnedward.app.ui.main.ProgressCard;
import com.mcnedward.ii.builder.GraphBuilder;
import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.listener.GraphExportListener;
import com.mcnedward.ii.listener.GraphLoadListener;
import com.mcnedward.ii.service.graph.IGraphService;
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
    private JPanel mGraphPanel;
    private JRadioButton mRdExportAll;
    private JRadioButton mRdExportSelected;
    private JButton mBtnExport;
    private JCheckBox mUseFullName;
    private JButton mBtnGenerate;

    private JavaSolution mSolution;
    private IGraphService mGraphService;
    private GraphBuilder mGraphBuilder;
    private Collection<String> mFullyQualifiedNames;
    private GraphPanelListener mListener;
    private Map<String, JungGraph> mGraphMap;
    private JungGraph mCurrentGraph;
    private ExportFileDialog mExportDialog;

    void update(JavaSolution solution, IGraphService graphService, Collection<String> fullyQualifiedNames, GraphPanelListener listener) {
        mSolution = solution;
        mGraphService = graphService;
        mFullyQualifiedNames = fullyQualifiedNames;
        mListener = listener;
        mGraphBuilder = new GraphBuilder(graphLoadListener(), graphExportListener());
        mGraphService = graphService;
        generateGraphs();
    }

    private void updateHasGraphs(boolean hasGraphs) {
        if (!hasGraphs) {
            mGraphProgress.error("No graphs for this metric.");
        } else {
            showCard(GRAPH_CARD);
        }
        mBtnExport.setEnabled(hasGraphs);
        mBtnGenerate.setEnabled(hasGraphs);
        mRdExportAll.setEnabled(hasGraphs);
        mRdExportSelected.setEnabled(hasGraphs);
        mUseFullName.setEnabled(hasGraphs);
    }

    private void generateGraphs() {
        showCard(GRAPH_PROGRESS_CARD);
        if (mGraphService == null) {
            updateHasGraphs(false);
            return;
        }
        if (mGraphMap == null)
            mGraphMap = new HashMap<>();
        else
            mGraphMap.clear();
        IILogger.info("Generating graphs for: %s", mFullyQualifiedNames);
        Integer width = mGraphPanel.getWidth();
        Integer height = mGraphPanel.getHeight();
        mGraphBuilder.setupForBuild(mGraphService, mSolution, mFullyQualifiedNames, width, height, mUseFullName.isSelected()).build();
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
        if (mGraphPanel.getComponents().length > 0) {
            mGraphPanel.remove(0);
        }
        mGraphPanel.add(graph.getGraphPane(), BorderLayout.CENTER);
        mGraphPanel.revalidate();
        mGraphPanel.repaint();
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
        mBtnGenerate = new JButton("Generate");
        mBtnGenerate.addActionListener(e -> generateGraphs());

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
    }
}
