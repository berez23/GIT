package it.webred.diogene.visualizzatore.beans;

import java.util.ArrayList;

/**
 * Bean che contiene i dati di un record della tabella DV_FORMAT_CLASSE (definizione entità per una classe non contenitore).<p>
 * I dati per la definizione di filtro, lista e dettaglio (campi XMLTYPE), gestiti da bean appositi (classi java Filter, FcList 
 * e DetailGroup / Detail) sono compresi in questo bean in forma di ArrayList rispettivamente di Filter, FcList e DetailGroup.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public class FormatClasse {

	/**
	 * Identificativo della classe (chiave esterna in tabella DV_CLASSE).
	 */
	private Long dvClasse;
	/**
	 * Identificativo della user entity (chiave esterna in tabella DV_USER_ENTITY).
	 */
	private Long dvUserEntity;
	/**
	 * ArrayList di oggetti Filter contenente i dati per la definizione del filtro (un oggetto Filter per ogni colonna 
	 * aggiunta al filtro).
	 */
	private ArrayList<Filter> filter;
	/**
	 * ArrayList di oggetti FcList contenente i dati per la definizione della lista (un oggetto FcList per ogni colonna 
	 * o javascript aggiunto alla lista).
	 */
	private ArrayList<FcList> list;
	/**
	 * ArrayList di oggetti DetailGroup contenente i dati per la definizione del dettaglio (un oggetto DetailGroup per 
	 * ogni tabella o divisione aggiunta al dettaglio). Se l'oggetto DetailGroup rappresenta una tabella, ogni oggetto 
	 * Detail in esso contenuto rappresenta una riga della tabella.
	 */
	private ArrayList<DetailGroup> detail;
	/**
	 * ArrayList di oggetti Link contenente i dati per la definizione dei link (un oggetto Link per ogni link 
	 * disponibile per la classe).
	 */
	private ArrayList<Link> link;	
	/**
	 * Titolo del filtro.
	 */
	private String filterTitle;
	/**
	 * Titolo della lista.
	 */
	private String listTitle;
	/**
	 * Titolo del dettaglio.
	 */
	private String detailTitle;
	
	
	public Long getDvClasse() {
		return dvClasse;
	}
	public void setDvClasse(Long dvClasse) {
		this.dvClasse = dvClasse;
	}
	public Long getDvUserEntity() {
		return dvUserEntity;
	}
	public void setDvUserEntity(Long dvUserEntity) {
		this.dvUserEntity = dvUserEntity;
	}
	public ArrayList<Filter> getFilter() {
		return filter;
	}
	public void setFilter(ArrayList<Filter> filter) {
		this.filter = filter;
	}
	public ArrayList<FcList> getList() {
		return list;
	}
	public void setList(ArrayList<FcList> list) {
		this.list = list;
	}
	public ArrayList<DetailGroup> getDetail() {
		return detail;
	}
	public void setDetail(ArrayList<DetailGroup> detail) {
		this.detail = detail;
	}
	public String getFilterTitle() {
		return filterTitle;
	}
	public void setFilterTitle(String filterTitle) {
		this.filterTitle = filterTitle;
	}
	public String getListTitle() {
		return listTitle;
	}
	public void setListTitle(String listTitle) {
		this.listTitle = listTitle;
	}
	public String getDetailTitle() {
		return detailTitle;
	}
	public void setDetailTitle(String detailTitle) {
		this.detailTitle = detailTitle;
	}
	public ArrayList<Link> getLink() {
		return link;
	}
	public void setLink(ArrayList<Link> link) {
		this.link = link;
	}

}
