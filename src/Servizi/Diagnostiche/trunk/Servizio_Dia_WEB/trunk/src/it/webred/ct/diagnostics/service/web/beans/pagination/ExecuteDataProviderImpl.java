package it.webred.ct.diagnostics.service.web.beans.pagination;

import java.util.ArrayList;
import java.util.List;

import it.webred.ct.diagnostics.service.data.access.DiagnosticheService;

import it.webred.ct.diagnostics.service.data.dto.DiaCommandDTO;
import it.webred.ct.diagnostics.service.web.beans.FonteBean;
import it.webred.ct.diagnostics.service.web.user.UserBean;

import it.webred.ct.rulengine.controller.model.RCommand;
import it.webred.ct.rulengine.controller.model.RProcessMonitor;
import it.webred.ct.rulengine.controller.model.RProcessMonitorPK;
import it.webred.ct.rulengine.service.bean.ProcessMonitorService;
import it.webred.ct.rulengine.service.bean.RecuperaComandoService;


public class ExecuteDataProviderImpl extends UserBean implements ExecuteDataProvider{

	private static final long serialVersionUID = 1L;

	private RecuperaComandoService commandService = 
			(RecuperaComandoService) getEjb("CT_Controller", "CT_Controller_EJB", "RecuperaComandoServiceBean");
	private DiagnosticheService diaService;
	private ProcessMonitorService procMonitorService = 
			(ProcessMonitorService) getEjb("CT_Controller", "CT_Controller_EJB", "ProcessMonitorServiceBean");
	
	private String typeDiagnostiche;
	
	
	
	public List<DiaCommandDTO> getEsecuzioneByRange(int start, int rowNumber) {
		getLogger().info("[Lista comandi] - Start");
		
		List<DiaCommandDTO> listaDiaExecute = new ArrayList<DiaCommandDTO>();
		
		try {
			List<Long> fonti = getFontiSelezionate();
			
			if (fonti != null && fonti.size() > 0) {
				getLogger().info("[Lista comandi] - Fonti Selezionate: " + fonti.size());
				
				for (Long strIdFonte : fonti) {
					getLogger().info("[Lista comandi] - id Fonte Selezionata: " + strIdFonte);
				}
				
				getLogger().info("[Lista comandi] - typeDiagnostiche: " + getTypeDiagnostiche());
				
				//Recupero la lista dei comandi secondo i filtri impostati
				getLogger().info("[Lista comandi] - Recupero lista comandi per fonte e tipo comando da RecuperaComandoService ");
				
				//chiamata al metodo dell'ejb che gestisce il filtro paginazione
				List<RCommand> lsCmd = 
					commandService.getRCommandsByFontiAndType(start,rowNumber,super.getEnte(), 
															fonti, Long.parseLong(getTypeDiagnostiche()));
				if (lsCmd == null){
					getLogger().debug("[Lista comandi] - Nessun comando soddisfa i criteri selezionati");
				}
				
				//Dalla lista dei comandi recupero il monitor del processo
				RProcessMonitorPK rpmPK = new RProcessMonitorPK();
				rpmPK.setBelfiore(super.getEnte());	
				
				for (RCommand rCommand : lsCmd) {
					DiaCommandDTO cmdDto = new DiaCommandDTO(rCommand,super.getUser().getCurrentEnte(),super.getUser().getName());			
					rpmPK.setFkCommand(cmdDto.getId());				
					RProcessMonitor rpm =  procMonitorService.getProcessMonitor(rpmPK);
					if (rpm != null)
						cmdDto.setDescrCommandStato(rpm.getRAnagStato().getDescr());
					else
						cmdDto.setDescrCommandStato(super.getMessage("dia.command.not.found"));

					listaDiaExecute.add(cmdDto);
				}
			}			
			
			getLogger().debug("[Lista comandi] - N. oggetti listaDiaExecute:"+listaDiaExecute.size());
			getLogger().info("[Lista comandi] - End");
			
		}catch(Exception e) {
			getLogger().warn("Eccezione: "+e.getMessage());
			listaDiaExecute = new ArrayList<DiaCommandDTO>();
		}
		
		return listaDiaExecute;
	}

	public long getEsecuzioneCount() {
		long size = 0;
		
		try {
			List<Long> fonti = getFontiSelezionate();
			
			if (fonti != null && fonti.size() > 0) {
				size = this.commandService.getRCommandsByFontiAndTypeCount(getUser().getCurrentEnte(), 
						fonti, new Long(getTypeDiagnostiche()));
			}
		}catch(Exception e) {
			getLogger().error("Eccezione: "+e.getMessage(),e);
		}
		
		return size;
	}
	
	

	private List<Long> getFontiSelezionate()  {
		if (getRequest().getSession().getAttribute("fonteBean") == null)
			return null;
		FonteBean fonti = (FonteBean)getRequest().getSession().getAttribute("fonteBean");
		return fonti.getFontiSelezionate();
	}

	public String getTypeDiagnostiche()  {
		if (getRequest().getSession().getAttribute("fonteBean") == null)
			return "0";
		FonteBean fonti = (FonteBean)getRequest().getSession().getAttribute("fonteBean");
		return fonti.getTypeDiagnostiche();
	}

	public void setTypeDiagnostiche(String typeDiagnostiche) {
		this.typeDiagnostiche = typeDiagnostiche;
	}

	public RecuperaComandoService getCommandService() {
		return commandService;
	}

	public void setCommandService(RecuperaComandoService commandService) {
		this.commandService = commandService;
	}

	public DiagnosticheService getDiaService() {
		return diaService;
	}

	public void setDiaService(DiagnosticheService diaService) {
		this.diaService = diaService;
	}

	public ProcessMonitorService getProcMonitorService() {
		return procMonitorService;
	}

	public void setProcMonitorService(ProcessMonitorService procMonitorService) {
		this.procMonitorService = procMonitorService;
	}
	

}
