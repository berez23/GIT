package it.webred.servlet;

import it.webred.cet.permission.CeTUser;
import it.webred.common.EnvSource;
import it.webred.common.TsEnvUtente;
import it.webred.utilities.CryptoroUtils;
import it.webred.utilities.DesEncrypter;

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

public class TsSoggiornoAuthServlet extends HttpServlet {
	
	private static final String ENCODER_ID_KEY = "it.webred.desalgorithm.encodekey";

	
	
	private static final long serialVersionUID = 1L;
	private CeTUser cetUser = null;
	private String ente = "";
	private String jUsername = "";
	private String jPassword;
	private String otPrik;

	public TsSoggiornoAuthServlet() {
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		boolean isbOk = false;
		Connection con = null;
		try {
			/*
			 * parametri URL: - ente (BELFIORE_ENTE); - utente - ot_prik (ONE
			 * TIME PRIVATE KEY);
			 */
			request = (HttpServletRequest) PolicyContext.getContext("javax.servlet.http.HttpServletRequest");
			ente = (String) request.getParameter("ente");
			jUsername = (String) request.getParameter("j_username");
			jPassword = (String) request.getParameter("j_password");
			otPrik = (String) request.getParameter("ot_prik");

			boolean isUrlDataOk = (null != ente && !ente.isEmpty()) && (null != jUsername && !jUsername.isEmpty());

			if (isUrlDataOk) {
				getLocalCeTUser(ente, jUsername);

				con = getConnectionAMprofiler(cetUser);
				con.setAutoCommit(false);

				isbOk = isValidprik(request, con);

				if (isbOk)
					updateAmTracciaAccessi(request, ente, jUsername, otPrik, con, cetUser);
				else
					System.out.println("record Traccia Accessi non trovato! chiave e/o username non validi! !!!");

			}

		} catch (Exception e) {
			System.out.println("Errore validazione Chiave privata");
			isbOk = false;
			e.printStackTrace();
		} finally {

			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

		}

		try {
			Principal userPrincipal = request.getUserPrincipal();
			if (userPrincipal != null) {
				request.logout();
				HttpSession ses = request.getSession(true);
				ses.setAttribute("user", null);
			}
			
			if (isbOk)
				loginTs(request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect(response.encodeRedirectURL(""));
	}

	private boolean loginTs(HttpServletRequest request, HttpServletResponse response) throws Exception {

		boolean bOk = false;

		String decodedPwd = new DesEncrypter(ENCODER_ID_KEY).decrypt(jPassword);
		boolean isAmUser = getCheckIsAm_User(decodedPwd);

		if (isAmUser) {
			boolean isSoggettoOk = getCheckIsISoggetto(request);
		
			if (isSoggettoOk) {
				request.login(jUsername, decodedPwd);
				setUserIntoSession(request);
			} else {
				System.out.println(" ISoggetto Non trovato");
			}

		} else
			System.out.println("AmUser non trovato");

		return bOk;
	}

	private boolean getCheckIsAm_User(String decodedPwd) {
		boolean isValid = false;
		
		String sql = "";
		Connection con = null;

		ResultSet rs = null;
		PreparedStatement st = null;

		try {

			// Genero md5 della pasword in arrivo
			String bytesCurMD5Pwd = CryptoroUtils.getMD5Pwd(decodedPwd);

			con = getConnectionAMprofiler(cetUser);
			con.setAutoCommit(false);
			sql = "SELECT * FROM AM_USER " + "WHERE " + "NAME = ? ";
			st = con.prepareStatement(sql);
			int paramIndex = 0;
			st.setString(++paramIndex, jUsername);
			rs = st.executeQuery();

			while (rs.next()) {
				String amMD5psw = rs.getString("PWD");
				if ((amMD5psw).equals(bytesCurMD5Pwd))
					isValid = true;
			}

		} catch (Exception e) {
			System.out.println("errore AmUser non trovato");
			e.printStackTrace();
		} finally {
			try {
				st.cancel();
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
		return isValid;
	}

	private boolean getCheckIsISoggetto(HttpServletRequest request) {

		String sql = "";
		Connection con = null;
		int paramIndex = 0;
		ResultSet rs = null;
		PreparedStatement st = null;
		boolean isSoggettoOk = false;

		try {

			System.out.println("Crea ISoggetto ");
			con = getConnectionVirgilioDS(cetUser);
			con.setAutoCommit(false);

			sql = "SELECT * FROM IS_SOGGETTO " + "WHERE " + "CODFISC = ? ";
			st = con.prepareStatement(sql);
			paramIndex = 0;
			st.setString(++paramIndex, jUsername);
			rs = st.executeQuery();

			while (rs.next()) {
				isSoggettoOk = true;
			}

		} catch (Exception e) {
			System.out.println("errore  loginPrincipalUser");
			e.printStackTrace();
		} finally {
			try {

				st.cancel();
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return isSoggettoOk;
	}

	private void setUserIntoSession(HttpServletRequest request) {
		System.out.println("set ISoggetto in session");
		HashMap<String, String> permList = new HashMap<String, String>();
		/*
		 * Preparo oggetti per la sessione
		 */
		HttpSession ses = request.getSession(true);
		// user.setGroupList(listaGruppi);

		// permList.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:RETTE",
		// "1");
		// permList.put("permission@-@fasfab@-@FascFabb@-@Immissione Richieste Fascicolo",
		// "1");

		cetUser.setPermList(permList);

		/*
		 * GRUPPI: select g.* from Am_Group g, Am_User_Group ug where
		 * UPPER(ug.fk_Am_User) = 'monzaM' and g.fk_Am_Comune = 'F704' and
		 * ug.fk_Am_Group = g.name order by g.name
		 */
		// List<AmGroup> listaGruppi =
		// LoginBeanService.getGruppi(utente, ente);
		cetUser.setSessionId(ses.getId());
		ses.setAttribute("user", cetUser);

	}

	private boolean isValidprik(HttpServletRequest request, Connection con) throws Exception, SQLException {

		boolean validato = false;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM AM_TRACCIA_ACCESSI " + "WHERE " + "USATA = ? AND " + "PRIK = ? AND " + "ENTE = ? AND " + "USER_NAME = ? ";
			st = con.prepareStatement(sql);

			int paramIndex = 0;
			st.setBoolean(++paramIndex, false);
			st.setString(++paramIndex, otPrik);
			st.setString(++paramIndex, ente);
			st.setString(++paramIndex, jUsername);

			rs = st.executeQuery();

			while (rs.next())
				validato = true;

			st.cancel();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return validato;
	}

	private void getLocalCeTUser(String ente, String jUsername) {
		cetUser = new CeTUser();
		List<String> enteList = new LinkedList<String>();
		enteList.add(ente);
		cetUser.setEnteList(enteList);
		cetUser.setUsername(jUsername);
		cetUser.setCurrentEnte(ente);

	}

	/**
	 * 
	 * Segno come usata la chiave primaria in modo da non permetterne piu
	 * l'utilizzo
	 */
	private PreparedStatement updateAmTracciaAccessi(HttpServletRequest request, String ente, String jUsername, String otPrik, Connection con, CeTUser user) throws SQLException {

		// HashMap<String, String> permList = new HashMap<String, String>();
		PreparedStatement st = null;
		String sql;
		int paramIndex = 0;
		try {
			sql = "UPDATE AM_TRACCIA_ACCESSI " + "SET USATA = ? " + "WHERE " + "PRIK = ? AND " + "ENTE = ? AND " + "USER_NAME = ? ";
			st = con.prepareStatement(sql);

			st.setBoolean(++paramIndex, true);
			st.setString(++paramIndex, otPrik);
			st.setString(++paramIndex, ente);
			st.setString(++paramIndex, jUsername);
			st.executeUpdate();

			st.cancel();

			con.commit();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			st.cancel();
			if (st != null)
				st.close();
			if (con != null)
				con.close();
		}

		return st;
	}

	protected Connection getConnectionVirgilioDS(CeTUser utente) throws Exception {
		Context initContext = new InitialContext();
		TsEnvUtente eu = new TsEnvUtente(utente, null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		DataSource ds = (DataSource) initContext.lookup(es.getDataSourceVirgilioDS());
		Connection conn = ds.getConnection();
		return conn;

	}

	protected Connection getConnectionAMprofiler(CeTUser utente) throws Exception {
		Context initContext = new InitialContext();
		TsEnvUtente eu = new TsEnvUtente(utente, null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		DataSource ds = (DataSource) initContext.lookup(es.getDataSourceAmProfiler());
		Connection conn = ds.getConnection();
		return conn;

	}
}
