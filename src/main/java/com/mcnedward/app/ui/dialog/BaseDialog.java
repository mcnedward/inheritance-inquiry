package com.mcnedward.app.ui.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * @author Edward - Jun 26, 2016
 *
 */
public abstract class BaseDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private boolean mSucceeded;

	BaseDialog(Frame parent, String name) {
		super(parent, name, true);
		
		initialize();
		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
		getRootPane().registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
	protected abstract void initialize();

	@Override
	public void actionPerformed(ActionEvent e) {
		setVisible(false);
	}
	
	void setDialogSize(int width, int height) {
		setMinimumSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
	}
	
	void closeWithSuccess() {
		mSucceeded = true;
		dispose();
	}
	
	public boolean isSuccessful() {
		return mSucceeded;
	}
}
