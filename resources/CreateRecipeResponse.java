/**
 * 
 */
package com.eatingcinci.app.response;

import java.util.List;

import com.eatingcinci.app.wrapper.AchievementWrapper;
import com.eatingcinci.app.wrapper.RecipeWrapper;

/**
 * @author Edward
 *
 */
public class CreateRecipeResponse {

	public RecipeWrapper recipe;
	public List<AchievementWrapper> achievements;

	public CreateRecipeResponse() {

	}

	public CreateRecipeResponse(RecipeWrapper recipe, List<AchievementWrapper> achievements) {
		this.recipe = recipe;
		this.achievements = achievements;
	}

}
