package mysqlinterpreter.parser;

import mysqlinterpreter.classification.Classif;
import mysqlinterpreter.parser.operations.CreateTableOperation;
import mysqlinterpreter.scanner.MySQLScanner;
import mysqlinterpreter.scanner.Token;

public class MySQLParser {
    private MySQLScanner scanner;
    private final String outputPath;
    
    public MySQLParser(MySQLScanner scanner, String outputPath){
        this.scanner = scanner;
        this.outputPath = outputPath;
    }

    public void run() throws Exception{
        while(scanner.currentToken.primClassif != Classif.EOF){
            executeOperation();
        }
    }

    private void executeOperation() throws Exception{
        this.scanner.getNext();
        Token currentToken = scanner.currentToken;

        if(currentToken.primClassif != Classif.OPERATION){
            error("Unexpected %s token for \"%s\" found, expected OPERATION token",
                    currentToken.primClassif.toString(), currentToken.tokenStr);
        }
        

        switch(currentToken.tokenStr) {
        case "CREATE":
        	new CreateTableOperation(this).execute();
        	
        	break;
        default:
        	error("%s OPERATION is not yet supported", currentToken.tokenStr);
        }
        
        
    }

    public void error(String format, Object... args) throws ParserException{
        String errorMessage = String.format(format, args);

        throw new ParserException(errorMessage, scanner.getLineNumber()+1, scanner.getInputFileName());
    }

    /** Getters */
    public MySQLScanner getScanner(){
        return scanner;
    }
    
    public String getOutputPath() {
    	return outputPath;
    }
}
