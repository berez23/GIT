package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="docfaCensuarioValoreDTO")
public class DocfaCensuarioValoreDTO implements Serializable{

	private static final long serialVersionUID = -1812472515899173582L;
	
	private String foglio = "";
	private String numero = "";
	private String subalterno = "";
	private String zona = "";
	private String categoria = "";
	private String classe = "";
	private String consistenza = "";
	private String superficie = "";
	private String rendita = "";
	private String identificativoImmobile = "";
	private String valoreCommerciale = "";

	public DocfaCensuarioValoreDTO() {
	}//-------------------------------------------------------------------------

	public String getSubalterno() {
		return subalterno;
	}

	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getConsistenza() {
		return consistenza;
	}

	public void setConsistenza(String consistenza) {
		this.consistenza = consistenza;
	}

	public String getSuperficie() {
		return superficie;
	}

	public void setSuperficie(String superficie) {
		this.superficie = superficie;
	}

	public String getRendita() {
		return rendita;
	}

	public void setRendita(String rendita) {
		this.rendita = rendita;
	}

	public String getValoreCommerciale() {
		return valoreCommerciale;
	}

	public void setValoreCommerciale(String valoreCommerciale) {
		this.valoreCommerciale = valoreCommerciale;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIdentificativoImmobile() {
		return identificativoImmobile;
	}

	public void setIdentificativoImmobile(String identificativoImmobile) {
		this.identificativoImmobile = identificativoImmobile;
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	
	
}
