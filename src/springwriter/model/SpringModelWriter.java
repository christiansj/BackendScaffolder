package springwriter.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import mysqlentity.mysqlcolumn.MySQLColumn;
import mysqlentity.mysqltable.MySQLTable;
import springwriter.SpringWriter;
import springwriter.SpringWriterUtil;

public class SpringModelWriter {
	final String SINGULAR = "model";
	final String PLURAL = "models";
	
	MySQLTable mySQLTable;
	
	String directory;
	String packageStr;
	SpringWriter springWriter;
	String primaryKeyType;
	
	HashMap<String, String> mySQLToJavaMap = new HashMap<>();
	
	public SpringModelWriter(SpringWriter springWriter) throws Exception {
		this.mySQLTable = springWriter.getMySqlTable();
		this.packageStr = springWriter.getPackageStr();
		this.springWriter = springWriter;
	
		initMySqlToJavaMap();
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
				springWriter.setDirectory(SINGULAR, PLURAL),
				mySQLTable.getName()
		);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		
		writer.write(createModelString());
		writer.close();
	}
	
	public String createModelString() throws Exception {
		StringBuilder sb = new StringBuilder();
		
		// package and imports
		sb.append("package " + springWriter.createPackageStr(SINGULAR, PLURAL) + ";");
		sb.append(importStrings());
		
		// class body
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
			
			if(col.isPrimaryKey()) {
				pw.println("\t@Id");
			}
			
			pw.println(String.format("\tprivate %s %s;", 
					mySQLToJavaMap.get(colType), 
					SpringWriterUtil.formatMySQLVariable(col.getName())
			));
		}
		
		return sw.toString();
	}
}
