package it.webred.gitland.web.bean.util;

import it.webred.amprofiler.ejb.anagrafica.dto.AnagraficaSearchCriteria;
import it.webred.amprofiler.model.AmUser;
import it.webred.gitland.data.model.Azienda;
import it.webred.gitland.data.model.AziendaLotto;
import it.webred.gitland.data.model.Comune;
import it.webred.gitland.data.model.IntCategoria;
import it.webred.gitland.data.model.IntParametro;
import it.webred.gitland.data.model.Lotto;
import it.webred.gitland.data.model.LottoCoordinate;
import it.webred.gitland.data.model.Pratica;
import it.webred.gitland.data.model.PraticaAllegato;
import it.webred.gitland.data.model.Tipologia;
import it.webred.gitland.ejb.dto.FiltroDTO;
import it.webred.gitland.web.bean.GitLandBaseBean;
import it.webred.gitland.web.bean.MapMan;
import it.webred.gitland.web.bean.items.AziendaView;
import it.webred.gitland.web.bean.items.Insediamento;
import it.webred.gitland.web.bean.items.Ricerca;
import it.webred.gitland.web.bean.items.RicercaLottoInsediamenti;
import it.webred.utils.CollectionsUtils;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.tabview.Tab;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;


@ManagedBean
@SessionScoped
public class RicercheBean extends GitLandBaseBean {
	
	private List<Ricerca> listaRicerche = null;
	private List<RicercaLottoInsediamenti> listaRicercheLotti = null;
	private Ricerca ricercaCur = null;
	private String cessato = "";
	private String statusUltimo = "";
	private Tab activeTab = null;
	private Azienda aziendaCur = null;
	private Object[] aziendaObjs = null;
	private AziendaView aziendaMod = null;
	private Lotto lottoCur = null;
	private Lotto lottoMod = null;
	private Pratica praticaCur = null;
	private Pratica praticaUltimaAppr = null;
	private Comune comuneAziCur = null;
	private Comune comuneLotCur = null;
	private AziendaLotto aziendaLottoCur = null;
	private List<Comune> listaComuni = null;
	private List<Azienda> listaAziende = null;
	private List<Object[]> listaInsediamenti = null;
	private List<Pratica> listaPratiche = null;
	private List<SelectItem> listaTipologieItems = null;
	private List<SelectItem> listaUtentiOperatoreItems = null;
	private AziendeBean aziendeBean = null;
	private MapMan mapMan = null;
	private StreamedContent praticaCorrenteAsWord;
	private StreamedContent allegatoDaScaricare;
	private Integer tabAttivo=0;
	private PraticaAllegato allegatoCur=null;
	
	public RicercheBean() {
		logger.info(RicercheBean.class + ".COSTRUTTORE");
		init();
	}//-------------------------------------------------------------------------
	
	public void init(){
		logger.info(RicercheBean.class + ".init");
	
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.removeAttribute("lottoIdAsiMap");
		listaComuni = new ArrayList<Comune>();
		listaInsediamenti = new ArrayList<Object[]>();
		listaPratiche = new ArrayList<Pratica>();
		listaRicerche = new ArrayList<Ricerca>();
		listaRicercheLotti = new ArrayList<RicercaLottoInsediamenti>();
		ricercaCur = new Ricerca();
		statusUltimo = "";
		cessato = "";
		tabAttivo=0;
		
		mapMan = (MapMan) getBeanReference("mapMan");
		mapMan.initializeData();
		
	}//-------------------------------------------------------------------------
	
