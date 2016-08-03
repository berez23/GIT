package it.webred.successioni.output;

public class Civico extends Indirizzo {

	private String civico;
	
	public Civico(Object sed, Object ind, String civico) {
		super(sed, ind);
		this.civico = civico;
		// TODO Auto-generated constructor stub
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}
	

}
