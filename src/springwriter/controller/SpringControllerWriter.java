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
	final String PRIMARY_TYPE;
	public SpringControllerWriter(SpringWriter springWriter) throws Exception {
		super(springWriter, "controller", "controllers");
		
		this.TABLE_NAME = mySQLTable.getName();
		this.MODEL_PATH = SpringWriterUtil.lowercaseFirstChar(TABLE_NAME);
		this.PRIMARY_TYPE = SpringWriterUtil.getPrimaryKeyType(mySQLTable);
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

		final String[] ANNOTATIONS = {"RestController", "GetMapping", "PostMapping", "RequestBody", "PathVariable"};
		
		pw.println("\n");
		pw.println("import java.util.List;");
		pw.println(String.format("import %s.%s;", 
				springWriter.createPackageStr("model", "models"), 
				TABLE_NAME));
		pw.println(String.format("import %s.%sRepository;",
				springWriter.createPackageStr("repository", "repositories"),
				TABLE_NAME));
		pw.println("import org.springframework.beans.factory.annotation.Autowired;\n");
		
		pw.println(SpringWriterUtil.writeImports("org.springframework.web.bind.annotation", ANNOTATIONS));
//		pw.println("\n");
		
		return sw.toString();
	}
	
	private String classBodyString() throws Exception {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		pw.println("\t@Autowired");
		pw.println(String.format("\tprivate %sRepository repository;\n", TABLE_NAME));
		
		writeGetAll(pw);
		writeGetOne(pw);
		writePost(pw);
		writePut(pw);
		
		return sw.toString();
	}
	
	private void writeGetAll(PrintWriter pw) {
		pw.println(mappingStr("Get", MODEL_PATH));
		pw.println(String.format("\tList<%s> getAll(){", TABLE_NAME));
		pw.println("\t\treturn repository.findAll();");
		pw.println("\t}\n");
	}
	
	private void writeGetOne(PrintWriter pw) throws Exception {
		pw.println(mappingStr("Get", MODEL_PATH + "/{id}"));
		pw.println(String.format("\t%s getOne(@PathVariable %s id){",
				TABLE_NAME, PRIMARY_TYPE
		));
		pw.println("\t\treturn repository.findById(id);");
		pw.println("\t}\n");
	}
	
	private void writePost(PrintWriter pw) {
		pw.println(mappingStr("Post", MODEL_PATH));
		pw.println(String.format("\t%s post%s(@RequestBody %s new%s){", 
				TABLE_NAME, TABLE_NAME, TABLE_NAME, TABLE_NAME
		));
		pw.println(String.format("\t\treturn repository.save(new%s);", TABLE_NAME));
		pw.println("\t}\n");
	}
	
	private void writePut(PrintWriter pw) {
		pw.println(mappingStr("Put", MODEL_PATH + "/{id}"));
		pw.println(String.format("\t%s put%s(@RequestBody %s new%s, @PathVariable %s id){", 
				TABLE_NAME, TABLE_NAME, TABLE_NAME, TABLE_NAME, PRIMARY_TYPE
		));
		
		final String PRIMARY_NAME = mySQLTable.getPrimaryKeyNames().get(0);
		pw.println(String.format("\t\tnew%s.set%s(id);", 
				TABLE_NAME, 
				SpringWriterUtil.uppercaseFirstChar(PRIMARY_NAME)
		));
		
		pw.println(String.format("\t\treturn repository.save(new%s);", TABLE_NAME));
		pw.println("\t}\n");
	}
	
	private String mappingStr(String method, String url) {
		return String.format("\t@%sMapping(\"/%s\")", method, url);
	}
}
