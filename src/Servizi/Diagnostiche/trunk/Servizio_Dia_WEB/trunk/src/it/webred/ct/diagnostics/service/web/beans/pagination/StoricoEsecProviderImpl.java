package it.webred.ct.diagnostics.service.web.beans.pagination;

import it.webred.ct.diagnostics.service.data.access.DiagnosticheService;
import it.webred.ct.diagnostics.service.data.dto.DiaTestataDTO;
import it.webred.ct.diagnostics.service.data.model.DiaTestata;
import it.webred.ct.diagnostics.service.web.beans.FonteBean;
import it.webred.ct.diagnostics.service.web.beans.FonteBean.AmFonteDTO;
import it.webred.ct.diagnostics.service.web.user.UserBean;

import java.util.ArrayList;
import java.util.List;


public class StoricoEsecProviderImpl extends UserBean implements StoricoEsecDataProvider{

	private static final long serialVersionUID = 1L;
	
	private DiagnosticheService diaService;
	private long idCatalogoDia;
	
	public long getIdCatalogoDia() {
		return idCatalogoDia;
	}

	public void setIdCatalogoDia(long idCatalogoDia) {
		this.idCatalogoDia = idCatalogoDia;
	}

	public List<DiaTestataDTO> getVisualizzaByRange(int start, int rowNumber) {
		getLogger().info("[ViewStoricoEsecDataProviderImpl.getVisualizzaByRange] - Start");
		
		List<DiaTestataDTO> listaStorico = new ArrayList<DiaTestataDTO>();
		
		try {
			
				DiaTestata dt4Params = new DiaTestata();
				dt4Params.setIdCatalogoDia(idCatalogoDia);
				
				DiaTestataDTO dt = new DiaTestataDTO(dt4Params,getUser().getCurrentEnte(),getUser().getName());								
				dt.setStart(start);
				dt.setNumberRecord(rowNumber);
				List<DiaTestata> lDia = diaService.getDiaEsecuzioneByIdDia(dt);
				
				for (DiaTestata diaTestata : lDia) {
					DiaTestataDTO esecDto = new DiaTestataDTO(diaTestata,
											super.getUser().getCurrentEnte(),super.getUser().getName());
					setDtoFonti(esecDto);										
					listaStorico.add(esecDto);
				}				
			
			
			getLogger().debug("[ViewStoricoEsecDataProviderImpl.getVisualizzaByRange] - N. oggetti listaDiaExecute:"+listaStorico.size());
			getLogger().info("[ViewStoricoEsecDataProviderImpl.getVisualizzaByRange] - End");
			
		}catch(Exception e) {
			getLogger().warn("Eccezione: "+e.getMessage());
			listaStorico = new ArrayList<DiaTestataDTO>();
		}
		
		return listaStorico;
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
			if(super.getUser() != null) {
				DiaTestata dt4Params = new DiaTestata();
				dt4Params.setIdCatalogoDia(idCatalogoDia);
				
				DiaTestataDTO dt = new DiaTestataDTO(dt4Params,getUser().getCurrentEnte(),getUser().getName());				
				size =  diaService.getStoricoEsecuzioniCount(dt);
			}
		}catch(Exception e) {
			getLogger().error("Eccezione: "+e.getMessage(),e);
		}
		
		return size;
	}
	
	
	public DiagnosticheService getDiaService() {
		return diaService;
	}

	public void setDiaService(DiagnosticheService diaService) {
		this.diaService = diaService;
	}


}
