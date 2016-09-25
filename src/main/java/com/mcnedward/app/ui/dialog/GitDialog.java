package com.mcnedward.app.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.mcnedward.app.InheritanceInquiry;
import com.mcnedward.app.ui.utils.PrefUtil;
import com.mcnedward.ii.listener.GitDownloadListener;
import com.mcnedward.ii.service.GitService;

/**
 * @author Edward - Jun 26, 2016
 *
 */
public class GitDialog extends BaseDialog {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 550;
	private static final int HEIGHT = 220;
	private static final String SEARCHED_REMOTES = "searchedRemotes";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";

	private JComboBox<String> mComboBox;
	private JTextField mTxtUsername;
	private JTextField mTxtPassword;
	private JLabel mLblUsername;
	private JLabel mLblPassword;

	private JPanel mProgressContainer;
	private JPanel mProgressPanel;
	private JPanel mBottomPanel;
	private JPanel mInfoPanel;
	private JLabel mLblProgress;
	private JProgressBar mProgressBar;

	private File mGitFile;
	private String mRepoName;

	public GitDialog(Frame parent) {
		super(parent, "Git Load");
	}

	@Override
	protected void initialize() {
        Font font = new Font(InheritanceInquiry.FONT_NAME, Font.PLAIN, 18);
		JPanel authPanel = new JPanel();
		authPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
		getContentPane().add(authPanel, BorderLayout.NORTH);
		authPanel.setLayout(new BorderLayout(0, 0));
		JPanel panel_2 = new JPanel();
		authPanel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));

		JPanel pnlLabel = new JPanel();
		pnlLabel.setBorder(new EmptyBorder(0, 0, 0, 5));
		panel_2.add(pnlLabel, BorderLayout.WEST);
		pnlLabel.setLayout(new GridLayout(3, 1, 0, 0));

		JLabel lblRemoteUrl = new JLabel("Remote URL:");
		pnlLabel.add(lblRemoteUrl);
		lblRemoteUrl.setFont(font);

		mLblUsername = new JLabel("Username:");
		mLblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlLabel.add(mLblUsername);
		mLblUsername.setFont(font);

		mLblPassword = new JLabel("Password:");
		mLblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlLabel.add(mLblPassword);
		mLblPassword.setFont(font);

		JPanel pnlText = new JPanel();
		panel_2.add(pnlText);
		pnlText.setLayout(new GridLayout(3, 1, 0, 5));

		mComboBox = new JComboBox<>();
		pnlText.add(mComboBox);
		mComboBox.setFont(font);
		mComboBox.setEditable(true);

		mTxtUsername = new JTextField();
		pnlText.add(mTxtUsername);
		mTxtUsername.setFont(font);
		mTxtUsername.setColumns(10);
		mTxtUsername.addActionListener(e -> doLoginAction());

		mTxtPassword = new JPasswordField();
		pnlText.add(mTxtPassword);
		mTxtPassword.setFont(font);
		mTxtPassword.setColumns(10);

		JPanel panel_1 = new JPanel();
		panel_2.add(panel_1, BorderLayout.NORTH);
		mTxtPassword.addActionListener(e -> doLoginAction());

		mBottomPanel = new JPanel();
		authPanel.add(mBottomPanel, BorderLayout.SOUTH);
		mBottomPanel.setLayout(new BorderLayout(0, 0));

		mInfoPanel = new JPanel();
		
		JPanel panel_3 = new JPanel();
		mBottomPanel.add(panel_3, BorderLayout.CENTER);

		JButton btnLogin = new JButton("Download");
        btnLogin.setFont(font);
		panel_3.add(btnLogin);

		JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(font);
		panel_3.add(btnCancel);
		btnCancel.addActionListener(e -> dispose());
		btnLogin.addActionListener(e -> doLoginAction());

		initializeProgress();
		checkPreferences();

		setDialogSize(WIDTH, HEIGHT);
	}

	private void initializeProgress() {
		mProgressContainer = new JPanel();
		mProgressContainer.setLayout(new BorderLayout(0, 0));
		mProgressPanel = new JPanel();

		mProgressPanel.setLayout(new BorderLayout(0, 0));
		mLblProgress = new JLabel("LOADING");
		mLblProgress.setFont(new Font(InheritanceInquiry.FONT_NAME, Font.PLAIN, 18));
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

	private void doLoginAction() {
		mLblUsername.setForeground(Color.BLACK);
		mTxtUsername.setBorder(null);
		mLblPassword.setForeground(Color.BLACK);
		mTxtPassword.setBorder(null);
		resetProgress();
		removeInfo();

		String username = mTxtUsername.getText();
		String password = mTxtPassword.getText();
		String remoteUrl = mComboBox.getSelectedItem().toString();

		if (remoteUrl == null || remoteUrl.equals("")) {
			JOptionPane.showMessageDialog(null, "You need to enter the URL to a git remote repository.", "Git Download", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (username == null || username.equals("")) {
			mLblUsername.setForeground(Color.RED);
			mTxtUsername.setBorder(BorderFactory.createLineBorder(Color.RED));
			return;
		}
		if (password == null || password.equals("")) {
			mLblPassword.setForeground(Color.RED);
			mTxtPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
			return;
		}

		showProgress();

		GitService.downloadFileFromGit(remoteUrl, username, password, new GitDownloadListener() {
			@Override
			public void finished(File gitFile, String repoName) {
				mGitFile = gitFile;
				mRepoName = repoName;
				updatePreferences(remoteUrl);
				closeWithSuccess();
			}

			@Override
			public void onProgressChange(String message, int progress) {
				updateProgress(message, progress);
			}

			@Override
			public void onDownloadError(String message, Exception exception) {
				updateProgress(message, 0);
				showError(message);
				exception.printStackTrace();
			}
		});
	}

	protected void resetProgress() {
		mProgressContainer.removeAll();
		mProgressContainer.validate();
		mProgressContainer.repaint();
		if (mProgressContainer != null)
			mBottomPanel.remove(mInfoPanel);
	}

	protected void showProgress() {
		mProgressContainer.add(mProgressPanel, BorderLayout.CENTER);
		mInfoPanel.add(mProgressContainer);
		mBottomPanel.add(mInfoPanel, BorderLayout.SOUTH);
	}

	protected void updateProgress(String message, int progress) {
		mLblProgress.setText(message);
		mProgressBar.setValue(progress);
	}

	private void showError(String error) {
		mInfoPanel.add(new JLabel(error));
		mBottomPanel.add(mInfoPanel, BorderLayout.SOUTH);
	}

	private void removeInfo() {
		if (mInfoPanel != null)
			mBottomPanel.remove(mInfoPanel);
	}

	private void checkPreferences() {
		List<String> searchedRemotes = PrefUtil.<GitDialog> getListPreference(SEARCHED_REMOTES, GitDialog.class);
		for (String remote : searchedRemotes)
			mComboBox.addItem(remote);
		mTxtUsername.setText(PrefUtil.<GitDialog> getPreference(USERNAME, GitDialog.class));
		mTxtPassword.setText(PrefUtil.<GitDialog> getPreference(PASSWORD, GitDialog.class));
	}

	private void updatePreferences(String remoteUrl) {
		List<String> searchedRemotes = PrefUtil.<GitDialog> getListPreference(SEARCHED_REMOTES, GitDialog.class);
		if (!searchedRemotes.contains(remoteUrl)) {
			PrefUtil.<GitDialog> putInListPreference(SEARCHED_REMOTES, remoteUrl, GitDialog.class);
			mComboBox.addItem(remoteUrl);
		}
		PrefUtil.<GitDialog> putPreference(USERNAME, mTxtUsername.getText(), GitDialog.class);
		PrefUtil.<GitDialog> putPreference(PASSWORD, mTxtPassword.getText(), GitDialog.class);
	}

    public static void clearPreference() {
        PrefUtil.clearPreference(SEARCHED_REMOTES, GitDialog.class);
        PrefUtil.clearPreference(USERNAME, GitDialog.class);
        PrefUtil.clearPreference(PASSWORD, GitDialog.class);
    }

	public File getGitFile() {
		return mGitFile;
	}

	public String getRepoName() {
		return mRepoName;
	}

}
