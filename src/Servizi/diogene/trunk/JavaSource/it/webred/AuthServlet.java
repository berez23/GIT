package it.webred;

import it.escsolution.escwebgis.common.EnvSource;

import it.escsolution.escwebgis.common.EnvUtente;
import it.webred.amprofiler.model.AmGroup;
import it.webred.cet.permission.CeTUser;

import java.io.IOException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.security.jacc.PolicyContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

public class AuthServlet extends  HttpServlet {

	private static final long serialVersionUID = -5035314350047424394L;
	private static final String FASCICOLO_CIVICO = "FASCICOLO_CIVICO";
	private static final String CATASTO_IMMOBILI = "CATASTO_IMMOBILI";

	public AuthServlet() {

	}//-------------------------------------------------------------------------
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			/*
			 * parametri URL:
			 * - ente (BELFIORE_ENTE);
			 * - utente
			 * - password
			 * - ot_prik (ONE TIME PRIVATE KEY);
			 * - servizio: FASCICOLO_CIVICO [CODICE VIA, CIVICO/BARRATO]; CATASTO_IMMOBILI [F,P];
			 */

			request = (HttpServletRequest)PolicyContext.getContext("javax.servlet.http.HttpServletRequest");
//			String appName = (String)request.getParameter("app_name");
			String ente = (String)request.getParameter("ente");
		    String jUsername = (String)request.getParameter("j_username");
//		    String jUsername = "monzaM";
		    String jPassword = (String)request.getParameter("j_password");
//		    String jPassword = "monzaM";
		    String otPrik = (String)request.getParameter("ot_prik");
		    String servizio = (String)request.getParameter("servizio");
		    String codVia = (String)request.getParameter("cod_via");
		    String civico = (String)request.getParameter("civico");
		    String foglio = (String)request.getParameter("foglio");
		    String particella = (String)request.getParameter("particella");
		    String ext = "1";

		    
		    if (jUsername != null && jPassword != null && ente != null && servizio != null && otPrik!=null && 
		    		!jUsername.trim().equalsIgnoreCase("") && !jPassword.trim().equalsIgnoreCase("") && !ente.trim().equalsIgnoreCase("") && !servizio.trim().equalsIgnoreCase("") && !otPrik.trim().equalsIgnoreCase("")){
			    /*
				 * JBOSS User
				 */
			    Principal userPrincipal = request.getUserPrincipal();
		        if (userPrincipal != null) {
		            request.logout();
		        }
				request.login(jUsername, jPassword);
				userPrincipal = request.getUserPrincipal();

				if (userPrincipal != null) {
					/*
					 * GIT User: validazione della chiave privata
					 */
					boolean validato = false;
					PreparedStatement st = null;
					ResultSet rs = null;
					Connection con = null;
					try {
						/*
						 * Per accedere al DB è necessario avere la connessione 
						 * agli schemi del comune corrente per cui si deve creare 
						 * l'oggetto CeTUser 
						 */
						List<String> enteList = new LinkedList<String>();
						enteList.add(ente);
						HashMap<String, String> permList = new HashMap<String, String>();
						List<AmGroup> listaGruppi = new LinkedList<AmGroup>();
						
						CeTUser user = new CeTUser();
			            user.setUsername(jUsername);
			            user.setEnteList(enteList);
			            user.setCurrentEnte(ente);
						
						con = getConnectionDiogene(user);
						con.setAutoCommit(false);
						String sql = "SELECT * FROM AM_TRACCIA_ACCESSI "
								+ "WHERE "
								+ "USATA = ? AND "
								+ "PRIK = ? AND "
								+ "ENTE = ? AND "
								+ "USER_NAME = ? ";
						st = con.prepareStatement(sql);
						int paramIndex = 0;
						st.setBoolean(++paramIndex, false);
						st.setString(++paramIndex, otPrik);
						st.setString(++paramIndex, ente);
						st.setString(++paramIndex, jUsername);
						rs = st.executeQuery();
				        
						while (rs.next()) {
							String id = rs.getString("ID");
							validato = true;
						}
				        st.cancel();
				        
						if (validato){
							/*
							 * Segno come usata la chiave primaria in modo da non 
							 * permetterne piu l'utilizzo
							 */
							sql = "UPDATE AM_TRACCIA_ACCESSI "
									+ "SET USATA = ? "
									+ "WHERE "
									+ "PRIK = ? AND "
									+ "ENTE = ? AND "
									+ "USER_NAME = ? ";
							st = con.prepareStatement(sql);
							paramIndex = 0;
							st.setBoolean(++paramIndex, true);
							st.setString(++paramIndex, otPrik);
							st.setString(++paramIndex, ente);
							st.setString(++paramIndex, jUsername);
							st.executeUpdate();
							
							st.cancel();
								
							con.commit();

							/*
							 * Preparo oggetti per la sessione
							 */
							HttpSession ses = request.getSession(true);
				            user.setGroupList(listaGruppi);

//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:RETTE", "1");
//							permList.put("permission@-@fasfab@-@FascFabb@-@Immissione Richieste Fascicolo", "1");
//							permList.put("permission@-@C336@-@Comma336@-@Supervisione Pratiche", "1");
//							permList.put("permission@-@agendaSegnalazioni@-@Agenda Segnalazioni@-@Consultazione Pratiche", "1");
							if (servizio.trim().equalsIgnoreCase(FASCICOLO_CIVICO)){
								permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Tema:Fascicoli", "1");
							}
//							permList.put("permission@-@Virgilio@-@Virgilio@-@mui-supusr", "1");
//							permList.put("permission@-@Bod@-@Ricerca Bod@-@Accertato", "1");
							if (servizio.trim().equalsIgnoreCase(CATASTO_IMMOBILI)){
								permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO", "1");								
							}
//							permList.put("permission@-@CarContrib@-@CarContrib@-@Supervisione Richieste Cartella", "1");
//							permList.put("permission@-@servizioc340@-@Comma340@-@Gestisci Comma340", "1");
//							permList.put("permission@-@CarSociale@-@Notifiche Cartella@-@VISUALIZZA-NOTIFICHE-SETTORE", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CONCEDI VISURE", "1");
//							permList.put("permission@-@CarSociale@-@CarSociale@-@Crea un caso", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:F24", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:IMU", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:PRATICHE PORTALE", "1");
//							permList.put("permission@-@fasfab@-@FascFabb@-@Gestione Richieste Fascicolo", "1");
//							permList.put("permission@-@AMProfiler@-@AMProfiler@-@Nuovo Gruppo", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Tema:Forniture Idriche", "1");
//							permList.put("permission@-@diogene@-@Catalogo Query@-@Accedi a Catalogo Query", "1");
//							permList.put("permission@-@Virgilio@-@Virgilio@-@mui-adm", "1");
//							permList.put("permission@-@Successioni@-@Successioni@-@succ-supusr", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Tema:Forniture Gas", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:GAS", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Tema:Ricerca veloce", "1");
//							permList.put("permission@-@SegretariatoSoc@-@SegretariatoSoc@-@segrsoc-delete", "1");
//							permList.put("permission@-@CarSociale@-@CarSociale@-@Visualizza Elenco Casi", "1");
//							permList.put("permission@-@Virgilio@-@Virgilio@-@mui-usr", "1");
//							permList.put("permission@-@AMProfiler@-@AMProfiler@-@Visualizzazione", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Visualizza Reddito da Lavoro", "1");
//							permList.put("permission@-@Bod@-@Ricerca Bod@-@Istruttoria", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:PLAN C340", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Tema:Visura Nazionale", "1");
//							permList.put("permission@-@CarSociale@-@CarSociale@-@Accedi ai Dati Riservati", "1");
//							permList.put("permission@-@GestPrep@-@Castelletto Prepagato@-@Gestione Castelletto", "1");
//							permList.put("permission@-@Diagnostics_F704@-@Diagnostiche@-@Visualizzazione avanzata", "1");
//							permList.put("permission@-@C336@-@Comma336@-@Gestione Pratiche Comma336", "1");
//							permList.put("permission@-@C336@-@Comma336@-@Visualizzazione Pratiche", "1");
//							permList.put("permission@-@Successioni@-@Successioni@-@succ-adm", "1");
//							permList.put("permission@-@Controller@-@Gestione Controller@-@Amministra Controller", "1");
//							permList.put("permission@-@AMProfiler@-@AMProfiler@-@Nuovo Permesso", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:ACQUA", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:URBANISTICA", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:RUOLO TARSU", "1");
//							permList.put("permission@-@CarSociale@-@CarSociale@-@Gestisci Configurazione", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:PREGEO", "1");
//							permList.put("permission@-@fasfab@-@FascFabb@-@Supervisione Richieste Fascicolo", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Diagnostiche@-@Vedi Diagnostiche", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Scarica Planimetrie", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Tema:Acquedotto e Imp.Termici", "1");
//							permList.put("permission@-@Bod@-@Esporta dati Bod@-@Esportazione completa", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Modifica Stato Aggiornamento Fonti", "1");
//							permList.put("permission@-@Indice@-@Gestione Indice@-@Accedi a Indice", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:DEMOGRAFIA", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:TRIBUTI", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:LOCAZIONI", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:LICCOMMERCIALI", "1");
//							permList.put("permission@-@SegretariatoSoc@-@SegretariatoSoc@-@segrsoc-write", "1");
//							permList.put("permission@-@SegretariatoSoc@-@SegretariatoSoc@-@segrsoc-print", "1");
//							permList.put("permission@-@SegretariatoSoc@-@SegretariatoSoc@-@segrsoc-edit", "1");
//							permList.put("permission@-@SegretariatoSoc@-@SegretariatoSoc@-@segrsoc-funzioniAdmin", "1");
//							permList.put("permission@-@SegretariatoSoc@-@SegretariatoSoc@-@segrsoc-funzioniEntiEsterni", "1");
//							permList.put("permission@-@CarSociale@-@CarSociale@-@Inserisci / Modifica Elementi del Fascicolo", "1");
//							permList.put("permission@-@CarSociale@-@CarSociale@-@Visualizza Carico di Lavoro", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:ECOGRAFICO CATASTALE", "1");
//							permList.put("permission@-@SegretariatoSoc@-@SegretariatoSoc@-@segrsoc-read", "1");
//							permList.put("permission@-@Diagnostics_F704@-@Diagnostiche@-@Visualizzazione Base", "1");
//							permList.put("permission@-@agendaSegnalazioni@-@Agenda Segnalazioni@-@Supervisione", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Tema:Camera di Commercio", "1");
//							permList.put("permission@-@AMProfiler@-@AMProfiler@-@Cancella Utente", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Elimina watermark da planimetrie", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CONCEDI", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:COMPRAVENDITE", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:DOCFA", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:REDDITI", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CNC", "1");
//							permList.put("permission@-@CarSociale@-@Notifiche Cartella@-@VISUALIZZA-NOTIFICHE-ORGANIZZAZIONE", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Tema:Forniture Elettriche", "1");
//							permList.put("permission@-@Successioni@-@Successioni@-@succ-usr", "1");
//							permList.put("permission@-@Bod@-@Ricerca Bod@-@Dichiarato", "1");
//							permList.put("permission@-@Bod@-@Statistiche Bod@-@Esegui Statistiche", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:SUCCESSIONI", "1");
//							permList.put("permission@-@CarContrib@-@CarContrib@-@Gestione Richieste Cartella", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:PUBBLICITA", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:ACQUA NEW", "1");
//							permList.put("permission@-@SpProf@-@SpProf@-@Amministrazione progetti", "1");
//							permList.put("permission@-@CarSociale@-@Notifiche Cartella@-@VISUALIZZA-NOTIFICHE-TIPO-IT", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:VERS ICI MISTERIALE", "1");
//							permList.put("permission@-@CarSociale@-@Notifiche Cartella@-@VISUALIZZA-NOTIFICHE-TIPO-DOCIND", "1");
//							permList.put("permission@-@CarSociale@-@CarSociale@-@Visualizza Fascicolo", "1");
//							permList.put("permission@-@CarSociale@-@CarSociale@-@Eroga Servizi e Interventi", "1");
//							permList.put("permission@-@CarSociale@-@Notifiche Cartella@-@VISUALIZZA-NOTIFICHE-TIPO-FAMGIT", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:MULTE", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:REDDITI ANALITICI", "1");
//							permList.put("permission@-@agendaSegnalazioni@-@Agenda Segnalazioni@-@Gestione Pratiche", "1");
//							permList.put("permission@-@muidocfa@-@MuiDocfa@-@Gestisci MuiDocfa", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Tema:Diagnostiche Docfa", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Tema:Soggetto", "1");
//							permList.put("permission@-@AMProfiler@-@AMProfiler@-@Gestione Permessi", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:FORNITURE ELETTRICHE", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:COSAP/TOSAP", "1");
//							permList.put("permission@-@SegretariatoSoc@-@SegretariatoSoc@-@segrsoc-funzioniSegrProfessionale", "1");
//							permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:RUOLO TARES", "1");
//							permList.put("permission@-@CarSociale@-@CarSociale@-@Visualizza Tutti i Casi del mio Settore", "1");
//							permList.put("permission@-@CarSociale@-@CarSociale@-@Modifica tutti i casi del mio Settore", "1");
//							permList.put("permission@-@CarSociale@-@CarSociale@-@Amministra Organizzazioni/Settori/Operatori", "1");
//							permList.put("permission@-@CarSociale@-@CarSociale@-@Upload Documenti Individuali", "1");
//							permList.put("permission@-@CarSociale@-@CarSociale@-@Visualizza Schede Segretariato", "1");
//							permList.put("permission@-@Tares_F704@-@Tares@-@Gestisci Tares", "1");
							
							user.setPermList(permList);
							
							/*
							 * GRUPPI:
							 * select g.* 
				            from Am_Group g, Am_User_Group ug
				            where UPPER(ug.fk_Am_User) = 'monzaM'
				            and g.fk_Am_Comune = 'F704'
				            and ug.fk_Am_Group = g.name
				            order by g.name
							 */
							//List<AmGroup> listaGruppi = LoginBeanService.getGruppi(utente, ente);
							user.setSessionId(ses.getId());
				            ses.setAttribute("user", user);
				            /*
				             * nascondi: (1= true; 0= false) non mostra i collegamenti 
				             * mappa, 3d, streetview, [altre mappe] in lista e dettaglio catasto immobili
				             * e la correlazione in dettaglio catasto immobili
				             */
				            ses.setAttribute("nascondi", "1");  

//				            String address2 = "/diogene/fascicolo/FascicoloRunnerServlet.jsp";
				            if (servizio.trim().equalsIgnoreCase(FASCICOLO_CIVICO)){
					            String address3 = "/diogene/IndagineCivico?DATASOURCE=jdbc/dbIntegrato&ST=4&UC=111";
					            address3 += "&ente=" + ente;
					            address3 += "&cod_via=" + codVia; 
					            address3 += "&civico=" + civico;
					            address3 += "&EXT=" + ext;
					            response.sendRedirect(response.encodeRedirectURL( address3 ));
				            }else if (servizio.trim().equalsIgnoreCase(CATASTO_IMMOBILI)){
					            String address3 = "/diogene/CatastoImmobili?DATASOURCE=jdbc/dbIntegrato&ST=22&UC=1";
					            address3 += "&ente=" + ente;
					            address3 += "&OGGETTO_SEL="+foglio+"|" + particella; 
					            address3 += "&IND_EXT=" + ext;
					            address3 += "&EXT=" + ext;
					            response.sendRedirect(response.encodeRedirectURL( address3 ));
				            }
//				            String address4 = "/diogene/IndagineCivico";
//				            request.setAttribute("DATASOURCE", "jdbc/dbIntegrato");
//				            request.setAttribute("ST", "1");
//				            request.setAttribute("UC", "111");
//				            request.getRequestDispatcher(address4).forward(request, response);
							
						}else{
							System.out.println("Credenziali GIT non valide!!!");
						}

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						
						try {
							if (rs != null)
								rs.close();
							if (st != null)
								st.close();
							if (con != null)
								con.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}

				}else{
					System.out.println("Credenziali JB non valide!!!");
				}

		    }else{
		    	System.out.println("Parametri non validi o assenti");
		    }

        } catch (Exception e) {
               e.printStackTrace();
        }
	      
	}//-------------------------------------------------------------------------
	
	//protected Connection getConnectionDiogene(HttpServletRequest request)throws Exception {
	protected Connection getConnectionDiogene(CeTUser utente)throws Exception {
		Context initContext = new InitialContext();
		
		//EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		EnvUtente eu = new EnvUtente(utente, null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		
		DataSource ds = (DataSource) initContext.lookup(es.getDataSource());
		Connection conn = ds.getConnection();
		return conn;
	}//-------------------------------------------------------------------------
	
	

}
