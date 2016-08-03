package it.webred.cs.csa.ejb.ejb;

import it.webred.amprofiler.ejb.anagrafica.AnagraficaService;
import it.webred.cs.csa.ejb.CarSocialeBaseSessionBean;
import it.webred.cs.csa.ejb.client.AccessTableComuniSessionBeanRemote;
import it.webred.cs.csa.ejb.dao.ComuneDAO;
import it.webred.ct.config.luoghi.LuoghiService;
import it.webred.ct.config.model.AmTabComuni;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

@Stateless
//@Remote(HelloWorldSessionBeanRemote.class)
//@LocalBean
public class AccessTableComuniSessionBean extends CarSocialeBaseSessionBean implements AccessTableComuniSessionBeanRemote {

	@EJB(mappedName = "java:global/AmProfiler/AmProfilerEjb/AnagraficaServiceBean")
	protected AnagraficaService anagraficaService;

	
	@EJB(mappedName = "java:global/CT_Service/CT_Config_Manager/LuoghiServiceBean")
	protected LuoghiService luoghiService;

	
	@Autowired
	private ComuneDAO comuneDAO;

	
    /**
     * Default constructor. 
     */
    public AccessTableComuniSessionBean() {
        // TODO Auto-generated constructor stub
    }

    public List<AmTabComuni> getComuni(){
    	
    	List<AmTabComuni> lista = luoghiService.getComuniIta();
    	
    	return lista;
    	
    }
    
    public AmTabComuni getComuneByIstat(String codIstat){
    	AmTabComuni comune = luoghiService.getComuneItaByIstat(codIstat);
    	
    	return comune;
    }
    
    public AmTabComuni getComuneByBelfiore(String belfiore) {
    	AmTabComuni comune = luoghiService.getComuneItaByBelfiore(belfiore);
    	
    	return comune;
    }
    
    public List<AmTabComuni> getComuniByDenominazioneStartsWith(String denominazione) {
    	
    	List<AmTabComuni> lista = luoghiService.getComuniItaByDenominazioneStartsWith(denominazione);
    	
    	return lista;
    	
    }
    
    public AmTabComuni getComuneByDenominazioneProv(String denominazione, String provincia) {
    	
    	AmTabComuni comune = luoghiService.getComuneItaByDenominazioneProvincia(denominazione, provincia);
    	
    	return comune;
    	
    }


    
}

