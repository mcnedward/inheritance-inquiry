package com.mcnedward.app.ui.form;

import com.mcnedward.app.InheritanceInquiry;
import com.mcnedward.app.ui.dialog.GitDialog;
import com.mcnedward.app.ui.dialog.ProjectFileDialog;
import com.mcnedward.ii.builder.ProjectBuilder;
import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.listener.SolutionBuildListener;
import com.mcnedward.ii.service.metric.element.DitMetric;
import com.mcnedward.ii.service.metric.element.NocMetric;
import com.mcnedward.ii.service.metric.element.WmcMetric;
import com.mcnedward.ii.utils.IILogger;
import com.mcnedward.ii.utils.ServiceFactory;

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
    private ProgressCard mMainProgressCard;
    private JButton mBtnFile;
    private JButton mBtnGit;
    private JPanel mHelpCard;
    private JPanel mTitlePanel;
    private JLabel mProjectName;
    private JLabel mProjectVersion;
    private JLabel mClassCount;
    private JLabel mInheritanceCount;
    private MetricPanel<DitMetric> mDitPanel;
    private MetricPanel<NocMetric> mNocPanel;
    private MetricPanel<WmcMetric> mWmcPanel;
    private FullHierarchyPanel mFullHierarchyPanel;

    private static ProjectBuilder mProjectBuilder;
    // Dialogs
    private static ProjectFileDialog mFileDialog;
    private static GitDialog mGitDialog;

    public MainPage() {
        mProjectBuilder = new ProjectBuilder(new SolutionBuildListener() {
            public void finished(JavaSolution solution) {
                IILogger.info("Build complete!");
                loadSolution(solution);
                showCard(SOLUTION_CARD);
            }

            @Override
            public void onProgressChange(String message, int progress) {
                mMainProgressCard.update(message, progress);
            }

            @Override
            public void onBuildError(String message, Exception exception) {
                JOptionPane.showMessageDialog(null, message, "Build Error", JOptionPane.ERROR_MESSAGE);
                IILogger.error(exception);
                showCard(HELP_CARD);
            }
        });
        initMenu(InheritanceInquiry.PARENT_FRAME);

        DefaultListModel<String> model = new DefaultListModel<>();
        for (int i = 0; i < 100; i++) {
            model.addElement("EDWARD " + i);
        }
        JList<String> jList = new JList<>(model);
        JScrollPane pane = new JScrollPane(jList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
//        mainPage.mHelpCard.add(pane);
    }

    private void initMenu(JFrame parent) {
        JMenuBar menuBar = new JMenuBar();
        parent.setJMenuBar(menuBar);

        JMenu mnAnalyze = new JMenu("Analyze");
        menuBar.add(mnAnalyze);

        mFileDialog = new ProjectFileDialog(parent);
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
        fileSettings.addActionListener(e -> mFileDialog.clearPreference());
        settingMenu.add(fileSettings);
        JMenuItem gitSettings = new JMenuItem("Clear Git Settings");
        gitSettings.addActionListener(e -> GitDialog.clearPreference());
        settingMenu.add(gitSettings);
    }

    private void loadSolution(JavaSolution solution) {
        // Setup the border
        mTitlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        mProjectName.setText(solution.getProjectName());
        mProjectVersion.setText(solution.getVersion());
        int classCount = solution.getClassCount();
        int inheritanceCount = solution.getInheritanceCount();
        int percentage = (int) (((double) inheritanceCount / classCount) * 100);
        mClassCount.setText(String.format("Classes [%s]", classCount));
        mInheritanceCount.setText(String.format("Inheritance Use [%s%%]", percentage));
        mDitPanel.update(solution, ServiceFactory.ditGraphService(), solution.getDitMetricInfo(), solution.getDitMetrics());
        mNocPanel.update(solution, ServiceFactory.nocGraphService(), solution.getNocMetricInfo(), solution.getNocMetrics());
        mWmcPanel.update(solution, null, solution.getWmcMetricInfo(), solution.getWmcMetrics());
        mFullHierarchyPanel.update(solution, ServiceFactory.fullGraphService());

    }

    /**
     * Loads a file or directory.
     *
     * @param file The File to loadFile
     */
    private void loadFile(File file) {
        showCard(PROGRESS_CARD);
        mProjectBuilder.setup(file).build();
    }

    private void openFileDialog() {
        mFileDialog.setVisible(true);
        if (mFileDialog.isSuccessful()) {
            loadFile(mFileDialog.getDirectory());
        }
    }

    private void openGitDialog() {
        mGitDialog.setVisible(true);
        if (mGitDialog.isSuccessful()) {
            loadFile(mGitDialog.getGitFile());
        }
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
