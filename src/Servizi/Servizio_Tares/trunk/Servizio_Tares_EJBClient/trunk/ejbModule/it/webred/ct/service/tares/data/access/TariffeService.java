package it.webred.ct.service.tares.data.access;

import it.webred.ct.service.tares.data.access.dto.DataInDTO;

import it.webred.ct.service.tares.data.model.SetarBilancioAnnoCorr;
import it.webred.ct.service.tares.data.model.SetarCoeff;
import it.webred.ct.service.tares.data.model.SetarConsuntivoAnnoPrec;
import it.webred.ct.service.tares.data.model.SetarTariffa;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface TariffeService {

	public List<SetarCoeff> getCoeff(DataInDTO dataIn);
	public SetarCoeff updCoeff(DataInDTO dataIn);
	
	public List<SetarTariffa> getTariffe(DataInDTO dataIn);
	public SetarTariffa updTariffa(DataInDTO dataIn);
	public void addTariffa(DataInDTO dataIn);
	
	public List<SetarBilancioAnnoCorr> getBilancio(DataInDTO dataIn);
	public SetarBilancioAnnoCorr updBilancio(DataInDTO dataIn);
	
	public List<SetarConsuntivoAnnoPrec> getConsuntivo(DataInDTO dataIn);
	public SetarConsuntivoAnnoPrec updConsuntivo(DataInDTO dataIn);
	
	public List<Object> getClassiTarsu(DataInDTO dataIn);
	public List<Object> getDistribuzioneSupTotTarsu(DataInDTO dataIn);
	public List<Object> getDistribuzioneComponenti(DataInDTO dataIn);


}
