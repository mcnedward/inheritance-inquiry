package com.mcnedward.app.ui;

import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Edward on 9/23/2016.
 */
public class SolutionPanel {
    private JLabel mProjectName;
    private JLabel mProjectVersion;
    private JLabel mClassCount;
    private JLabel mInheritanceCount;
    private TabPanel mTabPanel;
    private JPanel mRoot;
    private JPanel mTitlePanel;

    public JPanel getRoot() {
        return mRoot;
    }

    public void loadSolution(JavaSolution solution) {
        IILogger.debug("Loading solution into SolutionPanel");
        // Setup the border
        mTitlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        mProjectName.setText(solution.getProjectName());
        mProjectVersion.setText(solution.getVersion());
        int classCount = solution.getClassCount();
        int inheritanceCount = solution.getInheritanceCount();
        int percentage = (int) (((double) inheritanceCount / classCount) * 100);
        mClassCount.setText(String.format("Classes [%s]", classCount));
        mInheritanceCount.setText(String.format("Inheritance Use [%s%%]", percentage));

        mTabPanel.update(solution);
    }
}
