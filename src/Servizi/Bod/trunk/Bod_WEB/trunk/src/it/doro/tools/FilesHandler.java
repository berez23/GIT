package it.doro.tools;

import java.io.File;

public class FilesHandler {

	public FilesHandler() {
		
	}//-------------------------------------------------------------------------
	
	public static String fromPathToName(String filePath){
		String s = "";
		if (filePath != null && !filePath.trim().equalsIgnoreCase("")){
			/*int start = filePath.lastIndexOf('\\');
			if (start == -1)
				start = filePath.lastIndexOf('/');
			s = filePath.substring(start+1, filePath.length());*/
			
			File f = new File(filePath);
			s = f.getName();
			
		}
		return s;
	}//-------------------------------------------------------------------------
	
}
