/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.vus.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class VusGas extends EscObject implements Serializable
{
	private String codSer;
	private String ragSocUbi;
	private String viaUbi;
	private String civicoUbi;
	private String capUbi;
	private String comuneUbi;
	private String consumoMedio;
	private String codCategoria;
    private String descCategoria;
    private String ragiSoc;
    private String viaResi;
    private String civicoResi;
    private String capResi;
    private String comuneResi;
    private String prResi;
    private String chiave;
    private ArrayList datiCatastali;
    private String codiFisc;
    private String partitaIva;
	
	
	public VusGas()
	{
		this.codSer = "";
		this.ragSocUbi = "";
		this.viaUbi = "";
		this.civicoUbi = "";
		this.capUbi = "";
		this.comuneUbi = "";
		this.consumoMedio = "";
		this.codCategoria = "";
		this.descCategoria = "";
		this.ragiSoc = "";
		this.viaResi = "";
		this.civicoResi = "";
		this.capResi = "";
		this.comuneResi = "";
		this.prResi = "";
		this.chiave = "";
		this.datiCatastali = new ArrayList();
		this.codiFisc = "";
		this.partitaIva = "";
	}


	public String getCodSer() {
		return codSer;
	}


	public void setCodSer(String codSer) {
		this.codSer = codSer;
	}

	public String getChiave() {
		return this.codSer;
	}


	public ArrayList getDatiCatastali() {
		return datiCatastali;
	}


	public void setDatiCatastali(ArrayList datiCatastali) {
		this.datiCatastali = datiCatastali;
	}


	public String getCapResi() {
		return capResi;
	}


	public void setCapResi(String capResi) {
		this.capResi = capResi;
	}


	public String getCapUbi() {
		return capUbi;
	}


	public void setCapUbi(String capUbi) {
		this.capUbi = capUbi;
	}


	public String getCivicoResi() {
		return civicoResi;
	}


	public void setCivicoResi(String civicoResi) {
		this.civicoResi = civicoResi;
	}


	public String getCivicoUbi() {
		return civicoUbi;
	}


	public void setCivicoUbi(String civicoUbi) {
		this.civicoUbi = civicoUbi;
	}


	public String getCodCategoria() {
		return codCategoria;
	}


	public void setCodCategoria(String codCategoria) {
		this.codCategoria = codCategoria;
	}


	public String getComuneResi() {
		return comuneResi;
	}


	public void setComuneResi(String comuneResi) {
		this.comuneResi = comuneResi;
	}


	public String getComuneUbi() {
		return comuneUbi;
	}


	public void setComuneUbi(String comuneUbi) {
		this.comuneUbi = comuneUbi;
	}


	public String getConsumoMedio() {
		return consumoMedio;
	}


	public void setConsumoMedio(String consumoMedio) {
		this.consumoMedio = consumoMedio;
	}


	public String getDescCategoria() {
		return descCategoria;
	}


	public void setDescCategoria(String descCategoria) {
		this.descCategoria = descCategoria;
	}


	public String getPrResi() {
		return prResi;
	}


	public void setPrResi(String prResi) {
		this.prResi = prResi;
	}


	public String getRagiSoc() {
		return ragiSoc;
	}


	public void setRagiSoc(String ragiSoc) {
		this.ragiSoc = ragiSoc;
	}


	public String getRagSocUbi() {
		return ragSocUbi;
	}


	public void setRagSocUbi(String ragSocUbi) {
		this.ragSocUbi = ragSocUbi;
	}


	public String getViaResi() {
		return viaResi;
	}


	public void setViaResi(String viaResi) {
		this.viaResi = viaResi;
	}


	public String getViaUbi() {
		return viaUbi;
	}


	public void setViaUbi(String viaUbi) {
		this.viaUbi = viaUbi;
	}


	public String getCodiFisc() {
		return codiFisc;
	}


	public void setCodiFisc(String codiFisc) {
		this.codiFisc = codiFisc;
	}


	public String getPartitaIva() {
		return partitaIva;
	}


	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}


}
