package com.mcnedward.app.ui.solution.metric;

import com.mcnedward.app.ui.cellRenderer.MetricCellRenderer;
import com.mcnedward.ii.service.metric.element.MetricInfo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by Edward on 9/23/2016.
 */
public class MetricPanel<T> {
    private JPanel mRoot;
    private JLabel mMetricName;
    private JLabel mMin;
    private JLabel mAverage;
    private JLabel mMax;
    private JPanel mMetricListPanel;

    public void update(MetricInfo metricInfo, List<T> metrics) {
        updateMetricInfo(metricInfo);
        updateMetrics(metrics);
    }

    private void updateMetricInfo(MetricInfo metricInfo) {
        mMetricName.setText(metricInfo.getMetricType().metricName);
        mMin.setText(String.valueOf(metricInfo.getMin()));
        mAverage.setText(String.valueOf((int) metricInfo.getAverage()));
        mMax.setText(String.valueOf(metricInfo.getMax()));
    }

    private void updateMetrics(List<T> metrics) {
        DefaultListModel<T> metricListModel = new DefaultListModel<>();
        for (T metric : metrics) {
            metricListModel.addElement(metric);
        }
        JList<T> metricList = new JList<>(metricListModel);
        metricList.setCellRenderer(new MetricCellRenderer());

        metricList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        metricList.setVisibleRowCount(10);
        metricList.setSelectedIndex(0);
        metricList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        mDitMetricList.addMouseListener();
        JScrollPane scrollPane = new JScrollPane(metricList);
        mMetricListPanel.add(scrollPane);
    }
}
