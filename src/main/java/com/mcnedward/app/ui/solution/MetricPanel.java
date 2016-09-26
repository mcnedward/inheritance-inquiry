package com.mcnedward.app.ui.solution;

import com.mcnedward.app.ui.cellRenderer.MetricCellRenderer;
import com.mcnedward.app.ui.component.PlaceholderTextField;
import com.mcnedward.app.ui.dialog.ExportFileDialog;
import com.mcnedward.app.ui.main.MainPage;
import com.mcnedward.app.ui.main.ProgressCard;
import com.mcnedward.ii.builder.GraphBuilder;
import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.listener.GraphExportListener;
import com.mcnedward.ii.listener.GraphLoadListener;
import com.mcnedward.ii.service.graph.IGraphService;
import com.mcnedward.ii.service.graph.jung.JungGraph;
import com.mcnedward.ii.service.metric.element.Metric;
import com.mcnedward.ii.service.metric.element.MetricInfo;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Created by Edward on 9/23/2016.
 */
public class MetricPanel<T extends Metric> {

    private static final String GRAPH_CARD = "GraphPanel";
    private static final String GRAPH_PROGRESS_CARD = "GraphProgress";

    private JPanel mRoot;
    private JLabel mMetricName;
    private JLabel mMin;
    private JLabel mAverage;
    private JLabel mMax;
    private JPanel mMetricListPanel;
    private JList<T> mMetricList;
    private DefaultListModel<T> mCachedMetricListModel;
    private DefaultListModel<T> mMetricListModel;
    private JButton mBtnGenerate;
    private JTextField mTxtFilter;
    private JPanel mGraphPanel;
    private JCheckBox mUseFullName;
    private JPanel mTitlePanel;
    private JPanel mGraphCards;
    private ProgressCard mGraphProgress;
    private JPanel mGraphContainer;
    private JButton mBtnExport;
    private JRadioButton mRdDownloadAll;
    private JRadioButton mRdDownloadSelected;

    private JavaSolution mSolution;
    private GraphBuilder mGraphBuilder;
    private IGraphService mGraphService;
    private List<T> mMetrics;
    private Map<String, JungGraph> mGraphMap;
    private JungGraph mCurrentGraph;
    private ExportFileDialog mExportDialog;
    private boolean mMetricListCreated;
    private boolean mFilterFocused;

    public void update(JavaSolution solution, IGraphService graphService, MetricInfo metricInfo, List<T> metrics) {
        IILogger.info("Updating metric panel");
        mSolution = solution;
        mGraphBuilder = new GraphBuilder(graphLoadListener(), graphExportListener());
        mGraphService = graphService;
        updateMetricInfo(metricInfo);
        updateMetrics(metrics);
        generateGraphs();
    }

    private void updateMetricInfo(MetricInfo metricInfo) {
        mMetricName.setText(metricInfo.getMetricType().metricName);
        mMin.setText(String.valueOf(metricInfo.getMin()));
        mAverage.setText(String.valueOf((int) metricInfo.getAverage()));
        mMax.setText(String.valueOf(metricInfo.getMax()));
        mTitlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
    }

    private void updateMetrics(List<T> metrics) {
        mMetrics = metrics;
        checkMetricListCreation();
        if (mMetricListModel.size() > 0)
            mMetricListModel.clear();
        if (mCachedMetricListModel.size() > 0)
            mCachedMetricListModel.clear();
        for (T metric : metrics) {
            mMetricListModel.addElement(metric);
            mCachedMetricListModel.addElement(metric);
        }
    }

