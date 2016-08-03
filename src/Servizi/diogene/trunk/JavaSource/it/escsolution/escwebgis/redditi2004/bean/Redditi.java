/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.redditi2004.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Redditi extends EscObject implements Serializable{


	private String tipoRecord;
	private String tipoMod;
	private String idTeleDic;
	private String flagCorr;
	private String flagInte;
	private String statoDic;
	private String flagValuta;
	private String dicCf;
	private String flagDicConiu;
	private String dicCogn;
	private String dicNome;
	private String dicNaLuogo;
	private String dicNaData;
	private String dicSesso;
	private String dicAttRe;
	private String dicAttRf;
	private String dicAttRg;
	private String dicFiller;
	private String domFiscAnnoPrec;
	private String domDFiscAnnoAtt;
	private String indirAttuale;

	// contiene le coppie codiceQuadro e valore
	private HashMap<String, DecoQuadro> quadro = new LinkedHashMap<String, DecoQuadro>();

	
	

	
	
	
	public Redditi(){}
	
	
	public String getChiave(){ 
		return ""+dicCf;
	}


	public String getTipoRecord() {
		return tipoRecord;
	}


	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}


	public String getTipoMod() {
		return tipoMod;
	}


	public void setTipoMod(String tipoMod) {
		this.tipoMod = tipoMod;
	}


	public String getIdTeleDic() {
		return idTeleDic;
	}


	public void setIdTeleDic(String idTeleDic) {
		this.idTeleDic = idTeleDic;
	}


	public String getFlagCorr() {
		return flagCorr;
	}


	public void setFlagCorr(String flagCorr) {
		this.flagCorr = flagCorr;
	}


	public String getFlagInte() {
		return flagInte;
	}


	public void setFlagInte(String flagInte) {
		this.flagInte = flagInte;
	}


	public String getStatoDic() {
		return statoDic;
	}


	public void setStatoDic(String statoDic) {
		this.statoDic = statoDic;
	}


	public String getFlagValuta() {
		return flagValuta;
	}


	public void setFlagValuta(String flagValuta) {
		this.flagValuta = flagValuta;
	}


	public String getDicCf() {
		return dicCf;
	}


	public void setDicCf(String dicCf) {
		this.dicCf = dicCf;
	}


	public String getFlagDicConiu() {
		return flagDicConiu;
	}


	public void setFlagDicConiu(String flagDicConiu) {
		this.flagDicConiu = flagDicConiu;
	}


	public String getDicCogn() {
		return dicCogn;
	}


	public void setDicCogn(String dicCogn) {
		this.dicCogn = dicCogn;
	}


	public String getDicNome() {
		return dicNome;
	}


	public void setDicNome(String dicNome) {
		this.dicNome = dicNome;
	}


	public String getDicNaLuogo() {
		return dicNaLuogo;
	}


	public void setDicNaLuogo(String dicNaLuogo) {
		this.dicNaLuogo = dicNaLuogo;
	}





	public String getDicSesso() {
		return dicSesso;
	}


	public void setDicSesso(String dicSesso) {
		this.dicSesso = dicSesso;
	}


	public String getDicAttRe() {
		return dicAttRe;
	}


	public void setDicAttRe(String dicAttRe) {
		this.dicAttRe = dicAttRe;
	}


	public String getDicAttRf() {
		return dicAttRf;
	}


	public void setDicAttRf(String dicAttRf) {
		this.dicAttRf = dicAttRf;
	}


	public String getDicAttRg() {
		return dicAttRg;
	}


	public void setDicAttRg(String dicAttRg) {
		this.dicAttRg = dicAttRg;
	}


	public String getDicFiller() {
		return dicFiller;
	}


	public void setDicFiller(String dicFiller) {
		this.dicFiller = dicFiller;
	}


	public String getDomFiscAnnoPrec() {
		return domFiscAnnoPrec;
	}


	public void setDomFiscAnnoPrec(String domFiscAnnoPrec) {
		this.domFiscAnnoPrec = domFiscAnnoPrec;
	}


	public String getDomDFiscAnnoAtt() {
		return domDFiscAnnoAtt;
	}


	public void setDomDFiscAnnoAtt(String domDFiscAnnoAtt) {
		this.domDFiscAnnoAtt = domDFiscAnnoAtt;
	}


	public String getIndirAttuale() {
		return indirAttuale;
	}


	public void setIndirAttuale(String indirAttuale) {
		this.indirAttuale = indirAttuale;
	}


	public HashMap<String, DecoQuadro> getQuadro() {
		return quadro;
	}


	public void setQuadro(HashMap<String, DecoQuadro> quadro) {
		this.quadro = quadro;
	}
	
	public void addQuadro(String key, DecoQuadro quadro) {
		this.quadro.put(key, quadro);
	}


	public String getDicNaData() {
		return dicNaData;
	}


	public void setDicNaData(String dicNaData) {
		this.dicNaData = dicNaData;
	}











	

}
