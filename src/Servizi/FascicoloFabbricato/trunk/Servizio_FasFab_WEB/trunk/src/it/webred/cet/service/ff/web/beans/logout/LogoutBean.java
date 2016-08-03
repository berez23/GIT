package it.webred.cet.service.ff.web.beans.logout;

import it.webred.cet.service.ff.web.FFBaseBean;

public class LogoutBean extends FFBaseBean {

	public String doLogout() {
		try {
			super.getSession().invalidate();
        }
        catch(Exception e) {
        	logger.error(e.getMessage(),e);
        }
        
        return "ff.logout";
      }

}
