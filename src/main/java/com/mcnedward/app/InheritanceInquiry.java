package com.mcnedward.app;

import java.awt.EventQueue;

import com.mcnedward.app.ui.MainWindow;

/**
 * @author Edward - Aug 28, 2016
 *
 */
public class InheritanceInquiry {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
