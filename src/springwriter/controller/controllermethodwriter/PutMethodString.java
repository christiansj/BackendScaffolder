package springwriter.controller.controllermethodwriter;

import java.io.PrintWriter;
import java.io.StringWriter;

import springwriter.SpringWriterUtil;
import springwriter.controller.SpringControllerWriter;

public class PutMethodString extends AbstractControllerMethodString {

	public PutMethodString(SpringControllerWriter controllerWriter) throws Exception {
		super(controllerWriter);
	}

	public String mappingAnnotationStr() {
		return mappingStr("Put", SINGLE_RECORD_URL);
	}

	public String prototypeStr() {
		return singleRecordPrototypeStr("put");
	}

	public String bodyStr() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		pw.println(ifNotFoundStr(true));
		
		final String PRIMARY_NAME = mySQLTable.getPrimaryKeyNames().get(0);
		pw.println(String.format("\t\tnew%s.set%s(id);", 
				TABLE_NAME, 
				SpringWriterUtil.uppercaseFirstChar(PRIMARY_NAME)
		));
		
		pw.print(String.format("\t\treturn ResponseEntity.ok(repository.save(new%s));", TABLE_NAME));
		
		return sw.toString();
	}
}
