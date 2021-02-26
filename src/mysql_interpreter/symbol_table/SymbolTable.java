package mysql_interpreter.symbol_table;

import mysql_interpreter.classification.Classif;
import mysql_interpreter.classification.Subclassif;
import mysql_interpreter.stentry.STEntry;

import java.util.*;

public class SymbolTable {

    private HashMap<String, STEntry> ht = new HashMap<>();
    public final Map<String, String> MYSQL_TO_JAVA_MAP;

    public SymbolTable(){
        MYSQL_TO_JAVA_MAP = initSqlToJavaMap();
        initTable();
    }

    private Map<String, String> initSqlToJavaMap(){
        HashMap<String, String> temp = new HashMap<>();

        temp.put("INT", "int");
        temp.put("BIGINT", "long");
        temp.put("SMALLINT", "short");
        temp.put("TINYINT", "byte");
        temp.put("BIT", "boolean");
        temp.put("VARCHAR", "String");
        temp.put("FLOAT", "double");
        temp.put("DOUBLE", "double");
        temp.put("CHAR", "String");
        temp.put("DATE", "Date");
        temp.put("DATETIME", "Date");
        temp.put("TIMESTAMP", "Timestamp");

        return Collections.unmodifiableMap(temp);
    }

    private void initTable(){
        final Set<String> DECLARE_STRINGS = MYSQL_TO_JAVA_MAP.keySet();
        final String[] OPERATION_STRINGS = {"CREATE", "ALTER", "UPDATE", "DROP", "RENAME", "REFERENCES"};
        final String[] ARTICLE_STRINGS = {"TABLE", "COLUMN", "KEY"};
        final String[] KEY_TYPE_STRINGS = {"PRIMARY", "FOREIGN"};

        DECLARE_STRINGS.forEach(str->{
            ht.put(str, new STEntry(str, Classif.CONTROL, Subclassif.DECLARE));
        });

        putStringSymbols(OPERATION_STRINGS, new STEntry(null, Classif.OPERATION, Subclassif.DECLARE));
        putStringSymbols(ARTICLE_STRINGS, new STEntry(null, Classif.ARTICLE, Subclassif.BUILTIN));
        putStringSymbols(KEY_TYPE_STRINGS, new STEntry(null, Classif.KEY_TYPE, Subclassif.BUILTIN));

        ht.put("TO", new STEntry("TO", Classif.OPERATOR, Subclassif.BUILTIN));
    }

    private void putStringSymbols(String[] symbols, STEntry stEntry){
        for(String symbol : symbols){
            stEntry.symbol = symbol;
            ht.put(symbol, stEntry);
        }
    }

    public void putSymbol(String symbol, STEntry entry){
        ht.put(symbol, entry);
    }
}
