package mysqlinterpreter.stentry;

import mysqlinterpreter.classification.Classif;
import mysqlinterpreter.classification.Subclassif;

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
