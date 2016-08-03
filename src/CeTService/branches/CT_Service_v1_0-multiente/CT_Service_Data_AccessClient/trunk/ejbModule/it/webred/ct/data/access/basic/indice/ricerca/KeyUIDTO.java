package it.webred.ct.data.access.basic.indice.ricerca;

import java.util.Date;

public class KeyUIDTO extends KeyFabbricatoDTO {
	private String sub;

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}
	public KeyUIDTO () {
		super();
	}
	public KeyUIDTO (String codNazionale, String sezione ,String foglio ,String  particella ,	Date dtVal, String sub)  {
		super(codNazionale, sezione , foglio ,  particella ,	 dtVal);
		this.sub=sub;
		
	}
	public String stringValue() {
		String retVal = "";
		retVal = this.codNazionale!=null ? this.codNazionale: "";
		retVal += "-";
		retVal += this.sezione!=null ? this.sezione: "";
		retVal += "-";
		retVal += this.foglio!=null ? this.foglio : "";
		retVal += "-";
		retVal += this.particella!=null ? this.particella : "";
		retVal += "-";
		retVal += this.dtVal!=null ? this.dtVal.toString() : "null";
		retVal += "-";
		retVal += sub!=null ? sub : "";
		System.out.println("ogg: " + retVal);
		return retVal;	
	}
	
	}
