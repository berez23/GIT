package it.webred.ct.diagnostics.service.data.access.impl;

import java.util.List;

import it.webred.ct.diagnostics.service.data.access.DIABaseBean;
import it.webred.ct.diagnostics.service.data.access.DiagnosticheService;
import it.webred.ct.diagnostics.service.data.dao.IDAOListaDiagnostiche;
import it.webred.ct.diagnostics.service.data.dto.DiaCatalogoDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaDettaglioDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaLogAccessoDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaTestataDTO;
import it.webred.ct.diagnostics.service.data.exception.DIAServiceException;

import it.webred.ct.diagnostics.service.data.model.DiaLogAccesso;
import it.webred.ct.diagnostics.service.data.model.DiaTestata;
import it.webred.ct.diagnostics.service.data.model.controller.DiaCatalogo;
import it.webred.ct.support.datarouter.CeTBaseObject;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class DiagnosticheServiceBean
 */
@Stateless
public class DiagnosticheServiceBean extends DIABaseBean implements		DiagnosticheService {

	@Autowired
	private IDAOListaDiagnostiche diagnosticheDAO;

	/**
	 * Default constructor.
	 */
	public DiagnosticheServiceBean() {
		// TODO Auto-generated constructor stub
	}

	public List<DiaTestata> getDiaTestataByRange(DiaTestataDTO dt)
			throws DIAServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public DiaCatalogo getDiagnostica(DiaCatalogoDTO dc) throws DIAServiceException {

		try {
			logger.info("[DiagnosticheServiceBean.getDiagnostica] - Id Diagnostica: "
							+ dc.getIdDiagnostica());
			return diagnosticheDAO.getDiagnostica(dc.getIdDiagnostica());
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public List<DiaCatalogo> getDiagnostiche(CeTBaseObject o) throws DIAServiceException {

		try {
			logger.info("[DiagnosticheServiceBean.getDiagnostiche] - START");
			return diagnosticheDAO.getDiagnostiche();
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public List<DiaCatalogo> getDiagnosticheByCodCmdGrp(DiaCatalogoDTO dc)
			throws DIAServiceException {
		try {
			logger.info("[DiagnosticheServiceBean.getDiagnosticheByCodCmdGrp] - Cod Command Group : "
							+ dc.getCodCommand());
			return diagnosticheDAO.getDiagnosticheByCodCmdGrp(dc.getCodCommand());
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}
	
	/**
	 * Il metodo ritorna la lista delle esecuzioni relativa all'idcatalogoDia passato come parametro
	 * la lista è ordinata per data esecuzione desc.
	 * @param idCatalogoDia
	 * @return
	 * @throws DIAServiceException
	 */
	public List<DiaTestata> getDiaEsecuzioneByIdDia(DiaTestataDTO dt)
			throws DIAServiceException {
		try {
			logger.info("[DiagnosticheServiceBean.getDiaEsecuzioneByIdDia] - IdcatalogoDia: "	+ dt.getObjTestata().getIdCatalogoDia());
			return diagnosticheDAO.getDiaEsecuzioneByIdDia(dt.getStart(),dt.getNumberRecord(),dt.getObjTestata().getIdCatalogoDia());
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public List<DiaCatalogo> getDiagnosticheByCodCommand(DiaCatalogoDTO dc)
			throws DIAServiceException {
		try {
			logger.info("[DiagnosticheServiceBean.getDiagnosticheByCodCommand] - Cod Command Group : "
							+ dc.getCodCommand());
			return diagnosticheDAO.getDiagnosticheByCodCommand(dc.getCodCommand());
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public List<DiaLogAccesso> getLogAccessiByDiaTestata(DiaDettaglioDTO dd)
			throws DIAServiceException {
		try {
			logger.info("[DiagnosticheServiceBean.getLogAccessiByDiaTestata] - IdDiaTestata : "
							+ dd.getIdDiaTestata());
			return diagnosticheDAO.getLogAccessiByDiaTestata(dd.getStart(),dd.getMaxrows(),dd.getIdDiaTestata());
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public void insertLogAccesso(DiaLogAccessoDTO logAccesso)
			throws DIAServiceException {
		try {
			logger.info("[DiagnosticheServiceBean.insertLogAccesso] "
							+ logAccesso.toString());
			diagnosticheDAO.insertLogAccesso(logAccesso);
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
		
	}

	public Long getCountLog(DiaDettaglioDTO dd) throws DIAServiceException {
		try {
			logger.info("[DiagnosticheServiceBean.getCountLog] - IdDiaTestata : "
							+ dd.getIdDiaTestata());
			return diagnosticheDAO.getCountLogByDiaTestata(dd.getIdDiaTestata());
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public List<DiaCatalogo> getDiagnostiche(DiaCatalogoDTO dc)	throws DIAServiceException {
		try {
			logger.info("[DiagnosticheServiceBean.getDiagnostiche]");
			return diagnosticheDAO.getDiagnostiche(dc.getStart(),dc.getMaxrows(),dc.getEnteId(),
									dc.getFonti(),dc.getTipoComando());
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public Long getDiagnosticheCount(DiaCatalogoDTO dc) throws DIAServiceException {
		try {
			logger.info("[DiagnosticheServiceBean.getDiagnostiche]");
			return diagnosticheDAO.getDiagnosticheCount(dc.getStart(),dc.getMaxrows(),dc.getEnteId(),
									dc.getFonti(),dc.getTipoComando());
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}
	
	public List<DiaCatalogo> getAllDiagnosticheFonte(DiaCatalogoDTO dc) throws DIAServiceException {
		try {
			logger.info("[DiagnosticheServiceBean.getAllDiagnosticheFonte]");
			return diagnosticheDAO.getAllDiagnosticheFonte(dc.getEnteId(), dc.getFonti().get(0));
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public DiaTestata getDiaTestata(DiaTestataDTO dt) throws DIAServiceException {
		
		try {
			logger.info("[DiagnosticheServiceBean.getDiaTestata]");
			return diagnosticheDAO.getDiaTestata(dt.getObjTestata().getId());
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	/**
	 * Il metodo ritorna l'ultima esecuzione per l'idcatalogia passato
	 */
	public DiaTestata getLastEsecuzioneByIdDia(DiaTestataDTO dt)
			throws DIAServiceException {
		try {
			logger.info("[DiagnosticheServiceBean.getLastDiaEsecuzioneByIdDia] - IdcatalogoDia: "	+ dt.getObjTestata().getIdCatalogoDia());
			return diagnosticheDAO.getLastEsecuzioneByIdDia(dt.getObjTestata().getIdCatalogoDia());
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}
	
	public Long getStoricoEsecuzioniCount(DiaTestataDTO dt) throws DIAServiceException {
		try {
			logger.info("[DiagnosticheServiceBean.getStoricoEsecuzioniCount] - IdDiaTestata : "
							+ dt.getObjTestata().getIdCatalogoDia());
			return diagnosticheDAO.getStoricoEsecuzioniCount(dt.getObjTestata().getIdCatalogoDia());
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}
	
}
