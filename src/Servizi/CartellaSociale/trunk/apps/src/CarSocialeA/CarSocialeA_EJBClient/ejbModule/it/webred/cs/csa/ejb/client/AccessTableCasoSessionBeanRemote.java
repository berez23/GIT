package it.webred.cs.csa.ejb.client;

import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsACasoOpeTipoOpe;
import it.webred.cs.data.model.CsOOperatoreTipoOperatore;

import java.util.List;

import javax.ejb.Remote;

/**
 * @author Andrea
 *
 */
@Remote
public interface AccessTableCasoSessionBeanRemote {

	public CsACaso newCaso(IterDTO dto) throws Exception;

	public List<CsACaso> findAll(IterDTO dto);

	public CsACaso findCasoById(IterDTO dto) throws Exception;
	
	public void setDataModifica(IterDTO dto) throws Exception;

	public void salvaOperatoreCaso(BaseDTO dto) throws Exception;
	
	public void updateOperatoreCaso(BaseDTO dto) throws Exception;

	public void eliminaOperatoreTipoOpByCasoId(BaseDTO dto);

	public List<CsACasoOpeTipoOpe> getListaOperatoreTipoOpByCasoId(BaseDTO dto);
	
	public CsACasoOpeTipoOpe findResponsabile(BaseDTO dto) throws Exception;

	public CsOOperatoreTipoOperatore findOperatoreTipoOperatoreByOpSettore(BaseDTO dto)
			throws Exception;

	public CsACasoOpeTipoOpe findCasoOpeTipoOpeByOpSettore(BaseDTO dto)
			throws Exception;

	public Integer countCasiByResponsabileCatSociale(BaseDTO dto) throws Exception;

	public void updateCaso(BaseDTO dto) throws Exception;

}