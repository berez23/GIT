package it.bod.application.backing;

import it.bod.application.beans.DocfaClasseMinBean;
import it.bod.application.beans.DocfaFogliMicrozonaBean;
import it.bod.application.common.FilterItem;
import it.bod.business.service.BodLogicService;
import it.persistance.common.SqlHandler;
import it.webred.rich.common.ComboBoxRch;

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

public class ClassamentoTreBck extends ClassamentoBck {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4247484583460251013L;
	private static Logger logger = Logger.getLogger("it.bod.application.backing.ClassamentoTreBck");
	
	
	
	private ComboBoxRch cbxFoglio = null;
	private ComboBoxRch cbxZc = null;
	private HtmlOutputLabel outMicrozona = null;
	private String nuovaMicrozona=null;

	private HtmlSelectOneRadio radioCatEd = null;
	private ComboBoxRch cbxPostoAuto = null;
	
	private HtmlOutputLabel outValoreClasse = null;
		
	private List<SelectItem> listaTipoCategoriaEdilizia= new ArrayList<SelectItem>();
	
	private BodLogicService bodLogicService = null;
	
	private boolean abilitaPostoAuto=false;
	private boolean visualizzaOutput=false;
	
	
	

	public String init(){
		
		
		String esito = "classamentoTreBck.init";
		
		
		
		  cbxZc = new ComboBoxRch();
		  outMicrozona = new HtmlOutputLabel();
		  radioCatEd = new HtmlSelectOneRadio();
		  cbxPostoAuto= new ComboBoxRch();
		  outValoreClasse = new HtmlOutputLabel();
		  
			 
			
		  
		

		//setto la lista delle categorie edilizie
		List<SelectItem> listaCategorieEdilizie= new ArrayList<SelectItem>();
		SelectItem catEd= new SelectItem();
		catEd.setLabel("A10 - Uffici e studi privati");
		catEd.setValue("A10");
		listaCategorieEdilizie.add(catEd);
		catEd= new SelectItem();
		catEd.setLabel("C01 - Negozi e botteghe");
		catEd.setValue("C01");
		listaCategorieEdilizie.add(catEd);
		catEd= new SelectItem();
		catEd.setLabel("C02 - Magazzini e locali di deposito");
		catEd.setValue("C02");
		listaCategorieEdilizie.add(catEd);
		catEd= new SelectItem();
		catEd.setLabel("C03 - Laboratori per arti e mestieri");
		catEd.setValue("C03");
		listaCategorieEdilizie.add(catEd);
		catEd= new SelectItem();
		catEd.setLabel("C06 - Stalle, scuderie, rimesse e autorimesse");
		catEd.setValue("C06");
		listaCategorieEdilizie.add(catEd);
		
		/*UISelectItems radioCatEdOptions = new UISelectItems();
		radioCatEdOptions.setValue(listaCategorieEdilizie);
		
		radioCatEd.getChildren().add(radioCatEdOptions);
		*/
		
		this.setListaTipoCategoriaEdilizia(listaCategorieEdilizie);
		
		radioCatEd.setValue("A10");
		
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
		
		cbxPostoAuto.setSelectItems(listaSiNo) ;
		cbxPostoAuto.setState("SI");
				
		abilitaPostoAuto=false;
		
		getListaZC();
		getMicrozona();
		
		outValoreClasse.setValue("0");
		
		visualizzaOutput=false;
		resetForm();
		
		return esito;
	}
	
	
public void resetForm(){
		
		
		
		cbxFoglio.setState("");
		cbxZc.setState("");
		cbxZc.setSelectItems(new ArrayList<SelectItem>());
		outMicrozona.setValue(null);
		
		radioCatEd.setValue("A10");
		
		nuovaMicrozona="";
		cbxPostoAuto.setState("SI");
				
	}
	
	public void hideOutPanel(){
		
		this.visualizzaOutput=false;
	}

	
	
	
	public void selectFoglio(){
		
		
		
		getListaZC();
		getMicrozona();
		
		this.visualizzaOutput=false;
		
		
	}
	
public void selectCategoriaEdilizia(){
		
		
		String catEdilizia= (String) this.radioCatEd.getValue();
		
			//se abitazioni civili disabilito cbxAscensore
			if (catEdilizia != null && !catEdilizia.equals("C06"))
				this.abilitaPostoAuto=false;
			else if (catEdilizia != null && catEdilizia.equals("C06"))
				this.abilitaPostoAuto=true;
			
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
	


	
}
	
	
	
	private String getClasseMin(String foglio, String zc, String categoria){
		String classeMin= null;
		
		Hashtable htQry = new Hashtable();
		
		ArrayList<FilterItem> aryFilters = new ArrayList<FilterItem>();
		
		Properties prop = SqlHandler.loadProperties(propName);
		String sql = prop.getProperty("qryDocfaClasseMin");
		/*
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
	*/
		
		if (foglio != null &&  !foglio.equals("") ){	
			sql= sql + " foglio='"+ foglio+"'";
		}
		else{
			sql= sql + " foglio is null ";
		}
		
		if (zc != null &&  !zc.equals("")  ){
			sql= sql + " and zc='"+ zc+"'";
		}
		
		if (categoria != null &&  !categoria.equals("")  ){
			sql= sql + " and categoria='"+ categoria+"'";
		}
		
		htQry.put("queryString", sql);

		List lstObj = bodLogicService.getList(htQry);
		
		if (lstObj!= null && lstObj.size()>0){
			
			for (int i=0; i<lstObj.size(); i++){
				
				 classeMin = (String)lstObj.get(i);
			
			}
		}
	
		
		
		return classeMin;
	}
	
	
	
	public void calcola(){
		
		visualizzaOutput=true;
		
		NumberFormat nf= NumberFormat.getInstance(Locale.ITALIAN);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		
		
		
		String foglio= this.cbxFoglio.getState();
		String catEd=(String)this.radioCatEd.getValue();
		String zc=this.cbxZc.getState();
		String flgPostoAuto= this.cbxPostoAuto.getState();

		String classeMin= this.getClasseMin(foglio, zc, catEd);
		if (catEd != null && catEd.equals("C06") && flgPostoAuto != null && flgPostoAuto.equals("SI")){
			if (classeMin != null && !classeMin.equals(""))
				classeMin= String.valueOf(Integer.valueOf(classeMin).intValue() - 3);
		}
		
		outValoreClasse.setValue(classeMin);
		
		
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






	public ComboBoxRch getCbxPostoAuto() {
		return cbxPostoAuto;
	}






	public void setCbxPostoAuto(ComboBoxRch cbxPostoAuto) {
		this.cbxPostoAuto = cbxPostoAuto;
	}






	public HtmlOutputLabel getOutValoreClasse() {
		return outValoreClasse;
	}






	public void setOutValoreClasse(HtmlOutputLabel outValoreClasse) {
		this.outValoreClasse = outValoreClasse;
	}






	public boolean isAbilitaPostoAuto() {
		return abilitaPostoAuto;
	}






	public void setAbilitaPostoAuto(boolean abilitaPostoAuto) {
		this.abilitaPostoAuto = abilitaPostoAuto;
	}






	public boolean isVisualizzaOutput() {
		return visualizzaOutput;
	}






	public void setVisualizzaOutput(boolean visualizzaOutput) {
		this.visualizzaOutput = visualizzaOutput;
	}

	
	
	


	





}
