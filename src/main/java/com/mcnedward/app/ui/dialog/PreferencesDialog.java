package com.mcnedward.app.ui.dialog;

import com.mcnedward.app.InheritanceInquiry;
import com.mcnedward.app.ui.form.GraphPanel;
import com.mcnedward.app.utils.*;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

public class PreferencesDialog extends JDialog implements ActionListener {
    private JPanel mRoot;
    private JButton mBtnOk;
    private JButton mBtnClearFile;
    private JButton mBtnClearGit;
    private JButton mBtnClearGraph;
    private JPanel mThemesPanel;
    private JCheckBox mChkUseFullScreen;
    private JButton mBtnDeleteGitProjects;

    public PreferencesDialog(JFrame parent) {
        setTitle("Preferences");
        setContentPane(mRoot);
        setModal(true);
        getRootPane().setDefaultButton(mBtnOk);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
        getRootPane().registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        mBtnOk.addActionListener(e -> setVisible(false));
    }

    private void createUIComponents() {
        mBtnClearFile = new JButton();
        mBtnClearFile.addActionListener(e -> {
            PrefUtils.clearPreferences(ProjectFileDialog.class);
            PrefUtils.clearPreferences(ExportGraphDialog.class);
            PrefUtils.clearPreferences(ExportAllGraphsDialog.class);
            PrefUtils.clearPreferences(ExportMetricFileDialog.class);
            DialogUtils.openMessageDialog("Cleared saved file locations.", "Preferences");
        });

        mBtnClearGit = new JButton();
        mBtnClearGit.addActionListener(e -> {
            PrefUtils.clearPreferences(GitDialog.class);
            DialogUtils.openMessageDialog("Cleared saved git remote URLs.", "Preferences");
        });

        mBtnDeleteGitProjects = new JButton();
        mBtnDeleteGitProjects.addActionListener(e -> {
            List<String> gitFileLocations = PrefUtils.getPreferenceList(Constants.GIT_ANALYZED_FILES, InheritanceInquiry.class);
            for (String fileLocation : gitFileLocations) {
                IIAppUtils.deleteTempFile(fileLocation);
            }
            DialogUtils.openMessageDialog(String.format("Deleted %s downloaded git projects from temporary storage.", gitFileLocations.size()), "Preferences");
        });

        mBtnClearGraph = new JButton();
        mBtnClearGraph.addActionListener(e -> {
            PrefUtils.clearPreferences(GraphPanel.class);
            DialogUtils.openMessageDialog("Reset the graph options.", "Preferences");
        });

        mChkUseFullScreen = new JCheckBox();
        mChkUseFullScreen.setSelected(PrefUtils.getPreferenceBool(Constants.FULL_SCREEN, InheritanceInquiry.class));
        mChkUseFullScreen.addActionListener(e -> PrefUtils.putPreference(Constants.FULL_SCREEN, mChkUseFullScreen.isSelected(), InheritanceInquiry.class));

        createThemesPanel();
    }

    private void createThemesPanel() {
        mThemesPanel = new JPanel();
        ButtonGroup group = new ButtonGroup();
        Theme currentTheme = Theme.getCurrentTheme();
        for (Theme theme : Theme.values()) {
            JRadioButton button = new JRadioButton(theme.themeName());
            group.add(button);
            button.setSelected(currentTheme.themeName().equals(theme.themeName()));
            button.addActionListener(e -> Theme.updateTheme(theme));
            mThemesPanel.add(button);
        }
    }

    public void open() {
        setVisible(true);
    }

    private void close() {
        dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        close();
    }
}
