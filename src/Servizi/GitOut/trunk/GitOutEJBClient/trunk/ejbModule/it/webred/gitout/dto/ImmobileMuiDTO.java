package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="immobileMuiDTO")
public class ImmobileMuiDTO implements Serializable{
	
	private static final long serialVersionUID = 7923720172900660439L;
	
	private String foglio = "";
	private String numero = "";
	private String sezione = "";
	private String subalterno = "";
	private String ideCatastale  = "";
	private String graffato = "";
	private String categoria = "";
	private String metriQuadri = "";
	private String metriCubi = "";
	private String vani = "";
	private String superficie = "";
	private String indirizzoTras = "";
	private String indirizzoCata = "";
	private String scala = "";
	private String interno = "";
	private String piano = "";
	private String edificio = "";
	private String lotto = "";
	private String rendita = "";

	public ImmobileMuiDTO() {
	}//-------------------------------------------------------------------------

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getSubalterno() {
		return subalterno;
	}

	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}

	public String getIdeCatastale() {
		return ideCatastale;
	}

	public void setIdeCatastale(String ideCatastale) {
		this.ideCatastale = ideCatastale;
	}

	public String getGraffato() {
		return graffato;
	}

	public void setGraffato(String graffato) {
		this.graffato = graffato;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getVani() {
		return vani;
	}

	public void setVani(String vani) {
		this.vani = vani;
	}

	public String getIndirizzoTras() {
		return indirizzoTras;
	}

	public void setIndirizzoTras(String indirizzoTras) {
		this.indirizzoTras = indirizzoTras;
	}

	public String getIndirizzoCata() {
		return indirizzoCata;
	}

	public void setIndirizzoCata(String indirizzoCata) {
		this.indirizzoCata = indirizzoCata;
	}

	public String getScala() {
		return scala;
	}

	public void setScala(String scala) {
		this.scala = scala;
	}

	public String getInterno() {
		return interno;
	}

	public void setInterno(String interno) {
		this.interno = interno;
	}

	public String getPiano() {
		return piano;
	}

	public void setPiano(String piano) {
		this.piano = piano;
	}

	public String getEdificio() {
		return edificio;
	}

	public void setEdificio(String edificio) {
		this.edificio = edificio;
	}

	public String getLotto() {
		return lotto;
	}

	public void setLotto(String lotto) {
		this.lotto = lotto;
	}

	public String getRendita() {
		return rendita;
	}

	public void setRendita(String rendita) {
		this.rendita = rendita;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMetriQuadri() {
		return metriQuadri;
	}

	public void setMetriQuadri(String metriQuadri) {
		this.metriQuadri = metriQuadri;
	}

	public String getMetriCubi() {
		return metriCubi;
	}

	public void setMetriCubi(String metriCubi) {
		this.metriCubi = metriCubi;
	}

	public String getSuperficie() {
		return superficie;
	}

	public void setSuperficie(String superficie) {
		this.superficie = superficie;
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
