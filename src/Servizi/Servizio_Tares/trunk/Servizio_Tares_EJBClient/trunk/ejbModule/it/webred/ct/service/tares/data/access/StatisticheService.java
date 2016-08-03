package it.webred.ct.service.tares.data.access;

import it.webred.ct.service.tares.data.access.dto.DataInDTO;

import it.webred.ct.service.tares.data.model.SetarDia;
import it.webred.ct.service.tares.data.model.SetarStat;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface StatisticheService {

	public List<SetarStat> getStatistiche(DataInDTO dataIn);
	public Number getStatisticheCount(DataInDTO dataIn);
	public List<SetarDia> getDiagnostiche(DataInDTO dataIn);

}
