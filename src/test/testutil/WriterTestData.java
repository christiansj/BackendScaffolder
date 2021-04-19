package test.testutil;

import mysqlentity.datatype.MySQLType;
import mysqlentity.mysqlcolumn.MySQLColumn;
import mysqlentity.mysqltable.MySQLTable;

public class WriterTestData {
	public static MySQLTable bookTable() throws Exception {
		MySQLTable table = new MySQLTable("Book");
		table.addColumn(new MySQLColumn("id", MySQLType.INT, 10));
		table.addColumn(new MySQLColumn("title", MySQLType.VARCHAR, 255));
		table.addPrimaryKey("id", true);
		
		return table;
	}
	
	public static MySQLTable personTable() throws Exception {
		MySQLTable table = new MySQLTable("Person");
		table.addColumn(new MySQLColumn("id", MySQLType.INT, 100));
		table.addColumn(new MySQLColumn("first_name", MySQLType.VARCHAR, 100));
		table.addColumn(new MySQLColumn("last_name", MySQLType.VARCHAR, 100));
		
		return table;
	}
	
	public static MySQLTable employeeTable() throws Exception {
		MySQLTable table = new MySQLTable("Employee");
		table.addColumn(new MySQLColumn("id", MySQLType.INT, 100));
		table.addColumn(new MySQLColumn("start_date", MySQLType.DATE, 0));
		table.addPrimaryKey("id", true);
		
		return table;
	}
	
	public static MySQLTable studentTable() throws Exception {
		MySQLTable table = new MySQLTable("Student");
		table.addColumn(new MySQLColumn("id", MySQLType.INT, 11));
		table.addColumn(new MySQLColumn("school_id", MySQLType.VARCHAR, 30));
		table.addColumn(new MySQLColumn("first_name", MySQLType.VARCHAR, 255));
		table.addColumn(new MySQLColumn("last_name", MySQLType.VARCHAR, 255));
		table.addColumn(new MySQLColumn("major", MySQLType.VARCHAR, 50));
		
		table.addPrimaryKey("id", true);
		table.addPrimaryKey("school_id", true);
		
		return table;
	}
}
