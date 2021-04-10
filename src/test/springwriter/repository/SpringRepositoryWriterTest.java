package test.springwriter.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import mysqlentity.datatype.MySQLType;
import mysqlentity.mysqlcolumn.MySQLColumn;
import mysqlentity.mysqltable.MySQLTable;
import springwriter.SpringWriter;
import springwriter.model.SpringModelWriter;
import springwriter.repository.SpringRepositoryWriter;
import test.testutil.TestUtility;
import test.testutil.WriterTestData;

public class SpringRepositoryWriterTest {
	private final String REPO_DIR = "src/test/springwriter/repository";
	
	private SpringRepositoryWriter newRepoWriter(String directoryPath, MySQLTable table) throws Exception {
		SpringWriter springWriter = new SpringWriter(directoryPath, table);
		return new SpringRepositoryWriter(springWriter);
	}
	
	@Test
	@DisplayName("writeRepositoryFile should write repository file")
	public void testWriteRepositoryFile() throws Exception {
		MySQLTable table = WriterTestData.bookTable();
		SpringRepositoryWriter repoWriter = newRepoWriter(REPO_DIR, WriterTestData.bookTable());
		
		repoWriter.writeRepositoryFile();
		TestUtility.evaluateFileContents(table, "repository");
	}
	
	@Test
	@DisplayName("writeRepositoryFile should throw an Exception when PRIMARY KEY isn't defined")
	public void testNoPrimaryKeyException() throws Exception {
		SpringRepositoryWriter repoWriter = newRepoWriter(REPO_DIR, WriterTestData.personTable());
		
		Exception exception = assertThrows(Exception.class, ()->{
			repoWriter.writeRepositoryFile();
		});
		assertEquals("no primary key defined in table 'Person'", exception.getMessage());
	}
	
}
