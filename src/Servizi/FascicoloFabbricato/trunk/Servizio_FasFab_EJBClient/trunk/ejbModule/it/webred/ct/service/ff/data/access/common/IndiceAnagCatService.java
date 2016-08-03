package it.webred.ct.service.ff.data.access.common;
import it.webred.ct.service.ff.data.access.common.dto.RicercaDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface IndiceAnagCatService {

	public List<BigDecimal> getIndiciAnagCat(RicercaDTO dati, Date dtRif) ;

}
