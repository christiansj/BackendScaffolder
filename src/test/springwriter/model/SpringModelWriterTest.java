package test.springwriter.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import mysqlentity.mysqltable.MySQLTable;
import springwriter.SpringWriter;
import springwriter.model.SpringModelWriter;
import test.testutil.TestUtility;
import test.testutil.WriterTestData;

public class SpringModelWriterTest {
	private final String MODEL_DIR = "src/test/springwriter/model";
	
	private SpringModelWriter newModelWriter(MySQLTable table) throws Exception {
		SpringWriter springWriter = new SpringWriter(MODEL_DIR, table);
		return new SpringModelWriter(springWriter);
	}
	
	@Test
	@DisplayName("writeFile should write model file with correct contents")
	public void testWriteModelFile() throws Exception{
		MySQLTable table = WriterTestData.personTable();
		SpringModelWriter modelWriter = newModelWriter(table);
		
		modelWriter.writeFile();
		TestUtility.evaluateFileContents(table, "model");
	}
	
	@Test
	@DisplayName("should generate model file with @Id field")
	public void testPrimaryKeyModel() throws Exception {
		MySQLTable table = WriterTestData.bookTable();
		SpringModelWriter modelWriter = newModelWriter(table);
		
		modelWriter.writeFile();
		TestUtility.evaluateFileContents(table, "model");
	}
	
	@Test
	@DisplayName("should generate model with Date import")
	public void testModelWithDate() throws Exception {
		MySQLTable table = WriterTestData.employeeTable();
		SpringModelWriter modelWriter = newModelWriter(table);
		
		modelWriter.writeFile();
		TestUtility.evaluateFileContents(table, "model");
	}
	
	@Test
	@DisplayName("should generate model with composite variable")
	public void testModelWithComposite() throws Exception {
		MySQLTable table = WriterTestData.studentTable();
		SpringModelWriter modelWriter = newModelWriter(table);
		
		modelWriter.writeFile();
		TestUtility.evaluateFileContents(table, "model");
	}
}
