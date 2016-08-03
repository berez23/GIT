package it.webred.diogene.correlazione.beans;

import it.webred.diogene.metadata.beans.Table;

import java.util.ArrayList;
import java.util.Date;

/**
 * Bean che contiene i dati di una ricerca in uno o più archivi consultabili.
 * @author Filippo Mazzini
 * @version $Revision: 1.2 $ $Date: 2007/09/13 13:26:48 $
 */
public class Ricerca {
	
	/**
	 * Data di effettuazione della ricerca.
	 */
	private Date dataRicerca;
	/**
	 * Tema selezionato.
	 */
	private Tema tema;
	/**
	 * Tipi etichetta selezionati.
	 */
	private ArrayList<TipoEtichetta> tipiEtichetta;
	/**
	 * Archivi selezionati.
	 */
	private ArrayList<Table> archivi;
	/**
	 * Risultati della ricerca.
	 */
	private ArrayList<ArrayList<ArrayList<Object>>> risultati;
	/**
	 * Risultati della ricerca per la visualizzazione nella pagina (o per la stampa).
	 */
	private ArrayList<ArrayList<ArrayList<String>>> risultatiVis;
	/**
	 * Numero totale di righe trovate per ogni query effettuata nella ricerca (serve per la paginazione).
	 */
	private ArrayList<Integer> recordCount;
	

	public Date getDataRicerca()
	{
		return dataRicerca;
	}
	public void setDataRicerca(Date dataRicerca)
	{
		this.dataRicerca = dataRicerca;
	}
	public ArrayList<ArrayList<ArrayList<Object>>> getRisultati()
	{
		return risultati;
	}
	public void setRisultati(ArrayList<ArrayList<ArrayList<Object>>> risultati)
	{
		this.risultati = risultati;
	}
	public ArrayList<ArrayList<ArrayList<String>>> getRisultatiVis()
	{
		return risultatiVis;
	}
	public void setRisultatiVis(ArrayList<ArrayList<ArrayList<String>>> risultatiVis)
	{
		this.risultatiVis = risultatiVis;
	}
	public ArrayList<Table> getArchivi()
	{
		return archivi;
	}
	public void setArchivi(ArrayList<Table> archivi)
	{
		this.archivi = archivi;
	}
	public Tema getTema()
	{
		return tema;
	}
	public void setTema(Tema tema)
	{
		this.tema = tema;
	}
	public ArrayList<TipoEtichetta> getTipiEtichetta()
	{
		return tipiEtichetta;
	}
	public void setTipiEtichetta(ArrayList<TipoEtichetta> tipiEtichetta)
	{
		this.tipiEtichetta = tipiEtichetta;
	}
	public ArrayList<Integer> getRecordCount()
	{
		return recordCount;
	}
	public void setRecordCount(ArrayList<Integer> recordCount)
	{
		this.recordCount = recordCount;
	}
	
}
