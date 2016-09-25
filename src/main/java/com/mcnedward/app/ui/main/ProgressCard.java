package com.mcnedward.app.ui.main;

import com.mcnedward.app.InheritanceInquiry;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Edward on 9/25/2016.
 */
public class ProgressCard {
    private JProgressBar mProgressBar;
    private JLabel mLblProgress;
    private JPanel mRoot;

    public void update(String message, int progress) {
        mProgressBar.setVisible(true);
        mLblProgress.setText(message);
        mProgressBar.setValue(progress);
    }

    public void error(String error) {
        mLblProgress.setText(error);
        mProgressBar.setVisible(false);
    }

    private void createUIComponents() {
        mProgressBar = new JProgressBar();
        int progressWidth = (int) (InheritanceInquiry.WIDTH * 0.5f);
        int progressHeight = 30;
        Dimension progressBarDimensions = new Dimension(progressWidth, progressHeight);
        mProgressBar.setPreferredSize(progressBarDimensions);
        mProgressBar.setSize(progressBarDimensions);
        mProgressBar.setMaximumSize(progressBarDimensions);
        mProgressBar.setMinimumSize(progressBarDimensions);
    }
}
