package test.mysql_interpreter.stentry;

import mysql_interpreter.classification.Classif;
import mysql_interpreter.classification.Subclassif;
import mysql_interpreter.stentry.STEntry;
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
