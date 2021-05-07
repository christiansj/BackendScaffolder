package springwriter.controller.controllermethodwriter;

import java.io.PrintWriter;
import java.io.StringWriter;

import springwriter.controller.SpringControllerWriter;

public class DeleteMethodString extends AbstractControllerMethodString{

	public DeleteMethodString(SpringControllerWriter controllerWriter) throws Exception {
		super(controllerWriter);
	}

	public String mappingAnnotationStr() {
		return mappingStr("Delete", SINGLE_RECORD_URL);
	}

	// ResponseEntity<Record> deleteRecord(id-args...)
	public String prototypeStr() {
		return singleRecordPrototypeStr("delete");
	}

	public String bodyStr() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		pw.println("\t\t"+recordEqualsFindByStr());
		pw.println(ifNotFoundStr(false));
		
		pw.println(String.format("\t\trepository.delete(%s.get());", LOWERCASE_TABLE_NAME));
		pw.print(String.format("\t\treturn new %s(HttpStatus.OK);", responseEntityStr()));
		
		return sw.toString();
	}

}
