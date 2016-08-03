package it.webred.cet.service.ff.web.beans.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import it.webred.cet.service.ff.web.FFBaseBean;
import it.webred.ct.service.ff.data.access.common.FFCommonService;
import it.webred.ct.service.ff.data.model.FFTipoDoc;
import it.webred.ct.support.datarouter.CeTBaseObject;

public class FFCommonBean extends FFBaseBean implements Serializable {

	private FFCommonService commonService;
	
	public FFCommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(FFCommonService commonService) {
		this.commonService = commonService;
	}	
	
	public List<SelectItem> getTipoDocItems() {
		List<SelectItem> result = new ArrayList<SelectItem>();
		
		try {
			CeTBaseObject obj = new CeTBaseObject();
			obj.setEnteId(this.getEnte());
			List<FFTipoDoc> tipoDocList = getCommonService().getTipoDoc(obj);
			for (FFTipoDoc tipoDoc : tipoDocList) {
				SelectItem item = new SelectItem();
				item.setValue(tipoDoc.getCodTipDoc());
				item.setLabel(tipoDoc.getDesTipDoc());
				result.add(item);
			}
		}
		catch(Throwable t) {
			logger.error(t.getMessage(),t);
		}
		
		return result;
	}
}
