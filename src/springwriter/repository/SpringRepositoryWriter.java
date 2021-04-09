package springwriter.repository;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import mysqlentity.mysqlcolumn.MySQLColumn;
import mysqlentity.mysqltable.MySQLTable;
import springwriter.SpringWriter;

public class SpringRepositoryWriter {
	final String SINGULAR = "repository";
	final String PLURAL = "repositories";
	
	final MySQLTable mySQLTable;
	final SpringWriter springWriter;
	HashMap <String, String> mySQLToIdTypeMap = new HashMap<>();
	
	public SpringRepositoryWriter(SpringWriter springWriter) throws Exception {
		this.springWriter = springWriter;
		mySQLTable = springWriter.getMySqlTable();
		
		initMySQLtoIdTypeMap();
	}
	
	private void initMySQLtoIdTypeMap() {
		mySQLToIdTypeMap.put("INT", "Integer");
		mySQLToIdTypeMap.put("BIGINT", "Integer");
		mySQLToIdTypeMap.put("VARCHAR", "String");
	}
	
	public String createRepositoryString() throws Exception{
		StringBuilder sb = new StringBuilder();
		sb.append("package " + springWriter.createPackageStr(SINGULAR, PLURAL)+ ";\n");
		sb.append("\n");
		sb.append(importStrings());
		sb.append("@Repository\n");
		sb.append(prototypeString());
		sb.append("\n");
		sb.append("}\n");
		
		return sb.toString();
	}
	
	private String importStrings() throws Exception{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		pw.println(String.format("import %s.%s;", 
				springWriter.createPackageStr("model", "models"),
				mySQLTable.getName())
		);
		pw.println("import org.springframework.data.jpa.repository.JpaRepository;");
		pw.println("import org.springframework.stereotype.Repository;\n");
		
		return sw.toString();
	}
	
	private String prototypeString() throws Exception{
		final int PRIMARY_COUNT = mySQLTable.getPrimaryKeyNames().size();
		if(PRIMARY_COUNT == 0) {
			throw new Exception(String.format("no primary key defined in table '%s'", 
					mySQLTable.getName()));
		}else if(PRIMARY_COUNT > 1) {
			throw new Exception("composite keys are not supported yet");
		}
		
		String primaryKeyName = mySQLTable.getPrimaryKeyNames().get(0);
		String primaryKeyType = mySQLTable.getColumn(primaryKeyName).getMySQLType().name();
		return String.format("public interface %sRepository extends JpaRepository<%s, %s> {\n", 
				mySQLTable.getName(),
				mySQLTable.getName(),
				mySQLToIdTypeMap.get(primaryKeyType)
		);
	}
	
	
}
