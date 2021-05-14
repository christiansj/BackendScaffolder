package mysqlinterpreter;

import mysqlinterpreter.parser.MySQLParser;
import mysqlinterpreter.scanner.MySQLScanner;
import mysqlinterpreter.symboltable.SymbolTable;

public class MySQLInterpreter {
	
    private static void runScanner(String filePath) throws Exception{
        MySQLScanner scanner = new MySQLScanner(filePath, new SymbolTable());

        scanner.printCurrentLine();
        printTokenHeading();
        int currentLine = 0;

        while(!scanner.getNext().isEmpty()){
            scanner.currentToken.printToken();

            if(currentLine < scanner.getLineNumber() && scanner.getLineNumber() != scanner.getLineList().size()){
                scanner.printCurrentLine();
                printTokenHeading();
                currentLine += 1;
            }
        }
    }

    private static void printTokenHeading(){
        System.out.printf("%-11s %-12s %s\n"
                , "primClassif"
                , "subClassif"
                , "tokenStr");
    }
    
    private static void runParser(String inputFilePath, String outputPath) throws Exception{
    	MySQLScanner scanner = new MySQLScanner(inputFilePath, new SymbolTable());
    	MySQLParser parser = new MySQLParser(scanner, outputPath);
    	
    	parser.run();
    }

    public static void main(String[] args) {
        try{
//            runScanner(args[0]);
            runParser(args[0], args[1]);
        }catch(Exception e){
        	e.printStackTrace();
//            System.err.println(e);
        }
    }
}
