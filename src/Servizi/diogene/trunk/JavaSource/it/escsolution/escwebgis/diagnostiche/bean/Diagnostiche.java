
package it.escsolution.escwebgis.diagnostiche.bean;

import it.escsolution.escwebgis.common.EscObject;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Diagnostiche extends EscObject implements Serializable{
	String descrizioneFonte ;
	String codCommand;
	String descCommand;
	String sqlLista;
	String sqlExport;
	String tabellaDiagnostiche;
	String idCommand;
	String elaborazioni;
	String idCfgTemplate;
	LinkedHashMap<String, String> dateLancioController;
	List<Diagnostiche> listDiagnostiche = new ArrayList<Diagnostiche>(); 
	String tipo;
	String idFonte;
	String timeControllerSelected;
	String processId;
	String idCommanLaunch;
	TreeMap<String, Diagnostiche> listaCommandDiagnostiche = new TreeMap<String, Diagnostiche>();
	public String getUrlListaDiagnostica()
	throws Exception
	{
		String url="../Diagnostiche?UC=101&ST=2";
		url+="&COD_FONTE="+idFonte;
		url+="&cmd="+idCommand;
		url+="&IDTEMPL="+idCfgTemplate;
		url+="&idComLau="+idCommanLaunch;
		url+="&dataController=null";		
		return url;
	}
	
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getIdCfgTemplate()
	{
		return idCfgTemplate;
	}
	public void setIdCfgTemplate(String idCfgTemplate)
	{
		this.idCfgTemplate = idCfgTemplate;
	}
	public String getIdCommand()
	{
		return idCommand;
	}
	public void setIdCommand(String idCommand)
	{
		this.idCommand = idCommand;
	}
	public String getCodCommand()
	{
		return codCommand;
	}
	public void setCodCommand(String codCommand)
	{
		this.codCommand = codCommand;
	}
	public String getDescrizioneFonte()
	{
		return descrizioneFonte;
	}
	public void setDescrizioneFonte(String descrizioneFonte)
	{
		this.descrizioneFonte = descrizioneFonte;
	}
	public String getSqlExport()
	{
		return sqlExport;
	}
	public void setSqlExport(String sqlExport)
	{
		this.sqlExport = sqlExport;
	}
	public String getSqlLista()
	{
		return sqlLista;
	}
	public void setSqlLista(String sqlLista)
	{
		this.sqlLista = sqlLista;
	}
	public String getTabellaDiagnostiche()
	{
		return tabellaDiagnostiche;
	}
	public void setTabellaDiagnostiche(String tabellaDiagnostiche)
	{
		this.tabellaDiagnostiche = tabellaDiagnostiche;
	}
	public String getDescCommand()
	{
		return descCommand;
	}
	public void setDescCommand(String descCommand)
	{
		this.descCommand = descCommand;
	}
	public String getElaborazioni()
	{
		return elaborazioni;
	}
	public void setElaborazioni(String elaborazioni)
	{
		this.elaborazioni = elaborazioni;
	}
	public LinkedHashMap<String, String> getDateLancioController() {
		return dateLancioController;
	}
	public void setDateLancioController(
			LinkedHashMap<String, String> dateLancioController) {
		this.dateLancioController = dateLancioController;
	}
	public List<Diagnostiche> getListDiagnostiche() {
		return listDiagnostiche;
	}
	public void setListDiagnostiche(List<Diagnostiche> listDiagnostiche) {
		this.listDiagnostiche = listDiagnostiche;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getIdFonte() {
		return idFonte;
	}
	public void setIdFonte(String idFonte) {
		this.idFonte = idFonte;
	}
	public String getTimeControllerSelected() {
		return timeControllerSelected;
	}
	public void setTimeControllerSelected(String timeControllerSelected) {
		this.timeControllerSelected = timeControllerSelected;
	}
	public String getIdCommanLaunch() {
		return idCommanLaunch;
	}
	public void setIdCommanLaunch(String idCommanLaunch) {
		this.idCommanLaunch = idCommanLaunch;
	}
	public TreeMap<String, Diagnostiche> getListaCommandDiagnostiche() {
		return listaCommandDiagnostiche;
	}
	public void setListaCommandDiagnostiche(
			TreeMap<String, Diagnostiche> listaCommandDiagnostiche) {
		this.listaCommandDiagnostiche = listaCommandDiagnostiche;
	}
	



}
