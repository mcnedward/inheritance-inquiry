package com.mcnedward.app.utils;

import com.mcnedward.ii.utils.IILogger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Edward on 9/28/2016.
 */
public class IIAppUtils {

    public static java.util.List<String> getUIManagerKeys() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        java.util.List<String> keys = new ArrayList<>();
        try {
            InputStream stream = classloader.getResourceAsStream("UIManagerKeys.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            String line;
            while ((line = reader.readLine()) != null) {
                keys.add(line);
            }
            reader.close();
        } catch (Exception e) {
            IILogger.error(e);
        }
        return keys;
    }

    public static void loadIcon(JFrame frame) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try {
            InputStream stream = classloader.getResourceAsStream("IILogo.png");
            BufferedImage logo = ImageIO.read(stream);
            frame.setIconImage(logo);
        } catch (Exception e) {
            IILogger.error(e);
        }
    }

    public static void fixupIcons() {
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
