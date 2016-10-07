package com.mcnedward.app.utils;

import java.util.ResourceBundle;

/**
 * Created by Edward on 10/1/2016.
 */
public class Constants {

    private static ResourceBundle RESOURCES = ResourceBundle.getBundle("Resources");

    // Info Dialog
    public static final String DIT_TITLE = "Depth of Inheritance Tree";
    public static final String DIT_MESSAGE = "The <b>Depth of Inheritance Tree</b> (DIT) is the amount of elements that a single element is inheriting from, all the way to the root of the inheritance hierarchy tree.</p><p>In Java, all classes inherit from java.lang.Object, so the depth of Object would be 0. So if a class <i>A</i> does not explicitly inherit from any other class (does not use the <b>extends</b> keyword), then <i>A</i> will have a DIT of 1. If a class <i>B</i> <b>extends</b> <i>A</i>, the <i>B</i> would have a DIT of 2.</p><p>There isn't really a set standard for how deep a hierarchy tree should go, but a few suggestions say that an ideal level is around 5*. As the DIT increases, the complexity of a class also increase, as there are more methods and variables that can possibly be available to a subclass.</p><p>*See: <a href=\"http://www.javaspecialists.eu/archive/Issue121.html\">How Deep is Your Hierarchy</a> and <a href=\"http://www.devx.com/architect/Article/45611\">Improve the Quality of Java-Based Projects Using Metrics</a></p>";
    public static final String NOC_TITLE = "Number of Children";
    public static final String NOC_MESSAGE = "<p>The <b>Number of Children</b> (NOC) is the amount of elements that directly inherit from another single element, or the number of immediate subclasses.</p><p>For a parent class <i>A</i>, the NOC would be the amount of classes that <b>extends</b> <i>A</i>. So if there are 10 subclasses of <i>A</i>, the NOC would be 10. Classes that are at the bottom of a hierarchy treee would have an NOC of 0. This means that no other classes <b>extends</b> this class.</p><p>When a class has a high NOC, there are a large amount of other classes that will potentially be affected by changes in the parent class.</p>";
    public static final String WMC_TITLE = "Weighted Method Count";
    public static final String WMC_MESSAGE = "<p>The <b>Weighted Method Count</b> is basically the number of methods defined in a class (though this metric can have more complex descriptions*).</p><p>The amount of methods in a class, and how complex those methods are, can affect how much work is needed to maintain that class. Depending on the access level of those methods (<b>private</b>, <b>protected</b>, <b>public</b>), those methods can also have an effect on other classes.</p><p>*See: <a href=\"https://en.wikipedia.org/wiki/Cyclomatic_complexity\">Cyclomatic complexity</a></p>";

    // Git Help Dialog
    public static final String GIT_HELP_TITLE = RESOURCES.getString("git_help_title");
    public static String gitHelpMessage() {
        return String.format("<p>%s</p><ul><li>%s</li><li>%s</li><li>%s</li><li>%s</li><li>%s</li><li>%s</li><li>%s</li><li>%s</li></ul><p>%s</p><p>%s</p>",
                RESOURCES.getString("git_help_description"),
                RESOURCES.getString("git_step_1"),
                RESOURCES.getString("git_step_2"),
                RESOURCES.getString("git_step_3"),
                RESOURCES.getString("git_step_4"),
                RESOURCES.getString("git_step_5"),
                RESOURCES.getString("git_step_6"),
                RESOURCES.getString("git_step_7"),
                RESOURCES.getString("git_step_8"),
                RESOURCES.getString("git_info"),
                RESOURCES.getString("git_info_delete"));
    }

    // Graph Settings
    public static final String GRAPH_SETTINGS_KEY = "GraphSettingsKey";
    public static final String GRAPH_SHAPE = "GraphShape";
    public static final String FONT_COLOR = "FontColor";
    public static final String LABEL_COLOR = "LabelColor";
    public static final String ARROW_COLOR = "ArrowColor";
    public static final String EDGE_COLOR = "EdgeColor";
    public static final String INTERFACE_LABEL_COLOR = "InterfaceLabelColor";
    public static final String INTERFACE_ARROW_COLOR = "InterfaceArrowColor";
    public static final String INTERFACE_EDGE_COLOR = "InterfaceEdgeColor";
    public static final String FONT_SIZE = "FontSize";
    public static final String H_DISTANCE = "HDistance";
    public static final String V_DISTANCE = "VDistance";
    public static final String USE_FULL_NAME = "UseFullName";
    public static final String EDGE_LABEL = "EdgeLabel";
    public static final String UPDATE_ALL = "UpdateAll";

    // Project File Dialog Settings
    public static final String PROJECT_FILE_DIALOG_KEY = "ProjectFileDialogKey";

    // Export Metric File Dialog
    public static final String EXPORT_METRIC_FILE_DIALOG_KEY = "ExportMetricFileDialogKey";

    // Export Graph Dialog
    public static final String EXPORT_GRAPH_DIALOG = "ExportGraphDialogKey";

    // Export All Graphs Dialog
    public static final String EXPORT_ALL_GRAPHS_DIALOG_KEY = "ExportAllGraphsDialogKey";

    // Git Settings
    public static final String GIT_SEARCHED_REMOTES = "GitSearchedRemotes";
    public static final String GIT_USERNAME = "GitUsername";

    // Analyzed Files
    public static final String LOCAL_ANALYZED_FILES = "LocalAnalyzedFiles";
    public static final String GIT_ANALYZED_FILES = "GitAnalyzedFiles";

    // Theme Settings
    public static final String THEME_NAME = "ThemeName";

    // Use Full Screen for the app
    public static final String FULL_SCREEN = "FullScreen";

}
