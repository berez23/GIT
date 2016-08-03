package it.webred.ct.data.access.basic.anagrafe.dao;
 
import it.webred.ct.data.access.basic.anagrafe.AnagGenQueryBuilder;
import it.webred.ct.data.access.basic.anagrafe.AnagrafeException;
import it.webred.ct.data.access.basic.anagrafe.dto.AnagraficaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.ComponenteFamigliaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.IndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaIndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaLuogoDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaSoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.SoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.AttrPersonaDTO;
import it.webred.ct.data.access.basic.catasto.CatastoServiceException;
import it.webred.ct.data.access.basic.catasto.dao.CatastoBaseDAO;
import it.webred.ct.data.access.basic.catasto.dao.CatastoQueryBuilder;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaSoggettoDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.SitTIciOggettoDTO;
import it.webred.ct.data.model.anagrafe.SitComune;
import it.webred.ct.data.model.anagrafe.SitDCivicoV;
import it.webred.ct.data.model.anagrafe.SitDPersFam;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.anagrafe.SitDPersonaCivico;
import it.webred.ct.data.model.anagrafe.SitDVia;
import it.webred.ct.data.model.anagrafe.SitProvincia;
import it.webred.ct.data.model.anagrafe.SitStato;
import it.webred.ct.data.model.catasto.Siticomu;
import it.webred.ct.data.model.catasto.SiticonduzImmAll;
import it.webred.ct.data.model.ici.SitTIciOggetto;
import it.webred.ct.data.model.ici.SitTIciSogg;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
  
public class AnagrafeJPAImpl extends AnagrafeBaseDAO implements AnagrafeDAO  {
	
