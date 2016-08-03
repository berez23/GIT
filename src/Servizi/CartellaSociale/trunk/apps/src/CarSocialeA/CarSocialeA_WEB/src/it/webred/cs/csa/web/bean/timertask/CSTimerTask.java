package it.webred.cs.csa.web.bean.timertask;

import it.webred.cs.csa.ejb.client.AccessTableAlertSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableOperatoreSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableParentiGitSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.csa.ejb.dto.OperatoreDTO;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsACasoOpeTipoOpe;
import it.webred.cs.data.model.CsAFamigliaGruppoGit;
import it.webred.cs.data.model.CsAlert;
import it.webred.cs.data.model.CsAlertConfig;
import it.webred.cs.data.model.CsOOperatore;
import it.webred.ejb.utility.ClientUtility;

import java.util.List;
import java.util.TimerTask;

import org.jboss.logging.Logger;

public class CSTimerTask extends TimerTask {

	private static Logger logger;
	private String enteId;

	public CSTimerTask(String enteId) {
		super();
		this.enteId = enteId;
	}

	@Override
	public void run() {

		try {
			
			AccessTableAlertSessionBeanRemote alertService = (AccessTableAlertSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableAlertSessionBean");
			AccessTableParentiGitSessionBeanRemote parentiService = (AccessTableParentiGitSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableParentiGitSessionBean");
			AccessTableOperatoreSessionBeanRemote operatoreService = (AccessTableOperatoreSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableOperatoreSessionBean");
			AccessTableCasoSessionBeanRemote casoService = (AccessTableCasoSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableCasoSessionBean");
			
			BaseDTO bDto = new BaseDTO();
			IterDTO itDto = new IterDTO();
			OperatoreDTO opDto = new OperatoreDTO();

			
	    	bDto.setEnteId(enteId);
	    	itDto.setEnteId(enteId);
	    	opDto.setEnteId(enteId);
		
	    	try {
	    	
				List<CsAFamigliaGruppoGit> listaFamiglieAggiornate = parentiService.getFamigliaGruppoGitAggiornate(bDto);
				for(CsAFamigliaGruppoGit fam: listaFamiglieAggiornate) {
					
					CsACaso caso = fam.getCsASoggetto().getCsACaso();
					bDto.setObj(caso.getId());
					CsACasoOpeTipoOpe casoOpTipoOp = casoService.findResponsabile(bDto);
					
					//cerco se è già presente un alert
					itDto.setIdCaso(caso.getId());
					itDto.setTipo(DataModelCostanti.TipiAlert.FAMIGLIA_GIT);
					List<CsAlert> listaAlert = alertService.findAlertByIdCasoTipo(itDto);
					if(listaAlert != null && !listaAlert.isEmpty()) {
						
						CsAlert alert = listaAlert.get(0);
						CsOOperatore operatore;
						if(casoOpTipoOp != null) {
							
							//Responsabile
							operatore = casoOpTipoOp.getCsOOperatoreTipoOperatore().getCsOOperatoreSettore().getCsOOperatore();
							
						} else {
							
							//creatore
							opDto.setUsername(caso.getUserIns());
							operatore = operatoreService.findOperatoreByUsername(opDto);
							
						}
						
						if(alert.getVisibile() && !alert.getLetto() && alert.getCsOOperatore2().getId() == operatore.getId())
							continue;
						
						alert.setCsOOperatore2(operatore);
						alert.setVisibile(true);
						alert.setLetto(false);
						bDto.setObj(alert);
						
						logger.info(enteId + " __ Aggiorno Alert per aggiornamento famiglia: " + fam.getCsASoggetto().getCsAAnagrafica().getCf());
						alertService.updateAlert(bDto);
						
					} else {
					
						//nuovo alert
						String nome = fam.getCsASoggetto().getCsAAnagrafica().getCognome() + " " + fam.getCsASoggetto().getCsAAnagrafica().getNome();
						String cf = fam.getCsASoggetto().getCsAAnagrafica().getCf();
						itDto.setLabelTipo(DataModelCostanti.TipiAlert.FAMIGLIA_GIT_DESC);
						itDto.setDescrizione("Sono presenti aggiornamenti nei dati familiari di "+ nome +" ("+ cf +")");
						itDto.setUrl(null);
						itDto.setTitoloDescrizione("Sono presenti aggiornamenti nei dati familiari di " + nome +" (" + cf + ")");
						bDto.setObj(DataModelCostanti.TipiAlert.FAMIGLIA_GIT);
						CsAlertConfig aConfig = alertService.getAlertConfigByTipo(bDto);
						if(aConfig != null && aConfig.getCsOOrganizzazioneDefault() != null)
							itDto.setIdOrganizzazione(aConfig.getCsOOrganizzazioneDefault().getId());
						
						if(casoOpTipoOp != null) {
							
							//Responsabile
							CsOOperatore operatore = casoOpTipoOp.getCsOOperatoreTipoOperatore().getCsOOperatoreSettore().getCsOOperatore();
							itDto.setIdOpTo(operatore.getId());
							
						} else {
							
							//creatore
							opDto.setUsername(caso.getUserIns());
							CsOOperatore operatore = operatoreService.findOperatoreByUsername(opDto);
							itDto.setIdOpTo(operatore.getId());
							
						}
						
						logger.info(enteId + " __ Aggiungo Alert per aggiornamento famiglia: " + fam.getCsASoggetto().getCsAAnagrafica().getCf());
						alertService.addAlert(itDto);
					}
					
					//creato l alert azzero la segnalazione
					fam.setFlgSegnalazione("0");
					bDto.setObj(fam);
					parentiService.updateFamigliaGruppoGit(bDto);
					
				}
			
	    	} catch (Exception e2) {
				logger.info("__ CSTimerTask: Eccezione su ENTEID " + enteId + ": " + e2.getMessage());
			}
			
			logger.debug("__ CSTimerTask FINE __");

		} catch (Exception e) {
			logger.error("__ CSTimerTask: Eccezione su ENTEID " + enteId + ": " + e.getMessage(), e);
		}
	}

	public static void setLogger(Logger logger) {
		CSTimerTask.logger = logger;
	}

}
