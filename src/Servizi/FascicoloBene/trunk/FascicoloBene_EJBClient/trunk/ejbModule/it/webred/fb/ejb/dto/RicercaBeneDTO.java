package it.webred.fb.ejb.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

public class RicercaBeneDTO extends CeTBaseObject {

	private Integer maxResult;
	
	private boolean ricercaIndirizzo;
	private String tipoVia;
	private KeyValueDTO comuneInd;
	private KeyValueDTO via;
	private KeyValueDTO civico;
	
	private boolean ricercaInventario;
	private String codInventario;
	private String codCatInventario;
	private String codTipo;
	private String tipoDirittoReale;
	private String codFascicolo = "";
	private String desFascicolo = "";
	
	private boolean ricercaCatasto;
	private KeyValueDTO comuneCat;
	private String foglio;
	private String mappale;
	private String codEnte;
	
	public RicercaBeneDTO(){
		via = new KeyValueDTO();
		civico = new KeyValueDTO();
		comuneCat = new KeyValueDTO();
		comuneInd = new KeyValueDTO();
	}
	
	public boolean isEmpty(){
		boolean parImpostati=false;
		if(this.isRicercaCatasto()){
			parImpostati =  (foglio!=null && !foglio.isEmpty());
		}
		if(this.isRicercaInventario()){
			parImpostati = true;
		}
		if(this.isRicercaIndirizzo()){
			parImpostati = (via!=null && via.getCodice()!=null) || 
						   (civico!=null && civico.getCodice()!=null);
		}
		return !parImpostati;
		
	}
	
	public boolean isRicercaIndirizzo() {
		return ricercaIndirizzo;
	}
	public void setRicercaIndirizzo(boolean ricercaIndirizzo) {
		this.ricercaIndirizzo = ricercaIndirizzo;
	}
	public String getTipoVia() {
		return tipoVia;
	}
	public void setTipoVia(String tipoVia) {
		this.tipoVia = tipoVia;
	}
	public KeyValueDTO getCivico() {
		return civico;
	}

	public void setCivico(KeyValueDTO civico) {
		this.civico = civico;
	}
	public boolean isRicercaInventario() {
		return ricercaInventario;
	}
	public void setRicercaInventario(boolean ricercaInventario) {
		this.ricercaInventario = ricercaInventario;
	}
	public String getCodInventario() {
		return codInventario;
	}
	public void setCodInventario(String codInventario) {
		this.codInventario = codInventario;
	}
	public String getCodCatInventario() {
		return codCatInventario;
	}
	public void setCodCatInventario(String codCatInventario) {
		this.codCatInventario = codCatInventario;
	}
	public boolean isRicercaCatasto() {
		return ricercaCatasto;
	}
	public void setRicercaCatasto(boolean ricercaCatasto) {
		this.ricercaCatasto = ricercaCatasto;
	}
	public String getFoglio() {
		return foglio;
	}
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}
	public String getMappale() {
		return mappale;
	}
	public void setMappale(String mappale) {
		this.mappale = mappale;
	}
	public String getCodEnte() {
		return codEnte;
	}
	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}
	public Integer getMaxResult() {
		return maxResult;
	}
	public void setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
	}
	public String getCodTipo() {
		return codTipo;
	}
	public void setCodTipo(String codTipo) {
		this.codTipo = codTipo;
	}
	public KeyValueDTO getVia() {
		return via;
	}
	public void setVia(KeyValueDTO via) {
		this.via = via;
	}

	public KeyValueDTO getComune() {
		if(this.isRicercaCatasto())
			return comuneCat;
		else if(this.isRicercaIndirizzo())
			return comuneInd;
		else
			return null;
	}


	public KeyValueDTO getComuneInd() {
		return comuneInd;
	}

	public void setComuneInd(KeyValueDTO comuneInd) {
		this.comuneInd = comuneInd;
	}

	public KeyValueDTO getComuneCat() {
		return comuneCat;
	}

	public void setComuneCat(KeyValueDTO comuneCat) {
		this.comuneCat = comuneCat;
	}

	public String getTipoDirittoReale() {
		return tipoDirittoReale;
	}

	public void setTipoDirittoReale(String tipoDirittoReale) {
		this.tipoDirittoReale = tipoDirittoReale;
	}

	public String getCodFascicolo() {
		return codFascicolo;
	}

	public void setCodFascicolo(String codFascicolo) {
		this.codFascicolo = codFascicolo;
	}

	public String getDesFascicolo() {
		return desFascicolo;
	}

	public void setDesFascicolo(String desFascicolo) {
		this.desFascicolo = desFascicolo;
	}
	
}
