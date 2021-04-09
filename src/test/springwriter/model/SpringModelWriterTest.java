package test.springwriter.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat; 

import mysqlentity.datatype.MySQLType;
import mysqlentity.mysqlcolumn.MySQLColumn;
import mysqlentity.mysqltable.MySQLTable;
import springwriter.SpringWriter;
import springwriter.model.SpringModelWriter;

public class SpringModelWriterTest {
	@Test
	@DisplayName("testWriteModelFile should write model file with correct contents")
	public void testWriteModelFile() throws Exception{
		MySQLTable table = new MySQLTable("Person");
		table.addColumn(new MySQLColumn("id", MySQLType.INT, 100));
		table.addColumn(new MySQLColumn("first_name", MySQLType.VARCHAR, 100));
		table.addColumn(new MySQLColumn("last_name", MySQLType.VARCHAR, 100));
		
		SpringWriter springWriter = new SpringWriter("src/test/springwriter/model", table);
		SpringModelWriter modelWriter = new SpringModelWriter(springWriter);
		
		modelWriter.writeModelFile();
		evaluateFileContents("Person");
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
