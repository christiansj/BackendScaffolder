package test.mysqlinterpreter.scanner;

import mysqlinterpreter.classification.Classif;
import mysqlinterpreter.classification.Subclassif;
import mysqlinterpreter.scanner.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenTest {
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
}