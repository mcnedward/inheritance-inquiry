package com.mcnedward.app.ui.dialog;

import javax.swing.*;

/**
 * Created by Edward on 9/30/2016.
 */
public class MetricOptionsPanel {
    private JCheckBox mChkExportDit;
    private JCheckBox mChkExportNoc;
    private JCheckBox mChkExportWmc;
    private JCheckBox mChkExportFull;
    private JPanel mRoot;
    private JCheckBox mChkUseCsvFormat;

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

    public boolean useCsvFormat() {
        return mChkUseCsvFormat.isSelected();
    }

    public JPanel getRoot() {
        return mRoot;
    }
}
