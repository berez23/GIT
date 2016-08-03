package it.webred.ct.service.comma336.web.bean.statistiche;

import it.webred.ct.data.access.basic.c336.dto.C336StatOperatoreDTO;
import it.webred.ct.data.access.basic.c336.dto.RicercaStatisticheDTO;
import it.webred.ct.service.comma336.web.Comma336BaseBean;
import it.webred.ct.service.comma336.web.bean.util.PermessiHandler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

public class StatisticheBean extends Comma336BaseBean {
	
	private List<C336StatOperatoreDTO> statistiche;
	private boolean visStats; 
	
	
	public boolean isVisStats() {
		return visStats;
	}

	public void setVisStats(boolean visStats) {
		this.visStats = visStats;
	}

	@PostConstruct
	public void initService() {
		
		statistiche = new ArrayList<C336StatOperatoreDTO>();
	}
	
	public void loadStatistiche(){
			try {
			valVisStats();
			if (!visStats)
				return;
			Boolean supervisore = PermessiHandler.controlla(this.getUser(), PermessiHandler.PERMESSO_SUPERVISIONE_PRATICHE);
			
			RicercaStatisticheDTO rs = new RicercaStatisticheDTO();
			fillEnte(rs);
			rs.setUserId(this.getUser().getUsername());
			rs.setSupervisore(supervisore);
			
			statistiche = praticaService.getStatisticheOperatore(rs);
		} catch (Throwable t) {
			super.addErrorMessage("statistiche.error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
	}
	
	public String goStatistiche() {
		this.loadStatistiche();
		return "c336.statistiche";
	}

	public void setStatistiche(List<C336StatOperatoreDTO> statistiche) {
		this.statistiche = statistiche;
	}

	public List<C336StatOperatoreDTO> getStatistiche() {
		return statistiche;
	}
	

	private void valVisStats() {
		if (PermessiHandler.controlla(this.getUser(), PermessiHandler.PERMESSO_SUPERVISIONE_PRATICHE) || PermessiHandler.controlla(this.getUser(), PermessiHandler.PERMESSO_GESTIONE_PRATICHE)){
			visStats=true;
		}
		else {
			visStats=false;
		}
		
	}
	

}
