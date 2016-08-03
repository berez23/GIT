package it.bod.application.backing;

import it.bod.application.beans.DocfaClasseBean;

import it.bod.application.beans.DocfaFogliMicrozonaBean;
import it.bod.application.common.FilterItem;
import it.bod.business.service.BodLogicService;
import it.doro.tools.Character;
import it.persistance.common.SqlHandler;
import it.webred.rich.common.ComboBoxRch;
import it.webred.util.common.ApplicationResources;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

public class ClassamentoUnoBck extends ClassamentoBck {


	private static final long serialVersionUID = -4247484583460251013L;
	
	private static Logger logger = Logger.getLogger(ClassamentoUnoBck.class.getName());
	
	
	
	private ComboBoxRch cbxFoglio = null;
	private ComboBoxRch cbxZc = null;
	private HtmlOutputLabel outMicrozona = null;
	private String nuovaMicrozona=null;
	private HtmlOutputLabel outMappale = null;
	private ComboBoxRch cbxMappale = null;
	private HtmlSelectOneRadio radioCatEd = null;
	private HtmlSelectOneRadio radioTipoInt = null;
	private HtmlInputText txtConsistenza = null;
	private HtmlInputText txtSuperficie = null;
	private ComboBoxRch cbxAscensore = null;
	
	private HtmlOutputLabel outClasseA1 = null;
	private HtmlOutputLabel outClasseA2 = null;
	private HtmlOutputLabel outClasseA3 = null;
	private HtmlOutputLabel outClasseA4 = null;
	private HtmlOutputLabel outClasseA7 = null;
	private HtmlOutputLabel outClasseA8 = null;
	
	private HtmlOutputLabel outValoreCommercialeMq = null;
	private HtmlOutputLabel outValoreCommerciale = null;
	private HtmlOutputLabel outRenditaMinima = null;
	private HtmlOutputLabel outTariffaVano = null;
	
	
	
	private List<SelectItem> listaTipoCategoriaEdilizia= new ArrayList<SelectItem>();
	private List<SelectItem> listaTipologiaIntervento= new ArrayList<SelectItem>();
	private List<SelectItem> listaSiNo= new ArrayList<SelectItem>();
	private List<SelectItem> listaMappali= new ArrayList<SelectItem>();
	
	Hashtable<String, String> hashClassiMax= new Hashtable<String, String>();
	
	private BodLogicService bodLogicService = null;
	
	private boolean abilitaMappale=false;
	private boolean disabilitaAscensore=true;
	private boolean visualizzaOutput=false;
	
	private List<DocfaClasseBean> listaClassiCompatibili= new ArrayList<DocfaClasseBean>();
	private int numberOfRows=0;
	
	
	

