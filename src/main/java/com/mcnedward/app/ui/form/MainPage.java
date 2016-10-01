package com.mcnedward.app.ui.form;

import com.mcnedward.app.InheritanceInquiry;
import com.mcnedward.app.ui.dialog.*;
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
import java.util.Collection;

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
    private JMenuItem mSheetSettings;
    private JMenuItem mGraphSettings;
    private MetricPanel<DitMetric> mDitPanel;
    private MetricPanel<NocMetric> mNocPanel;
    private MetricPanel<WmcMetric> mWmcPanel;
    private FullHierarchyPanel mFullHierarchyPanel;

    private static ProjectBuilder mProjectBuilder;
    private JavaSolution mSolution;

    // Dialogs
    private static ProjectFileDialog mFileDialog;
    private static GitDialog mGitDialog;
    private ExportMetricFileDialog mExportMetricFileDialog;
    private ExportAllGraphsDialog mExportGraphMetricDialog;
    private PreferencesDialog mPreferencesDialog;

    public MainPage() {
        mProjectBuilder = new ProjectBuilder(solutionBuildListener());
        initMenu(InheritanceInquiry.PARENT_FRAME);
        initDialogs(InheritanceInquiry.PARENT_FRAME);
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
        mGitDialog = new GitDialog(parent, gitDownloadListener());
        JMenuItem mntmFromGit = new JMenuItem("From git");
        mnAnalyze.add(mntmFromGit);
        mntmFromGit.addActionListener(e -> mGitDialog.open());

        JMenu exportMenu = new JMenu("Export");
        menuBar.add(exportMenu);
        mGraphSettings = new JMenuItem("Metric Graphs");
        mGraphSettings.addActionListener(e -> openExportGraphDialog());
        mGraphSettings.setEnabled(false);
        exportMenu.add(mGraphSettings);
        mSheetSettings = new JMenuItem("Metric Sheets");
        mSheetSettings.addActionListener(e -> openExportMetricFileDialog());
        mSheetSettings.setEnabled(false);
        exportMenu.add(mSheetSettings);

        JMenu settingMenu = new JMenu("Settings");
        menuBar.add(settingMenu);
        JMenuItem preferenceItem = new JMenuItem("Preferences");
        preferenceItem.addActionListener(e -> mPreferencesDialog.open());
        settingMenu.add(preferenceItem);
        JMenuItem aboutItem = new JMenuItem("About");
//        aboutItem.addActionListener(e -> GitDialog.clearPreference());
        settingMenu.add(aboutItem);
    }

    private void initDialogs(JFrame parent) {
        mExportMetricFileDialog = new ExportMetricFileDialog(parent);
        mExportGraphMetricDialog = new ExportAllGraphsDialog(parent);
        mPreferencesDialog = new PreferencesDialog(parent);
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

    private void openFileDialog() {
        mFileDialog.open();
        if (mFileDialog.isSuccessful()) {
            showCard(PROGRESS_CARD);
            mProjectBuilder.setup(mFileDialog.getDirectory()).build();
        }
    }

    private void openExportMetricFileDialog() {
        mExportMetricFileDialog.open();
        if (mExportMetricFileDialog.isSuccessful()) {
            MetricOptions options = new MetricOptions(mSolution, mExportMetricFileDialog.getDirectory(), mExportMetricFileDialog.exportDit(), mExportMetricFileDialog.exportNoc(), mExportMetricFileDialog.exportWmc(), mExportMetricFileDialog.exportFull());
            options.setUseCsvFormat(mExportMetricFileDialog.useCsvFormat());
            new MetricBuilder(metricExportListener()).setup(ServiceFactory.metricService(), options).build();
        }
    }

    private int mGraphExportRequests;
    private void openExportGraphDialog() {
        mExportGraphMetricDialog.open();
        if (mExportGraphMetricDialog.isSuccessful()) {
            String projectName = mExportGraphMetricDialog.useProjectName() ? mSolution.getProjectName() : null;
            GraphOptions options = new GraphOptions(mExportGraphMetricDialog.getDirectory(), projectName, mExportGraphMetricDialog.usePackages());
            GraphBuilder builder = new GraphBuilder(graphExportListener());
            // The graph service type doesn't matter here, since we're just doing an export
            IGraphService service = ServiceFactory.ditGraphService();

            if (mExportGraphMetricDialog.exportDit()) {
                Collection<JungGraph> graphs = mDitPanel.requestGraphs();
                builder.setupForExport(service, graphs, options).build();
                mGraphExportRequests++;
            }
            if (mExportGraphMetricDialog.exportNoc()) {
                Collection<JungGraph> graphs = mNocPanel.requestGraphs();
                builder.setupForExport(service, graphs, options).build();

            }
            if (mExportGraphMetricDialog.exportFull()) {
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
        mBtnGit.addActionListener(e -> mGitDialog.open());

        mDitPanel = new MetricPanel<>();
        mNocPanel = new MetricPanel<>();
        mWmcPanel = new MetricPanel<>();
        mFullHierarchyPanel = new FullHierarchyPanel();
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
                JOptionPane.showMessageDialog(null, message, "Build Error", JOptionPane.ERROR_MESSAGE);
                IILogger.error(exception);
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
                    JOptionPane.showMessageDialog(null, "Metric graphs exported!", "Metric Graph Export Completed", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            @Override
            public void onProgressChange(String message, int progress) {

            }

            @Override
            public void onBuildError(String message, Exception exception) {
                JOptionPane.showMessageDialog(null, message, "Graph Export Error", JOptionPane.ERROR_MESSAGE);
            }
        };
    }

    private MetricExportListener metricExportListener() {
        return new MetricExportListener() {
            @Override
            public void onMetricsExported() {
                JOptionPane.showMessageDialog(null, "Metric details exported!", "Metric Export Completed", JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void onProgressChange(String message, int progress) {

            }

            @Override
            public void onBuildError(String message, Exception exception) {
                JOptionPane.showMessageDialog(null, message, "Metric Export Error", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(null, message, "Git Download Error", JOptionPane.ERROR_MESSAGE);
            }

            @Override
            public void finished(File gitFile, String repoName) {
                mProjectBuilder.setup(gitFile).build();
            }
        };
    }

}
