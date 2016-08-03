package it.webred.fb.web.bean;

import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.CatastoBaseDTO;
import it.webred.fb.data.model.DmBBene;
import it.webred.fb.data.model.DmBIndirizzo;
import it.webred.fb.data.model.DmBTerreno;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.BaseDTO;
import it.webred.fb.ejb.dto.MappaleDTO;
import it.webred.fb.web.bean.util.UserBean;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@ManagedBean
@ViewScoped
public class MapMan extends FascicoloBeneBaseBean {

	private String mapUrl;
	
	private String lastRootLoaded=null;
	private String centroFoglio=null;
	private boolean mapInComune=false;
	private boolean indInComune=false;
	private boolean terInComune=false;
	
	private List<MappaleDTO> listaMappaliEnte;
	private MapModel simpleModel;
	private String[] centroCoord;
	private String zoom;
	
	private String selectedMap="C";
	private String selectedMappale;
	
	private DmBBene root;

	public void initializeData(){
		
    	root = (DmBBene) getSession().getAttribute("dettaglioBene");
    	String enteCorrente = this.getEnte();
   	 	DettaglioBeneSessionBeanRemote bnService;
   	 	selectedMappale = null;
   	 
   		try {
   			bnService = this.getDettaglioBeneService();
   			
   			List<MappaleDTO> mappali = this.caricaMappali();
   					
   			if(!root.getChiave().equals(lastRootLoaded)){
	   			BaseDTO dtoBene = new BaseDTO();
	   			dtoBene.setObj(root.getChiave());
	   			
	   	        this.fillUserData(dtoBene);
	   	        
	   	        mapInComune=false;
	   	     	indInComune=false;
	   	     	terInComune=false;
	   	       // List<MappaleDTO> mappali = bnService.getMappalesInventario(dtoBene);
	   	        List<DmBIndirizzo> indirizzi = bnService.getIndirizziInventario(dtoBene);
	   	        List<DmBTerreno> terreni = bnService.getTerreniInventario(dtoBene);
	   	       
	   	        if(mappali.size()>0){
	   	        	int i = 0;
	   	        	while(i<mappali.size() && !mapInComune){
	   	        		MappaleDTO m = mappali.get(i);
	   	        		if(enteCorrente.equals(m.getCodComune())){
	   	        			mapInComune=true;
	   	        			centroFoglio=m.getFoglio();
	   	        		}
	   	        		i++;
	   	        	}
	   	        }
	   	        if(terreni.size()>0){
	   	        	int i=0;
	   	        	while(i<terreni.size() && !terInComune){
	   	        		DmBTerreno m = terreni.get(i);
	   	        		if(enteCorrente.equals(m.getCodComune())){
	   	        			terInComune=true;
	   	        			if(centroFoglio==null)
	   	        				centroFoglio=m.getFoglio();
	   	        		}
	   	        		i++;
	   	        	}
	   	        }
	   	        
				if(indirizzi.size()>0){
			    	int i = 0;
			    	while(i<indirizzi.size() && !indInComune){
			    		DmBIndirizzo m = indirizzi.get(i);
			    		if(enteCorrente.equals(m.getCodComune()))
			    			indInComune=true;
			    		i++;
			    	}
				}
				lastRootLoaded=root.getChiave();
   			}else{
   				BaseDTO dtoBene = new BaseDTO();
	   			dtoBene.setObj(bene.getId());
	   	        this.fillUserData(dtoBene);
	   	        //List<MappaleDTO> mappali = bnService.getMappales(dtoBene);
		   	    if(mappali.size()>0){
		   	        	int i = 0;
		   	        	boolean trovato = false;
		   	        	while(i<mappali.size() && !trovato){
		   	        		MappaleDTO m = mappali.get(i);
		   	        		if(enteCorrente.equals(m.getCodComune())){
		   	        			trovato=true;
		   	        			centroFoglio=m.getFoglio();
		   	        		}
		   	        		i++;
		   	        	}
		   	     }
   			}
   	        
	   		 		
   			boolean presentiCatasto = mapInComune || terInComune;
   	        if(presentiCatasto || indInComune){
   	        	caricaMapUrlInventario(bene.getChiave(),centroFoglio);
   	        }else{
   	        	String msg = "Tutti i " + 
   	        				(!presentiCatasto ? "MAPPALI " : "") + ((!presentiCatasto && !indInComune) ? "ed i " : "") +
   	        				(!indInComune ? "CIVICI " : "") 
   	        				+ "dell'inventario corrente sono collocati al di fuori del Comune";
   	        	
   	        	this.addWarning(msg,"");
   	        		
   	        	caricaMapUrl();
   	        }
   			
   		} catch (Exception e) {
   			addError("dettaglio.mappa.error", e.getMessage());
   			logger.error(e.getMessage(), e);
   		}
    }
	
