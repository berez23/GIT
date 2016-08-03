package it.webred.ct.service.ff.data.access.dettaglio.compravendite;

import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.compravendite.CompravenditeMUIException;
import it.webred.ct.data.access.basic.compravendite.CompravenditeMUIService;
import it.webred.ct.data.access.basic.compravendite.dto.RicercaCompravenditeDTO;
import it.webred.ct.data.access.basic.compravendite.dto.SoggettoCompravenditeDTO;
import it.webred.ct.data.model.compravendite.MuiFabbricatiIdentifica;
import it.webred.ct.data.model.compravendite.MuiNotaTras;
import it.webred.ct.service.ff.data.access.FFServiceBaseBean;
import it.webred.ct.service.ff.data.access.FFServiceException;
import it.webred.ct.service.ff.data.access.dettaglio.compravendite.dto.DatiCompravenditeDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class CompravenditeFasFabServiceBean extends FFServiceBaseBean implements CompravenditeFasFabService {
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CompravenditeMUIServiceBean")
	private CompravenditeMUIService compravenditeService;

	public List<DatiCompravenditeDTO> getListaCompravenditeUiu(RicercaOggettoCatDTO ro) {
	
		List<DatiCompravenditeDTO> listaNote= new ArrayList<DatiCompravenditeDTO>();
		
		try{
				
				List<MuiNotaTras> listaMuiNotaTras= compravenditeService.getListaNoteByFPS(ro);
				for (MuiNotaTras muiNota: listaMuiNotaTras){
					
					DatiCompravenditeDTO nota= new DatiCompravenditeDTO(muiNota);
					String idNota= nota.getIdNota();
					RicercaCompravenditeDTO ricercaCompravendite= new RicercaCompravenditeDTO();
					ricercaCompravendite.setEnteId(ro.getEnteId());
					ricercaCompravendite.setUserId(ro.getUserId());
					ricercaCompravendite.setIdNota(idNota);
					
					 List<SoggettoCompravenditeDTO> listaSoggettiCompravenditeUiu= compravenditeService.getListaSoggettiNota(ricercaCompravendite);
					 
					 nota.setListaSoggetti(listaSoggettiCompravenditeUiu);
					 
					 listaNote.add(nota);
			}	
		
		}	
		catch(Throwable t){
			throw new FFServiceException(t);
		}

		
		return listaNote;
	}
	
	
public List<DatiCompravenditeDTO> getListaCompravenditeTerreno(RicercaOggettoCatDTO ro) {
		
		List<DatiCompravenditeDTO> listaNote= new ArrayList<DatiCompravenditeDTO>();
		
		try{
		List<MuiNotaTras> listaMuiNotaTras= compravenditeService.getListaNoteTerrenoByFP(ro);
		
		for (MuiNotaTras muiNota: listaMuiNotaTras){
			if (ro.getDtVal() != null && muiNota.getDataValiditaAttoDate() !=null && muiNota.getDataValiditaAttoDate().after(ro.getDtVal())) 
				continue;
			DatiCompravenditeDTO nota= new DatiCompravenditeDTO(muiNota);
			String idNota= nota.getIdNota();
			RicercaCompravenditeDTO ricercaCompravendite= new RicercaCompravenditeDTO();
			ricercaCompravendite.setEnteId(ro.getEnteId());
			ricercaCompravendite.setUserId(ro.getUserId());
			ricercaCompravendite.setIdNota(idNota);
			
			 List<SoggettoCompravenditeDTO> listaSoggettiCompravenditeUiu= compravenditeService.getListaSoggettiNota(ricercaCompravendite);
			 
			 nota.setListaSoggetti(listaSoggettiCompravenditeUiu);
			 
			 listaNote.add(nota);
			 
			}	
		}
		catch(Throwable t){

			throw new FFServiceException(t);
		}
		
		return listaNote;
	}
	
	public List<DatiCompravenditeDTO> getListaCompravenditeParticella(RicercaOggettoCatDTO ro) {
		
		List<DatiCompravenditeDTO> listaNote= new ArrayList<DatiCompravenditeDTO>();
		
		try{
				
				List<MuiNotaTras> listaMuiNotaTras= compravenditeService.getListaNoteByFP(ro);
				for (MuiNotaTras muiNota: listaMuiNotaTras){
					if (ro.getDtVal() != null && muiNota.getDataValiditaAttoDate() !=null && muiNota.getDataValiditaAttoDate().after(ro.getDtVal())) 
						continue;
					DatiCompravenditeDTO nota= new DatiCompravenditeDTO(muiNota);
					String idNota= nota.getIdNota();
					BigDecimal iid = nota.getIid();
					RicercaCompravenditeDTO ricercaCompravendite= new RicercaCompravenditeDTO();
					ricercaCompravendite.setEnteId(ro.getEnteId());
					ricercaCompravendite.setUserId(ro.getUserId());
					//ricercaCompravendite.setIdNota(idNota);
					ricercaCompravendite.setIidNota(iid);
					
					 List<SoggettoCompravenditeDTO> listaSoggettiCompravendite= compravenditeService.getListaSoggettiNota(ricercaCompravendite);
					 
					 nota.setListaSoggetti(listaSoggettiCompravendite);
			
				  	 RicercaCompravenditeDTO rc = new RicercaCompravenditeDTO();
					 rc.setEnteId(ro.getEnteId());
					 rc.setUserId(ro.getUserId());
					 rc.setIidFornitura(nota.getIidFornitura());
					 rc.setIidNota(nota.getIid());
					 rc.setFoglio(ro.getFoglio());
					 rc.setParticella(ro.getParticella());
					 String elencoUI="";
					 List<MuiFabbricatiIdentifica>  listaUIComprav = compravenditeService.getListaUIByNotaFabb(rc);
					 if (listaUIComprav != null && listaUIComprav.size() > 0) {
						for (MuiFabbricatiIdentifica fabb: listaUIComprav)  {
							if(fabb.getSubalterno()!=null){
								if (elencoUI.equals(""))
									elencoUI = fabb.getSubalterno();
								else
									elencoUI += " - " + fabb.getSubalterno();
							}
								
						}
						nota.setElencoUI(elencoUI);
					 }
				
					 listaNote.add(nota);
			}	
		
		}	
		catch(Throwable t){
			throw new FFServiceException(t);
		}
	
		
		return listaNote;
	}

}
