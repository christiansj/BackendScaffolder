package test.springwriter.controller.springwriter.controller.controllermethodwriter;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import springwriter.controller.SpringControllerWriter;
import springwriter.controller.controllermethodwriter.GetOneMethodString;
import test.testutil.TestUtility;
import test.testutil.WriterTestData;

public class GetOneMethodStringTest extends AbstractMethodStringTest{
	
	private SpringControllerWriter controllerWriter;
	private GetOneMethodString getOneMethodString;
	
	@BeforeEach
	public void setUp() throws Exception {
		controllerWriter = TestUtility.newControllerWriter("src/", WriterTestData.bookTable());
		getOneMethodString = new GetOneMethodString(controllerWriter);
	}
	
	@Test
	@DisplayName("mappingAnnotationStr should return correct annotation for getOne")
	public void testMappingAnnotationStr() {
		final String EXPECTED = "\t@GetMapping(\"/book/{id}\")";
		assertEquals(EXPECTED, getOneMethodString.mappingAnnotationStr());
	}
	
	@Test
	@DisplayName("prototypeStr should return correct prototype for getOne")
	public void testPrototypeStr() {
		final String EXPECTED = "\tResponseEntity<Book> getOneBook(@PathVariable Integer id) {";
		assertEquals(EXPECTED, getOneMethodString.prototypeStr());
	}
	
	@Test
	@DisplayName("bodyStr should return correct body for getOne")
	public void testBodyStr() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		pw.println("\t\tOptional<Book> book = repository.findById(id);");
		pw.println(ifNotFoundStr(false));
		pw.print("\t\treturn ResponseEntity.ok(book.get());");
		
		assertEquals(sw.toString(), getOneMethodString.bodyStr());
	}
}