	@Override
	public SitDPersona getPersonaById(RicercaSoggettoAnagrafeDTO rs) {
		SitDPersona sogg = null;
		logger.debug("AnagrafeJPAImpl.getPersonaByI: " + rs.getIdVarSogg());
		try{
			Query q = manager_diogene.createNamedQuery("SitDPersona.getSoggById");
			q.setParameter("id", rs.getIdVarSogg());
			sogg = (SitDPersona)q.getSingleResult();
		}catch (NoResultException nre) {
			logger.warn("AnagrafeJPAImpl.getSoggettoById - No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return sogg;
	}
	
	@Override
	public List<SitDPersona> getListaPersoneByCF(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException{
		List<SitDPersona> listaPersone=null;
		String codFis = rs.getCodFis();
		logger.debug("AnagrafeJPAImpl.getListaPersoneByCF()" +	"[CODFISC: "+codFis+"]");
		try {
			Query q = manager_diogene.createNamedQuery("SitDPersona.getListaSoggByCF");
			q.setParameter("codFisc", codFis);
			listaPersone =(List<SitDPersona>) q.getResultList();
			logger.warn("TROVATO. N.ELE: "+ listaPersone.size());
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return listaPersone;
		
	}

	@Override
	public List<SitDPersona> getListaPersoneByCFAllaData(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException{
		List<SitDPersona> listaPersone=null;
		logger.debug("AnagrafeJPAImpl.getListaPersoneByCFAllaData()" +	"[CODFISC: "+rs.getCodFis()+"];[DATA:" + rs.getDtRif());
		try {
			Query q = manager_diogene.createNamedQuery("SitDPersona.getListaSoggByCFAllaData");
			q.setParameter("codFisc", rs.getCodFis());
			q.setParameter("dtVal", rs.getDtRif());
			listaPersone =(List<SitDPersona>) q.getResultList();
			logger.warn("TROVATO. N.ELE: "+ listaPersone.size());
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return listaPersone;
	}

	@Override
	public List<SitDPersona> getListaPersoneByDatiAna(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException {
		List<SitDPersona> listaPersone=null;
		logger.debug("AnagrafeJPAImpl.getListaPersoneByDatiAna " +"[COGNOME: "+rs.getCognome()+"];" +
				"[NOME: "+rs.getNome()+"];" +"[DATA_NASCITA: "+rs.getDtNas()+"];[ID_EXT_COMUNE_NASCITA: "+rs.getCodComNas()+"]");
		try {
			Query q =null;
			if (rs.getCodComNas() != null && rs.getCodComNas().equals("")) 	
				q= manager_diogene.createNamedQuery("SitDPersona.getListaSoggByDatiAnaCompleti");
			else
				q=manager_diogene.createNamedQuery("SitDPersona.getListaSoggByDatiAna");
			q.setParameter("cognome", rs.getCognome());
			q.setParameter("nome", rs.getNome());
			q.setParameter("dtNas", rs.getDtNas());
			if (rs.getCodComNas() != null && rs.getCodComNas().equals("")) 	 
				q.setParameter("idExtComNas", rs.getCodComNas());
			listaPersone =(List<SitDPersona>) q.getResultList();
			logger.warn("TROVATO. N.ELE: "+ listaPersone.size());
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return listaPersone;
	}

	@Override
	public List<SitDPersona> getListaPersoneByDatiAnaAllaData(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException{
		List<SitDPersona> listaPersone=null;
		logger.debug("AnagrafeJPAImpl.getListaPersoneByDatiAnaAllaData " +
				"[COGNOME: "+rs.getCognome()+"];" +	"[NOME: "+rs.getNome()+"];" +"[DATA_NASCITA: "+rs.getDtNas()+
				"];[ID_EXT_COMUNE_NASCITA: "+rs.getCodComNas()+"];[DATA: "+rs.getDtRif()+"];");
		try {
			Query q =null;
			if (rs.getCodComNas() != null && rs.getCodComNas().equals("")) 	
				q= manager_diogene.createNamedQuery("SitDPersona.getListaSoggByDatiAnaCompletiAllaData");
			else
				q=manager_diogene.createNamedQuery("SitDPersona.getListaSoggByDatiAnaAllaData");
			q.setParameter("cognome", rs.getCognome());
			q.setParameter("nome", rs.getNome());
			q.setParameter("dtNas", rs.getDtNas());
			if (rs.getCodComNas() != null && rs.getCodComNas().equals("")) 	 
				q.setParameter("idExtComNas", rs.getCodComNas());
			q.setParameter("dtVal", rs.getDtRif());
			listaPersone =(List<SitDPersona>) q.getResultList();
			logger.warn("TROVATO. N.ELE: "+ listaPersone.size());
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return listaPersone;
	}
	@Override
	public List<SitDPersona> getVariazioniPersonaByIdExt(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException{
		List<SitDPersona> listaPersone=null;
		String idExt = rs.getIdSogg();
		logger.debug("DATI SIT_D_PERSONA PER ID_EXT" +
				"[ID_EXT: "+idExt+"]");
		try {
			Query q = manager_diogene.createNamedQuery("SitDPersona.getListaSoggByIdExt");
			q.setParameter("idExt", idExt);
			listaPersone =(List<SitDPersona>) q.getResultList();
			logger.warn("TROVATO. N.ELE: "+ listaPersone.size());
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}	
		return listaPersone;
	}
	
	@Override
	public List<SitDPersonaCivico> getListaPersCivByIdExt(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException{
		List<SitDPersonaCivico> listaPersCiv=null;
		String idExt = rs.getIdSogg();
		logger.debug("DATI SIT_D_PERSONA_CIVICO PER ID_EXT" +
				"[ID_EXT: "+idExt+"]");
		try {
			Query q = manager_diogene.createNamedQuery("SitDPersonaCivico.getListaCivByIdPersona");
			q.setParameter("idExtDPersona", idExt);
			listaPersCiv =(List<SitDPersonaCivico>) q.getResultList();
			logger.warn("TROVATO. N.ELE: "+ listaPersCiv.size());
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}	
		return listaPersCiv;
	}

	@Override
	public List<SitDCivicoV> getListaCiviciByIdExt(RicercaIndirizzoAnagrafeDTO ri) throws AnagrafeException {
		List<SitDCivicoV> listaCiv=null;
		String idExt = ri.getSitDCivicoIdExt();
		logger.debug("DATI SIT_D_CIVICO_v PER ID_EXT" +
				"[ID_EXT: "+idExt+"]");
		try {
			Query q = manager_diogene.createNamedQuery("SitDCivicoV.getListaCivByIdExt");
			q.setParameter("idExt", idExt);
			listaCiv =(List<SitDCivicoV>) q.getResultList();
			logger.warn("TROVATO. N.ELE: "+ listaCiv.size());
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}	
		return listaCiv;

	}

	@Override
	public List<SitDVia> getListaVieByIdExt(RicercaIndirizzoAnagrafeDTO ri) throws AnagrafeException{
		List<SitDVia> listaVie=null;
		String idExt = ri.getSitDViaIdExt();
		logger.debug("DATI SIT_D_VIA PER ID_EXT" +
				"[ID_EXT: "+idExt+"]");
		try {
			Query q = manager_diogene.createNamedQuery("SitDVia.getListaVieByIdExt");
			q.setParameter("idExt", idExt);
			listaVie =(List<SitDVia>) q.getResultList();
			logger.warn("TROVATO. N.ELE: "+ listaVie.size());
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}	
		return listaVie;
	}

	@Override
	public List<SoggettoAnagrafeDTO> searchSoggetto(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException{
		List<SoggettoAnagrafeDTO> lista=null;
		try {
			String sql = (new AnagGenQueryBuilder(rs)).createQuery(false);
			logger.debug("searchSoggetto. SQL: " + sql);
			Query q = manager_diogene.createNativeQuery(sql);
			List<Object[]> result = q.getResultList();
			logger.debug("Result size ["+result.size()+"]");
			lista =new ArrayList<SoggettoAnagrafeDTO>();
			for (Object[] ele : result) {
				SoggettoAnagrafeDTO sogg = new SoggettoAnagrafeDTO(); 
				if (ele[0] != null)
					sogg.setCognome(ele[0].toString().trim());
				if (ele[1] != null)
					sogg.setNome(ele[1].toString().trim());
				if (ele[2] != null)
				sogg.setDtNas((Date)ele[2]);
				if (ele[3] != null)
					sogg.setCodFis(ele[3].toString().trim());
				if (ele[4] != null)
					sogg.setIdExtComNas(ele[4].toString().trim());
				if (ele[5] != null)
					sogg.setDesComNas(ele[5].toString().trim());
				lista.add(sogg);
			}
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return lista;
	}

	@Override
	public SitDPersFam getPersFamByIdExtDPersona(RicercaSoggettoAnagrafeDTO rs)	throws AnagrafeException {
		SitDPersFam sogg = null;
		logger.debug("AnagrafeJPAImpl.getPersFamByIdExtDPersona: ID_EXT: " + rs.getIdSogg() + " ; dt_rif: " + rs.getDtRif());
		try{
			Query q = manager_diogene.createNamedQuery("SitDPersFam.getPersFamByIdExtDPersonaAllaData");
			q.setParameter("idExtDPersona", rs.getIdSogg());
			java.util.Date dtRif =null;
			// se la data è null, si cercano le righe valide alla data di sistema
			if (rs.getDtRif()==null)
				dtRif = new java.util.Date();
			else
				dtRif= rs.getDtRif();
			q.setParameter("dtRif", dtRif);
			sogg = (SitDPersFam)q.getSingleResult();
			}catch (NoResultException nre) {
			logger.warn("AnagrafeJPAImpl.getPersFamByIdExtDPersona - No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return sogg;
	}

	@Override
	public List<ComponenteFamigliaDTO> getListaCompFamiglia(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException {
		List<ComponenteFamigliaDTO> listaPersone=null;
		logger.debug("AnagrafeJPAImpl.getListaCompFamiglia" +	"[ID_EXT_D_FAMIGLIA "+rs.getIdExtDFamiglia()+"];[DT_RIF: " + rs.getDtRif());
		try {
			Query q = manager_diogene.createNamedQuery("SitDPersona_SitDPersFam.getPersonaRelFamByIdExtDPersonaAllaData");
			q.setParameter("idExtDFamiglia", rs.getIdExtDFamiglia());
			java.util.Date dtRif =rs.getDtRif();
			// se la data è null, si cercano le righe valide alla data di sistema
			if (dtRif==null)
				dtRif = new java.util.Date();
			q.setParameter("dtRif", dtRif);
			List<Object[]> result = q.getResultList();
			logger.warn("TROVATO. N.ELE: "+ result.size());
			listaPersone=new ArrayList<ComponenteFamigliaDTO>();
			for (Object[] ele : result) {
				ComponenteFamigliaDTO comp= new ComponenteFamigliaDTO(); 
				if (ele[0] != null)
					comp.setPersona((SitDPersona)ele[0]);
				if (ele[1] != null)
					comp.setRelazPar(ele[1].toString().trim());
				listaPersone.add(comp);
			}
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return listaPersone;
	}

	@Override
	public SitComune getComune(RicercaLuogoDTO rl) throws AnagrafeException {
		SitComune comune = null;
		logger.debug("AnagrafeJPAImpl.getComune: ID_EXT_COMUNE: " + rl.getIdExtComune()+ " ; dt_rif: " + rl.getDtRif());
		try{
			Query q = null;
			if (rl.getDtRif()!=null) {
				q= manager_diogene.createNamedQuery("SitComune.getComuneAllaData");
				q.setParameter("dtRif", rl.getDtRif());
			}else
				q= manager_diogene.createNamedQuery("SitComune.getComuneAttuale");
			q.setParameter("idExt", rl.getIdExtComune());
			comune = (SitComune)q.getSingleResult();
		}catch (NoResultException nre) {
			logger.warn("AnagrafeJPAImpl.AnagrafeJPAImpl.getComune - No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return comune;
	}

	@Override
	public SitProvincia getProvincia(RicercaLuogoDTO rl)			throws AnagrafeException {
		SitProvincia provincia= null;
		logger.debug("AnagrafeJPAImpl.getProvincia: ID_EXT_PROVINCIA: " + rl.getIdExtProvincia()+ " ; dt_rif: " + rl.getDtRif());
		try{
			Query q = null;
			if (rl.getDtRif()!=null) {
				q= manager_diogene.createNamedQuery("SitProvincia.getProvinciaAllaData");
				q.setParameter("dtRif", rl.getDtRif());
			}else
				q= manager_diogene.createNamedQuery("SitProvincia.getProvinciaAttuale");
			q.setParameter("idExt", rl.getIdExtProvincia());
			provincia = (SitProvincia)q.getSingleResult();
		}catch (NoResultException nre) {
			logger.warn("AnagrafeJPAImpl.AnagrafeJPAImpl.getProvincia - No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return provincia;
	}
	@Override
	public List<ComponenteFamigliaDTO> getResidentiAlCivico(RicercaIndirizzoAnagrafeDTO ri) throws AnagrafeException {
		List<ComponenteFamigliaDTO> listaPersone=null;
		logger.debug("AnagrafeJPAImpl.getResidentiAlCivico" +	"[SIT_D_CIVICO.ID "+ ri.getSitDCivicoId());
		try {
			Query q = manager_diogene.createNamedQuery("Demografia.getResidentiAlCivico");
			
			q.setParameter("idCivico", ri.getSitDCivicoId());
			java.util.Date dtRif = new java.util.Date();
			q.setParameter("dtRif", dtRif);
			List<Object[]> result = q.getResultList();
			logger.warn("TROVATO. N.ELE: "+ result.size());
			listaPersone=new ArrayList<ComponenteFamigliaDTO>();
			for (Object[] ele : result) {
				ComponenteFamigliaDTO comp= new ComponenteFamigliaDTO(); 
				if (ele[0] != null)
					comp.setRelazPar(ele[0].toString().trim());
				else
					comp.setRelazPar("");
				if (ele[1] != null)
					comp.setIdExtDFamiglia(ele[1].toString().trim());
				else
					comp.setIdExtDFamiglia("");
				if (ele[2] != null)
					comp.setIdOrigFamiglia(ele[2].toString().trim());
				else
					comp.setIdOrigFamiglia("");
				if (ele[3] != null)
					comp.setPersona((SitDPersona)ele[3]);
				listaPersone.add(comp);
			}
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return listaPersone;
	}

	@Override
	public IndirizzoAnagrafeDTO getIndirizzo(RicercaIndirizzoAnagrafeDTO ri)	throws AnagrafeException {
		IndirizzoAnagrafeDTO ind = new IndirizzoAnagrafeDTO();
		
		logger.debug("AnagrafeJPAImpl.getIndirizzo" +	"[SIT_D_CIVICO.ID "+ ri.getSitDCivicoId());
		try {
			Query q = manager_diogene.createNamedQuery("SitDVia_SitDCivico.getIndirizzoByIdCiv");
			q.setParameter("idCivico", ri.getSitDCivicoId());
			java.util.Date dtRif = new java.util.Date();
			q.setParameter("dtRif", dtRif);
			List<Object[]> result = q.getResultList();
			logger.warn("TROVATO. N.ELE: "+ result.size());
			Object[] ele = result.get(0);
			SitDVia via =(SitDVia) ele[0];
			String civLiv1=""; String civLiv2=""; String civLiv3="";
			if(ele[1]!=null)
				civLiv1 = ele[1].toString().trim();
			else
				civLiv1 ="";
			if(ele[2]!=null)
				civLiv2 = ele[2].toString().trim();
			else
				civLiv2 ="";
			if(ele[3]!=null)
			   civLiv3 = ele[3].toString().trim();
			else
			   civLiv3 = "";
			ind.valIndirizzo(via);
			ind.setCivico(civLiv1);
			ind.setCivicoLiv2(civLiv2);
			ind.setCivicoLiv3(civLiv3);
			
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return ind;
		
	}
	
	@Override
	public List<AnagraficaDTO> getAnagrafeByCF(RicercaSoggettoDTO rs) throws AnagrafeException{
		List<AnagraficaDTO> anagraficaList = new ArrayList<AnagraficaDTO>();
		logger.debug("AnagrafeJPAImpl.getAnagrafeByCF" +	"[CODFISC: "+rs.getCodFis());
		try {
			
			Query q = manager_diogene.createNamedQuery("SitDPersona.getAnagrafeByCF");
			q.setParameter("codFisc", rs.getCodFis());
			List<Object> lista = q.getResultList();
			for(Object ana: lista){
				
				AnagraficaDTO anagrafica = new AnagraficaDTO();
				anagrafica.setPersona( (SitDPersona) ana);
				
				if(ana != null){
					Query q2 = manager_diogene.createNamedQuery("SitDPersona.getInfoAggiuntive");
					q2.setParameter("id", anagrafica.getPersona().getId());
					List<Object[]> listaInfoAgg = q2.getResultList();
					if(listaInfoAgg.size() > 0){
						Object[] infoAgg = listaInfoAgg.get(0);
 						anagrafica.setCivico( (SitDCivicoV) (infoAgg[0] != null? infoAgg[0]: new SitDCivicoV()));
						anagrafica.setVia( (SitDVia) (infoAgg[1] != null? infoAgg[1]: new SitDVia()));
						anagrafica.setComuneNascita((SitComune) (infoAgg[2] != null? infoAgg[2]: new SitComune()));
						anagrafica.setComuneResidenza((SitComune) (infoAgg[3] != null? infoAgg[3]: new SitComune()));
						anagrafica.setProvNascita((SitProvincia) (infoAgg[4] != null? infoAgg[4]: new SitProvincia()));
						anagrafica.setProvResidenza((SitProvincia) (infoAgg[5] != null? infoAgg[5]: new SitProvincia()));
						
					}
				}
				
				anagraficaList.add(anagrafica);
			}

		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return anagraficaList;
	}
	
	@Override
	public AnagraficaDTO getPersonaFamigliaByCF(RicercaSoggettoAnagrafeDTO rs) {
		AnagraficaDTO anagrafica = new AnagraficaDTO();
		logger.debug("AnagrafeJPAImpl.getPersonaFamigliaByCF: " + rs.getIdVarSogg());
		try{
			Query q = manager_diogene.createNamedQuery("SitDPersona.getPersonaFamigliaByCF");
			q.setParameter("idPersona", rs.getIdVarSogg());
			q.setParameter("codFisc", rs.getCodFis());
			List<Object> lista = q.getResultList();
			if(lista.size() > 0){
				Object ana = lista.get(0);
				anagrafica.setPersona( (SitDPersona) ana);

				if(ana != null){
					Query q2 = manager_diogene.createNamedQuery("SitDPersona.getInfoAggiuntive");
					q2.setParameter("id", anagrafica.getPersona().getId());
					List<Object[]> listaInfoAgg = q2.getResultList();
					if(listaInfoAgg.size() > 0){
						Object[] infoAgg = listaInfoAgg.get(0);
 						anagrafica.setCivico( (SitDCivicoV) (infoAgg[0] != null? infoAgg[0]: new SitDCivicoV()));
						anagrafica.setVia( (SitDVia) (infoAgg[1] != null? infoAgg[1]: new SitDVia()));
						anagrafica.setComuneNascita((SitComune) (infoAgg[2] != null? infoAgg[2]: new SitComune()));
						anagrafica.setComuneResidenza((SitComune) (infoAgg[3] != null? infoAgg[3]: new SitComune()));
						anagrafica.setProvNascita((SitProvincia) (infoAgg[4] != null? infoAgg[4]: new SitProvincia()));
						anagrafica.setProvResidenza((SitProvincia) (infoAgg[5] != null? infoAgg[5]: new SitProvincia()));
					}
				}
			
				return anagrafica;
			}
			
		}catch (NoResultException nre) {
			logger.warn("AnagrafeJPAImpl.getPersonaFamigliaByCF - No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return anagrafica;
	}
	
	
	public boolean verificaNazionalitaByCF(String descr, String codFisc){
		boolean nazionalita = false;
		
		logger.debug("SitStato.verificaNazionalitaByCF: " + descr);
		try{
			Query q = manager_diogene.createNamedQuery("SitStato.verificaNazionalitaByCFAllaData");
			q.setParameter("descr", descr);
			q.setParameter("codFisc", codFisc);
			List<SitStato> result = q.getResultList();
			if(result.size()> 0)
				nazionalita = true;
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return nazionalita;
		
	}
	
	public boolean verificaResidenzaByCFAllaData(RicercaSoggettoAnagrafeDTO rs){
		boolean residenza = false;
		
		logger.debug("SitDPersona.verificaResidenzaByCFAllaData: " + rs.getCodFis()+", "+rs.getDtRif());
		try{
			Query q = manager_diogene.createNamedQuery("SitDPersona.verificaResidenzaByCFAllaData");
			q.setParameter("codFisc", rs.getCodFis());
			q.setParameter("dtRif", rs.getDtRif());
			List<Object> result = q.getResultList();
			if(result.size()> 0)
				residenza = true;
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new AnagrafeException(t);
		}
		return residenza;
		
	}
	
	@Override
	public AttrPersonaDTO getAttributiPersonaByCF(RicercaSoggettoAnagrafeDTO rs){
		AttrPersonaDTO tipo= new AttrPersonaDTO();
		
		java.util.Date dtRef = rs.getDtRif();
		if(dtRef == null){
			dtRef = new java.util.Date();
			rs.setDtRif(dtRef);
		}
		
		//Verifica Nazionalità Italiana ("I")
		tipo.setItaliano(this.verificaNazionalitaByCF("I", rs.getCodFis()));
		
		//Verifica Residenza nel Comune alla Data
		tipo.setResidente(this.verificaResidenzaByCFAllaData(rs));
		
		List<SitDPersona> persone = this.getListaPersoneByCFAllaData(rs);
		
		Boolean defunto = false;
		for(SitDPersona p: persone){
			//Verifica Defunto
			defunto = defunto || (p.getDataMor()!=null);
			
		}
		tipo.setDefunto(defunto);
		
		return tipo;
	}

}
