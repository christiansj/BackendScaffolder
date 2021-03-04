package test.mysqlentity.mysqltable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import mysqlentity.datatype.MySQLType;
import mysqlentity.mysqlcolumn.MySQLColumn;
import mysqlentity.mysqltable.MySQLTable;

public class MySQLTableTest {
	@Test
	@DisplayName("constructor should set table name")
	public void testConstructor() {
		assertEquals("New Table", new MySQLTable("New Table").getName());
	}
	
	@Test
	@DisplayName("addColumn should add column")
	public void testAddColumn() throws Exception{
		MySQLTable table = new MySQLTable("Table");
		
		table.addColumn(new MySQLColumn("Column", MySQLType.VARCHAR, 64));
	}
	
	@Test
	@DisplayName("adding duplicate column should throw an Exception")
	public void testAddDuplicateColumn() throws Exception{
		MySQLTable table = new MySQLTable("Fruit");
		
		table.addColumn(new MySQLColumn("Apple", MySQLType.VARCHAR, 64));
		
		Exception exception = assertThrows(Exception.class, ()->{
			table.addColumn(new MySQLColumn("Apple", MySQLType.INT, 10));
		});
		
		assertEquals("Column 'Apple' already exists in table 'Fruit'", exception.getMessage());
	}
}
