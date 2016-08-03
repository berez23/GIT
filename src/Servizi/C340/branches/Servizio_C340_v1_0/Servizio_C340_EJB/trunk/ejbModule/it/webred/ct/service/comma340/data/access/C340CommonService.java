
package it.webred.ct.service.comma340.data.access;

import java.math.BigDecimal;
import java.util.List;

import it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO;
import it.webred.ct.service.comma340.data.access.dto.C340PratDepositoPlanimDTO;
import it.webred.ct.service.comma340.data.access.dto.C340PratRettificaSupDTO;
import it.webred.ct.service.comma340.data.access.dto.DettaglioC340ImmobileDTO;
import it.webred.ct.service.comma340.data.access.dto.ModuloPraticaDTO;
import it.webred.ct.service.comma340.data.model.ente.CodiceBelfiore;
import it.webred.ct.service.comma340.data.model.pratica.C340PratDepositoPlanim;
import it.webred.ct.service.comma340.data.model.pratica.C340PratRettificaSup;

import javax.ejb.Remote;

@Remote
public interface C340CommonService {
	
	public DettaglioC340ImmobileDTO getDettaglioC340Immobile(String codNazionale, String idUiu);
	
}
