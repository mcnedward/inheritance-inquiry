/**
 * 
 */
package com.eatingcinci.app.request;

import java.io.Serializable;

import com.eatingcinci.app.wrapper.RecipeWrapper;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com>
 *
 */
public class RecipeRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	public int accountId;
	public RecipeWrapper recipe;

	public RecipeRequest() {

	}

	public RecipeRequest(int accountId, RecipeWrapper recipe) {
		this.accountId = accountId;
		this.recipe = recipe;
	}
}
