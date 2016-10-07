package com.mcnedward.app.ui.form;

import com.mcnedward.app.InheritanceInquiry;
import com.mcnedward.app.ui.dialog.results.ExportAllGraphsResults;
import com.mcnedward.app.ui.dialog.results.ExportMetricFileResults;
import com.mcnedward.app.utils.Constants;
import com.mcnedward.app.utils.DialogUtils;
import com.mcnedward.app.utils.IIAppUtils;
import com.mcnedward.app.utils.PrefUtils;
import com.mcnedward.ii.builder.GraphBuilder;
import com.mcnedward.ii.builder.MetricBuilder;
import com.mcnedward.ii.builder.ProjectBuilder;
import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.listener.GitDownloadListener;
import com.mcnedward.ii.listener.GraphExportListener;
import com.mcnedward.ii.listener.MetricExportListener;
import com.mcnedward.ii.listener.SolutionBuildListener;
import com.mcnedward.ii.service.graph.IGraphService;
import com.mcnedward.ii.service.graph.element.GraphOptions;
import com.mcnedward.ii.service.graph.jung.JungGraph;
import com.mcnedward.ii.service.metric.element.DitMetric;
import com.mcnedward.ii.service.metric.element.MetricOptions;
import com.mcnedward.ii.service.metric.element.NocMetric;
import com.mcnedward.ii.service.metric.element.WmcMetric;
import com.mcnedward.ii.utils.IILogger;
import com.mcnedward.ii.utils.ServiceFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * Created by Edward on 9/25/2016.
 */
public class MainPage {

    private static ResourceBundle RESOURCES = ResourceBundle.getBundle("MainPage");
    private static final String HELP_CARD = "HelpCard";
    private static final String PROGRESS_CARD = "ProgressCard";
    private static final String SOLUTION_CARD = "SolutionCard";

    private JPanel mRoot;
    private JPanel mCards;
    private ProgressCard mMainProgressCard;
    private JButton mBtnFile;
    private JButton mBtnGit;
    private JPanel mTitlePanel;
    private JLabel mProjectName;
    private JLabel mProjectVersion;
    private JLabel mClassCount;
    private JLabel mInheritanceCount;
    private JMenuItem mSheetSettings;
    private JMenuItem mGraphSettings;
    private MetricPanel<DitMetric> mDitPanel;
    private MetricPanel<NocMetric> mNocPanel;
    private MetricPanel<WmcMetric> mWmcPanel;
    private FullHierarchyPanel mFullHierarchyPanel;

    private static ProjectBuilder mProjectBuilder;
    private JavaSolution mSolution;

    public MainPage() {
        mProjectBuilder = new ProjectBuilder(solutionBuildListener());
        initMenu(InheritanceInquiry.PARENT_FRAME);
        DialogUtils.loadDialogs(InheritanceInquiry.PARENT_FRAME, gitDownloadListener());
    }

    private void initMenu(JFrame parent) {
        JMenuBar menuBar = new JMenuBar();
        parent.setJMenuBar(menuBar);

        JMenu menuFile = new JMenu("File");
        menuBar.add(menuFile);

        JMenuItem fromFileItem = new JMenuItem(RESOURCES.getString("analyze_local_file"));
        fromFileItem.addActionListener(e -> openFileDialog());
        menuFile.add(fromFileItem);
        JMenuItem fromGitItem = new JMenuItem(RESOURCES.getString("analyze_from_github"));
        menuFile.add(fromGitItem);
        fromGitItem.addActionListener(e -> openGitDialog());
        menuFile.addSeparator();

        mGraphSettings = new JMenuItem(RESOURCES.getString("export_metric_graphs"));
        mGraphSettings.addActionListener(e -> openExportGraphDialog());
        mGraphSettings.setEnabled(false);
        menuFile.add(mGraphSettings);
        mSheetSettings = new JMenuItem(RESOURCES.getString("export_metric_sheets"));
        mSheetSettings.addActionListener(e -> openExportMetricFileDialog());
        mSheetSettings.setEnabled(false);
        menuFile.add(mSheetSettings);
        menuFile.addSeparator();

        addRecentItems(menuFile, Constants.LOCAL_ANALYZED_FILES);
        addRecentItems(menuFile, Constants.GIT_ANALYZED_FILES);

        JMenuItem exitItem = new JMenuItem(RESOURCES.getString("exit"));
        exitItem.addActionListener(e -> System.exit(0));
        menuFile.add(exitItem);

        JMenu settingMenu = new JMenu(RESOURCES.getString("settings"));
        menuBar.add(settingMenu);
        JMenuItem preferenceItem = new JMenuItem(RESOURCES.getString("preferences"));
        preferenceItem.addActionListener(e -> DialogUtils.openPreferencesDialog());
        settingMenu.add(preferenceItem);
        JMenuItem gitHelpItem = new JMenuItem(RESOURCES.getString("git_help"));
        gitHelpItem.addActionListener(e -> DialogUtils.openGitHelpDialog());
        settingMenu.add(gitHelpItem);
        JMenuItem aboutItem = new JMenuItem(RESOURCES.getString("about"));
        aboutItem.addActionListener(e -> DialogUtils.openAboutDialog());
        settingMenu.add(aboutItem);
    }

