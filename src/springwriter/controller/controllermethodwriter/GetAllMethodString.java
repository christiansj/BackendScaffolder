package springwriter.controller.controllermethodwriter;

import springwriter.controller.SpringControllerWriter;

public class GetAllMethodString extends AbstractControllerMethodString {

	public GetAllMethodString(SpringControllerWriter controllerWriter) throws Exception {
		super(controllerWriter);
	}

	public String mappingAnnotationStr() {
		return mappingStr("Get", LOWERCASE_TABLE_NAME);
	}

	// ResponseEntity<List<Record>> getAllRecords()
	public String prototypeStr() {
		return String.format("\tResponseEntity<List<%s>> getAll%s() {", TABLE_NAME, TABLE_NAME+"s");
	}

	public String bodyStr() {
		return "\t\treturn ResponseEntity.ok(repository.findAll());";
	}
}
