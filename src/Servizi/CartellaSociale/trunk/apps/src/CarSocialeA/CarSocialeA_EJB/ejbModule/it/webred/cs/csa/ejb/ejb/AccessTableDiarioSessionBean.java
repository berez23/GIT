package it.webred.cs.csa.ejb.ejb;

import it.webred.cs.csa.ejb.CarSocialeBaseSessionBean;
import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableConfigurazioneSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableDiarioSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableDocumentoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableInterventoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableOperatoreSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSchedaSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSoggettoSessionBeanRemote;
import it.webred.cs.csa.ejb.dao.DiarioDAO;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.InterventoDTO;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.csa.ejb.dto.PaiDTO;
import it.webred.cs.csa.ejb.dto.RelSettCatsocEsclusivaDTO;
import it.webred.cs.csa.ejb.dto.RelazioneDTO;
import it.webred.cs.csa.ejb.dto.SchedaBarthelDTO;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.base.ICsDDiarioChild;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsAFamigliaGruppo;
import it.webred.cs.data.model.CsCDiarioConchi;
import it.webred.cs.data.model.CsCDiarioDove;
import it.webred.cs.data.model.CsCTipoColloquio;
import it.webred.cs.data.model.CsCTipoIntervento;
import it.webred.cs.data.model.CsDClob;
import it.webred.cs.data.model.CsDColloquio;
import it.webred.cs.data.model.CsDDiario;
import it.webred.cs.data.model.CsDDiarioDoc;
import it.webred.cs.data.model.CsDDocIndividuale;
import it.webred.cs.data.model.CsDIsee;
import it.webred.cs.data.model.CsDPai;
import it.webred.cs.data.model.CsDRelazione;
import it.webred.cs.data.model.CsDScuola;
import it.webred.cs.data.model.CsDValutazione;
import it.webred.cs.data.model.CsLoadDocumento;
import it.webred.cs.data.model.CsRelRelazioneTipoint;
import it.webred.cs.data.model.CsRelRelazioneTipointPK;
import it.webred.cs.data.model.CsRelSettCatsocEsclusiva;
import it.webred.cs.data.model.CsTbTipoDiario;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Andrea
 *
 */

@Stateless
public class AccessTableDiarioSessionBean extends CarSocialeBaseSessionBean implements AccessTableDiarioSessionBeanRemote {
	
	@Autowired
	private DiarioDAO diarioDao;
	
	@EJB
	public AccessTableSoggettoSessionBeanRemote soggettoSessionBean;
	
	@EJB
	public AccessTableSchedaSessionBeanRemote schedaSessionBean;
	
	@EJB
	public AccessTableInterventoSessionBeanRemote interventoSessionBean;
	
	@EJB
	public AccessTableOperatoreSessionBeanRemote operatoreSessionBean;
	@EJB
	public AccessTableDiarioSessionBeanRemote diarioSessionBean;
	@EJB
	public AccessTableCasoSessionBeanRemote casoSessionBean;
	@EJB
	public AccessTableConfigurazioneSessionBeanRemote confSessionBean;
	@EJB
	public AccessTableDocumentoSessionBeanRemote docSessionBean;
	
	@Override
	public CsDDiario createDiario(BaseDTO dto) throws Exception {
		return newDiario(dto);
	}
	
	@Override
	public CsDDiario updateDiario(BaseDTO dto) throws Exception {
		return diarioDao.updateDiario((CsDDiario)dto.getObj());
	}
	
	public CsDDiario newDiario(BaseDTO dto) {
		CsDDiario diario = (CsDDiario) dto.getObj();
		
		return diarioDao.newDiario(diario);
	}
	
	protected void saveDiarioChild( ICsDDiarioChild dChild ) {
		CsDDiario csDiario = dChild.getCsDDiario();
		csDiario = diarioDao.newDiario(csDiario);
		dChild.setDiarioId(csDiario.getId());
		diarioDao.saveDiarioChild(dChild);
	}
	
	public void saveDiarioChild(BaseDTO dto) {
		ICsDDiarioChild dChild = (ICsDDiarioChild) dto.getObj();
		
		saveDiarioChild(dChild);
	}
	
