package test.mysqlentity.mysqltable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import mysqlentity.datatype.MySQLType;
import mysqlentity.mysqlcolumn.MySQLColumn;
import mysqlentity.mysqltable.MySQLTable;

public class MySQLTableTest {
	@Test
	@DisplayName("constructor should set table name and booleans should be set to false")
	public void testConstructor() {
		MySQLTable table = new MySQLTable("new_table");
		assertEquals("NewTable", table.getName());
		assertFalse(table.hasDate());
		assertFalse(table.hasSize());
		assertFalse(table.hasCompositeKey());
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
	
	@Test
	@DisplayName("adding Date column should set hasDate to true")
	public void testAddDateColumn() throws Exception{
		MySQLTable table = new MySQLTable("Employee");
		table.addColumn(new MySQLColumn("start_dt", MySQLType.DATE, 0));
		assertTrue(table.hasDate());
	}
	
	@Test
	@DisplayName("adding column compatible with Size should set hasSize to true")
	public void testAddSizeColumn() throws Exception{
		MySQLTable charTable = new MySQLTable("charTable");
		charTable.addColumn(new MySQLColumn("my_char", MySQLType.CHAR, 12));
		MySQLTable varCharTable = new MySQLTable("varCharTable");
		varCharTable.addColumn(new MySQLColumn("my_char", MySQLType.VARCHAR, 15));
		
		assertTrue(charTable.hasSize());
		assertTrue(varCharTable.hasSize());
	}
	
	@Test
	@DisplayName("adding PRIMARY KEY for nonexistent column should throw an Exception")
	public void testAddingNonexistentPrimaryColumn() {
		MySQLTable table = new MySQLTable("Person");
		Exception exception = assertThrows(Exception.class, ()->{
			table.addPrimaryKey("id");
		});
		
		assertEquals("'id' doesn't exist in table 'Person'", exception.getMessage());
	}
	
	@Test
	@DisplayName("adding existing PRIMARY KEY should throw an Exception")
	public void testAddingExistingPrimaryKey() throws Exception {
		MySQLTable table = new MySQLTable("Person");
		table.addColumn(new MySQLColumn("id", MySQLType.INT, 11));
		table.addPrimaryKey("id");
		
		Exception exception = assertThrows(Exception.class, ()->{
			table.addPrimaryKey("id");
		});
		assertEquals("table 'Person' already has PRIMARY KEY 'id'", exception.getMessage());
	}
	
	@Test
	@DisplayName("addPrimaryKeyName should add primary key name when isPrimary is true")
	public void testAddPrimaryKeyName() throws Exception {
		MySQLTable table = new MySQLTable("Person");
		table.addColumn(new MySQLColumn("id", MySQLType.INT, 11));
		table.addPrimaryKey("id");
		
		assertEquals(1, table.getPrimaryKeyNames().size());
		assertEquals("id", table.getPrimaryKeyNames().get(0));
	}
	
	@Test
	@DisplayName("hasCompositeKey should return boolean based on primary key count")
	public void testHasCompositeKey() throws Exception{
		MySQLTable table = new MySQLTable("Student");
		table.addColumn(new MySQLColumn("id", MySQLType.INT, 11));
		table.addColumn(new MySQLColumn("school_id", MySQLType.INT, 11));
		
		table.addPrimaryKey("id");
		assertFalse(table.hasCompositeKey());
		table.addPrimaryKey("school_id");
		assertTrue(table.hasCompositeKey());
	}
	
	@Test
	@DisplayName("toString method should print out table name, columns and primary keys")
	public void testToString() throws Exception {
		MySQLTable table = new MySQLTable("Person");
		table.addColumn(new MySQLColumn("id", MySQLType.INT, 11));
		table.addColumn(new MySQLColumn("first_name", MySQLType.VARCHAR, 255));
		table.addColumn(new MySQLColumn("last_name", MySQLType.VARCHAR, 255));
		table.addPrimaryKey("id");
		
		assertEquals(expectedToString(), table.toString());
	}
	
	private String expectedToString() {
		StringBuilder sb = new StringBuilder("=== MySQLTable ===\n");
		sb.append("Name:\n\t\"Person\"\n");
		sb.append("Columns:\n");
		
		final String HEADER_FMT = "\t%-15s %-10s %s\n";
		sb.append(String.format(HEADER_FMT,"NAME", "TYPE", "LENGTH"));
		sb.append(String.format(HEADER_FMT, "------", "------", "--------"));
		sb.append(String.format(HEADER_FMT, "id", "INT", "11"));
		sb.append(String.format(HEADER_FMT, "first_name", "VARCHAR", "255"));
		sb.append(String.format(HEADER_FMT, "last_name", "VARCHAR", "255"));
		
		sb.append("Primary Key:\n");
		sb.append("\tid\n");
		
		return sb.toString();
	}
}
