package springwriter.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import springwriter.SpringWriter;
import springwriter.SpringWriterUtil;
import springwriter.springfilewriter.SpringFileWriter;

public class SpringControllerWriter extends SpringFileWriter {
	
	final String TABLE_NAME;
	final String LOWERCASE_TABLE_NAME;
	final String URL_PARAMS;
	final String SINGLE_RECORD_URL;
	
	final ArrayList<String> PRIMARY_KEY_NAMES;
	final String LAST_PRIMARY_NAME;
	
	public SpringControllerWriter(SpringWriter springWriter) throws Exception {
		super(springWriter, "controller", "controllers");
		
		this.TABLE_NAME = mySQLTable.getName();
		this.LOWERCASE_TABLE_NAME = SpringWriterUtil.lowercaseFirstChar(TABLE_NAME);
		this.PRIMARY_KEY_NAMES = mySQLTable.getPrimaryKeyNames();
		this.LAST_PRIMARY_NAME = PRIMARY_KEY_NAMES.get(PRIMARY_KEY_NAMES.size()-1);
		
		this.URL_PARAMS = urlParameters();
		this.SINGLE_RECORD_URL = mySQLTable.hasCompositeKey() ? LOWERCASE_TABLE_NAME : LOWERCASE_TABLE_NAME + "/{id}";
	}
	
	private String urlParameters() throws Exception {
		if(!mySQLTable.hasCompositeKey()) {
			// @PathVariable String id
			return String.format("@PathVariable %s id", SpringWriterUtil.getPrimaryKeyType(mySQLTable));
		}
		
		final int TAB_COUNT = 8;
		StringBuilder sb = new StringBuilder();
		
		for(String primaryKeyName : PRIMARY_KEY_NAMES) {

			if(!primaryKeyName.equals(PRIMARY_KEY_NAMES.get(0))) {
				sb.append("\n");
				for(int t = 0; t < TAB_COUNT; t++) {
					sb.append("\t");
				}
			}
			
			String paramType = mySQLTable.getColumn(primaryKeyName).getMySQLType().name();
			
			// @RequestParam(name = "first-name") final String firstName;
			String paramStr = String.format("@RequestParam(name = \"%s\") final %s %s", 
					primaryKeyName.replace("_", "-").toLowerCase(), // hyphenate & lowercase
					mySQLToJavaMap.get(paramType), // variable type
					SpringWriterUtil.camelCaseMySQLVariable(primaryKeyName)
			);
			sb.append(paramStr);
			
			if(!primaryKeyName.equals(LAST_PRIMARY_NAME)) {
				sb.append(", ");
			}
		}
		
		return sb.toString();
	}
	
	public String createFileString() throws Exception{
		StringBuilder sb = new StringBuilder();
		
		// package and imports
		sb.append("package " + springWriter.createPackageStr(SINGULAR, PLURAL) + ";");
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
		pw.println("import java.util.List;");
		
		// model import - import path.model.Record;
		pw.println(String.format("import %s.%s;", 
				springWriter.createPackageStr("model", "models"), 
				TABLE_NAME));
		
		// repository import - import path.model.RecordRepository;
		pw.println(String.format("import %s.%sRepository;",
				springWriter.createPackageStr("repository", "repositories"),
				TABLE_NAME));
		
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

		writeGetAll(pw);
		writeGetOne(pw);
		writePost(pw);
		writePut(pw);
		writeDelete(pw);
		
		return sw.toString();
	}
	
	private void writeGetAll(PrintWriter pw) {
		pw.println(mappingStr("Get", LOWERCASE_TABLE_NAME));
		
		// Prototype
		// ResponseEntity<List<Record>> getAllRecords()
		pw.println(String.format("\tResponseEntity<List<%s>> getAll%s() {", TABLE_NAME, TABLE_NAME+"s"));
		pw.println("\t\treturn ResponseEntity.ok(repository.findAll());");
		pw.println("\t}\n");
	}
	
	private String mappingStr(String method, String url) {
		return String.format("\t@%sMapping(\"/%s\")", method, url);
	}
	
	private void writeGetOne(PrintWriter pw) throws Exception {
		String mappingUrl = mySQLTable.hasCompositeKey() ? "-by-composite-key" : "/{id}";
		pw.println(mappingStr("Get", LOWERCASE_TABLE_NAME + mappingUrl));
		
		// Prototype
		// ResponseEntity<Record> getOneRecord(id-args...)
		pw.println(singleRecordPrototypeStr("getOne"));
		
		pw.println("\t\t"+recordEqualsFindByStr());
		pw.println(ifNotFoundStr(false));
		
		pw.println(String.format("\t\treturn ResponseEntity.ok(%s.get());", LOWERCASE_TABLE_NAME));
		pw.println("\t}\n");
	}
	
