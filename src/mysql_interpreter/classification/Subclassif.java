package mysql_interpreter.classification;

public enum Subclassif {
    EMPTY,
    IDENTIFIER, // table/column name
    DECLARE, // INT BIGINT CHAR VARCHAR...
    BUILTIN // Builtin MySQL term
}
