package springwriter.model;

import java.io.PrintWriter;
import java.io.StringWriter;

import mysqlentity.mysqlcolumn.MySQLColumn;
import springwriter.SpringWriter;
import springwriter.SpringWriterUtil;
import springwriter.springfilewriter.SpringFileWriter;

public class SpringIdentityWriter extends SpringFileWriter {
	
	public SpringIdentityWriter(SpringWriter springWriter) {
		super(springWriter, "model", "models");
	}

	public String createFileString() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("package " + springWriter.createPackageStr(SINGULAR, PLURAL) + ";\n");
		sb.append(importStrings());
		
		sb.append("@Embeddable\n");
		for(String l : LOMBOKS) {
			sb.append(String.format("@%s\n", l));
		}
		sb.append(String.format("public class %sIdentity {\n\n", mySQLTable.getName()));
		sb.append(variableString());
		sb.append("}\n");
		
		return sb.toString();
	}
	
	private String importStrings() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		pw.println();
		pw.println("import javax.persistence.Embeddable;");
		pw.println("import javax.validation.constraints.NotNull;");
		
		if(mySQLTable.hasSize()) {
			pw.println("import javax.validation.constraints.Size;");
		}
		
		pw.println();
		pw.println(SpringWriterUtil.writeImports("lombok", LOMBOKS));
		
		return sw.toString();
	}
	
	private String variableString() throws Exception {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		for(String prim : mySQLTable.getPrimaryKeyNames()) {
			MySQLColumn col = mySQLTable.getColumn(prim);
			String colType = col.getMySQLType().name();
			
			if(!mySQLToJavaMap.containsKey(colType)) {
				String err = String.format("'%s' is not in MySQLToJavaMap", colType);
				throw new Exception(err);
			}
			
			pw.println("\t@NotNull");
			
			final String VARIABLE_TYPE = mySQLToJavaMap.get(col.getMySQLType().name());
			final int COL_LENGTH = col.getLength();
			if(VARIABLE_TYPE == "String" && COL_LENGTH > 0) {
				pw.println(String.format("\t@Size(max = %d)", COL_LENGTH));
			}
			
			pw.println(String.format("\tprivate %s %s;\n", 
					VARIABLE_TYPE, 
					SpringWriterUtil.formatMySQLVariable(col.getName())
			));
		}
		
		return sw.toString();
	}
}