    private void generateGraphs() {
        showCard(GRAPH_PROGRESS_CARD);
        if (mGraphService == null) {
            mGraphProgress.error("No graphs for this metric.");
            return;
        }
        List<String> graphMetrics = new ArrayList<>();
        for (T metric : mMetrics)
            graphMetrics.add(metric.getFullyQualifiedName());
        if (mGraphMap == null)
            mGraphMap = new HashMap<>();
        else
            mGraphMap.clear();
        IILogger.info("Generating graphs for: %s", graphMetrics);
        Integer width = mGraphPanel.getWidth();
        Integer height = mGraphPanel.getHeight();
        mGraphBuilder.setupForBuild(mGraphService, mSolution, graphMetrics, width, height, mUseFullName.isSelected()).build();
    }
    private void updateGraph(JungGraph graph) {
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
        boolean downloadAll = mRdDownloadAll.isSelected();
        mExportDialog.setVisible(true);
        if (mExportDialog.isSuccessful()) {
            boolean usePackages = mExportDialog.usePackages();
            boolean useProjectName = mExportDialog.useProjectName();
            File directory = mExportDialog.getDirectory();

            Collection<JungGraph> graphs;
            if (downloadAll) {
                graphs = mGraphMap.values();
            } else {
                List<T> graphKeys = mMetricList.getSelectedValuesList();
                graphs = new ArrayList<>();
                for (T key : graphKeys)
                    graphs.add(mGraphMap.get(key.getFullyQualifiedName()));
            }
            showCard(GRAPH_PROGRESS_CARD);
            String projectName = useProjectName ? mSolution.getProjectName() : null;
            mGraphBuilder.setupForExport(mGraphService, graphs, directory, usePackages, projectName).build();
            IILogger.debug("Downloading %s %s graphs to %s. Use packages? %s", downloadAll ? "all" : "selected", graphs.size(), directory, usePackages);
        }
    }

    private void updateFilter(String text) {
        if (text.length() == 0) {
            mMetricList.setModel(mCachedMetricListModel);
        } else {
            mMetricListModel.clear();
            for (T metric : mMetrics) {
                if (metric.getElementName().toLowerCase().startsWith(text.toLowerCase())) {
                    mMetricListModel.addElement(metric);
                }
            }
            mMetricList.setModel(mMetricListModel);
        }
    }

    private void showCard(String card) {
        ((CardLayout) mGraphCards.getLayout()).show(mGraphCards, card);
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

        mBtnGenerate = new JButton("Generate");
        mBtnGenerate.addActionListener(e -> generateGraphs());

        mUseFullName = new JCheckBox("Use full name");
        mUseFullName.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        mRdDownloadAll = new JRadioButton("Export all");
        mRdDownloadAll.setToolTipText("Export graphs for all classes.");
        mRdDownloadAll.setSelected(true);
        group.add(mRdDownloadAll);
        mRdDownloadSelected = new JRadioButton("Export selected");
        mRdDownloadSelected.setToolTipText("Export graphs for only those classes that are selected in the list.");
        group.add(mRdDownloadSelected);

        mBtnExport = new JButton("Export");
        mBtnExport.addActionListener(e -> exportGraphs());
        mExportDialog = new ExportFileDialog(MainPage.PARENT_FRAME);
    }

    private void checkMetricListCreation() {
        if (!mMetricListCreated) {
            mMetricListCreated = true;
            mMetricListModel = new DefaultListModel<>();
            mCachedMetricListModel = new DefaultListModel<>();
            mMetricList = new JList<>(mMetricListModel);
            mMetricList.setCellRenderer(new MetricCellRenderer());
            mMetricList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            mMetricList.setVisibleRowCount(10);
            mMetricList.setSelectedIndex(0);
            mMetricList.setDragEnabled(true);
            mMetricList.addListSelectionListener(e -> {
                int index = mMetricList.getSelectedIndex();
                // Don't update here if the Filter text field is focused
                if (mFilterFocused || index == -1) return;
                T metric = mMetricList.getModel().getElementAt(index);
                JungGraph graph = mGraphMap.get(metric.getFullyQualifiedName());
                if (graph != null) {
                    updateGraph(graph);
                }
            });
            mMetricList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//        mDitMetricList.addMouseListener();
            Dimension d = mMetricListPanel.getPreferredSize();
            d.setSize(250, d.getHeight());
            mMetricListPanel.setPreferredSize(d);
            JScrollPane scrollPane = new JScrollPane(mMetricList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            mMetricListPanel.add(scrollPane);
        }
    }

    private GraphLoadListener graphLoadListener() {
        return new GraphLoadListener() {
            @Override
            public void onGraphsLoaded(List<JungGraph> graphs) {
                if (graphs.size() == 0) {
                    mGraphProgress.error("No graphs were built.");
                    return;
                }
                for (JungGraph graph : graphs) {
                    mGraphMap.put(graph.getFullyQualifiedElementName(), graph);
                }
                mMetricList.setSelectedIndex(0);
                updateGraph(graphs.get(0));
                showCard(GRAPH_CARD);
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

}
