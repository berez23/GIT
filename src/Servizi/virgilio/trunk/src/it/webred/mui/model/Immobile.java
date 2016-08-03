package it.webred.mui.model;

import it.webred.mui.consolidation.ComunicazioneConverter;

public class Immobile extends ComunicazioneConverter {

    private Integer foglio;
    private String numero;    
    private Integer subalterno;
    private String allegato;
	public Immobile() {
	}
	public Integer getFoglio() {
		return foglio;
	}
	public void setFoglio(Integer foglio) {
		this.foglio = foglio;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Integer getSubalterno() {
		return subalterno;
	}
	public void setSubalterno(Integer subalterno) {
		this.subalterno = subalterno;
	}
	public String getAllegato() {
		return allegato;
	}
	public void setAllegato(String allegato) {
		this.allegato = allegato;
	}
	public boolean equals(Object obj){
		try {
			Immobile imm =(Immobile)obj;
			return checkEquals(getFoglio(),imm.getFoglio())&&checkEquals(getNumero(),imm.getNumero())&&checkEquals(getSubalterno(),imm.getSubalterno());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	private boolean checkEquals(Object o1, Object o2){
		if(o1 == null){
			return o2 ==null;
		}
		else return o1.equals(o2);
	}

}
