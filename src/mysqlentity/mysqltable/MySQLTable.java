package mysqlentity.mysqltable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mysqlentity.datatype.MySQLType;
import mysqlentity.mysqlcolumn.MySQLColumn;
import springwriter.SpringWriterUtil;

public class MySQLTable {

	private final String name;
	private ArrayList<String> primaryKeyNames = new ArrayList<String>();
	private ArrayList<MySQLColumn> columnList = new ArrayList<>();
	private HashMap<String, MySQLColumn> columnMap = new HashMap<>();
	
	private boolean hasDate = false;
	private boolean hasSize = false;
	
	public MySQLTable(String name) {
		this.name = SpringWriterUtil.uppercaseFirstChar(SpringWriterUtil.formatMySQLVariable(name));
	}

	public void addColumn(MySQLColumn column) throws Exception{
		if(columnMap.containsKey(column.getName())) {
			throw new Exception(String.format("Column '%s' already exists in table '%s'", 
					column.getName(), name));
		}
		setBooleans(column);
		
		columnMap.put(column.getName(), column);
		columnList.add(column);
	}
	
	private void setBooleans(MySQLColumn column) {
		List<String> SUPPORTED_SIZE_COLS =   Arrays.asList("VARCHAR", "CHAR"); 
		final MySQLType colType = column.getMySQLType();
		
		if(colType == MySQLType.DATE) {
			hasDate = true;
		}else if(column.getLength() > 0 && SUPPORTED_SIZE_COLS.contains(colType.name())) {
			hasSize = true;
		}
	}
	
	public void addPrimaryKey(String colName, boolean isPrimary) throws Exception {
		MySQLColumn col = columnMap.get(colName);
		if(col == null) {
			String fmt = "'%s' doesn't exist in table '%s'";
			throw new Exception(String.format(fmt, colName, name));
		}
		else if(primaryKeyNames.contains(colName) && isPrimary) {
			String fmt = "table '%s' already has PRIMARY KEY '%s'";
			throw new Exception(String.format(fmt, name, colName));
		}
		
		col.setIsPrimaryKey(isPrimary);
		columnList.set(columnList.indexOf(col), col);
		columnMap.put(colName, col);
		
		if(isPrimary) {
			primaryKeyNames.add(colName);
		}else {
			primaryKeyNames.remove(colName);
		}
	}
	
	public ArrayList<String> getPrimaryKeyNames(){
		return primaryKeyNames;
	}
	
	public ArrayList<MySQLColumn> getColumns(){
		return columnList;
	}
	
	public MySQLColumn getColumn(String columnName) throws Exception{
		if(!columnMap.containsKey(columnName)) {
			String fmt = String.format("column '%s' isn't defined in table '%s'", columnName, name);
			throw new Exception(fmt);
		}
		return columnMap.get(columnName);
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasDate() {
		return hasDate;
	}
	
	public boolean hasSize() {
		return hasSize;
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
