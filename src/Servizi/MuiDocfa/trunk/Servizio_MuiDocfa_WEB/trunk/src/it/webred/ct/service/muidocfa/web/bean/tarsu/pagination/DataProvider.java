package it.webred.ct.service.muidocfa.web.bean.tarsu.pagination;

import it.webred.ct.data.model.diagnostiche.DocfaTarReport;
import it.webred.ct.data.model.diagnostiche.DocfaTarReportSogg;

import java.util.List;

public interface DataProvider {
    
    public Long getReportCount();
    
    public List<DocfaTarReport> getReportByRange(int start, int rowNumber);
    
    public List<DocfaTarReportSogg> getReportSogg(String id);
    
	public String getCategoria(String codCategoria);
    
	public String getCausale(String codCausale);
	
    public void resetData();
}
