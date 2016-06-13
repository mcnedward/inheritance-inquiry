package com.eatingcinci.app.response;

import com.eatingcinci.app.entity.account.Account;
import com.eatingcinci.app.wrapper.AccountWrapper;

public class AccountResponse extends AchievementResponse {

	private AccountWrapper account;
	@SuppressWarnings("unused")
	private IngredientWrapper ingredient;

	public AccountResponse() {
		super();
	}

	@Override
	public boolean hasResults() {
		return account != null;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Account account) {
		setAccount(new AccountWrapper(account));
	}

	public void setAccount(AccountWrapper account) {
		this.account = account;
		setAchievements(this.account.achievements);
	}

	public void setIngredient(IngredientWrapper ingredient) {
		this.ingredient = ingredient;
	}

}
