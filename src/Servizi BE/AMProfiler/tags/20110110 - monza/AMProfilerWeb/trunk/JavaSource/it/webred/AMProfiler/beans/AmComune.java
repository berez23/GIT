package it.webred.AMProfiler.beans;

// Generated by Nicola Campanelli 24 august 2010

/**
 * AmApplication generated by hbm2java
 */
public class AmComune implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String belfiore;
	private String descrizione;

	// Constructors

	/** default constructor */
	public AmComune() {
	}

	public String getBelfiore() {
		return belfiore;
	}

	public void setBelfiore(String belfiore) {
		this.belfiore = belfiore;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/** minimal constructor */
	public AmComune(String belfiore) {
		this.belfiore = belfiore;
	}
}
