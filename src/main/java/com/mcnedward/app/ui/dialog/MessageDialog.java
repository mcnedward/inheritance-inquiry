package com.mcnedward.app.ui.dialog;

import com.mcnedward.app.ui.component.StandardHtmlTextPane;
import com.mcnedward.app.utils.IIAppUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MessageDialog extends JDialog implements ActionListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 150;

    private JPanel mRoot;
    private JButton mBtnOk;
    private JTextPane mLblMessage;
    private JLabel mLblIcon;

    public MessageDialog(JFrame parent) {
        setDialogSize(WIDTH, HEIGHT);
        setContentPane(mRoot);
        setModal(true);
        getRootPane().setDefaultButton(mBtnOk);
        setContentPane(mRoot);
        pack();
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(parent);
        getRootPane().registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void setInfo(String message, String title) {
        setTitle(title);
        ((StandardHtmlTextPane) mLblMessage).setHtml(message);
        pack();
    }

    private void createUIComponents() {
        mLblIcon = new JLabel();
        mLblIcon.setIcon(IIAppUtils.getScaledIcon());
        mLblMessage = new StandardHtmlTextPane();
        mBtnOk = new JButton();
        mBtnOk.addActionListener(e -> dispose());
    }

    public void open() {
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
    }

    private void setDialogSize(int width, int height) {
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
    }
}
