package it.webred.diogene.correlazione.beans;

/**
 * Bean che contiene i dati necessari per la gestione (visualizzazione, inserimento, modifica, cancellazione) di un tema.
 * @author Filippo Mazzini
 * @version $Revision: 1.2 $ $Date: 2007/09/13 13:26:48 $
 */
public class Tema {
	
	/**
	 *	Identificativo del tema.
	 */
	private Long id;
	/**
	 *	Nome del tema.
	 */
	private String name;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		Tema retVal = new Tema();
		retVal.setId(this.getId());
		retVal.setName(this.getName());
		return retVal;
	}	
	
}
