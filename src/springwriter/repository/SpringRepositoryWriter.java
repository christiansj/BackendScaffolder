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
		sb.append("package " + springWriter.getPackageStr()+ ";\n");
		sb.append("\n");
		sb.append(importStrings());
		
		// class body
		sb.append("@Repository\n");
		sb.append(prototypeString());
		if(mySQLTable.hasMaxIdMethod()) {
			sb.append(bodyString());
		}
		sb.append("\n");
		sb.append("}\n");
		
		return sb.toString();
	}
	
	private String importStrings() throws Exception{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		if(mySQLTable.hasMaxIdMethod()) {
			pw.println("import org.springframework.data.jpa.repository.Query;");
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
	
	private String bodyString() throws Exception {
		if(!mySQLTable.hasMaxIdMethod()) {
			return "";
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		String primaryKeyName = mySQLTable.getPrimaryKeyNames().get(0);
		primaryKeyName = mySQLTable.getColumn(primaryKeyName).getName();
		
		// @Query(value = "SELECT MAX(id) from table_name", nativeQuery = true)
		String queryString = String.format("\t@Query(value = \"SELECT IFNULL(MAX(%s), 0) FROM %s\", nativeQuery = true)\n",
				primaryKeyName, mySQLTable.getOriginalName());
		// Integer maxId();
		String methodString = String.format("\t%s maxId();\n", 
				SpringWriterUtil.getPrimaryKeyType(mySQLTable));
		return queryString + methodString;
	}
	
	
}
