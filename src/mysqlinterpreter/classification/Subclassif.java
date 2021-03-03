package mysqlinterpreter.classification;

public enum Subclassif {
    EMPTY,
    IDENTIFIER, // table/column name
    DECLARE, // INT BIGINT CHAR VARCHAR...
    INTEGER, // 1 2 3...
    BUILTIN // Builtin MySQL term
}
