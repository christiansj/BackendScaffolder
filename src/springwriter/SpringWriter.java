package springwriter;

import mysqlentity.mysqltable.MySQLTable;
import springwriter.controller.SpringControllerWriter;
import springwriter.model.SpringIdentityWriter;
import springwriter.model.SpringModelWriter;
import springwriter.repository.SpringRepositoryWriter;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class SpringWriter {
	private MySQLTable mySQLTable;
	private String packageStr;
	private String rootPackageStr;
	public SpringWriter(String packagePath, MySQLTable mySQLTable) throws Exception {
		String tableName = mySQLTable.getName().toLowerCase();
		setPackageStr(packagePath, mySQLTable.getName());
		this.mySQLTable = mySQLTable;

	}

	private void setPackageStr(String packagePath, String tableName) throws Exception{
		packageStr = packagePath.replaceAll("\\/", ".") + "." + tableName.toLowerCase();
		if(Arrays.asList(packageStr.split("\\.")).indexOf("src") < 0) {
			throw new Exception("'src' folder wasn't included in folder path");
		}

		final List<String> TOKENS = Arrays.asList(packageStr.split("\\."));
		String path = "";

		for(int i = TOKENS.indexOf("src")+1; i < TOKENS.size(); i++) {
			path += TOKENS.get(i);
			if(i + 1 != TOKENS.size()) {
				path +=".";
			}
		}

		rootPackageStr = path;
	}
	public String getPackageStr() {
		return rootPackageStr;
	}

	public String getDirectoryPath() {
		System.out.println("root " + rootPackageStr);
		System.out.println("package " + packageStr);
		return packageStr.replaceAll("\\.", "\\/");
	}

	public void writeFiles() throws Exception{
		SpringModelWriter modelWriter = new SpringModelWriter(this);
		SpringRepositoryWriter repoWriter = new SpringRepositoryWriter(this);
		SpringControllerWriter controllerWriter = new SpringControllerWriter(this);

		File dir = new File(getDirectoryPath());
		if(!dir.exists()){
			dir.mkdir();
		}

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

}
