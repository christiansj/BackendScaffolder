package test.springwriter.controller.springwriter.controller.controllermethodwriter;

public abstract class AbstractMethodStringTest {
	
	String ifNotFoundStr(boolean isFindInRepo) {
		StringBuilder sb = new StringBuilder();
		
		// !repository.findById(id).isPresent()
		// OR if !record.isPresent()
		final String IS_NOT_PRESENT_STR = String.format("!%s.isPresent()", 
				isFindInRepo ? "repository.findById(id)" : "book");
		
		sb.append(String.format("\t\tif(%s){\n", 
				IS_NOT_PRESENT_STR
		));
		sb.append("\t\t\treturn new ResponseEntity<Book>(HttpStatus.NOT_FOUND);\n");
		sb.append("\t\t}\n");
		
		return sb.toString();
	}
}
