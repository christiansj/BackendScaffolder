package springwriter.controller.controllermethodwriter;

import springwriter.controller.SpringControllerWriter;

public class PostMethodString extends AbstractControllerMethodString {

	public PostMethodString(SpringControllerWriter controllerWriter) throws Exception {
		super(controllerWriter);
	}

	public String mappingAnnotationStr() {
		return mappingStr("Post", LOWERCASE_TABLE_NAME);
	}

	public String prototypeStr() {
		return singleRecordPrototypeStr("post");
	}

	@Override
	public String bodyStr() {
		return String.format("\t\treturn ResponseEntity.ok(repository.save(new%s));", 
				TABLE_NAME);
	}

}
