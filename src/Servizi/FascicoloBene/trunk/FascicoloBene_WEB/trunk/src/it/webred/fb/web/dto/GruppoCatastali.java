package it.webred.fb.web.dto;

import it.webred.fb.ejb.dto.locazione.DatiCatastali;

import java.util.ArrayList;
import java.util.List;

public class GruppoCatastali {
	protected String provenienza = new String();
	protected List<DatiCatastali> datiCats = new ArrayList<DatiCatastali>();
	
	public GruppoCatastali(String provenienza){
		this.provenienza = provenienza;
		this.datiCats = new ArrayList<DatiCatastali>();
	}
	
	public GruppoCatastali(String provenienza, List<DatiCatastali> datiCats){
		this.provenienza = provenienza;
		this.datiCats = datiCats;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public List<DatiCatastali> getDatiCats() {
		return datiCats;
	}

	public void setDatiCats(List<DatiCatastali> datiCats) {
		this.datiCats = datiCats;
	}
}