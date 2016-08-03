package it.webred.ct.service.tsSoggiorno.data.access.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.model.IsClassiStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsDichiarazione;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaDovuta;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaIncassata;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsRifiuto;
import it.webred.ct.service.tsSoggiorno.data.model.IsRifiutoGruppo;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsStrutturaSnap;

public class RifiutoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private IsRifiuto rifiuto;
	private List<IsRifiutoGruppo> gruppo;
	
	public RifiutoDTO(String index){
		this.id = index;
		rifiuto = new IsRifiuto();
		gruppo = new ArrayList<IsRifiutoGruppo>();
	}
	
	public RifiutoDTO(BigDecimal idDichiarazione, String index){
		 this.id = index;
		 rifiuto = new IsRifiuto();
		 gruppo = new ArrayList<IsRifiutoGruppo>();
		 this.rifiuto.setFkIsDichiarazione(idDichiarazione);
	}
	
	public IsRifiuto getRifiuto() {
		return rifiuto;
	}
	public void setRifiuto(IsRifiuto rifiuto) {
		this.rifiuto = rifiuto;
	}
	public List<IsRifiutoGruppo> getGruppo() {
		return gruppo;
	}
	public void setGruppo(List<IsRifiutoGruppo> gruppo) {
		this.gruppo = gruppo;
	}
	
	public boolean isRifiutoValorizzato(){
		boolean isValorizzato = false;
		
		isValorizzato =  isFieldValorizzato(rifiuto.getCivicoRes()) || isFieldValorizzato(rifiuto.getCodfisc()) || isFieldValorizzato(rifiuto.getCognome()) || isFieldValorizzato(rifiuto.getComuneNasc()) ||
				isFieldValorizzato(rifiuto.getComuneRes()) || isFieldValorizzato(rifiuto.getDataNasc()) || isFieldValorizzato(rifiuto.getEmail()) || isFieldValorizzato(rifiuto.getFax()) || isFieldValorizzato(rifiuto.getIndirizzoRes()) ||
				isFieldValorizzato(rifiuto.getMotivoRifiuto()) || isFieldValorizzato(rifiuto.getNome()) || isFieldValorizzato(rifiuto.getSiglaProvNasc()) ||
				isFieldValorizzato(rifiuto.getSiglaProvRes()) || isFieldValorizzato(rifiuto.getSoggiornoAl()) ||
				isFieldValorizzato(rifiuto.getSoggiornoDal()) || isFieldValorizzato(rifiuto.getTel()) ;
	
		return isValorizzato;
	}
	
	public boolean isRifiutoValReq(){
		boolean isValorizzato = false;
		
		isValorizzato =  isFieldValorizzato(rifiuto.getCognome()) &&  isFieldValorizzato(rifiuto.getNome()) &&
				isFieldValorizzato(rifiuto.getSoggiornoAl()) && isFieldValorizzato(rifiuto.getSoggiornoDal());
	
		return isValorizzato;
	}
	
	public boolean isGruppoValReq(){
		boolean nonValido = false;
		
		int i=0;
		while(i<gruppo.size() &&  !nonValido){
			IsRifiutoGruppo rg = gruppo.get(i);
			nonValido =  !(isFieldValorizzato(rg.getCognome()) &&  isFieldValorizzato(rg.getNome()));
			i++;
		}
		
		return !nonValido;
	}
	
	private boolean isFieldValorizzato(Object r){
		boolean isValorizzato = false;
		isValorizzato = r!=null && !"".equals(r.toString());
		return isValorizzato;
	}
	
	public boolean isDateSoggiornoOrdinate(){
		
		boolean ord = true;
		Date inizio = rifiuto.getSoggiornoDal();
		Date fine = rifiuto.getSoggiornoAl();
		if(inizio!=null && fine!=null && !inizio.before(fine))
			ord = false;
		
		return ord;
	}
	
	public boolean isDateSoggiornoInRange(Date iniRange, Date fineRange){
		boolean inRange = true;
		Date inizio = rifiuto.getSoggiornoDal();
		Date fine = rifiuto.getSoggiornoAl();
		
		if(iniRange!=null && fineRange!=null){
			/*if(inizio!=null && (inizio.after(fineRange) || inizio.before(iniRange)))
				inRange = false;*/
			if(fine!=null && (fine.after(fineRange) || fine.before(iniRange)))
				inRange = false;
		}
		
		return inRange;
	}

	public void clearRifiuto(){
		BigDecimal idSel = rifiuto.getFkIsDichiarazione();
		rifiuto = new IsRifiuto();
		rifiuto.setFkIsDichiarazione(idSel);
		gruppo = new ArrayList<IsRifiutoGruppo>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public int getIntId(){
		return new Integer(id).intValue();
	}
	
	public void doDeleteLastComponenteGruppo(){
		if(gruppo.size()>0)
		this.gruppo.remove(gruppo.size()-1);
	}
	
	public void doAddComponenteGruppo(){
		IsRifiutoGruppo irg = new IsRifiutoGruppo();
		irg.setIsRifiuto(rifiuto);
		gruppo.add(irg);
	}
	
	public int getNumOspiti(){
		return this.gruppo.size()+1;
	}
	
}
