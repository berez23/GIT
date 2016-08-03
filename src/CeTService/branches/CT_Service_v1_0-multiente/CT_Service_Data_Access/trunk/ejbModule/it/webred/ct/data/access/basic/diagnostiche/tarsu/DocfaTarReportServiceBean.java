package it.webred.ct.data.access.basic.diagnostiche.tarsu;

import it.webred.ct.data.access.basic.CTServiceBaseBean;
import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheServiceException;
import it.webred.ct.data.access.basic.diagnostiche.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaIciTarsuDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.DiagnosticheIciServiceException;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.SitTIciOggettoDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.SoggettoIciDTO;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.dao.DocfaTarReportDAO;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.dto.SitTTarOggettoDTO;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.dto.SoggettoTarDTO;
import it.webred.ct.data.model.diagnostiche.DocfaAnomalie;
import it.webred.ct.data.model.diagnostiche.DocfaTarReport;
import it.webred.ct.data.model.diagnostiche.DocfaTarReportSogg;
import it.webred.ct.data.model.docfa.DocfaDatiMetrici;
import it.webred.ct.data.model.docfa.DocfaDichiaranti;
import it.webred.ct.data.model.docfa.DocfaInParteDueH;
import it.webred.ct.data.model.docfa.DocfaIntestati;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

