package com.mcnedward.app.ui;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * Created by Edward on 9/25/2016.
 */
public class IIScrollBar extends BasicScrollBarUI {

    public static ComponentUI createUI(JComponent c) {
        return new IIScrollBar();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        c.setBackground(Color.GRAY);
        Color original = g.getColor();
        g.setColor(Color.RED);
        g.drawRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        g.setColor(original);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Color original = g.getColor();
        g.setColor(Color.RED);
        g.drawRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
        g.setColor(original);
    }

}
