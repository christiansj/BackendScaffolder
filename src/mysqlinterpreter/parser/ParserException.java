package mysqlinterpreter.parser;

public class ParserException extends Exception {
    private int iLineNumber;
    private String message;
    private String sourceFileName;

    public ParserException(String message, int lineNumber, String sourceFileName){
        this.iLineNumber = lineNumber;
        this.message = message;
        this.sourceFileName = sourceFileName;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("ParserException\n");
        sb.append(String.format("File: %s\n", sourceFileName));
        sb.append("ErrorMessage: " + message);
        sb.append(String.format("\nLine(%d)", iLineNumber));

        return sb.toString();
    }

    public String getMessage() {
        return message;
    }
}
