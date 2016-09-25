package com.mcnedward.app.ui.main;

import com.mcnedward.app.ui.dialog.FileDialog;
import com.mcnedward.app.ui.dialog.GitDialog;
import com.mcnedward.app.ui.solution.SolutionPanel;
import com.mcnedward.ii.builder.ProjectBuilder;
import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.listener.SolutionBuildListener;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by Edward on 9/25/2016.
 */
public class MainPage {

    private static final String HELP_CARD = "HelpCard";
    private static final String PROGRESS_CARD = "ProgressCard";
    private static final String SOLUTION_CARD = "SolutionCard";

    private JPanel mRoot;
    private JPanel mCards;
    private ProgressCard mProgressCard;
    private SolutionPanel mSolutionCard;
    private JButton mBtnFile;
    private JButton mBtnGit;
    private JPanel mHelpCard;

    private static ProjectBuilder mProjectBuilder;
    // Dialogs
    private static FileDialog mFileDialog;
    private static GitDialog mGitDialog;

    public MainPage(JFrame parent) {
        mProjectBuilder = new ProjectBuilder(listener());
        initMenu(parent);
    }

    private void initMenu(JFrame parent) {
        JMenuBar menuBar = new JMenuBar();
        parent.setJMenuBar(menuBar);

        JMenu mnAnalyze = new JMenu("Analyze");
        menuBar.add(mnAnalyze);

        mFileDialog = new FileDialog(parent);
        JMenuItem mntmFromFile = new JMenuItem("From file");
        mntmFromFile.addActionListener(e -> openFileDialog());
        mnAnalyze.add(mntmFromFile);

        mGitDialog = new GitDialog(parent);
        JMenuItem mntmFromGit = new JMenuItem("From git");
        mnAnalyze.add(mntmFromGit);
        mntmFromGit.addActionListener(e -> openGitDialog());

        JMenu settingMenu = new JMenu("Settings");
        menuBar.add(settingMenu);
        JMenuItem fileSettings = new JMenuItem("Clear File Settings");
        fileSettings.addActionListener(e -> FileDialog.clearPreference());
        settingMenu.add(fileSettings);
        JMenuItem gitSettings = new JMenuItem("Clear Git Settings");
        gitSettings.addActionListener(e -> GitDialog.clearPreference());
        settingMenu.add(gitSettings);
    }

    /**
     * Loads a file or directory.
     *
     * @param file The File to load
     */
    private void load(File file, boolean deleteAfterBuild) {
        showCard(PROGRESS_CARD);
        mProjectBuilder.setup(file).build();
    }

    public void openFileDialog() {
        mFileDialog.setVisible(true);
        if (mFileDialog.isSuccessful()) {
            load(mFileDialog.getFile(), false);
        }
    }

    public void openGitDialog() {
        mGitDialog.setVisible(true);
        if (mGitDialog.isSuccessful()) {
            load(mGitDialog.getGitFile(), true);
        }
    }

    private SolutionBuildListener listener() {
        return new SolutionBuildListener() {
            public void finished(JavaSolution solution) {
                IILogger.info("Build complete!");
                mSolutionCard.loadSolution(solution);
                showCard(SOLUTION_CARD);
            }

            @Override
            public void onProgressChange(String message, int progress) {
                mProgressCard.update(message, progress);
            }

            @Override
            public void onBuildError(String message, Exception exception) {
                JOptionPane.showMessageDialog(mRoot, message, "Build Error", JOptionPane.ERROR_MESSAGE);
                IILogger.error(exception);
                showCard(HELP_CARD);
            }
        };
    }

    private void showCard(String card) {
        ((CardLayout) mCards.getLayout()).show(mCards, card);
    }

    public JPanel getRoot() {
        return mRoot;
    }

    private void createUIComponents() {
        mBtnFile = new JButton("From File");
        mBtnFile.addActionListener(e -> openFileDialog());
        mBtnGit = new JButton("From Git");
        mBtnGit.addActionListener(e -> openGitDialog());
    }

}
