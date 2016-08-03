package it.webred.cs.csa.web.manbean.scheda.sociali;

import it.webred.cs.csa.ejb.client.AccessTableConfigurazioneSessionBeanRemote;
import it.webred.cs.data.model.CsOOrganizzazione;
import it.webred.cs.data.model.CsTbProblematica;
import it.webred.cs.data.model.CsTbProfessione;
import it.webred.cs.data.model.CsTbSettoreImpiego;
import it.webred.cs.data.model.CsTbStesuraRelazioniPer;
import it.webred.cs.data.model.CsTbTipoContratto;
import it.webred.cs.data.model.CsTbTipologiaFamiliare;
import it.webred.cs.data.model.CsTbTitoloStudio;
import it.webred.cs.data.model.CsTbTutela;
import it.webred.cs.data.model.CsTbTutelante;
import it.webred.cs.jsf.bean.ValiditaCompBaseBean;
import it.webred.cs.jsf.interfaces.IDatiSociali;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class DatiSocialiComp extends ValiditaCompBaseBean implements IDatiSociali{
	
	private AccessTableConfigurazioneSessionBeanRemote confService = (AccessTableConfigurazioneSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableConfigurazioneSessionBean");
	
	private BigDecimal idTipologiaFam;
	private Integer nMinori;
	private BigDecimal idInviante;
	private BigDecimal idInviatoA;
	private BigDecimal idProblematica;
	private BigDecimal idStesuraRelPer;
	private boolean inCaricoCPS;
	private boolean inCaricoNOA;
	private boolean inCaricoSERT;
	private boolean interventiNucleo;
	private String interventiTipo;
	private String autosufficienza;
	private String patologia;
	private String patologiaAltro;
	private BigDecimal idTitoloStudio;
	private BigDecimal idProfessione;
	private BigDecimal idTutela;
	private BigDecimal idTutelante;
	private BigDecimal idSettoreImpiego;
	private BigDecimal idTipoContratto;
	
	private List<CsTbTipologiaFamiliare> lstCsTbTipologiaFam;
	private List<SelectItem> lstTipologiaFam;
	private List<SelectItem> lstInviante;
	private List<SelectItem> lstProblematiche;
	private List<SelectItem> lstStesuraRelPer;
	private List<SelectItem> lstTitoliStudio;
	private List<SelectItem> lstProfessioni;
	private List<SelectItem> lstTutele;
	private List<SelectItem> lstTutelanti;
	private List<SelectItem> lstAutosufficienza;
	private List<SelectItem> lstSettoreImpiego;
	private List<SelectItem> lstTipoContratto;

	private List<Long> lstOccupato;
	
	@Override
	public BigDecimal getIdTipologiaFam() {
		return idTipologiaFam;
	}

	public void setIdTipologiaFam(BigDecimal idTipologiaFam) {
		this.idTipologiaFam = idTipologiaFam;
	}

	@Override
	public Integer getnMinori() {
		return nMinori;
	}

	public void setnMinori(Integer nMinori) {
		this.nMinori = nMinori;
	}

	@Override
	public boolean isInCaricoCPS() {
		return inCaricoCPS;
	}

	public void setInCaricoCPS(boolean inCaricoCPS) {
		this.inCaricoCPS = inCaricoCPS;
	}

	@Override
	public boolean isInterventiNucleo() {
		return interventiNucleo;
	}

	public void setInterventiNucleo(boolean interventiNucleo) {
		this.interventiNucleo = interventiNucleo;
	}

	@Override
	public String getInterventiTipo() {
		return interventiTipo;
	}

	public void setInterventiTipo(String interventiTipo) {
		this.interventiTipo = interventiTipo;
	}

	@Override
	public BigDecimal getIdInviante() {
		return idInviante;
	}

	public void setIdInviante(BigDecimal idInviante) {
		this.idInviante = idInviante;
	}

	@Override
	public BigDecimal getIdInviatoA() {
		return idInviatoA;
	}

	public void setIdInviatoA(BigDecimal idInviatoA) {
		this.idInviatoA = idInviatoA;
	}

	@Override
	public BigDecimal getIdProblematica() {
		return idProblematica;
	}

	public void setIdProblematica(BigDecimal idProblematica) {
		this.idProblematica = idProblematica;
	}

	@Override
	public BigDecimal getIdStesuraRelPer() {
		return idStesuraRelPer;
	}

	public void setIdStesuraRelPer(BigDecimal idStesuraRelPer) {
		this.idStesuraRelPer = idStesuraRelPer;
	}

	@Override
	public BigDecimal getIdTitoloStudio() {
		return idTitoloStudio;
	}

	public void setIdTitoloStudio(BigDecimal idTitoloStudio) {
		this.idTitoloStudio = idTitoloStudio;
	}

	@Override
	public BigDecimal getIdProfessione() {
		return idProfessione;
	}

	public void setIdProfessione(BigDecimal idProfessione) {
		this.idProfessione = idProfessione;
	}

	@Override
	public BigDecimal getIdTutela() {
		return idTutela;
	}

	public void setIdTutela(BigDecimal idTutela) {
		this.idTutela = idTutela;
	}

	@Override
	public BigDecimal getIdTutelante() {
		return idTutelante;
	}

	public void setIdTutelante(BigDecimal idTutelante) {
		this.idTutelante = idTutelante;
	}

	@Override
	public List<SelectItem> getLstTipologiaFam() {
		
		if(lstTipologiaFam == null){
			lstTipologiaFam = new ArrayList<SelectItem>();
			lstTipologiaFam.add(new SelectItem(null, "--> scegli"));
			CeTBaseObject bo = new CeTBaseObject();
			fillEnte(bo);
			lstCsTbTipologiaFam = confService.getTipologieFamiliari(bo);
			if (lstCsTbTipologiaFam != null) {
				for (CsTbTipologiaFamiliare obj : lstCsTbTipologiaFam) {
					lstTipologiaFam.add(new SelectItem(obj.getId(), obj.getDescrizione()));
				}
			}		
		}
		
		return lstTipologiaFam;
	}

	public void setLstTipologiaFam(List<SelectItem> lstTipologiaFam) {
		this.lstTipologiaFam = lstTipologiaFam;
	}

	@Override
	public List<SelectItem> getLstInviante() {
		
		if(lstInviante == null){
			lstInviante = new ArrayList<SelectItem>();
			lstInviante.add(new SelectItem(null, "--> scegli"));
			CeTBaseObject bo = new CeTBaseObject();
			fillEnte(bo);
			List<CsOOrganizzazione> lst = confService.getOrganizzazioni(bo);
			if (lst != null) {
				for (CsOOrganizzazione obj : lst) {
					lstInviante.add(new SelectItem(obj.getId(), obj.getNome()));
				}
			}		
		}
		
		return lstInviante;
	}

	public void setLstInviante(List<SelectItem> lstInviante) {
		this.lstInviante = lstInviante;
	}

	@Override
	public List<SelectItem> getLstProblematiche() {
		
		if(lstProblematiche == null){
			lstProblematiche = new ArrayList<SelectItem>();
			lstProblematiche.add(new SelectItem(null, "--> scegli"));
			CeTBaseObject bo = new CeTBaseObject();
			fillEnte(bo);
			List<CsTbProblematica> lst = confService.getProblematiche(bo);
			if (lst != null) {
				for (CsTbProblematica obj : lst) {
					lstProblematiche.add(new SelectItem(obj.getId(), obj.getDescrizione()));
				}
			}		
		}
		
		return lstProblematiche;
	}

	public void setLstProblematiche(List<SelectItem> lstProblematiche) {
		this.lstProblematiche = lstProblematiche;
	}

	@Override
	public List<SelectItem> getLstStesuraRelPer() {
		
		if(lstStesuraRelPer == null){
			lstStesuraRelPer = new ArrayList<SelectItem>();
			lstStesuraRelPer.add(new SelectItem(null, "--> scegli"));
			CeTBaseObject bo = new CeTBaseObject();
			fillEnte(bo);
			List<CsTbStesuraRelazioniPer> lst = confService.getStesuraRelazioniPer(bo);
			if (lst != null) {
				for (CsTbStesuraRelazioniPer obj : lst) {
					lstStesuraRelPer.add(new SelectItem(obj.getId(), obj.getDescrizione()));
				}
			}
		}
		
		return lstStesuraRelPer;
	}

	public void setLstStesuraRelPer(List<SelectItem> lstStesuraRelPer) {
		this.lstStesuraRelPer = lstStesuraRelPer;
	}

	@Override
	public List<SelectItem> getLstTitoliStudio() {
		
		if(lstTitoliStudio == null){
			lstTitoliStudio = new ArrayList<SelectItem>();
			lstTitoliStudio.add(new SelectItem(null, "--> scegli"));
			CeTBaseObject bo = new CeTBaseObject();
			fillEnte(bo);
			List<CsTbTitoloStudio> lst = confService.getTitoliStudio(bo);
			if (lst != null) {
				for (CsTbTitoloStudio obj : lst) {
					lstTitoliStudio.add(new SelectItem(obj.getId(), obj.getDescrizione()));
				}
			}		
		}
		
		return lstTitoliStudio;
	}

	public void setLstTitoliStudio(List<SelectItem> lstTitoliStudio) {
		this.lstTitoliStudio = lstTitoliStudio;
	}

	@Override
	public List<SelectItem> getLstProfessioni() {
		
		if(lstProfessioni == null){
			lstOccupato=new ArrayList<Long>();
			lstProfessioni = new ArrayList<SelectItem>();
			lstProfessioni.add(new SelectItem(null, "--> scegli"));
			CeTBaseObject bo = new CeTBaseObject();
			fillEnte(bo);
			List<CsTbProfessione> lst = confService.getProfessioni(bo);
			if (lst != null) {
				for (CsTbProfessione obj : lst) {
					String desc=obj.getDescrizione();
					lstProfessioni.add(new SelectItem(obj.getId(), desc)); 
					if(!desc.equalsIgnoreCase("Disoccupato") && !desc.equalsIgnoreCase("Studente") && !desc.equalsIgnoreCase("Pensionato"))
						this.lstOccupato.add(obj.getId());
				}
			}		
		}
		
		return lstProfessioni;
	}

	public void setLstProfessioni(List<SelectItem> lstProfessioni) {
		this.lstProfessioni = lstProfessioni;
	}

	@Override
	public List<SelectItem> getLstTutele() {
		
		if(lstTutele == null){
			lstTutele = new ArrayList<SelectItem>();
			lstTutele.add(new SelectItem(null, "--> scegli"));
			CeTBaseObject bo = new CeTBaseObject();
			fillEnte(bo);
			List<CsTbTutela> lst = confService.getTutele(bo);
			if (lst != null) {
				for (CsTbTutela obj : lst) {
					lstTutele.add(new SelectItem(obj.getId(), obj.getDescrizione()));
				}
			}
		}
		
		return lstTutele;
	}

	public void setLstTutele(List<SelectItem> lstTutele) {
		this.lstTutele = lstTutele;
	}

	@Override
	public List<SelectItem> getLstTutelanti() {
		
		if(lstTutelanti == null){
			lstTutelanti = new ArrayList<SelectItem>();
			lstTutelanti.add(new SelectItem(null, "--> scegli"));
			CeTBaseObject bo = new CeTBaseObject();
			fillEnte(bo);
			List<CsTbTutelante> lst = confService.getTutelanti(bo);
			if (lst != null) {
				for (CsTbTutelante obj : lst) {
					lstTutelanti.add(new SelectItem(obj.getId(), obj.getDescrizione()));
				}
			}		
		}
		
		return lstTutelanti;
	}

	public void setLstTutelanti(List<SelectItem> lstTutelanti) {
		this.lstTutelanti = lstTutelanti;
	}

	@Override
	public List<CsTbTipologiaFamiliare> getLstCsTbTipologiaFam() {
		return lstCsTbTipologiaFam;
	}

	public void setLstCsTbTipologiaFam(
			List<CsTbTipologiaFamiliare> lstCsTbTipologiaFam) {
		this.lstCsTbTipologiaFam = lstCsTbTipologiaFam;
	}

	@Override
	public boolean isInCaricoNOA() {
		return inCaricoNOA;
	}

	public void setInCaricoNOA(boolean inCaricoNOA) {
		this.inCaricoNOA = inCaricoNOA;
	}

	@Override
	public boolean isInCaricoSERT() {
		return inCaricoSERT;
	}

	public void setInCaricoSERT(boolean inCaricoSERT) {
		this.inCaricoSERT = inCaricoSERT;
	}

	@Override
	public String getAutosufficienza() {
		return autosufficienza;
	}

	public void setAutosufficienza(String autosufficienza) {
		this.autosufficienza = autosufficienza;
	}

	@Override
	public BigDecimal getIdSettoreImpiego() {
		return idSettoreImpiego;
	}

	public void setIdSettoreImpiego(BigDecimal idSettoreImpiego) {
		this.idSettoreImpiego = idSettoreImpiego;
	}

	@Override
	public BigDecimal getIdTipoContratto() {
		return idTipoContratto;
	}

	public void setIdTipoContratto(BigDecimal idTipoContratto) {
		this.idTipoContratto = idTipoContratto;
	}

	@Override
	public List<SelectItem> getLstAutosufficienza() {
		return lstAutosufficienza;
	}

	public void setLstAutosufficienza(List<SelectItem> lstAutosufficienza) {
		this.lstAutosufficienza = lstAutosufficienza;
	}

	@Override
	public List<SelectItem> getLstSettoreImpiego() {
		
		if(lstSettoreImpiego == null){
			lstSettoreImpiego = new ArrayList<SelectItem>();
			lstSettoreImpiego.add(new SelectItem(null, "--> scegli"));
			CeTBaseObject bo = new CeTBaseObject();
			fillEnte(bo);
			List<CsTbSettoreImpiego> lst = confService.getSettoreImpiego(bo);
			if (lst != null) {
				for (CsTbSettoreImpiego obj : lst) {
					lstSettoreImpiego.add(new SelectItem(obj.getId(), obj.getDescrizione()));
				}
			}		
		}
		
		return lstSettoreImpiego;
	}

	public void setLstSettoreImpiego(List<SelectItem> lstSettoreImpiego) {
		this.lstSettoreImpiego = lstSettoreImpiego;
	}

	@Override
	public List<SelectItem> getLstTipoContratto() {
		if(lstTipoContratto == null){
			lstTipoContratto = new ArrayList<SelectItem>();
			lstTipoContratto.add(new SelectItem(null, "--> scegli"));
			CeTBaseObject bo = new CeTBaseObject();
			fillEnte(bo);
			List<CsTbTipoContratto> lst = confService.getTipoContratto(bo);
			if (lst != null) {
				for (CsTbTipoContratto obj : lst) {
					lstTipoContratto.add(new SelectItem(obj.getId(), obj.getDescrizione()));
				}
			}		
		}
		return lstTipoContratto;
	}

	public void setLstTipoContratto(List<SelectItem> lstTipoContratto) {
		this.lstTipoContratto = lstTipoContratto;
	}
	
	public String getPatologia() {
		return patologia;
	}

	public void setPatologia(String patologia) {
		this.patologia = patologia;
	}

	public String getPatologiaAltro() {
		return patologiaAltro;
	}

	public void setPatologiaAltro(String patologiaAltro) {
		this.patologiaAltro = patologiaAltro;
	}

	public boolean isOccupatoSelected(){
		
		boolean sel = false;
		if(this.idProfessione!=null){
			for(Long s : this.lstOccupato){
				if(s.longValue()==this.idProfessione.longValue())
					sel=true;
			}
		}
		return sel;
	}
}
