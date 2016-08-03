package it.webred.gitland.web.bean.util;

import it.webred.ct.data.access.basic.pgt.dto.CataloghiDataIn;
import it.webred.ct.data.model.pgt.PgtSqlLayer;
import it.webred.gitland.data.model.Lotto;
import it.webred.gitland.data.model.Parametro;
import it.webred.gitland.web.bean.GitLandBaseBean;
import it.webred.gitland.web.bean.items.Ricerca;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
//@SessionScoped modificata per evitare le doppie chiamate al DB introducendo le proprieta caricate lazy 
@ViewScoped
public class NotificheBean extends GitLandBaseBean {

	private String numeroLotti=null;
	private String numeroLottiGIS=null;
	private String numeroLottiGitLand=null;
	private String numeroLottiComuni=null;
	private String numeroAziendeInsediate=null;
	private String dateRiferimentoCatasto=null;
	private String dataUltimaAcquisizione=null;
	private String dataUltimaSincronizzazione=null;
	private String numeroPraticheAssegnate=null;
	private String numeroPraticheEnteDaCompilare=null;
	private Boolean sincronizzazioneNecessaria=false; 
	private List<Object[]> listaPratichePerUtente=null;
	private List<BigDecimal> listaIdLottiGIS=null;
	private List<BigDecimal> listaIdLottiGitLand=null;
	private List<BigDecimal> listaIdLottiSoloGitLand=null;
	private boolean diffenzaLottiCalcolata=false;
	private List<Object[]> listaLottiSoloGIS=null;
	private List<Object[]> listaLottiSoloGitLand=null;
	private List<BigDecimal> lstGis=null;
	private List<BigDecimal> lstGitLand=null;
	private RicercheBean ricercheBean = null;
	
	public NotificheBean() {
	}//-------------------------------------------------------------------------

	public List<BigDecimal> loadIdLottiGIS(){
		List<BigDecimal> lista= new ArrayList<BigDecimal>();
		String sql="select to_number(ID_ASI) from CAT_CENS_AZIENDALE order by ID_ASI";
		CataloghiDataIn dto = new CataloghiDataIn();
	    dto.setTable("CAT_CENS_AZIENDALE");
	    dto.setEnteId(getEnte());
	    dto.setUserId(getCurrUserName());
	    dto.setQry(sql);
	    List l= getPgtService().getListaSql(dto);
		return (List<BigDecimal>)l;
		
	}//-------------------------------------------------------------------------
	
	public String loadListaLottiSoloGIS(){
		logger.info("notificheBean.loadListaLottiSoloGIS");
		String esito = "notificheBean.loadListaLottiSoloGIS";
		
		listaLottiSoloGIS = new ArrayList<Object[]>();
		String notIn = "";
		String sql = "select ID_ASI, PARTICELLE, SHAPE_AREA, SHAPE_LENGTH from CAT_CENS_AZIENDALE where 1=1 ";
		/*
		 * la divisione per mille è relativa al limite fisico di oracle per ogni clausola NOT IN e IN 
		 */
		if (lstGitLand != null && lstGitLand.size()>1000){
			double subPart = lstGitLand.size() / 1000;
			int nPart = (int)subPart;
			//sql += " AND (";
			for (int index=0; index<nPart; index++){
				int start = index*1000;
				int end = start + 1000;
				//if (index==0)
				//	notIn = " ID_ASI NOT IN ( " + notIn + " ) ";
				//else
				//	notIn = " OR ID_ASI NOT IN ( " + notIn + " ) ";
				notIn += " AND ID_ASI NOT IN ( " + Arrays.toString( lstGitLand.subList( start, end-1 ).toArray() ) + " ) ";
				notIn = notIn.replace('[', ' ');
				notIn = notIn.replace(']', ' ');
			}
			int start = nPart*1000;
			int end = lstGitLand.size();
			notIn = " AND ID_ASI NOT IN ( " +  Arrays.toString( lstGitLand.subList( start, end ).toArray() ) + " ) ";
			notIn = notIn.replace('[', ' ');
			notIn = notIn.replace(']', ' ');

		}
		else{
			notIn = " AND ID_ASI NOT IN ( " + Arrays.toString( lstGitLand.toArray() ) + " ) ";
			notIn = notIn.replace('[', ' ');
			notIn = notIn.replace(']', ' ');
		}
		
		sql += notIn;
		
		sql += " order by ID_ASI ";
		
		logger.info(sql);
			
		CataloghiDataIn dto = new CataloghiDataIn();
	    dto.setTable("CAT_CENS_AZIENDALE");
	    dto.setEnteId(getEnte());
	    dto.setUserId(getCurrUserName());
	    dto.setQry(sql);
	    List<Object[]> lottiSoloGIS = getPgtService().getListaSql(dto);
	    if (lottiSoloGIS != null && lottiSoloGIS.size()>0){
	    	for (Object o: lottiSoloGIS){
		    	listaLottiSoloGIS.add( (Object[])o );	    		
	    	}

	    }
	    
		return esito;		
	}//-------------------------------------------------------------------------

