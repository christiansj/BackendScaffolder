package test.springwriter;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import mysqlentity.datatype.MySQLType;
import mysqlentity.mysqlcolumn.MySQLColumn;
import mysqlentity.mysqltable.MySQLTable;
import springwriter.SpringWriterUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpringWriterUtilTest {
	@Test
	@DisplayName("writeImports should return String with imports")
	public void writeImportsTest() {
		String expected = "import com.alpha.bravo.one;\nimport com.alpha.bravo.two;\nimport com.alpha.bravo.three;\n";
		String[] packages = {"one", "two", "three"};
		assertEquals(expected, SpringWriterUtil.writeImports("com.alpha.bravo", packages));
	}
	
	@Test
	@DisplayName("formatMySQLVariable should return camelcased String with removed underscores")
	public void formatMySQLVariableTest() {
		assertEquals("one", SpringWriterUtil.formatMySQLVariable("one"));
		assertEquals("alphaBravoCharlie", SpringWriterUtil.formatMySQLVariable("alpha_bravo_charlie"));
	}
	
	@Test
	@DisplayName("getPrimaryKeyType should return correct String for Primary Key")
	public void getPrimaryKeyTypeTest() throws Exception {
		assertEquals("Integer", SpringWriterUtil.getPrimaryKeyType(newTable(MySQLType.INT)));
		assertEquals("Long", SpringWriterUtil.getPrimaryKeyType(newTable(MySQLType.BIGINT)));
		assertEquals("String", SpringWriterUtil.getPrimaryKeyType(newTable(MySQLType.VARCHAR)));
	}
	
	private MySQLTable newTable(MySQLType primaryType) throws Exception {
		MySQLTable table = new MySQLTable("test_table");
		table.addColumn(new MySQLColumn("id", primaryType, 100));
		table.addPrimaryKey("id", true);
		
		return table;
	}
	
	@Test
	@DisplayName("getPrimaryKeyType should throw an Exception when passed in a table with incompatible primary key")
	public void getPrimaryKeyTypeIncompatibleTest() throws Exception {
		Exception exception = assertThrows(Exception.class, ()->{
			SpringWriterUtil.getPrimaryKeyType(newTable(MySQLType.DATE));
		});
		assertEquals("Primary key type 'DATE' isn't defined in mySQLToIdTypeMap", exception.getMessage());
	}
	
	@Test
	@DisplayName("getPrimaryKeyType should throw an Exception when passed in a table with a composite key")
	public void getPrimaryKeyTypeCompositeKey() throws Exception {
		Exception exception = assertThrows(Exception.class, ()->{
			MySQLTable table = newTable(MySQLType.INT);
			table.addColumn(new MySQLColumn("id_two", MySQLType.INT, 11));
			table.addPrimaryKey("id_two", true);
			
			SpringWriterUtil.getPrimaryKeyType(table);
		});
		assertEquals("composite keys are not supported yet", exception.getMessage());
	}
	
	@Test
	@DisplayName("getPrimaryKeyType should throw an Exception when passed in a table without a primary key")
	public void getPrimarykeyTypeWithoutPrimaryTest() throws Exception {
		Exception exception = assertThrows(Exception.class, ()->{
			MySQLTable table = new MySQLTable("test");
			table.addColumn(new MySQLColumn("id", MySQLType.INT, 11));
			
			SpringWriterUtil.getPrimaryKeyType(table);
		});
		assertEquals("no primary key defined in table 'Test'", exception.getMessage());
	}
	
	@Test
	@DisplayName("uppercaseFirstChar should uppercase first char of String and return it")
	public void uppercaseFirstCharTest() {
		assertEquals("Abc", SpringWriterUtil.uppercaseFirstChar("abc"));
		assertEquals("CoolKids", SpringWriterUtil.uppercaseFirstChar("coolKids"));
	}
	
	@Test
	@DisplayName("lowercaseFirstChar should lowercase first char of String and return it")
	public void lowercaseFirstCharTest() {
		assertEquals("red", SpringWriterUtil.lowercaseFirstChar("Red"));
		assertEquals("myVariable", SpringWriterUtil.lowercaseFirstChar("MyVariable"));
	}
}
