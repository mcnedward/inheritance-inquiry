package com.mcnedward.app.ui.component;

import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;

/**
 * Created by Edward on 10/2/2016.
 */
public abstract class HtmlTextPane extends JTextPane {

    public HtmlTextPane() {
        super();
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

    protected abstract String getHtml();

    public void updateText() {
        setText(getHtml());
    }

    @Override
    public void updateUI() {
        super.updateUI();
        updateText();
    }

}
