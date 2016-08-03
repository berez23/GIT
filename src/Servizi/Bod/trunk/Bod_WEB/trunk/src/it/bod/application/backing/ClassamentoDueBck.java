package it.bod.application.backing;

import it.bod.application.beans.DocfaClasseBean;

import it.bod.application.beans.DocfaFogliMicrozonaBean;
import it.bod.application.common.FilterItem;
import it.bod.business.service.BodLogicService;
import it.doro.tools.Character;
import it.persistance.common.SqlHandler;
import it.webred.rich.common.ComboBoxRch;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

public class ClassamentoDueBck extends ClassamentoBck {

	private static final long serialVersionUID = -4247484583460251013L;
	private static Logger logger = Logger.getLogger("it.bod.application.backing.ClassamentoDueBck");
	
	
	
	private ComboBoxRch cbxFoglio = null;
	private ComboBoxRch cbxZc = null;
	private HtmlOutputLabel outMicrozona = null;
	private String nuovaMicrozona=null;

	private HtmlSelectOneRadio radioCatEd = null;
	
	
	
	
	private HtmlOutputLabel outValoreCommercialeMq = null;
	private HtmlOutputLabel outRenditaMinimaMq = null;
	private HtmlOutputLabel outTariffaVano = null;
	
	
	
	private List<SelectItem> listaTipoCategoriaEdilizia= new ArrayList<SelectItem>();
	
	private BodLogicService bodLogicService = null;
	
	
	
	private List<DocfaClasseBean> listaClassiCompatibili= new ArrayList<DocfaClasseBean>();
	private int numberOfRows=0;
	
	private String stato="";
	
	
	private boolean visualizzaOutput;

	public String initDue(){
		
		
		String esito = "classamentoDueBck.init";
		
		String stato="Ottimo";
		
		this.stato=stato;
		
		  cbxZc = new ComboBoxRch();
		  outMicrozona = new HtmlOutputLabel();
		  radioCatEd = new HtmlSelectOneRadio();
		  
			
			 outValoreCommercialeMq = new HtmlOutputLabel();
			 outRenditaMinimaMq = new HtmlOutputLabel();
			 outTariffaVano = new HtmlOutputLabel();
			 
			 listaClassiCompatibili= new ArrayList<DocfaClasseBean>();
			 

		//setto la lista delle categorie edilizie
		List<SelectItem> listaCategorieEdilizie= new ArrayList<SelectItem>();
		SelectItem catEd= new SelectItem();
		catEd.setLabel("Abitazioni Civili (A1,A8)");
		catEd.setValue("1");
		listaCategorieEdilizie.add(catEd);
		catEd= new SelectItem();
		catEd.setLabel("Abitazioni Civili (A2,A7)");
		catEd.setValue("2");
		listaCategorieEdilizie.add(catEd);
		catEd= new SelectItem();
		catEd.setLabel("Abitazioni Economiche (A3)");
		catEd.setValue("3");
		listaCategorieEdilizie.add(catEd);
		
		/*UISelectItems radioCatEdOptions = new UISelectItems();
		radioCatEdOptions.setValue(listaCategorieEdilizie);
		
		radioCatEd.getChildren().add(radioCatEdOptions);
		*/
		
		this.setListaTipoCategoriaEdilizia(listaCategorieEdilizie);
		
		radioCatEd.setValue("1");
		
					
		
		getListaZC();
		getMicrozona();
		
		
		
		NumberFormat formatter = new DecimalFormat("#,##0.00");
				
		
		outValoreCommercialeMq.setValue(new Double(0));
		 outRenditaMinimaMq.setValue(new Double(0));
		 outTariffaVano.setValue(new Double(0));
		 
		 numberOfRows=0;
		 
		 visualizzaOutput=false;
		 
		 resetForm();
		
		return esito;
	}
	
	
public String initTre(){
		
		//String esito = "classamentoDue";
		String esito = "classamentoDueBck.init";
		
		
		String stato="Normale";
		
		this.stato=stato;
		
		cbxZc = new ComboBoxRch();
		outMicrozona = new HtmlOutputLabel();
		radioCatEd = new HtmlSelectOneRadio();
		  
			
		 outValoreCommercialeMq = new HtmlOutputLabel();
		 outRenditaMinimaMq = new HtmlOutputLabel();
		 outTariffaVano = new HtmlOutputLabel();
		 
		 listaClassiCompatibili= new ArrayList<DocfaClasseBean>();
			 
			 
			
		  
		

		//setto la lista delle categorie edilizie
		List<SelectItem> listaCategorieEdilizie= new ArrayList<SelectItem>();
		SelectItem catEd= new SelectItem();
		catEd.setLabel("Abitazioni Civili (A1,A8)");
		catEd.setValue("1");
		listaCategorieEdilizie.add(catEd);
		catEd= new SelectItem();
		catEd.setLabel("Abitazioni Civili (A2,A7)");
		catEd.setValue("2");
		listaCategorieEdilizie.add(catEd);
		catEd= new SelectItem();
		catEd.setLabel("Abitazioni Economiche (A3)");
		catEd.setValue("3");
		listaCategorieEdilizie.add(catEd);
		catEd= new SelectItem();
		catEd.setLabel("Abitazioni Economiche (A4,A5)");
		catEd.setValue("4");
		listaCategorieEdilizie.add(catEd);
		
		/*UISelectItems radioCatEdOptions = new UISelectItems();
		radioCatEdOptions.setValue(listaCategorieEdilizie);
		
		radioCatEd.getChildren().add(radioCatEdOptions);
		*/
		
		this.setListaTipoCategoriaEdilizia(listaCategorieEdilizie);
		
		radioCatEd.setValue("1");
		
					
		
		getListaZC();
		getMicrozona();
		
		
		
		//NumberFormat formatter = new DecimalFormat("#,##0.00");
				
		
		outValoreCommercialeMq.setValue(new Double(0));
		 outRenditaMinimaMq.setValue(new Double(0));
		 outTariffaVano.setValue(new Double(0));
		 
		 numberOfRows=0;
		 
		 visualizzaOutput=false;
		 
		 resetForm();
		
		return esito;
	}
	

