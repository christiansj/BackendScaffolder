package test.springwriter;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import springwriter.SpringWriterUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpringWriterUtilTest {
	@Test
	@DisplayName("writeImports should return String with imports")
	public void writeImportsTest() {
		String expected = "\nimport com.alpha.bravo.one\nimport com.alpha.bravo.two\nimport com.alpha.bravo.three\n";
		String[] packages = {"one", "two", "three"};
		assertEquals(expected, SpringWriterUtil.writeImports("com.alpha.bravo", packages));
	}
}
