package it.webred.diogene.visualizzatore.runtime.tag;

import it.webred.diogene.visualizzatore.runtime.GestoreNavigazione;
import it.webred.utils.ObjectEncoder;
import it.webred.utils.Replace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public abstract class BaseTag extends TagSupport
{
	public static final String nomePersistenza = "gestoreNavigazioneDiogeneStack";
	public static final String nomeParametroMappaKeyExt = "OggettoMappa";
	protected HashMap<String, String> getMapParameter()
	throws Exception
	{
		ServletRequest request = pageContext.getRequest();
		HashMap<String, String> returnmap = new HashMap<String, String>();

		
		Map m = request.getParameterMap();
        for(Iterator iterator = m.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            String chiave = (String)entry.getKey();
            if(chiave.equals("idDvClasse") || chiave.startsWith("ricerca[") || chiave.startsWith("operatore[") || chiave.startsWith("chiaveLista[")
            		|| chiave.equals("numberOfItemInPage") || chiave.equals("currentListPage")  || chiave.equals("totalRecordNumber") 
            		|| chiave.equals(nomePersistenza)  || chiave.startsWith("crossLinkParams") || chiave.equals("OggettoMappa")
            		|| chiave.equals("saltaLista") || chiave.equals(nomeParametroMappaKeyExt))
            {
                String valore = ((String[])entry.getValue())[0];
                returnmap.put(chiave, valore);
            }
        }
         
		
		if(pageContext.getRequest().getAttribute("forwardNavigazione") != null)
		{
			GestoreNavigazione nav = new GestoreNavigazione();
	        if(returnmap.get(nomePersistenza) != null)
	        {
	        	nav.setPreview((GestoreNavigazione)ObjectEncoder.decodeObject(returnmap.get(nomePersistenza)));	
	        }
	        returnmap.remove(nomePersistenza);// ridondante
	    	nav.setParams(returnmap);
	    	HashMap<String, String> returnmapForward =  (HashMap<String, String>)pageContext.getRequest().getAttribute("forwardNavigazione") ;
	    	returnmapForward.put(nomePersistenza,ObjectEncoder.encodeObject(nav));	
	    	return returnmapForward; 	
		}
		else if (pageContext.getRequest().getAttribute("backNavigazione") != null && returnmap.get(nomePersistenza) != null)
		{
			GestoreNavigazione nav = new GestoreNavigazione();
			nav = (GestoreNavigazione)ObjectEncoder.decodeObject(request.getParameter(nomePersistenza));
			HashMap<String, String> returnmapBack  = nav.getParams();
			nav.back();
			if(nav != null && nav.getParams() != null && nav.getParams().size()>0)
				returnmapBack.put(nomePersistenza,ObjectEncoder.encodeObject(nav));
			return returnmapBack;			
		}
		return returnmap;
	}
	
	protected void writeHidden(JspWriter out, HashMap<String, String> parametriRequest) throws IOException

	{
		for (Map.Entry<String, String> entry : parametriRequest.entrySet())
		{
			String chiave = entry.getKey();
			String valore = Replace.forHTMLTag(entry.getValue());
			out.println("<input type=\"hidden\" id=\"" + chiave + "\" name=\"" + chiave + "\" value=\"" + valore + "\" /> ");
		}

	}
	
	protected boolean permesso(String idDvClasse)
	{
		List idClassiPermesse = (List)pageContext.getSession().getAttribute("idClassiPermesse");
		if(idClassiPermesse == null)
			idClassiPermesse = new ArrayList<String>();		
		return idClassiPermesse.contains(idDvClasse);
	}

}
