package it.webred.ct.service.cnc.data.access;

import it.webred.ct.service.cnc.data.access.dao.CNCDAO;
import it.webred.ct.data.access.basic.cnc.CNCDTO;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class CNCCommonServiceBean
 */
@Stateless
public class CNCCommonServiceBean implements CNCCommonService {

	@Autowired
	private CNCDAO cncDAO;

	@Override
	public String getAmbitoDescr(CNCDTO dto) {
		return cncDAO.getAmbitoDescr(dto.getCodAmbito());
	}

	@Override
	public String getCodiceEntrataDescr(CNCDTO dto) {
		return cncDAO.getCodiceEntrataDescr(dto.getCodEntrata());
	}
} 
