package it.webred.cs.csa.web.manbean.fascicolo.interventi;

import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.web.manbean.fascicolo.FascicoloCompBaseBean;
import it.webred.cs.data.model.CsIIntervento;
import it.webred.cs.jsf.bean.DatiInterventoBean;
import it.webred.cs.jsf.interfaces.IDatiInterventi;

import java.util.ArrayList;
import java.util.List;

public class InterventiBean extends FascicoloCompBaseBean implements IDatiInterventi {
	
	private FglInterventoBean fglInterventoBean;
	
	@Override
	protected void initializeData() {
		
		fglInterventoBean = new FglInterventoBean();
		fglInterventoBean.initialize(csASoggetto);
		fglInterventoBean.setReadOnly(readOnly);
		
	};
	
	@Override
	public List<DatiInterventoBean> getListaInterventi()  {
		List<DatiInterventoBean> lst = new ArrayList<DatiInterventoBean>();
		try{
				if(idCaso!=null && idCaso>0){
				BaseDTO dto = new BaseDTO();
				fillEnte(dto);
				dto.setObj(idCaso);
				List<CsIIntervento> lsti = interventoService.getListaInterventiByCaso(dto);
				for(CsIIntervento i : lsti){
					DatiInterventoBean dib = new DatiInterventoBean(i, idSoggetto);
					lst.add(dib);
				}
				
			}
		
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
		
		return lst;
	}

	@Override
	public Long getIdCaso() {
		return idCaso;
	}

	@Override
	public void setIdCaso(Long idCaso) {
		this.idCaso = idCaso;
	}

	public FglInterventoBean getFglInterventoBean() {
		return fglInterventoBean;
	}

	public void setFglInterventoBean(FglInterventoBean fglInterventoBean) {
		this.fglInterventoBean = fglInterventoBean;
	}

}
