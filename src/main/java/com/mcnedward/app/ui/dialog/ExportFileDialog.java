package com.mcnedward.app.ui.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Edward on 9/26/2016.
 */
public class ExportFileDialog extends IIFileDialog {

    private static final int WIDTH = 650;
    private static final int HEIGHT = 220;
    private static final String PREFERENCE_KEY = "ExportFileDialogKey";

    private JRadioButton mRdExportAll;
    private JRadioButton mRdExportSelected;
    private JCheckBox mChkUsePackages;
    private JCheckBox mChkUseProjectName;

    public ExportFileDialog(Frame parent) {
        super(parent, "Export Metric Graphs");
    }

    @Override
    protected void initialize(JPanel optionsPanel) {
        setDialogSize(WIDTH, HEIGHT);
        optionsPanel.setVisible(true);

        ButtonGroup group = new ButtonGroup();
        mRdExportAll = new JRadioButton("Export all");
        mRdExportAll.setToolTipText("Export graphs for all classes.");
        mRdExportAll.setSelected(true);
        group.add(mRdExportAll);
        optionsPanel.add(mRdExportAll);
        mRdExportSelected = new JRadioButton("Export selected");
        mRdExportSelected.setToolTipText("Export graphs for only those classes that are selected in the list.");
        group.add(mRdExportSelected);
        optionsPanel.add(mRdExportSelected);

        mChkUsePackages = new JCheckBox("Use packages");
        mChkUsePackages.setToolTipText("Creates a directory structure to match the project's packages.");
        optionsPanel.add(mChkUsePackages);
        mChkUseProjectName = new JCheckBox("Use project name");
        mChkUseProjectName.setToolTipText("Uses the project name in the export directory for the graph, creating a new directory if necessary");
        optionsPanel.add(mChkUseProjectName);
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

    public boolean exportAll() {
        return mRdExportAll.isSelected();
    }

    public boolean usePackages() {
        return mChkUsePackages.isSelected();
    }

    public boolean useProjectName() {
        return mChkUseProjectName.isSelected();
    }
}
