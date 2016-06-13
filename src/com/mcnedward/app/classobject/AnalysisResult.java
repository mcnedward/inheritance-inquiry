package com.mcnedward.app.classobject;

import java.io.File;
import java.util.List;

/**
 * @author Edward - Jun 12, 2016
 *
 */
public class AnalysisResult {

	private File file;
	private List<ClassObject> classObjects;

	public AnalysisResult(File file, List<ClassObject> classObjects) {
		this.file = file;
		this.classObjects = classObjects;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @return the classObjects
	 */
	public List<ClassObject> getClassObjects() {
		return classObjects;
	}

}
