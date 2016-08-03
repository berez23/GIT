package it.webred.cs.csa.web.manbean.scheda.parenti;

import it.webred.cs.csa.ejb.client.AccessTableParentiGitSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.ComponenteDTO;
import it.webred.cs.csa.web.manbean.scheda.SchedaValiditaBaseBean;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsAComponente;
import it.webred.cs.data.model.CsAComponenteGit;
import it.webred.cs.data.model.CsAFamigliaGruppo;
import it.webred.cs.data.model.CsAFamigliaGruppoGit;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.jsf.bean.ValiditaCompBaseBean;
import it.webred.cs.jsf.interfaces.IDatiValiditaList;
import it.webred.ct.config.model.AmTabNazioni;
import it.webred.jsf.bean.ComuneBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class ParentiBean extends SchedaValiditaBaseBean implements IDatiValiditaList{

	private AccessTableParentiGitSessionBeanRemote parentiService = (AccessTableParentiGitSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableParentiGitSessionBean");
	
	private List<ComponenteDTO> lstComponentiGit;
	
	private int idxSelected;
	
	@Override
	public void initialize(Long sId) {
		
		super.initialize(sId);
		lstComponentiGit = null;
		
	}
	
	@Override
	public Object getTypeClass() {
		return new CsAFamigliaGruppo();
	}
	
	@Override
	public String getTypeComponent() {
		return "pnlParenti";
	}
	
	@Override
	public void nuovo() {
		
		Integer idx = getFirstActiveComponentIndex();
		if(idx != null){
			maxAttiviWarning("Nuovo elemento non disponibile");
			return;
		}
		
		ParentiComp comp = new ParentiComp();
		comp.setDataInizio(new Date());
		listaComponenti.add(0, comp);
		super.nuovo();
				
	}
	
	public void attivaParente() {

		Integer idx = getFirstActiveComponentIndex();
		
		if(idx == null){	
			nuovo();
			idx = getFirstActiveComponentIndex();
		}
		
		currentIndex = idx;
		ParentiComp pComp = (ParentiComp) listaComponenti.get(idx);
		pComp.getNuovoParenteBean().reset();
		
		pComp.setNuovo(true);
		pComp.setShowNewParente(true);
		pComp.setIdxSelected(0);
		
		CsAComponenteGit compGit = lstComponentiGit.get(idxSelected).getCompGit();
		
		//controllo se è già presente
		for(int i = 0; i < pComp.getLstParenti().size(); i++) {
			CsAComponente comp = pComp.getLstParenti().get(i);
			if(comp.getCsAAnagrafica().getCf().equals(compGit.getCf())
					|| (comp.getCsAAnagrafica().getCognome().equals(compGit.getCognome())
							&& comp.getCsAAnagrafica().getNome().equals(compGit.getNome()))) {
				pComp.setNuovo(false);
				pComp.setIdxSelected(i);
				pComp.loadModificaParente();
				break;
			}
		}
		
		//carico i dati sul popup di modifica parente
		NuovoParenteBean nuovoParente = pComp.getNuovoParenteBean();
		nuovoParente.setCellulare(compGit.getCell());
		nuovoParente.setCodFiscale(compGit.getCf());
		nuovoParente.setCittadinanza(compGit.getCittadinanza());
		nuovoParente.setCognome(compGit.getCognome());
		if(compGit.getComResCod() != null) {
			ComuneBean comuneRes = new ComuneBean(compGit.getComResCod(), compGit.getComResDes(),compGit.getProvResCod());
			nuovoParente.getComuneNazioneResidenzaBean().getComuneMan().setComune(comuneRes);
		}
		if(compGit.getComuneNascitaCod() != null) {
			ComuneBean comuneNas = new ComuneBean(compGit.getComuneNascitaCod(), compGit.getComuneNascitaDes(),compGit.getProvNascitaCod());
			nuovoParente.getComuneNazioneNascitaBean().getComuneMan().setComune(comuneNas);
		}
		if(compGit.getStatoNascitaCod() != null) {
			AmTabNazioni amTabNazioni = new AmTabNazioni();
			amTabNazioni.setCodice(compGit.getStatoNascitaCod());
			amTabNazioni.setNazione(compGit.getStatoNascitaDes());
			nuovoParente.getComuneNazioneNascitaBean().setValue(nuovoParente.getComuneNazioneNascitaBean().getNazioneValue());
			nuovoParente.getComuneNazioneNascitaBean().getNazioneMan().setNazione(amTabNazioni);
		}
		nuovoParente.setDataDecesso(compGit.getDataDecesso());
		nuovoParente.setDataNascita(compGit.getDataNascita());
		nuovoParente.setEmail(compGit.getEmail());
		nuovoParente.setIndirizzo(compGit.getIndirizzoRes());
		nuovoParente.setNome(compGit.getNome());
		nuovoParente.setNote(compGit.getNote());
		nuovoParente.setCivico(compGit.getNumCivRes());
		nuovoParente.setSesso(compGit.getSesso());
		nuovoParente.setTelefono(compGit.getTel());
		if(compGit.getCsTbTipoRapportoCon() != null)
			nuovoParente.setIdParentela(compGit.getCsTbTipoRapportoCon().getId());
		
		pComp.setNuovoParenteBean(nuovoParente);
				
		//quanto viene attivato resetto il flag attivazione
		compGit.setFlgSegnalazione("0");
		BaseDTO bo = new BaseDTO();
		fillEnte(bo);
		bo.setObj(compGit);
		parentiService.updateComponenteGit(bo);
		
		RequestContext.getCurrentInstance().addCallbackParam("canActivate", true);

	}
	
	private Integer getFirstActiveComponentIndex() {
		 
		if(listaComponenti != null) {
			for(int index = 0; index < listaComponenti.size(); index++) {
				ValiditaCompBaseBean comp = listaComponenti.get(index);
				if(comp.isAttivo()){
					return index;
				}
			}
		}
		
		return null;
	}
	
	public List<ComponenteDTO> getLstComponentiGit() {
		
		if(lstComponentiGit == null){
			lstComponentiGit = new ArrayList<ComponenteDTO>();
			BaseDTO bo = new BaseDTO();
			fillEnte(bo);
			bo.setObj(soggettoId);
			CsAFamigliaGruppoGit famigliaGit = parentiService.getFamigliaGruppoGit(bo);
			if(famigliaGit != null){
				
				Integer idx = getFirstActiveComponentIndex();
				ParentiComp pComp = null;
				if(idx == null){
					nuovo();
					idx = getFirstActiveComponentIndex();
				}
				if(idx != null)
					pComp = (ParentiComp) listaComponenti.get(idx);
				
				
				for(CsAComponenteGit compGit: famigliaGit.getCsAComponenteGits()){
					ComponenteDTO dto = new ComponenteDTO();
					dto.setCompGit(compGit);
					dto.setAttiva(true);
					//TODO:Verificare se il componente è già presente in cartella
					dto.setAttiva(!this.isComponenteAttivato(compGit,pComp));
					lstComponentiGit.add(dto);	
				}
			}
		}else {
			Integer idx = getFirstActiveComponentIndex();
			if(idx != null){
				ParentiComp pComp = (ParentiComp) listaComponenti.get(idx);
			}
		}
		
		return lstComponentiGit;
	}
	
	public boolean isComponenteAttivato(CsAComponenteGit compGit, ParentiComp pComp){
		boolean trovato = false;
		
		//controllo se è già presente
		for(int i = 0; i < pComp.getLstParenti().size(); i++) {
			CsAComponente comp = pComp.getLstParenti().get(i);
			if(comp.getCsAAnagrafica().getCf().equals(compGit.getCf())
					|| (comp.getCsAAnagrafica().getCognome().equals(compGit.getCognome())
							&& comp.getCsAAnagrafica().getNome().equals(compGit.getNome()))) {
				trovato = true;
				break;
			}
		}
		
		return trovato;
		
	}
	
	public void setLstComponentiGit(List<ComponenteDTO> lstComponentiGit) {
		this.lstComponentiGit = lstComponentiGit;
	}

	public int getIdxSelected() {
		return idxSelected;
	}

	public void setIdxSelected(int idxSelected) {
		this.idxSelected = idxSelected;
	}

	@Override
	public Object getCsFromComponente(Object obj) {
		
		ParentiComp comp = (ParentiComp) obj;
		
		BaseDTO dto = new BaseDTO();
		fillEnte(dto);		
		
		CsAFamigliaGruppo cs = new CsAFamigliaGruppo();
		if(comp.getId() != null){
			//update
			dto.setObj(comp.getId());
			cs = schedaService.getFamigliaGruppoById(dto);
		} else {
			//nuovo
			CsASoggetto sogg = new CsASoggetto();
			sogg.setAnagraficaId(soggettoId);
			cs.setCsASoggetto(sogg);
		}
		
		cs.setCsAComponentes(comp.getLstParenti());
		cs.getCsAComponentes().addAll(comp.getLstConoscenti());
		//TODO tipo
		
		cs.setDataInizioApp(comp.getDataInizio());
		if(comp.getDataInizio() == null)
			cs.setDataInizioApp(new Date());
		cs.setDataFineApp(comp.getDataFine());
		if(comp.getDataFine() == null)
			cs.setDataFineApp(DataModelCostanti.END_DATE);
		
		return cs;
		
	}

	@Override
	public ValiditaCompBaseBean getComponenteFromCs(Object obj) {
		
		CsAFamigliaGruppo cs = (CsAFamigliaGruppo) obj;
		ParentiComp comp = new ParentiComp();
		
		for(CsAComponente csComp: cs.getCsAComponentes()) {
			if(csComp.getCsTbTipoRapportoCon().getParente())
				comp.getLstParenti().add(csComp);
			else comp.getLstConoscenti().add(csComp);
		}
		//TODO tipo
		
		comp.setDataInizio(cs.getDataInizioApp());
		comp.setDataFine(cs.getDataFineApp());
		comp.setId(cs.getId());
		
		return comp;
	}

	@Override
	public boolean validaCs(ValiditaCompBaseBean comp) {
		
		
		
		ParentiComp pComp = (ParentiComp) comp;
		boolean ok = true;
		
		for(CsAComponente cs: pComp.getLstParenti()) {
			if(StringUtils.isBlank(cs.getCsAAnagrafica().getCognome())) {
				ok = false;
				addError("Lista parenti, " + cs.getCsAAnagrafica().getNome() + ":", "Cognome è un campo obbligatorio");
			}
			
			if(StringUtils.isBlank(cs.getCsAAnagrafica().getNome())) {
				ok = false;
				addError("Lista parenti, " + cs.getCsAAnagrafica().getCognome() + ":", "Nome è un campo obbligatorio");
			}
			
			if(StringUtils.isBlank(cs.getCsAAnagrafica().getSesso())) {
				ok = false;
				addError("Lista parenti, " + cs.getCsAAnagrafica().getCognome() + " " + cs.getCsAAnagrafica().getNome() + ":",
						"Sesso è un campo obbligatorio");
			}
			
			if(cs.getCsTbTipoRapportoCon() == null || cs.getCsTbTipoRapportoCon().getId() == null) {
				ok = false;
				addError("Lista parenti, " + cs.getCsAAnagrafica().getCognome() + " " + cs.getCsAAnagrafica().getNome() + ":",
						"Parentela è un campo obbligatorio");
			}
			
			if(cs.getConvivente() == null) {
				ok = false;
				addError("Lista parenti, " + cs.getCsAAnagrafica().getCognome() + " " + cs.getCsAAnagrafica().getNome() + ":",
						"Convivente è un campo obbligatorio");
			}
			
			if(cs.getCsTbContatto() == null || cs.getCsTbContatto().getId() == null) {
				ok = false;
				addError("Lista parenti, " + cs.getCsAAnagrafica().getCognome() + " " + cs.getCsAAnagrafica().getNome() + ":",
						"Contatto è un campo obbligatorio");
			}
			
			if(cs.getCsTbPotesta() == null || cs.getCsTbPotesta().getId() == null) {
				ok = false;
				addError("Lista parenti, " + cs.getCsAAnagrafica().getCognome() + " " + cs.getCsAAnagrafica().getNome() + ":",
						"Potestà è un campo obbligatorio");
			}
		}
		
		RequestContext.getCurrentInstance().addCallbackParam("canClose", ok);
		return ok;
	}
	
	@Override
	public boolean validaComponenti() {
		boolean ok = true;
		
		int count = 0;
		for(ValiditaCompBaseBean comp: listaComponenti) {
			if(comp.isAttivo()){
				count++;
			}
		}
		
		if(count > 1) {
			ok = false;
			maxAttiviWarning("Salvataggio non disponibile");
		}
		
		return ok;
	}
	
	private void maxAttiviWarning(String msg) {
		addWarning(msg, "E' possibile avere una sola situazione familiare attiva alla volta");
	}
	
}
