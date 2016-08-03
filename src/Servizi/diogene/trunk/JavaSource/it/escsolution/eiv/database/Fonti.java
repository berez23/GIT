package it.escsolution.eiv.database;

import it.escsolution.escwebgis.common.EnvBase;
import it.escsolution.escwebgis.common.EnvSource;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.webred.cet.permission.CeTUser;
import it.webred.ct.data.access.basic.fonti.FontiDataIn;
import it.webred.ct.data.access.basic.fonti.FontiService;
import it.webred.ct.data.access.basic.fonti.dto.FontiDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class Fonti extends EnvBase{
	
	
	private static final SimpleDateFormat FORMATO_DATA = new SimpleDateFormat("dd/MM/yyyy");
	
	private static final String SQL_SELECT_TEMI = "SELECT * FROM EWG_TEMA " +
			"WHERE FK_PROGETTO IN (2,3) AND URLHOME IS NOT NULL " +
			"ORDER BY FK_PROGETTO, ORDINAMENTO";
	
	private static final String SQL_SELECT_DATI_AGGIORNAMENTO = "SELECT COUNT(*) AS CONTA FROM COLS " +
			"WHERE TABLE_NAME = 'EWG_TEMA' " +
			"AND COLUMN_NAME IN ('DATA_AGG', 'SQL_DATA_AGG', 'NOTE', 'DATA_INI', 'SQL_DATA_INI', 'COD_COMMAND_RE_AGG')";
	
	private static final String SQL_SELECT_RE_MAX_DATE_START = "SELECT MAX(DATE_START) AS MAX_DATE_START " + 
			"FROM R_COMMAND_LAUNCH CL, R_COMMAND C " +
			"WHERE C.ID = CL.FK_COMMAND " +
			"AND C.COD_COMMAND = ? " +
			"AND DATE_END IS NOT NULL " +
			"AND NOT EXISTS (SELECT 1 FROM R_COMMAND_ACK A " +
			"WHERE A.FK_COMMAND = C.ID " +
			"AND A.FK_COMMAND_LAUNCH = CL.ID " +
			"AND A.ACK_NAME = 'ErrorAck')";
	
	private static final String SQL_SELECT_RE_PROCESSID = "SELECT PROCESSID " + 
			"FROM R_COMMAND_LAUNCH CL, R_COMMAND C " +
			"WHERE C.ID = CL.FK_COMMAND " +			
			"AND C.COD_COMMAND = ? " +			
			"AND DATE_START = ?";
	
	private static final String SQL_SELECT_RE_SIT_SINTESI_PROCESSI = "SELECT * " +
			"FROM SIT_SINTESI_PROCESSI WHERE PROCESSID = ? " +
			"ORDER BY NOME_TABELLA";
	
	private static final String STR_RITORNO_CAPO = "\\r\\n"; //per visualizzazione in popup html (alert)
	
	private static final Logger log = Logger.getLogger("diogene.log");
	
	private static final String SQL_SELECT_TRACCIA_STATI = "select rts.istante as ISTANTE, rts.NOTE from R_TRACCIA_STATI rts where rts.belfiore = ?	and rts.id_fonte = ? order by istante desc";
	//private static final String SQL_SELECT_DATA_AGGIORNAMENTO = "select MAX(rts.istante) as ISTANTE  from R_TRACCIA_STATI rts where rts.belfiore = ?	and rts.id.idFonte = ?";
	private static final String SQL_SELECT_FONTI = "SELECT ID, DESCRIZIONE, TIPO_FONTE " +
								"FROM AM_FONTE F " +
								"WHERE EXISTS (SELECT * FROM AM_FONTE_COMUNE FC " +
								"WHERE F.ID = FK_AM_FONTE " +
								"AND FK_AM_COMUNE = ?) " +
								"ORDER BY TIPO_FONTE DESC, N_ORDINE";

	private static final String SQL_SELECT_SQL_DATA_INIZIO= "select VALUE_CONF AS SQL from AM_KEY_VALUE_EXT where KEY_CONF='sql.data.inizio' AND FK_AM_COMUNE = ? AND FK_AM_FONTE= ? ";
	private static final String SQL_SELECT_SQL_DATA_AGGIORNAMENTO= "select VALUE_CONF AS SQL from AM_KEY_VALUE_EXT where KEY_CONF='sql.data.aggiornamento' AND FK_AM_COMUNE = ? AND FK_AM_FONTE= ? ";
	

	
	public ArrayList<FonteNew> getFonti(HttpServletRequest request) {
		ArrayList<FonteNew> fonti = new ArrayList<FonteNew>();		
		Connection conn = null;
		Connection connDiogene = null;
		
		try {
			Context ctx = new InitialContext();
			if(ctx == null) 
				throw new Exception("Boom - No Context");
			
			EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			DataSource ds = (DataSource)ctx.lookup(es.getDataSourceIntegrato());
			DataSource dsDiogene = (DataSource)ctx.lookup(es.getDataSource());
			
			FontiService fontiService= (FontiService) getEjb("CT_Service", "CT_Service_Data_Access", "FontiServiceBean");
			
			if (ds != null && dsDiogene != null) {
				conn = ds.getConnection();
				connDiogene = dsDiogene.getConnection();
				if (connDiogene != null)  {
					PreparedStatement pstmt = connDiogene.prepareStatement(SQL_SELECT_FONTI);
					pstmt.setString(1, eu.getEnte());
					ResultSet rst = pstmt.executeQuery();						
					while(rst.next()) {
						FonteNew fonte = new FonteNew();
						
						fonte.setId(rst.getInt("ID"));
						if (rst.getObject("TIPO_FONTE") == null) {
							fonte.setDescrizioneTipoFonte("");
							fonte.setTipoFonte("-");
						} else {
							fonte.setTipoFonte(rst.getString("TIPO_FONTE"));
							if (rst.getString("TIPO_FONTE").equals("I")) {
								fonte.setDescrizioneTipoFonte("Archivi Interni");
							} else if (rst.getString("TIPO_FONTE").equals("E")){
								fonte.setDescrizioneTipoFonte("Archivi Esterni");
							}
						}
						fonte.setDescrizione(rst.getObject("DESCRIZIONE") == null ? "" : rst.getString("DESCRIZIONE").trim());
						setDatiTracciaStati(connDiogene, fonte, eu.getEnte(), fontiService);
						//setDatiSqlDate(connDiogene, fonte, eu.getEnte());
						fonti.add(fonte);
					}
				}
			}
		} catch(Exception e) {
			log.debug(e);
		} finally {
			try {
				if(conn != null)
					conn.close();
				if(connDiogene != null)
					connDiogene.close();
			} catch (SQLException e) {
				log.debug(e);
			}			
		}
		return fonti;
	}
	
	public boolean isDatiAggiornamento(HttpServletRequest request) {
		boolean datiAggiornamento = false;
		Connection conn = null;
		try {
			Context ctx = new InitialContext();
			if(ctx == null)
				throw new Exception("Boom - No Context");
			EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			log.debug("Ente ["+eu.getEnte()+"]");
			DataSource ds = (DataSource)ctx.lookup(es.getDataSourceIntegrato());
			if (ds != null) {
				conn = ds.getConnection();
				if (conn != null)  {
					PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_DATI_AGGIORNAMENTO);
					ResultSet rst = pstmt.executeQuery();						
					while(rst.next()) {
						datiAggiornamento = rst.getObject("CONTA") != null && rst.getInt("CONTA") == 6;
					}
				}
			}
		} catch(Exception e) {
			log.debug(e);
		} finally {
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				log.debug(e);
			}			
		}
		return datiAggiornamento;
	}
	
	private void setDataAggiornamento(Connection conn, Connection connDiogene, Fonte fonte, Object dataAgg, Object codCommandReAgg, Object sqlDataAgg) {
		fonte.setLogSitSintesiProcessi("");		
		try {
			if (sqlDataAgg == null || ((String)sqlDataAgg).trim().equals("")) {
				if (codCommandReAgg != null && !((String)codCommandReAgg).trim().equals("")) {					
					//data aggiornamento (in questo caso si usa la connessione diogene
					PreparedStatement pstmt = connDiogene.prepareStatement(SQL_SELECT_RE_MAX_DATE_START);
					pstmt.setString(1, ((String)codCommandReAgg).trim());
					ResultSet rst = pstmt.executeQuery();
					Timestamp maxDateStart = null;
					while (rst.next()) {
						if (rst.getObject("MAX_DATE_START") != null) {
							maxDateStart = rst.getTimestamp("MAX_DATE_START");
						}
					}
					pstmt.close();
					if (maxDateStart != null) {
						fonte.setDataAggiornamento(FORMATO_DATA.format(maxDateStart));
						//dati su record inseriti, aggiornati, sostituiti
						pstmt = connDiogene.prepareStatement(SQL_SELECT_RE_PROCESSID);
						pstmt.setString(1, ((String)codCommandReAgg).trim());
						pstmt.setTimestamp(2, maxDateStart);
						rst = pstmt.executeQuery();
						String processId = null;
						while (rst.next()) {
							if (rst.getObject("PROCESSID") != null) {
								processId = rst.getString("PROCESSID").trim();
							}
						}
						pstmt.close();
						pstmt = connDiogene.prepareStatement(SQL_SELECT_RE_SIT_SINTESI_PROCESSI);
						pstmt.setString(1, processId);
						rst = pstmt.executeQuery();
						String logSitSintesiProcessi = "";
						while (rst.next()) {
							if (rst.getObject("NOME_TABELLA") != null) {
								if (!logSitSintesiProcessi.trim().equals("")) {
									logSitSintesiProcessi += STR_RITORNO_CAPO;
								}
								logSitSintesiProcessi += "Tabella " + rst.getString("NOME_TABELLA").trim() + ": ";
								int inseriti = rst.getObject("INSERITI") == null ? 0 : rst.getInt("INSERITI");	
								logSitSintesiProcessi += inseriti;							
								logSitSintesiProcessi += " record inserit" + (inseriti == 1 ? "o" : "i") + ", ";
								int aggiornati = rst.getObject("AGGIORNATI") == null ? 0 : rst.getInt("AGGIORNATI");	
								logSitSintesiProcessi += aggiornati;							
								logSitSintesiProcessi += " record aggiornat" + (aggiornati == 1 ? "o" : "i") + ", ";
								int sostituiti = rst.getObject("SOSTITUITI") == null ? 0 : rst.getInt("SOSTITUITI");	
								logSitSintesiProcessi += sostituiti;							
								logSitSintesiProcessi += " record sostituit" + (sostituiti == 1 ? "o" : "i");
							}							
						}
						fonte.setLogSitSintesiProcessi(logSitSintesiProcessi);
					} else {
						fonte.setDataAggiornamento(dataAgg == null ? "-" : FORMATO_DATA.format((java.util.Date)dataAgg));
					}
				} else {
					fonte.setDataAggiornamento(dataAgg == null ? "-" : FORMATO_DATA.format((java.util.Date)dataAgg));
				}				
			} else {
				String sql = (String)sqlDataAgg;
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rst = pstmt.executeQuery();
				while(rst.next()) {
					//la query deve avere il campo data aggiornamento (di tipo date) come DATA_AGG
					fonte.setDataAggiornamento(rst.getObject("DATA_AGG") == null ? "-" : FORMATO_DATA.format(rst.getDate("DATA_AGG")));
				}
			}
		} catch (Exception e) {
			//non deve bloccare il ciclo...
			fonte.setDataAggiornamento("-");
		}
	}
	
	private void setDataInizio(Connection conn, Fonte fonte, Object dataIni, Object sqlDataIni) {
		try {
			if (sqlDataIni == null || ((String)sqlDataIni).trim().equals("")) {
				fonte.setDataInizio(dataIni == null ? "-" : FORMATO_DATA.format((java.util.Date)dataIni));
			} else {
				String sql = (String)sqlDataIni;
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rst = pstmt.executeQuery();
				while(rst.next()) {
					//la query deve avere il campo data inizio (di tipo date) come DATA_INI
					fonte.setDataInizio(rst.getObject("DATA_INI") == null ? "-" : FORMATO_DATA.format(rst.getDate("DATA_INI")));
				}
			}
		} catch (Exception e) {
			//non deve bloccare il ciclo...
			fonte.setDataInizio("-");
		}
	}
	
	private void setDatiTracciaStati(Connection conn, FonteNew fonte, String ente, FontiService fontiService) {
		try {
			
				String sql = SQL_SELECT_TRACCIA_STATI;
				PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				pstmt.setString(1, ente);
				pstmt.setInt(2, fonte.getId());
				ResultSet rst = pstmt.executeQuery();
				
				fonte.setDataInizio("-");
				fonte.setDataAggiornamento("-");
				fonte.setNote("-");
				
			while(rst.next()){	
				if (rst.isFirst()) {
					//il primo elemento e l'ultimo in ordine di tempo
					Date dataAggiornamento=null;
					if (rst.getObject("ISTANTE") != null )
						dataAggiornamento= new Date(rst.getLong("ISTANTE"));
					fonte.setDataAggiornamento(rst.getObject("ISTANTE") == null ? "-" : FORMATO_DATA.format(dataAggiornamento));
					fonte.setNote(rst.getObject("NOTE") == null ? "-" : rst.getString("NOTE") );
					
				}	
			
			
			else if (rst.isLast()) {
					//l'ultimo elemento e il primo in ordine di tempo
					Date dataInizio=null;
					if (rst.getObject("ISTANTE") != null )
						dataInizio= new Date(rst.getLong("ISTANTE"));
					fonte.setDataInizio(rst.getObject("ISTANTE") == null ? "-" : FORMATO_DATA.format(dataInizio));
					
					}
				}
			
		} catch (Exception e) {
			//non deve bloccare il ciclo...
			fonte.setDataInizio("-");
			fonte.setDataAggiornamento("-");
			fonte.setNote("-");
			log.debug("Errore non bloccante setDatiTracciaStati: Fonte["+fonte.getId()+"]"+e);
		}
		
		//carico data di riferimento
		try{
			
			//Guardo se TRACCIA_DATE  e valorizzato
			FontiDataIn dataIn= new FontiDataIn();
			dataIn.setEnteId(ente);
			dataIn.setIdFonte(fonte.getId().toString());
			FontiDTO dto = fontiService.getDateRifFonteTracciaDate(dataIn);
			
			boolean tracciaDatePresente = false;
			if(dto!=null && dto.getDataRifInizio()!=null)
				tracciaDatePresente = true;
			
			//altrimenti carico l'SQL (PROPERTIES)
			if(!tracciaDatePresente)
				dto =  fontiService.getDateRiferimentoFonte(dataIn);
			
			
			if(dto!=null && dto.getDataRifInizio() != null && dto.getDataRifAggiornamento() != null)
				fonte.setDataRiferimento("Da " + FORMATO_DATA.format(dto.getDataRifInizio()) + " a " + FORMATO_DATA.format(dto.getDataRifAggiornamento()));
			else 
				fonte.setDataRiferimento("-");
			
		}catch (Exception e) {
			log.debug(e);
		}
	}
	
	private void setDatiSqlDate(Connection conn, FonteNew fonte, String ente) {
		try {
			
				String sql = SQL_SELECT_SQL_DATA_INIZIO;
				String sqlDataInizio= null;
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, ente);
				pstmt.setInt(2, fonte.getId());
				ResultSet rst = pstmt.executeQuery();
				
				
				while (rst.next()) {
					
					sqlDataInizio = (rst.getObject("SQL") == null ? "" : rst.getString("SQL"));
					
					
				}
				
				if (sqlDataInizio!= null && !sqlDataInizio.equals("")){
					pstmt = conn.prepareStatement(sqlDataInizio);
					 rst = pstmt.executeQuery();
					while(rst.next()) {
						//la query deve avere il campo data inizio (di tipo date) come DATA_INI
						fonte.setDataInizio(rst.getObject("DATA_INI") == null ? "-" : FORMATO_DATA.format(rst.getDate("DATA_INI")));
					}
				}
			
				
				sql = SQL_SELECT_SQL_DATA_AGGIORNAMENTO;
				String sqlDataAgg= null;
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, ente);
				pstmt.setInt(2, fonte.getId());
				 rst = pstmt.executeQuery();
				
				
				while (rst.next()) {
					
					sqlDataAgg = (rst.getObject("SQL") == null ? "" : rst.getString("SQL"));
					
					
				}
				
				if (sqlDataAgg!= null && !sqlDataAgg.equals("")){
					pstmt = conn.prepareStatement(sqlDataAgg);
					 rst = pstmt.executeQuery();
					while(rst.next()) {
						//la query deve avere il campo data inizio (di tipo date) come DATA_INI
						fonte.setDataAggiornamento(rst.getObject("DATA_AGG") == null ? "-" : FORMATO_DATA.format(rst.getDate("DATA_AGG")));
					}
				}
					
					
				
			
		} catch (Exception e) {
			//non deve bloccare il ciclo...
			log.debug(e);
		}
	}
	
	/*private void setDataAggiornamento(Connection conn, FonteNew fonte, String ente) {
		try {
			
				String sql = SQL_SELECT_DATA_AGGIORNAMENTO;
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, ente);
				pstmt.setInt(2, fonte.getId());
				ResultSet rst = pstmt.executeQuery();
				while(rst.next()) {
					//la query deve avere il campo data inizio (di tipo date) come DATA_INI
					fonte.setDataAggiornamento(rst.getObject("ISTANTE") == null ? "-" : FORMATO_DATA.format(rst.getDate("ISTANTE")));
				}
			
		} catch (Exception e) {
			//non deve bloccare il ciclo...
			fonte.setDataAggiornamento("-");
		}
	}*/
	
}
