package com.mcnedward.app.ui.utils;

import com.mcnedward.app.InheritanceInquiry;
import com.mcnedward.app.ui.dialog.InfoDialog;
import com.mcnedward.ii.service.metric.MetricType;

/**
 * Created by Edward on 10/1/2016.
 */
public class DialogUtils {

    private static final String DIT_TITLE = "Depth of Inheritance Tree";
    private static final String DIT_MESSAGE = "<html>The Depth of Inheritance Tree (DIT) is the amount of elements that a single element is inheriting from, all the way to the root of the inheritance hierarchy tree.</p><p>In Java, all classes inherit from java.lang.Object, so the depth of Object would be 0. So if a class <i>A</i> does not explicitly inherit from any other class (does not use the <b>extends</b> keyword), then <i>A</i> will have a DIT of 1. If a class <i>B</i> <b>extends</b> <i>A</i>, the <i>B</i> would have a DIT of 2.</p><p>There isn't really a set standard for how deep a hierarchy tree should go, but a few suggestions say that an ideal level is around 5*. As the DIT increases, the complexity of a class also increase, as there are more methods and variables that can possibly be available to a subclass.</p><p>*See: <a href=\"http://www.javaspecialists.eu/archive/Issue121.html\">How Deep is Your Hierarchy</a> and <a href=\"http://www.devx.com/architect/Article/45611\">Improve the Quality of Java-Based Projects Using Metrics</a></p></html>";

    private static InfoDialog mDitInfoDialog;

    public static void loadDialogs() {
        if (mDitInfoDialog == null)
            mDitInfoDialog = new InfoDialog(InheritanceInquiry.PARENT_FRAME, DIT_TITLE, DIT_MESSAGE);
    }

    public static void openMetricInfoDialog(MetricType metricType) {
        switch (metricType) {
            case DIT:
                openDitInfoDialog();
                break;
            case NOC:
                break;
            case WMC:
                break;
        }
    }

    private static void openDitInfoDialog() {
        if (mDitInfoDialog == null)
            mDitInfoDialog = new InfoDialog(InheritanceInquiry.PARENT_FRAME, DIT_TITLE, DIT_MESSAGE);
        mDitInfoDialog.setVisible(true);
    }

}
