package test.mysqlinterpreter.parser.operations.createtable;

import mysqlinterpreter.parser.MySQLParser;
import mysqlinterpreter.parser.ParserException;
import mysqlinterpreter.parser.operations.CreateTableOperation;
import mysqlinterpreter.scanner.MySQLScanner;
import mysqlinterpreter.symboltable.SymbolTable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import mysqlentity.mysqlcolumn.MySQLColumn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateTableOperationTest {
    private final String INPUT_FOLDER_PATH = "src/test/mysqlinterpreter/parser/operations/createtable/testinput/";

    private CreateTableOperation newCreateTableOperation(String fileName) throws Exception{
        MySQLScanner scanner = new MySQLScanner(INPUT_FOLDER_PATH + fileName, new SymbolTable());
        scanner.getNext();

        return new CreateTableOperation(new MySQLParser(scanner));
    }

    private void exceptionMessageTest(String fileName, String expectedMessage, boolean isParserException){
    	Exception exception = 
    	isParserException ? assertThrows(ParserException.class, ()->{
            newCreateTableOperation(fileName).execute();
        })
    	: assertThrows(Exception.class, ()->{
            newCreateTableOperation(fileName).execute();
        });
    
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("missing TABLE should throw an Exception")
    public void testMissingTABLEToken(){
        String expectedMessage = "Unexpected token \"MyTable\" found for CREATE TABLE operation, expected \"TABLE\"";
        exceptionMessageTest("missing_table.txt", expectedMessage, true);
    }

    @Test
    @DisplayName("missing tableName after CREATE TABLE should throw an Exception")
    public void testMissingTableNameToken(){
        String expectedMessage = "Expected IDENTIFIER token, instead got EMPTY for token '('";
        exceptionMessageTest("missing_table_name.txt", expectedMessage, true);
    }

    @Test
    @DisplayName("missing '(' after CREATE TABLE <tableName> should throw an Exception")
    public void testMissingOpenParenthesisToken(){
        String expectedMessage = "Expected '(' token, instead got 'id'";
        exceptionMessageTest("missing_open_parenthesis.txt", expectedMessage, true);
    }
    
    @Test
    @DisplayName("missing column name (IDENTIFIER) should throw an Excpetion")
    public void testMissingColumnName() {
    	String expectedMessage = "Expected IDENTIFIER token, instead got DECLARE for token 'INT'";
    	exceptionMessageTest("missing_column_name.txt", expectedMessage, true);
    }
    
    @Test
    @DisplayName("missing column type (DECLARE) should throw an Exception")
    public void testMissingColumnType() {
    	String expectedMessage = "Expected DECLARE token, instead got BUILTIN for token 'PRIMARY'";
    	exceptionMessageTest("missing_column_type.txt", expectedMessage, true);
    }
    
    @Test
    @DisplayName("innappropiate column type with length should throw an Exception")
    public void testBadColumnTypeLength() {
    	String expectedMessage = "type DATE can't have length";
    	exceptionMessageTest("columnlength/bad_column_type_length.txt", expectedMessage, true);
    }
    
    @Test
    @DisplayName("missing '(' for column type length should throw an Exception")
    public void testMissingOpenParenForLength() {
    	String expectedMessage = "unexpected token '255', expected '('";
    	exceptionMessageTest("columnlength/missing_open_paren_column_length.txt", expectedMessage, true);
    }
    
    @Test
    @DisplayName("missing length should throw an Exception")
    public void testMissingColumnLength() {
    	String expectedMessage = "Expected INTEGER token, instead got EMPTY for token ')'";
    	exceptionMessageTest("columnlength/missing_column_length.txt", expectedMessage, true);
    }
    
    @Test
    @DisplayName("missing close parenthesis for column length")
    public void testMissingCloseParenForLength() {
    	String expectedMessage = "Expected ')' token, instead got ','";
    	exceptionMessageTest("columnlength/missing_close_paren_column_length.txt", expectedMessage, true);
    }
    
    @Test
    @DisplayName("column with no length defined should have length of 0")
    public void testColumnLengthZero() throws Exception{
    	CreateTableOperation operation = newCreateTableOperation("columnlength/no_length.txt");
    	operation.execute();
    	MySQLColumn col = operation.getMySQLTable().getColumn("id_two");
    	
    	assertEquals(0, col.getLength());
    }
    
    @Test
    @DisplayName("column with lenghtless type should have length of 0")
    public void testLengthlessColumnType() throws Exception {
    	CreateTableOperation operation = newCreateTableOperation("columnlength/lengthless_type.txt");
    	operation.execute();
    	MySQLColumn col = operation.getMySQLTable().getColumn("start_dt");
    	
    	assertEquals(0, col.getLength());
    }
    
    @Test
    @DisplayName("unsupported AUTO_INCREMENT should throw an Exception")
    public void testMissingPrimaryBeforeKeyInColumn() {
    	String expectedMessage = "'AUTO_INCREMENT' key type is not supported yet";
    	exceptionMessageTest("autoincrement/autoincrement.txt", expectedMessage, true);
    }
    
    @Test
    @DisplayName("FOREIGN KEY in column declaration should throw an Exception")
    public void testForeignKeyInColumnDeclaration() {
    	String expectedMessage = "FOREIGN keys cannot be created in column declaration";
    	exceptionMessageTest("foreignkey/in_column_declaration.txt", expectedMessage, true);
    }
    
    @Test
    @DisplayName("PRIMARY KEY in column definition should throw Exception if PRIMARY KEY already exists")
    public void testAlreadyExistingPrimaryKey() {
    	String expectedMessage = "PRIMARY KEY(s) are already defined for table 'Person'";
    	exceptionMessageTest("primarykey/already_exists_3.txt", expectedMessage, false);
    }
    
    @Test
    @DisplayName("FOREIGN KEY should throw an Exception")
    public void testForeignKey() {
    	String expectedMessage = "FOREIGN keys are not supported yet";
    	exceptionMessageTest("foreignkey/foreign_key.txt", expectedMessage, true);
    }
    
    @Test
    @DisplayName("KEY without PRIMARY or FOREIGN preceding it should throw an Exception")
    public void testKeyWithoutPrimaryOrForeign() {
    	String expectedMessage = "'PRIMARY' or 'FOREIGN' must precede KEY";
    	exceptionMessageTest("key_without_primary_or_foreign.txt", expectedMessage, true);
    }
    
    @Test
    @DisplayName("PRIMARY KEY with missing 'KEY' should throw an Exception")
    public void testMissingKeyForPrimary() {
    	String expectedMessage = "Expected 'KEY' token, instead got '('";
    	exceptionMessageTest("primarykey/missing_key.txt", expectedMessage, true);
    }
    
    @Test
    @DisplayName("PRIMARY KEY with missing open parentheses should throw an Exception")
    public void testMissingOpenParnPrimaryKey() {
    	String expectedMessage = "Expected '(' token, instead got 'id'";
    	exceptionMessageTest("primarykey/missing_open_paren.txt", expectedMessage, true);
    }
    
    @Test
    @DisplayName("PRIMARY KEY with missing column name should throw an Exception")
    public void testMissingColumnNameForPrimary() {
    	String expectedMessage = "Expected column name for PRIMARY KEY";
    	exceptionMessageTest("primarykey/missing_column_name.txt", expectedMessage, true);
    }
    
    @Test
    @DisplayName("PRIMARY KEY with undefined column name should throw an Exception")
    public void testUndefinedColumnInPrimary() {
    	String expectedMessage = "column 'bad_id' isn't defined in table 'Person'";
    	exceptionMessageTest("primarykey/undefined_column.txt", expectedMessage, false);
    }
  
    @Test
    @DisplayName("PRIMARY KEY with invalid column Subclassif should throw an Exception")
    public void testInvalidColumnSubclassifPrimary() {
    	String expectedMessage = "Expected IDENTIFIER token, instead got DECLARE for token 'INT'";
    	exceptionMessageTest("primarykey/invalid_column_token_subclassif.txt", expectedMessage, true);
    }
    
    @Test
    @DisplayName("PRIMARY KEY that alreadys exists should throw an Exception")
    public void testExistingPrimaryKey() {
    	String expectedMessage = "table 'Person' already has PRIMARY KEY 'id'";
    	exceptionMessageTest("primarykey/already_exists.txt", expectedMessage, false);
    }
    
    @Test
    @DisplayName("PRIMARY KEY(s) already defined before PRIMARY KEY statement should throw an Exception")
    public void testExistingPrimaryKey2() {
    	String expectedMessage = "PRIMARY KEY(s) are already defined for table 'Person'";
    	exceptionMessageTest("primarykey/already_exists_2.txt", expectedMessage, false);
    }
    
    @Test
    @DisplayName("PRIMARY KEY should add PRIMARY KEYS to MySQLTable")
    public void testAddPrimaryKeys() throws Exception {
    	ArrayList<String> oneKeyList = new ArrayList<String>(Arrays.asList("id"));
    	ArrayList<String> twoKeyList = new ArrayList<String>(Arrays.asList("id_one", "id_two"));
    	ArrayList<String> threeKeyList = new ArrayList<String>(Arrays.asList("id_one", "id_two", "id_three"));
    	
    	evaulateAddedPrimaryKeyNames("1_primary_key.txt", oneKeyList);
    	evaulateAddedPrimaryKeyNames("2_primary_keys.txt", twoKeyList);
    	evaulateAddedPrimaryKeyNames("3_primary_keys.txt", threeKeyList);
    }
    
    private void evaulateAddedPrimaryKeyNames(String fileName, ArrayList<String> expectedPrimaryKeyNames) throws Exception {
    	CreateTableOperation createTableOperation = newCreateTableOperation("primarykey/"+fileName);
    	
    	try{
    		createTableOperation.execute();
    	}catch(Exception exception) {
    		if(!exception.getMessage().equals("composite keys are not supported yet")) {
    			throw exception;
    		}
    		assertEquals(expectedPrimaryKeyNames, createTableOperation.getMySQLTable().getPrimaryKeyNames());
    	}
    }
    
    @Test
    @DisplayName("AUTO_INCREMENT by itself should throw an Exception")
    public void testAutoIncrementByItself() {
    	String expectedMessage = "AUTO_INCREMENT is not connected to a column";
    	exceptionMessageTest("autoincrement_by_itself.txt", expectedMessage, true);
    }
}
