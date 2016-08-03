package it.webred.diogene.visualizzatore.beans;

/**
 * Bean che contiene i dati per la definizione di un valore generico tramite una coppia identificativo - valore.<p>
 * Di utilità per la definizione di parametri specifici di filtro (tradotti in XML e salvati come XMLTYPE in tabella 
 * DV_FORMAT_CLASSE)
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public class GenericValue {

	/**
	 * Identificativo del GenericValue.
	 */
	private Object id;
	/**
	 * Valore del GenericValue.
	 */
	private String valueDesc;
	
	/**
	 * Costruttore che inizializza i campi del bean.
	*	@param id L'identificativo (Object)
	*	@param valueDesc Il valore (String)
	*/
	public GenericValue(Object id, String valueDesc) {
		this.id = id;
		this.valueDesc = valueDesc;
	}
	public Object getId() {
		return id;
	}
	public void setId(Object id) {
		this.id = id;
	}
	public String getValueDesc() {
		return valueDesc;
	}
	public void setValueDesc(String valueDesc) {
		this.valueDesc = valueDesc;
	}
	/**
	 * Restituisce l'identificativo del GenericValue come String.
	 * @return L'identificativo del GenericValue come String
	 */
	public String getIdAsString() {
		return id == null ? null : id.toString();
	}
	/**
	 * Restituisce l'identificativo del GenericValue come Long.
	 * @return L'identificativo del GenericValue come Long
	 * @throws NumberFormatException Se si verifica un'eccezione di parsing
	 */
	public Long getIdAsLong() throws NumberFormatException {
		return id == null ? null : Long.parseLong(id.toString());
	}
	
}
