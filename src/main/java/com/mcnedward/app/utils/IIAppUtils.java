package com.mcnedward.app.utils;

import com.mcnedward.ii.utils.IILogger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Edward on 9/28/2016.
 */
public class IIAppUtils {

    /**
     * Deletes a file, if it exists, from the temp storage location.
     * @param fileLocation The path to the file to delete.
     */
    public static void deleteTempFile(String fileLocation) {
        // Delete an old project if there are too many
        String tempDirectory = System.getProperty("java.io.tmpdir");
        if (fileLocation != null && fileLocation.startsWith(tempDirectory)) {
            File oldProject = new File(fileLocation);
            boolean deleted = deleteDirectoryContents(oldProject);
            if (!deleted) {
                IILogger.error("Could not delete the project: %s", fileLocation);
            }
        }
    }

    private static boolean deleteDirectoryContents(File directory) {
        File[] files = directory.listFiles();
        if (files == null) return true;
        for (File file : files) {
            if (file.isDirectory()) {
                boolean deleted = deleteDirectoryContents(file);
                if (!deleted) return false;
            }
            else {
                boolean deleted = file.delete();
                if (!deleted) return false;
            }
        }
        return directory.delete();
    }

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
