package com.mcnedward.app.ui.solution;

import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.utils.IILogger;
import com.mcnedward.ii.utils.ServiceFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Edward on 9/23/2016.
 */
public class SolutionPanel {
    private JPanel mRoot;
    private JLabel mProjectName;
    private JLabel mProjectVersion;
    private JLabel mClassCount;
    private JLabel mInheritanceCount;
    private JPanel mTitlePanel;
    private MetricPanel mNocPanel;
    private MetricPanel mDitPanel;
    private MetricPanel mWmcPanel;
    private FullHierarchyPanel mFullHierarchyPanel;

    public void loadSolution(JavaSolution solution) {
        IILogger.debug("Loading solution into SolutionPanel");
        loadSolutionPanel(solution);
        loadTabPanel(solution);
    }

    private void loadSolutionPanel(JavaSolution solution) {
        // Setup the border
        mTitlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        mProjectName.setText(solution.getProjectName());
        mProjectVersion.setText(solution.getVersion());
        int classCount = solution.getClassCount();
        int inheritanceCount = solution.getInheritanceCount();
        int percentage = (int) (((double) inheritanceCount / classCount) * 100);
        mClassCount.setText(String.format("Classes [%s]", classCount));
        mInheritanceCount.setText(String.format("Inheritance Use [%s%%]", percentage));
    }

    private void loadTabPanel(JavaSolution solution) {
        mDitPanel.update(solution, ServiceFactory.ditGraphService(), solution.getDitMetricInfo(), solution.getDitMetrics());
        mNocPanel.update(solution, ServiceFactory.nocGraphService(), solution.getNocMetricInfo(), solution.getNocMetrics());
        mWmcPanel.update(solution, null, solution.getWmcMetricInfo(), solution.getWmcMetrics());
        mFullHierarchyPanel.update(solution, ServiceFactory.fullGraphService());
    }
}