	@Override
	public List<CsDColloquio> getColloquios(BaseDTO dto) throws Exception {
		List<CsDColloquio> colloquios =  new ArrayList<CsDColloquio>();
		
		if(dto.getObj() != null){
			
			String select = "SELECT c FROM CsDColloquio c ";
			String where = " WHERE c.csDDiario.csACaso.csASoggetto.anagraficaId = " + dto.getObj();
			String orderBy = " ORDER BY c.dataColloquio DESC";
			
			colloquios = diarioDao.getColloquios(select + where + orderBy);
			
		}
		else{
			colloquios = diarioDao.findAllColloquio();		
		}
		
		return colloquios;
	}

	@Override
	public List<CsCTipoColloquio> getTipoColloquios(BaseDTO dto) throws Exception {
		List<CsCTipoColloquio> tipoColloquios = diarioDao.findAllTipoColloquios();
		return tipoColloquios;
	}

	@Override
	public List<CsCDiarioDove> getDiarioDoves(BaseDTO dto) {
		List<CsCDiarioDove> diarioDoves = diarioDao.findAllDiarioDoves();
		return diarioDoves;
	}

	@Override
	public List<CsCDiarioConchi> getDiarioConchis(BaseDTO dto) {
		List<CsCDiarioConchi> diarioConchis = diarioDao.findAllDiarioConchis();
		return diarioConchis;
	}

	@Override
	public void updateColloquio(BaseDTO dto) throws Exception {
		CsDColloquio colloquio = (CsDColloquio) dto.getObj();
		
		diarioDao.updateColloquio(colloquio);
	}
	
//	@Override
//	public void saveColloquio(BaseDTO dto) throws Exception {
//		CsDColloquio colloquio = (CsDColloquio) dto.getObj();
//
//		diarioDao.saveColloquio(colloquio);
//	}

	@Override
	public CsTbTipoDiario findTipoDiarioById(BaseDTO tipoDiarioIdDTO) throws Exception {
		CsTbTipoDiario tipoDiario = diarioDao.findTipoDiarioById((Long) tipoDiarioIdDTO.getObj() );
		
		return tipoDiario;
	}

	@Override
	public List<RelazioneDTO> findRelazioniByCaso(InterventoDTO i) {
		Long idCaso = i.getCasoId();
		List<RelazioneDTO> lst = new ArrayList<RelazioneDTO>();
		List<CsDRelazione> lstr = diarioDao.findRelazioniByCaso(idCaso);
		for(CsDRelazione r : lstr){
			RelazioneDTO dto  = new RelazioneDTO();
			dto.setRelazione(r);
			List<CsDPai> lstPai = new ArrayList<CsDPai>();
			for(CsDDiario d : r.getCsDDiario().getCsDDiariPadre()){
				if(d.getCsDPai()!=null)
					lstPai.add(d.getCsDPai());
			}
			dto.setListaPai(lstPai);
			
			lst.add(dto);
		}
		return lst;
	}
	
	@Override
	public List<CsDRelazione> findRelazioniByCasoTipoIntervento(InterventoDTO i) {
		Long idCaso = i.getCasoId();
		Long idTipoIntervento = i.getIdTipoIntervento();
		
		List<CsDRelazione> lstTmp = diarioDao.findRelazioniByCaso(idCaso);
		List<CsDRelazione> lstRelTi = new ArrayList<CsDRelazione>();
		
		for(CsDRelazione r : lstTmp){
			Set<CsCTipoIntervento> lstTi = r.getCsCTipoInterventos();
			if(lstTi==null || r.getCsCTipoInterventos().size()==0)
				lstRelTi.add(r);
			else{
				//Verifico che tra i tipi associati ci sia quello corrente
				int j=0;
				boolean trovato = false;
				Iterator<CsCTipoIntervento> iter = lstTi.iterator();
				while(iter.hasNext() && !trovato){
					CsCTipoIntervento ti =iter.next();
					if(ti.getId()==idTipoIntervento.longValue()){
						lstRelTi.add(r);
						trovato=true;
					}
					j++;
				}
			}
			
		}
		
		return lstRelTi;
	}
	
	@Override
	public CsDRelazione findRelazioneById(BaseDTO dto) {
		return diarioDao.findRelazioneById((Long)dto.getObj());
	}
	
	@Override
	public void updateRelazione(BaseDTO dto) throws Exception {
		CsDRelazione relazione = (CsDRelazione) dto.getObj();
		diarioDao.updateRelazione(relazione);
	}
	
