package com.mcnedward.app.ui.dialog;

import javax.swing.*;

/**
 * Created by Edward on 10/1/2016.
 */
public class GraphOptionsPanel {

    private JCheckBox mChkExportDit;
    private JCheckBox mChkExportNoc;
    private JCheckBox mChkExportFull;
    private JCheckBox mChkUsePackages;
    private JCheckBox mChkUseProjectName;
    private JPanel mRoot;

    boolean exportDit() {
        return mChkExportDit.isSelected();
    }

    boolean exportNoc() {
        return mChkExportNoc.isSelected();
    }

    boolean exportFull() {
        return mChkExportFull.isSelected();
    }

    boolean exportUsePackages() {
        return mChkUsePackages.isSelected();
    }

    boolean exportUseProjectName() {
        return mChkUseProjectName.isSelected();
    }

    JPanel getRoot() {
        return mRoot;
    }
}
