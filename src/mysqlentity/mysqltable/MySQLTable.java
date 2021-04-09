package mysqlentity.mysqltable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import mysqlentity.mysqlcolumn.MySQLColumn;

public class MySQLTable {
	
	private final String name;
	private ArrayList<String> primaryKeyNames = new ArrayList<String>();
	private ArrayList<MySQLColumn> columnList = new ArrayList<>();
	private HashMap<String, MySQLColumn> columnMap = new HashMap<>();
	
	public MySQLTable(String name) {
		this.name = name;
	}

	public void addColumn(MySQLColumn column) throws Exception{
		if(columnMap.containsKey(column.getName())) {
			throw new Exception(String.format("Column '%s' already exists in table '%s'", 
					column.getName(), name));
		}

		columnMap.put(column.getName(), column);
		columnList.add(column);
	}
	
	public void addPrimaryKey(String colName, boolean isPrimary) throws Exception {
		MySQLColumn col = columnMap.get(colName);
		if(col == null) {
			String fmt = "'%s' doesn't exists in table '%s'";
			throw new Exception(String.format(fmt, colName, name));
		}
		
		col.setIsPrimaryKey(isPrimary);
		columnList.set(columnList.indexOf(col), col);
		columnMap.put(colName, col);
		primaryKeyNames.add(name);
	}
	
	public ArrayList<MySQLColumn> getColumns(){
		return columnList;
	}
	
	public String getName() {
		return name;
	}
}
