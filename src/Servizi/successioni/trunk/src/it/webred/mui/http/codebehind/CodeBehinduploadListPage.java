package it.webred.mui.http.codebehind;

import it.webred.mui.MuiException;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.http.cache.CacheRefresher;
import it.webred.mui.model.MiDupFornitura;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CodeBehinduploadListPage extends AbstractPage implements
		CacheRefresher {
	@Override
	public void init() {
		MuiApplication.getMuiApplication().add(UPLOAD_LIST_VARNAME, this);
	}

	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		if (req.getParameter("deleteFornitura") != null
				&& req.getParameter("deleteFornitura").trim().length() > 0) {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();
			try {
				MiDupFornitura fornitura = (MiDupFornitura) session.load(
						MiDupFornitura.class, Long.valueOf(req
								.getParameter("deleteFornitura")));
				fornitura.deleteFully(session);
				tx.commit();
				MuiApplication.getMuiApplication().forceEntryUpdate(
						UPLOAD_LIST_VARNAME);
			} catch (Throwable e) {
				Logger.log().error(this.getClass().getName(),
						"error while updating fornitura list", e);
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					e1.printStackTrace();
				}
				throw new MuiException("error while updating fornitura list", e);
			} finally {
				HibernateUtil.closeSession();
			}
		} else if (req.getParameter("doRefresh") != null
				&& new Boolean(req.getParameter("doRefresh")).booleanValue()) {
			try {
				MuiApplication.getMuiApplication().forceEntryUpdate(UPLOAD_LIST_VARNAME);
			} catch (Throwable e) {
				Logger.log().error(this.getClass().getName(),
						"error while updating fornitura list", e);
				throw new MuiException("error while updating fornitura list", e);
			}
		}
		return true;
	}

	public Object doRefresh() {
		Session session;
		Transaction tx;
		session = HibernateUtil.currentSession();
		tx = session.beginTransaction();
		try {
			List<MiDupFornitura> miDupFornituras = new ArrayList<MiDupFornitura>();
			Query query = session
					.createQuery("select c from it.webred.mui.model.MiDupFornitura as c order by dataInizialeDate desc");
			Logger.log().info(this.getClass().getName(),
					"executing query " + query);
			for (Iterator it = query.iterate(); it.hasNext();) {
				try {
					MiDupFornitura miDupFornitura = (MiDupFornitura) it.next();
					miDupFornituras.add(miDupFornitura);
				} catch (org.hibernate.ObjectNotFoundException e) {
					Logger.log().info(this.getClass().getName(),
							"ignoring phantom record reference", e);
				}
			}
			tx.commit();
			evalNumeroAnno(miDupFornituras);
			return miDupFornituras;
		} catch (Throwable e) {
			Logger.log().error(this.getClass().getName(),
					"error while updating fornitura list", e);
			try {
				tx.rollback();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			}
			throw new MuiException("error while updating fornitura list", e);
		} finally {
			HibernateUtil.closeSession();
			MuiApplication.getMuiApplication().forceEntryUpdate(DAP_LIST_VARNAME);
		}
	}

	private void evalNumeroAnno(List<MiDupFornitura> miDupFornituras) {
		Iterator<MiDupFornitura> iterFornitura = miDupFornituras.iterator();
		List<MiDupFornitura> v = new ArrayList<MiDupFornitura>();
		while (iterFornitura.hasNext()) {
			MiDupFornitura element = iterFornitura.next();
			v.add(0, element);
		}

		int anno = -1;
		int numeroAnno = 1;
		iterFornitura = v.iterator();
		while (iterFornitura.hasNext()) {
			MiDupFornitura element = iterFornitura.next();
			if (anno != element.getAnno()) {
				anno = element.getAnno();
				numeroAnno = 1;
			}
			element.setNumeroAnno(numeroAnno);
			numeroAnno++;
		}
	}

}
