package com.mcnedward.app.classobject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Edward - Feb 28, 2016
 *
 */
public class BaseObject {
	
	protected List<Modifier> modifiers;
	protected String name;
	protected boolean isAbstract;
	private String objectType;

	public BaseObject(String objectType) {
		this.objectType = objectType;
		modifiers = new ArrayList<>();
	}

	/**
	 * @return the modifiers
	 */
	public List<Modifier> getModifiers() {
		return modifiers;
	}

	/**
	 * @param modifiers
	 *            the modifiers to set
	 */
	public void setModifiers(List<Modifier> modifiers) {
		this.modifiers = modifiers;
		isAbstract = modifiers.contains(Modifier.ABSTRACT);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the isAbstract
	 */
	public boolean isAbstract() {
		return isAbstract;
	}

	/**
	 * @param isAbstract the isAbstract to set
	 */
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	@Override
	public String toString() {
		String value = objectType + ": ";
		if (modifiers != null)
			value += modifiers;
		if (name != null)
			value += name;
		return value;
	}

}
