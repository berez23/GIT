package it.webred.ct.service.comma340.data.access;

import it.webred.ct.service.comma340.data.access.dto.C340PratDepositoPlanimDTO;
import it.webred.ct.service.comma340.data.access.dto.C340PratRettificaSupDTO;
import it.webred.ct.service.comma340.data.model.ente.CodiceBelfiore;
import it.webred.ct.service.comma340.data.model.pratica.C340PratAllegato;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Remote;

import org.apache.log4j.Logger;

@Remote
public interface C340GestionePraticheService {
	
	
	public void creaPraticaDepositoPlanimetria(C340PratDepositoPlanimDTO dto);
	
	public void modificaPraticaDepositoPlanimetria(C340PratDepositoPlanimDTO dto);
	
	public void cancellaPraticaDepositoPlanimetria(Long idPra);
	
	public void creaPraticaRettificaSuperficie(C340PratRettificaSupDTO dto);
	
	public void modificaPraticaRettificaSuperficie(C340PratRettificaSupDTO dto);
	
	public void cancellaPraticaRettificaSuperficie(Long idPra);
	
	public List<CodiceBelfiore> getListaComuni(String desc);
	
	/*
	public List<C340PratRettificaSup> getSuperfici(BigDecimal foglio, BigDecimal particella, BigDecimal unimm );
	
	public List<C340PratDepositoPlanim> getPlanimetrie(BigDecimal foglio, BigDecimal particella, BigDecimal unimm );
	 */
	
	public C340PratDepositoPlanimDTO getPraticaPlanimetria(BigDecimal idUiu);
	
	public C340PratRettificaSupDTO getPraticaSuperficie(BigDecimal idUiu);
	
	public void creaAllegato(C340PratAllegato all);
	
	public void cancellaAllegato(Long idAll);
	
	public void cancellaListaAllegatiPratica(Long idPra);
	
	public List<C340PratAllegato> getListaAllegatiPratica(Long idPra);
	
	public C340PratAllegato getAllegato(Long idAll);
}