	public String init(){
		
		
		String esito = "classamentoUnoBck.init";
		
		  cbxZc = new ComboBoxRch();
		  outMicrozona = new HtmlOutputLabel();
		  outMappale = new HtmlOutputLabel();
		  cbxMappale = new ComboBoxRch();
		  radioCatEd = new HtmlSelectOneRadio();
		  radioTipoInt = new HtmlSelectOneRadio();
		  txtConsistenza = new HtmlInputText();
		  txtSuperficie = new HtmlInputText();
		  cbxAscensore = new ComboBoxRch();
		  
		 outClasseA1 = new HtmlOutputLabel();
		 outClasseA2 = new HtmlOutputLabel();
		 outClasseA3 = new HtmlOutputLabel();
		 outClasseA4 = new HtmlOutputLabel();
		 outClasseA7 = new HtmlOutputLabel();
		 outClasseA8 = new HtmlOutputLabel();
			
			 outValoreCommercialeMq = new HtmlOutputLabel();
			outValoreCommerciale = new HtmlOutputLabel();
			 outRenditaMinima = new HtmlOutputLabel();
			 outTariffaVano = new HtmlOutputLabel();
			 
			 listaClassiCompatibili= new ArrayList<DocfaClasseBean>();
			 
			 
			
		  
		/*//valorizzo la lista dei Fogli
			cbxFoglio= new ComboBoxRch();
			Properties prop = SqlHandler.loadProperties(propName);
			String sql = prop.getProperty("qryDocfaFogli");
			Hashtable htQry = new Hashtable();
			htQry.put("queryString", sql);
			
			List<SelectItem> listaFogliCb=new ArrayList<SelectItem>();
			//Hashtable<String, String> htFogliDocfa = null;
			
			//if (Info.htFogliDocfa.isEmpty()){
			
			List<Object> listaFogli= super.getBodLogicService().getList(htQry);
			
			if (listaFogli != null){
			Iterator<Object> itFogli = listaFogli.iterator();
			
				while(itFogli.hasNext()){
					
					DocfaFogliMicrozonaBean fogliBean = (DocfaFogliMicrozonaBean)itFogli.next();
					
					//Info.htFogliDocfa.put(fogliBean.getFoglio(), fogliBean.getFoglio());
					String foglio= fogliBean.getFoglio();
					
					SelectItem si = new SelectItem();
					si.setValue(foglio);
					si.setLabel(foglio);
					//si.setDescription(foglio);
					listaFogliCb.add(si);
					//suggestions += "," + foglio;
					
					 cbxFoglio.setSelectItems(listaFogliCb);
					 cbxFoglio.setState("1");
					cbxFoglio.setWidth("200");
					
				
				}
				
				
			}
		*/

		//setto la lista delle categorie edilizie
		List<SelectItem> listaCategorieEdilizie= new ArrayList<SelectItem>();
		SelectItem catEd= new SelectItem();
		catEd.setLabel("Abitazioni Civili (A1,A2,A7,A8)");
		catEd.setValue("1");
		listaCategorieEdilizie.add(catEd);
		catEd= new SelectItem();
		catEd.setLabel("Abitazioni Economiche (A3,A4)");
		catEd.setValue("2");
		listaCategorieEdilizie.add(catEd);
		
		/*UISelectItems radioCatEdOptions = new UISelectItems();
		radioCatEdOptions.setValue(listaCategorieEdilizie);
		
		radioCatEd.getChildren().add(radioCatEdOptions);
		*/
		
		this.setListaTipoCategoriaEdilizia(listaCategorieEdilizie);
		
		radioCatEd.setValue("1");
		
		
		//this.listaTipoCategoriaEdilizia=listaCategorieEdilizie;
		
		//setto la lista delle tipologie intervento
		List<SelectItem> listaTipologiaIntervento= new ArrayList<SelectItem>();
		SelectItem tipInt= new SelectItem();
		tipInt.setLabel("Nuova Costruzione");
		tipInt.setValue("1");
		listaTipologiaIntervento.add(tipInt);
		tipInt= new SelectItem();
		tipInt.setLabel("Ristrutturazione");
		tipInt.setValue("2");
		listaTipologiaIntervento.add(tipInt);
		
		
		
		/*UISelectItems radioTipoIntOptions = new UISelectItems();
		radioTipoIntOptions.setValue(listaTipologiaIntervento);
		
		radioTipoInt.getChildren().add(radioTipoIntOptions);*/
		
		this.listaTipologiaIntervento=listaTipologiaIntervento;
		
		radioTipoInt.setValue("1");
		
		//setto la lista Si No
		List<SelectItem> listaSiNo= new ArrayList<SelectItem>();
		SelectItem item= new SelectItem();
		item.setLabel("S");
		item.setValue("SI");
		listaSiNo.add(item);
		item= new SelectItem();
		item.setLabel("N");
		item.setValue("NO");
		listaSiNo.add(item);
		
		cbxAscensore.setSelectItems(listaSiNo) ;
		cbxAscensore.setState("SI");
		
		/*List<SelectItem> listaZc= new ArrayList<SelectItem>();
		listaZc.add(new SelectItem("",""));
		cbxZc.setSelectItems(listaZc);
		
		cbxZc.setState("");
		
		List<SelectItem> listaMappali= new ArrayList<SelectItem>();
		listaMappali.add(new SelectItem("",""));
		cbxMappale.setSelectItems(listaMappali);
		
		cbxMappale.setState("");
		
		outMicrozona.setValue("");*/
		
		getListaZC();
		getMicrozona();
		
		abilitaMappale=false;
		outMappale.setValue(0);
		
		NumberFormat formatter = new DecimalFormat("#,##0.00");
				
		txtConsistenza.setValue(formatter.format(2));
		txtSuperficie.setValue(formatter.format(1));
		
		outValoreCommercialeMq.setValue(new Double(0));
		outValoreCommerciale.setValue(new Double(0));
		 outRenditaMinima.setValue(new Double(0));
		 outTariffaVano.setValue(new Double(0));
		 
		 numberOfRows=0;
		 
		 this.visualizzaOutput=false;
		 
		 resetForm();
		
		return esito;
	}
	
