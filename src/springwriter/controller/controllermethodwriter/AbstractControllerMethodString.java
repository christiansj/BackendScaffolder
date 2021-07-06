package springwriter.controller.controllermethodwriter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import mysqlentity.mysqltable.MySQLTable;
import springwriter.SpringWriterUtil;
import springwriter.controller.SpringControllerWriter;

public abstract class AbstractControllerMethodString implements ControllerMethodStringInterface {
	
	SpringControllerWriter controllerWriter;
	MySQLTable mySQLTable;
	
	final String TABLE_NAME;
	final String LOWERCASE_TABLE_NAME;
	final String URL_PARAMS;
	final String SINGLE_RECORD_URL;
	
	final ArrayList<String> PRIMARY_KEY_NAMES;
	final String LAST_PRIMARY_NAME;
	
	public AbstractControllerMethodString(SpringControllerWriter controllerWriter) throws Exception {
		this.controllerWriter = controllerWriter;
		this.mySQLTable = controllerWriter.springWriter.getMySqlTable();
		
		this.TABLE_NAME = mySQLTable.getName();
		this.LOWERCASE_TABLE_NAME = SpringWriterUtil.lowercaseFirstChar(TABLE_NAME);
		this.PRIMARY_KEY_NAMES = mySQLTable.getPrimaryKeyNames();
		this.LAST_PRIMARY_NAME = PRIMARY_KEY_NAMES.get(PRIMARY_KEY_NAMES.size()-1);
		
		this.URL_PARAMS = urlParameters();
		String urlTableName = mySQLTable.getUrlName();
		this.SINGLE_RECORD_URL = mySQLTable.hasCompositeKey() ? urlTableName : urlTableName + "/{id}";
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
					SpringWriterUtil.hyphenateMySQLVariable(primaryKeyName),
					controllerWriter.mySQLToJavaMap.get(paramType), // variable type
					SpringWriterUtil.camelCaseMySQLVariable(primaryKeyName)
			);
			sb.append(paramStr);
			
			if(!primaryKeyName.equals(LAST_PRIMARY_NAME)) {
				sb.append(", ");
			}
		}
		
		return sb.toString();
	}
	
	String mappingStr(String method, String url) {
		return String.format("\t@%sMapping(\"/%s\")", method, url);
	}
	
	String singleRecordPrototypeStr(String methodName) {
		// @RequestBody Record newRecord
		final String REQUEST_BODY_PARAM = String.format("@RequestBody %s new%s", TABLE_NAME, TABLE_NAME);
		
		String params = methodName.equals("put")  ?  REQUEST_BODY_PARAM + ", " + URL_PARAMS
						: methodName.equals("post") ? REQUEST_BODY_PARAM
						: URL_PARAMS;
		
		return String.format("\t%s %s%s(%s) {", responseEntityStr(), methodName, TABLE_NAME, params);
	}
	
	String responseEntityStr() {
		return String.format("ResponseEntity<%s>", TABLE_NAME);
	}
	
	String recordEqualsFindByStr() {
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
	
	String ifNotFoundStr(boolean isFindInRepo) {
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
	
	public String toString() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		pw.println(mappingAnnotationStr());
		pw.println(prototypeStr());
		pw.println(bodyStr());
		pw.println("\t}\n");
		
		return sw.toString(); 
	}
}