	@Override
	public void saveRelazione(BaseDTO dto) throws Exception {
		CsDRelazione relazione = (CsDRelazione) dto.getObj();
		List<CsCTipoIntervento> listaTipoInt = new ArrayList<CsCTipoIntervento>();
		listaTipoInt.addAll(relazione.getCsCTipoInterventos());
		relazione.setCsCTipoInterventos(null);
		saveDiarioChild(dto);
		
		if(listaTipoInt != null) {
			for(CsCTipoIntervento tipoInt: listaTipoInt) {
				CsRelRelazioneTipoint relTipoInt = new CsRelRelazioneTipoint();
				CsRelRelazioneTipointPK relTipoIntPK = new CsRelRelazioneTipointPK();
				relTipoIntPK.setRelazioneDiarioId(relazione.getDiarioId());
				relTipoIntPK.setTipoInterventoId(tipoInt.getId());
				relTipoInt.setId(relTipoIntPK);
				
				dto.setObj(relTipoInt);
				interventoSessionBean.saveRelRelazioneTipoint(dto);
			}
		}
	}
	
	@Override
	public CsDPai salvaSchedaPai(PaiDTO dto) throws Exception {
		
		CsDPai pai = (CsDPai)dto.getPai();
		
		BaseDTO bdto = new BaseDTO();
		bdto.setEnteId(dto.getEnteId());
		bdto.setUserId(dto.getUserId());
		
		if(pai.getDiarioId()==null){
			pai.setUserIns(dto.getUserId());
			pai.setDtIns(new Date());
			pai.setUsrMod(null);
			pai.setDtMod(null);
			
			if(pai.getCsDDiario()==null){
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
					if(descrizione.equals("PAI"))
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
				pai.setDiarioId(diario.getId());
			    pai.setCsDDiario(diario);
			}
			
			diarioDao.saveSchedaPai(pai);
			
		}else{
			
			pai.setUsrMod(dto.getUserId());
			pai.setDtMod(new Date());
			diarioDao.updateSchedaPai(pai);
			
		}
		
		return pai;
	}
	
	@Override
	public CsDDiario findDiarioById(BaseDTO dto) throws Exception {
		return diarioDao.findDiarioById((Long)dto.getObj());
	}

	@Override
	public void deleteDiario(BaseDTO b) {
		 diarioDao.deleteDiario((Long)b.getObj());
	}
	
	@Override
	public List<CsDDiarioDoc> findDiarioDocById(BaseDTO b) {
		return diarioDao.findDiarioDocById((Long) b.getObj());
	}
	
	@Override
	public void saveDiarioDoc(BaseDTO b) {
		diarioDao.saveDiarioDoc((CsDDiarioDoc) b.getObj());
	}

	@Override
	public void deleteDiarioDoc(BaseDTO b) {
		diarioDao.deleteDiarioDoc((Long) b.getObj());
	}
	
	@Override
	public CsAFamigliaGruppo getFamigliaGruppoCorr(BaseDTO anaFamCurrDto) {
		
		CsAFamigliaGruppo anagraficaFamCorr = schedaSessionBean.findFamigliaAttivaBySoggettoId(anaFamCurrDto);
		
		if (anagraficaFamCorr == null)
			anagraficaFamCorr = new CsAFamigliaGruppo();
		
		return anagraficaFamCorr;
	}

	@Override
	public List<CsDValutazione> getSchedeMultidimAnzs(BaseDTO dto) {
		List<CsDValutazione> schedeMultidimAnzs =  new ArrayList<CsDValutazione>();
		
		if(dto.getObj() != null){
				
			String select = "SELECT v FROM CsDValutazione v " +
							" WHERE v.csDDiario.csACaso.csASoggetto.anagraficaId = " + dto.getObj() + 
							" AND v.csDDiario.csTbTipoDiario.id = " + DataModelCostanti.TipoDiario.VALUTAZIONE_MDS_ID + 
							" ORDER BY v.dataValutazione DESC";
			
			schedeMultidimAnzs = diarioDao.getSchedeMultidimAnz(select);
		}
		else{
			schedeMultidimAnzs = diarioDao.findAllSchedeMultidimAnz();		
		}
		
		return schedeMultidimAnzs;
	}

	@Override
	public CsDClob createClob(BaseDTO clobDTO) {
		CsDClob clob = (CsDClob) clobDTO.getObj();

		return diarioDao.saveClob(clob);
	}

	@Override
	public void updateClob(BaseDTO clobDTO) {
		CsDClob clob = (CsDClob) clobDTO.getObj();
		
		diarioDao.updateClob(clob);
	}

