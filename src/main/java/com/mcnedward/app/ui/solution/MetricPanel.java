package com.mcnedward.app.ui.solution;

import com.mcnedward.app.ui.PlaceholderTextField;
import com.mcnedward.app.ui.cellRenderer.MetricCellRenderer;
import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.exception.GraphBuildException;
import com.mcnedward.ii.service.graph.IGraphService;
import com.mcnedward.ii.service.graph.jung.JungGraph;
import com.mcnedward.ii.service.metric.element.Metric;
import com.mcnedward.ii.service.metric.element.MetricInfo;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Edward on 9/23/2016.
 */
public class MetricPanel<T extends Metric> {

    private JPanel mRoot;
    private JLabel mMetricName;
    private JLabel mMin;
    private JLabel mAverage;
    private JLabel mMax;
    private JPanel mMetricListPanel;
    private JList<T> mMetricList;
    private DefaultListModel<T> mCachedMetricListModel;
    private DefaultListModel<T> mMetricListModel;
    private JRadioButton mGenerateAll;
    private JRadioButton mGenerateSelected;
    private JButton mBtnGenerate;
    private JTextField mTxtFilter;
    private JPanel mGraphPanel;
    private JCheckBox mUseFullName;
    private JButton mBtnZoomIn;
    private JButton mBtnZoomOut;
    private JPanel mTitlePanel;

    private JavaSolution mSolution;
    private IGraphService mGraphService;
    private List<T> mMetrics;
    private Map<String, JungGraph> mGraphMap;
    private JungGraph mCurrentGraph;

    public void update(JavaSolution solution, IGraphService graphService, MetricInfo metricInfo, List<T> metrics) {
        IILogger.info("Updating metric panel");
        mSolution = solution;
        mGraphService = graphService;
        updateMetricInfo(metricInfo);
        updateMetrics(metrics);

        mBtnGenerate.addActionListener(e -> generateGraphs());
        mBtnZoomIn.addActionListener(e -> zoomIn());
        mBtnZoomOut.addActionListener(e -> zoomOut());
        setupRadioButtons();
    }

    private void updateMetricInfo(MetricInfo metricInfo) {
        mMetricName.setText(metricInfo.getMetricType().metricName);
        mMin.setText(String.valueOf(metricInfo.getMin()));
        mAverage.setText(String.valueOf((int) metricInfo.getAverage()));
        mMax.setText(String.valueOf(metricInfo.getMax()));

        mTitlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
    }

    private void updateMetrics(List<T> metrics) {
        mMetricListModel = new DefaultListModel<>();
        mCachedMetricListModel = new DefaultListModel<T>();
        mMetrics = metrics;

        for (T metric : metrics) {
            mMetricListModel.addElement(metric);
            mCachedMetricListModel.addElement(metric);
        }

        mMetricList = new JList<>(mMetricListModel);
        mMetricList.setCellRenderer(new MetricCellRenderer());
        mMetricList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mMetricList.setVisibleRowCount(10);
        mMetricList.setSelectedIndex(0);
        mMetricList.setDragEnabled(true);
        mMetricList.addMouseListener(listMouseListener());
        mMetricList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//        mDitMetricList.addMouseListener();

        // Set the size of the list panel
        Dimension d = mMetricListPanel.getPreferredSize();
        d.setSize(250, d.getHeight());
        mMetricListPanel.setPreferredSize(d);

        JScrollPane scrollPane = new JScrollPane(mMetricList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mMetricListPanel.add(scrollPane);
    }

    private void setupRadioButtons() {
        ButtonGroup group = new ButtonGroup();
        group.add(mGenerateAll);
        group.add(mGenerateSelected);
        mGenerateSelected.setSelected(true);
        mUseFullName.setSelected(true);
    }

    private void generateGraphs() {
        if (mGraphService == null) {
            JOptionPane.showMessageDialog(null, "No graphs for this metric.", "No Graphs", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        List<String> selectedMetrics = new ArrayList<>();
        if (mGenerateAll.isSelected()) {
            for (int i = 0; i < mMetricListModel.size(); i++) {
                selectedMetrics.add(mMetricListModel.getElementAt(i).fullyQualifiedName);
            }
        } else {
            List<T> metrics = mMetricList.getSelectedValuesList();
            for (T metric : metrics)
                selectedMetrics.add(metric.fullyQualifiedName);
        }
        if (mGraphMap == null)
            mGraphMap = new HashMap<>();
        else
            mGraphMap.clear();
        try {
            IILogger.info("Generating graphs for: %s", selectedMetrics);
            Integer width = mGraphPanel.getWidth();
            Integer height = mGraphPanel.getHeight();

            List<JungGraph> graphs = mGraphService.buildHierarchyGraphs(mSolution, selectedMetrics, width, height, mUseFullName.isSelected());
            if (graphs.size() == 0) {
                throw new GraphBuildException("No graphs were built...");
            }
            for (JungGraph graph : graphs) {
                mGraphMap.put(graph.getElementName(), graph);
            }
            updateGraph(graphs.get(0));
        } catch (GraphBuildException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Graph Build Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private MouseListener listMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mGraphMap != null && mGraphMap.size() > 0) {
                    JList<T> list = (JList<T>) e.getSource();
                    if (e.getClickCount() == 2) {
                        // Double-click detected
                        int index = list.locationToIndex(e.getPoint());
                        T metric = list.getModel().getElementAt(index);
                        JungGraph graph = mGraphMap.get(metric.fullyQualifiedName);
                        if (graph != null) {
                            updateGraph(graph);
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }

    private void updateGraph(JungGraph graph) {
        mCurrentGraph = graph;
        if (mGraphPanel.getComponents().length > 0) {
            mGraphPanel.remove(0);
        }
        mGraphPanel.add(graph.getGraphPane(), BorderLayout.CENTER);
        mGraphPanel.revalidate();
        mGraphPanel.repaint();
    }

    private void zoomIn() {
        mCurrentGraph.setZoom(1);
    }

    private void zoomOut() {
        mCurrentGraph.setZoom(-1);
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
    }

    private void updateFilter(String text) {
        if (text.length() == 0) {
            mMetricList.setModel(mCachedMetricListModel);
        } else {
            mMetricListModel.clear();
            for (T metric : mMetrics) {
                if (metric.elementName.toLowerCase().startsWith(text.toLowerCase())) {
                    mMetricListModel.addElement(metric);
                }
            }
            mMetricList.setModel(mMetricListModel);
        }
    }
}
