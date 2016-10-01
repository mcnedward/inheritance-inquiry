package com.mcnedward.app.ui.dialog;

import com.mcnedward.app.ui.form.GraphPanel;
import com.mcnedward.app.ui.utils.PrefUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class PreferencesDialog extends JDialog implements ActionListener {
    private JPanel mRoot;
    private JButton mBtnOk;
    private JButton mBtnClearFile;
    private JButton mBtnClearGit;
    private JButton mBtnClearGraph;
    private JTextPane mLblGit;
    private JTextPane mLblGraph;
    private JButton mBtnClearMetricGraph;
    private JButton mBtnClearMetricSheet;
    private JButton mBtnClearAll;

    public PreferencesDialog(JFrame parent) {
        setTitle("Preferences");
        setContentPane(mRoot);
        setModal(true);
        getRootPane().setDefaultButton(mBtnOk);

        String gitMessage = "<html><p>Clear the saved locations when choosing to Analyze from Git.<br>This also resets your saved username and password.</p></html>";
        mLblGit.setText(gitMessage);
        String graphMessage = "<html><p>Reset the graph options to the default.<br>You may have to restart the app for this to take effect.</p></html>";
        mLblGraph.setText(graphMessage);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
        getRootPane().registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        mBtnOk.addActionListener(e -> setVisible(false));
    }

    private void createUIComponents() {
        mLblGit = createTextPane();
        mLblGraph = createTextPane();

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
    }

    private JTextPane createTextPane() {
        JTextPane pane = new JTextPane();
        pane.setBackground(UIManager.getColor("Label.background"));
        pane.setBorder(UIManager.getBorder("Label.border"));
        pane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        pane.setContentType("text/html");
        pane.setMargin(new Insets(0, 0, 0, 0));
        return pane;
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
