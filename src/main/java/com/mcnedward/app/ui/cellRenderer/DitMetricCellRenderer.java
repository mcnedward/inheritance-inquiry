package com.mcnedward.app.ui.cellRenderer;

import com.mcnedward.ii.service.metric.element.DitMetric;

import javax.swing.*;
import java.awt.*;

/**
 * @author Edward - Jun 12, 2016
 *
 */
public class DitMetricCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;

	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value instanceof DitMetric) {
            DitMetric item = (DitMetric) value;
			setText(String.format("%s DIT: %s", item.elementName, item.metric));
			setToolTipText(item.fullyQualifiedName);
		}
		return this;
	}
}