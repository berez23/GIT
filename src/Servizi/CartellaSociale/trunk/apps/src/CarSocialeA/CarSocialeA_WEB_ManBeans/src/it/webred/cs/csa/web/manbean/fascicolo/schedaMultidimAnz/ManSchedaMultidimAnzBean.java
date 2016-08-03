package it.webred.cs.csa.web.manbean.fascicolo.schedaMultidimAnz;

import it.webred.amprofiler.ejb.anagrafica.AnagraficaService;
import it.webred.amprofiler.model.AmAnagrafica;
import it.webred.classfactory.WebredClassFactory;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.csa.ejb.dto.SchedaBarthelDTO;
import it.webred.cs.csa.web.manbean.fascicolo.FascicoloCompBaseBean;
import it.webred.cs.csa.web.manbean.fascicolo.schedaMultidimAnz.barthel.ISchedaBarthel;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsAComponente;
import it.webred.cs.data.model.CsDClob;
import it.webred.cs.data.model.CsDDiario;
import it.webred.cs.data.model.CsDValutazione;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsTbTipoDiario;
import it.webred.cs.jsf.interfaces.ISchedaMultidimAnz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ManSchedaMultidimAnzBean extends FascicoloCompBaseBean implements ISchedaMultidimAnz {

	private AnagraficaService anagraficaService = (AnagraficaService) getEjb("AmProfiler", "AmProfilerEjb", "AnagraficaServiceBean");
	
	private List<CsDValutazione> listaSchedeMultidims;
	private CsDValutazione selectedSchedaMultidimAnz;
	private CsDValutazione newSchedaMultidimAnz = new CsDValutazione();
	
	private List<String> prestazionis;
	private List<String> dachis;
	private List<String> prestDachis;
	
	private SchedaMultidimAnzBean schedaMultidimBean;
	
	private String infoValReteSoc;
	private String infoSalFisic;
	private String infoAdAbit;
	private String infoUbAbit;
	private String infoValFam;
	
	private Date dataValutazione;
	private String descrizioneScheda;
	
	private List<CsAComponente> famConvComponentes;
	private List<CsAComponente> famNonConvComponentes;
	private List<CsAComponente> famAltriComponentes;
	private List<CsAComponente> famComponentes;
	private List<CsAComponente> selectedFamComponentes;
	private List<AnagraficaMultidimAnzBean> selectedfamAnaConvs;
	private List<AnagraficaMultidimAnzBean> selectedfamAnaNonConvs;
	private List<AnagraficaMultidimAnzBean> selectedfamAnaAltris;
	
	private boolean removeAnaConvButton;
	private boolean addAnaCorrButton;
	private boolean removeAnaNonConvButton;
	private boolean addAnaNonCorrButton;
	private boolean removeAnaAltriButton;
	private boolean addAnaAltriButton;
	private boolean apriAnaConvButton;
	private boolean apriAnaNonConvButton;
	private boolean apriAltrifamButton;
	private boolean isConvivente;
	private boolean isParente;
	
	private boolean newSchedaMultidimAnzRendered;
	
	@Override
	public void initializeData() {
		
		BaseDTO dto = new BaseDTO();
		fillEnte(dto);
		
		if(csASoggetto != null){
			dto.setObj(csASoggetto.getAnagraficaId());
			newSchedaMultidimAnzRendered = true;
		}
		else{
			dto.setObj(null);
			newSchedaMultidimAnzRendered = false;
		}
			
		listaSchedeMultidims = diarioService.getSchedeMultidimAnzs(dto);
		
		for(CsDValutazione val: listaSchedeMultidims) {
			AmAnagrafica amAnaOperatore = anagraficaService.findAnagraficaByUserName(val.getCsDDiario().getCsOOperatoreSettore().getCsOOperatore().getUsername());
			if(amAnaOperatore != null) {
				String denom = amAnaOperatore.getCognome() + " " + amAnaOperatore.getNome();
				val.getCsDDiario().getCsOOperatoreSettore().getCsOOperatore().setDenominazione(denom);
			}
		}
	}
	
	private void initializeSchedaMultidimAnzDialog(Date dataVal, String descScheda, SchedaMultidimAnzBean schedaMultidim) throws Exception{
		// Initialize Info field
		infoValReteSoc = "Buone: Ha rapporti frequenti - quotidiani o settimanali - e positivi con i vari soggetti. <br /> <br /> Limitate: Ha rapporti scarsi, casuali, saltuari - 1 volta al mese o meno con i vari soggetti. <br /> <br /> Insufficienti: Rapporti inesistenti con i vari soggetti. <br /> <br /> Grave: Sistema familiare multiproblematico, forti e persistenti conflitti interne. <br /> <br />";
		infoSalFisic = "Buono: Buon livello di salute fisica, assenza di malattie significative.<br /> <br /> Precario: Malattie croniche non invalidanti che consentono una vita 'normale' e permettono all'anziano di mantenere un livello accettabile di autonomia.<br /> <br /> Compromesso: Malattie croniche parzialmente invalidanti che richiedono cure continue e controlli costanti e limitano l'autonomia dell'individuo.<br /> <br /> Gravemente compromesso: Malattie gravemente invalidanti che richiedono un'assistenza continua, allettamento, perdita totale dell'autonomia.<br /> <br />";
		infoAdAbit = "Buona: Condizioni igienico-sanitarie adeguate, riscaldamento adeguato, assenza di barriere architettoniche. <br /> <br /> Sufficiente: Condizioni igienico-sanitarie adeguate, riscaldamento adeguato, presenza di barriere architettoniche e/o disposizione dei locali tale da rendere difficile la fruizione degli ambienti e l'accesso/l'uscita dall'abitazione. <br /> <br /> Insufficiente: Condizioni igienico- sanitarie, mancanza di servizi igienici, riscaldamento inadeguato, barriere tali da pregiudicare l'incolumità dell'anziano, assenza di telefono. <br /> <br />";
		infoUbAbit = "<b>Vicino a servizi</b>: In prossimità dei negozi, chiesa, uffici pubblici, ...<br /><b>Servizi non in prossimità</b>: Non in prossimità dei servizi/negozi ma ad una distanza tale da renderne possibile l'accesso muovendosi a piedi.<br /><b>Lontananza dai servizi</b>: Lontana da servizi/negozi e ad una distanza tale da renderne l'impossibile accesso muovendosi a piedi, lontana da altre abitazioni.<br/>";
		infoValFam = "Buona: Rapporti quotidiani o settimanali - e positivi - con parenti che garantiscono un aiuto costante. <br /> <br /> Limitata: Ha rapporti scarsi con parenti che garantiscono un aiuto solo in situazione di emergenza.<br /> <br /> Insufficiente: Inesistenti o conflittuali rapporti con la parentela.<br /> <br /> Grave: Sistema familiare multiproblematico, forti e persistenti conflitti interne.";

		// Initialize Matrix fileds in Mappe Risorse
		prestazionis = new ArrayList<String>();
		prestazionis.add("Pasti");
		prestazionis.add("Igiene personale");
		prestazionis.add("Lavanderia");
		prestazionis.add("Igiene ambientale");
		prestazionis.add("Gestione economica");
		prestazionis.add("Assunzione farmaci");
		prestazionis.add("Commissioni/spesa");
		prestazionis.add("Accompagnamento/Visite mediche/esami");
		prestazionis.add("Compagnia");
	    
		dachis = new ArrayList<String>();
	    dachis.add("Anziano");
	    dachis.add("Familiari conviventi");
	    dachis.add("Familiari non conviventi");
	    dachis.add("Personale retribuito");
	    dachis.add("Vicini amici volontari");
	    dachis.add("Servizi");
	    dachis.add("Nessuno");
	    
	    prestDachis = new ArrayList<String>();
	    
	    for (String item : prestazionis) {
			for (String it : dachis) {
				prestDachis.add(item + '_' + it);
			}
		}
	    				
		BaseDTO anaFamCurrDto = new BaseDTO();
		fillEnte(anaFamCurrDto);
		
		if (csASoggetto != null) {
			anaFamCurrDto.setObj(csASoggetto.getAnagraficaId());
			List<CsAComponente> famAllComponentes = diarioService.getFamigliaGruppoCorr(anaFamCurrDto).getCsAComponentes();

			apriAnaConvButton = false;
			apriAnaNonConvButton = false;
			apriAltrifamButton = false;
			if(famAllComponentes != null) {
				for (CsAComponente it : famAllComponentes) {
					if (it.getCsTbTipoRapportoCon().getParente()) {
						if (it.getConvivente()) {
							famConvComponentes = new ArrayList<CsAComponente>();
							famConvComponentes.add(it);
							apriAnaConvButton = apriAnaConvButton || true;
							apriAnaNonConvButton = apriAnaNonConvButton || false;
							apriAltrifamButton = apriAltrifamButton || false;
						} else {
							famNonConvComponentes = new ArrayList<CsAComponente>();
							famNonConvComponentes.add(it);
							apriAnaConvButton = apriAnaConvButton || false;
							apriAnaNonConvButton = apriAnaNonConvButton || true;
							apriAltrifamButton = apriAltrifamButton || false;
						}
					} else {
						famAltriComponentes = new ArrayList<CsAComponente>();
						famAltriComponentes.add(it);
						apriAnaConvButton = apriAnaConvButton || false;
						apriAnaNonConvButton = apriAnaNonConvButton || false;
						apriAltrifamButton = apriAltrifamButton || true;
					}
				}
			}
		}
		
		dataValutazione = dataVal;
		descrizioneScheda = descScheda;
		schedaMultidimBean = schedaMultidim;
		removeAnaConvButton = true;
		addAnaCorrButton = true;
		removeAnaNonConvButton = true;
		addAnaNonCorrButton = true;
		removeAnaAltriButton = true;
		addAnaAltriButton = true;
	}
	
	public void loadSchedaMultidimAnzData() {
		String jsonObj  = selectedSchedaMultidimAnz.getCsDDiario().getCsDClob().getDatiClob();
		ObjectMapper objectMapper = new ObjectMapper();
		
		try{
		
			initializeSchedaMultidimAnzDialog(selectedSchedaMultidimAnz.getDataValutazione(), selectedSchedaMultidimAnz.getDescrizioneScheda(), objectMapper.readValue(jsonObj, SchedaMultidimAnzBean.class));
			
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
	}

	// Start Methods for Famiglia Anagrafica
	@Override
	public void loadAnagConv() {
		
		try{
		
			// Initialize famComponentes with db data
			famComponentes = new ArrayList<CsAComponente>();
			if(famConvComponentes != null) {
				famComponentes.addAll(famConvComponentes);
				removeAnaConvButton = true;
				addAnaCorrButton = true; 
				isConvivente = true;
				isParente = true;
				
				if(schedaMultidimBean.famAnaConvs.size() > 0){
					for (CsAComponente it : famComponentes) {
						for (AnagraficaMultidimAnzBean item : schedaMultidimBean.famAnaConvs) {
							if(it.getCsAAnagrafica().getId().equals(item.getId()))
								famComponentes.remove(it);
						}
						break;
					}
				}
			}
		
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
	}

	@Override
	public void loadAnagNonConv() {
		
		try{
		
			// Initialize famComponentes with db data
			famComponentes = new ArrayList<CsAComponente>();
			if(famNonConvComponentes != null) {
				famComponentes.addAll(famNonConvComponentes);
				removeAnaNonConvButton = true;
				addAnaCorrButton = true;
				isConvivente = false;
				isParente = true;
				
				if(schedaMultidimBean.famAnaNonConvs.size() > 0){
					for (CsAComponente it : famComponentes) {
						for (AnagraficaMultidimAnzBean item : schedaMultidimBean.famAnaNonConvs) {
							if(it.getCsAAnagrafica().getId().equals(item.getId()))
								famComponentes.remove(it);
						}
						break;
					}
				}
			}
		
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public void loadAnagAltri() {
		
		try {
		
			// Initialize famComponentes with db data
			famComponentes = new ArrayList<CsAComponente>();
			if(famAltriComponentes != null) {
				famComponentes.addAll(famAltriComponentes);
				addAnaCorrButton = true;
				removeAnaAltriButton = true;
				isParente = false;
				
				if(schedaMultidimBean.famAnaAltris.size() > 0){
					for (CsAComponente it : famComponentes) {
						for (AnagraficaMultidimAnzBean item : schedaMultidimBean.famAnaAltris) {
							if(it.getCsAAnagrafica().getId().equals(item.getId()))
								famComponentes.remove(it);
						}
						break;
					}
				}
			}
		
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public void removeAnaConv() {
		if(selectedfamAnaConvs.size() > 0){
			for (AnagraficaMultidimAnzBean it : schedaMultidimBean.famAnaConvs){
				for (AnagraficaMultidimAnzBean item : selectedfamAnaConvs) {
					if(it.getId().equals(item.getId()))
						schedaMultidimBean.famAnaConvs.remove(it);
				}
				
				if(schedaMultidimBean.famAnaConvs.size() == 0)
					removeAnaConvButton = true;
				break;
			}	
		}
		else{
			removeAnaConvButton = true;
		}
	}

	@Override
	public void removeAnaNonConv(){
		if(selectedfamAnaNonConvs.size() > 0){
			for (AnagraficaMultidimAnzBean it : schedaMultidimBean.famAnaNonConvs){
				for (AnagraficaMultidimAnzBean item : selectedfamAnaNonConvs) {
					if(it.getId().equals(item.getId()))
						schedaMultidimBean.famAnaNonConvs.remove(it);
				}
				if(schedaMultidimBean.famAnaNonConvs.size() == 0)
					removeAnaConvButton = true;
				break;
			}	
		}
		else{
			removeAnaConvButton = true;
		}
	}
	
	@Override
	public void removeAnaAltri(){
		if(selectedfamAnaAltris.size() > 0){
			for (AnagraficaMultidimAnzBean it : schedaMultidimBean.famAnaAltris){
				for (AnagraficaMultidimAnzBean item : selectedfamAnaAltris) {
					if(it.getId().equals(item.getId()))
						schedaMultidimBean.famAnaAltris.remove(it);
				}
				if(schedaMultidimBean.famAnaAltris.size() == 0)
					removeAnaAltriButton = true;
				break;
			}	
		}
		else{
			removeAnaAltriButton = true;
		}
	}
	
	@Override
	public void addAnaCorr(){
		if(selectedFamComponentes.size() > 0){
			ArrayList<AnagraficaMultidimAnzBean> listFamComps = new ArrayList<AnagraficaMultidimAnzBean>();
			
			for (CsAComponente item : selectedFamComponentes) {
				String disponibilita = null;
				if(item.getCsTbDisponibilita() != null)
					disponibilita = item.getCsTbDisponibilita().getDescrizione();
				listFamComps.add(new AnagraficaMultidimAnzBean(item.getCsAAnagrafica().getId(), item.getCsAAnagrafica().getCognome(), item.getCsAAnagrafica().getNome(), item.getIndirizzoRes(), item.getComResDes(), item.getCsTbTipoRapportoCon().getDescrizione(), item.getCsAAnagrafica().getTel(), disponibilita));
			}
			
			if (isParente) {
				if (isConvivente == true)
					schedaMultidimBean.famAnaConvs = listFamComps;
				else
					schedaMultidimBean.famAnaNonConvs = listFamComps;
			} else {
				schedaMultidimBean.famAnaAltris = listFamComps;
			}
		}
	}
	
	@Override
	public void onRowSelectFamAnaConv(SelectEvent event) {
		removeAnaConvButton = false;
    }
 
    @Override
	public void onRowUnselectFamAnaConv(UnselectEvent event) {
    	removeAnaConvButton = true;
    }
	
    @Override
	public void onRowSelectFamAnaNonConv(SelectEvent event) {
		removeAnaNonConvButton = false;
    }
 
    @Override
	public void onRowUnselectFamAnaNonConv(UnselectEvent event) {
    	removeAnaNonConvButton = true;
    }
    
    @Override
	public void onRowSelectFamAnaCorr(SelectEvent event) {
		addAnaCorrButton = false;
    }
 
    @Override
	public void onRowUnselectFamAnaCorr(UnselectEvent event) {
        addAnaCorrButton = true;
    }
    
    @Override
	public void onRowSelectFamAnaNonCorr(SelectEvent event) { 
		addAnaNonCorrButton = false;
    }
 
    @Override
	public void onRowUnselectFamAnaNonCorr(UnselectEvent event) {
        addAnaNonCorrButton = true;
    }
    
    @Override
	public void onRowSelectFamAnaAltri(SelectEvent event) { 
    	removeAnaAltriButton = false;
    }
 
    @Override
	public void onRowUnselectFamAnaAltri(UnselectEvent event) {
    	removeAnaAltriButton = true;
    }
	// End Methods for Famiglia Anagrafica
    
	@Override
	public void saveSchedaMultidimAnzDialog() {
					
		try {
		
			if(!validaSchedaMultidimanz()) {
				RequestContext.getCurrentInstance().addCallbackParam("saved", false);
				return;
			}
			
			CsDValutazione schedaMultidimAnz = selectedSchedaMultidimAnz;
			
			BaseDTO schedaMultidimAnzDto = new BaseDTO();
			fillEnte(schedaMultidimAnzDto);
			
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonObj = objectMapper.writeValueAsString(schedaMultidimBean);
			
			BaseDTO clobDTO = new BaseDTO();
			fillEnte(clobDTO);
			String username = clobDTO.getUserId();
			
			// new schedaMultidimAnz
			if(schedaMultidimAnz == null){
				schedaMultidimAnz = new CsDValutazione();
				
				IterDTO itDto = new IterDTO();
				fillEnte(itDto);
				itDto.setIdCaso(csASoggetto.getCsACaso().getId());
	
				CsACaso caso = casoService.findCasoById(itDto);
	
				BaseDTO tipoDiarioIdDTO = new BaseDTO();
				fillEnte(tipoDiarioIdDTO);
	
				long TipoDiarioId = DataModelCostanti.TipoDiario.VALUTAZIONE_MDS_ID;
				tipoDiarioIdDTO.setObj(TipoDiarioId);
	
				CsTbTipoDiario tipoDiario = diarioService.findTipoDiarioById(tipoDiarioIdDTO);
				CsOOperatoreSettore opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
				
				CsDClob jsonClob = new CsDClob();
				jsonClob.setDatiClob(jsonObj);
				
				clobDTO.setObj(jsonClob);
				jsonClob = diarioService.createClob(clobDTO);
				
				CsDDiario diario = new CsDDiario();
				diario.setCsACaso(caso);
				diario.setCsTbTipoDiario(tipoDiario);
				diario.setCsOOperatoreSettore(opSettore);
				diario.setCsDClob(jsonClob);
				
				schedaMultidimAnz.setUserIns(username);
				schedaMultidimAnz.setDtIns(new Date());
				schedaMultidimAnz.setDataValutazione(dataValutazione);
				//TODO: Versione Scheda 
				schedaMultidimAnz.setVersioneScheda("1");
				schedaMultidimAnz.setDescrizioneScheda(descrizioneScheda);
				schedaMultidimAnz.setCsDDiario(diario);
				schedaMultidimAnz.setDiarioId(diario.getId());
				
				schedaMultidimAnzDto.setObj(schedaMultidimAnz);
				
				diarioService.saveDiarioChild(schedaMultidimAnzDto);
			}
			else{
				schedaMultidimAnz.setUsrMod(username);
				schedaMultidimAnz.setDtMod(new Date());
				schedaMultidimAnz.setDataValutazione(dataValutazione);
				schedaMultidimAnz.setDescrizioneScheda(descrizioneScheda);
				
				//Update Clob with json
				schedaMultidimAnz.getDiarioId();
				
				CsDClob clob = schedaMultidimAnz.getCsDDiario().getCsDClob();
				clob.setDatiClob(jsonObj);
				clobDTO.setObj(clob);
				diarioService.updateClob(clobDTO);
				
				schedaMultidimAnzDto.setObj(schedaMultidimAnz);
				diarioService.updateSchedaMultidimAnz(schedaMultidimAnzDto);
			}
			initializeData();
		
		addInfoFromProperties("salva.ok");
		RequestContext.getCurrentInstance().addCallbackParam("saved", true);
		
		} catch (Exception e) {
			addErrorFromProperties("salva.error");
			logger.error(e.getMessage(),e);
		}
	}
	
	private boolean validaSchedaMultidimanz() {
		
		boolean ok = true;

		if(!schedaMultidimBean.isViveAltri()
				&& !schedaMultidimBean.isViveConiuge()
				&& !schedaMultidimBean.isViveFamiliari()
				&& !schedaMultidimBean.isViveFigli()
				&& !schedaMultidimBean.isViveSolo()) {
			addError("Rete familiare è un campo obbligatorio", "Tab Rete familiare");
			ok = false;
		}
		
		if(schedaMultidimBean.getValFamRating().intValue() == 0) {
			addError("Sintesi valutazione rete familiare è un campo obbligatorio", "Tab Rete familiare");
			ok = false;
		}
		
		if(schedaMultidimBean.getRelAltriSogg() == null || "".equals(schedaMultidimBean.getRelAltriSogg())) {
			addError("Relazioni con altri soggetti è un campo obbligatorio", "Tab Rete sociale");
			ok = false;
		}
		
		if(schedaMultidimBean.getRelAltriSoggRetr() == null || "".equals(schedaMultidimBean.getRelAltriSoggRetr())) {
			addError("Relazioni con altri soggetti retribuiti è un campo obbligatorio", "Tab Rete sociale");
			ok = false;
		}
		
		return ok;
	}
	
	@Override
	public List<CsDValutazione> getListaSchedeMultidims() {
		return listaSchedeMultidims;
	}

	public void setListaSchedeMultidims(List<CsDValutazione> listaSchedeMultidims) {
		this.listaSchedeMultidims = listaSchedeMultidims;
	}

	public SchedaMultidimAnzBean getSchedaMultidimBean() {
		return schedaMultidimBean;
	}

	public void setSchedaMultidimBean(SchedaMultidimAnzBean schedaMultidimBean) {
		this.schedaMultidimBean = schedaMultidimBean;
	}
	
	@Override
	public CsDValutazione getSelectedSchedaMultidimAnz() {
		return selectedSchedaMultidimAnz;
	}

	public void setSelectedSchedaMultidimAnz(CsDValutazione selectedSchedaMultidimAnz) throws Exception {
		this.selectedSchedaMultidimAnz = selectedSchedaMultidimAnz;
		loadSchedaMultidimAnzData();
	}

	@Override
	public CsDValutazione getNewSchedaMultidimAnz() {
		return newSchedaMultidimAnz;
	}

	public void setNewSchedaMultidimAnz() throws Exception {
		this.newSchedaMultidimAnz = new CsDValutazione();
		selectedSchedaMultidimAnz = null;
		initializeSchedaMultidimAnzDialog(new Date(), "", new SchedaMultidimAnzBean());
	}

	@Override
	public List<String> getPrestazionis() {
		return prestazionis;
	}

	@Override
	public List<String> getDachis() {
		return dachis;
	}

	@Override
	public List<String> getPrestDachis() {
		return prestDachis;
	}

	@Override
	public String getInfoValReteSoc() {
		return infoValReteSoc;
	}

	@Override
	public String getInfoSalFisic() {
		return infoSalFisic;
	}

	@Override
	public String getInfoAdAbit() {
		return infoAdAbit;
	}

	@Override
	public String getInfoUbAbit() {
		return infoUbAbit;
	}

	@Override
	public String getInfoValFam() {
		return infoValFam;
	}
	
	@Override
	public List<CsAComponente> getFamComponentes() {
		return famComponentes;
	}

	@Override
	public List<CsAComponente> getSelectedFamComponentes() {
		return selectedFamComponentes;
	}

	public void setSelectedFamComponentes(List<CsAComponente> selectedFamComponentes) {
		this.selectedFamComponentes = selectedFamComponentes;
	}

	public List<AnagraficaMultidimAnzBean> getSelectedfamAnaConvs() {
		return selectedfamAnaConvs;
	}

	public void setSelectedfamAnaConvs(List<AnagraficaMultidimAnzBean> selectedfamAnaConvs) {
		this.selectedfamAnaConvs = selectedfamAnaConvs;
	}

	public List<AnagraficaMultidimAnzBean> getSelectedfamAnaNonConvs() {
		return selectedfamAnaNonConvs;
	}

	public void setSelectedfamAnaNonConvs(List<AnagraficaMultidimAnzBean> selectedfamAnaNonConvs) {
		this.selectedfamAnaNonConvs = selectedfamAnaNonConvs;
	}

	public List<AnagraficaMultidimAnzBean> getSelectedfamAnaAltris() {
		return selectedfamAnaAltris;
	}

	public void setSelectedfamAnaAltris(List<AnagraficaMultidimAnzBean> selectedfamAnaAltris) {
		this.selectedfamAnaAltris = selectedfamAnaAltris;
	}

	@Override
	public Date getDataValutazione() {
		return dataValutazione;
	}

	public void setDataValutazione(Date dataValutazione) {
		this.dataValutazione = dataValutazione;
	}
	
	@Override
	public String getDescrizioneScheda() {
		return descrizioneScheda;
	}

	public void setDescrizioneScheda(String descrizioneScheda) {
		this.descrizioneScheda = descrizioneScheda;
	}

	@Override
	public boolean isRemoveAnaConvButton() {
		return removeAnaConvButton;
	}

	@Override
	public boolean isAddAnaCorrButton() {
		return addAnaCorrButton;
	}

	@Override
	public boolean isRemoveAnaNonConvButton() {
		return removeAnaNonConvButton;
	}

	@Override
	public boolean isAddAnaNonCorrButton() {
		return addAnaNonCorrButton;
	}

	@Override
	public boolean isApriAnaConvButton() {
		return apriAnaConvButton;
	}

	@Override
	public boolean isApriAnaNonConvButton() {
		return apriAnaNonConvButton;
	}
	
	@Override
	public boolean isRemoveAnaAltriButton() {
		return removeAnaAltriButton;
	}

	@Override
	public boolean isAddAnaAltriButton() {
		return addAnaAltriButton;
	}
	
	@Override
	public boolean isApriAltrifamButton() {
		return apriAltrifamButton;
	}

	@Override
	public List<CsAComponente> getFamAltriComponentes() {
		return famAltriComponentes;
	}

	@Override
	public boolean isConvivente() {
		return isConvivente;
	}

	@Override
	public boolean isParente() {
		return isParente;
	}

	@Override
	public boolean isNewSchedaMultidimAnzRendered() {
		return newSchedaMultidimAnzRendered;
	}
	

	//**************************************************************************************
	// SCHEDA BARTHEL
	//**************************************************************************************
	private ISchedaBarthel manSchedaBarthelBean;

	public ISchedaBarthel getManSchedaBarthelBean() {
		return manSchedaBarthelBean;
	}
	
	@Override
	public void setOnViewBarthel(CsDValutazione scheda) throws Exception {

		CsDValutazione valutazioneSchedaMultidim = scheda;

		SchedaBarthelDTO barthelDto = new SchedaBarthelDTO();
		fillEnte( barthelDto );
		barthelDto.setSchedaMultidimDiarioId( valutazioneSchedaMultidim.getDiarioId() );
		CsDValutazione valutazioneSchedaBarthel = diarioService.findBarthelByMutlidimId( barthelDto );

		String jsonString = ""; String className = ""; 
		if( valutazioneSchedaBarthel != null ) {
			jsonString = valutazioneSchedaBarthel.getCsDDiario().getCsDClob().getDatiClob();
			className = valutazioneSchedaBarthel.getVersioneScheda();
		}

		//la versione di default è utile se si vuole comunque instanziare una versione intermedia tra la prima e la max
		String defaultVersion = "";
		manSchedaBarthelBean = (ISchedaBarthel) WebredClassFactory.newInstance( className, ISchedaBarthel.class, defaultVersion );

		//Initialize scheda barthel
		manSchedaBarthelBean.init( scheda, valutazioneSchedaBarthel );
	}

}