	public List<MappaleDTO> caricaMappali(){
		centroCoord = null;
   	 	listaMappaliEnte = new ArrayList<MappaleDTO>();
   	 	simpleModel = new DefaultMapModel();
		
		DettaglioBeneSessionBeanRemote bnService = this.getDettaglioBeneService();
		List<MappaleDTO> mappali = new ArrayList<MappaleDTO>();
		
		BaseDTO dtoBene = new BaseDTO();
        this.fillUserData(dtoBene);
	       
		if(bene.getChiavePadre()==null){
			dtoBene.setObj(root.getChiave());		
			mappali = bnService.getMappalesInventario(dtoBene);
			zoom="13";
		}else{
			dtoBene.setObj(bene.getId());		
			mappali = bnService.getMappales(dtoBene);
			zoom="17";
		}
		
		String enteCorrente = this.getEnte();
		
		this.listaMappaliEnte= new ArrayList<MappaleDTO>();
		for(MappaleDTO m : mappali){
			if(enteCorrente.equals(m.getCodComune())){
				this.listaMappaliEnte.add(m);
				String label = "Foglio: "+m.getFoglio()+" Mappale: "+m.getMappale();
				String[] coord = m.getCoordinate();
				
				if(coord!=null && !"0".equals(coord[0]) && !"0".equals(coord[1])){
					
					if(centroCoord==null) centroCoord=coord;
					
					logger.debug(label+" "+coord[0]+" "+coord[1]);
					LatLng latlon = new LatLng(Double.parseDouble(coord[0]),Double.parseDouble(coord[1]));
					Marker mark = new Marker(latlon, label);
					simpleModel.addOverlay(mark);
				}
			}
		}
		return mappali;
	}
	
	public List<SelectItem> getListaMappali(){
		List<SelectItem> lst = new ArrayList<SelectItem>();
		for(MappaleDTO m : this.listaMappaliEnte){
			SelectItem si = new SelectItem();
			String label = "Foglio: "+m.getFoglio()+" Mappale: "+m.getMappale();
			si.setLabel(label);
			si.setValue(m.getCodComune()+"|"+m.getFoglio()+"|"+m.getMappale());
			lst.add(si);
		}
		
		if(lst.size()>0 && selectedMappale==null)
			setSelectedMappale((String)lst.get(0).getValue());
			
		
		return lst;
	}
	
	
	
    public List<DmBBene> getGroupParent(){
    	return bene.getDmBBenes() != null || !bene.getDmBBenes().isEmpty() ? bene.getDmBBenes() : new ArrayList<DmBBene>();
    }

	public List<DmBBene> getGroupChildren(DmBBene parent){
    	return parent.getDmBBenes() != null || !parent.getDmBBenes().isEmpty() ? parent.getDmBBenes() : new ArrayList<DmBBene>();
    }

	
	public void caricaMapUrl() {
		UserBean uBean = (UserBean) getBeanReference("userBean");
		String utente = uBean.getUsername();
	
		mapUrl = getBaseUrl() 
				+ "&cod_nazionale=A'OR'A'<'B&user=" + utente;
	}

	public void caricaMapUrlInventario(String codInventario,String foglio) {
		UserBean uBean = (UserBean) getBeanReference("userBean");
		String utente = uBean.getUsername();
		
		mapUrl = getBaseUrl() 
				+ "&cod_nazionale=" + getEnte() 
				+ (foglio!=null?"&foglio=" + foglio:"") 
				+ "&inventario=" + codInventario 
				+ "&user=" + utente;
	}
	
	/*public void caricaMapUrlSelezione(String codInventario) {
		UserBean uBean = (UserBean) getBeanReference("userBean");
		String utente = uBean.getUsername();
	
		String[] f = fogli.split("\\|");
		mapUrl = getBaseUrl() 
				+ "&cod_nazionale=" + getEnte() 
				+ "&foglio=" + f[0] 
				+ "&inventario=" + codInventario 
				+ "&fogli=" + fogli + "&particelle=" + particelle 
				+ "&user=" + utente;
	}*/
	
