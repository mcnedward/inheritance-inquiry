
package com.mcnedward.app.ui.dialog;

import com.mcnedward.app.ui.utils.PrefUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

/**
 * @author Edward - Jun 26, 2016
 */
public class FileDialog extends BaseDialog {
    private static final long serialVersionUID = 1L;
    private static final String SEARCHED_FILES = "searchedFiles";

    private static final int WIDTH = 600;
    private static final int HEIGHT = 100;

    private final JFileChooser mFileChooser;
    private JComboBox<String> mComboBox;
    private File mFile;

    public FileDialog(Frame parent) {
        super(parent, "File Load");
        mFileChooser = new JFileChooser();
    }

    @Override
    protected void initialize() {
        getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JPanel filePanel = new JPanel();
        getContentPane().add(filePanel);
        filePanel.setBorder(new EmptyBorder(10, 0, 0, 0));    // Some padding on top
        filePanel.setLayout(new BorderLayout(10, 0));
        filePanel.setLayout(new BorderLayout(0, 0));

        JPanel panel_1 = new JPanel();
        filePanel.add(panel_1, BorderLayout.CENTER);
        panel_1.setLayout(new BorderLayout(0, 0));

        JLabel lblFileLocation = new JLabel("Project Location:");
        panel_1.add(lblFileLocation, BorderLayout.WEST);
        lblFileLocation.setFont(new Font("Segue UI", Font.PLAIN, 18));
        lblFileLocation.setBorder(new EmptyBorder(0, 0, 0, 10));

        mComboBox = new JComboBox<>();
        panel_1.add(mComboBox);
        mComboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    loadAction();
                }
            }
        });
        mComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        mComboBox.setEditable(true);

        JPanel panel = new JPanel();
        filePanel.add(panel, BorderLayout.SOUTH);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JButton btnBrowse = new JButton("Browse");
        panel.add(btnBrowse);
        btnBrowse.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnBrowse.addActionListener(e -> browseAction());

        JButton btnLoad = new JButton("Load");
        panel.add(btnLoad);
        btnLoad.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnLoad.addActionListener(e -> loadAction());

        setDialogSize(WIDTH, HEIGHT);

        checkPreferences();
    }

    private void browseAction() {
        mFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        File file;
        Object selectedItem = mComboBox.getSelectedItem();
        if (selectedItem != null && !"".equals(selectedItem.toString())) {
            file = new File(selectedItem.toString());
            mFileChooser.setCurrentDirectory(file);
        }

        int result = mFileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String selectedFile = mFileChooser.getSelectedFile().getAbsolutePath();

            updatePreferences(selectedFile);
            mComboBox.setSelectedItem(selectedFile);
        }
    }

    private void loadAction() {
        String fileLocation = (String) mComboBox.getSelectedItem();
        if (fileLocation == null || fileLocation.equals("")) {
            JOptionPane.showMessageDialog(null, "You need to select a file.", "Project Load", JOptionPane.WARNING_MESSAGE);
            return;
        }
        mFile = new File(fileLocation);
        if (!mFile.exists()) {
            JOptionPane.showMessageDialog(null, "That file does not exist!", "File Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (mFile.isFile()) {
            if (!mFile.getPath().endsWith(".java")) {
                JOptionPane.showMessageDialog(null, "You need to select a Java project directory or file.", "Project Load",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        updatePreferences(fileLocation);
        closeWithSuccess();
    }

    private void updatePreferences(String fileLocation) {
        List<String> searchedLocations = PrefUtil.<FileDialog>getListPreference(SEARCHED_FILES, FileDialog.class);
        if (!searchedLocations.contains(fileLocation)) {
            PrefUtil.<FileDialog>putInListPreference(SEARCHED_FILES, fileLocation, FileDialog.class);
            mComboBox.addItem(fileLocation);
        }
    }

    private void checkPreferences() {
        List<String> searchedLocations = PrefUtil.<FileDialog>getListPreference(SEARCHED_FILES, FileDialog.class);
        for (String location : searchedLocations)
            mComboBox.addItem(location);
    }

    public static void clearPreference() {
        PrefUtil.clearPreference(SEARCHED_FILES, FileDialog.class);
    }

    public File getFile() {
        return mFile;
    }

}
