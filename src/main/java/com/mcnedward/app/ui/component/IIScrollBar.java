package com.mcnedward.app.ui.component;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * Created by Edward on 9/25/2016.
 */
public class IIScrollBar extends BasicScrollBarUI {

    private static final Color THUMB_COLOR = new Color(140, 70, 130, 120);

    public static ComponentUI createUI(JComponent c) {
        return new IIScrollBar();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        g.setColor(THUMB_COLOR);
        g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);

    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension d = super.getPreferredSize(c);
        d.width /= 2f;
        d.height /= 2f;
        return d;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createEmptyButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createEmptyButton();
    }

    private JButton createEmptyButton() {
        JButton jbutton = new JButton();
        jbutton.setPreferredSize(new Dimension(0, 0));
        jbutton.setMinimumSize(new Dimension(0, 0));
        jbutton.setMaximumSize(new Dimension(0, 0));
        return jbutton;
    }

}
