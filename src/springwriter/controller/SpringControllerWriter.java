package springwriter.controller;

import springwriter.SpringWriter;
import springwriter.springfilewriter.SpringFileWriter;
import springwriter.springfilewriter.SpringFileWriterInterface;

public class SpringControllerWriter extends SpringFileWriter implements SpringFileWriterInterface {
	public SpringControllerWriter(SpringWriter springWriter) {
		super(springWriter, "controller", "controllers");
	}
	
	public String createFileString() throws Exception{
		StringBuilder sb = new StringBuilder();
		
		sb.append("package " + springWriter.createPackageStr(SINGULAR, PLURAL));
		
		return sb.toString();
	}
}
