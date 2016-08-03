package it.webred.cet.service.gestprep.web.beans.menu;

import java.io.Serializable;

public class MenuBean implements Serializable {
	
	public String doAnagList() {
		return "gestprep.anagList";
	}

	public String doAnagCreate() {
		return "gestprep.anagCreate";
	}
	
	
	public String doVisuraList() {
		return "gestprep.visList";
	}
	
	public String doOperazioneList() {
		return "gestprep.operList";
	}

}
