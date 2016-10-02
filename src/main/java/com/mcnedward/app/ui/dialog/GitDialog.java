package com.mcnedward.app.ui.dialog;

import com.mcnedward.app.utils.Constants;
import com.mcnedward.app.utils.DialogUtils;
import com.mcnedward.app.utils.PrefUtils;
import com.mcnedward.app.utils.Theme;
import com.mcnedward.ii.builder.GitBuilder;
import com.mcnedward.ii.listener.GitDownloadListener;
import com.mcnedward.ii.utils.ServiceFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * A dialog that allows for loading a project from a remote Git repository.
 * Created by Edward on 10/1/2016.
 */
public class GitDialog extends JDialog implements ActionListener {

    private static final int WIDTH = 650;
    private static final int HEIGHT = 250;

    private JPanel mRoot;
    private JButton mBtnDownload;
    private JButton mBtnCancel;
    private JTextField mTxtUsername;
    private JTextField mTxtPassword;
    private JLabel mLblUsername;
    private JLabel mLblPassword;
    private JComboBox<String> mCmbRemoteUrl;

    private GitDownloadListener mListener;

    public GitDialog(JFrame parent, GitDownloadListener listener) {
        super(parent, "Git Project Load");
        mListener = listener;
        setContentPane(mRoot);
        setDialogSize(WIDTH, HEIGHT);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
        getRootPane().registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void doDownloadAction() {
        String username = mTxtUsername.getText();
        String password = mTxtPassword.getText();
        Object item = mCmbRemoteUrl.getSelectedItem();
        if (item == null) {
            DialogUtils.openMessageDialog("You need to enter the URL to a git remote repository.", "Git Download");
        } else {
            String remoteUrl = item.toString();
            if (remoteUrl == null || remoteUrl.equals("")) {
                DialogUtils.openMessageDialog("You need to enter the URL to a git remote repository.", "Git Download");
                return;
            }
            if (!remoteUrl.endsWith(".git")) {
                DialogUtils.openMessageDialog("You need to enter a git remote repository. This should end with \".git\".", "Git Download");
                return;
            }
            if (username == null || username.equals("")) {
                DialogUtils.openMessageDialog("You need to enter your username.", "Git Download");
                return;
            }
            if (password == null || password.equals("")) {
                DialogUtils.openMessageDialog("You need to enter your password.", "Git Download");
                return;
            }

            updatePreferences(remoteUrl);
            new GitBuilder(mListener).setup(ServiceFactory.gitService(), remoteUrl, username, password).build();
            close();
        }
    }

    private void checkPreferences() {
        java.util.List<String> searchedRemotes = PrefUtils.getPreferenceList(Constants.GIT_SEARCHED_REMOTES, GitDialog.class);
        mCmbRemoteUrl.removeAllItems();
        for (String remote : searchedRemotes)
            mCmbRemoteUrl.addItem(remote);
        mTxtUsername.setText(PrefUtils.getPreference(Constants.GIT_USERNAME, GitDialog.class));
        mTxtPassword.setText(PrefUtils.getPreference(Constants.GIT_PASSWORD, GitDialog.class));
    }

    private void updatePreferences(String remoteUrl) {
        java.util.List<String> searchedRemotes = PrefUtils.getPreferenceList(Constants.GIT_SEARCHED_REMOTES, GitDialog.class);
        if (!searchedRemotes.contains(remoteUrl)) {
            PrefUtils.putInListPreference(Constants.GIT_SEARCHED_REMOTES, remoteUrl, GitDialog.class);
            mCmbRemoteUrl.addItem(remoteUrl);
        }
        PrefUtils.putPreference(Constants.GIT_USERNAME, mTxtUsername.getText(), GitDialog.class);
        PrefUtils.putPreference(Constants.GIT_PASSWORD, mTxtPassword.getText(), GitDialog.class);
    }

    private void setDialogSize(int width, int height) {
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
    }

    public void open() {
        checkPreferences();
        setVisible(true);
    }

    private void close() {
        dispose();
    }

    private void createUIComponents() {
        Font font = new Font("Segoe UI", Font.PLAIN, 18);
        mBtnCancel = new JButton();
        mBtnCancel.setFont(font);
        mBtnCancel.addActionListener(e -> close());

        mBtnDownload = new JButton();
        mBtnDownload.setFont(font);
        mBtnDownload.addActionListener(e -> doDownloadAction());

        mCmbRemoteUrl = new JComboBox<>();
        mCmbRemoteUrl.setFont(font);
        mCmbRemoteUrl.setEditable(true);
        setupComboSize();
        mCmbRemoteUrl.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    doDownloadAction();
                }
            }
        });
        mCmbRemoteUrl.setBorder(BorderFactory.createCompoundBorder(
                mCmbRemoteUrl.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        mTxtUsername = new JTextField();
        mTxtUsername.setFont(font);
        mTxtUsername.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    doDownloadAction();
                }
            }
        });
        mTxtUsername.setBorder(BorderFactory.createCompoundBorder(
                mTxtUsername.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        mTxtPassword = new JPasswordField();
        mTxtPassword.setFont(font);
        mTxtUsername.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    doDownloadAction();
                }
            }
        });
        mTxtPassword.setBorder(BorderFactory.createCompoundBorder(
                mTxtPassword.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        checkPreferences();
    }

    private void setupComboSize() {
        int width = WIDTH - (WIDTH / 4);
        int height = 40;
        mCmbRemoteUrl.setMinimumSize(new Dimension(width, height));
        mCmbRemoteUrl.setMaximumSize(new Dimension(width, height));
        mCmbRemoteUrl.setPreferredSize(new Dimension(width, height));
        mCmbRemoteUrl.setSize(new Dimension(width, height));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        close();
    }

}
