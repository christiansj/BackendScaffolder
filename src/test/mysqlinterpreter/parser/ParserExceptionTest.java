package test.mysqlinterpreter.parser;

import mysqlinterpreter.parser.ParserException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserExceptionTest {
    @Test
    @DisplayName("constructor test")
    public void testConstructor(){
        try{
            throw new ParserException("Test Message", 1, "test.txt");
        }catch(ParserException pe){
            StringBuilder expectedMessageBuilder = new StringBuilder();
            expectedMessageBuilder.append("ParserException\n");
            expectedMessageBuilder.append("File: test.txt\n");
            expectedMessageBuilder.append("ErrorMessage: Test Message");
            expectedMessageBuilder.append("\nLine(1)");

            assertEquals(expectedMessageBuilder.toString(), pe.toString());
        }
    }
}
