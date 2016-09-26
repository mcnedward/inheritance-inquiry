package com.mcnedward.app.ui.dialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by Edward on 9/26/2016.
 */
public class ProjectFileDialog extends IIFileDialog {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 200;
    private static final String PREFERENCE_KEY = "ProjectFileDialogKey";

    public ProjectFileDialog(Frame parent) {
        super(parent, "Project File Load");
    }

    @Override
    protected void initialize() {
//        setDialogSize(WIDTH, HEIGHT);
    }

    @Override
    protected void mainAction(String fileLocation) {
        if (fileLocation == null || fileLocation.equals("")) {
            JOptionPane.showMessageDialog(null, "You need to select a file.", "Project Load", JOptionPane.WARNING_MESSAGE);
            return;
        }
        File file = new File(fileLocation);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "That file does not exist!", "File Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (file.isFile()) {
            if (!file.getPath().endsWith(".java")) {
                JOptionPane.showMessageDialog(null, "You need to select a Java project directory or file.", "Project Load",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        setFile(file);
        updatePreferences(fileLocation);
        closeWithSuccess();
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
