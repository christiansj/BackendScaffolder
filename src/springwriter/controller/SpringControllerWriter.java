package springwriter.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import springwriter.SpringWriter;
import springwriter.SpringWriterUtil;
import springwriter.controller.controllermethodwriter.*;
import springwriter.springfilewriter.SpringFileWriter;

public class SpringControllerWriter extends SpringFileWriter {
	
	private final String TABLE_NAME;
	
	public SpringControllerWriter(SpringWriter springWriter) throws Exception {
		super(springWriter, "controller", "controllers");
		this.TABLE_NAME = mySQLTable.getName();
	}
	
	public String createFileString() throws Exception{
		StringBuilder sb = new StringBuilder();
		
		// package and imports
		sb.append("package " + springWriter.getPackageStr() + ";");
		sb.append(importStrings());
		
		// class body
		sb.append("@RestController\n");
		sb.append(String.format("class %sController {\n\n", TABLE_NAME));
		sb.append(classBodyString());
		sb.append("}\n");
		
		return sb.toString();
	}
	
	private String importStrings() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		String[] annotations = {"RestController", "GetMapping", "PostMapping", "PutMapping", "DeleteMapping", "RequestBody", ""};
		annotations[annotations.length-1] = mySQLTable.hasCompositeKey() ? "RequestParam" : "PathVariable";
		
		final String[] HTTPS = {"ResponseEntity", "HttpStatus"};
		pw.println("\n");
		pw.println("import java.util.Optional;");
		pw.println("import java.util.List;\n");

		
		pw.println("import org.springframework.beans.factory.annotation.Autowired;\n");
		
		pw.println(SpringWriterUtil.writeImports("org.springframework.http", HTTPS));
		pw.println(SpringWriterUtil.writeImports("org.springframework.web.bind.annotation", annotations));
		
		return sw.toString();
	}
	
	private String classBodyString() throws Exception {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		pw.println("\t@Autowired");
		pw.println(String.format("\tprivate %sRepository repository;\n", TABLE_NAME));

		pw.print(new GetAllMethodString(this));
		pw.print(new GetOneMethodString(this));
		pw.print(new PostMethodString(this));
		pw.print(new PutMethodString(this));
		pw.print(new DeleteMethodString(this));
		
		return sw.toString();
	}
}
