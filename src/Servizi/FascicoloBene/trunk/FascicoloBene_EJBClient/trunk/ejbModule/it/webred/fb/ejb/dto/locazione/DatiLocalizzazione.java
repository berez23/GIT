package it.webred.fb.ejb.dto.locazione;

import it.webred.fb.ejb.dto.DatoSpec;

public class DatiLocalizzazione extends DatoDaProvenienza {

	private static final long serialVersionUID = 1L;
	
	private DatoSpec codInventario;
	
	private DatoSpec codVia;
	private DatoSpec descrizione;
	private DatoSpec codComune;
	private DatoSpec comune;
	private DatoSpec principale;
	private DatoSpec provenienzaLoc;
	private DatoSpec civico;
	private DatoSpec tipo;
	private DatoSpec tipoVia;
	
	
	public DatiLocalizzazione(DatoSpec provenienza){
		super(provenienza);
		this.provenienzaLoc = provenienza;
	}

	public DatoSpec getCodVia() {
		return codVia;
	}

	public void setCodVia(DatoSpec codVia) {
		this.codVia = codVia;
	}

	public DatoSpec getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(DatoSpec descrizione) {
		this.descrizione = descrizione;
	}

	public DatoSpec getCodComune() {
		return codComune;
	}

	public void setCodComune(DatoSpec codComune) {
		this.codComune = codComune;
	}

	public DatoSpec getComune() {
		return comune;
	}

	public void setComune(DatoSpec comune) {
		this.comune = comune;
	}

	public DatoSpec getPrincipale() {
		return principale;
	}

	public void setPrincipale(DatoSpec principale) {
		this.principale = principale;
	}

	public DatoSpec getProvenienzaLoc() {
		return provenienzaLoc;
	}

	public void setProvenienzaLoc(DatoSpec provenienzaLoc) {
		this.provenienzaLoc = provenienzaLoc;
	}

	public DatoSpec getCivico() {
		return civico;
	}

	public void setCivico(DatoSpec civico) {
		this.civico = civico;
	}

	public DatoSpec getTipo() {
		return tipo;
	}

	public void setTipo(DatoSpec tipo) {
		this.tipo = tipo;
	}

	public DatoSpec getTipoVia() {
		return tipoVia;
	}

	public void setTipoVia(DatoSpec tipoVia) {
		this.tipoVia = tipoVia;
	}

	public DatoSpec getCodInventario() {
		return codInventario;
	}

	public void setCodInventario(DatoSpec codInventario) {
		this.codInventario = codInventario;
	}
}
