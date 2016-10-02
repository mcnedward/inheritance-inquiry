package com.mcnedward.app.ui.dialog;

import com.mcnedward.app.ui.form.GraphPanel;
import com.mcnedward.app.utils.PrefUtils;
import com.mcnedward.app.utils.Theme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class PreferencesDialog extends JDialog implements ActionListener {
    private JPanel mRoot;
    private JButton mBtnOk;
    private JButton mBtnClearFile;
    private JButton mBtnClearGit;
    private JButton mBtnClearGraph;
    private JButton mBtnClearMetricGraph;
    private JButton mBtnClearMetricSheet;
    private JButton mBtnClearAll;
    private JPanel mPreferencesPanel;
    private JPanel mThemesPanel;

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
        mBtnClearFile.addActionListener(e -> PrefUtils.clearPreferences(ProjectFileDialog.class));

        mBtnClearMetricGraph = new JButton();
        mBtnClearMetricGraph.addActionListener(e -> {PrefUtils.clearPreferences(ExportGraphDialog.class); PrefUtils.clearPreferences(ExportAllGraphsDialog.class);});

        mBtnClearMetricSheet = new JButton();
        mBtnClearMetricSheet.addActionListener(e -> PrefUtils.clearPreferences(ExportMetricFileDialog.class));

        mBtnClearGit = new JButton();
        mBtnClearGit.addActionListener(e -> PrefUtils.clearPreferences(GitDialog.class));

        mBtnClearAll = new JButton();
        mBtnClearAll.addActionListener(e -> {
            PrefUtils.clearPreferences(ProjectFileDialog.class);
            PrefUtils.clearPreferences(ExportGraphDialog.class);
            PrefUtils.clearPreferences(ExportAllGraphsDialog.class);
            PrefUtils.clearPreferences(ExportMetricFileDialog.class);
            PrefUtils.clearPreferences(GitDialog.class);
        });

        mBtnClearGraph = new JButton();
        mBtnClearGraph.addActionListener(e -> PrefUtils.clearPreferences(GraphPanel.class));

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
