package test.springwriter.badwritertype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import springwriter.badwritertype.BadSpringTypeWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BadSpringTypeWriterTest {
	@Test
	@DisplayName("writeFile method should throw a 'bad type' Exception")
	public void testWriteFile() throws Exception {
		BadSpringTypeWriter badWriter = new BadSpringTypeWriter();
		Exception exception = assertThrows(Exception.class, ()->{
			badWriter.writeFile();
		});
		assertEquals("'BadSpringTypeWriter' is not defined in classNameToFileNameMap", exception.getMessage());
	}
	
	@Test
	@DisplayName("createFileString should return null")
	public void testCreateFileString() throws Exception {
		assertNull(new BadSpringTypeWriter().createFileString());
	}
}
