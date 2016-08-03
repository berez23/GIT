package it.webred.diogene.sqldiagram;

/**
 * TODO Scrivi descrizione
 * @author Giulio Quaresima
 * @author Marco Riccardini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public interface Operator extends Cloneable 
{
	/**
	 * @return
	 * Il nome univoco dell'operatore. 
	 */
	public String getName();
	
	/**
	 * @return
	 * Una nuova istanza dell'operatore, clonata su questo
	 * operatore. Se la classe che implementa l'operatore 
	 * contiene dei riferimenti a classi clonabili, dovr&agrave;
	 * clonare anche tutte quelle classi. Se la classe che implementa
	 * l'operatore contiene {@link java.util.Collection collections},
	 * dovr&agrave; clonare ogni eventuale classe clonabile contenuta
	 * all'interno di tali collections.
	 * @throws CloneNotSupportedException
	 */
	public Operator clone() throws CloneNotSupportedException;
	
	/**
	 * @return Come indicazione del tutto generale, la
	 * rappresentazione SQL di questo operatore.
	 * E' ovvio che ci&ograve; non potr&agrave; valere per 
	 * tutti gli operatori, in quanto la rappresentazione
	 * SQL di taluni di essi potr&agrave; dipendere dal contesto
	 * in cui si trovano.
	 */
	public String toString();
	/**
	 * @return Una chiave
	 * utile per individuare l'espressione
	 * univocamente in contesto
	 * di user interface
	 */
	public String getUIKey();

	/**
	 * @param key
	 * Una chiave
	 * utile per individuare l'espressione
	 * univocamente in contesto
	 * di user interface
	 */
	public void setUIKey(String key);
}
