package com.mcnedward.app.ui.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Edward on 9/26/2016.
 */
public class ProjectFileDialog extends IIFileDialog {

    private static final int WIDTH = 650;
    private static final int HEIGHT = 200;
    private static final String PREFERENCE_KEY = "ProjectFileDialogKey";

    public ProjectFileDialog(Frame parent) {
        super(parent, "Project File Load");
    }

    @Override
    protected void initialize(JPanel optionsPanel) {
        setDialogSize(WIDTH, HEIGHT);
    }

    @Override
    protected String getInfoText() {
        return "Project Location:";
    }

    @Override
    protected String getTextFieldText() {
        return "Choose the root directory of your project.";
    }

    @Override
    protected String getActionButtonText() {
        return "Load";
    }

    @Override
    protected String getPreferenceKey() {
        return PREFERENCE_KEY;
    }
}
