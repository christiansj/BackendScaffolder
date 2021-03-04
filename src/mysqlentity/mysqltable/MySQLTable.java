package mysqlentity.mysqltable;

import java.util.HashSet;
import java.util.Set;

import mysqlentity.mysqlcolumn.MySQLColumn;

public class MySQLTable {
	private final String name;
	
	private Set<MySQLColumn> columns = new HashSet<>();
	
	public MySQLTable(String name) {
		this.name = name;
	}
	
	
	public void addColumn(MySQLColumn column) throws Exception{
		for(MySQLColumn currColumn : columns) {
			if(currColumn.getName().equals(column.getName())){
				throw new Exception(String.format("Column '%s' already exists in table '%s'", 
						column.getName(), name));
			}
		}
		columns.add(column);
	}
	
	public Set<MySQLColumn> getColumns(){
		return columns;
	}
	
	public String getName() {
		return name;
	}
}
