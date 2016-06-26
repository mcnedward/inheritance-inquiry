package com.mcnedward.app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

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

	private static String PROJECT_LOCATION = "C:/users/edward/dev/workspace/eatingcinci-spring";

	private static InterfaceInquiry mInterfaceInquiry;

	// Panels
	private JPanel mTopLevelPanel;
	private JPanel mFilePanel;
	private ResultsPanel mResultsPanel;
	// Buttons
	private JButton mBtnBrowse;
	private JButton mBtnLoad;
	// ComboBox
	private JComboBox<String> mComboBox;
	// TextFields
	private JTextField mTxtProjectLocation;
	private JTextField mTxtRemoteUrl;
	private JPanel mContentPanel;
	private JProgressBar mProgressBar;
	private JLabel mLblProgress;
	private JPanel mProgressPanel;
	private JTabbedPane tabbedPane;

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
		mTopLevelPanel = new JPanel();
		mTopLevelPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mTopLevelPanel);

		initializeComponents();
	}

	private void initializeComponents() {
		mTopLevelPanel.setLayout(new BorderLayout(0, 10));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		mTopLevelPanel.add(tabbedPane, BorderLayout.NORTH);
		mFilePanel = new JPanel();
		tabbedPane.addTab("File", null, mFilePanel, null);
		mFilePanel.setLayout(new GridLayout(2, 1, 0, 0));
		mFilePanel.setLayout(new BorderLayout(10, 0));

		JLabel lblFileLocation = new JLabel("Project Location:");
		mFilePanel.add(lblFileLocation, BorderLayout.WEST);
		lblFileLocation.setFont(new Font(FONT_NAME, Font.BOLD, 12));

		mComboBox = new JComboBox<String>();
		mComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		mComboBox.setEditable(true);
		mComboBox.addItem(PROJECT_LOCATION);
		mFilePanel.add(mComboBox, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		mFilePanel.add(panel, BorderLayout.EAST);
		panel.setLayout(new GridLayout(0, 2, 0, 0));

		mBtnBrowse = new JButton("Browse");
		panel.add(mBtnBrowse);
		mBtnBrowse.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
		mBtnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browseAction();
			}
		});

		mBtnLoad = new JButton("Load");
		panel.add(mBtnLoad);
		mBtnLoad.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
		mBtnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadAction();
			}
		});

		mContentPanel = new JPanel();
		mTopLevelPanel.add(mContentPanel, BorderLayout.CENTER);

		initGitPanel();
		initProgressPanel();
		initResultsPanel();
	}
	
	private void initGitPanel() {
		JPanel gitPanel = new JPanel();
		tabbedPane.addTab("Git", null, gitPanel, null);
		gitPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblUrl = new JLabel("Remote URL:");
		lblUrl.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, 12));
		lblUrl.setBorder(new EmptyBorder(0, 0, 0, 10));
		gitPanel.add(lblUrl, BorderLayout.WEST);
		
		mTxtRemoteUrl = new JTextField();
		mTxtRemoteUrl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		gitPanel.add(mTxtRemoteUrl);
		mTxtRemoteUrl.setColumns(10);
		mTxtRemoteUrl.setText("https://github.com/mcnedward/program-analysis.git");
		
		JButton btnDownload = new JButton("Download");
		btnDownload.setFont(new Font(MainWindow.FONT_NAME, Font.PLAIN, 12));
		gitPanel.add(btnDownload, BorderLayout.EAST);
		
		final JFrame parent = this;
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String remoteUrl = mTxtRemoteUrl.getText();
				if (remoteUrl == null || remoteUrl.equals("")) {
					JOptionPane.showMessageDialog(null, "You need to enter the URL to a git remote repository.", "Git Download", JOptionPane.ERROR_MESSAGE);
					return;
				}
				AuthDialog dialog = new AuthDialog(parent, remoteUrl);
				dialog.setVisible(true);
				if (dialog.isSuccessful()) {
					load(dialog.getGitFile(), dialog.getRepoName(), true);
				}
			}
		});
	}

	private void initProgressPanel() {
		mContentPanel.setLayout(new BorderLayout(0, 0));

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

	private void initResultsPanel() {
		mResultsPanel = new ResultsPanel();
	}

	private void browseAction() {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		File file;
		Object selectedItem = mComboBox.getSelectedItem();
		if (selectedItem != null && !"".equals(selectedItem.toString()))
			file = new File(selectedItem.toString());
		else {
			file = new File(PROJECT_LOCATION);
		}
		fileChooser.setCurrentDirectory(file);

		int result = fileChooser.showOpenDialog(MainWindow.this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			mComboBox.addItem(selectedFile.getAbsolutePath());
			mComboBox.setSelectedItem(selectedFile.getAbsolutePath());
			// fileLocation = selectedFile.getAbsolutePath();
			// loadAction();
		}
	}

	private void loadAction() {
		String fileLocation = (String) mComboBox.getSelectedItem();
		if (fileLocation == null || fileLocation.equals("")) {
			JOptionPane.showMessageDialog(null, "You need to select a file.", "Project Load", JOptionPane.WARNING_MESSAGE);
			return;
		}
		File selectedFile = new File(fileLocation);
		// if (selectedFile.isDirectory()) {
		// boolean isProject = false;
		// for (File file : selectedFile.listFiles()) {
		// if (file.getName().contains(".project")) {
		// isProject = true;
		// break;
		// }
		// }
		// if (isProject) {
		load(selectedFile, selectedFile.getName(), false); // Don't care about .project files for now
		// } else {
		// JOptionPane.showMessageDialog(null, "You need to select a Java project directory or file.", "Project Load",
		// JOptionPane.ERROR_MESSAGE);
		// }
		// }
		if (selectedFile.isFile()) {
			if (selectedFile.getPath().endsWith(".java")) {
				load(selectedFile, selectedFile.getName(), false);
			} else {
				JOptionPane.showMessageDialog(null, "You need to select a Java project directory or file.", "Project Load",
						JOptionPane.ERROR_MESSAGE);
			}
		}
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
