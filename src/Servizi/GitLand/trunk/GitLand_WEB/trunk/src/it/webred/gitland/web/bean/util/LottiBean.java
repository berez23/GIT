package it.webred.gitland.web.bean.util;

import it.webred.gitland.data.model.Comune;
import it.webred.gitland.data.model.Lotto;
import it.webred.gitland.data.model.LottoCoordinate;
import it.webred.gitland.ejb.client.GitLandSessionBeanRemote;
import it.webred.gitland.ejb.dto.FiltroDTO;
import it.webred.gitland.web.bean.GitLandBaseBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;


@ManagedBean
@SessionScoped
public class LottiBean extends GitLandBaseBean {

	private List<Object[]> listaObjs = null;
	private List<Object[]> listaInsediamenti = null;
	private Comune comuneCur = null;
	private List<Lotto> listaLotti = null;
	private List<LottoCoordinate> listaCoord = null;
	private Lotto lottoNew = null;
	private String particellaSearch="";
	private String foglioSearch="";
	private Lotto lottoCur = null;
	private LottoCoordinate lottoCoordinateCur=null;
	private List<Comune> listaComuni = null;
	private String statusUltimo = "";
	private String cessato = "";
	private String codiceAsiDaCessare = "";
	private List<String[]> listaCodiciAsiDaCessare = null;
	private String cessoCur = "";
	private final String SEPARATORE = "_";

	public LottiBean() {
		System.out.println(LottiBean.class + ".COSTRUTTORE");
		init();
	}//-------------------------------------------------------------------------
	
	public void init(){
		System.out.println(LottiBean.class + ".init");
	
		comuneCur = new Comune();
		lottoNew = new Lotto();
		listaCoord = new ArrayList<LottoCoordinate>();
		listaLotti = new ArrayList<Lotto>();
		lottoCur = new Lotto();
		listaInsediamenti = new ArrayList<Object[]>();
		statusUltimo = "";
		cessato = "";
		codiceAsiDaCessare = "";
		listaCodiciAsiDaCessare = new ArrayList<String[]>();
		
	}//-------------------------------------------------------------------------
	
	public String searchLotti(){
		String esito = "lottiBean.searchLotti";
		System.out.println(LottiBean.class + ".searchLotti");

		String query="select distinct l from Lotto l join l.coordinate c where l.belfiore='"+getEnte()+"' ";
		
		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		
		listaLotti = new ArrayList<Lotto>();
		if (lottoNew != null){
			boolean almenoUno=false;
			if (lottoNew != null && lottoNew.getCodiceAsi() != null && lottoNew.getCodiceAsi().longValue()>0){
				query+=" and l.codiceAsi="+lottoNew.getCodiceAsi();
				almenoUno=true;
			}
			if (getFoglioSearch()!= null && !getFoglioSearch().trim().equalsIgnoreCase("")){
				query+=" and c.foglio='"+getFoglioSearch()+"'";
				almenoUno=true;
			}
			if (getParticellaSearch()!= null && !getParticellaSearch().trim().equalsIgnoreCase("")){
				query+=" and c.particella='"+getParticellaSearch()+"'";
				almenoUno=true;
			}
			if (statusUltimo != null && !statusUltimo.trim().equalsIgnoreCase("")){
				query+=" and l.statusUltimo="+statusUltimo;
				almenoUno=true;
			}
			if (cessato!= null && !cessato.trim().equalsIgnoreCase("")){
				query+=" and l.cessato="+statusUltimo;
				almenoUno=true;
			}
			if (!almenoUno){
				addMessage("globalMessage", "Inserire almeno un parametro per ricercare i lotti",FacesMessage.SEVERITY_INFO);
				return esito;
			}
		}
		List<Object[]> listina=gitLandService.getList(query, true);
		listaLotti= new ArrayList<Lotto>();
		for (int i=0;i< listina.size();i++) {
			Object lotto=listina.get(i);
			listaLotti.add((Lotto)lotto);
		}
		//listaLotti = gitLandService.getList(htQry, Lotto.class);
		/*
		 * Valorizzo oggetti transienti
		 */
		if (listaLotti != null && listaLotti.size()>0){
			Iterator<Lotto> itLot = listaLotti.iterator();
			while(itLot.hasNext()){
				Lotto lc = itLot.next();
				
				if (lc != null ){
					
					String istatp = lc.getIstat().substring(0, 3);
					String istatc = lc.getIstat().substring(3, 6);
					List lstComuni = null;
					htQry = new Hashtable();
					alFiltri = new ArrayList<FiltroDTO>();
					if (istatc != null && !istatc.trim().equalsIgnoreCase("") ){
						FiltroDTO f0 = new FiltroDTO();
						f0.setAttributo("istatc");
						f0.setParametro("istatc");
						f0.setOperatore("=");
						f0.setValore( istatc );
						alFiltri.add(f0);
					}
					if (istatp != null && !istatp.trim().equalsIgnoreCase("") ){
						FiltroDTO f1 = new FiltroDTO();
						f1.setAttributo("istatp");
						f1.setParametro("istatp");
						f1.setOperatore("=");
						f1.setValore( istatp );
						alFiltri.add(f1);
					}
					if (alFiltri != null && alFiltri.size()>0){
						htQry.put("where", alFiltri);
						htQry.put("limit", 1);
						lstComuni = gitLandService.getList(htQry, Comune.class);
					}
					if (lstComuni != null && lstComuni.size()>0)
						lc.setComune( (Comune)lstComuni.get(0) );

					
				}
			}
		}

		return esito;
	}//-------------------------------------------------------------------------
	
