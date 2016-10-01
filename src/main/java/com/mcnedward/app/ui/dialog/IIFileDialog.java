package com.mcnedward.app.ui.dialog;

import com.mcnedward.app.ui.utils.PrefUtils;

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
    private JPanel mOptionsPanel;

    private JFileChooser mFileChooser;
    private File mDirectory;
    private boolean mSucceeded;

    IIFileDialog(Frame parent, String name) {
        super(parent, name, true);
        setContentPane(mRoot);
        initialize(mOptionsPanel);
        mLblInfo.setText(getInfoText());
        mLblForTxtField.setText(getTextFieldText());
        mBtnAction.setText(getActionButtonText());
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
        getRootPane().registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * Does any additional initialization necessary for this {link IIFileDialog}. If the {@link JPanel} optionsPanel is
     * used, you MUST call setVisible(true), since it is defaulting to false.
     *
     * @param optionsPanel The optionsPanel
     */
    protected abstract void initialize(JPanel optionsPanel);

    protected abstract String getInfoText();

    protected abstract String getTextFieldText();

    protected abstract String getActionButtonText();

    protected abstract String getPreferenceKey();

    /**
     * Performs the main action for this IIFileDialog. If you need to provide additional functionality, override this
     * method, implement the extract functions, and then call super.mainAction(String) at the end.
     *
     * @param directoryLocation The location of the directory.
     */
    private void mainAction(String directoryLocation) {
        if (directoryLocation == null || directoryLocation.equals("")) {
            JOptionPane.showMessageDialog(null, "You need to select a directory.", "No Directory", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File directory = new File(directoryLocation);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                JOptionPane.showMessageDialog(null, "Could not create your directory [" + directory.getPath() + "!", "Directory Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (directory.isFile()) {
            JOptionPane.showMessageDialog(null, "You need to choose a directory, not a file.", "Must Be Directory", JOptionPane.ERROR_MESSAGE);
            return;
        }
        setDirectory(directory);
        updatePreferences(directoryLocation);
        close(true);
    }

    private void browseAction() {
        mFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        File file;
        Object selectedItem = mCmbFileLocation.getSelectedItem();
        if (selectedItem != null && !"".equals(selectedItem.toString())) {
            file = new File(selectedItem.toString());
            mFileChooser.setCurrentDirectory(file);
        }

        int result = mFileChooser.showOpenDialog(mRoot);
        if (result == JFileChooser.APPROVE_OPTION) {
            String selectedFile = mFileChooser.getSelectedFile().getAbsolutePath();
            mCmbFileLocation.setSelectedItem(selectedFile);
        }
    }

    private void updatePreferences(String fileLocation) {
        List<String> searchedLocations = PrefUtils.getPreferenceList(getPreferenceKey(), IIFileDialog.class);
        if (!searchedLocations.contains(fileLocation)) {
            PrefUtils.putInListPreference(getPreferenceKey(), fileLocation, IIFileDialog.class);
            mCmbFileLocation.addItem(fileLocation);
        }
    }

    private void checkPreferences() {
        List<String> fileLocations = PrefUtils.getPreferenceList(getPreferenceKey(), IIFileDialog.class);
        if (fileLocations == null) return;
        mCmbFileLocation.removeAllItems();
        for (String location : fileLocations)
            mCmbFileLocation.addItem(location);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        close(false);
    }

    void setDialogSize(int width, int height) {
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
    }

    public void open() {
        checkPreferences();
        mSucceeded = false;
        setVisible(true);
    }

    private void close(boolean succeeded) {
        mSucceeded = succeeded;
        dispose();
    }

    public boolean isSuccessful() {
        return mSucceeded;
    }

    private void setDirectory(File directory) {
        mDirectory = directory;
    }

    public File getDirectory() {
        return mDirectory;
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
                    mainAction((String) mCmbFileLocation.getSelectedItem());
                }
            }
        });
        mCmbFileLocation.setFont(font);
        mCmbFileLocation.setEditable(true);
        mCmbFileLocation.setBorder(BorderFactory.createCompoundBorder(
                mCmbFileLocation.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        mBtnBrowse = new JButton("Browse");
        mBtnBrowse.setFont(font);
        mBtnBrowse.addActionListener(e -> browseAction());

        mBtnAction = new JButton();
        mBtnAction.setFont(font);
        mBtnAction.addActionListener(e -> mainAction((String) mCmbFileLocation.getSelectedItem()));

        mBtnCancel = new JButton("Cancel");
        mBtnCancel.setFont(font);
        mBtnCancel.addActionListener(e -> close(false));

        mFileChooser = new JFileChooser();
        checkPreferences();
    }

}
