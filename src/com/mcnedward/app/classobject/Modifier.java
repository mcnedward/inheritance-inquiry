package com.mcnedward.app.classobject;

/**
 * @author Edward - May 27, 2016
 *
 */
public enum Modifier {
	
	PRIVATE("private"),
	PROTECTED("protected"),
	PUBLIC("public"),
	ABSTRACT("abstract"),
	STATIC("static"),
	FINAL("final"),
	SYNCHRONIZED("synchronized"),
	VOLATILE("volatile"),
	TRANSIENT("transient"),
	NATIVE("native"),
	STRICT("strict");

	public String name;
	
	Modifier(String name) {
		this.name = name;
	}

}
