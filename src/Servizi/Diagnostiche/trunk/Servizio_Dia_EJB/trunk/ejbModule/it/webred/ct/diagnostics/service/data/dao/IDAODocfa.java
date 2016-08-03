package it.webred.ct.diagnostics.service.data.dao;

import it.webred.ct.diagnostics.service.data.dto.DiaDocfaDTO;
import it.webred.ct.diagnostics.service.data.exception.DIAServiceException;
import it.webred.ct.diagnostics.service.data.model.DocfaNonResidenziale;
import it.webred.ct.diagnostics.service.data.model.DocfaReport;
import it.webred.ct.diagnostics.service.data.model.DocfaReportNoRes;
import it.webred.ct.diagnostics.service.data.model.DocfaResidenziale;
import it.webred.ct.diagnostics.service.data.model.zc.ZCErrata;

import java.util.Date;
import java.util.List;

public interface IDAODocfa {

	public List<String> getCategorieDocfaNonResidenziale(DiaDocfaDTO dd) throws DIAServiceException;

	public List<String> getCategorieDocfaResidenziale(DiaDocfaDTO dd) throws DIAServiceException;

	public List<Date> getFornitureDocfaNonResidenziale(DiaDocfaDTO dd) throws DIAServiceException;	

	public List<DocfaNonResidenziale> getDocfaNonResidenziale(DiaDocfaDTO dd) throws DIAServiceException;

	public long getDocfaNonResidenzialeCount(DiaDocfaDTO dd) throws DIAServiceException;

	public List<DocfaResidenziale> getDocfaResidenziale(DiaDocfaDTO dd) throws DIAServiceException;

	public long getDocfaResidenzialeCount(DiaDocfaDTO dd) throws DIAServiceException;
	
	public List<Date> getFornitureDocfaResidenziale(DiaDocfaDTO dd) throws DIAServiceException;
		
	public List<DocfaReport> getDocfaReport(DiaDocfaDTO dd) throws DIAServiceException;
	
	public List<ZCErrata>  getZCErrate(DiaDocfaDTO dd) throws DIAServiceException;
	
	public List<DocfaReportNoRes> getDocfaReportNoRes(DiaDocfaDTO dd) throws DIAServiceException;
	
	public List<ZCErrata>  getZCErrateNoRes(DiaDocfaDTO dd) throws DIAServiceException;
}
