package com.mcnedward.app.ui.component;

import com.mcnedward.app.utils.DialogUtils;
import com.mcnedward.ii.service.metric.MetricType;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;

/**
 * Created by Edward on 10/2/2016.
 */
public class InfoTextPane extends JTextPane {

    private MetricType mMetricType;

    public InfoTextPane(MetricType metricType) {
        super();
        mMetricType = metricType;
        setBackground(UIManager.getColor("Label.background"));
        setBorder(UIManager.getBorder("Label.border"));
        putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        setContentType("text/html");
        addHyperlinkListener(e -> {
            if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                if(Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (Exception exception) {
                        IILogger.error(exception);
                    }
                }
            }
        });
    }

    public void setText() {
        if (mMetricType == null) return;
        String message = "";
        switch (mMetricType) {
            case DIT:
                message = DialogUtils.DIT_MESSAGE;
                break;
            case NOC:
                message = DialogUtils.NOC_MESSAGE;
                break;
            case WMC:
                message = DialogUtils.WMC_MESSAGE;
                break;
        }
        setText(message);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setText();
    }

}
