package it.webred.cs.csa.web.manbean.fascicolo.schedeSegr;

import it.webred.cs.csa.ejb.client.AccessTableSchedaSegrSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsSsSchedaSegr;
import it.webred.cs.jsf.interfaces.ISchedaSegr;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.ct.config.luoghi.LuoghiService;
import it.webred.ct.config.model.AmTabComuni;
import it.webred.ss.data.model.SsDiario;
import it.webred.ss.data.model.SsInterventiSchede;
import it.webred.ss.data.model.SsInterventoEconomico;
import it.webred.ss.data.model.SsMotivazioniSchede;
import it.webred.ss.data.model.SsScheda;
import it.webred.ss.data.model.SsSchedaSegnalato;
import it.webred.ss.ejb.client.SsSchedaSessionBeanRemote;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SchedaSegrBean extends CsUiCompBaseBean implements ISchedaSegr {
	
	private SsScheda ssScheda;
	private SsSchedaSegnalato ssSchedaSegnalato;
	
	private AmTabComuni comuneSegnalante;
	private List<SsMotivazioniSchede> listaMotivazioni;
	private List<SsDiario> listaDiari;
	private List<SsInterventiSchede> listaInterventi;
	private List<SsInterventoEconomico> listaInterventiEcon;
	
	private SsSchedaSessionBeanRemote ssSchedaSegrService = (SsSchedaSessionBeanRemote) getEjb("SegretariatoSoc", "SegretariatoSoc_EJB", "SsSchedaSessionBean");
	private	AccessTableSchedaSegrSessionBeanRemote schedaSegrService = (AccessTableSchedaSegrSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableSchedaSegrSessionBean");
	private LuoghiService luoghiService = (LuoghiService) getEjb("CT_Service", "CT_Config_Manager", "LuoghiServiceBean");
	
	public void initialize(Long sId) {
		
		ssScheda = null;
		ssSchedaSegnalato = null;
		
		BaseDTO dto = new BaseDTO();
		fillEnte(dto);
		dto.setObj(sId);
		CsSsSchedaSegr csSsSchedaSegr = schedaSegrService.findSchedaSegrByIdAnagrafica(dto);
		
		if(csSsSchedaSegr != null) {
			caricaDettagliSchedaSegr(csSsSchedaSegr.getId());
		}
		
	}
	
	public void caricaDettagliSchedaSegr(Long idSchedaSegr) {
		
		try {
			it.webred.ss.ejb.dto.BaseDTO bDto = new it.webred.ss.ejb.dto.BaseDTO();
			fillEnte(bDto);
			bDto.setObj(idSchedaSegr);
			ssScheda = ssSchedaSegrService.readScheda(bDto);
			
			bDto.setObj(ssScheda.getSegnalato());
			ssSchedaSegnalato = ssSchedaSegrService.readSegnalatoById(bDto);
			
			if(ssScheda.getSegnalante() != null && ssScheda.getSegnalante().getComune() != null) {
				comuneSegnalante = luoghiService.getComuneItaByIstat(ssScheda.getSegnalante().getComune());
			}
			
			if(ssScheda.getMotivazione() != null) {
				bDto.setObj(ssScheda.getMotivazione());
				listaMotivazioni = ssSchedaSegrService.readMotivazioniScheda(bDto);
			}
			
			if(ssSchedaSegnalato != null && ssSchedaSegnalato.getAnagrafica() != null) {
				bDto.setObj(ssSchedaSegnalato.getAnagrafica());
				listaDiari = ssSchedaSegrService.readDiarioSociale(bDto);
				listaInterventiEcon = ssSchedaSegrService.readInterventiEconomici(bDto);
			}
			
			if(ssScheda.getInterventi() != null) {
				bDto.setObj(ssScheda.getInterventi());
				listaInterventi = ssSchedaSegrService.readInterventiScheda(bDto);
			}
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
	}
	
	public boolean isRenderSchedaSegr() {
		return checkPermesso(DataModelCostanti.PermessiSchedeSegr.VISUALIZZA_SCHEDE_SEGR) 
				&& ssScheda != null;
	}

	@Override
	public SsScheda getSsScheda() {
		return ssScheda;
	}

	public void setSsScheda(SsScheda ssScheda) {
		this.ssScheda = ssScheda;
	}

	@Override
	public SsSchedaSegnalato getSsSchedaSegnalato() {
		return ssSchedaSegnalato;
	}

	public void setSsSchedaSegnalato(SsSchedaSegnalato ssSchedaSegnalato) {
		this.ssSchedaSegnalato = ssSchedaSegnalato;
	}

	@Override
	public AmTabComuni getComuneSegnalante() {
		return comuneSegnalante;
	}

	public void setComuneSegnalante(AmTabComuni comuneSegnalante) {
		this.comuneSegnalante = comuneSegnalante;
	}

	@Override
	public List<SsMotivazioniSchede> getListaMotivazioni() {
		return listaMotivazioni;
	}

	public void setListaMotivazioni(List<SsMotivazioniSchede> listaMotivazioni) {
		this.listaMotivazioni = listaMotivazioni;
	}

	@Override
	public List<SsDiario> getListaDiari() {
		return listaDiari;
	}

	public void setListaDiari(List<SsDiario> listaDiari) {
		this.listaDiari = listaDiari;
	}

	@Override
	public List<SsInterventiSchede> getListaInterventi() {
		return listaInterventi;
	}

	public void setListaInterventi(List<SsInterventiSchede> listaInterventi) {
		this.listaInterventi = listaInterventi;
	}

	@Override
	public List<SsInterventoEconomico> getListaInterventiEcon() {
		return listaInterventiEcon;
	}

	public void setListaInterventiEcon(
			List<SsInterventoEconomico> listaInterventiEcon) {
		this.listaInterventiEcon = listaInterventiEcon;
	}
	
}