	private String getBaseUrl(){
		UserBean uBean = (UserBean) getBeanReference("userBean");
		String serverName = getRequest().getServerName();
		int port = getRequest().getServerPort();
		String ds = this.getDatasource();
		
		String s = "http://" + serverName + ":" + port
				+ "/viewerjs_FascicoloBene/extra/viewerjs/map?ente=" + uBean.getEnte()
				+ "&ds=" + ds
				+ "&qryoper=equal";
		return s;
	}

	/*public void caricaMapUrlDaSelezione(String codInventario) {
		fogli = "";
		particelle = "";
		
		DettaglioBeneSessionBeanRemote bnService;
		 
   		try {
   			bnService = this.getDettaglioBeneService();
		
			BaseDTO dtoBene = new BaseDTO();
			dtoBene.setObj(bene.getId());
				
		    this.fillUserData(dtoBene);
		    List<DmBMappale> mappali = bnService.getMappales(dtoBene);
			
			if (mappali.size() > 0) {
				for (DmBMappale dto : mappali) {
					if (!fogli.contains(dto.getFoglio()))
						fogli += "|" + dto.getFoglio();
					particelle += "|" + dto.getMappale();
				}
				fogli = fogli.substring(1);
				particelle = particelle.substring(1);
				caricaMapUrlSelezione(codInventario);
			}
   		} catch (Exception e) {
   			addError("general", "dettaglio.error");
   			logger.error(e.getMessage(), e);
   		}
	}*/
/*
	public void caricaMapUrlDaRisultati() {
		DataProviderImpl impl = (DataProviderImpl) getBeanReference("nuovoDataProviderImpl");
		fogli = impl.getListaFogliRicerca();
		particelle = impl.getListaParticelleRicerca();
		caricaMapUrlSelezione();
	}
*/

	public String getMapUrl() {
		if (mapUrl == null)
			caricaMapUrl();
		return mapUrl;
	}

	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl;
	}

	public String getMapHeight() {

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		try{
			logger.debug("Headless:"+java.awt.GraphicsEnvironment.isHeadless());
			Dimension dim = toolkit.getScreenSize();
			int h = (dim.height / 100) * 60;
			return h + "px";
		} catch (Exception e) {
   			addError("dettaglio.mappa.error", e.getMessage());
   			logger.error(e.getMessage(), e);
   			
   		}
		return null;
	}
	
	public boolean isMapInComune() {
		return mapInComune;
	}

	public void setMapInComune(boolean mapInComune) {
		this.mapInComune = mapInComune;
	}

	public boolean isIndInComune() {
		return indInComune;
	}

	public void setIndInComune(boolean indInComune) {
		this.indInComune = indInComune;
	}



	public String getSelectedMap() {
		return selectedMap;
	}



	public void setSelectedMap(String selectedMap) {
		this.selectedMap = selectedMap;
	}

	public List<MappaleDTO> getListaMappaliEnte() {
		return listaMappaliEnte;
	}

	public void setListaMappaliEnte(List<MappaleDTO> listaMappaliEnte) {
		this.listaMappaliEnte = listaMappaliEnte;
	}

	public String getSelectedMappale() {
		return selectedMappale;
	}

	public void setSelectedMappale(String selectedMappale) {
		this.selectedMappale = selectedMappale;
		
	/*	this.latitudine=null;
		this.longitudine=null;
		if(this.selectedMappale!=null){
			String[] fp = this.selectedMappale.split("\\|");
			CatastoService catastoService = this.getCatastOServiceBean();
			if(fp.length==3){
				RicercaOggettoCatDTO rc = new RicercaOggettoCatDTO();
				rc.setCodEnte(fp[0]);
				rc.setFoglio(fp[1]);
				rc.setParticella(fp[2]);
				this.fillUserData(rc);
				String[] latlon = catastoService.getLatitudineLongitudine(rc);
				this.latitudine = latlon[0];
				this.longitudine = latlon[1];
				
		   //     LatLng coord = new LatLng(Double.parseDouble(latitudine),Double.parseDouble(longitudine));
		       
		    //    simpleModel.addOverlay(new Marker(coord, selectedMappale));
				
			}
		}
	*/
		
	}

	public MapModel getSimpleModel() {
		return simpleModel;
	}

	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}

	public String[] getCentroCoord() {
		return centroCoord;
	}

	public void setCentroCoord(String[] centroCoord) {
		this.centroCoord = centroCoord;
	}

	public String getZoom() {
		return zoom;
	}

	public void setZoom(String zoom) {
		this.zoom = zoom;
	}

}