	public void resetForm(){
		
		NumberFormat formatter = new DecimalFormat("#,##0.00");
		
		cbxFoglio.setState("");
		cbxZc.setState("");
		cbxZc.setSelectItems(new ArrayList<SelectItem>());
		outMicrozona.setValue(null);
		outMappale.setValue(null);
		cbxMappale.setState("");
		cbxMappale.setSelectItems(new ArrayList<SelectItem>());
		radioCatEd.setValue("1");
		radioTipoInt.setValue("1");		
		txtConsistenza.setValue(formatter.format(2));
		txtSuperficie.setValue(formatter.format(1));
		cbxAscensore.setState("SI");
		nuovaMicrozona="";
		disabilitaAscensore=true;
				
	}
	
	/*//metodo che richiamo quando cambio il foglio
	public String selectionFoglioChanged() {
		String esito = "success";
		
		
		
		List<SelectItem> listaZC=this.getListaZC();
		
	
		if (listaZC != null){
			 cbxZc.setSelectItems(listaZC);
			 SelectItem first= listaZC.get(0);
			 cbxZc.setState(String.valueOf(first.getValue()));
			}
		cbxZc.setWidth("200");
		
		return esito;
	}
	*/
	
	public void selectFoglio(){
		
		String tipInt= (String)this.radioTipoInt.getValue();
		
		getListaZC();
		getMicrozona();
		
		if (tipInt!= null && tipInt.equals("2"))
			getListaMappali();
		
		this.visualizzaOutput=false;
			
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
		
	

		
	}//-----------------------------------------------------------------------------------------
	
	public void selectTipoIntervento(){
		
		String tipInt= (String)this.radioTipoInt.getValue();
		String catEdilizia= (String) this.radioCatEd.getValue();
		
		if (tipInt!= null && tipInt.equals("2")){
			
			this.abilitaMappale=true;
			getListaMappali();
			
			//se abitazioni civili disabilito cbxAscensore
			if (catEdilizia != null && catEdilizia.equals("1"))
				this.disabilitaAscensore=true;
			else if (catEdilizia != null && catEdilizia.equals("2"))
				this.disabilitaAscensore=false;
		}else{
			this.abilitaMappale=false;
			cbxMappale.setSelectItems(new ArrayList<SelectItem>());
			cbxMappale.setState("");
			outMappale.setValue(0);
			
			this.disabilitaAscensore=true;
		}
		
		this.visualizzaOutput=false;
		
	}
	
	public void selectCategoriaEdilizia(){
		
		String tipInt= (String)this.radioTipoInt.getValue();
		String catEdilizia= (String) this.radioCatEd.getValue();
		
			//se abitazioni civili disabilito cbxAscensore
			if (catEdilizia != null && catEdilizia.equals("1"))
				this.disabilitaAscensore=true;
			else if (catEdilizia != null && catEdilizia.equals("2")){
				
				if (tipInt!= null && tipInt.equals("2"))
					this.disabilitaAscensore=false;
				
				else
					this.disabilitaAscensore=true;
			}
			
			this.visualizzaOutput=false;
		
	}
	
	private void getListaMappali(){
		List<SelectItem> listaMappali=new ArrayList<SelectItem>();
		
		String foglio="";
		
		if (cbxFoglio!= null)
		 foglio = this.cbxFoglio.getState();
		
		if (foglio != null && !foglio.equals("")){
		
		Properties prop = SqlHandler.loadProperties(propName);
		String sql = prop.getProperty("qryDocfaMappali");
		sql += " and foglio= "+ foglio;
		sql += " order by particella";
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		
		
		List lstObj = bodLogicService.getList(htQry);
		
		if (lstObj!= null){
			for (int i=0; i< lstObj.size(); i++){
				String particella= String.valueOf(lstObj.get(i));
				
				SelectItem si = new SelectItem();
				si.setValue(particella);
				si.setLabel(particella);
				listaMappali.add(si);
			}
		}
		}
		cbxMappale.setSelectItems(listaMappali);
		if (listaMappali.size()>0)
		cbxMappale.setState((String)listaMappali.get(0).getValue());
		
		
	}
	
