package test.springwriter.controller.springwriter.controller.controllermethodwriter;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import springwriter.controller.SpringControllerWriter;
import springwriter.controller.controllermethodwriter.DeleteMethodString;
import test.testutil.TestUtility;
import test.testutil.WriterTestData;

public class DeleteMethodStringTest extends AbstractMethodStringTest {
	
	private SpringControllerWriter controllerWriter;
	private DeleteMethodString deleteMethodString;
	
	@BeforeEach
	public void setUp() throws Exception {
		controllerWriter = TestUtility.newControllerWriter("/src", WriterTestData.bookTable());
		deleteMethodString = new DeleteMethodString(controllerWriter);
	}
	
	@Test
	@DisplayName("mappingAnnotationStr should return correct annotation for delete")
	public void testMappingAnnotationStr() {
		final String EXPECTED = "\t@DeleteMapping(\"/book/{id}\")";
		assertEquals(EXPECTED, deleteMethodString.mappingAnnotationStr());
	}
	
	@Test
	@DisplayName("prototypeStr should return correct prototype for delete")
	public void testPrototypeStr() {
		final String EXPECTED = "\tResponseEntity<Book> deleteBook(@PathVariable Integer id) {";
		assertEquals(EXPECTED, deleteMethodString.prototypeStr());
	}
	
	@Test
	@DisplayName("bodyStr should return correct body for delete")
	public void testBodyStr() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		pw.println("\t\tOptional<Book> book = repository.findById(id);");
		pw.println(ifNotFoundStr(false));
		pw.println("\t\trepository.delete(book.get());");
		pw.print("\t\treturn new ResponseEntity<Book>(HttpStatus.OK);");
		
		assertEquals(sw.toString(), deleteMethodString.bodyStr());
	}
}
