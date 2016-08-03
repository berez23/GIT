package it.webred.mui.model;

public class Coniuge {

	public Coniuge() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String toString(){
		return String.valueOf(codiceFiscale) + " - " + String.valueOf(residenza);
	}
	private String codiceFiscale;
	private Residenza residenza;
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public Residenza getResidenza() {
		return residenza;
	}
	public void setResidenza(Residenza residenza) {
		this.residenza = residenza;
	}
	public boolean equals(Object obj){
		return ( this.codiceFiscale.equals( ((Coniuge)obj).codiceFiscale) && 
					this.residenza.equals( ((Coniuge)obj).residenza));
	}

}
