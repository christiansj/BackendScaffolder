package test.mysqlinterpreter.parser.operations;

import mysqlinterpreter.parser.MySQLParser;
import mysqlinterpreter.parser.operations.Operation;
import mysqlinterpreter.scanner.MySQLScanner;
import mysqlinterpreter.symboltable.SymbolTable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OperationTest {
    private final String INPUT_FOLDER_PATH = "src/test/mysqlinterpreter/parser/operations/";

    @Test
    @DisplayName("constructor should assign scanner and parser member variables")
    public void testConstructor() throws Exception{
        MySQLScanner scanner = new MySQLScanner(INPUT_FOLDER_PATH + "createtable/testinput/missing_table.txt", new SymbolTable());
        MySQLParser parser = new MySQLParser(scanner, "src/output");
        Operation operation = new Operation(parser);

        assertEquals(scanner, operation.getScanner());
        assertEquals(parser, operation.getParser());
    }
}
