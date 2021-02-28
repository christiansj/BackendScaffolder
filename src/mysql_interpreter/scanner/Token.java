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
        String primClassifStr;
        String subClassifStr;

        if(primClassif != Classif.EMPTY){
            primClassifStr = primClassif.toString();
        }else{
            primClassifStr = "**EMPTY**";
        }

        if(subclassif != Subclassif.EMPTY){
            subClassifStr = subclassif.toString();
        }else{
            subClassifStr = "**EMPTY**";
        }

        System.out.printf("%-11 %-12s %s\n"
                , primClassifStr
                , subClassifStr
                , tokenStr);
    }
}
