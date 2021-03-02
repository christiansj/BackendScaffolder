package mysql_interpreter.scanner;

import mysql_interpreter.classification.Classif;
import mysql_interpreter.classification.Subclassif;

public class Token {
    public String tokenStr = "";

    public Classif primClassif = Classif.EMPTY;

    public Subclassif subclassif = Subclassif.EMPTY;

    public int iColPos = 0;

    public int iLineNum = 0;

    public Token(String tokenStr){
        this.tokenStr = tokenStr;
    }

    public Token(){
        this("");
    }

    public void printToken(){
        String primClassifStr = primClassif != Classif.EMPTY ? primClassif.toString() : "**EMPTY**";
        String subClassifStr = subclassif != Subclassif.EMPTY ? subclassif.toString() : "**EMPTY**";

        System.out.printf("%-11s %-12s %s\n"
                , primClassifStr
                , subClassifStr
                , tokenStr);
    }
}
