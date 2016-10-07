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

    private static final String[] TYPES = new String[] {"Basic Auth", "Token"};
    private static final String BASIC_AUTH_LABEL_CARD = "BasicAuthLabelCard";
    private static final String BASIC_AUTH_TEXT_CARD = "BasicAuthTextCard";
    private static final String TOKEN_LABEL_CARD = "TokenLabelCard";
    private static final String TOKEN_TEXT_CARD = "TokenTextCard";

    private JPanel mRoot;
    private JButton mBtnDownload;
    private JButton mBtnCancel;
    private JTextField mTxtUsername;
    private JPasswordField mTxtPassword;
    private JComboBox<String> mCmbRemoteUrl;
    private JButton mBtnHelp;
    private JPasswordField mPswToken;
    private JComboBox<String> mCmbRemoteUrlToken;
    private JComboBox<String> mCmbType;
    private JPanel mCardsLabels;
    private JPanel mCardsText;

    private GitDownloadListener mListener;
    private boolean mIsToken;

    public GitDialog(JFrame parent, GitDownloadListener listener) {
        super(parent, "GitHub Project Load");
        mListener = listener;
        setContentPane(mRoot);
        setDialogSize(WIDTH, HEIGHT);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
        getRootPane().registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void doDownloadAction() {
        if (mIsToken) {
            downloadForToken();
        } else {
            downloadForBasicAuth();
        }
    }

    private void downloadForToken() {
        char[] token = mPswToken.getPassword();
        Object remote = mCmbRemoteUrl.getSelectedItem();
        if (remote == null || remote.toString().equals("")) {
            DialogUtils.openMessageDialog("You need to enter the URL to a git remote repository.", "Git Download");
        } else {
            String remoteUrl = remote.toString();
            if (!remoteUrl.endsWith(".git")) {
                DialogUtils.openMessageDialog("You need to enter a git remote repository. This should end with \".git\".", "Git Download");
                return;
            }
            if (token == null || token.length == 0) {
                DialogUtils.openMessageDialog("You need to enter your token.", "Git Download");
                return;
            }
            updatePreferences(remoteUrl);
            new GitBuilder(mListener).setup(ServiceFactory.gitService(), remoteUrl, token).build();
            close();
        }
    }

    private void downloadForBasicAuth() {
        String username = mTxtUsername.getText();
        char[] password = mTxtPassword.getPassword();
        Object remote = mCmbRemoteUrl.getSelectedItem();
        if (remote == null || remote.toString().equals("")) {
            DialogUtils.openMessageDialog("You need to enter the URL to a git remote repository.", "Git Download");
        } else {
            String remoteUrl = remote.toString();
            if (!remoteUrl.endsWith(".git")) {
                DialogUtils.openMessageDialog("You need to enter a git remote repository. This should end with \".git\".", "Git Download");
                return;
            }
            if (username == null || username.equals("")) {
                DialogUtils.openMessageDialog("You need to enter your username.", "Git Download");
                return;
            }
            if (password == null || password.length == 0) {
                DialogUtils.openMessageDialog("You need to enter your password.", "Git Download");
                return;
            }
            updatePreferences(remoteUrl);
            new GitBuilder(mListener).setup(ServiceFactory.gitService(), remoteUrl, username, password).build();
            close();
        }
    }

    private boolean validate(Object remote, char[] password) {
        if (remote == null || remote.toString().equals("")) {
            DialogUtils.openMessageDialog("You need to enter the URL to a git remote repository.", "Git Download");
            return false;
        } else {
            String remoteUrl = remote.toString();
            if (remoteUrl == null || remoteUrl.equals("")) {
                DialogUtils.openMessageDialog("You need to enter the URL to a git remote repository.", "Git Download");
                return false;
            }
            if (!remoteUrl.endsWith(".git")) {
                DialogUtils.openMessageDialog("You need to enter a git remote repository. This should end with \".git\".", "Git Download");
                return false;
            }
            if (password == null || password.length == 0) {
                DialogUtils.openMessageDialog("You need to enter your password.", "Git Download");
                return false;
            }
            return true;
        }
    }

    private void checkPreferences() {
        java.util.List<String> searchedRemotes = PrefUtils.getPreferenceList(Constants.GIT_SEARCHED_REMOTES, GitDialog.class);
        mCmbRemoteUrl.removeAllItems();
        mCmbRemoteUrlToken.removeAllItems();
        for (String remote : searchedRemotes) {
            mCmbRemoteUrl.addItem(remote);
            mCmbRemoteUrlToken.addItem(remote);
        }
        mTxtUsername.setText(PrefUtils.getPreference(Constants.GIT_USERNAME, GitDialog.class));
    }

    private void updatePreferences(String remoteUrl) {
        java.util.List<String> searchedRemotes = PrefUtils.getPreferenceList(Constants.GIT_SEARCHED_REMOTES, GitDialog.class);
        if (!searchedRemotes.contains(remoteUrl)) {
            PrefUtils.putInListPreference(Constants.GIT_SEARCHED_REMOTES, remoteUrl, GitDialog.class);
            mCmbRemoteUrl.addItem(remoteUrl);
        }
        PrefUtils.putPreference(Constants.GIT_USERNAME, mTxtUsername.getText(), GitDialog.class);
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

    private void showCard() {
        if (mIsToken) {
            ((CardLayout) mCardsLabels.getLayout()).show(mCardsLabels, TOKEN_LABEL_CARD);
            ((CardLayout) mCardsText.getLayout()).show(mCardsText, TOKEN_TEXT_CARD);
        } else {
            ((CardLayout) mCardsLabels.getLayout()).show(mCardsLabels, BASIC_AUTH_LABEL_CARD);
            ((CardLayout) mCardsText.getLayout()).show(mCardsText, BASIC_AUTH_TEXT_CARD);
        }
    }

    private void createUIComponents() {
        Font font = new Font("Segoe UI", Font.PLAIN, 18);
        mBtnCancel = new JButton();
        mBtnCancel.setFont(font);
        mBtnCancel.addActionListener(e -> close());

        mBtnDownload = new JButton();
        mBtnDownload.setFont(font);
        mBtnDownload.addActionListener(e -> doDownloadAction());

        mBtnHelp  = new JButton();
        mBtnHelp.setFont(font);
        mBtnHelp.addActionListener(e -> DialogUtils.openGitHelpDialog());

        mCmbType = new JComboBox<>(TYPES);
        mCmbType.setFont(font);
        mCmbType.addActionListener(e -> {
            // Switch the card, and set the text of the combo boxes to match.
            // TODO Probably a much better way to do this, but this will work for now
            if (mCmbType.getSelectedItem() == TYPES[0]) {
                mIsToken = false;
                showCard();
            }
            else {
                mIsToken = true;
                showCard();
            }
        });

        setupBasicAuthCard(font);
        setupTokenCard(font);

        checkPreferences();
    }

    private void setupBasicAuthCard(Font font) {
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
        mTxtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    doDownloadAction();
                }
            }
        });
        mTxtPassword.setBorder(BorderFactory.createCompoundBorder(
                mTxtPassword.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    private void setupTokenCard(Font font) {
        mCmbRemoteUrlToken = new JComboBox<>();
        mCmbRemoteUrlToken.setFont(font);
        mCmbRemoteUrlToken.setEditable(true);
        setupComboSize();
        mCmbRemoteUrlToken.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    doDownloadAction();
                }
            }
        });
        mCmbRemoteUrlToken.setBorder(BorderFactory.createCompoundBorder(
                mCmbRemoteUrlToken.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        mPswToken = new JPasswordField();
        mPswToken.setFont(font);
        mPswToken.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    doDownloadAction();
                }
            }
        });
        mPswToken.setBorder(BorderFactory.createCompoundBorder(
                mPswToken.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
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
