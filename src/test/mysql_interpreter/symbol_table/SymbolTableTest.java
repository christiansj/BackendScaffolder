package test.mysql_interpreter.symbol_table;

import mysql_entity.data_type.MySQLType;
import mysql_interpreter.classification.Classif;
import mysql_interpreter.classification.Subclassif;
import mysql_interpreter.stentry.STEntry;
import mysql_interpreter.symbol_table.SymbolTable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class SymbolTableTest {
    private SymbolTable symbolTable;
    final String[]  EXPECTED_KEYS = {"CREATE", "ALTER", "UPDATE", "DROP", "RENAME", "REFERENCES", "TABLE", "COLUMN",
            "KEY", "PRIMARY", "FOREIGN", "TO", "AUTO_INCREMENT"};

    @BeforeEach
    void setUp(){
        symbolTable = new SymbolTable();
    }

    @Test
    @DisplayName("HashMap should have correct keys")
    public void testHashMapKeys(){
        for(String expectedKey : EXPECTED_KEYS){
            assertTrue(symbolTable.getEntryHashMap().containsKey(expectedKey));
        }

        for(MySQLType mySQLType : MySQLType.values()){
            assertTrue(symbolTable.getEntryHashMap().containsKey(mySQLType.toString()));
        }
    }

    @Test
    @DisplayName("HashMap should have correct number of keys")
    public void testHashMapKeyCount(){
        final int expectedCount = EXPECTED_KEYS.length + MySQLType.values().length;
        assertEquals(expectedCount, symbolTable.getEntryHashMap().size(), );
    }

    @Test
    @DisplayName("HashMap should contain correct operation STEntities")
    public void testHashMapOperationEntities(){
        final String[] OPERATION_STRINGS = {"CREATE", "ALTER", "UPDATE", "DROP", "RENAME", "REFERENCES"};

        for(String operationString : OPERATION_STRINGS){
            STEntry stEntry =  symbolTable.getEntryHashMap().get(operationString);
            assertEquals(operationString, stEntry.symbol);
            assertEquals(Classif.OPERATION, stEntry.primClassif);
            assertEquals(Subclassif.BUILTIN, stEntry.subclassif);
        }
    }

    @Test
    @DisplayName("HashMap should contain correct article STEntities")
    public void testHashMapArticleEntities(){
        final String[] ARTICLE_STRINGS = {"TABLE", "COLUMN", "KEY"};

        for(String articleString : ARTICLE_STRINGS){
            STEntry stEntry = symbolTable.getEntryHashMap().get(articleString);
            assertEquals(articleString, stEntry.symbol);
            assertEquals(Classif.ARTICLE, stEntry.primClassif);
            assertEquals(Subclassif.BUILTIN, stEntry.subclassif);
        }
    }

    @Test
    @DisplayName("HashMap should contain correct key_type STEntities")
    public void testHashMapKeyTypeEntities(){
        final String[] KEY_TYPE_STRINGS = {"PRIMARY", "FOREIGN", "AUTO_INCREMENT"};

        for(String keyTypeString : KEY_TYPE_STRINGS){
            STEntry stEntry = symbolTable.getEntryHashMap().get(keyTypeString);
            assertEquals(keyTypeString, stEntry.symbol);
            assertEquals(Classif.KEY_TYPE, stEntry.primClassif);
            assertEquals(Subclassif.BUILTIN, stEntry.subclassif);
        }
    }

    @Test
    @DisplayName("HashMap should contain correct operator STEntities")
    public void testHashMapOperatorEntities(){
        final String[] OPERATOR_STRINGS = {"TO"};

        for(String operatorString : OPERATOR_STRINGS){
            STEntry stEntry = symbolTable.getEntryHashMap().get(operatorString);
            assertEquals(operatorString, stEntry.symbol);
            assertEquals(Classif.OPERATOR, stEntry.primClassif);
            assertEquals(Subclassif.BUILTIN, stEntry.subclassif);
        }
    }
}
