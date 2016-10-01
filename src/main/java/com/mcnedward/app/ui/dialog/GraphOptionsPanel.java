package com.mcnedward.app.ui.dialog;

import javax.swing.*;

/**
 * Created by Edward on 10/1/2016.
 */
public class GraphOptionsPanel {

    private JCheckBox mChkExportDit;
    private JCheckBox mChkExportNoc;
    private JCheckBox mChkExportWmc;
    private JCheckBox mChkExportFull;
    private JCheckBox mChkUsePackages;
    private JCheckBox mChkUseProjectName;
    private JPanel mRoot;

    public boolean exportDit() {
        return mChkExportDit.isSelected();
    }

    public boolean exportNoc() {
        return mChkExportNoc.isSelected();
    }

    public boolean exportWmc() {
        return mChkExportWmc.isSelected();
    }

    public boolean exportFull() {
        return mChkExportFull.isSelected();
    }

    public boolean exportUsePackages() {
        return mChkUsePackages.isSelected();
    }

    public boolean exportUseProjectName() {
        return mChkUseProjectName.isSelected();
    }

    public JPanel getRoot() {
        return mRoot;
    }
}
