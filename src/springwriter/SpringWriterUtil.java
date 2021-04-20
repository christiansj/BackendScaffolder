package springwriter;

import java.util.HashMap;

import mysqlentity.mysqltable.MySQLTable;

public class SpringWriterUtil {
	public static String writeImports(String rootString, String [] packages) {
		StringBuilder sb = new StringBuilder();
		
		for(String s : packages) {
			sb.append(String.format("import %s.%s;\n", rootString, s));
		}
		
		return sb.toString();
	}
	
	public static String camelCaseMySQLVariable(String mysqlVar) {
		final String[] TOKENS = mysqlVar.split("_");
		StringBuilder sb = new StringBuilder(TOKENS[0]);
		
		for(int i = 1; i < TOKENS.length; i++){
		    String t = TOKENS[i];
			sb.append(String.format("%c%s",
				Character.toUpperCase(t.charAt(0)), 
		        t.substring(1, t.length())
		    ));
		}
		
		return sb.toString();
	}
	
	public static String getPrimaryKeyType(MySQLTable table) throws Exception{
		HashMap <String, String> mySQLToIdTypeMap = new HashMap<>();
		
		mySQLToIdTypeMap.put("INT", "Integer");
		mySQLToIdTypeMap.put("BIGINT", "Long");
		mySQLToIdTypeMap.put("VARCHAR", "String");
		
		final int PRIMARY_COUNT = table.getPrimaryKeyNames().size();
		if(PRIMARY_COUNT == 0) {
			throw new Exception(String.format("no primary key defined in table '%s'", 
					table.getName()));
		}else if(PRIMARY_COUNT > 1) {
			return table.getName() + "Identity";
		}
		
		String primaryKeyName = table.getPrimaryKeyNames().get(0);
		String primaryKeyType = table.getColumn(primaryKeyName).getMySQLType().name();
		
		if(!mySQLToIdTypeMap.containsKey(primaryKeyType)) {
			throw new Exception(String.format("Primary key type '%s' isn't defined in mySQLToIdTypeMap", 
					primaryKeyType));
		}
		
		return mySQLToIdTypeMap.get(primaryKeyType);
	}
	
	public static String uppercaseFirstChar(String s) {
		return String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1);
	}
	
	public static String lowercaseFirstChar(String s) {
		return String.valueOf(s.charAt(0)).toLowerCase() + s.substring(1);
	}
}
