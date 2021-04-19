package test.springwriter.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
	@DisplayName("evaluateFileContents should throw an Excpetion when given invalid springFileType")
	public void testEvaluateFileContents() throws Exception {
		Exception exception = assertThrows(Exception.class, ()->{
			TestUtility.evaluateFileContents(new MySQLTable("bad_type"), "bad");
		});
		assertEquals("TestUtility - bad springFileType '%s', springFileType must be 'model' 'repository' 'identity' or 'controller'", exception.getMessage());	
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
