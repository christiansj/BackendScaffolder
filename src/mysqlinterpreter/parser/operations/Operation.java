package mysqlinterpreter.parser.operations;

import mysqlinterpreter.parser.MySQLParser;
import mysqlinterpreter.scanner.MySQLScanner;

public class Operation {
    final MySQLParser parser;
    final MySQLScanner scanner;
    final String outputPath;
    
    public Operation(MySQLParser parser){
        this.parser = parser;
        this.scanner = parser.getScanner();
        this.outputPath = parser.getOutputPath();
    }

    public MySQLParser getParser(){
        return parser;
    }

    public MySQLScanner getScanner(){
        return scanner;
    }
    
    public String getOutputPath() {
    	return outputPath;
    }
}
