package it.webred.diogene.visualizzatore.runtime;

/**
 * @author Dan Petre
 * 
 */
public abstract class Page
{

	private String	title;
	private Long	idDvClasse;

	public Long getIdDvClasse()
	{
		return idDvClasse;
	}

	public void setIdDvClasse(Long idDvClasse)
	{
		this.idDvClasse = idDvClasse;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

}
