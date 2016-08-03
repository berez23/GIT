package it.webred.ct.diagnostics.service.data.access;

import it.webred.ct.diagnostics.service.data.dto.DiaDocfaDTO;
import it.webred.ct.diagnostics.service.data.model.DocfaNonResidenziale;
import it.webred.ct.diagnostics.service.data.model.DocfaReport;
import it.webred.ct.diagnostics.service.data.model.DocfaReportNoRes;
import it.webred.ct.diagnostics.service.data.model.DocfaResidenziale;
import it.webred.ct.diagnostics.service.data.model.zc.ZCErrata;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface DiaDocfaService {
	
	public List<Date> getFornitureDocfaNonResidenziale(DiaDocfaDTO dd);
	
	public List<Date> getFornitureDocfaResidenziale(DiaDocfaDTO dd);
	
	public List<String> getCategorieDocfaResidenziale(DiaDocfaDTO dd);
	
	public List<String> getCategorieDocfaNonResidenziale(DiaDocfaDTO dd);

	public List<DocfaNonResidenziale> getDocfaNonResidenziale(DiaDocfaDTO dd);
	
	public List<DocfaResidenziale> getDocfaResidenziale(DiaDocfaDTO dd);

	public long getDocfaNonResidenzialeCount(DiaDocfaDTO dc);
	
	public long getDocfaResidenzialeCount(DiaDocfaDTO dc);
	
	public List<DocfaReport> getDocfaReport(DiaDocfaDTO dd);
			
	public List<ZCErrata> getZCErrate(DiaDocfaDTO dd);

	public List<DocfaReportNoRes> getDocfaReportNoRes(DiaDocfaDTO dd);
	
	public List<ZCErrata>  getZCErrateNoRes(DiaDocfaDTO dd);

}
