package it.escsolution.escwebgis.concessioni.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.File;
import java.io.Serializable;

public class SuapFileLink extends EscObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String tipo;
	private int livello;
	private String path;
	private String link;
	
	public static final String FOLDER = "FOLDER";
	public static final String FILE = "FILE";
	
	public SuapFileLink() {
	}

	public SuapFileLink(String id, String tipo, int livello, String path, String link) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.livello = livello;
		this.path = path;
		this.link = link;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public int getLivello() {
		return livello;
	}
	
	public void setLivello(int livello) {
		this.livello = livello;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public static String getLinkByPath(String path) throws Exception {
		if (path == null || path.equals("")) {
			return null;
		}
		path = path.replace("\\", "/").replace("/", File.separator);
		if (path.indexOf(File.separator) == -1) {
			return null;
		}
		return path.substring(path.lastIndexOf(File.separator) + File.separator.length());
	}

}