	private void calcolaDifferenzeLotti() {
		lstGis=getListaIdLottiGIS();
		lstGitLand=getListaIdLottiGitLand();
		int lottiTotGIS=lstGis.size();
		int lottiTotGitLand=lstGitLand.size();
		int lottiTotaliComuni=0;
		listaIdLottiSoloGitLand = new ArrayList<BigDecimal>();
		Comparator<BigDecimal> cmp= new Comparator<BigDecimal>() {
			@Override
			public int compare(BigDecimal o1, BigDecimal o2) {
				try {
					return o1.compareTo(o2);
				} catch (Exception e) {
					return -1;
				}
			}
		}; 
		for (BigDecimal lgl : lstGitLand) {
			if(lgl!=null){
				if(Collections.binarySearch(lstGis, lgl, cmp)>=0){
					lottiTotaliComuni++;
					lottiTotGitLand--;
					lottiTotGIS--;
				}else{
					/*
					 * XXX Solo in GitLand
					 */
					listaIdLottiSoloGitLand.add(lgl);
				}
			}
		}
		numeroLottiComuni=""+lottiTotaliComuni;
		numeroLottiGIS=""+lottiTotGIS;
		numeroLottiGitLand=""+lottiTotGitLand;
	}//-------------------------------------------------------------------------

	public List<BigDecimal> loadIdLottiGitLand(){
		String sql="select CODICE_ASI from LOTTI where BELFIORE='"+getEnte()+"' and (CESSATO = 0 or CESSATO is null)  order by CODICE_ASI";
		List l=getGitLandService().getList(sql, false); 
		return  (List<BigDecimal>)l;
		
	}//-------------------------------------------------------------------------
	
	public String loadListaLottiSoloGitLand(){
		String esito = "notificheBean.loadListaLottiSoloGitLand";
		
		listaLottiSoloGitLand = new ArrayList<Object[]>();
		String notIn = "";
		String sql = "select CODICE_ASI from LOTTI where BELFIORE='"+getEnte()+"' and (CESSATO = 0 or CESSATO is null) ";
		
		if (lstGis != null && lstGis.size()>1000){
			double subPart = lstGis.size() / 1000;
			int nPart = (int)subPart;
			//sql += " AND (";
			int lastEnd = 0;
			for (int index=0; index<nPart; index++){
				int start = index*1000;
				int end = start + 1000;
				notIn += " AND CODICE_ASI NOT IN ( " + Arrays.toString( lstGis.subList( start, end-1 ).toArray() ) + " ) ";
				lastEnd = end;
			}
			int start = nPart*1000;
			int end = lstGis.size();
			notIn += " AND CODICE_ASI NOT IN ( " + Arrays.toString( lstGis.subList( start, end ).toArray() ) + " ) ";
			notIn = notIn.replace('[', ' ');
			notIn = notIn.replace(']', ' ');
		}
		else{
			notIn += " and CODICE_ASI NOT IN ( " + Arrays.toString( lstGis.toArray() ) + " ) ";
			notIn = notIn.replace('[', ' ');
			notIn = notIn.replace(']', ' ');
		}
		
		sql += notIn;		
		
		sql += " order by CODICE_ASI ";
				
		logger.info(sql);
		
		List l = getGitLandService().getList(sql, false); 

		if (l != null && l.size()>0){

			ricercheBean = (RicercheBean)getBeanReference("ricercheBean");

//			for (int index=0; index<l.size(); index++){
//				BigDecimal casi = (BigDecimal)l.get(index);
//				Ricerca ricercaCur = new Ricerca();
//				Lotto lottoCur = new Lotto();
//				lottoCur.setCodiceAsi(casi.longValue());
//				ricercheBean.setRicercaCur(ricercaCur);
				String lottiAsiCodes = Arrays.toString( l.toArray() );
				lottiAsiCodes = lottiAsiCodes.replace('[', ' ');
				lottiAsiCodes = lottiAsiCodes.replace(']', ' ');
				ricercheBean.searchRicercheTree( lottiAsiCodes );
			//}
			
		}
		
		return  esito;
	}//-------------------------------------------------------------------------
	
