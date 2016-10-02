package com.mcnedward.app.ui.component;

import com.mcnedward.app.utils.Constants;
import com.mcnedward.ii.service.metric.MetricType;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;

/**
 * Created by Edward on 10/2/2016.
 */
public class InfoTextPane extends HtmlTextPane {

    private String mMessage;

    public InfoTextPane(String message) {
        super();
        mMessage = message;
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

    @Override
    protected String getHtml() {
        return mMessage;
    }

}
