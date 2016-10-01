package com.mcnedward.app.ui.dialog;

import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;

public class InfoDialog extends JDialog implements ActionListener {

    private static final int WIDTH = 750;
    private static final int HEIGHT = 500;

    private JPanel mRoot;
    private JButton mBtnOk;
    private JTextPane mEdtMessage;

    public InfoDialog(Frame parent, String name, String message) {
        super(parent, name, true);
        setDialogSize(WIDTH, HEIGHT);
        setContentPane(mRoot);
        mEdtMessage.setText(message);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
        getRootPane().registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        mBtnOk.addActionListener(e -> setVisible(false));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
    }

    private void setDialogSize(Integer width, Integer height) {
        Dimension d = getPreferredSize();
        setMinimumSize(new Dimension(width, height == null ? (int) d.getHeight() : height));
        setMaximumSize(new Dimension(width, height == null ? (int) d.getHeight() : height));
        setPreferredSize(new Dimension(width, height == null ? (int) d.getHeight() : height));
        setSize(new Dimension(width, height == null ? (int) d.getHeight() : height));
    }

    private void createUIComponents() {
        mEdtMessage = new JTextPane();
        mEdtMessage.setBackground(UIManager.getColor("Label.background"));
        mEdtMessage.setBorder(UIManager.getBorder("Label.border"));
        mEdtMessage.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        mEdtMessage.setContentType("text/html");
        mEdtMessage.addHyperlinkListener(e -> {
            if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                if(Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (Exception exception) {
                        IILogger.error(exception);
                    }
                }
            }
        });
    }
}