	public List<Object[]> loadListaPratichePerUtente(){
		logger.info(NotificheBean.class + ".loadListaPratichePerUtente");

		String qry ="select utente_operatore, count(*) pratiche_aperte from pratiche p " +
						"where p.belfiore='" + getEnte() + "'  " +
						"and p.chiudip!='1' " +
						"and utente_operatore is not null " +
						"and utente_creazione = '" + getCurrUserName() + "' " +
						"group by utente_operatore "; 
		logger.info(qry);
		return  gitLandService.getList(qry, false);
	}//-------------------------------------------------------------------------
	
	private String loadDataUltimaSincronizzazione() {
		String esito = "Mai"; 
		logger.info(NotificheBean.class + ".loadDataUltimaSincronizzazione");
		Parametro par=getParametro(Parametro.DATA_ULTIMA_SINCRONIZZAZIONE);
		if(par!=null){
			esito=par.getValore();
			if("Mai".equals(dataUltimaAcquisizione)){
				esito+=" - Verificare l'acquisizione dei Lotti";
				sincronizzazioneNecessaria=true;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				Date acquisiz= sdf.parse(getDataUltimaAcquisizione());
				Date sincron= sdf.parse(par.getValore());
				if(acquisiz.after(sincron)){
					esito+=" - Data Acquisizione più recende della Data Sincronizzazione è necessario sincronizzare";
					sincronizzazioneNecessaria=true;
				}
			} catch (ParseException e) {
				logger.error(e);
			}
		}
		logger.info("Data Ultima Sincronizzazione: " + esito);
		return esito;
	}//-------------------------------------------------------------------------

	public String loadNumeroPraticheAssegnate(){
		String esito = "";
		logger.info(NotificheBean.class + ".loadNumeroPraticheAssegnate");

		String qry ="select count(*) cnt from pratiche p " +
				"where p.belfiore='" + getEnte() + "'  " +
				"and p.utente_operatore='" + getCurrUserName() + "'  " +
				"and p.chiudip!='1' "; 

		logger.info(qry);
		esito = "";
		List<Object[]> lstNumLotti = gitLandService.getList(qry, false);
		if (lstNumLotti != null && lstNumLotti.size()>0){
			Object objs = (Object)lstNumLotti.get(0);
			esito = objs.toString();
		}
		logger.info("Numero Pratiche Assegnate: " + esito);

		return esito;
	}//-------------------------------------------------------------------------
	
