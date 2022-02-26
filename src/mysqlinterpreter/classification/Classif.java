package mysqlinterpreter.classification;

public enum Classif{
    EMPTY,
    SEPARATOR, // ( ) , ;
    OPERATOR, // TO
    OPERATION, // CREATE ALTER UPDATE DROP RENAME REFERENCES
    ARTICLE, // TABLE COLUMN KEY
    CONTROL,
    KEY_TYPE, // PRIMARY FOREIGN UNIQUE
    EOF
}
