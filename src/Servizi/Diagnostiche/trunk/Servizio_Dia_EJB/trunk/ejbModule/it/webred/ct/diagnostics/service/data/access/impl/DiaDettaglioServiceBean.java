package it.webred.ct.diagnostics.service.data.access.impl;

import java.util.List;

import it.webred.ct.diagnostics.service.data.SuperDia;
import it.webred.ct.diagnostics.service.data.access.DIABaseBean;
import it.webred.ct.diagnostics.service.data.access.DiaDettaglioService;
import it.webred.ct.diagnostics.service.data.dao.IDAODettaglioDiagnostica;
import it.webred.ct.diagnostics.service.data.dto.DiaDettaglioDTO;
import it.webred.ct.diagnostics.service.data.exception.DIAServiceException;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class DiaDettaglioServiceBean
 */
@Stateless
public class DiaDettaglioServiceBean extends DIABaseBean implements DiaDettaglioService {
	
	@Autowired
	private IDAODettaglioDiagnostica dettaglioDiagnosticaDAO;
	
    /**
     * Default constructor. 
     */
    public DiaDettaglioServiceBean() {
        // TODO Auto-generated constructor stub
    }

	

	public List<? extends SuperDia> getDettaglioDiagnostica(DiaDettaglioDTO dd) {
		try {
			
			return dettaglioDiagnosticaDAO.getDiagnosticaListaDettagli(dd.getStart(),dd.getMaxrows(), dd.getIdDiaTestata(), dd.getModelClassname());
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}



	public List<String[]> getDettaglioDiagnosticaArray(DiaDettaglioDTO dd) {
		try {
			
			return dettaglioDiagnosticaDAO.getDiagnosticaArrayDettagli(dd.getStart(),dd.getMaxrows(),dd.getIdDiaTestata(), dd.getModelClassname());
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}



	public Long getCount(DiaDettaglioDTO dd) {
		try {
			
			return dettaglioDiagnosticaDAO.getCount(dd.getIdDiaTestata(), dd.getModelClassname());
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}



	public List<? extends SuperDia> getDettaglioDiagnosticaCompleto(
			DiaDettaglioDTO dd) {
		try {
			
			return dettaglioDiagnosticaDAO.getDiagnosticaListaDettagli(dd.getIdDiaTestata(), dd.getModelClassname());
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}

}
