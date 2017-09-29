package com.example.cadpart;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;

import at.smartpart.beans.CadPart;

public class PRT {

	private String fileName;
	private String localVersion;
	private String dbVersion;
	private String newVersion;
	private String child;
	private String parent;
	private String version;
	private Object myObject = new Object();

	public PRT(String fileName, String version) {
		this.fileName = fileName;
		this.localVersion = version;
	}


	public PRT(String child, String parent, String fileName, String localVersion) {
		
		this.child = child;
		this.parent = parent;
		this.fileName = fileName;
		this.localVersion = localVersion;

	}
	
	@SuppressWarnings("unchecked")
	public PRT(String fileName, String version, Object object) {
		
		this.fileName=fileName;
		this.version=version;
		
		try {
			byte[] st = (byte[])object;
			ByteArrayInputStream boi = new ByteArrayInputStream(st);
			ObjectInputStream ois = new ObjectInputStream(boi);
					
			myObject  =  ois.readObject();

		} catch (ClassNotFoundException e) {
			
			System.out.println("//////////clnf\\\\\\\\\\");

			e.printStackTrace();
			
		} catch (IOException e) {
			
			System.out.println("//////////ioex\\\\\\\\\\");

			e.printStackTrace();
		}



	}

	public String getFileName() {
		return fileName;
	}

	public String getLocalVersion() {
		return localVersion;

	}
	
	public void setCurVersion (String dbVersion) {
		this.dbVersion = dbVersion;
	}
	
	public void setNewVersion (String newVersion) {
		this.newVersion = newVersion;
	}

	public String getCurVersion() {
		return dbVersion;
	}
	
	public String getNewVersion() {
		return newVersion;
	}

	
	public String getPRT() {
		return child;
	}

	public String getASM() {
		return parent;

	}

	
	public JTree getTree() {
		return (JTree)this.myObject;
	}



	public List<CadPart> getDrwMembers() {
		return (List<CadPart>) this.myObject;
	}

	public String getVersion () {
		return version;
	}
	
	public void setVersion (String version) {
		this.version = version;
	} 

	public String getFilename() {
		return fileName;
	}
	
	
}
