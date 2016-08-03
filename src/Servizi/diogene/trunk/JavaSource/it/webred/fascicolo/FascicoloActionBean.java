package it.webred.fascicolo;


/**
 * @author dan
 *
 */
public class FascicoloActionBean {
	private String id = null;
	private String descrizione =  null;
	private String executor = null;
	private String action = null;
	private String jsp = null;
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getDescrizione()
	{
		return descrizione;
	}
	public void setDescrizione(String descrizione)
	{
		this.descrizione = descrizione;
	}
	public String getExecutor()
	{
		return executor;
	}
	public void setExecutor(String executor)
	{
		this.executor = executor;
	}
	public String getAction()
	{
		return action;
	}
	public void setAction(String action)
	{
		this.action = action;
	}
	public FascicoloActionBean(String id, String descrizione, String executor, String action, String jsp)
	{
		super();
		this.id = id;
		this.descrizione = descrizione;
		this.executor = executor;
		this.action = action;
		this.jsp = jsp;
	}
	public String getJsp()
	{
		return jsp;
	}
	public void setJsp(String jsp)
	{
		this.jsp = jsp;
	}
	
	
	
}
