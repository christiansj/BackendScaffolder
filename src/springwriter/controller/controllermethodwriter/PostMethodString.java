package springwriter.controller.controllermethodwriter;

import springwriter.SpringWriterUtil;
import springwriter.controller.SpringControllerWriter;

public class PostMethodString extends AbstractControllerMethodString {

	public PostMethodString(SpringControllerWriter controllerWriter) throws Exception {
		super(controllerWriter);
	}

	public String mappingAnnotationStr() {
		return mappingStr("Post", URL_TABLE_NAME);
	}

	public String prototypeStr() {
		return singleRecordPrototypeStr("post");
	}

	@Override
	public String bodyStr() {
		try {
			String newEntityStr = "new" + TABLE_NAME;
			// return ResponseEntity.ok(repositoyr.save(newEntity);
			String returnString = String.format("\t\treturn ResponseEntity.ok(repository.save(%s));", 
					newEntityStr);
			
			if(!mySQLTable.hasMaxIdMethod()) {
				return returnString;
			}
			
			StringBuilder sb = new StringBuilder();
			String primaryKeyName = mySQLTable.getPrimaryKeyNames().get(0);
			
			// newEntity.set(repository.maxId() + 1);
			sb.append(String.format("\t\t%s.set%s(repository.maxId() + 1);\n",
					newEntityStr,SpringWriterUtil.uppercaseFirstChar(primaryKeyName)));
			sb.append(returnString);
			
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
