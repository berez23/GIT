package it.webred.fb.web.bean;

import it.webred.ejb.utility.ClientUtility;
import it.webred.fb.data.model.DmBBene;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.BaseDTO;
import it.webred.fb.ejb.dto.locazione.DatiCatastali;
import it.webred.fb.ejb.dto.locazione.DatiTerreni;
import it.webred.fb.web.dto.GruppoCatastali;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@ManagedBean
@ViewScoped
public class TerreniMan extends TabBaseMan{
	private HashMap<Long, List<GruppoCatastali>> cacheGruppoTerreni = new HashMap<Long, List<GruppoCatastali>>();
	
	public List<GruppoCatastali> getGroupTerreni(DmBBene selBene){
		boolean Iscached = cacheGruppoTerreni.containsKey(selBene.getId());
		
		if (Iscached)
			return  cacheGruppoTerreni.get(selBene.getId());
			
		List<GruppoCatastali> gruppoMappales = new ArrayList<GruppoCatastali>(); 
    	List<DatiTerreni> datiCatastalis = new ArrayList<DatiTerreni>();
    	
    	BaseDTO dtoBene = new BaseDTO();
		fillUserData(dtoBene);
		dtoBene.setObj(selBene.getId());
		
    	DettaglioBeneSessionBeanRemote beneService;
		try {
			beneService = (DettaglioBeneSessionBeanRemote) 
					ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
			
			datiCatastalis = beneService.buildDatiTerreni(dtoBene);
			
			HashMap<String, GruppoCatastali> mappa = new HashMap<String,GruppoCatastali>();
			
			if (datiCatastalis.size() > 0) {
				for (DatiCatastali it : datiCatastalis) {
					GruppoCatastali gruppo =  mappa.get(it.getProvenienza().getDato());
					if(gruppo==null)
						gruppo = new GruppoCatastali(it.getProvenienza().getDato());
					gruppo.getDatiCats().add(it);
					mappa.put(it.getProvenienza().getDato(), gruppo);
				}
			}
			//Riverso nella lista finale
			Iterator<String> iter = mappa.keySet().iterator();
			while(iter.hasNext()){
				String key = iter.next();
				gruppoMappales.add(mappa.get(key));
			}
			cacheGruppoTerreni.put(selBene.getId(),gruppoMappales );

		} catch (Exception e) {
			addError("dettaglio.terreni.error", e.getMessage());
			logger.error(e.getMessage(), e);
		}
		
		return gruppoMappales;
	}
		
	public List<String[]> getListaContratti(String json){
		ArrayList<String[]> lst = new ArrayList<String[]>();
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray array = obj.getJSONArray("contratti");
			  //Caricamento dati TAB CATASTO
		    for(int i=0; i<array.length();i++){
		    	String[] contratto = new String[3];
		    	JSONObject c;
				c = array.getJSONObject(i);
				contratto[0]=(String)c.get("Intestatario");
				contratto[1]=(String)c.get("Tipologia");
				contratto[2]=(String)c.get("Parte");
				lst.add(contratto);
		    }
		} catch (JSONException e) {
			logger.error(e.getMessage(),e);
		}
		return lst;
	}
}
