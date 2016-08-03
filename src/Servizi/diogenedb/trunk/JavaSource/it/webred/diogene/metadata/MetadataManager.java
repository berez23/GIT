package it.webred.diogene.metadata;

import static it.webred.utils.MetadataConstants.*;

/**
 * Classe che gestisce le operazioni di connessione e lettura di metadati dal DB di origine, tramite l'istanza di 
 * una MetadataFactory appropriata per il tipo di DB.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class MetadataManager {
	
	/**
	 * Un codice che rappresenta il tipo di database.
	 */
	private String database = "";
	
	public MetadataManager() {
		super();
	}
	
	/**
	* Costruttore che imposta il valore del codice tipo DB secondo il parametro DBType (da it.webred.utils.MetadataConstants).
	*	@param db Il DBType
	*/
	public MetadataManager(DBType db) {
		this.database = DBType.valueOf(DBType.class, db.name()).stringValue();
	}
	
	/**
	* Costruttore che imposta il valore del codice tipo DB secondo il parametro String che rappresenta la 
	* descrizione estesa del tipo DB.
	*	@param std Descrizione estesa del tipo DB
	*/
	public MetadataManager(String std) {
		this.database = getStringValueFromDisplay(std);
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	/** 
	 * Restituisce un'istanza della MetadataFactory appropriata per il tipo DB selezionato
	 * @return Un'istanza della MetadataFactory appropriata per il DB selezionato
	 * @throws Exception Se non è possibile reperire una MetadataFactory appropriata
	 */
	public MetadataFactory getMetadataFactory() throws Exception
	{
		if (database == null || database.equals(""))
			throw new Exception("Tipo DB non impostato");
		String mdFstring = MetadataFactory.class.getPackage().getName() + ".impl." + database + "MetadataFactory";
		try {
			MetadataFactory mdF = (MetadataFactory)Class.forName(mdFstring).newInstance();
			return mdF;
		}
		catch (Exception e) {
			throw new Exception("Impossibile reperire metadata factory " + mdFstring);
		}
	}
	
}
