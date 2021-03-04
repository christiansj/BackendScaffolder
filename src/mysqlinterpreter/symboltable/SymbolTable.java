package mysqlinterpreter.symboltable;

import mysqlinterpreter.classification.Classif;
import mysqlinterpreter.classification.Subclassif;
import mysqlinterpreter.stentry.STEntry;

import java.util.*;

import mysqlentity.datatype.MySQLType;

public class SymbolTable {

    private HashMap<String, STEntry> entryHashMap = new HashMap<>();

    public SymbolTable(){
        initTable();
    }

    private void initTable(){
        final String[] OPERATION_STRINGS = {"CREATE", "ALTER", "UPDATE", "DROP", "RENAME", "REFERENCES"};
        final String[] ARTICLE_STRINGS = {"TABLE", "COLUMN", "KEY"};
        final String[] KEY_TYPE_STRINGS = {"PRIMARY", "FOREIGN", "AUTO_INCREMENT"};

        for (MySQLType mySQLType :  MySQLType.values()) {
            entryHashMap.put(mySQLType.name(), new STEntry(mySQLType.name(), Classif.CONTROL, Subclassif.DECLARE));
        }

        putStringSymbols(OPERATION_STRINGS, Classif.OPERATION, Subclassif.BUILTIN);
        putStringSymbols(ARTICLE_STRINGS, Classif.ARTICLE, Subclassif.BUILTIN);
        putStringSymbols(KEY_TYPE_STRINGS, Classif.KEY_TYPE, Subclassif.BUILTIN);

        entryHashMap.put("TO", new STEntry("TO", Classif.OPERATOR, Subclassif.BUILTIN));
    }

    private void putStringSymbols(String[] symbols, Classif classif, Subclassif subclassif){
        for(String symbol : symbols){
            entryHashMap.put(symbol, new STEntry(symbol, classif, subclassif));
        }
    }

    public STEntry getSTEntry(String key){
        return entryHashMap.get(key);
    }

    public HashMap<String, STEntry> getEntryHashMap(){
        return entryHashMap;
    }
}
