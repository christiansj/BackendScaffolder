package test.mysqlinterpreter.scanner;

import mysqlinterpreter.classification.Classif;
import mysqlinterpreter.classification.Subclassif;
import mysqlinterpreter.scanner.MySQLScanner;
import mysqlinterpreter.scanner.Token;
import mysqlinterpreter.symboltable.SymbolTable;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MySqlScannerTest {
    private final String INPUT_FOLDER_PATH = "src/test/mysqlinterpreter/scanner/testinput/";

    private MySQLScanner newTestScanner(String fileName) throws Exception{
        return new MySQLScanner(INPUT_FOLDER_PATH + fileName, new SymbolTable());
    }

    @Test
    @DisplayName("Constructor should properly initialize member variables")
    public void testConstructor() throws Exception{
        MySQLScanner scanner = newTestScanner("test1.txt");
        final Token currentToken = scanner.currentToken;
        final Token nextToken = scanner.nextToken;

        assertEquals("CREATE TABLE MyTest( 1", scanner.getLineList().get(0));

        assertEquals("", currentToken.tokenStr);
        assertEquals(Classif.EMPTY, currentToken.primClassif);
        assertEquals(Subclassif.EMPTY, currentToken.subclassif);

        assertEquals("CREATE", nextToken.tokenStr);
        assertEquals(Classif.EMPTY, nextToken.primClassif);
        assertEquals(Subclassif.EMPTY, nextToken.subclassif);
    }

    @Test
    @DisplayName("Constructor with nonexistent file should throw FileNotFoundException")
    public void testConstructorWithNonexistentFile(){
        assertThrows(FileNotFoundException.class, ()->{
            newTestScanner("BADINPUT.txt");
        });
    }

    @Test
    @DisplayName("getNext method should return tokenStr of the next token")
    public void testGetNextReturnValue() throws Exception{
        MySQLScanner scanner = newTestScanner("test1.txt");
        assertEquals("CREATE", scanner.getNext());
    }

    @Test
    @DisplayName("getNext method should set member variables of currentToken")
    public void testGetNextCurrentToken() throws Exception{
        MySQLScanner scanner = newTestScanner("test1.txt");
        scanner.getNext();

        assertEquals("CREATE", scanner.currentToken.tokenStr);
        assertEquals(Classif.OPERATION, scanner.currentToken.primClassif);
        assertEquals(Subclassif.BUILTIN, scanner.currentToken.subclassif);
    }

    @Test
    @DisplayName("getNext method should set member variables of nextToken")
    public void testGetNext_NextToken() throws Exception{
        MySQLScanner scanner = newTestScanner("test1.txt");
        scanner.getNext();

        assertEquals("TABLE", scanner.nextToken.tokenStr);
        assertEquals(Classif.ARTICLE, scanner.nextToken.primClassif);
        assertEquals(Subclassif.BUILTIN, scanner.nextToken.subclassif);
    }

    @Test
    @DisplayName("scanning in EOF token at end of file")
    public void testEOFToken() throws Exception {
        MySQLScanner scanner = newTestScanner("test1.txt");
        String tokenStr = "A";

        while(!tokenStr.isEmpty()){
            tokenStr = scanner.getNext();
        }

        assertEquals("", tokenStr);
        assertEquals("", scanner.currentToken.tokenStr);
        assertEquals(Classif.EOF, scanner.currentToken.primClassif);
        assertEquals(Subclassif.EMPTY, scanner.currentToken.subclassif);
    }

    private void fileTokenClassTests(String fileName, Classif expectedClassif, Subclassif expectedSubclassif) throws Exception{
        MySQLScanner scanner = newTestScanner(fileName);

        while(!scanner.getNext().isEmpty()){
            assertEquals(expectedClassif, scanner.currentToken.primClassif);
            assertEquals(expectedSubclassif, scanner.currentToken.subclassif);
        }
    }
    @Test
    @DisplayName("scanning in SEPARATOR tokens")
    public void testSeparatorTokens() throws Exception{
        fileTokenClassTests("separators.txt", Classif.SEPARATOR, Subclassif.EMPTY);
    }

    @Test
    @DisplayName("scanning in INTEGER tokens")
    public void testNumericalTokens() throws Exception{
        fileTokenClassTests("numericals.txt", Classif.EMPTY, Subclassif.INTEGER);
    }

    @Test
    @DisplayName("scanning in IDENTIFIER tokens")
    public void testIdentifierTokens() throws Exception{
        fileTokenClassTests("indentifiers.txt", Classif.EMPTY, Subclassif.IDENTIFIER);
    }

    @Test
    @DisplayName("scanning in OPERATION tokens")
    public void testOperationTokens() throws Exception{
        fileTokenClassTests("operations.txt", Classif.OPERATION, Subclassif.BUILTIN);
    }

    @Test
    @DisplayName("scanning in KEY_TYPE tokens")
    public void testKeyTypeTokens() throws Exception{
        fileTokenClassTests("key_types.txt", Classif.KEY_TYPE, Subclassif.BUILTIN);
    }

    @Test
    @DisplayName("scanning in ARTICLE tokens")
    public void testArticleTokens() throws Exception{
        fileTokenClassTests("articles.txt", Classif.ARTICLE, Subclassif.BUILTIN);
    }

    @Test
    @DisplayName("scanning in DEFINE (MySQL type) tokens")
    public void testDefineTokens() throws Exception{
        fileTokenClassTests("mysql_types.txt", Classif.CONTROL, Subclassif.DECLARE);
    }
}
