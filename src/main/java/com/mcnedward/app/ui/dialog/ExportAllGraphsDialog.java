package com.mcnedward.app.ui.dialog;

import com.mcnedward.app.utils.SettingsConst;

import javax.swing.*;
import java.awt.*;

/**
 * A dialog from the Export menu options that allows for exporting graphs for a project.
 * Created by Edward on 9/26/2016.
 */
public class ExportAllGraphsDialog extends IIFileDialog {

    private static final int WIDTH = 650;
    private static final int HEIGHT = 300;

    private GraphOptionsPanel mGraphOptionsPanel;

    public ExportAllGraphsDialog(Frame parent) {
        super(parent, "Export Metric Graphs");
    }

    @Override
    protected void initialize(JPanel optionsPanel) {
        setDialogSize(WIDTH, HEIGHT);
        optionsPanel.setVisible(true);
        mGraphOptionsPanel = new GraphOptionsPanel();
        optionsPanel.add(mGraphOptionsPanel.getRoot());
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
        return SettingsConst.EXPORT_ALL_GRAPHS_DIALOG_KEY;
    }

    public boolean exportDit() {
        return mGraphOptionsPanel.exportDit();
    }

    public boolean exportNoc() {
        return mGraphOptionsPanel.exportNoc();
    }

    public boolean exportFull() {
        return mGraphOptionsPanel.exportFull();
    }

    public boolean useProjectName() {
        return mGraphOptionsPanel.exportUseProjectName();
    }

    public boolean usePackages() {
        return mGraphOptionsPanel.exportUsePackages();
    }
}
