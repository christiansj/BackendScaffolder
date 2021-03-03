package mysqlinterpreter.parser.operations;

import mysqlinterpreter.parser.MySQLParser;
import mysqlinterpreter.scanner.MySQLScanner;

public class Operation {
    final MySQLParser parser;
    final MySQLScanner scanner;

    public Operation(MySQLParser parser){
        this.parser = parser;
        this.scanner = parser.getScanner();
    }

    public MySQLParser getParser(){
        return parser;
    }

    public MySQLScanner getScanner(){
        return scanner;
    }
}
