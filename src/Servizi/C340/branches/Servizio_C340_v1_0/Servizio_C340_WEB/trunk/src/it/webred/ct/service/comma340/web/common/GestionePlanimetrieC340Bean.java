package it.webred.ct.service.comma340.web.common;


import java.io.File;
import java.io.Serializable;

public class GestionePlanimetrieC340Bean extends GestioneFileBean implements Serializable {


	@Override
	protected String getFilePath(String fileName) {
		String path = super.getParamValue("path_planimetrie_c340");
		String pathFile = path + File.separatorChar + fileName;
		return pathFile;
	}
	
	@Override
	public void doDownload(){
		super.doDownload();
	}
	
}
