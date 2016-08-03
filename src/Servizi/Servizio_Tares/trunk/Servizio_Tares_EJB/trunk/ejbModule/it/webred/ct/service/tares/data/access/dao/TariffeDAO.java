package it.webred.ct.service.tares.data.access.dao;

import it.webred.ct.service.tares.data.access.dto.TariffeSearchCriteria;



import it.webred.ct.service.tares.data.model.SetarBilancioAnnoCorr;
import it.webred.ct.service.tares.data.model.SetarCoeff;
import it.webred.ct.service.tares.data.model.SetarConsuntivoAnnoPrec;
import it.webred.ct.service.tares.data.model.SetarTariffa;

import java.util.List;

public interface TariffeDAO {

	public List<SetarCoeff> getCoeff(TariffeSearchCriteria criteria);
	public SetarCoeff updCoeff(SetarCoeff setarCoeff);
	
	public List<SetarTariffa> getTariffe(TariffeSearchCriteria criteria);
	public SetarTariffa updTariffa(SetarTariffa setarTariffa);
	public void addTariffa(SetarTariffa setarTariffa);
	
	public List<SetarBilancioAnnoCorr> getBilancio(TariffeSearchCriteria criteria);
	public SetarBilancioAnnoCorr updBilancio(SetarBilancioAnnoCorr setarBilancio);
	
	public List<SetarConsuntivoAnnoPrec> getConsuntivo(TariffeSearchCriteria criteria);
	public SetarConsuntivoAnnoPrec updConsuntivo(SetarConsuntivoAnnoPrec setarConsuntivo);
	
	public List<Object> getClassiTarsu(TariffeSearchCriteria criteria);
	public List<Object> getDistribuzioneSupTotTarsu(TariffeSearchCriteria criteria);
	public List<Object> getDistribuzioneComponenti(TariffeSearchCriteria criteria);
	
}
