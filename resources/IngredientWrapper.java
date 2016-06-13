/**
 * 
 */
package com.eatingcinci.app.response;

import java.util.UUID;

/**
 * @author Edward
 *
 */
public class IngredientWrapper {

	public String id;
	public String name;
	public boolean selected;

	public IngredientWrapper() {
		id = UUID.randomUUID().toString();
		selected = true;
	}

	public IngredientWrapper(String name) {
		this();
		this.name = name;
	}
}