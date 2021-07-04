package springwriter;

import java.util.HashMap;

import mysqlentity.mysqltable.MySQLTable;

/**
 * This class contains a library of utility static methods with uses
 * ranging from getting the primary type of a <code>MySQLTable</code> to 
 * uppercasing or lowercasing a <code>String</code>.
 */
public class SpringWriterUtil {
	
	/**
	 * Creates a String of import statements for every element in
	 * the passed in <code>packages</code> array.
	 * 
	 * @param rootString root path for each package in <code>packages</code>
	 * @param packages array of every imported package
	 * @return String of import statements
	 */
	public static String writeImports(String rootString, String [] packages) {
		StringBuilder sb = new StringBuilder();
		
		for(String s : packages) {
			sb.append(String.format("import %s.%s;\n", rootString, s));
		}
		
		return sb.toString();
	}
	
	/**
	 * CamelCases the passed in MySQL variable String. Joins the passed
	 * by any found underscores in the String.
	 * 
	 * @param mysqlVar variable String to be CamelCased
	 * @return <code>mysqlVar</code> CamelCased
	 */
	public static String camelCaseMySQLVariable(String mysqlVar) {
		final String[] TOKENS = mysqlVar.split("_");
		StringBuilder sb = new StringBuilder(TOKENS[0].toLowerCase());
		
		for(int i = 1; i < TOKENS.length; i++){
		    sb.append(uppercaseFirstChar(TOKENS[i]));
		}
		
		return sb.toString();
	}
	
	/**
	 * Hyphenates the passed in MySQL variable String. Joins the passed
	 * by any found underscores in the String.
	 * @param mysqlVar to be hyphenated
	 * @return <code>mysqlVar</code> hyphenated
	 */
	public static String hyphenateMySQLVariable(String mysqlVar) {
		return mysqlVar.replace("_", "-").toLowerCase();
	}
	
	/**
	 * Returns the primary key type of the passed in MySQLTable
	 * in the form of a Java variable type (ex: String, Integer)
	 * 
	 * @param table <code>MySQLTable</code> to retrieve primary key type from 
	 * @return String of the primary key type of table in Java form
	 * @throws Exception if table has no primary key 
	 * 		or a primary key type without a conversion (ex: DATE, TIMESTAMP, BIT)
	 * @see MySQLTable
	 */
	public static String getPrimaryKeyType(MySQLTable table) throws Exception{
		final int PRIMARY_COUNT = table.getPrimaryKeyNames().size();
		
		if(PRIMARY_COUNT == 0) {
			throw new Exception(String.format("no primary key defined in table '%s'", 
					table.getName()));
		}else if(PRIMARY_COUNT > 1) {
			return table.getName() + "Identity";
		}
		
		HashMap <String, String> mySQLToIdTypeMap = new HashMap<>();
		
		mySQLToIdTypeMap.put("INT", "Integer");
		mySQLToIdTypeMap.put("BIGINT", "Long");
		mySQLToIdTypeMap.put("VARCHAR", "String");
		
		String primaryKeyName = table.getPrimaryKeyNames().get(0);
		String primaryKeyType = table.getColumn(primaryKeyName).getMySQLType().name();
		
		if(!mySQLToIdTypeMap.containsKey(primaryKeyType)) {
			throw new Exception(String.format("Primary key type '%s' isn't defined in mySQLToIdTypeMap", 
					primaryKeyType));
		}

		return mySQLToIdTypeMap.get(primaryKeyType);
	}
	
	/**
	 * Uppercases the first character of a String
	 * 
	 * @param str String to be uppercased
	 * @return str with its first character uppercased
	 */
	public static String uppercaseFirstChar(String str) {
		return str.substring(0,1).toUpperCase() + str.substring(1);
	}
	
	/**
	 * Lowercases the first character of a String
	 * 
	 * @param str String to be lowercased
	 * @return str with its first character lowercased
	 */
	public static String lowercaseFirstChar(String str) {
		return str.substring(0,1).toLowerCase() + str.substring(1);
	}
}
