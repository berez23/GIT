package it.webred.indice;


import it.escsolution.escwebgis.common.Utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OggettoIndice implements Serializable {
	
	private String descrizione;
	private String idOrig;
	private String fonte;
	private String progr;
	
	private String fonteDest;
	private String progrDest;
	private String idDest;
	private String fonteDescr;
	private String progrDescr;
	
	private String att;
	private String rating;
	
	private String appName;
	private String uc;
	
	private boolean coloreLista=false;
	
	// 1 Soggetto, 2 Via, 3 Civici, 4 Oggetti
	private String tipo;
	
	private String tipoPersona;
	
	private List<OggettoIndice> subList;
	
	private List<String> listaAttributi;
	private List<String> listaAttributiVista;
	
	private List<String> listaNomiAttributiSub;
	private List<String> listaAttributiVistaSub;
	
	private String sortTypes;
	
	DecimalFormat DF = new DecimalFormat("#0.00");
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDescrizione() {
		
		
		if (descrizione != null)
			descrizione= Utils.pulisciAccentoApostrofo(descrizione);
		
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getIdOrig() {
		return idOrig;
	}
	public void setIdOrig(String idOrig) {
		this.idOrig = idOrig;
	}
	public String getFonte() {
		return fonte;
	}
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}
	public String getProgr() {
		return progr;
	}
	public void setProgr(String progr) {
		this.progr = progr;
	}
	public String getFonteDest() {
		return fonteDest;
	}
	public void setFonteDest(String fonteDest) {
		this.fonteDest = fonteDest;
	}
	public String getProgrDest() {
		return progrDest;
	}
	public void setProgrDest(String progrDest) {
		this.progrDest = progrDest;
	}
	public String getIdDest() {
		return idDest;
	}
	public void setIdDest(String idDest) {
		this.idDest = idDest;
	}
	public String getFonteDescr() {
		return fonteDescr;
	}
	public void setFonteDescr(String fonteDescr) {
		this.fonteDescr = fonteDescr;
	}
	public String getAtt() {
		return att;
	}
	public void setAtt(String att) {
		this.att = att;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getUc() {
		return uc;
	}
	public void setUc(String uc) {
		this.uc = uc;
	}
	public String getTipoPersona() {
		return tipoPersona;
	}
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	public List<OggettoIndice> getSubList() {
		return subList;
	}
	public void setSubList(List<OggettoIndice> subList) {
		this.subList = subList;
	}
	public String getProgrDescr() {
		return progrDescr;
	}
	public void setProgrDescr(String progrDescr) {
		this.progrDescr = progrDescr;
	}
	public List<String> getListaAttributi() {
		return listaAttributi;
	}
	public void setListaAttributi(List<String> listaAttributi) {
		this.listaAttributi = listaAttributi;
	}
	
	public List<String> getListaAttributiVista() {
		
		List<String> listaAttributiVista= new ArrayList<String>();
		
		if (tipo != null && tipo.equals("1"))
			listaAttributiVista.addAll(this.getListaAttributiVistaSoggetti());
		else if (tipo != null && tipo.equals("4"))
			listaAttributiVista.addAll(this.getListaAttributiVistaOggetti());
		else if (tipo != null && tipo.equals("5")) //attributi per fabbricati
			listaAttributiVista.addAll(this.getListaAttributiVistaFabbricati());			
		else if (tipo != null && (tipo.equals("2") || tipo.equals("3")))
			listaAttributiVista.addAll(this.getListaAttributiVistaVieCivici());		
			
		return listaAttributiVista;
	}//-------------------------------------------------------------------------
	
	private List<String> getListaAttributiVistaSoggetti(){
		
		List<String> listaAttributiVista= new ArrayList<String>();
		
		if (fonteDest != null && fonteDest.equals("1")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(8));
			listaAttributiVista.add(listaAttributi.get(11));
			
			listaAttributiVista.add(listaAttributi.get(listaAttributi.size()-2));
			
			String value = listaAttributi.get(listaAttributi.size()-1);
		//	log.debug("Data Fine Val " + value);
			String stato = value!=null && !value.equals("-")? value: "ATTUALE";		
			listaAttributiVista.add(stato);
		}
		else if (fonteDest != null && fonteDest.equals("2")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			
			String indirizzoNew= Utils.pulisciVia(listaAttributi.get(9));
		
			String civico= listaAttributi.get(11);
			String civicoNew="";
			
			if (civico != null){
        		try{
        			Integer civicoInt= Integer.valueOf(civico);
        			civicoNew= String.valueOf(civicoInt);
        		}catch(Exception e){
        			civicoNew= civico;
        		}
    		}
			
			listaAttributiVista.add(indirizzoNew!=null ? indirizzoNew : "-");
			listaAttributiVista.add(civicoNew);
		
			listaAttributiVista.add(listaAttributi.get(28));
			listaAttributiVista.add(listaAttributi.get(6));
		}
		else if (fonteDest != null && fonteDest.equals("10")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(4));
		}
		else if (fonteDest != null && fonteDest.equals("12")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(7));
			listaAttributiVista.add(listaAttributi.get(9));
			listaAttributiVista.add(listaAttributi.get(11));
			listaAttributiVista.add(listaAttributi.get(12));
		}
		else if (fonteDest != null && fonteDest.equals("4")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(4));
		}
		else if (fonteDest != null && fonteDest.equals("5")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(4));
			listaAttributiVista.add(listaAttributi.get(5));
			listaAttributiVista.add(listaAttributi.get(12));
			listaAttributiVista.add(listaAttributi.get(13));
			listaAttributiVista.add(listaAttributi.get(14));
			listaAttributiVista.add(listaAttributi.get(15));
			listaAttributiVista.add(listaAttributi.get(16));
		}
		else if (fonteDest != null && fonteDest.equals("3")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(5));
			listaAttributiVista.add(listaAttributi.get(6));
		}
		else if (fonteDest != null && fonteDest.equals("6")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(5));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(4));
			listaAttributiVista.add(listaAttributi.get(6));
			listaAttributiVista.add(listaAttributi.get(7));
			listaAttributiVista.add(listaAttributi.get(8));
			listaAttributiVista.add(listaAttributi.get(9));
			listaAttributiVista.add(listaAttributi.get(10));
			listaAttributiVista.add(listaAttributi.get(11));
			listaAttributiVista.add(listaAttributi.get(12));
		}
		else if (fonteDest != null && fonteDest.equals("13")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(4));
			listaAttributiVista.add(listaAttributi.get(5));
			listaAttributiVista.add(listaAttributi.get(7));
			listaAttributiVista.add(listaAttributi.get(8));
		}
		else if (fonteDest != null && fonteDest.equals("11")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			//l'ultimo attributo è aggiunto a mano
			listaAttributiVista.add(listaAttributi.get(listaAttributi.size() - 1));
		}
		else if (fonteDest != null && fonteDest.equals("9") && progr != null && progr.equals("1")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(4));
			listaAttributiVista.add(listaAttributi.get(5));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
		}
		else if (fonteDest != null && fonteDest.equals("9") && progr != null && progr.equals("2")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add("-");
			listaAttributiVista.add("-");
			listaAttributiVista.add("-");
			listaAttributiVista.add("-");
		}
		else if (fonteDest != null && fonteDest.equals("7") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
		}
		else if (fonteDest != null && fonteDest.equals("14") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
		}
		else if (fonteDest != null && fonteDest.equals("17") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
		}
		else if (fonteDest != null && fonteDest.equals("18") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
		}
		else if (fonteDest != null && fonteDest.equals("28") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
		}
		else if (fonteDest != null && fonteDest.equals("30") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
		}
		else if (fonteDest != null && fonteDest.equals("33") && progr.equals("1") ){
			listaAttributiVista.add(listaAttributi.get(12));
			listaAttributiVista.add(listaAttributi.get(11));
			listaAttributiVista.add(listaAttributi.get(13));
			listaAttributiVista.add(listaAttributi.get(6));
			listaAttributiVista.add(listaAttributi.get(7));
			listaAttributiVista.add(listaAttributi.get(8));
			listaAttributiVista.add("-");
			listaAttributiVista.add(listaAttributi.get(2) + " - "+ listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(4) + " - "+ listaAttributi.get(5));
		}
		else if (fonteDest != null && fonteDest.equals("33") && progr.equals("2") ){
			listaAttributiVista.add(listaAttributi.get(12));
			listaAttributiVista.add(listaAttributi.get(11));
			listaAttributiVista.add(listaAttributi.get(13));
			listaAttributiVista.add(listaAttributi.get(6));
			listaAttributiVista.add(listaAttributi.get(7));
			listaAttributiVista.add(listaAttributi.get(8));
			listaAttributiVista.add(listaAttributi.get(20));
			listaAttributiVista.add(listaAttributi.get(2) + " - "+ listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(4) + " - "+ listaAttributi.get(5));
		}
		else if (fonteDest != null && fonteDest.equals("33") && progr.equals("3") ){
			listaAttributiVista.add(listaAttributi.get(12));
			listaAttributiVista.add(listaAttributi.get(11));
			listaAttributiVista.add(listaAttributi.get(13));
			listaAttributiVista.add(listaAttributi.get(6));
			listaAttributiVista.add("-");
			listaAttributiVista.add("-");
			listaAttributiVista.add("-");
			listaAttributiVista.add(listaAttributi.get(2) + " - "+ listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(4) + " - "+ listaAttributi.get(5));
		}
		else if (fonteDest != null && fonteDest.equals("35")){
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(4));
			listaAttributiVista.add(listaAttributi.get(5));
			listaAttributiVista.add(listaAttributi.get(6));
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdfOk = new SimpleDateFormat("dd/MM/yyyy");
			String dataDoc = listaAttributi.get(7);
			try{
				dataDoc = sdfOk.format(sdf.parse(dataDoc));
				
			}catch(Exception e){}
			
			listaAttributiVista.add(dataDoc);
		}
		else if (fonteDest != null && fonteDest.equals("37")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(2));
			BigDecimal bd = listaAttributi.get(3)!=null ? new BigDecimal(listaAttributi.get(3)) : BigDecimal.ZERO;
			listaAttributiVista.add("&euro; "+DF.format(bd.doubleValue()));
		}
		else if (fonteDest != null && fonteDest.equals("39")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3).equals("S") ? "Conguaglio" : "Principale");
		}
		else if (fonteDest != null && fonteDest.equals("40")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3).equals("S") ? "Conguaglio" : "Principale");
		}
		else if (fonteDest != null && fonteDest.equals("45")){	//SOGGETTI
			listaAttributiVista.add(listaAttributi.get(7));	//DATA PROT
			listaAttributiVista.add(listaAttributi.get(8));	//CODICE IDENTIFICATIVO PRATICA
		}
		return listaAttributiVista;
	}//-------------------------------------------------------------------------
	
	private List<String> getListaAttributiVistaOggetti(){
		List<String> listaAttributiVista= new ArrayList<String>();
		
		if (fonteDest != null && fonteDest.equals("2") && progr != null && progr.equals("2")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(4));
			
			String indirizzoNew= Utils.pulisciVia(listaAttributi.get(30));
			
			String civico= listaAttributi.get(32);
			String civicoNew="";
			
			if (civico != null){
        		try{
        			Integer civicoInt= Integer.valueOf(civico);
        			civicoNew= String.valueOf(civicoInt);
        		}catch(Exception e){
        			civicoNew= civico;
        		}
    		}
			
			listaAttributiVista.add(indirizzoNew);
			listaAttributiVista.add(civicoNew);
		
			String flgPos3112= (listaAttributi.get(21) == null ? "NO" : 
				listaAttributi.get(21).equalsIgnoreCase("1") || listaAttributi.get(21).equalsIgnoreCase("S") ? "SI" : "NO");
			
			listaAttributiVista.add(flgPos3112);
			
			String flgAbiPri= (listaAttributi.get(24) == null ? "NO" : 
				listaAttributi.get(24).equalsIgnoreCase("1") || listaAttributi.get(24).equalsIgnoreCase("S") ? "SI" : "NO");
			
			listaAttributiVista.add(flgAbiPri);
			
			String flgAcq= (listaAttributi.get(25) == null ? "NO" : 
				listaAttributi.get(25).equalsIgnoreCase("1") || listaAttributi.get(25).equalsIgnoreCase("S") ? "SI" : "NO");
			
			listaAttributiVista.add(listaAttributi.get(25));
			
			String flgCess= (listaAttributi.get(26) == null ? "NO" : 
				listaAttributi.get(26).equalsIgnoreCase("1") || listaAttributi.get(26).equalsIgnoreCase("S") ? "SI" : "NO");
			
			listaAttributiVista.add(listaAttributi.get(26));
			
			listaAttributiVista.add("-");
			listaAttributiVista.add("-");
		}
		else if (fonteDest != null && fonteDest.equals("2") && progr != null && progr.equals("3")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add("-");
			listaAttributiVista.add("-");
			
			String indirizzoNew= Utils.pulisciVia(listaAttributi.get(6));
			
			String civico= listaAttributi.get(8);
			String civicoNew="";
			
			if (civico != null){
        		try{
        			Integer civicoInt= Integer.valueOf(civico);
        			civicoNew= String.valueOf(civicoInt);
        		}catch(Exception e){
        			civicoNew= civico;
        		}
    		}
			
			listaAttributiVista.add(indirizzoNew);
			listaAttributiVista.add(civicoNew);
			
			
			listaAttributiVista.add("-");
			listaAttributiVista.add("-");
			listaAttributiVista.add("-");
			listaAttributiVista.add("-");
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(5));
		}
		else if (fonteDest != null && fonteDest.equals("3") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(7));
		}
		else if (fonteDest != null && fonteDest.equals("4") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
		}
		else if (fonteDest != null && fonteDest.equals("5") && progr != null && progr.equals("1")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(4));
			listaAttributiVista.add(listaAttributi.get(5));
			listaAttributiVista.add(listaAttributi.get(6));
			listaAttributiVista.add("-");
			listaAttributiVista.add(listaAttributi.get(7));
			listaAttributiVista.add(listaAttributi.get(8));
			
			String importoCanoneS= listaAttributi.get(9);
			BigDecimal importoCanone= null;
			if (importoCanoneS != null)
				try{
					importoCanone = new BigDecimal(importoCanoneS).divide(new BigDecimal(100));
				}
				catch (Exception e){
					
				}
			if (importoCanone != null)
				importoCanoneS= DF.format(importoCanone.doubleValue());
			else
				importoCanoneS="-";
			listaAttributiVista.add(importoCanoneS);
			
			listaAttributiVista.add(listaAttributi.get(10));
			listaAttributiVista.add("-");
		}
		else if (fonteDest != null && fonteDest.equals("5") && progr != null && (progr.equals("2") || progr.equals("3"))){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(4));
			listaAttributiVista.add(listaAttributi.get(5));
			listaAttributiVista.add(listaAttributi.get(6));
			listaAttributiVista.add(listaAttributi.get(7));
			listaAttributiVista.add(listaAttributi.get(8));
			listaAttributiVista.add(listaAttributi.get(9));
			
			String importoCanoneS= listaAttributi.get(10);
			BigDecimal importoCanone= null;
			if (importoCanoneS != null)
				try{
					importoCanone = new BigDecimal(importoCanoneS).divide(new BigDecimal(100));
				}
				catch (Exception e){
					
				}
			if (importoCanone != null)
				importoCanoneS= DF.format(importoCanone.doubleValue());
			else
				importoCanoneS="-";
			listaAttributiVista.add(importoCanoneS);
			
			listaAttributiVista.add(listaAttributi.get(11));
			listaAttributiVista.add(listaAttributi.get(12));
		}
		else if (fonteDest != null && fonteDest.equals("6") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(6));
			listaAttributiVista.add(listaAttributi.get(9));
		}
		else if (fonteDest != null && fonteDest.equals("7") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			
			String indirizzoNew= Utils.pulisciVia(listaAttributi.get(16));
			
			String civico= listaAttributi.get(18);
			String civicoNew="";
			
			if (civico != null){
        		try{
        			Integer civicoInt= Integer.valueOf(civico);
        			civicoNew= String.valueOf(civicoInt);
        		}catch(Exception e){
        			civicoNew= civico;
        		}
    		}
			
			listaAttributiVista.add(indirizzoNew);
			listaAttributiVista.add(civicoNew);
			
		}
		else if (fonteDest != null && fonteDest.equals("9") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			
			String indirizzoNew= Utils.pulisciVia(listaAttributi.get(6));
			
			
			String civico= listaAttributi.get(7);
			String civicoNew="";
			
			if (civico != null){
        		try{
        			Integer civicoInt= Integer.valueOf(civico);
        			civicoNew= String.valueOf(civicoInt);
        		}catch(Exception e){
        			civicoNew= civico;
        		}
    		}
			
			listaAttributiVista.add(indirizzoNew);
			listaAttributiVista.add(civicoNew);
		}
		else if (fonteDest != null && fonteDest.equals("10") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(5));
			listaAttributiVista.add(listaAttributi.get(11));
			listaAttributiVista.add(listaAttributi.get(16));
			listaAttributiVista.add(listaAttributi.get(17));
			listaAttributiVista.add(listaAttributi.get(19));
		}
		else if (fonteDest != null && fonteDest.equals("30") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
		}
		else if (fonteDest != null && fonteDest.equals("40") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(4).equals("S") ? "Conguaglio" : "Principale");
			listaAttributiVista.add(listaAttributi.get(10));
			listaAttributiVista.add(listaAttributi.get(11));
		}
		else if (fonteDest != null && fonteDest.equals("45") ){	//OGGETTI
			listaAttributiVista.add(listaAttributi.get(2));	//SUB TUTTI
			listaAttributiVista.add(listaAttributi.get(6));	//DATA PROT
			listaAttributiVista.add(listaAttributi.get(7));	//CODICE IDENTIFICATIVO PRATICA
		}
		
		return listaAttributiVista;
	}//-------------------------------------------------------------------------
	
	private List<String> getListaAttributiVistaFabbricati(){
		List<String> listaAttributiVista= new ArrayList<String>();
		
		//Oggetti ICI
		if (fonteDest != null && fonteDest.equals("2") && progr != null && progr.equals("2")){ 
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(4));
			listaAttributiVista.add(listaAttributi.get(5));
			
			String indirizzoNew= Utils.pulisciVia(listaAttributi.get(31));
			
			String civico= listaAttributi.get(33);
			String civicoNew="";
			
			if (civico != null){
        		try{
        			Integer civicoInt= Integer.valueOf(civico);
        			civicoNew= String.valueOf(civicoInt);
        		}catch(Exception e){
        			civicoNew= civico;
        		}
    		}
			
			listaAttributiVista.add(indirizzoNew);
			listaAttributiVista.add(civicoNew);
		
			int index_flgPos3112 = 22;
			String flgPos3112= (listaAttributi.get(index_flgPos3112) == null ? "NO" : 
				listaAttributi.get(index_flgPos3112).equalsIgnoreCase("1") || listaAttributi.get(index_flgPos3112).equalsIgnoreCase("S") ? "SI" : "NO");
			
			listaAttributiVista.add(flgPos3112);
			
			int index_flgAbiPri = 25;
			String flgAbiPri= (listaAttributi.get(index_flgAbiPri) == null ? "NO" : 
				listaAttributi.get(index_flgAbiPri).equalsIgnoreCase("1") || listaAttributi.get(index_flgAbiPri).equalsIgnoreCase("S") ? "SI" : "NO");
			
			listaAttributiVista.add(flgAbiPri);
			
			int index_flgAcq = 26;
			String flgAcq= (listaAttributi.get(index_flgAcq) == null ? "NO" : 
				listaAttributi.get(index_flgAcq).equalsIgnoreCase("1") || listaAttributi.get(index_flgAcq).equalsIgnoreCase("S") ? "SI" : "NO");
			
			listaAttributiVista.add(flgAcq);
			
			int index_flgCess = 27;
			String flgCess= (listaAttributi.get(index_flgCess) == null ? "NO" : 
				listaAttributi.get(index_flgCess).equalsIgnoreCase("1") || listaAttributi.get(index_flgCess).equalsIgnoreCase("S") ? "SI" : "NO");
			
			listaAttributiVista.add(flgCess);
			
			listaAttributiVista.add("-");
			listaAttributiVista.add("-");
			listaAttributiVista.add(listaAttributi.get(35));
			listaAttributiVista.add(listaAttributi.get(36));
		
		}
		else if (fonteDest != null && fonteDest.equals("2") && progr != null && progr.equals("3")){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add("-");
			listaAttributiVista.add("-");
			
			String indirizzoNew= Utils.pulisciVia(listaAttributi.get(7));
			
			String civico= listaAttributi.get(9);
			String civicoNew="";
			
			if (civico != null){
        		try{
        			Integer civicoInt= Integer.valueOf(civico);
        			civicoNew= String.valueOf(civicoInt);
        		}catch(Exception e){
        			civicoNew= civico;
        		}
    		}
			
			listaAttributiVista.add(indirizzoNew);
			listaAttributiVista.add(civicoNew);
			
			
			listaAttributiVista.add("-");
			listaAttributiVista.add("-");
			listaAttributiVista.add("-");
			listaAttributiVista.add("-");
			listaAttributiVista.add(listaAttributi.get(4));
			listaAttributiVista.add(listaAttributi.get(6));
			listaAttributiVista.add(listaAttributi.get(35));
			listaAttributiVista.add(listaAttributi.get(36));
		}
		else if (fonteDest != null && fonteDest.equals("3") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
		
		}
		else if (fonteDest != null && fonteDest.equals("4") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(35));
			listaAttributiVista.add(listaAttributi.get(36));
		}
		else if (fonteDest != null && fonteDest.equals("5") && progr != null && progr.equals("1")){
			
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(4));
			listaAttributiVista.add(listaAttributi.get(5));
			listaAttributiVista.add(listaAttributi.get(6));
			listaAttributiVista.add("-");
			listaAttributiVista.add(listaAttributi.get(7));
			listaAttributiVista.add(listaAttributi.get(8));
			
			String importoCanoneS= listaAttributi.get(9);
			BigDecimal importoCanone= null;
			if (importoCanoneS != null)
				try{
					importoCanone = new BigDecimal(importoCanoneS).divide(new BigDecimal(100));
				}
				catch (Exception e){
					
				}
			if (importoCanone != null)
				importoCanoneS= DF.format(importoCanone.doubleValue());
			else
				importoCanoneS="-";
			listaAttributiVista.add(importoCanoneS);
			
			listaAttributiVista.add(listaAttributi.get(10));
			listaAttributiVista.add("-");
			
		}
		else if (fonteDest != null && fonteDest.equals("5") && progr != null && (progr.equals("2") || progr.equals("3"))){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(4));
			listaAttributiVista.add(listaAttributi.get(5));
			listaAttributiVista.add(listaAttributi.get(6));
			listaAttributiVista.add(listaAttributi.get(7));
			listaAttributiVista.add(listaAttributi.get(8));
			listaAttributiVista.add(listaAttributi.get(9));
			listaAttributiVista.add(listaAttributi.get(10));
			
			String importoCanoneS= listaAttributi.get(11);
			BigDecimal importoCanone= null;
			if (importoCanoneS != null)
				try{
				importoCanone = new BigDecimal(importoCanoneS).divide(new BigDecimal(100));
				}
				catch (Exception e){
					
				}
			if (importoCanone != null)
				importoCanoneS= DF.format(importoCanone.doubleValue());
			else
				importoCanoneS="-";
			listaAttributiVista.add(importoCanoneS);
			
			listaAttributiVista.add(listaAttributi.get(12));
		}
		else if (fonteDest != null && fonteDest.equals("6") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(7));//6
			listaAttributiVista.add(listaAttributi.get(10));//9
		
		}
		else if (fonteDest != null && fonteDest.equals("7") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			
			String indirizzoNew= Utils.pulisciVia(listaAttributi.get(17));
			
			String civico= listaAttributi.get(18); 
			String civicoNew="";
			
			if (civico != null){
        		try{
        			Integer civicoInt= Integer.valueOf(civico);
        			civicoNew= String.valueOf(civicoInt);
        		}catch(Exception e){
        			civicoNew= civico;
        		}
    		}
			
			listaAttributiVista.add(indirizzoNew);
			listaAttributiVista.add(civicoNew);
			
		}
		else if (fonteDest != null && fonteDest.equals("9") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			
			String indirizzoNew= Utils.pulisciVia(listaAttributi.get(7));
			
			
			String civico= listaAttributi.get(8);
			String civicoNew="";
			
			if (civico != null){
        		try{
        			Integer civicoInt= Integer.valueOf(civico);
        			civicoNew= String.valueOf(civicoInt);
        		}catch(Exception e){
        			civicoNew= civico;
        		}
    		}
			
			listaAttributiVista.add(indirizzoNew);
			listaAttributiVista.add(civicoNew);
			
		}
		else if (fonteDest != null && fonteDest.equals("10") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(6));
			listaAttributiVista.add(listaAttributi.get(12));
			listaAttributiVista.add(listaAttributi.get(17));
			listaAttributiVista.add(listaAttributi.get(18));
			listaAttributiVista.add(listaAttributi.get(20));
			
		}
		else if (fonteDest != null && fonteDest.equals("14") ){
			listaAttributiVista.add(listaAttributi.get(8));
			listaAttributiVista.add(listaAttributi.get(16));
			listaAttributiVista.add(listaAttributi.get(22));
			
		}
		else if (fonteDest != null && fonteDest.equals("30") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
		}
		else if (fonteDest != null && fonteDest.equals("40") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
			listaAttributiVista.add(listaAttributi.get(3));
			listaAttributiVista.add(listaAttributi.get(4));
			listaAttributiVista.add(listaAttributi.get(5).equals("S") ? "Conguaglio" : "Principale");
			listaAttributiVista.add(listaAttributi.get(11));
			listaAttributiVista.add(listaAttributi.get(12));
		}
		else if (fonteDest != null && fonteDest.equals("42") ){
			listaAttributiVista.add(listaAttributi.get(0));
			listaAttributiVista.add(listaAttributi.get(1));
			listaAttributiVista.add(listaAttributi.get(2));
		}
		else if (fonteDest != null && fonteDest.equals("45") ){	//FABBRICATI
			listaAttributiVista.add(listaAttributi.get(4));	//SUB_TUTTI
			listaAttributiVista.add(listaAttributi.get(8));	//DATA PROT
			listaAttributiVista.add(listaAttributi.get(9));	//CODICE IDENTIFICATIVO PRATICA
		}
		
		return listaAttributiVista;
	}//-------------------------------------------------------------------------
	
	private List<String> getListaAttributiVistaVieCivici(){
		List<String> listaAttributiVista= new ArrayList<String>();
		
		  if(fonteDest != null && fonteDest.equals("3") && progr != null && progr.equals("2")){
			  listaAttributiVista.add(listaAttributi.get(0));
			  listaAttributiVista.add(listaAttributi.get(2));
			  listaAttributiVista.add(listaAttributi.get(3));
			  listaAttributiVista.add(listaAttributi.get(4));
			  listaAttributiVista.add(listaAttributi.get(5));
			
		  }else if (fonteDest != null && fonteDest.equals("4") && progr != null && progr.equals("2")){
				listaAttributiVista.add("-");
				listaAttributiVista.add("-");
				listaAttributiVista.add("-");
				listaAttributiVista.add("-");
			}
			else if (fonteDest != null && fonteDest.equals("4") && progr != null && progr.equals("4")){
					listaAttributiVista.add(listaAttributi.get(0));
					listaAttributiVista.add(listaAttributi.get(1));
					listaAttributiVista.add(listaAttributi.get(2));
					listaAttributiVista.add(listaAttributi.get(3));
			}
			else if (fonteDest != null && fonteDest.equals("5") && progr != null && progr.equals("1")){
				listaAttributiVista.add(listaAttributi.get(0));
				listaAttributiVista.add(listaAttributi.get(1));
				listaAttributiVista.add(listaAttributi.get(2));
				listaAttributiVista.add(listaAttributi.get(3));
				listaAttributiVista.add("-");
				listaAttributiVista.add(listaAttributi.get(4));
				listaAttributiVista.add(listaAttributi.get(5));
				
				String importoCanoneS= listaAttributi.get(6);
				BigDecimal importoCanone= null;
				if (importoCanoneS != null)
					try{
						importoCanone = new BigDecimal(importoCanoneS).divide(new BigDecimal(100));
					}
					catch (Exception e){
						
					}
				if (importoCanone != null)
					importoCanoneS= DF.format(importoCanone.doubleValue());
				else
					importoCanoneS="-";
				listaAttributiVista.add(importoCanoneS);
				
				listaAttributiVista.add(listaAttributi.get(7));
				listaAttributiVista.add("-");
			}
			else if (fonteDest != null && fonteDest.equals("5") && progr != null && (progr.equals("2") || progr.equals("3"))){
				listaAttributiVista.add(listaAttributi.get(0));
				listaAttributiVista.add(listaAttributi.get(1));
				listaAttributiVista.add(listaAttributi.get(2));
				listaAttributiVista.add(listaAttributi.get(3));
				listaAttributiVista.add(listaAttributi.get(4));
				listaAttributiVista.add(listaAttributi.get(5));
				listaAttributiVista.add(listaAttributi.get(6));
				
				String importoCanoneS= listaAttributi.get(7);
				BigDecimal importoCanone= null;
				if (importoCanoneS != null)
					try{
						importoCanone = new BigDecimal(importoCanoneS).divide(new BigDecimal(100));
					}
					catch (Exception e){
						
					}
				if (importoCanone != null)
					importoCanoneS= DF.format(importoCanone.doubleValue());
				else
					importoCanoneS="-";
				listaAttributiVista.add(importoCanoneS);
				
				listaAttributiVista.add(listaAttributi.get(8));
				listaAttributiVista.add(listaAttributi.get(9));
			}
			else if (fonteDest != null && fonteDest.equals("6")){
				listaAttributiVista.add(listaAttributi.get(0));
				listaAttributiVista.add(listaAttributi.get(1));
				listaAttributiVista.add(listaAttributi.get(2));
				listaAttributiVista.add(listaAttributi.get(3));
				listaAttributiVista.add(listaAttributi.get(4));
				listaAttributiVista.add(listaAttributi.get(5));
				listaAttributiVista.add(listaAttributi.get(6));
				listaAttributiVista.add(listaAttributi.get(7));
				listaAttributiVista.add(listaAttributi.get(8));
				listaAttributiVista.add(listaAttributi.get(9));
				listaAttributiVista.add(listaAttributi.get(10));
			}
			else if (fonteDest != null && fonteDest.equals("7") && progr != null && progr.equals("2")){
				listaAttributiVista.add(listaAttributi.get(0));
				listaAttributiVista.add(listaAttributi.get(1));
				listaAttributiVista.add(listaAttributi.get(2));
				listaAttributiVista.add(listaAttributi.get(3));
				listaAttributiVista.add(listaAttributi.get(4));
				listaAttributiVista.add(listaAttributi.get(5));
				listaAttributiVista.add(listaAttributi.get(6));
				listaAttributiVista.add(listaAttributi.get(7));
				listaAttributiVista.add(listaAttributi.get(8));
				listaAttributiVista.add("-");
				listaAttributiVista.add("-");
				listaAttributiVista.add("-");
			}
			else if (fonteDest != null && fonteDest.equals("7") && progr != null && progr.equals("1")){
				listaAttributiVista.add("-");
				listaAttributiVista.add("-");
				listaAttributiVista.add("-");
				listaAttributiVista.add("-");
				listaAttributiVista.add("-");
				listaAttributiVista.add("-");
				listaAttributiVista.add("-");
				listaAttributiVista.add("-");
				listaAttributiVista.add("-");
				listaAttributiVista.add(listaAttributi.get(0));
				listaAttributiVista.add(listaAttributi.get(1));
				listaAttributiVista.add(listaAttributi.get(2));
			}
			else if (fonteDest != null && fonteDest.equals("9")){
				listaAttributiVista.add(listaAttributi.get(0));
				listaAttributiVista.add(listaAttributi.get(1));
				listaAttributiVista.add(listaAttributi.get(2));
				listaAttributiVista.add(listaAttributi.get(3));
				listaAttributiVista.add(listaAttributi.get(4));
			}
			else if (fonteDest != null && fonteDest.equals("10")){
				listaAttributiVista.add(listaAttributi.get(0));
				listaAttributiVista.add(listaAttributi.get(1));
				listaAttributiVista.add(listaAttributi.get(2));
			}
			else if (fonteDest != null && fonteDest.equals("11")){
				listaAttributiVista.add(listaAttributi.get(0));
				listaAttributiVista.add(listaAttributi.get(1));
				listaAttributiVista.add(listaAttributi.get(2));
				listaAttributiVista.add(listaAttributi.get(3));
				listaAttributiVista.add(listaAttributi.get(4));
				listaAttributiVista.add(listaAttributi.get(5));
				listaAttributiVista.add(listaAttributi.get(6));
				//l'ultimo attributo è aggiunto a mano
				listaAttributiVista.add(listaAttributi.get(listaAttributi.size() - 1));
			}
			else if (fonteDest != null && fonteDest.equals("12")){
				listaAttributiVista.add(listaAttributi.get(0));
				listaAttributiVista.add(listaAttributi.get(1));
				listaAttributiVista.add(listaAttributi.get(2));
				listaAttributiVista.add(listaAttributi.get(3));
				listaAttributiVista.add(listaAttributi.get(4));
				listaAttributiVista.add(listaAttributi.get(5));
			}
			 //PER LE LICENZE COMMERCIALI I DATI AGGIUNTIVI FIELD CI SONO SOLO PER IL CIVICO - PER LE VIE c'è LA STRUTTURA AD ALBERO
			else if (fonteDest != null && fonteDest.equals("13") && tipo.equals("3")){
				listaAttributiVista.add(listaAttributi.get(0));
				listaAttributiVista.add(listaAttributi.get(1));
				listaAttributiVista.add(listaAttributi.get(2));
				listaAttributiVista.add(listaAttributi.get(3));
				listaAttributiVista.add(listaAttributi.get(4));
				listaAttributiVista.add(listaAttributi.get(5));
			}
			else if (fonteDest != null && fonteDest.equals("14") && progr != null && progr.equals("1")){
				listaAttributiVista.add("-");
				listaAttributiVista.add("-");
				listaAttributiVista.add("-");
			}
			else if (fonteDest != null && fonteDest.equals("14") && progr != null && progr.equals("2")){
				listaAttributiVista.add(listaAttributi.get(2));
				listaAttributiVista.add(listaAttributi.get(3));
				listaAttributiVista.add(listaAttributi.get(10));
			}
			else if (fonteDest != null && fonteDest.equals("17")){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat sdfOk = new SimpleDateFormat("dd/MM/yyyy");
				try {
					String dataMulta = sdfOk.format(sdf.parse(listaAttributi.get(1)));
					listaAttributiVista.add(dataMulta);
					listaAttributiVista.add(listaAttributi.get(3));
					listaAttributiVista.add(listaAttributi.get(4));
					listaAttributiVista.add(listaAttributi.get(5));
				} catch (ParseException e) {
				}
			}
			else if (fonteDest != null && fonteDest.equals("27") && progr != null && progr.equals("2")){
				listaAttributiVista.add(listaAttributi.get(0));
				listaAttributiVista.add(listaAttributi.get(1));
				listaAttributiVista.add(listaAttributi.get(2));
				listaAttributiVista.add(listaAttributi.get(3));
				listaAttributiVista.add(listaAttributi.get(4));
				listaAttributiVista.add(listaAttributi.get(5));
				listaAttributiVista.add(listaAttributi.get(6));
				listaAttributiVista.add(listaAttributi.get(7));
			}
			else if (fonteDest != null && fonteDest.equals("29") && progr != null && progr.equals("1") ){
				if(tipo.equals("3")){
					listaAttributiVista.add(listaAttributi.get(3)+", "+ listaAttributi.get(0));
					listaAttributiVista.add(listaAttributi.get(1));
					listaAttributiVista.add(listaAttributi.get(2));
				}else{
					listaAttributiVista.add("-");
					listaAttributiVista.add("-");
					listaAttributiVista.add("-");
				}
			}else if (fonteDest != null && fonteDest.equals("30") ){
				String denominazione = "-";
				if(progr.equals("1"))
					denominazione = "-".equals(listaAttributi.get(0))?listaAttributi.get(3):(listaAttributi.get(0)+" "+listaAttributi.get(1));
				if(progr.equals("2"))
					denominazione = listaAttributi.get(0);
				listaAttributiVista.add(denominazione);
			}else if (fonteDest != null && fonteDest.equals("35") ){
				listaAttributiVista.add(listaAttributi.get(0));
				listaAttributiVista.add(listaAttributi.get(1));
				listaAttributiVista.add(listaAttributi.get(2));
				listaAttributiVista.add(listaAttributi.get(3));
				listaAttributiVista.add(listaAttributi.get(4));
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat sdfOk = new SimpleDateFormat("dd/MM/yyyy");
				String dataDoc = listaAttributi.get(5);
				try{
					dataDoc = sdfOk.format(sdf.parse(dataDoc));
					
				}catch(Exception e){}
				
				listaAttributiVista.add(dataDoc);
		 }else if (fonteDest != null && (fonteDest.equals("39")||fonteDest.equals("40")) && (progr!=null && progr.equals("1"))){
				listaAttributiVista.add(listaAttributi.get(0));
				listaAttributiVista.add(listaAttributi.get(1).equals("S") ? "Conguaglio" : "Principale");
				listaAttributiVista.add(listaAttributi.get(8));
				listaAttributiVista.add(listaAttributi.get(9));
		  }else if (fonteDest != null && (fonteDest.equals("39")||fonteDest.equals("40")) && (progr!=null && progr.equals("2"))){
				listaAttributiVista.add(listaAttributi.get(0));
				listaAttributiVista.add(listaAttributi.get(1).equals("S") ? "Conguaglio" : "Principale");
				listaAttributiVista.add(listaAttributi.get(7));
				listaAttributiVista.add(listaAttributi.get(8));
		  }else if (fonteDest != null && fonteDest.equals("42") && (progr!=null && progr.equals("1"))){
			  listaAttributiVista.add(listaAttributi.get(2));
			  listaAttributiVista.add(listaAttributi.get(3));
			  listaAttributiVista.add("1".equals(listaAttributi.get(4)) ? "Si" : "No");
		  }else if (fonteDest != null && fonteDest.equals("45")){ 	//VIA E CIVICI
			  listaAttributiVista.add(listaAttributi.get(5));	//DATA PROT
			  listaAttributiVista.add(listaAttributi.get(0));	//CODICE IDENTIFICATIVO PRATICA
		  }
		
		return listaAttributiVista;
	}//-------------------------------------------------------------------------
	
	
	public void setListaAttributiVista(List<String> listaAttributiVista) {
		this.listaAttributiVista = listaAttributiVista;
	}
	
	//Lista Attributi per le Fonti che prevedono la struttura ad albero per le Vie e i Civici (Demografia, Tributi, Licenze, Concessioni...)
	public List<String> getListaNomiAttributiSub() {
		
		List<String> listaNomiAttributiSub= new ArrayList<String>();
		
		if (tipo != null && ( tipo.equals("2")|| tipo.equals("3")) ){
			
			//DEMOGRAFIA
			if (fonteDest != null && fonteDest.equals("1")){
				
				listaNomiAttributiSub.add("COGNOME");
				listaNomiAttributiSub.add("NOME");
				listaNomiAttributiSub.add("CODICE FISCALE");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
				
			}
			//TRIBUTI
			else if (fonteDest != null && fonteDest.equals("2")){
				
				listaNomiAttributiSub.add("ANNO");
				listaNomiAttributiSub.add("FOGLIO");
				listaNomiAttributiSub.add("PARTICELLA");
				listaNomiAttributiSub.add("SUB");
				listaNomiAttributiSub.add("INDIRIZZO");
				this.setSortTypes("[\"CaseInsensitiveString\", \"Number\", \"Number\", \"Number\", \"CaseInsensitiveString\" , \"CaseInsensitiveString\"]");
				
			}
			//LICENZE
			else if (fonteDest != null && fonteDest.equals("13") && tipo.equals("2")){
				
				listaNomiAttributiSub.add("NUMERO");
				listaNomiAttributiSub.add("RAGIONE SOCIALE");
				listaNomiAttributiSub.add("TIPOLOGIA");
				listaNomiAttributiSub.add("CIVICO");
				listaNomiAttributiSub.add("DATA VALIDITA");
				listaNomiAttributiSub.add("DATA RILASCIO");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\", \"Date\"]");
			}
			//CONCESSIONI   
			else if (fonteDest != null && fonteDest.equals("3") ){
				
				listaNomiAttributiSub.add("NUMERO CONCESSIONE");
				listaNomiAttributiSub.add("PROTOCOLLO");
				listaNomiAttributiSub.add("DATA PROTOCOLLO");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\"]");
			}
		}
		
		return listaNomiAttributiSub;
	}
	public void setListaNomiAttributiSub(List<String> listaNomiAttributiSub) {
		this.listaNomiAttributiSub = listaNomiAttributiSub;
	}
	public List<String> getListaAttributiVistaSub() {
		
		return listaAttributiVistaSub;
	}
	public void setListaAttributiVistaSub(List<String> listaAttributiVistaSub) {
		this.listaAttributiVistaSub = listaAttributiVistaSub;
	}
	public String getSortTypes() {
		return sortTypes;
	}
	public void setSortTypes(String sortTypes) {
		this.sortTypes = sortTypes;
	}
	public boolean isColoreLista() {
		return coloreLista;
	}
	public void setColoreLista(boolean coloreLista) {
		this.coloreLista = coloreLista;
	}
	
	
	
	
	
	

}
