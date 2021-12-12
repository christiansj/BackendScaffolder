package test.springwriter.controller.springwriter.controller.controllermethodwriter;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import springwriter.controller.SpringControllerWriter;
import springwriter.controller.controllermethodwriter.PostMethodString;
import test.testutil.TestUtility;
import test.testutil.WriterTestData;

public class PostMethodStringTest {
	
	private SpringControllerWriter controllerWriter;
	private PostMethodString postMethodString;
	
	@BeforeEach
	public void setUp() throws Exception {
		controllerWriter = TestUtility.newControllerWriter("src/", WriterTestData.bookTable());
		postMethodString = new PostMethodString(controllerWriter);
	}
	
	@Test
	@DisplayName("mappingAnnotationStr should return correct annotation for post")
	public void testMappingAnnotationStr() {
		final String EXPECTED = "\t@PostMapping(\"/book\")";
		assertEquals(EXPECTED, postMethodString.mappingAnnotationStr());
	}
	
	@Test
	@DisplayName("prototypeStr should return correct prototype for post")
	public void testPrototypeStr() {
		final String EXPECTED = "\tResponseEntity<Book> postBook(@RequestBody Book newBook) {";
		assertEquals(EXPECTED, postMethodString.prototypeStr());
	}
	
	@Test
	@DisplayName("bodyStr should return correct body for post")
	public void testBodyStr() {
		String setIdString = "\t\tnewBook.setId(repository.maxId() + 1);\n";
		String returnString = "\t\treturn ResponseEntity.ok(repository.save(newBook));";
		assertEquals(setIdString + returnString, postMethodString.bodyStr());
	}
}