	public String ricercheShowLst(){
		String esito = "ricercheBean.ricercheShowLst";
		logger.info(RicercheBean.class + ".ricercheShowLst");
		
		init();
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String onChangeTab(TabChangeEvent event){
		String esito = "ricercheBean.onChangeTab";
		logger.info(RicercheBean.class + ".onChangeTab");

		activeTab = event.getTab();

		ricercaCur = new Ricerca();
		statusUltimo = "";
		cessato = "";

		logger.info("ACTIVE TAB TITLE: " + activeTab.getTitle());
		if ( activeTab.getTitle().equalsIgnoreCase("Mappa") ){
			
			
		}else if ( activeTab.getTitle().equalsIgnoreCase("Ricerca per LOTTI") ){
			HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			logger.info("SESSION ID: " + session.getId());
			String strLottoIdAsiMap = (String)session.getAttribute("lottoIdAsiMap");
			Long lottoIdAsiMap = new Long(0);
			if (strLottoIdAsiMap != null)
				lottoIdAsiMap = new Long(strLottoIdAsiMap);
			if (ricercaCur != null && ricercaCur.getLotto() != null){
				ricercaCur.getLotto().setCodiceAsi(lottoIdAsiMap);
			}
		}else if ( activeTab.getTitle().equalsIgnoreCase("Mappa Lotto") ){
			
		}

		return esito;
	}//-------------------------------------------------------------------------
	
	public String searchRicerche(){
		String esito = "ricercheBean.searchRicerche";
		logger.info(RicercheBean.class + ".searchRicerche");
		listaRicerche = new ArrayList<Ricerca>();
		
		//if (activeTab != null && activeTab.getTitle()!=null && activeTab.getTitle().equalsIgnoreCase("Ricerca per AZIENDE")){
		if (tabAttivo==1){
			/*
			 * Ricerche per AZIENDE
			 */			
			String qry = "select distinct codice_asi, ragione_sociale, via_civico, attivita, telefono, comuni.nome, aziende.belfiore "
					+ "from aziende "
					+ "left join comuni on comuni.istatp = aziende.istatp and comuni.istatc = aziende.istatc "
					+ "where aziende.belfiore = '" + getEnte() + "' ";

			if (ricercaCur != null){
				if (ricercaCur.getAzienda()!=null && ricercaCur.getAzienda().getCodiceAsi() != null && ricercaCur.getAzienda().getCodiceAsi().longValue()>0)
					qry += "and aziende.codice_asi = '" + ricercaCur.getAzienda().getCodiceAsi() + "' ";
				if (ricercaCur.getAzienda()!=null && ricercaCur.getAzienda().getRagioneSociale() != null && !ricercaCur.getAzienda().getRagioneSociale().trim().equalsIgnoreCase(""))
					qry += "and upper(aziende.ragione_sociale) LIKE upper('%" + ricercaCur.getAzienda().getRagioneSociale() + "%') ";
				if (ricercaCur.getAzienda()!=null && ricercaCur.getAzienda().getViaCivico() != null && !ricercaCur.getAzienda().getViaCivico().trim().equalsIgnoreCase(""))
					qry += "and upper(aziende.via_civico) LIKE upper('%" + ricercaCur.getAzienda().getViaCivico() + "%') ";
			}
			qry += "order by aziende.ragione_sociale ";
			
			logger.info(qry);
			
			List<Object[]> lstAziendeObjs = gitLandService.getList(qry, false);
			
			if (lstAziendeObjs != null && lstAziendeObjs.size()>0){
				for (int i=0; i<lstAziendeObjs.size(); i++){
					Ricerca ricerca = new Ricerca();
					Azienda azienda = new Azienda();
					Comune comuneAz = new Comune();
					Object[] aziendaObjs = lstAziendeObjs.get(i);
					azienda.setRagioneSociale( (String)aziendaObjs[1] );
					azienda.setAttivita( (String)aziendaObjs[3] );
					azienda.setTelefono( (String)aziendaObjs[4] );
					azienda.setViaCivico( (String)aziendaObjs[2] );
					BigDecimal bdCodiceAsiAz = (BigDecimal)aziendaObjs[0];
					azienda.setCodiceAsi(  bdCodiceAsiAz!=null?bdCodiceAsiAz.longValue():0 );
					comuneAz.setNome( (String)aziendaObjs[5] );
					azienda.setComune(comuneAz);
					azienda.setBelfiore( (String)aziendaObjs[6] );
					ricerca.setAzienda(azienda);
					
					String sql = "select data_fine_status, status, codice_asi, area, perimetro, indirizzo, lotti.istatc, note, lotti.status_ultimo, comuni.nome, aziende_lotti.belfiore, aziende_lotti.pk_azienda_lotto "
							+ "from aziende_lotti "
							+ "left join lotti on lotti.codice_asi = aziende_lotti.codice_asi_lotto and aziende_lotti.status = lotti.status_ultimo "
							+ "left join comuni on comuni.istatp = substr(lotti.istatc, 0, 3) and comuni.istatc = substr(lotti.istatc, 4, 3) "
							+ "where aziende_lotti.belfiore = '" + getEnte() + "' and aziende_lotti.codice_asi_azienda = '" + azienda.getCodiceAsi() + "' ";

					if (ricercaCur != null){
						if (ricercaCur.getLotto()!=null && ricercaCur.getLotto().getCodiceAsi() != null && ricercaCur.getLotto().getCodiceAsi().longValue()>0)
							sql += "and lotti.codice_asi = '" + ricercaCur.getLotto().getCodiceAsi() + "' ";
						if (statusUltimo != null && statusUltimo.trim().equalsIgnoreCase("true") )
							sql += "and lotti.status_ultimo = '1' ";
						else if (statusUltimo != null && statusUltimo.trim().equalsIgnoreCase("false") )
							sql += "and lotti.status_ultimo <> '1' ";
						if (ricercaCur.getLotto()!=null && ricercaCur.getLotto().getIndirizzo() != null && !ricercaCur.getLotto().getIndirizzo().trim().equalsIgnoreCase(""))
							sql += "and upper(lotti.indirizzo) LIKE upper('%" + ricercaCur.getLotto().getIndirizzo() + "%') ";
						if (cessato != null && cessato.trim().equalsIgnoreCase("true") )
							sql += "and lotti.cessato = '1' ";
						else if (cessato != null && cessato.trim().equalsIgnoreCase("false") )
							sql += "and lotti.cessato <> '1' ";
						
					}
					sql += "order by codice_asi ";
					
					logger.info(sql);
					
					List<Object[]> lstLottiObjs = gitLandService.getList(sql, false);
					if (lstLottiObjs != null && lstLottiObjs.size()>0){
						//for (int j=0; j<lstLottiObjs.size(); j++){
							AziendaLotto aziendaLotto = new AziendaLotto();							
							Lotto lotto = new Lotto();
							Comune comuneLo = new Comune();
							Object[] lottoObjs = lstLottiObjs.get(0);
							
							aziendaLotto.setDataFineStatus( (String)lottoObjs[0] );
							BigDecimal bdStatus = (BigDecimal)lottoObjs[1];
							aziendaLotto.setStatus( bdStatus.intValue()==1?true:false );
							aziendaLotto.setBelfiore( (String)lottoObjs[10] );
							BigDecimal bdAziendaLotto = (BigDecimal)lottoObjs[11];
							aziendaLotto.setPkAziendaLotto( bdAziendaLotto!=null?bdAziendaLotto.longValue():null );
							ricerca.setAziendaLotto(aziendaLotto);

							
							BigDecimal bdCodiceAsiLo = (BigDecimal)lottoObjs[2]; 
							lotto.setCodiceAsi( bdCodiceAsiLo!=null?bdCodiceAsiLo.longValue():0 );
							BigDecimal bdArea = (BigDecimal)lottoObjs[3]; 
							lotto.setArea( bdArea!=null?bdArea.doubleValue():0 );
							BigDecimal bdPerimetro = (BigDecimal)lottoObjs[4];
							lotto.setPerimetro( bdPerimetro!=null?bdPerimetro.doubleValue():0 );
							lotto.setIndirizzo( (String)lottoObjs[5] );
							lotto.setIstat( (String)lottoObjs[6] );
							lotto.setNote( (String)lottoObjs[7] );
							BigDecimal bdStatusUlt = (BigDecimal)lottoObjs[8];
							Boolean status = false;
							if (bdStatusUlt != null && bdStatusUlt.longValue() == 1)
								status = true;
							lotto.setStatusUltimo( status );
							comuneLo.setNome( (String)lottoObjs[9] );
							lotto.setBelfiore( (String)lottoObjs[10] );
							lotto.setComune(comuneLo);
							ricerca.setLotto(lotto);

						//}
					}
					
					listaRicerche.add(ricerca);
					
				}
			}
			
		}else{
			/*
			 * Ricerche per LOTTI (Default nel caso di activeTab.getTitle != null && activeTab.getTitle <> Ricerca per AZIENDE
			 */
			String sql = "select distinct codice_asi, area, perimetro, indirizzo, lotti.istatc, note, lotti.status_ultimo, "
					+ "comuni.nome, lotti.belfiore, lotti.cessato, lotti.data_cessazione "
					+ "from lotti "
					+ "left join comuni on comuni.istatp = substr(lotti.istatc, 0, 3) and comuni.istatc = substr(lotti.istatc, 4, 3) "
					+ "where lotti.belfiore = '" + getEnte() + "' ";
			if (ricercaCur != null){
				if (ricercaCur.getLotto()!=null && ricercaCur.getLotto().getCodiceAsi() != null && ricercaCur.getLotto().getCodiceAsi().longValue()>0)
					sql += "and lotti.codice_asi = '" + ricercaCur.getLotto().getCodiceAsi() + "' ";
				if (statusUltimo != null && statusUltimo.trim().equalsIgnoreCase("true") )
					sql += "and lotti.status_ultimo = '1' ";
				else if (statusUltimo != null && statusUltimo.trim().equalsIgnoreCase("false") )
					sql += "and lotti.status_ultimo <> '1' ";
				if (ricercaCur.getLotto()!=null && ricercaCur.getLotto().getIndirizzo() != null && !ricercaCur.getLotto().getIndirizzo().trim().equalsIgnoreCase(""))
					sql += "and upper(lotti.indirizzo) LIKE upper('%" + ricercaCur.getLotto().getIndirizzo() + "%') ";
				if (cessato != null && cessato.trim().equalsIgnoreCase("true") )
					sql += "and lotti.cessato = '1' ";
				else if (cessato != null && cessato.trim().equalsIgnoreCase("false") )
					sql += "and lotti.cessato <> '1' ";
				
			}
			sql += "order by codice_asi ";
			
			logger.info(sql);
			
			List<Object[]> lstLottiObjs = gitLandService.getList(sql, false);
			
			if (lstLottiObjs != null && lstLottiObjs.size()>0){
				for (int i=0; i<lstLottiObjs.size(); i++){
					Ricerca ricerca = new Ricerca();
					Lotto lotto = new Lotto();
					Comune comune = new Comune();
					Object[] lottoObjs = lstLottiObjs.get(i);
					BigDecimal bdCodiceAsi = (BigDecimal)lottoObjs[0]; 
					lotto.setCodiceAsi( bdCodiceAsi!=null?bdCodiceAsi.longValue():0 );
					BigDecimal bdArea = (BigDecimal)lottoObjs[1]; 
					lotto.setArea( bdArea.doubleValue() );
					BigDecimal bdPerimetro = (BigDecimal)lottoObjs[2];
					lotto.setPerimetro( bdPerimetro.doubleValue() );
					//lotto.setIndirizzo( (String)lottoObjs[3] );
					lotto.setIstat( (String)lottoObjs[4] );
					lotto.setNote( (String)lottoObjs[5] );
					BigDecimal bdStatus = (BigDecimal)lottoObjs[6];
					Boolean status = null;
					if (bdStatus != null && bdStatus.longValue() == 1)
						status = true;
					else if (bdStatus != null && bdStatus.longValue() == 0)
						status = false;
					lotto.setStatusUltimo( status );
					comune.setNome( (String)lottoObjs[7] );
					lotto.setBelfiore( (String)lottoObjs[8] );
					BigDecimal bdCessato = (BigDecimal)lottoObjs[9];
					Boolean cessato = null;
					if (bdCessato != null && bdCessato.longValue() == 1)
						cessato = true;
					else if (bdCessato != null && bdCessato.longValue() == 0)
						cessato = false;
					lotto.setCessato( cessato );
					Timestamp dtCessa = (Timestamp)lottoObjs[10];
					if (dtCessa != null ){
							lotto.setDataCessazione( new Date(dtCessa.getTime()) );
					}
					lotto.setComune(comune);
					ricerca.setLotto(lotto);
					
					String qry = "select data_fine_status, status, ragione_sociale, attivita, telefono, via_civico, aziende.codice_asi, aziende_lotti.belfiore, aziende_lotti.pk_azienda_lotto "
							+ "from aziende_lotti "
							+ "left join aziende on aziende.codice_asi = aziende_lotti.codice_asi_azienda "
							+ "where aziende_lotti.belfiore = '" + getEnte() + "' and aziende_lotti.belfiore = aziende.belfiore and aziende_lotti.codice_asi_lotto = '" + lotto.getCodiceAsi() + "' ";
					
					if ( lotto.getStatusUltimo() != null){
						if ( lotto.getStatusUltimo() )
							qry += "and aziende_lotti.status = '1' ";
						else
							qry += "and aziende_lotti.status = '0' ";
					}
					
					if (ricercaCur != null){
						if (ricercaCur.getAzienda()!=null && ricercaCur.getAzienda().getCodiceAsi() != null && ricercaCur.getAzienda().getCodiceAsi().longValue()>0)
							qry += "and aziende_lotti.codice_asi_azienda = '" + ricercaCur.getAzienda().getCodiceAsi() + "' ";
						if (ricercaCur.getAzienda()!=null && ricercaCur.getAzienda().getRagioneSociale() != null && !ricercaCur.getAzienda().getRagioneSociale().trim().equalsIgnoreCase(""))
							qry += "and upper(aziende.ragione_sociale) LIKE upper('%" + ricercaCur.getAzienda().getRagioneSociale() + "%') ";
						if (ricercaCur.getAzienda()!=null && ricercaCur.getAzienda().getViaCivico() != null && !ricercaCur.getAzienda().getViaCivico().trim().equalsIgnoreCase(""))
							qry += "and upper(aziende.via_civico) LIKE upper('%" + ricercaCur.getAzienda().getViaCivico() + "%') ";
						
					}
					qry += "order by TO_DATE(aziende_lotti.data_fine_status, 'dd/mm/yyyy') DESC ";
					
					logger.info(qry);

					List<Object[]> lstAziendeObjs = gitLandService.getList(qry, false);
					if (lstAziendeObjs != null && lstAziendeObjs.size()>0){
						//for (int index=0; index<lstAziendeObjs.size(); index++){
							AziendaLotto aziendaLotto = new AziendaLotto();
							Azienda azienda = new Azienda();
							Object[] aziendaObjs = lstAziendeObjs.get(0);
							aziendaLotto.setDataFineStatus( (String)aziendaObjs[0] );
							bdStatus = (BigDecimal)aziendaObjs[1];
							aziendaLotto.setStatus( bdStatus.intValue()==1?true:false );
							aziendaLotto.setBelfiore( (String)aziendaObjs[7] );
							BigDecimal bdIde = (BigDecimal)aziendaObjs[8];
							aziendaLotto.setPkAziendaLotto( bdIde.longValue() );
							ricerca.setAziendaLotto(aziendaLotto);
							
							azienda.setRagioneSociale( (String)aziendaObjs[2] );
							azienda.setAttivita( (String)aziendaObjs[3] );
							azienda.setTelefono( (String)aziendaObjs[4] );
							azienda.setViaCivico( (String)aziendaObjs[5] );
							bdCodiceAsi = (BigDecimal)aziendaObjs[6];
							azienda.setCodiceAsi( bdCodiceAsi!=null?bdCodiceAsi.longValue():0l );
							azienda.setBelfiore( (String)aziendaObjs[7] );
							ricerca.setAzienda(azienda);
							
							//Se decommenti questo 
						//listaRicerche.add(ricerca);
					}
					
					listaRicerche.add(ricerca);
					
				}
			}			
		}
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String searchRicercheTree(){
		String esito = "ricercheBean.searchRicerche";
		logger.info(RicercheBean.class + ".searchRicercheTree");
		listaRicercheLotti = new ArrayList<RicercaLottoInsediamenti>();

		String sql="select distinct * from ( select lotti.codice_asi, lotti.area, lotti.perimetro, lotti.indirizzo, lotti.istatc, lotti.note, lotti.status_ultimo,comuni.nome, lotti.belfiore, lotti.cessato, lotti.data_cessazione, " +
		"al.data_fine_status, al.status, az.ragione_sociale, az.attivita, az.telefono, az.via_civico, az.codice_asi as codice_asi_azienda, al.belfiore as belfiore_azienda, al.pk_azienda_lotto  " +
		"from lotti   " +
		"left join aziende_lotti al on lotti.BELFIORE=AL.BELFIORE and lotti.CODICE_ASI=AL.CODICE_ASI_LOTTO  " +
		"left join aziende az on lotti.BELFIORE=AZ.BELFIORE and AL.CODICE_ASI_AZIENDA=AZ.CODICE_ASI  " +
		"left join comuni on comuni.istatp = substr(lotti.istatc, 0, 3) and comuni.istatc = substr(lotti.istatc, 4, 3)  " +
		"where (AL.DATA_FINE_STATUS=(select max(data_fine_status) from aziende_lotti where lotti.BELFIORE=BELFIORE and lotti.CODICE_ASI=CODICE_ASI_LOTTO AND AZ.CODICE_ASI = CODICE_ASI_AZIENDA) or AL.DATA_FINE_STATUS is null) " +
		"and lotti.belfiore='"+getEnte()+"' ";

		//if (activeTab != null && activeTab.getTitle()!=null && activeTab.getTitle().equalsIgnoreCase("Ricerca per AZIENDE")){
		if (tabAttivo==1){
			if (ricercaCur != null){
				if (ricercaCur.getAzienda()!=null && ricercaCur.getAzienda().getCodiceAsi() != null && ricercaCur.getAzienda().getCodiceAsi().longValue()>0)
					sql += "and az.codice_asi = '" + ricercaCur.getAzienda().getCodiceAsi() + "' ";
				if (ricercaCur.getAzienda()!=null && ricercaCur.getAzienda().getRagioneSociale() != null && !ricercaCur.getAzienda().getRagioneSociale().trim().equalsIgnoreCase(""))
					sql += "and upper(az.ragione_sociale) LIKE upper('%" + ricercaCur.getAzienda().getRagioneSociale() + "%') ";
				if (ricercaCur.getAzienda()!=null && ricercaCur.getAzienda().getViaCivico() != null && !ricercaCur.getAzienda().getViaCivico().trim().equalsIgnoreCase(""))
					sql += "and upper(az.via_civico) LIKE upper('%" + ricercaCur.getAzienda().getViaCivico() + "%') ";
			}
		}else{
		
			if (ricercaCur != null){
				if (ricercaCur.getLotto()!=null && ricercaCur.getLotto().getCodiceAsi() != null && ricercaCur.getLotto().getCodiceAsi().longValue()>0)
					sql += "and lotti.codice_asi = '" + ricercaCur.getLotto().getCodiceAsi() + "' ";
				if (statusUltimo != null && statusUltimo.trim().equalsIgnoreCase("true") )
					sql += "and lotti.status_ultimo = '1' ";
				else if (statusUltimo != null && statusUltimo.trim().equalsIgnoreCase("false") )
					sql += "AND (lotti.status_ultimo <> 1 or lotti.status_ultimo is null) ";
				if (ricercaCur.getLotto()!=null && ricercaCur.getLotto().getIndirizzo() != null && !ricercaCur.getLotto().getIndirizzo().trim().equalsIgnoreCase(""))
					sql += "and upper(lotti.indirizzo) LIKE upper('%" + ricercaCur.getLotto().getIndirizzo() + "%') ";
				if (cessato != null && cessato.trim().equalsIgnoreCase("true") )
					sql += "and lotti.cessato = '1' ";
				else if (cessato != null && cessato.trim().equalsIgnoreCase("false") )
					sql += "and (lotti.cessato <> '1' or lotti.cessato is null) ";
				
			}
		}
			/*
			 * Ricerche per LOTTI (Default nel caso di activeTab.getTitle != null && activeTab.getTitle <> Ricerca per AZIENDE
			 */
			
			sql +="order by LOTTI.CODICE_ASI, AL.CODICE_ASI_AZIENDA) order by CODICE_ASI ";
			
			logger.info(sql);
			
			List<Object[]> lstLottiObjs = gitLandService.getList(sql, false);
			Lotto prevLotto= new Lotto();
			if (lstLottiObjs != null && lstLottiObjs.size()>0){
				RicercaLottoInsediamenti ricerca =null;
				Lotto lotto =null;
				BigDecimal bdStatus =null;
				BigDecimal bdCodiceAsi =null;
				
				for (int i=0; i<lstLottiObjs.size(); i++){
					Object[] lottoObjs = lstLottiObjs.get(i);
					bdCodiceAsi = (BigDecimal)lottoObjs[0];
					if(bdCodiceAsi.compareTo(new BigDecimal(prevLotto.getCodiceAsi()))!=0){
						ricerca= new RicercaLottoInsediamenti();
						lotto = new Lotto();
						Comune comune = new Comune();
						prevLotto=lotto;
						 
						lotto.setCodiceAsi( bdCodiceAsi!=null?bdCodiceAsi.longValue():0 );
						BigDecimal bdArea = (BigDecimal)lottoObjs[1]; 
						lotto.setArea( bdArea.doubleValue() );
						BigDecimal bdPerimetro = (BigDecimal)lottoObjs[2];
						lotto.setPerimetro( bdPerimetro.doubleValue() );
						//lotto.setIndirizzo( (String)lottoObjs[3] );
						lotto.setIstat( (String)lottoObjs[4] );
						lotto.setNote( (String)lottoObjs[5] );
						bdStatus = (BigDecimal)lottoObjs[6];
						Boolean status = null;
						if (bdStatus != null && bdStatus.longValue() == 1)
							status = true;
						else if (bdStatus != null && bdStatus.longValue() == 0)
							status = false;
						lotto.setStatusUltimo( status );
						comune.setNome( (String)lottoObjs[7] );
						lotto.setBelfiore( (String)lottoObjs[8] );
						BigDecimal bdCessato = (BigDecimal)lottoObjs[9];
						Boolean cessato = null;
						if (bdCessato != null && bdCessato.longValue() == 1)
							cessato = true;
						else if (bdCessato != null && bdCessato.longValue() == 0)
							cessato = false;
						lotto.setCessato( cessato );
						Timestamp dtCessa = (Timestamp)lottoObjs[10];
						if (dtCessa != null ){
								lotto.setDataCessazione( new Date(dtCessa.getTime()) );
						}
						lotto.setComune(comune);
						ricerca.setLotto(lotto);
						listaRicercheLotti.add(ricerca);
					}

					//for (int index=0; index<lstAziendeObjs.size(); index++){
					AziendaLotto aziendaLotto = new AziendaLotto();
					Azienda azienda = new Azienda();
					Insediamento ins= new Insediamento();
					aziendaLotto.setDataFineStatus( (String)lottoObjs[11] );
					bdStatus = (BigDecimal)lottoObjs[12];
					if(bdStatus==null)
						aziendaLotto.setStatus(false);
					else
						aziendaLotto.setStatus( bdStatus.intValue()==1?true:false );
					aziendaLotto.setBelfiore( (String)lottoObjs[18] );
					BigDecimal bdIde = (BigDecimal)lottoObjs[19];
					if(bdIde!=null)aziendaLotto.setPkAziendaLotto( bdIde.longValue() );
					ins.setAziendaLotto(aziendaLotto);
					
					azienda.setRagioneSociale( (String)lottoObjs[13] );
					azienda.setAttivita( (String)lottoObjs[14] );
					azienda.setTelefono( (String)lottoObjs[15] );
					azienda.setViaCivico( (String)lottoObjs[16] );
					bdCodiceAsi = (BigDecimal)lottoObjs[17];
					azienda.setCodiceAsi( bdCodiceAsi!=null?bdCodiceAsi.longValue():0l );
					azienda.setBelfiore( (String)lottoObjs[18] );
					ins.setAzienda(azienda);
					ricerca.getInsediamenti().add(ins);
				}
			}
		
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String searchRicercheTree(String lottiAsiCodes){
		String esito = "ricercheBean.searchRicerche";
		logger.info(RicercheBean.class + ".searchRicercheTree(String lottiAsiCodes)");
		listaRicercheLotti = new ArrayList<RicercaLottoInsediamenti>();

		String sql="select distinct * from ( select lotti.codice_asi, lotti.area, lotti.perimetro, lotti.indirizzo, lotti.istatc, lotti.note, lotti.status_ultimo,comuni.nome, lotti.belfiore, lotti.cessato, lotti.data_cessazione, " +
		"al.data_fine_status, al.status, az.ragione_sociale, az.attivita, az.telefono, az.via_civico, az.codice_asi as codice_asi_azienda, al.belfiore as belfiore_azienda, al.pk_azienda_lotto  " +
		"from lotti   " +
		"left join aziende_lotti al on lotti.BELFIORE=AL.BELFIORE and lotti.CODICE_ASI=AL.CODICE_ASI_LOTTO  " +
		"left join aziende az on lotti.BELFIORE=AZ.BELFIORE and AL.CODICE_ASI_AZIENDA=AZ.CODICE_ASI  " +
		"left join comuni on comuni.istatp = substr(lotti.istatc, 0, 3) and comuni.istatc = substr(lotti.istatc, 4, 3)  " +
		"where (AL.DATA_FINE_STATUS=(select max(data_fine_status) from aziende_lotti where lotti.BELFIORE=BELFIORE and lotti.CODICE_ASI=CODICE_ASI_LOTTO AND AZ.CODICE_ASI = CODICE_ASI_AZIENDA) or AL.DATA_FINE_STATUS is null) " +
		"and lotti.belfiore='"+getEnte()+"' ";

		if (lottiAsiCodes != null && !lottiAsiCodes.trim().equalsIgnoreCase("")){
			
				sql += "and lotti.codice_asi IN ( " + lottiAsiCodes + " ) ";
				
				sql +="order by LOTTI.CODICE_ASI, AL.CODICE_ASI_AZIENDA) order by CODICE_ASI ";
				
				logger.info(sql);
				
				List<Object[]> lstLottiObjs = gitLandService.getList(sql, false);
				Lotto prevLotto= new Lotto();
				if (lstLottiObjs != null && lstLottiObjs.size()>0){
					RicercaLottoInsediamenti ricerca =null;
					Lotto lotto =null;
					BigDecimal bdStatus =null;
					BigDecimal bdCodiceAsi =null;
					
					for (int i=0; i<lstLottiObjs.size(); i++){
						Object[] lottoObjs = lstLottiObjs.get(i);
						bdCodiceAsi = (BigDecimal)lottoObjs[0];
						if(bdCodiceAsi.compareTo(new BigDecimal(prevLotto.getCodiceAsi()))!=0){
							ricerca= new RicercaLottoInsediamenti();
							lotto = new Lotto();
							Comune comune = new Comune();
							prevLotto=lotto;
							 
							lotto.setCodiceAsi( bdCodiceAsi!=null?bdCodiceAsi.longValue():0 );
							BigDecimal bdArea = (BigDecimal)lottoObjs[1]; 
							lotto.setArea( bdArea.doubleValue() );
							BigDecimal bdPerimetro = (BigDecimal)lottoObjs[2];
							lotto.setPerimetro( bdPerimetro.doubleValue() );
							//lotto.setIndirizzo( (String)lottoObjs[3] );
							lotto.setIstat( (String)lottoObjs[4] );
							lotto.setNote( (String)lottoObjs[5] );
							bdStatus = (BigDecimal)lottoObjs[6];
							Boolean status = null;
							if (bdStatus != null && bdStatus.longValue() == 1)
								status = true;
							else if (bdStatus != null && bdStatus.longValue() == 0)
								status = false;
							lotto.setStatusUltimo( status );
							comune.setNome( (String)lottoObjs[7] );
							lotto.setBelfiore( (String)lottoObjs[8] );
							BigDecimal bdCessato = (BigDecimal)lottoObjs[9];
							Boolean cessato = null;
							if (bdCessato != null && bdCessato.longValue() == 1)
								cessato = true;
							else if (bdCessato != null && bdCessato.longValue() == 0)
								cessato = false;
							lotto.setCessato( cessato );
							Timestamp dtCessa = (Timestamp)lottoObjs[10];
							if (dtCessa != null ){
									lotto.setDataCessazione( new Date(dtCessa.getTime()) );
							}
							lotto.setComune(comune);
							ricerca.setLotto(lotto);
							listaRicercheLotti.add(ricerca);
						}

						//for (int index=0; index<lstAziendeObjs.size(); index++){
						AziendaLotto aziendaLotto = new AziendaLotto();
						Azienda azienda = new Azienda();
						Insediamento ins= new Insediamento();
						aziendaLotto.setDataFineStatus( (String)lottoObjs[11] );
						bdStatus = (BigDecimal)lottoObjs[12];
						if(bdStatus==null)
							aziendaLotto.setStatus(false);
						else
							aziendaLotto.setStatus( bdStatus.intValue()==1?true:false );
						aziendaLotto.setBelfiore( (String)lottoObjs[18] );
						BigDecimal bdIde = (BigDecimal)lottoObjs[19];
						if(bdIde!=null)aziendaLotto.setPkAziendaLotto( bdIde.longValue() );
						ins.setAziendaLotto(aziendaLotto);
						
						azienda.setRagioneSociale( (String)lottoObjs[13] );
						azienda.setAttivita( (String)lottoObjs[14] );
						azienda.setTelefono( (String)lottoObjs[15] );
						azienda.setViaCivico( (String)lottoObjs[16] );
						bdCodiceAsi = (BigDecimal)lottoObjs[17];
						azienda.setCodiceAsi( bdCodiceAsi!=null?bdCodiceAsi.longValue():0l );
						azienda.setBelfiore( (String)lottoObjs[18] );
						ins.setAzienda(azienda);
						ricerca.getInsediamenti().add(ins);
					}
				}

				
		}		
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String resetForm(){
		String esito = "ricercheBean.resetForm";
		logger.info(RicercheBean.class + ".resetForm");
		
		init();
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String onInsediamentoRowSelect(SelectEvent event)throws IOException{
		logger.info(RicercheBean.class + ".onInsediamentoRowSelect");
		String esito="success";
		aziendaObjs= (Object[])event.getObject();
		aziendaShowMod();
		return esito;
	}//-------------------------------------------------------------------------
	
	
	public String onPraticaRowSelect(SelectEvent event)throws IOException{
		logger.info(RicercheBean.class + ".onPraticaRowSelect");
		String esito="success";
		praticaCur= (Pratica)event.getObject();;
		praticaShowMod();
		return esito;
	}//-------------------------------------------------------------------------
	
	public String onRowSelect(SelectEvent event) throws IOException{
		logger.info(RicercheBean.class + ".onRowSelect");
		String esito = "ricercheBean.onRowSelect";
		/*
		 * Anche se la ricerca visualizza in ogni riga la corrispondenza tra lotti 
		 * e aziende (se esiste) il dettaglio deve essere basato sulle informazioni
		 * riguardanti il lotto:
		 * - info del lotto
		 * - info aziende legate al lotto
		 * - info pratiche legate al lotto
		 */
		RicercaLottoInsediamenti r = (RicercaLottoInsediamenti) event.getObject();
		//Azienda a = r.getAzienda();
		Lotto l = r.getLotto();
		AziendaLotto al = r.getInsediamenti().get(0).getAziendaLotto();
		
		if (l != null){
			Hashtable htQry = new Hashtable();
			ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
			if (l.getCodiceAsi() != null && l.getCodiceAsi()>0 ){
				FiltroDTO f0 = new FiltroDTO();
				f0.setAttributo("codiceAsi");
				f0.setParametro("codiceAsi");
				f0.setOperatore("=");
				f0.setValore( l.getCodiceAsi() );
				alFiltri.add(f0);
				
				FiltroDTO f1 = new FiltroDTO();
				f1.setAttributo("belfiore");
				f1.setParametro("belfiore");
				f1.setOperatore("=");
				f1.setValore( l.getBelfiore() );
				alFiltri.add(f1);
				
			}
			if (alFiltri != null && alFiltri.size()>0){
				htQry.put("where", alFiltri);
				htQry.put("limit", 1);
				List lst = gitLandService.getList(htQry, Lotto.class);
				if (lst != null && lst.size()>0)
					lottoCur = (Lotto)lst.get(0);
				
			}
		}
				
		if (lottoCur != null){
			/*
			 * recupero comune del lotto e suoi insediamenti
			 */
			String istat = lottoCur.getIstat();
			String istatp = istat.substring(0, 3);
			String istatc = istat.substring(3, 6);
			Hashtable htQry = new Hashtable();
			ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
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
				comuneLotCur = (Comune)listaComuni.get(0);

			/*
			 * Valorizzo l'elenco storico degli insediamenti per questo lotto
			 */
//			listaInsediamenti = new ArrayList<Object[]>();
//			
//			String sql = "select al.codice_asi_lotto, al.codice_asi_azienda, al.data_fine_status, al.status, "
//					+ "aziende.codice_asi, aziende.ragione_sociale, aziende.rappr_legale, aziende.via_civico, comuni.nome, aziende.num_addetti, aziende.p_iva, aziende.cod_fiscale, aziende.telefono, aziende.email "
//					+ "from aziende_lotti al "
//					+ "left join aziende on al.codice_asi_azienda = aziende.codice_asi "
//					+ "left join comuni on aziende.istatc = comuni.istatc and aziende.istatp = comuni.istatp "
//					+ "where al.codice_asi_lotto = '" + lottoCur.getCodiceAsi() + "' "
//					+ "order by al.data_fine_status desc, al.codice_asi_azienda ";
//			
//			logger.info(sql);
//			
//			listaInsediamenti = gitLandService.getList(sql, false);
			loadInsediamentiLst();
			
			/*
			 * Valorizziamo le pratiche relative al lotto
			 */
			htQry = new Hashtable();
			alFiltri = new ArrayList<FiltroDTO>();
			
			FiltroDTO f0 = new FiltroDTO();
			f0.setAttributo("belfiore");
			f0.setParametro("belfiore");
			f0.setOperatore("=");
			f0.setValore( getEnte() );
			alFiltri.add(f0);
			
			FiltroDTO f1 = new FiltroDTO();
			f1.setAttributo("codiceAsiLotto");
			f1.setParametro("codiceAsiLotto");
			f1.setOperatore("=");
			f1.setValore( lottoCur.getCodiceAsi().toString() );
			alFiltri.add(f1);
			
			htQry.put("where", alFiltri);
			
			htQry.put("orderBy", "codicePratica");
			listaPratiche = gitLandService.getList(htQry, Pratica.class);
			
			setAziendaInPratica();
			
		}

		if (al != null && al.getPkAziendaLotto() > 0)
			aziendaLottoCur = (AziendaLotto)gitLandService.getItemById(al.getPkAziendaLotto(), AziendaLotto.class);
		
		//if (aziendaCur != null && aziendaCur.getPkAzienda() != null && aziendaCur.getPkAzienda().longValue()>0){
		if (lottoCur != null && lottoCur.getPkLotto() != null && lottoCur.getPkLotto().longValue()>0){
			/*
			 * La mappa potrebbe già essere stata usata per impostare il filtro 
			 * di ricerca ... per cui adesso cambiamo la chiamata 
			 */
			mapMan = (MapMan) getBeanReference("mapMan");
			if (lottoCur != null){
				List<LottoCoordinate> lstCoor = lottoCur.getCoordinate();
				if (lstCoor != null && lstCoor.size()>0){
					LottoCoordinate lc = lstCoor.get(0);
					mapMan.initializeData(lottoCur.getCodiceAsi().toString(), (lc!=null && lc.getFoglio()!=null)?lc.getFoglio():"");
				}
			}else
				mapMan.initializeData();	
			
			//La chiamata ad un ajax listener è diversa da una action e pertanto
			//se deve cambiare pagina va indicato il redirect
			FacesContext.getCurrentInstance().getExternalContext().redirect("infoView.faces");
			
		}else{
			
			esito = "failure";

		}
		
		return esito;
	}//-------------------------------------------------------------------------
	
//	public String lottoSaveMod( ActionEvent actionEvent ){
	public String lottoSaveMod(){
		logger.info(RicercheBean.class + ".lottoSaveMod");
		String esito = "ricercheBean.lottoSaveMod";
		
		gitLandService.updItem(lottoCur);
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String lottoShowMod(){
		logger.info(RicercheBean.class + ".lottoShowMod");
		String esito = "ricercheBean.lottoShowMod";
		
		lottoMod = (Lotto)gitLandService.getItemById(lottoCur.getPkLotto(), Lotto.class);
		
		return esito;
	}//-------------------------------------------------------------------------
	
//	public String aziendaShowMod(SelectEvent event){
	public String aziendaShowMod(){
		logger.info(RicercheBean.class + ".aziendaShowMod");
		String esito = "ricercheBean.aziendaShowMod";
		
//		Object[] a = (Object[]) event.getObject();
//		BigDecimal codiceAsi = (BigDecimal)a[1];
		BigDecimal codiceAsi = (BigDecimal)aziendaObjs[1];
		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		FiltroDTO f0 = new FiltroDTO();
		f0.setAttributo("belfiore");
		f0.setParametro("belfiore");
		f0.setOperatore("=");
		f0.setValore( getEnte() );
		alFiltri.add(f0);
		if (codiceAsi != null ){
			FiltroDTO f1 = new FiltroDTO();
			f1.setAttributo("codiceAsi");
			f1.setParametro("codiceAsi");
			f1.setOperatore("=");
			f1.setValore( codiceAsi.longValue() );
			alFiltri.add(f1);
		}
		if (alFiltri != null && alFiltri.size()>0){
			htQry.put("where", alFiltri);
			htQry.put("limit", 1);
			listaAziende = gitLandService.getList(htQry, Azienda.class);
		}
		if (listaAziende != null && listaAziende.size()>0)
			aziendaCur = (Azienda)listaAziende.get(0);
		
		htQry = new Hashtable();
		alFiltri = new ArrayList<FiltroDTO>();
		String istatc = aziendaCur.getIstatc();
		String istatp = aziendaCur.getIstatp();
		if (istatc != null && !istatc.trim().equalsIgnoreCase("") ){
			FiltroDTO f1 = new FiltroDTO();
			f1.setAttributo("istatc");
			f1.setParametro("istatc");
			f1.setOperatore("=");
			f1.setValore( istatc );
			alFiltri.add(f1);
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
			comuneAziCur = (Comune)listaComuni.get(0);
		
		aziendaCur.setComune(comuneAziCur);
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String aziendaSaveMod(){
		logger.info(RicercheBean.class + ".aziendaSaveMod");
		String esito = "ricercheBean.aziendaSaveMod";
		
		gitLandService.updItem(aziendaCur);
		
//		String sql = "select al.codice_asi_lotto, al.codice_asi_azienda, al.data_fine_status, al.status, "
//				+ "aziende.codice_asi, aziende.ragione_sociale, aziende.rappr_legale, aziende.via_civico, comuni.nome, aziende.num_addetti, aziende.p_iva, aziende.cod_fiscale, aziende.telefono, aziende.email "
//				+ "from aziende_lotti al "
//				+ "left join aziende on al.codice_asi_azienda = aziende.codice_asi "
//				+ "left join comuni on aziende.istatc = comuni.istatc and aziende.istatp = comuni.istatp "
//				+ "where al.codice_asi_lotto = '" + lottoCur.getCodiceAsi() + "' "
//				+ "order by al.data_fine_status desc, al.codice_asi_azienda ";
//		
//		logger.info(sql);
//		
//		listaInsediamenti = gitLandService.getList(sql, false);
		loadInsediamentiLst();
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String praticaShowMod(){
//	public String praticaShowMod(SelectEvent event){
		logger.info(RicercheBean.class + ".praticaShowMod");
		String esito = "ricercheBean.praticaShowMod";		
	
//		Pratica p = (Pratica) event.getObject();
		praticaCur = (Pratica)gitLandService.getItemById(praticaCur.getPkPratica(), Pratica.class);

		//carico l'ultima pratica approvata
		praticaUltimaAppr = recuperaUltimaPraticaApprovata(praticaCur.getCodiceAsiLotto());
		
		
		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		FiltroDTO f0 = new FiltroDTO();
		f0.setAttributo("belfiore");
		f0.setParametro("belfiore");
		f0.setOperatore("=");
		f0.setValore( getEnte() );
		alFiltri.add(f0);
		if (praticaCur.getCodiceAsiAzienda() != null ){
			FiltroDTO f1 = new FiltroDTO();
			f1.setAttributo("codiceAsi");
			f1.setParametro("codiceAsi");
			f1.setOperatore("=");
			f1.setValore( praticaCur.getCodiceAsiAzienda().toString() );
			alFiltri.add(f1);
		}
		if (alFiltri != null && alFiltri.size()>0){
			htQry.put("where", alFiltri);
			htQry.put("limit", 1);
			listaAziende = gitLandService.getList(htQry, Azienda.class);
		}
		if (listaAziende != null && listaAziende.size()>0)
			aziendaCur = (Azienda)listaAziende.get(0);
		
		praticaCur.setAzienda(aziendaCur);
		
		//preparaDocumento();
        
		
		loadTipologieItems(true);
		loadUtentiOperatoreItems(false);
		
		return esito;
	}//-------------------------------------------------------------------------
	

	public String praticaShowDel(){
			logger.info(RicercheBean.class + ".praticaShowDel");
			String esito = "ricercheBean.praticaShowDel";

			praticaCur = (Pratica)gitLandService.getItemById(praticaCur.getPkPratica(), Pratica.class);
			
			return esito;
	}//-------------------------------------------------------------------------
	
	public String praticaShowNew(){
			logger.info(RicercheBean.class + ".praticaShowNew");
			String esito = "ricercheBean.praticaShowNew";

			BigDecimal codiceAsi = (BigDecimal)aziendaObjs[1];

			
			Hashtable htQry = new Hashtable();
			ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
			FiltroDTO f1 = new FiltroDTO();
			f1.setAttributo("belfiore");
			f1.setParametro("belfiore");
			f1.setOperatore("=");
			f1.setValore( getEnte() );
			alFiltri.add(f1);
			if (codiceAsi != null ){
				FiltroDTO f0 = new FiltroDTO();
				f0.setAttributo("codiceAsi");
				f0.setParametro("codiceAsi");
				f0.setOperatore("=");
				f0.setValore( codiceAsi.longValue() );
				alFiltri.add(f0);
			}
			if (alFiltri != null && alFiltri.size()>0){
				htQry.put("where", alFiltri);
				htQry.put("limit", 1);
				listaAziende = gitLandService.getList(htQry, Azienda.class);
			}
			if (listaAziende != null && listaAziende.size()>0)
				aziendaCur = (Azienda)listaAziende.get(0);

			praticaCur = new Pratica();
			praticaCur.setCodiceAsiAzienda(aziendaCur.getCodiceAsi());
			praticaCur.setAzienda(aziendaCur);
			praticaCur.setCodiceAsiLotto(lottoCur.getCodiceAsi());
			praticaCur.setLotto(lottoCur);
			praticaCur.setBelfiore(getEnte());
			praticaCur.setChiudip("0");
			praticaCur.setAllegati(new ArrayList<PraticaAllegato>());

			//carico l'ultima pratica approvata
			praticaUltimaAppr = recuperaUltimaPraticaApprovata(praticaCur.getCodiceAsiLotto());
			
			loadTipologieItems(true);
			loadUtentiOperatoreItems(false);
			
			return esito;
	}//-------------------------------------------------------------------------
	
	public String praticaSaveMod(){
		logger.info(RicercheBean.class + ".praticaSaveMod");
		String esito = "ricercheBean.praticaSaveMod";
		
		praticaCur.setTipologia(getTipologiaByCod(praticaCur.getCodTipo()));
		gitLandService.updItem(praticaCur);
		
		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		FiltroDTO f0 = new FiltroDTO();
		f0.setAttributo("belfiore");
		f0.setParametro("belfiore");
		f0.setOperatore("=");
		f0.setValore( getEnte() );
		alFiltri.add(f0);
		FiltroDTO f1 = new FiltroDTO();
		f1.setAttributo("codiceAsiLotto");
		f1.setParametro("codiceAsiLotto");
		f1.setOperatore("=");
		f1.setValore( lottoCur.getCodiceAsi().toString() );
		alFiltri.add(f1);
		
		htQry.put("where", alFiltri);
		htQry.put("orderBy", "codicePratica");
		listaPratiche = gitLandService.getList(htQry, Pratica.class);
		
		setAziendaInPratica();
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String praticaSaveNew(){
		logger.info(RicercheBean.class + ".praticaSaveNew");
		String esito = "ricercheBean.praticaSaveNew";
		
		praticaCur.setTipologia(getTipologiaByCod(praticaCur.getCodTipo()));
		gitLandService.addItem(praticaCur);
		
		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		FiltroDTO f0 = new FiltroDTO();
		f0.setAttributo("belfiore");
		f0.setParametro("belfiore");
		f0.setOperatore("=");
		f0.setValore( getEnte() );
		alFiltri.add(f0);
		FiltroDTO f1 = new FiltroDTO();
		f1.setAttributo("codiceAsiLotto");
		f1.setParametro("codiceAsiLotto");
		f1.setOperatore("=");
		f1.setValore( lottoCur.getCodiceAsi().toString() );
		alFiltri.add(f1);
		
		htQry.put("where", alFiltri);
		listaPratiche = gitLandService.getList(htQry, Pratica.class);
		
		setAziendaInPratica();
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String praticaSaveDel(){
		logger.info(RicercheBean.class + ".praticaSaveDel");
		String esito = "ricercheBean.praticaSaveDel";

		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();

//		FiltroDTO f0 = new FiltroDTO();
//		f0.setAttributo("belfiore");
//		f0.setParametro("belfiore");
//		f0.setOperatore("=");
//		f0.setValore( getEnte() );
//		alFiltri.add(f0);
//		FiltroDTO f1 = new FiltroDTO();
//		f1.setAttributo("codicePratica");
//		f1.setParametro("codicePratica");
//		f1.setOperatore("=");
//		f1.setValore( praticaCur.getCodicePratica() );
//		alFiltri.add(f1);
//		htQry.put("where", alFiltri);
//		gitLandService.delList(htQry, Pratica.class);
		gitLandService.delItemById(praticaCur.getPkPratica(), Pratica.class, true);
		
		htQry = new Hashtable();
		alFiltri = new ArrayList<FiltroDTO>();
		FiltroDTO f0 = new FiltroDTO();
		f0.setAttributo("belfiore");
		f0.setParametro("belfiore");
		f0.setOperatore("=");
		f0.setValore( getEnte() );
		alFiltri.add(f0);
		FiltroDTO f1 = new FiltroDTO();
		f1.setAttributo("codiceAsiLotto");
		f1.setParametro("codiceAsiLotto");
		f1.setOperatore("=");
		f1.setValore( lottoCur.getCodiceAsi().toString() );
		alFiltri.add(f1);
		htQry.put("where", alFiltri);
		listaPratiche = gitLandService.getList(htQry, Pratica.class);

		setAziendaInPratica();

		return esito;
	}//-------------------------------------------------------------------------
	
	public String insediamentoShowNew(){
		System.out.println(RicercheBean.class + ".insediamentoShowNew");
		String esito = "ricercheBean.insediamentoShowNew";

		aziendeBean = (AziendeBean)getBeanReference("aziendeBean");
		aziendeBean.setListaAziende( new ArrayList<AziendaView>() ) ;
		aziendeBean.setAziendaNew(new Azienda());
		
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String insediamentoSaveNew(){
		logger.info(RicercheBean.class + ".insediamentoSaveNew");
		String esito = "ricercheBean.insediamentoSaveNew";

		if (lottoCur != null && aziendaMod != null){
			/*
			 * Questo lotto selezionato potrebbe avere piu fogli eparticelle collegati
			 * per cui devo aggiornare tutte le righe ad esso corrispondenti
			 */
			
			Hashtable htQry = new Hashtable();
			ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
			FiltroDTO f0 = new FiltroDTO();
			f0.setAttributo("belfiore");
			f0.setParametro("belfiore");
			f0.setOperatore("=");
			f0.setValore( getEnte() );
			alFiltri.add(f0);
			FiltroDTO f1 = new FiltroDTO();
			f1.setAttributo("codiceAsi");
			f1.setParametro("codiceAsi");
			f1.setOperatore("=");
			f1.setValore( lottoCur.getCodiceAsi().toString() );
			alFiltri.add(f1);
			htQry.put("where", alFiltri);
			List<Lotto> ll = gitLandService.getList(htQry, Lotto.class);
			if (ll != null && ll.size()>0){
				Iterator<Lotto> itLot = ll.iterator();
				while(itLot.hasNext()){
					Lotto iLotto = itLot.next();

					Lotto l = (Lotto)gitLandService.getItemById(iLotto.getPkLotto(), Lotto.class);

					aziendaLottoCur = new AziendaLotto();
					aziendaLottoCur.setCodiceAsiAzienda(aziendaMod.getCodiceAsi());
					aziendaLottoCur.setCodiceAsiLotto(l.getCodiceAsi());
					aziendaLottoCur.setDataFineStatus("31/12/9999");
					aziendaLottoCur.setFkAzienda(aziendaMod.getPkAzienda());
					aziendaLottoCur.setFkLotto(l.getPkLotto());
					aziendaLottoCur.setStatus(true);
					aziendaLottoCur.setBelfiore( getEnte() );
					aziendaLottoCur.setPkAziendaLotto(null);
					gitLandService.addItem(aziendaLottoCur);
					
					//l.setDataUltimaModifica( new Date(System.currentTimeMillis()) );
					Long nal = 0l;
					Long naa = 0l;
					if (l.getNumAddetti() != null ){
						nal = l.getNumAddetti();
					}
					if (aziendaMod.getNumAddetti() != null ){
						naa = aziendaMod.getNumAddetti();
					}

					l.setNumAddetti( nal + naa );
					l.setStatusUltimo(true);
					
					lottoCur=(Lotto)gitLandService.updItem(l);
					
				}
			}

			/*
			 * ricarica insediamenti
			 */
			loadInsediamentiLst();
			
		}else
			logger.info("LOTTO E/O AZIENDA NON VALORIZZATI!!!");
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String insediamentoShowDel(){
		logger.info(RicercheBean.class + ".insediamentoShowDel");
		String esito = "ricercheBean.insediamentoShowDel";

		BigDecimal codiceAsi = (BigDecimal)aziendaObjs[1];
		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		FiltroDTO f0 = new FiltroDTO();
		f0.setAttributo("belfiore");
		f0.setParametro("belfiore");
		f0.setOperatore("=");
		f0.setValore( getEnte() );
		alFiltri.add(f0);
		if (codiceAsi != null ){
			FiltroDTO f1 = new FiltroDTO();
			f1.setAttributo("codiceAsi");
			f1.setParametro("codiceAsi");
			f1.setOperatore("=");
			f1.setValore( codiceAsi.longValue() );
			alFiltri.add(f1);
		}
		if (alFiltri != null && alFiltri.size()>0){
			htQry.put("where", alFiltri);
			htQry.put("limit", 1);
			listaAziende = gitLandService.getList(htQry, Azienda.class);
		}
		if (listaAziende != null && listaAziende.size()>0)
			aziendaCur = (Azienda)listaAziende.get(0);

		
		return esito;
	}//-------------------------------------------------------------------------
	
	public boolean isPraticaReadOnly(){
		//la pratica non è readonly se :
		//	- si è Admin
		//  - la pratica appartiene all'operatore ed è aperta
		
		String ute= getCurrUserName(); 
		
		if(ute==null)return true;
		if(isManager())return false;
		if(praticaCur!=null && ute.equals(praticaCur.getUtenteOperatore()) && !"1".equals(praticaCur.getChiudip()))return false;
		return true;
	}
	public boolean isPraticaReadOnlyEnte(){
		if(isManager())return false;
		if(praticaCur!=null){
			if(isEnte() && "1".equals(praticaCur.getChiudip()))return false;
		}
		return true;
	}
	
	public boolean isDirigente(){
		return isManager();
	}
	
	public String insediamentoSaveDel(){
		logger.info(RicercheBean.class + ".insediamentoSaveDel");
		String esito = "ricercheBean.insediamentoSaveDel";

		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		
		FiltroDTO f0 = new FiltroDTO();
		f0.setAttributo("belfiore");
		f0.setParametro("belfiore");
		f0.setOperatore("=");
		f0.setValore( getEnte() );
		alFiltri.add(f0);
		
		FiltroDTO f2 = new FiltroDTO();
		f2.setAttributo("codiceAsiAzienda");
		f2.setParametro("codiceAziAzienda");
		f2.setOperatore("=");
		f2.setValore( aziendaCur.getCodiceAsi() );
		alFiltri.add(f2);

		FiltroDTO f1 = new FiltroDTO();
		f1.setAttributo("codiceAsiLotto");
		f1.setParametro("codiceAziLotto");
		f1.setOperatore("=");
		f1.setValore( lottoCur.getCodiceAsi() );
		alFiltri.add(f1);

		htQry.put("where", alFiltri);
		gitLandService.delList(htQry, AziendaLotto.class);
		List<Pratica> eliminate= new ArrayList<Pratica>();
		//elimino le pratiche dell'insediamento
		for (Pratica pra : listaPratiche) {
			if(aziendaCur.getCodiceAsi().equals(pra.getCodiceAsiAzienda())){
				gitLandService.delItemById(pra.getPkPratica(), Pratica.class, true);
				eliminate.add(pra);
			}
		}
		listaPratiche.removeAll(eliminate);
		loadInsediamentiLst();
		/*
		 * Se la lista degli insediamenti è vuota dobbiamo aggiornare lo status del 
		 * lotto (per ogni foglio e particella) a libero
		 */
		if (listaInsediamenti != null && listaInsediamenti.size()>0){
			
		}else{
			lottoMod = (Lotto)gitLandService.getItemById(lottoCur.getPkLotto(), Lotto.class);
			lottoMod.setStatusUltimo(false);
			lottoCur=(Lotto)gitLandService.updItem(lottoMod);
		}
		
		return esito;
	}//-------------------------------------------------------------------------
	
	private void loadInsediamentiLst(){
		listaInsediamenti = new ArrayList<Object[]>();

		String sql = "select distinct al.codice_asi_lotto, al.codice_asi_azienda, al.data_fine_status, al.status, "
				+ "aziende.codice_asi, aziende.ragione_sociale, aziende.rappr_legale, aziende.via_civico, comuni.nome, aziende.num_addetti, aziende.p_iva, aziende.cod_fiscale, aziende.telefono, aziende.email "
				+ "from aziende_lotti al "
				+ "left join aziende on al.codice_asi_azienda = aziende.codice_asi "
				+ "left join comuni on aziende.istatc = comuni.istatc and aziende.istatp = comuni.istatp "
				+ "where al.belfiore = '" + getEnte() + "'  and al.belfiore = aziende.belfiore and al.codice_asi_lotto = '" + lottoCur.getCodiceAsi() + "' "
				+ "order by TO_DATE(al.data_fine_status, 'dd/mm/yyyy') desc, al.codice_asi_azienda ";
		
		logger.info(sql);
		
		listaInsediamenti = gitLandService.getList(sql, false);
	}//-------------------------------------------------------------------------
	
	private void setAziendaInPratica(){
		if (listaPratiche != null && listaPratiche.size()>0){
			Iterator<Pratica> itPratica = listaPratiche.iterator();
			while(itPratica.hasNext()){
				Pratica p = itPratica.next();
				/*
				 * recuperiamo azienda legata alla pratica
				 */
				Hashtable htQry = new Hashtable();
				ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
				
				FiltroDTO f0 = new FiltroDTO();
				f0.setAttributo("belfiore");
				f0.setParametro("belfiore");
				f0.setOperatore("=");
				f0.setValore( getEnte() );
				alFiltri.add(f0);
				
				FiltroDTO f2 = new FiltroDTO();
				f2.setAttributo("codiceAsi");
				f2.setParametro("codiceAsi");
				f2.setOperatore("=");
				f2.setValore( p.getCodiceAsiAzienda().toString() );
				alFiltri.add(f2);
				
				htQry.put("where", alFiltri);
				htQry.put("limit", 1);
				List<Azienda> lstAz = gitLandService.getList(htQry, Azienda.class);
				if (lstAz != null && lstAz.size()>0)
					p.setAzienda( (Azienda)lstAz.get(0) );


			}
		}
	}//-------------------------------------------------------------------------
	
	private void loadTipologieItems(Boolean firstEmpty){
		
		String sql = "select cod_tipo, tipologia "
				+ "from tipologia "
				+ "order by cod_tipo, tipologia ";
		
		logger.info(sql);
		
		listaTipologieItems = new LinkedList<SelectItem>();
		if (firstEmpty){
			SelectItem si0 = new SelectItem();
			si0.setDescription("");
			si0.setLabel("");
			si0.setValue("");
			listaTipologieItems.add(si0);
		}
		List<Object[]> tipologieObjs = gitLandService.getList(sql, false);
		Collections.sort(tipologieObjs, new Comparator<Object[]>() {
			@Override
			public int compare(Object[] o1, Object[] o2) {
				try {
					Long i1=Long.parseLong((String)o1[0]);
					Long i2=Long.parseLong((String)o2[0]);
					return i1.compareTo(i2);
				} catch (Exception e) {
					// In caso di errore va in fondo
					return 1;
				}
			}
		} );
		if (tipologieObjs != null && tipologieObjs.size()>0){
			Iterator<Object[]> itTipoObjs = tipologieObjs.iterator();
			while(itTipoObjs.hasNext()){
				Object[] objTipol = itTipoObjs.next();
				String cod = (String)objTipol[0];
				String des = (String)objTipol[1];

				SelectItem si = new SelectItem();
				si.setDescription(des);
				si.setLabel(cod + " - " + des);
				si.setValue(cod);
				listaTipologieItems.add(si);
				
			}
		}
	}//-------------------------------------------------------------------------
	
	private void loadUtentiOperatoreItems(Boolean firstEmpty){
		
		listaUtentiOperatoreItems = new LinkedList<SelectItem>();
		if (firstEmpty){
			SelectItem si0 = new SelectItem();
			si0.setDescription("");
			si0.setLabel("");
			si0.setValue("");
			listaUtentiOperatoreItems.add(si0);
		}
		List<Object[]> utentiObjs = getListaUtenti();
		if (utentiObjs != null && utentiObjs.size()>0){
			Iterator<Object[]> itTipoObjs = utentiObjs.iterator();
			while(itTipoObjs.hasNext()){
				
				Object[] objUte = itTipoObjs.next();

				SelectItem si = new SelectItem();
				si.setDescription(objUte[0].toString());
				si.setLabel(objUte[0]+ " - "+ objUte[5]);
				si.setValue(objUte[0]);
				if("Base".equals(objUte[5]))listaUtentiOperatoreItems.add(si);
				
			}
		}
	}//-------------------------------------------------------------------------
	
	
	private String getTipologiaByCod(String codTipo) {
		if(StringUtils.isNotBlank(codTipo)){
			String strQry="select tipologia from Tipologia where cod_tipo='"+codTipo+"'";
			List<Object[]>lista =  gitLandService.getList(strQry, false);
			Object ret=lista.get(0);
			if(lista.size()==1) return ret.toString();
			
		}
		return null;
	}

	public List<Ricerca> getListaRicerche() {
		return listaRicerche;
	}

	public void setListaRicerche(List<Ricerca> listaRicerche) {
		this.listaRicerche = listaRicerche;
	}

	public Ricerca getRicercaCur() {
		return ricercaCur;
	}

	public void setRicercaCur(Ricerca ricercaCur) {
		this.ricercaCur = ricercaCur;
	}

	public String getStatusUltimo() {
		return statusUltimo;
	}

	public void setStatusUltimo(String statusUltimo) {
		this.statusUltimo = statusUltimo;
	}

	public Tab getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(Tab activeTab) {
		this.activeTab = activeTab;
	}

	public Azienda getAziendaCur() {
		return aziendaCur;
	}

	public void setAziendaCur(Azienda aziendaCur) {
		this.aziendaCur = aziendaCur;
	}

	public Lotto getLottoCur() {
		return lottoCur;
	}

	public void setLottoCur(Lotto lottoCur) {
		this.lottoCur = lottoCur;
	}

	public Comune getComuneAziCur() {
		return comuneAziCur;
	}

	public void setComuneAziCur(Comune comuneAziCur) {
		this.comuneAziCur = comuneAziCur;
	}

	public Comune getComuneLotCur() {
		return comuneLotCur;
	}

	public void setComuneLotCur(Comune comuneLotCur) {
		this.comuneLotCur = comuneLotCur;
	}

	public AziendaLotto getAziendaLottoCur() {
		return aziendaLottoCur;
	}

	public void setAziendaLottoCur(AziendaLotto aziendaLottoCur) {
		this.aziendaLottoCur = aziendaLottoCur;
	}

	public List<Comune> getListaComuni() {
		return listaComuni;
	}

	public void setListaComuni(List<Comune> listaComuni) {
		this.listaComuni = listaComuni;
	}

	public List<Object[]> getListaInsediamenti() {
		return listaInsediamenti;
	}

	public void setListaInsediamenti(List<Object[]> listaInsediamenti) {
		this.listaInsediamenti = listaInsediamenti;
	}

	public List<Pratica> getListaPratiche() {
		return listaPratiche;
	}

	public void setListaPratiche(List<Pratica> listaPratiche) {
		this.listaPratiche = listaPratiche;
	}

	public Lotto getLottoMod() {
		return lottoMod;
	}

	public void setLottoMod(Lotto lottoMod) {
		this.lottoMod = lottoMod;
	}

	public AziendaView getAziendaMod() {
		return aziendaMod;
	}

	public void setAziendaMod(AziendaView aziendaMod) {
		this.aziendaMod = aziendaMod;
	}

	public List<Azienda> getListaAziende() {
		return listaAziende;
	}

	public void setListaAziende(List<Azienda> listaAziende) {
		this.listaAziende = listaAziende;
	}

	public Pratica getPraticaCur() {
		return praticaCur;
	}

	public void setPraticaCur(Pratica praticaCur) {
		this.praticaCur = praticaCur;
	}

	public Pratica getPraticaUltimaAppr() {
		return praticaUltimaAppr;
	}

	public void setPraticaUltimaAppr(Pratica praticaUltimaAppr) {
		this.praticaUltimaAppr = praticaUltimaAppr;
	}

	public Object[] getAziendaObjs() {
		return aziendaObjs;
	}

	public void setAziendaObjs(Object[] aziendaObjs) {
		this.aziendaObjs = aziendaObjs;
	}

	public AziendeBean getAziendeBean() {
		return aziendeBean;
	}

	public void setAziendeBean(AziendeBean aziendeBean) {
		this.aziendeBean = aziendeBean;
	}

	public String getCessato() {
		return cessato;
	}

	public void setCessato(String cessato) {
		this.cessato = cessato;
	}

	public List<SelectItem> getListaTipologieItems() {
		return listaTipologieItems;
	}

	public void setListaTipologieItems(List<SelectItem> listaTipologieItems) {
		this.listaTipologieItems = listaTipologieItems;
	}

	public it.webred.gitland.web.bean.MapMan getMapMan() {
		return mapMan;
	}

	public void setMapMan(it.webred.gitland.web.bean.MapMan mapMan) {
		this.mapMan = mapMan;
	}

	public List<Object[]> getListaUtenti(){
		return  getUserService().getUtentiByApplicazioneEnte("GitLand", getEnte());
	}
	public String getStilePratica(Pratica pratica){
		String ret="non-modificabile";
		// se la pratica appartiene all'utente oppure l'utente è ente allora abilito la modifica		
		if( getCurrUserName().equals( pratica.getUtenteOperatore()) || isEnte())ret="";
		//il dirigente vede tutte le pratiche come modificabili
		if(isDirigente())ret="";
		//se la pratica è chiusa allora la visualizzo con lo stile relativo
		if("1".equals(pratica.getChiudip()))ret="pratica-chiusa";
		return ret;
	}
	public String selezionaAllegato(PraticaAllegato allegato){
		allegatoCur=allegato;
		return "success";
	}
	public void eliminaAllegato(PraticaAllegato allegato){
		praticaCur.getAllegati().remove(allegato);
	}
	public StreamedContent getAllegatoDaScaricare() {
		allegatoDaScaricare=preparaAllegato();

		return allegatoDaScaricare;
	}
	public StreamedContent preparaAllegato(){
        InputStream stream= null;
        try {
            byte[] tmp=Files.readAllBytes(Paths.get(allegatoCur.getPercorso()));
            stream= new ByteArrayInputStream(tmp);
            Tika tika= new Tika();
            return new DefaultStreamedContent(stream, tika.detect(allegatoCur.getNomeFile()), allegatoCur.getNomeFile());
		} catch (Exception e) {
			logger.error(e);
		}
        return null;
	}
	public void handleFileUpload(FileUploadEvent event) {
		FileOutputStream fos;
		try {
			Calendar oggi= new GregorianCalendar();
			String percorso=getDirDatiDiogene()+getEnte();
			if(Files.notExists(Paths.get(percorso))){
				Files.createDirectory(Paths.get(percorso));
			}
			percorso+="/GITLAND";
			if(Files.notExists(Paths.get(percorso))){
				Files.createDirectory(Paths.get(percorso));
			}
			percorso+="/Pratiche";
			if(Files.notExists(Paths.get(percorso))){
				Files.createDirectory(Paths.get(percorso));
			}
			percorso+="/Allegati";
			if(Files.notExists(Paths.get(percorso))){
				Files.createDirectory(Paths.get(percorso));
			}
			percorso+="/"+oggi.get(Calendar.YEAR);
			if(Files.notExists(Paths.get(percorso))){
				Files.createDirectory(Paths.get(percorso));
			}
			percorso+="/"+(oggi.get(Calendar.MONTH)+1);
			if(Files.notExists(Paths.get(percorso))){
				Files.createDirectory(Paths.get(percorso));
			}
			percorso+="/"+oggi.getTimeInMillis()+"_"+event.getFile().getFileName();
			fos = new FileOutputStream(percorso);
			IOUtils.copy(event.getFile().getInputstream(), fos);
			fos.close();
	        Map<String,String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	        PraticaAllegato allegato= new PraticaAllegato();
	        allegato.setDescrizione(requestParams.get("descrizione"));
	        allegato.setNomeFile(event.getFile().getFileName());
	        allegato.setPercorso(percorso);
	        allegato.setPratica(praticaCur);
	        praticaCur.getAllegati().add(allegato);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		logger.info(event.getFile().getFileName() + " is uploaded.");
        logger.info(event.getComponent().getParent());
        HtmlForm form=(HtmlForm)event.getComponent().getParent();
        InputText intx=(InputText) form.getChildren().get(1);
        logger.info(intx.getValue());
    }	
	public StreamedContent getPraticaCorrenteAsWord() {
		preparaDocumento();
		return praticaCorrenteAsWord;
    }
	private void preparaDocumento() {
        InputStream stream= null;
        try {
            byte[] tmp=Files.readAllBytes(Paths.get(getDirDatiDiogene()+"template/gitland/SchedaIstruttoriaInsediamento.rtf"));
            String template=new String(tmp);
            template=valorizzaDocumento(template);
            stream= new ByteArrayInputStream(template.getBytes());
		} catch (Exception e) {
			logger.error(e);
		}
        praticaCorrenteAsWord=new DefaultStreamedContent(stream, "application/msword", "Pratica.doc");
	}

	private Pratica recuperaUltimaPraticaApprovata(Long codiceAsiLotto) {

		Pratica ret= new Pratica();
		ret.setPkPratica(null);
		
		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		FiltroDTO f0 = new FiltroDTO();
		f0.setAttributo("belfiore");
		f0.setParametro("belfiore");
		f0.setOperatore("=");
		f0.setValore( getEnte() );
		alFiltri.add(f0);
		FiltroDTO f1 = new FiltroDTO();
		f1.setAttributo("codiceAsiLotto");
		f1.setParametro("codiceAsiLotto");
		f1.setOperatore("=");
		f1.setValore(codiceAsiLotto);
		alFiltri.add(f1);
		
		//_IS_NOT_NULL_   {d 'yyyy-mm-dd'}
		FiltroDTO f2 = new FiltroDTO();
		f2.setAttributo("nullaOstaData");
		f2.setParametro("nullaOstaData");
		f2.setOperatore("_IS_NOT_NULL_");
		alFiltri.add(f2);
		
		htQry.put("where", alFiltri);
		htQry.put("limit", 1);
		htQry.put("orderBy", "nullaOstaData DESC");
		List<Pratica> lstPrat = gitLandService.getList(htQry, Pratica.class);
		if (lstPrat != null && lstPrat.size()>0)
			ret = (Pratica)lstPrat.get(0);
		
		return ret;
	}

	private String valorizzaDocumento(String template) {
		String ret = template;
		ret=replaceTagsWithProperties(ret,praticaCur,"Pratica");
		ret=replaceTagsWithProperties(ret,praticaUltimaAppr,"PraticaAppr",praticaUltimaAppr.getPkPratica()==null);
		ret=replaceTagsWithProperties(ret,lottoCur,"Lotto");
		ret=replaceTagsWithProperties(ret,aziendaCur,"Azienda");
		ret=replaceTagDettaglioLotto(ret);
		return ret;
	}
	private String replaceTagDettaglioLotto(String ret) {
		//<@DatiCatastali@>
		String query="from LottoCoordinate l where l.codiceAsi="+lottoCur.getCodiceAsi() +" and l.belfiore='"+getEnte()+"'";
		List coordinate= getGitLandService().getList(query, true);
		String datiCatastali="";
		for (Object coord : coordinate) {
			datiCatastali+="\\\\tab Foglio "+((LottoCoordinate)coord).getFoglio()+", Particella "+((LottoCoordinate)coord).getParticella()+" \\\\par \n";
		}
		ret=ret.replaceAll("<@DatiCatastali@>", datiCatastali);
		return ret;
	}

	private String replaceTagsWithProperties(String src, Object obj, String className) {
		return replaceTagsWithProperties(src, obj, className,false);
	}
	
	private String replaceTagsWithProperties(String src, Object obj, String className, boolean replaceZeroWithDash) {
		Locale locale  = new Locale("it", "IT");
		String pattern = "#,##0.00";

		DecimalFormat ndf = (DecimalFormat)NumberFormat.getNumberInstance(locale);
		ndf.applyPattern(pattern);
		
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		String ret=src;
		for (Field f : obj.getClass().getDeclaredFields()) {
			String value="";
			try {
				Object val=getProperty(obj, f.getName());
				if(val!=null){
					if(val.getClass().getName().equals("java.util.Date")){
						value=sdf.format(val);
					}else if(val.getClass().getName().equals("java.lang.Double")){
						if((((Double)val).doubleValue()==0)&& replaceZeroWithDash){
							value="-";
						}else
							value=ndf.format(val);
					}else
						value=val.toString();
				}
			} catch (Exception e) {
				logger.debug(e);
			}
			ret=ret.replaceAll("<@"+className+"."+f.getName()+"@>", value);
		}
		return ret;
	}
	 
	public Object getProperty(Object obj, String property) {
	        Object returnValue = null;

	        try {
	            String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1, property.length());
	            Class clazz = obj.getClass();
	            Method method = clazz.getMethod(methodName, null);
	            returnValue = method.invoke(obj, null);
	        }
	        catch (Exception e) {
	        	return "";
	        }
	        return returnValue;
	}

	public List<SelectItem> getListaUtentiOperatoreItems() {
		return listaUtentiOperatoreItems;
	}

	public void setListaUtentiOperatoreItems(
			List<SelectItem> listaUtentiOperatoreItems) {
		this.listaUtentiOperatoreItems = listaUtentiOperatoreItems;
	}

	public Integer getTabAttivo() {
		return tabAttivo;
	}

	public void setTabAttivo(Integer tabAttivo) {
		this.tabAttivo = tabAttivo;
	}

	public List<RicercaLottoInsediamenti> getListaRicercheLotti() {
		return listaRicercheLotti;
	}

	public void setListaRicercheLotti(
			List<RicercaLottoInsediamenti> listaRicercheLotti) {
		this.listaRicercheLotti = listaRicercheLotti;
	}

	public PraticaAllegato getAllegatoCur() {
		return allegatoCur;
	}

	public void setAllegatoCur(PraticaAllegato allegatoCur) {
		this.allegatoCur = allegatoCur;
	}

}
