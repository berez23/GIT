package it.webred.cs.csa.ejb.ejb;

import it.webred.cs.csa.ejb.CarSocialeBaseSessionBean;
import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableConfigurazioneSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableDiarioSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableInterventoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableOperatoreSessionBeanRemote;
import it.webred.cs.csa.ejb.dao.InterventoDAO;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.InterventoDTO;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsCTipoIntervento;
import it.webred.cs.data.model.CsDDiario;
import it.webred.cs.data.model.CsFlgIntervento;
import it.webred.cs.data.model.CsIIntervento;
import it.webred.cs.data.model.CsOSettore;
import it.webred.cs.data.model.CsRelRelazioneTipoint;
import it.webred.cs.data.model.CsRelSettCsocTipoInter;
import it.webred.cs.data.model.CsRelSettCsocTipoInterEr;
import it.webred.cs.data.model.CsRelSettCsocTipoInterErPK;
import it.webred.cs.data.model.CsRelSettCsocTipoInterPK;
import it.webred.cs.data.model.CsTbTipoDiario;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

@Stateless
public class AccessTableInterventoSessionBean extends CarSocialeBaseSessionBean implements AccessTableInterventoSessionBeanRemote  {
	
	@Autowired
	private InterventoDAO interventoDao;
	
	@EJB
	public AccessTableOperatoreSessionBeanRemote operatoreSessionBean;
	@EJB
	public AccessTableDiarioSessionBeanRemote diarioSessionBean;
	@EJB
	public AccessTableCasoSessionBeanRemote casoSessionBean;
	@EJB
	public AccessTableConfigurazioneSessionBeanRemote confSessionBean;
	
	
	@Override
	public CsIIntervento salvaIntervento(BaseDTO dto) throws Exception {
		
		CsIIntervento inter = (CsIIntervento)dto.getObj();
		
		if(inter.getId()==null){
			inter.setUserIns(dto.getUserId());
			inter.setDtIns(new Date());
			inter.setUsrMod(null);
			inter.setDtMod(null);
			
			if(inter.getCsIContrEco()!=null)
				inter.getCsIContrEco().setCsIIntervento(inter);
				
			if(inter.getCsICentrod()!=null)
				inter.getCsICentrod().setCsIIntervento(inter);
			
			if(inter.getCsIPasti()!=null)
				inter.getCsIPasti().setCsIIntervento(inter);
			
			if(inter.getCsIBuonoSoc()!=null)
				inter.getCsIBuonoSoc().setCsIIntervento(inter);
			
			if(inter.getCsIResiMinore()!=null)
				inter.getCsIResiMinore().setCsIIntervento(inter);
					
			if(inter.getCsIVouchersad()!=null)
				inter.getCsIVouchersad().setCsIIntervento(inter);
			
			if(inter.getCsIResiAdulti()!=null)
				inter.getCsIResiAdulti().setCsIIntervento(inter);
			
			if(inter.getCsIAdmAdh()!=null)
				inter.getCsIAdmAdh().setCsIIntervento(inter);
			
			if(inter.getCsIAffidoFam()!=null)
				inter.getCsIAffidoFam().setCsIIntervento(inter);
			
			if(inter.getCsISemiResiMin()!=null)
				inter.getCsISemiResiMin().setCsIIntervento(inter);
			
			if(inter.getCsISchedaLavoro()!=null)
				inter.getCsISchedaLavoro().setCsIIntervento(inter);
			
			interventoDao.saveIntervento(inter);
		}else{
			inter.setUsrMod(dto.getUserId());
			inter.setDtMod(new Date());
			interventoDao.updateIntervento(inter);
		}
		return inter;
	}
	
