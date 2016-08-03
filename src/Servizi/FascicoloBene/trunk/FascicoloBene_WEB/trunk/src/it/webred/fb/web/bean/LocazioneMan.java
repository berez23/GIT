package it.webred.fb.web.bean;

import it.webred.ejb.utility.ClientUtility;
import it.webred.fb.data.model.DmBBene;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.BaseDTO;
import it.webred.fb.ejb.dto.locazione.DatiCatastali;
import it.webred.fb.ejb.dto.locazione.DatiLocalizzazione;
import it.webred.fb.web.dto.GruppoCatastali;
import it.webred.fb.web.dto.GruppoLocalizzazione;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class LocazioneMan extends TabBaseMan{
	private HashMap<Long, List<GruppoCatastali>> cacheGruppoCatastali = new HashMap<Long, List<GruppoCatastali>>();
	private HashMap<Long, List<GruppoLocalizzazione>> cacheLocalizzazione = new HashMap<Long, List<GruppoLocalizzazione>>();
   
	public List<GruppoCatastali> getGroupMappales(DmBBene selBene){
		boolean Iscached = cacheGruppoCatastali.containsKey(selBene.getId());
		
		if (Iscached)
			return  cacheGruppoCatastali.get(selBene.getId());
			
		List<GruppoCatastali> gruppoMappales = new ArrayList<GruppoCatastali>();
    	List<DatiCatastali> datiCatastalis = new ArrayList<DatiCatastali>();
    	List<DatiCatastali> tempCats = new ArrayList<DatiCatastali>();
    	
    	BaseDTO dtoBene = new BaseDTO();
		fillUserData(dtoBene);
		dtoBene.setObj(selBene.getId());
		
    	DettaglioBeneSessionBeanRemote beneService;
		try {
			beneService = (DettaglioBeneSessionBeanRemote) 
					ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
			
			datiCatastalis = beneService.buildDatiCatastali(dtoBene);
				
			if (datiCatastalis.size() > 0) {
				String before = "";
				int counter = datiCatastalis.size();
				
				for (DatiCatastali it : datiCatastalis) {
					if (datiCatastalis.size() != counter) {
						if (!it.getProvenienza().equals(before)) {
							gruppoMappales.add(new GruppoCatastali(it.getProvenienza().getDato(), tempCats));
							tempCats = new ArrayList<DatiCatastali>();
							tempCats.add(it);
						} else {
							tempCats.add(it);
						}
					} else {
						tempCats = new ArrayList<DatiCatastali>();
						tempCats.add(it);
					}

					if (counter == 1)
						gruppoMappales.add(new GruppoCatastali(it.getProvenienza().getDato(), tempCats));
										
					counter--;
				}
			}
			cacheGruppoCatastali.put(selBene.getId(),gruppoMappales );

		} catch (Exception e) {
			addError("general", "dettaglio.error");
			logger.error(e.getMessage(), e);
		}
		
		return gruppoMappales;
	}
		
	public List<GruppoLocalizzazione> getGroupIndirizzos(DmBBene selBene){
		boolean isCached = cacheLocalizzazione.containsKey(selBene.getId());
		if (isCached)
			return cacheLocalizzazione.get(selBene.getId());
		
		List<GruppoLocalizzazione> gruppoLocalizzazionis = new ArrayList<GruppoLocalizzazione>();
    	List<DatiLocalizzazione> datiLocalizzazionis = new ArrayList<DatiLocalizzazione>();
		
    	BaseDTO dtoBene = new BaseDTO();
		fillUserData(dtoBene);
		dtoBene.setObj(selBene.getId());
		
		
    	DettaglioBeneSessionBeanRemote beneService;
    	try {
			beneService = (DettaglioBeneSessionBeanRemote) 
					ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
			
			datiLocalizzazionis = beneService.buildDatiLocalizzazione(dtoBene);
    	
			if (datiLocalizzazionis.size() > 0) {
				String before = "";
				List<DatiLocalizzazione> tempLocs = new ArrayList<DatiLocalizzazione>();
				int counter = datiLocalizzazionis.size();
				
				for (DatiLocalizzazione it : datiLocalizzazionis) {
					if (datiLocalizzazionis.size() != counter) {
						if (!it.getProvenienzaLoc().equals(before)) {
							gruppoLocalizzazionis.add(new GruppoLocalizzazione(it.getProvenienzaLoc().getDato(), tempLocs));
							tempLocs = new ArrayList<DatiLocalizzazione>();
							tempLocs.add(it);
						} else {
							tempLocs.add(it);
						}
					} else {
						tempLocs = new ArrayList<DatiLocalizzazione>();
						tempLocs.add(it);
					}
	
					if (counter == 1)
						gruppoLocalizzazionis.add(new GruppoLocalizzazione(it.getProvenienzaLoc().getDato(), tempLocs));
					
					counter--;
				}
			}
    	
			cacheLocalizzazione.put(selBene.getId(),gruppoLocalizzazionis );

    	} catch (Exception e) {
			addError("general", "dettaglio.error");
			logger.error(e.getMessage(), e);
		}
    	
		return gruppoLocalizzazionis;
	}
}
