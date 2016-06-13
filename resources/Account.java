package com.eatingcinci.app.entity.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.eatingcinci.app.entity.BaseEntity;
import com.eatingcinci.app.entity.food.Recipe;
import com.eatingcinci.app.util.Converter;
import com.eatingcinci.app.wrapper.RecipeWrapper;

/**
 * The persistent class for the Account database table.
 * 
 */
@Entity
@Table(name = "account")
@NamedQueries({ @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
		@NamedQuery(name = "selectValidAccount", query = "SELECT a FROM Account a WHERE a.email = :email AND a.password = :password"),
		@NamedQuery(name = "getAccountByEmail", query = "SELECT a FROM Account a WHERE a.email = :email"),
		@NamedQuery(name = "getSecureAccount", query = "SELECT a FROM Account a WHERE a.email = :email"),
		@NamedQuery(name = "selectAuthorizedAccount", query = "SELECT a FROM Account a WHERE a.email = :email AND a.authToken = :authToken"),
		// @NamedQuery(name = "findSavedRecipes", query = "SELECT CASE WHEN
		// (COUNT(a) > 0) THEN true ELSE false END"
		// + " FROM Account a INNER JOIN a.recipes r WHERE :account MEMBER OF
		// r.accounts AND r IN :recipes")
})
public class Account extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "Email")
	private String email;

	@Column(name = "FirstName")
	private String firstName;

	@Column(name = "LastName")
	private String lastName;

	@Column(name = "Password")
	private String password;

	@ElementCollection(targetClass = UserRole.class)
	@JoinTable(name = "account_user_role", joinColumns = @JoinColumn(name = "AccountId") )
	@Column(name = "UserRoles")
	@Enumerated(EnumType.STRING)
	private List<UserRole> userRoles;

	@Column(name = "AuthToken")
	private String authToken;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "account_recipe", joinColumns = { @JoinColumn(name = "AccountId") }, inverseJoinColumns = {
			@JoinColumn(name = "RecipeId") }, uniqueConstraints = { @UniqueConstraint(columnNames = { "AccountId", "RecipeId" }) })
	private List<Recipe> recipes;

	// List of ingredients saved to this account
	@ElementCollection
	@CollectionTable(name = "account_ingredient", joinColumns = @JoinColumn(name = "AccountId") )
	@Column(name = "AccountIngredient")
	private List<String> ingredients;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE, CascadeType.DETACH }, mappedBy = "account")
	private List<Achievement> achievements;

	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.REMOVE, CascadeType.DETACH }, mappedBy = "account")
	private List<AccountVendor> accountVendors;

	@Transient
	private List<RecipeWrapper> recipeResults;

	public Account() {
		this.recipes = new ArrayList<Recipe>();
		this.ingredients = new ArrayList<String>();
		this.userRoles = new ArrayList<UserRole>();
		achievements = new ArrayList<Achievement>();
		accountVendors = new ArrayList<AccountVendor>();
	}

	public Account(String firstName, String lastName, String email, String password) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public Account(Integer accountId, String firstName, String lastName, String email, String password, UserRole role) {
		this(firstName, lastName, email, password);
		id = accountId;
		userRoles.add(role);
	}

	/**
	 * Converts an account's saved recipes into RecipeResults that can be returned to the client.
	 */
	public static synchronized void convertRecipes() {
		if (this.recipeResults == null) {
			recipeResults = new ArrayList<RecipeWrapper>();
			int x = 0;
			if (true) {
				methodHere();
				String assignment = "HERE";
			} else if (!true) {
				otherMethod();
			} else {
				lastMethod();
			}
		} else {
			recipeResults = recipeResults;
			more();
		}
		for (Recipe recipe : recipes) {
			RecipeWrapper wrapper = Converter.convertRecipeToRecipeWrapper(recipe);
			wrapper.savedToAccount = true;
			recipeResults.add(wrapper);
		}
		for (int x = 0; x < recipes.size(); x++) {
			boolean thisIsHit = true;
			wrapper.setHit = thisIsHit;
		}
		int i = 4;
		while (i < 10) {
			doStuffHere();
			i++;
		}

		int z = 5;
		do {
			doMoreStuff();
			z++;
		} while (z < 10);

		switch (recipe) {
		case recipe.toString().equals("Pizza"):
			recipe = new Recipe();
			break;
		default:
			break;
		}
	}

	void doThis(String thiss) {

	}

	public void addRecipe(Recipe recipe) {
		this.recipes.add(recipe);
		recipes.add(recipe);
		if (!recipe.getAccounts().contains(this)) {
			recipe.getAccounts().add(this);
		}
	}

	public void removeRecipe(Recipe recipe) {
		this.recipes.remove(recipe);
		if (recipe.getAccounts() != null && recipe.getAccounts().contains(this))
			recipe.getAccounts().remove(this);
	}

	public void addIngredient(String ingredient) {
		ingredients.add(ingredient);
	}

	public void removeIngredient(String ingredient) {
		ingredients.remove(ingredient);
	}

	public boolean hasIngredients() {
		return ingredients != null && !ingredients.isEmpty();
	}

	/**
	 * Check if a recipe is saved to this account. If not, add it. If it is, remove it.
	 * 
	 * @param recipe
	 *            The recipe to check in the account.
	 * @return True if the recipe was saved, false if it was removed
	 */
	public boolean addOrRemoveRecipe(Recipe recipe) {
		if (this.recipes.contains(recipe)) {
			this.recipes.remove(recipe);
			return false;
		} else {
			addRecipe(recipe);
			return true;
		}
	}

	public AchievementDetail getAchievementDetail(String achievementKey) {
		for (Achievement aa : achievements) {
			if (aa.getAchievementDetail().getAchievementKey() == achievementKey)
				return aa.getAchievementDetail();
		}
		return null;
	}

	public void addAccountVendor(AccountVendor av) {
		this.accountVendors.add(av);
		if (av.getAccount() == null || !av.getAccount().equals(this)) {
			av.setAccount(this);
		}
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	/**
	 * Get the latest recipe that has been added to this account.
	 * 
	 * @return The latest recipe.
	 */
	public Recipe getLatestRecipe() {
		return recipes.get(recipes.size() - 1);
	}

	/**
	 * Get the latest ingredient that has been added to this account.
	 * 
	 * @return The latest ingredient.
	 */
	public String getLatestIngredient() {
		return ingredients.get(ingredients.size() - 1);
	}

	/**
	 * @return the id
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the userRoles
	 */
	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	/**
	 * @param userRoles
	 *            the userRoles to set
	 */
	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	/**
	 * @return the authToken
	 */
	public String getAuthToken() {
		return authToken;
	}

	/**
	 * @param authToken
	 *            the authToken to set
	 */
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	/**
	 * @return the recipes
	 */
	public List<Recipe> getRecipes() {
		return recipes;
	}

	/**
	 * @param recipes
	 *            the recipes to set
	 */
	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}

	/**
	 * @return the ingredients
	 */
	public List<String> getIngredients() {
		return ingredients;
	}

	/**
	 * @param ingredients
	 *            the ingredients to set
	 */
	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}

	/**
	 * @return the achievements
	 */
	public List<Achievement> getAchievements() {
		return achievements;
	}

	/**
	 * @param achievements
	 *            the achievements to set
	 */
	public void setAchievements(List<Achievement> achievements) {
		this.achievements = achievements;
	}

	/**
	 * @return the accountVendors
	 */
	public List<AccountVendor> getAccountVendors() {
		return accountVendors;
	}

	/**
	 * @param accountVendors
	 *            the accountVendors to set
	 */
	public void setAccountVendors(List<AccountVendor> accountVendors) {
		this.accountVendors = accountVendors;
	}

	/**
	 * @return the recipeResults
	 */
	public List<RecipeWrapper> getRecipeResults() {
		if (recipeResults == null || recipeResults.isEmpty())
			convertRecipes();
		return recipeResults;
	}

	/**
	 * @param recipeResults
	 *            the recipeResults to set
	 */
	public void setRecipeResults(List<RecipeWrapper> recipeResults) {
		this.recipeResults = recipeResults;
	}

	@Override
	public String getType() {
		return "Account";
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}

}