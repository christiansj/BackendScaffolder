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
	final String[] LOMBOKS = {"Data", "Getter", "Setter", "AllArgsConstructor", "NoArgsConstructor"};
	
	public SpringModelWriter(SpringWriter springWriter) throws Exception {
		super(springWriter, "model", "models");

		initMySqlToJavaMap();
	}
	
	private void initMySqlToJavaMap() {
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
		
	public String createFileString() throws Exception {
		StringBuilder sb = new StringBuilder();
		
		// package and imports
		sb.append("package " + springWriter.createPackageStr(SINGULAR, PLURAL) + ";\n");
		sb.append(importStrings());
		
		// class body
		sb.append("@Entity\n");
		for(String l : LOMBOKS) {
			sb.append(String.format("@%s\n", l));
		}
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
		writeOptionalImports(pw);
		
		pw.println(SpringWriterUtil.writeImports("javax.persistance", PERSISTANCES));
		pw.println(SpringWriterUtil.writeImports("lombok", LOMBOKS));
		
		return sw.toString();
	}
	
	private void writeOptionalImports(PrintWriter pw) {
		if(mySQLTable.hasDate()) {
			pw.println("import java.util.Date;");
		}
		
		if(mySQLTable.hasSize()) {
			pw.println("import javax.validation.constraints.Size;");
		}
		
		if(mySQLTable.hasDate() || mySQLTable.hasSize()) {
			pw.println();
		}
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
			final String VARIABLE_TYPE = mySQLToJavaMap.get(colType);
			
			if(col.isPrimaryKey()) {
				pw.println("\t@Id");
			}
			
			if(VARIABLE_TYPE.equals("String")  && col.getLength() > 0) {
				pw.println(String.format("\t@Size(max = %d)", col.getLength()));
			}
			
			pw.println(String.format("\tprivate %s %s;\n", 
					mySQLToJavaMap.get(colType), 
					SpringWriterUtil.formatMySQLVariable(col.getName())
			));
		}
		
		return sw.toString();
	}
}
