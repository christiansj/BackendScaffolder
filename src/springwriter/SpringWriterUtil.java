package springwriter;

public class SpringWriterUtil {
	public static String writeImports(String rootString, String [] packages) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for(String s : packages) {
			sb.append(String.format("import %s.%s;\n", rootString, s));
		}
		
		return sb.toString();
	}
	
	public static String formatMySQLVariable(String mysqlVar) {
		final String[] TOKENS = mysqlVar.split("_");
		StringBuilder sb = new StringBuilder(TOKENS[0]);
		
		for(int i = 1; i < TOKENS.length; i++){
		    String t = TOKENS[i];
			sb.append(String.format("%c%s",
				Character.toUpperCase(t.charAt(0)), 
		        t.substring(1, t.length())
		    ));
		}
		
		return sb.toString();
	}
}