	@Override
	public void updateSchedaMultidimAnz(BaseDTO schedaMultidimAnzDto) {
		CsDValutazione schedaMultidimAnz = (CsDValutazione) schedaMultidimAnzDto.getObj();
		
		diarioDao.updateValutazione(schedaMultidimAnz);
	}

	@Override
	public void eliminaSchedaPai(PaiDTO dto) {
		
		Long idDiario = dto.getPai().getCsDDiario().getId();
		
		diarioDao.deleteSchedaPai(dto.getPai());
		diarioDao.deleteRelDiarioDiarioRif(idDiario);
		diarioDao.deleteDiario(idDiario);
		
	}

	@Override
	public List<CsDDocIndividuale> findDocIndividualiByCaso(BaseDTO dto) {
		return diarioDao.findDocIndividualiByCaso((Long)dto.getObj());
	}
	
	@Override
	public void updateDocIndividuale(BaseDTO dto) throws Exception {
		CsDDocIndividuale docIndividuale = (CsDDocIndividuale) dto.getObj();
		diarioDao.updateDocIndividuale(docIndividuale);
	}
	
	@Override
	public CsDDiario saveDocIndividuale(BaseDTO dto) throws Exception {
		saveDiarioChild(dto);
		CsDDocIndividuale docIndividuale = (CsDDocIndividuale) dto.getObj();
		return docIndividuale.getCsDDiario();
	}
	
	@Override
	public void deleteDocIndividuale(BaseDTO dto) throws Exception {
		CsDDocIndividuale docInd = (CsDDocIndividuale) dto.getObj();
		CsLoadDocumento loadDoc = docInd.getCsDDiario().getCsDDiarioDocs().iterator().next().getCsLoadDocumento();
		
		dto.setObj(loadDoc.getId());
		docSessionBean.deleteLoadDocumento(dto);
		diarioDao.deleteDiarioDoc(loadDoc.getId());
		diarioDao.deleteDocIndividualeById(docInd.getDiarioId());
		diarioDao.deleteDiario(docInd.getDiarioId());
	}

	@Override
	public void saveSchedaBarthel(SchedaBarthelDTO schedaBarthelDTO) throws Exception {

		String username = schedaBarthelDTO.getUserId();

		if( schedaBarthelDTO.getId() == null )
		{
			CsDValutazione valutazioneMultidim = diarioDao.findValutazioneById(schedaBarthelDTO.getSchedaMultidimDiarioId());
			CsDValutazione valutazioneSchedaBarthel = new CsDValutazione();
			
			IterDTO itDto = new IterDTO();
			itDto.setEnteId(schedaBarthelDTO.getEnteId());
			itDto.setUserId(schedaBarthelDTO.getUserId());
			itDto.setSessionId(schedaBarthelDTO.getSessionId());
			itDto.setIdCaso(valutazioneMultidim.getCsDDiario().getCsACaso().getId());
	
			CsACaso caso = casoSessionBean.findCasoById(itDto);
			
			CsTbTipoDiario tipoDiario = diarioDao.findTipoDiarioById(DataModelCostanti.TipoDiario.BARTHEL_ID);

			CsDClob jsonClob = new CsDClob();
			jsonClob.setDatiClob(schedaBarthelDTO.getJsonString());
			jsonClob = diarioDao.saveClob(jsonClob);
			
			CsDDiario diario = new CsDDiario();
			diario.setCsACaso(caso);
			diario.setCsTbTipoDiario(tipoDiario);
			diario.setCsOOperatoreSettore(schedaBarthelDTO.getOpSettore());
			diario.setCsDClob(jsonClob);
			
			valutazioneSchedaBarthel.setCsDDiario(diario);
			valutazioneSchedaBarthel.setUserIns(username);
			valutazioneSchedaBarthel.setDtIns(new Date());
			valutazioneSchedaBarthel.setDataValutazione(schedaBarthelDTO.getDataDiValutazione());
			valutazioneSchedaBarthel.setDiarioId(diario.getId());
	
			valutazioneSchedaBarthel.setVersioneScheda(schedaBarthelDTO.getVersione());
			valutazioneSchedaBarthel.setDescrizioneScheda(schedaBarthelDTO.getDescrizione());
			
			saveDiarioChild( valutazioneSchedaBarthel );
			
			diarioDao.saveDiarioRif( valutazioneMultidim.getCsDDiario().getId(), valutazioneSchedaBarthel.getCsDDiario().getId() );
		}
		else
		{
			CsDValutazione valutazioneSchedaBarthel = diarioDao.findValutazioneById( schedaBarthelDTO.getId() );

			valutazioneSchedaBarthel.setUsrMod(username);
			valutazioneSchedaBarthel.setDtMod(new Date());
			valutazioneSchedaBarthel.setDataValutazione(schedaBarthelDTO.getDataDiValutazione());

			valutazioneSchedaBarthel.setVersioneScheda(schedaBarthelDTO.getVersione());
			valutazioneSchedaBarthel.setDescrizioneScheda(schedaBarthelDTO.getDescrizione());

			//Update Clob with json
			CsDClob clob = valutazioneSchedaBarthel.getCsDDiario().getCsDClob();
			clob.setDatiClob(schedaBarthelDTO.getJsonString());
			diarioDao.updateClob(clob);

			diarioDao.updateValutazione(valutazioneSchedaBarthel);
		}
	}