	@Override
	public CsFlgIntervento salvaFoglioAmministrativo(InterventoDTO dto) throws Exception {
		
		CsFlgIntervento inter = (CsFlgIntervento)dto.getFoglio();
		
		BaseDTO bdto = new BaseDTO();
		bdto.setEnteId(dto.getEnteId());
		bdto.setUserId(dto.getUserId());
		
		if(inter.getDiarioId()==null){
			inter.setUserIns(dto.getUserId());
			inter.setDtIns(new Date());
			inter.setUsrMod(null);
			inter.setDtMod(null);
			
			if(inter.getCsDDiario()==null){
				IterDTO iter = new IterDTO();
				
				iter.setIdCaso(dto.getCasoId());
				iter.setUserId(dto.getUserId());
				iter.setEnteId(dto.getEnteId());
				CsACaso caso = casoSessionBean.findCasoById(iter);
				
				CsDDiario diario = new CsDDiario();
				diario.setCsACaso(caso);
				
				List<CsTbTipoDiario> lstTipi = confSessionBean.getTipiDiario(bdto);
				for(CsTbTipoDiario tipo : lstTipi){
					String descrizione = tipo.getDescrizione();
					if(descrizione.equals("Foglio Amministrativo"))
						diario.setCsTbTipoDiario(tipo);
				}
				
				//Recupero il diario associato alla relazione da inserire
				if(dto.getIdRelazione()!=null){
					bdto.setObj(dto.getIdRelazione());
					CsDDiario relazione =  diarioSessionBean.findDiarioById(bdto);
					diario.addCsDDiariFiglio(relazione);
				}
				
				bdto.setObj(diario);
				diario = diarioSessionBean.createDiario(bdto);
				inter.setDiarioId(diario.getId());
				inter.setCsDDiario(diario);
			}
			
			interventoDao.saveFglIntervento(inter);
			
		}else{
			
			List<CsDDiario> lstd = inter.getCsDDiario().getCsDDiariFiglio();
			CsDDiario relazione = null;
			if(lstd!=null && lstd.size()>0){
				for(CsDDiario d:lstd){
					//La rimuovo e riaggiungo
					if(d.getCsDRelazione()!=null){
						relazione = d;
					}
				}
				if(relazione!=null)
					inter.getCsDDiario().getCsDDiariFiglio().remove(relazione);
			}
			
			//Recupero il diario associato alla relazione da inserire
			if(dto.getIdRelazione()!=null){
				bdto.setObj(dto.getIdRelazione());
				CsDDiario nrelazione =  diarioSessionBean.findDiarioById(bdto);
				inter.getCsDDiario().addCsDDiariFiglio(nrelazione);
			}
			
			bdto.setObj(inter.getCsDDiario());
			CsDDiario diario = diarioSessionBean.updateDiario(bdto);
			
			inter.setDiarioId(diario.getId());
			inter.setCsDDiario(diario);
			
			inter.setUsrMod(dto.getUserId());
			inter.setDtMod(new Date());
			interventoDao.updateFglIntervento(inter);
			
		}
		return inter;
	}

	@Override
	public List<CsCTipoIntervento> findAllTipiIntervento(BaseDTO dto) {
		return interventoDao.findAllTipiIntervento();
	}
	
	@Override
	public List<CsCTipoIntervento> findTipiInterventoCatSoc(BaseDTO dto){
		return interventoDao.findTipiInterventoCatSoc((Long) dto.getObj(), new Date());	
	}
	
	@Override
	public List<CsCTipoIntervento> findTipiInterventoSettoreCatSoc(InterventoDTO dto){
		return interventoDao.findTipiInterventoSettoreCatsoc(dto.getIdSettore(),dto.getIdCatsoc());	
	}

	@Override
	public CsIIntervento getInterventoById(BaseDTO dto) throws Exception { 
		return interventoDao.getInterventoById((Long)dto.getObj());
	}
	
	@Override
	public CsCTipoIntervento getTipoInterventoById(BaseDTO dto) throws Exception{
		return interventoDao.getTipoInterventoById(Long.parseLong((String)dto.getObj()));
	}

	@Override
	public CsFlgIntervento getFoglioInterventoById(BaseDTO dto) throws Exception {
		return interventoDao.getFoglioInterventoById((Long)dto.getObj());
	}

	@Override
	public List<CsIIntervento> getListaInterventiByCaso(BaseDTO dto)
			throws Exception {
		return interventoDao.getListaInterventiByIdCaso((Long)dto.getObj());
	}

	@Override
	public CsRelSettCsocTipoInter findRelSettCsocTipoInterById(BaseDTO bdto) {
		return interventoDao.findRelSettCsocTipoInterById((CsRelSettCsocTipoInterPK) bdto.getObj());
	}

	@Override
	public CsRelSettCsocTipoInterEr findRelSettCsocTipoInterErById(BaseDTO bdto) {
		return interventoDao.findRelSettCsocTipoInterErById((CsRelSettCsocTipoInterErPK) bdto.getObj());
	}
	
	@Override
	public List<CsOSettore> getListaSettori(BaseDTO b) {
		return interventoDao.getListaSettori();
	}
	
	@Override
	public List<CsOSettore> getListaSettoriEr(InterventoDTO idto) {
		return interventoDao.getListaSettoriEr(idto.getIdCatsoc(), idto.getIdTipoIntervento());
	}

	@Override
	public void deleteFoglioAmministrativo(BaseDTO b) {
		interventoDao.deleteFoglioAmministrativo((Long)b.getObj());
	}
	
	@Override
	public void deleteIntervento(BaseDTO b) throws Exception {
		interventoDao.deleteIntervento((Long)b.getObj());
	}
	
	@Override
	public void saveRelRelazioneTipoint(BaseDTO b) {
		interventoDao.saveRelRelazioneTipoint((CsRelRelazioneTipoint) b.getObj());
	}

	@Override
	public void deleteRelRelazioneTipointByIdRelazione(BaseDTO b) {
		interventoDao.deleteRelRelazioneTipointByIdRelazione((Long) b.getObj());
	}

}
