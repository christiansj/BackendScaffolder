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
        String expectedMessage = "Expected IDENTIFIER token after CREATE TABLE, instead got EMPTY for token \"(\"";
        exceptionMessageTest("missing_table_name.txt", expectedMessage);
    }

    @Test
    @DisplayName("missing '(' after CREATE TABLE <tableName> should throw an Exception")
    public void testMissingOpenParenthesisToken(){
        String expectedMessage = "Expected '(' token, instead got 'id'";
        exceptionMessageTest("missing_open_parenthesis.txt", expectedMessage);
    }
}
