package com.mcnedward.app.ui.utils;

import com.mcnedward.app.ui.form.GraphPanel;
import com.mcnedward.ii.service.graph.element.GraphOptions;
import com.mcnedward.ii.utils.IILogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author Edward - Jun 30, 2016
 *
 */
public class PrefUtils {
	private static final String PREF_REGEX = "\\s*,\\s*";
	private static final String PREF_LIST_SEPARATOR = " , ";
    private static final String GRAPH_DEFAULTS_SET = "GraphDefaultsSet";

    public static <T> void putPreference(String key, String pref, Class<T> clazz) {
        Preferences preferences = Preferences.userNodeForPackage(clazz);
        preferences.put(key, pref);
        save(preferences);
    }

    public static <T> void putPreference(String key, int pref, Class<T> clazz) {
        Preferences preferences = Preferences.userNodeForPackage(clazz);
        preferences.putInt(key, pref);
        save(preferences);
    }

    public static <T> void putPreference(String key, boolean pref, Class<T> clazz) {
        Preferences preferences = Preferences.userNodeForPackage(clazz);
        preferences.putBoolean(key, pref);
        save(preferences);
    }

	public static <T> void putInListPreference(String key, String pref, Class<T> clazz) {
		Preferences preferences = Preferences.userNodeForPackage(clazz);
		String currentPref = preferences.get(key, "");
		if (currentPref != null && !currentPref.equals("")) {
			currentPref += PREF_LIST_SEPARATOR + pref;
		} else {
			currentPref = pref;
		}
		preferences.put(key, currentPref);
        save(preferences);
	}

    public static <T> String getPreference(String key, Class<T> clazz) {
        Preferences preferences = Preferences.userNodeForPackage(clazz);
        return preferences.get(key, "");
    }

    public static <T> int getPreferenceInt(String key, Class<T> clazz) {
        Preferences preferences = Preferences.userNodeForPackage(clazz);
        return preferences.getInt(key, 0);
    }

    public static <T> boolean getPreferenceBool(String key, Class<T> clazz) {
        Preferences preferences = Preferences.userNodeForPackage(clazz);
        return preferences.getBoolean(key, false);
    }
	
	public static <T> List<String> getPreferenceList(String key, Class<T> clazz) {
		Preferences preferences = Preferences.userNodeForPackage(clazz);
		String pref = preferences.get(key, "");
		if (pref == null || pref.equals("")) {
			return new ArrayList<>();
		} else {
			return Arrays.asList(pref.split(PREF_REGEX));
		}
	}

	public static <T> void clearPreference(String key, Class<T> clazz) {
        Preferences preferences = Preferences.userNodeForPackage(clazz);
        preferences.put(key, "");
        save(preferences);
    }

    public static <T> void clearPreferences(Class<T> clazz) {
        IILogger.info("Clearing preferences for %s.", clazz);
        Preferences preferences = Preferences.userNodeForPackage(clazz);
        try {
            preferences.clear();
            save(preferences);
        } catch (BackingStoreException e) {
            IILogger.error(e);
        }
    }

    public static void loadGraphDefaults() {
        Preferences preferences = Preferences.userNodeForPackage(GraphPanel.class);
        boolean graphDefaultsSet = preferences.getBoolean(GRAPH_DEFAULTS_SET, false);
        if (!graphDefaultsSet) {
            // Need to load the Graph defaults for the first app run time.
            preferences.putBoolean(GRAPH_DEFAULTS_SET, true);
            preferences.putInt(SettingsConst.H_DISTANCE, GraphOptions.DEFAULT_X_DIST);
            preferences.putInt(SettingsConst.V_DISTANCE, GraphOptions.DEFAULT_Y_DIST);
            preferences.putInt(SettingsConst.FONT_SIZE, GraphOptions.DEFAULT_FONT_SIZE);
            preferences.putInt(SettingsConst.LABEL_COLOR, GraphOptions.DEFAULT_LABEL_COLOR.getRGB());
            preferences.putInt(SettingsConst.FONT_COLOR, GraphOptions.DEFAULT_FONT_COLOR.getRGB());
            preferences.putInt(SettingsConst.ARROW_COLOR, GraphOptions.DEFAULT_ARROW_COLOR.getRGB());
            preferences.putInt(SettingsConst.INTERFACE_LABEL_COLOR, GraphOptions.DEFAULT_INTERFACE_LABEL_COLOR.getRGB());
            preferences.putInt(SettingsConst.INTERFACE_EDGE_COLOR, GraphOptions.DEFAULT_INTERFACE_EDGE_COLOR.getRGB());
            preferences.putInt(SettingsConst.INTERFACE_ARROW_COLOR, GraphOptions.DEFAULT_INTERFACE_ARROW_COLOR.getRGB());
            preferences.putInt(SettingsConst.GRAPH_SHAPE, GraphOptions.DEFAULT_GRAPH_SHAPE.graphShapeValue);
            save(preferences);
        }
    }

    private static void save(Preferences preferences) {
        try {
            preferences.flush();
        } catch (BackingStoreException e) {
            IILogger.error(e);
        }
    }
}
