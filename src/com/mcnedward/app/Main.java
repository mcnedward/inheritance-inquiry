package com.mcnedward.app;

import java.io.File;
import java.util.List;

import com.mcnedward.app.analyze.Analyser;
import com.mcnedward.app.classobject.ClassObject;
import com.mcnedward.app.classobject.LineObject;
import com.mcnedward.app.classobject.method.MethodObject;
import com.mcnedward.app.classobject.statement.BaseStatement;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 16, 2015
 *
 */
public class Main {

	private static String FILE_LOCATION = "resources/Account.java";
	@SuppressWarnings("unused")
	private static String DIRECTORY_LOCATION = "C:\\users\\edward\\dev\\workspace\\eatingcinci-spring";

	private static Analyser analyser;

	public static void main(String[] args) {
		analyser = new Analyser();
		List<ClassObject> classObjects = analyser.analyze(new File(FILE_LOCATION)).getClassObjects();

		ClassObject classObject = classObjects.get(0);
		System.out.println(classObject.getName());

		MethodObject method = classObject.getMethods().get(1);
		System.out.println(method);

		List<BaseStatement> statements = method.getStatement().getStatements();

		// getMethodLines()
		int startingLine = 0;
		int endingLine = 0;
		int mNumber = 1;
		for (int currentLine = startingLine; currentLine < endingLine; currentLine++) {
			// Check if the current line is part of a statement
			boolean breakOut = false, valueFound = false;;
			for (BaseStatement statement : statements) {
				printLines(statement);
				if (breakOut)
					continue;
				LineObject firstLineInStatement = statement.getLines().get(0);
				if (firstLineInStatement.getLineNumber() == currentLine) {
					handleStatementBranching(statement);
					for (LineObject statementLine : statement.getLines()) {
						System.out.println("m" + mNumber + " [" + statementLine.getLineNumber() + "]: " + statementLine.getLine());
						mNumber++;
						currentLine = statementLine.getLineNumber();
						valueFound = true;
					}
					breakOut = true;
					continue;
				}
			}
			// If nothing found in statements, then find line in single line objects
			if (!valueFound) {
				for (LineObject line : method.getMethodLines()) {
					if (line.getLineNumber() == currentLine) {
						System.out.println("m" + mNumber + " [" + currentLine + "]: " + line.getLine());
					}
				}
			}
		}
	}
	
	private static void handleStatementBranching(BaseStatement statement) {
	}
	
	private static void printLines(BaseStatement statement) {
		for (LineObject line : statement.getLines()) {
			System.out.println(line);
		}
	}
}
