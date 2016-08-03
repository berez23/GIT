package it.webred.ct.diagnostics.service.web.beans.pagination;

import java.util.ArrayList;
import java.util.List;


import it.webred.ct.diagnostics.service.data.access.DiagnosticheService;
import it.webred.ct.diagnostics.service.data.dto.DiaCatalogoDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaEsecuzioneDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaTestataDTO;
import it.webred.ct.diagnostics.service.data.model.DiaTestata;
import it.webred.ct.diagnostics.service.data.model.controller.DiaCatalogo;
import it.webred.ct.diagnostics.service.web.beans.FonteBean;
import it.webred.ct.diagnostics.service.web.beans.FonteBean.AmFonteDTO;
import it.webred.ct.diagnostics.service.web.user.UserBean;
import it.webred.ct.rulengine.service.bean.RecuperaComandoService;


public class ViewDataProviderImpl extends UserBean implements ViewDataProvider{

	private static final long serialVersionUID = 1L;

	private DiagnosticheService diaService;
	
	
	
	public List<DiaEsecuzioneDTO> getVisualizzaByRange(int start, int rowNumber) {
		getLogger().info("[Lista esecuzioni] - Start");
		
		List<DiaEsecuzioneDTO> listaDiaExecute = new ArrayList<DiaEsecuzioneDTO>();
		
		try {
			List<Long> fontiSelezionate = getFontiSelezionate();
			if(fontiSelezionate != null && fontiSelezionate.size() > 0) {
				getLogger().info("[Lista esecuzioni] - Fonti Selezionate: " + fontiSelezionate.size());
				
				for(Long fs: fontiSelezionate) {
					getLogger().debug("[Lista esecuzioni] - id Fonte Selezionata: " + fs);
				}
				
				getLogger().debug("[Lista esecuzioni] - typeDiagnostiche: " + getTypeDiagnostiche());
				
				DiaCatalogoDTO dc = new DiaCatalogoDTO(null,getUser().getCurrentEnte(),getUser().getName());
				dc.setFonti(fontiSelezionate);
				dc.setTipoComando(getTypeDiagnostiche());
				dc.setStart(start);
				dc.setMaxrows(rowNumber);
				List<DiaCatalogo> lDia = diaService.getDiagnostiche(dc);
				
				for (DiaCatalogo diaCatalogo : lDia) {
					DiaEsecuzioneDTO esecDto = new DiaEsecuzioneDTO(diaCatalogo,
											super.getUser().getCurrentEnte(),super.getUser().getName());
					
					getLogger().info("[Lista esecuzioni] - Recupero le esecuzioni per la diagnostica:"+ diaCatalogo.getId());
					
					DiaTestata obj = new DiaTestata();
					obj.setIdCatalogoDia(esecDto.getIdCatalogoDia());
					DiaTestataDTO dtDTO = new DiaTestataDTO(obj,getUser().getCurrentEnte(),getUser().getName());
					DiaTestata objDt = diaService.getLastEsecuzioneByIdDia(dtDTO);
					if (objDt == null )
						getLogger().debug("Nessuna esecuzione per la diagnostica:"	+ diaCatalogo.getId());
					else  {
						DiaTestataDTO testDto = new DiaTestataDTO(objDt,getUser().getCurrentEnte(),getUser().getName());
						setDtoFonti(testDto);
						esecDto.addEsecuzioni(testDto);
					}
					
					listaDiaExecute.add(esecDto);
				}				
			}
			
			getLogger().debug("[Lista esecuzioni] - N. oggetti listaDiaExecute:"+listaDiaExecute.size());
			getLogger().info("[Lista esecuzioni] - End");
			
		}catch(Exception e) {
			getLogger().warn("Eccezione: "+e.getMessage());
			listaDiaExecute = new ArrayList<DiaEsecuzioneDTO>();
		}
		
		return listaDiaExecute;
	}
	
	private void setDtoFonti(DiaTestataDTO testDto) {
		if (getRequest().getSession().getAttribute("fonteBean") == null)
			return;
		FonteBean fonteBean = (FonteBean)getRequest().getSession().getAttribute("fonteBean");
		List<AmFonteDTO> fontiForEnteView = fonteBean.getFontiForEnteView();
		String dataRifShort = testDto.getDataRifShort();
		if (dataRifShort.indexOf("Fonte") > -1) {
			StringBuffer sb = new StringBuffer();
			String[] fonti = dataRifShort.split("Fonte");
			for (String fonte : fonti) {
				fonte = fonte.trim();					
				if (fonte.equals("")) {
					continue;
				}
				int idxDa = fonte.indexOf(":[") + ":[".length();
				int idxA = fonte.indexOf("]");
				String desFonte = fonte.substring(idxDa, idxA);
				try {
					Integer idFonte = Integer.parseInt(fonte.substring(idxDa, idxA));					
					for (AmFonteDTO fonteForEnteView : fontiForEnteView) {
						if (fonteForEnteView.getFonte().getId().intValue() == idFonte.intValue()) {
							desFonte = fonteForEnteView.getFonte().getDescrizione();
							break;
						}
					}
				} catch (Exception e) {}
				sb.append("Fonte ");
				sb.append(desFonte);
				sb.append(": ");
				sb.append(fonte.substring(idxA + 1));
			}
			dataRifShort = sb.toString();
		}
		testDto.setDataRifShort(dataRifShort);		
	}

	public long getVisualizzaCount() {
		long size = 0;
		
		try {
			List<Long> fontiSelezionate = getFontiSelezionate();
			if(fontiSelezionate != null && fontiSelezionate.size() > 0) {
				DiaCatalogoDTO dc = new DiaCatalogoDTO(null,getUser().getCurrentEnte(),getUser().getName());
				dc.setFonti(fontiSelezionate);
				dc.setTipoComando(getTypeDiagnostiche());
				size =  diaService.getDiagnosticheCount(dc);
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
		return fonti.getFontiSelezionateView();
	}

	private long getTypeDiagnostiche()  {
		if (getRequest().getSession().getAttribute("fonteBean") == null)
			return 0;
		FonteBean fonti = (FonteBean)getRequest().getSession().getAttribute("fonteBean");
		return Long.parseLong(fonti.getTypeDiagnostiche());
	}

	public DiagnosticheService getDiaService() {
		return diaService;
	}

	public void setDiaService(DiagnosticheService diaService) {
		this.diaService = diaService;
	}


}
