/*
 * Created on 9-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.fascicolo.logic;

import it.escsolution.escwebgis.catasto.bean.VisuraNazionaleFinder;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.fascicolo.bean.FascFabbAppFinder;
import it.webred.SisterClient.VisuraCatastale;
import it.webred.SisterClient.dto.VisuraDTO;
import it.webred.amprofiler.ejb.user.UserService;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.CatastoSearchCriteria;
import it.webred.ct.data.access.basic.catasto.dto.ParticellaKeyDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Sitidstr;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.naming.Context;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;


public class FascFabbAppLogic extends EscLogic{
	private String appoggioDataSource;
	private SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	
	public static final String URL_FASC_FABB = "URL_FASC_FABB";
	public final static String FINDER = "FINDER127";
	
	public FascFabbAppLogic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
			}

	private static final Logger log = Logger.getLogger(FascFabbAppLogic.class.getName());

	private static final DecimalFormat DF = new DecimalFormat();
	static {
		DF.setGroupingUsed(false);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		DF.setDecimalFormatSymbols(dfs);
		DF.setMaximumFractionDigits(2);
	}
	
	public static final String RICERCA_NOME_VIA = "FascFabbAppLogic@nomeVia";
	public static final String RICERCA_LISTA_VIE = "FascFabbAppLogic@listaVie";
	public static final String RICERCA_ID_VIA = "FascFabbAppLogic@idVia";
	public static final String RICERCA_LISTA_CIVICI = "FascFabbAppLogic@listaCivici";
	public static final String RICERCA_ID_CIVICO = "FascFabbAppLogic@idCivico";
	public static final String RICERCA_LISTA_FP = "FascFabbAppLogic@listaFp";
	
	
	public Hashtable getRisultatoRicerca(String sezione, String foglio,String particella, FascFabbAppFinder finder) throws SQLException{
		
		Hashtable ht = new Hashtable();
		
		finder.setTotaleRecordFiltrati(1);
		// pagine totali
		finder.setPagineTotali(1);
		finder.setTotaleRecord(1);
		finder.setRighePerPagina(1);

		ht.put(this.FINDER,finder);
		ht.put(this.URL_FASC_FABB,this.getUrl(sezione,foglio,particella));
		
		/*INIZIO AUDIT*/
		try {
			Object[] arguments = new Object[2];
			arguments[0] = sezione + "|" + foglio + "|" + particella;
			arguments[1] = finder;
			writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
		} catch (Throwable e) {				
			log.debug("ERRORE nella scrittura dell'audit", e);
		}
		/*FINE AUDIT*/
		
		return ht;
	}
	
	
	public String getUrlByFkParCatastali(String fkParCatastali) throws SQLException{
		
		String codNazionale = fkParCatastali.substring(0,4);
		
		//Recuperare sezione da codNazionale
		String sezione = "";
		
		String foglio = fkParCatastali.substring(5,9);
		String particella = fkParCatastali.substring(9,14);
		
		return this.getUrl(sezione, foglio, particella);
	}
	
	
	
	public String getUrl(String sezione,String foglio, String particella) throws SQLException{
		Connection conn = null;
		Context cont;
		String url = "";
		
		String sql = "SELECT DISTINCT * FROM( " +
				"SELECT DISTINCT a.*, c.FK_AM_COMUNE, i.URL " + 
				    "FROM am_application a, am_instance i, am_instance_comune c, am_user_air air " + 
				    "WHERE i.fk_am_application = a.NAME  " +
				    "AND c.FK_AM_INSTANCE  = i.NAME  " +
				    "AND a.NAME != 'AMProfiler'  " +
				    "AND AIR.FK_AM_USER = ? "+
				    "AND (AIR.FK_AM_COMUNE = C.FK_AM_COMUNE OR AIR.FK_AM_COMUNE IS NULL)  " +
				"UNION " +
				"SELECT DISTINCT a.*, c.FK_AM_COMUNE, i.URL  " +
				    "FROM am_application a, am_instance i, am_instance_comune c, am_user_group ugroup, am_group  gruppo " +
				    "WHERE i.fk_am_application = a.NAME  " +
				    "AND c.FK_AM_INSTANCE  = i.NAME  " +
				    "AND a.NAME != 'AMProfiler'  " +
				    "AND ugroup.FK_AM_USER = ? "+
				    "AND (GRUPPO.FK_AM_COMUNE = C.FK_AM_COMUNE OR GRUPPO.FK_AM_COMUNE IS NULL) " +
				    "AND UGROUP.FK_AM_GROUP = GRUPPO.NAME " +
				") where app_type=? and fk_am_comune=?" +
				"order by FK_AM_COMUNE, APP_CATEGORY, NAME ";
		
		PreparedStatement st=null;
		ResultSet rs = null;
		
		try{
			
			if(foglio!=null && !"".equals(foglio) && particella!=null && !"".equals(particella)){
				
				String username = this.getEnvUtente().getUtente().getUsername();	
				String ente = this.getEnvUtente().getUtente().getCurrentEnte();
				
				conn = this.getConnection();
				st = conn.prepareStatement(sql);
				st.setString(1, username);
				st.setString(2, username);
				st.setString(3,"FascFabb");
				st.setString(4, ente);
				
				rs = st.executeQuery();
				
				if(rs.next())
					url = rs.getString("URL");
				
				if(url!=null && !"".equals(url)){
					foglio = removeLeadingZero(foglio);
					particella = removeLeadingZero(particella);
					
					url+="/jsp/protected/richieste/richieste.faces?es=" + encode(ente)+"&SEZ="+sezione+"&FOGLIO="+foglio+"&PARTICELLA="+particella;
					url = sezione+"#"+foglio+"#"+particella+"#"+url;
				}else
					url="ERR#Accesso al Fascicolo Fabbricato non consentito all'utente "+username;
			
			}else{
				url = "ERR#Immettere foglio e particella.";
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			url = "ERR#"+e;
		}finally{
			try{
				rs.close();
				st.close();
				conn.close();
			}catch(Exception e){}
		}
		
		return url;
		
	}
	
	public List<Sitidstr> getListaVie(String nomeVia) throws Exception {
		try {
			String username = this.getEnvUtente().getUtente().getUsername();	
			String ente = this.getEnvUtente().getUtente().getCurrentEnte();
			
			RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
			ro.setVia(nomeVia);
			ro.setEnteId(ente);
			ro.setUserId(username);
			ro.setCodEnte(ente);
			
			CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
			return catastoService.getListaVie(ro);			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}		
	}
	
	public List<Siticivi> getListaCivici(String idVia) throws Exception {
		try {
			String username = this.getEnvUtente().getUtente().getUsername();	
			String ente = this.getEnvUtente().getUtente().getCurrentEnte();
			
			RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
			ro.setPkIdStra(new BigDecimal(idVia));
			ro.setEnteId(ente);
			ro.setUserId(username);
			ro.setCodEnte(ente);
			
			CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
			return catastoService.getListaIndirizzi(ro);			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}
	
	public List<ParticellaKeyDTO> getListaFp(String idCivico) throws Exception {
		try {
			String username = this.getEnvUtente().getUtente().getUsername();	
			String ente = this.getEnvUtente().getUtente().getCurrentEnte();
			
			RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
			CatastoSearchCriteria criteria = new CatastoSearchCriteria();
			criteria.setIdCivico(idCivico);
			criteria.setCodNazionale(ente);
			ro.setCriteria(criteria);
			ro.setEnteId(ente);
			ro.setUserId(username);
			ro.setCodEnte(ente);
			
			CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
			return catastoService.getListaParticelle(ro);			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}
	
	public static String removeLeadingZero(String str) {
		if (str==null || str.length() == 0)
			return str; 
		
		String retVal=new String(str);
		int i=0;
		while (i<str.length()) {
			if(str.charAt(i)=='0' && str.length() >i+1 ) {
				retVal=str.substring(i+1);
			}else
				break;
			i++;
		}
		return retVal;
	
	}	
	
	public String encode(String stringToEncode) {
		String returnValue = "";
		BASE64Encoder encrypt = new BASE64Encoder();
		try {
			String codedString = encrypt.encode(stringToEncode.getBytes());
			returnValue = codedString;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return returnValue;
	}
	
}

