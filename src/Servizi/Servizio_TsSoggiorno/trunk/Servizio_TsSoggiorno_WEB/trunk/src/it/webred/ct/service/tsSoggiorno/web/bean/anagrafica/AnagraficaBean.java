package it.webred.ct.service.tsSoggiorno.web.bean.anagrafica;

import java.util.Date;

import it.webred.ct.service.tsSoggiorno.data.access.AnagraficaService;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsSoggetto;
import it.webred.ct.service.tsSoggiorno.web.bean.TsSoggiornoBaseBean;
import it.webred.ct.service.tsSoggiorno.web.bean.util.UserBean;

public class AnagraficaBean extends TsSoggiornoBaseBean {

	private IsSoggetto soggetto;

	public IsSoggetto getSoggetto() {
		if (soggetto == null) {
			try {
				
				UserBean user = (UserBean) getBeanReference("userBean");
				DataInDTO dataIn = new DataInDTO();
				fillEnte(dataIn);
				dataIn.setCodFiscale(user.getUsername());
				soggetto = super.getAnagraficaService().getSoggettoByCodFis(dataIn);

			} catch (Throwable t) {
				super.addErrorMessage("caricamento.error", t.getMessage());
				getLogger().error("Eccezione: " + t.getMessage(), t);
			}
		}
		return soggetto;
	}

	public void setSoggetto(IsSoggetto soggetto) {
		this.soggetto = soggetto;
	}
	
	
	public void doUpdateSoggetto() {
		try {
			
			
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			soggetto.setUsrMod(soggetto.getCodfisc());
			soggetto.setDtMod(new Date());
			dataIn.setObj(soggetto);
			super.getAnagraficaService().updateSoggetto(dataIn);
			
			if(this.showMessage)
				super.addInfoMessage("salvataggio.anagrafica.ok");

		} catch (Throwable t) {
			super.addErrorMessage("salvataggio.anagrafica.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

}
