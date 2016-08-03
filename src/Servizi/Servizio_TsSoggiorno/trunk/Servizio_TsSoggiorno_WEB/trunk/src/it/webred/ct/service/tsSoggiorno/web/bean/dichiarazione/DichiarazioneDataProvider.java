package it.webred.ct.service.tsSoggiorno.web.bean.dichiarazione;

import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneDTO;

import java.util.List;

public interface DichiarazioneDataProvider {
    
    public int getDichiarazioniCount();
    
    public List<DichiarazioneDTO> getDichiarazioniByRange(int start, int rowNumber);

}
