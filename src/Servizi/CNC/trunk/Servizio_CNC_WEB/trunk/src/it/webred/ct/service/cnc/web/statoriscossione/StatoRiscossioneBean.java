package it.webred.ct.service.cnc.web.statoriscossione;

import it.webred.ct.data.access.basic.cnc.statoriscossione.StatoRiscossioneServiceException;
import it.webred.ct.data.access.basic.cnc.statoriscossione.dto.FullRiscossioneInfo;
import it.webred.ct.data.model.cnc.statoriscossione.ChiaveULStatoRiscossione;

import java.io.Serializable;

public class StatoRiscossioneBean extends StatoRiscossioneBaseBean implements Serializable {
	
	private ChiaveULStatoRiscossione chiave = new ChiaveULStatoRiscossione();
	private FullRiscossioneInfo riscossioneInfo;
	
	
	public String doFindDettaglioRiscossione() {
		try {
			riscossioneInfo = super.getStatoRiscossioneService().getRiscossioneInfo(createDataIn(chiave));
		}
		catch(StatoRiscossioneServiceException srse) {
			super.getLogger().error("", srse);
			super.addErrorMessage("statoriscossione.dettaglio.error", srse.getCause().getMessage());
			getLogger().debug("Riscossione ["+riscossioneInfo+"]");
		}
		
		return "StatoRiscossione.dettaglioRiscossione";
	}


	public ChiaveULStatoRiscossione getChiave() {
		return chiave;
	}


	public void setChiave(ChiaveULStatoRiscossione chiave) {
		this.chiave = chiave;
	}


	public FullRiscossioneInfo getRiscossioneInfo() {
		return riscossioneInfo;
	}


	public void setRiscossioneInfo(FullRiscossioneInfo riscossioneInfo) {
		this.riscossioneInfo = riscossioneInfo;
	}
	
	public String doGotoList() {
		return "StatoRiscossione.listaRiscossioni";
	}

}