	public void hideOutPanel(){
		
		this.visualizzaOutput=false;
	}
	
	
	
	private Hashtable<String, String> getHashClassiMax(String foglio, String categoriaEdilizia, String mappale){
		
		Hashtable<String, String> hashClassiMax = null;
		
		Properties prop = SqlHandler.loadProperties(propName);
		String sql = prop.getProperty("qryDocfaClassiMax");
		sql +=  " and foglio= "+ foglio;
		sql +=  " and particella= '"+ mappale + "'";
		if (categoriaEdilizia.equals("1"))
			sql +=  " and categoria IN ('A01','A02','A07','A08')";
		else if (categoriaEdilizia.equals("2"))
			sql +=  " and categoria IN ('A03','A04')";
		sql += " group by categoria";
		
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		
		
		List lstObj = bodLogicService.getList(htQry);
		
		if (lstObj != null && lstObj.size()>0){
			hashClassiMax = new Hashtable<String, String>();
			for (int i=0; i<lstObj.size(); i++){
				Object[] objs = (Object[])lstObj.get(i);
				hashClassiMax.put(((String)objs[0]), ((String)objs[1]));
			}
		}
		this.hashClassiMax=hashClassiMax;
		
		return hashClassiMax;
	}
	
	private List<DocfaClasseBean> getListaClassiComp(){
		
		List<DocfaClasseBean> listaClassi= new ArrayList<DocfaClasseBean>();
		
		String zc= this.getCbxZc().getState();
		String categoriaEdilizia= (String)this.getRadioCatEd().getValue();
		String tipologiaIntervento= (String)this.getRadioTipoInt().getValue();
		String flgAscensore= (String)this.getCbxAscensore().getState();
		//String tariffaS= (String)outTariffaVano.getValue();
		String consistenzaS= (String)txtConsistenza.getValue();
		Double consistenza=new Double(0);
		Double tariffa= (Double)outTariffaVano.getValue();
		
		/*NumberFormat nf= NumberFormat.getInstance(Locale.ITALIAN);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);*/
		
	
		
		  if (consistenzaS.indexOf(",") > 0)
			  consistenzaS=consistenzaS.replace(",",".");
			
			consistenza=  Double.valueOf(consistenzaS);
			
/*			
		try{
			 tariffa= (Double)nf.parse(tariffaS);
			}
			catch(Exception e){
				
				
			}
	*/	
			
		listaClassi= bodLogicService.getListaClassiComp( zc,  categoriaEdilizia,  tipologiaIntervento,  hashClassiMax,  flgAscensore,  consistenza,  tariffa);
		
		
		return listaClassi;
	}
	
	/*   PER L'ALTRO BACKING BEAN
	private BigDecimal getClasseMin(String foglio, String zc, String categoria){
		BigDecimal classeMin= null;
		
		Hashtable htQry = new Hashtable();
		
		ArrayList<FilterItem> aryFilters = new ArrayList<FilterItem>();
		
		FilterItem fi = new FilterItem();
		fi.setOperatore("=");
		fi.setParametro("foglio");
		fi.setAttributo("foglio");
		fi.setValore(foglio);
		aryFilters.add(fi);
		
		fi = new FilterItem();
		fi.setOperatore("=");
		fi.setParametro("zc");
		fi.setAttributo("zc");
		fi.setValore(zc);
		aryFilters.add(fi);
		
		fi = new FilterItem();
		fi.setOperatore("=");
		fi.setParametro("categoria");
		fi.setAttributo("categoria");
		fi.setValore(categoria);
		aryFilters.add(fi);
		
		htQry.put("where", aryFilters);
		

		List lstObj = docfaService.getList(htQry, DocfaClasseMinBean.class );
		
		if (lstObj!= null){
			
			DocfaClasseMinBean docfaClasseMinBean= (DocfaClasseMinBean)lstObj.get(0);
			classeMin=  docfaClasseMinBean.getClasse_min();
			}
		
		
		return classeMin;
	}
	
	*/
	
