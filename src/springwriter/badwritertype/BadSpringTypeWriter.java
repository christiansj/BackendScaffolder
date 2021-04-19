package springwriter.badwritertype;

import mysqlentity.mysqltable.MySQLTable;
import springwriter.SpringWriter;
import springwriter.springfilewriter.SpringFileWriter;

/** 
 * This class is used to test Exception thrown in  
 * writeFile() method within SpringFileWriter.java
 */
public class BadSpringTypeWriter extends SpringFileWriter {

	public BadSpringTypeWriter() throws Exception {
		super(new SpringWriter("src/bad", new MySQLTable("Bad")), "bad", "bads");
	}

	public String createFileString() throws Exception {
		return null;
	}	
}
