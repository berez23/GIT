package it.webred.indice;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.redditiAnnuali.bean.RedditiAnnuali;
import it.escsolution.escwebgis.redditiAnnuali.logic.RedditiAnnualiLogic;
import it.escsolution.escwebgis.tributiNew.logic.TributiContribuentiNewLogic;
import it.webred.cet.permission.CeTUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class IndiceCorrLogic extends EscLogic {
	
	private Properties sqlProps;
	private Properties mapProps;
	
	public IndiceCorrLogic(EnvUtente eu, Properties sqlProps, Properties mapProps) {
		super(eu);
		this.sqlProps = sqlProps;
		this.mapProps = mapProps;
	}
	
	public IndiceCorrLogic(EnvUtente eu) {
		super(eu);
	}
	
	
	public HashMap<String, Fonte> getFonti() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap<String, Fonte> result = new HashMap<String, Fonte>();
		Connection c = null;
		try {
		    c = getAMProfConnection();			
			String sql = "SELECT ID, DESCRIZIONE, INFORMAZIONE, PROG_OLD, CODICE FROM AM_FONTE f, AM_FONTE_TIPOINFO ti WHERE f.id = ti.fk_am_fonte";
			ps = c.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Fonte f = new Fonte();
				String id = rs.getString(1);
				String descr = rs.getString(2);	
				String info = rs.getString(3);
				String progOld = rs.getString(4);
				String codice= rs.getString(5);
				f.setId(id);
				f.setDescr(descr);
				f.setInfo(info);
				f.setProg_old(progOld);
				f.setCodice(codice);
				result.put(id + "_" + progOld, f);
				
				if (result.get(id) == null) {
					result.put(id, f);
				}
			}
			
		}
		catch(Exception e) {
			log.error("Errore recupero fonti", e);
		}
		finally {
			try {
				rs.close();
			}
			catch(Throwable t) {}
			try {
				ps.close();
			}
			catch(Throwable t) {}
			try {
				c.close();
			}
			catch(Throwable t) {}
		}
		
		return result;
	}
	
	protected Connection getAMProfConnection() throws NamingException, SQLException
	{
		Context cont = new InitialContext();
		//Context datasourceContext = (Context) cont.lookup("java:comp/env");
    	//DataSource theDataSource = (DataSource) datasourceContext.lookup("jdbc/AM_Profile" + "_" + this.getEnvUtente().getEnte());
		//DataSource theDataSource = (DataSource) datasourceContext.lookup("jdbc/AM_Profile" + "_" + this.getEnvUtente().getEnte());
		DataSource theDataSource = (DataSource)cont.lookup("java:/AMProfiler" );
		
		return theDataSource.getConnection();
	}
	
	public List<DatiFonte> getSoggettiCorr(String fonte, String progr, String tipo, String idOrig, CeTUser  user) {
		List<DatiFonte> result = new ArrayList<DatiFonte>();

		//result = getOggettiCorr(sqlProps.getProperty("Q1"), fonte, progr, idOrig, "1", user);
		result = getOggettiCorrelatiOrdinati("Q1", fonte, progr, idOrig, "1", user);
		
		for (int i=0; i < result.size(); i++) {
			DatiFonte df = result.get(i);
			
			if (df.getId().equals("11")) {
				List<OggettoIndice> oggettiList = df.getOggettoIndiceList();
				
				for (int x=0; x < oggettiList.size(); x++) {
					OggettoIndice oi = oggettiList.get(x);					
					List<OggettoIndice> loi = getDescTipoModello(oi);
					String descTipoModello = loi == null || loi.size() == 0 || 
											loi.get(0).getListaAttributiVistaSub() == null || loi.get(0).getListaAttributiVistaSub().size() == 0 ?
											"-" :
											loi.get(0).getListaAttributiVistaSub().get(0);
					oi.getListaAttributi().add(descTipoModello);
				}
			}
		}
		
		return result;
	}
	
	public List<DatiFonte> getSoggettiCorrByUnico(String fonte, String tipo, String idOrig, CeTUser  user) {
		List<DatiFonte> result = new ArrayList<DatiFonte>();

		String sql  = this.getQuerySqlProps("UQ1", fonte);
		result = getOggettiCorr(sql, fonte, null, idOrig, "1", user);
		
		for (int i=0; i < result.size(); i++) {
			DatiFonte df = result.get(i);
			
			if (df.getId().equals("11")) {
				List<OggettoIndice> oggettiList = df.getOggettoIndiceList();
				
				for (int x=0; x < oggettiList.size(); x++) {
					OggettoIndice oi = oggettiList.get(x);
					List<OggettoIndice> loi = getDescTipoModello(oi);
					String descTipoModello = loi == null || loi.size() == 0 || 
											loi.get(0).getListaAttributiVistaSub() == null || loi.get(0).getListaAttributiVistaSub().size() == 0 ?
											"-" :
											loi.get(0).getListaAttributiVistaSub().get(0);
					oi.getListaAttributi().add(descTipoModello);
				}
			}
		}
		
		return result;
	}
	
	public List<DatiFonte> getVieCorrByUnico(String fonte, String tipo, String idOrig, CeTUser  user) {
		List<DatiFonte> result = new ArrayList<DatiFonte>();
		
		String sql  = this.getQuerySqlProps("UQ2", fonte);
		result = getOggettiCorr(sql, fonte, null, idOrig, "2", user);
		
		// Esplodo i singoli casi per i tributi e la demgrafia
		
		for (int i=0; i < result.size(); i++) {
			DatiFonte df = result.get(i);
			
			if (df.getId().equals("1")) {
				// Demografia
				//df.setOggettoIndiceList(null);
				List<OggettoIndice> oggettiList = df.getOggettoIndiceList();
				
				for (int x=0; x < oggettiList.size(); x++) {
					OggettoIndice oi = oggettiList.get(x);
					List<OggettoIndice> tmpResult = getPersonePerVia(oi);
					
					// df.setOggettoIndiceList(tmpResult);
					oi.setSubList(tmpResult);
					df.setTree(true);
				}
			}
			else if (df.getId().equals("2")) {
				List<OggettoIndice> oggettiList = df.getOggettoIndiceList();
				
				for (int x=0; x < oggettiList.size(); x++) {
					OggettoIndice oi = oggettiList.get(x);
					List<OggettoIndice> tmpResult = null;
					if (oi.getProgr().equals("2"))
						tmpResult = getOggettiIciPerVia(oi);					
					else if (oi.getProgr().equals("3"))
						tmpResult = getOggettiTarsuPerVia(oi);

					oi.setSubList(tmpResult);
					df.setTree(true);
					}
				}
			else if (df.getId().equals("13")) {
				List<OggettoIndice> oggettiList = df.getOggettoIndiceList();
				
				for (int x=0; x < oggettiList.size(); x++) {
					OggettoIndice oi = oggettiList.get(x);
					List<OggettoIndice> tmpResult = null;
					
					tmpResult = getLicenzeCommercialiPerVia(oi);					
					
					oi.setSubList(tmpResult);
					df.setTree(true);
				}				
			}
			else if (df.getId().equals("11")) {
				List<OggettoIndice> oggettiList = df.getOggettoIndiceList();
				
				for (int x=0; x < oggettiList.size(); x++) {
					OggettoIndice oi = oggettiList.get(x);
					List<OggettoIndice> loi = getDescTipoModello(oi);
					String descTipoModello = loi == null || loi.size() == 0 || 
											loi.get(0).getListaAttributiVistaSub() == null || loi.get(0).getListaAttributiVistaSub().size() == 0 ?
											"-" :
											loi.get(0).getListaAttributiVistaSub().get(0);
					oi.getListaAttributi().add(descTipoModello);
				}
			}
			
		}
		
		return result;
	}
	
	public List<DatiFonte> getCiviciCorrByUnico(String fonte, String tipo, String idOrig, CeTUser  user) {
		List<DatiFonte> result = new ArrayList<DatiFonte>();
		
		String sql = this.getQuerySqlProps("UQ3", fonte);
		
		result = getOggettiCorr(sql, fonte, null, idOrig, "3", user);
		
		for (int i=0; i < result.size(); i++) {
			DatiFonte df = result.get(i);
			
			if (df.getId().equals("1")) {
				// Demografia				
				List<OggettoIndice> oggettiList = df.getOggettoIndiceList();
				for (int x=0; x < oggettiList.size(); x++) {
					OggettoIndice oi = oggettiList.get(x);
					List<OggettoIndice> tmpResult = getPersonaPerCiv(oi);		
					oi.setSubList(tmpResult);
					df.setTree(true);
					
				}
			}
			else if (df.getId().equals("2")) {
				List<OggettoIndice> oggettiList = df.getOggettoIndiceList();
				
				for (int x=0; x < oggettiList.size(); x++) {
				OggettoIndice oi = oggettiList.get(x);
				List<OggettoIndice> tmpResult = null;
				if (oi.getProgr().equals("2"))
					tmpResult = getOggettiIciPerCiv(oi);					
				else if (oi.getProgr().equals("3"))
					tmpResult = getOggettiTarsuPerCiv(oi);

				oi.setSubList(tmpResult);
				df.setTree(true);
				}
			}
			else if (df.getId().equals("11")) {
				List<OggettoIndice> oggettiList = df.getOggettoIndiceList();
				
				for (int x=0; x < oggettiList.size(); x++) {
					OggettoIndice oi = oggettiList.get(x);
					List<OggettoIndice> loi = getDescTipoModello(oi);
					String descTipoModello = loi == null || loi.size() == 0 || 
											loi.get(0).getListaAttributiVistaSub() == null || loi.get(0).getListaAttributiVistaSub().size() == 0 ?
											"-" :
											loi.get(0).getListaAttributiVistaSub().get(0);
					oi.getListaAttributi().add(descTipoModello);
				}
			}
			
		}
		
		return result;
	}
	
	private String getQuerySqlProps(String idQuery, String fonte) {
		
		String sql = sqlProps.getProperty(idQuery);
		String orderBy = sqlProps.getProperty("OB_"+idQuery+"_"+fonte);
		
		return this.personalizzaOrderBy(sql, orderBy);
	
	}

	private String personalizzaOrderBy(String sql, String orderBy) {
			
			if(orderBy!=null){
				
				sql += " " + orderBy;
				
			}
			
			return sql;
		}
	

	public List<DatiFonte> getOggettiCorrByUnico(String fonte, String tipo, String idOrig, CeTUser  user) {
		List<DatiFonte> result = new ArrayList<DatiFonte>();
		
		String sql = this.getQuerySqlProps("UQ4", fonte);
		
		result = getOggettiCorr(sql, fonte, null, idOrig, "4", user);
		
		return result;
	}
	
	
	public List<DatiFonte> getFabbricatiCorrByUnico(String fonte, String tipo, String idOrig, CeTUser  user) {
		List<DatiFonte> result = new ArrayList<DatiFonte>();
		
		String sql = this.getQuerySqlProps("UQ5", fonte);
		
		result = getOggettiCorr(sql, fonte, null, idOrig, "5", user);
		
		
		return result;
	}
	
	
	public List<DatiFonte> getVieCorr(String fonte, String progr, String tipo, String idOrig, CeTUser  user) {
		List<DatiFonte> result = new ArrayList<DatiFonte>();
		
		//result = getOggettiCorr(getProperty("Q2"), fonte, progr, idOrig, "2", user);
		result = getOggettiCorrelatiOrdinati("Q2", fonte, progr, idOrig, "2", user);
		
		// Esplodo i singoli casi per i tributi e la demgrafia
		
		for (int i=0; i < result.size(); i++) {
			DatiFonte df = result.get(i);
			
			if (df.getId().equals("1")) {
				// Demografia
				//df.setOggettoIndiceList(null);
				List<OggettoIndice> oggettiList = df.getOggettoIndiceList();
				
				for (int x=0; x < oggettiList.size(); x++) {
					OggettoIndice oi = oggettiList.get(x);
					List<OggettoIndice> tmpResult = getPersonePerVia(oi);
					// df.setOggettoIndiceList(tmpResult);
					oi.setSubList(tmpResult);
					df.setTree(true);
				}
			}
			else if (df.getId().equals("2")) {
				List<OggettoIndice> oggettiList = df.getOggettoIndiceList();
				
				for (int x=0; x < oggettiList.size(); x++) {
					OggettoIndice oi = oggettiList.get(x);
					List<OggettoIndice> tmpResult = null;
					if (oi.getProgr().equals("2"))
						tmpResult = getOggettiIciPerVia(oi);					
					else if (oi.getProgr().equals("3"))
						tmpResult = getOggettiTarsuPerVia(oi);

					oi.setSubList(tmpResult);
					df.setTree(true);
					}
				}
			else if (df.getId().equals("13")) {
				List<OggettoIndice> oggettiList = df.getOggettoIndiceList();
				
				for (int x=0; x < oggettiList.size(); x++) {
					OggettoIndice oi = oggettiList.get(x);
					List<OggettoIndice> tmpResult = null;
					
					tmpResult = getLicenzeCommercialiPerVia(oi);					
					
					oi.setSubList(tmpResult);
					df.setTree(true);
				}
				
			}
			else if (df.getId().equals("11")) {
				List<OggettoIndice> oggettiList = df.getOggettoIndiceList();
				
				for (int x=0; x < oggettiList.size(); x++) {
					OggettoIndice oi = oggettiList.get(x);
					List<OggettoIndice> loi = getDescTipoModello(oi);
					String descTipoModello = loi == null || loi.size() == 0 || 
											loi.get(0).getListaAttributiVistaSub() == null || loi.get(0).getListaAttributiVistaSub().size() == 0 ?
											"-" :
											loi.get(0).getListaAttributiVistaSub().get(0);
					oi.getListaAttributi().add(descTipoModello);
				}
			}
			// qui va messa la gestione per gli altri casi es Tributi
			
		}
		
		return result;
	}
	
	public List<DatiFonte> getCivicoCorr(String fonte, String progr, String tipo, String idOrig, CeTUser  user) {
		List<DatiFonte> result = new ArrayList<DatiFonte>();

		//result = getOggettiCorr(sqlProps.getProperty("Q3"), fonte, progr, idOrig, "3", user);
		result = getOggettiCorrelatiOrdinati("Q3", fonte, progr, idOrig, "3", user);
		

		for (int i=0; i < result.size(); i++) {
			DatiFonte df = result.get(i);
			
			if (df.getId().equals("1")) {
				// Demografia				
				List<OggettoIndice> oggettiList = df.getOggettoIndiceList();
				for (int x=0; x < oggettiList.size(); x++) {
					OggettoIndice oi = oggettiList.get(x);
					List<OggettoIndice> tmpResult = getPersonaPerCiv(oi);		
					oi.setSubList(tmpResult);
					df.setTree(true);
					
				}
			}else if (df.getId().equals("2")) {
				List<OggettoIndice> oggettiList = df.getOggettoIndiceList();
				
				for (int x=0; x < oggettiList.size(); x++) {
					OggettoIndice oi = oggettiList.get(x);
					List<OggettoIndice> tmpResult = null;
					if (oi.getProgr().equals("2"))
						tmpResult = getOggettiIciPerCiv(oi);					
					else if (oi.getProgr().equals("3"))
						tmpResult = getOggettiTarsuPerCiv(oi);

					oi.setSubList(tmpResult);
					df.setTree(true);
					}
			}
		}
		
		
		return result;
	}	

	public List<DatiFonte> getOggettiCorr(String fonte, String progr, String tipo, String idOrig, CeTUser  user) {
		List<DatiFonte> result = new ArrayList<DatiFonte>();

		//result = getOggettiCorr(sqlProps.getProperty("Q4"), fonte, progr, idOrig, "4", user);
		result = getOggettiCorrelatiOrdinati("Q4", fonte, progr, idOrig, "4", user);
		
		return result;
	}	
	
	public List<DatiFonte> getFabbricatiCorr(String fonte, String progr, String tipo, String idOrig, CeTUser  user) {
		List<DatiFonte> result = new ArrayList<DatiFonte>();

		//result = getOggettiCorr(sqlProps.getProperty("Q5"), fonte, progr, idOrig, "5", user);
		result = getOggettiCorrelatiOrdinati("Q5", fonte, progr, idOrig, "5", user);
		
		return result;
	}	


	private List<DatiFonte> getOggettiCorrelatiOrdinati(String idQueryQ, String fonte, String progr, String idOrig, String tipo, CeTUser  user){
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection c = null;
		
		List<DatiFonte> datiFonteList = new ArrayList<DatiFonte>();
		
		String sqlQBase = sqlProps.getProperty(idQueryQ);
		
		String idQueryB = "";
		if(idQueryQ.startsWith("Q")){
			idQueryB = idQueryQ.replace("Q", "B");
			
			String sqlB = sqlProps.getProperty(idQueryB);
			
			
			log.debug("SQL B ["+sqlB+"]");
			log.debug("Params idOrig["+idOrig+"]");
			log.debug("Params fonte["+fonte+"]");
			log.debug("Params progr["+progr+"]");
			
			try {		
				c = super.getConnectionDiogene();
				ps = c.prepareStatement(sqlB);
				ps.setString(1, idOrig);
				if (progr != null && !progr.trim().equals("")) {
					ps.setString(2, progr);
					ps.setString(3, fonte);
				}
				else
					ps.setString(2, fonte);
				
				rs = ps.executeQuery();
			
				while (rs.next()) {
					
					String fonteDest = rs.getString(1);
					//String progDest = rs.getString(2);
					
					//In base ai valori recuperati, richiamo la query con il filtro e l'ordinamento giusto
					
					String sqlQ = sqlQBase +" AND SG1.FK_ENTE_SORGENTE='"+fonteDest+"'";
					String orderBy = sqlProps.getProperty("OB_"+idQueryQ+"_"+fonteDest);
					
					if(orderBy==null)
						orderBy = sqlProps.getProperty("OB_U"+idQueryQ+"_"+fonteDest);
					
					if(orderBy!=null)
						sqlQ += " " + orderBy;
					
					//Spezzare la query per ciascuna fonte di destinazione e ordinare singolarmente.
					datiFonteList.addAll(getOggettiCorr(sqlQ, fonte, progr, idOrig, tipo, user));
					
				}
				
			}catch(Exception e) {
					log.error("Errore recupero oggetti correlati ordinati",e);
				}
				finally {
					try {
						rs.close();
					}
					catch(Throwable t) {}
					try {
						ps.close();
					}
					catch(Throwable t) {}
					try {
						c.close();
					}
					catch(Throwable t) {}
			
		}
		
		}
		
		return datiFonteList;
		
	}
	
	
	
	private List<DatiFonte> getOggettiCorr(String sql, String fonte, String progr, String idOrig, String tipo, CeTUser  user) {
	PreparedStatement ps = null;
	ResultSet rs = null;
	Connection c = null;
	
	// Recupero i dati sulle fonti
	HashMap<String, Fonte> fontiDescrMap = getFonti();
	
	
	
	List<DatiFonte> datiFonteList = new ArrayList<DatiFonte>();
	
	HashMap<String, DatiFonte> fonteMap = new HashMap<String, DatiFonte>();
	
	HashMap<String,String> listaPermessi= user.getPermList();
	ArrayList<String> listaPermessiEnte= new ArrayList<String>();
	
	//String enteCorrente= user.getCurrentEnte();
	
	
	if (listaPermessi!= null){
		
		Iterator it= listaPermessi.keySet().iterator();
			
			if (it !=  null){
				while (it.hasNext()){
					String permesso= (String)it.next();
					
					//elimino il filtro per l'ente corrente
					//if (permesso.contains(enteCorrente))
						listaPermessiEnte.add(permesso);
				}
			
		}
	}
	
	
	
	
	log.debug("SQL Q/UQ ["+sql+"]");
	log.debug("Params idOrig["+idOrig+"]");
	log.debug("Params fonte["+fonte+"]");
	log.debug("Params progr["+progr+"]");
	
	
	try {		
			c = super.getConnectionDiogene();
			ps = c.prepareStatement(sql);
			ps.setString(1, idOrig);
			if (progr != null && !progr.trim().equals("")) {
				ps.setString(2, progr);
				ps.setString(3, fonte);
			}
			else
				ps.setString(2, fonte);
			
			rs = ps.executeQuery();
		
			while (rs.next()) {
				
				String fonteDest= rs.getString(2);
				
				Fonte fonteObj= fontiDescrMap.get(fonteDest);
			
				String fonteCodice= fonteObj.getCodice();
				
				if (listaPermessiEnte.size()>0){
					
					//filtro le fonti correlate da visualizzare in base ai permessi su quella fonte
					
					for (String permesso: listaPermessiEnte){
						if (permesso.contains("Fonte:"+ fonteCodice)){
									
									OggettoIndice oi = new OggettoIndice();
															
									oi.setIdDest(rs.getString(1));
										
									oi.setFonteDest(rs.getString(2));
									oi.setProgr(rs.getString(3));
									oi.setDescrizione(rs.getString(4));
									
									//Per i Soggetti ICI e TARSU occorre concatenare il campo ID
									if (oi.getFonteDest()!= null && oi.getFonteDest().equals("2") && oi.getProgr()!= null && (oi.getProgr().equals("1")|| oi.getProgr().equals("4") ) )
										oi.setIdDest("ID|"+ rs.getString(1));
									
									
									if (tipo != null && tipo.equals("3")){
										String desc= oi.getDescrizione();
										try{
											Integer civico=Integer.valueOf(desc);
											oi.setDescrizione(String.valueOf(civico));
										}
										catch(NumberFormatException e){
											
										}
									}
									
									if (tipo != null && tipo.equals("1")){
					
										List<String> listaAttributi= getListaAttributiSoggetti(rs);
										
										if(oi.getFonteDest()!= null && oi.getFonteDest().equals("2")){
											
											String indirizzoNew = listaAttributi.get(9);
											
											//se assente, recupero la descrizione dal codice via
											if(indirizzoNew.equals("-")){
												String codVia = listaAttributi.get(10);
												TributiContribuentiNewLogic logic = new TributiContribuentiNewLogic(this.getEnvUtente());
												if(codVia!=null && oi.getProgr()!=null && oi.getProgr().equals("1"))
													 indirizzoNew = logic.getDescViaByIdExt(codVia, "ICI");
												else
													 indirizzoNew = logic.getDescViaByIdExt(codVia, "TAR");
											}
											
											listaAttributi.set(9, indirizzoNew);
											 	
										}
										
										oi.setListaAttributi(listaAttributi);
										
									}
									
									else if (tipo != null && tipo.equals("4")){
														
										List<String> listaAttributi= getListaAttributiOggetti(rs);			
										oi.setListaAttributi(listaAttributi);
										
									}
									
									else if (tipo != null && tipo.equals("5")){
										
										List<String> listaAttributi= getListaAttributiFabbricati(rs);
										oi.setListaAttributi(listaAttributi);
										
									}
									
									else if (tipo != null && (tipo.equals("2") || tipo.equals("3"))){
										
										List<String> listaAttributi=	getListaAttributiVieCivici(rs);
										oi.setListaAttributi(listaAttributi);
										
									}
									
									
									
									oi.setRating(rs.getString(5));
									oi.setAtt(rs.getString(6));
									oi.setTipoPersona(rs.getString(7));
									
									String fonteId = rs.getString(2);
									String progEs = rs.getString(3);
									Fonte f = fontiDescrMap.get(fonteId + "_" + progEs);
									oi.setFonteDescr( f.getInfo());
									
									String uc="";
									if (oi.getTipoPersona()== null || oi.getTipoPersona().equals("")){
										uc = mapProps.getProperty(oi.getFonteDest() + "_" + oi.getProgr());
										if (oi.getFonteDest()!= null && oi.getFonteDest().equals("4") && oi.getProgr()!= null && oi.getProgr().equals("2") ){
											if (tipo.equals("2"))
												uc=mapProps.getProperty(oi.getFonteDest() + "_" + oi.getProgr()+ "_V");
											else if (tipo.equals("3"))
												uc=mapProps.getProperty(oi.getFonteDest() + "_" + oi.getProgr()+ "_C");
										}
										if (oi.getFonteDest()!= null && oi.getFonteDest().equals("29") && oi.getProgr()!= null && oi.getProgr().equals("1") ){
											if (tipo.equals("2"))
												uc=mapProps.getProperty(oi.getFonteDest() + "_" + oi.getProgr()+ "_V");
											else if (tipo.equals("3"))
												uc=mapProps.getProperty(oi.getFonteDest() + "_" + oi.getProgr()+ "_C");
										}
									}
									else{
											uc=mapProps.getProperty(oi.getFonteDest() + "_" + oi.getProgr()+ "_"+ oi.getTipoPersona());
											if(uc==null) //Nel caso in cui il tipo persona abbia un valore diverso da P o G
												uc=mapProps.getProperty(oi.getFonteDest() + "_" + oi.getProgr());
									}
									if (uc != null){
										oi.setAppName(Tema.getServletMapping(Integer.parseInt(uc)));
										oi.setUc(uc);
									}
									oi.setTipo(tipo);
									//result.add(oi);
									
									DatiFonte df = fonteMap.get(fonteId);
									
									if (df == null) {
										df = new DatiFonte();
										df.setId(fonteId);
										Fonte f1 = fontiDescrMap.get(fonteId); 
										df.setDescr(f1.getDescr());
										List<OggettoIndice> result = new ArrayList<OggettoIndice>();
										
										result.add(oi);
										df.setOggettoIndiceList(result);
										fonteMap.put(fonteId, df);
									}
									else {
										List<OggettoIndice> result = df.getOggettoIndiceList();
										result.add(oi);
									}
									break;
								}
						}
				}
	}
			
			Iterator<String> keySet = fonteMap.keySet().iterator();
			while (keySet.hasNext()) {
				String id = keySet.next();
				DatiFonte df = fonteMap.get(id);
				datiFonteList.add(df);
			}
	}
	catch(Exception e) {
		log.error("get oggetti correlati",e);
	}
	finally {
		try {
			rs.close();
		}
		catch(Throwable t) {}
		try {
			ps.close();
		}
		catch(Throwable t) {}
		try {
			c.close();
		}
		catch(Throwable t) {}
		
	}
	
	return datiFonteList;
	}
	

	private List<OggettoIndice> getPersonePerVia(OggettoIndice oiOrig) {
		String sql = "SELECT * FROM (SELECT ROWNUM AS n, cognome, nome, cod_anagrafe, codice_fiscale, " +
        "  data_nascita, codice_nazionale, id_orig, id_ext, ID, id_fam " +
        " FROM (SELECT ROWNUM AS n, DECODE (sit_d_persona.id_orig, NULL, '-', " +
        "  sit_d_persona.id_orig) AS cod_anagrafe, DECODE (sit_d_persona.cognome, " +
        "     NULL, '-', sit_d_persona.cognome) AS cognome, DECODE (sit_d_persona.nome, " +
        "     NULL, '-',  sit_d_persona.nome) AS nome, DECODE (sit_d_persona.codfisc, " +
        "      NULL, '-', sit_d_persona.codfisc ) AS codice_fiscale,  sit_d_persona.id_orig AS id_orig, " +
        " sit_d_persona.id_ext AS id_ext, sit_d_pers_fam.id_ext_d_famiglia AS id_fam, sit_d_persona.ID AS ID, " +
        " NVL(TO_CHAR (sit_d_persona.data_nascita, 'dd/mm/yyyy'), '-') AS data_nascita, " +
        " codent AS codice_nazionale FROM sit_d_persona LEFT JOIN sit_d_pers_fam " +
        " ON sit_d_persona.id_ext = sit_d_pers_fam.id_ext_d_persona, "  +
        " sit_ente " +
        " WHERE 1 = 1 " +
        " AND sit_d_persona.posiz_ana IN ('A', 'ISCRITTO NELL''A.P.R.') " +
        " AND sit_d_persona.dt_fine_val IS NULL AND sit_d_pers_fam.dt_fine_val IS NULL " +
        " AND sit_d_persona.id_ext in ( SELECT pciv.id_ext_d_persona from SIT_D_PERSONA_CIVICO pciv, SIT_D_CIVICO_V civ, SIT_D_VIA via WHERE " +
        "  civ.id_ext_d_via = via.id_ext and via.dt_fine_val is null " +
        " and pciv.id_ext_d_civico=civ.id_ext and via.id=?) ) order by id_fam, cognome, nome)";
		
		return getExtraInfo(oiOrig, sql, "1,1");
	}
	
	private List<OggettoIndice> getOggettiIciPerVia(OggettoIndice oiOrig) {
		String sql = "select * from (" +
		"select ROWNUM as N, ID, YEA_DEN, NUM_DEN, FOGLIO, NUMERO, SUB, CAT, CLS, FLG_POS3112, PROVENIENZA, VIA, CIVICO from (" +
		"select distinct ogg.ID, " + 
		"NVL(ogg.YEA_DEN,'-') AS YEA_DEN, " +
		"NVL(ogg.NUM_DEN,'-') AS NUM_DEN, " +
		"NVL(ogg.FOGLIO,'-') AS FOGLIO, " +
		"NVL(ogg.NUMERO,'-') AS NUMERO, " +
		"NVL(ogg.SUB,'-') AS SUB, " +
		"NVL(ogg.CAT,'-') AS CAT, " +
		"NVL(ogg.CLS,'-') AS CLS, " +
		"ogg.FLG_POS3112 AS FLG_POS3112, " +
		"NVL(ogg.PROVENIENZA,'-') AS PROVENIENZA, " +
		"via.descrizione AS VIA, " +
		"LTRIM(ogg.num_civ,'0')||DECODE(ogg.esp_civ,NULL,'','/'||ogg.esp_civ) AS CIVICO " +
		"from SIT_T_ICI_OGGETTO ogg, SIT_T_ICI_VIA via where via.id=? AND via.id_ext=ogg.id_ext_via))";
		
		sql+=" ORDER BY FOGLIO, NUMERO, SUB, yea_den desc, VIA, CIVICO";
	
		return getExtraInfo(oiOrig, sql, "2,2");
	}
	
	private List<OggettoIndice> getOggettiIciPerCiv(OggettoIndice oiOrig) {
		
		String id = oiOrig.getIdDest();
		StringTokenizer st = new StringTokenizer(id, "|");
		
		String idVia = st.nextToken();
		String numCiv = st.nextToken();
		String espCiv = "";
		try{
			 espCiv = st.nextToken();
		}
		catch(Exception e){
			
		}
		
		String sql = "select * from (" +
					"select ROWNUM as N, ID, YEA_DEN, NUM_DEN, FOGLIO, NUMERO, SUB, CAT, CLS, FLG_POS3112, PROVENIENZA, via, civico from (" +
					"select distinct ogg.ID, " + 
					"NVL(ogg.YEA_DEN,'-') AS YEA_DEN, " +
					"NVL(ogg.NUM_DEN,'-') AS NUM_DEN, " +
					"NVL(ogg.FOGLIO,'-') AS FOGLIO, " +
					"NVL(ogg.NUMERO,'-') AS NUMERO, " +
					"NVL(ogg.SUB,'-') AS SUB, " +
					"NVL(ogg.CAT,'-') AS CAT, " +
					"NVL(ogg.CLS,'-') AS CLS, " +
					"ogg.FLG_POS3112 AS FLG_POS3112, " +
					"NVL(ogg.PROVENIENZA,'-') AS PROVENIENZA, " +
					"via.descrizione AS VIA, " +
					"LTRIM(ogg.num_civ,'0')||DECODE(ogg.esp_civ,NULL,'','/'||ogg.esp_civ) AS CIVICO " +
					"from SIT_T_ICI_OGGETTO ogg, SIT_T_ICI_VIA via where via.id='" +idVia+ "' AND via.id_ext=ogg.id_ext_via AND ogg.num_civ='"+numCiv+"' and 1=?";
					if (espCiv != null && !espCiv.equals(""))
						sql= sql + " and ogg.esp_civ ='"+ espCiv +"'";
					else
						sql+= " and (ogg.esp_civ IS NULL OR ogg.esp_civ='')";
					sql= sql +" ))";
					
		sql+=" ORDER BY FOGLIO, NUMERO, SUB, yea_den desc, VIA, CIVICO";
					
		return getExtraInfo(oiOrig, sql, "2,2", false);
	}	
	
	private List<OggettoIndice> getOggettiTarsuPerCiv(OggettoIndice oiOrig) {
		// E' necessario spacchettare l'id che è composto da id via e numero civico
		String id = oiOrig.getIdDest();
		StringTokenizer st = new StringTokenizer(id, "|");
		
		String idVia = st.nextToken();
		String numCiv = st.nextToken();
		String espCiv = "";
		try{
			 espCiv = st.nextToken();
		}
		catch(Exception e){
			
		}
		
		String sql = "select * from (" +
					 "select ROWNUM as N, ID, FOGLIO, NUMERO, SUB, DES_CLS_RSU, SUP_TOT, DAT_INI, DAT_FIN, PROVENIENZA, VIA, CIVICO from (" +
					 "select distinct SIT_T_TAR_OGGETTO.ID, " + 
					 "NVL(SIT_T_TAR_OGGETTO.FOGLIO,'-') AS FOGLIO, " +
					 "NVL(SIT_T_TAR_OGGETTO.NUMERO,'-') AS NUMERO, " +
					 "NVL(SIT_T_TAR_OGGETTO.SUB,'-') AS SUB, " +
					 "NVL(SIT_T_TAR_OGGETTO.DES_CLS_RSU,'-') AS DES_CLS_RSU, " +
					 "SIT_T_TAR_OGGETTO.SUP_TOT AS SUP_TOT, " +
					 "SIT_T_TAR_OGGETTO.DAT_INI AS DAT_INI, " +
					 "SIT_T_TAR_OGGETTO.DAT_FIN AS DAT_FIN, " +
					 "NVL(SIT_T_TAR_OGGETTO.PROVENIENZA,'-') AS PROVENIENZA, " +
					 "SIT_T_TAR_VIA.descrizione AS VIA, " +
					 "LTRIM(SIT_T_TAR_OGGETTO.num_civ,'0')||DECODE(SIT_T_TAR_OGGETTO.esp_civ,NULL,'','/'||SIT_T_TAR_OGGETTO.esp_civ) AS CIVICO " +
					 "from SIT_T_TAR_OGGETTO, SIT_T_TAR_VIA " +
					 "where SIT_T_TAR_VIA.id='" + idVia + "' AND SIT_T_TAR_OGGETTO.NUM_CIV='" + numCiv + "' AND SIT_T_TAR_VIA.id_ext=SIT_T_TAR_OGGETTO.id_ext_via and 1=?";
					if (espCiv != null && !espCiv.equals(""))
						sql= sql + " and SIT_T_TAR_OGGETTO.esp_civ ='"+ espCiv +"'";
					else
						sql+= " and (SIT_T_TAR_OGGETTO.esp_civ IS NULL OR SIT_T_TAR_OGGETTO.esp_civ='')";
					sql= sql +" ))";
					
		sql+=" ORDER BY FOGLIO, NUMERO, SUB, VIA, CIVICO ";

		return getExtraInfo(oiOrig, sql, "2,3", false);
	}		
	
	
	private List<OggettoIndice> getOggettiTarsuPerVia(OggettoIndice oiOrig) {

		String sql = "select * from (" +
		"select ROWNUM as N, ID, FOGLIO, NUMERO, SUB, DES_CLS_RSU, SUP_TOT, DAT_INI, DAT_FIN, PROVENIENZA, VIA, CIVICO from (" +
		"select distinct SIT_T_TAR_OGGETTO.ID, " + 
		"NVL(SIT_T_TAR_OGGETTO.FOGLIO,'-') AS FOGLIO, " +
		"NVL(SIT_T_TAR_OGGETTO.NUMERO,'-') AS NUMERO, " +
		"NVL(SIT_T_TAR_OGGETTO.SUB,'-') AS SUB, " +
		"NVL(SIT_T_TAR_OGGETTO.DES_CLS_RSU,'-') AS DES_CLS_RSU, " +
		"SIT_T_TAR_OGGETTO.SUP_TOT AS SUP_TOT, " +
		"SIT_T_TAR_OGGETTO.DAT_INI AS DAT_INI, " +
		"SIT_T_TAR_OGGETTO.DAT_FIN AS DAT_FIN, " +
		"NVL(SIT_T_TAR_OGGETTO.PROVENIENZA,'-') AS PROVENIENZA, " +
		"SIT_T_TAR_VIA.descrizione AS VIA, " +
		"LTRIM(SIT_T_TAR_OGGETTO.num_civ,'0')||DECODE(SIT_T_TAR_OGGETTO.esp_civ,NULL,'','/'||SIT_T_TAR_OGGETTO.esp_civ) AS CIVICO " +
		"from SIT_T_TAR_OGGETTO, SIT_T_TAR_VIA " +
		"where SIT_T_TAR_VIA.id=? AND SIT_T_TAR_VIA.id_ext=SIT_T_TAR_OGGETTO.id_ext_via))";
		
		sql+=" ORDER BY FOGLIO, NUMERO, SUB, VIA, CIVICO ";

		return getExtraInfo(oiOrig, sql, "2,3");
	}	
	
	private List<OggettoIndice> getPersonaPerCiv(OggettoIndice oiOrig) {
		String sql = "SELECT * FROM (SELECT ROWNUM AS n, cognome, nome, cod_anagrafe, codice_fiscale, " +
        "  data_nascita, codice_nazionale, id_orig, id_ext, ID, id_fam " +
        " FROM (SELECT ROWNUM AS n, DECODE (sit_d_persona.id_orig, NULL, '-', " +
        "  sit_d_persona.id_orig) AS cod_anagrafe, DECODE (sit_d_persona.cognome, " +
        "     NULL, '-', sit_d_persona.cognome) AS cognome, DECODE (sit_d_persona.nome, " +
        "     NULL, '-',  sit_d_persona.nome) AS nome, DECODE (sit_d_persona.codfisc, " +
        "      NULL, '-', sit_d_persona.codfisc ) AS codice_fiscale,  sit_d_persona.id_orig AS id_orig, " +
        " sit_d_persona.id_ext AS id_ext, sit_d_pers_fam.id_ext_d_famiglia AS id_fam, sit_d_persona.ID AS ID, " +
        " NVL(TO_CHAR (sit_d_persona.data_nascita, 'dd/mm/yyyy'), '-') AS data_nascita, " +
        " codent AS codice_nazionale FROM sit_d_persona LEFT JOIN sit_d_pers_fam " +
        " ON sit_d_persona.id_ext = sit_d_pers_fam.id_ext_d_persona, "  +
        " sit_ente " +
        " WHERE 1 = 1 " +
        " AND sit_d_persona.posiz_ana IN ('A', 'ISCRITTO NELL''A.P.R.') " +
        " AND sit_d_persona.dt_fine_val IS NULL AND sit_d_pers_fam.dt_fine_val IS NULL " +
        " AND sit_d_persona.id_ext in ( SELECT pciv.id_ext_d_persona from SIT_D_PERSONA_CIVICO pciv, SIT_D_CIVICO_V civ WHERE " +
        " pciv.id_ext_d_civico=civ.id_ext and civ.id=?) ) order by id_fam, cognome, nome)";
		
		return getExtraInfo(oiOrig, sql, "1,1");
	}
	
	private List<OggettoIndice> getLicenzeCommercialiPerVia(OggettoIndice oiOrig) {
		String sql = "SELECT * FROM " +
		"(SELECT ROWNUM N, SEL.* FROM (SELECT A.ID, A.ID_EXT, A.ID_ORIG, A.FK_ENTE_SORGENTE," +
		" A.NUMERO, NUMERO_PROTOCOLLO, ANNO_PROTOCOLLO, TIPOLOGIA, CARATTERE, STATO, " +
		" DATA_INIZIO_SOSPENSIONE, DATA_FINE_SOSPENSIONE," +
		"  NVL(SEDIME, '') || DECODE(SEDIME, NULL, '', '', '', ' ') || NVL(INDIRIZZO, '') AS INDIRIZZO," +
		" CIVICO, CIVICO2, CIVICO3, SUPERFICIE_MINUTO, SUPERFICIE_TOTALE, SEZIONE_CATASTALE," +
		" FOGLIO, PARTICELLA, SUBALTERNO, CODICE_FABBRICATO, to_char(DATA_VALIDITA, 'dd/MM/yyyy') as DATA_VALIDITA, to_char(DATA_RILASCIO, 'dd/MM/yyyy') as DATA_RILASCIO, " +
		" ZONA, RAG_SOC, NOTE, RIFERIM_ATTO, A.PROVENIENZA" +
		" FROM SIT_LICENZE_COMMERCIO A, SIT_LICENZE_COMMERCIO_VIE B " +
		" WHERE A.DT_FINE_VAL IS NULL AND A.ID_EXT_VIE = B.ID_EXT(+) " +
		" and B.id= ? "+
		" ORDER BY ID_ORIG) SEL) ";
	
		return getExtraInfo(oiOrig, sql, "13,1");
	}
	
	private List<OggettoIndice> getDescTipoModello(OggettoIndice oiOrig) {
		String sql = "SELECT RED_DESC.IDE_TELEMATICO || '|' || RED_DESC.CODICE_FISCALE_DIC AS ID, " +
		"RED_DESC.TIPO_MODELLO, RED_DESC.ANNO_IMPOSTA " +
		"FROM RED_DATI_ANAGRAFICI RED_DAT_ANA " +
		"LEFT OUTER JOIN RED_ATTIVITA RED_ATT " +
		"ON RED_DAT_ANA.IDE_TELEMATICO = RED_ATT.IDE_TELEMATICO " +
		"AND RED_DAT_ANA.CODICE_FISCALE_DIC = RED_ATT.CODICE_FISCALE_DIC " +
		"AND RED_DAT_ANA.ANNO_IMPOSTA = RED_ATT.ANNO_IMPOSTA " +
		"LEFT OUTER JOIN RED_DESCRIZIONE RED_DESC " +
		"ON RED_DAT_ANA.IDE_TELEMATICO = RED_DESC.IDE_TELEMATICO " +
		"AND RED_DAT_ANA.CODICE_FISCALE_DIC = RED_DESC.CODICE_FISCALE_DIC " +
		"AND RED_DAT_ANA.ANNO_IMPOSTA = RED_DESC.ANNO_IMPOSTA " +
		"LEFT OUTER JOIN RED_DOMICILIO_FISCALE RED_DOM_FISC " +
		"ON RED_DAT_ANA.IDE_TELEMATICO = RED_DOM_FISC.IDE_TELEMATICO " +
		"AND RED_DAT_ANA.CODICE_FISCALE_DIC = RED_DOM_FISC.CODICE_FISCALE_DIC " +
		"AND RED_DAT_ANA.ANNO_IMPOSTA = RED_DOM_FISC.ANNO_IMPOSTA " +
		"WHERE " +
		"RED_DAT_ANA.IDE_TELEMATICO || '|' || RED_DAT_ANA.CODICE_FISCALE_DIC = ?";
	
		return getExtraInfo(oiOrig, sql, "11,1");
	}
	
	private List<OggettoIndice> getRTarsuDaImm(OggettoIndice oiOrig) {
		String sql = "select * from (" +
		"select ROWNUM as N, ID, YEA_DEN, NUM_DEN, FOGLIO, NUMERO, SUB, CAT, CLS, FLG_POS3112, PROVENIENZA, VIA, CIVICO from (" +
		"select distinct ogg.ID, " + 
		"NVL(ogg.YEA_DEN,'-') AS YEA_DEN, " +
		"NVL(ogg.NUM_DEN,'-') AS NUM_DEN, " +
		"NVL(ogg.FOGLIO,'-') AS FOGLIO, " +
		"NVL(ogg.NUMERO,'-') AS NUMERO, " +
		"NVL(ogg.SUB,'-') AS SUB, " +
		"NVL(ogg.CAT,'-') AS CAT, " +
		"NVL(ogg.CLS,'-') AS CLS, " +
		"ogg.FLG_POS3112 AS FLG_POS3112, " +
		"NVL(ogg.PROVENIENZA,'-') AS PROVENIENZA, " +
		"via.descrizione AS VIA, " +
		"LTRIM(ogg.num_civ,'0')||DECODE(ogg.esp_civ,NULL,'','/'||ogg.esp_civ) AS CIVICO " +
		"from SIT_T_ICI_OGGETTO ogg, SIT_T_ICI_VIA via where via.id=? AND via.id_ext=ogg.id_ext_via))";
		
		sql+=" ORDER BY FOGLIO, NUMERO, SUB, yea_den desc, VIA, CIVICO";
	
		return getExtraInfo(oiOrig, sql, "2,2");
	}

	private List<OggettoIndice> getExtraInfo(OggettoIndice oiOrig, String sql, String tipo) {
		return getExtraInfo(oiOrig, sql, tipo, true);
	}
	
	private List<OggettoIndice> getExtraInfo(OggettoIndice oiOrig, String sql, String tipo, boolean setParam) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection c = null;

		List<OggettoIndice> result = new ArrayList<OggettoIndice>();
		
		
		
		try {		
			
			c = super.getConnectionDiogene();
			log.debug("getExtraInfo - SQL["+sql+"]");
			this.initialize();
			
			if (setParam) {
				this.setString(1, oiOrig.getIdDest());
				log.debug("Param - IdDest["+ oiOrig.getIdDest()+"]");
			}else
				this.setInt(1,1);
			
			prepareStatement(sql);
			rs = executeQuery(c, this.getClass().getName(), getEnvUtente());
			
			List<String> lstFamiglia = new ArrayList<String>();
			
			while (rs.next()) {
				
				String descr = "";
				
			/*	if (tipo.equals("1,1"))
				  descr = rs.getString("COGNOME") + " " + tornaValoreRS(rs,"NOME") + "(" + tornaValoreRS(rs,"CODICE_FISCALE") + ") - (" + oiOrig.getDescrizione() + ")";
				else if (tipo.equals("2,2"))
					descr = "Anno:" + rs.getString("YEA_DEN") + " F:" + rs.getString("FOGLIO") + " P: " + rs.getString("NUMERO") + " S:" + rs.getString("SUB");
				else if (tipo.equals("2,3"))
					descr = " F:" + rs.getString("FOGLIO") + " P: " + rs.getString("NUMERO") + " S:" + rs.getString("SUB");
				*/
				
				
				String id = rs.getString("ID");
				OggettoIndice oi = new OggettoIndice();
				oi.setIdDest(id);
				
				//setto la lista degli attributi da visualizzare in base al tipo 
				
				List<String> listaAttributiVistaSub= new ArrayList<String>();
				
				
				if (tipo.equals("1,1")){
					listaAttributiVistaSub.add(tornaValoreRS(rs,"COGNOME"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"NOME"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"CODICE_FISCALE"));
					
					//Imposto il colore in base alla famiglia di appartenenza
					String idFamiglia = tornaValoreRS(rs,"ID_FAM");
					if(!lstFamiglia.contains(idFamiglia))
						lstFamiglia.add(idFamiglia);
					
					int index = lstFamiglia.indexOf(idFamiglia);
					boolean colore = false;
					if(index%2>0)
						colore = true;
						
					oi.setColoreLista(colore);
					
				}
				else if (tipo.equals("2,2")){
					listaAttributiVistaSub.add(tornaValoreRS(rs,"YEA_DEN"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"FOGLIO"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"NUMERO"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"SUB"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"VIA")+" "+tornaValoreRS(rs,"CIVICO"));
				}
				else if (tipo.equals("2,3")){
					listaAttributiVistaSub.add("-");
					listaAttributiVistaSub.add(tornaValoreRS(rs,"FOGLIO"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"NUMERO"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"SUB"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"VIA")+" "+tornaValoreRS(rs,"CIVICO"));
				}
				else if (tipo.equals("11,1")){
					String tipoModello = tornaValoreRS(rs,"TIPO_MODELLO");
					String annoImposta = tornaValoreRS(rs,"ANNO_IMPOSTA");
					RedditiAnnuali red = new RedditiAnnuali();
					red.setTipoModello(tipoModello);
					red.setAnnoImposta(annoImposta);
					String descTipoModello = RedditiAnnualiLogic.getDescTipoModello(red);
					descTipoModello = descTipoModello == null || descTipoModello.equals("") ? "-" : descTipoModello;
					listaAttributiVistaSub.add(descTipoModello);
				}
				else if (tipo.equals("13,1")){
					listaAttributiVistaSub.add(tornaValoreRS(rs,"NUMERO"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"RAG_SOC"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"TIPOLOGIA"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"CIVICO"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"DATA_VALIDITA"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"DATA_RILASCIO"));
				}
				/*else if (tipo.equals("39,2")){
					listaAttributiVistaSub.add(tornaValoreRS(rs,"ANNO"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"TIPO"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"CODFISC"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"NOMINATIVO_CONTRIB"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"INDIRIZZO_IMM"));
				}
				else if (tipo.equals("40,2")){
					listaAttributiVistaSub.add(tornaValoreRS(rs,"ANNO"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"TIPO"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"CODFISC"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"NOMINATIVO_CONTRIB"));
					listaAttributiVistaSub.add(tornaValoreRS(rs,"INDIRIZZO_IMM"));
				}*/
				
				
				
				oi.setListaAttributiVistaSub(listaAttributiVistaSub);
				
				//oi.setDescrizione(descr);
				oi.setFonteDescr(oiOrig.getFonteDescr());
				oi.setFonteDest(oiOrig.getFonteDest());
				oi.setProgrDest(oiOrig.getProgrDest());
				oi.setTipo(oiOrig.getTipo());
				
				oi.setFonte(oiOrig.getFonte());
				oi.setProgr(oiOrig.getProgr());
				oi.setRating(oiOrig.getRating());
				oi.setAtt(oiOrig.getAtt());
				
				
			
			
				String uc="";
				//if (oi.getTipoPersona()== null || oi.getTipoPersona().equals("")){
					uc = mapProps.getProperty(oi.getFonteDest() + "_" + oi.getProgr());
					if (oi.getFonteDest()!= null && oi.getFonteDest().equals("4") && oi.getProgr()!= null && oi.getProgr().equals("2") ){
						if (tipo.equals("2"))
							uc=mapProps.getProperty(oi.getFonteDest() + "_" + oi.getProgr()+ "_V");
						else if (tipo.equals("3"))
							uc=mapProps.getProperty(oi.getFonteDest() + "_" + oi.getProgr()+ "_C");
					}
					
			/*	}
				else{
					uc=mapProps.getProperty(oi.getFonteDest() + "_" + oi.getProgr()+ "_"+ oi.getTipoPersona());
				}*/
				
			
					
				if (uc != null){
					oi.setAppName(Tema.getServletMapping(Integer.parseInt(uc)));
					oi.setUc(uc);
				}
				
				result.add(oi);
			}

		}
		catch(Exception e) {
				log.error("getExtraInfo",e);
		}
		finally {
				try {
					rs.close();
				}
				catch(Throwable t) {}
				try {
					ps.close();
				}
				catch(Throwable t) {}
				try {
					c.close();
				}
				catch(Throwable t) {}
				
		}
	
	   return result;
	   
	}
	
	private List<String> getListaAttributiSoggetti(ResultSet rs ) throws Exception{
		
	
		
		List<String> listaAttributi= new ArrayList<String>();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		
		for (int i=8; i<=columnsNumber;i++){
			String field="";
			// il secondo elemento è di tipo Date, gli altri sono tutti stringa
			if (i!= 9 && i!=34 && i!=35){
				 field= rs.getString(i);
			}
			else{
				Date fieldD= rs.getDate(i);
				SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
				if (fieldD != null)
				field= sdf.format(fieldD);
				
			}
			
			if (field != null && !field.trim().equals(""))
				listaAttributi.add(field);
			else
				listaAttributi.add("-");
			
		}
		
		return listaAttributi;
	}
		
		private List<String> getListaAttributiOggetti(ResultSet rs ) throws Exception{
			
			
			List<String> listaAttributi= new ArrayList<String>();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount(); //41
			
			for (int i=8; i<=columnsNumber;i++){
				
				String field= rs.getString(i);
				
				if (field != null && !field.trim().equals(""))
					listaAttributi.add(field);
				else
					listaAttributi.add("-");
				
			}
		
		return listaAttributi;
	}
		
private List<String> getListaAttributiFabbricati(ResultSet rs ) throws Exception{
			
			
			List<String> listaAttributi= new ArrayList<String>();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount(); //44
			
			for (int i=8; i<=columnsNumber;i++){
				
				String field= rs.getString(i);
				
				if (field != null && !field.trim().equals(""))
					listaAttributi.add(field);
				else
					listaAttributi.add("-");
				
			}
		
		return listaAttributi;
	}
		
	private List<String> getListaAttributiVieCivici(ResultSet rs ) throws Exception{
			
			
			List<String> listaAttributi= new ArrayList<String>();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount(); //18
			
			
			for (int i=8; i<=columnsNumber;i++){
				
				String field= rs.getString(i);
				
				if (field != null && !field.trim().equals(""))
					listaAttributi.add(field);
				else
					listaAttributi.add("-");
				
			}
		
		return listaAttributi;
	}
}