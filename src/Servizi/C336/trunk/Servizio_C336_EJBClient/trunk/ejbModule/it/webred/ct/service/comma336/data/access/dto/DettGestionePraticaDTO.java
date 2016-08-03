package it.webred.ct.service.comma336.data.access.dto;

import it.webred.ct.data.access.basic.c336.dto.C336PraticaDTO;
import it.webred.ct.data.model.c336.C336Allegato;
import it.webred.ct.data.model.c336.C336GesPratica;
import it.webred.ct.data.model.c336.C336GridAttribCatA2;
import it.webred.ct.data.model.c336.C336SkCarGenFabbricato;
import it.webred.ct.data.model.c336.C336SkCarGenUiu;
import it.webred.ct.data.model.c336.C336TabValIncrClsA4A3;
import it.webred.ct.data.model.c336.C336TabValIncrClsA5A6;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class DettGestionePraticaDTO extends CeTBaseObject {

	private static final long serialVersionUID = 1L;
	
	C336PraticaDTO datiPratica = new C336PraticaDTO();
	private List<C336GesPratica> listaOperatoriPratica = new ArrayList<C336GesPratica>();
	private List<C336Allegato> listaAllegatiPratica = new ArrayList<C336Allegato>();
	private C336SkCarGenFabbricato schedaFabbricato = new C336SkCarGenFabbricato();
	private C336SkCarGenUiu schedaUiu = new  C336SkCarGenUiu();
	private C336TabValIncrClsA4A3 tabellaValutIncrA4A3 = new C336TabValIncrClsA4A3();
	private C336TabValIncrClsA5A6 tabellaValutIncrA5A6 = new C336TabValIncrClsA5A6();
	private C336GridAttribCatA2 grigliaAttribA2 = new C336GridAttribCatA2();
	
	private Integer punteggio;
	private String suggestedCat;

	private String provaNota ;
	
	public C336PraticaDTO getDatiPratica() {
		return datiPratica;
	}
	public void setDatiPratica(C336PraticaDTO datiPratica) {
		this.datiPratica = datiPratica;
	}
	public List<C336GesPratica> getListaOperatoriPratica() {
		return listaOperatoriPratica;
	}
	public void setListaOperatoriPratica(List<C336GesPratica> listaOperatoriPratica) {
		this.listaOperatoriPratica = listaOperatoriPratica;
	}
	public List<C336Allegato> getListaAllegatiPratica() {
		return listaAllegatiPratica;
	}
	public void setListaAllegatiPratica(List<C336Allegato> listaAllegatiPratica) {
		this.listaAllegatiPratica = listaAllegatiPratica;
	}
	public C336SkCarGenFabbricato getSchedaFabbricato() {
		return schedaFabbricato;
	}
	public void setSchedaFabbricato(C336SkCarGenFabbricato schedaFabbricato) {
		this.schedaFabbricato = schedaFabbricato;
	}
	public C336SkCarGenUiu getSchedaUiu() {
		return schedaUiu;
	}
	public void setSchedaUiu(C336SkCarGenUiu schedaUiu) {
		this.schedaUiu = schedaUiu;
	}
	public C336TabValIncrClsA4A3 getTabellaValutIncrA4A3() {
		return tabellaValutIncrA4A3;
	}
	public void setTabellaValutIncrA4A3(C336TabValIncrClsA4A3 tabellaValutIncrA4A3) {
		this.tabellaValutIncrA4A3 = tabellaValutIncrA4A3;
	}
	public C336TabValIncrClsA5A6 getTabellaValutIncrA5A6() {
		return tabellaValutIncrA5A6;
	}
	public void setTabellaValutIncrA5A6(C336TabValIncrClsA5A6 tabellaValutIncrA5A6) {
		this.tabellaValutIncrA5A6 = tabellaValutIncrA5A6;
	}
	public C336GridAttribCatA2 getGrigliaAttribA2() {
		return grigliaAttribA2;
	}
	public void setGrigliaAttribA2(C336GridAttribCatA2 grigliaAttribA2) {
		this.grigliaAttribA2 = grigliaAttribA2;
	}
	public String getProvaNota() {
		return provaNota;
	}
	public void setProvaNota(String provaNota) {
		this.provaNota = provaNota;
	}
	
	public Integer getPunteggio() {	
		punteggio = null;
		
		Integer count = null;
		if(this.grigliaAttribA2!=null){
			BigDecimal val1 = this.grigliaAttribA2.getValCarAndrone();
			BigDecimal val2 = this.grigliaAttribA2.getValNumUiuPiano();
			BigDecimal val3 = this.grigliaAttribA2.getValSupMediaUiu();
			if(val1!=null && val2!=null && val3!=null){
				count = val1.intValue() + val2.intValue() + val3.intValue();
			}
		}
		punteggio = count;
		return punteggio;
	}
	
	public String getSuggestedCat() {
		Integer punteggio = this.getPunteggio();
		suggestedCat = "";
		if(punteggio!=null){
			if(punteggio>=0 && punteggio<=3)
				suggestedCat = "A4";
			else if(punteggio==4 || punteggio==5)
				suggestedCat = "A4/A2";
			else if(punteggio>=6 && punteggio<=8)
				suggestedCat = "A2";
		}
		return suggestedCat;
	}
	
	
}
