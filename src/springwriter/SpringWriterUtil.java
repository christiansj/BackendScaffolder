package springwriter;

public class SpringWriterUtil {
	public static String writeImports(String rootString, String [] packages) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for(String s : packages) {
			sb.append(String.format("import %s.%s\n", rootString, s));
		}
		
		return sb.toString();
	}
}