@Stateless
public class DocfaTarReportServiceBean extends CTServiceBaseBean implements
		DocfaTarReportService {

	@Autowired
	private DocfaTarReportDAO docfaTarReportDAO;

	@Override
	public DocfaTarReport getDocfaTarReportById(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException {
		String id = (String) dataIn.getObj();
		return docfaTarReportDAO.getDocfaTarReportById(id);
	}

	@Override
	public List<Object> getSuggestionVie(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException {
		String via = (String) dataIn.getObj();
		return docfaTarReportDAO.getSuggestionVie(via);
	}

	@Override
	public List<Object> getSuggestionCognomi(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException {
		String cognome = (String) dataIn.getObj();
		return docfaTarReportDAO.getSuggestionCognomi(cognome);
	}

	@Override
	public List<Object> getSuggestionNomi(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException {
		String nome = (String) dataIn.getObj();
		return docfaTarReportDAO.getSuggestionNomi(nome);
	}

	@Override
	public List<Object> getSuggestionCodFisc(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException {
		String codFisc = (String) dataIn.getObj();
		return docfaTarReportDAO.getSuggestionCodFisc(codFisc);
	}

	@Override
	public List<DocfaTarReport> searchReport(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException {
		RicercaDocfaReportDTO rs = dataIn.getRicerca();
		return docfaTarReportDAO.searchReport(rs);
	}

	@Override
	public List<DocfaTarReportSogg> getReportSogg(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException {
		String id = (String) dataIn.getObj();
		return docfaTarReportDAO.getReportSogg(id);
	}

	@Override
	public List<DocfaTarReportSogg> getReportSoggTitolari(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException {
		String id = (String) dataIn.getObj();
		return docfaTarReportDAO.getReportSoggTitolari(id);
	}

	@Override
	public Long searchReportCount(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException {
		RicercaDocfaReportDTO rs = dataIn.getRicerca();
		return docfaTarReportDAO.searchReportCount(rs);
	}

	@Override
	public List<Object[]> countDocfaFornitura(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException {
		return docfaTarReportDAO.countDocfaFornitura();
	}


	@Override
	public List<IndirizzoDTO> getIndirizziCatasto(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException {
		RicercaDocfaReportDTO rs = dataIn.getRicerca();
		return docfaTarReportDAO.getIndirizziCatasto(rs);
	}
	
	@Override
	public List<IndirizzoDTO> getIndirizziTarsu(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException {
		RicercaDocfaReportDTO rs = dataIn.getRicerca();
		return docfaTarReportDAO.getIndirizziTar(rs);
	}
	
	
	@Override
	public List<SitTTarOggettoDTO> getTarDocfa(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException {
		RicercaDocfaReportDTO dto = dataIn.getRicerca();
		return docfaTarReportDAO.getTarDocfa(dto);
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarAnteDocfaBySoggettoUiu(
			DiagnosticheDataIn dataIn) throws DiagnosticheTarServiceException {
		RicercaIciTarsuDTO dto = dataIn.getRicercaIciTarsuDto();
		return docfaTarReportDAO.getTarAnteDocfaBySoggettoUiu(dto);
	}

	@Override
	public List<SitTTarOggettoDTO> getTarPostDocfaBySoggettoUiu(
			DiagnosticheDataIn dataIn) throws DiagnosticheTarServiceException {
		RicercaIciTarsuDTO dto = dataIn.getRicercaIciTarsuDto();
		return docfaTarReportDAO.getTarPostDocfaBySoggettoUiu(dto);
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarAnteDocfaBySoggettoUiuUnd(
			DiagnosticheDataIn dataIn) throws DiagnosticheTarServiceException {
		RicercaIciTarsuDTO dto = dataIn.getRicercaIciTarsuDto();
		return docfaTarReportDAO.getTarAnteDocfaBySoggettoUiuUnd(dto);
	}

	@Override
	public List<SitTTarOggettoDTO> getTarPostDocfaBySoggettoUiuUnd(
			DiagnosticheDataIn dataIn) throws DiagnosticheTarServiceException {
		RicercaIciTarsuDTO dto = dataIn.getRicercaIciTarsuDto();
		return docfaTarReportDAO.getTarPostDocfaBySoggettoUiuUnd(dto);
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarAnteDocfaBySoggettoCiv(
			DiagnosticheDataIn dataIn) throws DiagnosticheTarServiceException {
		RicercaIciTarsuDTO dto = dataIn.getRicercaIciTarsuDto();
		return docfaTarReportDAO.getTarAnteDocfaBySoggettoCiv(dto);
	}

	@Override
	public List<SitTTarOggettoDTO> getTarPostDocfaBySoggettoCiv(
			DiagnosticheDataIn dataIn) throws DiagnosticheTarServiceException {
		RicercaIciTarsuDTO dto = dataIn.getRicercaIciTarsuDto();
		return docfaTarReportDAO.getTarPostDocfaBySoggettoCiv(dto);
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarAnteDocfaByFamiliariUiu(
			DiagnosticheDataIn dataIn) throws DiagnosticheTarServiceException {
		RicercaIciTarsuDTO dto = dataIn.getRicercaIciTarsuDto();
		return docfaTarReportDAO.getTarAnteDocfaByFamiliariUiu(dto);
	}

	@Override
	public List<SitTTarOggettoDTO> getTarPostDocfaByFamiliariUiu(
			DiagnosticheDataIn dataIn) throws DiagnosticheTarServiceException {
		RicercaIciTarsuDTO dto = dataIn.getRicercaIciTarsuDto();
		return docfaTarReportDAO.getTarPostDocfaByFamiliariUiu(dto);
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarAnteDocfaByFamiliariUiuUnd(
			DiagnosticheDataIn dataIn) throws DiagnosticheTarServiceException {
		RicercaIciTarsuDTO dto = dataIn.getRicercaIciTarsuDto();
		return docfaTarReportDAO.getTarAnteDocfaByFamiliariUiuUnd(dto);
	}

	@Override
	public List<SitTTarOggettoDTO> getTarPostDocfaByFamiliariUiuUnd(
			DiagnosticheDataIn dataIn) throws DiagnosticheTarServiceException {
		RicercaIciTarsuDTO dto = dataIn.getRicercaIciTarsuDto();
		return docfaTarReportDAO.getTarPostDocfaByFamiliariUiuUnd(dto);
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarAnteDocfaByFamiliariCiv(
			DiagnosticheDataIn dataIn) throws DiagnosticheTarServiceException {
		RicercaIciTarsuDTO dto = dataIn.getRicercaIciTarsuDto();
		return docfaTarReportDAO.getTarAnteDocfaByFamiliariCiv(dto);
	}

	@Override
	public List<SitTTarOggettoDTO> getTarPostDocfaByFamiliariCiv(
			DiagnosticheDataIn dataIn) throws DiagnosticheTarServiceException {
		RicercaIciTarsuDTO dto = dataIn.getRicercaIciTarsuDto();
		return docfaTarReportDAO.getTarPostDocfaByFamiliariCiv(dto);
	}
	
	@Override
	public List<SoggettoTarDTO> getSoggettiTarsu(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException {
		String idExt = (String) dataIn.getObj();
		return docfaTarReportDAO.getSoggettiTar(idExt);
	}
	

	@Override
	public List<DocfaAnomalie> getListaAnomalie(DiagnosticheDataIn dataIn)
			throws DiagnosticheServiceException {
		return docfaTarReportDAO.getListaAnomalie("T");
	}
}
