package mysqlinterpreter.parser.operations;

import mysqlinterpreter.classification.Subclassif;
import mysqlinterpreter.parser.MySQLParser;

public class CreateTableOperation extends Operation {

    private String tableName;

    public CreateTableOperation(MySQLParser parser){
        super(parser);
    }

    public void execute() throws Exception{
        scanner.getNext();

        if(!scanner.currentToken.tokenStr.equals("TABLE")){
            parser.error("Unexpected token \"%s\" found for CREATE TABLE operation, expected \"TABLE\"",
                    scanner.currentToken.tokenStr);
        }

        readTableName();
    }

    private void readTableName() throws Exception{
        tableName = scanner.getNext();

        if(scanner.currentToken.subclassif != Subclassif.IDENTIFIER){
            parser.error("Expected IDENTIFIER token after CREATE TABLE, instead got %s for token \"%s\"",
                    scanner.currentToken.subclassif, tableName);
        }

        if(!scanner.getNext().equals("(")){
            parser.error("Expected '(' token, instead got '%s'",
                    scanner.currentToken.tokenStr);
        }
    }

    public String getTableName(){
        return tableName;
    }
}
