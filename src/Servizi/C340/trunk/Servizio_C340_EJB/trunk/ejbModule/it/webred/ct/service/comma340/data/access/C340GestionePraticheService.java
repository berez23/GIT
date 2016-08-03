package it.webred.ct.service.comma340.data.access;

import it.webred.ct.service.comma340.data.access.dto.C340PratAllegatoDTO;
import it.webred.ct.service.comma340.data.access.dto.C340PratDepositoPlanimDTO;
import it.webred.ct.service.comma340.data.access.dto.C340PratRettificaSupDTO;
import it.webred.ct.service.comma340.data.access.dto.RicercaGestionePraticheDTO;
import it.webred.ct.service.comma340.data.model.ente.CodiceBelfiore;
import it.webred.ct.service.comma340.data.model.pratica.C340PratAllegato;

import java.util.List;

import javax.ejb.Remote;


@Remote
public interface C340GestionePraticheService {
	
	
	public void creaPraticaDepositoPlanimetria(C340PratDepositoPlanimDTO dto);
	
	public C340PratDepositoPlanimDTO modificaPraticaDepositoPlanimetria(C340PratDepositoPlanimDTO dto);
	
	public void cancellaPraticaDepositoPlanimetria(RicercaGestionePraticheDTO ro);
	
	public void creaPraticaRettificaSuperficie(C340PratRettificaSupDTO dto);
	
	public C340PratRettificaSupDTO modificaPraticaRettificaSuperficie(C340PratRettificaSupDTO dto);
	
	public void cancellaPraticaRettificaSuperficie(RicercaGestionePraticheDTO ro);
	
	public List<CodiceBelfiore> getListaComuni(RicercaGestionePraticheDTO ro);
	
	/*
	public List<C340PratRettificaSup> getSuperfici(BigDecimal foglio, BigDecimal particella, BigDecimal unimm );
	
	public List<C340PratDepositoPlanim> getPlanimetrie(BigDecimal foglio, BigDecimal particella, BigDecimal unimm );
	 */
	
	public C340PratDepositoPlanimDTO getPraticaPlanimetria(RicercaGestionePraticheDTO ro);
	
	public C340PratRettificaSupDTO getPraticaSuperficie(RicercaGestionePraticheDTO ro);
	
	public void creaAllegato(C340PratAllegatoDTO dto);
	
	public void cancellaAllegato(RicercaGestionePraticheDTO ro);
	
	public void cancellaListaAllegatiPratica(RicercaGestionePraticheDTO ro);
	
	public List<C340PratAllegato> getListaAllegatiPratica(RicercaGestionePraticheDTO ro);
	
	public C340PratAllegato getAllegato(RicercaGestionePraticheDTO ro);
}
