package it.webred.gitland.web.bean.util;

import it.webred.cet.permission.CeTUser;
import it.webred.gitland.web.bean.GitLandBaseBean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class UserBean extends GitLandBaseBean{
	
	private String username;
	
	public void doLogout(){
		
		getRequest().getSession().invalidate();
		getRequest().getSession();
		try {
			getResponse().sendRedirect(getRequest().getContextPath() + "/");
		} catch (IOException e) {
			logger.error("Eccezione: "+e.getMessage(),e);
		}
		
	}//-------------------------------------------------------------------------

	public boolean isLogged() {
		
		String retry = getRequest().getParameter("retry");
		if("true".equals(retry)){
			Map params = getRequest().getParameterMap();
			params.remove("retry");
			addError("login.denied", "Ricorda che la password è sensibile alle maiuscole e minuscole");
		}
		
		if (getRequest().getUserPrincipal()==null || getRequest().getUserPrincipal().getName()==null)
			return false;
		return true;
	}//-------------------------------------------------------------------------
	
	public String getUsername() {
		
		if (getRequest().getUserPrincipal()==null || getRequest().getUserPrincipal().getName()==null)
			return "";
		
		username = getRequest().getUserPrincipal().getName();
		return username;
	}//-------------------------------------------------------------------------

	public void setUsername(String username) {
		this.username = username;
	}//-------------------------------------------------------------------------

	public String getEnte() {
		HttpSession session = this.getSession();
		CeTUser user = (CeTUser) session.getAttribute("user");
		if (user != null) {
			return user.getCurrentEnte();
		}
		return null;
	}//-------------------------------------------------------------------------
	
	public String getEnteDescrizione() {
		HttpSession session = this.getSession();
		CeTUser user = (CeTUser) session.getAttribute("user");
		if (user != null) {
			String sql = "select nome from comuni where belfiore = '"+user.getCurrentEnte()+"' ";
			List lst = gitLandService.getList(sql, false);
			return (String)lst.get(0);
		}
		return "";
	}//-------------------------------------------------------------------------

	
}
