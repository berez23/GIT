package it.webred.mui.http;

import it.webred.mui.consolidation.DapManager;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.cache.CacheManager;
import it.webred.mui.http.cache.CacheRefresher;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.skillbill.logging.Logger;

import org.hibernate.Hibernate;
import org.hibernate.Session;

public class MuiApplication extends HttpServlet implements MuiHttpConstants {
	public MuiApplication() {
	}

	private static MuiApplication _theInstance = null;

	private  CacheManager _cache = null;

	private Map<String, DataSource> _dataSources = null;

	private Context _ctx = null;

	private Long _timeShift = null;

	private long _lastRenewedDateMilli = 0;

	private long _lastdbDateMilli = 0;

	private java.util.Date _lastRenewedDate = null;

	private ServletContext _context = null;
	
	public static String belfiore = null;
	
	public static String descComune = null;
	
	public static String nomeApp = null;

	public void service(HttpServletRequest req, HttpServletResponse resp)
			throws javax.servlet.ServletException, java.io.IOException {
		/** @todo Implement this uma.http.UmaBaseServlet abstract method */
		throw new AbstractMethodError();
	}

	public void init() throws ServletException {
		_dataSources = new HashMap<String, DataSource>();
		belfiore = getServletConfig().getInitParameter("belfiore");
		descComune = getServletConfig().getInitParameter("desc_comune");
		nomeApp = getServletConfig().getInitParameter("nome_app");
		try {
			Context initCtx = new InitialContext();
			_ctx = (Context) initCtx.lookup("java:comp/env");
			Logger.log().info(this.getClass().getName(),
					"MuiApplication IS STARTING, DBTIME IS " + getDbTime());
		} catch (Throwable ex) {
			Logger.log().error("MuiApplication NOT STARTED ", ex);
			ex.printStackTrace();
			throw new ServletException(ex);
		}
		_context = getServletContext();
		_theInstance = this;
		Logger.log().info(this.getClass().getName(), "MuiApplication STARTED ");
		_cache = CacheManager.getCacheManager("CACHE_"+nomeApp+"_");
		_cache.start();
		Controller.prefetch();
		//add(BELFIORE_VARNAME,new CodiceBelfioreMap());
	}

	public void destroy() {
		try {
			Logger.log()
					.info(
							this.getClass().getName(),
							"MuiApplication IS SHUTTING DOWN, DBTIME IS "
									+ getDbTime());
		} catch (Throwable ex) {
			Logger.log().error(this.getClass().getName(),
					"MuiApplication IS SHUTTING DOWN, DBTIME IS UKNOWN! ", ex);
		}
		_cache.stop();
		DapManager.stop();
		_cache = null;
		_context = null;
		_dataSources.clear();
		_theInstance = null;
		Logger.log().info(this.getClass().getName(),
				"MuiApplication SHUT DOWN COMPLETE ");
	}

	public static MuiApplication getMuiApplication() {
		return _theInstance;
	}

	public java.util.Date getDbTime() throws SQLException, NamingException {
		return getDbTime(false);
	}

	public java.util.Date getDbTime(boolean refresh) throws SQLException,
			NamingException {
		java.util.Date res = null;
		if (_timeShift == null || refresh) {
			synchronized (this) {
				if (_timeShift == null || refresh) {
					Session session = HibernateUtil.currentSession();
					try {
						// Query query = session
						// .createQuery("select sysdate(),c.login from MiDupUser
						// as c");
						// query.setMaxResults(1);
						// Iterator results = query.list().iterator();
						//
						// while (results.hasNext()) {
						// Object[] row = (Object[]) results.next();
						// res = (java.sql.Date) row[0];
						// }
						res = (Date)session.createSQLQuery(
								"select sysdate dbtime from dual")
								.addScalar("dbtime", Hibernate.TIMESTAMP).uniqueResult();
						_lastdbDateMilli = res.getTime();
						_timeShift = new Long(System.currentTimeMillis()
								- _lastdbDateMilli);
					} catch (Throwable ex) {
						Logger.log().error(this.getClass().getName(),
								"MuiApplicati DBTIME IS UKNOWN! ", ex);
						_timeShift = null;
					} finally {
					}

					// Connection conn = null;
					// ResultSet rset = null;
					// try {
					// conn = getConnection();
					// rset = conn
					// .prepareStatement("SELECT SYSDATE FROM DUAL")
					// .executeQuery();
					// while (rset.next()) {
					// res = rset.getTimestamp(1);
					// }
					// _lastdbDateMilli = res.getTime();
					// _timeShift = new Long(System.currentTimeMillis()
					// - _lastdbDateMilli);
					// } finally {
					// try {
					// if (rset != null) {
					// rset.close();
					// }
					// } catch (Exception ex) {
					// }
					// if (conn != null) {
					// conn.close();
					// }
					// }
				} else {
					long now = System.currentTimeMillis();
					if ((now - _lastRenewedDateMilli) > MUI_DATE_DURATION) {
						res = new java.util.Date(now - _timeShift.longValue());
						_lastRenewedDateMilli = now;
						_lastRenewedDate = res;
					} else {
						res = _lastRenewedDate;
					}
				}
			}
		} else {
			long now = System.currentTimeMillis();
			if (now - _lastdbDateMilli > MUI_DBDATE_DURATION) {
				return getDbTime(true);
			}
			if ((now - _lastRenewedDateMilli) > MUI_DATE_DURATION) {
				res = new java.util.Date(now - _timeShift.longValue());
				synchronized (this) {
					_lastRenewedDateMilli = now;
					_lastRenewedDate = res;
				}
			} else {
				res = _lastRenewedDate;
			}
		}
		return res;
	}

	public Connection getConnection() throws SQLException, NamingException {
		return getConnection("mui");
	}

	protected Connection getConnection(String aliasConn) throws SQLException {
		aliasConn = aliasConn != null ? aliasConn.toLowerCase() : null;
		DataSource ds = _dataSources.get(aliasConn);
		Connection conn = null;
		if (ds == null) {
			synchronized (_dataSources) {
				ds = _dataSources.get(aliasConn);
				if (ds == null) {
					try {
						ds = (DataSource) _ctx.lookup("jdbc/" + aliasConn);
						_dataSources.put(aliasConn, ds);
					} catch (javax.naming.NamingException ne) {
						ne.printStackTrace();
						throw new SQLException("exception for " + "jdbc/"
								+ aliasConn + " :" + ne.getExplanation());
					}
				}
			}
		}
		conn = ds.getConnection();
		if (conn == null)
			throw new SQLException(" NO Connection for Datasource:" + "jdbc/"
					+ aliasConn);
		return conn;
	}

	protected ServletContext getContext() {
		return _context;
	}
	public Object get(String name) {
		return _cache.get(name);
	}

	public void forceEntryUpdate(String name) {
		_cache.forceCacheUpdate(name);
	}

	public void add(String name, CacheRefresher refresher) {
		_cache.add(name,refresher);
	}

}
//class FakeServetContext implements ServletContext{
//	private Map _container = new HashMap();
//	public String getResourcePath(String arg){
//		return null;
//	}
//	public Servlet getServlet(String arg){
//		return null;
//	}
//	public int getMinorVersion(){
//		return -1;
//	}
//	public int getMajorVersion(){
//		return -1;
//	}
//	public java.util.Enumeration 	getAttributeNames() {
//		return 
//	}
//}
