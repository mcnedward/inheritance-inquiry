package com.mcnedward.app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.mcnedward.app.ui.dialog.FileDialog;
import com.mcnedward.app.ui.dialog.GitDialog;
import com.mcnedward.ii.InterfaceInquiry;
import com.mcnedward.ii.element.JavaProject;
import com.mcnedward.ii.listener.ProjectBuildListener;

/**
 * @author Edward - Jun 12, 2016
 *
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	public static final String FONT_NAME = "Segoe UI";
	
	private static int MIN_WIDTH = 500;
	private static int MIN_HEIGHT = 250;
	private static int WIDTH = 600;
	private static int HEIGHT = 500;
	private static int MAX_WIDTH = 650;
	private static int MAX_HEIGHT = 400;

	private static InterfaceInquiry mInterfaceInquiry;

	// Panels
	private JPanel mTopLevelPanel;
	private ResultsPanel mResultsPanel;
	// TextFields
	private JTextField mTxtProjectLocation;
	private JPanel mContentPanel;
	private JProgressBar mProgressBar;
	private JLabel mLblProgress;
	private JPanel mProgressPanel;
	private JMenuBar menuBar;
	private JMenu mnAnalyze;
	private JMenuItem mntmFromFile;
	private JMenuItem mntmFromGit;
	private JPanel panelHelp;
	private JLabel lblNewLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainWindow() {
		initialize();
		mInterfaceInquiry = new InterfaceInquiry();
	}

	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Interface Inquiry");

		JLabel lblProjectLocation = new JLabel("Project Location");
		getContentPane().add(lblProjectLocation, BorderLayout.WEST);

		mTxtProjectLocation = new JTextField();
		getContentPane().add(mTxtProjectLocation, BorderLayout.CENTER);
		mTxtProjectLocation.setColumns(10);

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
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnAnalyze = new JMenu("Analyze");
		menuBar.add(mnAnalyze);
		
		final FileDialog fileDialog = new FileDialog(this);
		mntmFromFile = new JMenuItem("From file");
		mntmFromFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileDialog.setVisible(true);
				if (fileDialog.isSuccessful()) {
					load(fileDialog.getFile(), fileDialog.getFile().getName(), false);
				}
			}
		});
		mnAnalyze.add(mntmFromFile);
		

		GitDialog gitDialog = new GitDialog(this);
		mntmFromGit = new JMenuItem("From git");
		mnAnalyze.add(mntmFromGit);
		mntmFromGit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gitDialog.setVisible(true);
				if (gitDialog.isSuccessful()) {
					load(gitDialog.getGitFile(), gitDialog.getRepoName(), true);
				}
			}
		});
		
		mTopLevelPanel = new JPanel();
		mTopLevelPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mTopLevelPanel);

		initializeComponents();
	}

	private void initializeComponents() {
		mTopLevelPanel.setLayout(new BorderLayout(0, 10));
		mContentPanel = new JPanel();
		mTopLevelPanel.add(mContentPanel, BorderLayout.CENTER);
		mResultsPanel = new ResultsPanel();
		initProgressPanel();
	}
	
	private void initProgressPanel() {
		mContentPanel.setLayout(new BorderLayout(0, 0));
		
		panelHelp = new JPanel();
		mContentPanel.add(panelHelp, BorderLayout.CENTER);
		panelHelp.setLayout(new BorderLayout(0, 0));
		
		String helpMessage = "Use Analyze to select a local file or remote git repository";
		lblNewLabel = new JLabel(helpMessage);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panelHelp.add(lblNewLabel, BorderLayout.CENTER);

		mProgressPanel = new JPanel();
		mProgressPanel.setLayout(new BorderLayout(0, 0));
		mLblProgress = new JLabel("LOADING");
		mLblProgress.setHorizontalAlignment(SwingConstants.CENTER);
		mProgressPanel.add(mLblProgress, BorderLayout.NORTH);

		JPanel panel_1 = new JPanel();
		mProgressPanel.add(panel_1, BorderLayout.CENTER);
		mProgressBar = new JProgressBar();
		panel_1.add(mProgressBar);

		int progressWidth = 400;
		int progressHeight = 20;
		Dimension progressBarDimensions = new Dimension(progressWidth, progressHeight);
		mProgressBar.setPreferredSize(progressBarDimensions);
		mProgressBar.setMaximumSize(progressBarDimensions);
		mProgressBar.setMinimumSize(progressBarDimensions);
	}

	/**
	 * Loads a file or directory.
	 * 
	 * @param file
	 *            The File to load
	 * @param clearList
	 *            True if the project explorer list should be cleared
	 */
	public void load(File file, String projectName, boolean deleteAfterBuild) {
		// Clear the contents and add the progress bar
		mContentPanel.removeAll();
		mContentPanel.validate();
		mContentPanel.repaint();
		mContentPanel.add(mProgressPanel, BorderLayout.CENTER);

		mInterfaceInquiry.buildProject(file, projectName, deleteAfterBuild, new ProjectBuildListener() {
			public void finished(JavaProject project) {
				mContentPanel.removeAll();
				mContentPanel.validate();
				mContentPanel.repaint();
				mContentPanel.add(mResultsPanel, BorderLayout.CENTER);
				mResultsPanel.loadProject(project);
			}

			@Override
			public void onProgressChange(String message, int progress) {
				mLblProgress.setText(message);
				mProgressBar.setValue(progress);
			}
			
			@Override
			public void onBuildError(String message, Exception exception) {
				mLblProgress.setText(message);
				mLblProgress.setForeground(Color.RED);
				exception.printStackTrace();
			}
		});
	}
}
