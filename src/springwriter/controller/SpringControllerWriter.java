package springwriter.controller;

import mysqlentity.mysqltable.MySQLTable;
import springwriter.SpringWriter;

public class SpringControllerWriter {
	SpringWriter springWriter;
	MySQLTable mySQLTable;
	
	public SpringControllerWriter(SpringWriter springWriter) {
		this.springWriter = springWriter;
		mySQLTable = springWriter.getMySqlTable();
	}
	
	public void writeControllerFile() throws Exception {
		
	}
}
