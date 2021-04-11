package springwriter.controller;

import springwriter.SpringWriter;
import springwriter.springfilewriter.SpringFileWriter;
import springwriter.springfilewriter.SpringFileWriterInterface;

public class SpringControllerWriter extends SpringFileWriter implements SpringFileWriterInterface {

	public SpringControllerWriter(SpringWriter springWriter) {
		super(springWriter);
	}
	
	public void writeFile() throws Exception {
		
	}
	
	public String createFileString() throws Exception{
		return "";
	}
}
