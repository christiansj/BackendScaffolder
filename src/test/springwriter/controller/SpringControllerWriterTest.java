package test.springwriter.controller;

import springwriter.SpringWriter;
import springwriter.controller.SpringControllerWriter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import mysqlentity.mysqltable.MySQLTable;
import test.testutil.TestUtility;
import test.testutil.WriterTestData;

public class SpringControllerWriterTest {
	private final String CONTROL_DIR = "src/test/springwriter/controller";
	
	private SpringControllerWriter newControllerWriter(String directoryPath, MySQLTable table) throws Exception{
		SpringWriter writer = new SpringWriter(directoryPath, table);
		return new SpringControllerWriter(writer);
	}
	
	@Test
	@DisplayName("testWriterControllerFile should write Controller")
	public void testWriterControllerFile() throws Exception{
		MySQLTable table = WriterTestData.bookTable();
		SpringControllerWriter controllerWriter = newControllerWriter(CONTROL_DIR, table);
		
		controllerWriter.writeFile();
		TestUtility.evaluateFileContents(table, "controller");
	}
}
