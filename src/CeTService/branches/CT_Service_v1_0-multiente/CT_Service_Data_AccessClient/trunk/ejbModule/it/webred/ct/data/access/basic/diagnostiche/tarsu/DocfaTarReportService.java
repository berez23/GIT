package it.webred.ct.data.access.basic.diagnostiche.tarsu;

import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheService;
import it.webred.ct.data.access.basic.diagnostiche.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaIciTarsuDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.DiagnosticheIciServiceException;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.SitTIciOggettoDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.SoggettoIciDTO;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.dto.SitTTarOggettoDTO;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.dto.SoggettoTarDTO;
import it.webred.ct.data.model.diagnostiche.DocfaTarReport;
import it.webred.ct.data.model.diagnostiche.DocfaTarReportSogg;
import it.webred.ct.data.model.docfa.DocfaDatiMetrici;
import it.webred.ct.data.model.docfa.DocfaDichiaranti;
import it.webred.ct.data.model.docfa.DocfaInParteDueH;
import it.webred.ct.data.model.docfa.DocfaIntestati;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface DocfaTarReportService extends DiagnosticheService{

	public DocfaTarReport getDocfaTarReportById(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

	public List<Object> getSuggestionVie(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

	public List<Object> getSuggestionCognomi(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

	public List<Object> getSuggestionNomi(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

	public List<Object> getSuggestionCodFisc(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

	public List<DocfaTarReport> searchReport(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

	public List<DocfaTarReportSogg> getReportSogg(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

	public List<DocfaTarReportSogg> getReportSoggTitolari(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

	public Long searchReportCount(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

	public List<Object[]> countDocfaFornitura(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

	List<IndirizzoDTO> getIndirizziCatasto(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

	List<SitTTarOggettoDTO> getTarAnteDocfaBySoggettoUiu(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

	List<SitTTarOggettoDTO> getTarPostDocfaBySoggettoUiu(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;
	
	List<SitTTarOggettoDTO> getTarAnteDocfaBySoggettoUiuUnd(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

	List<SitTTarOggettoDTO> getTarPostDocfaBySoggettoUiuUnd(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;
	
	List<SitTTarOggettoDTO> getTarAnteDocfaBySoggettoCiv(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;
	
	List<SitTTarOggettoDTO> getTarPostDocfaBySoggettoCiv(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;
	
	List<SitTTarOggettoDTO> getTarAnteDocfaByFamiliariUiu(DiagnosticheDataIn dataIn)
	throws DiagnosticheTarServiceException;

	List<SitTTarOggettoDTO> getTarPostDocfaByFamiliariUiu(DiagnosticheDataIn dataIn)
		throws DiagnosticheTarServiceException;
	
	List<SitTTarOggettoDTO> getTarAnteDocfaByFamiliariUiuUnd(DiagnosticheDataIn dataIn)
		throws DiagnosticheTarServiceException;
	
	List<SitTTarOggettoDTO> getTarPostDocfaByFamiliariUiuUnd(DiagnosticheDataIn dataIn)
		throws DiagnosticheTarServiceException;
	
	List<SitTTarOggettoDTO> getTarAnteDocfaByFamiliariCiv(DiagnosticheDataIn dataIn)
		throws DiagnosticheTarServiceException;
	
	List<SitTTarOggettoDTO> getTarPostDocfaByFamiliariCiv(DiagnosticheDataIn dataIn)
		throws DiagnosticheTarServiceException;

	List<IndirizzoDTO> getIndirizziTarsu(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

	List<SoggettoTarDTO> getSoggettiTarsu(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

	List<SitTTarOggettoDTO> getTarDocfa(DiagnosticheDataIn dataIn)
			throws DiagnosticheTarServiceException;

}
