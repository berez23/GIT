package it.webred.ct.diagnostics.service.data.access.impl;

import it.webred.ct.diagnostics.service.data.access.DIABaseBean;
import it.webred.ct.diagnostics.service.data.access.DiaDocfaService;
import it.webred.ct.diagnostics.service.data.dao.IDAODocfa;
import it.webred.ct.diagnostics.service.data.dto.DiaDocfaDTO;
import it.webred.ct.diagnostics.service.data.exception.DIAServiceException;
import it.webred.ct.diagnostics.service.data.model.DocfaNonResidenziale;
import it.webred.ct.diagnostics.service.data.model.DocfaReport;
import it.webred.ct.diagnostics.service.data.model.DocfaReportNoRes;
import it.webred.ct.diagnostics.service.data.model.DocfaResidenziale;
import it.webred.ct.diagnostics.service.data.model.zc.ZCErrata;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class DiaDettaglioServiceBean
 */
@Stateless
public class DiaDocfaServiceBean extends DIABaseBean implements DiaDocfaService {
	
	
	@Autowired
	private IDAODocfa diaDocfaDAO;
		
    /**
     * Default constructor. 
     */
    public DiaDocfaServiceBean() {
        // TODO Auto-generated constructor stub
    }

	public List<String> getCategorieDocfaNonResidenziale(DiaDocfaDTO dd) {
		try {
			
			return diaDocfaDAO.getCategorieDocfaNonResidenziale(dd);
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}


	public List<String> getCategorieDocfaResidenziale(DiaDocfaDTO dd) {
		try {
			
			return diaDocfaDAO.getCategorieDocfaResidenziale(dd);
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}


	public List<Date> getFornitureDocfaNonResidenziale(DiaDocfaDTO dd) {
		try {
			
			return diaDocfaDAO.getFornitureDocfaNonResidenziale(dd);
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}	}


	public List<Date> getFornitureDocfaResidenziale(DiaDocfaDTO dd) {
		try {
			
			return diaDocfaDAO.getFornitureDocfaResidenziale(dd);
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}


	public List<DocfaNonResidenziale> getDocfaNonResidenziale(DiaDocfaDTO dd) {
		try {
			
			return diaDocfaDAO.getDocfaNonResidenziale(dd);
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}

	
	public long getDocfaNonResidenzialeCount(DiaDocfaDTO dd) {
		try {
			
			return diaDocfaDAO.getDocfaNonResidenzialeCount(dd);
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}	
	}


	public List<DocfaResidenziale> getDocfaResidenziale(DiaDocfaDTO dd) {
		try {
			
			return diaDocfaDAO.getDocfaResidenziale(dd);
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}	
	}

	public long getDocfaResidenzialeCount(DiaDocfaDTO dd) {
		try {
			
			return diaDocfaDAO.getDocfaResidenzialeCount(dd);
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}	
	}

	public List<DocfaReport> getDocfaReport(DiaDocfaDTO dd) {
		try {
					
			return diaDocfaDAO.getDocfaReport(dd);
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}

	public List<DocfaReportNoRes> getDocfaReportNoRes(DiaDocfaDTO dd) {
		try {
			
			return diaDocfaDAO.getDocfaReportNoRes(dd);
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}

	

	public List<ZCErrata> getZCErrate(DiaDocfaDTO dd) {
		try {
			
			return diaDocfaDAO.getZCErrate(dd);
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}

	public List<ZCErrata> getZCErrateNoRes(DiaDocfaDTO dd) {
		try {
			
			return diaDocfaDAO.getZCErrateNoRes(dd);
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}


	

}
