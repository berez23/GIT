package it.webred.diogene.correlazione.backingbeans;

import it.webred.diogene.correlazione.beans.TipoEtichetta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * Backing bean utilizzato per la visualizzazione della lista dei tipi etichetta per un tema selezionato, in fase di 
 * visualizzazione dei dati da archivi.
 * @author Filippo Mazzini
 * @version $Revision: 1.3 $ $Date: 2007/09/24 15:01:59 $
 */ 
public class ListaTipiEtichettaBB extends TipiEtichettaBB
{
	
	/**
	 * ArrayList di oggetti di classe TipoEtichetta, che rappresenta i tipi etichetta selezionati nella lista relativa.
	 */
	private ArrayList<TipoEtichetta> selTipiEtichetta;
	/**
	 * Rappresenta la dimensione (size) dell'ArrayList selTipiEtichetta
	 */
	private Integer numeroSelTipiEtichetta = new Integer(0);
	/**
	 * ArrayList di oggetti di classe TipoEtichetta, che rappresenta i tipi etichetta non selezionati nella lista relativa.
	 */
	private ArrayList<TipoEtichetta> altriTipiEtichetta;
	/**
	 * Costante di tipo String utilizzata per identificare, tra i parametri della request, quelli che rappresentano la selezione 
	 * dei tipi etichetta nella lista relativa (la cui chiave comincia per...).
	 */
	private final String SEL_STARTS_WITH = "form:listaTipiEtichetta_";
	/**
	 * Costante di tipo String utilizzata per identificare, tra i parametri della request, quelli che rappresentano la selezione 
	 * dei tipi etichetta nella lista relativa (la cui chiave termina con...).
	 */
	private final String SEL_ENDS_WITH = ":selTipoe";
	/**
	 * Costante di tipo String che rappresenta il formato da visualizzare a fianco dei tipi etichetta di tipo data.
	 */
	public static final String FORMATO_DATA_VIS = "gg/mm/aaaa";
	
	
	/**
	*	Costruttore che crea un ListaTipiEtichettaBB vuoto.
	*/
	public ListaTipiEtichettaBB() {
		super();
		selTipiEtichetta = new ArrayList<TipoEtichetta>();
	}
	
	/* (non-Javadoc)
	 * @see it.webred.diogene.correlazione.backingbeans.TipiEtichettaBB#getIdTemaSel()
	 */
	protected Long getIdTemaSel() {
		Map map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if (map.containsKey("ListaTemi")) {
			ListaTemiBB listaTemiBB = (ListaTemiBB)map.get("ListaTemi");
			return new Long(listaTemiBB.getIdTemaSel());
		}
		return null;
	}
	
	/**
	 * Salva la selezione dei tipi etichetta effettuata nella lista relativa e chiama la pagina di selezione archivi.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della 
	 * pagina di selezione archivi.
	 */
	public String seleziona() {
		Map map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		Iterator it = map.keySet().iterator();
		ArrayList<Integer> arrList = new ArrayList<Integer>();
		while (it.hasNext()) {
			String key = (String)it.next();
			if (key.startsWith(SEL_STARTS_WITH) && key.endsWith(SEL_ENDS_WITH)) {
				Integer index = new Integer(key.substring(SEL_STARTS_WITH.length(), key.length() - SEL_ENDS_WITH.length()));
				arrList.add(index);
			}
		}
		Object[] objArr = arrList.toArray();
		Integer[] arr = new Integer[objArr.length];
		for (int i = 0; i < objArr.length; i++) {
			arr[i] = (Integer)objArr[i];
		}
		Arrays.sort(arr);
		ArrayList<TipoEtichetta> tipiEtichetta = getTipiEtichetta();
		selTipiEtichetta = new ArrayList<TipoEtichetta>();
		altriTipiEtichetta = new ArrayList<TipoEtichetta>();
		for (Integer index : arr) {
			selTipiEtichetta.add(tipiEtichetta.get(index.intValue()));
		}
		numeroSelTipiEtichetta = selTipiEtichetta.size();
		for (int i = 0; i < tipiEtichetta.size(); i++) {
			boolean sel = false;
			for (Integer index : arr) {
				if (index.intValue() == i) {
					sel = true;
					break;
				}
			}
			if (!sel) {
				altriTipiEtichetta.add(tipiEtichetta.get(i));
			}
		}
		return "vaiAListaArchivi";
	}
	
	/**
	 * Elimina il bean ListaTipiEtichetta dalla SessionMap perché venga chiamato di nuovo il suo costruttore 
	 * ripristinando la situazione iniziale (come alla prima apertura della pagina).
	 */
	private void cancellaForm() {
		/*elimino il bean ListaTipiEtichetta dalla SessionMap perché venga chiamato di nuovo il suo costruttore
		 ripristinando la situazione iniziale (come alla prima apertura della pagina)*/
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("ListaTipiEtichetta");
	}	
	
	/**
	 * Effettua il rientro alla pagina di visualizzazione lista temi.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di visualizzazione lista temi.
	 */
	public String esci() {
		cancellaForm();
		return "vaiAListaTemi";
	}

	public ArrayList<TipoEtichetta> getSelTipiEtichetta()
	{
		return selTipiEtichetta;
	}

	public void setSelTipiEtichetta(ArrayList<TipoEtichetta> selTipiEtichetta)
	{
		this.selTipiEtichetta = selTipiEtichetta;
	}

	public Integer getNumeroSelTipiEtichetta()
	{
		return numeroSelTipiEtichetta;
	}

	public void setNumeroSelTipiEtichetta(Integer numeroSelTipiEtichetta)
	{
		this.numeroSelTipiEtichetta = numeroSelTipiEtichetta;
	}

	public ArrayList<TipoEtichetta> getAltriTipiEtichetta()
	{
		return altriTipiEtichetta;
	}

	public void setAltriTipiEtichetta(ArrayList<TipoEtichetta> altriTipiEtichetta)
	{
		this.altriTipiEtichetta = altriTipiEtichetta;
	}
	
}
