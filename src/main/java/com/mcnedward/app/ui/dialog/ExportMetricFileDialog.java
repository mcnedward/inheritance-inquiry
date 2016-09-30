package com.mcnedward.app.ui.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Edward on 9/26/2016.
 */
public class ExportMetricFileDialog extends IIFileDialog {

    private static final int WIDTH = 650;
    private static final int HEIGHT = 300;
    private static final String PREFERENCE_KEY = "ExportMetricFileDialogKey";

    private MetricOptionsPanel mMetricOptionsPanel;

    public ExportMetricFileDialog(Frame parent) {
        super(parent, "Export Metric Sheets");
    }

    @Override
    protected void initialize(JPanel optionsPanel) {
        setDialogSize(WIDTH, HEIGHT);
        optionsPanel.setVisible(true);
        mMetricOptionsPanel = new MetricOptionsPanel();
        optionsPanel.add(mMetricOptionsPanel.getRoot());
    }

    @Override
    protected String getInfoText() {
        return "Export Directory:";
    }

    @Override
    protected String getTextFieldText() {
        return "You can export metric details to an Excel read-able format.";
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
        return mMetricOptionsPanel.exportDit();
    }

    public boolean exportNoc() {
        return mMetricOptionsPanel.exportNoc();
    }

    public boolean exportWmc() {
        return mMetricOptionsPanel.exportWmc();
    }

    public boolean exportFull() {
        return mMetricOptionsPanel.exportFull();
    }

    public boolean useCsvFormat() {
        return mMetricOptionsPanel.useCsvFormat();
    }
}
