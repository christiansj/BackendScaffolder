package test.mysqlinterpreter.parser.operations.createtable;

import mysqlinterpreter.parser.MySQLParser;
import mysqlinterpreter.parser.ParserException;
import mysqlinterpreter.parser.operations.CreateTableOperation;
import mysqlinterpreter.scanner.MySQLScanner;
import mysqlinterpreter.symboltable.SymbolTable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateTableOperationTest {
    private final String INPUT_FOLDER_PATH = "src/test/mysqlinterpreter/parser/operations/createtable/testinput/";

    private CreateTableOperation newCreateTableOperation(String fileName) throws Exception{
        MySQLScanner scanner = new MySQLScanner(INPUT_FOLDER_PATH + fileName, new SymbolTable());
        scanner.getNext();

        return new CreateTableOperation(new MySQLParser(scanner));
    }

    private void exceptionMessageTest(String fileName, String expectedMessage){
        ParserException exception = assertThrows(ParserException.class, ()->{
            newCreateTableOperation(fileName).execute();
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("missing TABLE should throw an Exception")
    public void testMissingTABLEToken(){
        String expectedMessage = "Unexpected token \"MyTable\" found for CREATE TABLE operation, expected \"TABLE\"";
        exceptionMessageTest("missing_table.txt", expectedMessage);
    }

    @Test
    @DisplayName("missing tableName after CREATE TABLE should throw an Exception")
    public void testMissingTableNameToken(){
        String expectedMessage = "Expected IDENTIFIER token, instead got EMPTY for token '('";
        exceptionMessageTest("missing_table_name.txt", expectedMessage);
    }

    @Test
    @DisplayName("missing '(' after CREATE TABLE <tableName> should throw an Exception")
    public void testMissingOpenParenthesisToken(){
        String expectedMessage = "Expected '(' token, instead got 'id'";
        exceptionMessageTest("missing_open_parenthesis.txt", expectedMessage);
    }
    
    @Test
    @DisplayName("missing column name (IDENTIFIER) should throw an Excpetion")
    public void testMissingColumnName() {
    	String expectedMessage = "Expected IDENTIFIER token, instead got DECLARE for token 'INT'";
    	exceptionMessageTest("missing_column_name.txt", expectedMessage);
    }
    
    @Test
    @DisplayName("missing column type (DECLARE) should throw an Exception")
    public void testMissingColumnType() {
    	String expectedMessage = "Expected DECLARE token, instead got BUILTIN for token 'PRIMARY'";
    	exceptionMessageTest("missing_column_type.txt", expectedMessage);
    }
    
    @Test
    @DisplayName("innappropiate column type with length should throw an Exception")
    public void testBadColumnTypeLength() {
    	String expectedMessage = "type DATE can't have length";
    	exceptionMessageTest("bad_column_type_length.txt", expectedMessage);
    }
    
    @Test
    @DisplayName("missing '(' for column type length should throw an Exception")
    public void testMissingOpenParenForLength() {
    	String expectedMessage = "Expected DECLARE token, instead got IDENTIFIER for token 'VARCHAR255'";
    	exceptionMessageTest("missing_open_paren_column_length.txt", expectedMessage);
    }
    
    @Test
    @DisplayName("missing length should throw an Exception")
    public void testMissingColumnLength() {
    	String expectedMessage = "Expected INTEGER token, instead got EMPTY for token ')'";
    	exceptionMessageTest("missing_column_length.txt", expectedMessage);
    }
    
    @Test
    @DisplayName("missing close parenthesis for column length")
    public void testMissingCloseParenForLength() {
    	String expectedMessage = "Expected ')' token, instead got ','";
    	exceptionMessageTest("missing_close_paren_column_length.txt", expectedMessage);
    }
}
