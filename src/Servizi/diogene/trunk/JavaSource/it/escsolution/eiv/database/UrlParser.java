/*
 * Created on 9-giu-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.eiv.database;

import java.util.HashMap;

/**
 * @author silviat
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UrlParser {
String url;
String baseUrl;
HashMap param;


	/**
	 * 
	 */
	public UrlParser(String u) {
		setUrl(u);
		param = new HashMap();
		String []s = url.split("\\?");
		if(s.length>0)
		{
			baseUrl=s[0];		
		}
		if(s.length>1)
		{
			String[] params=s[1].split("\\&");
			for(int i=0;i<params.length;i++)
			{
				String[] sParam = params[i].split("\\=");
				if(sParam.length==2)
				{
					param.put(sParam[0],sParam[1]);
				}
			}
		}
		// TODO Auto-generated constructor stub
	}

/**
 * @return
 */
public String getBaseUrl() {
	return baseUrl;
}

/**
 * @return
 */
public HashMap getParam() {
	return param;
}

/**
 * @param string
 */
public void setUrl(String string) {
	url = string;
}


}
