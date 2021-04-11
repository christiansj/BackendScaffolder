package springwriter.springfilewriter;

import mysqlentity.mysqltable.MySQLTable;
import springwriter.SpringWriter;

public abstract class SpringFileWriter {
	public SpringWriter springWriter;
	public MySQLTable mySQLTable; 
	
	public SpringFileWriter(SpringWriter springWriter) {
		this.springWriter = springWriter;
		mySQLTable = springWriter.getMySqlTable();
	}
}
