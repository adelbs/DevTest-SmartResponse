package com.ca.smartresponse.parser;

import com.itko.lisa.test.TestExec;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * This class will parse the text through the BeanShell library.<br/>
 * The content of the String should be at the following format:<br/><br/>
 * <i>
 * text text text<br/>
 * text text text<br/>
 * <%<br/>
 * command1();<br/>
 * command2();<br/>
 * %><br/>
 * text text text<br/><br/>
 * </i>
 * All the content outside of the <% and %> tags, will be converted to "print(value);" 
 * and the content inside the <% and %> tags will keep the same. So, the example above will be converted to:<br/><br/>
 * <i>
 * print("text text text");<br/>
 * print("text text text");<br/>
 * command1();<br/>
 * command2();<br/>
 * print("text text text");<br/><br/>
 * </i>
 * And then, the script will be passed to the BeanShell interpreter.<br/>
 * By default, all scripts have the default import: com.ca.smartresponse.command.*;<br/>
 * 
 * @author CA - AD
 *
 */
public class Parser {
	
	public static final String OPENSCRIPT = "<%";
	public static final String CLOSESCRIPT = "%>";

	public static final String PARSEFILE = "DO_PARSE";
	public static final String IGNOREFILE = "DO_NOT_PARSE";
	
	private boolean executeScript = false;

	/**
	 * This method will parse the String through the BeanShell library.<br/>
	 * The content of the String should be at the following format:<br/><br/>
	 * <i>
	 * text text text<br/>
	 * text text text<br/>
	 * <%<br/>
	 * command1();<br/>
	 * command2();<br/>
	 * %><br/>
	 * text text text<br/><br/>
	 * </i>
	 * All the content outside of the <% and %> tags, will be converted to "print(value);" 
	 * and the content inside the <% and %> tags will keep the same. So, the example above will be converted to:<br/><br/>
	 * <i>
	 * print("text text text");<br/>
	 * print("text text text");<br/>
	 * command1();<br/>
	 * command2();<br/>
	 * print("text text text");<br/><br/>
	 * </i>
	 * And then, the script will be passed to the BeanShell interpreter.<br/>
	 * By default, all scripts have the default import: com.ca.smartresponse.command.*;<br/>
	 * 
	 * @param fullText String to be parsed
	 * @return parsed String
	 */
	public String parseText(String fullText) {
		return parseText(fullText, null, true, true);
	}
	
	public String parseText(String fullText, TestExec testExec, boolean isDefaultParse, boolean isSupressNewLine) {
		
		Console console = new Console();
		Interpreter interpreter = new Interpreter(console);
		StringBuilder script = new StringBuilder();
		String[] splitLine = fullText.split("\n");
		
		String result = fullText.replaceAll(IGNOREFILE, "");
		result = fullText.replaceAll(PARSEFILE, "");
		
		console.setSupressNewLine(isSupressNewLine);
		
		if (!isIgnoreFile(fullText, isDefaultParse)) {
			script.append("import com.ca.smartresponse.command.*;")
				.append("public void print(String str) {")
				.append("    console.print(str);")
				.append("}")
				.append("public void println(String str) {")
				.append("    console.println(str);")
				.append("}")
				.append("public void setSupressNewLine(boolean value) {")
				.append("    console.setSupressNewLine(value);")
				.append("}")
				.append("public boolean isSupressNewLine() {")
				.append("    return console.isSupressNewLine();")
				.append("}")
				.append("public void autoprint(String value) {")
				.append("    if (isSupressNewLine()) print(value);")
				.append("    else println(value);")
				.append("}")
				.append("public Object parseInState(String value) {")
				.append("    return testExec.parseInState(\"{{\"+ value +\"}}\");")
				.append("}");
			
			for (int i = 0; i <= splitLine.length - 1; i++)
				parseLine(script, splitLine[i].replaceAll("\r", ""));
			
			try {
				interpreter.set("console", console);
				if (testExec != null) {
					interpreter.set("testExec", testExec);
					interpreter.set("request", testExec.getStateObject("lisa.vse.request"));
				}
				interpreter.eval(script.toString());
			} 
			catch (EvalError e) {
				console.print(e);
				e.printStackTrace();
			}
			
			result = console.getResult().toString();
		}
		
		return result;
	}
	
	public boolean isIgnoreFile(String fullText, boolean isDefaultParse) {
		return (fullText.indexOf(IGNOREFILE) > -1) || 
				(fullText.indexOf(PARSEFILE) == -1 && fullText.indexOf(IGNOREFILE) == -1 && !isDefaultParse);
	}
	
	private void parseLine(StringBuilder result, String line) {
		
		if (line.indexOf(OPENSCRIPT) > -1) {
			
			result.append(getBeanPrintCmd(line.substring(0, line.indexOf(OPENSCRIPT)))).append("\n");

			if (line.indexOf(CLOSESCRIPT) > -1) {
				result.append(line.substring(line.indexOf(OPENSCRIPT) + OPENSCRIPT.length(), line.indexOf(CLOSESCRIPT))).append("\n");
				result.append(getBeanPrintCmd(line.substring(line.indexOf(CLOSESCRIPT) + CLOSESCRIPT.length()))).append("\n");
			}
			else {
				executeScript = true;
				result.append(line.substring(line.indexOf(OPENSCRIPT) + OPENSCRIPT.length())).append("\n");
			}
		}
		else if (line.indexOf(CLOSESCRIPT) > -1) {
			executeScript = false;
		
			result.append(line.substring(0, line.indexOf(CLOSESCRIPT))).append("\n");
			result.append(getBeanPrintCmd(line.substring(line.indexOf(CLOSESCRIPT) + CLOSESCRIPT.length()))).append("\n");
		}
		else {
			if (executeScript) result.append(line).append("\n");
			else result.append(getBeanPrintCmd(line)).append("\n");
		}
	}

	private String getBeanPrintCmd(String value) {
		String content = getBeanString(value);
		if (content.equals("\"\"")) content = "";
		else content = "autoprint(".concat(content).concat(");");
		return content;
	}
	
	private String getBeanString(String value) {
		String result = value;
		result = result.replaceAll("\\\\", "\\\\\\\\");
		result = result.replaceAll("\"", "\\\\\"");
		
		return "\"".concat(result).concat("\"");
	}
}
