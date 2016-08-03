package it.webred.ct.service.tares.data.access.dao;

import it.webred.ct.service.tares.data.access.dto.DiagnosticheSearchCriteria;
import it.webred.ct.service.tares.data.access.dto.StatisticheSearchCriteria;
import it.webred.ct.service.tares.data.model.SetarDia;
import it.webred.ct.service.tares.data.model.SetarStat;

import java.util.List;

public interface StatisticheDAO {

	public List<SetarStat> getStatistiche(StatisticheSearchCriteria criteria);
	public Number getStatisticheCount(StatisticheSearchCriteria criteria);
	public List<SetarDia> getDiagnostiche(DiagnosticheSearchCriteria criteria);
	
}
