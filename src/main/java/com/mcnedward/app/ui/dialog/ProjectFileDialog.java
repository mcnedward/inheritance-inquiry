package com.mcnedward.app.ui.dialog;

import com.mcnedward.app.ui.utils.SettingsConst;

import javax.swing.*;
import java.awt.*;

/**
 * A dialog that allows for loading a project from a local file.
 * Created by Edward on 9/26/2016.
 */
public class ProjectFileDialog extends IIFileDialog {

    private static final int WIDTH = 650;
    private static final int HEIGHT = 200;

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
        return SettingsConst.PROJECT_FILE_DIALOG_KEY;
    }
}
