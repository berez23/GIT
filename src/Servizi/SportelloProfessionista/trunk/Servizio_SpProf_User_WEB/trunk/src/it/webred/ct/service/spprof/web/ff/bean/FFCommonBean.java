package it.webred.ct.service.spprof.web.ff.bean;

import it.webred.ct.service.ff.data.access.common.FFCommonService;
import it.webred.ct.service.ff.data.model.FFTipoDoc;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

public class FFCommonBean extends SpProfBaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private FFCommonService commonService = (FFCommonService) getEjb("Servizio_FasFab", "Servizio_FasFab_EJB", "FFCommonServiceBean");
	
	
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
			logger.error(t);
		}
		
		return result;
	}

	
}
