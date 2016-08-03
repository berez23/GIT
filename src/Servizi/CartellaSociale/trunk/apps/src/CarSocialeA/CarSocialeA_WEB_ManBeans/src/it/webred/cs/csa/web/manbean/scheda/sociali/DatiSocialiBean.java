package it.webred.cs.csa.web.manbean.scheda.sociali;

import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.web.manbean.scheda.SchedaValiditaBaseBean;
import it.webred.cs.csa.web.manbean.scheda.disabilita.DatiDisabilitaComp;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsADatiSociali;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.jsf.bean.ValiditaCompBaseBean;
import it.webred.cs.jsf.interfaces.IDatiValiditaList;

import java.math.BigDecimal;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class DatiSocialiBean extends SchedaValiditaBaseBean implements IDatiValiditaList{
	
	@Override
	public Object getTypeClass() {
		return new CsADatiSociali();
	}
	
	@Override
	public String getTypeComponent() {
		return "pnlDatiSociali";
	}
	
	@Override
	public void nuovo() {
		
		DatiSocialiComp comp = new DatiSocialiComp();
		comp.setDataInizio(new Date());
		listaComponenti.add(0, comp);
		super.nuovo();

	}
	
	@Override
	public CsADatiSociali getCsFromComponente(Object obj) {
		
		DatiSocialiComp comp = (DatiSocialiComp) obj;
		
		BaseDTO dto = new BaseDTO();
		fillEnte(dto);		
		
		CsADatiSociali cs = new CsADatiSociali();
		if(comp.getId() != null){
			//update
			dto.setObj(comp.getId());
			cs = schedaService.getDatiSocialiById(dto);
		} else {
			//nuovo
			CsASoggetto sogg = new CsASoggetto();
			sogg.setAnagraficaId(soggettoId);
			cs.setCsASoggetto(sogg);
		}
		
		cs.setCps(comp.isInCaricoCPS()?"1":"0");
		cs.setNoa(comp.isInCaricoNOA()?"1":"0");
		cs.setSert(comp.isInCaricoSERT()?"1":"0");
		cs.setAutosufficienza(comp.getAutosufficienza());
		cs.setPatologia(comp.getPatologia());
		if(comp.getPatologia().equals("Altro") && comp.getPatologiaAltro()!=null)
			cs.setPatologia("Altro:"+comp.getPatologiaAltro());
		cs.setInterventiSuNucleo(comp.isInterventiNucleo()?"1":"0");
		cs.setInvianteId(comp.getIdInviante());
		cs.setInviatoAId(comp.getIdInviatoA());
		if(comp.getnMinori() != null)
			cs.setnMinori(new BigDecimal(comp.getnMinori()));
		cs.setProblematicaId(comp.getIdProblematica());
		cs.setProfessioneId(comp.getIdProfessione());
		cs.setStesuraRelazioniPerId(comp.getIdStesuraRelPer());
		cs.setTipoInterventi(comp.getInterventiTipo());
		cs.setTipologiaFamiliareId(comp.getIdTipologiaFam());
		cs.setTitoloStudioId(comp.getIdTitoloStudio());
		cs.setSettImpiegoId(comp.getIdSettoreImpiego());
		cs.setTipoContrattoId(comp.getIdTipoContratto());
		cs.setTutelaId(comp.getIdTutela());
		cs.setTutelanteId(comp.getIdTutelante());
		
		cs.setDataInizioApp(comp.getDataInizio());
		if(comp.getDataInizio() == null)
			cs.setDataInizioApp(new Date());
		cs.setDataFineApp(comp.getDataFine());
		if(comp.getDataFine() == null)
			cs.setDataFineApp(DataModelCostanti.END_DATE);
		
		return cs;
		
	}
	
	@Override
	public DatiSocialiComp getComponenteFromCs(Object obj) {
		
		CsADatiSociali cs = (CsADatiSociali) obj;
		
		DatiSocialiComp comp = new DatiSocialiComp();
		comp.setIdInviante(cs.getInvianteId());
		comp.setIdInviatoA(cs.getInviatoAId());
		comp.setIdProblematica(cs.getProblematicaId());
		comp.setIdProfessione(cs.getProfessioneId());
		comp.setIdStesuraRelPer(cs.getStesuraRelazioniPerId());
		comp.setIdTipologiaFam(cs.getTipologiaFamiliareId());
		comp.setIdTitoloStudio(cs.getTitoloStudioId());
		comp.setIdTipoContratto(cs.getTipoContrattoId());
		comp.setIdSettoreImpiego(cs.getSettImpiegoId());
		comp.setIdTutela(cs.getTutelaId());
		comp.setIdTutelante(cs.getTutelanteId());
		comp.setInCaricoCPS("1".equals(cs.getCps()));
		comp.setInCaricoNOA("1".equals(cs.getNoa()));
		comp.setInCaricoSERT("1".equals(cs.getSert()));
		comp.setAutosufficienza(cs.getAutosufficienza());
		if(cs.getPatologia()!=null && cs.getPatologia().startsWith("Altro:")){
			comp.setPatologia("Altro");
			comp.setPatologiaAltro(cs.getPatologia().replaceFirst("Altro:", ""));
		}else
			comp.setPatologia(cs.getPatologia());
		comp.setInterventiNucleo("1".equals(cs.getInterventiSuNucleo()));
		comp.setInterventiTipo(cs.getTipoInterventi());
		if(cs.getnMinori() != null)
			comp.setnMinori(cs.getnMinori().intValue());
		
		comp.setDataInizio(cs.getDataInizioApp());
		comp.setDataFine(cs.getDataFineApp());
		comp.setId(cs.getId());
		
		return comp;
		
	}
	
	@Override
	public boolean validaComponenti() {
		boolean ok = true;
		
		for(ValiditaCompBaseBean comp: listaComponenti) {
			DatiSocialiComp disComp = (DatiSocialiComp) comp;
			
			if(disComp.getIdTipologiaFam() == null || disComp.getIdTipologiaFam().intValue() == 0) {
				ok = false;
				addError("Il campo Tipologia familiare è obbligatorio", "");
			}
			
			if(disComp.getIdProblematica() == null || disComp.getIdProblematica().intValue() == 0) {
				ok = false;
				addError("Il campo Problematica è obbligatorio", "");
			}
			
			if(disComp.getIdStesuraRelPer() == null || disComp.getIdStesuraRelPer().intValue() == 0) {
				ok = false;
				addError("Il campo Stesura relazioni per è obbligatorio", "");
			}
			
			if(disComp.getIdTitoloStudio() == null || disComp.getIdTitoloStudio().intValue() == 0) {
				ok = false;
				addError("Il campo Titolo di studio è obbligatorio", "");
			}
			
			if(disComp.getIdProfessione() == null || disComp.getIdProfessione().intValue() == 0) {
				ok = false;
				addError("Il campo Professione è obbligatorio", "");
			}
		}
		
		return ok;
		
	}
	
}
