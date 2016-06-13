package com.mcnedward.app.analyze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.mcnedward.app.classobject.AnalysisResult;
import com.mcnedward.app.classobject.ClassObject;
import com.mcnedward.app.listener.AnalysisListener;
import com.mcnedward.app.visitor.ClassVisitor;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 23, 2015
 *
 */
public class Analyser {

	private List<CompilationHolder> compilationHolders;

	private ClassVisitor classVisitor;
	private List<ClassObject> classObjects;
	private List<File> files;

	public Analyser() {
		classVisitor = new ClassVisitor();
	}

	public void analyze(File file, AnalysisListener listener) {
		Runnable task = () -> {
			AnalysisResult result = analyzeFile(file, listener);
			listener.finished(result);
		};
		
		Thread thread = new Thread(task);
		thread.start();
	}
	
	public AnalysisResult analyze(File file) {
		return analyzeFile(file, null);	// TODO THIS WILL NOT WORK HERE!
	}
	
	/**
	 * Analyse the file or directory. This will create a List of ClassObjects for every Java item in the File.
	 * 
	 * @param file
	 *            The file or directory.
	 * @return A List of all the ClassObjects in the file or directory.
	 */
	private AnalysisResult analyzeFile(File file, AnalysisListener listener) {
		compilationHolders = new ArrayList<CompilationHolder>();
		classObjects = new ArrayList<ClassObject>();
		files = new ArrayList<File>();

		listener.onProgressChange(String.format("Starting to load %s...", file.getName()), 0);
		try {
			parserFile(file, listener);
		} catch (IOException e) {
			listener.onProgressChange("Something went wrong loading the file.", 100);
			return null;
		}
		listener.onProgressChange("File loaded.", 0);

		int classesCount = compilationHolders.size();
		
		for (int i = 0; i < compilationHolders.size(); i++) {
			CompilationHolder holder = compilationHolders.get(i);
			
			int progress = (int) (((double) i / classesCount) * 100);
			listener.onProgressChange(String.format("Analyzing..."), progress);
			
			classVisitor.reset();
			classVisitor.visit(holder.compilationUnit, null);
			ClassObject classObject = classVisitor.getClassObject();
			classObject.setSourceFile(holder.file);
			classObjects.add(classObject);
		}
		listener.onProgressChange(String.format("Finished!"), 100);

		return new AnalysisResult(file, classObjects);
	}

	/**
	 * Loads the file or directory and creates the CompilationUnits.
	 * 
	 * @param selectedFile
	 *            The selected file or directory.
	 * @throws IOException
	 */
	private void parserFile(File selectedFile, AnalysisListener listener) throws IOException {
		System.out.println("Loading: " + selectedFile.getAbsolutePath());
		
		if (selectedFile.isDirectory())
			handleDirectory(selectedFile.listFiles());
		else {
			files.add(selectedFile);
		}
		try {
			int fileCount = files.size();
			for (int i = 0; i < fileCount; i++) {
				File file = files.get(i);
				
				int progress = (int) (((double) i / fileCount) * 100);
				listener.onProgressChange(String.format("Parsing..."), progress);

				CompilationUnit cu = JavaParser.parse(file);
				compilationHolders.add(new CompilationHolder(cu, file));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void handleDirectory(File[] directory) {
		for (File file : directory) {
			if (file.isDirectory())
				handleDirectory(file.listFiles());
			if (file.isFile()) {
				if (file.getAbsolutePath().contains(".java")) {
					files.add(file);
				}
			}
		}
	}

	/**
	 * @return the files
	 */
	public List<File> getFiles() {
		return files;
	}
}

class CompilationHolder {
	protected CompilationUnit compilationUnit;
	protected File file;

	protected CompilationHolder(CompilationUnit cu, File file) {
		compilationUnit = cu;
		this.file = file;
	}
}
