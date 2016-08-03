package it.webred.ct.data.access.basic.common;

import java.util.List;

import it.webred.ct.data.access.basic.CTServiceBaseBean;
import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.data.access.basic.common.dao.CommonDAO;
import it.webred.ct.data.model.common.SitEnte;
import it.webred.ct.support.datarouter.CeTBaseObject;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

@Stateless
public class CommonServiceBean extends CTServiceBaseBean implements CommonService {
	
	@Autowired
	private CommonDAO commonDAO;
	
	public SitEnte getEnte(CeTBaseObject cet){
		return commonDAO.getEnte();
	}
	
	public  List<Object[]> executeNativeQuery(CommonDataIn dataIn) {
		return commonDAO.executeNativeQuery((String) dataIn.getObj());
	}
}
