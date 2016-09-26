package com.mcnedward.app.ui.dialog;

import com.mcnedward.app.ui.utils.PrefUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

public abstract class IIFileDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;

    private JLabel mLblInfo;
    private JLabel mLblForTxtField;
    private JComboBox<String> mCmbFileLocation;
    private JButton mBtnBrowse;
    private JButton mBtnAction;
    private JButton mBtnCancel;
    private JPanel mRoot;

    private JFileChooser mFileChooser;
    private File mFile;
    private boolean mSucceeded;

    IIFileDialog(Frame parent, String name) {
        super(parent, name, true);
        setContentPane(mRoot);
        initialize();
        mLblInfo.setText(getInfoText());
        mLblForTxtField.setText(getTextFieldText());
        mBtnAction.setText(getActionButtonText());
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
        getRootPane().registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    protected abstract void initialize();

    protected abstract void mainAction(String fileLocation);

    protected abstract String getInfoText();

    protected abstract String getTextFieldText();

    protected abstract String getActionButtonText();

    protected abstract String getPreferenceKey();

    private void browseAction() {
        mFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        File file;
        Object selectedItem = mCmbFileLocation.getSelectedItem();
        if (selectedItem != null && !"".equals(selectedItem.toString())) {
            file = new File(selectedItem.toString());
            mFileChooser.setCurrentDirectory(file);
        }

        int result = mFileChooser.showOpenDialog(mRoot);
        if (result == JFileChooser.APPROVE_OPTION) {
            String selectedFile = mFileChooser.getSelectedFile().getAbsolutePath();

            updatePreferences(selectedFile);
            mCmbFileLocation.setSelectedItem(selectedFile);
        }
    }

    private void cancelAction() {
        dispose();
    }

    void updatePreferences(String fileLocation) {
        List<String> searchedLocations = PrefUtil.getListPreference(getPreferenceKey(), IIFileDialog.class);
        if (!searchedLocations.contains(fileLocation)) {
            PrefUtil.putInListPreference(getPreferenceKey(), fileLocation, IIFileDialog.class);
            mCmbFileLocation.addItem(fileLocation);
        }
    }

    private void checkPreferences() {
        List<String> fileLocations = PrefUtil.getListPreference(getPreferenceKey(), IIFileDialog.class);
        if (fileLocations == null) return;
        for (String location : fileLocations)
            mCmbFileLocation.addItem(location);
    }

    public void clearPreference() {
        PrefUtil.clearPreference(getPreferenceKey(), IIFileDialog.class);
    }

    protected void setFile(File file) {
        mFile = file;
    }

    public File getFile() {
        return mFile;
    }

    private void createUIComponents() {
        Font font = new Font("Segoe UI", Font.PLAIN, 18);
        mLblInfo = new JLabel();
        mLblInfo.setFont(font);
        mLblForTxtField = new JLabel();
        mLblForTxtField.setFont(font);

        mCmbFileLocation = new JComboBox<>();
        mCmbFileLocation.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    mainAction((String)mCmbFileLocation.getSelectedItem());
                }
            }
        });
        mCmbFileLocation.setFont(font);
        mCmbFileLocation.setEditable(true);

        mBtnBrowse = new JButton("Browse");
        mBtnBrowse.setFont(font);
        mBtnBrowse.addActionListener(e -> browseAction());

        mBtnAction = new JButton();
        mBtnAction.setFont(font);
        mBtnAction.addActionListener(e -> mainAction((String) mCmbFileLocation.getSelectedItem()));

        mBtnCancel = new JButton("Cancel");
        mBtnCancel.setFont(font);
        mBtnCancel.addActionListener(e -> cancelAction());

        mFileChooser = new JFileChooser();
        checkPreferences();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
    }

    void setDialogSize(int width, int height) {
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
    }

    public void closeWithSuccess() {
        mSucceeded = true;
        dispose();
    }

    public boolean isSuccessful() {
        return mSucceeded;
    }
}
