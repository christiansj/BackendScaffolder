package springwriter.repository;

import java.io.PrintWriter;
import java.io.StringWriter;

import springwriter.SpringWriter;
import springwriter.SpringWriterUtil;
import springwriter.springfilewriter.SpringFileWriter;
import springwriter.springfilewriter.SpringFileWriterInterface;

public class SpringRepositoryWriter extends SpringFileWriter implements SpringFileWriterInterface{

	public SpringRepositoryWriter(SpringWriter springWriter) throws Exception {
		super(springWriter, "repository", "repositories");
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
		return String.format("public interface %sRepository extends JpaRepository<%s, %s> {\n", 
				mySQLTable.getName(),
				mySQLTable.getName(),
				SpringWriterUtil.getPrimaryKeyType(mySQLTable)
		);
	}
}
