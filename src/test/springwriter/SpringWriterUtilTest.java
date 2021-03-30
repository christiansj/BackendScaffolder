package test.springwriter;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import springwriter.SpringWriterUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpringWriterUtilTest {
	@Test
	@DisplayName("writeImports should return String with imports")
	public void writeImportsTest() {
		String expected = "com.alpha.bravo.one\ncom.alpha.bravo.two\ncom.alpha.bravo.three\n";
		String[] packages = {"one", "two", "three"};
		assertEquals(expected, SpringWriterUtil.writeImports("com.alpha.bravo", packages));
	}
}
