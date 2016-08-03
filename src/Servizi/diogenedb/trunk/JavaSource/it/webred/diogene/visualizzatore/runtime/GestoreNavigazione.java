package it.webred.diogene.visualizzatore.runtime;

import java.io.Serializable;
import java.util.HashMap;

public class GestoreNavigazione implements Serializable
{
	private static final long	serialVersionUID	= 5875828042107919352L;
	private HashMap<String,String> params = new HashMap<String,String>();
	private GestoreNavigazione preview = null;
	public HashMap<String,String> getParams()
	{
		return params;
	}
	public void setParams(HashMap<String,String> params)
	{
		this.params = params;
	}
	public GestoreNavigazione getPreview()
	{
		return preview;
	}
	public void back()
	{
		if(this.preview != null)
		{
			this.params = this.preview.getParams();
			this.preview = this.preview.preview;
		}
		else
		{
			this.preview = null;
			this.params = null;
		}
	}

	
	public void setPreview(GestoreNavigazione preview)
	{
		this.preview = preview;
	}	
	
}
