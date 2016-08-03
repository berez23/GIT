package it.webred.fb.dao;

import it.webred.fb.data.model.DmBBene;
import it.webred.fb.data.model.DmBFascicolo;
import it.webred.fb.data.model.DmBIndirizzo;
import it.webred.fb.data.model.DmBInfo;
import it.webred.fb.data.model.DmBMappale;
import it.webred.fb.data.model.DmBTerreno;
import it.webred.fb.data.model.DmBTipoUso;
import it.webred.fb.data.model.DmBTitolo;
import it.webred.fb.data.model.DmConfClassificazione;
import it.webred.fb.data.model.DmDDoc;
import it.webred.fb.ejb.client.FascicoloBeneServiceException;
import it.webred.fb.ejb.dto.EventoDTO;
import it.webred.fb.ejb.dto.IndirizzoDTO;
import it.webred.fb.ejb.dto.KeyValueDTO;
import it.webred.fb.ejb.dto.MappaleDTO;
import it.webred.fb.ejb.dto.RicercaBeneDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

@Named
public class DettaglioBeneDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public static Logger logger = Logger.getLogger("fascicolobene.log");
	
	@PersistenceContext(unitName="FascicoloBene_DataModel")
	protected EntityManager em;
		
	
	public DmBBene getBene(Long id) {
		DmBBene bene = em.find(DmBBene.class, id);
		return bene;
	}
	
	public List<DmBBene> getBeniTitolo(String idTitolo){
		try{
			logger.debug("getBeniTitolo[id:"+idTitolo+"]");
			Query q = em.createNamedQuery("DmBTitolo.getBeniTitoloAttuali");
			q.setParameter("idTitolo", idTitolo);
			return (List<DmBBene>)q.getResultList();
		}catch(Exception e){
			logger.error("getBeniTitolo "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
	}
	
	public List<DmBTitolo> getTitoliBene(Long id){
		try{
			logger.debug("getTitoliBene[id:"+id+"]");
			Query q = em.createNamedQuery("DmBTitolo.getTitoliBeneAttuali");
			q.setParameter("idBene", id);
			return (List<DmBTitolo>)q.getResultList();
		}catch(Exception e){
			logger.error("getTitoliBene "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		
	}
	
	public List<DmBTitolo> getTitoliBeneTree(Long id){
		try{
			logger.debug("getTitoliBeneTree[id:"+id+"]");
			String sql = 
			"select t.* from "+
			"(SELECT  LPAD(' ',2*(LEVEL-1)) || CHIAVE gerarchia,b.id, level "+
			"FROM dM_B_BENE B "+
			"START WITH B.id=:idBene "+
			"CONNECT BY PRIOR ID=DM_B_BENE_PADRE_ID "+
			"order by level)b JOIN DM_B_TITOLO T ON (B.ID=T.DM_B_BENE_ID) "+
			"order by t.id_titolo,b.id";
			
			logger.debug("getTitoliBeneTree - SQL["+sql+"]");
			
			Query q = em.createNativeQuery(sql,DmBTitolo.class);
			q.setParameter("idBene", id);
			return (List<DmBTitolo>)q.getResultList();
		}catch(Exception e){
			logger.error("getTitoliBeneTree "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		
	}
	
	public List<DmDDoc> getDocumentiBene(Long id){
		try{
			logger.debug("getDocumentiBene[id:"+id+"]");
			Query q = em.createNamedQuery("DmDDoc.getDocumentiBene");
			q.setParameter("idBene", id);
			return (List<DmDDoc>)q.getResultList();
		}catch(Exception e){
			logger.error("getDocumentiBene "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		
	}
	
	public List<DmDDoc> getDocumentiBeneByClasse(Long id, DmConfClassificazione cls){
		try{
			logger.debug("getDocumentiBeneByClasse[id:"+id+"]"
					+ "[macro:"+cls.getId().getMacro()+"]"
					+ "[progCat:"+cls.getId().getProgCategoria()+"]");
			Query q = em.createNamedQuery("DmDDoc.getDocumentiBeneByClasse");
			q.setParameter("idBene", id);
			q.setParameter("classificazione", cls);
			return (List<DmDDoc>)q.getResultList();
		}catch(Exception e){
			logger.error("getDocumentiBeneByClasse "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		
	}
	
	public List<DmDDoc> getDocumentiBeneTree(Long id){
		try{
			logger.debug("getDocumentiBeneTree[id:"+id+"]");
			String sql = 
			"select t.* from "+
			"(SELECT  LPAD(' ',2*(LEVEL-1)) || CHIAVE gerarchia, b.id, level "+
			"FROM dM_B_BENE B "+
			"START WITH B.id=:idBene "+
			"CONNECT BY PRIOR ID=DM_B_BENE_PADRE_ID "+
			"order by level)b JOIN DM_D_DOC T ON (B.ID=T.DM_B_BENE_ID AND T.flg_rimosso='0')  "+
			"left join dm_conf_classificazione c on  (T.MACRO_CATEGORIA=C.MACRO and T.PROG_CATEGORIA=C.PROG_CATEGORIA) "+
            "order by C.tipo,b.id,nome_file_base";
			logger.debug("getDocumentiBeneTree - SQL["+id+"]");
			
			Query q = em.createNativeQuery(sql,DmDDoc.class);
			q.setParameter("idBene", id);
			return (List<DmDDoc>)q.getResultList();
		}catch(Exception e){
			logger.error("getDocumentiBeneTree "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<DmBBene> getListaBene(RicercaBeneDTO rb){
		try{
			QueryBuilder qb = new QueryBuilder(rb);
			String sql = qb.createQuery(false);
			logger.debug("getListaBene - SQL["+sql+"]");
			
			Query q = em.createQuery(sql);
			return q.getResultList();
			
		}catch(Exception e){
			logger.error("getListaBene "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<KeyValueDTO> getListaCatInventariale() {
		List<KeyValueDTO> lst = new ArrayList<KeyValueDTO>();
		try{
			logger.debug("getListaCatInventariale");
			Query q = em.createNamedQuery("DmBBeneInv.listaCatInventariale");
			List<Object[]> lista = (List<Object[]>)q.getResultList();
			for(Object[] o:lista){
				KeyValueDTO i = new KeyValueDTO();
				i.setCodice(o[0].toString());
				String via = (String)o[1];
				i.setDescrizione(via.trim());
				lst.add(i);
			}
			
		}catch(Exception e){
			logger.error("getListaCatInventariale "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getListaCiviciVia(BigDecimal codVia){
		try{
			logger.debug("getListaCiviciVia[codVia:"+codVia+"]");
			Query q = em.createNamedQuery("DmBIndirizzo.listaCiviciVia");
			q.setParameter("codVia", codVia);
			return (List<String>)q.getResultList();
		}catch(Exception e){
			logger.error("getListaCiviciVia "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getListaCiviciViaCiv(BigDecimal codVia, String civico){
		try{
			logger.debug("getListaCiviciViaCiv[codVia:"+codVia+"],[civico:"+civico+"]");
			Query q = em.createNamedQuery("DmBIndirizzo.listaCiviciViaCiv");
			q.setParameter("codVia", codVia);
			q.setParameter("civico",civico+"%");
			return (List<String>)q.getResultList();
		}catch(Exception e){
			logger.error("getListaCiviciViaCiv "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<KeyValueDTO> getListaVie(RicercaBeneDTO rb) {
		List<KeyValueDTO> lst = new ArrayList<KeyValueDTO>();
		try{
			Query q;
			String codEnte = rb.getComuneInd().getCodice();
			String descVia = rb.getVia().getDescrizione().toUpperCase();
			logger.debug("getListaVie[descVia:"+descVia+"],[codEnte:"+codEnte+"]");
			
			if(codEnte!=null){
				q = em.createNamedQuery("DmBIndirizzo.listaByEnteDescrizione");
				q.setParameter("codEnte", codEnte);
			}else
				q = em.createNamedQuery("DmBIndirizzo.listaByDescrizione");
			
			q.setParameter("desc", "%" + descVia + "%");
			
			if(rb.getMaxResult() != null)
				q.setMaxResults(rb.getMaxResult());
			List<Object[]> lista = (List<Object[]>)q.getResultList();
			for(Object[] o:lista){
				KeyValueDTO i = new KeyValueDTO();
				i.setCodice(o[0].toString());
				String via = (String)o[1];
				i.setDescrizione(via.trim());
				lst.add(i);
			}
			
		}catch(Exception e){
			logger.error("getListaVie "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		
		return lst;
	}

	@SuppressWarnings("unchecked")
	public List<KeyValueDTO> getListaTipologia() {
		List<KeyValueDTO> lst = new ArrayList<KeyValueDTO>();
		try{
			logger.debug("getListaTipologia");
			Query q = em.createNamedQuery("DmBBeneInv.listaTipo");
			
			List<Object[]> lista = (List<Object[]>)q.getResultList();
			for(Object[] o:lista){
				KeyValueDTO i = new KeyValueDTO();
				i.setCodice(o[0].toString());
				String des = (String)o[1];
				i.setDescrizione(des.trim());
				lst.add(i);
			}
			
		}catch(Exception e){
			logger.error("getListaTipologia "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		
		return lst;
	}

	@SuppressWarnings("unchecked")
	public List<KeyValueDTO> getListaComuniIndirizzo(RicercaBeneDTO rb) {
		List<KeyValueDTO> lst = new ArrayList<KeyValueDTO>();
		try{
			String desc =  rb.getComuneInd().getDescrizione().toUpperCase();
			logger.debug("getListaComuniIndirizzo[descEnte:"+desc+"]");
			
			Query q = em.createNamedQuery("DmBIndirizzo.listaComuni");
			q.setParameter("desc", "%" + desc + "%");
			if(rb.getMaxResult() != null)
				q.setMaxResults(rb.getMaxResult());
			List<Object[]> lista = (List<Object[]>)q.getResultList();
			for(Object[] o:lista){
				KeyValueDTO i = new KeyValueDTO();
				i.setCodice(o[0].toString());
				String comune = (String)o[1];
				String prov = (String)o[2];
				i.setDescrizione(comune.trim()+"-"+prov.trim());
				lst.add(i);
			}
			
		}catch(Exception e){
			logger.error("getListaComuniIndirizzo "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<KeyValueDTO> getListaComuniMappale(RicercaBeneDTO rb) {
		List<KeyValueDTO> lst = new ArrayList<KeyValueDTO>();
		try{
			String desc = rb.getComuneCat().getDescrizione().toUpperCase();
			logger.debug("getListaComuniMappale[descEnte:"+desc+"]");
			
			Query q = em.createNamedQuery("DmBMappale.listaComuni");
			q.setParameter("desc", "%" + desc + "%");
			if(rb.getMaxResult() != null)
				q.setMaxResults(rb.getMaxResult());
			List<Object[]> lista = (List<Object[]>)q.getResultList();
			for(Object[] o:lista){
				KeyValueDTO i = new KeyValueDTO();
				i.setCodice(o[0].toString());
				String comune = (String)o[1];
				String prov = (String)o[2];
				i.setDescrizione(comune.trim()+"-"+prov.trim());
				lst.add(i);
			}
			
		}catch(Exception e){
			logger.error("getListaComuniMappale "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<DmBTerreno> getTerreniBene(Long id) {		
		try{
			logger.debug("getTerreniBene[id:"+id+"]");
			
			Query q = em.createNamedQuery("DmBTerreno.listaByBene");
			q.setParameter("id", id);
		
			return q.getResultList();
		}catch(Exception e){
			logger.error("getTerreniBene "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<MappaleDTO> getMappales(Long id) {		
		List<MappaleDTO> lst = new ArrayList<MappaleDTO>();
		HashMap<String,String[]> mappaComuni = new HashMap<String, String[]>();
		try{
			logger.debug("getMappales[id:"+id+"]");
			
			Query q = em.createNamedQuery("DmBMappale.listaByBene");
			q.setParameter("id", id);
		
			List<DmBMappale> res = q.getResultList();
			for(DmBMappale mm : res){
				MappaleDTO m = this.getDTOfromMappale(mm, mappaComuni);
				lst.add(m);
			}
			
			return lst;
		}catch(Exception e){
			logger.error("getMappales "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<MappaleDTO> getMappaliInventario(String codInventario) {	
		List<MappaleDTO> lst = new ArrayList<MappaleDTO>();
		HashMap<String,String[]> mappaComuni = new HashMap<String, String[]>();
		try{
			logger.debug("getMappaliInventario[codInventario:"+codInventario+"]");
			Query q = em.createNamedQuery("DmBMappale.listaByInventario");
			q.setParameter("inventario", codInventario);
		
			List<DmBMappale> res = q.getResultList();
			for(DmBMappale mm : res){
				MappaleDTO m = this.getDTOfromMappale(mm, mappaComuni);
				lst.add(m);
			}
			
			return lst;
		}catch(Exception e){
			logger.error("getMappaliInventario "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
	}
	
	private MappaleDTO getDTOfromMappale(DmBMappale mm, HashMap<String,String[]> mappaComuni){
		MappaleDTO m = new MappaleDTO();
		m.setId(mm.getId());
		m.setFoglio(mm.getFoglio());
		m.setMappale(mm.getMappale());
		m.setSezione(mm.getSezione());
		m.setTipo(mm.getTipoMappale());
		m.setCodComune(mm.getCodComune());
		m.setProvenienza(mm.getProvenienza());
		m.setCodInventario(mm.getDmBBene().getCodChiave1());
		
		//Recupero la decodifica del comune
		String[] c = null;
		if(!mappaComuni.containsKey(mm.getCodComune())){
			c = this.getDescComune(mm.getCodComune());
			mappaComuni.put(mm.getCodComune(), c);
		}
		
		c = mappaComuni.get(mm.getCodComune());
		if(c!=null){
			m.setProv((String)c[0]);
			m.setDesComune((String)c[1]);
		}
		
		return m;
	}
	
	@SuppressWarnings("unchecked")
	public String[] getDescComune(String belfiore) {		
		try{
			logger.debug("getDescComune[belfiore:"+belfiore+"]");
			Query q = em.createNamedQuery("Siticomu.getDescComuneByBelfiore");
			q.setParameter("belfiore", belfiore);
			
			List<Object[]> rs = (List<Object[]>) q.getResultList();
			String[] s = null;
			if(rs.size()>0){
				Object[] o =rs.get(0);
				s = new String[2];
				s[0]=(String)o[0];
				s[1]=(String)o[1];
			}
			return s;
		
		}catch(Exception e){
			logger.error("getDescComune "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<DmBTerreno> getTerreniInventario(String codInventario) {		
		try{
			logger.debug("getTerreniInventario[codInventario:"+codInventario+"]");
			Query q = em.createNamedQuery("DmBTerreno.listaByInventario");
			q.setParameter("inventario", codInventario);
		
			return q.getResultList();
		}catch(Exception e){
			logger.error("getTerreniInventario "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<DmBIndirizzo> getIndirizziInventario(String codInventario) {		
		try{
			logger.debug("getIndirizziInventario[codInventario:"+codInventario+"]");
			Query q = em.createNamedQuery("DmBIndirizzo.listaByInventario");
			q.setParameter("inventario", codInventario);
		
			return q.getResultList();
		}catch(Exception e){
			logger.error("getIndirizziInventario "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<IndirizzoDTO> getIndirizziInventarioDesc(String codInventario) {		
		List<IndirizzoDTO> lstInd = new ArrayList<IndirizzoDTO>();
		try{
			logger.debug("getIndirizziInventarioDesc[codInventario:"+codInventario+"]");
			Query q = em.createNamedQuery("DmBIndirizzo.listaDescByInventario");
			q.setParameter("inventario", codInventario);
			List<Object[]> lst = q.getResultList();
			for(Object[] o : lst){
				IndirizzoDTO ind = new IndirizzoDTO();
				ind.setCodVia(o[0]!=null ? o[0].toString() : null);
				ind.setTipoVia(o[1]!=null ? o[1].toString() : "");
				ind.setDescrVia(o[2]!=null ? o[2].toString() : "");
				ind.setCivico((String)o[3]);
				ind.setCodComune((String)o[4]);
				ind.setDesComune(o[5]!=null ? o[5].toString() : "");
				ind.setProv((String)o[6]);
				lstInd.add(ind);
			}
			
			return lstInd;
			
		}catch(Exception e){
			logger.error("getIndirizziInventario "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public DmBBene getBeneByChiave(String chiave) {
	  try{	
		  	logger.debug("getBeneByChiave[chiave:"+chiave+"]");
			
			Query q = em.createNamedQuery("DmBBene.beneByChiave");
			q.setParameter("chiave", chiave);
			
			List results = q.getResultList();
			
			if (!results.isEmpty())
			{
				DmBBene bene = (DmBBene)results.get(0);
	
				return bene;
			}
	  }catch(Exception e){
			logger.error("getBeneByChiave[chiave:"+chiave+"] "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		return null;
	}

	public List<DmBIndirizzo> getIndirizziBene(long id) {
		try{
			logger.debug("getIndirizziBene[id:"+id+"]");
			
			Query q = em.createNamedQuery("DmBIndirizzo.getIndirizziBene");
			q.setParameter("idBene", id);
			return (List<DmBIndirizzo>)q.getResultList();
		}catch(Exception e){
			logger.error("getIndirizziBene "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
	}

	public List<EventoDTO> getEventiBene(Long id) {
		List<EventoDTO> le = new ArrayList<EventoDTO>();
		try{
			logger.debug("getEventiBene[id:"+id+"]");
			
			Query q = em.createNamedQuery("DmEEvento.getEventiBeneAttuali");
			q.setParameter("idBene", id);
			List<Object[]> lst = (List<Object[]>)q.getResultList();
			
			for(Object[] o : lst){
				EventoDTO e = new EventoDTO();
				e.setCodice((String)o[0]);
				e.setDescrizione((String)o[1]);
				e.setData((String)o[2]);
				le.add(e);
			}
		}catch(Exception e){
			logger.error("getEventiBene "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		return le;
		
	}

	public DmBInfo getInfoBene(Long id) {
		try{
			logger.debug("getInfoBene[id:"+id+"]");
			
			Query q = em.createNamedQuery("DmBInfo.getInfoBeneAttuale");
			q.setParameter("idBene", id);
			List<DmBInfo> lst = (List<DmBInfo>)q.getResultList();
			if(lst.size()>0)
				return lst.get(0);
			else return null;
		}catch(Exception e){
			logger.error("getInfoBene "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
	}

	public DmBTipoUso getTipoUso(Long id) {
		try{
			logger.debug("getTipoUso[id:"+id+"]");
			
			Query q = em.createNamedQuery("DmBTipoUso.getUsoBeneAttuale");
			q.setParameter("idBene", id);
			List<DmBTipoUso> lst = (List<DmBTipoUso>)q.getResultList();
			if(lst.size()>0)
				return lst.get(0);
			else return null;
		}catch(Exception e){
			logger.error("getTipoUso "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
	}

	public DmBFascicolo getFascicolo(Long id) {
		try{
			logger.debug("getFascicolo[id:"+id+"]");
			
			Query q = em.createNamedQuery("DmBFascicolo.getFascicoloBeneAttuale");
			q.setParameter("idBene", id);
			List<DmBFascicolo> lst = (List<DmBFascicolo>)q.getResultList();
			if(lst.size()>0)
				return lst.get(0);
			else return null;
		}catch(Exception e){
			logger.error("getFascicolo "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
	}
	
	public List<KeyValueDTO> getLstFascicoli(RicercaBeneDTO rb) {
		try{
			List<KeyValueDTO> lst = new ArrayList<KeyValueDTO>();
			String cod = rb.getCodFascicolo();
			String des = rb.getDesFascicolo();
			logger.debug("getLstFascicoli[cod: "+cod+" des: "+des+"] ");
			
			String sql = "select distinct COD_FASCICOLO, DES_FASCICOLO from DM_B_FASCICOLO where 1=1 ";
			if (cod != null && !cod.trim().equalsIgnoreCase(""))
				sql += " and COD_FASCICOLO LIKE UPPER('%" + cod + "%') ";
			if (des != null && !des.trim().equalsIgnoreCase(""))
				sql += " and DES_FASCICOLO LIKE UPPER('%" + des + "%') ";

			sql += " order by DES_FASCICOLO ";
			logger.debug("getLstFascicoli - SQL["+sql+"]");

			Query q = em.createNativeQuery(sql);
			q.setMaxResults(rb.getMaxResult());
			
			List l = q.getResultList();
			if (l != null && l.size()>0){
				for (int index=0; index<l.size(); index++){
					KeyValueDTO kv = new KeyValueDTO();
					Object[] objs = (Object[])l.get(index);
					kv.setCodice((String)objs[0]);
					kv.setDescrizione((String)objs[1]);
					lst.add(kv);
				}
			}

			return lst;
			
		}catch(Exception e){
			logger.error("getLstFascicoli "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
	}//-------------------------------------------------------------------------

	public KeyValueDTO getIndirizzoByCodVia(RicercaBeneDTO b) {
		KeyValueDTO dto = new KeyValueDTO();
		try{
			KeyValueDTO via = b.getVia();
			if(via!=null && via.getCodice()!=null && !"null".equalsIgnoreCase(via.getCodice())){
				logger.debug("getIndirizzoByCodVia[codVia: "+via.getCodice()+"] ");
				BigDecimal codVia=null;
				try{
					codVia = new BigDecimal(via.getCodice());
				}catch(NumberFormatException e){}
				
				Query q = em.createNamedQuery("DmBIndirizzo.getByCodVia");
				q.setParameter("codVia", codVia);
				List<Object[]> lista = (List<Object[]>)q.getResultList();
				if(lista.size()>0){
					Object[] o = lista.get(0);
					dto.setCodice(o[0].toString());
					String dvia = (String)o[1];
					dto.setDescrizione(dvia.trim());
				}
			}
		}catch(Exception e){
			logger.error("getIndirizzoByCodVia "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		return dto;
	}

	public KeyValueDTO getComuneByCod(RicercaBeneDTO b) {
		KeyValueDTO dto = new KeyValueDTO();
		try{
			String codEnte = b.getComune().getCodice();
			logger.debug("getComuneByCod[codComune:"+codEnte+"]");
			Query q = em.createNamedQuery("DmBIndirizzo.getByCodComune");
			q.setParameter("codComune", codEnte);
			List<Object[]> lista = (List<Object[]>)q.getResultList();
			if(lista.size()>0){
				Object[] o = lista.get(0);
				dto.setCodice(o[0].toString());
				String desc = (String)o[1];
				dto.setDescrizione(desc.trim());
			}
		}catch(Exception e){
			logger.error("getComuneByCod "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		return dto;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getListaTipoDirittoReale() {
		try{
			logger.debug("getListaTipoDirittoReale");
			Query q = em.createNamedQuery("DmBTitolo.listaTipoDirittoReale");
			
			return q.getResultList();
		
		}catch(Exception e){
			logger.error("getListaTipoDirittoReale "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
	}

	public List<KeyValueDTO> getListaMacro() {
		List<KeyValueDTO> lstMacro = new ArrayList<KeyValueDTO>();
		try{
			logger.debug("getListaMacro");
			Query q = em.createNamedQuery("DmConfClassificazione.listaMacro");
			
			List<Object[]> lst = (List<Object[]>) q.getResultList();
			for(Object[] o : lst){
				KeyValueDTO kv = new KeyValueDTO();
				kv.setCodice((String)o[0]);
				kv.setDescrizione((String)o[1]);
				lstMacro.add(kv);
			}
		
		}catch(Exception e){
			logger.error("getListaMacro "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		
		return lstMacro;
	}

	public List<KeyValueDTO> getListaCategorie(String codMacro) {
		List<KeyValueDTO> lstCat = new ArrayList<KeyValueDTO>();
		try{
			logger.debug("getListaCategorie");
			Query q = em.createNamedQuery("DmConfClassificazione.listaCategorie");
			q.setParameter("codMacro", codMacro);
			List<Object[]> lst = (List<Object[]>) q.getResultList();
			for(Object[] o : lst){
				KeyValueDTO kv = new KeyValueDTO();
				kv.setCodice((String)o[0]);
				kv.setDescrizione((String)o[1]);
				lstCat.add(kv);
			}
		
		}catch(Exception e){
			logger.error("getListaCategorie "+e.getMessage(),e);
			throw new FascicoloBeneServiceException(e);
		}
		
		return lstCat;
	}


	
}
