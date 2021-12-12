package springwriter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import mysqlentity.mysqltable.MySQLTable;
import springwriter.controller.SpringControllerWriter;
import springwriter.model.SpringIdentityWriter;
import springwriter.model.SpringModelWriter;
import springwriter.repository.SpringRepositoryWriter;

public class SpringWriter {
	private MySQLTable mySQLTable;
	private String packageStr;
	private String folderPath;
	private final String packageRootStr;
		
	public SpringWriter(String packagePath, MySQLTable mySQLTable) throws Exception {
		this.folderPath = packagePath;
		this.packageStr = packagePath.replaceAll("\\/", ".");
		this.mySQLTable = mySQLTable;
		this.packageRootStr = initPackageRoot();	
	}
	
	private String initPackageRoot() throws Exception {
		if(Arrays.asList(packageStr.split("\\.")).indexOf("src") < 0) {
			throw new Exception("'src' folder wasn't included in folder path");
		}
		
		final List<String> TOKENS = Arrays.asList(packageStr.split("\\."));
		String packagePath = "";
		
		for(int i = TOKENS.indexOf("src")+1; i < TOKENS.size(); i++) {
			packagePath += TOKENS.get(i);
			if(i + 1 != TOKENS.size()) {
				packagePath+=".";
			}
		}
		return packagePath;
	}
	
	public String createPackageStr(String singular, String plural) {
		if(directoryExists(folderPath + "/" + singular)) {
			return String.format("%s.%s", packageRootStr,  singular);
		}
		return String.format("%s.%s", packageRootStr, plural);
	}
	
	public String setDirectory(String singular, String plural) {
		String directory = packageStr.replaceAll("\\.", "\\/");
		String singularPath = directory + "/" + singular;
		String pluralPath = directory + "/" + plural;
		
		if(directoryExists(singularPath)) {
			return singularPath;
		}
		
		if(!directoryExists(pluralPath)) {
			new File(pluralPath).mkdir();	
		}
		return pluralPath;
	}
	
	private boolean directoryExists(String dirPath) {
		return Files.exists(Paths.get(dirPath));
	}
	
	public void writeFiles() throws Exception{
		SpringModelWriter modelWriter = new SpringModelWriter(this);
		SpringRepositoryWriter repoWriter = new SpringRepositoryWriter(this);
		SpringControllerWriter controllerWriter = new SpringControllerWriter(this);
		
		modelWriter.writeFile();
		if(mySQLTable.hasCompositeKey()) {
			SpringIdentityWriter identityWriter = new SpringIdentityWriter(this);
			identityWriter.writeFile();
		}
		repoWriter.writeFile();
		controllerWriter.writeFile();
	}
		
	public MySQLTable getMySqlTable() {
		return this.mySQLTable;
	}
	
	public String getPackageStr() {
		return this.packageStr;
	}
}
