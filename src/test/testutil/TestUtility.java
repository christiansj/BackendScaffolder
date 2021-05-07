package test.testutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import mysqlentity.mysqltable.MySQLTable;
import springwriter.SpringWriter;
import springwriter.controller.SpringControllerWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUtility {
	
	public static void evaluateFileContents(MySQLTable table, String springFileType) throws Exception {
		final String WRITER_DIR = "src/test/springwriter/";

		HashMap<String, String> springDirMap = new HashMap<>();
		
		springDirMap.put("model", "model/models/" + table.getName());
		springDirMap.put("identity", "model/models/" + table.getName() + "Identity");
		springDirMap.put("repository", String.format("repository/repositories/%sRepository", table.getName()));
		springDirMap.put("controller", String.format("controller/controllers/%sController", table.getName()));
		if(!springDirMap.containsKey(springFileType)) {
			throw new Exception("TestUtility - bad springFileType '%s', springFileType must be 'model' 'repository' 'identity' or 'controller'");
		}
		
		String testFilePath = String.format("%s%s.java", WRITER_DIR, springDirMap.get(springFileType), table.getName());
		String expectedFilePath = String.format("%s%s.txt", WRITER_DIR, springDirMap.get(springFileType), table.getName());
		
		final String EXPECTED = fileToString(expectedFilePath);
		final String ACTUAL = fileToString(testFilePath);
		new File(testFilePath).delete();
		
		assertEquals(EXPECTED, ACTUAL);
	}
	
	private static String fileToString(String filePath) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String str = reader.lines().collect(Collectors.joining("\n"));
		reader.close();
		
		return str;
	}
	
	public static SpringControllerWriter newControllerWriter(String directoryPath, MySQLTable table) throws Exception {
		SpringWriter writer = new SpringWriter(directoryPath, table);
		return new SpringControllerWriter(writer);
	}
}
