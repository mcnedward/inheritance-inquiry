package com.mcnedward.app.ui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.mcnedward.app.classobject.ClassObject;

/**
 * @author Edward - Jun 12, 2016
 *
 */
public class ClassObjectListCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;

	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value instanceof ClassObject) {
			ClassObject classObject = (ClassObject) value;
			setText(classObject.getName());
			setToolTipText(classObject.toString());
		}
		return this;
	}
}