package it.webred.ct.service.comma336.web.bean.util;

import it.webred.ct.service.comma336.web.Comma336BaseBean;
import it.webred.ct.service.comma336.web.bean.pagination.DataProviderImpl;

import java.io.Serializable;

import javax.annotation.PostConstruct;

public class MenuBean extends Comma336BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public String doRicercaFabbMaiDichiarati() {
		Object o = super.getBeanReference("dataProviderImpl");
		if (o!=null)
		{
			DataProviderImpl d = (DataProviderImpl)o;
			d.resetDataFabbrMaiDic();
			d.setVisibleFlagsInList(false);
		}
		return "c336.search.fabbrMaiDichiarati";
	}
	public String doRicercaFabbExRurali() {
		Object o = super.getBeanReference("dataProviderImpl");
		if (o!=null)
		{
			DataProviderImpl d = (DataProviderImpl)o;
			d.resetDataFabbrExRurali();
			d.setVisibleFlagsInList(false);
		}
		return "c336.search.fabbrExRurali";
	}
	public String doRicercaUiuDaRiclassificare() {
		Object o = super.getBeanReference("dataProviderImpl");
		if (o!=null)
		{
			DataProviderImpl d = (DataProviderImpl)o;
			d.resetData();
			d.setVisibleFlagsInList(true);
		}
		return "c336.search";
	}
	
	
}