	public void calcola() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		/*NumberFormat nf= NumberFormat.getInstance(Locale.ITALIAN);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);*/
		this.visualizzaOutput=true;
		
		Hashtable<String, String>  hashClassiMax=null;
		String foglio= this.cbxFoglio.getState();
		String catEd=(String)this.radioCatEd.getValue();
		String mappale=this.cbxMappale.getState();
		String nuovaMicrozona= this.getNuovaMicrozona();
		
		Double valoreCommMq= new Double(0);
		Double valoreComm=new Double(0);
		Double renditaMinima=new Double(0);
		Double tariffaVano=new Double(0);
		
		String tipoInt=(String)this.radioTipoInt.getValue();
		
		//se sono nel caso di ristrutturazione calcolo le classi max per categoria in base al mappale
		 if (tipoInt != null && tipoInt.equals("2") && foglio!= null && !foglio.equals("")){
			 Hashtable<String, String> hashClassi= this.getHashClassiMax(foglio, catEd, mappale);
			 
			 if (hashClassi!= null){
				 
				 outClasseA1.setValue(hashClassi.get("A01"));
				 outClasseA2.setValue(hashClassi.get("A02"));
				 outClasseA3.setValue(hashClassi.get("A03"));
				 outClasseA4.setValue(hashClassi.get("A04"));
				 outClasseA7.setValue(hashClassi.get("A07"));
				 outClasseA8.setValue(hashClassi.get("A08"));
			 }
			 else{
				 //visualizzo messaggio di alert
				   FacesMessage message = new FacesMessage();
				      message.setSeverity(FacesMessage.SEVERITY_WARN);
				      message.setSummary(ApplicationResources.getMessageResourceString(context.getApplication()
								.getMessageBundle(), "msgNoUiCategorie", null, context.getViewRoot()
								.getLocale()));
				      
				      context.addMessage (null, message); 
			 }
			 
		 }
		 
		 //ricavo il valore commerciale 
		 
		 Double valoreCommercialeMq= new Double(0); 
		 String tipologiaEdilizia="";
		 String stato="";
		 
		 
		 if (catEd!= null && catEd.equals("1"))
			 tipologiaEdilizia="ABITAZIONI CIVILI";
		 if (catEd!= null && catEd.equals("2"))
			 tipologiaEdilizia="ABITAZIONI TIPO ECONOMICO";
		 
		 if (tipoInt != null && tipoInt.equals("1"))
				 stato="Ottimo";
		 if (tipoInt != null && tipoInt.equals("2"))
				 stato="Normale";
		 
		 
		 BigDecimal valoreCommMqBD=bodLogicService.getValoreCommercialeMq(Character.fillUpZeroInFront( nuovaMicrozona, 3), tipologiaEdilizia,  stato);
		 if (valoreCommMqBD!=null)
			 valoreCommMq= new Double(String.valueOf(valoreCommMqBD));
			 
		 outValoreCommercialeMq.setValue(valoreCommMq);
		 
		 /*String valoreCommercialeMqS= nf.format(valoreCommMqBD);
		 outValoreCommercialeMq.setValue(valoreCommercialeMqS);*/
		
		
		Double consistenzaD= new Double(0);
		Double superficieD= new Double(0);
		
			String consistenzaS= (String)txtConsistenza.getValue();
			  if (consistenzaS.indexOf(",") > 0)
				  consistenzaS=consistenzaS.replace(",",".");
			
			consistenzaD=  Double.valueOf(consistenzaS);
			
			String superficieS= (String)txtSuperficie.getValue();
			  if (superficieS.indexOf(",") > 0)
				  superficieS=superficieS.replace(",",".");
			
			  superficieD=  Double.valueOf(superficieS);
			  
			  valoreComm= new Double(valoreCommMq.doubleValue() * superficieD.doubleValue());
			  
			  //outValoreCommerciale.setValue(nf.format(valoreComm));
			  outValoreCommerciale.setValue(valoreComm);
			  
