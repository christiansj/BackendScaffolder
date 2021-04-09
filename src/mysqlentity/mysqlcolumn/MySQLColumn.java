package mysqlentity.mysqlcolumn;

import mysqlentity.datatype.MySQLType;

public class MySQLColumn {
	final String name;
	final MySQLType mySQLType;
	final int length;
	
	private boolean isNotNull = false;
	private boolean isPrimaryKey = false;
	
	public MySQLColumn(String name, MySQLType mySQLType, int length) {
		this.name = name;
		this.mySQLType = mySQLType;
		this.length = length;
	}
	
	public void setIsNotNull(boolean isNotNull) {
		this.isNotNull= isNotNull;
	}
	
	public void setIsPrimaryKey(boolean isPrimaryKey){
		this.isPrimaryKey = isPrimaryKey;
	}
	
	public String getName() {
		return name;
	}
	
	public MySQLType getMySQLType() {
		return mySQLType;
	}
	
	public int getLength() {
		return length;
	}
	
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
}
