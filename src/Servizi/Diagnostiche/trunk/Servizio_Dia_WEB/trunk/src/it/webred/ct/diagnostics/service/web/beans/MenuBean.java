package it.webred.ct.diagnostics.service.web.beans;

import java.io.Serializable;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.diagnostics.service.web.DiaBaseBean;
import it.webred.ct.diagnostics.service.web.user.UserBean;

public class MenuBean extends DiaBaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String doVisualizzaDia()
	{								
		return "menu.visualizzaDia";
	}
	
	public String doEseguiDia()
	{			
		return "menu.eseguiDia";
	}
	
	public String doSottoscrittori() {
		
		DiaSottoscrittoriBean dBean = 
			(DiaSottoscrittoriBean)getBeanReference("diaSottoscrittoriBean");
		
		//impostazioni liste del bean prima del acricamento della pagina
		dBean.doCaricaListaUtentiEnte();
		
		//dBean.doCaricaListaComandiDiagEnte();		
		
		return "menu.diagnostiche.sottoscrittori";
	}
	
	public String doHome() {
		return "menu.home";
	}
	
	public void doLogout() {
		
		Object o = super.getBeanReference("userBean");
		if (o!=null)
		{
			UserBean ub = (UserBean)o;
			ub.doLogout();
		}					
	}
}
