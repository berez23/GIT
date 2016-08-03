package it.webred.cs.csa.ejb.ejb;

import it.webred.cs.csa.ejb.CarSocialeBaseSessionBean;
import it.webred.cs.csa.ejb.client.AccessTableNazioniSessionBeanRemote;
import it.webred.ct.config.luoghi.LuoghiService;
import it.webred.ct.config.model.AmTabNazioni;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class AccessTableDataNazioniSessionBean
 */
@Stateless
public class AccessTableNazioniSessionBean extends CarSocialeBaseSessionBean implements AccessTableNazioniSessionBeanRemote {

	@EJB(mappedName = "java:global/CT_Service/CT_Config_Manager/LuoghiServiceBean")
	protected LuoghiService luoghiService;
	
	/**
     * Default constructor. 
     */
    public AccessTableNazioniSessionBean() {
        // TODO Auto-generated constructor stub
    }

    public List<AmTabNazioni> getNazioniByDenominazioneStartsWith(String nazione) {
    	
    	List<AmTabNazioni> lista = luoghiService.getNazioniByDenominazioneStartsWith(nazione);
    	
    	return lista;
    	
    }
    
    public AmTabNazioni getNazioneByIstat(String codIstat){
    	AmTabNazioni nazione = luoghiService.getNazioneByIstat(codIstat);
    	
    	return nazione;
    }
    
    public AmTabNazioni getNazioneByDenominazione(String denominazione) {
    	AmTabNazioni nazione = luoghiService.getNazioneByDenominazione(denominazione);
    	
    	return nazione;
    }
    
    public  List<String>  getCittadinanze(){
    	
    	List<String> lista = luoghiService.getCittadinanze();
    	
    	return lista;
    	
    }

}