	@Override
	public CsDValutazione findBarthelByMutlidimId(SchedaBarthelDTO barthelDto) {
		CsDValutazione barthel = diarioDao.findBarthelByMutlidimId( barthelDto.getSchedaMultidimDiarioId() ); 
		return barthel;
	}
	
	@Override
	public List<CsRelSettCatsocEsclusiva> findRelSettCatsocEsclusivaByTipoDiarioId(RelSettCatsocEsclusivaDTO dto) {
		return diarioDao.findRelSettCatsocEsclusivaByTipoDiarioId(dto.getTipoDiarioId());
	}
	
	@Override
	public CsRelSettCatsocEsclusiva findRelSettCatsocEsclusivaByIds(RelSettCatsocEsclusivaDTO dto) {
		return diarioDao.findRelSettCatsocEsclusivaByIds(dto.getTipoDiarioId(), dto.getCategoriaSocialeId(), dto.getSettoreId());
	}
	
	@Override
	public void saveCsRelSettCatsocEsclusiva(BaseDTO dto) {
		diarioDao.saveCsRelSettCatsocEsclusiva((CsRelSettCatsocEsclusiva) dto.getObj());
	}

	@Override
	public void updateCsRelSettCatsocEsclusiva(BaseDTO dto) {
		diarioDao.updateCsRelSettCatsocEsclusiva((CsRelSettCatsocEsclusiva) dto.getObj());
	}
			
	@Override
	public void deleteCsRelSettCatsocEsclusiva(RelSettCatsocEsclusivaDTO dto) {
		diarioDao.deleteCsRelSettCatsocEsclusiva(dto.getTipoDiarioId(), dto.getCategoriaSocialeId(), dto.getSettoreId());
	}

	@Override
	public List<CsDScuola> findScuoleByCaso(BaseDTO dto) {
		return diarioDao.findScuoleByCaso((Long)dto.getObj());
	}
	
	@Override
	public void updateScuola(BaseDTO dto) throws Exception {
		CsDScuola scuola = (CsDScuola) dto.getObj();
		diarioDao.updateScuola(scuola);
	}
	
	@Override
	public CsDDiario saveScuola(BaseDTO dto) throws Exception {
		saveDiarioChild(dto);
		CsDScuola scuola = (CsDScuola) dto.getObj();
		return scuola.getCsDDiario();
	}
	
	@Override
	public void deleteScuola(BaseDTO dto) throws Exception {
		CsDScuola scuola = (CsDScuola) dto.getObj();
		diarioDao.deleteScuolaById(scuola.getDiarioId());
		diarioDao.deleteDiario(scuola.getDiarioId());
	}
	
	@Override
	public List<CsDIsee> findIseeByCaso(BaseDTO dto) {
		return diarioDao.findIseeByCaso((Long)dto.getObj());
	}
	
	@Override
	public void updateIsee(BaseDTO dto) throws Exception {
		CsDIsee isee = (CsDIsee) dto.getObj();
		diarioDao.updateIsee(isee);
	}
	
	@Override
	public CsDDiario saveIsee(BaseDTO dto) throws Exception {
		saveDiarioChild(dto);
		CsDIsee isee = (CsDIsee) dto.getObj();
		return isee.getCsDDiario();
	}
	
	@Override
	public void deleteIsee(BaseDTO dto) throws Exception {
		CsDIsee isee = (CsDIsee) dto.getObj();
		diarioDao.deleteIseeById(isee.getDiarioId());
		diarioDao.deleteDiario(isee.getDiarioId());
	}
}
