package it.webred.fb.ejb.dto.locazione;


import it.webred.fb.ejb.dto.DatoSpec;

public class DatiCatastali extends DatoDaProvenienza {

	private static final long serialVersionUID = 1L;
	
	private DatoSpec codInventario = new DatoSpec();
	
	private DatoSpec comune = new DatoSpec();
	private DatoSpec sezione = new DatoSpec();
	private DatoSpec foglio = new DatoSpec();
	private DatoSpec mappale = new DatoSpec();
	private DatoSpec codComune = new DatoSpec();
	
	public DatiCatastali(DatoSpec codInventario, DatoSpec comune, DatoSpec codComune, DatoSpec sezione, DatoSpec foglio, DatoSpec mappale, DatoSpec provenienza){
		super(provenienza);
		this.codInventario = codInventario;
		this.comune = comune;
		this.codComune = codComune;
		this.foglio = foglio;
		this.mappale = mappale;
	}
	
	public DatoSpec getFoglio() {
		return foglio;
	}
	
	public void setFoglio(DatoSpec foglio) {
		this.foglio = foglio;
	}
	
	public DatoSpec getMappale() {
		return mappale;
	}
	
	public void setMappale(DatoSpec mappale) {
		this.mappale = mappale;
	}

	public DatoSpec getComune() {
		return comune;
	}

	public void setComune(DatoSpec comune) {
		this.comune = comune;
	}

	public DatoSpec getSezione() {
		return sezione;
	}

	public void setSezione(DatoSpec sezione) {
		this.sezione = sezione;
	}

	public DatoSpec getCodComune() {
		return codComune;
	}

	public void setCodComune(DatoSpec codComune) {
		this.codComune = codComune;
	}

	public DatoSpec getCodInventario() {
		return codInventario;
	}

	public void setCodInventario(DatoSpec codInventario) {
		this.codInventario = codInventario;
	}
	
}
