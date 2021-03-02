package test.mysql_interpreter.parser;

import mysql_interpreter.parser.MySQLParser;
import mysql_interpreter.parser.ParserException;
import mysql_interpreter.scanner.MySQLScanner;
import mysql_interpreter.symbol_table.SymbolTable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MySQLParserTest {
    private final String INPUT_FOLDER_PATH = "src/test/mysql_interpreter/parser/testinput/";

    @Test
    @DisplayName("constructor should set scanner member variable")
    public void testConstructor() throws Exception{
        MySQLScanner scanner = new MySQLScanner(INPUT_FOLDER_PATH + "bad_operation.txt", new SymbolTable());
        MySQLParser parser = new MySQLParser(scanner);

        assertEquals(scanner, parser.getScanner());
    }

    private MySQLParser newTestParser(String fileName) throws Exception{
        return new MySQLParser(new MySQLScanner(INPUT_FOLDER_PATH + fileName, new SymbolTable()));
    }

    @Test
    @DisplayName("throw ParserException if operation isn't found")
    public void testNoOperationTokenFound() throws Exception{
        MySQLParser parser = newTestParser("bad_operation.txt");

        ParserException exception =  assertThrows(ParserException.class, parser::run);
        System.out.println(exception);
    }
}