	public String lottoShowNew(){
		String esito = "lottiBean.lottoShowNew";

		init();
		
		/*
		 * Calcolo il max codice ASI per il BELFIORE corrente
		 */
		List<Object[]> listaMax = new ArrayList<Object[]>();
		String hql = "select max(LOTTO.codiceAsi) from Lotto LOTTO where LOTTO.belfiore = '" + getEnte() + "' group by LOTTO.belfiore";
		logger.info(hql);
		listaMax = gitLandService.getList(hql, true);
		Long codiceAsiMax = 0l;
		if (listaMax != null && listaMax.size() == 1){
			Object objMax = listaMax.get(0);
			codiceAsiMax = (Long)objMax;
		}
		lottoNew.setCodiceAsi(codiceAsiMax.longValue()+1);
		lottoNew.setBelfiore(getEnte());
		
		return esito;
	}//-------------------------------------------------------------------------

	public String resetForm(){
		String esito = "lottiBean.resetForm";
		System.out.println(LottiBean.class + ".resetForm");
		
		init();
		
		return esito;
	}//-------------------------------------------------------------------------

	public String lottiShowLst(){
		String esito = "lottiBean.lottiShowLst";

		init();
		
		listaLotti = new ArrayList<Lotto>();
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String onRowSelect(SelectEvent event) throws IOException{
		System.out.println(LottiBean.class + ".onRowSelect");
		String esito = "lottiBean.onRowSelect";
		/*
		 * recupero il lotto con tutte le diverse coordinate catastali
		 */
		Lotto lc = (Lotto) event.getObject();
		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();

		lottoNew = (Lotto)gitLandService.getItemById(lc.getPkLotto(), Lotto.class);

		lottoCoordinateCur= new LottoCoordinate();
		if (lottoNew!=null){
			//lottoNew = listaCoord.get(0);
			/*
			 * Valorizzo il Comune
			 */
				String istatp = lottoNew.getIstat().substring(0, 3);
				String istatc = lottoNew.getIstat().substring(3, 6);
				htQry = new Hashtable();
				alFiltri = new ArrayList<FiltroDTO>();
				if (istatc != null && !istatc.trim().equalsIgnoreCase("") ){
					FiltroDTO f0 = new FiltroDTO();
					f0.setAttributo("istatc");
					f0.setParametro("istatc");
					f0.setOperatore("=");
					f0.setValore( istatc );
					alFiltri.add(f0);
					
				}
				if (istatp != null && !istatp.trim().equalsIgnoreCase("") ){
					FiltroDTO f1 = new FiltroDTO();
					f1.setAttributo("istatp");
					f1.setParametro("istatp");
					f1.setOperatore("=");
					f1.setValore( istatp );
					alFiltri.add(f1);
					
				}
				if (alFiltri != null && alFiltri.size()>0){
					htQry.put("where", alFiltri);
					htQry.put("limit", 1);
					listaComuni = gitLandService.getList(htQry, Comune.class);
				}
				if (listaComuni != null && listaComuni.size()>0)
					comuneCur = (Comune)listaComuni.get(0);
				
				if (comuneCur != null)
					lottoNew.setComune(comuneCur);
				
			/*
			 * Valorizzo l'elenco storico degli insediamenti per questo lotto
			 */
				listaInsediamenti = new ArrayList<Object[]>();
				
				String sql = "select al.codice_asi_lotto, al.codice_asi_azienda, al.data_fine_status, al.status, "
						+ "aziende.ragione_sociale, aziende.attivita, aziende.via_civico, comuni.nome, aziende.num_addetti, aziende.p_iva, aziende.cod_fiscale, aziende.telefono, aziende.email "
						+ "from aziende_lotti al "
						+ "left join aziende on al.codice_asi_azienda = aziende.codice_asi "
						+ "left join comuni on aziende.istatc = comuni.istatc and aziende.istatp = comuni.istatp "
						+ "where al.belfiore = '" + getEnte() + "'  and al.belfiore = aziende.belfiore and al.codice_asi_lotto = '" + lottoNew.getCodiceAsi() + "' "
						+ "order by TO_DATE(al.data_fine_status, 'dd/mm/yyyy') desc, al.codice_asi_azienda ";

				logger.info(sql);
				
				listaInsediamenti = gitLandService.getList(sql, false);
				
				//La chiamata ad un ajax listener è diversa da una action e pertanto
				//se deve cambiare pagina va indicato il redirect
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("lottoMod.faces");

		}else{

			esito = "failure";

		}
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public List<Comune> completeComuni(String filtro){
		logger.info(LottiBean.class + ".completeComuni: " + filtro);
		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		FiltroDTO f0 = new FiltroDTO();
		f0.setAttributo("nome");
		f0.setParametro("nome");
		f0.setOperatore("LIKE");
		f0.setValore(filtro + "%");
		alFiltri.add(f0);
		htQry.put("where", alFiltri);
		htQry.put("limit", 5);
		htQry.put("orderBy", "nome");
		List<Comune> comuni = gitLandService.getList(htQry, Comune.class);

		return comuni;
	}//-------------------------------------------------------------------------

	public List<String> completeLotti(String filtro){
		logger.info(LottiBean.class + ".completeLotti: " + filtro);
		String query="select L.CODICE_ASI  from Lotti l where belfiore='"+getEnte()+"' and to_char(CODICE_ASI) like'%"+filtro+"%' order by L.CODICE_ASI";
		
		List lotti = gitLandService.getList(query,false);

		return lotti;
	}//-------------------------------------------------------------------------
	
	public String setComuneValues(SelectEvent event){
		System.out.println(LottiBean.class + ".setComuneValues");
		String esito = "lottiBean.setComuneValues";

		Object item = event.getObject();
		FacesMessage msg = new FacesMessage("Selected", "Item:" + (Long)item);

		comuneCur = (Comune)gitLandService.getItemById( (Long)item, Comune.class );

		return esito;
	}//------------------------------------------------------------------------- 
	
	public String lottoSaveNew(){
		System.out.println(LottiBean.class + ".lottoSaveNew");
		String esito = "lottiBean.lottoSaveNew";

		GitLandSessionBeanRemote gitLandService = getGitLandService();
		if (lottoNew != null && listaCoord != null && listaCoord.size()>0){
			if (comuneCur != null && comuneCur.getIdComune()>0){
				Comune c = (Comune)gitLandService.getItemById( comuneCur.getIdComune() , Comune.class);
				if (c != null && c.getIdComune() != null && c.getIdComune().longValue()>0){
					lottoNew.setIstat( c.getIstatp() + c.getIstatc() );
				}
			}
			lottoNew.setStatusUltimo(false); // 0= libero 1= occupato
			//lottoNew.setDataUltimaModifica(new Date(System.currentTimeMillis()));
			lottoNew.setCessato(false);			//il nuovo lotto è impostato ad attivo
			//lottoNew.setDataCessazione(null); 	//senza data di cessazione
			lottoNew.setBelfiore( getEnte() );
			/*
			 * in caso di presenza di lotti da cessare li inseriamo tra gli avi
			 * del lotto che andiamo ad aggiungere che a sua volta andrà ad aggiornare
			 * gli eredi dei cessati    
			 */
			if (listaCodiciAsiDaCessare != null && listaCodiciAsiDaCessare.size()>0){
				String avi = SEPARATORE;
				for (int index=0; index<listaCodiciAsiDaCessare.size(); index++){
					String[] str = listaCodiciAsiDaCessare.get(index);
					String codiceAsiLottoDaCessare = str[0]; 
					avi+= codiceAsiLottoDaCessare + SEPARATORE;

					Hashtable htQry = new Hashtable();
					ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
					
					FiltroDTO f0 = new FiltroDTO();
					f0.setAttributo("codiceAsi");
					f0.setParametro("codiceAsi");
					f0.setOperatore("=");
					f0.setValore( codiceAsiLottoDaCessare );
					alFiltri.add(f0);
					
					FiltroDTO f1 = new FiltroDTO();
					f1.setAttributo("belfiore");
					f1.setParametro("belfiore");
					f1.setOperatore("=");
					f1.setValore( getEnte() );
					alFiltri.add(f1);
					
					htQry.put("where", alFiltri);
					htQry.put("orderBy", "codiceAsi");
					List<Lotto> lstAvi = gitLandService.getList(htQry, Lotto.class);
					/*
					 * elenco di codiceAsi unico ma foglio particella diversi
					 */
					if (lstAvi != null && lstAvi.size()>0){
					Lotto lottoAvo = null;
						for (int z=0; z<lstAvi.size(); z++){
							lottoAvo = lstAvi.get(z);
							if (lottoAvo != null && 
									(lottoAvo.getStatusUltimo() != null & !lottoAvo.getStatusUltimo() | lottoAvo.getStatusUltimo() == null) && 
									(lottoAvo.getCessato() != null  & !lottoAvo.getCessato() | lottoAvo.getCessato() == null) ){
								String lottoErede = lottoAvo.getLottiEredi();
								if (lottoErede != null){
									lottoErede += lottoNew.getCodiceAsi().toString();
								}else{
									lottoErede = lottoNew.getCodiceAsi().toString();
								}
								lottoAvo.setLottiEredi(lottoErede);
								lottoAvo.setCessato(true);
								lottoAvo.setDataCessazione( new Date(System.currentTimeMillis()) );
								//lottoAvo.setDataUltimaModifica( new Date(System.currentTimeMillis()) );
								
								gitLandService.updItem(lottoAvo);
							}else{
								logger.info("LOTTO CODICE ASI: " + codiceAsiLottoDaCessare + " ancora OCCUPATO o gia CESSATO!!!");
							}
						}
					}
				}

				if (avi != null && !avi.equalsIgnoreCase(SEPARATORE))
					lottoNew.setLottiAvi(avi);

			}

			//TODO:Fixare la ricerca per partincella e Foglio in join con LottoCoordinate
			
//			Iterator<Lotto> itCoord = listaCoord.iterator();
//			while(itCoord.hasNext()){
//				Lotto l = itCoord.next();
//				lottoNew.setFoglio(l.getFoglio());
//				lottoNew.setParticella(l.getParticella());
//				
//				gitLandService.addItem(lottoNew);
//				
//			}
			
			lottoNew = new Lotto();
			comuneCur = new Comune();
			listaCoord = new ArrayList<LottoCoordinate>();
			lottoCur = new Lotto();
			listaInsediamenti = new ArrayList<Object[]>();
			listaCodiciAsiDaCessare = new ArrayList<String[]>();
			codiceAsiDaCessare = "";
		}else{
			logger.info("COORDINATE CATASTALI ASSENTI O INFORMAZIONI NON COMPLETE!!! ");
		}
			

		return esito;
	}//-------------------------------------------------------------------------
	
	public String codiceAsiDaCessareAdd(){
		System.out.println(LottiBean.class + ".codiceAsiDaCessareAdd");
		String esito = "lottiBean.codiceAsiDaCessareAdd";
		if (codiceAsiDaCessare != null){
			
			/*
			 * Recuperare il lotto con il codice asi richiesto insieme alla informazione
			 * riguardante lo stato attivo/cessato con data di cessazione, 
			 * libero/occupato
			 */
			Hashtable htQry = new Hashtable();
			ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
			FiltroDTO f0 = new FiltroDTO();
			f0.setAttributo("codiceAsi");
			f0.setParametro("codiceAsi");
			f0.setOperatore("=");
			f0.setValore( codiceAsiDaCessare );
			alFiltri.add(f0);
			FiltroDTO f1 = new FiltroDTO();
			f1.setAttributo("belfiore");
			f1.setParametro("belfiore");
			f1.setOperatore("=");
			f1.setValore( getEnte() );
			alFiltri.add(f1);
			htQry.put("where", alFiltri);
			htQry.put("orderBy", "codiceAsi");
			List<Lotto> lotti = gitLandService.getList(htQry, Lotto.class);
			if (lotti != null && lotti.size()>0){
				Lotto l = (Lotto)lotti.get(0);
				String[] cesso = new String[3];
				cesso[0] = l.getCodiceAsi()!=null?l.getCodiceAsi().toString():"";
				cesso[1] = l.getCessato()!=null?l.getCessato().toString():"";
				cesso[2] = l.getStatusUltimo()!=null?l.getStatusUltimo().toString():"";

				listaCodiciAsiDaCessare.add(cesso);				
			}
		}
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String codiceAsiDaCessareDel(){
		System.out.println(LottiBean.class + ".codiceAsiDaCessareDel");
		String esito = "lottiBean.codiceAsiDaCessareDel";
		ArrayList<String[]> lst = new ArrayList<String[]>(listaCodiciAsiDaCessare);
		listaCodiciAsiDaCessare = new ArrayList<String[]>();
		if (cessoCur != null){
			Iterator<String[]> it = lst.iterator();
			while(it.hasNext()){
				String[] casi = it.next();
				if ( casi[0].trim().equalsIgnoreCase(cessoCur.trim()) ){
					/*
					 * se il codice ASI coincide viene rimosso dalla lista pertanto  
					 * NON aggiungo la stringa all'array
					 */
				}else{
					listaCodiciAsiDaCessare.add(casi);
				}
			}
		}
		
		return esito;
	}//-------------------------------------------------------------------------

	public String coordAdd(){
		System.out.println(LottiBean.class + ".coordAdd");
		String esito = "lottiBean.coordAdd";

		if (lottoNew != null && lottoCoordinateCur!=null){
			LottoCoordinate lc = new LottoCoordinate();
			lc.setFoglio(lottoCoordinateCur.getFoglio());
			lc.setParticella(lottoCoordinateCur.getParticella());
			lc.setCodiceAsi(lottoNew.getCodiceAsi());
			lc.setBelfiore(getEnte());
			lc.setLotto(lottoNew);
			lottoNew.getCoordinate().add(lc);
		}
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String coordDel(){
		System.out.println(LottiBean.class + ".coordDel");
		String esito = "lottiBean.coordDel";
		if (lottoCoordinateCur != null){
			lottoNew.getCoordinate().remove(lottoCoordinateCur);
		}
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String lottoSaveMod(){
		System.out.println(LottiBean.class + ".lottoSaveMod");
		String esito = "LottiBean.lottoSaveMod";
		GitLandSessionBeanRemote gitLandService = getGitLandService();
		lottoNew= (Lotto)gitLandService.updItem(lottoNew);
		
		lottoNew = new Lotto();
		comuneCur = new Comune();
		listaCoord = new ArrayList<LottoCoordinate>();
		lottoCur = new Lotto();
		listaInsediamenti = new ArrayList<Object[]>();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("lottiLst.faces");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return esito;
	}//-------------------------------------------------------------------------

	public List<Object[]> getListaObjs() {
		return listaObjs;
	}

	public void setListaObjs(List<Object[]> listaObjs) {
		this.listaObjs = listaObjs;
	}

	public Comune getComuneCur() {
		return comuneCur;
	}

	public void setComuneCur(Comune comuneCur) {
		this.comuneCur = comuneCur;
	}

	public Lotto getLottoNew() {
		return lottoNew;
	}

	public void setLottoNew(Lotto lottoNew) {
		this.lottoNew = lottoNew;
	}

	public List<Lotto> getListaLotti() {
		return listaLotti;
	}

	public void setListaLotti(List<Lotto> listaLotti) {
		this.listaLotti = listaLotti;
	}

	public List<LottoCoordinate> getListaCoord() {
		return listaCoord;
	}

	public void setListaCoord(List<LottoCoordinate> listaCoord) {
		this.listaCoord = listaCoord;
	}

	public Lotto getLottoCur() {
		return lottoCur;
	}

	public void setLottoCur(Lotto lottoCur) {
		this.lottoCur = lottoCur;
	}

	public List<Object[]> getListaInsediamenti() {
		return listaInsediamenti;
	}

	public void setListaInsediamenti(List<Object[]> listaInsediamenti) {
		this.listaInsediamenti = listaInsediamenti;
	}

	public List<Comune> getListaComuni() {
		return listaComuni;
	}

	public void setListaComuni(List<Comune> listaComuni) {
		this.listaComuni = listaComuni;
	}

	public String getStatusUltimo() {
		return statusUltimo;
	}

	public void setStatusUltimo(String statusUltimo) {
		this.statusUltimo = statusUltimo;
	}

	public String getCessato() {
		return cessato;
	}

	public void setCessato(String cessato) {
		this.cessato = cessato;
	}

	public String getCodiceAsiDaCessare() {
		return codiceAsiDaCessare;
	}

	public void setCodiceAsiDaCessare(String codiceAsiDaCessare) {
		this.codiceAsiDaCessare = codiceAsiDaCessare;
	}

	public String getCessoCur() {
		return cessoCur;
	}

	public void setCessoCur(String cessoCur) {
		this.cessoCur = cessoCur;
	}

	public List<String[]> getListaCodiciAsiDaCessare() {
		return listaCodiciAsiDaCessare;
	}

	public void setListaCodiciAsiDaCessare(List<String[]> listaCodiciAsiDaCessare) {
		this.listaCodiciAsiDaCessare = listaCodiciAsiDaCessare;
	}

	public String getParticellaSearch() {
		return particellaSearch;
	}

	public void setParticellaSearch(String particellaSearch) {
		this.particellaSearch = particellaSearch;
	}

	public String getFoglioSearch() {
		return foglioSearch;
	}

	public void setFoglioSearch(String foglioSearch) {
		this.foglioSearch = foglioSearch;
	}

	public LottoCoordinate getLottoCoordinateCur() {
		return lottoCoordinateCur;
	}

	public void setLottoCoordinateCur(LottoCoordinate lottoCoordinateCur) {
		this.lottoCoordinateCur = lottoCoordinateCur;
	}


}
