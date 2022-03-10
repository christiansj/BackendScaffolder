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
	final boolean DEBUG_TABLE = true;
	
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
    	if(DEBUG_TABLE) {
    		System.out.println(mySQLTable);
    	}
    	
    	SpringWriter springWriter = new SpringWriter(getOutputPath(), mySQLTable);
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
    		if(scanner.currentToken.primClassif == Classif.KEY_TYPE) {
    			System.out.println("GOT" + scanner.currentToken.tokenStr);
    			readKey();
    		}else {
    			readColumn();
    		}
    		
    		if(scanner.currentToken.tokenStr.equals(",")) {
    			scanner.getNext();
    		}
    	}
    }
    
    private void readKey() throws Exception {
    	String tokenStr = scanner.currentToken.tokenStr;
    	if(tokenStr.equals("FOREIGN")) {
    		parser.error("FOREIGN keys are not supported yet");
    	}else if(tokenStr.equals("AUTO_INCREMENT")) {
    		parser.error("AUTO_INCREMENT is not connected to a column");
    	}else if(tokenStr.equals("PRIMARY")) {
    		readPrimaryKey();
    	}else if(tokenStr.equals("UNIQUE")) {
    		readUniqueKey();
    	}
    	
    	
    	
    }

    private void readPrimaryKey() throws Exception {
    	String tokenStr = scanner.currentToken.tokenStr;
    	checkTokenStr("PRIMARY", false);
    	checkTokenStr("KEY", true);
    	checkTokenStr("(", true);
    	
    	if(mySQLTable.getPrimaryKeyNames().size() > 0) {
    		parser.error("PRIMARY KEY(s) are already defined for table '%s'", mySQLTable.getName());
    	}
    	
    	tokenStr = scanner.getNext();
    	
    	if(tokenStr.equals(")")) {
    		parser.error("Expected column name for PRIMARY KEY");
    	}
    	
    	while(!scanner.currentToken.tokenStr.equals(")")) {
    		addPrimaryKey();
    	}
    	scanner.getNext();
    }
    
    private void addPrimaryKey() throws Exception {
    	checkSubclassif(Subclassif.IDENTIFIER);
    	MySQLColumn col = mySQLTable.getColumn(scanner.currentToken.tokenStr);
    	mySQLTable.addPrimaryKey(col.getName());
    	
    	if(scanner.getNext().equals(",")) {
    		scanner.getNext();
    	}
    }
    
    private void readUniqueKey() throws Exception {
    	String tokenStr = scanner.currentToken.tokenStr;
    	checkTokenStr("UNIQUE", false);
    	checkTokenStr("(", true);
    	
    	tokenStr = scanner.getNext();
    	
    	if(tokenStr.equals(")")) {
    		parser.error("Expected column name for UNIQUE key");
    	}
    	
    	while(!scanner.currentToken.tokenStr.equals(")")) {
    		addUniqueKey();
    	}
    	scanner.getNext();
    }
    
    private void addUniqueKey() throws Exception {
    	checkSubclassif(Subclassif.IDENTIFIER);
    	MySQLColumn col = mySQLTable.getColumn(scanner.currentToken.tokenStr);
    	mySQLTable.addUniqueKey(col.getName());
    	
    	if(scanner.getNext().equals(",")) {
    		parser.error("composite UNIQUE constraints are not supported yet");
    	}
    }
    
    private void readColumn() throws Exception {
    	String columnName = scanner.currentToken.tokenStr;
    	if(columnName.equals("KEY")) {
    		parser.error("'PRIMARY' or 'FOREIGN' must precede KEY");
    	}
    	checkSubclassif(Subclassif.IDENTIFIER);

    	scanner.getNext();
    	checkSubclassif(Subclassif.DECLARE);
    	MySQLType columnType = MySQLType.valueOf(scanner.currentToken.tokenStr);
    	
    	scanner.getNext();
    	int columnLength = getColumnLength(columnType.name());
    	
    	MySQLColumn col = new MySQLColumn(columnName, columnType, columnLength);
    	if(scanner.currentToken.primClassif == Classif.KEY_TYPE) {
    		col = handleKeyType(col);
    	}
    	
    	mySQLTable.addColumn(col);
    	if(col.isPrimaryKey()) {
    		mySQLTable.addPrimaryKey(col.getName());
    	}
    }
    
    private int getColumnLength(String columnType) throws Exception {
    	String tokenStr = scanner.currentToken.tokenStr;
    	
    	if(scanner.currentToken.primClassif == Classif.KEY_TYPE || tokenStr.equals(",")) {
    		return 0;
    	}else if(!tokenStr.equals("(")) {
    		if(tokenStr.equals(")")) {
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
    
    private MySQLColumn handleKeyType(MySQLColumn col) throws Exception {
    	final String tokenStr = scanner.currentToken.tokenStr;
    	if(tokenStr.equals("FOREIGN")) {
			parser.error("FOREIGN keys cannot be created in column declaration");
		} else if(!tokenStr.equals("PRIMARY")) {
    		parser.error("'%s' key type is not supported yet", 
    			scanner.currentToken.tokenStr);
    	}
    	checkTokenStr("KEY", true);
    	
    	if(mySQLTable.getPrimaryKeyNames().size() > 0) {
    		parser.error("PRIMARY KEY(s) are already defined for table '%s'", mySQLTable.getName());
    	}
    	
    	col.setIsPrimaryKey(true);
    	
    	scanner.getNext();
    	
    	return col;
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
        
    public MySQLTable getMySQLTable() {
    	return mySQLTable;
    }
}
