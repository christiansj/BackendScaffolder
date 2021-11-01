package springwriter.model;

import java.io.PrintWriter;
import java.io.StringWriter;

import mysqlentity.mysqlcolumn.MySQLColumn;
import springwriter.SpringWriter;
import springwriter.SpringWriterUtil;
import springwriter.springfilewriter.SpringFileWriter;

public class SpringModelWriter extends SpringFileWriter {
	private final boolean HAS_OPTIONAL_IMPORTS;
	private final String PACKAGE_STR;
	
	public SpringModelWriter(SpringWriter springWriter) throws Exception {
		super(springWriter, "model", "models");
		PACKAGE_STR = springWriter.createPackageStr(SINGULAR, PLURAL);
		HAS_OPTIONAL_IMPORTS = mySQLTable.hasCompositeKey() || mySQLTable.hasDate() || mySQLTable.hasSize();
	}
		
	public String createFileString() throws Exception {
		StringBuilder sb = new StringBuilder();
		
		// package and imports
		sb.append("package " + PACKAGE_STR + ";\n");
		sb.append(importStrings());
		
		// class body
		sb.append("@Entity\n");
		for(String l : LOMBOKS) {
			sb.append(String.format("@%s\n", l));
		}
		sb.append(String.format("public class %s {\n\n", mySQLTable.getName()));
		sb.append(createVariableString());
		sb.append("}\n");
		
		return sb.toString();
	}
		
	private String importStrings() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		pw.println();
		
		if(HAS_OPTIONAL_IMPORTS) {
			writeOptionalImports(pw);
		}
		
		pw.println("import javax.persistence.Entity;");
		pw.println(String.format("import javax.persistence.%s;", 
				mySQLTable.hasCompositeKey() ? "EmbeddedId" : "Id"));
		
		pw.println();
		pw.println(SpringWriterUtil.writeImports("lombok", LOMBOKS));
		
		return sw.toString();
	}
	
	private void writeOptionalImports(PrintWriter pw) {
		if(mySQLTable.hasCompositeKey()) {
			pw.println(String.format("import %s.%s;\n",
					PACKAGE_STR, mySQLTable.getName() + "Identity"
			));
		}
		
		if(mySQLTable.hasDate()) {
			pw.println("import java.util.Date;");
		}
		
		if(mySQLTable.hasSize()) {
			pw.println("import javax.persistence.Column;");
		}
		
		pw.println();
	}
	
	private String createVariableString() throws Exception {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		if(mySQLTable.hasCompositeKey()) {
			pw.println("\t@EmbeddedId");
			pw.println(String.format("\tprivate %sIdentity %sIdentity;\n", 
					mySQLTable.getName(),
					SpringWriterUtil.lowercaseFirstChar(mySQLTable.getName())
			));
		}
		
		for(MySQLColumn col : mySQLTable.getColumns()) {
			String colType = col.getMySQLType().name();
			if(col.isPrimaryKey() && mySQLTable.hasCompositeKey()) {
				continue;
			}
			
			if(!mySQLToJavaMap.containsKey(colType)) {
				String err = String.format("'%s' is not in MySQLToJavaMap", colType);
				throw new Exception(err);
			}
			final String VARIABLE_TYPE = mySQLToJavaMap.get(colType);
			
			// @Id
			if(col.isPrimaryKey()) {
				pw.println("\t@Id");
			}
			
			if(VARIABLE_TYPE.equals("String")  && col.getLength() > 0) {
				pw.println(String.format("\t@Column(length = %d)", col.getLength()));
			}
			
			pw.println(String.format("\tprivate %s %s;\n", 
					mySQLToJavaMap.get(colType), 
					SpringWriterUtil.camelCaseMySQLVariable(col.getName())
			));
		}
		
		return sw.toString();
	}
}
