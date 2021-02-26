package mysql_interpreter.classification;

public enum Classif{
    EMPTY,
    SEPARATOR, // ( ) , ;
    OPERATOR, // TO
    OPERATION, // CREATE ALTER UPDATE DROP RENAME
    ARTICLE, // TABLE COLUMN KEY
    EOF
}
