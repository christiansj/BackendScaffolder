package springwriter.model;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import mysqlentity.mysqlcolumn.MySQLColumn;
import springwriter.SpringWriter;
import springwriter.SpringWriterUtil;
import springwriter.springfilewriter.SpringFileWriter;
import springwriter.springfilewriter.SpringFileWriterInterface;

public class SpringModelWriter extends SpringFileWriter implements SpringFileWriterInterface {
	HashMap<String, String> mySQLToJavaMap = new HashMap<>();
	
	public SpringModelWriter(SpringWriter springWriter) throws Exception {
		super(springWriter, "model", "models");
	
		initMySqlToJavaMap();
	}
	
	private void initMySqlToJavaMap() {
		mySQLToJavaMap.put("INT", "int");
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
		
	public String createFileString() throws Exception {
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
