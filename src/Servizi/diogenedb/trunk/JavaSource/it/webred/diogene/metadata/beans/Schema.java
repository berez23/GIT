package it.webred.diogene.metadata.beans;

/**
 * Bean che contiene i dati necessari per il recupero degli schemi (schemas) presenti nel DB di origine.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class Schema {
	
	/**
	 *	Il nome dello schema.
	 */
	private String tableSchema;
	/**
	 *	Il nome del catalog
	 */
	private String tableCatalog;
	
	public String getTableCatalog() {
		return tableCatalog;
	}
	
	public void setTableCatalog(String tableCatalog) {
		this.tableCatalog = tableCatalog;
	}
	
	public String getTableSchema() {
		return tableSchema;
	}
	
	public void setTableSchema(String tableSchema) {
		this.tableSchema = tableSchema;
	}
	
}
