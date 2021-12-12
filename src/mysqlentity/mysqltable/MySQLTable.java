package mysqlentity.mysqltable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mysqlentity.datatype.MySQLType;
import mysqlentity.mysqlcolumn.MySQLColumn;
import springwriter.SpringWriterUtil;

/**
 * This class represents a MySQL table. It stores collections of MySQLColumns.
 * 
 * @see MySQLColumn
 */
public class MySQLTable {
	private final String originalName;
	private final String name;
	private final String urlName;
	
	private ArrayList<String> primaryKeyNames = new ArrayList<>();
	
	/**
	 * ArrayList to retain order of columns
	 */
	private ArrayList<MySQLColumn> columnList = new ArrayList<>();
	
	/**
	 * HashMap to easily retrieve columns 
	 */
	private HashMap<String, MySQLColumn> columnMap = new HashMap<>();
	
	/**
	 * indicates whether to include <code>import java.util.Date;</code> in a Spring Boot model class.
	 */
	private boolean hasDate = false;
	
	/**
	 * indicates whether to include @Size and its import for Spring Boot model and entity (composite key) class
	 */
	private boolean hasSize = false;
	
	
	/**
	 * Creates a new MySQLTable object
	 * 
	 * @param name String of the table's name
	 */
	public MySQLTable(String name) {
		this.originalName = name;
		this.name = SpringWriterUtil.uppercaseFirstChar(SpringWriterUtil.camelCaseMySQLVariable(name));
		this.urlName = name.replace("_", "-").toLowerCase();
	}

	/**
	 * Adds a <code>MySQLColumn</code> to this table.
	 * 
	 * @param column new <code>MySQLColumn</code> to be added to this table
	 * @throws Exception if this table contains a column with the same name
	 * @see MySQLColumn
	 */
	public void addColumn(MySQLColumn column) throws Exception{
		if(columnMap.containsKey(column.getName())) {
			throw new Exception(String.format("Column '%s' already exists in table '%s'", 
					column.getName(), name));
		}
		setBooleans(column);
		
		columnMap.put(column.getName(), column);
		columnList.add(column);
	}
	
	/**
	 * Sets boolean <code>hasDate</code> and <code>hasSize</code> based on passed in column  
	 * 
	 * @param column MySQLColumn to evaluate whether to set stated booleans to true
	 */
	private void setBooleans(MySQLColumn column) {
		List<String> SUPPORTED_SIZE_COLS =   Arrays.asList("VARCHAR", "CHAR"); 
		final MySQLType colType = column.getMySQLType();
		
		if(colType == MySQLType.DATE) {
			hasDate = true;
		}else if(column.getLength() > 0 && SUPPORTED_SIZE_COLS.contains(colType.name())) {
			hasSize = true;
		}
	}
	
	/**
	 * Adds a primary key to this table given the passed in <code>colName</code> 
	 * 
	 * @param colName String of the new primary key column name
	 * @throws Exception if a column doesn't have <code>colName</code> or if
	 * a primary key with <code>colName</code> already exists
	 */
	public void addPrimaryKey(String colName) throws Exception {
		MySQLColumn col = columnMap.get(colName);
		if(col == null) {
			String fmt = "'%s' doesn't exist in table '%s'";
			throw new Exception(String.format(fmt, colName, name));
		}
		else if(primaryKeyNames.contains(colName)) {
			String fmt = "table '%s' already has PRIMARY KEY '%s'";
			throw new Exception(String.format(fmt, name, colName));
		}
		
		col.setIsPrimaryKey(true);
		columnList.set(columnList.indexOf(col), col);
		columnMap.put(colName, col);
		
		primaryKeyNames.add(colName);
	}
	
	public ArrayList<String> getPrimaryKeyNames(){
		return primaryKeyNames;
	}
	
	public ArrayList<MySQLColumn> getColumns(){
		return columnList;
	}
	
	/**
	 * Retrieves a <code>MySQLColumn</code> given a columnn name.
	 * 
	 * @param columnName String name of the column to return
	 * @return <code>MySQLColumn</code> that has <code>columnName</code>
	 * @throws Exception if a column with <code>columnName</code> is not found
	 * @see MySQLColumn
	 */
	public MySQLColumn getColumn(String columnName) throws Exception{
		if(!columnMap.containsKey(columnName)) {
			String fmt = String.format("column '%s' isn't defined in table '%s'", columnName, name);
			throw new Exception(fmt);
		}
		return columnMap.get(columnName);
	}
	
	public String getOriginalName() {
		return originalName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUrlName() {
		return urlName;
	}
	
	/**
	 * Returns true if this table has a DATE column
	 * 
	 * @return true if this table has a DATE column.
	 */
	public boolean hasDate() {
		return hasDate;
	}
	
	/**
	 * Returns true if this table has a column supporting the Spring Boot
	 * <code>@Size</code> annotation.
	 * 
	 * @return true if this table has a column supporting the Spring Boot
	 * <code>@Size</code> annotation.
	 */
	public boolean hasSize() {
		return hasSize;
	}
	
	/**
	 * Returns true if this table has a composite key - having more than one primary key
	 * 
	 * @return true if this table has a composite key
	 */
	public boolean hasCompositeKey() {
		return primaryKeyNames.size() > 1;
	}
	
	public boolean hasMaxIdMethod() throws Exception {
		if(hasCompositeKey()) {
			return false;
		}

		String primaryKeyType = SpringWriterUtil.getPrimaryKeyType(this);
		return primaryKeyType.equals("Integer") || primaryKeyType.equals("Long");
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("=== MySQLTable ===\n");
		sb.append(String.format("Name:\n\t\"%s\"\n", name));
		
		sb.append("Columns:\n");
		final String HEADER_FMT = "\t%-15s %-10s %s\n";
		sb.append(String.format(HEADER_FMT, 
				"NAME", "TYPE", "LENGTH"
		));
		sb.append(String.format(HEADER_FMT, 
				"------", "------", "--------"
		));
		for(MySQLColumn col : columnList) {
			sb.append(String.format(HEADER_FMT, 
					col.getName(), col.getMySQLType().name(), col.getLength()
			));
		}
		
		sb.append(String.format("Primary Key%s:\n\t",
				primaryKeyNames.size() > 1 ? "s" : ""
		));
		
		for(int i = 0; i < primaryKeyNames.size(); i++) {
			sb.append(primaryKeyNames.get(i));
			sb.append(i+1 < primaryKeyNames.size() ? ", " : "\n");
		}
		
		return sb.toString();
	}
}
