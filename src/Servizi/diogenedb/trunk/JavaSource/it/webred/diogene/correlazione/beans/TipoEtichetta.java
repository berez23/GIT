package it.webred.diogene.correlazione.beans;

/**
 * Bean che contiene i dati necessari per la gestione (visualizzazione, inserimento, modifica, cancellazione) di un tipo etichetta.
 * @author Filippo Mazzini
 * @version $Revision: 1.5 $ $Date: 2007/11/22 15:59:57 $
 */
public class TipoEtichetta {
	
	/**
	 *	Identificativo del tipo etichetta.
	 */
	private Long id;
	/**
	 *	Nome del tipo etichetta.
	 */
	private String name;
	/**
	 *	Identificativo del tema a cui è associato il tipo etichetta.
	 */
	private Long fkDvTema;
	/**
	 *	Nome del tema a cui è associato il tipo etichetta.
	 */
	private String nameTema;
	/**
	 *	Chiave specificata (nella pagina) per il tipo etichetta.
	 */
	private String chiave;
	/**
	 *	Operatore (es. "=", "<>" ecc.) specificato (nella pagina) per il tipo etichetta.
	 */
	private String operatore;
	/**
	 *	Flag che indica se il tipo etichetta è selezionato, nel caso, ad esempio, di lista con check box.
	 */
	private boolean selezionato;
	/**
	 *	Formato eventualmente da visualizzare (nella pagina) per il tipo etichetta; ad esempio, "gg/mm/aaaa" per i 
	 *	campi di tipo data.
	 */
	private String formato;
	
	public Long getFkDvTema()
	{
		return fkDvTema;
	}
	public void setFkDvTema(Long fkDvTema)
	{
		this.fkDvTema = fkDvTema;
	}
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
	public String getNameTema()
	{
		return nameTema;
	}
	public void setNameTema(String nameTema)
	{
		this.nameTema = nameTema;
	}
	public String getChiave()
	{
		return chiave;
	}
	public void setChiave(String chiave)
	{
		this.chiave = chiave;
	}
	public String getOperatore()
	{
		return operatore;
	}
	public void setOperatore(String operatore)
	{
		this.operatore = operatore;
	}
	public boolean isSelezionato()
	{
		return selezionato;
	}
	public void setSelezionato(boolean selezionato)
	{
		this.selezionato = selezionato;
	}
	public String getFormato()
	{
		return formato;
	}
	public void setFormato(String formato)
	{
		this.formato = formato;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		TipoEtichetta retVal = new TipoEtichetta();
		retVal.setId(this.getId());
		retVal.setName(this.getName());
		retVal.setChiave(this.getChiave());
		retVal.setOperatore(this.getOperatore());
		retVal.setFkDvTema(this.getFkDvTema());
		retVal.setNameTema(this.getNameTema());
		retVal.setSelezionato(this.isSelezionato());
		retVal.setFormato(this.getFormato());
		return retVal;
	}
	
}
