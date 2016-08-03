package it.webred.gitout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class GitOutBase {
	
	protected final static String ITEM_VISUALIZZA_FONTI_DATI = "Visualizzazione Fonti Dati";
	protected final static String ITEM_CATALOGO_QUERY = "Catalogo Query";
	protected final static String ITEM_DIAGNOSTICHE = "Visualizzazione Diagnostiche";
	protected final static String PERMESSO_DIAGNOSTICHE = "Vedi Diagnostiche";
	protected final static String PERMESSO_CATALOGO_QUERY = "Accedi a Catalogo Query";
	protected final static String PERMESSO_SCARICA_PLANIMETRIE = "Scarica Planimetrie";
	protected final static String PERMESSO_ELIMINA_WATERMARK = "Elimina watermark da planimetrie";
	protected final static String PERMESSO_VISUALIZZA_REDDITO_LAVORO = "Visualizza Reddito da Lavoro";
	protected final static String PERMESSO_MODIFICA_STATO_AGGIORNAMENTO_FONTI = "Modifica Stato Aggiornamento Fonti";
	protected final static String PERMESSO_TEMA_VISURA_NAZIONALE = "Tema:Visura Nazionale";
	protected final static String PERMESSO_GESTIONE_LINK_ESTERNI = "Gestione Link Esterni";
	
	public static HashMap<String,String[]> mappaTemiFonti= new HashMap<String,String[]>();

	 static{
			
			mappaTemiFonti.put("ACQUEDOTTO E IMP.TERMICI", null);
			mappaTemiFonti.put("CAMERA DI COMMERCIO", null);
			
			mappaTemiFonti.put("CATASTO", new String[]{"CATASTO"});
			mappaTemiFonti.put("COMPRAVENDITE", new String[]{"COMPRAVENDITE"});
			mappaTemiFonti.put("CONCESSIONI EDILIZIE", new String[]{"CONCEDI","CONCEDI VISURE"});
			mappaTemiFonti.put("COSAP/TOSAP", new String[]{"COSAP/TOSAP"});
			mappaTemiFonti.put("DOCFA", new String[]{"DOCFA"});
			mappaTemiFonti.put("ECOGRAFICO CATASTALE",new String[]{"ECOGRAFICO CATASTALE"});
			mappaTemiFonti.put("FORNITURE ELETTRICHE",new String[]{"FORNITURE ELETTRICHE"});
			mappaTemiFonti.put("FORNITURE GAS",new String[]{"GAS"});
			mappaTemiFonti.put("LICENZE COMMERCIO", new String[]{"LICCOMMERCIALI"});
			mappaTemiFonti.put("LOCAZIONI", new String[]{"LOCAZIONI"});
			mappaTemiFonti.put("POPOLAZIONE", new String[]{"DEMOGRAFIA"});
			mappaTemiFonti.put("PRATICHE PORTALE", new String[]{"PRATICHE PORTALE"});
			mappaTemiFonti.put("PREGEO", new String[]{"PREGEO"});
			mappaTemiFonti.put("PUBBLICITA", new String[]{"PUBBLICITA"});
			mappaTemiFonti.put("REDDITI", new String[]{"REDDITI"});
			mappaTemiFonti.put("REDDITI ANALITICI", new String[]{"REDDITI ANALITICI"});
			mappaTemiFonti.put("RETTE", new String[]{"RETTE"});
			mappaTemiFonti.put("SUCCESSIONI", new String[]{"SUCCESSIONI"});
			mappaTemiFonti.put("TOPONOMASTICA", new String[]{"CATASTO"});
			mappaTemiFonti.put("TRAFFICO", new String[]{"MULTE"});;
			mappaTemiFonti.put("TRIBUTI", new String[]{"TRIBUTI","IMU"});
			mappaTemiFonti.put("RUOLI E VERSAMENTI", new String[]{"TRIBUTI","F24","RUOLO TARSU","RUOLO TARES","VERS ICI MISTERIALE","VERS TAR POSTE"});
			mappaTemiFonti.put("UTENZE", new String[]{"ACQUA NEW","ACQUA","FORNITURE ELETTRICHE","GAS"});
			mappaTemiFonti.put("URBANISTICA", new String[]{"URBANISTICA"}); //Olbia
				
		}


	public GitOutBase() {
	}//-------------------------------------------------------------------------

	protected static Object getEjb(String ear, String module, String ejbName){
		Context cont;
		try {
			cont = new InitialContext();
			return cont.lookup("java:global/" + ear + "/" + module + "/" + ejbName);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
	}//-------------------------------------------------------------------------
	
	protected HashMap<String, String> getPermissionOfUser(Connection conn, String utente, String belfiore){
		HashMap<String, String> hmPermission = new HashMap<String, String>(); 

		String qry = "select DISTINCT 'permission@-@'|| ISTANZA ||'@-@'|| SEL.FK_AM_ITEM ||'@-@'|| p.NAME PERMISSION , 1 as val "
				+ "from (  "
				+ "select distinct air.ID ID_AIR,  i.NAME ISTANZA  , AI.FK_AM_ITEM,  u.NAME uname, u.DISABLE_CAUSE udiscause, ic.FK_AM_COMUNE comune, a.FLG_DATA_ROUTER dr "
				+ "from AM_AI_ROLE air "
				+ "LEFT JOIN AM_USER_AIR uair on uair.FK_AM_AI_ROLE=air.ID "
				+ "LEFT JOIN AM_USER u on uair.FK_AM_USER=u.NAME "
				+ "LEFT JOIN AM_INSTANCE_COMUNE ic1 on uair.fk_am_comune = ic1.fk_am_comune "
				+ "LEFT JOIN AM_APPLICATION_ITEM ai ON air.FK_AM_APPLICATION_ITEM = ai.ID "
				+ "LEFT JOIN AM_APPLICATION a ON ai.FK_AM_APPLICATION = a.NAME "
				+ "LEFT JOIN AM_INSTANCE i ON ai.FK_AM_APPLICATION = i.FK_AM_APPLICATION "
				+ "LEFT JOIN AM_INSTANCE_COMUNE ic ON i.NAME = ic.FK_AM_INSTANCE "
				+ "where (ic1.fk_am_comune = ic.fk_am_comune "
				+ "and ic1.fk_am_instance = ic.fk_am_instance) or u.name ='profiler' "
				+ "UNION "
				+ "select distinct air.ID ID_AIR, i.NAME ISTANZA , AI.FK_AM_ITEM,  u.NAME uname, u.DISABLE_CAUSE udiscause, ic.FK_AM_COMUNE comune, a.FLG_DATA_ROUTER dr "
				+ "from AM_AI_ROLE air "
				+ "LEFT JOIN AM_GROUP_AIR gair on gair.FK_AM_AI_ROLE=air.ID "
				+ "LEFT JOIN AM_GROUP g on gair.FK_AM_GROUP=g.NAME "
				+ "LEFT JOIN AM_USER_GROUP ug on ug.FK_AM_GROUP=g.NAME "
				+ "LEFT JOIN AM_USER u on ug.FK_AM_USER=u.NAME "
				+ "LEFT JOIN AM_APPLICATION_ITEM ai ON air.FK_AM_APPLICATION_ITEM = ai.ID "
				+ "LEFT JOIN AM_APPLICATION a ON ai.FK_AM_APPLICATION = a.NAME "
				+ "LEFT JOIN AM_INSTANCE i ON ai.FK_AM_APPLICATION = i.FK_AM_APPLICATION "
				+ "LEFT JOIN AM_INSTANCE_COMUNE ic ON i.NAME = ic.FK_AM_INSTANCE "
				+ ") sel , "
				+ "AM_PERMISSION_AIR PAIR, AM_PERMISSION P "
				+ "where PAIR.FK_AM_AI_ROLE = sel.id_air "
				+ "and PAIR.FK_AM_PERMISSION = P.NAME "
				+ "AND P.FK_AM_ITEM =  SEL.FK_AM_ITEM "
				+ "and udiscause IS NULL "
				+ "and uname=  ? "
				+ "and (dr = 'N' or comune = ?) ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement( qry );
			pstmt.setString(1, utente);
			pstmt.setString(2, belfiore);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String permission = rs.getString("PERMISSION");
				String val = rs.getString("VAL");
				hmPermission.put(permission, permission);
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			try{
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				
			}catch(SQLException sqle){
				sqle.printStackTrace();
			}
		}
		
		return hmPermission;

	}//-------------------------------------------------------------------------
	
	protected Long getOtpValidationByToken(Connection conn, String utente, String belfiore, String ot_prik){
		Long validato = 0l;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			
			String sql = "SELECT * FROM AM_TRACCIA_ACCESSI "
					+ "WHERE "
					+ "USATA = ? AND "
					+ "PRIK = ? AND "
					+ "ENTE = ? AND "
					+ "USER_NAME = ? ";
		
		ps = conn.prepareStatement(sql);

			int paramIndex = 0;
			ps.setBoolean(++paramIndex, false);
			ps.setString(++paramIndex, ot_prik);
			ps.setString(++paramIndex, belfiore);
			ps.setString(++paramIndex, utente);
		rs = ps.executeQuery();
				        
			while (rs.next()) {
				validato = rs.getLong("ID");
			}
			
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				
			}catch(SQLException sqle){
				sqle.printStackTrace();
			}
		}
			
		return validato;

	}//-------------------------------------------------------------------------
	
	protected Long getOtpValidationByIp(Connection conn, String utente, String belfiore, String authType, String remoteHost){
		Long validato = 0l;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			boolean ok = false;
			String[] ips = authType.split(";");
			if (ips != null && ips.length>0){
				/*
				 * VALUE_CONF dovrebbe contenere un valore simile a questo: IP;172.16.2.117;10.1.1.190;
				 * per cui il primo valore dell'ary conterrà la stringa "IP"
				 */
				for(int j=1; j<ips.length; j++){
					String currentIP = ips[j];
					if (currentIP != null && currentIP.trim().equalsIgnoreCase(remoteHost))
						ok = true;
				}
			}

			if (ok){
				/*
				 * Inserisco la riga in AM_TRACCIA_ACCESSI
				 * 
				 * Insert into AM_TRACCIA_ACCESSI (USER_NAME, RAGIONE_ACCESSO, FK_AM_ITEM, PRATICA, TIME_ACCESSO, ID)
 					Values
   				   ('monzaM', 'x', 'AMProfiler', 'x', TO_TIMESTAMP_TZ('27/05/2013 16:17:53,000000','DD/MM/YYYY HH24:MI:SS,FF'), 61);
 
				 */
				String sql = "SELECT MAX(ID) MAX_ID FROM AM_TRACCIA_ACCESSI";
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				Long maxId = 0l;
				while (rs.next()) {
					maxId = rs.getLong("MAX_ID");
				}
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				
				sql = "Insert into AM_TRACCIA_ACCESSI (USER_NAME, RAGIONE_ACCESSO, FK_AM_ITEM, PRATICA, TIME_ACCESSO, ID, ENTE, PUBK) VALUES "
						+ "(?, ?, ?, ?, ?, ?, ?, ?) ";
			
				ps = conn.prepareStatement(sql);

				int paramIndex = 0;
				ps.setString(++paramIndex, utente);
				ps.setString(++paramIndex, "RAGIONE ESTERNA GIT");
				ps.setString(++paramIndex, "diogene");
				ps.setString(++paramIndex, "ESTERNA AL GIT");
				ps.setTimestamp(++paramIndex, new Timestamp(System.currentTimeMillis()) );
				ps.setLong(++paramIndex, maxId+1);
				ps.setString(++paramIndex, belfiore);
				ps.setString(++paramIndex, remoteHost);
				
				int ris = ps.executeUpdate();
				
				//conn.commit();
				
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				
				validato = maxId;
			}
			
	
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				
				
		
			}catch(SQLException sqle){
				sqle.printStackTrace();
			}
		}
			
		return validato;

	}//-------------------------------------------------------------------------
	
	protected String getGitOutAuthenticationType(Connection conn, String belfiore){
		String valueConf = "";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			
			String sql = "SELECT * FROM AM_KEY_VALUE_EXT "
					+ "WHERE "
					+ "KEY_CONF = ? AND "
					+ "FK_AM_COMUNE = ? ";
		
			ps = conn.prepareStatement(sql);

			int paramIndex = 0;
			ps.setString(++paramIndex, "gitout.tipo.autenticazione");
			ps.setString(++paramIndex, belfiore);

			rs = ps.executeQuery();
			while (rs.next()) {
				valueConf = rs.getString("VALUE_CONF");
			}
			
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				
			}catch(SQLException sqle){
				sqle.printStackTrace();
			}
		}
			
		return valueConf;
	}//-------------------------------------------------------------------------
	
	protected String notNullAndTrim(String s){
		String v = "";
		if (s != null)
			v = s.trim();
		return v;
	}//-------------------------------------------------------------------------
	
	protected void setOtpValidation(Connection conn, Long idValid){
		
		PreparedStatement pstmt = null;
				
		try{

			/*
			 * Segno come usata la chiave primaria in modo da non 
			 * permetterne piu l'utilizzo
			 */
			String sql = "UPDATE AM_TRACCIA_ACCESSI "
					+ "SET USATA = ? "
					+ "WHERE "
					+ "ID = ? ";
			
			pstmt = conn.prepareStatement(sql);
			int paramIndex = 0;
			pstmt.setBoolean(++paramIndex, true);
			pstmt.setLong(++paramIndex, idValid);
			pstmt.executeUpdate();
			
			pstmt.cancel();
				
			if (pstmt != null) {
				pstmt.close();
			}
				
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{

				if (pstmt != null) {
					pstmt.close();
				}
				
			}catch(SQLException sqle){
				sqle.printStackTrace();
			}
		}
		

	}//-------------------------------------------------------------------------
	
	protected Boolean gitAuthentication(Connection conn, String usr) {
		Boolean autenticato = false;
		
		PreparedStatement ps0 = null;
		ResultSet rs0 = null;
		
//		PreparedStatement ps1 = null;
//		ResultSet rs1 = null;

		try{
			/*
	         * verifico la presenza dell'utente in GIT recuperando la password 
	         */

			String sql = "SELECT PWD FROM AM_USER WHERE NAME = ? ";

			ps0 = conn.prepareStatement(sql);
			int paramIndex = 0;
			ps0.setString(++paramIndex, usr);

			rs0 = ps0.executeQuery();
			String pazzword = "";        
			while (rs0.next()) {
				pazzword = rs0.getString("PWD");
				autenticato = true;
			}
			/*
			 * decodifica pwd
			 */
//			MessageDigest m = MessageDigest.getInstance("MD5");  
//	        m.update(pwd.getBytes(), 0, pwd.length());  
//	        String md5Pwd = new BigInteger(1, m.digest()).toString(16);
//	        
//	        System.out.println("Input MD5 Pwd: "+ md5Pwd);  
//	        System.out.println("Database MD5 Pwd: "+ pazzword);
	        
			
//	        if (pazzword.equalsIgnoreCase(md5Pwd)){
		        	
//					String qry = "SELECT DT_UPD_PWD FROM AM_USER U WHERE NAME = ? ";
//					ps1 = conn.prepareStatement(qry);
//					paramIndex = 0;
//					ps1.setString(++paramIndex, usr);
//
//					rs1 = ps1.executeQuery();
//					Date dtUpdPwd = null;
//					while (rs1.next()) {
//						dtUpdPwd = rs1.getDate("DT_UPD_PWD");
//					}
//					 
//					 Calendar calUpdPwd = Calendar.getInstance();
//					 calUpdPwd.setTime(dtUpdPwd);
//					 calUpdPwd.add(Calendar.DAY_OF_YEAR, 90);
//					 Calendar calOggi = Calendar.getInstance();
//					 calOggi.set(Calendar.HOUR_OF_DAY, 0);
//					 calOggi.set(Calendar.MINUTE, 0);
//					 calOggi.set(Calendar.SECOND, 0);
//					 calOggi.set(Calendar.MILLISECOND, 0);
//					 Boolean pwdValida = new Boolean(calOggi.getTime().getTime() <= calUpdPwd.getTime().getTime()); 
//					 if (pwdValida){
//						 autenticato = true;
//					 }else{
//						 System.out.println("Pwd Scaduta");	 
//					 }
					
//		        }else{
//		        	System.out.println("Usr e Pwd Errati");
//		        }
	        
	        if (rs0 != null) {
				rs0.close();
			}
			if (ps0 != null) {
				ps0.close();
			}
//			 if (rs1 != null) {
//				rs1.close();
//			}
//			if (ps1 != null) {
//				ps1.close();
//			}

			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{

				if (rs0 != null) {
					rs0.close();
				}
				if (ps0 != null) {
					ps0.close();
				}
//				 if (rs1 != null) {
//					rs1.close();
//				}
//				if (ps1 != null) {
//					ps1.close();
//				}
				
			}catch(SQLException sqle){
				sqle.printStackTrace();
			}
		}
		
		return autenticato;
	}//-------------------------------------------------------------------------
	
	protected String getCommValueCalcType(Connection conn, String belfiore){
		String valueConf = "";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			
			String sql = "SELECT * FROM AM_KEY_VALUE_EXT "
					+ "WHERE "
					+ "KEY_CONF = ? AND "
					+ "FK_AM_COMUNE = ? ";
		
			ps = conn.prepareStatement(sql);

			int paramIndex = 0;
			ps.setString(++paramIndex, "docfa.modalita.calcolo.valore.commerciale");
			ps.setString(++paramIndex, belfiore);

			rs = ps.executeQuery();
			while (rs.next()) {
				valueConf = rs.getString("VALUE_CONF");
			}
			
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				
			}catch(SQLException sqle){
				sqle.printStackTrace();
			}
		}
			
		return valueConf;
	}//-------------------------------------------------------------------------
	
}
