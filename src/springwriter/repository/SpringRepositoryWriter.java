package springwriter.repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import springwriter.SpringWriter;
import springwriter.springfilewriter.SpringFileWriter;
import springwriter.springfilewriter.SpringFileWriterInterface;

public class SpringRepositoryWriter extends SpringFileWriter implements SpringFileWriterInterface{
	final String SINGULAR = "repository";
	final String PLURAL = "repositories";
	

	HashMap <String, String> mySQLToIdTypeMap = new HashMap<>();
	
	public SpringRepositoryWriter(SpringWriter springWriter) throws Exception {
		super(springWriter);
		
		initMySQLtoIdTypeMap();
	}
	
	private void initMySQLtoIdTypeMap() {
		mySQLToIdTypeMap.put("INT", "Integer");
		mySQLToIdTypeMap.put("BIGINT", "Integer");
		mySQLToIdTypeMap.put("VARCHAR", "String");
	}
	
	public void writeFile() throws Exception {
		String filePath = String.format("%s/%sRepository.java", 
				springWriter.setDirectory(SINGULAR, PLURAL),
				mySQLTable.getName()
		);
		final String REPO_STRING = createFileString();
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		
		writer.write(REPO_STRING);
		writer.close();
	}
	
	public String createFileString() throws Exception{
		StringBuilder sb = new StringBuilder();
		// package and imports
		sb.append("package " + springWriter.createPackageStr(SINGULAR, PLURAL)+ ";\n");
		sb.append("\n");
		sb.append(importStrings());
		
		// class body
		sb.append("@Repository\n");
		sb.append(prototypeString());
		sb.append("\n");
		sb.append("}\n");
		
		return sb.toString();
	}
	
	private String importStrings() throws Exception{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		pw.println(String.format("import %s.%s;", 
				springWriter.createPackageStr("model", "models"),
				mySQLTable.getName())
		);
		pw.println("import org.springframework.data.jpa.repository.JpaRepository;");
		pw.println("import org.springframework.stereotype.Repository;\n");
		
		return sw.toString();
	}
	
	private String prototypeString() throws Exception{
		final int PRIMARY_COUNT = mySQLTable.getPrimaryKeyNames().size();
		if(PRIMARY_COUNT == 0) {
			throw new Exception(String.format("no primary key defined in table '%s'", 
					mySQLTable.getName()));
		}else if(PRIMARY_COUNT > 1) {
			throw new Exception("composite keys are not supported yet");
		}
		
		String primaryKeyName = mySQLTable.getPrimaryKeyNames().get(0);
		String primaryKeyType = mySQLTable.getColumn(primaryKeyName).getMySQLType().name();
		return String.format("public interface %sRepository extends JpaRepository<%s, %s> {\n", 
				mySQLTable.getName(),
				mySQLTable.getName(),
				mySQLToIdTypeMap.get(primaryKeyType)
		);
	}
}
