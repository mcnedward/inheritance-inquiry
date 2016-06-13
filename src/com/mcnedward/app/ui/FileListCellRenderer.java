package com.mcnedward.app.ui;

import java.awt.Component;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * @author Edward - Jun 12, 2016
 *
 */
public class FileListCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;

	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value instanceof File) {
			File file = (File) value;
			if (file.isDirectory()) {
				setText(file.getName() + " DIR");
				setToolTipText(file.getAbsolutePath());
			} else {
				setText(file.getName());
				setToolTipText(file.getAbsolutePath());
			}
		}
		return this;
	}
}