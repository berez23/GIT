package it.webred.diogene.correlazione.backingbeans;

import it.webred.diogene.correlazione.beans.Tema;

import java.util.ArrayList;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * Backing bean utilizzato per la visualizzazione della lista dei temi che apre la visualizzazione dei dati da archivi.
 * @author Filippo Mazzini
 * @version $Revision: 1.4 $ $Date: 2007/10/08 07:39:44 $
 */
public class ListaTemiBB extends TemiBB
{
	
	/**
	 * ArrayList di oggetti di classe Tema, che contiene il tema selezionato nella lista dei temi. 
	 * Il tema selezionato è restituito incluso in una ArrayList perché deve essere visualizzato in una dataTable.
	 */
	private ArrayList<Tema> temaSel;
	/**
	 * Flag che indica se nelle pagine di visualizzazione deve essere visibile il pulsante di uscita dalla visualizzazione.
	 * Se le pagine sono aperte in popup dalla pagina ApriInPopup, il pulsante non deve essere visibile.
	 */
	private boolean renderedBack = true;
	
	/**
	*	Costruttore che crea un ListaTemiBB vuoto.
	*/
	public ListaTemiBB() {
		super();
		Map map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if (map.containsKey("renderedBack")) {
			renderedBack = ((Boolean)map.get("renderedBack")).booleanValue();
			map.remove("renderedBack");
		}
	}
	
	/**
	 * Effettua la selezione del tema prescelto nella lista dei temi, chiamando la pagina di visualizzazione dei tipi etichetta 
	 * associati al tema.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina di 
	 * visualizzazione dei tipi etichetta associati ad un tema.
	 */
	public String seleziona(){
		ArrayList<Tema> temi = getTemi();
		temaSel = new ArrayList<Tema>();
		for (Tema tema : temi) {
			if (tema.getId().longValue() == new Long(getIdTemaSel()).longValue()) {
				temaSel.add(tema);
				break;
			}
		}
		//per eventuale chiamata della lista temi direttamente da URL
		Map map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if (map.containsKey("ListaTipiEtichetta"))
			map.remove("ListaTipiEtichetta");
		if (map.containsKey("ListaArchivi"))
			map.remove("ListaArchivi");
		if (map.containsKey("Ricerche"))
			map.remove("Ricerche");
		
		return "vaiAListaTipiEtichetta";
	}

	public ArrayList<Tema> getTemaSel()
	{
		return temaSel;
	}

	public void setTemaSel(ArrayList<Tema> temaSel)
	{
		this.temaSel = temaSel;
	}

	public boolean isRenderedBack()
	{
		return renderedBack;
	}

	public void setRenderedBack(boolean renderedBack)
	{
		this.renderedBack = renderedBack;
	}
	
}
