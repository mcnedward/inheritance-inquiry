package com.mcnedward.app.ui.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;

/**
 * @author Edward - Jun 25, 2016
 *
 */
public class FontUtils {
	public static void changeFont(Component component, Font font) {
		component.setFont(font);
		if (component instanceof Container) {
			for (Component child : ((Container) component).getComponents()) {
				changeFont(child, font);
			}
		}
	}
}
