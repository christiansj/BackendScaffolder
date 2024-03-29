package mysqlentity.mysqlcolumn;

import mysqlentity.datatype.MySQLType;

/**
 * <code>MySQLColumn</code> represents a column in MySQL. Contains information such as: name, type, and length.
 */
public class MySQLColumn {
	final String name;
	final MySQLType mySQLType;
	final int length;
	
	private boolean isNotNull = false;
	private boolean isPrimaryKey = false;
	private boolean isUnique = false;
	
	/**
	 * Creates a <code>MySQLColumn</code> object.
	 * 
	 * @param name String name of this column
	 * @param mySQLType <code>MySQLType</code> type of this column
	 * @param length int length of this column
	 */
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
	
	public void setIsUnique(boolean isUnique) {
		this.isUnique = isUnique;
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
	
	public boolean isUnique() {
		return isUnique;
	}
}
