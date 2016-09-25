package com.mcnedward.app.ui.component;

import javax.swing.*;
import java.awt.*;

/**
 * Source: http://stackoverflow.com/a/16229082
 * Created by Edward on 9/24/2016.
 */
public class PlaceholderTextField extends JTextField {

    private String placeholder;

    public PlaceholderTextField(final int pColumns, final String placeholderText) {
        super(pColumns);
        placeholder = placeholderText;
    }

    public PlaceholderTextField(final String actualText, final String placeholderText) {
        super(actualText);
        placeholder = placeholderText;
    }

    public PlaceholderTextField(final String actualText, final int pColumns, final String placeholderText) {
        super(actualText, pColumns);
        placeholder = placeholderText;
    }

    @Override
    protected void paintComponent(final Graphics pG) {
        super.paintComponent(pG);

        if (placeholder.length() == 0 || getText().length() > 0) {
            return;
        }

        final Graphics2D g = (Graphics2D) pG;
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getDisabledTextColor());
        g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
                .getMaxAscent() + getInsets().top);
    }

    public void setPlaceholder(final String s) {
        placeholder = s;
    }
}
