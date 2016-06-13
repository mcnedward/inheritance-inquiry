package com.mcnedward.app.comparator;

import java.util.Comparator;

import com.mcnedward.app.classobject.LineObject;

/**
 * @author Edward - Feb 28, 2016
 *
 */
public class LineObjectComparator implements Comparator<LineObject> {
	@Override
	public int compare(LineObject o1, LineObject o2) {
		return o1.getLineNumber() - o2.getLineNumber();
	}

}