    private void addRecentItems(JMenu menu, String key) {
        java.util.List<String> recentList = PrefUtils.getPreferenceList(key, InheritanceInquiry.class);
        int max = recentList.size() > 5 ? 5 : recentList.size();
        java.util.List<String> pathsToDelete = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            String recentItem = recentList.get(i);
            File file = new File(recentItem);
            if (!file.exists()) {
                pathsToDelete.add(recentItem);
                continue;
            }
            JMenuItem item = new JMenuItem(String.format("%s: %s", i + 1, recentItem));
            item.addActionListener(e -> loadFile(file));
            menu.add(item);
        }
        if (pathsToDelete.size() > 0) {
            for (String p : pathsToDelete) {
                recentList.remove(p);
                max--;
            }
            PrefUtils.putPreference(key, recentList, InheritanceInquiry.class);
        }
        if (max > 0)
            menu.addSeparator();
    }

    private void loadSolution(JavaSolution solution) {
        mSolution = solution;
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
        mGraphSettings.setEnabled(true);
        mSheetSettings.setEnabled(true);
    }

    private void loadFile(File file) {
        showCard(PROGRESS_CARD);
        mProjectBuilder.setup(file).build();
        java.util.List<String> localFiles = PrefUtils.getPreferenceList(Constants.LOCAL_ANALYZED_FILES, InheritanceInquiry.class);
        updateAnalyzedFilePaths(Constants.LOCAL_ANALYZED_FILES, localFiles, file.getAbsolutePath());
    }

    private void openFileDialog() {
        if (DialogUtils.openProjectFileDialog()) {
            loadFile(DialogUtils.getProjectFile());
        }
    }

    private void openGitDialog() {
        DialogUtils.openGitDialog();
    }

    private void openExportMetricFileDialog() {
        if (DialogUtils.openExportMetricFileDialogForSuccess()) {
            ExportMetricFileResults results = DialogUtils.getExportMetricGraphResults();
            MetricOptions options = new MetricOptions(mSolution,
                    results.getDirectory(),
                    results.exportDit(),
                    results.exportNoc(),
                    results.exportWmc(),
                    results.exportFull());
            options.setUseCsvFormat(results.useCsvFormat());
            new MetricBuilder(metricExportListener()).setup(ServiceFactory.metricService(), options).build();
        }
    }

    private int mGraphExportRequests;

    private void openExportGraphDialog() {
        if (DialogUtils.openExportAllGraphsDialog()) {
            ExportAllGraphsResults results = DialogUtils.getExportAllGraphResults();
            String projectName = results.usePackages() ? mSolution.getProjectName() : null;
            GraphOptions options = new GraphOptions(results.getDirectory(), projectName, results.usePackages());
            GraphBuilder builder = new GraphBuilder(graphExportListener());
            // The graph service type doesn't matter here, since we're just doing an export
            IGraphService service = ServiceFactory.ditGraphService();

            if (results.exportDit()) {
                Collection<JungGraph> graphs = mDitPanel.requestGraphs();
                builder.setupForExport(service, graphs, options).build();
                mGraphExportRequests++;
            }
            if (results.exportNoc()) {
                Collection<JungGraph> graphs = mNocPanel.requestGraphs();
                builder.setupForExport(service, graphs, options).build();

            }
            if (results.exportFull()) {
                Collection<JungGraph> graphs = mFullHierarchyPanel.requestGraphs();
                builder.setupForExport(service, graphs, options).build();
                mGraphExportRequests++;
            }
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

        mDitPanel = new MetricPanel<>();
        mNocPanel = new MetricPanel<>();
        mWmcPanel = new MetricPanel<>();
        mFullHierarchyPanel = new FullHierarchyPanel();
    }

    /**
     * Updates the Preferences for analyzed project paths and adds the most recent to the Menu. If an old project path
     * is being replaced (when there are more than 5 paths), this will return the path of the replaced file. This can be
     * used to determine if the file can be deleted or not, if it was downloaded from GitHub. BE CAREFUL with this, to
     * make sure not to delete files that are the user's saved local projects.
     *
     * @param key         The Preferences key.
     * @param files       The list of already saved project paths.
     * @param newFilePath The new file path to save.
     * @return True if an old project path is being replaced.
     */
    private String updateAnalyzedFilePaths(String key, java.util.List<String> files, String newFilePath) {
        String oldPath = null;
        if (files.contains(newFilePath)) return null;
        if (files.size() >= 5) {
            oldPath = files.get(0);
            String[] updatedList = new String[5];
            for (int i = 0; i < 4; i++) {
                String file = files.get(i + 1);
                updatedList[i] = file;
            }
            updatedList[4] = newFilePath;
            files = new ArrayList<>(Arrays.asList(updatedList));
        } else {
            files.add(newFilePath);
        }
        PrefUtils.putPreference(key, files, InheritanceInquiry.class);
        initMenu(InheritanceInquiry.PARENT_FRAME);
        return oldPath;
    }

    private SolutionBuildListener solutionBuildListener() {
        return new SolutionBuildListener() {
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
                IILogger.error(exception);
                DialogUtils.openMessageDialog(message, "Build Error");
                showCard(HELP_CARD);
            }
        };
    }

    private GraphExportListener graphExportListener() {
        return new GraphExportListener() {
            private int mGraphExportCount;

            @Override
            public void onGraphsExport() {
                mGraphExportCount++;
                if (mGraphExportCount == mGraphExportRequests) {
                    mGraphExportCount = 0;
                    mGraphExportRequests = 0;
                    DialogUtils.openMessageDialog("Metric graphs exported!", "Metric Graph Export Completed");
                }
            }

            @Override
            public void onProgressChange(String message, int progress) {

            }

            @Override
            public void onBuildError(String message, Exception exception) {
                DialogUtils.openMessageDialog(message, "Graph Export Error");
            }
        };
    }

    private MetricExportListener metricExportListener() {
        return new MetricExportListener() {
            @Override
            public void onMetricsExported() {
                DialogUtils.openMessageDialog("Metric details exported!", "Metric Export Completed");
            }

            @Override
            public void onProgressChange(String message, int progress) {

            }

            @Override
            public void onBuildError(String message, Exception exception) {
                DialogUtils.openMessageDialog(message, "Metric Export Error");
            }
        };
    }

    private GitDownloadListener gitDownloadListener() {
        return new GitDownloadListener() {
            private boolean mProgressSwitched;

            @Override
            public void onProgressChange(String message, int progress) {
                if (!mProgressSwitched) {
                    showCard(PROGRESS_CARD);
                    mProgressSwitched = true;
                }
                mMainProgressCard.update(message, progress);
            }

            @Override
            public void onBuildError(String message, Exception exception) {
                IILogger.error(exception);
                message += "<br>Check Settings -> Git Help if you're having trouble.";
                DialogUtils.openMessageDialog(message, "Git Download Error");
                showCard(HELP_CARD);
                mProgressSwitched = false;
            }

            @Override
            public void finished(File gitFile, String repoName) {
                mProgressSwitched = false;
                mProjectBuilder.setup(gitFile, repoName).build();
                java.util.List<String> gitDownloads = PrefUtils.getPreferenceList(Constants.GIT_ANALYZED_FILES, InheritanceInquiry.class);
                String oldProjectPath = updateAnalyzedFilePaths(Constants.GIT_ANALYZED_FILES, gitDownloads, gitFile.getAbsolutePath());
                // Delete an old project if there are too many
                IIAppUtils.deleteTempFile(oldProjectPath);
            }
        };
    }

}
