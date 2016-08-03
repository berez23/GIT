package it.webred.cet.service.ff.web.beans.menu;

import it.webred.cet.service.ff.web.FFBaseBean;
import it.webred.cet.service.ff.web.beans.filtro.FiltroDataModel;

import java.io.Serializable;

public class MenuBean extends FFBaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public String doInserisciRichiesta() {
		return "menu.richiesta.inserisci";
	}
	
	public String doFiltroRichiesta() {
		Object o = super.getBeanReference("filtroDataModel");
		if (o!=null)
		{
			FiltroDataModel f = (FiltroDataModel)o;
			f.controllaPermessi();
			//logger.debug("-->visualizza select operatori: " + f.isVisualizzaSelectOperatori());
			f.setShowTableRusult(false);
		}
		
		return "menu.richiesta.filtro";
	}

}
