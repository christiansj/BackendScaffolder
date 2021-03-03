package test.mysqlinterpreter.stentry;

import mysqlinterpreter.classification.Classif;
import mysqlinterpreter.classification.Subclassif;
import mysqlinterpreter.stentry.STEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class STEntryTest {
    @Test
    @DisplayName("constructor should properly set class variables")
    public void testConstructor(){
        STEntry stEntry = new STEntry("mySymbol", Classif.ARTICLE, Subclassif.BUILTIN);

        assertEquals("mySymbol", stEntry.symbol);
        assertEquals(Classif.ARTICLE, stEntry.primClassif);
        assertEquals(Subclassif.BUILTIN, stEntry.subclassif);
    }
}
