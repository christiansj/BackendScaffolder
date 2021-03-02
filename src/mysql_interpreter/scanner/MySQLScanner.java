package mysql_interpreter.scanner;

import mysql_interpreter.classification.Classif;
import mysql_interpreter.classification.Subclassif;
import mysql_interpreter.stentry.STEntry;
import mysql_interpreter.symbol_table.SymbolTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class MySQLScanner {
    private final String DELIMITERS = " ();,\n\t";
    private SymbolTable symbolTable;

    private int iColPos;
    private int iLineNumber;
    private String inputFileName;
    private ArrayList<String> lineList = new ArrayList<>();
    private char[] textCharM;

    public Token currentToken = new Token();
    public Token nextToken;

    public MySQLScanner(String fileName, SymbolTable symbolTable) throws Exception{
        this.inputFileName = fileName;
        this.symbolTable = symbolTable;
        iColPos = 0;
        iLineNumber = 0;

        initLineList();
        if(lineList.size() > 0){
            this.textCharM = lineList.get(0).toCharArray();
        }

        nextToken = readToken();
    }

    private void initLineList() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(this.inputFileName));
        String line;

        while((line = reader.readLine()) != null){
            this.lineList.add(line);
        }
    }

    private Token readToken(){
        String line;

        while(iLineNumber < lineList.size()){
            line = lineList.get(iLineNumber);
            textCharM = line.toCharArray();

            if(iColPos < textCharM.length){
                return createToken(line);
            }

            iLineNumber += 1;
            iColPos = 0;
        }

        return new Token();
    }

    private Token createToken(String line) {
        Token token = new Token();
        eatWhiteSpace();

        token.iColPos = iColPos;
        token.iLineNum = iLineNumber;
        String firstChar = String.valueOf(textCharM[token.iColPos]);

        if("();".contains(firstChar)){
            token.tokenStr = firstChar;
            iColPos += 1;
        }else{
            token.tokenStr = line.substring(token.iColPos, findEndOfToken());
        }

        return token;
    }

    private void eatWhiteSpace(){
        while(iColPos < textCharM.length){
            String currentChar = String.valueOf(textCharM[iColPos]);
            if(!" \t\n".contains(currentChar)){
                return;
            }
            iColPos+=1;
        }
    }

    private int findEndOfToken(){
        while(iColPos < textCharM.length){
            if(DELIMITERS.contains((String.valueOf(textCharM[iColPos])))){
                return iColPos;
            }
            iColPos += 1;
        }
        return iColPos;
    }

    public String getNext(){
        // the last line has been scanned
        // return an EOF token
        if(iLineNumber == lineList.size()) {
            currentToken.tokenStr = "";
            currentToken.primClassif = Classif.EOF;
            currentToken.subclassif = Subclassif.EMPTY;

            return "";
        }

        currentToken = nextToken;

        setTokenClass();
        readNextToken();

        return currentToken.tokenStr;
    }

    private void readNextToken(){
        Token temp = currentToken;

        nextToken = readToken();
        currentToken = nextToken;
        if(!currentToken.tokenStr.equals("")){
            setTokenClass();
        }

        currentToken = temp;
    }

    private void setTokenClass(){
        String tokenStr = currentToken.tokenStr;
        STEntry stEntry = symbolTable.getSTEntry(tokenStr);

        if(stEntry != null){
            currentToken.primClassif = stEntry.primClassif;
            currentToken.subclassif = stEntry.subclassif;
        }else if("(),;".contains(tokenStr)){
            currentToken.primClassif = Classif.SEPARATOR;
        }else if(String.valueOf(tokenStr.charAt(0)).matches("\\d")){
            currentToken.subclassif = Subclassif.INTEGER;
        }else{
            currentToken.subclassif = Subclassif.IDENTIFIER;
        }
    }

    public void printCurrentLine(){
        System.out.printf(" %d. %s\n", iLineNumber+1, lineList.get(iLineNumber));
    }

    public int getLineNumber(){
        return iLineNumber;
    }

    public ArrayList<String> getLineList(){
        return lineList;
    }

    public String getInputFileName(){
        return inputFileName;
    }
}
