package it.webred.ct.service.tsSoggiorno.web.bean.rimbsanz;

import it.webred.ct.service.tsSoggiorno.data.access.dto.RimbSanzDTO;

import java.util.List;

public interface RimbSanzDataProvider {
    
    public int getRimbSanzCount();
    
    public List<RimbSanzDTO> getRimbSanzByRange(int start, int rowNumber);

}
