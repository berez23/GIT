package it.webred.ct.diagnostics.service.web.beans.pagination;



import java.util.List;

public interface DataProvider {
    
	public List<String[]> getDettaglioByRange(int start, int rowNumber);
	
	public long getDettaglioCount();
}