	private String singleRecordPrototypeStr(String methodName) {
		// @RequestBody Record newRecord
		final String REQUEST_BODY_PARAM = String.format("@RequestBody %s new%s", TABLE_NAME, TABLE_NAME);
		
		String params = methodName.equals("put")  ?  REQUEST_BODY_PARAM + ", " + URL_PARAMS
						: methodName.equals("post") ? REQUEST_BODY_PARAM
						: URL_PARAMS;
		
		return String.format("\t%s %s%s(%s) {", responseEntityStr(), methodName, TABLE_NAME, params);
	}
	
	private String responseEntityStr() {
		return String.format("ResponseEntity<%s>", TABLE_NAME);
	}
	
	private String recordEqualsFindByStr() {
		// Optional<Record> record = repository.findBy...(id-args...);
		return String.format("Optional<%s> %s = %s;", 
			TABLE_NAME, LOWERCASE_TABLE_NAME, findByStr());
	}
	
	private String findByStr() {
		if(!mySQLTable.hasCompositeKey()) {
			return "repository.findById(id)";
		}
		
		// repository.findByFirstAndLastName(firstName, lastName)
		StringBuilder sb = new StringBuilder("repository.findBy");
		StringBuilder parameterBuilder = new StringBuilder();
		
		for(String primaryKeyName : PRIMARY_KEY_NAMES) {
			String camelCasedName = SpringWriterUtil.camelCaseMySQLVariable(primaryKeyName);
			
			sb.append(SpringWriterUtil.uppercaseFirstChar(camelCasedName));
			parameterBuilder.append(camelCasedName);
			
			if(!primaryKeyName.equals(LAST_PRIMARY_NAME)) {
				sb.append("And");
				parameterBuilder.append(", ");
			}
		}
		
		sb.append("(" + parameterBuilder.toString() + ")");
		
		return sb.toString();
	}
	
	private String ifNotFoundStr(boolean isFindInRepo) {
		StringBuilder sb = new StringBuilder();
		
		// !repository.findById(id).isPresent()
		// OR if !record.isPresent()
		final String IS_NOT_PRESENT_STR = String.format("!%s.isPresent()", 
				isFindInRepo ? findByStr() : LOWERCASE_TABLE_NAME);
		
		sb.append(String.format("\t\tif(%s){\n", 
				IS_NOT_PRESENT_STR
		));
		sb.append(String.format("\t\t\treturn new %s(HttpStatus.NOT_FOUND);\n", responseEntityStr()));
		sb.append("\t\t}\n");
		
		return sb.toString();
	}
	
	private void writePost(PrintWriter pw) {
		pw.println(mappingStr("Post", LOWERCASE_TABLE_NAME));
		
		// Prototype
		// ResponseEntity<Record> postRecord(@RequestBody Record newRecord){
		pw.println(singleRecordPrototypeStr("post"));
		pw.println(String.format("\t\treturn ResponseEntity.ok(repository.save(new%s));", 
				TABLE_NAME));
		pw.println("\t}\n");
	}
	
	private void writePut(PrintWriter pw) {
		pw.println(mappingStr("Put", SINGLE_RECORD_URL));
		
		// Prototype
		// ResponseEntity<Record> putRecord(@RequestBody newRecord, id-args...)
		pw.println(singleRecordPrototypeStr("put"));
		
		pw.println(ifNotFoundStr(true));
		
		final String PRIMARY_NAME = mySQLTable.getPrimaryKeyNames().get(0);
		pw.println(String.format("\t\tnew%s.set%s(id);", 
				TABLE_NAME, 
				SpringWriterUtil.uppercaseFirstChar(PRIMARY_NAME)
		));
		
		pw.println(String.format("\t\treturn ResponseEntity.ok(repository.save(new%s));", TABLE_NAME));
		pw.println("\t}\n");
	}
	
	private void writeDelete(PrintWriter pw) {
		pw.println(mappingStr("Delete", SINGLE_RECORD_URL));
		
		// Prototype
		// ResponseEntity<Record> deleteRecord(id-args...)
		pw.println(singleRecordPrototypeStr("delete"));
		
		pw.println("\t\t"+recordEqualsFindByStr());
		pw.println(ifNotFoundStr(false));
		
		pw.println(String.format("\t\trepository.delete(%s.get());", LOWERCASE_TABLE_NAME));
		pw.println(String.format("\t\treturn new %s(HttpStatus.OK);", responseEntityStr()));
		pw.println("\t}\n");
	}
}
