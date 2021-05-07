package springwriter.controller.controllermethodwriter;

import java.io.PrintWriter;
import java.io.StringWriter;

import springwriter.controller.SpringControllerWriter;

public class GetOneMethodString extends AbstractControllerMethodString {

	public GetOneMethodString(SpringControllerWriter controllerWriter) throws Exception {
		super(controllerWriter);
	}

	public String mappingAnnotationStr() {
		final String MAPPING_URL = mySQLTable.hasCompositeKey() ? "-by-composite-key" : "/{id}";
		return mappingStr("Get", LOWERCASE_TABLE_NAME + MAPPING_URL);
	}

	public String prototypeStr() {
		return singleRecordPrototypeStr("getOne");
	}

	public String bodyStr() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		pw.println("\t\t"+recordEqualsFindByStr());
		pw.println(ifNotFoundStr(false));
		pw.print(String.format("\t\treturn ResponseEntity.ok(%s.get());", LOWERCASE_TABLE_NAME));
		
		return sw.toString();
	}
}
