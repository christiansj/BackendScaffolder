package springwriter.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import springwriter.SpringWriter;
import springwriter.SpringWriterUtil;
import springwriter.springfilewriter.SpringFileWriter;
import springwriter.springfilewriter.SpringFileWriterInterface;

public class SpringControllerWriter extends SpringFileWriter implements SpringFileWriterInterface {
	final String MODEL_PATH;
	final String TABLE_NAME;
	
	public SpringControllerWriter(SpringWriter springWriter) {
		super(springWriter, "controller", "controllers");
		
		this.TABLE_NAME = mySQLTable.getName();
		this.MODEL_PATH = String.valueOf(TABLE_NAME.charAt(0)).toLowerCase() + TABLE_NAME.substring(1);
	}
	
	public String createFileString() throws Exception{
		StringBuilder sb = new StringBuilder();
		
		sb.append("package " + springWriter.createPackageStr(SINGULAR, PLURAL) + ";");
		sb.append(importStrings());
		sb.append("@RestController\n");
		sb.append(String.format("class %sController {\n\n", TABLE_NAME));
		sb.append(classBodyString());
		sb.append("}\n");
		
		return sb.toString();
	}
	
	private String importStrings() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		final String[] ANNOTATIONS = {"RestController", "GetMapping", "PostMapping", "RequestBody"};
		
		pw.println("\n");
		pw.println("import java.util.List;");
		pw.println(String.format("import %s.%s;", 
				springWriter.createPackageStr("model", "models"), 
				TABLE_NAME));
		pw.println(String.format("import %s.%sRepository;",
				springWriter.createPackageStr("repository", "repositories"),
				TABLE_NAME));
		pw.println("import org.springframework.beans.factory.annotation.Autowired;");
		
		pw.println(SpringWriterUtil.writeImports("org.springframework.web.bind.annotation", ANNOTATIONS));
		
		
		return sw.toString();
	}
	
	private String classBodyString() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		pw.println("\t@Autowired");
		pw.println(String.format("\tprivate %sRepository repository;\n", TABLE_NAME));
		
		pw.println(mappingStr("Get", MODEL_PATH));
		pw.println(String.format("\tList<%s> getAll(){", TABLE_NAME));
		pw.println("\t\treturn repository.findAll();");
		pw.println("\t}\n");
		
		pw.println(mappingStr("Post", MODEL_PATH));
		pw.println(String.format("\t%s post%s(@RequestBody %s new%s){", 
				TABLE_NAME, TABLE_NAME, TABLE_NAME, TABLE_NAME
		));
		pw.println(String.format("\t\treturn repository.save(new%s);", TABLE_NAME));
		pw.println("\t}");
		
		return sw.toString();
	}
	
	private String mappingStr(String method, String url) {
		return String.format("\t@%sMapping(\"/%s\")", method, url);
	}
}
