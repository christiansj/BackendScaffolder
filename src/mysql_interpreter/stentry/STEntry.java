package mysql_interpreter.stentry;

import mysql_interpreter.classification.Classif;
import mysql_interpreter.classification.Subclassif;

public class STEntry {
    public String symbol;
    public Classif primClassif;
    public Subclassif subclassif;

    public STEntry(String symbol, Classif classif, Subclassif subclassif){
        this.symbol = symbol;
        this.primClassif = classif;
        this.subclassif = subclassif;
    }
}
