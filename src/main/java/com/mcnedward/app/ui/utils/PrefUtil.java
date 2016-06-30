package com.mcnedward.app.ui.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * @author Edward - Jun 30, 2016
 *
 */
public class PrefUtil {
	private static final String PREF_REGEX = "\\s*,\\s*";
	private static final String PREF_LIST_SEPARATOR = " , ";
	
	public static <T> void putInListPreference(String key, String pref, Class<T> clazz) {
		Preferences preferences = Preferences.userNodeForPackage(clazz);
		String currentPref = preferences.get(key, "");
		if (currentPref != null && !currentPref.equals("")) {
			currentPref += PREF_LIST_SEPARATOR + pref;
		} else {
			currentPref = pref;
		}
		preferences.put(key, currentPref);
	}
	
	public static <T> List<String> getListPreference(String key, Class<T> clazz) {
		Preferences preferences = Preferences.userNodeForPackage(clazz);
		String pref = preferences.get(key, "");
		if (pref == null || pref.equals("")) {
			return new ArrayList<>();
		} else {
			return Arrays.asList(pref.split(PREF_REGEX));
		}
	}
	
	public static <T> void putPreference(String key, String pref, Class<T> clazz) {
		Preferences preferences = Preferences.userNodeForPackage(clazz);
		preferences.put(key, pref);
	}
	
	public static <T> String getPreference(String key, Class<T> clazz) {
		Preferences preferences = Preferences.userNodeForPackage(clazz);
		return preferences.get(key, "");
	}
}
