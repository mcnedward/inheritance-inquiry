package com.mcnedward.app.ui.cellRenderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * @author Edward - Jun 12, 2016
 *
 */
public class InheritanceListCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;

	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value instanceof InheritanceListModelItem) {
			InheritanceListModelItem item = (InheritanceListModelItem) value;
			setText(String.format("%s (%s)", item.element.getName(), item.dit));
			setToolTipText(item.element.getName().toString());
		}
		return this;
	}
}