	public void resetForm(){
		
		
		cbxFoglio.setState("");
		cbxZc.setState("");
		cbxZc.setSelectItems(new ArrayList<SelectItem>());
		outMicrozona.setValue(null);
		
		radioCatEd.setValue("1");
		
		nuovaMicrozona="";
				
	}
	
	public void hideOutPanel(){
		
		this.visualizzaOutput=false;
	}
	
	
	public void selectFoglio(){
		
		
		
		getListaZC();
		getMicrozona();
		
		visualizzaOutput=false;
		
		
	}
	
	private  void getListaZC(){
		List<SelectItem> listaZC=new ArrayList<SelectItem>();
		
		//ComboBoxRch cbxFoglio=this.cbxFoglio;
		
		if (cbxFoglio!= null){
		String foglio = cbxFoglio.getState();
		
		listaZC= bodLogicService.getListaZC(foglio);
		}
		if (listaZC != null && listaZC.size()>0){
			

			
			 cbxZc.setSelectItems( listaZC );
			 SelectItem first= listaZC.get(0);
			 cbxZc.setState(String.valueOf(first.getValue()));
			 //cbxZc.setWidth("50");
			 //cbxZc.setListHeight("10");
			}
		
			
	}
	
	public void getMicrozona(){
		
		String foglio = "";
		String zc = "";
		
		if (cbxFoglio != null && cbxZc!= null){
			
		 foglio = this.cbxFoglio.getState();
		 zc = this.cbxZc.getState();
		}	
		String microzona= "";
		String newMicrozona="";
		
		microzona= bodLogicService.getMicrozona(foglio, zc);
					
		if (microzona!= null && !microzona.equals("")){
			String[] microzonaArr=microzona.split("-");
			if (microzonaArr != null && microzonaArr.length>1){
				newMicrozona= microzonaArr[1];
			}
				
		}
			
		this.outMicrozona.setValue(microzona);
			
		if (newMicrozona != null)
		this.nuovaMicrozona=newMicrozona.trim();
		
		this.visualizzaOutput=false;			
		
	

		
	}
	
	
	

	
	
	
	
	
	private List<DocfaClasseBean> getListaClassiComp(){
		
		List<DocfaClasseBean> listaClassi= new ArrayList<DocfaClasseBean>();
		
		String zc= this.getCbxZc().getState();
		String categoriaEdilizia= (String)this.getRadioCatEd().getValue();
		//String tariffaS= (String)outTariffaVano.getValue();

		Double tariffa= (Double)outTariffaVano.getValue();
		
		/*NumberFormat nf= NumberFormat.getInstance(Locale.ITALIAN);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		
		
			
		try{
			 tariffa= (Double)nf.parse(tariffaS);
			}
			catch(Exception e){
				
				
			}
		*/
			
		listaClassi= bodLogicService.getListaClassiComp(zc, categoriaEdilizia, tariffa);
		return listaClassi;
	}
	
	
	
