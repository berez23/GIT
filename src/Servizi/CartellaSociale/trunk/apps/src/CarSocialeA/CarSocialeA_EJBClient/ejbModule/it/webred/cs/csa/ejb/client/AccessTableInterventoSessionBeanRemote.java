package it.webred.cs.csa.ejb.client;

import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.InterventoDTO;
import it.webred.cs.data.model.CsCTipoIntervento;
import it.webred.cs.data.model.CsFlgIntervento;
import it.webred.cs.data.model.CsIIntervento;
import it.webred.cs.data.model.CsOSettore;
import it.webred.cs.data.model.CsRelSettCsocTipoInter;
import it.webred.cs.data.model.CsRelSettCsocTipoInterEr;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface AccessTableInterventoSessionBeanRemote {

	public List<CsCTipoIntervento> findAllTipiIntervento(BaseDTO dto);
	public List<CsCTipoIntervento> findTipiInterventoCatSoc(BaseDTO dto);
	public List<CsCTipoIntervento> findTipiInterventoSettoreCatSoc(InterventoDTO dto);
	
	public CsIIntervento getInterventoById(BaseDTO dto) throws Exception;
	public CsFlgIntervento getFoglioInterventoById(BaseDTO dto) throws Exception;
	public CsCTipoIntervento getTipoInterventoById(BaseDTO dto) throws Exception;
	
	public CsIIntervento salvaIntervento(BaseDTO dto) throws Exception;
	public CsFlgIntervento salvaFoglioAmministrativo(InterventoDTO dto) throws Exception;
	
	public List<CsIIntervento> getListaInterventiByCaso(BaseDTO dto) throws Exception;
	public CsRelSettCsocTipoInter findRelSettCsocTipoInterById(BaseDTO bdto);
	public CsRelSettCsocTipoInterEr findRelSettCsocTipoInterErById(BaseDTO bdto);
	public List<CsOSettore> getListaSettoriEr(InterventoDTO idto);
	public void deleteFoglioAmministrativo(BaseDTO b);
	public void deleteIntervento(BaseDTO b) throws Exception;
	public void saveRelRelazioneTipoint(BaseDTO b);
	public void deleteRelRelazioneTipointByIdRelazione(BaseDTO b);
	public List<CsOSettore> getListaSettori(BaseDTO b);

}