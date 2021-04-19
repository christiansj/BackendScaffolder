package springwriter.repository;

import java.io.PrintWriter;
import java.io.StringWriter;

import springwriter.SpringWriter;
import springwriter.SpringWriterUtil;
import springwriter.springfilewriter.SpringFileWriter;

public class SpringRepositoryWriter extends SpringFileWriter {

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
		final String PACKAGE_STR = String.format("%s.%s", 
				springWriter.createPackageStr("model", "models"),
				mySQLTable.getName()
		);
		pw.println(String.format("import %s;", 
				PACKAGE_STR
		));
		if(mySQLTable.hasCompositeKey()) {
			pw.println(String.format("import %sIdentity;", PACKAGE_STR));
		}
			
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