	public String loadNumeroPraticheEnteDaCompilare(){
		String esito = "";
		logger.info(NotificheBean.class + ".loadNumeroPraticheEnteDaCompilare");

		String qry ="select count(*) cnt from pratiche p  " +
				"where p.belfiore='" + getEnte() + "'  " +
				"and p.chiudip='1' " +
				"and p.INIZIO_LAVCOM is null " +
				"and p.FINE_LAVCOM is null " +
				"and p.CONCEDIL_N is null " +
				"and p.CONCEDIL_C is null " +
				"and p.CONCEDIL_R is null " +
				"and p.INIZIOATT is null "; 

		logger.info(qry);
		esito = "";
		List<Object[]> lstNumPraticheAssegnate = gitLandService.getList(qry, false);
		if (lstNumPraticheAssegnate != null && lstNumPraticheAssegnate.size()>0){
			Object objs = (Object)lstNumPraticheAssegnate.get(0);
			esito = objs.toString();
		}
		logger.info("Numero Pratiche Assegnate: " + esito);

		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String loadPraticheEnteDaCompilare(){
		String esito = "notificheBean.loadPraticheEnteDaCompilare";
		
		String sql = "select distinct CODICE_ASI_LOTTO from pratiche p  " +
				"where p.belfiore='" + getEnte() + "'  " +
				"and p.chiudip='1' " +
				"and p.INIZIO_LAVCOM is null " +
				"and p.FINE_LAVCOM is null " +
				"and p.CONCEDIL_N is null " +
				"and p.CONCEDIL_C is null " +
				"and p.CONCEDIL_R is null " +
				"and p.INIZIOATT is null ";
		
		logger.info(sql);
		
		List<Object[]> lstLottiPraticheDaCompilare = gitLandService.getList(sql, false);
		if (lstLottiPraticheDaCompilare != null && lstLottiPraticheDaCompilare.size()>0){
			ricercheBean = (RicercheBean)getBeanReference("ricercheBean");
			String lottiAsiCodes = Arrays.toString( lstLottiPraticheDaCompilare.toArray() );
			lottiAsiCodes = lottiAsiCodes.replace('[', ' ');
			lottiAsiCodes = lottiAsiCodes.replace(']', ' ');
			ricercheBean.searchRicercheTree( lottiAsiCodes );
				
		}
		
		return  esito;
	}//-------------------------------------------------------------------------
	
	public String loadDataUltimaAcquisizione(){
		
		logger.info(NotificheBean.class + ".loadDataUltimaAcquisizione");
		CataloghiDataIn dto = new CataloghiDataIn();
        dto.setTable("CAT_CENS_AZIENDALE");
        dto.setEnteId(getEnte());
        dto.setUserId(getCurrUserName());
		String esito="Mai";
		PgtSqlLayer layer= getPgtService().getLayerByTable(dto);
		if(layer!=null){
			Date data=layer.getDataAcquisizione();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if(data!=null)esito=sdf.format(data);
		}
		logger.info("Data Ultima Acquisizione: " + esito);
		return esito;
	
	}//----------------------------------------------------------------

	public String loadNumeroLotti(){
		String esito = "notificheBean.getNumeroLotti";
		logger.info(NotificheBean.class + ".loadNumeroLotti");

		String qry = "select count(*) as cnt "
				+ "from lotti "
				+ "where belfiore = '" + getEnte() + "' ";

		//qry += "order by aziende.ragione_sociale ";

		logger.info(qry);
		esito = "";
		List<Object[]> lstNumLotti = gitLandService.getList(qry, false);
		if (lstNumLotti != null && lstNumLotti.size()>0){
			Object objs = (Object)lstNumLotti.get(0);
			esito = objs.toString();
		}

		qry = "select count(*) as cnt "
				+ "from lotti "
				+ "where belfiore = '" + getEnte() + "' and cessato=1 ";
		
		lstNumLotti = gitLandService.getList(qry, false);
		if (lstNumLotti != null && lstNumLotti.size()>0){
			Object objs = (Object)lstNumLotti.get(0);
			if(!"0".equals(objs.toString())) esito+=" di cui "+ objs.toString()+" cessati";
		}
		logger.info("Numero Lotti: " + esito);


		return esito;
	}//-------------------------------------------------------------------------
	
	public String loadNumeroAziendeInsediate(){
		String esito = "notificheBean.getNumeroAziendeInsediate";
		logger.info(NotificheBean.class + ".loadNumeroAziendeInsediate");
		
		//select * from aziende_lotti where belfiore = 'F704' and status = 1 order by codice_asi_azienda
		
		String qry = "select count(*) as cnt from aziende_lotti "
				+ "where belfiore = '" + getEnte() + "' "
						+ " and status = 1 ";

		logger.info(qry);
		
		esito = "";
		List<Object[]> lstNumAziendeInsediate = gitLandService.getList(qry, false);
		if (lstNumAziendeInsediate != null && lstNumAziendeInsediate.size()>0){
			Object objs = (Object)lstNumAziendeInsediate.get(0);
			esito = objs.toString();
		}
		logger.info("Numero Aziende Insediate: " + esito);
		
		return esito;
	}//-------------------------------------------------------------------------
	/*
	 * String sql = "SELECT MIN (TO_DATE (p_min.dt_rif, 'DD/MM/YYYY')) AS data_ini, MAX (TO_DATE (p_max.dt_rif, 'DD/MM/YYYY')) AS data_agg " +
				"FROM load_cat_prm_incr p_min, load_cat_prm_incr p_max ";
		sql += " WHERE p_min.dt_carico = (SELECT MIN (dt_carico) FROM load_cat_prm_incr) AND p_max.dt_carico = (SELECT MAX (dt_carico) FROM load_cat_prm_incr) ";
	 */
	public String loadDateRiferimentoCatasto(){
		String esito = "notificheBean.getNumeroAziendeInsediate";
		logger.info(NotificheBean.class + ".loadDateRiferimentoCatasto");

		//select * from aziende_lotti where belfiore = 'F704' and status = 1 order by codice_asi_azienda

		String qry = "SELECT MIN (TO_DATE (p_min.dt_rif, 'DD/MM/YYYY')) AS data_ini, MAX (TO_DATE (p_max.dt_rif, 'DD/MM/YYYY')) AS data_agg "
				+ "FROM SITI_" + getEnte() + ".load_cat_prm_incr p_min, SITI_" + getEnte() + ".load_cat_prm_incr p_max "
				+ "WHERE p_min.dt_carico = (SELECT MIN (dt_carico) FROM SITI_" + getEnte() + ".load_cat_prm_incr) AND p_max.dt_carico = (SELECT MAX (dt_carico) FROM SITI_" + getEnte() + ".load_cat_prm_incr) ";

		logger.info(qry);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		esito = "";
		List<Object[]> lstDateRiferimentoCatasto = gitLandService.getList(qry, false);
		if (lstDateRiferimentoCatasto != null && lstDateRiferimentoCatasto.size()>0){
			Object[] objs = (Object[])lstDateRiferimentoCatasto.get(0);
			if (objs != null && objs.length>0){
				Timestamp tIni = (Timestamp)objs[0];
				Timestamp tAgg = (Timestamp)objs[1];
				if (tIni != null)
					esito += "Data Inizio " + sdf.format( new Date( tIni.getTime() ) ) + "; ";
				if (tAgg != null)
					esito += "Data Aggiornamento " + sdf.format( new Date( tAgg.getTime() ) ) + "; ";
			}
		}

		logger.info("Date di Riferimento Catasto: " + esito);
		
		return esito;
	}//-------------------------------------------------------------------------

	public String getNumeroLotti() {
		if(numeroLotti==null)numeroLotti=loadNumeroLotti();
		return numeroLotti;
	}

	public String getNumeroAziendeInsediate() {
		if(numeroAziendeInsediate==null)numeroAziendeInsediate=loadNumeroAziendeInsediate();
		return numeroAziendeInsediate;
	}

	public String getDateRiferimentoCatasto() {
		if(dateRiferimentoCatasto==null)dateRiferimentoCatasto=loadDateRiferimentoCatasto();
		return dateRiferimentoCatasto;
	}
	
	public String getDataUltimaAcquisizione() {
		if(dataUltimaAcquisizione==null)dataUltimaAcquisizione=loadDataUltimaAcquisizione();
		return dataUltimaAcquisizione;
	}
	
	public String getDataUltimaSincronizzazione() {
		getDataUltimaAcquisizione();
		if(dataUltimaSincronizzazione==null)dataUltimaSincronizzazione=loadDataUltimaSincronizzazione();
		return dataUltimaSincronizzazione;
	}

	public String getNumeroPraticheAssegnate() {
		if(numeroPraticheAssegnate==null)numeroPraticheAssegnate=loadNumeroPraticheAssegnate();
		return numeroPraticheAssegnate;
	}

	public List<Object[]> getListaPratichePerUtente() {
		if(listaPratichePerUtente==null)listaPratichePerUtente=loadListaPratichePerUtente();
		return listaPratichePerUtente;
	}

	public String getNumeroPraticheEnteDaCompilare() {
		if(numeroPraticheEnteDaCompilare==null)numeroPraticheEnteDaCompilare=loadNumeroPraticheEnteDaCompilare();
		return numeroPraticheEnteDaCompilare;
	}

	public Boolean getSincronizzazioneNecessaria() {
		getDataUltimaSincronizzazione();
		return sincronizzazioneNecessaria;
	}

	public String getNumeroLottiGIS() {
		if(!diffenzaLottiCalcolata)calcolaDifferenzeLotti();
		return numeroLottiGIS;
	}

	public String getNumeroLottiGitLand() {
		if(!diffenzaLottiCalcolata)calcolaDifferenzeLotti();
		return numeroLottiGitLand;
	}

	public String getNumeroLottiComuni() {
		if(!diffenzaLottiCalcolata)calcolaDifferenzeLotti();
		return numeroLottiComuni;
	}

	public List<BigDecimal> getListaIdLottiGIS() {
		if(listaIdLottiGIS==null)listaIdLottiGIS=loadIdLottiGIS();
		return listaIdLottiGIS;
	}

	public List<BigDecimal> getListaIdLottiGitLand() {
		if(listaIdLottiGitLand==null)listaIdLottiGitLand=loadIdLottiGitLand();
		return listaIdLottiGitLand;
	}

	public List<Object[]> getListaLottiSoloGIS() {
		return listaLottiSoloGIS;
	}

	public void setListaLottiSoloGIS(List<Object[]> listaLottiSoloGIS) {
		this.listaLottiSoloGIS = listaLottiSoloGIS;
	}

	public List<BigDecimal> getLstGis() {
		return lstGis;
	}

	public void setLstGis(List<BigDecimal> lstGis) {
		this.lstGis = lstGis;
	}

	public List<BigDecimal> getLstGitLand() {
		return lstGitLand;
	}

	public void setLstGitLand(List<BigDecimal> lstGitLand) {
		this.lstGitLand = lstGitLand;
	}

	public List<Object[]> getListaLottiSoloGitLand() {
		return listaLottiSoloGitLand;
	}

	public void setListaLottiSoloGitLand(List<Object[]> listaLottiSoloGitLand) {
		this.listaLottiSoloGitLand = listaLottiSoloGitLand;
	}


	
}
