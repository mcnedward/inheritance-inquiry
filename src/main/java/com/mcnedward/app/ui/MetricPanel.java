package com.mcnedward.app.ui;

import com.mcnedward.ii.service.metric.element.MetricInfo;

import javax.swing.*;

/**
 * Created by Edward on 9/23/2016.
 */
public class MetricPanel {
    private JLabel mMin;
    private JLabel mAverage;
    private JLabel mMax;
    private JPanel mRoot;

    public void update(MetricInfo metricInfo) {
        mMin.setText(String.valueOf(metricInfo.getMin()));
        mAverage.setText(String.valueOf((int) metricInfo.getAverage()));
        mMax.setText(String.valueOf(metricInfo.getMax()));
    }
}
