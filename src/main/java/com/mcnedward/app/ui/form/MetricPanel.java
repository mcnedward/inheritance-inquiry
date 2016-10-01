package com.mcnedward.app.ui.form;

import com.mcnedward.app.ui.cellRenderer.MetricCellRenderer;
import com.mcnedward.app.ui.component.IIListModel;
import com.mcnedward.app.ui.component.PlaceholderTextField;
import com.mcnedward.app.ui.listener.GraphPanelListener;
import com.mcnedward.app.ui.utils.DialogUtils;
import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.service.graph.IGraphService;
import com.mcnedward.ii.service.graph.jung.JungGraph;
import com.mcnedward.ii.service.metric.MetricType;
import com.mcnedward.ii.service.metric.element.Metric;
import com.mcnedward.ii.service.metric.element.MetricInfo;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.*;
import java.util.List;

/**
 * Created by Edward on 9/23/2016.
 */
public class MetricPanel<T extends Metric> implements GraphPanelListener {

    private JPanel mRoot;
    private JLabel mMetricName;
    private JLabel mMin;
    private JLabel mAverage;
    private JLabel mMax;
    private JPanel mMetricListPanel;
    private JList<T> mMetricList;
    private IIListModel<T> mCachedMetricListModel;
    private IIListModel<T> mMetricListModel;
    private JTextField mTxtFilter;
    private GraphPanel<T> mGraphPanel;
    private JPanel mMetricTitlePanel;
    private JButton mInfoButton;
    private JButton mBtnSortAlpa;
    private JButton mBtnSortMetric;

    private List<T> mMetrics;
    private MetricType mMetricType;
    private boolean mMetricListCreated;
    private boolean mFilterFocused;

    public void update(JavaSolution solution, IGraphService graphService, MetricInfo metricInfo, List<T> metrics) {
        IILogger.info("Updating metric panel");
        updateMetricInfo(metricInfo);
        updateMetrics(metrics);
        List<String> fullyQualifiedNames = new ArrayList<>();
        for (T metric : mMetrics)
            fullyQualifiedNames.add(metric.getFullyQualifiedName());
        mGraphPanel.update(solution, graphService, fullyQualifiedNames, this);
        mMetricType = metricInfo.getMetricType();
    }

    private void updateMetricInfo(MetricInfo metricInfo) {
        mMetricName.setText(metricInfo.getMetricType().metricName);
        mMin.setText(String.valueOf(metricInfo.getMin()));
        mAverage.setText(String.valueOf((int) metricInfo.getAverage()));
        mMax.setText(String.valueOf(metricInfo.getMax()));
        mMetricTitlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
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

    private void checkMetricListCreation() {
        if (!mMetricListCreated) {
            mMetricListCreated = true;
            mMetricListModel = new IIListModel<>();
            mCachedMetricListModel = new IIListModel<>();
            mMetricList = new JList<>(mMetricListModel);
            mMetricList.setCellRenderer(new MetricCellRenderer());
            mMetricList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            mMetricList.setVisibleRowCount(10);
            mMetricList.setSelectedIndex(0);
            mMetricList.setDragEnabled(true);
            mMetricList.addListSelectionListener(e -> {
                moveToSelected();
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

    private void moveToSelected() {
        int index = mMetricList.getSelectedIndex();
        // Don't update here if the Filter text field is focused
        if (mFilterFocused || index == -1) return;
        T metric = mMetricList.getModel().getElementAt(index);
        mGraphPanel.updateGraph(metric.getFullyQualifiedName());
    }

    private void openInfoDialog() {
        DialogUtils.openMetricInfoDialog(mMetricType);
    }

    private void sortList(int sortType) {
        mMetricListModel.sort(sortType);
        mCachedMetricListModel.sort(sortType);
    }

    @Override
    public void onGraphsLoaded(JungGraph firstGraph) {
        moveToSelected();
    }

    @Override
    public Collection<JungGraph> requestGraphs(Map<String, JungGraph> graphMap, boolean exportAll) {
        Collection<JungGraph> graphs;
        if (exportAll) {
            graphs = graphMap.values();
        } else {
            List<T> graphKeys = mMetricList.getSelectedValuesList();
            graphs = new ArrayList<>();
            for (T key : graphKeys) {
                JungGraph graph = graphMap.get(key.getFullyQualifiedName());
                if (graph != null) {
                    graphs.add(graph);
                }
            }
        }
        return graphs;
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
        mInfoButton = new JButton("?");
        mInfoButton.setMargin(new Insets(2, 5, 0, 5));
        mInfoButton.addActionListener(e -> openInfoDialog());

        mBtnSortAlpa = new JButton();
        mBtnSortAlpa.setMargin(new Insets(4, 4, 4, 4));
        mBtnSortAlpa.addActionListener(e -> sortList(IIListModel.ALPHA));
        mBtnSortMetric = new JButton();
        mBtnSortMetric.setMargin(new Insets(4, 4, 4, 4));
        mBtnSortMetric.addActionListener(e -> sortList(IIListModel.VALUE));
    }

}
