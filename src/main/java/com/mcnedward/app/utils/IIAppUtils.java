package com.mcnedward.app.utils;

import com.mcnedward.ii.utils.IILogger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
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


    public static ImageIcon getScaledIcon() {
        ImageIcon icon = null;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try {
            InputStream stream = classloader.getResourceAsStream("IILogo.png");
            BufferedImage logo = ImageIO.read(stream);
            icon = new ImageIcon(logo.getScaledInstance(64, 64, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            IILogger.error(e);
        }
        return icon;
    }

    public static URL getIconPath() {
        URL path = null;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try {
            path = classloader.getResource("IILogo.png");
        } catch (Exception e) {
            IILogger.error(e);
        }
        return path;
    }
}
