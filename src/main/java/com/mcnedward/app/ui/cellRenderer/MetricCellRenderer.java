package com.mcnedward.app.ui.cellRenderer;

import com.mcnedward.ii.service.metric.element.DitMetric;
import com.mcnedward.ii.service.metric.element.Metric;
import com.mcnedward.ii.service.metric.element.NocMetric;
import com.mcnedward.ii.service.metric.element.WmcMetric;

import javax.swing.*;
import java.awt.*;

/**
 * @author Edward - Jun 12, 2016
 *
 */
public class MetricCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;

	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        String metricType = "";
        if (value instanceof DitMetric) {
            metricType = "DIT";
        }
        if (value instanceof NocMetric) {
            metricType = "NOC";
        }
        if (value instanceof WmcMetric) {
            metricType = "WMC";
        }
        if (value instanceof Metric) {
            Metric item = (Metric) value;
            setText(String.format("%s %s: %s", item.getElementName(), metricType, item.getMetric()));
            setToolTipText(item.getFullyQualifiedName());
        }
        return this;
	}
}