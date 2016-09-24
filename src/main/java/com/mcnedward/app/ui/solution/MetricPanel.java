package com.mcnedward.app.ui.solution;

import com.mcnedward.app.ui.PlaceholderTextField;
import com.mcnedward.app.ui.cellRenderer.MetricCellRenderer;
import com.mcnedward.ii.service.metric.element.Metric;
import com.mcnedward.ii.service.metric.element.MetricInfo;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
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
    private JPanel mInfoPanel;
    private JTextField mTxtFilter;

    private List<T> mMetrics;

    public void update(MetricInfo metricInfo, List<T> metrics) {
        updateMetricInfo(metricInfo);
        updateMetrics(metrics);

        mBtnGenerate.addActionListener(e -> generateGraphs());
        setupRadioButtons();
    }

    private void updateMetricInfo(MetricInfo metricInfo) {
        mMetricName.setText(metricInfo.getMetricType().metricName);
        mMin.setText(String.valueOf(metricInfo.getMin()));
        mAverage.setText(String.valueOf((int) metricInfo.getAverage()));
        mMax.setText(String.valueOf(metricInfo.getMax()));
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
        mMetricList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//        mDitMetricList.addMouseListener();
        JScrollPane scrollPane = new JScrollPane(mMetricList);
        mMetricListPanel.add(scrollPane);
    }

    private void setupRadioButtons() {
        ButtonGroup group = new ButtonGroup();
        group.add(mGenerateAll);
        group.add(mGenerateSelected);
        mGenerateAll.setSelected(true);
    }

    private void generateGraphs() {
        List<T> selectedMetrics = mMetricList.getSelectedValuesList();
        IILogger.info("Generating graphs.");
    }

    private void createUIComponents() {
        mTxtFilter = new PlaceholderTextField("", "Filter metrics");
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