			  Double docfaRapporto= bodLogicService.getDocfaRapporto();
			  
			  double denom= docfaRapporto.doubleValue() * 105;  
			  renditaMinima= new Double(valoreComm.doubleValue() / denom);
			  
			  //outRenditaMinima.setValue(nf.format(renditaMinima));
			  outRenditaMinima.setValue(renditaMinima);
			  
			  tariffaVano= new Double(renditaMinima/consistenzaD);
			  
			 // outTariffaVano.setValue(nf.format(tariffaVano));
			  outTariffaVano.setValue(tariffaVano);
			

			  
			  //ricavo la lista delle classi compatibili con l'intervento
			  
			  List<DocfaClasseBean> listaClassiCompatibili= this.getListaClassiComp();
			  this.numberOfRows=listaClassiCompatibili.size();

		this.listaClassiCompatibili=listaClassiCompatibili;
		
	}
	
	

	public void validateConsistenza(FacesContext context, UIComponent component, Object numero) throws ValidatorException {
	    Double d = null;
	    
	   

	    String numeroS= (String)numero;
	    
	    int i=numeroS.indexOf(",");
	    
	    if (numeroS.indexOf(",") > 0)
	    		numeroS=numeroS.replace(",",".");
	    
	    
	    //verifico se è un numero
	    try {
	      d = Double.valueOf(numeroS);
	    } catch (NumberFormatException e) {
	      FacesMessage message = new FacesMessage();
	      message.setSeverity(FacesMessage.SEVERITY_ERROR);
	      message.setSummary(ApplicationResources.getMessageResourceString(context.getApplication()
					.getMessageBundle(), "msgValoreNumerico", null, context.getViewRoot()
					.getLocale()));
	      throw new ValidatorException(message);
	    }
	    
	    if (d.doubleValue() <= 1){
	    	
	    	Object[] params= new Object[1];
	    	params[0]= new Integer(1);
	    	
	    	 FacesMessage message = new FacesMessage();
		      message.setSeverity(FacesMessage.SEVERITY_ERROR);
		      message.setSummary(ApplicationResources.getMessageResourceString(context.getApplication()
						.getMessageBundle(), "msgValoreMaggioreDi", params, context.getViewRoot()
						.getLocale()));
		      throw new ValidatorException(message);
	    }
	    
	  }
	
	/*public void validateConsistenza(FacesContext context, UIComponent component, Object numero) throws ValidatorException {
	    
		NumberFormat nf= NumberFormat.getInstance(Locale.ITALIAN);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		
	
	    String consistenzaS= (String)numero;
		Double consistenza=new Double(0);
		Long consistenzaL= new Long(0);
	    
	    
	    //verifico se è un numero
	    
	    try{
			 consistenza= (Double)nf.parse(consistenzaS);
			}
			catch(Exception e){
				
			
			try{
				 consistenzaL= (Long)nf.parse(consistenzaS);
				 consistenza= new Double(consistenzaL.doubleValue());
				}
				catch(Exception ex){
					FacesMessage message = new FacesMessage();
				      message.setSeverity(FacesMessage.SEVERITY_ERROR);
				      message.setSummary("Devi inserire un numero");
				      throw new ValidatorException(message);
				}
			}
	    
	 
	    
	    if (consistenza.doubleValue() <= 1){
	    	 FacesMessage message = new FacesMessage();
		      message.setSeverity(FacesMessage.SEVERITY_ERROR);
		      message.setSummary("Il valore deve essere maggiore di 1 ");
		      throw new ValidatorException(message);
	    }
	    
	  }*/
	
