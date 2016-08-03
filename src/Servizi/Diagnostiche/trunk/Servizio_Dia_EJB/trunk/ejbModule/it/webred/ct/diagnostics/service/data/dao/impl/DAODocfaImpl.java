package it.webred.ct.diagnostics.service.data.dao.impl;

import it.webred.ct.diagnostics.service.data.dao.DIABaseDAO;
import it.webred.ct.diagnostics.service.data.dao.IDAODocfa;
import it.webred.ct.diagnostics.service.data.dto.DiaDocfaDTO;
import it.webred.ct.diagnostics.service.data.exception.DIAServiceException;
import it.webred.ct.diagnostics.service.data.model.DocfaNonResidenziale;
import it.webred.ct.diagnostics.service.data.model.DocfaReport;
import it.webred.ct.diagnostics.service.data.model.DocfaReportNoRes;
import it.webred.ct.diagnostics.service.data.model.DocfaResidenziale;
import it.webred.ct.diagnostics.service.data.model.zc.ZCErrata;
import it.webred.ct.diagnostics.service.data.util.DiaUtility;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

public class DAODocfaImpl extends DIABaseDAO implements IDAODocfa {
	
	public List<String> getCategorieDocfaNonResidenziale(DiaDocfaDTO dd)
			throws DIAServiceException {
		try {
			logger.debug("[DAODocfaImpl] - getCategorieDocfaNonResidenziale ");
			Query q = manager.createNamedQuery("DIA.getCategorieDocfaNonResidenziale");
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public List<String> getCategorieDocfaResidenziale(DiaDocfaDTO dd)
			throws DIAServiceException {
		try {
			logger.debug("[DAODocfaImpl] - getCategorieDocfaResidenziale ");
			Query q = manager.createNamedQuery("DIA.getCategorieDocfaResidenziale");
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public List<Date> getFornitureDocfaNonResidenziale(DiaDocfaDTO dd)
			throws DIAServiceException {
		try {
			logger.debug("[DAODocfaImpl] - getFornitureDocfaNonResidenziale ");
			Query q = manager.createNamedQuery("DIA.getFornitureDocfaNonResidenziale");
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public List<Date> getFornitureDocfaResidenziale(DiaDocfaDTO dd)
			throws DIAServiceException {
		try {
			logger.debug("[DAODocfaImpl] - getFornitureDocfaResidenziale ");
			Query q = manager.createNamedQuery("DIA.getFornitureDocfaResidenziale");
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}


	public List<DocfaNonResidenziale> getDocfaNonResidenziale(DiaDocfaDTO dd)
			throws DIAServiceException {
		try {
			logger.debug("[DAODocfaImpl] - getDocfaNonResidenziale ");
						
			Query q = manager.createNamedQuery("DIA.getDocfaNonResidenziale");
			q.setFirstResult(dd.getStart());
			q.setMaxResults(dd.getMaxrows());
			q.setParameter("fornituraDa", dd.getFornituraDa());
			q.setParameter("fornituraA", dd.getFornituraA());			
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public long getDocfaNonResidenzialeCount(DiaDocfaDTO dd)
			throws DIAServiceException {
		try {
			logger.debug("[DAODocfaImpl] - getDocfaNonResidenzialeCount ");
						
			Query q = manager.createNamedQuery("DIA.getDocfaNonResidenzialeCount");			
			q.setParameter("fornituraDa", dd.getFornituraDa());
			q.setParameter("fornituraA", dd.getFornituraA());			
			return (Long)q.getSingleResult();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public List<DocfaResidenziale> getDocfaResidenziale(DiaDocfaDTO dd)
			throws DIAServiceException {
		try {
			logger.debug("[DAODocfaImpl] - getDocfaResidenziale ");
						
			Query q = manager.createNamedQuery("DIA.getDocfaResidenziale");
			q.setFirstResult(dd.getStart());
			q.setMaxResults(dd.getMaxrows());
			q.setParameter("fornituraDa", dd.getFornituraDa());
			q.setParameter("fornituraA", dd.getFornituraA());			
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public long getDocfaResidenzialeCount(DiaDocfaDTO dd)
			throws DIAServiceException {
		try {
			logger.debug("[DAODocfaImpl] - getDocfaResidenzialeCount ");
						
			Query q = manager.createNamedQuery("DIA.getDocfaResidenzialeCount");			
			q.setParameter("fornituraDa", dd.getFornituraDa());
			q.setParameter("fornituraA", dd.getFornituraA());			
			return (Long)q.getSingleResult();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}
		

	public List<ZCErrata> getZCErrate(DiaDocfaDTO dd) throws DIAServiceException {
		Query q = null;
		
		try {
			logger.debug("[DAODocfaImpl] - getZCErrate ");
			if(dd.getCategoria() != null && !dd.getCategoria().equals("")) {
				q = manager.createNamedQuery("DIA.getZCErrateWithCategoria");
				q.setParameter("categoria", dd.getCategoria());
			}
			else {
				q = manager.createNamedQuery("DIA.getZCErrate");
			}
			
			String da = DiaUtility.getFormattedData("dd/MM/yyyy", dd.getFornituraDa());
			String a = DiaUtility.getFormattedData("dd/MM/yyyy", dd.getFornituraA());
			
			q.setParameter("dataDa", da);
			q.setParameter("dataA", a);
			
			List<ZCErrata> zcErrate = q.getResultList();
			
			return zcErrate;
			
		}catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}


	public List<DocfaReport> getDocfaReport(DiaDocfaDTO dd) throws DIAServiceException {
		Query q = null;
		
		try {
			logger.debug("[DAODocfaImpl] - getDocfaReport ");
			if(dd.getCategoria() != null && !dd.getCategoria().equals("")) {
				q = manager.createNamedQuery("DIA.getDocfaReportWithCategoria");
				q.setParameter("categoria", dd.getCategoria());
			}
			else {
				q = manager.createNamedQuery("DIA.getDocfaReport");
			}
			
			q.setParameter("dataDa", dd.getFornituraDa());
			q.setParameter("dataA", dd.getFornituraA());
			
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public List<DocfaReportNoRes> getDocfaReportNoRes(DiaDocfaDTO dd) throws DIAServiceException {
		Query q = null;
		
		try {
			logger.debug("[DAODocfaImpl] - getDocfaReportNoRes ");
			if(dd.getCategoria() != null && !dd.getCategoria().equals("")) {
				q = manager.createNamedQuery("DIA.getDocfaReportNoResWithCategoria");
				q.setParameter("categoria", dd.getCategoria());
			}
			else {
				q = manager.createNamedQuery("DIA.getDocfaReportNoRes");
			}
			
			q.setParameter("dataDa", dd.getFornituraDa());
			q.setParameter("dataA", dd.getFornituraA());
			
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public List<ZCErrata> getZCErrateNoRes(DiaDocfaDTO dd) throws DIAServiceException {
		Query q = null;
		
		try {
			logger.debug("[DAODocfaImpl] - getZCErrateNoRes ");
			if(dd.getCategoria() != null && !dd.getCategoria().equals("")) {
				q = manager.createNamedQuery("DIA.getZCErrateNoresWithCategoria");
				q.setParameter("categoria", dd.getCategoria());
			}
			else {
				q = manager.createNamedQuery("DIA.getZCErrateNores");
			}
			
			String da = DiaUtility.getFormattedData("dd/MM/yyyy", dd.getFornituraDa());
			String a = DiaUtility.getFormattedData("dd/MM/yyyy", dd.getFornituraA());

			q.setParameter("dataDa", da);
			q.setParameter("dataA", a);
			
			List<ZCErrata> zcErrate = q.getResultList();
			
			return zcErrate;
			
		}catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

}

