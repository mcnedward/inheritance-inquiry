package com.mcnedward.app.ui.dialog;

import com.mcnedward.app.ui.component.InfoTextPane;
import com.mcnedward.ii.service.metric.MetricType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class InfoDialog extends JDialog implements ActionListener {

    private static final int WIDTH = 750;
    private static final int HEIGHT = 450;

    private JPanel mRoot;
    private JButton mBtnOk;
    private JTextPane mEdtMessage;
    private MetricType mMetricType;

    public InfoDialog(Frame parent, String name, MetricType metricType) {
        super(parent, name, true);
        mMetricType = metricType;
        ((InfoTextPane) mEdtMessage).setText();
        setDialogSize(WIDTH, HEIGHT);
        setContentPane(mRoot);
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
        mEdtMessage = new InfoTextPane(mMetricType);
    }

}
