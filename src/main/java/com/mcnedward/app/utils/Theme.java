package com.mcnedward.app.utils;

import com.mcnedward.app.InheritanceInquiry;
import com.mcnedward.ii.service.metric.MetricType;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

/**
 * Created by Edward on 10/2/2016.
 */
public enum Theme {

    DEFAULT("Default", defaultPanelColor(), defaultLabelColor()),
    RED("Red", new Color(64, 24, 34), new Color(205, 180, 180)),
    BLUE("Blue", new Color(35, 45, 80), new Color(205, 205, 220)),
    GREEN("Green", new Color(15, 100, 15), new Color(175, 195, 175)),
    ORANGE("Orange", new Color(145, 75, 10), new Color(175, 195, 175)),
    PURPLE("Purple", new Color(95, 30, 70), new Color(175, 195, 175)),
    DARK("Dark", new Color(25, 20, 30), new Color(155, 150, 160));

    private static final String PANEL_BACKGROUND = "Panel.background";
    private static final String LABEL_BACKGROUND = "Label.background";
    private static final String LABEL_FOREGROUND = "Label.foreground";
    private static final String TEXT_PANE_BACKGROUND = "TextPane.background";
    private static final String TEXT_PANE_FOREGROUND = "TextPane.foreground";
    private static final String TITLED_BORDER_FONT = "TitledBorder.font";
    private static final String TITLED_BORDER_TITLE_COLOR = "TitledBorder.titleColor";
    private static final String OPTION_PANE_FONT = "OptionPane.font";
    private static final String OPTION_PANE_MESSAGE_FONT = "OptionPane.messageFont";
    private static final String OPTION_PANE_FOREGROUND = "OptionPane.foreground";
    private static final String OPTION_PANE_MESSAGE_FOREGROUND = "OptionPane.messageForeground";
    private static final String RADIO_BUTTON_BACKGROUND = "RadioButton.background";
    private static final String RADIO_BUTTON_FOREGROUND = "RadioButton.foreground";
    private static final String RADIO_BUTTON_FONT = "RadioButton.font";
    private static final String CHECK_BOX_BACKGROUND = "CheckBox.background";
    private static final String CHECK_BOX_FOREGROUND = "CheckBox.foreground";
    private static final String CHECK_BOX_FONT = "CheckBox.font";
    private static final String SCROLL_PANE_BORDER = "ScrollPane.border";

    private String themeName;
    private Color panelColor;
    private Color fontColor;

    Theme(String themeName, Color panelColor, Color fontColor) {
        this.themeName = themeName;
        this.panelColor = panelColor;
        this.fontColor = fontColor;
    }

    public static Theme getCurrentTheme() {
        String currentThemeName = PrefUtils.getPreference(Constants.THEME_NAME, Theme.class);
        Theme currentTheme;
        if (currentThemeName == null) {
            currentTheme = Theme.DEFAULT;
            PrefUtils.putPreference(Constants.THEME_NAME, currentTheme.themeName(), Theme.class);
        } else {
            currentTheme = Theme.getByThemeName(currentThemeName);
        }
        return currentTheme;
    }

    public static void setTheme(Theme newTheme) {
        PrefUtils.putPreference(Constants.THEME_NAME, newTheme.themeName(), Theme.class);
        UIManager.getLookAndFeelDefaults().put(PANEL_BACKGROUND, new ColorUIResource(newTheme.panelColor()));
        UIManager.getLookAndFeelDefaults().put(LABEL_BACKGROUND, new ColorUIResource(newTheme.panelColor()));
        UIManager.getLookAndFeelDefaults().put(LABEL_FOREGROUND, new ColorUIResource(newTheme.fontColor()));
        UIManager.getLookAndFeelDefaults().put(TEXT_PANE_BACKGROUND, new ColorUIResource(newTheme.panelColor()));
        UIManager.getLookAndFeelDefaults().put(TEXT_PANE_FOREGROUND, new ColorUIResource(newTheme.fontColor()));
        UIManager.getLookAndFeelDefaults().put(TITLED_BORDER_TITLE_COLOR, new ColorUIResource(newTheme.fontColor()));
        UIManager.getLookAndFeelDefaults().put(RADIO_BUTTON_BACKGROUND, new ColorUIResource(newTheme.panelColor()));
        UIManager.getLookAndFeelDefaults().put(RADIO_BUTTON_FOREGROUND, new ColorUIResource(newTheme.fontColor()));
        UIManager.getLookAndFeelDefaults().put(CHECK_BOX_BACKGROUND, new ColorUIResource(newTheme.panelColor()));
        UIManager.getLookAndFeelDefaults().put(CHECK_BOX_FOREGROUND, new ColorUIResource(newTheme.fontColor()));
        UIManager.getLookAndFeelDefaults().put(SCROLL_PANE_BORDER, new ColorUIResource(newTheme.panelColor()));
    }

    public static void updateTheme(Theme newTheme) {
        setTheme(newTheme);
        SwingUtilities.updateComponentTreeUI(InheritanceInquiry.PARENT_FRAME);
        for (Component c : DialogUtils.getAppDialogs()) {
            SwingUtilities.updateComponentTreeUI(c);
        }
    }

    public static Theme getByThemeName(String themeName) {
        for (Theme theme : values()) {
            if (theme.themeName.equals(themeName)) return theme;
        }
        return Theme.DEFAULT;
    }

    public static String wrapHtml(String message) {
        return String.format("<html<body style=\"background-color:%s; color: %s;\">%s</body></html>", getBackgroundColorRGB(), getColorRgb(), message);
    }

    private static String getBackgroundColorRGB() {
        Color color = Theme.getCurrentTheme().panelColor();
        return String.format("rgb(%s, %s, %s)", color.getRed(), color.getGreen(), color.getBlue());
    }

    private static String getColorRgb() {
        Color color = Theme.getCurrentTheme().fontColor();
        return String.format("rgb(%s, %s, %s)", color.getRed(), color.getGreen(), color.getBlue());
    }

    public String themeName() {
        return themeName;
    }

    public Color panelColor() {
        return panelColor;
    }

    public Color fontColor() {
        return fontColor;
    }

    private static Color defaultPanelColor() {
        UIDefaults defaults = javax.swing.UIManager.getDefaults();
        return defaults.getColor(PANEL_BACKGROUND);
    }

    private static Color defaultLabelColor() {
        UIDefaults defaults = javax.swing.UIManager.getDefaults();
        return defaults.getColor(LABEL_FOREGROUND);
    }

}
