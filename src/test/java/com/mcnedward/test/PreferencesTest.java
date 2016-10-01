package com.mcnedward.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.mcnedward.app.ui.utils.PrefUtils;
/**
 * @author Edward - Jun 30, 2016
 *
 */
public class PreferencesTest {
	
	@Test
	public void preference_list_works() {
		PrefUtils.<PreferencesTest>putInListPreference("list", "hey", PreferencesTest.class);
		PrefUtils.<PreferencesTest>putInListPreference("list", "there", PreferencesTest.class);
		PrefUtils.<PreferencesTest>putInListPreference("list", "ed", PreferencesTest.class);
		
		List<String> list = PrefUtils.<PreferencesTest>getPreferenceList("list", PreferencesTest.class);
		
		assertThat(list.get(0), is("hey"));
		assertThat(list.get(1), is("there"));
		assertThat(list.get(2), is("ed"));
	}
	
}
