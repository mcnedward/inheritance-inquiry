package com.mcnedward.app.ui.solution;

import com.mcnedward.app.ui.solution.metric.MetricPanel;
import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.service.metric.element.DitMetric;
import com.mcnedward.ii.service.metric.element.NocMetric;
import com.mcnedward.ii.service.metric.element.WmcMetric;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;

/**
 * Created by Edward on 9/23/2016.
 */
public class TabPanel {
    private JTabbedPane mTabPane;
    private JPanel mRoot;
    private MetricPanel<DitMetric> mDitPanel;
    private MetricPanel<NocMetric> mNocPanel;
    private MetricPanel<WmcMetric> mWmcPanel;

    public void update(JavaSolution solution) {
        IILogger.debug("Updating TabPanel");
        mDitPanel.update(solution.getDitMetricInfo(), solution.getDitMetrics());
        mNocPanel.update(solution.getNocMetricInfo(), solution.getNocMetrics());
        mWmcPanel.update(solution.getWmcMetricInfo(), solution.getWmcMetrics());

    }
}
