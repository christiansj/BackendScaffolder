package test.springwriter.controller.springwriter.controller.controllermethodwriter;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import springwriter.controller.SpringControllerWriter;
import springwriter.controller.controllermethodwriter.GetAllMethodString;
import test.testutil.TestUtility;
import test.testutil.WriterTestData;

public class GetAllMethodStringTest {
	
	private SpringControllerWriter controllerWriter;
	private GetAllMethodString getAllMethodString;
	
	@BeforeEach
	public void setUp() throws Exception {
		controllerWriter = TestUtility.newControllerWriter("src/", WriterTestData.bookTable());
		getAllMethodString = new GetAllMethodString(controllerWriter);
	}
	
	@Test
	@DisplayName("mappingAnnotationStr should return correct annotation for getAll")
	public void testMappingAnnotationStr() {
		final String EXPECTED = "\t@GetMapping(\"/book\")";
		assertEquals(EXPECTED, getAllMethodString.mappingAnnotationStr());
	}
	
	@Test
	@DisplayName("prototypeStr should return correct prototype for getAll")
	public void testPrototypeStr() {
		final String EXPECTED = "\tResponseEntity<List<Book>> getAllBooks() {";
		assertEquals(EXPECTED, getAllMethodString.prototypeStr());
	}
	
	@Test
	@DisplayName("bodyStr should return correct body for getAll")
	public void testBodyStr() {
		final String EXPECTED = "\t\treturn ResponseEntity.ok(repository.findAll());";
		assertEquals(EXPECTED, getAllMethodString.bodyStr());
	}
}
