package com.mcnedward.app.ui.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Edward on 9/26/2016.
 */
public class ExportFileDialog extends IIFileDialog {

    private static final int WIDTH = 650;
    private static final int HEIGHT = 200;
    private static final String PREFERENCE_KEY = "ExportFileDialogKey";

    private JCheckBox mChkUsePackages;
    private JCheckBox mChkUseProjectName;

    public ExportFileDialog(Frame parent) {
        super(parent, "Export Metric Graphs");
    }

    @Override
    protected void initialize(JPanel optionsPanel) {
        setDialogSize(WIDTH, HEIGHT);
        mChkUsePackages = new JCheckBox("Use packages");
        mChkUsePackages.setToolTipText("Creates a directory structure to match the project's packages.");
        optionsPanel.add(mChkUsePackages);
        mChkUseProjectName = new JCheckBox("Use project name");
        mChkUseProjectName.setToolTipText("Uses the project name in the export directory for the graph, creating a new directory if necessary");
        optionsPanel.add(mChkUseProjectName);
        optionsPanel.setVisible(true);
    }

    @Override
    protected String getInfoText() {
        return "Export Directory:";
    }

    @Override
    protected String getTextFieldText() {
        return "Choose the directory you want to export your graphs to.";
    }

    @Override
    protected String getActionButtonText() {
        return "Export";
    }

    @Override
    protected String getPreferenceKey() {
        return PREFERENCE_KEY;
    }

    public boolean usePackages() {
        return mChkUsePackages.isSelected();
    }

    public boolean useProjectName() {
        return mChkUseProjectName.isSelected();
    }
}
