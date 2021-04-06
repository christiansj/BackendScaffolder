package mysqlinterpreter.parser.operations;

import java.util.Arrays;

import mysqlentity.datatype.MySQLType;
import mysqlentity.mysqlcolumn.MySQLColumn;
import mysqlentity.mysqltable.MySQLTable;
import mysqlinterpreter.classification.Classif;
import mysqlinterpreter.classification.Subclassif;
import mysqlinterpreter.parser.MySQLParser;
import springwriter.SpringWriter;

public class CreateTableOperation extends Operation {
	final String[] typesWithLength = {"TINYINT", "INT", "VARCHAR", "BIGINT", "CHAR"};
	private MySQLTable mySQLTable;

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
        
        checkTokenStr(";", true);
    	scanner.getNext();
    	
    	SpringWriter springWriter = new SpringWriter("src/output", mySQLTable);
    	springWriter.writeFiles();
    }

    private void readTableName() throws Exception{
        mySQLTable = new MySQLTable(scanner.getNext());

        checkSubclassif(Subclassif.IDENTIFIER);
        
        checkTokenStr("(", true);
        scanner.getNext();
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
    		if(scanner.currentToken.tokenStr.equals(",")) {
    			scanner.getNext();
    		}
    	}
    }
    
    private void readColumn() throws Exception {
    	String columnName = scanner.currentToken.tokenStr;
    	checkSubclassif(Subclassif.IDENTIFIER);

    	scanner.getNext();
    	checkSubclassif(Subclassif.DECLARE);
    	MySQLType columnType = MySQLType.valueOf(scanner.currentToken.tokenStr);
    	
    	scanner.getNext();
    	int columnLength = getColumnLength(columnType.name());
    	
    	System.out.printf("columnName: %s columnType: %s length %d\n", 
    			columnName, columnType.name(), columnLength);
    	
    	if(scanner.currentToken.primClassif == Classif.KEY_TYPE) {
    		parser.error("KEY_TYPE columns are not supported yet");
    	}
    	
    	mySQLTable.addColumn(new MySQLColumn(columnName, columnType, columnLength));
    }
    
    private int getColumnLength(String columnType) throws Exception {
    	String tokenStr = scanner.currentToken.tokenStr;
    	
    	if(tokenStr.equals("PRIMARY") || tokenStr.equals(",") || tokenStr.equals("AUTO_INCREMENT")) {
    		return 0;
    	}else if(!tokenStr.equals("(")) {
    		if(!Arrays.asList(typesWithLength).contains(columnType)) {
    			return 0;
    		}
    		parser.error("unexpected token '%s', expected '('", tokenStr);
    	}else if(!Arrays.asList(typesWithLength).contains(columnType)) {
    		parser.error("type %s can't have length", columnType);
    	}
    	
    	tokenStr = scanner.getNext();
    	
    	checkSubclassif(Subclassif.INTEGER);
    	checkTokenStr(")", true);
    	
    	scanner.getNext();
    	return Integer.parseInt(tokenStr);
    }
    
    private void checkTokenStr(String expectedString, boolean isGetNext) throws Exception {
    	if(isGetNext) {
    		scanner.getNext();
    	}
    	if(!scanner.currentToken.tokenStr.equals(expectedString)) {
    		parser.error("Expected '%s' token, instead got '%s'", 
    				expectedString, scanner.currentToken.tokenStr);
    	}
    }
    
    public String getTableName(){
        return mySQLTable.getName();
    }
    
    public MySQLTable getMySQLTable() {
    	return mySQLTable;
    }
}
