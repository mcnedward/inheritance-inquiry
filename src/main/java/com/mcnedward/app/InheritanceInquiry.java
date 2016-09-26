package com.mcnedward.app;

import com.mcnedward.app.ui.main.MainPage;
import com.mcnedward.ii.utils.IILogger;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Edward - Aug 28, 2016
 *
 */
public class InheritanceInquiry {

    public static final String FONT_NAME = "Segoe UI";
    public static int WIDTH;
    public static int HEIGHT;

	public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                JFrame frame = new JFrame("Interface Inquiry");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    fixupIcons();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    IILogger.error("Something went wrong when trying to use the System Look and Feel...", e);
                }

                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                int screenWidth = gd.getDisplayMode().getWidth();
                int screenHeight = gd.getDisplayMode().getHeight();
                WIDTH = (int) (screenWidth * 0.8f);
                HEIGHT = (int) (screenHeight * 0.8f);
                int minWidth = (int) (screenWidth * 0.6f);
                int minHeight = (int) (screenHeight * 0.6f);
                frame.setBounds(100, 100, WIDTH, HEIGHT);
                frame.setLocation(screenWidth / 2 - WIDTH / 2, screenHeight / 2 - HEIGHT / 2);
                frame.setMinimumSize(new Dimension(minWidth, minHeight));
                frame.setMaximumSize(new Dimension(WIDTH, HEIGHT));

                MainPage mainPage = new MainPage(frame);
                frame.setContentPane(mainPage.getRoot());

                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
	}

    private static void fixupIcons() {
        // TODO This is probably not the best way to handle this, but it works for now.
        // Source: http://stackoverflow.com/questions/30774828/bad-swing-ui-scaling-on-high-resolution-ms-surface
        String[] iconOpts = {"OptionPane.errorIcon", "OptionPane.informationIcon", "OptionPane.warningIcon", "OptionPane.questionIcon"};
        for (String key : iconOpts) {
            ImageIcon icon = (ImageIcon) UIManager.get(key);
            Image img = icon.getImage();
            BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics g = bi.createGraphics();
            g.drawImage(img, 0, 0, (int) (img.getWidth(null) * 0.95), (int) (img.getHeight(null) * 0.95), null);
            ImageIcon newIcon = new ImageIcon(bi);
            UIManager.put(key, newIcon);
        }
    }
}
