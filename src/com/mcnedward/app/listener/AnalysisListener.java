package com.mcnedward.app.listener;

import com.mcnedward.app.classobject.AnalysisResult;

/**
 * @author Edward - Jun 12, 2016
 *
 */
public interface AnalysisListener {
	
	void onProgressChange(String message, int progress);

	void finished(AnalysisResult result);
	
}
