package mysqlinterpreter;

import mysqlinterpreter.scanner.MySQLScanner;
import mysqlinterpreter.symboltable.SymbolTable;

public class MySQLInterpreter {
    private static void runScanner(String filePath) throws Exception{
        MySQLScanner scanner = new MySQLScanner(filePath, new SymbolTable());

        scanner.printCurrentLine();
        printTokenHeading();
        int currentLine = 1;

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

    public static void main(String[] args) {
        try{
            runScanner(args[0]);
        }catch(Exception e){
            System.err.println(e);
        }
    }
}
