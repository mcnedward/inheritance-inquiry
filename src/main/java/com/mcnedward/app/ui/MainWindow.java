package com.mcnedward.app.ui;

import com.mcnedward.app.ui.dialog.FileDialog;
import com.mcnedward.app.ui.dialog.GitDialog;
import com.mcnedward.ii.builder.ProjectBuilder;
import com.mcnedward.ii.element.JavaSolution;
import com.mcnedward.ii.listener.SolutionBuildListener;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Logger;

/**
 * @author Edward - Jun 12, 2016
 */
public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    public static final String FONT_NAME = "Segoe UI";

    private static final String HELP_CARD = "HelpCard";
    private static final String PROGRESS_CARD = "ProgressCard";
    private static final String RESULTS_CARD = "ResultsCard";
    private static int MIN_WIDTH = 500;
    private static int MIN_HEIGHT = 250;
    private static int WIDTH = 600;
    private static int HEIGHT = 500;
    private static int MAX_WIDTH = 650;
    private static int MAX_HEIGHT = 400;

    private static ProjectBuilder mProjectBuilder;

    // Panels
    private JFrame root = this;
    private JPanel mTopLevelPanel;
    private ResultsPanel mResultsPanel;
    // TextFields
    private JTextField mTxtProjectLocation;
    private JPanel mContentPanel;
    private CardLayout mCardLayout;
    private JProgressBar mProgressBar;
    private JLabel mLblProgress;
    private JMenuBar menuBar;
    private JMenu mnAnalyze;
    private JMenuItem mntmFromFile;
    private JMenuItem mntmFromGit;
    private JPanel mHelpCard;
    private JLabel lblNewLabel;
    private JButton mBtnFromFile;
    private JButton mBtnFromGit;
    private JPanel mProgressCard;
    // Dialogs
    private FileDialog mFileDialog;
    private GitDialog mGitDialog;
    private JPanel mMainContent;

    public MainWindow() {
        fixupIcons();
        initialize();
        mProjectBuilder = new ProjectBuilder(listener());
    }

    private void initialize() {
        initSetup();
        initMenu();
        initHelpCard();
        initProgressCard();
        initResultsCard();
    }

    private void initSetup() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Interface Inquiry");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            System.out.println("Something went wrong when trying to use the System Look and Feel...");
        }

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(100, 100, WIDTH, HEIGHT);
        setLocation(dimension.width / 2 - WIDTH / 2, dimension.height / 2 - HEIGHT / 2);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setMaximumSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));

        JLabel lblProjectLocation = new JLabel("Project Location");
        getContentPane().add(lblProjectLocation, BorderLayout.WEST);

        mTxtProjectLocation = new JTextField();
        getContentPane().add(mTxtProjectLocation, BorderLayout.CENTER);
        mTxtProjectLocation.setColumns(10);

        mTopLevelPanel = new JPanel();
        mTopLevelPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        mTopLevelPanel.setLayout(new BorderLayout(0, 10));
        setContentPane(mTopLevelPanel);

        mContentPanel = new JPanel();
        mCardLayout = new CardLayout(0, 0);
        mContentPanel.setLayout(mCardLayout);
        mTopLevelPanel.add(mContentPanel, BorderLayout.CENTER);
    }

    private void initMenu() {
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        mnAnalyze = new JMenu("Analyze");
        menuBar.add(mnAnalyze);

        mFileDialog = new FileDialog(this);
        mntmFromFile = new JMenuItem("From file");
        mntmFromFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileDialog();
            }
        });
        mnAnalyze.add(mntmFromFile);

        mGitDialog = new GitDialog(this);
        mntmFromGit = new JMenuItem("From git");
        mnAnalyze.add(mntmFromGit);
        mntmFromGit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openGitDialog();
            }
        });
    }

    private void initHelpCard() {
        mHelpCard = new JPanel();
        mContentPanel.add(mHelpCard, HELP_CARD);

        String helpMessage = "Use the Analyze option or the buttons below to select a local file or remote git repository";
        mHelpCard.setLayout(new BorderLayout(0, 0));

        JPanel helpMessagePanel = new JPanel();
        mHelpCard.add(helpMessagePanel, BorderLayout.NORTH);
        lblNewLabel = new JLabel(helpMessage);
        helpMessagePanel.add(lblNewLabel);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel helpButtonsPanel = new JPanel();
        mHelpCard.add(helpButtonsPanel);

        mBtnFromFile = new JButton("From File");
        helpButtonsPanel.add(mBtnFromFile);
        mBtnFromFile.addActionListener(e -> openFileDialog());

        mBtnFromGit = new JButton("From Git");
        helpButtonsPanel.add(mBtnFromGit);
        mBtnFromGit.addActionListener(e -> openGitDialog());
    }

    private void initProgressCard() {
        mProgressCard = new JPanel();
        mContentPanel.add(mProgressCard, PROGRESS_CARD);
        mProgressCard.setLayout(new BorderLayout(0, 0));

        JPanel progressMessagePanel = new JPanel();
        mProgressCard.add(progressMessagePanel, BorderLayout.NORTH);
        mLblProgress = new JLabel("Loading...");
        progressMessagePanel.add(mLblProgress, BorderLayout.NORTH);
        mLblProgress.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panel_1 = new JPanel();
        mProgressCard.add(panel_1, BorderLayout.CENTER);
        mProgressBar = new JProgressBar();
        panel_1.add(mProgressBar);

        int progressWidth = 400;
        int progressHeight = 20;
        Dimension progressBarDimensions = new Dimension(progressWidth, progressHeight);
        mProgressBar.setPreferredSize(progressBarDimensions);
        mProgressBar.setMaximumSize(progressBarDimensions);
        mProgressBar.setMinimumSize(progressBarDimensions);
    }

    private void initResultsCard() {
        mResultsPanel = new ResultsPanel();
        mContentPanel.add(mResultsPanel, RESULTS_CARD);
    }

    /**
     * Loads a file or directory.
     *
     * @param file The File to load
     */
    public void load(File file, boolean deleteAfterBuild) {
        mCardLayout.show(mContentPanel, PROGRESS_CARD);
        mProjectBuilder.build(file);
    }

    private SolutionBuildListener listener() {
        return new SolutionBuildListener() {
            public void finished(JavaSolution solution) {
                IILogger.info("Build complete!");
                mCardLayout.show(mContentPanel, RESULTS_CARD);
                mResultsPanel.loadProject(solution);
            }

            @Override
            public void onProgressChange(String message, int progress) {
                mLblProgress.setText(message);
                mProgressBar.setValue(progress);
                IILogger.info(message);
            }

            @Override
            public void onBuildError(String message, Exception exception) {
                JOptionPane.showMessageDialog(mContentPanel, message, "BuildError", JOptionPane.ERROR_MESSAGE);
                mCardLayout.show(mContentPanel, HELP_CARD);
            }
        };
    }

    private void clearContentPanel() {
        mContentPanel.removeAll();
        mContentPanel.validate();
        mContentPanel.repaint();
    }

    private void openFileDialog() {
        mFileDialog.setVisible(true);
        if (mFileDialog.isSuccessful()) {
            load(mFileDialog.getFile(), false);
        }
    }

    private void openGitDialog() {
        mGitDialog.setVisible(true);
        if (mGitDialog.isSuccessful()) {
            load(mGitDialog.getGitFile(), true);
        }
    }

    private void fixupIcons() {
        // TODO This is probably not the best way to handle this, but it works for now.
        // Source: http://stackoverflow.com/questions/30774828/bad-swing-ui-scaling-on-high-resolution-ms-surface
        String[] iconOpts = {"OptionPane.errorIcon", "OptionPane.informationIcon", "OptionPane.warningIcon", "OptionPane.questionIcon"};
        for (String key : iconOpts) {
            ImageIcon icon = (ImageIcon) UIManager.get(key);
            Image img = icon.getImage();
            BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics g = bi.createGraphics();
            g.drawImage(img, 0, 0, (int) (img.getWidth(null) * 0.95), (int) (img.getHeight(null) * 0.95), null);
            ImageIcon newIcon = new ImageIcon(bi);
            UIManager.put(key, newIcon);
        }
    }
}
