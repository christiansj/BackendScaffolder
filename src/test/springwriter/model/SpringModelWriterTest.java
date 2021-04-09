package test.springwriter.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import mysqlentity.datatype.MySQLType;
import mysqlentity.mysqlcolumn.MySQLColumn;
import mysqlentity.mysqltable.MySQLTable;
import springwriter.SpringWriter;
import springwriter.model.SpringModelWriter;

public class SpringModelWriterTest {
	private final String MODEL_DIR = "src/test/springwriter/model";
	private SpringModelWriter newModelWriter(String directoryPath, MySQLTable table) throws Exception {
		SpringWriter springWriter = new SpringWriter(directoryPath, table);
		return new SpringModelWriter(springWriter);
	}
	@Test
	@DisplayName("testWriteModelFile should write model file with correct contents")
	public void testWriteModelFile() throws Exception{
		MySQLTable table = new MySQLTable("Person");
		table.addColumn(new MySQLColumn("id", MySQLType.INT, 100));
		table.addColumn(new MySQLColumn("first_name", MySQLType.VARCHAR, 100));
		table.addColumn(new MySQLColumn("last_name", MySQLType.VARCHAR, 100));
		
		SpringModelWriter modelWriter = newModelWriter(MODEL_DIR, table);
		
		modelWriter.writeModelFile();
		evaluateFileContents("Person");
	}
	
	@Test
	@DisplayName("should generate model file with @Id field")
	public void testPrimaryKeyModel() throws Exception {
		MySQLTable table = new MySQLTable("Book");
		table.addColumn(new MySQLColumn("id", MySQLType.INT, 10));
		table.addColumn(new MySQLColumn("title", MySQLType.VARCHAR, 255));
		table.addPrimaryKey("id", true);
		
		SpringModelWriter modelWriter = newModelWriter(MODEL_DIR, table);
		modelWriter.writeModelFile();
		
		evaluateFileContents("Book");
	}
	
	private void evaluateFileContents(String className) throws Exception {
		String filePath = String.format("src/test/springwriter/model/models/%s.java", className);
		String expectedPath = String.format("src/test/springwriter/model/models/%s.txt", className);

		assertEquals(fileToString(expectedPath), fileToString(filePath));
		new File(filePath).delete();
	}
	
	private String fileToString(String filePath) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String str = reader.lines().collect(Collectors.joining("\n"));
		reader.close();
		
		return str;
	}
}
