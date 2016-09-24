package com.mcnedward.app.ui;

import com.mcnedward.app.ui.cellRenderer.DitMetricCellRenderer;
import com.mcnedward.ii.service.metric.element.DitMetric;
import com.mcnedward.ii.service.metric.element.MetricInfo;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by Edward on 9/23/2016.
 */
public class DitPanel {
    private JPanel mRoot;
    private MetricPanel mMetricPanel;
    private JPanel mMetricListPanel;

    public void update(MetricInfo metricInfo, List<DitMetric> metrics) {
        IILogger.debug("Updating DitPanel");
        mMetricPanel.update(metricInfo);

        DefaultListModel<DitMetric> ditMetricListModel = new DefaultListModel<>();
        for (DitMetric metric : metrics) {
            ditMetricListModel.addElement(metric);
        }
        JList<DitMetric> ditMetricList = new JList<>(ditMetricListModel);
        ditMetricList.setCellRenderer(new DitMetricCellRenderer());

        ditMetricList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ditMetricList.setVisibleRowCount(10);
        ditMetricList.setSelectedIndex(0);
        ditMetricList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        mDitMetricList.addMouseListener();
        JScrollPane scrollPane = new JScrollPane(ditMetricList);
        mMetricListPanel.add(scrollPane);
    }
}
