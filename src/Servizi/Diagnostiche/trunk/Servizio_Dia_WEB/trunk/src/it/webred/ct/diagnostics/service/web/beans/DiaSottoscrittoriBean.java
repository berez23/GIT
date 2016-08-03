package it.webred.ct.diagnostics.service.web.beans;

import it.webred.amprofiler.ejb.user.UserService;
import it.webred.amprofiler.model.AmUser;
import it.webred.ct.diagnostics.service.data.dto.DiaCommandDTO;
import it.webred.ct.diagnostics.service.web.user.UserBean;
import it.webred.ct.rulengine.controller.model.RCommand;
import it.webred.ct.rulengine.controller.model.RSottoscrittori;
import it.webred.ct.rulengine.service.bean.RecuperaComandoService;
import it.webred.ct.rulengine.service.bean.SottoscrizioniService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

public class DiaSottoscrittoriBean extends UserBean implements Serializable {
	

	private static final long serialVersionUID = 1L;
	
	List<SelectItem> utenti = new ArrayList<SelectItem>();
	List<DiaCommandDTO> comandi = new ArrayList<DiaCommandDTO>();
	
	private String belfiore;
	private String utente;
	
	private UserService userService;
	private RecuperaComandoService commandService = 
			(RecuperaComandoService) getEjb("CT_Controller", "CT_Controller_EJB", "RecuperaComandoServiceBean");
	private SottoscrizioniService sottoscrizioniService = 
			(SottoscrizioniService) getEjb("CT_Controller", "CT_Controller_EJB", "SottoscrizioniServiceBean");;
	
	private boolean subscribedOff;
	private String codCommand;
	private Long idSottoscrizione;
	
	@PostConstruct
	public void init() {
		belfiore = getUser().getCurrentEnte();
	}


	public void doCaricaListaUtentiEnte() {
		String ente = getUser().getCurrentEnte();
		
		utenti = new ArrayList<SelectItem>();
		super.getLogger().info("Caricamento utenti appartenenti all'ente "+ente);
		
		List<AmUser> uu = userService.getUsersByEnte(ente);
		super.getLogger().info("Utenti recuperati: "+uu.size());
		
		for(AmUser au: uu) {
			utenti.add(new SelectItem(au.getName()+"::"+au.getEmail(),au.getName()));
		}
	}
	
	
	public void doCaricaListaComandiDiagEnte() {
		String ente = getUser().getCurrentEnte();
		
		List<RCommand> cmds = new ArrayList<RCommand>();
		super.getLogger().info("Caricamento comandi di diagnostica appartenenti all'ente "+ente);
		
		if(utente != null && !utente.equals("")) {
			String[] utenteParts = utente.split("::");
			
			//comandi di diagnostiva di controllo 31
			cmds.addAll(commandService.getRCommandsByType(ente,new Long(31)));
			
			//comandi di diagnostiva di confronto 32
			cmds.addAll(commandService.getRCommandsByType(ente,new Long(32)));
			
			//comandi di diagnostiva di elaborazione 34
			cmds.addAll(commandService.getRCommandsByType(ente,new Long(34)));
			
			//comandi di diagnostiva di accertamento 35
			cmds.addAll(commandService.getRCommandsByType(ente,new Long(35)));
			
			//comandi di diagnostiva di staticstica 36
			cmds.addAll(commandService.getRCommandsByType(ente,new Long(36)));
			
			//comandi di diagnostiva di ricerca 37
			cmds.addAll(commandService.getRCommandsByType(ente,new Long(37)));
			
			super.getLogger().info("Comandi recuperati: "+comandi.size());
			
			//in base al valore di utente stabile chi è selected e chi no rivoltando ogni oggetto su un dto
			comandi = new ArrayList<DiaCommandDTO>();
			for(RCommand rc: cmds) {
				DiaCommandDTO comando = new DiaCommandDTO(rc,getUser().getCurrentEnte(),getUser().getName());
				
				//reperire le info sulle sottoscrizioni da tabella di RE_DEV R_SOTTOSCRIZIONI
				RSottoscrittori rs = sottoscrizioniService.getSottoscrizione(utenteParts[0],ente,rc.getCodCommand());
				if(rs != null) {
					comando.setSelected(true);
				}
				else {
					comando.setSelected(false);
				}
				
				comandi.add(comando);
			}
		}
	}

	
	
	public void doSubscribe() {
		
		String[] utenteParts = utente.split("::");
		
		if(subscribedOff) {
			sottoscrizioniService.unsubscribe(utenteParts[0],belfiore,codCommand);
		}
		else {
			RSottoscrittori rs = new RSottoscrittori();
			rs.setBelfiore(belfiore);
			rs.setFkCodCommand(codCommand);
			rs.setFkName(utenteParts[0]);
			rs.setFkUserEmail(utenteParts[1]);
			sottoscrizioniService.subscribe(rs);
		}
	}
	
	



	public String getBelfiore() {
		return belfiore;
	}


	public void setBelfiore(String belfiore) {
		this.belfiore = belfiore;
	}


	public UserService getUserService() {
		return userService;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public RecuperaComandoService getCommandService() {
		return commandService;
	}


	public void setCommandService(RecuperaComandoService commandService) {
		this.commandService = commandService;
	}


	public String getUtente() {
		return utente;
	}


	public void setUtente(String utente) {
		this.utente = utente;
	}


	public List<SelectItem> getUtenti() {
		return utenti;
	}


	public void setUtenti(List<SelectItem> utenti) {
		this.utenti = utenti;
	}


	public List<DiaCommandDTO> getComandi() {
		return comandi;
	}


	public void setComandi(List<DiaCommandDTO> comandi) {
		this.comandi = comandi;
	}




	public String getCodCommand() {
		return codCommand;
	}


	public void setCodCommand(String codCommand) {
		this.codCommand = codCommand;
	}


	public boolean isSubscribedOff() {
		return subscribedOff;
	}


	public void setSubscribedOff(boolean subscribedOff) {
		this.subscribedOff = subscribedOff;
	}


	public SottoscrizioniService getSottoscrizioniService() {
		return sottoscrizioniService;
	}


	public void setSottoscrizioniService(SottoscrizioniService sottoscrizioniService) {
		this.sottoscrizioniService = sottoscrizioniService;
	}


	public Long getIdSottoscrizione() {
		return idSottoscrizione;
	}


	public void setIdSottoscrizione(Long idSottoscrizione) {
		this.idSottoscrizione = idSottoscrizione;
	}


}
