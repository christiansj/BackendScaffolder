package springwriter.springfilewriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;

import mysqlentity.mysqltable.MySQLTable;
import springwriter.SpringWriter;

public abstract class SpringFileWriter implements SpringFileWriterInterface{
	public SpringWriter springWriter;
	public MySQLTable mySQLTable; 
	public final String SINGULAR;
	public final String PLURAL;
	public final String[] LOMBOKS = {"Data", "Getter", "Setter", "AllArgsConstructor", "NoArgsConstructor"};
	
	public HashMap<String, String> mySQLToJavaMap = new HashMap<>();
	private HashMap<String, String> classNameToFileNameMap = new HashMap<>();
	
	public SpringFileWriter(SpringWriter springWriter, String singular, String plural) {
		this.springWriter = springWriter;
		this.SINGULAR = singular;
		this.PLURAL = plural;
		this.mySQLTable = springWriter.getMySqlTable();
		initClassNameToFileNameMap();
		initMySQLToJavaMap();
	}
	
	private void initClassNameToFileNameMap() {
		String tableName = mySQLTable.getName();
		
		classNameToFileNameMap.put("SpringModelWriter", tableName);
		classNameToFileNameMap.put("SpringIdentityWriter", tableName + "Identity");
		classNameToFileNameMap.put("SpringRepositoryWriter", tableName + "Repository");
		classNameToFileNameMap.put("SpringControllerWriter", tableName + "Controller");
	}
	
	private void initMySQLToJavaMap() {
		mySQLToJavaMap.put("INT", "Integer");
		mySQLToJavaMap.put("BIGINT", "Long");
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
	
	public void writeFile() throws Exception {
		final String CLASS_NAME = this.getClass().getSimpleName();

		if(!classNameToFileNameMap.containsKey(CLASS_NAME)) {
			throw new Exception(String.format("'%s' is not defined in classNameToFileNameMap", CLASS_NAME));
		}
		
		String filePath = String.format("%s/%s.java", 
				springWriter.setDirectory(SINGULAR, PLURAL),
				classNameToFileNameMap.get(CLASS_NAME)
		);
		
		final String FILE_STRING = createFileString();
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		
		writer.write(FILE_STRING);
		writer.close();
	}
}
