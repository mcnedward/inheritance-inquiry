package com.mcnedward.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mcnedward.app.utils.PrefUtils;
/**
 * @author Edward - Jun 30, 2016
 *
 */
public class PreferencesTest {
	
	@Test
	public void preference_ListWorks() {
		PrefUtils.putInListPreference("list", "hey", PreferencesTest.class);
		PrefUtils.putInListPreference("list", "there", PreferencesTest.class);
		PrefUtils.putInListPreference("list", "ed", PreferencesTest.class);
		
		List<String> list = PrefUtils.<PreferencesTest>getPreferenceList("list", PreferencesTest.class);
		
		assertThat(list.get(0), is("hey"));
		assertThat(list.get(1), is("there"));
		assertThat(list.get(2), is("ed"));
	}

	@Test
    public void preferences_RemoveItems() {
        List<String> originalList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            originalList.add("Item " + i);
        }
        PrefUtils.putPreference("list", originalList, PreferencesTest.class);

        List<String> newList = PrefUtils.getPreferenceList("list", PreferencesTest.class);
        newList.remove(3);
        PrefUtils.putPreference("list", newList, PreferencesTest.class);

        List<String> testList = PrefUtils.getPreferenceList("list", PreferencesTest.class);
        assertThat(testList.size(), is(4));
        assertThat(testList.get(3), is("Item 4"));
    }
	
}
