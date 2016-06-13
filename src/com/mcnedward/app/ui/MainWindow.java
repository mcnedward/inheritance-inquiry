package com.mcnedward.app.ui;

import java.awt.BorderLayout;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.mcnedward.app.analyze.Analyser;
import com.mcnedward.app.classobject.AnalysisResult;
import com.mcnedward.app.listener.AnalysisListener;

/**
 * @author Edward - Jun 12, 2016
 *
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private static int MIN_WIDTH = 500;
	private static int MIN_HEIGHT = 250;
	private static int WIDTH = 600;
	private static int HEIGHT = 300;
	private static int MAX_WIDTH = 650;
	private static int MAX_HEIGHT = 400;

	private static String PROJECT_LOCATION = "C:/users/edward/dev/workspace/eatingcinci-spring";

	private Analyser analyser;

	// Panels
	private JPanel topLevelPanel;
	private JPanel filePanel;
	private ResultsPanel resultsPanel;
	// Buttons
	private JButton btnBrowse;
	private JButton btnLoad;
	// ComboBox
	private JComboBox<String> comboBox;
	// TextFields
	private JTextField txtProjectLocation;
	private JPanel contentPanel;
	private JProgressBar progressBar;
	private JLabel lblProgress;
	private JPanel progressPanel;

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

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		initialize();
		analyser = new Analyser();
	}

	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Interface Inquiry");

		JLabel lblProjectLocation = new JLabel("Project Location");
		getContentPane().add(lblProjectLocation, BorderLayout.WEST);

		txtProjectLocation = new JTextField();
		getContentPane().add(txtProjectLocation, BorderLayout.CENTER);
		txtProjectLocation.setColumns(10);

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
		topLevelPanel = new JPanel();
		topLevelPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(topLevelPanel);

		initializeComponents();
	}

	private void initializeComponents() {
		topLevelPanel.setLayout(new BorderLayout(0, 10));
		filePanel = new JPanel();
		topLevelPanel.add(filePanel, BorderLayout.NORTH);
		filePanel.setLayout(new GridLayout(2, 1, 0, 0));
		filePanel.setLayout(new BorderLayout(10, 0));

		JLabel lblFileLocation = new JLabel("Project Location:");
		filePanel.add(lblFileLocation, BorderLayout.WEST);
		lblFileLocation.setFont(new Font("Segoe UI", Font.BOLD, 12));

		comboBox = new JComboBox<String>();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 10));
		comboBox.setEditable(true);
		comboBox.addItem(PROJECT_LOCATION);
		filePanel.add(comboBox, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		filePanel.add(panel, BorderLayout.EAST);
		panel.setLayout(new GridLayout(0, 2, 0, 0));

		btnBrowse = new JButton("Browse");
		panel.add(btnBrowse);
		btnBrowse.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browseAction();
			}
		});

		btnLoad = new JButton("Load");
		panel.add(btnLoad);
		btnLoad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadAction();
			}
		});

		contentPanel = new JPanel();
		topLevelPanel.add(contentPanel, BorderLayout.CENTER);

		initProgressPanel();
		initResultsPanel();
	}

	private void initProgressPanel() {
		contentPanel.setLayout(new BorderLayout(0, 0));

		progressPanel = new JPanel();
		progressPanel.setLayout(new BorderLayout(0, 0));
		lblProgress = new JLabel("LOADING");
		lblProgress.setHorizontalAlignment(SwingConstants.CENTER);
		progressPanel.add(lblProgress, BorderLayout.NORTH);

		JPanel panel_1 = new JPanel();
		progressPanel.add(panel_1, BorderLayout.CENTER);
		progressBar = new JProgressBar();
		panel_1.add(progressBar);
		
		int progressWidth = 400;
		int progressHeight = 20;
		Dimension progressBarDimensions = new Dimension(progressWidth, progressHeight);
		progressBar.setPreferredSize(progressBarDimensions);
		progressBar.setMaximumSize(progressBarDimensions);
		progressBar.setMinimumSize(progressBarDimensions);
	}

	private void initResultsPanel() {
		resultsPanel = new ResultsPanel();
	}

	private void browseAction() {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		File file;
		Object selectedItem = comboBox.getSelectedItem();
		if (selectedItem != null && !"".equals(selectedItem.toString()))
			file = new File(selectedItem.toString());
		else {
			file = new File(PROJECT_LOCATION);
		}
		fileChooser.setCurrentDirectory(file);

		int result = fileChooser.showOpenDialog(MainWindow.this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			comboBox.addItem(selectedFile.getAbsolutePath());
			comboBox.setSelectedItem(selectedFile.getAbsolutePath());
			// fileLocation = selectedFile.getAbsolutePath();
			// loadAction();
		}
	}

	private void loadAction() {
		String fileLocation = (String) comboBox.getSelectedItem();
		if (fileLocation == null || fileLocation.equals("")) {
			JOptionPane.showMessageDialog(null, "You need to enter a file.", "Project Load", JOptionPane.WARNING_MESSAGE);
			return;
		}
		File selectedFile = new File(fileLocation);
		if (selectedFile.isDirectory()) {
			boolean isProject = false;
			for (File file : selectedFile.listFiles()) {
				if (file.getName().contains(".project")) {
					isProject = true;
					break;
				}
			}
			if (isProject) {
				load(selectedFile, true);
			} else {
				JOptionPane.showMessageDialog(null, "You need to select a Java project directory or file.", "Project Load",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		if (selectedFile.isFile()) {
			if (selectedFile.getPath().endsWith(".java")) {
				load(selectedFile, true);
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
	private void load(File file, boolean clearList) {
		// Clear the contents and add the progress bar
		contentPanel.removeAll();
		contentPanel.validate();
		contentPanel.repaint();
		contentPanel.add(progressPanel, BorderLayout.CENTER);

		analyser.analyze(file, new AnalysisListener() {
			public void finished(AnalysisResult result) {
				contentPanel.removeAll();
				contentPanel.validate();
				contentPanel.repaint();
				contentPanel.add(resultsPanel, BorderLayout.CENTER);
				resultsPanel.loadClasses(result);
			}

			@Override
			public void onProgressChange(String message, int progress) {
				lblProgress.setText(message);
				progressBar.setValue(progress);
			}
		});
	}
}
