package test.mysqlinterpreter.scanner;

import mysqlinterpreter.classification.Classif;
import mysqlinterpreter.classification.Subclassif;
import mysqlinterpreter.scanner.Token;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TokenTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	
	@BeforeEach
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	}
	
	@AfterEach
	public void restoreStreams() {
	    System.setOut(originalOut);
	}
	
    @Test
    @DisplayName("constructor without parameters should return empty Token")
    public void testConstructorWithoutParameters(){
        Token token = new Token();
        assertEquals("", token.tokenStr);
        assertEquals(Classif.EMPTY, token.primClassif);
        assertEquals(Subclassif.EMPTY, token.subclassif);
        assertEquals(0, token.iColPos);
        assertEquals(0, token.iLineNum);
    }

    @Test
    @DisplayName("constructor with String parameter should be assigned to Token.tokenStr")
    public void testConstructorWithTokenStrParameter(){
        Token token = new Token("Test String");
        assertEquals("Test String", token.tokenStr);
        assertEquals(Classif.EMPTY, token.primClassif);
        assertEquals(Subclassif.EMPTY, token.subclassif);
        assertEquals(0, token.iColPos);
        assertEquals(0, token.iLineNum);
    }
    
    @Test
    @DisplayName("printToken should print primClassif-less Token correctly")
    public void testPrintTokenWithoutPrimClassif() {
    	evaluatePrintToken(Classif.EMPTY, Subclassif.IDENTIFIER);
    }
    
    private void evaluatePrintToken(Classif classif, Subclassif subClassif) {
    	Token token = createTestPrintToken(classif, subClassif);
    	token.printToken();
    	assertEquals(expectedPrintOut(token), outContent.toString());
    }
    
    private Token createTestPrintToken(Classif classif, Subclassif subclassif) {
    	Token token = new Token("myToken");
    	token.primClassif = classif;
    	token.subclassif = subclassif;
    	return token;
    }
    
    private String expectedPrintOut(Token token) {
    	return String.format("%-11s %-12s %s\n", 
    			token.primClassif == Classif.EMPTY ? "**EMPTY**" : token.primClassif, 
    			token.subclassif == Subclassif.EMPTY ? "**EMPTY**" : token.subclassif, 
    			token.tokenStr);
    }
    
    @Test
    @DisplayName("printToken should print subclassif-less Token correctly")
    public void testPrintTokenWithoutSubclassif() {
    	evaluatePrintToken(Classif.ARTICLE, Subclassif.EMPTY);
    }
    
    @Test
    @DisplayName("printToken should print primClassif-less and subclassif-less Token correctly")
    public void testPrintTokenWithoutSubAndPrim() {
    	evaluatePrintToken(Classif.EMPTY, Subclassif.EMPTY);
    }
    
    @Test
    @DisplayName("printToken should print Token attributes correctly")
    public void testPrintToken() {
    	evaluatePrintToken(Classif.ARTICLE, Subclassif.BUILTIN);
    }
}