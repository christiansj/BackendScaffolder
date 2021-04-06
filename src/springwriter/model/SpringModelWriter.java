package springwriter.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mysqlentity.mysqlcolumn.MySQLColumn;
import mysqlentity.mysqltable.MySQLTable;
import springwriter.SpringWriter;
import springwriter.SpringWriterUtil;

public class SpringModelWriter {
	MySQLTable mySQLTable;
	
	String directory;
	String packageStr;
	String primaryKeyType;
	HashMap<String, String> mySQLToJavaMap = new HashMap<String, String>();
	
	
	public SpringModelWriter(SpringWriter springWriter) throws Exception {
		this.mySQLTable = springWriter.getMySqlTable();
		this.packageStr = springWriter.getPackageStr();
		
		initPackageAndDirectory();
		initMySqlToJavaMap();
	}
	
	private void initPackageAndDirectory() throws Exception {
		directory = packageStr.replaceAll("\\.", "\\/");
		packageStr = initPackage();
	
		
		if(directoryExists(directory + "/model")) {
			packageStr += ".model";
			directory += "/model";
		}else {
			packageStr += ".models";
			directory += "/models";
			if(!directoryExists(directory + "/models")) {
				new File(directory).mkdir();	
			}
		}
	}
	
	private String initPackage() throws Exception {
		final List<String> TOKENS = Arrays.asList(packageStr.split("\\."));
		String packagePath = "";
		for(int i = TOKENS.indexOf("src")+1; i < TOKENS.size(); i++) {
			packagePath += TOKENS.get(i);
			if(i + 1 != TOKENS.size()) {
				packagePath+=".";
			}
		}
		return packagePath;
	}
	
	private void initMySqlToJavaMap() {
		mySQLToJavaMap.put("INT", "int");
		mySQLToJavaMap.put("BIGINT", "long");
		mySQLToJavaMap.put("TINYINT", "byte");
		mySQLToJavaMap.put("BIT", "boolean");
		mySQLToJavaMap.put("VARCHAR", "String");
		mySQLToJavaMap.put("CHAR", "String");
		mySQLToJavaMap.put("FLOAT", "double");
		mySQLToJavaMap.put("DOUBLE", "double");
		mySQLToJavaMap.put("DATE", "Date");
		mySQLToJavaMap.put("DATETIME", "Date");
		mySQLToJavaMap.put("TIMESTAMP", "Timestamp");
	}
	
	public void writeModelFile() throws Exception {
		String filePath = String.format("%s/%s.java", 
				directory,
				mySQLTable.getName()
		);

		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		
		writer.write(createModelString());
		writer.close();
	}
	
	private boolean directoryExists(String pathStr) {
		return Files.exists(Paths.get(pathStr));
	}
	
	private String createModelString() throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("package %s;", packageStr));
		sb.append(importStrings());
		
		sb.append("@Entity\n");
		sb.append(String.format("public class %s {\n\n", mySQLTable.getName()));
		sb.append(createVariableString());
		sb.append("}\n");
		
		return sb.toString();
	}
		
	private String importStrings() {
		final String[] PERSISTANCES = {"Entity", "Id"};
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		pw.println();	
		pw.println(SpringWriterUtil.writeImports("javax.persistance", PERSISTANCES));

		return sw.toString();
	}
	
	private String createVariableString() throws Exception {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		for(MySQLColumn col : mySQLTable.getColumns()) {
			String colType = col.getMySQLType().name();
			if(!mySQLToJavaMap.containsKey(colType)) {
				String err = String.format("'%s' is not in MySQLToJavaMap", colType);
				throw new Exception(err);
			}
			pw.println(String.format("\tprivate %s %s;", 
					mySQLToJavaMap.get(colType), 
					SpringWriterUtil.formatMySQLVariable(col.getName())
			));
		}
		
		return sw.toString();
	}
}
