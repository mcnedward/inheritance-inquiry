package com.mcnedward.app.ui;

import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;

/**
 * Created by Edward on 9/23/2016.
 */
public class TabPanel {
    private JTabbedPane mTabPane;
    private JPanel mNocPane;
    private JPanel mWmcPane;
    private JPanel mRoot;
    private DitPanel mDitPanel;

    public void update(JavaSolution solution) {
        IILogger.debug("Updating TabPanel");
        mDitPanel.update(solution.getDitMetricInfo(), solution.getDitMetrics());
    }
}
