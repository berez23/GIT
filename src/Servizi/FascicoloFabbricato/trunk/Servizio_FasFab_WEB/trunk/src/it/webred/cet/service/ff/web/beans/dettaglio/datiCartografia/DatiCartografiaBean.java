package it.webred.cet.service.ff.web.beans.dettaglio.datiCartografia;

import it.webred.cet.service.ff.web.beans.dettaglio.DatiDettaglio;
import it.webred.cet.service.ff.web.beans.dettaglio.datiTecnici.TipologiaPRG;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.data.access.basic.pgt.PGTService;
import it.webred.ct.data.access.basic.pgt.dto.CataloghiDataIn;
import it.webred.ct.data.access.basic.pgt.dto.DatoPgtDTO;
import it.webred.ct.data.access.basic.pgt.dto.RicercaPgtDTO;
import it.webred.ct.data.access.basic.pgt.dto.VisLayerSqlDTO;
import it.webred.ct.data.model.pgt.PgtSqlLayer;
import it.webred.ct.data.model.pgt.PgtSqlVisLayer;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;


public class DatiCartografiaBean extends DatiDettaglio implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private ParameterService parameterService;
	
	private PGTService pgtService;

	private List<VisLayerSqlDTO> listaLayer;
	private List<TipologiaPRG> listaPRG;

	public void doSwitch()
	{
		this.caricaListaLayer();
	}
	
	
	public void caricaListaLayer(){
		listaPRG = new ArrayList<TipologiaPRG>();
		
		logger.debug("DATI CARTOGRAFICI - DATA RIF: " + this.getDataRif());
		CataloghiDataIn dataIn= new CataloghiDataIn();
		dataIn.setEnteId(super.getEnte());
		dataIn.setUserId(super.getUsername());
		dataIn.setNomeApp("FasFabb");
		
		listaLayer = pgtService.getListaSqlVisLayerByApp(dataIn);
		
		for(VisLayerSqlDTO s : listaLayer){
			PgtSqlLayer layer = s.getLayer();
			
			if(s.getVisualizza()){
				//Recupero la modalità di interrogazione del layer per l'applicativo
				dataIn.setPkLayer(layer.getId());
				PgtSqlVisLayer pgtSqlVis = pgtService.getSqlVisLayerByLayerApp(dataIn);
				
				TipologiaPRG tabella = this.caricaDatiLayer(layer,pgtSqlVis);
				if(tabella!=null)
					listaPRG.add(tabella);
			}
			
		}
		
	}

	
	public TipologiaPRG caricaDatiLayer(PgtSqlLayer layer, PgtSqlVisLayer pgtSqlVis){
		TipologiaPRG tableTipologia = null;
		
		logger.debug("layer des " + layer.getDesLayer());
			
		List<List<String>> righeCorpoTabella = new ArrayList<List<String>>();
		List<String> rigaHeaderTabella = new ArrayList<String>() ;
		
		RicercaPgtDTO rpLayer = new RicercaPgtDTO();
		rpLayer.setId(layer.getId().getId());
		rpLayer.setStandard(layer.getId().getStandard());
		rpLayer.setCodNazionale(super.getCodNazionale());
		rpLayer.setSezione(super.getSezione());
		rpLayer.setParticella(super.getParticella());
		rpLayer.setFoglio(new Integer(super.getFoglio()));
		rpLayer.setEnteId(super.getEnte());
		rpLayer.setUserId(super.getUsername());
		rpLayer.setVisLayer(pgtSqlVis);
		
		//Imposto i parametri per la query alfanumerica
		String mask = "00000";		    
		rpLayer.addParam(super.getFoglio());
		rpLayer.addParam(mask.substring(0 , mask.length() - super.getParticella().length()) + super.getParticella());
		
		Hashtable<String, Object> tableLayer = (Hashtable<String, Object>)pgtService.getDatiPgtLayer(rpLayer);

		if(tableLayer!=null && tableLayer.size()>0)
		{
			Enumeration keys = tableLayer.keys();
			
			while (keys.hasMoreElements()) {
				
				Object key = keys.nextElement();
				Object value = tableLayer.get(key);
				if (key.equals("KEY_HASH_VALORI"))
				{
				  
				  ArrayList<ArrayList> listaRisultati = (ArrayList<ArrayList>)value;
				  for(Object dd:listaRisultati)
				  {	
					  List<String> rigaCorpoTabella = new ArrayList<String>() ;
					  
					  ArrayList listaValoriRiga = (ArrayList)dd;
					  for(Object valoreRiga:listaValoriRiga)
					  {
						  if (valoreRiga instanceof Number)
							  rigaCorpoTabella.add(((Number)valoreRiga).toString());
						  else if (valoreRiga instanceof String)
							  rigaCorpoTabella.add(((String)valoreRiga).toString());
						  else if (valoreRiga instanceof Date) {
							  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							  rigaCorpoTabella.add(sdf.format(valoreRiga));
						  }else {
							  logger.debug("NESSUN CAST VALIDO");
							  logger.debug("***** CLASSE ====" + valoreRiga.getClass().getName());
							  rigaCorpoTabella.add("???");
						  }
					  }
					  righeCorpoTabella.add(rigaCorpoTabella);
				  }
				 
				}
				else
				{
				  ArrayList<DatoPgtDTO> listaRis = (ArrayList<DatoPgtDTO>)value;
				  
				  for(DatoPgtDTO dd:listaRis)
				  {
					  rigaHeaderTabella.add(dd.getNomeDato());
				  }
				}
			  }
		}
		else
			logger.debug("loadPRG tableLayer NULL");

		//ADD TABELLA 
		tableTipologia = new TipologiaPRG();
		tableTipologia.setDescrizione(layer.getTipoLayer());
		tableTipologia.setDescrizioneLayer(layer.getDesLayer());
		tableTipologia.setListaColonne(rigaHeaderTabella);
		tableTipologia.setListaDati(righeCorpoTabella);

			
		return tableTipologia;
	}
	
	public ParameterService getParameterService() {
		return parameterService;
	}


	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}


	public PGTService getPgtService() {
		return pgtService;
	}


	public void setPgtService(PGTService pgtService) {
		this.pgtService = pgtService;
	}


	public List<VisLayerSqlDTO> getListaLayer() {
		return listaLayer;
	}


	public void setListaLayer(List<VisLayerSqlDTO> listaLayer) {
		this.listaLayer = listaLayer;
	}


	public List<TipologiaPRG> getListaPRG() {
		return listaPRG;
	}


	public void setListaPRG(List<TipologiaPRG> listaPRG) {
		this.listaPRG = listaPRG;
	}


}
