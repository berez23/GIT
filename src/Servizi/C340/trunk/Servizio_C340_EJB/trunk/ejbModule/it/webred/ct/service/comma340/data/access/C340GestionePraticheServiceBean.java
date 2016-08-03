package it.webred.ct.service.comma340.data.access;

import it.webred.ct.service.comma340.data.access.dao.C340DAO;
import it.webred.ct.service.comma340.data.access.dto.C340PratAllegatoDTO;
import it.webred.ct.service.comma340.data.access.dto.C340PratDepositoPlanimDTO;
import it.webred.ct.service.comma340.data.access.dto.C340PratRettificaSupDTO;
import it.webred.ct.service.comma340.data.access.dto.RicercaGestionePraticheDTO;
import it.webred.ct.service.comma340.data.model.ente.CodiceBelfiore;
import it.webred.ct.service.comma340.data.model.pratica.C340PratAllegato;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Session Bean implementation class C340CommonServiceBean
 */
@Stateless
public class C340GestionePraticheServiceBean extends C340ServiceBaseBean implements C340GestionePraticheService {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private C340DAO c340DAO;
	

	public List<CodiceBelfiore> getListaComuni(RicercaGestionePraticheDTO ro) {		
		return c340DAO.getListaComuni(ro);
	}

	//@Interceptors(AuditStateless.class)
	public void creaPraticaDepositoPlanimetria(C340PratDepositoPlanimDTO dto) {
		c340DAO.creaPraticaDepositoPlanimetria(dto);
	}
	
	//@Interceptors(AuditStateless.class)
	public void creaPraticaRettificaSuperficie(C340PratRettificaSupDTO dto) {
		c340DAO.creaPraticaRettificaSuperficie(dto);
	}

	//@Interceptors(AuditStateless.class)
	public C340PratDepositoPlanimDTO modificaPraticaDepositoPlanimetria(C340PratDepositoPlanimDTO dto) {
		return c340DAO.modificaPraticaDepositoPlanimetria(dto);
	}

	//@Interceptors(AuditStateless.class)
	public C340PratRettificaSupDTO modificaPraticaRettificaSuperficie(C340PratRettificaSupDTO dto) {
		return c340DAO.modificaPraticaRettificaSuperficie(dto);
	}
	
	//@Interceptors(AuditStateless.class)
	public void cancellaPraticaDepositoPlanimetria(RicercaGestionePraticheDTO ro) {		
		c340DAO.cancellaPraticaDepositoPlanimetria(ro);
	}

	//@Interceptors(AuditStateless.class)
	public void cancellaPraticaRettificaSuperficie(RicercaGestionePraticheDTO ro) {		
		c340DAO.cancellaPraticaRettificaSuperficie(ro);
	}
	
	//@Interceptors(AuditStateless.class)
	public C340PratDepositoPlanimDTO getPraticaPlanimetria(RicercaGestionePraticheDTO ro) {		
		return c340DAO.getPraticaPlanimetria(ro);
	}

	//@Interceptors(AuditStateless.class)
	public C340PratRettificaSupDTO getPraticaSuperficie(RicercaGestionePraticheDTO ro) {		
		return c340DAO.getPraticaSuperficie(ro);
	}

	//@Interceptors(AuditStateless.class)
	public void creaAllegato(C340PratAllegatoDTO dto) {		
		c340DAO.creaAllegato(dto);
	}
	
	//@Interceptors(AuditStateless.class)
	public void cancellaAllegato(RicercaGestionePraticheDTO ro) {		
		c340DAO.cancellaAllegato(ro);
	}
	
	//@Interceptors(AuditStateless.class)
	public void cancellaListaAllegatiPratica(RicercaGestionePraticheDTO ro) {
		c340DAO.cancellaListaAllegatiPratica(ro);
	}
	
	//@Interceptors(AuditStateless.class)
	public C340PratAllegato getAllegato(RicercaGestionePraticheDTO ro) {		
		return c340DAO.getAllegato(ro);
	}
	
	public List<C340PratAllegato> getListaAllegatiPratica(RicercaGestionePraticheDTO ro) {		
		return c340DAO.getListaAllegatiPratica(ro);
	}

}