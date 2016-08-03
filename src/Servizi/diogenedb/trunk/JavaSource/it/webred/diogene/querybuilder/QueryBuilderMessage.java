package it.webred.diogene.querybuilder;

import java.io.Serializable;

/**
 * Questa classe serve per spedire messaggi all-interno
 * di questo componente.
 * @see it.webred.diogene.querybuilder.control.MessagesHolder
 * @author Giulio Quaresima
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class QueryBuilderMessage implements Serializable
{
	private static final long serialVersionUID = 7039045475963669692L;

	private String
		resourceKey,
		elementId;
	private String[] messageFormatParameters;
	
	private QueryBuilderMessage() {}
	
	/**
	* @param resourceKey
	* La chiave del {@link java.util.Properties} o del
	* {@link java.util.ResourceBundle} contenente il messaggio
	* @param elementId
	* Utile in contesto JSF, se non null serve per stabilire
	* a qualle componente visuale associare il messaggio.
	* @param messageFormatParameters
	* I parametri da passare al messaggio, se esistono. 
	* vedi {@link java.text.MessageFormat}
	*/
	public QueryBuilderMessage(String resourceKey, String elementId, String... messageFormatParameters)
	{
		super();
		this.resourceKey = resourceKey;
		this.elementId = elementId;
		this.messageFormatParameters = messageFormatParameters;
	}
	/**
	 * Costruisce il messaggio partendo dall'eccezione.
	 * @param e
	 */
	public QueryBuilderMessage(QueryBuilderException e)
	{
		super();
		if (e != null)
		{
			this.resourceKey = e.getMessage();
			this.elementId = e.getComponentId();
			this.messageFormatParameters = e.getMessagFormatParameters();			
		}
	}

	/**
	* Utile in contesto JSF, se non null serve per stabilire
	* a qualle componente visuale associare il messaggio.
	 */
	public String getElementId()
	{
		return elementId;
	}

	/**
	* Utile in contesto JSF, se non null serve per stabilire
	* a qualle componente visuale associare il messaggio.
	 * @param elementId
	 */
	public void setElementId(String elementId)
	{
		this.elementId = elementId;
	}

	/**
	* I parametri da passare al messaggio, se esistono. 
	* @see java.text.MessageFormat
	 * @return
	 */
	public String[] getMessageFormatParameters()
	{
		return messageFormatParameters;
	}

	/**
	* I parametri da passare al messaggio, se esistono. 
	* @see java.text.MessageFormat
	 * @param messageFormatParameters
	 */
	public void setMessageFormatParameters(String[] messageFormatParameters)
	{
		this.messageFormatParameters = messageFormatParameters;
	}

	/**
	* La chiave del {@link java.util.Properties} o del
	* {@link java.util.ResourceBundle} contenente il messaggio
	 * @return
	 */
	public String getResourceKey()
	{
		return resourceKey;
	}

	/**
	* La chiave del {@link java.util.Properties} o del
	* {@link java.util.ResourceBundle} contenente il messaggio
	 * @param resourceKey
	 */
	public void setResourceKey(String resourceKey)
	{
		this.resourceKey = resourceKey;
	}
}
