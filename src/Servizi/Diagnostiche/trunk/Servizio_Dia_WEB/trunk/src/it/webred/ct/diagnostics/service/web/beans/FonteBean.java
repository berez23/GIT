package it.webred.ct.diagnostics.service.web.beans;

import it.webred.cet.permission.GestionePermessi;
import it.webred.ct.config.model.AmFonte;
import it.webred.ct.config.model.AmInstance;
import it.webred.ct.config.parameters.fonte.FonteService;
import it.webred.ct.diagnostics.service.data.access.DiagnosticheService;
import it.webred.ct.diagnostics.service.data.dto.DiaCatalogoDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaEsecuzioneDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaTestataDTO;
import it.webred.ct.diagnostics.service.data.model.DiaTestata;
import it.webred.ct.diagnostics.service.data.model.controller.DiaCatalogo;
import it.webred.ct.diagnostics.service.web.user.UserBean;
import it.webred.ct.rulengine.service.bean.RecuperaComandoService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FonteBean extends UserBean implements Serializable {

	public class AmFonteDTO {
		
		private static final long serialVersionUID = 1L;
		private boolean checked;
		private AmFonte fonte;
		
		public AmFonte getFonte() {
			return fonte;
		}

		public void setFonte(AmFonte fonte) {
			this.fonte = fonte;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}
				
		public AmFonteDTO(AmFonte obj){
			fonte = obj;
		}
	}
	
	private static final long serialVersionUID = 1L;
	private static final String application = "diogene";
	private static final String item = "Visualizzazione Fonti Dati";
	private FonteService fonteService;
	private DiagnosticheService diaService;
	private RecuperaComandoService commandService = 
			(RecuperaComandoService) getEjb("CT_Controller", "CT_Controller_EJB", "RecuperaComandoServiceBean");
	
	private List<AmFonte> fontiInterne;				
	private List<AmFonte> fontiEsterne;
	private List<AmFonteDTO> fontiForEnteView;
	private List<AmFonteDTO> fontiForEnte;
	private String typeDiagnostiche; 
	private String navRule;
	
	public FonteBean(){
		super();
		// TODO Auto-generated constructor stub
	}
	
	private class FonteComparator implements Comparator<AmFonteDTO> {
		public int compare(AmFonteDTO o1, AmFonteDTO o2) {
			return o1.getFonte().getId().compareTo(o2.getFonte().getId());
		}
	} 
	
	public String doInit(){
		getLogger().debug("[doInit] - Start");
		
		//recupero diogene istanza
		String ente = getUser().getCurrentEnte();
		AmInstance istanza = applicationService.getInstanceByApplicationComune(application, ente);
		
		if (istanza != null)  {
			fontiForEnteView = new ArrayList<AmFonteDTO>();
			GestionePermessi.getPermissionsList(getUser(), true);
			
			for (AmFonte fonte : getFontiInterne()) {
				if (GestionePermessi.autorizzato(getUser(), istanza.getName() , item, "Fonte:"+fonte.getCodice().toUpperCase()))
					fontiForEnteView.add(new AmFonteDTO(fonte));
			}
			for (AmFonte fonte : getFontiEsterne()) {
				if (GestionePermessi.autorizzato(getUser(), istanza.getName() , item, "Fonte:"+fonte.getCodice().toUpperCase()))
					fontiForEnteView.add(new AmFonteDTO(fonte));
			}
			Collections.sort(fontiForEnteView, new FonteComparator());
			
			List<AmFonteDTO> newList = new ArrayList<AmFonteDTO>();
			for (AmFonteDTO fonte : fontiForEnteView) {
				DiaCatalogoDTO dc = new DiaCatalogoDTO(null,getUser().getCurrentEnte(),getUser().getName());
				List<Long> fontiId = new ArrayList<Long>();
				fontiId.add(Long.parseLong(fonte.fonte.getId().toString()));
				dc.setFonti(fontiId);
				List<DiaCatalogo> lDia = diaService.getAllDiagnosticheFonte(dc);
				for (DiaCatalogo diaCatalogo : lDia) {
					DiaEsecuzioneDTO esecDto = new DiaEsecuzioneDTO(diaCatalogo,
											super.getUser().getCurrentEnte(),super.getUser().getName());
					
					DiaTestata obj = new DiaTestata();
					obj.setIdCatalogoDia(esecDto.getIdCatalogoDia());
					DiaTestataDTO dtDTO = new DiaTestataDTO(obj,getUser().getCurrentEnte(),getUser().getName());
					DiaTestata objDt = diaService.getLastEsecuzioneByIdDia(dtDTO);
					if (objDt != null) {
						newList.add(fonte);
						break;
					}
				}
			}
			fontiForEnteView = newList;
			
		}
		
		if (istanza != null)  {
			fontiForEnte = new ArrayList<AmFonteDTO>();
			GestionePermessi.getPermissionsList(getUser(), true);
			
			for (AmFonte fonte : getFontiInterne()) {
				if (GestionePermessi.autorizzato(getUser(), istanza.getName() , item, "Fonte:"+fonte.getCodice().toUpperCase()))
					fontiForEnte.add(new AmFonteDTO(fonte));
			}
			for (AmFonte fonte : getFontiEsterne()) {
				if (GestionePermessi.autorizzato(getUser(), istanza.getName() , item, "Fonte:"+fonte.getCodice().toUpperCase()))
					fontiForEnte.add(new AmFonteDTO(fonte));
			}
			Collections.sort(fontiForEnte, new FonteComparator());
			
			List<AmFonteDTO> newList = new ArrayList<AmFonteDTO>();
			List<Long> types = new ArrayList<Long>();
			types.add(new Long("31"));
			types.add(new Long("32"));
			types.add(new Long("34"));
			types.add(new Long("35"));
			types.add(new Long("36"));
			types.add(new Long("37"));
			
			for (AmFonteDTO fonte : fontiForEnte) {
				Long fonteId = Long.parseLong(fonte.fonte.getId().toString());
				Long conta = commandService.getRCommandsByFonteTypesCount(getUser().getCurrentEnte(), fonteId, types);
				if (conta != null && conta.longValue() > 0) {
					newList.add(fonte);
				}
			}
			fontiForEnte = newList;
		}
		
	
		getLogger().debug("[doInit] - End");
		
		return navRule;
	}
	
	public String getNavRule() {
		return navRule;
	}

	public void setNavRule(String navRule) {
		this.navRule = navRule;
	}

	public String getTypeDiagnostiche() {
		return typeDiagnostiche;
	}

	public void setTypeDiagnostiche(String typeDiagnostiche) {
		this.typeDiagnostiche = typeDiagnostiche;
	}
	
	public List<AmFonteDTO> getFontiForEnteView() {
		return fontiForEnteView;
	}
	
	public void setFontiForEnteView(List<AmFonteDTO> fontiForEnteView) {
		this.fontiForEnteView = fontiForEnteView;
	}

	public List<AmFonteDTO> getFontiForEnte() {
		return fontiForEnte;
	}

	public void setFontiForEnte(List<AmFonteDTO> fontiForEnte) {
		this.fontiForEnte = fontiForEnte;
	}

	public FonteService getFonteService() {
		return fonteService;
	}


	public void setFonteService(FonteService fonteService) {
		this.fonteService = fonteService;
	}

	public DiagnosticheService getDiaService() {
		return diaService;
	}

	public void setDiaService(DiagnosticheService diaService) {
		this.diaService = diaService;
	}

	public RecuperaComandoService getCommandService() {
		return commandService;
	}

	public void setCommandService(RecuperaComandoService commandService) {
		this.commandService = commandService;
	}

	public List<AmFonte> getFontiInterne() {
		if (getUser() != null){
			fontiInterne = fonteService.getListaFonteByComuneETipoFonte(getUser().getCurrentEnte(), "I"); 
			return fontiInterne;
		}
		else		
			return null;
	}


	public void setFontiInterne(List<AmFonte> fontiInterne) {
		this.fontiInterne = fontiInterne;
	}


	public List<AmFonte> getFontiEsterne() {
		if (getUser() != null){
			fontiEsterne = fonteService.getListaFonteByComuneETipoFonte(getUser().getCurrentEnte(), "E"); 
			return fontiEsterne;
		}
		else
			return null;
	}


	public void setFontiEsterne(List<AmFonte> fontiEsterne) {
		this.fontiEsterne = fontiEsterne;
	}
	
	public List<Long> getFontiSelezionateView(){
		List<Long> fontiSel = new ArrayList<Long>();
		if (fontiForEnteView == null || fontiForEnteView.size() == 0) return fontiSel;
		
		for (AmFonteDTO fonteDto : fontiForEnteView) {
			if (fonteDto.checked) fontiSel.add(Long.parseLong(fonteDto.fonte.getId().toString()));
		}
		
		return fontiSel;
	}
	
	public List<Long> getFontiSelezionate(){
		List<Long> fontiSel = new ArrayList<Long>();
		if (fontiForEnte == null || fontiForEnte.size() == 0) return fontiSel;
		
		for (AmFonteDTO fonteDto : fontiForEnte) {
			if (fonteDto.checked) fontiSel.add(Long.parseLong(fonteDto.fonte.getId().toString()));
		}
		
		return fontiSel;
	}
}