	public void calcola(){
		
		NumberFormat nf= NumberFormat.getInstance(Locale.ITALIAN);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		
		
		Hashtable<String, String>  hashClassiMax=null;
		String foglio= this.cbxFoglio.getState();
		String catEd=(String)this.radioCatEd.getValue();

		String nuovaMicrozona= this.getNuovaMicrozona();
		
		Double valoreCommMq= new Double(0);
		Double renditaMinimaMq=new Double(0);
		Double tariffaVano=new Double(0);
		
		 /*
		  * ricavo il valore commerciale 
		  */
		 
		 Double valoreCommercialeMq= new Double(0); 
		 String tipologiaEdilizia="";
		 String stato=this.stato;
		 
		 
		 if (catEd!= null && (catEd.equals("1") || catEd.equals("2")))
			 tipologiaEdilizia="ABITAZIONI CIVILI";
		 if (catEd!= null && (catEd.equals("3") || catEd.equals("4")))
			 tipologiaEdilizia="ABITAZIONI TIPO ECONOMICO";
		 
		
		 
		 BigDecimal valoreCommMqBD=bodLogicService.getValoreCommercialeMq(Character.fillUpZeroInFront( nuovaMicrozona, 3), tipologiaEdilizia,  stato);
		 if (valoreCommMqBD!=null)
			 valoreCommMq= new Double(String.valueOf(valoreCommMqBD));
			 
		 outValoreCommercialeMq.setValue(valoreCommMq);
		 
		 /*String valoreCommercialeMqS= nf.format(valoreCommMqBD);
		 outValoreCommercialeMq.setValue(valoreCommercialeMqS);*/
		
		
		 Double docfaRapporto= bodLogicService.getDocfaRapporto();
		  
		  double denom= docfaRapporto.doubleValue() * 105;  
			  
			
			  renditaMinimaMq= new Double(valoreCommMq.doubleValue() / denom);
			  
			  //outRenditaMinimaMq.setValue(nf.format(renditaMinimaMq));
			  
			  outRenditaMinimaMq.setValue(renditaMinimaMq);
			  
			  double superficieMedia=0;
			  
			  if (catEd!= null && catEd.equals("1"))
				  superficieMedia=24;
			  else if (catEd!= null && catEd.equals("2"))
				  superficieMedia=19;
			  else if (catEd!= null && catEd.equals("3"))
				  superficieMedia=18;
			  else if (catEd!= null && catEd.equals("4"))
				  superficieMedia=19;
			  
			  
			  
			  
			  tariffaVano= new Double(renditaMinimaMq*superficieMedia);
			  
			  //outTariffaVano.setValue(nf.format(tariffaVano));
			  outTariffaVano.setValue(tariffaVano);
			

			  
			  //ricavo la lista delle classi compatibili con l'intervento
			  
			  List<DocfaClasseBean> listaClassiCompatibili= this.getListaClassiComp();
			  this.numberOfRows=listaClassiCompatibili.size();

		this.listaClassiCompatibili=listaClassiCompatibili;
		
		this.visualizzaOutput=true;
		
	}
	
	
	
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ComboBoxRch getCbxFoglio() {
		return cbxFoglio;
	}

	public void setCbxFoglio(ComboBoxRch cbxFoglio) {
		this.cbxFoglio = cbxFoglio;
	}

	public ComboBoxRch getCbxZc() {
		return cbxZc;
	}

	public void setCbxZc(ComboBoxRch cbxZc) {
		this.cbxZc = cbxZc;
	}

	public HtmlOutputLabel getOutMicrozona() {
		return outMicrozona;
	}

	public void setOutMicrozona(HtmlOutputLabel outMicrozona) {
		this.outMicrozona = outMicrozona;
	}

	public String getNuovaMicrozona() {
		return nuovaMicrozona;
	}

	public void setNuovaMicrozona(String nuovaMicrozona) {
		this.nuovaMicrozona = nuovaMicrozona;
	}

	public HtmlSelectOneRadio getRadioCatEd() {
		return radioCatEd;
	}

	public void setRadioCatEd(HtmlSelectOneRadio radioCatEd) {
		this.radioCatEd = radioCatEd;
	}

	public HtmlOutputLabel getOutValoreCommercialeMq() {
		return outValoreCommercialeMq;
	}

	public void setOutValoreCommercialeMq(HtmlOutputLabel outValoreCommercialeMq) {
		this.outValoreCommercialeMq = outValoreCommercialeMq;
	}

	public HtmlOutputLabel getOutRenditaMinimaMq() {
		return outRenditaMinimaMq;
	}

	public void setOutRenditaMinimaMq(HtmlOutputLabel outRenditaMinimaMq) {
		this.outRenditaMinimaMq = outRenditaMinimaMq;
	}

	public HtmlOutputLabel getOutTariffaVano() {
		return outTariffaVano;
	}

	public void setOutTariffaVano(HtmlOutputLabel outTariffaVano) {
		this.outTariffaVano = outTariffaVano;
	}

	public List<SelectItem> getListaTipoCategoriaEdilizia() {
		return listaTipoCategoriaEdilizia;
	}

	public void setListaTipoCategoriaEdilizia(
			List<SelectItem> listaTipoCategoriaEdilizia) {
		this.listaTipoCategoriaEdilizia = listaTipoCategoriaEdilizia;
	}

	public BodLogicService getBodLogicService() {
		return bodLogicService;
	}

	public void setBodLogicService(BodLogicService bodLogicService) {
		this.bodLogicService = bodLogicService;
	}

	public List<DocfaClasseBean> getListaClassiCompatibili() {
		return listaClassiCompatibili;
	}

	public void setListaClassiCompatibili(
			List<DocfaClasseBean> listaClassiCompatibili) {
		this.listaClassiCompatibili = listaClassiCompatibili;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}


	public String getStato() {
		return stato;
	}


	public void setStato(String stato) {
		this.stato = stato;
	}


	public boolean isVisualizzaOutput() {
		return visualizzaOutput;
	}


	public void setVisualizzaOutput(boolean visualizzaOutput) {
		this.visualizzaOutput = visualizzaOutput;
	}
	
	


	





}
