package com.mcnedward.app.ui.dialog;

import com.mcnedward.app.ui.utils.PrefUtils;
import com.mcnedward.app.ui.utils.SettingsConst;
import com.mcnedward.ii.builder.GitBuilder;
import com.mcnedward.ii.listener.GitDownloadListener;
import com.mcnedward.ii.utils.ServiceFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * A dialog that allows for loading a project from a remote Git repository.
 * Created by Edward on 10/1/2016.
 */
public class GitDialog extends JDialog implements ActionListener {

    private static final int WIDTH = 650;
    private static final int HEIGHT = 200;

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
        mLblUsername.setForeground(Color.BLACK);
        mTxtUsername.setBorder(null);
        mLblPassword.setForeground(Color.BLACK);
        mTxtPassword.setBorder(null);

        String username = mTxtUsername.getText();
        String password = mTxtPassword.getText();
        Object item = mCmbRemoteUrl.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(null, "You need to enter the URL to a git remote repository.", "Git Download", JOptionPane.ERROR_MESSAGE);
        } else {
            String remoteUrl = item.toString();
            if (remoteUrl == null || remoteUrl.equals("")) {
                JOptionPane.showMessageDialog(null, "You need to enter the URL to a git remote repository.", "Git Download", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!remoteUrl.endsWith(".git")) {
                JOptionPane.showMessageDialog(null, "You need to enter a git remote repository. This should end with \".git\".", "Git Download", JOptionPane.ERROR_MESSAGE);
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

            updatePreferences(remoteUrl);
            new GitBuilder(mListener).setup(ServiceFactory.gitService(), remoteUrl, username, password).build();
            close();
        }
    }

    private void checkPreferences() {
        java.util.List<String> searchedRemotes = PrefUtils.getPreferenceList(SettingsConst.GIT_SEARCHED_REMOTES, GitDialog.class);
        mCmbRemoteUrl.removeAllItems();
        for (String remote : searchedRemotes)
            mCmbRemoteUrl.addItem(remote);
        mTxtUsername.setText(PrefUtils.getPreference(SettingsConst.GIT_USERNAME, GitDialog.class));
        mTxtPassword.setText(PrefUtils.getPreference(SettingsConst.GIT_PASSWORD, GitDialog.class));
    }

    private void updatePreferences(String remoteUrl) {
        java.util.List<String> searchedRemotes = PrefUtils.getPreferenceList(SettingsConst.GIT_SEARCHED_REMOTES, GitDialog.class);
        if (!searchedRemotes.contains(remoteUrl)) {
            PrefUtils.putInListPreference(SettingsConst.GIT_SEARCHED_REMOTES, remoteUrl, GitDialog.class);
            mCmbRemoteUrl.addItem(remoteUrl);
        }
        PrefUtils.putPreference(SettingsConst.GIT_USERNAME, mTxtUsername.getText(), GitDialog.class);
        PrefUtils.putPreference(SettingsConst.GIT_USERNAME, mTxtPassword.getText(), GitDialog.class);
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
        mTxtUsername = new JTextField();
        mTxtUsername.setFont(font);
        mTxtPassword = new JPasswordField();
        mTxtPassword.setFont(font);
        checkPreferences();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        close();
    }

}
