package it.webred.ct.service.muidocfa.web.bean.ici.pagination;

import it.webred.ct.data.model.diagnostiche.DocfaIciReport;
import it.webred.ct.data.model.diagnostiche.DocfaIciReportSogg;

import java.util.List;

public interface DataProvider {
    
    public Long getReportCount();
    
    public List<DocfaIciReport> getReportByRange(int start, int rowNumber);
    
    public List<DocfaIciReportSogg> getReportSogg(String id);
    
	public String getCategoria(String codCategoria);
	
	public String getCausale(String codCausale);
    
    public void resetData();
}
