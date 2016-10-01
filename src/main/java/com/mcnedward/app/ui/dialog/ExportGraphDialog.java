package com.mcnedward.app.ui.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Edward on 9/26/2016.
 */
public class ExportGraphDialog extends IIFileDialog {

    private static final int WIDTH = 650;
    private static final int HEIGHT = 300;
    private static final String PREFERENCE_KEY = "ExportMetricGraphDialogKey";

    private GraphOptionsPanel mGraphptionsPanel;

    public ExportGraphDialog(Frame parent) {
        super(parent, "Export Metric Graphs");
    }

    @Override
    protected void initialize(JPanel optionsPanel) {
        setDialogSize(WIDTH, HEIGHT);
        optionsPanel.setVisible(true);
        mGraphptionsPanel = new GraphOptionsPanel();
        optionsPanel.add(mGraphptionsPanel.getRoot());
    }

    @Override
    protected String getInfoText() {
        return "Export Directory:";
    }

    @Override
    protected String getTextFieldText() {
        return "Export graphs for your project.";
    }

    @Override
    protected String getActionButtonText() {
        return "Export";
    }

    @Override
    protected String getPreferenceKey() {
        return PREFERENCE_KEY;
    }

    public boolean exportDit() {
        return mGraphptionsPanel.exportDit();
    }

    public boolean exportNoc() {
        return mGraphptionsPanel.exportNoc();
    }

    public boolean exportWmc() {
        return mGraphptionsPanel.exportWmc();
    }

    public boolean exportFull() {
        return mGraphptionsPanel.exportFull();
    }

    public boolean useProjectName() {
        return mGraphptionsPanel.exportUseProjectName();
    }

    public boolean usePackages() {
        return mGraphptionsPanel.exportUsePackages();
    }
}
