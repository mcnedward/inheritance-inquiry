package com.mcnedward.app.ui.cellRenderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.mcnedward.ii.element.JavaElement;

/**
 * @author Edward - Jun 12, 2016
 *
 */
public class JavaElementListCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;

	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value instanceof JavaElement) {
			JavaElement javaElement = (JavaElement) value;
			setText(javaElement.getName());
			setToolTipText(javaElement.toString());
		}
		return this;
	}
}