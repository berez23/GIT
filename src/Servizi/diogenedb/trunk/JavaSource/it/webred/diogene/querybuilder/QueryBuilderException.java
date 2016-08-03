package it.webred.diogene.querybuilder;

/**
 * TODO Scrivi descrizione
 * @author Giulio Quaresima
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class QueryBuilderException extends Exception
{
	private static final long serialVersionUID = -8316953312219127988L;
	
	private String componentId;
	private String[] messageFormatParameters;

	private QueryBuilderException() {}
	/**
	* @param resourceKey
	* La chiave del {@link java.util.Properties} o del
	* {@link java.util.ResourceBundle} contenente il messaggio
	* @param componentId
	* Utile in contesto JSF, se non null serve per stabilire
	* a qualle componente visuale associare il messaggio.
	*/
	public QueryBuilderException(String resourceKey, String componentId)
	{
		super(resourceKey);
		this.componentId = componentId;
	}
	/**
	* @param resourceKey
	* La chiave del {@link java.util.Properties} o del
	* {@link java.util.ResourceBundle} contenente il messaggio
	* @param componentId
	* Utile in contesto JSF, se non null serve per stabilire
	* a qualle componente visuale associare il messaggio.
	* @param messageFormatParameters
	* I parametri da passare al messaggio, se esistono. 
	* vedi {@link java.text.MessageFormat}
	*/
	public QueryBuilderException(String resourceKey, String componentId, String... messageFormatParameters)
	{
		super(resourceKey);
		this.componentId = componentId;
		this.messageFormatParameters = messageFormatParameters;
	}
	
	/**
	* Utile in contesto JSF, se non null serve per stabilire
	* a qualle componente visuale associare il messaggio.
	 * @return
	 */
	public String getComponentId()
	{
		return componentId;
	}
	/**
	* Utile in contesto JSF, se non null serve per stabilire
	* a qualle componente visuale associare il messaggio.
	 * @param componentId
	 */
	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}
	/**
	* I parametri da passare al messaggio, se esistono. 
	* @see java.text.MessageFormat
	 * @return
	 */
	public String[] getMessagFormatParameters()
	{
		return messageFormatParameters;
	}
	/**
	* I parametri da passare al messaggio, se esistono. 
	* @see java.text.MessageFormat
	 * @param messagFormatParameters
	 */
	public void setMessagFormatParameters(String[] messagFormatParameters)
	{
		this.messageFormatParameters = messagFormatParameters;
	}
	/**
	* La chiave del {@link java.util.Properties} o del
	* {@link java.util.ResourceBundle} contenente il messaggio
	 * @return
	 */
	public String getResourceKey()
	{
		return getMessage();
	}
}
