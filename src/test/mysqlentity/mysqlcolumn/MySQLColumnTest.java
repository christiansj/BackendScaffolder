package test.mysqlentity.mysqlcolumn;

import mysqlentity.datatype.MySQLType;
import mysqlentity.mysqlcolumn.MySQLColumn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MySQLColumnTest {
	@Test
	@DisplayName("constructor should set member variables")
	public void testConstructor() {
		MySQLColumn mySQLColumn = new MySQLColumn("New Column", MySQLType.VARCHAR, 200);
		
		assertEquals("New Column", mySQLColumn.getName());
		assertEquals(MySQLType.VARCHAR, mySQLColumn.getMySQLType());
		assertEquals(200, mySQLColumn.getLength());
	}
	
}
