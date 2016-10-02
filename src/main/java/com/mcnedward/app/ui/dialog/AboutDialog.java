package com.mcnedward.app.ui.dialog;

import com.mcnedward.app.ui.component.HtmlTextPane;
import com.mcnedward.app.ui.component.StandardHtmlTextPane;
import com.mcnedward.app.utils.IIAppUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

public class AboutDialog extends JDialog implements ActionListener {

    private static ResourceBundle RESOURCES = ResourceBundle.getBundle("Resources");

    private JPanel mRoot;
    private JLabel mLblAppIcon;
    private JTextPane mLblWebsite;
    private JTextPane mLblEclipse;
    private JTextPane mLblJung;

    public AboutDialog(JFrame parent) {
        setTitle(RESOURCES.getString("about_dialog_title"));
        setContentPane(mRoot);
        ((HtmlTextPane) mLblWebsite).updateText();
        ((HtmlTextPane) mLblEclipse).updateText();
        ((HtmlTextPane) mLblJung).updateText();
        pack();
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(parent);
        getRootPane().registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void createUIComponents() {
        mLblAppIcon = new JLabel();
        mLblAppIcon.setIcon(IIAppUtils.getScaledIcon());

        mLblWebsite = new StandardHtmlTextPane(String.format("<a href=\"%s\">%s</a>", RESOURCES.getString("website"), RESOURCES.getString("website_name")));
        mLblEclipse = new StandardHtmlTextPane(String.format("<a href=\"%s\">%s</a>", RESOURCES.getString("eclipse_jdt_website"), RESOURCES.getString("eclipse_jdt")));
        mLblJung = new StandardHtmlTextPane(String.format("<a href=\"%s\">%s</a>", RESOURCES.getString("jung_website"), RESOURCES.getString("jung")));
    }

    public void open() {
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
    }

}
