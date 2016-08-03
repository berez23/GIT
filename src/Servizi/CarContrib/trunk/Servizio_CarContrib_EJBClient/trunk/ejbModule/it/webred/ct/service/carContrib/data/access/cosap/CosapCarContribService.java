package it.webred.ct.service.carContrib.data.access.cosap;

import it.webred.ct.data.access.basic.common.dto.RicercaSoggettoDTO;
import it.webred.ct.data.access.basic.cosap.dto.RicercaSoggettoCosapDTO;
import it.webred.ct.data.model.cosap.SitTCosapContrib;
import it.webred.ct.data.model.cosap.SitTCosapTassa;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface CosapCarContribService {
	public List<SitTCosapContrib> searchSoggettoCosap (RicercaSoggettoCosapDTO rs );
	public SitTCosapContrib getDatiCosapSoggetto (RicercaSoggettoCosapDTO rs );
	public List<SitTCosapTassa> getDatiCosap(RicercaSoggettoCosapDTO rs, Date dtRif);

}
