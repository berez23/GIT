package it.webred.ct.service.spprof.web.admin.beans.menu;

import it.webred.ct.service.spprof.web.admin.SpProfBaseBean;

import java.io.Serializable;

public class MenuBean extends SpProfBaseBean implements Serializable {
	
	
	public String doAnagList() {
		return "spprof.anagList";
	}

	public String doAnagCreate() {
		return "spprof.anagCreate";
	}
	
}
