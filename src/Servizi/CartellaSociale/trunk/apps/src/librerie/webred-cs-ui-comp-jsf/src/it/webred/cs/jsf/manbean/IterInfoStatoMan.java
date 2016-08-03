package it.webred.cs.jsf.manbean;

import it.webred.amprofiler.ejb.anagrafica.AnagraficaService;
import it.webred.amprofiler.model.AmAnagrafica;
import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.data.model.CsACasoOpeTipoOpe;
import it.webred.cs.data.model.CsItStep;
import it.webred.cs.data.model.CsItStepAttrValue;
import it.webred.cs.data.model.CsOOperatore;
import it.webred.cs.data.model.CsOOrganizzazione;
import it.webred.cs.data.model.CsOSettore;
import it.webred.cs.jsf.interfaces.IIterInfoStato;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.dto.utility.KeyValuePairBean;
import it.webred.ejb.utility.ClientUtility;
import it.webred.utilities.DateTimeUtils;

import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class IterInfoStatoMan extends CsUiCompBaseBean implements IIterInfoStato {

	private long idCaso;
	
	private String nomeStato;
	private String dataCreazione;
	private String responsabile;
	private String enteSegnalante;
	private String ufficioSegnalante;
	private String operatoreSegnalante;
	private String enteSegnalato;
	private String ufficioSegnalato;
	private String operatoreSegnalato;
	private String nota;
	private String cssClassStato;
	private String segnalatoDaLabel;
	private String segnalatoALabel;
	private String sezioneAttributiLabel;
	
	public List<KeyValuePairBean<String,String>> listaAttrValues;

	@Override
	public String getNomeStato() {
		return nomeStato;
	}
	
	@Override
	public String getDataCreazione() {
		return dataCreazione;
	}

	@Override
	public String getResponsabile() {
		return responsabile;
	}
	
	@Override
	public String getEnteSegnalante() {
		return enteSegnalante;
	}
	
	@Override
	public String getUfficioSegnalante() {
		return ufficioSegnalante;
	}
	
	@Override
	public String getOperatoreSegnalante() {
		return operatoreSegnalante;
	}
	
	@Override
	public String getEnteSegnalato() {
		return enteSegnalato;
	}

	@Override
	public String getUfficioSegnalato() {
		return ufficioSegnalato;
	}

	@Override
	public String getOperatoreSegnalato() {
		return operatoreSegnalato;
	}

	@Override
	public String getNota() {
		return nota;
	}
	
	@Override
	public String getCssClassStato() {
		return cssClassStato;
	}
	
	@Override
	public String getSegnalatoDaLabel() {
		return segnalatoDaLabel;
	}
	
	@Override
	public String getSegnalatoALabel() {
		return segnalatoALabel;
	}

	@Override
	public List<KeyValuePairBean<String,String>> getListaAttrValues() {
		return listaAttrValues;
	}

	@Override
	public String getSezioneAttributiLabel() {
		return sezioneAttributiLabel;
	}
	
	@Override
	public boolean isEnteARendered() {
		if (enteSegnalato == null)
			return false;
		else
			return true;
	}
	
	@Override
	public boolean isUfficioARendered() {
		if (ufficioSegnalato == null)
			return false;
		else
			return true;
	}
	
	@Override
	public boolean isOperatoreARendered() {
		if (operatoreSegnalato == null)
			return false;
		else
			return true;
	}
	
	@Override
	public boolean isNotaRendered() {
		if (nota == null)
			return false;
		else
			return true;
	}
	
	@Override
	public boolean isResponsabileRendered(){
		if(responsabile == null)
			return false;
		else
			return true;
	}
	
	@Override
	public boolean isSezioneAttributiRendered() {
		if (listaAttrValues != null) {
			if (listaAttrValues.isEmpty())
				return false;
			else
				return true;
		}
		return false;
	}
	
	@Override
	public boolean isOpPanelARendered() {
		if (operatoreSegnalato == null && ufficioSegnalato == null && enteSegnalato == null)
			return false;
		else
			return true;
	}
	
	@Override
	public void initialize(CsItStep itStep) {	
		
		try {
		
			this.idCaso = itStep.getCsACaso().getId();
			AccessTableCasoSessionBeanRemote casoService = (AccessTableCasoSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableCasoSessionBean");
			AnagraficaService anagraficaService = (AnagraficaService) ClientUtility.getEjbInterface("AmProfiler", "AmProfilerEjb", "AnagraficaServiceBean");
	
			this.listaAttrValues = new LinkedList<KeyValuePairBean<String,String>>();
			for(CsItStepAttrValue itStepValue : itStep.getCsItStepAttrValues() )
				this.listaAttrValues.add( new KeyValuePairBean<String,String>(itStepValue.getCsCfgAttr().getLabel(), itStepValue.getValore() ) );
			
			this.nomeStato= itStep.getCsCfgItStato().getNome();
			
			this.dataCreazione = DateTimeUtils.dateToString(itStep.getDataCreazione(), "dd/MM/yy HH:mm");
			
			BaseDTO bDto = new BaseDTO();
			CsUiCompBaseBean.fillEnte(bDto);
			bDto.setObj(itStep.getCsACaso().getId());
			CsACasoOpeTipoOpe casoOpTipoOp = casoService.findResponsabile(bDto);
			if( casoOpTipoOp != null ) {
				CsOOperatore operatore = casoOpTipoOp.getCsOOperatoreTipoOperatore().getCsOOperatoreSettore().getCsOOperatore();
				AmAnagrafica amAna = anagraficaService.findAnagraficaByUserName(operatore.getUsername());
				this.responsabile = amAna.getCognome() + " " + amAna.getNome();
			}
			
			this.enteSegnalante = itStep.getCsOOrganizzazione1().getNome();
			this.ufficioSegnalante= itStep.getCsOSettore1().getNome();
			
			AmAnagrafica amAna = anagraficaService.findAnagraficaByUserName(itStep.getCsOOperatore1().getUsername());
			this.operatoreSegnalante = amAna.getCognome() + " " + amAna.getNome();
			
			getSegnalato(itStep.getCsOOrganizzazione2(), itStep.getCsOSettore2(), itStep.getCsOOperatore2() );
			
			this.nota = itStep.getNota();
			this.cssClassStato = itStep.getCsCfgItStato().getCssClassStato();
			this.segnalatoDaLabel = itStep.getCsCfgItStato().getSegnalatoDaLabel();
			this.segnalatoALabel = itStep.getCsCfgItStato().getSegnalatoALabel();
			this.sezioneAttributiLabel = itStep.getCsCfgItStato().getSezioneAttributiLabel();
		
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(), e);
		}
	}

	private void getSegnalato(CsOOrganizzazione ente, CsOSettore ufficio, CsOOperatore operatore) {
		
		try {
		
			if (ente != null)
				this.enteSegnalato = ente.getNome();
			
			if (ufficio != null)
				this.ufficioSegnalato = ufficio.getNome();
			
			if (operatore != null) {
				AnagraficaService anagraficaService = (AnagraficaService) ClientUtility.getEjbInterface("AmProfiler", "AmProfilerEjb", "AnagraficaServiceBean");
				AmAnagrafica amAna = anagraficaService.findAnagraficaByUserName(operatore.getUsername());
				this.operatoreSegnalato = amAna.getCognome() + " " + amAna.getNome();
			}
		
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public long getIdCaso() {
		return idCaso;
	}
	
}
