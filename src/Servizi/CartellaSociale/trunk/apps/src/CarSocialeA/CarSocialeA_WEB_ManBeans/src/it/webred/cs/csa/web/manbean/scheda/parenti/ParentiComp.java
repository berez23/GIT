package it.webred.cs.csa.web.manbean.scheda.parenti;

import it.webred.cs.data.model.CsAAnagrafica;
import it.webred.cs.data.model.CsAComponente;
import it.webred.cs.data.model.CsTbContatto;
import it.webred.cs.data.model.CsTbDisponibilita;
import it.webred.cs.data.model.CsTbPotesta;
import it.webred.cs.data.model.CsTbTipoRapportoCon;
import it.webred.cs.jsf.bean.ValiditaCompBaseBean;
import it.webred.cs.jsf.interfaces.IParenti;
import it.webred.ct.config.model.AmTabNazioni;
import it.webred.jsf.bean.ComuneBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ParentiComp extends ValiditaCompBaseBean implements IParenti{
	
	private List<CsAComponente> lstParenti = new ArrayList<CsAComponente>();
	private List<CsAComponente> lstConoscenti = new ArrayList<CsAComponente>();
	
	private int idxSelected;
	private boolean nuovo;
	
	private boolean showNewParente = false;
	private boolean showNewConoscente = false;
	private NuovoParenteBean nuovoParenteBean = new NuovoParenteBean();
	private NuovoConoscenteBean nuovoConoscenteBean = new NuovoConoscenteBean();
	
	@Override
	public void salvaNuovoParente() {
		
		CsAComponente comp = null;
		CsAAnagrafica compAna = null;
		CsTbTipoRapportoCon compTipoRapporto = null;
		if(nuovo){
			comp = new CsAComponente();
			compAna = new CsAAnagrafica();
			compTipoRapporto = new CsTbTipoRapportoCon();
		}
		else {
			comp = lstParenti.get(idxSelected);
			compAna = comp.getCsAAnagrafica();
			compTipoRapporto = comp.getCsTbTipoRapportoCon();
		}
		
		compAna.setCf(nuovoParenteBean.getCodFiscale());
		compAna.setCognome(nuovoParenteBean.getCognome());
		comp.setDtIns(new Date());
		comp.setIndirizzoRes(nuovoParenteBean.getIndirizzo());
		compAna.setNome(nuovoParenteBean.getNome());
		comp.setNumCivRes(nuovoParenteBean.getCivico());
		compAna.setSesso(nuovoParenteBean.getSesso());
		comp.setUserIns(getUser().getUsername());
		compAna.setCell(nuovoParenteBean.getCellulare());
		compAna.setCittadinanza(nuovoParenteBean.getCittadinanza());
		comp.setConvivente(nuovoParenteBean.getConvivente());
		CsTbContatto contatto = new CsTbContatto();
		contatto.setId(nuovoParenteBean.getIdContatto());
		comp.setCsTbContatto(contatto);
		comp.setCsTbDisponibilita(null);
		if(nuovoParenteBean.getIdDisponibilita() != null && nuovoParenteBean.getIdDisponibilita().intValue() != 0) {
			CsTbDisponibilita disponibilita = new CsTbDisponibilita();
			disponibilita.setId(nuovoParenteBean.getIdDisponibilita());
			comp.setCsTbDisponibilita(disponibilita);
		}
		compTipoRapporto.setId(nuovoParenteBean.getIdParentela());
		compTipoRapporto.setDescrizione(nuovoParenteBean.getDescrParentela());
		comp.setCsTbTipoRapportoCon(compTipoRapporto);
		CsTbPotesta potesta = new CsTbPotesta();
		potesta.setId(nuovoParenteBean.getIdPotesta());
		comp.setCsTbPotesta(potesta);
		compAna.setDataDecesso(nuovoParenteBean.getDataDecesso());
		nuovoParenteBean.setDecesso(compAna.getDataDecesso() != null);
		compAna.setDataNascita(nuovoParenteBean.getDataNascita());
		compAna.setEmail(nuovoParenteBean.getEmail());
		comp.setIndirizzoRes(nuovoParenteBean.getIndirizzo());
		compAna.setNote(nuovoParenteBean.getNote());
		comp.setNumCivRes(nuovoParenteBean.getCivico());
		compAna.setTel(nuovoParenteBean.getTelefono());
		if(nuovoParenteBean.getComuneNazioneNascitaBean().getComuneMan().getComune() != null){
			compAna.setComuneNascitaCod(nuovoParenteBean.getComuneNazioneNascitaBean().getComuneMan().getComune().getCodIstatComune());
			compAna.setComuneNascitaDes(nuovoParenteBean.getComuneNazioneNascitaBean().getComuneMan().getComune().getDenominazione());
			compAna.setProvNascitaCod(nuovoParenteBean.getComuneNazioneNascitaBean().getComuneMan().getComune().getSiglaProv());
		} else {
			compAna.setComuneNascitaCod(null);
			compAna.setComuneNascitaDes(null);
			compAna.setProvNascitaCod(null);
		}
		if(nuovoParenteBean.getComuneNazioneNascitaBean().getNazioneMan().getNazione() != null){
			compAna.setStatoNascitaCod(nuovoParenteBean.getComuneNazioneNascitaBean().getNazioneMan().getNazione().getCodice());
			compAna.setStatoNascitaDes(nuovoParenteBean.getComuneNazioneNascitaBean().getNazioneMan().getNazione().getNazione());
		} else {
			compAna.setStatoNascitaCod(null);
			compAna.setStatoNascitaDes(null);
		}
		if(nuovoParenteBean.getComuneNazioneResidenzaBean().getComuneMan().getComune() != null){
			comp.setComResCod(nuovoParenteBean.getComuneNazioneResidenzaBean().getComuneMan().getComune().getCodIstatComune());
			comp.setComResDes(nuovoParenteBean.getComuneNazioneResidenzaBean().getComuneMan().getComune().getDenominazione());
			comp.setProvResCod(nuovoParenteBean.getComuneNazioneResidenzaBean().getComuneMan().getComune().getSiglaProv());
		} else {
			comp.setComResCod(null);
			comp.setComResDes(null);
			comp.setProvResCod(null);
		}
		if(nuovoParenteBean.getComuneNazioneResidenzaBean().getNazioneMan().getNazione() != null){
			comp.setComAltroCod(nuovoParenteBean.getComuneNazioneResidenzaBean().getNazioneMan().getNazione().getCodice());
			comp.setComAltroDes(nuovoParenteBean.getComuneNazioneResidenzaBean().getNazioneMan().getNazione().getNazione());
		} else {
			comp.setComAltroCod(null);
			comp.setComAltroDes(null);
		}
		comp.setCsAAnagrafica(compAna);
		
		if(nuovo)
			lstParenti.add(comp);

		showNewParente = false;
		
	}
	
	@Override
	public void salvaNuovoConoscente() {
		
		CsAComponente comp = null;
		CsAAnagrafica compAna = null;
		CsTbTipoRapportoCon compTipoRapporto = null;
		if(nuovo){
			comp = new CsAComponente();
			compAna = new CsAAnagrafica();
			compTipoRapporto = new CsTbTipoRapportoCon();
		}
		else {
			comp = lstConoscenti.get(idxSelected);
			compAna = comp.getCsAAnagrafica();
			compTipoRapporto = comp.getCsTbTipoRapportoCon();
		}
		
		compAna.setCognome(nuovoConoscenteBean.getCognome());
		comp.setDtIns(new Date());
		comp.setIndirizzoRes(nuovoConoscenteBean.getIndirizzo());
		compAna.setNome(nuovoConoscenteBean.getNome());
		comp.setNumCivRes(nuovoConoscenteBean.getCivico());
		comp.setUserIns(getUser().getUsername());
		compAna.setCell(nuovoConoscenteBean.getCellulare());
		compTipoRapporto.setId(nuovoConoscenteBean.getIdParentela());
		compTipoRapporto.setDescrizione(nuovoConoscenteBean.getDescrParentela());
		comp.setCsTbTipoRapportoCon(compTipoRapporto);
		comp.setIndirizzoRes(nuovoConoscenteBean.getIndirizzo());
		compAna.setNote(nuovoConoscenteBean.getNote());
		comp.setNumCivRes(nuovoConoscenteBean.getCivico());
		compAna.setTel(nuovoConoscenteBean.getTelefono());
		if(nuovoConoscenteBean.getComuneNazioneResidenzaBean().getComuneMan().getComune() != null){
			comp.setComResCod(nuovoConoscenteBean.getComuneNazioneResidenzaBean().getComuneMan().getComune().getCodIstatComune());
			comp.setComResDes(nuovoConoscenteBean.getComuneNazioneResidenzaBean().getComuneMan().getComune().getDenominazione());
			comp.setProvResCod(nuovoConoscenteBean.getComuneNazioneResidenzaBean().getComuneMan().getComune().getSiglaProv());
		} else {
			comp.setComResCod(null);
			comp.setComResDes(null);
			comp.setProvResCod(null);
		}
		if(nuovoConoscenteBean.getComuneNazioneResidenzaBean().getNazioneMan().getNazione() != null){
			comp.setComAltroCod(nuovoConoscenteBean.getComuneNazioneResidenzaBean().getNazioneMan().getNazione().getCodice());
			comp.setComAltroDes(nuovoConoscenteBean.getComuneNazioneResidenzaBean().getNazioneMan().getNazione().getNazione());
		} else {
			comp.setComAltroCod(null);
			comp.setComAltroDes(null);
		}
		comp.setCsAAnagrafica(compAna);
		
		if(nuovo)
			lstConoscenti.add(comp);
		
		showNewConoscente = false;
				
	}
	
	@Override
	public void loadModificaParente() {
		CsAComponente comp = lstParenti.get(idxSelected);
		nuovoParenteBean.setCognome(comp.getCsAAnagrafica().getCognome());
		nuovoParenteBean.setNome(comp.getCsAAnagrafica().getNome());
		nuovoParenteBean.setCellulare(comp.getCsAAnagrafica().getCell());
		nuovoParenteBean.setCittadinanza(comp.getCsAAnagrafica().getCittadinanza());
		nuovoParenteBean.setCivico(comp.getNumCivRes());
		nuovoParenteBean.setCodFiscale(comp.getCsAAnagrafica().getCf());
		nuovoParenteBean.setConvivente(comp.getConvivente());
		nuovoParenteBean.setDataDecesso(comp.getCsAAnagrafica().getDataDecesso());
		nuovoParenteBean.setDataNascita(comp.getCsAAnagrafica().getDataNascita());
		nuovoParenteBean.setDecesso(comp.getCsAAnagrafica().getDataDecesso() != null);
		nuovoParenteBean.setEmail(comp.getCsAAnagrafica().getEmail());
		if(comp.getCsTbContatto() != null)
			nuovoParenteBean.setIdContatto(comp.getCsTbContatto().getId());
		if(comp.getCsTbDisponibilita() != null)
			nuovoParenteBean.setIdDisponibilita(comp.getCsTbDisponibilita().getId());
		if(comp.getCsTbTipoRapportoCon() != null)
			nuovoParenteBean.setIdParentela(comp.getCsTbTipoRapportoCon().getId());
		if(comp.getCsTbPotesta() != null)
			nuovoParenteBean.setIdPotesta(comp.getCsTbPotesta().getId());
		nuovoParenteBean.setIndirizzo(comp.getIndirizzoRes());
		nuovoParenteBean.setNote(comp.getCsAAnagrafica().getNote());
		nuovoParenteBean.setSesso(comp.getCsAAnagrafica().getSesso());
		nuovoParenteBean.setTelefono(comp.getCsAAnagrafica().getTel());
		ComuneBean comuneRes = new ComuneBean(comp.getComResCod(), comp.getComResDes(),comp.getProvResCod());
		nuovoParenteBean.getComuneNazioneResidenzaBean().getComuneMan().setComune(comuneRes);
		ComuneBean comuneNas = new ComuneBean(comp.getCsAAnagrafica().getComuneNascitaCod(), comp.getCsAAnagrafica().getComuneNascitaDes(),comp.getCsAAnagrafica().getProvNascitaCod());
		nuovoParenteBean.getComuneNazioneNascitaBean().getComuneMan().setComune(comuneNas);
		if(comp.getCsAAnagrafica().getStatoNascitaCod() != null) {
			AmTabNazioni amTabNazioni = new AmTabNazioni();
			amTabNazioni.setCodice(comp.getCsAAnagrafica().getStatoNascitaCod());
			amTabNazioni.setNazione(comp.getCsAAnagrafica().getStatoNascitaDes());
			nuovoParenteBean.getComuneNazioneNascitaBean().setValue(nuovoParenteBean.getComuneNazioneNascitaBean().getNazioneValue());
			nuovoParenteBean.getComuneNazioneNascitaBean().getNazioneMan().setNazione(amTabNazioni);
		}
		if(comp.getComAltroCod() != null) {
			AmTabNazioni amTabNazioni = new AmTabNazioni();
			amTabNazioni.setCodice(comp.getComAltroCod());
			amTabNazioni.setNazione(comp.getComAltroDes());
			nuovoParenteBean.getComuneNazioneResidenzaBean().setValue(nuovoParenteBean.getComuneNazioneResidenzaBean().getNazioneValue());
			nuovoParenteBean.getComuneNazioneResidenzaBean().getNazioneMan().setNazione(amTabNazioni);
		}
	}
	
	@Override
	public void loadModificaConoscente() {
		CsAComponente comp = lstConoscenti.get(idxSelected);
		nuovoConoscenteBean.setCognome(comp.getCsAAnagrafica().getCognome());
		nuovoConoscenteBean.setNome(comp.getCsAAnagrafica().getNome());
		nuovoConoscenteBean.setCellulare(comp.getCsAAnagrafica().getCell());
		nuovoConoscenteBean.setCivico(comp.getNumCivRes());
		if(comp.getCsTbTipoRapportoCon() != null)
			nuovoConoscenteBean.setIdParentela(comp.getCsTbTipoRapportoCon().getId());
		nuovoConoscenteBean.setIndirizzo(comp.getIndirizzoRes());
		nuovoConoscenteBean.setNote(comp.getCsAAnagrafica().getNote());
		nuovoConoscenteBean.setTelefono(comp.getCsAAnagrafica().getTel());
		ComuneBean comuneRes = new ComuneBean(comp.getComResCod(), comp.getComResDes(),comp.getProvResCod());
		nuovoConoscenteBean.getComuneNazioneResidenzaBean().getComuneMan().setComune(comuneRes);
		if(comp.getComAltroCod() != null) {
			AmTabNazioni amTabNazioni = new AmTabNazioni();
			amTabNazioni.setCodice(comp.getComAltroCod());
			amTabNazioni.setNazione(comp.getComAltroDes());
			nuovoConoscenteBean.getComuneNazioneResidenzaBean().setValue(nuovoParenteBean.getComuneNazioneResidenzaBean().getNazioneValue());
			nuovoConoscenteBean.getComuneNazioneResidenzaBean().getNazioneMan().setNazione(amTabNazioni);
		}
	}
	
	@Override
	public void eliminaParente() {
		
		lstParenti.remove(idxSelected);
		
	}
	
	@Override
	public void eliminaConoscente() {
		
		lstConoscenti.remove(idxSelected);
		
	}
	
	@Override
	public List<CsAComponente> getLstParenti() {
		return lstParenti;
	}
	
	public void setLstParenti(List<CsAComponente> lstParenti) {
		this.lstParenti = lstParenti;
	}

	@Override
	public int getIdxSelected() {
		return idxSelected;
	}

	public void setIdxSelected(int idxSelected) {
		this.idxSelected = idxSelected;
	}

	public NuovoParenteBean getNuovoParenteBean() {
		return nuovoParenteBean;
	}

	public void setNuovoParenteBean(NuovoParenteBean nuovoParenteBean) {
		this.nuovoParenteBean = nuovoParenteBean;
	}

	@Override
	public List<CsAComponente> getLstConoscenti() {
		return lstConoscenti;
	}

	public void setLstConoscenti(List<CsAComponente> lstConoscenti) {
		this.lstConoscenti = lstConoscenti;
	}

	public NuovoConoscenteBean getNuovoConoscenteBean() {
		return nuovoConoscenteBean;
	}

	public void setNuovoConoscenteBean(NuovoConoscenteBean nuovoConoscenteBean) {
		this.nuovoConoscenteBean = nuovoConoscenteBean;
	}

	@Override
	public boolean isNuovo() {
		return nuovo;
	}

	public void setNuovo(boolean nuovo) {
		this.nuovo = nuovo;
	}

	public boolean isShowNewParente() {
		return showNewParente;
	}

	public void setShowNewParente(boolean showNewParente) {
		this.showNewParente = showNewParente;
	}

	public boolean isShowNewConoscente() {
		return showNewConoscente;
	}

	public void setShowNewConoscente(boolean showNewConoscente) {
		this.showNewConoscente = showNewConoscente;
	}

}
