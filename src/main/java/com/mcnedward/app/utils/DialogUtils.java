package com.mcnedward.app.utils;

import com.mcnedward.app.InheritanceInquiry;
import com.mcnedward.app.ui.dialog.InfoDialog;
import com.mcnedward.ii.service.metric.MetricType;

import java.awt.*;

/**
 * Created by Edward on 10/1/2016.
 */
public class DialogUtils {

    private static final String DIT_TITLE = "Depth of Inheritance Tree";
    public static final String DIT_MESSAGE = "<html<body style=\"background-color: " + getRGB() + "\">The <b>Depth of Inheritance Tree</b> (DIT) is the amount of elements that a single element is inheriting from, all the way to the root of the inheritance hierarchy tree.</p><p>In Java, all classes inherit from java.lang.Object, so the depth of Object would be 0. So if a class <i>A</i> does not explicitly inherit from any other class (does not use the <b>extends</b> keyword), then <i>A</i> will have a DIT of 1. If a class <i>B</i> <b>extends</b> <i>A</i>, the <i>B</i> would have a DIT of 2.</p><p>There isn't really a set standard for how deep a hierarchy tree should go, but a few suggestions say that an ideal level is around 5*. As the DIT increases, the complexity of a class also increase, as there are more methods and variables that can possibly be available to a subclass.</p><p>*See: <a href=\"http://www.javaspecialists.eu/archive/Issue121.html\">How Deep is Your Hierarchy</a> and <a href=\"http://www.devx.com/architect/Article/45611\">Improve the Quality of Java-Based Projects Using Metrics</a></p></body></html>";
    private static final String NOC_TITLE = "Number of Children";
    public static final String NOC_MESSAGE = "<html><p>The <b>Number of Children</b> (NOC) is the amount of elements that directly inherit from another single element, or the number of immediate subclasses.</p><p>For a parent class <i>A</i>, the NOC would be the amount of classes that <b>extends</b> <i>A</i>. So if there are 10 subclasses of <i>A</i>, the NOC would be 10. Classes that are at the bottom of a hierarchy treee would have an NOC of 0. This means that no other classes <b>extends</b> this class.</p><p>When a class has a high NOC, there are a large amount of other classes that will potentially be affected by changes in the parent class.</p></html>";
    private static final String WMC_TITLE = "Weighted Method Count";
    public static final String WMC_MESSAGE = "<html><p>The <b>Weighted Method Count</b> is basically the number of methods defined in a class (though this metric can have more complex descriptions*).</p><p>The amount of methods in a class, and how complex those methods are, can affect how much work is needed to maintain that class. Depending on the access level of those methods (<b>private</b>, <b>protected</b>, <b>public</b>), those methods can also have an effect on other classes.</p><p>*See: <a href=\"https://en.wikipedia.org/wiki/Cyclomatic_complexity\">Cyclomatic complexity</a></p></html>";

    private static InfoDialog mDitInfoDialog;
    private static InfoDialog mNocInfoDialog;
    private static InfoDialog mWmcInfoDialog;

    public static void loadDialogs() {
        if (mDitInfoDialog == null)
            mDitInfoDialog = new InfoDialog(InheritanceInquiry.PARENT_FRAME, DIT_TITLE, MetricType.DIT);
        if (mNocInfoDialog == null)
            mNocInfoDialog = new InfoDialog(InheritanceInquiry.PARENT_FRAME, NOC_TITLE, MetricType.NOC);
        if (mWmcInfoDialog == null)
            mWmcInfoDialog = new InfoDialog(InheritanceInquiry.PARENT_FRAME, WMC_TITLE, MetricType.WMC);
    }

    public static void openMetricInfoDialog(MetricType metricType) {
        switch (metricType) {
            case DIT:
                openDitInfoDialog();
                break;
            case NOC:
                openNocInfoDialog();
                break;
            case WMC:
                openWmcInfoDialog();
                break;
        }
    }

    private static void openDitInfoDialog() {
        if (mDitInfoDialog == null)
            mDitInfoDialog = new InfoDialog(InheritanceInquiry.PARENT_FRAME, DIT_TITLE, MetricType.DIT);
        mDitInfoDialog.setVisible(true);
    }

    private static void openNocInfoDialog() {
        if (mNocInfoDialog == null)
            mNocInfoDialog = new InfoDialog(InheritanceInquiry.PARENT_FRAME, NOC_TITLE, MetricType.NOC);
        mNocInfoDialog.setVisible(true);
    }

    private static void openWmcInfoDialog() {
        if (mWmcInfoDialog == null)
            mWmcInfoDialog = new InfoDialog(InheritanceInquiry.PARENT_FRAME, WMC_TITLE, MetricType.WMC);
        mWmcInfoDialog.setVisible(true);
    }

    private static String getRGB() {
        Color color = Theme.getCurrentTheme().panelColor();
        return String.format("rgb(%s, %s, %s)", color.getRed(), color.getGreen(), color.getBlue());
    }

}