/*public void validateConsistenza(FacesContext context, UIComponent component, Object numero) throws ValidatorException {
	    
		
	
	   
		Double consistenza=new Double(0);
		Long consistenzaL= new Long(0);
	
	 //verifico se è un numero
    
    try{
		 consistenza= (Double)numero;
		}
		catch(Exception e){
			
		
		try{
			 consistenzaL= (Long)numero;
			 consistenza= new Double(consistenzaL.doubleValue());
			}
			catch(Exception ex){
				FacesMessage message = new FacesMessage();
			      message.setSeverity(FacesMessage.SEVERITY_ERROR);
			      message.setSummary("Devi inserire un numero");
			      throw new ValidatorException(message);
			}
		}
    
 
    
    if (consistenza.doubleValue() <= 1){
    	 FacesMessage message = new FacesMessage();
	      message.setSeverity(FacesMessage.SEVERITY_ERROR);
	      message.setSummary("Il valore deve essere maggiore di 1 ");
	      throw new ValidatorException(message);
    }
    
  }
	*/
	
	public void validateSuperficie(FacesContext context, UIComponent component, Object numero) throws ValidatorException {
	    Double d = null;
	    
	    String numeroS= (String)numero;
	    
	    int i=numeroS.indexOf(",");
	    
	    if (numeroS.indexOf(",") > 0)
	    		numeroS=numeroS.replace(",",".");
	    
	    
	    //verifico se è un numero
	    try {
		      d = Double.valueOf(numeroS);
		    } catch (NumberFormatException e) {
		      FacesMessage message = new FacesMessage();
		      message.setSeverity(FacesMessage.SEVERITY_ERROR);
		      message.setSummary(ApplicationResources.getMessageResourceString(context.getApplication()
						.getMessageBundle(), "msgValoreNumerico", null, context.getViewRoot()
						.getLocale()));
		      throw new ValidatorException(message);
		    }
		    
		    if (d.doubleValue() <= 0){
		    	
		    	Object[] params= new Object[1];
		    	params[0]= new Integer(0);
		    	
		    	 FacesMessage message = new FacesMessage();
			      message.setSeverity(FacesMessage.SEVERITY_ERROR);
			      message.setSummary(ApplicationResources.getMessageResourceString(context.getApplication()
							.getMessageBundle(), "msgValoreMaggioreDi", params, context.getViewRoot()
							.getLocale()));
			      throw new ValidatorException(message);
		    }
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


	public HtmlInputText getTxtConsistenza() {
		return txtConsistenza;
	}



	public void setTxtConsistenza(HtmlInputText txtConsistenza) {
		this.txtConsistenza = txtConsistenza;
	}



	public HtmlInputText getTxtSuperficie() {
		return txtSuperficie;
	}



	public void setTxtSuperficie(HtmlInputText txtSuperficie) {
		this.txtSuperficie = txtSuperficie;
	}



	public ComboBoxRch getCbxAscensore() {
		return cbxAscensore;
	}



	public void setCbxAscensore(ComboBoxRch cbxAscensore) {
		this.cbxAscensore = cbxAscensore;
	}

	public HtmlSelectOneRadio getRadioCatEd() {
		return radioCatEd;
	}

	public void setRadioCatEd(HtmlSelectOneRadio radioCatEd) {
		this.radioCatEd = radioCatEd;
	}

	public HtmlSelectOneRadio getRadioTipoInt() {
		return radioTipoInt;
	}

	public void setRadioTipoInt(HtmlSelectOneRadio radioTipoInt) {
		this.radioTipoInt = radioTipoInt;
	}

	public BodLogicService getBodLogicService() {
		return bodLogicService;
	}

	public void setBodLogicService(BodLogicService bodLogicService) {
		this.bodLogicService = bodLogicService;
	}

	public void setListaMappali(List<SelectItem> listaMappali) {
		this.listaMappali = listaMappali;
	}

	public HtmlOutputLabel getOutMappale() {
		return outMappale;
	}

	public void setOutMappale(HtmlOutputLabel outMappale) {
		this.outMappale = outMappale;
	}

	public ComboBoxRch getCbxMappale() {
		return cbxMappale;
	}

	public void setCbxMappale(ComboBoxRch cbxMappale) {
		this.cbxMappale = cbxMappale;
	}

	public boolean isAbilitaMappale() {
		return abilitaMappale;
	}

	public void setAbilitaMappale(boolean abilitaMappale) {
		this.abilitaMappale = abilitaMappale;
	}

	public boolean isDisabilitaAscensore() {
		return disabilitaAscensore;
	}

	public void setDisabilitaAscensore(boolean disabilitaAscensore) {
		this.disabilitaAscensore = disabilitaAscensore;
	}
	
	public List<SelectItem> getListaTipoCategoriaEdilizia() {
		return listaTipoCategoriaEdilizia;
	}

	public void setListaTipoCategoriaEdilizia(
			List<SelectItem> listaTipoCategoriaEdilizia) {
		this.listaTipoCategoriaEdilizia = listaTipoCategoriaEdilizia;
	}

	public List<SelectItem> getListaTipologiaIntervento() {
		return listaTipologiaIntervento;
	}

	public void setListaTipologiaIntervento(
			List<SelectItem> listaTipologiaIntervento) {
		this.listaTipologiaIntervento = listaTipologiaIntervento;
	}

	public List<SelectItem> getListaSiNo() {
		return listaSiNo;
	}

	public void setListaSiNo(List<SelectItem> listaSiNo) {
		this.listaSiNo = listaSiNo;
	}

	public HtmlOutputLabel getOutClasseA1() {
		return outClasseA1;
	}

	public void setOutClasseA1(HtmlOutputLabel outClasseA1) {
		this.outClasseA1 = outClasseA1;
	}

	public HtmlOutputLabel getOutClasseA2() {
		return outClasseA2;
	}

	public void setOutClasseA2(HtmlOutputLabel outClasseA2) {
		this.outClasseA2 = outClasseA2;
	}

	public HtmlOutputLabel getOutClasseA3() {
		return outClasseA3;
	}

	public void setOutClasseA3(HtmlOutputLabel outClasseA3) {
		this.outClasseA3 = outClasseA3;
	}

	public HtmlOutputLabel getOutClasseA4() {
		return outClasseA4;
	}

	public void setOutClasseA4(HtmlOutputLabel outClasseA4) {
		this.outClasseA4 = outClasseA4;
	}

	public HtmlOutputLabel getOutClasseA7() {
		return outClasseA7;
	}

	public void setOutClasseA7(HtmlOutputLabel outClasseA7) {
		this.outClasseA7 = outClasseA7;
	}

	public HtmlOutputLabel getOutClasseA8() {
		return outClasseA8;
	}

	public void setOutClasseA8(HtmlOutputLabel outClasseA8) {
		this.outClasseA8 = outClasseA8;
	}

	public String getNuovaMicrozona() {
		return nuovaMicrozona;
	}

	public void setNuovaMicrozona(String nuovaMicrozona) {
		this.nuovaMicrozona = nuovaMicrozona;
	}

	public HtmlOutputLabel getOutRenditaMinima() {
		return outRenditaMinima;
	}

	public void setOutRenditaMinima(HtmlOutputLabel outRenditaMinima) {
		this.outRenditaMinima = outRenditaMinima;
	}

	public HtmlOutputLabel getOutTariffaVano() {
		return outTariffaVano;
	}

	public void setOutTariffaVano(HtmlOutputLabel outTariffaVano) {
		this.outTariffaVano = outTariffaVano;
	}

	public HtmlOutputLabel getOutValoreCommercialeMq() {
		return outValoreCommercialeMq;
	}

	public void setOutValoreCommercialeMq(HtmlOutputLabel outValoreCommercialeMq) {
		this.outValoreCommercialeMq = outValoreCommercialeMq;
	}

	public HtmlOutputLabel getOutValoreCommerciale() {
		return outValoreCommerciale;
	}

	public void setOutValoreCommerciale(HtmlOutputLabel outValoreCommerciale) {
		this.outValoreCommerciale = outValoreCommerciale;
	}

	public Hashtable<String, String> getHashClassiMax() {
		return hashClassiMax;
	}

	public void setHashClassiMax(Hashtable<String, String> hashClassiMax) {
		this.hashClassiMax = hashClassiMax;
	}

	public void setListaClassiCompatibili(
			List<DocfaClasseBean> listaClassiCompatibili) {
		this.listaClassiCompatibili = listaClassiCompatibili;
	}

	public List<DocfaClasseBean> getListaClassiCompatibili(){
		return listaClassiCompatibili;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public boolean isVisualizzaOutput() {
		return visualizzaOutput;
	}

	public void setVisualizzaOutput(boolean visualizzaOutput) {
		this.visualizzaOutput = visualizzaOutput;
	}



}
