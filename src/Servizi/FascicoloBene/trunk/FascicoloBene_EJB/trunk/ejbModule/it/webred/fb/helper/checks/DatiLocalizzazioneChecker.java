package it.webred.fb.helper.checks;

import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaCivicoDTO;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Sitidstr;
import it.webred.ejb.utility.ClientUtility;
import it.webred.fb.data.DataModelCostanti.Segnalibri.TipoAlert;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.Alert;
import it.webred.fb.ejb.dto.BeneInListaDTO;
import it.webred.fb.ejb.dto.DatoSpec;
import it.webred.fb.ejb.dto.KeyValueDTO;
import it.webred.fb.ejb.dto.RicercaBeneDTO;
import it.webred.fb.ejb.dto.TabellaDatiCollegati;
import it.webred.fb.ejb.dto.locazione.DatiLocalizzazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class DatiLocalizzazioneChecker extends IChecker<DatiLocalizzazione> {

	protected static Logger logger = Logger.getLogger("fascicolobene.log");

	public DatiLocalizzazioneChecker(String ente) {
		super(ente);
		// TODO Auto-generated constructor stub
	}

	DatiLocalizzazione dato;
	List<BeneInListaDTO> inventari;
	

	@Override
	protected DatiLocalizzazione getDato() {
		return dato;
	}

	@Override
	public void setDato(DatiLocalizzazione dato) {
		this.dato =dato;
		
		
	}

	@Override
	protected List<Alert> checkImpl() throws CheckerException {
		List<Alert> resp = new ArrayList<Alert>();
		checkEsistenzaIndirizzo(resp);
		checkEsistenzaAltriInventari(resp);
		return resp;
	}
	
	protected LinkedList<TabellaDatiCollegati> collegaDati() {
		
		LinkedList<TabellaDatiCollegati> tabelle = new LinkedList<TabellaDatiCollegati>();

		if(!inventari.isEmpty())
			tabelle.add(packInventariInDatiAggiuntivi(inventari));
		return tabelle;
		
	}
	
	
	
	private void checkEsistenzaAltriInventari(List<Alert> alerts) throws CheckerException {
		try{
			//Verifico la presenza di altri inventari collegati al mappale
			inventari = new ArrayList<BeneInListaDTO>();
			DettaglioBeneSessionBeanRemote beneService = (DettaglioBeneSessionBeanRemote) 
					ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
			RicercaBeneDTO rb = new RicercaBeneDTO();
			rb.setEnteId(ente);
			rb.setRicercaIndirizzo(true);
			
			KeyValueDTO comuneCat = new KeyValueDTO();
			comuneCat.setCodice(dato.getCodComune().getDato());
			rb.setComuneInd(comuneCat);
			
			KeyValueDTO codVia = new KeyValueDTO();
			codVia.setCodice(dato.getCodVia().getDato());
			rb.setVia(codVia);
			
			KeyValueDTO civico = new KeyValueDTO();
			civico.setCodice(dato.getCivico().getDato());
			rb.setCivico(civico);
			
			inventari = beneService.searchBene(rb);
			//Rimuovo l'inventario corrente
			int i = inventari.size()-1;
			while(i>=0){
				BeneInListaDTO b = inventari.get(i);
				if(b.getBene().getChiave().equalsIgnoreCase(dato.getCodInventario().getDato()))
					inventari.remove(b);
				i--;
			}
			
			if(!inventari.isEmpty())
				alerts.add(new Alert(TipoAlert.INV_VALIDATE, "Indirizzo presente in altri Inventari"));
		}catch(NamingException ne){
			logger.error("Errore recupero DettaglioBeneSessionBeanRemote");
		}
	}
	
	
	private void checkEsistenzaIndirizzo(List<Alert> alerts) throws CheckerException {
		 CatastoService ejb=null;
		try {
			ejb = (CatastoService) ClientUtility.getEjbInterface("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
		

			RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
			roc.setEnteId(ente);
			roc.setCodiceVia(dato.getCodVia().getDato());
			
			List<Sitidstr> vie = ejb.getListaVieByCodiceVia(roc);
			
			if (vie.isEmpty())
		    	alerts.add(new Alert(TipoAlert.NOT_VALIDATE, "Via non presente in viario SIT"));
			else {
		    	alerts.add(new Alert(TipoAlert.VALIDATE, "Via riscontrata in SIT"));

		    	if (dato.getCivico().getDato()!=null) {
			    	boolean isCivicoInCatasto = false;
					for (Sitidstr sitidstr : vie) {
					    	isCivicoInCatasto = checkEsistenzaCivico(sitidstr.getPkidStra() , dato.getCivico());
					    	if (isCivicoInCatasto) {
					    		break;
					    	}
	
					}
					if (!isCivicoInCatasto)
						dato.getCivico().getDatoArricchito().addAlert(TipoAlert.NOT_VALIDATE, "Civico non presente in SIT");
					else
						dato.getCivico().getDatoArricchito().addAlert(TipoAlert.VALIDATE, "Civico presente in SIT");
				
		    	} else 
					alerts.add(new Alert(TipoAlert.WARNING, "Civico non indicato"));

			}
		    
		} catch (Exception e) {
			logger.error("Impossibile verificare LA VIA a causa della chiamata fallita su EJB di CT_Service");
			throw new CheckerException();
		}
	
	}


	private boolean checkEsistenzaCivico(BigDecimal idVia, DatoSpec datoSpec) throws CheckerException {
			 	boolean trovato = false;;
				CatastoService ejb=null;
				try {
					ejb = (CatastoService) ClientUtility.getEjbInterface("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
					DatoSpec civico = dato.getCivico();
			
			    	RicercaCivicoDTO rc = new RicercaCivicoDTO();
					rc.setEnteId(ente);
					rc.setIdVia(idVia.toString());
					String civicoStr = dato.getCivico().getDato();
					if (civicoStr!=null) {
				    	rc.setCivico(civicoStr);
				    	Siticivi resp = ejb.getCivico(rc) ;
				    	if (resp!=null)
				    		trovato = true;
					}
				} catch (Exception e) {
					logger.error("Impossibile verificare esistenza civico a causa della chiamata fallita su EJB di CT_Service",e);
					throw new CheckerException();
				}
	    		return trovato;
	}


	@Override
	protected boolean isCheckable() throws CheckerException {
		if (dato.getCodVia()==null && ente.equals(dato.getComune()))
				return false;
		else
				return true;
		
	}
	
	

	

}
