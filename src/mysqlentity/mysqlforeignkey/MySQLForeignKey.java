package mysqlentity.mysqlforeignkey;

import mysqlentity.datatype.MySQLType;
import mysqlentity.mysqlcolumn.MySQLColumn;

public class MySQLForeignKey extends MySQLColumn{
	private final String foreignTableName;
	private final String foreignColumnName; 
	
	public MySQLForeignKey(String name, MySQLType mySQLType, int length, String foreignTableName, String foreignColumnName) {
		super(name, mySQLType, length);
		this.foreignTableName = foreignTableName;
		this.foreignColumnName = foreignColumnName;
	}
	
	public String getForeignTableName() {
		return foreignTableName;
	}
	
	public String getForeignColumnName() {
		return foreignColumnName;
	}
}
