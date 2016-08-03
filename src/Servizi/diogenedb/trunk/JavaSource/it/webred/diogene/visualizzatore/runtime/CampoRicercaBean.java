package it.webred.diogene.visualizzatore.runtime;

public class CampoRicercaBean
{

	private String	nomeAlias;
	private String	valoreCampo;
	private String	valoreOperatore;
	private String	javaType;			// serve per sql like

	public CampoRicercaBean(String nomeAlias, String valoreCampo, String valoreOperatore, String javaType)
	{

		this.nomeAlias = nomeAlias;
		this.valoreCampo = valoreCampo;
		this.valoreOperatore = valoreOperatore;
		this.javaType = javaType;
	}

	public String getNomeAlias()
	{
		return nomeAlias;
	}

	public void setNomeAlias(String nomeAlias)
	{
		this.nomeAlias = nomeAlias;
	}

	public boolean isStringType()
	{
		return javaType != null && javaType.equals("java.lang.String") ? true : false;
	}
	public boolean isDateType()
	{
		return javaType != null && (javaType.equals("java.util.Date") || javaType.equals("java.sql.Date")) ? true : false;
	}
	public String getJavaType()
	{
		return javaType;
	}

	public void setJavaType(String javaType)
	{
		this.javaType = javaType;
	}

	public String getValoreCampo()
	{
		return valoreCampo;
	}

	public void setValoreCampo(String valoreCampo)
	{
		this.valoreCampo = valoreCampo;
	}

	public String getValoreOperatore()
	{
		return valoreOperatore;
	}

	public void setValoreOperatore(String valoreOperatore)
	{
		this.valoreOperatore = valoreOperatore;
	}

}
