package net.skillbill.webred.taglib;


import java.util.StringTokenizer;

//import it.webred.permessi.GestionePermessi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class SecurityLockTag extends TagSupport {

	private String[] roles = null;

	public void setRole(String role) {
		this.roles = role.split(",");
	}

	public int doStartTag() throws JspException {
		// System.out.println("role="+this.role);
		boolean hasRole = false;
		int i = 0;
		HttpServletRequest  request = ((HttpServletRequest) pageContext.getRequest());
		if(request.getUserPrincipal() == null)
			return SKIP_BODY;
		while (i < roles.length){			
			//hasRole = ((HttpServletRequest) pageContext.getRequest()).isUserInRole(roles[i]);
			//hasRole = GestionePermessi.autorizzato(request.getUserPrincipal(),request.getContextPath().substring(1),"Virgilio",roles[i]);
			//per ora inibito - verificare se e come gestire - Filippo Mazzini 04.09.12
			hasRole = true;
			///////////////////////////////////////////////////////////////////////////
			if(hasRole) break;
			i++;
		}
		if (hasRole) {
			return EVAL_BODY_INCLUDE;
		} else {
			return SKIP_BODY;
		}
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

}
