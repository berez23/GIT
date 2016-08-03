package it.webred.ct.service.comma340.data.access.dao;

import it.webred.ct.service.comma340.data.access.dto.C340PratAllegatoDTO;
import it.webred.ct.service.comma340.data.access.dto.C340PratDepositoPlanimDTO;
import it.webred.ct.service.comma340.data.access.dto.C340PratRettificaSupDTO;
import it.webred.ct.service.comma340.data.access.dto.RicercaGestionePraticheDTO;
import it.webred.ct.service.comma340.data.model.ente.CodiceBelfiore;
import it.webred.ct.service.comma340.data.model.pratica.C340PratAllegato;

import java.util.List;

public interface C340DAO {
	
	public List<CodiceBelfiore> getListaComuni(RicercaGestionePraticheDTO ro);
	
	public void creaPraticaDepositoPlanimetria(C340PratDepositoPlanimDTO dto);
	
	public void creaPraticaRettificaSuperficie(C340PratRettificaSupDTO dto);
	
	public C340PratDepositoPlanimDTO modificaPraticaDepositoPlanimetria(C340PratDepositoPlanimDTO dto);
	
	public C340PratRettificaSupDTO modificaPraticaRettificaSuperficie(C340PratRettificaSupDTO dto);
	
	public void cancellaPraticaDepositoPlanimetria(RicercaGestionePraticheDTO ro);
	
	public void cancellaPraticaRettificaSuperficie(RicercaGestionePraticheDTO ro);
	
	public C340PratDepositoPlanimDTO getPraticaPlanimetria(RicercaGestionePraticheDTO ro);
	
	public C340PratRettificaSupDTO getPraticaSuperficie(RicercaGestionePraticheDTO ro);
	
	public void creaAllegato(C340PratAllegatoDTO dto);
	
	public void cancellaAllegato(RicercaGestionePraticheDTO ro);
	
	public void cancellaListaAllegatiPratica(RicercaGestionePraticheDTO ro);
	
	public C340PratAllegato getAllegato(RicercaGestionePraticheDTO ro);
	
	public List<C340PratAllegato> getListaAllegatiPratica(RicercaGestionePraticheDTO ro);

}
