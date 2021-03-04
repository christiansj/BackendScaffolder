package test.mysqlentity.mysqlforeignkey;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import mysqlentity.datatype.MySQLType;
import mysqlentity.mysqlforeignkey.MySQLForeignKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MySQLForeignKeyTest {
	@Test
	@DisplayName("constructor should set member variables")
	public void testConstructor() {
		MySQLForeignKey foreignKey = new MySQLForeignKey("Foreign Key", MySQLType.INT, 10, "Foreign Table", "Foreign Column");
		
		assertEquals("Foreign Key", foreignKey.getName());
		assertEquals(MySQLType.INT, foreignKey.getMySQLType());
		assertEquals(10, foreignKey.getLength());
		assertEquals("Foreign Table", foreignKey.getForeignTableName());
		assertEquals("Foreign Column", foreignKey.getForeignColumnName());
	}
}
