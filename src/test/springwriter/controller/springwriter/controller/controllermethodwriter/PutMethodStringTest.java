package test.springwriter.controller.springwriter.controller.controllermethodwriter;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import springwriter.controller.SpringControllerWriter;
import springwriter.controller.controllermethodwriter.PutMethodString;
import test.testutil.TestUtility;
import test.testutil.WriterTestData;

public class PutMethodStringTest extends AbstractMethodStringTest {
	
	private SpringControllerWriter controllerWriter;
	private PutMethodString putMethodString;
	
	@BeforeEach
	public void setUp() throws Exception {
		controllerWriter = TestUtility.newControllerWriter("src/", WriterTestData.bookTable());
		putMethodString = new PutMethodString(controllerWriter);
	}
	
	@Test
	@DisplayName("mappingAnnotationStr should return correct annotation for Put")
	public void testMappingAnnotationStr() {
		final String EXPECTED = "\t@PutMapping(\"/book/{id}\")";
		assertEquals(EXPECTED, putMethodString.mappingAnnotationStr());
	}
	
	@Test
	@DisplayName("prototypeStr should return correct prototype for Put")
	public void testPrototypeStr() {
		final String EXPECTED = "\tResponseEntity<Book> putBook(@RequestBody Book newBook, @PathVariable Integer id) {";
		assertEquals(EXPECTED, putMethodString.prototypeStr());
	}
	
	@Test
	@DisplayName("bodyStr should return correct body for Put")
	public void testBodyStr() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		pw.println(ifNotFoundStr(true));
		pw.println("\t\tnewBook.setId(id);");
		pw.print("\t\treturn ResponseEntity.ok(repository.save(newBook));");
		
		assertEquals(sw.toString(), putMethodString.bodyStr());
	}
}
