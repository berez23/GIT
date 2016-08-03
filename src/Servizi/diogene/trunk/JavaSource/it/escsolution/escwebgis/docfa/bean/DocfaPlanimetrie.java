
package it.escsolution.escwebgis.docfa.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.File;
import java.io.Serializable;


public class DocfaPlanimetrie extends EscObject implements Serializable{
	
	private File file;
	private int formato;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public int getFormato() {
		return formato;
	}
	public void setFormato(int formato) {
		this.formato = formato;
	} 
	
	
}
