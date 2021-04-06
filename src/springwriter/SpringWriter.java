package springwriter;

import java.util.Arrays;

import mysqlentity.mysqltable.MySQLTable;
import springwriter.model.SpringModelWriter;

public class SpringWriter {
	MySQLTable mySQLTable;
	String packageStr;
	
	public SpringWriter(String packagePath, MySQLTable mySQLTable) throws Exception {
		this.packageStr = packagePath.replaceAll("\\/", ".");
		this.mySQLTable = mySQLTable;
	
		if(Arrays.asList(packageStr.split("\\.")).indexOf("src") < 0) {
			throw new Exception("'src' folder wasn't included in folder path");
		}
	}
	
	public void writeFiles() throws Exception{
		SpringModelWriter modelWriter = new SpringModelWriter(this);
		modelWriter.writeModelFile();
	}
	
	public MySQLTable getMySqlTable() {
		return this.mySQLTable;
	}
	
	public String getPackageStr() {
		return this.packageStr;
	}
}
