package test.springwriter.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import mysqlentity.mysqltable.MySQLTable;
import springwriter.SpringWriter;
import springwriter.model.SpringIdentityWriter;
import springwriter.model.SpringModelWriter;
import test.testutil.TestUtility;
import test.testutil.WriterTestData;

public class SpringIdentityWriterTest {
	private final String MODEL_DIR = "src/test/springwriter/model";
	
	@Test
	@DisplayName("writeFile should write identity file with correct contents")
	public void testWriteIdentityFile() throws Exception{
		MySQLTable table = WriterTestData.studentTable();
		SpringWriter springWriter = new SpringWriter(MODEL_DIR, table);
		SpringIdentityWriter identityWriter = new SpringIdentityWriter(springWriter);
//		
		identityWriter.writeFile();
		TestUtility.evaluateFileContents(table, "identity");
		System.out.println(identityWriter.createFileString());
	}
}
