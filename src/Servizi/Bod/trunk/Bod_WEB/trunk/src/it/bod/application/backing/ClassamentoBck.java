package it.bod.application.backing;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import it.bod.application.common.MasterClass;
import it.bod.business.service.BodLogicService;

import it.persistance.common.SqlHandler;
import it.webred.rich.common.ComboBoxRch;

public class ClassamentoBck extends MasterClass {

	private static final long serialVersionUID = 5966787541689198048L;


	private static Logger logger = Logger.getLogger("it.bod.application.backing.ClassamentoBck");
	
//	private DocfaService docfaService = null;
	protected BodLogicService bodLogicService = null;
	protected ComboBoxRch cbxFoglio = null;
	
	
	
	

	public String init(){
		
		String esito = "classamentoBck.init";
		
		
		//valorizzo la lista dei Fogli
		cbxFoglio= new ComboBoxRch();
		Properties prop = SqlHandler.loadProperties(propName);
		String sql = prop.getProperty("qryDocfaFogli");
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		
		List<SelectItem> listaFogliCb=new ArrayList<SelectItem>();
		//Hashtable<String, String> htFogliDocfa = null;
		
		//if (Info.htFogliDocfa.isEmpty()){
		
		List listaFogli=  bodLogicService.getList(htQry);
		
		if (listaFogli != null){
		for (int i=0; i<listaFogli.size();i++){
				//DocfaFogliMicrozonaBean fogliBean = (DocfaFogliMicrozonaBean)itFogli.next();
				
				//Info.htFogliDocfa.put(fogliBean.getFoglio(), fogliBean.getFoglio());
				//String foglio= fogliBean.getFoglio();
				String objs = (String)listaFogli.get(i);
				
				SelectItem si = new SelectItem();
				si.setValue(objs);
				si.setLabel(objs);
				//si.setDescription(foglio);
				listaFogliCb.add(si);
				//suggestions += "," + foglio;
				
				
				
			}
		 cbxFoglio.setSelectItems(listaFogliCb);
		 if (listaFogliCb.size()>0)
		 cbxFoglio.setState((String)listaFogliCb.get(0).getValue());
		cbxFoglio.setWidth("200");
		
			
		}
		return esito;

	}//-------------------------------------------------------------------------
	
	

	public BodLogicService getBodLogicService() {
		return bodLogicService;
	}

	public void setBodLogicService(BodLogicService bodLogicService) {
		this.bodLogicService = bodLogicService;
	}



	public ComboBoxRch getCbxFoglio() {
		return cbxFoglio;
	}



	public void setCbxFoglio(ComboBoxRch cbxFoglio) {
		this.cbxFoglio = cbxFoglio;
	}


}
