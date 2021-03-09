package mysqlinterpreter.parser.operations;

import java.util.Arrays;

import mysqlentity.datatype.MySQLType;
import mysqlinterpreter.classification.Classif;
import mysqlinterpreter.classification.Subclassif;
import mysqlinterpreter.parser.MySQLParser;

public class CreateTableOperation extends Operation {
	final String[] typesWithLength = {"TINYINT", "INT", "VARCHAR", "BIGINT", "CHAR"};
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
        readColumns();
        
    	if(!scanner.getNext().equals(";")) {
    		parser.error("expected ';' after CREATE TABLE operation, instead got '%s'", 
    			scanner.currentToken.tokenStr);
    	}
    	scanner.getNext();
    }

    private void readTableName() throws Exception{
        tableName = scanner.getNext();

        checkSubclassif(Subclassif.IDENTIFIER);

        if(!scanner.getNext().equals("(")){
            parser.error("Expected '(' token, instead got '%s'",
                    scanner.currentToken.tokenStr);
        }
    }
    
    private void checkSubclassif(Subclassif expectedSubclassif) throws Exception{
    	if(scanner.currentToken.subclassif != expectedSubclassif) {
    		parser.error("Expected %s token, instead got %s for token '%s'",
    				expectedSubclassif,
    				scanner.currentToken.subclassif,
    				scanner.currentToken.tokenStr);
    	}
    }
    
    private void readColumns() throws Exception {
    	while(!scanner.currentToken.tokenStr.equals(")")) {
    		readColumn();
    	}
    }
    
    private void readColumn() throws Exception {
    	String columnName = scanner.getNext();
    	checkSubclassif(Subclassif.IDENTIFIER);

    	scanner.getNext();
    	checkSubclassif(Subclassif.DECLARE);
    	MySQLType columnType = MySQLType.valueOf(scanner.currentToken.tokenStr);
    	
    	scanner.getNext();
    	int columnLength = getColumnLength(columnType.name());
    	
    	// https://www.sqlshack.com/mysql-create-table-statement-with-examples/
    	System.out.printf("columnName: %s columnType: %s length %d\n", 
    			columnName, columnType.name(), columnLength);
    	
    	if(scanner.currentToken.primClassif == Classif.KEY_TYPE) {
    		parser.error("KEY_TYPE columns are not supported yet");
    	}
    }
    
    private int getColumnLength(String columnType) throws Exception {
    	String tokenStr = scanner.currentToken.tokenStr;
    	
    	if(tokenStr.equals("PRIMARY") || tokenStr.equals(",") || tokenStr.equals("AUTO_INCREMENT")) {
    		return 0;
    	}else if(!tokenStr.equals("(")) {
    		parser.error("unexpected token '%s', expected '('", tokenStr);
    	}else if(!Arrays.asList(typesWithLength).contains(columnType)) {
    		parser.error("type %s can't have length", columnType);
    	}
    	
    	tokenStr = scanner.getNext();
    	checkSubclassif(Subclassif.INTEGER);
    	
    	if(!scanner.getNext().equals(")")) {
    		parser.error("Expected ')' instead got '%s'", 
    				scanner.currentToken.tokenStr);
    	}
    	
    	scanner.getNext();
    	
    	return Integer.parseInt(tokenStr);
    }
    
    public String getTableName(){
        return tableName;
    }
}
