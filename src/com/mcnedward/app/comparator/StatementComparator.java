package com.mcnedward.app.comparator;

import java.util.Comparator;

import com.mcnedward.app.classobject.statement.BaseStatement;

/**
 * @author Edward - Feb 28, 2016
 *
 */
public class StatementComparator implements Comparator<BaseStatement> {
	@Override
	public int compare(BaseStatement o1, BaseStatement o2) {
		return o1.getBeginLine() - o2.getBeginLine();
	}

}
