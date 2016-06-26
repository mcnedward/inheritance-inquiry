package com.mcnedward.app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.mcnedward.ii.GitService;
import com.mcnedward.ii.listener.GitDownloadListener;

/**
 * @author Edward - Jun 25, 2016
 *
 */
public class AuthDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private static int WIDTH = 450;
	private static int HEIGHT = 200;

	private JTextField mTxtUsername;
	private JTextField mTxtPassword;
	private JLabel mLblUsername;
	private JLabel mLblPassword;

	private JPanel mProgressContainer;
	private JPanel mProgressPanel;
	private JLabel mLblProgress;
	private JProgressBar mProgressBar;

	private String mRemoteUrl;
	private File mGitFile;
	private String mRepoName;
	private boolean mSucceeded;

	public AuthDialog(Frame parent, String remoteUrl) {
		super(parent, "Authentication", true);
		mRemoteUrl = remoteUrl;

		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));

		JPanel authPanel = new JPanel();
		authPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		getContentPane().add(authPanel, BorderLayout.NORTH);
		authPanel.setLayout(new BorderLayout(0, 0));
		JPanel panel_2 = new JPanel();
		authPanel.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BorderLayout(0, 0));

		JPanel pnlLabel = new JPanel();
		pnlLabel.setBorder(new EmptyBorder(0, 0, 0, 5));
		panel_2.add(pnlLabel, BorderLayout.WEST);
		pnlLabel.setLayout(new GridLayout(2, 1, 0, 0));

		mLblUsername = new JLabel("Username:");
		mLblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlLabel.add(mLblUsername);
		mLblUsername.setFont(new Font(MainWindow.FONT_NAME, Font.PLAIN, 12));

		mLblPassword = new JLabel("Password:");
		mLblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlLabel.add(mLblPassword);
		mLblPassword.setFont(new Font(MainWindow.FONT_NAME, Font.PLAIN, 12));

		JPanel pnlText = new JPanel();
		panel_2.add(pnlText);
		pnlText.setLayout(new GridLayout(2, 1, 0, 5));

		mTxtUsername = new JTextField();
		pnlText.add(mTxtUsername);
		mTxtUsername.setColumns(10);

		mTxtPassword = new JPasswordField();
		pnlText.add(mTxtPassword);
		mTxtPassword.setColumns(10);

		JPanel panel = new JPanel();
		authPanel.add(panel, BorderLayout.CENTER);

		JButton btnLogin = new JButton("Login");
		panel.add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doLoginAction();
			}
		});

		JButton btnCancel = new JButton("Cancel");
		panel.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		mProgressContainer = new JPanel();
		mProgressContainer.setLayout(new BorderLayout(0, 0));
		add(mProgressContainer);
		mProgressPanel = new JPanel();

		mProgressPanel.setLayout(new BorderLayout(0, 0));
		mLblProgress = new JLabel("LOADING");
		mLblProgress.setFont(new Font(MainWindow.FONT_NAME, Font.PLAIN, 12));
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
		
		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
	}

	private void doLoginAction() {
		mLblUsername.setForeground(Color.BLACK);
		mTxtUsername.setBorder(null);
		mLblPassword.setForeground(Color.BLACK);
		mTxtPassword.setBorder(null);
		mProgressContainer.removeAll();
		mProgressContainer.validate();
		mProgressContainer.repaint();

		String username =  mTxtUsername.getText();
		String password = mTxtPassword.getText();

		if (username == null || username.equals("")) {
			mLblUsername.setForeground(Color.RED);
			mTxtUsername.setBorder(BorderFactory.createLineBorder(Color.RED));
			mSucceeded = false;
			return;
		}
		if (password == null || password.equals("")) {
			mLblPassword.setForeground(Color.RED);
			mTxtPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
			mSucceeded = false;
			return;
		}

		mProgressContainer.add(mProgressPanel, BorderLayout.CENTER);
		GitService.downloadFileFromGit(mRemoteUrl, username, password, new GitDownloadListener() {
			@Override
			public void finished(File gitFile, String repoName) {
				mGitFile = gitFile;
				mRepoName = repoName;
				mSucceeded = true;
				dispose();
			}

			@Override
			public void onProgressChange(String message, int progress) {
				mLblProgress.setText(message);
				mProgressBar.setValue(progress);
			}

			@Override
			public void onDownloadError(String message, Exception exception) {
				mLblProgress.setText(message);
				mProgressBar.setValue(0);
				exception.printStackTrace();
			}
		});
	}

	public File getGitFile() {
		return mGitFile;
	}

	public String getRepoName() {
		return mRepoName;
	}
	
	public boolean isSuccessful() {
		return mSucceeded;
	}
}
